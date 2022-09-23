package com.oss.pages.iaa.acd.settingsview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.iaa.acd.BaseACDPage;

import io.qameta.allure.Step;

public class NotificationManagementPage extends BaseACDPage {

    private static final Logger log = LoggerFactory.getLogger(NotificationManagementPage.class);

    private static final String NOTIFICATION_MANAGEMENT_TABLE_ID = "notificationManagementTableId";
    private static final String SYSTEM_SETTINGS_VIEW_SUFFIX = "%s/#/view/acd/systemSettings";
    private static final String ADD_NOTIFICATION_BUTTON = "notificationManagementButtonId-3";
    private static final String EDIT_NOTIFICATION_BUTTON_ID = "notificationManagementButtonId-2";
    private static final String CHANGE_STATUS_BUTTON_ID = "notificationManagementButtonId-0";
    private static final String DELETE_NOTIFICATION_BUTTON_ID = "notificationManagementButtonId-1";
    private static final String SEARCH_NAME_ID = "notification_name";
    private static final String DELETE_LABEL = "Delete";
    private static final String CHANGE_LABEL = "Change";

    public NotificationManagementPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open Settings View")
    public static NotificationManagementPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 150);

        String pageUrl = String.format(SYSTEM_SETTINGS_VIEW_SUFFIX, basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new NotificationManagementPage(driver, wait);
    }

    @Step("I get name of the first rule in table")
    public String getFirstRuleName() {
        return getNotificationManagementTable().getCellValue(0, "Notification Name");
    }

    @Step("I get current status of the rule")
    public String getFirstRuleStatus() {
        return getNotificationManagementTable().getCellValue(0, "Notification Status");
    }

    @Step("I check if there is data in Notification Management table")
    public Boolean isDataInNotificationRulesTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I check if there is data in table");
        return !getNotificationManagementTable().hasNoData();
    }

    @Step("I select first rule from table")
    public void selectFirstNotificationRuleFromTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I select first Notification rule from table");
        getNotificationManagementTable().selectRow(0);
    }

    @Step("I clear filters")
    public void clearFilters() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getNotificationManagementTable().clearFilters();
        log.info("I clear set filter");
    }

    @Step("I confirm changes")
    public void confirmChanges() {
        ConfirmationBox.create(driver, wait).clickButtonByLabel(CHANGE_LABEL);
        log.info("Changes have been confirmed");
    }

    @Step("I confirm delete")
    public void confirmDelete() {
        ConfirmationBox.create(driver, wait).clickButtonByLabel(DELETE_LABEL);
        log.info("Deletion have been confirmed");
    }

    @Step("Click add notification")
    public NotificationWizardPage clickAddNotification() {
        clickContextButton(ADD_NOTIFICATION_BUTTON);
        log.info("Click add notification");
        return new NotificationWizardPage(driver, wait);
    }

    @Step("Click edit notification")
    public NotificationWizardPage clickEditNotification() {
        clickContextButton(EDIT_NOTIFICATION_BUTTON_ID);
        log.info("Click edit notification");
        return new NotificationWizardPage(driver, wait);
    }

    @Step("Click change status")
    public void clickChangeStatus() {
        clickContextButton(CHANGE_STATUS_BUTTON_ID);
        log.info("Click change status");
    }

    @Step("Click delete notification")
    public void clickDeleteNotification() {
        clickContextButton(DELETE_NOTIFICATION_BUTTON_ID);
        log.info("Click delete notification");
    }

    @Step("Search in Notification Management Table by Name")
    public void searchByName(String name) {
        getNotificationManagementTable().searchByAttribute(SEARCH_NAME_ID, name);
        log.info("Search by name");
    }

    private OldTable getNotificationManagementTable() {
        return OldTable.createById(driver, wait, NOTIFICATION_MANAGEMENT_TABLE_ID);
    }
}