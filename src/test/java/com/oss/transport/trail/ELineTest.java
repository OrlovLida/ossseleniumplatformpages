package com.oss.transport.trail;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.platform.toolsmanager.ToolsManagerPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.trail.ConnectionWizardPage;

import io.qameta.allure.Description;

public class ELineTest extends BaseTestCase {

    private static final String RESOURCE_INVENTORY_VIEW_SIDE_MENU = "Resource Inventory";
    private static final String INVENTORY_VIEW_SIDE_MENU = "Inventory View";
    private static final String NETWORK_VIEW_SIDE_MENU = "Network View";
    private static final String ELINE_TRAIL_TYPE = "E-Line";
    private static final String ELINE_NAME_CREATE = "Eline_Selenium_Test" + (int) (Math.random() * 1001);
    private static final String ELINE_CAPACITY_UNIT = "bps";
    private static final String ELINE_CAPACITY_VALUE_CREATE = "128000000";
    private static final String ELINE_NAME_ATTRIBUTE = "name";
    private static final String CAPACITY_VALUE_NAME = "capacityValue";
    private static final String CAPACITY_UNIT_NAME = "capacityUnit";
    private static final String ASSERT_MESSAGE_PATTERN = "%s assertion failed";
    private static final String ELINE_NAME_UPDATE = "Eline_Selenium_Test_Update" + (int) (Math.random() * 1001);
    private static final String ELINE_NAME_UPDATE_FULL = ELINE_NAME_UPDATE + " (0%)";
    private static final String ELINE_CAPACITY_VALUE_UPDATE = "256000000";
    private static final String DEVICE_NAME_1 = "Selenium_Eline_device_1";
    private static final String DEVICE_NAME_2 = "Selenium_Eline_device_2";
    private static final String NAME_COLUMN_LABEL = "Name";
    private static final String START_TERMINATION_PATH = "1.1_1";
    private static final String END_TERMINATION_PATH = "1.1_2";
    private static final String START_PORT_TERMINATION = "MGT LAN 0";
    private static final String START_TERMINATION_POINT = "MGT LAN 0";
    private static final String END_PORT_TERMINATION = "MGT LAN 1";
    private static final String END_TERMINATION_POINT = "MGT LAN 1";
    private static final String TERMINATION_POINT_NAME_COLUMN = "terminationPoint.name";
    private static final String TERMINATIONS_TAB_ID = "TerminationIndexedWidget";
    private static final String ETHERNET_LINK_NAME = "SeleniumElineTest";
    private static final String CONNECTION_NAME_COLUMN = "trail.name";
    private static final String ROUTING_1ST_LEVEL_TAB_ID = "RoutingSegmentIndexedWidget";
    private static final String ADD_TO_ROUTING_BUTTON = "EDIT_Add to Routing-null";
    private static final String ELEMENT_ROUTING_WIZARD_ID = "routingElementWizardWidget";
    private static final String MESSAGE_NOT_REMOVED = "The object has not been removed";
    private static final String DELETE_CONFIRMATION_BOX = "wizard-submit-button-deleteWidgetId";
    private static final String DELETE_ELINE = "DeleteTrailWizardActionId";
    private static final String DELETE_ELINE_WIZARD_ID = "deleteWidgetId";

    @Test(priority = 1, description = "Create E-Line")
    @Description("Create E-Line")
    public void createEline() {
        navigateToCreateEthernetLinkWizard();
        setElineAttributesValue(ELINE_NAME_CREATE, ELINE_CAPACITY_VALUE_CREATE);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandAttributesPanel();
        assertElineAttributesValue(ELINE_NAME_CREATE, ELINE_CAPACITY_VALUE_CREATE);
    }

    @Test(priority = 2, description = "Update E-Line", dependsOnMethods = {"createEline"})
    @Description("Update E-Line attributes")
    public void updateELine() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.ATTRIBUTES_AND_TERMINATIONS_ACTION);
        setElineAttributesValue(ELINE_NAME_UPDATE, ELINE_CAPACITY_VALUE_UPDATE);
        networkViewPage.expandViewContentPanel();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_LABEL, ELINE_NAME_UPDATE_FULL);
        assertElineAttributesValue(ELINE_NAME_UPDATE, ELINE_CAPACITY_VALUE_UPDATE);
    }

    @Test(priority = 3, description = "Terminate E-Line", dependsOnMethods = {"createEline"})
    @Description("Terminate E-Line on selected devices")
    public void terminateEline() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.startEditingSelectedTrail();
        addDeviceToNetworkView(DEVICE_NAME_1);
        addDeviceToNetworkView(DEVICE_NAME_2);
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_LABEL, DEVICE_NAME_1);
        networkViewPage.addSelectedObjectsToTermination();
        setPreciseTerminations();
        networkViewPage.selectObjectInViewContentContains(NAME_COLUMN_LABEL, ELINE_NAME_UPDATE);
        networkViewPage.openTerminationsTab();
        //assertPresenceOfObjectInTab(0, TERMINATION_POINT_NAME_COLUMN, TERMINATIONS_TAB_ID, START_TERMINATION_POINT);
        //assertPresenceOfObjectInTab(1, TERMINATION_POINT_NAME_COLUMN, TERMINATIONS_TAB_ID, END_TERMINATION_POINT);
    }

    @Test(priority = 4, description = "Add trail to routing", dependsOnMethods = {"createEline"})
    @Description("Add Ethernet Link do E-Lines routing")
    public void addTrailToRouting() {
        addConnectionToNetworkView();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.addSelectedObjectsToRouting().accept();
        networkViewPage.selectObjectInViewContentContains(NAME_COLUMN_LABEL, ELINE_NAME_UPDATE);
        networkViewPage.openRouting1stLevelTab();
        assertPresenceOfObjectInTab(0, CONNECTION_NAME_COLUMN, ROUTING_1ST_LEVEL_TAB_ID, ETHERNET_LINK_NAME);
    }

    @Test(priority = 5, description = "Add device to element routing", dependsOnMethods = {"terminateEline"})
    @Description("Add IP Device to E-lines element routing")
    public void addDeviceToElementRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_LABEL, ELINE_NAME_UPDATE_FULL);
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_LABEL, DEVICE_NAME_1);
        networkViewPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, ADD_TO_ROUTING_BUTTON);
        Wizard.createByComponentId(driver, webDriverWait, ELEMENT_ROUTING_WIZARD_ID).clickAccept();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_LABEL, DEVICE_NAME_1);
        networkViewPage.selectObjectInViewContentContains(NAME_COLUMN_LABEL, ELINE_NAME_UPDATE);
        networkViewPage.openRoutingElementsTab();
        networkViewPage.isObjectInRoutingElements(NAME_COLUMN_LABEL, DEVICE_NAME_1);
        assertPresenceOfObjectInOldTab();
    }

    @Test(priority = 6, description = "Remove terminations", dependsOnMethods = {"terminateEline"})
    @Description("Remove E-lines terminations")
    public void removeTerminations() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.openTerminationsTab();
        selectAndRemoveTermination();
        selectAndRemoveTermination();
        assertIfTableIsEmpty(TERMINATIONS_TAB_ID);
    }

    @Test(priority = 7, description = "Remove trail from routing", dependsOnMethods = {"addTrailToRouting"})
    @Description("Remove trail from E-lines routing")
    public void removeRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.openRouting1stLevelTab();
        selectAndRemoveConnectionFromRouting();
        assertIfTableIsEmpty(ROUTING_1ST_LEVEL_TAB_ID);
    }

    @Test(priority = 8, description = "Remove E-Line", dependsOnMethods = "createEline")
    @Description("Remove E-Line")
    public void removeEline() {
        navigateToELInventoryView();
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow().callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_ELINE);
        Wizard.createByComponentId(driver, webDriverWait, DELETE_ELINE_WIZARD_ID).clickButtonById(DELETE_CONFIRMATION_BOX);
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.checkIfTableIsEmpty();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void expandTiles() {
        ToolsManagerPage toolsManagerPage = new ToolsManagerPage(driver);
        waitForPageToLoad();
        ToolsManagerWindow toolsManagerWindow = toolsManagerPage.getToolsManager();
        waitForPageToLoad();
        toolsManagerWindow.openApplication(RESOURCE_INVENTORY_VIEW_SIDE_MENU, NETWORK_VIEW_SIDE_MENU);
    }

    private void navigateToCreateEthernetLinkWizard() {
        homePage.goToHomePage(driver, BASIC_URL);
        expandTiles();
        waitForPageToLoad();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.openConnectionWizard(ELINE_TRAIL_TYPE);
    }

    private void setElineAttributesValue(String elineName, String elineCapacityValue) {
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.setName(elineName);
        connectionWizardPage.setCapacityUnit(ELINE_CAPACITY_UNIT);
        connectionWizardPage.setCapacityValue(elineCapacityValue);
        connectionWizardPage.clickNext();
        connectionWizardPage.clickAccept();
    }

    private void assertElineAttributesValue(String elineName, String elineCapacityValue) {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(networkViewPage.getAttributeValue(ELINE_NAME_ATTRIBUTE), elineName, String.format(ASSERT_MESSAGE_PATTERN, ELINE_NAME_ATTRIBUTE));
        softAssert.assertEquals(networkViewPage.getAttributeValue(CAPACITY_VALUE_NAME), elineCapacityValue, String.format(ASSERT_MESSAGE_PATTERN, CAPACITY_VALUE_NAME));
        softAssert.assertEquals(networkViewPage.getAttributeValue(CAPACITY_UNIT_NAME), ELINE_CAPACITY_UNIT, String.format(ASSERT_MESSAGE_PATTERN, CAPACITY_UNIT_NAME));
        softAssert.assertAll();
    }

    private void addDeviceToNetworkView(String deviceName) {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        networkViewPage.queryElementAndAddItToView("name", deviceName);
    }

    private void setPreciseTerminations() {
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.selectConnectionTermination(START_TERMINATION_PATH);
        waitForPageToLoad();
        connectionWizardPage.setNonexistentCard();
        connectionWizardPage.terminatePort(START_PORT_TERMINATION);
        waitForPageToLoad();
        //Terminowanie precyzyjne blokowane przez - OSSTRAIL-7374
        //connectionWizardPage.terminateTerminationPort(START_TERMINATION_POINT);
        connectionWizardPage.selectConnectionTermination(END_TERMINATION_PATH);
        waitForPageToLoad();
        connectionWizardPage.setNonexistentCard();
        connectionWizardPage.terminatePort(END_PORT_TERMINATION);
        waitForPageToLoad();
        //connectionWizardPage.terminateTerminationPort(END_TERMINATION_POINT);
        connectionWizardPage.clickAccept();
    }

    private void assertPresenceOfObjectInTab(Integer index, String columnId, String tabId, String objectName) {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        String objectValue = networkViewPage.getObjectValueFromTab(index, columnId, tabId);
        Assert.assertEquals(objectValue, objectName, String.format("%s value is not correct", objectName));
    }

    private void addConnectionToNetworkView() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.CONNECTION_ACTION);
        networkViewPage.queryElementAndAddItToView("name", ETHERNET_LINK_NAME);
    }

    private void assertPresenceOfObjectInOldTab() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        Assert.assertTrue(networkViewPage.isObjectInRoutingElements(NAME_COLUMN_LABEL, DEVICE_NAME_1), "Element Routing Table is empty");
    }

    private void selectAndRemoveTermination() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        TableWidget.createById(driver, TERMINATIONS_TAB_ID, webDriverWait).selectFirstRow();
        networkViewPage.removeSelectedTerminations();
        networkViewPage.refreshTerminationsTab();
    }

    private void assertIfTableIsEmpty(String tabID) {
        Assert.assertTrue(TableWidget.createById(driver, tabID, webDriverWait).hasNoData(), MESSAGE_NOT_REMOVED);
    }

    private void selectAndRemoveConnectionFromRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        TableWidget.createById(driver, ROUTING_1ST_LEVEL_TAB_ID, webDriverWait).selectFirstRow();
        networkViewPage.deleteSelectedConnectionsFromRouting();
        networkViewPage.refreshFirstLevelRoutingTab();
    }

    private void navigateToELInventoryView() {
        SideMenu sidemenu = SideMenu.create(driver, webDriverWait);
        sidemenu.callActionByLabel(INVENTORY_VIEW_SIDE_MENU, RESOURCE_INVENTORY_VIEW_SIDE_MENU);
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(ELINE_TRAIL_TYPE);
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(ELINE_NAME_UPDATE).selectFirstRow();
    }
}
