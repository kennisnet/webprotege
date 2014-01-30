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
public class NamedIndividualHierarchyParentRemovedEvent extends HierarchyChangedEvent<OWLNamedIndividual, NamedIndividualHierarchyParentRemovedHandler> {

    public static final transient Type<NamedIndividualHierarchyParentRemovedHandler> TYPE = new Type<NamedIndividualHierarchyParentRemovedHandler>();

    public NamedIndividualHierarchyParentRemovedEvent(ProjectId source, OWLNamedIndividual child, OWLNamedIndividual parent, HierarchyId<OWLNamedIndividual> hierarchyId) {
        super(source, child, parent, hierarchyId);
    }

    private NamedIndividualHierarchyParentRemovedEvent() {
    }

    @Override
    public Type<NamedIndividualHierarchyParentRemovedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NamedIndividualHierarchyParentRemovedHandler handler) {
        handler.handleNamedIndividualHierarchyParentRemoved(this);
    }
}
