package com.oss.pages.iaa.acd.settingsview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.iaa.acd.BaseACDPage;

import io.qameta.allure.Step;

public class NotificationManagementPage extends BaseACDPage {

    private static final Logger log = LoggerFactory.getLogger(NotificationManagementPage.class);

    private static final String NOTIFICATION_MANAGEMENT_TABLE_ID = "notificationManagementTableId";
    private static final String ADVANCED_SEARCH_ID = "advanced-search_component";

    public NotificationManagementPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open Settings View")
    public static NotificationManagementPage goToPage(WebDriver driver, String suffixURL, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 150);

        String pageUrl = String.format(suffixURL, basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new NotificationManagementPage(driver, wait);
    }

    @Step("I get name of the first rule in table")
    public String getRuleName() {
        return getNotificationManagementTable().getCellValue(0, "Notification Name");
    }

    @Step("I get current status of the rule")
    public String getRuleStatus() {
        return getNotificationManagementTable().getCellValue(0, "Notification Status");
    }

    @Step("I get current name of the rule")
    public String getNotificationRuleName() {
        return getNotificationManagementTable().getCellValue(0, "Notification Name");
    }

    @Step("I check if there is data in Notification Management table")
    public Boolean isDataInNotificationRulesTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getNotificationManagementTable();
        log.info("I check if there is data in table");
        return !getNotificationManagementTable().hasNoData();
    }

    @Step("I select first rule from table")
    public void selectFirstNotificationRuleFromTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I select first Notification rule from table");
        OldTable.createById(driver, wait, NOTIFICATION_MANAGEMENT_TABLE_ID).selectRow(0);
    }

    @Step("I clear filters")
    public void clearFilters() {
        DelayUtils.waitForPageToLoad(driver, wait);
        AdvancedSearch.createByClass(driver, wait, ADVANCED_SEARCH_ID).clearAllFilters();
        log.info("I clear set filter");
    }

    @Step("I confirm changes")
    public void confirmChanges(String buttonLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ConfirmationBox.create(driver, wait).clickButtonByLabel(buttonLabel);
        log.info("Changes have been confirmed");
    }

    private OldTable getNotificationManagementTable() {
        return OldTable.createById(driver, wait, NOTIFICATION_MANAGEMENT_TABLE_ID);
    }
}