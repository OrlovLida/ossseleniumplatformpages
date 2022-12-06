package com.oss.iaa.bigdata.dfe.SMOKE;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.ThresholdPage;

import io.qameta.allure.Description;

public class ThresholdSmokeTest extends BaseTestCase {

    private ThresholdPage thresholdPage;
    private static final String THRESHOLD_NAME = "t:SMOKE#alwaysWorking";
    private static final String STATUS_SUCCESS = "Success";
    private static final String STATUS_WARNING = "Warning";

    @BeforeClass
    public void goToThresholdsView() {
        thresholdPage = ThresholdPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check if Threshold is working", description = "Check if Threshold is working")
    @Description("Check if Threshold is working")
    public void checkIfThresholdIsWorking() {
        boolean thresholdExists = thresholdPage.thresholdExistsIntoTable(THRESHOLD_NAME);
        if (thresholdExists) {
            thresholdPage.selectFirstThresholdInTable();
            thresholdPage.selectExecutionHistoryTab();
            thresholdPage.clickRefreshInTabTable();

            boolean ifRunsFresh = thresholdPage.isIfRunsFresh();
            Assert.assertTrue(ifRunsFresh);

            String actualStatus = thresholdPage.checkStatus();
            boolean statusIsAcceptable = actualStatus.equals(STATUS_SUCCESS) || actualStatus.equals(STATUS_WARNING);
            Assert.assertTrue(statusIsAcceptable);
        } else {
            Assert.fail("Cannot find existing Threshold " + THRESHOLD_NAME);
        }
    }
}
