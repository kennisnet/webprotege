package edu.stanford.bmir.protege.web.server.owlapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.protege.editor.owl.model.hierarchy.AbstractOWLObjectHierarchyProvider;
import org.protege.owlapi.inference.orphan.Relation;
import org.protege.owlapi.inference.orphan.TerminalElementFinder;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomChange;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.RemoveAxiom;

import edu.stanford.bmir.protege.web.server.UIConfigurationManager;
import edu.stanford.bmir.protege.web.server.frame.AxiomPropertyValueTranslator;
import edu.stanford.bmir.protege.web.server.frame.PropertyValueComparator;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.PropertyIndividualValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;


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

    public final static String TYPE_INHOUD = "Inhoud";
    
    public final static String TYPE_DOELEN = "Doelen";
    
    private OWLNamedIndividual leergebiedProxy;
    
    private OWLNamedIndividual vakkernProxy;
    private OWLNamedIndividual subkernProxy;
    private OWLNamedIndividual inhoudProxy;
    
    private OWLNamedIndividual kerndoelProxy;
    private OWLNamedIndividual tussendoelProxy;
    
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

    public void updateNamedIndividual(OWLAPIProject project, OWLNamedIndividual subject, OWLNamedIndividual object, OWLObjectProperty predicate) {
        OWLOntology o = project.getRootOntology();
        OWLDataFactory df = owlOntologyManager.getOWLDataFactory();
        // We want to state the relation between the 2 individuals
        // subject -> predicate -> object
        OWLAxiom assertion = df.getOWLObjectPropertyAssertionAxiom(predicate, subject, object);
        // Finally, add the axiom to our ontology
        AddAxiom addAxiomChange = new AddAxiom(o, assertion);
        owlOntologyManager.applyChange(addAxiomChange);
//        try {
//            owlOntologyManager.saveOntology(o);
//        } catch (OWLOntologyStorageException exc) {
//            //skip
//        }
    }

    //Make Protege parent class happy
    public Set<OWLNamedIndividual> getRoots() {
        Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();
        return result;
    }
    
    public Set<OWLNamedIndividual> getRoots(ProjectId projectId) {
//      if (root == null) {
//      	//UIConfigurationManager.getConfigurationFile(projectId);
//      	root = owlOntologyManager.getOWLDataFactory().getOWLThing().asOWLNamedIndividual();
//  	}
//  	return Collections.singleton(root);
    	
        Set<OWLNamedIndividual> empty = new HashSet<OWLNamedIndividual>();
//        return empty;
        
		try {
	        OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.getProjectDocumentStore(projectId);
	        OWLAPIProject project = OWLAPIProject.getProject(documentStore);
	        return getRoots(project.getRootOntology(), project);
		} catch (OWLParserException e) {
			e.printStackTrace();
			return empty;
		} catch (IOException e) {
			e.printStackTrace();
			return empty;
		}
    }

    private Set<OWLNamedIndividual> getRoots(OWLOntology rootOntology, OWLAPIProject project) {
    	OWLNamedIndividual leergebiedProxy = findProxy(rootOntology, project, "http://www.openarchives.org/ore/terms/proxyFor", /* IRI van Leergebied Begrip */ "http://purl.edustandaard.nl/begrippenkader/5c0413bd-cbaf-407d-93ff-4f7409559ff6");
        Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();
        for (OWLNamedIndividual individual : rootOntology.getIndividualsInSignature()) {
            PropertyValue typeProperty = getProperty(getNamedIndividualPropertyValues(individual, rootOntology, project), "http://purl.edustandaard.nl/begrippenkader/heeftBkInhoudType");
            if (typeProperty != null && typeProperty.getValue().equals(leergebiedProxy)) {
                result.add(individual);
            }
        }
        return result;
//        if (root == null) {
//            //UIConfigurationManager.getConfigurationFile(projectId);
//            root = owlOntologyManager.getOWLDataFactory().getOWLThing().asOWLNamedIndividual();
//        }
//        return Collections.singleton(root);
    }

    public Set<OWLNamedIndividual> getChildren(OWLNamedIndividual object) {
        return getChildren(object, null, null, null);
    }
    
    public Set<OWLNamedIndividual> getChildren(OWLNamedIndividual object, OWLOntology rootOntology, OWLAPIProject project, String type) {
        Set<OWLNamedIndividual> result;
        if (object.equals(root)) {
            result = new HashSet<OWLNamedIndividual>();
            result.addAll(rootFinder.getTerminalElements());
            if (type.equals(TYPE_DOELEN)) {
                result.addAll(extractDoelenChildren(object, rootOntology, project));
            } else {
                result.addAll(extractChildren(object, rootOntology, project));
            }
            result.remove(object);
        }
        else {
            if (type.equals(TYPE_DOELEN)) {
                result = extractDoelenChildren(object, rootOntology, project);
            } else {
                result = extractChildren(object, rootOntology, project);
            }
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
      List<PropertyValue> parentPropertyValues = getNamedIndividualPropertyValues(parent, rootOntology, project);
      boolean parentIsSubkern = false;
      //TODO: use .contains(Object) ?
      for (PropertyValue propertyValue : parentPropertyValues) {
          if (propertyValue.getProperty().toStringID().equals("http://purl.edustandaard.nl/begrippenkader/heeftBkInhoudType")) {
              if (propertyValue.getValue().toString().equals("<http://purl.edustandaard.nl/begrippenkader/8f47dd8f-c414-4778-bbbe-383b37bb47e3>")) {
                  parentIsSubkern = true;
                  break;
              }
          }
      }
      String parentIRI = parent.getIRI().toQuotedString();
      Set<OWLNamedIndividual> individivuals = rootOntology.getIndividualsInSignature();
      for (OWLNamedIndividual niv : individivuals) {
          List<PropertyValue> childPropertyValues = getNamedIndividualPropertyValues(niv, rootOntology, project);
          for (PropertyValue propertyValue : childPropertyValues) {
              if (propertyValue.getProperty().toStringID().equals("http://purl.edustandaard.nl/begrippenkader/isBkDeelinhoudVan")) {
                  if (parentIRI.equals(propertyValue.getValue().toString())) {
                      result.add(niv);
                      break;
                  }
              }
          }
          if (parentIsSubkern) {
              for (PropertyValue propertyValue : childPropertyValues) {
                  if (propertyValue.getProperty().toStringID().equals("http://purl.edustandaard.nl/begrippenkader/isBkDoelVan")) {
                      if (parentIRI.equals(propertyValue.getValue().toString())) {
                          result.add(niv);
                          break;
                      }
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

    private Set<OWLNamedIndividual> extractDoelenChildren(OWLNamedIndividual parent, OWLOntology rootOntology, OWLAPIProject project) {
    	OWLNamedIndividual vakkernProxy = findProxy(rootOntology, project, "http://www.openarchives.org/ore/terms/proxyFor", /* IRI van Vakkern Begrip */ "http://purl.edustandaard.nl/begrippenkader/83e9bb89-b5f4-4ec8-b228-a6b920a38f55");
    	OWLNamedIndividual kerndoelProxy = findProxy(rootOntology, project, "http://www.openarchives.org/ore/terms/proxyFor", /* IRI van Kerndoel Begrip */ "http://purl.edustandaard.nl/begrippenkader/489f4d28-ed61-4589-a16a-585936dfd632");
    	
        //parent is Kerndoel?
        List<PropertyValue> parentPropertyValues = getNamedIndividualPropertyValues(parent, rootOntology, project);
        PropertyValue propertyValue = getProperty(parentPropertyValues, "http://purl.edustandaard.nl/begrippenkader/heeftBkDoelType");
        boolean parentIsKerndoel = propertyValue != null && ((PropertyIndividualValue) propertyValue).getValue().equals(kerndoelProxy);
        
        Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();
        String parentIRI = parent.getIRI().toQuotedString();
        
        if (parentIsKerndoel) {
            //naar welke vakkernen wijst dit kerndoel
        	parentPropertyValues = getProperties(parentPropertyValues, "http://purl.edustandaard.nl/begrippenkader/isBkDoelVan");
            for (PropertyValue p : parentPropertyValues) {
            	OWLNamedIndividual individual = (OWLNamedIndividual) p.getValue();

                //value is Vakkern?
	        	List<PropertyValue> childPropertyValues = getNamedIndividualPropertyValues(individual, rootOntology, project);
                propertyValue = getProperty(childPropertyValues, "http://purl.edustandaard.nl/begrippenkader/heeftBkInhoudType");
                boolean isVakkern = propertyValue != null && ((PropertyIndividualValue) propertyValue).getValue().equals(vakkernProxy);

                if (isVakkern) {
                    result.add(individual);
                }
        	}
        	
    	// assume parent is leergebied or vakkern
        } else {
	        for (OWLNamedIndividual individual : rootOntology.getIndividualsInSignature()) {
	        	List<PropertyValue> childPropertyValues = getNamedIndividualPropertyValues(individual, rootOntology, project);
	            
                //child is Kerndoel?
                propertyValue = getProperty(childPropertyValues, "http://purl.edustandaard.nl/begrippenkader/heeftBkDoelType");
                boolean isKerndoel = propertyValue != null && ((PropertyIndividualValue) propertyValue).getValue().equals(kerndoelProxy);

                if (isKerndoel) {
                    //Kerndoel van geselecteerde parent vak/leergebied?
                    childPropertyValues = getProperties(childPropertyValues, "http://purl.edustandaard.nl/begrippenkader/isBkDoelVan");
                    for (PropertyValue p : childPropertyValues) {
    	            	if (parentIRI.equals(p.getValue().toString())) {
    	                    result.add(individual);
    	                    break;
    	                }
                	}
                }
            }
        }
        
        return result;
    }

    private OWLNamedIndividual findProxy(OWLOntology rootOntology, OWLAPIProject project, String typeID, String typeValue) {
        for (OWLNamedIndividual individual : rootOntology.getIndividualsInSignature()) {
            PropertyValue typeProperty = getProperty(getNamedIndividualPropertyValues(individual, rootOntology, project), typeID);
            if (typeProperty != null && typeProperty.getValue().toString().equals(typeValue)) {
                return individual;
            }
        }
        return null;
    }

    private PropertyValue getProperty(List<PropertyValue> propertyValues, String propertyID) {
        for (PropertyValue propertyValue : propertyValues) {
            if (propertyValue.getProperty().toStringID().equals(propertyID)) {
                return propertyValue;
            }
        }
        return null;
    }

    private List<PropertyValue> getProperties(List<PropertyValue> propertyValues, String propertyID) {
    	List<PropertyValue> props = new ArrayList<PropertyValue>();
        for (PropertyValue propertyValue : propertyValues) {
            if (propertyValue.getProperty().toStringID().equals(propertyID)) {
                props.add(propertyValue);
            }
        }
        return props;
    }

    public List<PropertyValue> getNamedIndividualPropertyValues(OWLNamedIndividual subject, OWLOntology rootOntology, OWLAPIProject project) {
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

    //Make parent class happy
    public Set<OWLNamedIndividual> getParents(OWLNamedIndividual child) {
        Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();
        return result;
    }
    
    public Set<OWLNamedIndividual> getParents(OWLNamedIndividual child, ProjectId projectId) {
        // If the object is root then there are no
        // parents
        if (child.equals(root)) {
            return Collections.emptySet();
        }
        OWLAPIProjectManager pm = OWLAPIProjectManager.getProjectManager();
        OWLAPIProject project =  pm.getProject(projectId);
        OWLOntology rootOntology = project.getRootOntology();
        
        Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();
        
        List<PropertyValue> childPropertyValues = getNamedIndividualPropertyValues(child, rootOntology, project);
        for (PropertyValue propertyValue : childPropertyValues) {
            if (propertyValue.getProperty().toStringID().equals("http://purl.edustandaard.nl/begrippenkader/isBkDeelinhoudVan")
            	|| propertyValue.getProperty().toStringID().equals("http://purl.edustandaard.nl/begrippenkader/isBkDoelVan")) {
                OWLNamedIndividual parent = ((PropertyIndividualValue) propertyValue).getValue();
                result.add(parent);
            }
        }
        
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
