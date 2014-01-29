package edu.stanford.bmir.protege.web.client.ui.projectmanager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import edu.stanford.bmir.protege.web.client.download.DownloadFormatExtensionHandler;
import edu.stanford.bmir.protege.web.client.download.DownloadSettingsDialog;
import edu.stanford.bmir.protege.web.client.download.ProjectReportDownloader;
import edu.stanford.bmir.protege.web.client.download.ProjectRevisionDownloader;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.download.DownloadFormatExtension;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public class ReportProjectRequestHandlerImpl implements ReportProjectRequestHandler {

    @Override
    public void handleProjectReportRequest(final ProjectId projectId) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess() {
                doReport(projectId);
//                DownloadSettingsDialog.showDialog(new DownloadFormatExtensionHandler() {
//                    @Override
//                    public void handleDownload(DownloadFormatExtension extension) {
//                        doReport(projectId);
//                    }
//                });
            }
        });

    }

    private void doReport(ProjectId projectId) {
    	// [GJo] TODO: add code to actually generate leerplan in beeld report..
        ProjectReportDownloader downloader = new ProjectReportDownloader(projectId);
        downloader.report();
    }

}
