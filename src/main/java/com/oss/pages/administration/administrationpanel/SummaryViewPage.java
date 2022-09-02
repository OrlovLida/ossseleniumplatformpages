package com.oss.pages.administration.administrationpanel;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;

import io.qameta.allure.Step;

import static com.oss.serviceClient.ServicesClient.BASIC_URL;

public class SummaryViewPage extends BaseAdminPanelPage {

    private static final Logger log = LoggerFactory.getLogger(SummaryViewPage.class);
    private static final String SUMMARY_PAGE_URL = String.format("%s/#/view/admin/summary", BASIC_URL);
    private static final String ADMIN_PROPERTY_PANEL_ID = "ADMINISTRATION_BASIC_PROPERTY_PANEL_APP_ID";
    private static final String HOSTS_TABLE_ID = "ADMINISTRATION_HOSTS_TABLE_APP_ID";
    private static final String HELP_BUTTON_ID = "SUMMARY_HELP_ACTION_ID";
    private static final String HTML_EDITOR_ID = "HELP_HTML_EDITOR_READ_ONLY_COMPONENT_ID";
    private static final String HELP_HEADER_TEXT = "Administration - Summary Help";

    private static final String NO_SUCH_ELEMENT_EXCEPTION = "Chosen Property is not visible";

    public SummaryViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static SummaryViewPage goToSummaryPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        goToPage(driver, wait, SUMMARY_PAGE_URL);
        return new SummaryViewPage(driver, wait);
    }

    @Step("Get value for {propertyName}")
    public String getValueFromPanel(String propertyName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        if (checkPropertyVisibility(propertyName)) {
            return getAdminPropertyPanel().getPropertyValue(propertyName);
        } else {
            throw new NoSuchElementException(NO_SUCH_ELEMENT_EXCEPTION);
        }
    }

    @Step("Click link from Panel")
    public void clickLinkFromPanel(String propertyName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getAdminPropertyPanel().clickLink(propertyName);
        log.info("Click Link to {}", propertyName);
    }

    public boolean checkPropertyVisibility(String propertyName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getVisibleAttributes().contains(propertyName);
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

    @Step("Click Help button")
    public void clickHelp() {
        callActionInTable(HOSTS_TABLE_ID, HELP_BUTTON_ID);
    }

    @Step("Check if text in Help is displayed")
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

    public List<String> getVisibleAttributes() {
        return getAdminPropertyPanel().getVisibleAttributes();
    }

    private OldPropertyPanel getAdminPropertyPanel() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldPropertyPanel.createById(driver, wait, ADMIN_PROPERTY_PANEL_ID);
    }
}