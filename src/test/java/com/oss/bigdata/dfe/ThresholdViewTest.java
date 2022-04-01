package com.oss.bigdata.dfe;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.ThresholdPage;
import com.oss.pages.bigdata.dfe.thresholds.ThresholdStepWizardPage;
import com.oss.pages.bigdata.utils.ConstantsDfe;

import io.qameta.allure.Description;

public class ThresholdViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ThresholdViewTest.class);

    private ThresholdPage thresholdPage;
    private String thresholdName;
    private String updatedThresholdName;

    private static final String NOTIFICATION_STATUS = "Yes";
    private static final String AGGREGATION_PERIOD = "Five Minutes";
    private static final String DEBUG_MODE = "No";
    private static final String CONDITION_TYPE = "Simple Condition";
    private static final String CONDITION_TYPE_ELSE = "Else Condition";
    private static final String THRESHOLD_FORMULA = "$[K:t:SMOKE#SuccessRate] < 30";
    private static final String SEVERITY = "WARNING";
    private static final String PROBLEM_NAME = "t:SMOKE#Problem";
    private static final String SEVERITY_ELSE_CONDITION = "CLEARED";
    private static final String DIMENSION_NAME = "d1 [EtlForKqis_d1]";
    private static final String GROUPING_FLAG = "Yes";
    private static final String FILTERING_TYPE = "NONE";
    private static final String MO_ID_PATTERN = "$[d1.d1]";

    @BeforeClass
    public void goToThresholdsView() {
        thresholdPage = ThresholdPage.goToPage(driver, BASIC_URL);

        thresholdName = ConstantsDfe.createName() + "_ThreshTest";
        updatedThresholdName = thresholdName + "_updated";
    }

    @Test(priority = 1, testName = "Add new Threshold", description = "Add new Threshold")
    @Description("Add new Threshold")
    public void addThreshold() {
        thresholdPage.clickAddNewThreshold();
        WebDriverWait wait = new WebDriverWait(driver, 45);
        ThresholdStepWizardPage thresholdsStepWizardPage = new ThresholdStepWizardPage(driver, wait);
        thresholdsStepWizardPage.getThresholdsConfigurationStep().fillThresholdConfigurationStep(thresholdName, NOTIFICATION_STATUS, PROBLEM_NAME, AGGREGATION_PERIOD, DEBUG_MODE);
        thresholdsStepWizardPage.getThresholdsConfigurationStep().fillSimpleConditionStep(CONDITION_TYPE, THRESHOLD_FORMULA, SEVERITY);
        thresholdsStepWizardPage.getThresholdsConfigurationStep().fillElseConditionStep(CONDITION_TYPE_ELSE, SEVERITY_ELSE_CONDITION);
        thresholdsStepWizardPage.clickNext();
        thresholdsStepWizardPage.getThresholdsConfigurationStep().clickAddNewFilter();
        thresholdsStepWizardPage.getThresholdsDimensionsFilteringStep().fillDimensionFilteringStep(DIMENSION_NAME, GROUPING_FLAG, FILTERING_TYPE);
        thresholdsStepWizardPage.getThresholdsDimensionsFilteringStep().clickSaveFilter();
        thresholdsStepWizardPage.getThresholdsDimensionsFilteringStep().fillMOPatternField(MO_ID_PATTERN);
        thresholdsStepWizardPage.clickAccept();
        boolean thresholdIsCreated = thresholdPage.thresholdExistsIntoTable(thresholdName);

        if (!thresholdIsCreated) {
            log.error("Cannot find created threshold configuration ");
        }
        Assert.assertTrue(thresholdIsCreated);
    }

    @Test(priority = 2, testName = "Edit Threshold", description = "Edit Threshold")
    @Description("Edit Threshold")
    public void editThreshold() {
        boolean thresholdExists = thresholdPage.thresholdExistsIntoTable(thresholdName);
        if (thresholdExists) {
            thresholdPage.selectFoundThreshold();
            thresholdPage.clickEditThreshold();

            WebDriverWait wait = new WebDriverWait(driver, 45);
            ThresholdStepWizardPage thresholdsStepWizardPage = new ThresholdStepWizardPage(driver, wait);
            thresholdsStepWizardPage.getThresholdsConfigurationStep().fillName(updatedThresholdName);
            thresholdsStepWizardPage.clickNext();
            thresholdsStepWizardPage.clickAccept();

            boolean thresholdIsEdited = thresholdPage.thresholdExistsIntoTable(updatedThresholdName);
            if (!thresholdIsEdited) {
                log.info("Cannot find existing edited threshold {}", updatedThresholdName);
            }
            Assert.assertTrue(thresholdIsEdited);
        } else {
            log.error("Cannot find existing threshold {}", thresholdName);
            Assert.fail("Cannot find existing threshold " + thresholdName);
        }
    }

    @Test(priority = 3, testName = "Delete Threshold", description = "Delete Threshold")
    @Description("Delete Threshold")
    public void deleteThreshold() {
        boolean thresholdExists = thresholdPage.thresholdExistsIntoTable(updatedThresholdName);
        if (thresholdExists) {
            thresholdPage.selectFoundThreshold();
            thresholdPage.clickDeleteThreshold();
            thresholdPage.confirmDelete();
            boolean thresholdDeleted = !thresholdPage.thresholdExistsIntoTable(updatedThresholdName);

            Assert.assertTrue(thresholdDeleted);
        } else {
            log.error("Threshold with name: {} was not deleted", updatedThresholdName);
            Assert.fail();
        }
    }
}
