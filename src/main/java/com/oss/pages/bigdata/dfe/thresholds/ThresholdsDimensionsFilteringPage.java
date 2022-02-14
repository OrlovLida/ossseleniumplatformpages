package com.oss.pages.bigdata.dfe.thresholds;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class ThresholdsDimensionsFilteringPage extends BasePage {

    private static final String DIMENSION_COMBOBOX = "comboBoxDimensionId";
    private static final String GROUPING_COMBOBOX = "comboBoxGroupingId";
    private static final String FILTERING_TYPE_COMBOBOX = "comboBoxQueryOperatorId";
    private static final String MO_PATTERN_ID = "additionalPropertiesGroupMoIdentifierPatternId";
    private static final String WIZARD_ID = "thresholdWizard";

    private final Wizard dimensionsFilterWizard;

    private static final Logger log = LoggerFactory.getLogger(ThresholdsDimensionsFilteringPage.class);

    public ThresholdsDimensionsFilteringPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        dimensionsFilterWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public void fillDimensionCombobox(String dimension) {
        DelayUtils.waitForPageToLoad(driver, wait);
        dimensionsFilterWizard.setComponentValue(DIMENSION_COMBOBOX, dimension, COMBOBOX);
        log.debug("Setting dimension with: {}", dimension);
    }

    public void fillGroupingCombobox(String grouping) {
        DelayUtils.waitForPageToLoad(driver, wait);
        dimensionsFilterWizard.setComponentValue(GROUPING_COMBOBOX, grouping, COMBOBOX);
        log.debug("Setting grouping with: {}", grouping);
    }

    public void fillFilteringTypeCombobox(String filteringType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        dimensionsFilterWizard.setComponentValue(FILTERING_TYPE_COMBOBOX, filteringType, COMBOBOX);
        log.debug("Setting filteringType with: {}", filteringType);
    }

    public void clickSaveFilter() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep();
        dimensionsFilterWizard.clickSave();
        log.debug("Saving filter by clicking Save button");
    }

    public void fillMOPatternField(String moPattern) {
        DelayUtils.waitForPageToLoad(driver, wait);
        dimensionsFilterWizard.setComponentValue(MO_PATTERN_ID, moPattern, TEXT_FIELD);
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
