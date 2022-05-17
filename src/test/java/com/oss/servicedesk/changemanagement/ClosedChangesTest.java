package com.oss.servicedesk.changemanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.platform.NotificationWrapperPage;
import com.oss.pages.servicedesk.changemanagement.ChangeDashboardPage;
import com.oss.pages.servicedesk.changemanagement.ClosedChangesPage;
import com.oss.pages.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.CHANGE_ISSUE_TYPE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.DOWNLOAD_FILE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;

public class ClosedChangesTest extends BaseTestCase {

    private ChangeDashboardPage changeDashboardPage;
    private SDWizardPage sdWizardPage;
    private IssueDetailsPage issueDetailsPage;
    private ClosedChangesPage closedChangesPage;
    private NotificationWrapperPage notificationWrapperPage;
    private String changeID;
    private static final String STATUS_READY_FOR_APPROVAL = "Ready for Approval";
    private static final String STATUS_APPROVED = "Approved";
    private static final String STATUS_IMPLEMENTED = "Implemented";
    private static final String STATUS_VERIFIED = "Verified";
    private static final String STATUS_CLOSED = "Closed";
    private static final String EXPORT_WIZARD_ID = "exportgui-mainview_prompt-card";
    private static final String CHANGE_DESCRIPTION = "Selenium Test - Closed Change";

    @BeforeClass
    public void goToChangeDashboard() {
        changeDashboardPage = new ChangeDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Parameters({"changeRequester", "changeAssignee"})
    @Test(priority = 1, testName = "Close Change", description = "Create change and go through all necessary steps to close it")
    @Description("Create change and go through all necessary steps to close it")
    public void createAndCloseChange(
            @Optional("sd_seleniumtest") String changeRequester,
            @Optional("Tier 2 Mobile") String changeAssignee
    ) {
        sdWizardPage = changeDashboardPage.openCreateChangeWizard();
        sdWizardPage.createChange(changeRequester, changeAssignee, CHANGE_DESCRIPTION);
        changeID = changeDashboardPage.getIdFromMessage();
        issueDetailsPage = changeDashboardPage.openIssueDetailsView(changeID, BASIC_URL, CHANGE_ISSUE_TYPE);
        issueDetailsPage.changeIssueStatus(STATUS_READY_FOR_APPROVAL);
        issueDetailsPage.changeIssueStatus(STATUS_APPROVED);
        issueDetailsPage.changeIssueStatus(STATUS_IMPLEMENTED);
        issueDetailsPage.changeIssueStatus(STATUS_VERIFIED);
        issueDetailsPage.changeIssueStatus(STATUS_CLOSED);
        Assert.assertEquals(issueDetailsPage.checkIssueStatus(), STATUS_CLOSED);
    }

    @Test(priority = 2, testName = "Check Closed Changes View", description = "Refresh, search and check if change is shown in the closed changes table")
    @Description("Refresh, search and check if change is shown in the closed changes table")
    public void checkClosedChangesView() {
        closedChangesPage = new ClosedChangesPage(driver, webDriverWait).openView(driver, BASIC_URL);
        closedChangesPage.clickRefresh();
        closedChangesPage.filterByTextField(ID_ATTRIBUTE, changeID);
        Assert.assertEquals(closedChangesPage.getIdForNthTicketInTable(0), changeID);
    }

    @Test(priority = 3, testName = "Export Closed Changes", description = "Export Closed Changes")
    @Description("Export Closed Changes")
    public void ExportClosedChanges() {
        closedChangesPage = new ClosedChangesPage(driver, webDriverWait).openView(driver, BASIC_URL);
        try {
            closedChangesPage.exportFromSearchViewTable(EXPORT_WIZARD_ID);
        } catch (RuntimeException e) {
            Assert.fail(e.getMessage());
        }
        notificationWrapperPage = new NotificationWrapperPage(driver);

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(closedChangesPage.checkIfFileIsNotEmpty(DOWNLOAD_FILE));
    }
}

