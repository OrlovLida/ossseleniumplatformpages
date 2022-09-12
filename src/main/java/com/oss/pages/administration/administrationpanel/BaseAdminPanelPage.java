package com.oss.pages.administration.administrationpanel;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.HtmlEditor;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.untils.FileDownload;

import io.qameta.allure.Step;

public abstract class BaseAdminPanelPage extends BasePage {

    protected static final Logger log = LoggerFactory.getLogger(BaseAdminPanelPage.class);
    private static final String HELP_WIZARD_ID = "ADMINISTRATIVE_PANEL_HELP_WIZARD_ID";
    private static final String REFRESH_BUTTON_ID = "tableRefreshButton";
    private static final String NO_SUCH_ELEMENT_EXCEPTION = "Chosen Property is not visible";

    protected BaseAdminPanelPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static void goToPage(WebDriver driver, WebDriverWait wait, String url) {
        driver.get(url);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", url);
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
        log.info("Clicking Refresh in table");
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
        log.info("Selecting first row in the table");
    }

    @Step("Get value from first row in table")
    public String getFirstValueFromTable(String tableId, String columnLabel) {
        return getOldTable(tableId).getCellValue(0, columnLabel);
    }

    @Step("Search in table")
    public void searchInTable(String tableId, String searchedText) {
        getOldTable(tableId).fullTextSearch(searchedText);
    }

    @Step("Change first column")
    public void changeFirstColumn(String tableId, String columnLabel) {
        getOldTable(tableId).changeColumnsOrder(columnLabel, 0);
        log.info("Change '{}' column to be the first", columnLabel);
    }

    @Step("Get first column label")
    public String getFirstColumnLabel(String tableId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getOldTable(tableId).getColumnsHeaders().get(0);
    }

    @Step("Set Filter in Column")
    public void setColumnFilter(String tableId, String columnLabel, String value) {
        getOldTable(tableId).searchByAttributeWithLabel(columnLabel, Input.ComponentType.TEXT_FIELD, value);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Search in: {} for: {}", columnLabel, value);
    }

    @Step("Go to next page by clicking Pages Navigation button in table")
    public void goToNextPage(String tableId) {
        getOldTable(tableId).goToNextPage();
        log.info("Clicking Pages Navigation button in table");
    }

    @Step("Get total number of items in table")
    public int getItemsCount(String tableId) {
        log.info("Get total number of items in table {}", tableId);
        return getOldTable(tableId).getTotalCount();
    }

    @Step("Attach downloaded file to report")
    public void attachFileToReport(String fileName) {
        FileDownload.attachDownloadedFileToReport(fileName);
        log.info("Attaching downloaded file to report");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Get value for {propertyName}")
    public String getValueFromPanel(String propertyPanelId, String propertyName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        if (checkPropertyVisibility(propertyPanelId, propertyName)) {
            return getAdminPropertyPanel(propertyPanelId).getPropertyValue(propertyName);
        } else {
            throw new NoSuchElementException(NO_SUCH_ELEMENT_EXCEPTION);
        }
    }

    @Step("Click link from Panel")
    public void clickLinkFromPanel(String propertyPanelId, String propertyName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getAdminPropertyPanel(propertyPanelId).clickLink(propertyName);
        log.info("Click Link to {}", propertyName);
    }

    private boolean checkPropertyVisibility(String propertyPanelId, String propertyName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getVisibleAttributes(propertyPanelId).contains(propertyName);
    }

    private List<String> getVisibleAttributes(String propertyPanelId) {
        return getAdminPropertyPanel(propertyPanelId).getVisibleAttributes();
    }

    private OldTable getOldTable(String tableId) {
        return OldTable.createById(driver, wait, tableId);
    }

    protected OldPropertyPanel getAdminPropertyPanel(String propertyPanelId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldPropertyPanel.createById(driver, wait, propertyPanelId);
    }
}