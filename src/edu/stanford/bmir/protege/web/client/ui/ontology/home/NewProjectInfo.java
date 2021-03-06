package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/05/2012
 */
public class NewProjectInfo {

    private String projectName;
    
    private String projectDescription;

    private String projectCourse;

    private String projectLevel;

    private ProjectType projectType;

    /**
     * Constructs a {@link NewProjectInfo} that holds a project name and a project description.
     * @param projectName The project name.  Not null.
     * @param projectDescription The project description.  Not null.
     * @param  projectType The type of the project.  Not null.
     * @throws NullPointerException if projectName, projectDescription are null.
     */
    public NewProjectInfo(String projectName, String projectDescription, String projectCourse, String projectLevel, ProjectType projectType) {
        if(projectName == null) {
            throw new NullPointerException("projectName must not be null");
        }
        if(projectDescription == null) {
            throw new NullPointerException("projectDescription must not be null");
        }
        if(projectCourse == null) {
            throw new NullPointerException("vak/leergebied mag niet leeg zijn");
        }
        if(projectLevel == null) {
            throw new NullPointerException("leerniveau mag niet leeg zijn");
        }
        if(projectType == null) {
            throw new NullPointerException("projectType must not be null");
        }
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectCourse = projectCourse;
        this.projectLevel = projectLevel;
        this.projectType = projectType;
    }

    /**
     * Gets the project name
     * @return A string representing the project name.  Not null.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets the project description.
     * @return A string representing the project description.  Not null.
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    /**
     * Gets the project course.
     * @return A string representing the project course.  Not null.
     */
    public String getProjectCourse() {
        return projectCourse;
    }

    /**
     * Gets the project level.
     * @return A string representing the project level.  Not null.
     */
    public String getProjectLevel() {
        return projectLevel;
    }

    /**
     * Gets the type of the project.
     * @return The project type. Not null.
     */
    public ProjectType getProjectType() {
        return projectType;
    }
}
