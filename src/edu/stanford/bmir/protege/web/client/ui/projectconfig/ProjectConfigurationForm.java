package edu.stanford.bmir.protege.web.client.ui.projectconfig;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectType;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasFormData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.ui.library.dropdown.DropDown;
import edu.stanford.bmir.protege.web.client.ui.library.dropdown.DropDownModel;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2012
 */
public class ProjectConfigurationForm extends FlowPanel implements HasFormData<ProjectConfigurationInfo>, HasInitialFocusable {

    public static final String FIELD_WIDTH = "300px";

    public static final String PROJECT_NAME_FIELD_NAME = "Name";

    public static final String PROJECT_TYPE_DROPDOWN_FIELD_NAME = "Type";

    public static final String PROJECT_DESCRIPTION_FIELD_NAME = "Description";

    public static final String PROJECT_COURSE_FIELD_NAME = "Vak/leergebied";

    public static final String PROJECT_LEVEL_FIELD_NAME = "leerniveau";

    private DropDown<ProjectType> projectTypeDropDown;

    private ProjectId projectId;

    private final TextBox projectNameTextBox;
    
    private final TextArea projectDescriptionTextArea;

    private final TextBox projectCourseTextBox;

    private final TextBox projectLevelTextBox;

//    private final TextBox defaultLanguageBox;

//    private final TextBox bioPortalRestBaseTextBox;

    public ProjectConfigurationForm(ProjectId id) {
        this.projectId = id;

        WebProtegeDialogForm form = new WebProtegeDialogForm();

        projectNameTextBox = new TextBox();
        projectNameTextBox.setWidth(FIELD_WIDTH);
        form.addWidget(PROJECT_NAME_FIELD_NAME, projectNameTextBox);
        form.addVerticalSpacer();

        projectTypeDropDown = new DropDown<ProjectType>(new ProjectTypeDropDownModel());
        form.addWidget(PROJECT_TYPE_DROPDOWN_FIELD_NAME, projectTypeDropDown);
        form.addVerticalSpacer();

//        defaultLanguageBox = form.addTextBox("Default language", "Enter a default language e.g. en", "en", new NullWebProtegeDialogTextFieldValidator());
//        form.addVerticalSpacer();

        projectDescriptionTextArea = new TextArea();
        projectDescriptionTextArea.setVisibleLines(3);
        projectDescriptionTextArea.setCharacterWidth(60);
        form.addWidget(PROJECT_DESCRIPTION_FIELD_NAME, projectDescriptionTextArea);

        form.addVerticalSpacer();

        projectCourseTextBox = new TextBox();
        projectCourseTextBox.setWidth(FIELD_WIDTH);
        form.addWidget(PROJECT_COURSE_FIELD_NAME, projectCourseTextBox);

        form.addVerticalSpacer();

        projectLevelTextBox = new TextBox();
        projectLevelTextBox.setWidth(FIELD_WIDTH);
        form.addWidget(PROJECT_LEVEL_FIELD_NAME, projectLevelTextBox);

        form.addVerticalSpacer();

//        BioPortalConfigurationManager manager = BioPortalConfigurationManager.getManager();
//        bioPortalRestBaseTextBox = form.addTextBox("BioPortal Rest API Base", "The URL of the BioPortal Rest API Service", manager.getRestBase());


//        form.addVerticalSpacer();
//
//        form.addWidget("", new Button("Entity Id Generator Settings ...", new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                EntityIdGeneratorSettingsDialog dlg = new EntityIdGeneratorSettingsDialog(projectId);
//                dlg.show();
//            }
//        }));

        add(form);


    }
    
    public void setAllowedProjectTypes(List<ProjectType> projectTypes) {
        projectTypeDropDown.setModel(new ProjectTypeDropDownModel(projectTypes));
    }

    public void setData(ProjectConfigurationInfo info) {
        projectNameTextBox.setText(info.getProjectName());
        projectTypeDropDown.setSelectedItem(info.getProjectType());
        projectDescriptionTextArea.setText(info.getProjectDescription());
        projectCourseTextBox.setText(info.getProjectCourse());
        projectLevelTextBox.setText(info.getProjectLevel());
//        defaultLanguageBox.setText(info.getDefaultLanguage());
    }



    public ProjectConfigurationInfo getData() {
        // TODO: DEF LANG
        return new ProjectConfigurationInfo(projectId, getProjectName(), getProjectType(), "en", getProjectDescription(), getProjectCourse(), getProjectLevel());
    }

//    private String getDefaultLanguage() {
//        return defaultLanguageBox.getText().trim();
//    }

    public String getProjectName() {
        return projectNameTextBox.getText().trim();
    }

    public ProjectType getProjectType() {
        return projectTypeDropDown.getSelectedItem();
    }
    
    public String getProjectDescription() {
        return projectDescriptionTextArea.getText().trim();
    }

    public String getProjectCourse() {
        return projectCourseTextBox.getText().trim();
    }

    public String getProjectLevel() {
        return projectLevelTextBox.getText().trim();
    }

    
    
    private class ProjectTypeDropDownModel implements DropDownModel<ProjectType> {

        private List<ProjectType> projectTypeList = new ArrayList<ProjectType>();

        private ProjectTypeDropDownModel() {
        }

        private ProjectTypeDropDownModel(List<ProjectType> projectTypes) {
            this.projectTypeList.addAll(projectTypes);
        }

        public int getSize() {
            return projectTypeList.size();
        }

        public ProjectType getItemAt(int index) {
            return projectTypeList.get(index);
        }

        public String getRendering(int index) {
            return projectTypeList.get(index).getName();
        }


    }
    
    public Optional<Focusable> getInitialFocusable() {
        return Optional.<Focusable>of(projectTypeDropDown);
    }
}
