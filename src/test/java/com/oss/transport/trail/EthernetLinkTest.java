package com.oss.transport.trail;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.bpm.processinstances.ProcessOverviewPage;
import com.oss.pages.bpm.processinstances.creation.ProcessWizardPage;
import com.oss.pages.bpm.tasks.TasksPageV2;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.platform.toolsmanager.ToolsManagerPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.trail.ConnectionWizardPage;
import com.oss.pages.transport.trail.EthernetLinkWizardPage;

import io.qameta.allure.Description;

/**
 * @author Szymon Masarczyk
 */

public class EthernetLinkTest extends BaseTestCase {

    private static final String ETHERNET_LINK_NAME = "Ethernet_Link_Selenium_Test" + (int) (Math.random() * 100);
    private static final String ETHERNET_LINK_NAME_FULL = ETHERNET_LINK_NAME + " (0%)";
    private static final String ETHERNET_LINK_DESCRIPTION = "EL_Selenium_Test_Description";
    private static final String IS_TRUNK_TRUE = "true";
    private static final String SPEED_CREATE = "10M";
    private static final String ROLE_CREATE = "E-NNI";
    private static final String LATENCY_CREATE = "3";
    private static final String EFFECTIVE_CAPACITY_UNIT_CREATE = "Mbps";
    private static final String EFFECTIVE_CAPACITY_CREATE = "5";
    private static final String RESOURCE_INVENTORY_VIEW_SIDE_MENU = "Resource Inventory";
    private static final String INVENTORY_VIEW_SIDE_MENU = "Inventory View";
    private static final String NETWORK_VIEW_SIDE_MENU = "Network View";
    private static final String ETHERNET_LINK_TRAIL_TYPE = "Ethernet Link";
    private static final String NAME_COLUMN_LABEL = "Name";
    private static final String OBJECT_TYPE_COLUMN_LABEL = "Object Type";
    private static final String ETHERNET_LINK_DESCRIPTION_UPDATE = "EL_Test_description_update";
    private static final String SPEED_UPDATE = "100M";
    private static final String ROLE_UPDATE = "NNI";
    private static final String LATENCY_UPDATE = "57";
    private static final String EFFECTIVE_CAPACITY_UPDATE = "1";
    private static final String ASSERT_MESSAGE_PATTERN = "Checking assertion for %s.";
    private static final String DEVICE_1_NAME = "SeleniumTestDeviceEL1";
    private static final String DEVICE_2_NAME = "SeleniumTestDeviceEL2";
    private static final String OBJECT_TYPE_CONNECTION = "Connection";
    private static final String SEARCH_BY_COMPONENT_NAME_ID = "name";
    private static final String START_TERMINATION_PATH = "1.1_2";
    private static final String END_TERMINATION_PATH = "1.1_1";
    //private static final String START_TERMINATION_POINT = "SYNC 0";
    private static final String START_PORT_TERMINATION = "SYNC 0";
    private static final String END_PORT_TERMINATION = "MGT LAN 0";
    //private static final String END_TERMINATION_POINT = "MGT LAN 0";

    private static final String PHYSICAL_DEVICE_NAME_COLUMN = "physicalDevice.name";
    //private static final String TERMINATION_POINT_NAME_COLUMN = "terminationPoint.name";
    private static final String TERMINATIONS_TAB_ID = "TerminationIndexedWidget";
    //private static final String TERMINATION_POINT_NAME = "SYNC 0";
    private static final String PHYSICAL_DEVICE_NAME = DEVICE_1_NAME;
    private static final String PORT_SHORT_IDENTIFIER_COLUMN = "port.shortIdentifier";
    private static final String PORT_SHORT_IDENTIFIER = START_PORT_TERMINATION;
    private static final String CONNECTION_NAME_COLUMN = "trail.name";
    private static final String ROUTING_1ST_LEVEL_TAB_ID = "RoutingSegmentIndexedWidget";
    private static final String ROUTED_TRAIL_NAME = "SeleniumRoutingConnection";
    private static final String ELEMENT_ROUTING_TAB_ID = "RoutingElementsWidget";
    private static final String ADD_TO_ROUTING_BUTTON = "EDIT_Add to Routing-null";
    private static final String ELEMENT_ROUTING_WIZARD_ID = "routingElementWizardWidget";
    //private static final String CREATE_IP_LINK_CHECKBOX_ID = "EthernetLink.CreateAssociatedIPLinkCombobox";
    private static final String DELETE_ETHERNET_LINK = "DeleteTrailWizardActionId";
    private static final String DELETE_CONFIRMATION_BOX = "wizard-submit-button-deleteWidgetId";
    private static final String DELETE_ETHERNET_LINK_WIZARD_ID = "simple-wizard-component-deleteWidgetId";
    private static final String TYPE_COLUMN_LABEL = "Type";
    private static final String DEVICE_TYPE = "IP Device";
    private static final String ROUTED_CONNECTION_NAME = "SeleniumRoutingConnection";
    private String processNRPCode;

    @BeforeClass
    public void openWebConsole() {
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("Process Overview", "BPM and Planning", "Network Planning");
        waitForPageToLoad();
    }

    @Test(priority = 110)
    @Description("Create NRP Process")
    public void createProcessNRP() {
        ProcessOverviewPage processOverviewPage = new ProcessOverviewPage(driver);
        processOverviewPage.openProcessCreationWizard();
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRPV2();
        checkMessageSize();
        checkMessageType();
        checkMessageContainsText(processNRPCode);
    }

    @Test(priority = 12)
    @Description("Start High Level Planning Task")
    public void startHLPTask() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        checkTaskAssignment();
    }

    @Test(priority = 3)
    @Description("Create Ethernet Link")
    public void createEthernetLink() {
        EthernetLinkAttributes ethernetLinkAttributes = getELAttributesForCreate();
        navigateToCreateEthernetLinkWizard();
        EthernetLinkWizardPage elWizard = new EthernetLinkWizardPage(driver);
        fillEthernetLinkAttributesForCreate(ethernetLinkAttributes, elWizard);
        elWizard.clickNext();
        elWizard.clickAccept();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandAttributesPanel();
        assertEthernetLinkAttributes(networkViewPage, ethernetLinkAttributes);
    }

    @Test(priority = 14)
    @Description("Update Ethernet Link Attributes")
    public void updateEthernetLinkAttributes() {
        EthernetLinkAttributes ethernetLinkAttributes = getELAttributesForUpdate();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.ATTRIBUTES_AND_TERMINATIONS_ACTION);
        EthernetLinkWizardPage elWizard = new EthernetLinkWizardPage(driver);
        fillEthernetLinkAttributesForUpdate(ethernetLinkAttributes, elWizard);
        elWizard.clickNext();
        elWizard.clickAccept();
        networkViewPage.expandViewContentPanel();
        networkViewPage.selectObjectInViewContentContains(NAME_COLUMN_LABEL, ETHERNET_LINK_NAME);
        networkViewPage.expandAttributesPanel();
        assertEthernetLinkAttributes(networkViewPage, ethernetLinkAttributes);
    }

    @Test(priority = 5)
    @Description("Terminate Ethernet Link")
    public void terminateEthernetLink() {
        addTerminationDeviceToNetworkView(DEVICE_1_NAME);
        addTerminationDeviceToNetworkView(DEVICE_2_NAME);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandViewContentPanel();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_LABEL, DEVICE_2_NAME);
        networkViewPage.selectObjectInViewContentContains(NAME_COLUMN_LABEL, ETHERNET_LINK_NAME);
        networkViewPage.startEditingSelectedTrail();
        networkViewPage.unselectObjectInViewContent(OBJECT_TYPE_COLUMN_LABEL, OBJECT_TYPE_CONNECTION);
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_LABEL, DEVICE_1_NAME);
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_LABEL, DEVICE_2_NAME);
        networkViewPage.addSelectedObjectsToTermination();
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.selectConnectionTermination(START_TERMINATION_PATH);
        waitForPageToLoad();
        connectionWizardPage.setNonexistentCard();
        connectionWizardPage.terminatePort(START_PORT_TERMINATION);
        //Terminacja precyzyjna możliwa dopiero po rozwiązaniu błędu - OSSTRAIL-7374
        //connectionWizardPage.terminateTerminationPort(START_TERMINATION_POINT);
        connectionWizardPage.clickAccept();
        networkViewPage.selectObjectInViewContentContains(NAME_COLUMN_LABEL, ETHERNET_LINK_NAME);
        networkViewPage.openTerminationsTab();
        assertPresenceOfObjectInTab(0, PHYSICAL_DEVICE_NAME_COLUMN, TERMINATIONS_TAB_ID, PHYSICAL_DEVICE_NAME);
        assertPresenceOfObjectInTab(1, PORT_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ID, PORT_SHORT_IDENTIFIER);
        //Asercja na precyzyjną terminację (zamiast PORT), blokowana przez - OSSTRAIL-7374
        //assertPresenceOfObjectInTab(1, TERMINATION_POINT_NAME_COLUMN, TERMINATIONS_TAB_ID, TERMINATION_POINT_NAME);
    }

    @Test(priority = 16)
    @Description("Add selected trail to Ethernet Links routing")
    public void addSelectedTrailToRouting() {
        addCreatedTrailToNetworkView();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.addSelectedObjectsToRouting().accept();
        networkViewPage.selectObjectInViewContentContains(NAME_COLUMN_LABEL, ETHERNET_LINK_NAME);
        networkViewPage.openRouting1stLevelTab();
        assertPresenceOfObjectInTab(0, CONNECTION_NAME_COLUMN, ROUTING_1ST_LEVEL_TAB_ID, ROUTED_TRAIL_NAME);
    }

    @Test(priority = 7)
    @Description("Add selected IP Device to Ethernet Links element routing")
    public void addSelectedDeviceToRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_LABEL, ETHERNET_LINK_NAME_FULL);
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_LABEL, DEVICE_1_NAME);
        networkViewPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, ADD_TO_ROUTING_BUTTON);
        Wizard.createByComponentId(driver, webDriverWait, ELEMENT_ROUTING_WIZARD_ID).clickAccept();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_LABEL, DEVICE_1_NAME);
        networkViewPage.selectObjectInViewContentContains(NAME_COLUMN_LABEL, ETHERNET_LINK_NAME);
        networkViewPage.openRoutingElementsTab();
        networkViewPage.isObjectInRoutingElements(NAME_COLUMN_LABEL, DEVICE_1_NAME);
        assertPresenceOfObjectInOldTab();
    }

    @Test(priority = 18)
    @Description("Modify termination and Create associated IP Link")
    public void modifyTerminationAndCreateIPLink() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.ATTRIBUTES_AND_TERMINATIONS_ACTION);
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.clickNext();
        connectionWizardPage.selectConnectionTermination(END_TERMINATION_PATH);
        waitForPageToLoad();
        connectionWizardPage.setNonexistentCard();
        connectionWizardPage.terminatePort(END_PORT_TERMINATION);
        //Terminowanie precyzyjne blokowane przez - OSSTRAIL-7374
        //connectionWizardPage.terminateTerminationPort(END_TERMINATION_POINT);
        //connectionWizardPage.setCheckbox(CREATE_IP_LINK_CHECKBOX_ID, true);
        connectionWizardPage.clickAccept();
    }

    @Test(priority = 19)
    @Description("Remove terminations")
    public void removeTerminations() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.selectObjectInViewContentContains(NAME_COLUMN_LABEL, ETHERNET_LINK_NAME);
        networkViewPage.openTerminationsTab();
        selectAndRemoveTermination();
        selectAndRemoveTermination();
        assertIfTableIsEmpty(TERMINATIONS_TAB_ID);
    }

    @Test(priority = 100)
    @Description("Remove trails from Ethernet Links routing")
    public void removeRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.openRouting1stLevelTab();
        selectAndRemoveConnectionFromRouting();
        assertIfTableIsEmpty(ROUTING_1ST_LEVEL_TAB_ID);
    }

    @Test(priority = 11)
    @Description("Remove objects from Ethernet Links element routing")
    public void removeElementRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.openRoutingElementsTab();
        selectAndRemoveElementFromRouting();
        assertIfOldTableIsEmpty();
    }

    @Test(priority = 12)
    @Description("Finish rest of NRP and IP task")
    public void finishProcessesTask() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeNRP(processNRPCode);
    }

    @Test(priority = 13)
    @Description("Delete Ethernet Link")
    public void removeEthernetLink() {
        navigateToELInventoryView();
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow().callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_ETHERNET_LINK);
        Wizard.createByComponentId(driver, webDriverWait, DELETE_ETHERNET_LINK_WIZARD_ID).clickButtonById(DELETE_CONFIRMATION_BOX);
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.checkIfTableIsEmpty();
    }

    private void addTerminationDeviceToNetworkView(String deviceName) {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        networkViewPage.queryElementAndAddItToView("name", deviceName);
    }

    private void addCreatedTrailToNetworkView() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.CONNECTION_ACTION);
        networkViewPage.queryElementAndAddItToView(SEARCH_BY_COMPONENT_NAME_ID, ROUTED_CONNECTION_NAME);
    }

    private void assertPresenceOfObjectInTab(Integer index, String columnId, String tabId, String objectName) {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        String objectValue = networkViewPage.getObjectValueFromTab(index, columnId, tabId);
        Assert.assertEquals(objectValue, objectName);
    }

    private void assertPresenceOfObjectInOldTab() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        Assert.assertTrue(networkViewPage.isObjectInRoutingElements(NAME_COLUMN_LABEL, DEVICE_1_NAME));
    }

    private void assertIfTableIsEmpty(String tabID) {
        Assert.assertTrue(TableWidget.createById(driver, tabID, webDriverWait).hasNoData());
    }

    private void assertIfOldTableIsEmpty() {
        Assert.assertTrue(OldTable.createById(driver, webDriverWait, ELEMENT_ROUTING_TAB_ID).hasNoData());
    }

    private void selectAndRemoveTermination() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        TableWidget.createById(driver, TERMINATIONS_TAB_ID, webDriverWait).selectFirstRow();
        networkViewPage.removeSelectedTerminations();
        networkViewPage.refreshTerminationsTab();
    }

    private void selectAndRemoveConnectionFromRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        TableWidget.createById(driver, ROUTING_1ST_LEVEL_TAB_ID, webDriverWait).selectFirstRow();
        networkViewPage.deleteSelectedConnectionsFromRouting();
        networkViewPage.refreshFirstLevelRoutingTab();
    }

    private void selectAndRemoveElementFromRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.selectRoutingElement(TYPE_COLUMN_LABEL, DEVICE_TYPE);
        networkViewPage.deleteSelectedElementsFromRouting();
        networkViewPage.refreshElementRoutingTab();
    }

    private void assertEthernetLinkAttributes(NetworkViewPage networkViewPage, EthernetLinkAttributes ethernetLinkAttributes) {
        String latencyAttribute = "Latency";
        String actualLatency = networkViewPage.getAttributeValue(latencyAttribute);
        String roleAttribute = "Role";
        String actualRole = networkViewPage.getAttributeValue(roleAttribute);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualLatency, ethernetLinkAttributes.latency, String.format(ASSERT_MESSAGE_PATTERN, latencyAttribute));
        softAssert.assertEquals(actualRole, ethernetLinkAttributes.role, String.format(ASSERT_MESSAGE_PATTERN, roleAttribute));
        softAssert.assertAll();
    }

    private void fillEthernetLinkAttributesForCreate(EthernetLinkAttributes ethernetLinkAttributes, EthernetLinkWizardPage elWizard) {
        waitForPageToLoad();
        elWizard.setName(ethernetLinkAttributes.name);
        elWizard.setDescription(ethernetLinkAttributes.description);
        elWizard.setIsTrunk(ethernetLinkAttributes.isTrunk);
        elWizard.setSpeed(ethernetLinkAttributes.speed);
        elWizard.setRole(ethernetLinkAttributes.role);
        elWizard.setLatency(ethernetLinkAttributes.latency);
        elWizard.setEffectiveCapacityUnit(ethernetLinkAttributes.effectiveCapacityUnit);
        elWizard.setEffectiveCapacity(ethernetLinkAttributes.effectiveCapacity);
    }

    private void fillEthernetLinkAttributesForUpdate(EthernetLinkAttributes ethernetLinkAttributes, EthernetLinkWizardPage elWizard) {
        elWizard.setDescription(ethernetLinkAttributes.description);
        elWizard.setSpeed(ethernetLinkAttributes.speed);
        elWizard.setRole(ethernetLinkAttributes.role);
        elWizard.setLatency(ethernetLinkAttributes.latency);
        elWizard.setEffectiveCapacity(ethernetLinkAttributes.effectiveCapacity);
    }

    private void navigateToCreateEthernetLinkWizard() {
        homePage.goToHomePage(driver, BASIC_URL);
        expandTiles();
        waitForPageToLoad();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.openConnectionWizard(ETHERNET_LINK_TRAIL_TYPE);
    }

    private void navigateToELInventoryView() {
        SideMenu sidemenu = SideMenu.create(driver, webDriverWait);
        sidemenu.callActionByLabel(INVENTORY_VIEW_SIDE_MENU, RESOURCE_INVENTORY_VIEW_SIDE_MENU);
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(ETHERNET_LINK_TRAIL_TYPE);
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(ETHERNET_LINK_NAME).selectFirstRow();
    }

    private void expandTiles() {
        ToolsManagerPage toolsManagerPage = new ToolsManagerPage(driver);
        waitForPageToLoad();
        ToolsManagerWindow toolsManagerWindow = toolsManagerPage.getToolsManager();
        waitForPageToLoad();
        toolsManagerWindow.openApplication(RESOURCE_INVENTORY_VIEW_SIDE_MENU, NETWORK_VIEW_SIDE_MENU);
    }

    private EthernetLinkAttributes getELAttributesForCreate() {
        EthernetLinkAttributes elAttributes = new EthernetLinkAttributes();
        elAttributes.name = ETHERNET_LINK_NAME;
        elAttributes.description = ETHERNET_LINK_DESCRIPTION;
        elAttributes.isTrunk = IS_TRUNK_TRUE;
        elAttributes.speed = SPEED_CREATE;
        elAttributes.role = ROLE_CREATE;
        elAttributes.latency = LATENCY_CREATE;
        elAttributes.effectiveCapacityUnit = EFFECTIVE_CAPACITY_UNIT_CREATE;
        elAttributes.effectiveCapacity = EFFECTIVE_CAPACITY_CREATE;
        return elAttributes;
    }

    private EthernetLinkAttributes getELAttributesForUpdate() {
        EthernetLinkAttributes elAttributes = new EthernetLinkAttributes();
        elAttributes.description = ETHERNET_LINK_DESCRIPTION_UPDATE;
        elAttributes.speed = SPEED_UPDATE;
        elAttributes.role = ROLE_UPDATE;
        elAttributes.latency = LATENCY_UPDATE;
        elAttributes.effectiveCapacity = EFFECTIVE_CAPACITY_UPDATE;
        return elAttributes;
    }

    private static class EthernetLinkAttributes {
        private String name;
        private String description;
        private String isTrunk;
        private String speed;
        private String role;
        private String latency;
        private String effectiveCapacityUnit;
        private String effectiveCapacity;
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void checkMessageType() {
        Assert.assertEquals((getFirstMessage().getMessageType()), SystemMessageContainer.MessageType.SUCCESS);
    }

    private void checkMessageContainsText(String message) {
        Assert.assertTrue((getFirstMessage().getText())
                .contains(message));
    }

    private void checkMessageText() {
        Assert.assertEquals((getFirstMessage().getText()), "The task properly assigned.");
    }

    private void checkMessageSize() {
        Assert.assertEquals((SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .size()), 1);
    }

    private SystemMessageContainer.Message getFirstMessage() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getFirstMessage()
                .orElseThrow(() -> new RuntimeException("The list is empty"));
    }

    private void checkTaskAssignment() {
        checkMessageType();
        checkMessageText();
    }
}