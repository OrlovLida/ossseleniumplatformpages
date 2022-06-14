package com.oss.acd;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.acd.settingsview.NotificationManagementPage;

import io.qameta.allure.Description;

public class NotificationManagementTest extends BaseTestCase {

    private static final String date = new SimpleDateFormat("dd-MM-yyyy_HH:mm").format(new Date());
    private static final Logger log = LoggerFactory.getLogger(NotificationManagementTest.class);

    private NotificationManagementPage notificationManagementPage;

    private static final String systemSettingsViewSuffixUrl = "%s/#/view/acd/systemSettings";
    private static final String SYSTEM_SETTINGS_TAB_ID = "systemTabsContainerId";
    private static final String MANAGEMENT_TAB = "Notification Management";
    private static final String ADD_NOTIFICATION_BUTTON = "notificationManagementButtonId-3";
    private static final String NAME_FIELD_ID = "notificationNameId";
    private static final String NAME_FIELD_VALUE = "SELENIUM_NOTIFICATION";
    private static final String EDITED_NAME_FIELD_VALUE = "SELENIUM_NOTIFICATION_EDITED";
    private static final String STATUS_COMBOBOX_ID = "notificationStatusId";
    private static final String STATUS_COMBOBOX_VALUE = "Active";
    private static final String SEND_TYPE_COMBOBOX_ID = "notificationSendTypeId";
    private static final String SEND_TYPE_COMBOBOX_VALUE = "Early";
    private static final String CREATION_TYPE_COMBOBOX_ID = "notificationIssueCreationTypeId";
    private static final String CREATION_TYPE_COMBOBOX_VALUE = "Automatically";
    private static final String NOTIFICATION_TYPE_COMBOBOX_ID = "notificationTypeId";
    private static final String NOTIFICATION_TYPE_COMBOBOX_VALUE = "Mail";
    private static final String TITLE_ID = "notificationMessageTitleId";
    private static final String TITLE_VALUE = "This is SELENIUM title";
    private static final String RECIPIENT_ID = "notificationMessageAddressId";
    private static final String RECIPIENT_VALUE = "weronika.pawlowicz@comarch.com";
    private static final String BODY_ID = "notificationMessageBodyId";
    private static final String BODY_VALUE = "This is SELENIUM notification body";
    private static final String SCENARIO_ID = "notificationScenarioId";
    private static final String SCENARIO_VALUE = "APD";
    private static final String EVENT_TYPE_ID = "notificationEventTypeId";
    private static final String EVENT_TYPE_VALUE = "Issue Detected";
    private static final String SEARCHING_CLASS_ID = "notificationSearchingClassId";
    private static final String SEARCHING_CLASS_VALUE = "Root Classification";
    private static final String QUERY_STRING_ID = "notificationQueryStringId";
    private static final String QUERY_STRING_VALUE = "Test_String";
    private static final String SAVE_RULE_BUTTON_LABEL = "Save";
    private static final String SHOW_FILTER_BUTTON_ID = "filters-panel-button";
    private static final String SEARCH_NAME_ID = "notification_name";
    private static final String APPLY_FILTER_BUTTON_LABEL = "Apply";
    private static final String EDIT_NOTIFICATION_BUTTON_ID = "notificationManagementButtonId-2";
    private static final String CHANGE_STATUS_BUTTON_ID = "notificationManagementButtonId-0";
    private static final String DELETE_STATUS_BUTTON_ID = "notificationManagementButtonId-1";

    @BeforeClass
    public void goToSystemSettingsView() {
        notificationManagementPage = NotificationManagementPage.goToPage(driver, systemSettingsViewSuffixUrl, BASIC_URL);
    }

    @Test(priority = 1, testName = "Add new Notification rule", description = "Add new Notification rule")
    @Description("Add new Notification rule")
    public void addNewNotificationRule() {
        notificationManagementPage.goToTab(SYSTEM_SETTINGS_TAB_ID, MANAGEMENT_TAB);
        notificationManagementPage.clickContextButton(ADD_NOTIFICATION_BUTTON);
        notificationManagementPage.setAttributeValue(NAME_FIELD_ID, NAME_FIELD_VALUE + '_' + date.replace(":", "_"));
        notificationManagementPage.setAttributeValue(STATUS_COMBOBOX_ID, STATUS_COMBOBOX_VALUE);
        notificationManagementPage.setAttributeValue(SEND_TYPE_COMBOBOX_ID, SEND_TYPE_COMBOBOX_VALUE);
        notificationManagementPage.setAttributeValue(CREATION_TYPE_COMBOBOX_ID, CREATION_TYPE_COMBOBOX_VALUE);
        notificationManagementPage.setAttributeValue(NOTIFICATION_TYPE_COMBOBOX_ID, NOTIFICATION_TYPE_COMBOBOX_VALUE);
        notificationManagementPage.setAttributeValue(TITLE_ID, TITLE_VALUE);
        notificationManagementPage.setAttributeValue(RECIPIENT_ID, RECIPIENT_VALUE);
        notificationManagementPage.setAttributeValue(BODY_ID, BODY_VALUE);
        notificationManagementPage.setAttributeValue(SCENARIO_ID, SCENARIO_VALUE);
        notificationManagementPage.setAttributeValue(EVENT_TYPE_ID, EVENT_TYPE_VALUE);
        notificationManagementPage.setAttributeValue(SEARCHING_CLASS_ID, SEARCHING_CLASS_VALUE);
        notificationManagementPage.setAttributeValue(QUERY_STRING_ID, QUERY_STRING_VALUE);
        log.info("Form has been completed. I try to save it.");
        try {
            notificationManagementPage.clickButtonByLabel(SAVE_RULE_BUTTON_LABEL);
            log.info("I clicked Save button");
        } catch (Exception e) {
            log.info("I couldn't click Save button");
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Test(priority = 2, testName = "Verify if created Notification rule exists", description = "Verify if created Notification rule exists")
    @Description("Verify if created Notification rule exists")
    public void searchForNewRule() {

        notificationManagementPage.clickContextButton(SHOW_FILTER_BUTTON_ID);
        notificationManagementPage.setAttributeValue(SEARCH_NAME_ID, NAME_FIELD_VALUE);

        try {
            notificationManagementPage.clickButtonByLabel(APPLY_FILTER_BUTTON_LABEL);
            Assert.assertTrue(notificationManagementPage.getRuleName().contains(NAME_FIELD_VALUE));

        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Test(priority = 3, testName = "Edit Notification Rule", description = "Edit Notification Rule")
    @Description("Edit Notification Rule")
    public void editNotificationRule() {

        try {
            notificationManagementPage.selectFirstNotificationRuleFromTable();
            notificationManagementPage.clickContextButton(EDIT_NOTIFICATION_BUTTON_ID);
            notificationManagementPage.setAttributeValue(NAME_FIELD_ID, EDITED_NAME_FIELD_VALUE);
            notificationManagementPage.clickButtonByLabel(SAVE_RULE_BUTTON_LABEL);
            Assert.assertTrue(notificationManagementPage.getRuleName().contains(EDITED_NAME_FIELD_VALUE));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Test(priority = 4, testName = "Change status of Notification Rule", description = "Change status of Notification Rule")
    @Description("Change status of Notification Rule")
    public void changeStatusOfRule() {

        if (notificationManagementPage.getRuleName().contains(NAME_FIELD_VALUE + "_V2")) {
            try {
                notificationManagementPage.selectFirstNotificationRuleFromTable();
                notificationManagementPage.clickContextButton(CHANGE_STATUS_BUTTON_ID);
                notificationManagementPage.confirmChanges("Change");
                Assert.assertTrue(notificationManagementPage.getRuleStatus().equals("Inactive"));
            } catch (Exception e) {
                log.error(e.getMessage());
                Assert.fail();
            }
        } else {
            log.info("Name of notification rule is different than the one created by Selenium");
            Assert.fail();
        }
    }

    @Test(priority = 5, testName = "Delete Notification Rule", description = "Delete Notification Rule")
    @Description("Delete Notification Rule")
    public void deleteRule() {

        if (notificationManagementPage.getRuleName().contains(NAME_FIELD_VALUE + "_V2")) {
            try {
                notificationManagementPage.selectFirstNotificationRuleFromTable();
                notificationManagementPage.clickContextButton(DELETE_STATUS_BUTTON_ID);
                notificationManagementPage.confirmChanges("Delete");
                Assert.assertFalse(notificationManagementPage.isDataInNotificationRulesTable());
                log.info("Selected rule has been deleted");
                notificationManagementPage.clearFilters();
                log.info("Filters have been cleared");
            } catch (Exception e) {
                log.info("Rule couldn't be deleted or filters couldn't be cleared");
                log.error(e.getMessage());
                Assert.fail();
            }
        } else {
            log.info("Name of notification rule is different than the one created by Selenium");
            Assert.fail();
        }
    }
}