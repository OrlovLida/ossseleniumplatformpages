package com.oss.servicedesk.changemanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.platform.NotificationWrapperPage;
import com.oss.pages.servicedesk.changemanagement.ChangeDashboardPage;
import com.oss.pages.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.servicedesk.issue.tabs.MessagesTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedChangesTab;
import com.oss.pages.servicedesk.issue.tabs.RootCausesTab;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.CHANGE_ISSUE_TYPE;

public class ChangesTest extends BaseTestCase {

    private ChangeDashboardPage changeDashboardPage;
    private SDWizardPage sdWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private MessagesTab messagesTab;
    private RelatedChangesTab relatedChangesTab;
    private NotificationWrapperPage notificationWrapperPage;
    private RootCausesTab rootCausesTab;
    private String changeID;

    private static final String RISK_ASSESSMENT_ID = "TT_WIZARD_INPUT_RISK_ASSESSMENT_LABEL";
    private static final String RISK = "LOW";
    private static final String REQUESTER_ID = "TT_WIZARD_INPUT_REQUESTER_LABEL";
    private static final String ASSIGNEE_ID = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private static final String INCIDENT_DESCRIPTION_ID = "TT_WIZARD_INPUT_INCIDENT_DESCRIPTION_LABEL";
    private static final String INCIDENT_DESCRIPTION_TXT = "Selenium Incident Description";
    private static final String INCIDENT_DESCRIPTION_TXT_EDITED = "Selenium Incident Description Edited";
    private static final String DESCRIPTION_TAB_ID = "_descriptionTab";
    private static final String DESCRIPTION_FIELD_ID = "_descriptionWidget";
    private static final String DESCRIPTION_WINDOW_ID = "_descriptionsWindow";
    private static final String NOTIFICATION_INTERNAL_MSG = "Test Selenium Internal Message";
    private static final String NOTIFICATION_EMAIL_MSG = "Test Selenium Email Message";
    private static final String TABLES_WINDOW_ID = "_tablesWindow";
    private static final String INTERNAL_COMMENT_MSG = "Test Selenium Message Comment in Change";

    @BeforeMethod
    public void goToTicketDashboardPage() {
        changeDashboardPage = new ChangeDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
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

    @Parameters({"newAssignee"})
    @Test(priority = 2, testName = "Edit Change", description = "Edit change - assignee and description")
    @Description("Edit change - assignee and description")
    public void editChange(
            @Optional("ca_kodrobinska") String newAssignee
    ) {
        issueDetailsPage = changeDashboardPage.openIssueDetailsView(changeID, BASIC_URL, CHANGE_ISSUE_TYPE);
        sdWizardPage = issueDetailsPage.openEditChangeWizard();
        sdWizardPage.insertValueToSearchComponent(newAssignee, ASSIGNEE_ID);
        sdWizardPage.insertValueToTextAreaComponent(INCIDENT_DESCRIPTION_TXT_EDITED, INCIDENT_DESCRIPTION_ID);
        sdWizardPage.clickNextButtonInWizard();
        sdWizardPage.clickAcceptButtonInWizard();
        Assert.assertEquals(issueDetailsPage.checkAssignee(), newAssignee);
    }

    @Test(priority = 3, testName = "Check description", description = "Check change description on Description tab")
    @Description("Check change description on Description tab")
    public void checkDescription() {
        issueDetailsPage = changeDashboardPage.openIssueDetailsView(changeID, BASIC_URL, CHANGE_ISSUE_TYPE);
        issueDetailsPage.selectTabFromTabsWidget(DESCRIPTION_WINDOW_ID, DESCRIPTION_TAB_ID);
        Assert.assertEquals(issueDetailsPage.getDisplayedText(DESCRIPTION_WINDOW_ID, DESCRIPTION_FIELD_ID), INCIDENT_DESCRIPTION_TXT_EDITED);
    }

    @Parameters({"messageTo"})
    @Test(priority = 4, testName = "Add Internal Notification in Change", description = "Add Internal Notification in Change")
    @Description("Add Internal Notification in Change")
    public void addInternalNotification(
            @Optional("ca_kodrobinska") String messageTo
    ) {
        issueDetailsPage = changeDashboardPage.openIssueDetailsView(changeID, BASIC_URL, CHANGE_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(TABLES_WINDOW_ID);
        messagesTab = issueDetailsPage.selectMessagesTab();
        sdWizardPage = messagesTab.createNewNotificationOnMessagesTab();
        sdWizardPage.createInternalNotification(NOTIFICATION_INTERNAL_MSG, messageTo);

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_INTERNAL_MSG);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
        Assert.assertEquals(messagesTab.getBadgeTextFromMessage(0, 0), "New");
    }

    @Parameters({"notificationEmailTo", "notificationEmailFrom"})
    @Test(priority = 5, testName = "Add Email Notification in Change", description = "Add Email Notification in Change")
    @Description("Add Email Notification in Change")
    public void addEmailNotification(
            @Optional("kornelia.odrobinska@comarch.com") String notificationEmailTo,
            @Optional("switch.ticket@comarch.com") String notificationEmailFrom
    ) {
        issueDetailsPage = changeDashboardPage.openIssueDetailsView(changeID, BASIC_URL, CHANGE_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(TABLES_WINDOW_ID);
        messagesTab = issueDetailsPage.selectMessagesTab();
        sdWizardPage = messagesTab.createNewNotificationOnMessagesTab();
        sdWizardPage.createEmailNotification(notificationEmailTo, notificationEmailFrom, NOTIFICATION_EMAIL_MSG);

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.getMessageText(0), NOTIFICATION_EMAIL_MSG);
        Assert.assertEquals(messagesTab.checkMessageType(0), "NOTIFICATION");
    }

    @Test(priority = 6, testName = "Add Internal Comment", description = "Add Internal Comment")
    @Description("Add Internal Comment")
    public void addInternalComment() {
        issueDetailsPage = changeDashboardPage.openIssueDetailsView(changeID, BASIC_URL, CHANGE_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(TABLES_WINDOW_ID);
        messagesTab = issueDetailsPage.selectMessagesTab();
        messagesTab.addInternalComment(INTERNAL_COMMENT_MSG);

        Assert.assertFalse(messagesTab.isMessagesTabEmpty());
        Assert.assertEquals(messagesTab.checkMessageType(0), "COMMENT");
        Assert.assertEquals(messagesTab.getMessageText(0), INTERNAL_COMMENT_MSG);
        Assert.assertEquals(messagesTab.checkCommentType(0), "internal");
    }

    @Parameters({"RelatedChangeID"})
    @Test(priority = 7, testName = "Check Related Changes Tab - link Change", description = "Check Related Changes Tab - link Change")
    @Description("Check Related Changes Tab - link Change")
    public void addRelatedChange(
            @Optional("35") String RelatedChangeID
    ) {
        issueDetailsPage = changeDashboardPage.openIssueDetailsView(changeID, BASIC_URL, CHANGE_ISSUE_TYPE);
        relatedChangesTab = issueDetailsPage.selectRelatedChangesTab();
        sdWizardPage = relatedChangesTab.openLinkIssueWizard();
        sdWizardPage.insertValueToMultiSearchComponent(RelatedChangeID, "linkChange");
        sdWizardPage.clickLinkButton();

        Assert.assertEquals(relatedChangesTab.checkRelatedIssueId(0), RelatedChangeID);
    }

    @Test(priority = 8, testName = "Export Related Changes", description = "Export Related Changes")
    @Description("Export Related Changes")
    public void exportRelatedChanges() {
        issueDetailsPage = changeDashboardPage.openIssueDetailsView(changeID, BASIC_URL, CHANGE_ISSUE_TYPE);
        relatedChangesTab = issueDetailsPage.selectRelatedChangesTab();
        notificationWrapperPage = relatedChangesTab.openNotificationPanel();
        notificationWrapperPage.clearNotifications();
        relatedChangesTab.clickExport();
        notificationWrapperPage = relatedChangesTab.openNotificationPanel();
        notificationWrapperPage.waitForExportFinish();
        notificationWrapperPage.clickDownload();
        notificationWrapperPage.waitAndGetFinishedNotificationText();
        notificationWrapperPage.clearNotifications();
        relatedChangesTab.attachRelatedIssuesFile();

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(relatedChangesTab.isRelatedIssuesFileNotEmpty());
    }

    @Test(priority = 9, testName = "Check Related Changes Tab - unlink Change", description = "Check Related Changes Tab - unlink Change")
    @Description("Check Related Changes Tab - unlink Change")
    public void unlinkChange() {
        issueDetailsPage = changeDashboardPage.openIssueDetailsView(changeID, BASIC_URL, CHANGE_ISSUE_TYPE);
        relatedChangesTab = issueDetailsPage.selectRelatedChangesTab();
        relatedChangesTab.selectIssue(0);
        relatedChangesTab.unlinkIssue();
        relatedChangesTab.confirmUnlinking();

        Assert.assertTrue(relatedChangesTab.isRelatedIssueTableEmpty());
    }

    @Parameters({"SecondMOIdentifier"})
    @Test(priority = 10, testName = "Add second MO in Root Causes tab", description = "Add second MO in Root Causes tab")
    @Description("Add second MO in Root Causes tab")
    public void addRootCause(@Optional("TEST_MO") String SecondMOIdentifier) {
        issueDetailsPage = changeDashboardPage.openIssueDetailsView(changeID, BASIC_URL, CHANGE_ISSUE_TYPE);
        rootCausesTab = issueDetailsPage.selectRootCauseTab();
        sdWizardPage = rootCausesTab.openAddRootCauseWizard();
        sdWizardPage.getMoStep().enterTextIntoSearchComponent(SecondMOIdentifier);
        sdWizardPage.getMoStep().selectObjectInMOTable(SecondMOIdentifier);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertTrue(rootCausesTab.checkIfMOIdentifierIsPresentOnRootCauses(SecondMOIdentifier));
    }
}
