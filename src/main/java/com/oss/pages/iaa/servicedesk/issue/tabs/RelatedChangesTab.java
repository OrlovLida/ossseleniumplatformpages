package com.oss.pages.iaa.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.CHANGE_WIZARD_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.CREATE_CHANGE_BUTTON_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.ID_UPPERCASE_ATTRIBUTE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.NORMAL_BUTTON_ID;

public class RelatedChangesTab extends RelatedTab {

    public static final String CHANGES_TABLE_ID = "_relatedChangesApp";
    public static final String LINK_CHANGE_ID = "_linkChange";
    public static final String UNLINK_CHANGE_ID = "_unlinkChange";
    private static final String RELATED_CHANGE_EXPORT_FILE = "ChangeRequest*.xlsx";
    private static final String LINK_CHANGE_PROMPT_ID = "_linkChangeModal_prompt-card";
    private static final String INCIDENT_DESCRIPTION_LABEL = "Incident Description";

    public RelatedChangesTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public SDWizardPage openCreateChangeWizard() {
        return SDWizardPage.openCreateWizard(driver, wait, NORMAL_BUTTON_ID, CREATE_CHANGE_BUTTON_ID, CHANGE_WIZARD_ID);
    }

    public String getIncidentDescriptionFromTable() {
        return getRelatedIssuesTable().getCellValue(0, INCIDENT_DESCRIPTION_LABEL);
    }

    @Override
    public String getTableId() {
        return CHANGES_TABLE_ID;
    }

    @Override
    public String getLinkActionId() {
        return LINK_CHANGE_ID;
    }

    @Override
    public String getUnlinkActionId() {
        return UNLINK_CHANGE_ID;
    }

    @Override
    public String getRelatedIssueExportFile() {
        return RELATED_CHANGE_EXPORT_FILE;
    }

    @Override
    public String getLinkIssuePromptId() {
        return LINK_CHANGE_PROMPT_ID;
    }

    @Override
    public String getIdAttribute() {
        return ID_UPPERCASE_ATTRIBUTE;
    }
}
