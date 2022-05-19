package com.oss.servicedesk.leftsidemenu;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.ServiceDeskMenuPage;
import com.oss.pages.servicedesk.changemanagement.ChangeDashboardPage;
import com.oss.pages.servicedesk.changemanagement.ChangeSearchPage;
import com.oss.pages.servicedesk.changemanagement.ClosedChangesPage;
import com.oss.pages.servicedesk.changemanagement.MyChangesPage;
import com.oss.pages.servicedesk.changemanagement.MyGroupChangesPage;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.CHANGE_DASHBOARD;

public class LeftSideMenuChangeTest extends BaseTestCase {

    private ServiceDeskMenuPage serviceDeskMenuPage;
    private ChangeDashboardPage changeDashboardPage;
    private ChangeSearchPage changeSearchPage;
    private MyChangesPage myChangesPage;
    private MyGroupChangesPage myGroupChangesPage;
    private ClosedChangesPage closedChangesPage;

    private static final String DASHBOARD_PAGE = "Dashboard";
    private static final String CHANGES_SEARCH_PAGE = "Change Request Search";
    private static final String MY_CHANGES_PAGE = "My Changes";
    private static final String MY_GROUP_CHANGES_PAGE = "My Group Changes";
    private static final String CLOSED_CHANGES_PAGE = "Closed Changes";

    @BeforeMethod
    public void openMainPage() {
        serviceDeskMenuPage = new ServiceDeskMenuPage(driver, webDriverWait).openMainPage(BASIC_URL);
    }

    @Test(testName = "Go to Changes Dashboard", description = "Going to Changes Dashboard from Left Side Menu")
    @Description("Going to Changes Dashboard from Left Side Menu")
    public void goToChangeDashboard() {
        serviceDeskMenuPage.chooseFromChangeManagementMenu(DASHBOARD_PAGE);
        changeDashboardPage = new ChangeDashboardPage(driver, webDriverWait);
        Assert.assertTrue(changeDashboardPage.isDashboardOpen(BASIC_URL, CHANGE_DASHBOARD));
    }

    @Test(testName = "Go to Change Search", description = "Going to Change Search from Left Side Menu")
    @Description("Going to Change Search from Left Side Menu")
    public void goToChangeSearch() {
        serviceDeskMenuPage.chooseFromChangeManagementMenu(CHANGES_SEARCH_PAGE);
        changeSearchPage = new ChangeSearchPage(driver, webDriverWait);
        Assert.assertTrue(changeSearchPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(changeSearchPage.getViewTitle(), CHANGES_SEARCH_PAGE);
    }

    @Test(testName = "Go to My Changes", description = "Going to My Changes from Left Side Menu")
    @Description("Going to My Changes from Left Side Menu")
    public void goToMyChanges() {
        serviceDeskMenuPage.chooseFromChangeManagementMenu(MY_CHANGES_PAGE);
        myChangesPage = new MyChangesPage(driver, webDriverWait);
        Assert.assertTrue(myChangesPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(myChangesPage.getViewTitle(), MY_CHANGES_PAGE);
    }

    @Test(testName = "Go to My Group Changes", description = "Going to My Group Changes from Left Side Menu")
    @Description("Going to My Group Changes from Left Side Menu")
    public void goToMyGroupChanges() {
        serviceDeskMenuPage.chooseFromChangeManagementMenu(MY_GROUP_CHANGES_PAGE);
        myGroupChangesPage = new MyGroupChangesPage(driver, webDriverWait);
        Assert.assertTrue(myGroupChangesPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(myGroupChangesPage.getViewTitle(), MY_GROUP_CHANGES_PAGE);
    }

    @Test(testName = "Go to Closed Changes", description = "Going to Closed Changes from Left Side Menu")
    @Description("Going to Closed Changes from Left Side Menu")
    public void goToClosedChanges() {
        serviceDeskMenuPage.chooseFromChangeManagementMenu(CLOSED_CHANGES_PAGE);
        closedChangesPage = new ClosedChangesPage(driver, webDriverWait);
        Assert.assertTrue(closedChangesPage.isPageOpened(BASIC_URL));
        Assert.assertEquals(closedChangesPage.getViewTitle(), CLOSED_CHANGES_PAGE);
    }
}

