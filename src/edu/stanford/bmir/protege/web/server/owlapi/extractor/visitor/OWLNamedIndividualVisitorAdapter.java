// $Id$
package edu.stanford.bmir.protege.web.server.owlapi.extractor.visitor;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;

/**
 * 
 * 
 * @author reumerman
 * @version $Id$
 */
public class OWLNamedIndividualVisitorAdapter implements OWLNamedIndividualVisitor {

        @Override
        public void visit(OWLClass desc) {
        }


        @Override
        public void visit(OWLObjectIntersectionOf desc) {
        }


        @Override
        public void visit(OWLObjectUnionOf desc) {
        }


        @Override
        public void visit(OWLObjectComplementOf desc) {
        }


        @Override
        public void visit(OWLObjectSomeValuesFrom desc) {
        }


        @Override
        public void visit(OWLObjectAllValuesFrom desc) {
        }


        @Override
        public void visit(OWLObjectHasValue desc) {
        }


        @Override
        public void visit(OWLObjectMinCardinality desc) {
        }


        @Override
        public void visit(OWLObjectExactCardinality desc) {
        }


        @Override
        public void visit(OWLObjectMaxCardinality desc) {
        }


        @Override
        public void visit(OWLObjectHasSelf desc) {
        }


        @Override
        public void visit(OWLObjectOneOf desc) {
        }


        @Override
        public void visit(OWLDataSomeValuesFrom desc) {
        }


        @Override
        public void visit(OWLDataAllValuesFrom desc) {
        }


        @Override
        public void visit(OWLDataHasValue desc) {
        }


        @Override
        public void visit(OWLDataMinCardinality desc) {
        }


        @Override
        public void visit(OWLDataExactCardinality desc) {
        }


        @Override
        public void visit(OWLDataMaxCardinality desc) {
        }
    }
