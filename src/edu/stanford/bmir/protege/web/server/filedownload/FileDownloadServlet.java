package edu.stanford.bmir.protege.web.server.filedownload;

import edu.stanford.bmir.protege.web.server.frame.PropertyValueComparator;
import edu.stanford.bmir.protege.web.server.owlapi.AssertedNamedIndividualHierarchyProvider;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import com.sun.xml.internal.ws.encoding.RootOnlyCodec;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            
//            OWLNamedIndividual root = null;
//            
//            if (projectId.getId().equals("17983a52-a09a-43dc-a605-dd51193dd8c1")) {
//            	root = project.getDataFactory().getOWLNamedIndividual(IRI.create("http://purl.edustandaard.nl/begrippenkader/fbd066cb-ebd9-4aac-9eb7-c5978a4358fa"));
//            } else if (projectId.getId().equals("27c9e350-e784-4b52-9961-641dee89d437")) {
//            	root = project.getDataFactory().getOWLNamedIndividual(IRI.create("http://purl.edustandaard.nl/begrippenkader/030506c9-dd12-4f99-81c2-7c436c438a59"));
//            } else if (projectId.getId().equals("32621e27-d10c-42bf-828b-2a3e5f56c1e1")) {
//            	root = project.getDataFactory().getOWLNamedIndividual(IRI.create("http://purl.edustandaard.nl/begrippenkader/24a33af5-7ad9-4407-b782-e81ea2f17f06"));
//            } else if (projectId.getId().equals("40a2a792-2976-47a9-9a56-ef21353f42b2")) {
//            	root = project.getDataFactory().getOWLNamedIndividual(IRI.create("http://purl.edustandaard.nl/begrippenkader/bbdb9d07-853a-486f-9bb8-a8ea3e3d682a"));
//            } else if (projectId.getId().equals("6922ef90-fe35-4cea-acd0-7e2cff6142f1")) {
//            	root = project.getDataFactory().getOWLNamedIndividual(IRI.create("http://purl.edustandaard.nl/begrippenkader/b5dcf68d-df0b-4c00-b0aa-9cc63c7a9f63"));
//            } else if (projectId.getId().equals("7b06e9eb-8a21-4bc4-9bca-4143fcfd0d57")) {
//            	root = project.getDataFactory().getOWLNamedIndividual(IRI.create("http://purl.edustandaard.nl/begrippenkader/bc31b870-925f-4f82-9037-6017b0ca4edf"));
//            } else if (projectId.getId().equals("86a6fa1e-69b3-464c-87d0-6cfca68e092e")) {
//            	root = project.getDataFactory().getOWLNamedIndividual(IRI.create("http://purl.edustandaard.nl/begrippenkader/332b6382-c6b9-4c4b-a04e-2e36e72cc337"));
//            } else if (projectId.getId().equals("968d328d-b66b-40a1-9dce-12e7e68e35d7")) {
//            	root = project.getDataFactory().getOWLNamedIndividual(IRI.create("http://purl.edustandaard.nl/begrippenkader/a9be8e05-738b-43b8-9d43-161420a4ee8d"));
//            } else if (projectId.getId().equals("a97510bc-e452-4519-8a70-a784c2a0f489")) {
//            	root = project.getDataFactory().getOWLNamedIndividual(IRI.create("http://purl.edustandaard.nl/begrippenkader/d61086bf-827a-476a-9403-7c5b317a12ed"));
//            } else if (projectId.getId().equals("bbb09cf6-4694-4b24-beb5-6be7ff4e0565")) {
//            	root = project.getDataFactory().getOWLNamedIndividual(IRI.create("http://purl.edustandaard.nl/begrippenkader/290f7aa0-4a45-4164-b30b-fb5f1e036b6c"));
//            } else if (projectId.getId().equals("d2201719-9e2e-4697-929e-9ff3ca8f6eee")) {
//            	root = project.getDataFactory().getOWLNamedIndividual(IRI.create("http://purl.edustandaard.nl/begrippenkader/d134fbd3-9e42-4bc2-a233-125197b3842b"));
//            } else if (projectId.getId().equals("e78300d2-3a03-4170-9eaa-1b48a330e427")) {
//            	root = project.getDataFactory().getOWLNamedIndividual(IRI.create("http://purl.edustandaard.nl/begrippenkader/f8b3332d-5b33-4963-b69e-3914d27c5f12"));
//            }
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
