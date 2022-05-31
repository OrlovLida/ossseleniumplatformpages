package com.oss.servicedesk.changemanagement;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.changemanagement.ChangeDashboardPage;
import com.oss.pages.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.servicedesk.issue.MoreDetailsPage;
import com.oss.pages.servicedesk.issue.tabs.AffectedTab;
import com.oss.pages.servicedesk.issue.tabs.AttachmentsTab;
import com.oss.pages.servicedesk.issue.tabs.DescriptionTab;
import com.oss.pages.servicedesk.issue.tabs.MessagesTab;
import com.oss.pages.servicedesk.issue.tabs.OverviewTab;
import com.oss.pages.servicedesk.issue.tabs.ParticipantsTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedChangesTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedProblemsTab;
import com.oss.pages.servicedesk.issue.tabs.RolesTab;
import com.oss.pages.servicedesk.issue.tabs.RootCausesTab;
import com.oss.pages.servicedesk.issue.tabs.SummaryTab;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.CHANGE_ISSUE_TYPE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.COMBOBOX_LINK_PROBLEM_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.CSV_FILE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.DETAILS_TABS_CONTAINER_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.FILE_TO_UPLOAD_PATH;
import static com.oss.pages.servicedesk.ServiceDeskConstants.USER_NAME;

public class ChangesTest extends BaseTestCase {

    private ChangeDashboardPage changeDashboardPage;
    private SDWizardPage sdWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private MoreDetailsPage moreDetailsPage;
    private OverviewTab changeOverviewTab;
    private MessagesTab messagesTab;
    private RelatedChangesTab relatedChangesTab;
    private RootCausesTab rootCausesTab;
    private RelatedProblemsTab relatedProblemsTab;
    private RolesTab rolesTab;
    private ParticipantsTab participantsTab;
    private AttachmentsTab attachmentsTab;
    private DescriptionTab descriptionTab;
    private AffectedTab affectedTab;
    private SummaryTab summaryTab;
    private String changeID;

    private static final String RISK_ASSESSMENT_ID = "TT_WIZARD_INPUT_RISK_ASSESSMENT_LABEL";
    private static final String RISK = "LOW";
    private static final String REQUESTER_ID = "TT_WIZARD_INPUT_REQUESTER_LABEL";
    private static final String ASSIGNEE_ID = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private static final String INCIDENT_DESCRIPTION_ID = "TT_WIZARD_INPUT_INCIDENT_DESCRIPTION_LABEL";
    private static final String INCIDENT_DESCRIPTION_TXT = "Selenium Incident Description";
    private static final String INCIDENT_DESCRIPTION_TXT_EDITED = "Selenium Incident Description Edited";
    private static final String INCIDENT_DESCRIPTION_TXT_MODIFY = "Selenium Incident Description Modified in description Tab";
    private static final String NOTIFICATION_INTERNAL_MSG = "Test Selenium Internal Message";
    private static final String NOTIFICATION_EMAIL_MSG = "Test Selenium Email Message";
    private static final String INTERNAL_COMMENT_MSG = "Test Selenium Message Comment in Change";
    private static final String PARTICIPANT_FIRST_NAME = "SeleniumTest";
    private static final String PARTICIPANT_FIRST_NAME_EDITED = "SeleniumTestEdited";
    private static final String PARTICIPANT_SURNAME = LocalDateTime.now().toString();
    private static final String PARTICIPANT_ROLE = "Contact";
    private static final String TABS_WIDGET_ID = "_tablesWindow";
    private static final String COMBOBOX_LINK_CHANGE_ID = "linkChange";
    private static final String RISK_ATTRIBUTE = "Risk assessment";
    private static final String CHANGE_CREATED_LOG = "ChangeRequest has been created";
    private static final String SUMMARY_TEXT = "Test Selenium Summary Note";

    @BeforeMethod
    public void goToChangeDashboardOrIssue(Method method) {
        changeDashboardPage = new ChangeDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
        if (changeID != null) {
            issueDetailsPage = changeDashboardPage.openIssueDetailsView(changeID, BASIC_URL, CHANGE_ISSUE_TYPE);
        } else if (!method.getName().equals("createChange")) {
            Assert.fail("Change has not been created.");
        }
    }

    @Parameters({"userName"})
    @Test(priority = 1, testName = "Create Change", description = " Create Change")
    @Description("Create Change")
    public void createChange(
            @Optional("sd_seleniumtest") String userName
    ) {
        sdWizardPage = changeDashboardPage.openCreateChangeWizard();
        sdWizardPage.insertValueToTextComponent(RISK, RISK_ASSESSMENT_ID);
        sdWizardPage.insertValueToSearchComponent(userName, REQUESTER_ID);
        sdWizardPage.insertValueToSearchComponent(userName, ASSIGNEE_ID);
        sdWizardPage.insertValueToTextAreaComponent(INCIDENT_DESCRIPTION_TXT, INCIDENT_DESCRIPTION_ID);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        changeID = changeDashboardPage.getIdFromMessage();
        Assert.assertEquals(changeDashboardPage.getRequesterFromNthRow(0), userName);
        Assert.assertEquals(changeDashboardPage.getRowForIssueWithID(changeID), 0);
    }

    @Test(priority = 2, testName = "Check Attributes", description = "Check Attributes")
    @Description("Check Attributes")
    public void checkAttributes() {
        changeOverviewTab = issueDetailsPage.selectOverviewTab(CHANGE_ISSUE_TYPE);
        changeOverviewTab.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        moreDetailsPage = changeOverviewTab.clickMoreDetails();
        Assert.assertEquals(moreDetailsPage.checkValueOfAttribute(RISK_ATTRIBUTE), RISK);
    }

    @Test(priority = 3, testName = "Check Logs", description = "Check Logs")
    @Description("Check Logs")
    public void checkLogs() {
        changeOverviewTab = issueDetailsPage.selectOverviewTab(CHANGE_ISSUE_TYPE);
        changeOverviewTab.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        moreDetailsPage = changeOverviewTab.clickMoreDetails();
        Assert.assertTrue(moreDetailsPage.isNoteInLogsTable(CHANGE_CREATED_LOG));
    }

    @Parameters({"newAssignee"})
    @Test(priority = 4, testName = "Edit Change", description = "Edit change - assignee and description")
    @Description("Edit change - assignee and description")
    public void editChange(
            @Optional("ca_kodrobinska") String newAssignee
    ) {
        changeOverviewTab = issueDetailsPage.selectOverviewTab(CHANGE_ISSUE_TYPE);
        sdWizardPage = changeOverviewTab.openEditIssueWizard();
        sdWizardPage.insertValueToSearchComponent(newAssignee, ASSIGNEE_ID);
        sdWizardPage.insertValueToTextAreaComponent(INCIDENT_DESCRIPTION_TXT_EDITED, INCIDENT_DESCRIPTION_ID);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        Assert.assertEquals(changeOverviewTab.checkAssignee(), newAssignee);
    }

    @Test(priority = 5, testName = "Check description", description = "Check change description on Description tab")
    @Description("Check change description on Description tab")
    public void checkDescription() {
        descriptionTab = issueDetailsPage.selectDescriptionTab();
        Assert.assertEquals(descriptionTab.getTextMessage(), INCIDENT_DESCRIPTION_TXT_EDITED);

        descriptionTab.addTextNote(INCIDENT_DESCRIPTION_TXT_MODIFY);
        Assert.assertEquals(descriptionTab.getTextMessage(), INCIDENT_DESCRIPTION_TXT_MODIFY);
    }

    @Parameters({"messageTo"})
    @Test(priority = 6, testName = "Add Internal Notification in Change", description = "Add Internal Notification in Change")
    @Description("Add Internal Notification in Change")
    public void addInternalNotification(
            @Optional("ca_kodrobinska") String messageTo
    ) {
        issueDetailsPage.maximizeWindow(TABS_WIDGET_ID);
        messagesTab = issueDetailsPage.selectMessagesTab();
        sdWizardPage = messagesTab.createNewNotificationOnMessagesTab();
        sdWizardPage.createInternalNotification(NOTIFICATION_INTERNAL_MSG, messageTo);

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_INTERNAL_MSG);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
        Assert.assertEquals(messagesTab.getBadgeTextFromMessage(0, 0), "New");
    }

    @Parameters({"notificationEmailTo", "notificationEmailFrom"})
    @Test(priority = 7, testName = "Add Email Notification in Change", description = "Add Email Notification in Change")
    @Description("Add Email Notification in Change")
    public void addEmailNotification(
            @Optional("kornelia.odrobinska@comarch.com") String notificationEmailTo,
            @Optional("switch.ticket@comarch.com") String notificationEmailFrom
    ) {
        issueDetailsPage.maximizeWindow(TABS_WIDGET_ID);
        messagesTab = issueDetailsPage.selectMessagesTab();
        sdWizardPage = messagesTab.createNewNotificationOnMessagesTab();
        sdWizardPage.createEmailNotification(notificationEmailTo, notificationEmailFrom, NOTIFICATION_EMAIL_MSG);

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_EMAIL_MSG);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
    }

    @Test(priority = 8, testName = "Add Internal Comment", description = "Add Internal Comment")
    @Description("Add Internal Comment")
    public void addInternalComment() {
        issueDetailsPage.maximizeWindow(TABS_WIDGET_ID);
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.addInternalComment(INTERNAL_COMMENT_MSG);

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.checkMessageType(0), "COMMENT");
        Assert.assertEquals(messagesTab.getMessageText(0), INTERNAL_COMMENT_MSG);
        Assert.assertEquals(messagesTab.checkCommentType(0), "internal");
    }

    @Parameters({"RelatedChangeID"})
    @Test(priority = 9, testName = "Check Related Changes Tab - link Change", description = "Check Related Changes Tab - link Change")
    @Description("Check Related Changes Tab - link Change")
    public void addRelatedChange(
            @Optional("35") String RelatedChangeID
    ) {
        relatedChangesTab = issueDetailsPage.selectRelatedChangesTab();
        relatedChangesTab.linkIssue(RelatedChangeID, COMBOBOX_LINK_CHANGE_ID);

        Assert.assertEquals(relatedChangesTab.checkRelatedIssueId(0), RelatedChangeID);
    }

    @Test(priority = 10, testName = "Export Related Changes", description = "Export Related Changes")
    @Description("Export Related Changes")
    public void exportRelatedChanges() {
        relatedChangesTab = issueDetailsPage.selectRelatedChangesTab();
        relatedChangesTab.exportFromTabTable();
        int amountOfNotifications = relatedChangesTab.openNotificationPanel().amountOfNotifications();

        Assert.assertEquals(amountOfNotifications, 0);
        Assert.assertTrue(relatedChangesTab.isRelatedIssuesFileNotEmpty());
    }

    @Test(priority = 11, testName = "Check Related Changes Tab - unlink Change", description = "Check Related Changes Tab - unlink Change")
    @Description("Check Related Changes Tab - unlink Change")
    public void unlinkChange() {
        relatedChangesTab = issueDetailsPage.selectRelatedChangesTab();
        relatedChangesTab.unlinkFirstIssue();

        Assert.assertTrue(relatedChangesTab.isRelatedIssueTableEmpty());
    }

    @Parameters({"SecondMOIdentifier"})
    @Test(priority = 12, testName = "Add second MO in Root Causes tab", description = "Add second MO in Root Causes tab")
    @Description("Add second MO in Root Causes tab")
    public void addRootCause(@Optional("TEST_MO") String SecondMOIdentifier) {
        rootCausesTab = issueDetailsPage.selectRootCauseTab();
        sdWizardPage = rootCausesTab.openAddRootCauseWizard();
        sdWizardPage.getMoStep().enterTextIntoSearchComponent(SecondMOIdentifier);
        sdWizardPage.getMoStep().selectObjectInMOTable(SecondMOIdentifier);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertTrue(rootCausesTab.checkIfMOIdentifierIsPresentOnRootCauses(SecondMOIdentifier));
    }

    @Parameters({"ProblemToLinkId"})
    @Test(priority = 13, testName = "Link Problem to Problem", description = "Link Problem to Problem")
    @Description("Link Problem to Problem")
    public void linkProblem(
            @Optional("35") String ProblemToLinkId
    ) {
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        relatedProblemsTab.linkIssue(ProblemToLinkId, COMBOBOX_LINK_PROBLEM_ID);

        Assert.assertEquals(relatedProblemsTab.checkRelatedIssueId(0), ProblemToLinkId);
    }

    @Test(priority = 14, testName = "Export from Related Problems tab", description = "Export from Related Problems tab")
    @Description("Export from Related Problems tab")
    public void exportFromRelatedProblemsTab() {
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        relatedProblemsTab.exportFromTabTable();
        int amountOfNotifications = relatedProblemsTab.openNotificationPanel().amountOfNotifications();

        Assert.assertEquals(amountOfNotifications, 0);
        Assert.assertTrue(relatedProblemsTab.isRelatedIssuesFileNotEmpty());
    }

    @Test(priority = 15, testName = "Unlink Problem from Problem", description = "Unlink Problem from Problem")
    @Description("Unlink Problem from Problem")
    public void unlinkProblem() {
        relatedProblemsTab = issueDetailsPage.selectRelatedProblemsTab();
        relatedProblemsTab.unlinkFirstIssue();

        Assert.assertTrue(relatedProblemsTab.isRelatedIssueTableEmpty());
    }

    @Parameters({"userForRoles"})
    @Test(priority = 16, testName = "Add Roles in Roles Tab", description = "Assign test user to roles in Roles Tab")
    @Description("Assign test user to roles in Roles Tab")
    public void rolesTabTest(
            @Optional("sd_seleniumtest") String userForRoles
    ) {
        rolesTab = issueDetailsPage.selectRolesTab();
        rolesTab.fillUser(rolesTab.getImplementerSearchBoxId(), userForRoles);
        rolesTab.fillUser(rolesTab.getApproverSearchBoxId(), userForRoles);
        rolesTab.fillUser(rolesTab.getCoordinatorSearchBoxId(), userForRoles);
        rolesTab.fillUser(rolesTab.getManagerSearchBoxId(), userForRoles);

        Assert.assertEquals(rolesTab.getValueFromSearchBox(rolesTab.getImplementerSearchBoxId()), userForRoles);
        Assert.assertEquals(rolesTab.getValueFromSearchBox(rolesTab.getApproverSearchBoxId()), userForRoles);
        Assert.assertEquals(rolesTab.getValueFromSearchBox(rolesTab.getCoordinatorSearchBoxId()), userForRoles);
        Assert.assertEquals(rolesTab.getValueFromSearchBox(rolesTab.getManagerSearchBoxId()), userForRoles);
    }

    @Test(priority = 17, testName = "Check Participants", description = "Check Participants Tab - add Participant")
    @Description("Check Participants Tab - add Participant")
    public void addParticipant() {
        participantsTab = issueDetailsPage.selectParticipantsTab();
        participantsTab.createParticipantAndLinkToIssue(PARTICIPANT_FIRST_NAME, PARTICIPANT_SURNAME, PARTICIPANT_ROLE);
        int newParticipantRow = participantsTab.participantRow(PARTICIPANT_FIRST_NAME);

        Assert.assertEquals(participantsTab.checkParticipantFirstName(newParticipantRow), PARTICIPANT_FIRST_NAME);
        Assert.assertEquals(participantsTab.checkParticipantSurname(newParticipantRow), PARTICIPANT_SURNAME);
        Assert.assertEquals(participantsTab.checkParticipantRole(newParticipantRow), PARTICIPANT_ROLE.toUpperCase());
    }

    @Test(priority = 18, testName = "Edit participant", description = "Edit participant")
    @Description("Edit participant")
    public void editParticipant() {
        participantsTab = issueDetailsPage.selectParticipantsTab();
        int newParticipantRow = participantsTab.participantRow(PARTICIPANT_FIRST_NAME);
        participantsTab.selectParticipant(newParticipantRow);
        participantsTab.editParticipant(PARTICIPANT_FIRST_NAME_EDITED, PARTICIPANT_SURNAME, PARTICIPANT_ROLE);
        int editedParticipantRow = participantsTab.participantRow(PARTICIPANT_FIRST_NAME_EDITED);

        Assert.assertEquals(participantsTab.checkParticipantFirstName(editedParticipantRow), PARTICIPANT_FIRST_NAME_EDITED);
        Assert.assertEquals(participantsTab.checkParticipantSurname(editedParticipantRow), PARTICIPANT_SURNAME);
        Assert.assertEquals(participantsTab.checkParticipantRole(editedParticipantRow), PARTICIPANT_ROLE.toUpperCase());
    }

    @Test(priority = 19, testName = "Unlink Participant", description = "Unlink Edited Participant")
    @Description("Unlink Edited Participant")
    public void unlinkParticipant() {
        participantsTab = issueDetailsPage.selectParticipantsTab();
        int participantsInTable = participantsTab.countParticipantsInTable();
        participantsTab.selectParticipant(participantsTab.participantRow(PARTICIPANT_FIRST_NAME_EDITED));
        participantsTab.clickUnlinkParticipant();

        Assert.assertEquals(participantsTab.countParticipantsInTable(), participantsInTable - 1);
    }

    @Test(priority = 20, testName = "Remove Participant", description = "Remove Edited Participant")
    @Description("Remove Edited Participant")
    public void removeParticipant() {
        participantsTab = issueDetailsPage.selectParticipantsTab();
        participantsTab.addExistingParticipant(PARTICIPANT_FIRST_NAME_EDITED, PARTICIPANT_ROLE);
        int participantsInTable = participantsTab.countParticipantsInTable();
        participantsTab.selectParticipant(participantsTab.participantRow(PARTICIPANT_FIRST_NAME_EDITED));
        participantsTab.clickRemoveParticipant();
        participantsTab.clickConfirmRemove();

        Assert.assertEquals(participantsTab.countParticipantsInTable(), participantsInTable - 1);
    }

    @Test(priority = 21, testName = "Add attachment to change", description = "Add attachment to change")
    @Description("Add attachment to change")
    public void addAttachment() {
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(TABS_WIDGET_ID);

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.addAttachment(FILE_TO_UPLOAD_PATH);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        Assert.assertEquals(attachmentsTab.getAttachmentOwner(), USER_NAME);
    }

    @Test(priority = 22, testName = "Download Attachment", description = "Download the Attachment from Attachment tab in Change")
    @Description("Download the Attachment from Attachment tab in Change")
    public void downloadAttachment() {
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(TABS_WIDGET_ID);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDownloadAttachment();
        issueDetailsPage.attachFileToReport(CSV_FILE);

        Assert.assertTrue(issueDetailsPage.checkIfFileIsNotEmpty(CSV_FILE));
    }

    @Test(priority = 23, testName = "Delete Attachment", description = "Delete the Attachment from Attachment tab in Change")
    @Description("Delete the Attachment from Attachment tab in Change")
    public void deleteAttachment() {
        attachmentsTab = issueDetailsPage.selectAttachmentsTab(TABS_WIDGET_ID);
        issueDetailsPage.maximizeWindow(TABS_WIDGET_ID);

        Assert.assertFalse(attachmentsTab.isAttachmentListEmpty());
        attachmentsTab.clickDeleteAttachment();

        Assert.assertTrue(attachmentsTab.isAttachmentListEmpty());
    }

    @Parameters({"serviceMOIdentifier"})
    @Test(priority = 24, testName = "Add Affected", description = "Add Affected Service to the Change")
    @Description("Add Affected Service to the Change")
    public void addAffected(
            @Optional("TEST_MO_ABS_SRV") String serviceMOIdentifier
    ) {
        affectedTab = issueDetailsPage.selectAffectedTab();
        int initialServiceCount = affectedTab.countServicesInTable();
        affectedTab.addServiceToTable(serviceMOIdentifier);

        Assert.assertEquals(affectedTab.countServicesInTable(), initialServiceCount + 1);
    }

    @Test(priority = 25, testName = "Add Summary", description = "Add Summary on Summary tab")
    @Description("Add Summary on Summary tab")
    public void checkSummary() {
        summaryTab = issueDetailsPage.selectSummaryTab();

        summaryTab.addTextNote(SUMMARY_TEXT);
        Assert.assertEquals(summaryTab.getTextMessage(), SUMMARY_TEXT);
    }
}
