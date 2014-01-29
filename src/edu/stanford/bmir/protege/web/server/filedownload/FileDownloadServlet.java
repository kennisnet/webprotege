package edu.stanford.bmir.protege.web.server.filedownload;

import edu.stanford.bmir.protege.web.server.owlapi.AssertedNamedIndividualHierarchyProvider;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/06/2012
 * <p>
 *     A servlet which allows ontologies to be downloaded from WebProtege.  See {@link OWLAPIProjectDownloader} for
 *     the piece of machinery that actually does the processing of request parameters and the downloading.
 * </p>
 */
public class FileDownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FileDownloadParameters downloadParameters = new FileDownloadParameters(req);
        if(downloadParameters.isProjectDownload()) {
            ProjectId projectId = downloadParameters.getProjectId();
            RevisionNumber revisionNumber = downloadParameters.getRequestedRevision();
            DownloadFormat format = downloadParameters.getFormat();
            OWLAPIProjectDownloader downloader = new OWLAPIProjectDownloader(projectId, revisionNumber, format);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(resp.getOutputStream());
            downloader.writeProject(resp, bufferedOutputStream);
            bufferedOutputStream.flush();
        }
        else if(downloadParameters.isProjectReport()) {
            ProjectId projectId = downloadParameters.getProjectId();
            RevisionNumber revisionNumber = downloadParameters.getRequestedRevision();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(resp.getOutputStream());
            
            // Access to data
            OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.getProjectDocumentStore(projectId);
            OWLAPIProject project = OWLAPIProject.getProject(documentStore);
            AssertedNamedIndividualHierarchyProvider provider = project.getNamedIndividualHierarchyProvider();
            OWLNamedIndividual root = provider.getRoots().iterator().next();
            
            // Presentation
            StringBuffer output = new StringBuffer();
            output.append("<html><head><title>Leerplan in beeld - Project " + projectId + "</title>" +
            		"<link rel='stylesheet' type='text/css' href='css/Report.css'/>" +
            		"</head><body><ul>");
            output.append("<li>" + root.getIRI() + "</li><ul>");
            for (OWLNamedIndividual child : provider.getChildren(root)) {
                output.append("<li>" + child.getIRI() + "</li>");
            }
            output.append("</ul></ul></body></html>");
            
            bufferedOutputStream.write(output.toString().getBytes());
            bufferedOutputStream.flush();
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }
}
