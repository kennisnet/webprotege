package edu.stanford.bmir.protege.web.client.ui.projectdetails;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetRootOntologyIdAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetRootOntologyIdResult;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

public class ProjectDetailsPortlet extends AbstractOWLEntityPortlet {

    private ProjectDetailsEditor editor;

    private boolean loaded = false;

    public ProjectDetailsPortlet(Project project) {
        super(project);
    }

    public ProjectDetailsPortlet(Project project, boolean initialize) {
        super(project, initialize);
    }

    @Override
    public void initialize() {
        editor = new ProjectDetailsViewImpl();
        add(editor.asWidget());
        setTitle("Project details");
        editor.setEnabled(false);
    }

    @Override
    public void onPermissionsChanged() {
//        editor.setEnabled(hasWritePermission());
    }

    @Override
    protected void onRefresh() {
        editor.setValue(getProject().getProjectDetails());
    }

    @Override
    public void reload() {
        if(!loaded) {
            loaded = true;
            onRefresh();
        }
    }

    @Override
    public Collection<EntityData> getSelection() {
        return null;
    }
    
}
