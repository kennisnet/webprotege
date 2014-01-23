// $Id$
package edu.stanford.bmir.protege.web.server.owlapi.extractor;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

import edu.stanford.bmir.protege.web.server.owlapi.extractor.model.OWLSubClassofAxiomForNamedIndividual;

/**
 * 
 * @author reumerman
 * @version $Id$
 */
public class ChildNamedIndividualExtractor extends OWLAxiomVisitorAdapter {

    
        private NamedConjunctChecker checker = new NamedConjunctChecker();

        private NamedIndividualExtractor namedClassExtractor = new NamedIndividualExtractor();

        private OWLNamedIndividual currentParentClass;

        private Set<OWLNamedIndividual> results = new HashSet<OWLNamedIndividual>();


        public void reset() {
            results.clear();
            namedClassExtractor.reset();
        }


        public void setCurrentParentClass(OWLNamedIndividual currentParentClass) {
            this.currentParentClass = currentParentClass;
            reset();
        }


        public Set<OWLNamedIndividual> getResult() {
            return new HashSet<OWLNamedIndividual>(results);
        }


        public void visit(OWLSubClassofAxiomForNamedIndividual axiom) {
            // Example:
            // If searching for subs of B, candidates are:
            // SubClassOf(A B)
            // SubClassOf(A And(B ...))
            if (false) {//checker.containsConjunct(currentParentClass, axiom.getSuperClass())) {
                // We only want named classes
                if (!axiom.getSubClass().isAnonymous()) {
                    results.add(axiom.getSubClass());
                }
            }
        }


//        public void visit(OWLEquivalentClassesAxiom axiom) {
//            // EquivalentClasses(A  And(B...))
//            if (!namedClassInEquivalentAxiom(axiom)){
//                return;
//            }
//            Set<OWLClassExpression> candidateDescriptions = new HashSet<OWLClassExpression>();
//            boolean found = false;
//            for (OWLClassExpression equivalentClass : axiom.getClassExpressions()) {
//                if (!checker.containsConjunct(currentParentClass, equivalentClass)) {
//                    // Potential operand
//                    candidateDescriptions.add(equivalentClass);
//                }
//                else {
//                    // This axiom is relevant
//                    if (equivalentClass.isAnonymous()) {
//                        found = true;
//                    }
//                }
//            }
//            if (!found) {
//                return;
//            }
//            namedClassExtractor.reset();
//            for (OWLClassExpression desc : candidateDescriptions) {
//                //TODO
//                //desc.accept(namedClassExtractor);
//            }
//            results.addAll(namedClassExtractor.getResult());
//        }
//
//
//        private boolean namedClassInEquivalentAxiom(OWLEquivalentClassesAxiom axiom) {
//            for (OWLClassExpression equiv : axiom.getClassExpressions()){
//                if (!equiv.isAnonymous()){
//                    return true;
//                }
//            }
//            return false;
//        }
    }
