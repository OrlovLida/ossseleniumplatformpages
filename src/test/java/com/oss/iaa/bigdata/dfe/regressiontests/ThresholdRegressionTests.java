package com.oss.iaa.bigdata.dfe.regressiontests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.ThresholdPage;

import io.qameta.allure.Description;

public class ThresholdRegressionTests extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ThresholdRegressionTests.class);
    private ThresholdPage thresholdPage;

    @BeforeMethod
    public void goToThresholdsView() {
        thresholdPage = ThresholdPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Threshold View is opening ", description = "Threshold View is opening")
    @Description("Threshold View is opening")
    public void checkThresholdView() {
        Assert.assertEquals(thresholdPage.getViewTitle(), "Thresholds Configuration");
    }

    @Parameters({"thresholdName"})
    @Test(priority = 2, testName = "Check Threshold Details Tab", description = "Check Threshold Details Tab")
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

    @Parameters({"thresholdName"})
    @Test(priority = 3, testName = "Check Threshold Conditions Tab", description = "Check if Threshold Conditions Tab has simple and else condition")
    @Description("Check if Threshold Conditions Tab has simple and else condition")
    public void checkThresholdConditionsTab(
            @Optional("t:SMOKE#alwaysWorking") String thresholdName
    ) {
        boolean thresholdExists = thresholdPage.thresholdExistsIntoTable(thresholdName);
        if (thresholdExists) {
            thresholdPage.selectFoundThreshold();
            thresholdPage.selectConditionsTab();

            Assert.assertTrue(thresholdPage.isSimpleAndElseConditionInTable());
        } else {
            log.info("Cannot find existing Threshold {}", thresholdName);
            Assert.fail("Cannot find existing Threshold " + thresholdName);
        }
    }

    @Test(priority = 4, testName = "Check 'Is Active' search", description = "Check 'Is Active' search")
    @Description("Check 'Is Active' search")
    public void checkActiveFunctionality() {
        thresholdPage.chooseIsActive("Yes");
        Assert.assertEquals(thresholdPage.getIsActive(0), "Yes");
    }

    @Parameters({"ProblemId"})
    @Test(priority = 5, testName = "Check Problem ID Search", description = "Check Problem ID Search")
    @Description("Check Problem ID Search")
    public void checkId(
            @Optional("00012") String ProblemId
    ) {
        thresholdPage.searchProblemId(ProblemId);
        Assert.assertEquals(thresholdPage.getProblemId(0), ProblemId);
    }

    @Parameters({"thresholdName"})
    @Test(priority = 6, testName = "Check Activate and Deactivate threshold", description = "Check Activate and Deactivate threshold")
    @Description("Check Activate and Deactivate threshold")
    public void checkActivateDeactivateFunctionality(
            @Optional("t:SMOKE#alwaysWorking") String thresholdName
    ) {
        boolean thresholdExists = thresholdPage.thresholdExistsIntoTable(thresholdName);
        if (thresholdExists) {
            thresholdPage.selectFoundThreshold();
            boolean activeThresholdExist = thresholdPage.isThresholdActive();
            if (activeThresholdExist) {
                thresholdPage.clickDeactivateBatch();
                thresholdPage.confirmDeactivationOrActivation();
                Assert.assertEquals(thresholdPage.getIsActive(0), "No");
                thresholdPage.clickActivateBatch();
                thresholdPage.confirmDeactivationOrActivation();
                Assert.assertEquals(thresholdPage.getIsActive(0), "Yes");
            } else {
                log.info("Threshold {} is not active", thresholdName);
            }
        } else {
            log.info("Cannot find existing Threshold {}", thresholdName);
            Assert.fail("Cannot find existing Threshold " + thresholdName);
        }
    }
}

