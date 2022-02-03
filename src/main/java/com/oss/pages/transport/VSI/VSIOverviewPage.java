package com.oss.pages.transport.VSI;


import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabWindowWidget;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kamil Sikora
 */
public class VSIOverviewPage extends BasePage {

    private static final String NAME_LABEL = "Name";
    private static final String VPN_ID_LABEL = "VPN ID";
    private static final String DEVICE_LABEL = "Device";
    private static final String VE_ID_LABEL = "VE ID";
    private static final String ROUTE_DISTINGUISHER_LABEL = "Route Distinguisher";
    private static final String DESCRIPTION_LABEL = "Description";
    private static final String ROUTE_TARGETS_TAB_ID = "vsi_route_targets_tab_id";
    private static final String VSI_INTERFACES_TAB_ID = "vsi_interfaces_table_tab_id";

    private static final String BOTTOM_INTERFACES_TABLE_DATA_ATTRIBUTENAME = "vsi_interfaces_table_app_id";
    private static final String BOTTOM_ROUTE_TARGET_TABLE_DATA_ATTRIBUTENAME = "vsi_rt_assinment_app_id";
    private static final String BOTTOM_TABLE_ROUTE_TARGET_LABEL = "Route Target";
    private static final String BOTTOM_TABLE_INTERFACE_NAME_LABEL = "Name";

    private static final String EDIT_BUTTON_DATA_ATTRIBUTENAME = "vsi_buttons_id-0";
    private static final String REMOVE_VSI_BUTTON_DATA_ATTRIBUTENAME = "vsi_buttons_id-1";
    private static final String VSI_REMOVAL_CONFIRMATION_BUTTON_DATA_ATTRIBUTENAME = "ConfirmationBox_deleteAppId_close_button";

    private static final String ADD_ROUTE_TARGET_BUTTON_DATA_ATTRIBUTENAME = "addRouteTarget";
    private static final String REMOVE_ROUTE_TARGET_BUTTON_DATA_ATTRIBUTENAME = "Remove";
    private static final String REMOVAL_CONFIRMATION_BUTTON_DATA_ATTRIBUTENAME = "ConfirmationBox_remove_RT_app_id_action_button";
    private static final String PROPERTY_PANEL_ID = "propertyPanel";

    private final OldPropertyPanel propertyPanel;

    public VSIOverviewPage(WebDriver driver){
        super(driver);
        propertyPanel = OldPropertyPanel.createById(driver, wait, PROPERTY_PANEL_ID);
    }

    @Step("Click edit button")
    public VSIWizardPage clickEdit() {
        Button editButton = Button.createBySelectorAndId(driver, "a", EDIT_BUTTON_DATA_ATTRIBUTENAME);
        editButton.click();
        DelayUtils.waitForPageToLoad(driver, wait);
        return new VSIWizardPage(driver);
    }

    @Step("Click remove button and confirm removal")
    public void removeVsi(){
        clickRemoveVsi();
        confirmVsiRemoval();
    }

    private void clickRemoveVsi() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button removeButton = Button.createBySelectorAndId(driver, "a", REMOVE_VSI_BUTTON_DATA_ATTRIBUTENAME);
        removeButton.click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void confirmVsiRemoval() {
        Button confirmationButton = Button.createById(driver, VSI_REMOVAL_CONFIRMATION_BUTTON_DATA_ATTRIBUTENAME);
        confirmationButton.click();
    }

    @Step("Click add route target button")
    public VSIRouteTargetAssignmentPage addRouteTarget() {
        OldTable routeTargetTable = getTableWidget(BOTTOM_ROUTE_TARGET_TABLE_DATA_ATTRIBUTENAME);
        routeTargetTable.callAction(ADD_ROUTE_TARGET_BUTTON_DATA_ATTRIBUTENAME);
        return new VSIRouteTargetAssignmentPage(driver);
    }

    public String getNameValue(){
        return getPropertyValue(NAME_LABEL);
    }

    public String getVpnIdValue(){
        return getPropertyValue(VPN_ID_LABEL);
    }

    public String getDeviceValue(){
        return getPropertyValue(DEVICE_LABEL);
    }

    public String getVeIdValue(){
        return getPropertyValue(VE_ID_LABEL);
    }

    public String getRouteDistinguisherValue(){
        return getPropertyValue(ROUTE_DISTINGUISHER_LABEL);
    }

    public String getDescriptionValue(){
        return getPropertyValue(DESCRIPTION_LABEL);
    }

    private String getPropertyValue(String propertyLabel){
        return propertyPanel.getPropertyValue(propertyLabel);
    }

    @Step("Open Route Target tab at the bottom")
    public void openBottomRouteTargetsTab(){
        navigateToBottomTabById(ROUTE_TARGETS_TAB_ID);
    }

    @Step("Open VSI Interfaces tab at the bottom")
    public void openBottomVSIInterfacesTab(){
        navigateToBottomTabById(VSI_INTERFACES_TAB_ID);
    }

    private void navigateToBottomTabById(String id){
        TabsInterface tabsInterface = TabWindowWidget.create(driver, wait);
        tabsInterface.selectTabById(id);
    }

    @Step("Get names of all assigned interfaces")
    public List<String> getAssignedInterfaces(){
        OldTable table = getTableWidget(BOTTOM_INTERFACES_TABLE_DATA_ATTRIBUTENAME);
        return getListOfElementsInColumn(table, BOTTOM_TABLE_INTERFACE_NAME_LABEL);
    }

    @Step("Get values of all assigned Route Targets")
    public List<String> getAssignedRouteTargets(){
        OldTable table = getTableWidget(BOTTOM_ROUTE_TARGET_TABLE_DATA_ATTRIBUTENAME);
        return getListOfElementsInColumn(table, BOTTOM_TABLE_ROUTE_TARGET_LABEL);
    }

    private List<String> getListOfElementsInColumn(OldTable table, String columnName){
        int rows = table.countRows(columnName);
        List<String> elements = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            elements.add(table.getCellValue(i, columnName));
        }
        return elements;
    }

    @Step("Select first Route Target, click remove and confirm removal")
    public void removeFirstRouteTarget(){
        selectFirstRouteTarget();
        OldTable routeTargetTable = getTableWidget(BOTTOM_ROUTE_TARGET_TABLE_DATA_ATTRIBUTENAME);
        routeTargetTable.callAction(REMOVE_ROUTE_TARGET_BUTTON_DATA_ATTRIBUTENAME);
        DelayUtils.waitForPageToLoad(driver, wait);
        confirmRouteTargetRemoval();
    }

    private OldTable getTableWidget(String tableId){
        return OldTable.createById(driver, wait, tableId);
    }

    private void selectFirstRouteTarget(){
        OldTable table = OldTable.createById(driver, wait, BOTTOM_ROUTE_TARGET_TABLE_DATA_ATTRIBUTENAME);
        table.selectRow(0);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void confirmRouteTargetRemoval() {
        Button confirmationButton = Button.createById(driver, REMOVAL_CONFIRMATION_BUTTON_DATA_ATTRIBUTENAME);
        confirmationButton.click();
    }

}
