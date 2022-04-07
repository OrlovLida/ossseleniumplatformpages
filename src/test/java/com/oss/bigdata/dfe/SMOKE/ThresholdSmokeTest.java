package com.oss.bigdata.dfe.SMOKE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.ThresholdPage;

import io.qameta.allure.Description;

public class ThresholdSmokeTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ThresholdSmokeTest.class);
    private ThresholdPage thresholdPage;
    private static final String THRESHOLD_NAME = "t:SMOKE#alwaysWorking";

    @BeforeClass
    public void goToThresholdsView() {
        thresholdPage = ThresholdPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check if Threshold is working", description = "Check if Threshold is working")
    @Description("Check if Threshold is working")
    public void checkIfThresholdIsWorking() {
        boolean thresholdExists = thresholdPage.thresholdExistsIntoTable(THRESHOLD_NAME);
        if (thresholdExists) {
            thresholdPage.selectFoundThreshold();
            thresholdPage.selectExecutionHistoryTab();
            thresholdPage.clickRefreshInTabTable();

            boolean ifRunsFresh = thresholdPage.isIfRunsFresh();
            Assert.assertTrue(ifRunsFresh);

            String actualStatus = thresholdPage.checkStatus();
            boolean statusIsAcceptable = actualStatus.equals("Success") || actualStatus.equals("Warning");
            Assert.assertTrue(statusIsAcceptable);
        } else {
            log.info("Cannot find existing Threshold {}", THRESHOLD_NAME);
            Assert.fail("Cannot find existing Threshold " + THRESHOLD_NAME);
        }
    }
}
