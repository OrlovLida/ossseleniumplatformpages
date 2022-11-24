package com.oss.pages.reconciliation.audit;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

public class ValidationRulesManagerPage extends BasePage {

    private static final String TREE_VIEW_ID = "networkParametersAudit_validationRuleManagerIdwindowId";
    private static final String CREATE_GROUP_ACTION_ID = "networkParametersAudit_ValidationRulesGroupActionCreateId";
    private static final String DELETE_GROUP_ACTION_ID = "networkParametersAudit_ValidationRulesGroupActionDeleteId";
    private static final String DELETE_WIZARD_ID = "networkParametersAudit_validationRuleManagerIdvalidationRulesGroupActionWidgetId";
    private static final String DELETE_BUTTON_ID = "wizard-submit-button-networkParametersAudit_validationRuleManagerIdvalidationRulesGroupActionWidgetId";
    private static final String NO_FILTERING_LABEL = "No filtering";
    private static final String TABS_ID = "networkParametersAudit_validationRuleManagerIdrulesDefinitionsWindowId";
    private static final String SIMPLE_VALUE_CHECK_TABLE_ID = "networkParametersAudit_validationRuleManagerIdsingleAttributeRulesTableId";
    private static final String COMPLEX_VALUE_CHECK_TABLE_ID = "networkParametersAudit_validationRuleManagerIdmultiAttributeRulesTableId";
    private static final String OBJECT_CARDINALITY_CHECK_TABLE_ID = "networkParametersAudit_validationRuleManagerIdcardinalityRulesTableId";
    private static final String CARDINALITY_AND_VALUES_CHECK_ID = "networkParametersAudit_validationRuleManagerIdmultiAttributesAndCardinalityRulesTableId";
    public static final String CM_INTERFACE_NAME_COLUMN = "CM Interface Name";
    public static final String STATE_COLUMN = "State";
    public static final String OBJECT_NAME_COLUMN = "Object Name";
    public static final String ATTRIBUTE_NAME_COLUMN = "Attribute Name";
    public static final String OPERATION_COLUMN = "Operation";
    public static final String VALUE_1_COLUMN = "Value 1";
    public static final String VALUE_2_COLUMN = "Value 2";
    public static final String OBJECT_TYPE_COLUMN = "Object Type";
    public static final String CHECKED_ATTRIBUTES_COLUMN = "Checked Attributes";
    public static final String PARENT_OBJECT_TYPE_COLUMN = "Parent Object Type";
    public static final String CHILD_OBJECT_TYPE_COLUMN = "Child Object Type";
    public static final String EXPECTED_CARDINALITY_COLUMN = "Expected Cardinality";

    public ValidationRulesManagerPage(WebDriver driver) {
        super(driver);
    }

    public void searchGroup(String groupName) {
        getTreeView().search(groupName);
    }

    public void openCreateGroupWizard() {
        callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_GROUP_ACTION_ID);
    }

    public void deleteGroup(String groupName) {
        expandNoFiltering();
        getTreeView().selectTreeRow(groupName);
        waitForPageToLoad();
        getTreeView().callActionById(ActionsContainer.EDIT_GROUP_ID, DELETE_GROUP_ACTION_ID);
        waitForPageToLoad();
        getDeleteGroupWizard().clickButtonById(DELETE_BUTTON_ID);
    }

    public void callAction(String groupId, String id) {
        getTreeView().callActionById(groupId, id);
    }

    public boolean isGroupPresent(String groupName) {
        if (!getTreeView().isRowPresent(NO_FILTERING_LABEL)) {
            return false;
        }
        if (!getTreeView().isExpanderIconPresent(NO_FILTERING_LABEL)) {
            return false;
        }
        expandNoFiltering();
        return getTreeView().isRowPresent(groupName);
    }

    public void selectGroup(String groupName) {
        expandNoFiltering();
        getTreeView().selectTreeRow(groupName);
    }

    public int getRowNumberFromSimpleValueCheckTable(String attributeName) {
        return getSimpleValueCheckTable().getRowNumber(attributeName, ATTRIBUTE_NAME_COLUMN);
    }

    public void selectSimpleValueCheckTab() {
        selectTab("0");
    }

    public void selectComplexValueCheckTab() {
        selectTab("1");
    }

    public void selectObjectCardinalityCheckTab() {
        selectTab("2");
    }

    public void selectCardinalityAndValuesCheckTab() {
        selectTab("3");
    }

    public OldTable getSimpleValueCheckTable() {
        selectSimpleValueCheckTab();
        waitForPageToLoad();
        return getDefinitionsTable(SIMPLE_VALUE_CHECK_TABLE_ID);
    }

    public OldTable getComplexValueCheckTable() {
        selectComplexValueCheckTab();
        waitForPageToLoad();
        return getDefinitionsTable(COMPLEX_VALUE_CHECK_TABLE_ID);
    }

    public OldTable getObjectCardinalityCheckTable() {
        selectObjectCardinalityCheckTab();
        waitForPageToLoad();
        return getDefinitionsTable(OBJECT_CARDINALITY_CHECK_TABLE_ID);
    }

    public OldTable getCardinalityAndValuesCheckTable() {
        selectCardinalityAndValuesCheckTab();
        waitForPageToLoad();
        return getDefinitionsTable(CARDINALITY_AND_VALUES_CHECK_ID);
    }

    public SimpleValueCheckDefinition getSimpleValueCheckTableValues(int rowNumber) {
        OldTable table = getSimpleValueCheckTable();
        return SimpleValueCheckDefinition.builder()
                .setInterfaceName(table.getCellValue(rowNumber, CM_INTERFACE_NAME_COLUMN))
                .setState(table.getCellValue(rowNumber, STATE_COLUMN))
                .setObjectType(table.getCellValue(rowNumber, OBJECT_NAME_COLUMN))
                .setAttributeName(table.getCellValue(rowNumber, ATTRIBUTE_NAME_COLUMN))
                .setOperator(table.getCellValue(rowNumber, OPERATION_COLUMN))
                .setValue1(table.getCellValue(rowNumber, VALUE_1_COLUMN))
                .setValue2(table.getCellValue(rowNumber, VALUE_2_COLUMN))
                .build();
    }

    public ComplexValueCheckDefinition getComplexValueCheckTableValues(int rowNumber) {
        OldTable table = getComplexValueCheckTable();
        return ComplexValueCheckDefinition.builder()
                .setInterfaceName(table.getCellValue(rowNumber, CM_INTERFACE_NAME_COLUMN))
                .setState(table.getCellValue(rowNumber, STATE_COLUMN))
                .setObjectType(table.getCellValue(rowNumber, OBJECT_TYPE_COLUMN))
                .setRuleDefinitions(table.getCellValue(rowNumber, CHECKED_ATTRIBUTES_COLUMN))
                .build();
    }

    public ObjectCardinalityCheckDefinition getObjectCardinalityCheckTableValues(int rowNumber) {
        OldTable table = getObjectCardinalityCheckTable();
        return ObjectCardinalityCheckDefinition.builder()
                .setInterfaceName(table.getCellValue(rowNumber, CM_INTERFACE_NAME_COLUMN))
                .setState(table.getCellValue(rowNumber, STATE_COLUMN))
                .setParentObjectType(table.getCellValue(rowNumber, PARENT_OBJECT_TYPE_COLUMN))
                .setChildObjectType(table.getCellValue(rowNumber, CHILD_OBJECT_TYPE_COLUMN))
                .setExpectedCardinality(table.getCellValue(rowNumber, EXPECTED_CARDINALITY_COLUMN))
                .build();
    }

    public CardinalityAndValueCheckDefinition getCardinalityAndValuesCheckTableValues(int rowNumber) {
        OldTable table = getCardinalityAndValuesCheckTable();
        return CardinalityAndValueCheckDefinition.builder()
                .setInterfaceName(table.getCellValue(rowNumber, CM_INTERFACE_NAME_COLUMN))
                .setState(table.getCellValue(rowNumber, STATE_COLUMN))
                .setParentObjectType(table.getCellValue(rowNumber, PARENT_OBJECT_TYPE_COLUMN))
                .setChildObjectType(table.getCellValue(rowNumber, CHILD_OBJECT_TYPE_COLUMN))
                .setRuleDefinitions(table.getCellValue(rowNumber, CHECKED_ATTRIBUTES_COLUMN))
                .setExpectedCardinality(table.getCellValue(rowNumber, EXPECTED_CARDINALITY_COLUMN))
                .build();
    }

    private TreeWidget getTreeView() {
        return TreeWidget.createById(driver, wait, TREE_VIEW_ID);
    }

    private void expandNoFiltering() {
        getTreeView().expandTreeRow(NO_FILTERING_LABEL);
        waitForPageToLoad();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private Wizard getDeleteGroupWizard() {
        return Wizard.createByComponentId(driver, wait, DELETE_WIZARD_ID);
    }

    private void selectTab(String tabId) {
        TabsWidget.createById(driver, wait, TABS_ID).selectTabById(tabId);
    }

    private OldTable getDefinitionsTable(String tableId) {
        return OldTable.createById(driver, wait, tableId);
    }
}
