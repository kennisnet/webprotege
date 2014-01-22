package edu.stanford.bmir.protege.web.client.ui.library.suggest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.gwtext.client.widgets.MessageBox;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsAction;
import edu.stanford.bmir.protege.web.shared.project.GetAvailableProjectsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import org.semanticweb.owlapi.model.EntityType;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class EntitySuggestOracle extends SuggestOracle {

    public static final int DEFAULT_SUGGEST_LIMIT = 30;

    private int suggestLimit = DEFAULT_SUGGEST_LIMIT;

    final private ProjectId projectId;
    private List<ProjectDetails> projects;

    final Set<EntityType<?>> entityTypes = new HashSet<EntityType<?>>();

    public EntitySuggestOracle(ProjectId projectId, int suggestLimit, EntityType<?> ... entityMatchTypes) {
        this.projectId = projectId;
        this.suggestLimit = suggestLimit;
        entityTypes.addAll(Arrays.asList(entityMatchTypes));
        
        GetAvailableProjectsAction action = new GetAvailableProjectsAction();
        DispatchServiceManager.get().execute(action, new AsyncCallback<GetAvailableProjectsResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log(caught.getMessage());
                MessageBox.alert("There was a problem retrieving the list of projects from the server.  Please refresh your browser to try again.");
            }

            @Override
            public void onSuccess(GetAvailableProjectsResult result) {
                final List<ProjectDetails> projectDetails = result.getDetails();
                List<ProjectDetails> list = new ArrayList<ProjectDetails>(projectDetails.size());
                for(ProjectDetails pd : projectDetails) {
                	if (! pd.isInTrash()) {
                		list.add(pd);
                	}
                }
                setProjects(list);
            }
        });
        
    }
    
    public void setProjects(List<ProjectDetails> list) {
    	this.projects = list;
    }

    public EntitySuggestOracle(ProjectId projectId) {
        this(projectId, DEFAULT_SUGGEST_LIMIT);
    }

    public void setEntityTypes(Set<EntityType<?>> entityTypes) {
        this.entityTypes.clear();
        this.entityTypes.addAll(entityTypes);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public void requestSuggestions(final Request request, final Callback callback) {
        if(entityTypes.isEmpty()) {
            callback.onSuggestionsReady(request, new Response(Collections.<Suggestion>emptyList()));
            return;
        }
        final AsyncCallbackMerger callbackMerger = new AsyncCallbackMerger(this.projects, request, callback);
        for (final ProjectDetails project : this.projects) {
	        //final String projectName = project.getDisplayName();
        	DispatchServiceManager.get().execute(new LookupEntitiesAction(project.getProjectId(), new EntityLookupRequest(request.getQuery(), SearchType.getDefault(), suggestLimit, entityTypes)), /*new AsyncCallback<LookupEntitiesResult>() {
	            @Override
	            public void onFailure(Throwable caught) {
	                MessageBox.alert(caught.getMessage());
	            }
	
	            @Override
	            public void onSuccess(LookupEntitiesResult result) {
	                List<EntitySuggestion> suggestions = new ArrayList<EntitySuggestion>();
	                for (final EntityLookupResult entity : result.getEntityLookupResults()) {
	                    suggestions.add(new EntitySuggestion(entity.getOWLEntityData(), entity.getDisplayText() + " (" + projectName + ")"));
	                }
	                callback.onSuggestionsReady(request, new Response(suggestions));
	            }
	        }*/ new AsyncCallback<LookupEntitiesResult>() {
            	
            	@Override
            	public void onFailure(Throwable caught) {
            		callbackMerger.onFailure(project, caught);
            	}
            	
            	@Override
            	public void onSuccess(LookupEntitiesResult result) {
            		callbackMerger.onSuccess(project, result);
            	}
            });
        }
    }

    @Override
    public boolean isDisplayStringHTML() {
        return true;
    }

    private class AsyncCallbackMerger {

    	final private List<ProjectDetails> projects;
    	final private Request request;
    	final private Callback callback;
    	private int handled;
        private List<EntitySuggestion> suggestions;
    	
    	public AsyncCallbackMerger(List<ProjectDetails> projects, final Request request, final Callback callback) {
    		this.projects = projects;
    		this.request = request;
    		this.callback = callback;
    		this.handled = 0;
    		this.suggestions = new ArrayList<EntitySuggestion>();
    	}
    	
        public void onFailure(ProjectDetails project, Throwable caught) {
            this.handled++;
        	MessageBox.alert(caught.getMessage());
        	suggestIfDone();
        }

        public void onSuccess(ProjectDetails project, LookupEntitiesResult result) {
        	this.handled++;
            for (EntityLookupResult entity : result.getEntityLookupResults()) {
            	suggestions.add(new EntitySuggestion(entity.getOWLEntityData(), entity.getDisplayText() + " (" + project.getDisplayName() + ")"));
            }
        	suggestIfDone();
        }
        
        private void suggestIfDone() {
        	if (handled == projects.size()) {
	            callback.onSuggestionsReady(request, new Response(suggestions));
        	}
        }
    }

}
