package com.oss.pages.iaa.bigdata.dfe.externalresource;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.bigdata.dfe.BaseDfePage;

import io.qameta.allure.Step;

public class ExternalResourcesPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(ExternalResourcesPage.class);
    private static final String TABLE_ID = "ExternalResourcesTableWindowId";
    private static final String ADD_NEW_EXTERNAL_RESOURCE_LABEL = "Add New External Resource";
    private static final String EDIT_EXTERNAL_RESOURCE_LABEL = "Edit External Resource";
    private static final String DELETE_EXTERNAL_RESOURCE_LABEL = "Delete External Resource";
    private static final String SEARCH_INPUT_ID = "input_external-resources-listSearchAppId";
    private static final String NAME_COLUMN_LABEL = "Name";
    private static final String DELETE_LABEL = "Delete";
    private static final String EXTERNAL_RESOURCE_PROPERTY_PANEL_ID = "ExternalDetailsAppId";
    private static final String TYPE_SEARCH_ID = "type";

    public ExternalResourcesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Open External Resources View")
    public static ExternalResourcesPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "external-resources");
        return new ExternalResourcesPage(driver, wait);
    }

    @Step("Click add new External Resource")
    public void clickAddNewExternalResource() {
        clickContextActionAdd();
    }

    @Step("Click edit External Resource")
    public void clickEditExternalResource() {
        clickContextActionEdit();
    }

    @Step("Click delete External Resource")
    public void clickDeleteExternalResource() {
        clickContextActionDelete();
    }

    @Step("Check if External Resource: {externalResourceName} exists into the table")
    public Boolean externalResourceExistsIntoTable(String externalResourceName) {
        searchFeed(externalResourceName);
        DelayUtils.waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(NAME_COLUMN_LABEL);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, externalResourceName);
        return numberOfRowsInTable == 1;
    }

    @Step("Select first External Resource in table")
    public void selectFirstExternalResourceInTable() {
        getTable().selectRow(0);
    }

    @Step("Check if External Resource Table is empty")
    public boolean isExternalResourceTableEmpty() {
        return getTable().hasNoData();
    }

    @Step("Get first value from column: {columnName}")
    public String getValueFromColumn(String columnName) {
        return getTable().getCellValue(0, columnName);
    }

    @Step("Get value: {propertyName} form External Resource Property Panel")
    public String getValueFromExternalResourcePropertyPanel(String propertyName) {
        return getValueFromPropertyPanel(EXTERNAL_RESOURCE_PROPERTY_PANEL_ID, propertyName);
    }

    @Step("Set type to: {type}")
    public void setTypeSearch(String type) {
        ComponentFactory.create(TYPE_SEARCH_ID, driver, wait).setSingleStringValue(type);
        log.info("Setting type search to: {}", type);
    }

    @Step("Confirm the removal of External Resource")
    public void confirmDelete() {
        confirmDelete(DELETE_LABEL);
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_EXTERNAL_RESOURCE_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_EXTERNAL_RESOURCE_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_EXTERNAL_RESOURCE_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}
