package com.oss.pages.reconciliation;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class VS20Page extends BasePage {

    private static final String TABLE_WIDGET = "InventoryView_MainCard_VS_Object";
    private static final String CM_DOMAIN_NAME = "cmDomainName";
    private static final String DISTINGUISH_NAME = "distinguishName";
    private static final String NATIVE_TYPE = "nativeType";
    private static final String PROPERTY_PANEL_ID = "PropertyPanel";
    private static final String INVENTORY_VIEW_ID = "InventoryView";

    public VS20Page(WebDriver driver) {
        super(driver);
    }

    public static VS20Page goToVS20Page(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/views/management/views/inventory-view/VS_Object" +
                "?perspective=NETWORK", basicURL));
        return new VS20Page(driver);
    }

    @Step("Search for item by {cmDomainName}")
    public void searchByCMDomainName(String cmDomainName) {
        getTableWidget().searchByAttribute(CM_DOMAIN_NAME, cmDomainName);
    }

    @Step("Search for item by {distName}")
    public void searchByDistinguishName(String distName) {
        getTableWidget().searchByAttribute(DISTINGUISH_NAME, distName);
    }

    @Step("Search for item by type")
    public void searchByType(String objectType) {
        getTableWidget().searchByAttribute(NATIVE_TYPE, objectType);
    }

    @Step("Select first row")
    public void selectFirstRow() {
        getTableWidget().clickRow(0);
    }

    @Step("Get active column headers")
    public List<String> getColumnsHeaders() {
        return getTableWidget().getActiveColumnHeaders();
    }

    @Step("Get domain name from property value")
    public String getCmDomainNameFromPropertyValue() {
        return getPropertyPanel().getPropertyValue(CM_DOMAIN_NAME);
    }

    @Step("Get all available filters")
    public List<String> getAvailableFilters() {
        return getTableWidget().getAllVisibleFilters();
    }

    @Step("Navigate to Inventory View")
    public void navigateToInventoryView() {
        getTableWidget().callAction(ActionsContainer.SHOW_ON_GROUP_ID, INVENTORY_VIEW_ID);
    }

    @Step("Get all properties labels")
    public List<String> getPropertiesLabels() {
        return getPropertyPanel().getPropertyLabels();
    }

    @Step("Get {propertyName} value from property panel")
    public String getPropertyValue(String propertyName) {
        return getPropertyPanel().getPropertyValue(propertyName);
    }

    private PropertyPanel getPropertyPanel() {
        return PropertyPanel.createById(driver, wait, PROPERTY_PANEL_ID);
    }

    private TableWidget getTableWidget() {
        return TableWidget.createById(driver, TABLE_WIDGET, wait);
    }
}
