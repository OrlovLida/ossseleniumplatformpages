package com.oss.iaa.acd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.acd.BaseACDPage;
import com.oss.pages.iaa.acd.settingsview.AssignmentManagementPage;

import io.qameta.allure.Description;

public class AssignmentManagementTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(AssignmentManagementTest.class);

    private AssignmentManagementPage assignmentManagementPage;
    private BaseACDPage baseACDPage;

    private final String systemSettingsViewSuffixUrl = "%s/#/view/acd/systemSettings";
    private final String DISTRIBUTION_NAME_FIELD_ID = "distributionNameFieldId";
    private final String DISTRIBUTION_NAME_VALUE = "SELENIUM_Distribution_Name_01";
    private final String STATUS_COMBOBOX_ID = "statusComboBoxId";
    private final String STATUS_TYPE_VALUE = "Active";
    private final String ASSIGNMENT_TYPE_COMBOBOX_ID = "assignmentTypeComboBoxId";
    private final String ASSIGNMENT_TYPE_VALUE = "Early";
    private final String SEARCHING_CLASS_COMBOBOX_ID = "searchingClassFieldId";
    private final String SEARCHING_CLASS_VALUE = "Root Classification";
    private final String QUERY_STRING_FIELD_ID = "queryStringFieldId";
    private final String QUERY_STRING_FIELD_VALUE = "Test_Query_String";
    private final String SCENARIO_COMBOBOX_ID = "scenarioComboBoxId";
    private final String SCENARIO_VALUE = "ASD";
    private final String RULE_ORDER_COMBOBOX_ID = "ruleOrderSearchId";
    private final String RULE_ORDER_VALUE = "1";
    private final String ASSIGNED_GROUP_COMBOBOX_ID = "assignedGroupComboBoxId";
    private final String ASSIGNED_GROUP_VALUE = "ASD SysAdmin";
    private final String ASSIGNED_USER_COMBOBOX_ID = "assignedUserComboBoxId";
    private final String ASSIGNED_USER_VALUE = "acd_system";
    private final String DISTRIBUTION_COMBOBOX_ID = "distribution_name";
    private final String DISTRIBUTION_NAME_VALUE_V2 = "Selenium_Distribution_Name_02";
    private final String BUTTON_SAVE_LABEL = "Save";
    private final String SCENARIO_SEARCH_ID = "scenario";
    private final String EDIT_RULE_BUTTON = "assignmentManagementButtonId-3";
    private final String DELETE_RULE_BUTTON = "assignmentManagementButtonId-2";

    @BeforeClass
    public void goToSystemSettingsView() {
        assignmentManagementPage = AssignmentManagementPage.goToPage(driver, systemSettingsViewSuffixUrl, BASIC_URL);
        baseACDPage = new BaseACDPage(driver, webDriverWait);
    }

    @Test(priority = 1, testName = "Add new Assignment Management rule", description = "Add new Assignment Management rule")
    @Description("Add new Assignment Management rule")
    public void addNewAssignmentManagementRule() {
        assignmentManagementPage.clickCreateNewRule();
        baseACDPage.setAttributeValue(DISTRIBUTION_NAME_FIELD_ID, DISTRIBUTION_NAME_VALUE);
        baseACDPage.setAttributeValue(STATUS_COMBOBOX_ID, STATUS_TYPE_VALUE);
        baseACDPage.setAttributeValue(ASSIGNMENT_TYPE_COMBOBOX_ID, ASSIGNMENT_TYPE_VALUE);
        baseACDPage.setAttributeValue(SEARCHING_CLASS_COMBOBOX_ID, SEARCHING_CLASS_VALUE);
        baseACDPage.setAttributeValue(QUERY_STRING_FIELD_ID, QUERY_STRING_FIELD_VALUE);
        baseACDPage.setAttributeValue(SCENARIO_COMBOBOX_ID, SCENARIO_VALUE);
        baseACDPage.setAttributeValue(RULE_ORDER_COMBOBOX_ID, RULE_ORDER_VALUE);
        baseACDPage.setAttributeValue(ASSIGNED_GROUP_COMBOBOX_ID, ASSIGNED_GROUP_VALUE);
        baseACDPage.setAttributeValue(ASSIGNED_USER_COMBOBOX_ID, ASSIGNED_USER_VALUE);
        log.info("Form has been completed. I try to save it.");
        try {
            assignmentManagementPage.clickButtonByLabel(BUTTON_SAVE_LABEL);
            log.info("I clicked Save button");
        } catch (Exception e) {
            Assert.fail("I couldn't click Save button\n" + e.getMessage());
        }
    }

    @Test(priority = 2, testName = "Verify if created Assignment Management rule exists", description = "Verify if created Assignment Management rule exists")
    @Description("Verify if created Assignment Management rule exists")
    public void searchingForCreatedRule() {

        if (!assignmentManagementPage.searchingThroughTable(DISTRIBUTION_COMBOBOX_ID, DISTRIBUTION_NAME_VALUE)) {
            log.error("Assignment Management table is empty");
            Assert.fail();
        }

        if (!assignmentManagementPage.isDataInAssignmentManagementTable()) {
            log.error("Assignment Management table doesn't contain data for provided filters");
            Assert.fail();
        }
        log.info("Assignment Management rule has been created with ID: {}", assignmentManagementPage.getNewRuleId());
        Assert.assertFalse(assignmentManagementPage.getNewRuleId().isEmpty());
    }

    @Test(priority = 3, testName = "Edit Assignment Management rule", description = "Edit Assignment Management rule")
    @Description("Edit Assignment Management rule")
    public void editActionTemplate() {
        assignmentManagementPage.setAttributeValue(SCENARIO_SEARCH_ID, SCENARIO_VALUE);
        assignmentManagementPage.setAttributeValue(DISTRIBUTION_COMBOBOX_ID, DISTRIBUTION_NAME_VALUE);
        assignmentManagementPage.selectFirstRuleFromTable();
        assignmentManagementPage.clickTabsContainerButton(EDIT_RULE_BUTTON);
        baseACDPage.setValueInTextField(DISTRIBUTION_NAME_FIELD_ID, DISTRIBUTION_NAME_VALUE_V2);
        assignmentManagementPage.clickButtonByLabel(BUTTON_SAVE_LABEL);
        assignmentManagementPage.setAttributeValue(DISTRIBUTION_COMBOBOX_ID, DISTRIBUTION_NAME_VALUE_V2);
        assignmentManagementPage.setAttributeValue(SCENARIO_SEARCH_ID, SCENARIO_VALUE);
        Assert.assertEquals(assignmentManagementPage.getRuleName(), DISTRIBUTION_NAME_VALUE_V2);
    }

    @Test(priority = 4, testName = "Delete Assignment Management rule", description = "Delete Assignment Management rule")
    @Description("Delete Assignment Management rule")
    public void deleteActionTemplate() {
        assignmentManagementPage.setAttributeValue(SCENARIO_SEARCH_ID, SCENARIO_VALUE);
        assignmentManagementPage.setAttributeValue(DISTRIBUTION_COMBOBOX_ID, DISTRIBUTION_NAME_VALUE_V2);
        assignmentManagementPage.selectFirstRuleFromTable();
        assignmentManagementPage.deleteRule(DELETE_RULE_BUTTON);
        assignmentManagementPage.searchingThroughTable(DISTRIBUTION_COMBOBOX_ID, DISTRIBUTION_NAME_VALUE_V2);
        assignmentManagementPage.setAttributeValue(SCENARIO_SEARCH_ID, SCENARIO_VALUE);
        Assert.assertFalse(assignmentManagementPage.isDataInAssignmentManagementTable());
    }
}