package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import java.util.ArrayList;
import java.util.List;

import com.gwtext.client.widgets.tree.TreeNode;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;

/**
 * Portlet for displaying class trees. It can be configured to show only a
 * subtree of an ontology, by setting the portlet property <code>topClass</code>
 * to the name of the top class to show. <br>
 * Also supports creating and editing classes.
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class NamedIndividualTreePortlet extends ClassTreePortlet {

    public NamedIndividualTreePortlet(final Project project) {
        super(project);
    }

    public NamedIndividualTreePortlet(final Project project, final boolean showToolbar, final boolean showTitle, final boolean showTools, final boolean allowsMultiSelection, final String topClass) {
        super(project, showToolbar, showTitle, showTools, allowsMultiSelection, topClass);
    }

    public void getSubclasses(final String parentClsName, final TreeNode parentNode) {
        if (isSubclassesLoaded(parentNode)) {
            return;
        }
        if (hierarchyProperty == null) {
            invokeGetSubclassesForNamedIndividualRemoteCall(parentClsName, getSubclassesCallback(parentClsName, parentNode));
        }
        else {
            final List<String> subjects = new ArrayList<String>();
            subjects.add(parentClsName);
            final List<String> props = new ArrayList<String>();
            props.add(hierarchyProperty);
            OntologyServiceManager.getInstance().getEntityTriples(getProjectId(), subjects, props, new GetPropertyHierarchySubclassesOfClassHandler(parentClsName, parentNode));
        }
    }


}
