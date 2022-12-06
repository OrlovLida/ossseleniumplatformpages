package com.oss.pages.reconciliation.audit;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

public class RuleDefinitionWizardPage extends BasePage {

    private static final String WIZARD_ID = "networkParametersAudit_validationRuleManagerIdRuleDefinitionSaveWizardId";
    private static final String ACCEPT_ID = "wizard-submit-button-networkParametersAudit_validationRuleManagerIdRuleDefinitionSaveWizardId";
    private static final String INTERFACE_ID = "networkParametersAudit_validationRuleManagerIdCmInterfaceComponentSearchFieldId";
    private static final String GROUP_ID = "networkParametersAudit_validationRuleManagerIdGroupNameComponentComboboxId";
    private static final String TYPE_ID = "networkParametersAudit_validationRuleManagerIdVsTypeComponentComboboxId";
    private static final String ATTRIBUTE_ID = "networkParametersAudit_validationRuleManagerIdVsAttributeComponentComboboxFieldId";
    private static final String OPERATOR_ID = "networkParametersAudit_validationRuleManagerIdOperatorComponentComboboxId";
    private static final String VALUE1_TEXT_ID = "networkParametersAudit_validationRuleManagerIdValue1ComponentTextId";
    private static final String VALUE1_ID = "networkParametersAudit_validationRuleManagerIdValue1ComponentNumberFiledId";
    private static final String VALUE2_ID = "networkParametersAudit_validationRuleManagerIdValue2ComponentNumberFiledId";
    private static final String RULE_DEFINITIONS_ID = "networkParametersAudit_validationRuleManagerIdRuleDefinitionComponentHtmlEditorId";
    private static final String PARENT_OBJECT_TYPE_ID = "networkParametersAudit_validationRuleManagerIdParentVsTypeComponentComboboxId";
    private static final String CHILD_OBJECT_TYPE_ID = "networkParametersAudit_validationRuleManagerIdChildVsTypeComponentComboboxId";
    private static final String EXPECTED_CARDINALITY_ID = "networkParametersAudit_validationRuleManagerIdCardinalityComponentNumberFieldId";
    private static final String VALIDATE_ID = "networkParametersAudit_validationRuleManagerIdRuleValidateComponentButtonId";
    private static final String VALIDATION_MESSAGE_ID = "networkParametersAudit_validationRuleManagerIdValidationMessageComponentLabelId";
    private static final String EQUAL_TO_OPERATOR = "EQUAL TO";

    public RuleDefinitionWizardPage(WebDriver driver) {
        super(driver);
    }

    public void createSimpleValueCheckDefinition(SimpleValueCheckDefinition def) {
        def.getGroupName().ifPresent(groupName -> setGroup(def.getGroupName().get()));
        waitForPageToLoad();
        def.getInterfaceName().ifPresent(interfaceName -> setInterface(def.getInterfaceName().get()));
        waitForPageToLoad();
        def.getObjectType().ifPresent(objectType -> setObjectType(def.getObjectType().get()));
        waitForPageToLoad();
        def.getAttributeName().ifPresent(attributeName -> setAttribute(def.getAttributeName().get()));
        waitForPageToLoad();
        setOperator(def.getOperator());
        waitForPageToLoad();
        def.getValue1().ifPresent(value1 -> setValue1(def.getValue1().get(), def.getOperator()));
        waitForPageToLoad();
        def.getValue2().ifPresent(value2 -> setValue2(def.getValue2().get()));
        waitForPageToLoad();
        accept();
    }

    public void createObjectCardinalityCheckDefinition(ObjectCardinalityCheckDefinition def) {
        def.getGroupName().ifPresent(groupName -> setGroup(def.getGroupName().get()));
        waitForPageToLoad();
        def.getInterfaceName().ifPresent(interfaceName -> setInterface(def.getInterfaceName().get()));
        waitForPageToLoad();
        setParentObjectType(def.getParentObjectType());
        waitForPageToLoad();
        def.getChildObjectType().ifPresent(objectType -> setChildObjectType(def.getChildObjectType().get()));
        waitForPageToLoad();
        setExpectedCardinality(def.getExpectedCardinality());
        waitForPageToLoad();
        accept();
    }

    public void fillComplexValueCheckDefinition(ComplexValueCheckDefinition def) {
        def.getGroupName().ifPresent(groupName -> setGroup(def.getGroupName().get()));
        waitForPageToLoad();
        def.getInterfaceName().ifPresent(interfaceName -> setInterface(def.getInterfaceName().get()));
        waitForPageToLoad();
        def.getObjectType().ifPresent(objectType -> setObjectType(def.getObjectType().get()));
        waitForPageToLoad();
        setRuleDefinitions(def.getRuleDefinitions());
        waitForPageToLoad();
    }

    public void fillCardinalityAndValueCheckDefinition(CardinalityAndValueCheckDefinition def) {
        def.getGroupName().ifPresent(groupName -> setGroup(def.getGroupName().get()));
        waitForPageToLoad();
        def.getInterfaceName().ifPresent(interfaceName -> setInterface(def.getInterfaceName().get()));
        waitForPageToLoad();
        setParentObjectType(def.getParentObjectType());
        waitForPageToLoad();
        def.getChildObjectType().ifPresent(objectType -> setChildObjectType(def.getChildObjectType().get()));
        waitForPageToLoad();
        setRuleDefinitions(def.getRuleDefinitions());
        waitForPageToLoad();
    }

    public void setValue1(String value1, String operator) {
        if (operator.equals(EQUAL_TO_OPERATOR)) {
            setValue1Text(value1);
        } else {
            setValue1(value1);
        }
    }

    public void setInterface(String interfaceName) {
        getWizard().setComponentValue(INTERFACE_ID, interfaceName);
    }

    public void setGroup(String group) {
        getWizard().setComponentValue(GROUP_ID, group);
    }

    public void setObjectType(String objectType) {
        getWizard().setComponentValue(TYPE_ID, objectType);
    }

    public void setAttribute(String attribute) {
        getWizard().setComponentValue(ATTRIBUTE_ID, attribute);
    }

    public void setOperator(String operator) {
        getWizard().setComponentValue(OPERATOR_ID, operator);
    }

    public void setValue1(String value1) {
        getWizard().setComponentValue(VALUE1_ID, value1);
    }

    public void setValue1Text(String value1) {
        getWizard().setComponentValue(VALUE1_TEXT_ID, value1);
    }

    public void setValue2(String value2) {
        getWizard().setComponentValue(VALUE2_ID, value2);
    }

    public void setRuleDefinitions(String ruleDefinitions) {
        getWizard().setComponentValue(RULE_DEFINITIONS_ID, ruleDefinitions, Input.ComponentType.HTML_EDITOR);
    }

    public void setParentObjectType(String parentObjectType) {
        getWizard().setComponentValue(PARENT_OBJECT_TYPE_ID, parentObjectType);
    }

    public void setChildObjectType(String childObjectType) {
        getWizard().setComponentValue(CHILD_OBJECT_TYPE_ID, childObjectType);
    }

    public void setExpectedCardinality(String expectedCardinality) {
        getWizard().setComponentValue(EXPECTED_CARDINALITY_ID, expectedCardinality);
    }

    public void validate() {
        getWizard().clickButtonById(VALIDATE_ID);
    }

    public void accept() {
        getWizard().clickButtonById(ACCEPT_ID);
    }

    public boolean isValidationMessagePresent() {
        return CSSUtils.isElementPresent(driver, VALIDATION_MESSAGE_ID);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
