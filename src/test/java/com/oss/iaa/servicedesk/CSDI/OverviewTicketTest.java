package com.oss.iaa.servicedesk.CSDI;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.MoreDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.RemainderForm;
import com.oss.pages.iaa.servicedesk.issue.tabs.AttachmentsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ExternalTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ImprovementTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.MessagesTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ParticipantsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RelatedTicketsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ResolutionTab;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketDashboardPage;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketOverviewTab;
import com.oss.pages.iaa.servicedesk.issue.wizard.ExternalPromptPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.IssueCSDIWizardPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DESCRIPTION_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DETAILS_TABS_CONTAINER_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DICTIONARIES_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.FILE_TO_UPLOAD_PATH;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.INTERNAL_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.MOST_IMPORTANT_INFO_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TYPE_COMMENT;

public class OverviewTicketTest extends BaseTestCase {

    private static final String ISSUE_TITLE = "Selenium Test";
    private static final String SEVERITY = "Warning";
    private static final String STEPS_TO_REPRODUCE = "Test Steps";
    private static final String EXPECTED_BEHAVIOR = "Test Behavior";
    private static final String CONTACT_PERSON = "Test Person";
    private static final String IMPACT = "Low";
    private static final String IMPACT_EXPLANATION = "Test Explanation";
    private static final String URGENCY = "Low";
    private static final String URGENCY_EXPLANATION = "Test Explanation";
    private static final String ENVIRONMENT_TYPE = "REF";
    private static final String COMPONENT = "Service Desk";
    private static final String FLOW_TYPE = "Incident";
    private static final String INCIDENT_DESCRIPTION = "Selenium test ticket";
    private static final String SEVERITY_ATTRIBUTE_NAME = "Severity";
    private static final String STATUS_RESOLVED = "Resolved";
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
    private static final String DICTIONARIES_TAB_ARIA_CONTROLS = "_dictionariesTab";
    private static final String MOST_IMPORTANT_INFO_TAB_ARIA_CONTROLS = "_mostImportantTab";
    private static final String TT_EXTERNAL = "Selenium test external";
    private static final String TT_EXTERNAL_URL = "http://test.pl";
    private static final String EXTERNAL_LIST_ID = "_detailsExternalsListApp";
    private static final String RESOLUTION_NOTE = "resolution-test_Selenium";
    private static final String IMPROVEMENT_NOTE = "improvement-test_Selenium";
    private static final String ADD_TO_LIBRARY_LABEL = "Add to Library";
    private static final String ADD_TO_LIBRARY_WIZARD_ID = "addToLibraryPrompt_prompt-card";
    private static final String WIZARD_LIBRARY_TYPE_ID = "{\"identifier\":\"type\",\"parentIdentifier\":\"type\",\"parentValueIdentifier\":\"\",\"groupId\":\"type\"}";
    private static final String WIZARD_CATEGORY_ID = "{\"identifier\":\"RootCauseCategory\",\"parentIdentifier\":\"type\",\"parentValueIdentifier\":\"\",\"groupId\":\"RootCauseCategory\"}";
    private static final String TT_LIBRARY_TYPE = "Root Cause Category";
    private static final String TT_CATEGORY_NAME = "Comarch application error";

    private static final String PARTICIPANT_FIRST_NAME = "SeleniumTest";
    private static final String PARTICIPANT_SURNAME = "Selenium";
    private static final String PARTICIPANT_ROLE = "Contact";
    private static final String REMAINDER_NOTE = "Selenium Description Note";

    private TicketDashboardPage ticketDashboardPage;
    private IssueCSDIWizardPage issueCSDIWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private TicketOverviewTab ticketOverviewTab;
    private String ticketID;
    private SDWizardPage sdWizardPage;
    private MessagesTab messagesTab;
    private MessagesTab mostImportantInfoTab;
    private ExternalTab externalTab;
    private ExternalPromptPage externalPromptPage;
    private ResolutionTab resolutionTab;
    private ImprovementTab improvementTab;
    private AttachmentsTab attachmentsTab;
    private RelatedTicketsTab relatedTicketsTab;
    private ParticipantsTab participantsTab;
    private RemainderForm remainderForm;
    private MoreDetailsPage moreDetailsPage;

    @BeforeMethod
    public void goToTicketDashboardPage() {
        ticketDashboardPage = new TicketDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Create Incident Ticket", description = "Create Incident Ticket")
    @Description("Create Incident Ticket")
    public void createIncidentTicket(
    ) {
        issueCSDIWizardPage = ticketDashboardPage.openCreateTicketWizard(FLOW_TYPE).openIssueCSDIWizardPage();
        issueCSDIWizardPage.setIssueTitle(ISSUE_TITLE);
        issueCSDIWizardPage.enterDescription(INCIDENT_DESCRIPTION);
        issueCSDIWizardPage.setSeverity(SEVERITY);
        issueCSDIWizardPage.clickNextButtonInWizard();

        issueCSDIWizardPage.setStepsToReproduce(STEPS_TO_REPRODUCE);
        issueCSDIWizardPage.setExpectedBehavior(EXPECTED_BEHAVIOR);
        issueCSDIWizardPage.setContactPerson(CONTACT_PERSON);

        issueCSDIWizardPage.setImpact(IMPACT);
        issueCSDIWizardPage.setImpactExplanation(IMPACT_EXPLANATION);
        issueCSDIWizardPage.setUrgency(URGENCY);
        issueCSDIWizardPage.setUrgencyExplanation(URGENCY_EXPLANATION);
        issueCSDIWizardPage.setEnvironmentType(ENVIRONMENT_TYPE);
        issueCSDIWizardPage.setComponent(COMPONENT);
        issueCSDIWizardPage.clickAcceptButtonInWizard();

        ticketID = ticketDashboardPage.getIdFromMessage();
        Assert.assertTrue(ticketDashboardPage.getAttributeFromTable(0, SEVERITY_ATTRIBUTE_NAME).contains(SEVERITY));
        Assert.assertEquals(ticketDashboardPage.getRowForIssueWithID(ticketID), 0);
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

    @Test(priority = 5, testName = "Add dictionary to ticket", description = "Add dictionary to ticket")
    @Description("Add dictionary to ticket")
    public void addDictionaryToTicket() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTabFromDetailsWindow(DICTIONARIES_TAB_ARIA_CONTROLS, DICTIONARIES_TAB_LABEL);
        issueDetailsPage.clickContextAction(ADD_TO_LIBRARY_LABEL);

        SDWizardPage dictionarySDWizardPage = new SDWizardPage(driver, webDriverWait, ADD_TO_LIBRARY_WIZARD_ID);
        dictionarySDWizardPage.insertValueToComponent(TT_LIBRARY_TYPE, WIZARD_LIBRARY_TYPE_ID);
        dictionarySDWizardPage.insertValueToComponent(TT_CATEGORY_NAME, WIZARD_CATEGORY_ID);
        dictionarySDWizardPage.clickAcceptButtonInWizard();

        Assert.assertEquals(issueDetailsPage.checkExistingDictionary(), TT_CATEGORY_NAME);
    }

    @Test(priority = 6, testName = "Add attachment to ticket", description = "Add attachment to ticket")
    @Description("Add attachment to ticket")
    public void addAttachment() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(DETAILS_TABS_CONTAINER_ID);

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.addAttachment(FILE_TO_UPLOAD_PATH);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
    }

    @Test(priority = 7, testName = "Skip checklist actions and change status", description = "Skip checklist actions and change status")
    @Description("Skip checklist actions and change status")
    public void changeTicketStatus() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        ticketOverviewTab = (TicketOverviewTab) issueDetailsPage.selectOverviewTab(TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.skipAllActionsOnCheckList();

        ticketOverviewTab.changeIssueStatus(STATUS_RESOLVED);
        ticketOverviewTab.fillReasonChange(webDriverWait, driver);
        DelayUtils.sleep(1000);
        Assert.assertEquals(ticketOverviewTab.checkTicketStatus(), STATUS_RESOLVED);
    }

    @Test(priority = 8, testName = "Edit Ticket Details", description = "Edit Ticket Details and check if change is visible in Description tab")
    @Description("Edit Ticket Details and check if change is visible in Description tab")
    public void editTicketDetails() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        sdWizardPage = issueDetailsPage.clickEditDetails();
        sdWizardPage.enterDescription(TT_DESCRIPTION_EDITED);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();

        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        issueDetailsPage.selectTabFromTablesWindow(DESCRIPTION_TAB_ARIA_CONTROLS, DESCRIPTION_TAB_LABEL);
        Assert.assertEquals(issueDetailsPage.getDisplayedText(TABLES_WINDOW_ID, DESCRIPTION_FIELD_ID), TT_DESCRIPTION_EDITED);
    }

    @Test(priority = 9, testName = "Check Messages Tab - add Internal Notification", description = "Check Messages Tab - add Internal Notification")
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

    @Test(priority = 10, testName = "Check Messages Tab - Add Internal Comment", description = "Check Messages Tab - Add Internal Comment")
    @Description("Check Messages Tab - Add Internal Comment")
    public void addInternalComment() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.clickCreateNewCommentButton();
        messagesTab.enterCommentMessage(NOTIFICATION_MESSAGE_COMMENT);
        messagesTab.selectCommentType(INTERNAL_TYPE);
        messagesTab.clickCreateCommentButton();

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.checkMessageType(0), TYPE_COMMENT);
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_MESSAGE_COMMENT);
        Assert.assertEquals(messagesTab.checkCommentType(0), INTERNAL_TYPE.toLowerCase());
    }

    @Test(priority = 11, testName = "Most Important Info test", description = "add comment as important and check if it is displayed in most important info tab")
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
    @Test(priority = 12, testName = "Check Related Tickets Tab - link Ticket", description = "Check Related Tickets Tab - link Ticket")
    @Description("Check Related Tickets Tab - link Ticket")
    public void linkTicketToTicket(@Optional("397") String RelatedTicketID) {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.linkIssue(RelatedTicketID, "issueIdsToLink");

        Assert.assertEquals(relatedTicketsTab.checkRelatedIssueId(0), RelatedTicketID);
    }

    @Test(priority = 13, testName = "Check Related Tickets Tab - unlink Ticket", description = "Check Related Tickets Tab - unlink Ticket")
    @Description("Check Related Tickets Tab - unlink Ticket")
    public void unlinkTicketFromTicket() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        relatedTicketsTab = issueDetailsPage.selectRelatedTicketsTab();
        relatedTicketsTab.selectIssue(0);
        relatedTicketsTab.unlinkIssue();
        relatedTicketsTab.confirmUnlinking();

        Assert.assertTrue(relatedTicketsTab.isRelatedIssueTableEmpty());
    }

    @Test(priority = 14, testName = "Check Participants", description = "Check Participants Tab - add Participant")
    @Description("Check Participants Tab - add Participant")
    public void addParticipant() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        participantsTab = issueDetailsPage.selectParticipantsTab();
        participantsTab.createParticipantAndLinkToIssue(PARTICIPANT_FIRST_NAME, PARTICIPANT_SURNAME, PARTICIPANT_ROLE);
        int newParticipantRow = participantsTab.participantRow(PARTICIPANT_FIRST_NAME);

        Assert.assertEquals(participantsTab.checkParticipantFirstName(newParticipantRow), PARTICIPANT_FIRST_NAME);
        Assert.assertEquals(participantsTab.checkParticipantSurname(newParticipantRow), PARTICIPANT_SURNAME);
        Assert.assertEquals(participantsTab.checkParticipantRole(newParticipantRow), PARTICIPANT_ROLE.toUpperCase());
    }

    @Test(priority = 15, testName = "Remove Participant", description = "Remove Edited Participant")
    @Description("Remove Edited Participant")
    public void removeParticipant() {
        issueDetailsPage = ticketDashboardPage.openIssueDetailsView(ticketID, BASIC_URL, TROUBLE_TICKET_ISSUE_TYPE);
        participantsTab = issueDetailsPage.selectParticipantsTab();
        int participantsInTable = participantsTab.countParticipantsInTable();
        participantsTab.selectParticipant(participantsTab.participantRow(PARTICIPANT_FIRST_NAME));
        participantsTab.clickRemoveParticipant();
        participantsTab.clickConfirmRemove();

        Assert.assertEquals(participantsTab.countParticipantsInTable(), participantsInTable - 1);
    }

    @Test(priority = 16, testName = "Add Remainder", description = "Add Remainder")
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
}
