package com.oss.pages.administration.administrationpanel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Step;

import static com.oss.transport.infrastructure.ServicesClient.BASIC_URL;

public class ElasticPage extends BaseAdminPanelPage {

    private static final String ELASTIC_PAGE_URL = String.format("%s/#/view/admin/elastic", BASIC_URL);
    private static final String ELASTIC_TABLE_ID = "ADMINISTRATION_ELASTIC_WINDOW_ID";
    private static final String HELP_BUTTON_ID = "ELASTIC_HELP_ACTION_ID";
    private static final String HTML_EDITOR_ID = "HELP_HTML_EDITOR_READ_ONLY_COMPONENT_ID";
    private static final String HELP_HEADER_TEXT = "Administration - Elastic Help";
    private static final String ADMIN_ELASTIC_PROPERTY_PANEL_ID = "ADMINISTRATION_ELASTIC_PROPERTY_PANEL_APP_ID";

    public ElasticPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static ElasticPage goToElasticPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        goToPage(driver, wait, ELASTIC_PAGE_URL);
        return new ElasticPage(driver, wait);
    }

    public void clickHelp() {
        callActionInTable(ELASTIC_TABLE_ID, HELP_BUTTON_ID);
        log.info("Clicking Help button in Elastic table");
    }

    public boolean isTextInHelp() {
        return isTextInHelp(HTML_EDITOR_ID, HELP_HEADER_TEXT);
    }

    @Step("Click Refresh in Elastic table")
    public void clickRefreshInElasticTable() {
        clickRefreshInTable(ELASTIC_TABLE_ID);
        log.info("Clicking Refresh button in Elastic table");
    }

    public boolean isElasticTableEmpty() {
        log.info("Check if Elastic table is empty");
        return isTableEmpty(ELASTIC_TABLE_ID);
    }

    @Step("Change first column in Elastic table")
    public void changeFirstColumnInElasticTable(String columnLabel) {
        changeFirstColumn(ELASTIC_TABLE_ID, columnLabel);
        log.info("Change column {} to be first in Elastic table", columnLabel);
    }

    @Step("Check first column label in Elastic table")
    public String getFirstColumnLabelInElasticTable() {
        log.info("Check first column label in Elastic table");
        return getFirstColumnLabel(ELASTIC_TABLE_ID);
    }

    public String getValueFromPanel(String propertyName) {
        return getValueFromPanel(ADMIN_ELASTIC_PROPERTY_PANEL_ID, propertyName);
    }
}