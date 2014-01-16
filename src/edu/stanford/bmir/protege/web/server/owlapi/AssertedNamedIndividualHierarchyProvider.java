package edu.stanford.bmir.protege.web.server.owlapi;

import org.protege.editor.owl.model.hierarchy.AbstractOWLObjectHierarchyProvider;
import org.protege.owlapi.inference.orphan.Relation;
import org.protege.owlapi.inference.orphan.TerminalElementFinder;
import org.semanticweb.owlapi.model.*;

import edu.stanford.bmir.protege.web.server.frame.AxiomPropertyValueTranslator;
import edu.stanford.bmir.protege.web.server.frame.PropertyValueComparator;
import edu.stanford.bmir.protege.web.server.owlapi.extractor.ChildNamedIndividualExtractor;
import edu.stanford.bmir.protege.web.server.owlapi.extractor.ParentNamedIndividualExtractor;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.frame.NamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;

import java.util.*;


/**
 * Author: Roland Reumerman<br>
 * Hinttech B.V.<br>
 * Dayon B.V.<br>
 * Date: 15-Jan-2014<br><br>
 */
public class AssertedNamedIndividualHierarchyProvider extends AbstractOWLObjectHierarchyProvider<OWLNamedIndividual> {

    private OWLOntologyManager owlOntologyManager;

    /*
     * It is not safe to set the collection of ontologies to a HashSet or TreeSet.
     * When an ontology changes name it gets a new Hash Code and it is sorted
     * differently, so these Collections do not work.
     */
    private Collection<OWLOntology> ontologies;

    private OWLNamedIndividual root;

    private OWLOntologyChangeListener listener;

    private TerminalElementFinder<OWLNamedIndividual> rootFinder;

    private Set<OWLNamedIndividual> nodesToUpdate = new HashSet<OWLNamedIndividual>();


    public AssertedNamedIndividualHierarchyProvider(OWLOntologyManager owlOntologyManager) {
        super(owlOntologyManager);
        this.owlOntologyManager = owlOntologyManager;
        /*
         * It is not safe to set the collection of ontologies to a HashSet or TreeSet.
         * When an ontology changes name it gets a new Hash Code and it is sorted
         * differently, so these Collections do not work.
         */
        ontologies = new ArrayList<OWLOntology>();
        rootFinder = new TerminalElementFinder<OWLNamedIndividual>(new Relation<OWLNamedIndividual>() {
            public Collection<OWLNamedIndividual> getR(OWLNamedIndividual niv) {
                Collection<OWLNamedIndividual> parents = getParents(niv);
                parents.remove(root);
                return parents;
            }
        });

        listener = new OWLOntologyChangeListener() {
            public void ontologiesChanged(List<? extends OWLOntologyChange> changes) {
                handleChanges(changes);
            }
        };
        getManager().addOntologyChangeListener(listener);
    }


    /**
     * Sets the ontologies that this hierarchy provider should use
     * in order to determine the hierarchy.
     */
    public void setOntologies(Set<OWLOntology> ontologies) {
        /*
         * It is not safe to set the collection of ontologies to a HashSet or TreeSet.
         * When an ontology changes name it gets a new Hash Code and it is sorted
         * differently, so these Collections do not work.
         */
        this.ontologies = new ArrayList<OWLOntology>(ontologies);
        nodesToUpdate.clear();
        if (root == null) {
            root = owlOntologyManager.getOWLDataFactory().getOWLThing().asOWLNamedIndividual();
        }
        rebuildImplicitRoots();
        fireHierarchyChanged();
    }

    private void rebuildImplicitRoots() {
        rootFinder.clear();
        for (OWLOntology ont : ontologies) {
            Set<OWLNamedIndividual> ref = ont.getIndividualsInSignature();//getClassesInSignature();
            rootFinder.appendTerminalElements(ref);
        }
        rootFinder.finish();
    }

    public void dispose() {
        getManager().removeOntologyChangeListener(listener);
    }


    private void handleChanges(List<? extends OWLOntologyChange> changes) {
        Set<OWLNamedIndividual> oldTerminalElements = new HashSet<OWLNamedIndividual>(rootFinder.getTerminalElements());
        Set<OWLNamedIndividual> changedClasses = new HashSet<OWLNamedIndividual>();
        changedClasses.add(root);
        List<OWLAxiomChange> filteredChanges = filterIrrelevantChanges(changes);
        updateImplicitRoots(filteredChanges);
        for (OWLOntologyChange change : filteredChanges) {
            for (OWLEntity entity : ((OWLAxiomChange) change).getEntities()) {
                if (entity instanceof OWLNamedIndividual && !entity.equals(root)) {
                    changedClasses.add((OWLNamedIndividual) entity);
                }
            }
        }
        for (OWLNamedIndividual cls : changedClasses) {
            registerNodeChanged(cls);
        }
        for (OWLNamedIndividual cls : rootFinder.getTerminalElements()) {
            if (!oldTerminalElements.contains(cls)) {
                registerNodeChanged(cls);
            }
        }
        for (OWLNamedIndividual cls : oldTerminalElements) {
            if (!rootFinder.getTerminalElements().contains(cls)) {
                registerNodeChanged(cls);
            }
        }
        notifyNodeChanges();
    }

    private List<OWLAxiomChange> filterIrrelevantChanges(List<? extends OWLOntologyChange> changes) {
        List<OWLAxiomChange> filteredChanges = new ArrayList<OWLAxiomChange>();
        for (OWLOntologyChange change : changes) {
            // only listen for changes on the appropriate ontologies
            if (ontologies.contains(change.getOntology())){
                if (change.isAxiomChange()) {
                    filteredChanges.add((OWLAxiomChange) change);
                }
            }
        }
        return filteredChanges;
    }


    private void registerNodeChanged(OWLNamedIndividual node) {
        nodesToUpdate.add(node);
    }


    private void notifyNodeChanges() {
        for (OWLNamedIndividual node : nodesToUpdate){
            fireNodeChanged(node);
        }
        nodesToUpdate.clear();
    }


    private void updateImplicitRoots(List<OWLAxiomChange> changes) {
        Set<OWLNamedIndividual> possibleTerminalElements = new HashSet<OWLNamedIndividual>();
        Set<OWLNamedIndividual> notInOntologies = new HashSet<OWLNamedIndividual>();

        for (OWLOntologyChange change : changes) {
            // only listen for changes on the appropriate ontologies
            if (ontologies.contains(change.getOntology())){
                if (change.isAxiomChange()) {
                    boolean remove = change instanceof RemoveAxiom;
                    OWLAxiom axiom = change.getAxiom();

                    for (OWLEntity entity : axiom.getSignature()) {
                        if (!(entity instanceof OWLNamedIndividual) || entity.equals(root)) {
                            continue;
                        }
                        OWLNamedIndividual cls = (OWLNamedIndividual) entity;
                        if (remove && !containsReference(cls)) {
                            notInOntologies.add(cls);
                            continue;
                        }
                        possibleTerminalElements.add(cls);
                    }
                }
            }
        }

        possibleTerminalElements.addAll(rootFinder.getTerminalElements());
        possibleTerminalElements.removeAll(notInOntologies);
        rootFinder.findTerminalElements(possibleTerminalElements);
    }

    public Set<OWLNamedIndividual> getRoots() {
        if (root == null) {
            root = owlOntologyManager.getOWLDataFactory().getOWLThing().asOWLNamedIndividual();
        }
        return Collections.singleton(root);
    }

    public Set<OWLNamedIndividual> getChildren(OWLNamedIndividual object) {
        return getChildren(object, null, null);
    }
    
    public Set<OWLNamedIndividual> getChildren(OWLNamedIndividual object, OWLOntology rootOntology, OWLAPIProject project) {
        Set<OWLNamedIndividual> result;
        if (object.equals(root)) {
            result = new HashSet<OWLNamedIndividual>();
            result.addAll(rootFinder.getTerminalElements());
            result.addAll(extractChildren(object, rootOntology, project));
            result.remove(object);
        }
        else {
            result = extractChildren(object, rootOntology, project);
            for (Iterator<OWLNamedIndividual> it = result.iterator(); it.hasNext();) {
                OWLNamedIndividual curChild = it.next();
                if (getAncestors(object).contains(curChild)) {
                    it.remove();
                }
            }
        }

        return result;
    }


    //TODO: temporary workaround to determine children of node
    private Set<OWLNamedIndividual> extractChildren(OWLNamedIndividual parent, OWLOntology rootOntology, OWLAPIProject project) {
      Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();
      String parentIRI = "<" + parent.getIRI().toString() + ">";
      for (OWLNamedIndividual niv : rootOntology.getIndividualsInSignature()) {
          List<PropertyValue> childPropertyValues = getNamedIndividualPropertyValues(niv, rootOntology, project);
          for (PropertyValue propertyValue : childPropertyValues) {
              if (propertyValue.getProperty().toStringID().equals("http://purl.edustandaard.nl/begrippenkader/isBkDeelinhoudVan")) {
                  if (parentIRI.equals(propertyValue.getValue().toString())) {
                      result.add(niv);
                      break;
                  }
              }
          }
//          switch ("") {
//              case "":
//                  if (true){//nivEntityTypeName.equals("")) {
//                      result.add(niv);
//                  }
//                  break;
//          }
      }
      return result;
//        ChildNamedIndividualExtractor childNamedIndividualExtractor = new ChildNamedIndividualExtractor();
//        childNamedIndividualExtractor.setCurrentParentClass(parent);
//        for (OWLOntology ont : ontologies) {
//            for (OWLAxiom ax : ont.getReferencingAxioms(parent)) {
//                if (ax.isLogicalAxiom()) {
//                    ax.accept(childNamedIndividualExtractor);
//                }
//            }
//        }
//        return childNamedIndividualExtractor.getResult();
    }

    private List<PropertyValue> getNamedIndividualPropertyValues(OWLNamedIndividual subject, OWLOntology rootOntology, OWLAPIProject project) {
        Set<OWLAxiom> translateFrom = new HashSet<OWLAxiom>();
        for (OWLOntology ontology : rootOntology.getImportsClosure()) {
            translateFrom.addAll(ontology.getClassAssertionAxioms(subject));
            translateFrom.addAll(ontology.getAnnotationAssertionAxioms(subject.getIRI()));
            translateFrom.addAll(ontology.getObjectPropertyAssertionAxioms(subject));
            translateFrom.addAll(ontology.getDataPropertyAssertionAxioms(subject));
        }
        List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
        for(OWLAxiom axiom : translateFrom) {
            AxiomPropertyValueTranslator translator = new AxiomPropertyValueTranslator();
            propertyValues.addAll(translator.getPropertyValues(subject, axiom, rootOntology));
        }
        Collections.sort(propertyValues, new PropertyValueComparator(project));

        return propertyValues;
    }

    public boolean containsReference(OWLNamedIndividual object) {
        for (OWLOntology ont : ontologies) {
            if (ont.containsIndividualInSignature(object.getIRI())) {
                return true;
            }
        }
        return false;
    }


    public Set<OWLNamedIndividual> getParents(OWLNamedIndividual object) {
        ParentNamedIndividualExtractor parentNamedIndividualExtractor = new ParentNamedIndividualExtractor();
        // If the object is thing then there are no
        // parents
        if (object.equals(root)) {
            return Collections.emptySet();
        }
        Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();
        // Thing if the object is a root class
        if (rootFinder.getTerminalElements().contains(object)) {
            result.add(root);
        }
        // Not a root, so must have another parent
        parentNamedIndividualExtractor.reset();
        parentNamedIndividualExtractor.setCurrentClass(object);
        for (OWLOntology ont : ontologies) {
            for (OWLAxiom ax : ont.getAxioms(object)) {
                ax.accept(parentNamedIndividualExtractor);
            }
        }
        result.addAll(parentNamedIndividualExtractor.getResult());
        return result;
    }


    //TODO: not used?
    public Set<OWLNamedIndividual> getEquivalents(OWLNamedIndividual object) {
//        Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();
//        for (OWLOntology ont : ontologies) {
//            for (OWLClassExpression equiv : object.getEquivalentClasses(ont)) {
//                if (!equiv.isAnonymous()) {
//                    result.add((OWLNamedIndividual) equiv);
//                }
//            }
//        }
//        Set<OWLNamedIndividual> ancestors = getAncestors(object);
//        if (ancestors.contains(object)) {
//            for (OWLNamedIndividual cls : ancestors) {
//                if (getAncestors(cls).contains(object)) {
//                    result.add(cls);
//                }
//            }
//            result.remove(object);
//            result.remove(root);
//        }
//        return result;
        return null;
    }

}