package com.oss.pages.administration.administrationpanel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.untils.FileDownload;

import io.qameta.allure.Step;

import java.time.Duration;

import static com.oss.serviceClient.ServicesClient.BASIC_URL;

public class ModulesPage extends BaseAdminPanelPage {

    private static final String MODULES_PAGE_URL = String.format("%s/#/view/admin/modules", BASIC_URL);
    private static final String MODULES_TABLE_ID = "ADMINISTRATION_MODULES_TABLE_APP_ID";
    private static final String INSTANCE_TABLE_ID = "ADMINISTRATION_MODULE_INSTANCES_TABLE_APP_ID";
    private static final String HELP_BUTTON_ID = "MODULES_HELP_ACTION_ID";
    private static final String TEXT_IN_HELP = "Administration - Modules Help";
    private static final String HTML_EDITOR_HELP_ID = "HELP_HTML_EDITOR_READ_ONLY_COMPONENT_ID";
    private static final String MODULE_NAME_COLUMN_LABEL = "Module Name";
    private static final String PORTS_COLUMN_LABEL = "Ports";
    private static final String LOGS_BUTTON_ID = "MODULE_GET_LOGS_ACTION";

    public ModulesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static ModulesPage goToModulesPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver,  Duration.ofSeconds(90));
        goToPage(driver, wait, MODULES_PAGE_URL);
        return new ModulesPage(driver, wait);
    }

    public void clickHelp() {
        callActionInTable(MODULES_TABLE_ID, HELP_BUTTON_ID);
        log.info("Clicking Help button");
    }

    public boolean isTextInHelp() {
        return isTextInHelp(HTML_EDITOR_HELP_ID, TEXT_IN_HELP);
    }

    public boolean isModulesTableEmpty() {
        return isTableEmpty(MODULES_TABLE_ID);
    }

    public boolean isInstancesTableEmpty() {
        return isTableEmpty(INSTANCE_TABLE_ID);
    }

    public void clickRefreshInModulesTable() {
        clickRefreshInTable(MODULES_TABLE_ID);
    }

    public void clickRefreshInInstanceTable() {
        clickRefreshInTable(INSTANCE_TABLE_ID);
    }

    public void searchInModulesTable(String moduleName) {
        searchInTable(MODULES_TABLE_ID, moduleName);
    }

    public String getFirstModuleNameFromModulesTable() {
        return getFirstValueFromTable(MODULES_TABLE_ID, MODULE_NAME_COLUMN_LABEL);
    }

    public String getFirstModuleNameFromInstanceTable() {
        return getFirstValueFromTable(INSTANCE_TABLE_ID, MODULE_NAME_COLUMN_LABEL);
    }

    public void selectFirstRowInModulesTable() {
        selectFirstRowInTable(MODULES_TABLE_ID);
    }

    public void searchForPortsInInstanceTable(String portNumber) {
        setColumnFilter(INSTANCE_TABLE_ID, PORTS_COLUMN_LABEL, portNumber);
    }

    public String getPortsFromFirstRowInInstanceTable() {
        return getFirstValueFromTable(INSTANCE_TABLE_ID, PORTS_COLUMN_LABEL);
    }

    @Step("Click Logs button")
    public LogsWizardPage clickLogsButton() {
        callActionInTable(MODULES_TABLE_ID, LOGS_BUTTON_ID);
        return new LogsWizardPage(driver, wait);
    }

    @Step("Download Logs from Notifications Panel")
    public void downloadLogFromNotifications() {
        openNotificationPanel()
                .waitForExportFinish()
                .clickDownload();
        openNotificationPanel()
                .clearNotifications();
    }

    public boolean checkIfFileIsNotEmpty(String fileName) {
        log.info("Checking if file is not empty");
        return FileDownload.checkIfFileIsNotEmpty(fileName);
    }
}
