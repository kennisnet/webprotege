package edu.stanford.bmir.protege.web.client.ui.projectdetails;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.smi.protege.server.util.ProjectInfo;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2013
 */
public class ProjectDetailsViewImpl extends Composite implements ProjectDetailsEditor {

    interface ProjectDetailsViewImplUiBinder extends UiBinder<HTMLPanel, ProjectDetailsViewImpl> {

    }

    private static ProjectDetailsViewImplUiBinder ourUiBinder = GWT.create(ProjectDetailsViewImplUiBinder.class);

    @UiField
    protected TextBoxBase ownerField;

    @UiField
    protected TextBoxBase courseField;

    @UiField
    protected TextBoxBase levelField;


    private boolean dirty = false;

    @Override
    public Widget getWidget() {
        return this;
    }

    @UiHandler("ownerField")
    protected void handleOwnerChanged(ValueChangeEvent<String> event) {
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("courseField")
    protected void handleCourseChanged(ValueChangeEvent<String> event) {
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    @UiHandler("levelField")
    protected void handleLevelChanged(ValueChangeEvent<String> event) {
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
    }

    public ProjectDetailsViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }


    @Override
    public void clearValue() {
        ownerField.setText("");
        courseField.setText("");
        levelField.setText("");
        dirty = false;
    }

    @Override
    public void setValue(ProjectDetails object) {
        dirty = false;
        ownerField.setValue("");
        courseField.setValue("");
        levelField.setValue("");
        if(object == null) {
            return;
        }
        ownerField.setValue(object.getOwner().getUserName());
        courseField.setValue(object.getCourse());
        levelField.setValue(object.getLevel());
    }

    @Override
    public Optional<ProjectDetails> getValue() {
        String owner = ownerField.getValue().trim();
        String course = courseField.getValue().trim();
        String level = levelField.getValue().trim();
        return Optional.of(null);
    }

    @Override
    public boolean isEnabled() {
        return ownerField.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        ownerField.setEnabled(enabled);
        courseField.setEnabled(enabled);
        levelField.setEnabled(enabled);
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<ProjectDetails>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }
}