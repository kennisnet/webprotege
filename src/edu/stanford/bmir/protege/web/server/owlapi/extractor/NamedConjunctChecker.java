// $Id$
package edu.stanford.bmir.protege.web.server.owlapi.extractor;

import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLIndividualVisitor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;

//import edu.stanford.bmir.protege.web.server.owlapi.extractor.visitor.OWLNamedIndividualVisitorAdapter;

/**
 * Checks whether a class description contains a specified named conjunct.
 * 
 * @author reumerman
 * @version $Id$
 */
public class NamedConjunctChecker implements OWLIndividualVisitor {

        private boolean found;

        private OWLNamedIndividual searchClass;


        public boolean containsConjunct(OWLNamedIndividual conjunct, OWLNamedIndividual description) {
            //conjunct.getEntityType().
            
            found = false;
            searchClass = conjunct;
            //TODO
            //description.accept(this);
            return found;
        }

        //////////////////////////////////////////////////////////////////////////////////////////


        public void visit(OWLNamedIndividual desc) {
            if (desc.equals(searchClass)) {
                found = true;
            }
        }

        public void visit(OWLAnonymousIndividual desc) {
            if (desc.equals(searchClass)) {
                found = true;
            }
        }

//        public void visit(OWLObjectIntersectionOf desc) {
//            for (OWLClassExpression op : desc.getOperands()) {
//                //TODO
//                //op.accept(this);
//                if (found) {
//                    break;
//                }
//            }
//        }

        //////////////////////////////////////////////////////////////////////////////////////////
    }
