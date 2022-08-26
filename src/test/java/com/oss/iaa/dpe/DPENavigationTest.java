package com.oss.iaa.dpe;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.NavigationPanelPage;

import io.qameta.allure.Description;

public class DPENavigationTest extends BaseTestCase {

    private static final String DPE_CATEGORY = "Data Processing Engine";
    private static final String INDICATORS_VIEW = "Indicators View";
    private static final String INDICATORS_VIEW_PARTIAL_URL = "view/indicators-view/indicators-view";
    private static final String ADMINISTRATION_SUBCATEGORY = "Administration";
    private static final String SIMPLE_FILTERS = "Simple Filters";
    private static final String SIMPLE_FILTERS_PARTIAL_URL = "view/Assurance/KPIView-config/config";
    private static final String SIMPLE_FILTERS_VIEW_NAME = "Indicators View Configuration";
    private static final String CONFIGURATION_SUBCATEGORY = "Configuration";
    private static final String INDICATORS = "Indicators";
    private static final String INDICATORS_PARTIAL_URL = "view/inventory/view/type/ITVariable";
    private static final String INDICATORS_VIEW_NAME = "Legacy Inventory View for an object type: Indicator";
    private static final String Y_AXES = "Y Axes";
    private static final String Y_AXES_PARTIAL_URL = "view/inventory/view/type/ITYAxis";
    private static final String Y_AXES_VIEW_NAME = "Legacy Inventory View for an object type: IT Y Axis";
    private static final String INDICATOR_SETS = "Indicator Sets";
    private static final String INDICATOR_SETS_PARTIAL_URL = "view/inventory/view/type/ITIndicatorSet";
    private static final String INDICATOR_SETS_VIEW_NAME = "Legacy Inventory View for an object type: Indicator Set";
    private static final String GLOBAL_QVP = "Global Quick View Policies";
    private static final String GLOBAL_QVP_PARTIAL_URL = "view/inventory/view/type/ITGlobalQuickViewPolicyProblem";
    private static final String GLOBAL_QVP_VIEW_NAME = "Legacy Inventory View for an object type: Global Quick View Policy (for Alarm)";
    private static final String ALARM_QVP = "Alarm Quick View Policies";
    private static final String ALARM_QVP_PARTIAL_URL = "view/inventory/view/type/ITProblem";
    private static final String ALARM_QVP_VIEW_NAME = "Legacy Inventory View for an object type: Problem";
    private static final String MO_QVP = "MO Type Quick View Policies";
    private static final String MO_QVP_PARTIAL_URL = "view/inventory/view/type/ITManagedObjectType";
    private static final String MO_QVP_VIEW_NAME = "Legacy Inventory View for an object type: Managed Object Type";

    private NavigationPanelPage navigationPanelPage;

    @BeforeMethod
    public void goToNavigationPanelPage() {
        navigationPanelPage = NavigationPanelPage.goToHomePage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check DPE Dashboard - Administration", description = "Check DPE Dashboard - Administration subcategory")
    @Description("Check DPE Dashboard - Administration subcategory")
    public void checkDPEDashboardAdministration() {
        navigationPanelPage.openApplicationInSubCategory(DPE_CATEGORY, ADMINISTRATION_SUBCATEGORY, SIMPLE_FILTERS);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(SIMPLE_FILTERS_PARTIAL_URL));
        Assert.assertTrue(navigationPanelPage.getViewTitle().contains(SIMPLE_FILTERS_VIEW_NAME));
    }

    @DataProvider(name = "applicationsWithViewName")
    public Object[][] applicationsWithViewName() {
        return new Object[][]{
                {INDICATORS, INDICATORS_PARTIAL_URL, INDICATORS_VIEW_NAME},
                {Y_AXES, Y_AXES_PARTIAL_URL, Y_AXES_VIEW_NAME},
                {INDICATOR_SETS, INDICATOR_SETS_PARTIAL_URL, INDICATOR_SETS_VIEW_NAME},
                {GLOBAL_QVP, GLOBAL_QVP_PARTIAL_URL, GLOBAL_QVP_VIEW_NAME},
                {ALARM_QVP, ALARM_QVP_PARTIAL_URL, ALARM_QVP_VIEW_NAME},
                {MO_QVP, MO_QVP_PARTIAL_URL, MO_QVP_VIEW_NAME}
        };
    }

    @Test(priority = 1, dataProvider = "applicationsWithViewName", testName = "Check DPE Dashboard - Configuration", description = "Check DPE Dashboard - Configuration subcategory")
    @Description("Check DPE Dashboard - Configuration subcategory")
    public void checkDPEDashboardConfiguration(String applicationName, String partialURL, String viewName) {
        navigationPanelPage.openApplicationInSubCategory(DPE_CATEGORY, CONFIGURATION_SUBCATEGORY, applicationName);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(partialURL));
        Assert.assertTrue(navigationPanelPage.getViewTitle().contains(viewName));
    }

    @Test(priority = 2, testName = "Check Indicators View", description = "Check Indicators View navigation")
    @Description("Check Indicators View navigation")
    public void checkIndicatorsView() {
        navigationPanelPage.openApplicationInCategory(INDICATORS_VIEW, INDICATORS_VIEW);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(INDICATORS_VIEW_PARTIAL_URL));
        Assert.assertTrue(navigationPanelPage.getViewTitle().contains(INDICATORS_VIEW));
    }
}
