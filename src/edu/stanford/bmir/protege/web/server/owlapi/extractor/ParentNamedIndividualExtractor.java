// $Id$
package edu.stanford.bmir.protege.web.server.owlapi.extractor;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

import edu.stanford.bmir.protege.web.server.owlapi.extractor.model.OWLSubClassofAxiomForNamedIndividual;

/**
 * 
 * 
 * @author reumerman
 * @version $Id$
 */
public class ParentNamedIndividualExtractor extends OWLAxiomVisitorAdapter {

        private NamedIndividualExtractor extractor = new NamedIndividualExtractor();

        private OWLNamedIndividual current;


        public void setCurrentClass(OWLNamedIndividual current) {
            this.current = current;
        }


        public void reset() {
            extractor.reset();
        }


        public Set<OWLNamedIndividual> getResult() {
            return extractor.getResult();
        }


        public void visit(OWLSubClassofAxiomForNamedIndividual axiom) {
            axiom.getSuperClass().accept(extractor);
        }


//        public void visit(OWLEquivalentClassesAxiom axiom) {
//            for (OWLClassExpression desc : axiom.getClassExpressions()) {
//                if (desc.equals(current)) {
//                    continue;
//                }
//                //TODO
//                //desc.accept(extractor);
//            }
//        }
    }
