package com.oss.pages.administration.administrationpanel;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.oss.serviceClient.ServicesClient.BASIC_URL;

public class ServicesPage extends BaseAdminPanelPage {

    private static final String SERVICES_PAGE_URL = String.format("%s/#/view/admin/services", BASIC_URL);
    private static final String SERVICES_TABLE_ID = "ADMINISTRATION_SERVICES_TABLE_APP_ID";
    private static final String HELP_BUTTON_ID = "SERVICES_HELP_ACTION_ID";
    private static final String HTML_EDITOR_ID = "HELP_HTML_EDITOR_READ_ONLY_COMPONENT_ID";
    private static final String TEXT_IN_HELP = "Administration - Services Help";
    private static final String SERVICE_NAME_COLUMN_LABEL = "Service Name";
    private static final String MODULE_NAME_COLUMN_LABEL = "Module Name";

    public ServicesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static ServicesPage goToServicesPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(90));
        goToPage(driver, wait, SERVICES_PAGE_URL);
        return new ServicesPage(driver, wait);
    }

    public void clickHelp() {
        callActionInTable(SERVICES_TABLE_ID, HELP_BUTTON_ID);
        log.info("Clicking Help button");
    }

    public boolean isTextInHelp() {
        return isTextInHelp(HTML_EDITOR_ID, TEXT_IN_HELP);
    }

    public boolean isServicesTableEmpty() {
        return isTableEmpty(SERVICES_TABLE_ID);
    }

    public void clickRefresh() {
        clickRefreshInTable(SERVICES_TABLE_ID);
    }

    public void searchForServiceName(String serviceName) {
        setColumnFilter(SERVICES_TABLE_ID, SERVICE_NAME_COLUMN_LABEL, serviceName);
    }

    public void searchForModuleName(String moduleName) {
        setColumnFilter(SERVICES_TABLE_ID, MODULE_NAME_COLUMN_LABEL, moduleName);
    }

    public String getValueFromFirstRow(String columnLabel) {
        return getFirstValueFromTable(SERVICES_TABLE_ID, columnLabel);
    }

    public void goToNextPage() {
        goToNextPage(SERVICES_TABLE_ID);
    }
}
