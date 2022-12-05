package com.oss.iaa.servicedesk.DTAG;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.ClosedTicketsPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.MOST_IMPORTANT_INFO_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;

public class TicketDetailsTest extends BaseTestCase {

    private IssueDetailsPage issueDetailsPage;
    private SDWizardPage sdWizardPage;
    private ClosedTicketsPage closedTicketsPage;

    private static final String MOST_IMPORTANT_TAB_ARIA_CONTROLS = "_mostImportantTab";
    private static final String SAME_MO_TT_TAB_ARIA_CONTROLS = "_sameMOTTTab";
    private static final String RELATED_TICKETS_TABLE_ID = "_relatedTicketsTableWidget";
    private static final String SAME_MO_TT_TABLE_ID = "_sameMOTTTableWidget";
    private static final String PARTICIPANTS_TABLE_ID = "_participantsTableApp";
    private static final String ROOT_CAUSES_TABLE_ID = "_rootCausesApp";
    private static final String ROOT_CAUSES_TREE_TABLE_ID = "_rootCausesAppTreeTable";
    private static final String NOTIFICATION_WIZARD_CANCEL_ID = "wizard-cancel-button-notification-wizard";
    private static final String EXCLUDED_TICKET_TYPE = "Email Request";

    @BeforeMethod
    public void goToClosedTicketDashboardPage(
    ) {
        closedTicketsPage = new ClosedTicketsPage(driver, webDriverWait).openView(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check Ticket Details view", description = "Check Ticket Details view")
    @Description("Check Ticket Details view")
    public void checkTicketDetailsView(
    ) {
        String ticketID = closedTicketsPage.getIdOfFirstFilteredTicket(EXCLUDED_TICKET_TYPE);
        issueDetailsPage = closedTicketsPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectDescriptionTab();
        issueDetailsPage.selectTabFromTablesWindow(MOST_IMPORTANT_TAB_ARIA_CONTROLS, MOST_IMPORTANT_INFO_TAB_LABEL);
        sdWizardPage = issueDetailsPage.selectMessagesTab().createNewNotificationOnMessagesTab();
        sdWizardPage.clickButton(NOTIFICATION_WIZARD_CANCEL_ID);
        issueDetailsPage.selectRelatedTicketsTab();
        Assert.assertTrue(issueDetailsPage.checkIfTableExists(RELATED_TICKETS_TABLE_ID));
        issueDetailsPage.selectTabFromTablesWindow(SAME_MO_TT_TAB_ARIA_CONTROLS, SAME_MO_TT_TABLE_ID); // DO SPR TO DRUGIE
        Assert.assertTrue(issueDetailsPage.checkIfTableExists(SAME_MO_TT_TABLE_ID));
        issueDetailsPage.selectParticipantsTab();
        Assert.assertTrue(issueDetailsPage.checkIfTableExists(PARTICIPANTS_TABLE_ID));
        issueDetailsPage.selectRootCauseTab();
        Assert.assertTrue(issueDetailsPage.checkIfTableExists(ROOT_CAUSES_TABLE_ID));
        Assert.assertTrue(issueDetailsPage.checkIfTableExists(ROOT_CAUSES_TREE_TABLE_ID));
    }
}