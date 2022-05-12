package com.oss.pages.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.oss.pages.servicedesk.ServiceDeskConstants.ID_UPPERCASE_ATTRIBUTE;

public class RelatedChangesTab extends RelatedTab {

    public static final String CHANGES_TABLE_ID = "_relatedChangesApp";
    public static final String LINK_CHANGE_ID = "_linkChange";
    public static final String UNLINK_CHANGE_ID = "_unlinkChange";
    private static final String RELATED_CHANGE_EXPORT_FILE = "ChangeRequest*.xlsx";
    private static final String LINK_CHANGE_PROMPT_ID = "_linkChangeModal_prompt-card";

    public RelatedChangesTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
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
