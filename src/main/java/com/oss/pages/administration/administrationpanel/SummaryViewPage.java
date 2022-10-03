package com.oss.pages.administration.administrationpanel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Step;

import static com.oss.serviceClient.ServicesClient.BASIC_URL;

public class SummaryViewPage extends BaseAdminPanelPage {

    private static final String SUMMARY_PAGE_URL = String.format("%s/#/view/admin/summary", BASIC_URL);
    private static final String ADMIN_PROPERTY_PANEL_ID = "ADMINISTRATION_BASIC_PROPERTY_PANEL_APP_ID";
    private static final String HOSTS_TABLE_ID = "ADMINISTRATION_HOSTS_TABLE_APP_ID";
    private static final String HELP_BUTTON_ID = "SUMMARY_HELP_ACTION_ID";
    private static final String HTML_EDITOR_ID = "HELP_HTML_EDITOR_READ_ONLY_COMPONENT_ID";
    private static final String HELP_HEADER_TEXT = "Administration - Summary Help";

    public SummaryViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static SummaryViewPage goToSummaryPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        goToPage(driver, wait, SUMMARY_PAGE_URL);
        return new SummaryViewPage(driver, wait);
    }

    public String getValueFromPanel(String propertyName) {
        return getValueFromPanel(ADMIN_PROPERTY_PANEL_ID, propertyName);
    }

    public void clickLinkFromPanel(String propertyName) {
        clickLinkFromPanel(ADMIN_PROPERTY_PANEL_ID, propertyName);
    }

    @Step("Check if chosen application button leads to url with name: {expectedSuffix}")
    public boolean isChosenUrlOpen(String expectedSuffix) {
        log.info("Check if current url contains text {}", expectedSuffix);
        return driver.getCurrentUrl().contains(expectedSuffix);
    }

    @Step("Check if chosen application button leads to page with title: {expectedTitle}")
    public boolean isExpectedTitleDisplayed(String expectedTitle) {
        log.info("Check if currently opened page has title {}", expectedTitle);
        return driver.getTitle().contains(expectedTitle);
    }

    @Step("Return to previous view")
    public void returnToPreviousView() {
        log.info("Return to previous view");
        driver.navigate().back();
    }

    public void clickHelp() {
        callActionInTable(HOSTS_TABLE_ID, HELP_BUTTON_ID);
    }

    public boolean isTextInHelp() {
        return isTextInHelp(HTML_EDITOR_ID, HELP_HEADER_TEXT);
    }

    @Step("Click Refresh in Hosts table")
    public void clickRefreshInHostsTable() {
        clickRefreshInTable(HOSTS_TABLE_ID);
        log.info("Clicking Refresh button in Hosts table");
    }

    @Step("Check if Hosts table is empty")
    public boolean isHostsTableEmpty() {
        return isTableEmpty(HOSTS_TABLE_ID);
    }

    @Step("Change first column in Hosts table")
    public void changeFirstColumnInHostsTable(String columnLabel) {
        changeFirstColumn(HOSTS_TABLE_ID, columnLabel);
        log.info("Change column {} to be first in hosts table", columnLabel);
    }

    @Step("Check first column label in Hosts table")
    public String getFirstColumnLabelInHostsTable() {
        log.info("Check first column label in Hosts table");
        return getFirstColumnLabel(HOSTS_TABLE_ID);
    }
}