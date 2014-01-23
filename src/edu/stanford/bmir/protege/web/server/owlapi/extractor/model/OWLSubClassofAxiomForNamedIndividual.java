// $Id$
package edu.stanford.bmir.protege.web.server.owlapi.extractor.model;

import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * 
 * 
 * @author reumerman
 * @version $Id$
 */
public interface OWLSubClassofAxiomForNamedIndividual extends OWLIndividualAxiom {

        /**
         * Gets the subclass in this axiom
         * @return The class expression that represents the subclass in this axiom.
         */
        OWLNamedIndividual getSubClass();

        /**
         * Gets the superclass in this axiom.
         * @return The class expression that represents the superclass in this axiom.
         */
        OWLNamedIndividual getSuperClass();


        /**
         * Determines if this subclass axiom has a subclass that is anonymous.
         * (if the subclass is anonymous then the subclass axiom is known as
         * a General Concept Inclusion - GCI).
         *
         * @return <code>true</code> if this axiom is a GCI, other wise <code>false</code>.
         */
        boolean isGCI();

        @Override
        OWLSubClassofAxiomForNamedIndividual getAxiomWithoutAnnotations();
    }
