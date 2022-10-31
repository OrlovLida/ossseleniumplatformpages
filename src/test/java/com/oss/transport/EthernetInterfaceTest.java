package com.oss.transport;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessOverviewPage;
import com.oss.pages.bpm.TasksPageV2;
import com.oss.pages.bpm.processinstances.ProcessWizardPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.transport.ethernetInterface.EIWizardPage;

import io.qameta.allure.Description;

/**
 * @author Kamil Jacko
 */
public class EthernetInterfaceTest extends BaseTestCase {

    private static final String PRE_CREATED_IP_NETWORK_ELEMENT = "SeleniumEITest";
    private static final String CREATE_ETHERNET_INTERFACE = "CreateEthernetInterfaceLogicalFunctionContextAction";
    private static final String AUTO_NEGOTIATION_CREATE = "Enabled";
    private static final String AUTO_NEGOTIATION = "Disabled";
    private static final String AUTO_ADVERTISEMENT_MAX_CAPACITY_CREATE = "True";
    private static final String ADMINISTRATIVE_SPEED = "10M";
    private static final String ADMINISTRATIVE_DUPLEX_MODE = "Half Duplex";
    private static final String MAXIMUM_FRAME_SIZE_CREATE = "100";
    private static final String MAXIMUM_FRAME_SIZE = "200";
    private static final String FLOW_CONTROL_CREATE = "Ingress";
    private static final String FLOW_CONTROL = "Egress";
    private static final String FLOW_CONTROL_EMPTY_VALUE = "None";
    private static final String MTU_CREATE = "25";
    private static final String MTU = "10";
    private static final String ENCAPSULATION_CREATE = "QinQ";
    private static final String ENCAPSULATION = "802.1Q";
    private static final String BANDWIDTH_CREATE = "100000000";
    private static final String BANDWIDTH = "7000";
    private static final String SWITCH_PORT = "Yes";
    private static final String SWITCH_MODE = "Access";
    private static final String ACCESS_FUNCTION_CREATE = "Uplink";
    private static final String ACCESS_FUNCTION = "Downlink";
    private static final String DESCRIPTION_CREATE = "Opis CREATE";
    private static final String DESCRIPTION = "Opis_UPDATE";
    private static final String DELETE_ETHERNET_INTERFACE_ID = "DeleteEthernetInterfaceContextAction";
    private static final String EDIT_ETHERNET_INTERFACE_LABEL = "Edit Ethernet Interface";
    private static final String RESOURCE_INVENTORY_VIEW_SIDE_MENU = "Resource Inventory";
    private static final String INVENTORY_VIEW_SIDE_MENU = "Inventory View";
    private static final String SEARCH_TYPE_ETHERNET_INTERFACE = "Ethernet Interface";
    private static final String SEARCH_TYPE_IP_NETWORK_ELEMENT = "IP Network Element";
    private String processNRPCode;

    @BeforeClass
    public void openWebConsole() {
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("Process Overview", "BPM and Planning", "Network Planning");
        waitForPageToLoad();
    }

    @Test(priority = 1)
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

    @Test(priority = 2)
    @Description("Start High Level Planning Task")
    public void startHLPTask() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        checkTaskAssignment();
    }

    @Test(priority = 3)
    @Description("Create Ethernet Interface")
    public void createEI() {
        EIAttributes eiAttributes = getEIAttributesForCreate();
        EIWizardPage eiWizard = navigateToEICreateWizard();
        fillEIWizardForCreate(eiAttributes, eiWizard);
        eiWizard.clickNext().clickAccept();
        navigateToEIInventoryView(SEARCH_TYPE_ETHERNET_INTERFACE);
        NewInventoryViewPage eiInventoryView = new NewInventoryViewPage(driver, webDriverWait);
        assertEIAttributes(eiAttributes, eiInventoryView);
    }

    @Test(priority = 4)
    @Description("Update Ethernet Interface")
    public void updateEI() {
        EIAttributes eiAttributes = getEIAttributesForUpdate();
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.callActionByLabel(ActionsContainer.EDIT_GROUP_ID, EDIT_ETHERNET_INTERFACE_LABEL);
        EIWizardPage eiWizard = new EIWizardPage(driver);
        fillEIWizardForUpdate(eiAttributes, eiWizard);
        eiWizard.clickNext().clickAccept();
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.selectFirstRow();
        assertEIAttributes(eiAttributes, newInventoryViewPage);
    }

    @Test(priority = 5)
    @Description("Clear Ethernet Interface attributes")
    public void clearEI() {
        EIAttributes eiAttributes = getEIAttributesForClearing();
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.callActionByLabel(ActionsContainer.EDIT_GROUP_ID, EDIT_ETHERNET_INTERFACE_LABEL);
        EIWizardPage eiWizard = new EIWizardPage(driver);
        fillEIWizardEmptyAttributes(eiWizard);
        eiWizard.clickNext().clickAccept();
        NewInventoryViewPage newInventoryViewPage1 = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage1.refreshMainTable();
        newInventoryViewPage1.selectFirstRow();
        assertEIAttributesClear(eiAttributes, newInventoryViewPage1);
    }

    @Test(priority = 6)
    @Description("Finish rest of NRP and IP Tasks")
    public void finishProcessesTasks() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeNRP(processNRPCode);
    }

    @Test(priority = 7)
    @Description("Remove Ethernet Interface")
    public void deleteEI() {
        navigateToEIInventoryView(SEARCH_TYPE_ETHERNET_INTERFACE);
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow().callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_ETHERNET_INTERFACE_ID).clickConfirmationRemovalButton();
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.searchObject(PRE_CREATED_IP_NETWORK_ELEMENT);
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
    }

    private EIAttributes getEIAttributesForCreate() {
        EIAttributes eiAttributes = new EIAttributes();
        eiAttributes.autoNegotiation = AUTO_NEGOTIATION_CREATE;
        eiAttributes.autoAdvertisementMaxCapacity = AUTO_ADVERTISEMENT_MAX_CAPACITY_CREATE;
        eiAttributes.maximumFrameSize = MAXIMUM_FRAME_SIZE_CREATE;
        eiAttributes.flowControl = FLOW_CONTROL_CREATE;
        eiAttributes.MTU = MTU_CREATE;
        eiAttributes.encapsulation = ENCAPSULATION_CREATE;
        eiAttributes.bandwidth = BANDWIDTH_CREATE;
        eiAttributes.switchPort = SWITCH_PORT;
        eiAttributes.accessFunction = ACCESS_FUNCTION_CREATE;
        eiAttributes.description = DESCRIPTION_CREATE;

        return eiAttributes;
    }

    private EIAttributes getEIAttributesForUpdate() {
        EIAttributes eiAttributes = new EIAttributes();
        eiAttributes.autoNegotiation = AUTO_NEGOTIATION;
        eiAttributes.administrativeSpeed = ADMINISTRATIVE_SPEED;
        eiAttributes.administrativeDuplexMode = ADMINISTRATIVE_DUPLEX_MODE;
        eiAttributes.maximumFrameSize = MAXIMUM_FRAME_SIZE;
        eiAttributes.flowControl = FLOW_CONTROL;
        eiAttributes.MTU = MTU;
        eiAttributes.encapsulation = ENCAPSULATION;
        eiAttributes.bandwidth = BANDWIDTH;
        eiAttributes.switchPort = SWITCH_PORT;
        eiAttributes.switchMode = SWITCH_MODE;
        eiAttributes.accessFunction = ACCESS_FUNCTION;
        eiAttributes.description = DESCRIPTION;

        return eiAttributes;
    }

    private EIAttributes getEIAttributesForClearing() {
        EIAttributes eiAttributes = new EIAttributes();
        eiAttributes.autoNegotiation = AUTO_NEGOTIATION;
        eiAttributes.flowControl = FLOW_CONTROL_EMPTY_VALUE;
        eiAttributes.encapsulation = ENCAPSULATION;

        return eiAttributes;
    }

    private EIWizardPage navigateToEICreateWizard() {
        navigateToEIInventoryView(SEARCH_TYPE_IP_NETWORK_ELEMENT);
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_ETHERNET_INTERFACE);
        return new EIWizardPage(driver);
    }

    private void navigateToEIInventoryView(String searchingType) {
        SideMenu sidemenu = SideMenu.create(driver, webDriverWait);
        sidemenu.callActionByLabel(INVENTORY_VIEW_SIDE_MENU, RESOURCE_INVENTORY_VIEW_SIDE_MENU);
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(searchingType);
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(PRE_CREATED_IP_NETWORK_ELEMENT).selectFirstRow();
    }

    private void fillEIWizardForCreate(EIAttributes eiAttributes, EIWizardPage eiWizard) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        eiWizard.setAutoNegotiation(eiAttributes.autoNegotiation);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        eiWizard.setAutoAdvertisementMaxCapacity(eiAttributes.autoAdvertisementMaxCapacity);
        eiWizard.setMaximumFrameSize(eiAttributes.maximumFrameSize);
        eiWizard.setFlowControl(eiAttributes.flowControl);
        eiWizard.setMTU(eiAttributes.MTU);
        eiWizard.setEncapsulation(eiAttributes.encapsulation);
        eiWizard.setBandwidth(eiAttributes.bandwidth);
        eiWizard.setSwitchPort(eiAttributes.switchPort);
        eiWizard.setAccessFunction(eiAttributes.accessFunction);
        eiWizard.setDescription(eiAttributes.description);
    }

    private void fillEIWizardForUpdate(EIAttributes eiAttributes, EIWizardPage eiWizard) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        eiWizard.setAutoNegotiation(eiAttributes.autoNegotiation);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        eiWizard.setAdministrativeSpeed(eiAttributes.administrativeSpeed);
        eiWizard.setAdministrativeDuplexMode(eiAttributes.administrativeDuplexMode);
        eiWizard.setMaximumFrameSize(eiAttributes.maximumFrameSize);
        eiWizard.setFlowControl(eiAttributes.flowControl);
        eiWizard.setMTU(eiAttributes.MTU);
        eiWizard.setEncapsulation(eiAttributes.encapsulation);
        eiWizard.setBandwidth(eiAttributes.bandwidth);
        eiWizard.setSwitchPort(eiAttributes.switchPort);
        eiWizard.setSwitchMode(eiAttributes.switchMode);
        eiWizard.setAccessFunction(eiAttributes.accessFunction);
        eiWizard.setDescription(eiAttributes.description);
    }

    private void fillEIWizardEmptyAttributes(EIWizardPage eiWizard) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        eiWizard.clearMaximumFrameSize();
        eiWizard.clearFlowControl();
        eiWizard.clearMTU();
        eiWizard.clearBandwidth();
        eiWizard.clearSwitchPort();
        eiWizard.clearSwitchMode();
        eiWizard.clearAccessFunction();
        eiWizard.clearDescription();
    }

    private void assertEIAttributes(EIAttributes eiAttributes, NewInventoryViewPage eiInventoryView) {
        SoftAssert softAssert = new SoftAssert();
        String maximumFrameSize = eiInventoryView.getPropertyPanelValue("maximumFrameSize");
        softAssert.assertEquals(maximumFrameSize, eiAttributes.maximumFrameSize);

        String bandwidth = eiInventoryView.getPropertyPanelValue("bandwidth");
        softAssert.assertEquals(bandwidth, eiAttributes.bandwidth);

        String autoNegotiation = eiInventoryView.getPropertyPanelValue("autoNegotiation");
        softAssert.assertEquals(autoNegotiation, eiAttributes.autoNegotiation);

        String accessFunction = eiInventoryView.getPropertyPanelValue("accessFunction");
        softAssert.assertEquals(accessFunction, eiAttributes.accessFunction);

        String encapsulation = eiInventoryView.getPropertyPanelValue("encapsulation");
        softAssert.assertEquals(encapsulation, eiAttributes.encapsulation);
        softAssert.assertAll();
    }

    private void assertEIAttributesClear(EIAttributes eiAttributes, NewInventoryViewPage eiInventoryView) {
        SoftAssert softAssert = new SoftAssert();
        String maximumFrameSize = eiInventoryView.getPropertyPanelValue("maximumFrameSize");
        softAssert.assertEquals(maximumFrameSize, "-");

        String flowControl = eiInventoryView.getPropertyPanelValue("flowControl");
        softAssert.assertEquals(flowControl, eiAttributes.flowControl);
        softAssert.assertAll();
    }

    private static class EIAttributes {
        private String autoNegotiation;
        private String administrativeSpeed;
        private String administrativeDuplexMode;
        private String autoAdvertisementMaxCapacity;
        private String maximumFrameSize;
        private String flowControl;
        private String MTU;
        private String encapsulation;
        private String bandwidth;
        private String switchPort;
        private String switchMode;
        private String accessFunction;
        private String description;
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

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
