package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public interface ReportProjectRequestHandler {

    /**
     * Handle a request to download a report of the specified project.  The project is identified by its {@link ProjectId}.
     * @param projectId The {@link ProjectId} that identifies the project of which a report to be downloaded.
     */
    void handleProjectReportRequest(ProjectId projectId);
}
