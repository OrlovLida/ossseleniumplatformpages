package com.oss.iaa.faultmanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.NavigationPanelPage;

import io.qameta.allure.Description;

public class FMNavigationPanelTest extends BaseTestCase {

    private static final String FAULT_MANAGEMENT_CATEGORY = "Fault Management";
    private static final String DASHBOARD_SUBCATEGORY = "Dashboard";
    private static final String FILTER_MANAGER_SUBCATEGORY = "Filter Manager";
    private static final String KEDB_CONFIGURATION_SUBCATEGORY = "KEDB Configuration";
    private static final String KEDB_LIBRARY_SUBCATEGORY = "KEDB Library";
    private static final String TAGGED_ALARM_LISTS_SUBCATEGORY = "Tagged Alarm Lists";
    private static final String USER_PREFERENCES_SUBCATEGORY = "User Preferences";
    private static final String ALARM_GENERATOR_SUBCATEGORY = "Alarm Generator";

    private static final String FM_DASHBOARD_URL_SUFFIX = "dashboard/predefined/id/_FaultManagement";
    private static final String FILTER_MANAGER_URL_SUFFIX = "view/filter-manager/manager";
    private static final String KEDB_CONFIGURATION_URL_SUFFIX = "views/fault-management/kedb/configuration";
    private static final String KEDB_LIBRARY_URL_SUFFIX = "views/fault-management/kedb/library";
    private static final String TAGGED_ALARM_LISTS_URL_SUFFIX = "views/fault-management/tagged-alarm-list";
    private static final String USER_PREFERENCES_URL_SUFFIX = "view/fault-management/preferences";
    private static final String ALARM_GENERATOR_URL_SUFFIX = "views/fault-management/alarm-generator";

    private NavigationPanelPage navigationPanelPage;

    @BeforeMethod
    public void goToNavigationPanelPage() {
        navigationPanelPage = NavigationPanelPage.goToHomePage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check FM Dashboard button", description = "Check FM Dashboard button")
    @Description("Check FM Dashboard button")
    public void checkFMDashboardButton() {
        navigationPanelPage.openApplicationInCategory(FAULT_MANAGEMENT_CATEGORY, DASHBOARD_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(FM_DASHBOARD_URL_SUFFIX));
    }

    @Test(priority = 2, testName = "Check Filter Manager button", description = "Check Filter Manager button")
    @Description("Check Filter Manager button")
    public void checkFilterManagerButton() {
        navigationPanelPage.openApplicationInCategory(FAULT_MANAGEMENT_CATEGORY, FILTER_MANAGER_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(FILTER_MANAGER_URL_SUFFIX));
    }

    @Test(priority = 3, testName = "Check KEDB configuration button", description = "Check KEDB configuration button")
    @Description("Check KEDB configuration button")
    public void checkKEDBConfigurationButton() {
        navigationPanelPage.openApplicationInCategory(FAULT_MANAGEMENT_CATEGORY, KEDB_CONFIGURATION_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(KEDB_CONFIGURATION_URL_SUFFIX));
    }

    @Test(priority = 4, testName = "Check KEDB library button", description = "Check KEDB library button")
    @Description("Check KEDB library button")
    public void checkKEDBLibraryButton() {
        navigationPanelPage.openApplicationInCategory(FAULT_MANAGEMENT_CATEGORY, KEDB_LIBRARY_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(KEDB_LIBRARY_URL_SUFFIX));
    }

    @Test(priority = 5, testName = "Check Tagged Alarms button", description = "Check Tagged Alarms button")
    @Description("Check Tagged Alarms button")
    public void checkTaggedAlarmsButton() {
        navigationPanelPage.openApplicationInCategory(FAULT_MANAGEMENT_CATEGORY, TAGGED_ALARM_LISTS_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(TAGGED_ALARM_LISTS_URL_SUFFIX));
    }

    @Test(priority = 6, testName = "Check User Preferences button", description = "Check User Preferences button")
    @Description("Check User Preferences button")
    public void checkUserPreferencesButton() {
        navigationPanelPage.openApplicationInCategory(FAULT_MANAGEMENT_CATEGORY, USER_PREFERENCES_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(USER_PREFERENCES_URL_SUFFIX));
    }

    @Test(priority = 7, testName = "Check Alarm Generator button", description = "Check Alarm Generator button")
    @Description("Check Alarm Generator button")
    public void checkAlarmGeneratorButton() {
        navigationPanelPage.openApplicationInCategory(FAULT_MANAGEMENT_CATEGORY, ALARM_GENERATOR_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(ALARM_GENERATOR_URL_SUFFIX));
    }
}