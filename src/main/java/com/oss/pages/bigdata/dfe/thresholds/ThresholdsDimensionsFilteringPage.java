package com.oss.pages.bigdata.dfe.thresholds;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.stepwizard.commons.BaseStepPage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.StepWizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThresholdsDimensionsFilteringPage extends BaseStepPage {

    private final String DIMENSION_COMBOBOX = "comboBoxDimensionId";
    private final String GROUPING_COMBOBOX = "comboBoxGroupingId";
    private final String FILTERING_TYPE_COMBOBOX = "comboBoxQueryOperatorId";
    private final String MO_PATTERN_ID = "additionalPropertiesGroupMoIdentifierPatternId";

    private static final Logger log = LoggerFactory.getLogger(ThresholdsDimensionsFilteringPage.class);

    public ThresholdsDimensionsFilteringPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }


    public void fillDimensionCombobox(String dimension) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillCombobox(dimension, DIMENSION_COMBOBOX);
        log.debug("Setting dimension with: {}", dimension);
    }

    public void fillGroupingCombobox(String grouping) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillCombobox(grouping, GROUPING_COMBOBOX);
        log.debug("Setting grouping with: {}", grouping);
    }

    public void fillFilteringTypeCombobox(String filteringType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillCombobox(filteringType, FILTERING_TYPE_COMBOBOX);
        log.debug("Setting filteringType with: {}", filteringType);
    }

    public void clickSaveFilter() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep();
        ButtonContainer.create(driver, wait).callActionByLabel("Save");
    }

    public void fillMOPatternField(String moPattern) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(moPattern, MO_PATTERN_ID);
        log.debug("Setting MO Identifier pattern with: {}", moPattern);
    }

    @Step("I fill Dimension Filtering Step")
    public void fillDimensionFilteringStep(String dimension, String grouping, String filteringType) {
        fillDimensionCombobox(dimension);
        fillGroupingCombobox(grouping);
        fillFilteringTypeCombobox(filteringType);
        log.info("Filled Dimension Filtering Step");
    }

}
