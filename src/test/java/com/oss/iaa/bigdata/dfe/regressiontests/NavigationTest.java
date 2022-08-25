package com.oss.iaa.bigdata.dfe.regressiontests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.NavigationPanelPage;

import io.qameta.allure.Description;

public class NavigationTest extends BaseTestCase {

    private static final String DFE_CATEGORY = "Data Factory Engine";
    private static final String CONFIGURATION_SUBCATEGORY = "Configuration";
    private static final String ADMINISTRATION_SUBCATEGORY = "Administration";
    private static final String XDR_BROWSER = "XDR Browser";
    private static final String XDR_BROWSER_PARTIAL_URL = "view/dfe/xdr-browser";
    private static final String SERVER_GROUPS = "Server Groups";
    private static final String SERVER_GROUPS_PARTIAL_URL = "view/dfe/server-group";
    private static final String EXTERNAL_RESOURCES = "External Resources";
    private static final String EXTERNAL_RESOURCES_PARTIAL_URL = "view/dfe/external-resources";
    private static final String DICTIONARIES = "Dictionaries";
    private static final String DICTIONARIES_PARTIAL_URL = "view/dfe/dictionaries";
    private static final String DATA_SOURCES = "Data Sources";
    private static final String DATA_SOURCES_PARTIAL_URL = "view/dfe/datasource";
    private static final String DIMENSIONS = "Dimensions";
    private static final String DIMENSIONS_PARTIAL_URL = "view/dfe/dimension";
    private static final String ETL_DC = "ETL Data Collections";
    private static final String ETL_DC_PARTIAL_URL = "view/dfe/etl-data-collection";
    private static final String AGGREGATES = "Aggregates";
    private static final String AGGREGATES_PARTIAL_URL = "view/dfe/aggregates";
    private static final String KQIS = "KQIs";
    private static final String KQIS_PARTIAL_URL = "view/dfe/kqi";
    private static final String THRESHOLDS = "Thresholds";
    private static final String THRESHOLDS_PARTIAL_URL = "view/dfe/thresholds";
    private static final String PROBLEMS = "Problems";
    private static final String PROBLEMS_PARTIAL_URL = "view/dfe/problems";
    private static final String STORAGE_POLICIES = "Storage Policies";
    private static final String STORAGE_POLICIES_PARTIAL_URL = "view/dfe/storage-policies";
    private static final String ETL_CORRELATIONS = "ETL Correlations";
    private static final String ETL_CORRELATIONS_PARTIAL_URL = "view/dfe/configuration";
    private static final String ETL_CORRELATIONS_VIEW_NAME = "Config Service Editor";
    private static final String ALARM_QVP = "Alarm Quick View Policies";
    private static final String ALARM_QVP_PARTIAL_URL = "view/inventory/view/type/ITProblem?dfe";
    private static final String ALARM_QVP_VIEW_NAME = "Legacy Inventory View for an object type: Problem";

    private NavigationPanelPage navigationPanelPage;

    @BeforeMethod
    public void goToNavigationPanelPage() {
        navigationPanelPage = NavigationPanelPage.goToHomePage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check DFE Dashboard - XDR Browser", description = "Check DFE Dashboard blocks without subcategory - XDR Browser")
    @Description("Check DFE Dashboard blocks without subcategory - XDR Browser")
    public void checkDFEDashboard() {
        navigationPanelPage.openApplicationInCategory(DFE_CATEGORY, XDR_BROWSER);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(XDR_BROWSER_PARTIAL_URL));
        Assert.assertTrue(navigationPanelPage.getViewTitle().contains(XDR_BROWSER));
    }

    @DataProvider(name = "categories")
    public Object[][] categories() {
        return new Object[][]{
                {SERVER_GROUPS, SERVER_GROUPS_PARTIAL_URL},
                {EXTERNAL_RESOURCES, EXTERNAL_RESOURCES_PARTIAL_URL},
                {DICTIONARIES, DICTIONARIES_PARTIAL_URL},
                {DATA_SOURCES, DATA_SOURCES_PARTIAL_URL},
                {DIMENSIONS, DIMENSIONS_PARTIAL_URL},
                {ETL_DC, ETL_DC_PARTIAL_URL},
                {AGGREGATES, AGGREGATES_PARTIAL_URL},
                {KQIS, KQIS_PARTIAL_URL},
                {THRESHOLDS, THRESHOLDS_PARTIAL_URL},
                {PROBLEMS, PROBLEMS_PARTIAL_URL}
        };
    }

    @Test(priority = 2, dataProvider = "categories", testName = "Check DFE Dashboard - Configuration", description = "Check DFE Dashboard blocks in subcategory Configuration")
    @Description("Check DFE Dashboard blocks in subcategory Configuration")
    public void checkDFEConfigurationSubcategory(String application, String partialURL) {
        navigationPanelPage.openApplicationInSubCategory(DFE_CATEGORY, CONFIGURATION_SUBCATEGORY, application);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(partialURL));
        Assert.assertTrue(navigationPanelPage.getViewTitle().contains(application));
    }

    @Test(priority = 3, testName = "Check DFE Dashboard - Administration", description = "Check DFE Dashboard blocks in subcategory Administration")
    @Description("Check DFE Dashboard blocks in subcategory Administration")
    public void checkDFEAdministrationSubcategory() {
        navigationPanelPage.openApplicationInSubCategory(DFE_CATEGORY, ADMINISTRATION_SUBCATEGORY, STORAGE_POLICIES);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(STORAGE_POLICIES_PARTIAL_URL));
        Assert.assertTrue(navigationPanelPage.getViewTitle().contains(STORAGE_POLICIES));
    }

    @DataProvider(name = "applicationsWithViewName")
    public Object[][] applicationsWithViewName() {
        return new Object[][]{
                {ETL_CORRELATIONS, ETL_CORRELATIONS_PARTIAL_URL, ETL_CORRELATIONS_VIEW_NAME},
                {ALARM_QVP, ALARM_QVP_PARTIAL_URL, ALARM_QVP_VIEW_NAME}
        };
    }

    @Test(priority = 4, dataProvider = "applicationsWithViewName", testName = "Check blocks with different view name", description = "Check DFE Dashboard blocks with subcategory and View Name different than application name")
    @Description("Check DFE Dashboard blocks with subcategory and View Name different than application name")
    public void checkBlocksWithDifferentViewName(String applicationName, String partialURL, String viewName) {
        navigationPanelPage.openApplicationInSubCategory(DFE_CATEGORY, CONFIGURATION_SUBCATEGORY, applicationName);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(partialURL));
        Assert.assertTrue(navigationPanelPage.getViewTitle().contains(viewName));
    }
}
