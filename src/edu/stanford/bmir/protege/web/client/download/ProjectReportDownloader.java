package edu.stanford.bmir.protege.web.client.download;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/10/2013
 * <p>
 *     Downloads a project (possibly a specific revision) by opening a new browser window
 * </p>
 */
public class ProjectReportDownloader {

    private final ProjectId projectId;

    /**
     * Constructs a ProjectRevisionDownloader for the specified project, revision and project format.
     * @param projectId The project id.  Not {@code null}.
     * @param revisionNumber The revision to download.  Not {@code null}.
     * @param downloadFormatExtension The format that the project should be downloaded in.  Not {@code null}.
     * @throws  NullPointerException if any parameters are {@code null}.
     */
    public ProjectReportDownloader(ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    /**
     * Causes a new browser window to be opened which will download the specified project revision in the specified
     * format.
     */
    public void report() {
        String encodedProjectName = URL.encode(projectId.getId());
        String baseURL = GWT.getHostPageBaseURL();
        String downloadURL = baseURL + "download?ontology=" + encodedProjectName  + "&report=1";
        Window.open(downloadURL, "Download report", "");
    }

}
