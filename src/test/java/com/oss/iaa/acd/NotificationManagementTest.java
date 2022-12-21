package com.oss.iaa.acd;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.acd.settingsview.NotificationManagementPage;
import com.oss.pages.iaa.acd.settingsview.NotificationWizardPage;

import io.qameta.allure.Description;

public class NotificationManagementTest extends BaseTestCase {

    private static final String date = new SimpleDateFormat("dd-MM-yyyy_HH:mm").format(new Date());

    private NotificationManagementPage notificationManagementPage;

    private static final String SYSTEM_SETTINGS_TAB_ID = "systemTabsContainerId";
    private static final String MANAGEMENT_TAB = "Notification Management";
    private static final String NAME_FIELD_VALUE = "SELENIUM_NOTIFICATION";
    private static final String EDITED_NAME_FIELD_VALUE = "SELENIUM_NOTIFICATION_EDITED";
    private static final String STATUS_COMBOBOX_VALUE = "Active";
    private static final String CHANGED_STATUS_COMBOBOX_VALUE = "Inactive";
    private static final String SEND_TYPE_COMBOBOX_VALUE = "Early";
    private static final String CREATION_TYPE_COMBOBOX_VALUE = "Automatically";
    private static final String NOTIFICATION_TYPE_COMBOBOX_VALUE = "Mail";
    private static final String TITLE_VALUE = "This is SELENIUM title";
    private static final String RECIPIENT_VALUE = "weronika.pawlowicz@comarch.com";
    private static final String BODY_VALUE = "This is SELENIUM notification body";
    private static final String SCENARIO_VALUE = "ASD";
    private static final String EVENT_TYPE_VALUE = "Issue Detected";
    private static final String SEARCHING_CLASS_VALUE = "Root Classification";
    private static final String QUERY_STRING_VALUE = "Test_String";
    private final String notificationRuleName = NAME_FIELD_VALUE + '_' + date.replace(":", "_");
    private final String editedNotificationRuleName = EDITED_NAME_FIELD_VALUE + '_' + date.replace(":", "_");

    @BeforeMethod
    public void goToSystemSettingsView() {
        notificationManagementPage = NotificationManagementPage.goToPage(driver, BASIC_URL);
        notificationManagementPage.goToTab(SYSTEM_SETTINGS_TAB_ID, MANAGEMENT_TAB);
    }

    @Test(priority = 1, testName = "Add new Notification rule", description = "Add new Notification rule")
    @Description("Add new Notification rule")
    public void addNewNotificationRule() {
        NotificationWizardPage notificationWizardPage = notificationManagementPage.clickAddNotification();
        fillNotificationWizard();
        notificationWizardPage.clickSaveButton();
        notificationManagementPage.searchByName(notificationRuleName);
        Assert.assertTrue(notificationManagementPage.getFirstRuleName().contains(notificationRuleName));
    }

    @Test(priority = 2, testName = "Edit Notification Rule", description = "Edit Notification Rule")
    @Description("Edit Notification Rule")
    public void editNotificationRule() {
        notificationManagementPage.searchByName(notificationRuleName);
        notificationManagementPage.selectFirstNotificationRuleFromTable();
        NotificationWizardPage notificationWizardPage = notificationManagementPage.clickEditNotification();
        notificationWizardPage.setName(editedNotificationRuleName);
        notificationWizardPage.clickSaveButton();
        notificationManagementPage.clearFilters();
        notificationManagementPage.searchByName(editedNotificationRuleName);
        Assert.assertTrue(notificationManagementPage.getFirstRuleName().contains(editedNotificationRuleName));
    }

    @Test(priority = 3, testName = "Change status of Notification Rule", description = "Change status of Notification Rule")
    @Description("Change status of Notification Rule")
    public void changeStatusOfRule() {
        notificationManagementPage.searchByName(editedNotificationRuleName);
        notificationManagementPage.selectFirstNotificationRuleFromTable();
        notificationManagementPage.clickChangeStatus();
        notificationManagementPage.confirmChanges();
        Assert.assertTrue(notificationManagementPage.getFirstRuleStatus().equals(CHANGED_STATUS_COMBOBOX_VALUE));
    }

    @Test(priority = 4, testName = "Delete Notification Rule", description = "Delete Notification Rule")
    @Description("Delete Notification Rule")
    public void deleteRule() {
        notificationManagementPage.searchByName(editedNotificationRuleName);
        notificationManagementPage.selectFirstNotificationRuleFromTable();
        notificationManagementPage.clickDeleteNotification();
        notificationManagementPage.confirmDelete();
        Assert.assertFalse(notificationManagementPage.isDataInNotificationRulesTable());
        notificationManagementPage.clearFilters();
    }

    private void fillNotificationWizard() {
        NotificationWizardPage notificationWizardPage = new NotificationWizardPage(driver, webDriverWait);
        notificationWizardPage.setName(notificationRuleName);
        notificationWizardPage.setStatus(STATUS_COMBOBOX_VALUE);
        notificationWizardPage.setSendType(SEND_TYPE_COMBOBOX_VALUE);
        notificationWizardPage.setCreationType(CREATION_TYPE_COMBOBOX_VALUE);
        notificationWizardPage.setNotificationType(NOTIFICATION_TYPE_COMBOBOX_VALUE);
        notificationWizardPage.setTitle(TITLE_VALUE);
        notificationWizardPage.setRecipient(RECIPIENT_VALUE);
        notificationWizardPage.setBody(BODY_VALUE);
        notificationWizardPage.setScenario(SCENARIO_VALUE);
        notificationWizardPage.setEventType(EVENT_TYPE_VALUE);
        notificationWizardPage.setSearchingClass(SEARCHING_CLASS_VALUE);
        notificationWizardPage.setQueryString(QUERY_STRING_VALUE);
    }
}