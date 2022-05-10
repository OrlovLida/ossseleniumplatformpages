package com.oss.pages.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.oss.pages.servicedesk.ServiceDeskConstants.ID_UPPERCASE_ATTRIBUTE;

public class RelatedTicketsTab extends RelatedTab {

    private static final String LINK_ISSUE_BUTTON_ID = "LINK_TICKETS_ACTION";
    private static final String RELATED_TICKETS_TABLE_ID = "_relatedTicketsTableWidget";
    private static final String UNLINK_ISSUE_BUTTON_ID = "UNLINK_TICKET";
    private static final String RELATED_TICKETS_EXPORT_FILE = "TroubleTicket*.xlsx";
    private static final String LINK_TICKET_PROMPT_ID = "_linkSubTicketModal_prompt-card";

    public RelatedTicketsTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    public String getTableId() {
        return RELATED_TICKETS_TABLE_ID;
    }

    @Override
    public String getLinkActionId() {
        return LINK_ISSUE_BUTTON_ID;
    }

    @Override
    public String getUnlinkActionId() {
        return UNLINK_ISSUE_BUTTON_ID;
    }

    @Override
    public String getRelatedIssueExportFile() {
        return RELATED_TICKETS_EXPORT_FILE;
    }

    @Override
    public String getLinkIssuePromptId() {
        return LINK_TICKET_PROMPT_ID;
    }

    @Override
    public String getIdAttribute() {
        return ID_UPPERCASE_ATTRIBUTE;
    }
}

