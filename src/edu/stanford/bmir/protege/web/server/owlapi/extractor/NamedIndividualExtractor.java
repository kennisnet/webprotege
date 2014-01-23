// $Id$
package edu.stanford.bmir.protege.web.server.owlapi.extractor;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLIndividualVisitor;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;

//import edu.stanford.bmir.protege.web.server.owlapi.extractor.visitor.OWLNamedIndividualVisitorAdapter;


/**
 * 
 * 
 * @author reumerman
 * @version $Id$
 */
public class NamedIndividualExtractor implements OWLIndividualVisitor {

        Set<OWLNamedIndividual> result = new HashSet<OWLNamedIndividual>();


        public void reset() {
            result.clear();
        }


        public Set<OWLNamedIndividual> getResult() {
            return result;
        }


        public void visit(OWLNamedIndividual desc) {
            result.add(desc);
        }

        public void visit(OWLAnonymousIndividual desc) {
//            if (desc.equals(searchClass)) {
//                found = true;
//            }
        }

//        public void visit(OWLObjectIntersectionOf desc) {
//            for (OWLClassExpression op : desc.getOperands()) {
//                //TODO
//                //op.accept(this);
//            }
//        }
    }
