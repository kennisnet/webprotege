package edu.stanford.bmir.protege.web.client.ui.projectconfig;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2012
 */
public class ProjectConfigurationInfo implements Serializable {

    private ProjectId projectId;
    
    private ProjectType projectType;
    
    private String projectDescription;

    private String projectCourse;

    private String projectLevel;

    private String defaultLanguage;

    // For serialization
    private ProjectConfigurationInfo() {

    }

    public ProjectConfigurationInfo(ProjectId projectId, ProjectType projectType, String defaultLanguage, String projectDescription, String projectCourse, String projectLevel) {
        this.projectId = projectId;
        this.projectType = projectType;
        this.defaultLanguage = defaultLanguage;
        this.projectDescription = projectDescription;
        this.projectCourse = projectCourse;
        this.projectLevel = projectLevel;
    }


    public ProjectId getProjectId() {
        return projectId;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getProjectCourse() {
        return projectCourse;
    }

    public String getProjectLevel() {
        return projectLevel;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    @Override
    public int hashCode() {
        return projectType.hashCode() + projectDescription.hashCode() + projectCourse.hashCode() + projectLevel.hashCode() + projectId.hashCode() + defaultLanguage.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectConfigurationInfo)) {
            return false;
        }
        ProjectConfigurationInfo other = (ProjectConfigurationInfo) obj;
        return this.projectType.equals(other.projectType) && this.projectDescription.equals(other.projectDescription) && this.projectCourse.equals(other.projectCourse) && this.projectLevel.equals(other.projectLevel) && this.projectId.equals(other.projectId) && this.defaultLanguage.equals(other.defaultLanguage);
    }

    @Override
    public String toString() {
        return "ProjectConfigurationInfo(" + projectId + " Type(" + projectType + ") Description(" + projectDescription + ")" + " Course(" + projectCourse + ")" + " Level(" + projectLevel + ")" + " DefaultLanguage(" + defaultLanguage + ")";
    }
}
