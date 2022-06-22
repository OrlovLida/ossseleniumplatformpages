package com.oss.acd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.acd.BaseACDPage;
import com.oss.pages.acd.settingsview.AssignmentManagementPage;

import io.qameta.allure.Description;

public class AssignmentManagementTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(AssignmentManagementTest.class);

    private AssignmentManagementPage assignmentManagementPage;
    private BaseACDPage baseACDPage;

    private final String systemSettingsViewSuffixUrl = "%s/#/view/acd/systemSettings";
    private final String DISTRIBUTION_NAME_FIELD_ID = "distributionNameFieldId";
    private final String DISTRIBUTION_NAME_VALUE = "SELENIUM_Distribution_Name_01";
    private final String STATUS_COMBOBOX_ID = "statusComboBoxId";
    private final String ASSIGNMENT_TYPE_COMBOBOX_ID = "assignmentTypeComboBoxId";
    private final String SEARCHING_CLASS_COMBOBOX_ID = "searchingClassFieldId";
    private final String QUERY_STRING_FIELD_ID = "queryStringFieldId";
    private final String QUERY_STRING_FIELD_VALUE = "Test_Query_String";
    private final String SCENARIO_COMBOBOX_ID = "scenarioComboBoxId";
    private final String RULE_ORDER_COMBOBOX_ID = "ruleOrderSearchId";
    private final String ASSIGNED_GROUP_COMBOBOX_ID = "assignedGroupComboBoxId";
    private final String ASSIGNED_USER_COMBOBOX_ID = "assignedUserComboBoxId";
    private final String DISTRIBUTION_COMBOBOX_ID = "distribution_name";
    private final String DISTRIBUTION_NAME_VALUE_V2 = "Selenium_Distribution_Name_02";
    private static final String EDIT_RULE_BUTTON = "assignmentManagementButtonId-3";
    private static final String DELETE_RULE_BUTTON = "assignmentManagementButtonId-2";
    private static final String CHANGE_STATUS_BUTTON = "assignmentManagementButtonId-1";

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
        baseACDPage.setAttributeValue(STATUS_COMBOBOX_ID, "Active");
        baseACDPage.setAttributeValue(ASSIGNMENT_TYPE_COMBOBOX_ID, "Early");
        baseACDPage.setAttributeValue(SEARCHING_CLASS_COMBOBOX_ID, "Root Classification");
        baseACDPage.setAttributeValue(QUERY_STRING_FIELD_ID, QUERY_STRING_FIELD_VALUE);
        baseACDPage.setAttributeValue(SCENARIO_COMBOBOX_ID, "APD");
        baseACDPage.setAttributeValue(RULE_ORDER_COMBOBOX_ID, "1");
        baseACDPage.setAttributeValue(ASSIGNED_GROUP_COMBOBOX_ID, "APD SysAdmin");
        baseACDPage.setAttributeValue(ASSIGNED_USER_COMBOBOX_ID, "acdadmin");
        log.info("Form has been completed. I try to save it.");
        try {
            assignmentManagementPage.clickButtonByLabel("Save");
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
            log.error("Assignment Management  table doesn't contain data for provided filters");
            Assert.fail();
        }
        log.info("Assignment Management rule has been created with ID: {}", assignmentManagementPage.getNewRuleId());
        Assert.assertFalse(assignmentManagementPage.getNewRuleId().isEmpty());
    }

    @Test(priority = 3, testName = "Edit Assignment Management rule", description = "Edit Assignment Management rule")
    @Description("Edit Assignment Management rule")
    public void editActionTemplate() {
        assignmentManagementPage.setAttributeValue("scenario", "APD");
        assignmentManagementPage.setAttributeValue("distribution_name", DISTRIBUTION_NAME_VALUE);
        assignmentManagementPage.selectFirstRuleFromTable();
        assignmentManagementPage.clickTabsContainerButton(EDIT_RULE_BUTTON);
        baseACDPage.setValueInTextField(DISTRIBUTION_NAME_FIELD_ID, DISTRIBUTION_NAME_VALUE_V2);
        assignmentManagementPage.clickButtonByLabel("Save");
        assignmentManagementPage.setAttributeValue("scenario", "APD");
        Assert.assertEquals(assignmentManagementPage.getRuleName(), DISTRIBUTION_NAME_VALUE_V2);
    }

    @Test(priority = 4, testName = "Delete Assignment Management rule", description = "Delete Assignment Management rule")
    @Description("Delete Assignment Management rule")
    public void deleteActionTemplate() {
        assignmentManagementPage.setAttributeValue("scenario", "APD");
        assignmentManagementPage.setAttributeValue("distribution_name", DISTRIBUTION_NAME_VALUE_V2);
        assignmentManagementPage.selectFirstRuleFromTable();
        assignmentManagementPage.deleteRule(DELETE_RULE_BUTTON);
        assignmentManagementPage.searchingThroughTable(DISTRIBUTION_COMBOBOX_ID, DISTRIBUTION_NAME_VALUE_V2);
        assignmentManagementPage.setAttributeValue("scenario", "APD");
        Assert.assertFalse(assignmentManagementPage.isDataInAssignmentManagementTable());
    }
}