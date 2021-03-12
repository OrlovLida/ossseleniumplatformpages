/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.transport.VRF;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kamil Szota
 */
public class VRFOverviewPage extends BasePage {

    private static final String NAME = "VRF Name";
    private static final String DESCRIPTION = "Description";
    private static final String ADDRESS_FAMILY_IPV4 = "Address Family IPv4";
    private static final String ADDRESS_FAMILY_IPV6 = "Address Family IPv6";
    private static final String ROUTE_DISTINGUISHER = "Route Distinguisher";
    private static final String DEVICE_NAME = "Device";
    private static final String ROUTE_TARGET_LABEL = "Route Target";
    private static final String ADDRESS_FAMILY_LABEL = "Address Family";
    private static final String EDIT_BUTTON_DATA_ATTRIBUTENAME = "Top_Window_Buttons_Id-0";
    private static final String REMOVE_BUTTON_DATA_ATTRIBUTENAME = "Top_Window_Buttons_Id-1";
    private static final String CONFIRM_REMOVAL_BUTTON_DATA_ATTRIBUTENAME = "ConfirmationBox_confirmationBoxAppDeleteVRF_action_button";
    private static final String INTERFACE_ASSIGNMENT_LABEL = "Interface Assignment";
    private static final String ADD_ROUTE_TARGET_BUTTON_DATA_ATTRIBUTENAME = "addExisting";
    private static final String INTERFACES_TABLE_ID = "interfaces_table_id";
    private static final String BOTTOM_ROUTE_TARGET_TABLE_ID = "bottom_app_id";
    private static final String BOTTOM_INTERFACES_TAB_INTERFACE_NAME_COLUMN_LABEL = "Name";

    private final OldPropertyPanel propertyPanel;

    public VRFOverviewPage(WebDriver driver) {
        super(driver);
        propertyPanel = OldPropertyPanel.create(driver, wait);
    }

    @Step("Click edit button")
    public VRFWizardPage clickEdit() {
        Button editButton = Button.createBySelectorAndId(driver, "a", EDIT_BUTTON_DATA_ATTRIBUTENAME);
        editButton.click();
        return new VRFWizardPage(driver);
    }

    @Step("Click remove button")
    public void clickRemove() {
        Button removeButton = Button.createBySelectorAndId(driver, "a", REMOVE_BUTTON_DATA_ATTRIBUTENAME);
        removeButton.click();
    }

    @Step("Click remove confirmation button")
    public void confirmRemoval() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button confirmRemovalButton = Button.createById(driver, CONFIRM_REMOVAL_BUTTON_DATA_ATTRIBUTENAME);
        confirmRemovalButton.click();
    }

    public String getNameValue() {
        return getAttributeValue(NAME);
    }

    public String getDescriptionValue() {
        return getAttributeValue(DESCRIPTION);
    }

    public String getAddressFamilyIPv4Value() {
        return getAttributeValue(ADDRESS_FAMILY_IPV4);
    }

    public String getAddressFamilyIPv6Value() {
        return getAttributeValue(ADDRESS_FAMILY_IPV6);
    }

    public String getRouteDistinguisherValue() {
        return getAttributeValue(ROUTE_DISTINGUISHER);
    }

    public String getDeviceNameValue() {
        return getAttributeValue(DEVICE_NAME);
    }

    @Step("Open bottom Interfaces tab")
    public void openInterfaceAssignmentTab() {
        TabsInterface tabsWidget = TabWindowWidget.create(driver, wait);
        tabsWidget.selectTabByLabel(INTERFACE_ASSIGNMENT_LABEL);
    }

    @Step("Open bottom Route Target tab")
    public void openRouteTargetTab() {
        TabsInterface tabsWidget = TabWindowWidget.create(driver, wait);
        tabsWidget.selectTabByLabel(ROUTE_TARGET_LABEL);
    }

    @Step("Click add Route Target button")
    public VRFImpExpRouteTargetWizardPage clickAddRouteTargetButton() {
        openRouteTargetTab();
        OldTable routeTargetTabTable = getTableWidget(BOTTOM_ROUTE_TARGET_TABLE_ID);
        routeTargetTabTable.callAction(ADD_ROUTE_TARGET_BUTTON_DATA_ATTRIBUTENAME);
        return new VRFImpExpRouteTargetWizardPage(driver);
    }


    @Step("Get all assigned interfaces names")
    public List<String> getAssignedInterfaces() {
        openInterfaceAssignmentTab();
        OldTable interfacesTable = getTableWidget(INTERFACES_TABLE_ID);
        return getAllElementsInColumn(interfacesTable, BOTTOM_INTERFACES_TAB_INTERFACE_NAME_COLUMN_LABEL);
    }

    @Step("Get all assigned Route Targets values")
    public List<String> getAssignedRouteTargets() {
        openRouteTargetTab();
        OldTable routeTargetTabTable = getTableWidget(BOTTOM_ROUTE_TARGET_TABLE_ID);
        return getAllElementsInColumn(routeTargetTabTable, ROUTE_TARGET_LABEL);
    }

    @Step("Get all assigned Route Targets address families")
    public List<String> getAssignedAddressFamilies() {
        openRouteTargetTab();
        OldTable routeTargetTabTable = getTableWidget(BOTTOM_ROUTE_TARGET_TABLE_ID);
        return getAllElementsInColumn(routeTargetTabTable, ADDRESS_FAMILY_LABEL);
    }

    private OldTable getTableWidget(String tableId){
        return OldTable.createByComponentDataAttributeName(driver, wait, tableId);
    }

    private List<String> getAllElementsInColumn(OldTable table, String columnName){
        List<String> elementsInColumn = new ArrayList<>();
        int numberOfRows = table.getNumberOfRowsInTable(columnName);
        for(int i = 0; i < numberOfRows; i++){
            String addressFamily = table.getCellValue(i, columnName);
            elementsInColumn.add(addressFamily);
        }
        return elementsInColumn;
    }

    private String getAttributeValue(String attributeName) {
        return propertyPanel.getPropertyValue(attributeName);
    }
}
