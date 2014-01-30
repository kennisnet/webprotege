package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/03/2013
 */
public class NamedIndividualHierarchyParentAddedEvent extends HierarchyChangedEvent<OWLNamedIndividual, NamedIndividualHierarchyParentAddedHandler> {

    public static final transient Type<NamedIndividualHierarchyParentAddedHandler> TYPE = new Type<NamedIndividualHierarchyParentAddedHandler>();

    public NamedIndividualHierarchyParentAddedEvent(ProjectId source, OWLNamedIndividual child, OWLNamedIndividual parent, HierarchyId<OWLNamedIndividual> hierarchyId) {
        super(source, child, parent, hierarchyId);
    }

    private NamedIndividualHierarchyParentAddedEvent() {
    }

    @Override
    public Type<NamedIndividualHierarchyParentAddedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NamedIndividualHierarchyParentAddedHandler handler) {
        handler.handleNamedIndividualHierarchyParentAdded(this);
    }
}
