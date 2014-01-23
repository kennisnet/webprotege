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
@SuppressWarnings("javadoc")
public interface OWLNamedIndividualVisitor {

        void visit(OWLClass ce);

        void visit(OWLObjectIntersectionOf ce);

        void visit(OWLObjectUnionOf ce);

        void visit(OWLObjectComplementOf ce);

        void visit(OWLObjectSomeValuesFrom ce);

        void visit(OWLObjectAllValuesFrom ce);

        void visit(OWLObjectHasValue ce);

        void visit(OWLObjectMinCardinality ce);

        void visit(OWLObjectExactCardinality ce);

        void visit(OWLObjectMaxCardinality ce);

        void visit(OWLObjectHasSelf ce);

        void visit(OWLObjectOneOf ce);

        void visit(OWLDataSomeValuesFrom ce);

        void visit(OWLDataAllValuesFrom ce);

        void visit(OWLDataHasValue ce);

        void visit(OWLDataMinCardinality ce);

        void visit(OWLDataExactCardinality ce);

        void visit(OWLDataMaxCardinality ce);


    }
