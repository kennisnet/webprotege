// Id$
package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.SubclassEntityData;

/**
 * 
 * 
 * @author reumerman
 * @version $Id$
 */
public class InhoudTreePortlet extends NamedIndividualTreePortlet {
    public InhoudTreePortlet(final Project project) {
        super(project, true, true, true, true, null);
    }

    public InhoudTreePortlet(final Project project, final boolean showToolbar, final boolean showTitle,
            final boolean showTools, final boolean allowsMultiSelection, final String topClass) {
        super(project, showToolbar, showTitle, showTools, allowsMultiSelection, topClass);
    }
    
    @Override
    protected void invokeGetSubclassesRemoteCall(final String parentClsName,
            AsyncCallback<List<SubclassEntityData>> callback) {
        OntologyServiceManager.getInstance().getSubclassesForNamedIndividual(getProjectId(), parentClsName,
                callback);
    }

}
