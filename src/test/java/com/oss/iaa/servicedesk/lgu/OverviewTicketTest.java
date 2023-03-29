package com.oss.iaa.servicedesk.lgu;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.faultmanagement.FMSMDashboardPage;
import com.oss.pages.iaa.faultmanagement.WAMVPage;
import com.oss.pages.iaa.faultmanagement.wamv.Area3Page;
import com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.MoreDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.RemainderForm;
import com.oss.pages.iaa.servicedesk.issue.tabs.AttachmentsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ExternalTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ImprovementTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.MessagesTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RelatedProblemsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RelatedTicketsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ResolutionTab;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketOverviewTab;
import com.oss.pages.iaa.servicedesk.issue.wizard.ExternalPromptPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DESCRIPTION_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DETAILS_TABS_CONTAINER_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.FILE_TO_UPLOAD_PATH;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.INTERNAL_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.MOST_IMPORTANT_INFO_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;

public class OverviewTicketTest extends BaseTestCase {

    private static final String ALARM_MANAGEMENT_VIEW_ID = "_UserViewsListALARM_MANAGEMENT";
    private static final String STATUS_CLOSED = "Closed";
    private static final String DESCRIPTION_TAB_ARIA_CONTROLS = "_descriptionTab";
    private static final String TABLES_WINDOW_ID = "_tablesWindow";
    private static final String DESCRIPTION_FIELD_ID = "TEXT_FIELD_COMPONENT-_incidentDescriptionApp";
    private static final String TT_DESCRIPTION_EDITED = "TestSelenium_edited";
    private static final String NOTIFICATION_CHANNEL_INTERNAL = "Internal";
    private static final String NOTIFICATION_MESSAGE_INTERNAL = "test selenium message internal";
    private static final String NOTIFICATION_MESSAGE_COMMENT = "test selenium message comment";
    private static final String NOTIFICATION_MESSAGE_COMMENT_IMPORTANT = "Selenium message - Most Important";
    private static final String NOTIFICATION_INTERNAL_TO = "sd_seleniumtest";
    private static final String NOTIFICATION_TYPE = "Success";
    private static final String NOTIFICATION_WIZARD_CHANNEL_ID = "channel-component";
    private static final String NOTIFICATION_WIZARD_MESSAGE_ID = "message-component";
    private static final String NOTIFICATION_WIZARD_INTERNAL_TO_ID = "internal-to-component";
    private static final String NOTIFICATION_WIZARD_TYPE_ID = "internal-type-component";
    private static final String NOTIFICATION_WIZARD_TEMPLATE_ID = "template-component";
    private static final String MOST_IMPORTANT_INFO_TAB_ARIA_CONTROLS = "_mostImportantTab";
    private static final String TT_EXTERNAL = "Selenium test external";
    private static final String TT_EXTERNAL_URL = "http://test.pl";
    private static final String EXTERNAL_LIST_ID = "_detailsExternalsListApp";
    private static final String RESOLUTION_NOTE = "resolution-test_Selenium";
    private static final String IMPROVEMENT_NOTE = "improvement-test_Selenium";
    private static final String SAME_MO_TT_TAB_ARIA_CONTROLS = "_sameMOTTTab";
    private static final String SAME_MO_TT_TAB_LABEL = "Same MO TT";
    private static final String REMAINDER_NOTE = "Selenium Description Note";

    private WAMVPage wamvPage;
    private Area3Page area3Page;
    private String ticketID;
    private FMSMDashboardPage fmsmDashboardPage;
    private TicketDashboardPage ticketDashboardPage;
    private IssueDetailsPage issueDetailsPage;
    private TicketOverviewTab ticketOverviewTab;
    private SDWizardPage sdWizardPage;
    private MessagesTab messagesTab;
    private MessagesTab mostImportantInfoTab;
    private ExternalTab externalTab;
    private ExternalPromptPage externalPromptPage;
    private ResolutionTab resolutionTab;
    private ImprovementTab improvementTab;
    private AttachmentsTab attachmentsTab;
    private RelatedProblemsTab relatedProblemsTab;
    private RemainderForm remainderForm;
    private MoreDetailsPage moreDetailsPage;
    private RelatedTicketsTab relatedTicketsTab;

    @BeforeMethod
    public void goToTicketDashboardPage() {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Parameters({"alarmListRowTroubleTicket", "alarmListName", "chosenDashboard", "alarmListRowTroubleTicket"})
    @Test(priority = 1, testName = "Check Trouble Tickets Creation", description = "Check Trouble Tickets Creation")
    @Description("Check Trouble Tickets Creation")
    public void openWAMVAndCheckTroubleTicketCreation(
            @Optional("0") int alarmListRowTroubleTicket,
            @Optional("FaultManagement") String chosenDashboard,
            @Optional("WAMV_RefID_MK") String alarmListName,
            @Optional("0") int alarmManagementViewRow
    ) {
        fmsmDashboardPage = FMSMDashboardPage.goToPage(driver, BASIC_URL, chosenDashboard);
        wamvPage = searchAndOpenWamv(alarmListName, alarmManagementViewRow);
        area3Page = new Area3Page(driver);

        wamvPage.selectSpecificRow(alarmListRowTroubleTicket);
        wamvPage.createTroubleTicket();
        ticketID = wamvPage.getIdFromMessage();
        area3Page.clickOnTroubleTicketsTab();
        Assert.assertEquals(area3Page.getFirstTroubleTicketIdFromTTTab(), ticketID);
        area3Page.selectSpecificRowFromTTTabTable(0);
        area3Page.clickOpenTTDetailsButton();
        Assert.assertTrue(wamvPage.checkPageTitleInNewTab(ticketID));
    }

    @Test(priority = 2, testName = "Add External to ticket", description = "Add External to ticket")
    @Description("Add External to ticket")
    public void addExternalToTicket() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.allowEditingTicket();

        externalTab = issueDetailsPage.selectExternalTab();
        externalPromptPage = externalTab.clickAddExternal();
        externalPromptPage.fillExternalName(TT_EXTERNAL);
        externalPromptPage.fillExternalUrl(TT_EXTERNAL_URL);
        externalPromptPage.clickCreateExternal();

        Assert.assertTrue(externalTab.isExternalCreated(TT_EXTERNAL, EXTERNAL_LIST_ID));
    }

    @Test(priority = 3, testName = "Add Resolution", description = "Add Resolution")
    @Description("Add Resolution in Resolution tab")
    public void addResolution() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        resolutionTab = issueDetailsPage.selectResolutionTab();
        resolutionTab.addTextNote(RESOLUTION_NOTE);

        Assert.assertEquals(resolutionTab.getTextMessage(), RESOLUTION_NOTE);
    }

    @Test(priority = 4, testName = "Add Improvement", description = "Add Improvement")
    @Description("Add Improvement in Improvement tab")
    public void addImprovement() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        improvementTab = issueDetailsPage.selectImprovementTab();
        improvementTab.addTextNote(IMPROVEMENT_NOTE);

        Assert.assertEquals(improvementTab.getTextMessage(), IMPROVEMENT_NOTE);
    }

    @Test(priority = 5, testName = "Add attachment to ticket", description = "Add attachment to ticket")
    @Description("Add attachment to ticket")
    public void addAttachment() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(DETAILS_TABS_CONTAINER_ID);

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.addAttachment(FILE_TO_UPLOAD_PATH);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
    }

    @Test(priority = 6, testName = "Check ticket status", description = "Check ticket status")
    @Description("Check ticket status")
    public void checkTicketStatus() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);

        Assert.assertEquals(ticketOverviewTab.checkTicketStatus(), STATUS_CLOSED);
    }

    @Test(priority = 7, testName = "Check if Problem was created automatically", description = "Check if Problem was created automatically")
    @Description("Check if Problem was created automatically")
    public void checkRelatedProblemsTab() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        Assert.assertFalse(relatedProblemsTab.isRelatedProblemsTabEmpty());
    }

    @Test(priority = 7, testName = "Edit Ticket Details", description = "Edit Ticket Details and check if change is visible in Description tab")
    @Description("Edit Ticket Details and check if change is visible in Description tab")
    public void editTicketDetails() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        sdWizardPage = issueDetailsPage.clickEditDetails();
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.enterDescription(TT_DESCRIPTION_EDITED);
        sdWizardPage.clickAcceptButtonInWizard();

        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTabFromTablesWindow(DESCRIPTION_TAB_ARIA_CONTROLS, DESCRIPTION_TAB_LABEL);
        Assert.assertEquals(issueDetailsPage.getDisplayedText(TABLES_WINDOW_ID, DESCRIPTION_FIELD_ID), TT_DESCRIPTION_EDITED);
    }

    @Test(priority = 8, testName = "Check Messages Tab - add Internal Notification", description = "Check Messages Tab - add Internal Notification")
    @Description("Check Messages Tab - add Internal Notification")
    public void addInternalNotification() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        messagesTab = issueDetailsPage.selectMessagesTab();

        sdWizardPage = messagesTab.createNewNotificationOnMessagesTab();
        sdWizardPage.insertValueToComponent(NOTIFICATION_CHANNEL_INTERNAL, NOTIFICATION_WIZARD_CHANNEL_ID);
        sdWizardPage.insertValueToComponent(NOTIFICATION_MESSAGE_INTERNAL, NOTIFICATION_WIZARD_MESSAGE_ID);
        sdWizardPage.insertValueContainsToComponent(NOTIFICATION_INTERNAL_TO, NOTIFICATION_WIZARD_INTERNAL_TO_ID);
        sdWizardPage.clickComboBox(NOTIFICATION_WIZARD_TEMPLATE_ID);
        sdWizardPage.insertValueToComponent(NOTIFICATION_TYPE, NOTIFICATION_WIZARD_TYPE_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_INTERNAL);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
    }

    @Test(priority = 9, testName = "Check Messages Tab - Add Internal Comment", description = "Check Messages Tab - Add Internal Comment")
    @Description("Check Messages Tab - Add Internal Comment")
    public void addInternalComment() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.clickCreateNewCommentButton();
        messagesTab.enterCommentMessage(NOTIFICATION_MESSAGE_COMMENT);
        messagesTab.selectCommentType(INTERNAL_TYPE);
        messagesTab.clickCreateCommentButton();

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_COMMENT);
    }

    @Test(priority = 10, testName = "Most Important Info test", description = "add comment as important and check if it is displayed in most important info tab")
    @Description("add comment as important and check if it is displayed in most important info tab")
    public void mostImportantInfoTest() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.addComment(NOTIFICATION_MESSAGE_COMMENT_IMPORTANT, INTERNAL_TYPE);
        messagesTab.markAsImportant(0);
        Assert.assertEquals(messagesTab.getBadgeTextFromMessage(0, 1), "IMPORTANT");

        issueDetailsPage.selectTabFromTablesWindow(MOST_IMPORTANT_INFO_TAB_ARIA_CONTROLS, MOST_IMPORTANT_INFO_TAB_LABEL);
        mostImportantInfoTab = new MessagesTab(driver, webDriverWait);

        Assert.assertFalse(mostImportantInfoTab.isMessagesTabEmpty());
        Assert.assertEquals(mostImportantInfoTab.getMessageText(0), NOTIFICATION_MESSAGE_COMMENT_IMPORTANT);
        Assert.assertEquals(mostImportantInfoTab.getBadgeTextFromMessage(0, 1), "IMPORTANT");
    }

    @Parameters({"RelatedTicketID"})
    @Test(priority = 11, testName = "Check Related Tickets Tab - link Ticket", description = "Check Related Tickets Tab - link Ticket")
    @Description("Check Related Tickets Tab - link Ticket")
    public void linkTicketToTicket(@Optional("32881") String RelatedTicketID) {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.linkIssue(RelatedTicketID, "issueIdsToLink");

        Assert.assertEquals(relatedTicketsTab.checkRelatedIssueId(0), RelatedTicketID);
    }

    @Test(priority = 12, testName = "Check Related Tickets Tab - unlink Ticket", description = "Check Related Tickets Tab - unlink Ticket")
    @Description("Check Related Tickets Tab - unlink Ticket")
    public void unlinkTicketFromTicket() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.selectIssue(0);
        relatedTicketsTab.unlinkIssue();
        relatedTicketsTab.confirmUnlinking();

        Assert.assertTrue(relatedTicketsTab.isRelatedIssueTableEmpty());
    }

    @Test(priority = 13, testName = "Add Remainder", description = "Add Remainder")
    @Description("Add Remainder")
    public void addRemainderTest() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        remainderForm = ticketOverviewTab.clickAddRemainder();
        remainderForm.createReminderWithNote(REMAINDER_NOTE);
        moreDetailsPage = ticketOverviewTab.clickMoreDetails();
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);

        Assert.assertTrue(ticketDashboardPage.isReminderPresent(ticketDashboardPage.getRowForIssueWithID(ticketID), REMAINDER_NOTE));
    }

    @Test(priority = 14, testName = "Check Same MO TT Tab", description = "Check Same MO TT Tab")
    @Description("Check Same MO TT Tab")
    public void checkSameMOTTTab() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTabFromTablesWindow(SAME_MO_TT_TAB_ARIA_CONTROLS, SAME_MO_TT_TAB_LABEL);
        Assert.assertTrue(issueDetailsPage.checkIfSameMOTTTableIsNotEmpty());
    }

    private WAMVPage searchAndOpenWamv(String alarmListName, int alarmManagementViewRow) {
        fmsmDashboardPage.searchInView(ALARM_MANAGEMENT_VIEW_ID, alarmListName);
        return fmsmDashboardPage.openSelectedView(ALARM_MANAGEMENT_VIEW_ID, alarmManagementViewRow);
    }
}
