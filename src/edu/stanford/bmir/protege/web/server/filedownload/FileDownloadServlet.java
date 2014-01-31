package edu.stanford.bmir.protege.web.server.filedownload;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import edu.stanford.bmir.protege.web.server.owlapi.AssertedNamedIndividualHierarchyProvider;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

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
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(resp.getOutputStream());
            
            // Access to data
            OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.getProjectDocumentStore(projectId);
            OWLAPIProject project = OWLAPIProject.getProject(documentStore);
            AssertedNamedIndividualHierarchyProvider provider = project.getNamedIndividualHierarchyProvider();
            final RenderingManager rm = new RenderingManager(project);
            
            OWLNamedIndividual root = provider.getRoots(projectId).iterator().next();
            
            // Presentation
            StringBuffer output = new StringBuffer();
            output.append("<html><head><title>Leerplan in beeld - Project " + projectId + "</title>" +
            		"<link rel='stylesheet' type='text/css' href='css/Report.css'/>" +
            		"</head><body>");
            if (root != null) {
                // root = vak / leergebied
            	output.append("<h1>" + rm.getBrowserText(root) + "</h1>" +
            			"<table><thead><tr><th>Vakgebieden</th><th>Kerndoelen</th></tr></thead><tbody>");
                
            	// child = vakkern
            	List<OWLNamedIndividual> children = new ArrayList<OWLNamedIndividual>(provider.getChildren(root, project.getRootOntology(), project, AssertedNamedIndividualHierarchyProvider.TYPE_INHOUD));
            	Collections.sort(children, new Comparator<OWLNamedIndividual>() {

					@Override
					public int compare(OWLNamedIndividual arg0,
							OWLNamedIndividual arg1) {
						return rm.getBrowserText(arg0).compareTo(rm.getBrowserText(arg1));
					}
            		
            	});

	            for (OWLNamedIndividual child : children) {
	                output.append("<tr><td>" + rm.getBrowserText(child) + "</td><td><ul>");
	                
	                // subchild = kerndoel
	                List<OWLNamedIndividual> subchildren = new ArrayList<OWLNamedIndividual>(provider.getChildren(child, project.getRootOntology(), project, AssertedNamedIndividualHierarchyProvider.TYPE_DOELEN));
	            	Collections.sort(subchildren, new Comparator<OWLNamedIndividual>() {

						@Override
						public int compare(OWLNamedIndividual arg0,
								OWLNamedIndividual arg1) {
							return rm.getBrowserText(arg0).compareTo(rm.getBrowserText(arg1));
						}
	            		
	            	});

	                for (OWLNamedIndividual subchild : subchildren) {
    	                output.append("<li>" + rm.getBrowserText(subchild) + "</li>");
    	            }
	                output.append("</ul></td></tr>");
	            }
            }
            output.append("</tbody></table></body></html>");
            
            bufferedOutputStream.write(output.toString().getBytes());
            bufferedOutputStream.flush();
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }
}
