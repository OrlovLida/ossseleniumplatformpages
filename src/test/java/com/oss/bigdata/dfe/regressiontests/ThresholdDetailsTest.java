package com.oss.bigdata.dfe.regressiontests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.ThresholdPage;

import io.qameta.allure.Description;

public class ThresholdDetailsTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ThresholdDetailsTest.class);
    private ThresholdPage thresholdPage;

    @BeforeClass
    public void goToThresholdsView() {
        thresholdPage = ThresholdPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"thresholdName"})
    @Test(priority = 1, testName = "Check Threshold Details Tab", description = "Check Threshold Details Tab")
    @Description("Check Threshold Details Tab")
    public void checkThresholdDetailsTab(
            @Optional("t:SMOKE#alwaysWorking") String thresholdName
    ) {
        boolean thresholdExists = thresholdPage.thresholdExistsIntoTable(thresholdName);
        if (thresholdExists) {
            thresholdPage.selectFoundThreshold();
            thresholdPage.selectDetailsTab();
            Assert.assertEquals(thresholdPage.checkNameInPropertyPanel(), thresholdName);
        } else {
            log.info("Cannot find existing Threshold {}", thresholdName);
            Assert.fail("Cannot find existing Threshold " + thresholdName);
        }
    }
}

