package com.oss.pages.bigdata.dfe.thresholds;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.*;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.bigdata.dfe.stepwizard.commons.BaseStepPage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.StepWizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThresholdsConfigurationPage extends BaseStepPage {

    private static final Logger log = LoggerFactory.getLogger(ThresholdsConfigurationPage.class);

    private final String THRESHOLD_STEP_WIZARD_ID = "thresholdWizardWindowId";
    private final String NAME_INPUT_ID = "conditionGroupNameId";
    private final String ACTIVE_COMBOBOX_ID = "conditionGroupActiveId";
    private final String AGGREGATION_COMBOBOX_ID = "conditionGroupTimeGrainId";
    private final String DEBUG_COMBOBOX_ID = "conditionGroupDebugModeId";
    private final String CONDITION_COMBOBOX_ID = "selectConditionTypeId";
    private final String SEVERITY_COMBOBOX_ID = "simpleConditionSeverityId";
    private final String ELSE_SEVERITY_COMBOBOX_ID = "elseConditionSeverityId";
    private final String FORMULA_AREA_ID = "simpleConditionFormulaId";
    private final String PROBLEM_NAME_FIELD_ID = "conditionGroupProblemId";

    public ThresholdsConfigurationPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    public void fillName(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(name, NAME_INPUT_ID);
        log.debug("Setting name with: {}", name);
    }

    public void fillActiveCombobox(String notificationStatus) {
        fillCombobox(notificationStatus, ACTIVE_COMBOBOX_ID);
    }

    public void fillProblemNameSearchField(String problemName) {
        fillSearchField(problemName, PROBLEM_NAME_FIELD_ID);
    }

    public void fillAggregationCombobox(String aggregationPeriod) {
        fillCombobox(aggregationPeriod, AGGREGATION_COMBOBOX_ID);
        log.debug("Setting aggregation period with: {}", aggregationPeriod);
    }

    public void fillDebugCombobox(String debugMode) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillCombobox(debugMode, DEBUG_COMBOBOX_ID);
        log.debug("Setting debug mode: {}", debugMode);
    }

    public void addConditionGroup() {
        DelayUtils.waitForPageToLoad(driver, wait);
        TreeWidget treeWidget = TreeWidget.createByClass(driver, "tree-component", wait);
        treeWidget.callActionById(ActionsContainer.KEBAB_GROUP_ID, "ADD");
        log.debug("Adding new condition group");
    }

    public void fillConditionTypeCombobox(String conditionType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillCombobox(conditionType, CONDITION_COMBOBOX_ID);
        log.debug("Setting condition type with: {}", conditionType);
    }

    public void fillFormula(String formula) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TextArea formulaInput = (TextArea) getWizard(driver, wait).getComponent(FORMULA_AREA_ID, Input.ComponentType.TEXT_AREA);
        formulaInput.setValue(Data.createSingleData(formula));
        log.debug("Setting description with: {}", formula);
    }

    public void fillSeverityCombobox(String severity) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillCombobox(severity, SEVERITY_COMBOBOX_ID);
        log.debug("Setting severity for simple condition with: {}", severity);
    }

    public void fillElseSeverityCombobox(String severity) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillCombobox(severity, ELSE_SEVERITY_COMBOBOX_ID);
        log.debug("Setting severity for else condition with: {}", severity);
    }

    public void clickAddNewFilter() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep();
        ButtonContainer.create(driver, wait).callActionByLabel("Add New Filter");
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

    @Override
    public String getWizardId() {
        return THRESHOLD_STEP_WIZARD_ID;
    }
}
