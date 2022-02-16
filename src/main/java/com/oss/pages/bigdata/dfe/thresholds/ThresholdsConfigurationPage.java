package com.oss.pages.bigdata.dfe.thresholds;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_AREA;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;
import static com.oss.framework.utils.DelayUtils.sleep;
import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class ThresholdsConfigurationPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ThresholdsConfigurationPage.class);

    private static final String WIZARD_ID = "thresholdWizard";
    private static final String NAME_INPUT_ID = "conditionGroupNameId";
    private static final String ACTIVE_COMBOBOX_ID = "conditionGroupActiveId";
    private static final String AGGREGATION_COMBOBOX_ID = "conditionGroupTimeGrainId";
    private static final String DEBUG_COMBOBOX_ID = "conditionGroupDebugModeId";
    private static final String CONDITION_COMBOBOX_ID = "selectConditionTypeId";
    private static final String SEVERITY_COMBOBOX_ID = "simpleConditionSeverityId";
    private static final String ELSE_SEVERITY_COMBOBOX_ID = "elseConditionSeverityId";
    private static final String FORMULA_AREA_ID = "simpleConditionFormulaId";
    private static final String PROBLEM_NAME_FIELD_ID = "conditionGroupProblemId";

    private final Wizard configurationWizard;

    public ThresholdsConfigurationPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        configurationWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public void fillName(String name) {
        waitForPageToLoad(driver, wait);
        configurationWizard.setComponentValue(NAME_INPUT_ID, name, TEXT_FIELD);
        log.debug("Setting name with: {}", name);
    }

    public void fillActiveCombobox(String notificationStatus) {
        configurationWizard.setComponentValue(ACTIVE_COMBOBOX_ID, notificationStatus, COMBOBOX);
        log.debug("Setting notification status with: {}", notificationStatus);
    }

    public void fillProblemNameSearchField(String problemName) {
        configurationWizard.setComponentValue(PROBLEM_NAME_FIELD_ID, problemName, SEARCH_FIELD);
        log.debug("Setting problem name with: {}", problemName);
    }

    public void fillAggregationCombobox(String aggregationPeriod) {
        configurationWizard.setComponentValue(AGGREGATION_COMBOBOX_ID, aggregationPeriod, COMBOBOX);
        log.debug("Setting aggregation period with: {}", aggregationPeriod);
    }

    public void fillDebugCombobox(String debugMode) {
        waitForPageToLoad(driver, wait);
        configurationWizard.setComponentValue(DEBUG_COMBOBOX_ID, debugMode, COMBOBOX);
        log.debug("Setting debug mode: {}", debugMode);
    }

    public void addConditionGroup() {
        waitForPageToLoad(driver, wait);
        TreeComponent treeComponent = configurationWizard.getTreeComponent();
        treeComponent.getNodeByLabelsPath("Condition Group").callAction("ADD");
        log.debug("Adding new condition group");
    }

    public void fillConditionTypeCombobox(String conditionType) {
        waitForPageToLoad(driver, wait);
        configurationWizard.setComponentValue(CONDITION_COMBOBOX_ID, conditionType, COMBOBOX);
        log.debug("Setting condition type with: {}", conditionType);
    }

    public void fillFormula(String formula) {
        waitForPageToLoad(driver, wait);
        configurationWizard.setComponentValue(FORMULA_AREA_ID, formula, TEXT_AREA);
        log.debug("Setting description with: {}", formula);
    }

    public void fillSeverityCombobox(String severity) {
        waitForPageToLoad(driver, wait);
        configurationWizard.setComponentValue(SEVERITY_COMBOBOX_ID, severity, COMBOBOX);
        log.debug("Setting severity for simple condition with: {}", severity);
    }

    public void fillElseSeverityCombobox(String severity) {
        waitForPageToLoad(driver, wait);
        configurationWizard.setComponentValue(ELSE_SEVERITY_COMBOBOX_ID, severity, COMBOBOX);
        log.debug("Setting severity for else condition with: {}", severity);
    }

    public void clickAddNewFilter() {
        waitForPageToLoad(driver, wait);
        sleep();
        configurationWizard.clickButtonById("dimensionButtonAddId");
        log.debug("Clicking add new filter button");
    }

    @Step("I fill Threshold Configuration Step")
    public void fillThresholdConfigurationStep(String name, String notificationStatus, String problemName, String aggregationPeriod, String debugMode) {
        fillName(name);
        fillActiveCombobox(notificationStatus);
        fillProblemNameSearchField(problemName);
        fillAggregationCombobox(aggregationPeriod);
        fillDebugCombobox(debugMode);
        log.info("Filled Basic Information Step");
    }

    @Step("I fill Threshold Configuration Step - simple condition")
    public void fillSimpleConditionStep(String conditionType, String formula, String severity) {
        addConditionGroup();
        fillConditionTypeCombobox(conditionType);
        fillFormula(formula);
        fillSeverityCombobox(severity);
        log.info("Filled Basic Information Step - Simple Condition");
    }

    @Step("I fill Threshold Configuration Step - else condition")
    public void fillElseConditionStep(String elseConditionType, String elseSeverity) {
        addConditionGroup();
        fillConditionTypeCombobox(elseConditionType);
        fillElseSeverityCombobox(elseSeverity);
        log.info("Filled Basic Information Step - Else Condition");
    }
}
