package com.oss.pages.reconciliation;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
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
    private static final String NAVIGATION_ID = "NAVIGATION";
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
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Search for item by distinguish name")
    public void searchItemByDistinguishName(String distName) {
        getTableWidget().searchByAttribute(DISTINGUISH_NAME, Input.ComponentType.TEXT_FIELD, distName);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Search for item by type")
    public void searchItemByType(String objectType) {
        getTableWidget().searchByAttribute(NATIVE_TYPE, Input.ComponentType.TEXT_FIELD, objectType);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clickFirstItem() {
        getTableWidget().clickRow(0);
    }

    public List<String> getColumnsIds() {
        return getTableWidget().getActiveColumnHeaders();
    }

    @Step("Go to Network Discovery Control View")
    public void goToNDCV() {
        getTableWidget().callAction(NAVIGATION_ID);
    }

    @Step("Get value of property")
    public String getPropertyValue() {
        return getPropertyPanel().getPropertyValue(CM_DOMAIN_NAME);
    }

    public List<String> getAvailableFilters() {
        return getTableWidget().getAllVisibleFilters();
    }

    public void navigateToInventoryView() {
        getTableWidget().callAction(NAVIGATION_ID, INVENTORY_VIEW_ID);
    }

    @Step("Get all properties to list")
    public List<String> getPropertiesToList() {
        return getPropertyPanel().getPropertyLabels();
    }

    @Step("Get property value")
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
