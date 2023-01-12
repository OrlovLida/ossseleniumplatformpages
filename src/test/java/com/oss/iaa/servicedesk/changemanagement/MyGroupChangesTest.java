package com.oss.iaa.servicedesk.changemanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.comarch.oss.web.pages.NotificationWrapperPage;
import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.changemanagement.ChangeDashboardPage;
import com.oss.pages.iaa.servicedesk.changemanagement.MyChangesPage;
import com.oss.pages.iaa.servicedesk.changemanagement.MyGroupChangesPage;
import com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.tabs.OverviewTab;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.CHANGE_ISSUE_TYPE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DETAILS_TABS_CONTAINER_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DOWNLOAD_FILE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.EXPORT_WIZARD_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.USER_NAME;

@Listeners({TestListener.class})
public class MyGroupChangesTest extends BaseTestCase {
    private ChangeDashboardPage changeDashboardPage;
    private MyChangesPage myChangesPage;
    private MyGroupChangesPage myGroupChangesPage;
    private NotificationWrapperPage notificationWrapperPage;
    private IssueDetailsPage issueDetailsPage;
    private OverviewTab changeOverviewTab;
    private SDWizardPage sdWizardPage;
    private String changeID;

    private static final String CHANGE_DESCRIPTION = "Selenium Test Change";

    @BeforeMethod
    public void goToChangeDashboardPage() {
        changeDashboardPage = new ChangeDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL);
    }

    @Parameters({"changeRequester", "changeAssignee"})
    @Test(priority = 1, testName = "Create Change", description = "Create Change")
    @Description("Create Change")
    public void createChange(
            @Optional("sd_seleniumtest") String changeRequester,
            @Optional("Tier 2 Mobile") String changeAssignee
    ) {
        sdWizardPage = changeDashboardPage.openCreateChangeWizard();
        sdWizardPage.createChange(changeRequester, changeAssignee, CHANGE_DESCRIPTION);
        changeID = changeDashboardPage.getIdFromMessage();
        Assert.assertTrue(changeDashboardPage.isChangeCreated(CHANGE_DESCRIPTION));
    }

    @Test(priority = 2, testName = "Check My Group Changes", description = "Check My Group Changes")
    @Description("Check My Group Changes")
    public void checkMyGroupChanges() {
        myGroupChangesPage = new MyGroupChangesPage(driver, webDriverWait).openView(driver, BASIC_URL);
        myGroupChangesPage.filterBy(ID_ATTRIBUTE, changeID);
        myGroupChangesPage.getIdForNthTicketInTable(0);
        Assert.assertFalse(myGroupChangesPage.isIssueTableEmpty());
        Assert.assertEquals(myGroupChangesPage.getIdForNthTicketInTable(0), changeID);
    }

    @Test(priority = 3, testName = "Refresh MyGroup Changes", description = "Refresh MyGroup Changes")
    @Description("Refresh MyGroup Changes")
    public void refreshMyGroupChanges() {
        myGroupChangesPage = new MyGroupChangesPage(driver, webDriverWait).openView(driver, BASIC_URL);
        if (!myGroupChangesPage.isIssueTableEmpty()) {
            int changesInTable = myGroupChangesPage.countIssuesInTable();
            myGroupChangesPage.clickRefresh();
            int changesAfterRefresh = myGroupChangesPage.countIssuesInTable();

            Assert.assertFalse(myGroupChangesPage.isIssueTableEmpty());
            Assert.assertTrue(changesInTable <= changesAfterRefresh);
        } else {
            Assert.fail("No data in table - cannot check refresh function");
        }
    }

    @Test(priority = 4, testName = "Export from My Group Changes", description = "Export from My Group Changes")
    @Description("Export from My Group Changes")
    public void exportFromMyGroupChanges() {
        myGroupChangesPage = new MyGroupChangesPage(driver, webDriverWait).openView(driver, BASIC_URL);
        try {
            myGroupChangesPage.exportFromSearchViewTable(EXPORT_WIZARD_ID);
        } catch (RuntimeException e) {
            Assert.fail(e.getMessage());
        }
        notificationWrapperPage = new NotificationWrapperPage(driver);

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(myGroupChangesPage.checkIfFileIsNotEmpty(DOWNLOAD_FILE));
    }

    @Test(priority = 5, testName = "My Changes Check", description = "Change assignee and check if change is visible in My Changes View")
    @Description("Change assignee and check if change is visible in My Changes View")
    public void myChangesCheck() {
        issueDetailsPage = changeDashboardPage.openIssueDetailsView(changeID, BASIC_URL, CHANGE_ISSUE_TYPE);
        issueDetailsPage.maximizeWindow(DETAILS_TABS_CONTAINER_ID);
        changeOverviewTab = issueDetailsPage.selectOverviewTab(CHANGE_ISSUE_TYPE);
        changeOverviewTab.changeIssueAssignee(USER_NAME);
        myChangesPage = new MyChangesPage(driver, webDriverWait).openView(driver, BASIC_URL);
        Assert.assertFalse(myChangesPage.isIssueTableEmpty());

        myChangesPage.filterBy(ID_ATTRIBUTE, changeID);
        Assert.assertFalse(myChangesPage.isIssueTableEmpty());
    }
}

