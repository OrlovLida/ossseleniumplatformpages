package com.oss.pages.administration.administrationpanel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.HtmlEditor;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public abstract class BaseAdminPanelPage extends BasePage {

    protected static final Logger log = LoggerFactory.getLogger(UsersPage.class);
    private static final String HELP_WIZARD_ID = "ADMINISTRATIVE_PANEL_HELP_WIZARD_ID";
    private static final String HELP_BUTTON_ID = "USER_HELP_ACTION_ID";
    private static final String REFRESH_BUTTON_ID = "tableRefreshButton";

    protected BaseAdminPanelPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Click Help button")
    public void clickHelp(String tableId) {
        getOldTable(tableId).callAction(HELP_BUTTON_ID);
        log.info("Clicking Help button");
    }

    @Step("Check if text in Help is displayed")
    public boolean isTextInHelp(String htmlEditorId, String helpHeaderText) {
        return HtmlEditor.create(driver, wait, htmlEditorId).getStringValue().contains(helpHeaderText);
    }

    @Step("Click Accept")
    public void clickAccept() {
        Wizard.createByComponentId(driver, wait, HELP_WIZARD_ID).clickAccept();
        log.info("Clicking Accept button");
    }

    @Step("Check if Table is empty")
    public boolean isTableEmpty(String tableId) {
        return getOldTable(tableId).hasNoData();
    }

    @Step("Click Refresh in table")
    public void clickRefreshInTable(String tableId) {
        getOldTable(tableId).callAction(ActionsContainer.KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Call Action in table")
    public void callActionInTable(String tableId, String actionId) {
        getOldTable(tableId).callAction(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Select first row in table")
    public void selectFirstRowInTable(String tableId) {
        getOldTable(tableId).selectFirstRow();
    }

    @Step("Get value from first row in table")
    public String getFirstValueFromTable(String tableId, String columnLabel) {
        return getOldTable(tableId).getCellValue(0, columnLabel);
    }

    @Step("Search in table")
    public void searchInTable(String tableId, String searchedText) {
        getOldTable(tableId).fullTextSearch(searchedText);
    }

    private OldTable getOldTable(String tableId) {
        return OldTable.createById(driver, wait, tableId);
    }
}