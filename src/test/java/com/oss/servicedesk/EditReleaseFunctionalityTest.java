package com.oss.servicedesk;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.servicedesk.issue.tabs.ParticipantsTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedProblemsTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedTicketsTab;
import com.oss.pages.servicedesk.issue.tabs.RootCausesTab;
import com.oss.pages.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.servicedesk.issue.ticket.TicketOverviewTab;
import com.oss.pages.servicedesk.issue.ticket.TicketSearchPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;

@Listeners({TestListener.class})
public class EditReleaseFunctionalityTest extends BaseTestCase {

    private TicketDashboardPage ticketDashboardPage;
    private TicketSearchPage ticketSearchPage;
    private SDWizardPage sdWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private TicketOverviewTab ticketOverviewTab;
    private RootCausesTab rootCausesTab;
    private RelatedTicketsTab relatedTicketsTab;
    private ParticipantsTab participantsTab;
    private RelatedProblemsTab relatedProblemsTab;
    private String ticketID;

    @Parameters({"MOIdentifier", "ttAssignee"})
    @Test(priority = 1, testName = "Create CTT Ticket", description = "Create CTT Ticket")
    @Description("Create CTT Ticket")
    public void createCTTTicket(
            @Optional("CFS_Access_Product_Selenium_1") String MOIdentifier,
            @Optional("ca_kodrobinska") String ttAssignee
    ) {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
        sdWizardPage = ticketDashboardPage.openCreateTicketWizard("CTT");
        sdWizardPage.createTicket(MOIdentifier, ttAssignee);
        ticketID = ticketDashboardPage.getIdFromMessage();
        Assert.assertTrue(ticketDashboardPage.getAssigneeForNthTicketInTable(0).contains(ttAssignee));
    }

    @Test(priority = 2, testName = "Checklist - edit mode", description = "Edit mode - check if Skip buttons are available")
    @Description("Edit mode - check if Skip buttons are available")
    public void checklistOnEditMode() {
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait).openView(driver, BASIC_URL);
        ticketSearchPage.filterByTextField(ID_ATTRIBUTE, ticketID);
        issueDetailsPage = ticketSearchPage.openIssueDetailsViewFromSearchPage("0", BASIC_URL);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.allowEditingTicket();

        Assert.assertTrue(issueDetailsPage.areSkipButtonsActive());
    }

    @Test(priority = 3, testName = "Related Tickets - edit mode", description = "Edit mode - check if Link Ticket button is available")
    @Description("Edit mode - check if Link Ticket button is available")
    public void relatedTicketsOnEditMode() {
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        Assert.assertTrue(relatedTicketsTab.isLinkIssueButtonActive());
    }

    @Test(priority = 4, testName = "Root Cause - edit mode", description = "Edit mode - check if Add Root Cause button is available")
    @Description("Edit mode - check if Add Root Cause button is available")
    public void rootCauseOnEditMode() {
        rootCausesTab = issueDetailsPage.selectRootCauseTab();
        Assert.assertTrue(rootCausesTab.isAddRootCauseButtonActive());
    }

    @Test(priority = 5, testName = "Participant - edit mode", description = "Edit mode - check if Add Participant button is available")
    @Description("Edit mode - check if Add Participant button is available")
    public void participantOnEditMode() {
        participantsTab = issueDetailsPage.selectParticipantsTab();
        Assert.assertTrue(participantsTab.isAddParticipantButtonActive());
    }

    @Test(priority = 6, testName = "Related Problem - edit mode", description = "Edit mode - check if Create Problem button is available")
    @Description("Edit mode - check if Create Problem button is available")
    public void relatedProblemOnEditMode() {
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        Assert.assertTrue(relatedProblemsTab.isCreateProblemButtonActive());
    }

    @Test(priority = 7, testName = "Checklist - read only mode", description = "Read only mode - check if Skip buttons are not available")
    @Description("Read only mode - check if Skip buttons are available")
    public void checklistOnReadOnlyMode() {
        ticketOverviewTab.releaseTicket();

        Assert.assertFalse(issueDetailsPage.areSkipButtonsActive());
    }

    @Test(priority = 8, testName = "Related Ticket - read only mode", description = "Read only mode - check if Link Ticket button is not available")
    @Description("Read only mode - check if Link Ticket button is available")
    public void relatedTicketsOnReadOnlyMode() {
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        Assert.assertFalse(relatedTicketsTab.isLinkIssueButtonActive());
    }

    @Test(priority = 9, testName = "Root Cause - read only mode", description = "Read only mode - check if Add Root Cause button is not available")
    @Description("Read only mode - check if Add Root Cause button is available")
    public void rootCauseOnReadOnlyMode() {
        rootCausesTab = issueDetailsPage.selectRootCauseTab();
        Assert.assertFalse(rootCausesTab.isAddRootCauseButtonActive());
    }

    @Test(priority = 10, testName = "Participant - read only mode", description = "Read only mode - check if Add Participant button is not available")
    @Description("Read only mode - check if Add Participant button is available")
    public void participantOnReadOnlyMode() {
        participantsTab = issueDetailsPage.selectParticipantsTab();
        Assert.assertFalse(participantsTab.isAddParticipantButtonActive());
    }

    @Test(priority = 11, testName = "Related Problem - read ony mode", description = "Read only mode - check if Create Problem button is not available")
    @Description("Read only mode - check if Create Problem button is available")
    public void relatedProblemOnReadOnlyMode() {
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        Assert.assertFalse(relatedProblemsTab.isCreateProblemButtonActive());
    }
}
