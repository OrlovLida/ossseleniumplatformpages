package com.oss.pages.reconciliation;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
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

    @Step("Search for item by CMDomain name")
    public void searchItemByCMDomainName(String cmDomainName) {
        getTableWidget().searchByAttribute(CM_DOMAIN_NAME, Input.ComponentType.TEXT_FIELD, cmDomainName);
    }

    @Step("Search for item by distinguish name")
    public void searchItemByDistinguishName(String distName) {
        getTableWidget().searchByAttribute(DISTINGUISH_NAME, Input.ComponentType.TEXT_FIELD, distName);
    }

    @Step("Search for item by type")
    public void searchItemByType(String objectType) {
        getTableWidget().searchByAttribute(NATIVE_TYPE, Input.ComponentType.TEXT_FIELD, objectType);
    }

    @Step("Select first row")
    public void selectFirstRow() {
        getTableWidget().clickRow(0);
    }

    @Step("Get active column headers")
    public List<String> getColumnsIds() {
        return getTableWidget().getActiveColumnHeaders();
    }

    @Step("Get domain name from property value")
    public String getCmDomainNameFromPropertyValue() {
        return getPropertyPanel().getPropertyValue(CM_DOMAIN_NAME);
    }

    @Step("Get all available fiters to list")
    public List<String> getAvailableFilters() {
        return getTableWidget().getAllVisibleFilters();
    }

    @Step("Navigate to Inventory View")
    public void navigateToInventoryView() {
        getTableWidget().callAction(ActionsContainer.SHOW_ON_GROUP_ID, INVENTORY_VIEW_ID);
    }

    @Step("Get all properties to list")
    public List<String> getPropertiesLabels() {
        return getPropertyPanel().getPropertyLabels();
    }

    @Step("Get property value by {propertyName}")
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
