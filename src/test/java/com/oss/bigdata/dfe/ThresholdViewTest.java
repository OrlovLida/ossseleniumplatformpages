package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.ThresholdPage;
import com.oss.pages.bigdata.dfe.thresholds.ThresholdStepWizardPage;
import com.oss.pages.bigdata.dfe.thresholds.ThresholdsConfigurationPage;
import io.qameta.allure.Description;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThresholdViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ThresholdViewTest.class);

    private ThresholdPage thresholdPage;
    private String thresholdName;
    private String updatedThresholdName;

    private final static String NOTIFICATION_STATUS = "Yes";
    private final static String AGGREGATION_PERIOD = "Five Minutes";
    private final static String DEBUG_MODE = "No";
    private final static String CONDITION_TYPE = "Simple Condition";
    private final static String CONDITION_TYPE_ELSE = "Else Condition";
    private final static String THRESHOLD_FORMULA = "$[K:t:SMOKE#SuccessRate] < 30";
    private final static String SEVERITY = "WARNING";
    private final static String PROBLEM_NAME = "t:SMOKE#Problem";
    private final static String SEVERITY_ELSE_CONDITION = "CLEARED";
    private final static String DIMENSION_NAME = "d1 [EtlForKqis_d1]";
    private final static String GROUPING_FLAG = "Yes";
    private final static String FILTERING_TYPE = "NONE";
    private final static String MO_ID_PATTERN = "$[d1.d1]";

    @BeforeClass
    public void goToThresholdsView() {
        thresholdPage = ThresholdPage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        thresholdName = "Selenium_" + date + "_ThreshTest";
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
        Boolean thresholdIsCreated = thresholdPage.thresholdExistsIntoTable(thresholdName);

        if (!thresholdIsCreated) {
            log.info("Cannot find created threshold configuration ");
        }
        Assert.assertTrue(thresholdIsCreated);
    }

    @Test(priority = 2, testName = "Edit Threshold", description = "Edit Threshold")
    @Description("Edit Threshold")
    public void editThreshold() {
        Boolean thresholdExists = thresholdPage.thresholdExistsIntoTable(thresholdName);
        if (thresholdExists) {
            thresholdPage.selectFoundThreshold();
            thresholdPage.clickEditThreshold();

            WebDriverWait wait = new WebDriverWait(driver, 45);
            ThresholdStepWizardPage thresholdsStepWizardPage = new ThresholdStepWizardPage(driver, wait);
            thresholdsStepWizardPage.getThresholdsConfigurationStep().fillName(updatedThresholdName);
            thresholdsStepWizardPage.clickNext();
            thresholdsStepWizardPage.clickAccept();

            Boolean thresholdIsEdited = thresholdPage.thresholdExistsIntoTable(updatedThresholdName);
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
        Boolean thresholdExists = thresholdPage.thresholdExistsIntoTable(updatedThresholdName);
        if (thresholdExists) {
            thresholdPage.selectFoundThreshold();
            thresholdPage.clickDeleteThreshold();
            thresholdPage.confirmDelete();
            Boolean thresholdDeleted = !thresholdPage.thresholdExistsIntoTable(updatedThresholdName);

            Assert.assertTrue(thresholdDeleted);
        } else {
            log.error("Threshold with name: {} was not deleted", updatedThresholdName);
            Assert.fail();
        }
    }
}
