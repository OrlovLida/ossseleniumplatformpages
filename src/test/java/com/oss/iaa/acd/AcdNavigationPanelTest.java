package com.oss.iaa.acd;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.NavigationPanelPage;

import io.qameta.allure.Description;

public class AcdNavigationPanelTest extends BaseTestCase {

    private static final String ACD_CATEGORY = "AI Control Desk";
    private static final String OVERALL_INSIGHT_SUBCATEGORY = "Overall Insight";
    private static final String ASD_SUMMARY_SUBCATEGORY = "ASD Summary";
    private static final String ABGAD_SUMMARY_SUBCATEGORY = "ABGAD Summary";
    private static final String APD_SUMMARY_SUBCATEGORY = "APD Summary";
    private static final String ASD_SETTINGS_SUBCATEGORY = "ASD Settings";
    private static final String ABGAD_SETTINGS_SUBCATEGORY = "ABGAD Settings";
    private static final String APD_SETTINGS_SUBCATEGORY = "APD Settings";
    private static final String AR_SETTINGS_SUBCATEGORY = "AR Settings";
    private static final String SYSTEM_SETTINGS_SUBCATEGORY = "System Settings";
    private static final String ASD_ASSESSMENT_SUBCATEGORY = "ASD Assessment";
    private static final String ABGAD_ASSESSMENT_SUBCATEGORY = "ABGAD Assessment";
    private static final String APD_ASSESSMENT_SUBCATEGORY = "APD Assessment";

    private static final String OVERALL_INSIGHT_URL_SUFFIX = "dashboard/predefined/id/_Automation_Control_Desk";
    private static final String ASD_SUMMARY_URL_SUFFIX = "view/acd/asd";
    private static final String ABGAD_SUMMARY_URL_SUFFIX = "view/acd/abgad";
    private static final String APD_SUMMARY_URL_SUFFIX = "view/acd/apd";
    private static final String ASD_SETTINGS_URL_SUFFIX = "view/acd/asdSettings";
    private static final String ABGAD_SETTINGS_URL_SUFFIX = "view/acd/abgadSettings";
    private static final String APD_SETTINGS_URL_SUFFIX = "view/acd/apdSettings";
    private static final String AR_SETTINGS_URL_SUFFIX = "view/acd/kaSettings";
    private static final String SYSTEM_SETTINGS_URL_SUFFIX = "view/acd/systemSettings";
    private static final String ASD_ASSESSMENT_URL_SUFFIX = "view/acd/asdAssessment";
    private static final String ABGAD_ASSESSMENT_URL_SUFFIX = "view/acd/abgadAssessment";
    private static final String APD_ASSESSMENT_URL_SUFFIX = "view/acd/apdAssessment";

    private NavigationPanelPage navigationPanelPage;

    @BeforeMethod
    public void goToNavigationPanelPage() {
        navigationPanelPage = NavigationPanelPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check Overall Insight button", description = "Check Overall Insight button")
    @Description("Check Overall Insight button")
    public void checkOverallInsightButton() {
        navigationPanelPage.openChosenApplication(ACD_CATEGORY, OVERALL_INSIGHT_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(OVERALL_INSIGHT_URL_SUFFIX));
    }

    @Test(priority = 2, testName = "Check ASD Summary button", description = "Check ASD Summary button")
    @Description("Check ASD Summary button")
    public void checkAsdSummaryButton() {
        navigationPanelPage.openChosenApplication(ACD_CATEGORY, ASD_SUMMARY_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(ASD_SUMMARY_URL_SUFFIX));
    }

    @Test(priority = 3, testName = "Check ABGAD Summary button", description = "Check ABGAD Summary button")
    @Description("Check ABGAD Summary button")
    public void checkAbgadSummaryButton() {
        navigationPanelPage.openChosenApplication(ACD_CATEGORY, ABGAD_SUMMARY_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(ABGAD_SUMMARY_URL_SUFFIX));
    }

    @Test(priority = 4, testName = "Check APD Summary button", description = "Check APD Summary button")
    @Description("Check APD Summary button")
    public void checkApdSummaryButton() {
        navigationPanelPage.openChosenApplication(ACD_CATEGORY, APD_SUMMARY_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(APD_SUMMARY_URL_SUFFIX));
    }

    @Test(priority = 5, testName = "Check ASD Settings button", description = "Check ASD Settings button")
    @Description("Check ASD Settings button")
    public void checkAsdSettingsButton() {
        navigationPanelPage.openChosenApplication(ACD_CATEGORY, ASD_SETTINGS_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(ASD_SETTINGS_URL_SUFFIX));
    }

    @Test(priority = 6, testName = "Check ABGAD Settings button", description = "Check ABGAD Settings button")
    @Description("Check ABGAD Settings button")
    public void checkAbgadSettingsButton() {
        navigationPanelPage.openChosenApplication(ACD_CATEGORY, ABGAD_SETTINGS_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(ABGAD_SETTINGS_URL_SUFFIX));
    }

    @Test(priority = 7, testName = "Check APD Settings button", description = "Check APD Settings button")
    @Description("Check APD Settings button")
    public void checkApdSettingsButton() {
        navigationPanelPage.openChosenApplication(ACD_CATEGORY, APD_SETTINGS_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(APD_SETTINGS_URL_SUFFIX));
    }

    @Test(priority = 8, testName = "Check AR Settings button", description = "Check AR Settings button")
    @Description("Check AR Settings button")
    public void checkArSettingsButton() {
        navigationPanelPage.openChosenApplication(ACD_CATEGORY, AR_SETTINGS_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(AR_SETTINGS_URL_SUFFIX));
    }

    @Test(priority = 9, testName = "Check System Settings button", description = "Check System Settings button")
    @Description("Check System Settings button")
    public void checkSystemSettingsButton() {
        navigationPanelPage.openChosenApplication(ACD_CATEGORY, SYSTEM_SETTINGS_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(SYSTEM_SETTINGS_URL_SUFFIX));
    }

    @Test(priority = 10, testName = "Check ASD Assessments button", description = "Check ASD Assessments button")
    @Description("Check ASD Assessment button")
    public void checkAsdAssessmentButton() {
        navigationPanelPage.openChosenApplication(ACD_CATEGORY, ASD_ASSESSMENT_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(ASD_ASSESSMENT_URL_SUFFIX));
    }

    @Test(priority = 11, testName = "Check ABGAD Assessments button", description = "Check ABGAD Assessments button")
    @Description("Check ABGAD Assessment button")
    public void checkAbgadAssessmentButton() {
        navigationPanelPage.openChosenApplication(ACD_CATEGORY, ABGAD_ASSESSMENT_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(ABGAD_ASSESSMENT_URL_SUFFIX));
    }

    @Test(priority = 12, testName = "Check APD Assessments button", description = "Check APD Assessments button")
    @Description("Check APD Assessment button")
    public void checkApdAssessmentButton() {
        navigationPanelPage.openChosenApplication(ACD_CATEGORY, APD_ASSESSMENT_SUBCATEGORY);
        Assert.assertTrue(navigationPanelPage.isChosenUrlOpen(APD_ASSESSMENT_URL_SUFFIX));
    }
}