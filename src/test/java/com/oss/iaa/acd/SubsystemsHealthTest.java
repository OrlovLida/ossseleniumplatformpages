package com.oss.iaa.acd;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.acd.settingsview.SubsystemsHealthPage;

import io.qameta.allure.Description;

public class SubsystemsHealthTest extends BaseTestCase {

    private SubsystemsHealthPage subsystemsHealthPage;

    private static final String ASD_SETTINGS_VIEW_SUFFIX_URL = "%s/#/view/acd/asdSettings";
    private static final String APD_SETTINGS_VIEW_SUFFIX_URL = "%s/#/view/acd/apdSettings";
    private static final String ABGAD_SETTINGS_VIEW_SUFFIX_URL = "%s/#/view/acd/abgadSettings";
    private static final String AR_SETTINGS_VIEW_SUFFIX_URL = "%s/#/view/acd/kaSettings";
    private static final String ABGAD_SETTINGS_TAB_ID = "abgadSettingsTabsContainer";
    private static final String AR_SETTINGS_TAB_ID = "arSettingsTabsContainer";
    private static final String SUBSYSTEMS_HEALTH_TAB = "Subsystems Health";

    @BeforeClass
    public void initializePage() {
        subsystemsHealthPage = new SubsystemsHealthPage(driver, webDriverWait);
    }

    @Test(priority = 1, testName = "Check ASD Subsystems Health", description = "Check ASD Subsystems Health")
    @Description("Check ASD Subsystems Health")
    public void checkASDSubsystemsHealth() {
        SubsystemsHealthPage.goToSettingsView(driver, ASD_SETTINGS_VIEW_SUFFIX_URL, BASIC_URL);
        Assert.assertTrue(subsystemsHealthPage.isSubsystemUpAndRunning());
    }

    @Test(priority = 2, testName = "Check ABGAD Subsystems Health", description = "Check ABGAD Subsystems Health")
    @Description("Check ABGAD Subsystems Health")
    public void checkABGADSubsystemsHealth() {
        SubsystemsHealthPage.goToSettingsView(driver, ABGAD_SETTINGS_VIEW_SUFFIX_URL, BASIC_URL);
        subsystemsHealthPage.goToTab(ABGAD_SETTINGS_TAB_ID, SUBSYSTEMS_HEALTH_TAB);
        Assert.assertTrue(subsystemsHealthPage.isSubsystemUpAndRunning());
    }

    @Test(priority = 3, testName = "Check APD Subsystems Health", description = "Check APD Subsystems Health")
    @Description("Check APD Subsystems Health")
    public void checkAPDSubsystemsHealth() {
        SubsystemsHealthPage.goToSettingsView(driver, APD_SETTINGS_VIEW_SUFFIX_URL, BASIC_URL);
        Assert.assertTrue(subsystemsHealthPage.isSubsystemUpAndRunning());
    }

    @Test(priority = 4, testName = "Check AR Subsystems Health", description = "Check AR Subsystems Health")
    @Description("Check AR Subsystems Health")
    public void checkARSubsystemsHealth() {
        SubsystemsHealthPage.goToSettingsView(driver, AR_SETTINGS_VIEW_SUFFIX_URL, BASIC_URL);
        subsystemsHealthPage.goToTab(AR_SETTINGS_TAB_ID, SUBSYSTEMS_HEALTH_TAB);
        Assert.assertTrue(subsystemsHealthPage.isSubsystemUpAndRunning());
    }
}