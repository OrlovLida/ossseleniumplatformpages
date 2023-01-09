package com.oss.E2E;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.transport.tpt.tp.api.dto.TerminationPointDetailDTO;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.Popup;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.trail.AELWizardPage;
import com.oss.pages.transport.trail.ConnectionWizardPage;
import com.oss.pages.transport.trail.RoutingWizardPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.EthernetCoreRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.services.EthernetCoreClient;
import com.oss.services.ResourceCatalogClient;
import com.oss.services.TPServiceClient;
import com.oss.untils.Environment;

import io.qameta.allure.Description;

public class BucOssTpt011Test extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(BucOssTpt011Test.class);
    private static final String SITE = "Site";
    private static final String COUNTRY_NAME = "Poland";
    private static final String REGION_NAME = "Silesia";
    private static final String DISTRICT_NAME = "District 1";
    private static final String CITY_NAME = "Gliwice";
    private static final String POSTAL_CODE_NAME = "44100";
    private static final String LOCATION_NAME = "Jasna";
    private static final String DEVICE_1_NAME = "Jasna_ASR9001_1";
    private static final String DEVICE_2_NAME = "Jasna_ASR9001_2";
    private static final String DEVICE_MODEL = "ASR9001";
    private static final String DEVICE_MODEL_TYPE = "DeviceModel";
    private static final String FIRST_ETHERNET_LINK_NAME = "Jasna_1_EthernetLink";
    private static final String SECOND_ETHERNET_LINK_NAME = "Jasna_2_EthernetLink";
    private static final String CATEGORY_NAME = "Network Domains";
    private static final String VIEW_NAME = "Network View";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String NAME_COLUMN = "Name";
    private static final String AGGREGATED_ETHERNET_LINK = "Aggregated Ethernet Link";
    private static final String AGGREGATED_ETHERNET_LINK_NAME = "BUC AE Link #1";
    private static final String AGGREGATED_ETHERNET_LINK_DESCRIPTION = "AE Link #1";
    private static final String AGGREGATED_ETHERNET_LINK_SPEED = "2000";
    private static final String PROTOCOL = "LACP";
    private static final String AGGREGATED_ETHERNET_LINK_CAPACITY = "2000";
    private static final String START_CONNECTION_DATA_PATH = "1.1_1";
    private static final String END_CONNECTION_DATA_PATH = "1.1_2";
    private static final String WRONG_TERMINATION_PATTERN = "%s termination on Network Element is incorrect.";
    private static final String AEI_NAME_1 = "BUC AE Interface #1";
    private static final String AEI_NAME_2 = "BUC AE Interface #2";
    private static final String AEI_NUMBER = "1";
    private static final String AEI_ENCAPSULATION = "ETH";
    private static final String AEI_LACP_MODE = "ACTIVE";
    private static final String AEI_MTU = "4900";
    private static final String AEI_MAC_ADDRESS = "00-0C-29-94-55-05";
    private static final String AEI_MINIMUM_ACTIVE_LINKS = "2";
    private static final String AEI_MINIMUM_BANDWIDTH = "2000";
    private static final String ETHERNET_INTERFACE = "EthernetInterface";
    private static final String CREATED_ETHERNET_LINKS_PATTERN = "Created two Ethernet Links with ids: %s and %s.";
    private static final String DELETED_ETHERNET_LINKS_PATTERN = "Deleted Ethernet Link with id: %s.";
    private static final String CANNOT_GET_ETHERNET_INTERFACE_EXCEPTION = "Cannot find Ethernet Interface.";
    private static final String PROTECTION_TYPE = "Alternative";
    private static final String LINE_TYPE = "Primary";
    private static final String SEQUENCE_NUMBER = "Test AEL 123";
    private static final String DELETE_AEL_BUTTON_ID = "wizard-submit-button-deleteWidgetId";
    private static final String DELETE_AEI_ACTION_ID = "DeleteAggregatedEthernetInterfaceContextAction";
    private static final String CONFIRM_DELETE_AEI_BUTTON_ID = "ConfirmationBox_deleteAppId_action_button";
    private static final String DELETE_AEL_ASSERTION_ERROR = "Problem with system message after Aggregated Ethernet Link removal.";
    private static final String ATTRIBUTE_VALIDATION_PATTERN = "Wrong value of attribute: %s.";
    private static final String CONNECTION_TYPE_WIZARD_NAME = "Select Connection Type";
    private static final String CONNECTION_WIZARD_NAME = "Connection Wizard";
    private static final String CONNECTION_WIZARD_OPEN_VALIDATION = "Connection Wizard not opened.";
    private static final String ATTRIBUTES_STEP_NAME = "1. Attributes";
    private static final String TERMINATIONS_STEP_NAME = "2. Terminations";
    private static final String AE_INTERFACES_STEP_NAME = "3. AE Interfaces";
    private static final String WIZARD_NAME_VALIDATION = "Wrong name of wizard.";
    private static final String WIZARD_STEP_NAME_VALIDATION = "Wrong name of step in wizard.";
    private static final String WIZARD_LABEL_NOT_PRESENT = "Cannot find appropriate label in routing wizard.";
    private static final String AEI_URL = String.format("%s/#/views/management/views/inventory-view/AggregatedEthernetInterface_TP?perspective=LIVE", BASIC_URL);
    private static Long FIRST_ETHERNET_LINK_ID;
    private static Long SECOND_ETHERNET_LINK_ID;
    private final Environment env = Environment.getInstance();
    private PhysicalInventoryRepository physicalInventoryRepository;
    private NetworkViewPage networkViewPage;

    @BeforeClass
    public void checkPrereq() {
        physicalInventoryRepository = new PhysicalInventoryRepository(env);
        TPServiceClient tpServiceClient = new TPServiceClient(env);
        String locationId = getOrCreateLocation();
        Long firstDeviceId = getOrCreateDevice(locationId, DEVICE_1_NAME);
        Long secondDeviceId = getOrCreateDevice(locationId, DEVICE_2_NAME);
        List<TerminationPointDetailDTO> firstDeviceTP = tpServiceClient.getTerminationPointDetailList(firstDeviceId);
        List<TerminationPointDetailDTO> secondDeviceTP = tpServiceClient.getTerminationPointDetailList(secondDeviceId);
        FIRST_ETHERNET_LINK_ID = createEthernetLink(FIRST_ETHERNET_LINK_NAME, getEthernetInterface(firstDeviceTP, 0), getEthernetInterface(secondDeviceTP, 0));
        SECOND_ETHERNET_LINK_ID = createEthernetLink(SECOND_ETHERNET_LINK_NAME, getEthernetInterface(firstDeviceTP, 1), getEthernetInterface(secondDeviceTP, 1));
        LOGGER.debug(String.format(CREATED_ETHERNET_LINKS_PATTERN, FIRST_ETHERNET_LINK_ID, SECOND_ETHERNET_LINK_ID));
    }

    @Test(priority = 1, description = "Open Aggregated Ethernet Link wizard.")
    @Description("Open Aggregated Ethernet Link wizard.")
    public void openAEIwizard() {
        waitForPageToLoad();
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        waitForPageToLoad();
        PerspectiveChooser.create(driver, webDriverWait).setWithoutRemoved();
        waitForPageToLoad();
        ToolsManagerWindow toolsManagerWindow = ToolsManagerWindow.create(driver, webDriverWait);
        toolsManagerWindow.openApplication(CATEGORY_NAME, VIEW_NAME);
        networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        networkViewPage.queryElementAndAddItToView(NAME, DEVICE_1_NAME);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        networkViewPage.queryElementAndAddItToView(NAME, DEVICE_2_NAME);
        networkViewPage.expandViewContentPanel();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN, DEVICE_1_NAME);
        waitForPageToLoad();
        networkViewPage.useContextAction(ActionsContainer.CREATE_GROUP_ID, NetworkViewPage.CREATE_CONNECTION_ID);
        Assert.assertEquals(networkViewPage.getConnectionTypeWizardName(), CONNECTION_TYPE_WIZARD_NAME, WIZARD_NAME_VALIDATION);
        networkViewPage.selectTrailType(AGGREGATED_ETHERNET_LINK);
        networkViewPage.acceptTrailType();
        waitForPageToLoad();
        Assert.assertTrue(CSSUtils.isElementPresent(driver, ConnectionWizardPage.WIZARD_ID), CONNECTION_WIZARD_OPEN_VALIDATION);
    }

    @Test(priority = 2, description = "Fill in the Aggregated Ethernet Link attributes.", dependsOnMethods = {"openAEIwizard"})
    @Description("Fill in the Aggregated Ethernet Link attributes.")
    public void fillAttributes() {
        AELWizardPage wizard = new AELWizardPage(driver);
        Assert.assertEquals(wizard.getWizard().getWizardName(), CONNECTION_WIZARD_NAME, WIZARD_NAME_VALIDATION);
        Assert.assertEquals(wizard.getWizard().getCurrentStepTitle(), ATTRIBUTES_STEP_NAME, WIZARD_STEP_NAME_VALIDATION);
        wizard.setName(AGGREGATED_ETHERNET_LINK_NAME);
        wizard.setDescription(AGGREGATED_ETHERNET_LINK_DESCRIPTION);
        wizard.setSpeed(AGGREGATED_ETHERNET_LINK_SPEED);
        wizard.setAggregationProtocol(PROTOCOL);
        wizard.setEffectiveCapacity(AGGREGATED_ETHERNET_LINK_CAPACITY);
        wizard.clickNext();
        wizard.getWizard().waitForWizardToLoad();
        Assert.assertEquals(wizard.getWizard().getCurrentStepTitle(), TERMINATIONS_STEP_NAME, WIZARD_STEP_NAME_VALIDATION);
    }

    @Test(priority = 3, description = "Determine the Aggregated Ethernet Link terminations (Start / End) at the level of Device.", dependsOnMethods = {"fillAttributes"})
    @Description("Determine the Aggregated Ethernet Link terminations (Start / End) at the level of Device.")
    public void fillTerminations() {
        SoftAssert softAssert = new SoftAssert();
        AELWizardPage wizard = new AELWizardPage(driver);
        wizard.selectConnectionTermination(START_CONNECTION_DATA_PATH);
        softAssert.assertEquals(wizard.getNetworkElementTermination(), DEVICE_1_NAME, String.format(WRONG_TERMINATION_PATTERN, "Start"));
        wizard.selectConnectionTermination(END_CONNECTION_DATA_PATH);
        softAssert.assertEquals(wizard.getNetworkElementTermination(), DEVICE_2_NAME, String.format(WRONG_TERMINATION_PATTERN, "End"));
        wizard.clickNext();
        softAssert.assertAll();
        wizard.getWizard().waitForWizardToLoad();
        Assert.assertEquals(wizard.getWizard().getCurrentStepTitle(), AE_INTERFACES_STEP_NAME, WIZARD_STEP_NAME_VALIDATION);
    }

    @Test(priority = 4, description = "Create Aggregated Ethernet Interfaces at both endpoints and set their related attributes.", dependsOnMethods = {"fillTerminations"})
    @Description("Create Aggregated Ethernet Interfaces at both endpoints and set their related attributes.")
    public void fillAggregatedEthernetInterfaces() {
        AELWizardPage wizard = new AELWizardPage(driver);
        wizard.setCreateStartAggregatedEthernetInterface();
        wizard.setStartAeiName(AEI_NAME_1);
        wizard.setStartAeiNumber(AEI_NUMBER);
        wizard.setStartAeiAggregationProtocol(PROTOCOL);
        wizard.setStartAeiEncapsulation(AEI_ENCAPSULATION);
        wizard.setStartAeiLacpMode(AEI_LACP_MODE);
        wizard.setStartAeiLacpShortPeriod();
        wizard.setStartAeiMtu(AEI_MTU);
        wizard.setStartAeiMacAddress(AEI_MAC_ADDRESS);
        wizard.setStartAeiMinimumActiveLinks(AEI_MINIMUM_ACTIVE_LINKS);
        wizard.setStartAeiMinimumBandwidth(AEI_MINIMUM_BANDWIDTH);
        wizard.setCreateEndAggregatedEthernetInterface();
        wizard.setEndAeiName(AEI_NAME_2);
        wizard.setEndAeiNumber(AEI_NUMBER);
        wizard.setEndAeiAggregationProtocol(PROTOCOL);
        wizard.setEndAeiEncapsulation(AEI_ENCAPSULATION);
        wizard.setEndAeiLacpMode(AEI_LACP_MODE);
        wizard.setEndAeiLacpShortPeriod();
        wizard.setEndAeiMtu(AEI_MTU);
        wizard.setEndAeiMacAddress(AEI_MAC_ADDRESS);
        wizard.setEndAeiMinimumActiveLinks(AEI_MINIMUM_ACTIVE_LINKS);
        wizard.setEndAeiMinimumBandwidth(AEI_MINIMUM_BANDWIDTH);
        wizard.clickAccept();
        checkPopupMessageType();
        waitForPageToLoad();
    }

    @Test(priority = 5, description = "Select created Ethernet Links and open wizard to add them to the Aggregated Ethernet Links routing.", dependsOnMethods = {"fillAggregatedEthernetInterfaces"})
    @Description("Select created Ethernet Links and open wizard to add them to the Aggregated Ethernet Links routing.")
    public void openRoutingWizard() {
        networkViewPage = new NetworkViewPage(driver);
        networkViewPage.startEditingSelectedTrail();
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.CONNECTION_ACTION);
        networkViewPage.queryElementAndAddItToView(ID, FIRST_ETHERNET_LINK_ID.toString());
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.CONNECTION_ACTION);
        networkViewPage.queryElementAndAddItToView(ID, SECOND_ETHERNET_LINK_ID.toString());
        networkViewPage.expandViewContentPanel();
        networkViewPage.selectObjectInViewContentContains(NAME_COLUMN, FIRST_ETHERNET_LINK_NAME);
        waitForPageToLoad();
        RoutingWizardPage wizard = networkViewPage.addSelectedObjectsToRouting();
        Assert.assertEquals(wizard.getWizard().getWizardName(), CONNECTION_WIZARD_NAME, WIZARD_NAME_VALIDATION);
    }

    @Test(priority = 6, description = "Set routing attributes.", dependsOnMethods = {"openRoutingWizard"})
    @Description("Set routing attributes.")
    public void fillRoutingWizard() {
        RoutingWizardPage wizard = new RoutingWizardPage(driver);
        String connectionPath = AGGREGATED_ETHERNET_LINK_NAME + "." + FIRST_ETHERNET_LINK_NAME;
        if (!isNodePresent(wizard, connectionPath)) {
            wizard.clickCancel();
            networkViewPage = new NetworkViewPage(driver);
            networkViewPage.unselectObjectInViewContentContains(NAME_COLUMN, FIRST_ETHERNET_LINK_NAME);
            networkViewPage.unselectObjectInViewContentContains(NAME_COLUMN, SECOND_ETHERNET_LINK_NAME);
            Assert.fail(WIZARD_LABEL_NOT_PRESENT);
        }
        wizard.selectConnection(connectionPath);
        wizard.setProtectionType(PROTECTION_TYPE);
        wizard.setLineType(LINE_TYPE);
        wizard.setSequenceNumber(SEQUENCE_NUMBER);
        wizard.accept();
        checkPopupMessageType();
        waitForPageToLoad();
    }

    @Test(priority = 7, description = "Check Aggregated Ethernet Link attributes.", dependsOnMethods = {"fillAggregatedEthernetInterfaces"})
    @Description("Check Aggregated Ethernet Link attributes.")
    public void validateAELAttributes() {
        networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandViewContentPanel();
        networkViewPage.selectObjectInViewContentContains(NAME_COLUMN, AGGREGATED_ETHERNET_LINK_NAME);
        networkViewPage.expandAttributesPanel();
        List<String> attributes = Arrays.asList(NAME, "label", "type", "layer", "AggregationProtocol", "SpeedMbps", "EffectiveCapacityMbps",
                "routingStatus", "terminationStatus", "adaptation.adaptationType", "topology", "lifecycleState", "startTermination.location.name", "startTermination.physicalDevice.name",
                "startTermination.terminationPoint.name", "endTermination.location.name", "endTermination.physicalDevice.name", "endTermination.terminationPoint.name");
        List<String> expectedValues = Arrays.asList(AGGREGATED_ETHERNET_LINK_NAME, AGGREGATED_ETHERNET_LINK_NAME, "AggregatedEthernetLink", "ETHERNET", PROTOCOL,
                AGGREGATED_ETHERNET_LINK_SPEED, "20", "Complete", "Precise", "Unstructured", "Line", AEI_LACP_MODE, LOCATION_NAME, DEVICE_1_NAME, AEI_NAME_1,
                LOCATION_NAME, DEVICE_2_NAME, AEI_NAME_2);
        SoftAssert softAssert = new SoftAssert();
        int i = 0;
        for (String attribute : attributes) {
            softAssert.assertEquals(networkViewPage.getAttributeValue(attribute), expectedValues.get(i), String.format(ATTRIBUTE_VALIDATION_PATTERN, attribute));
            i++;
        }
        softAssert.assertAll();
    }

    @Test(priority = 8, description = "Remove Aggregated Ethernet Link.", dependsOnMethods = {"fillAggregatedEthernetInterfaces"})
    @Description("Remove Aggregated Ethernet Link.")
    public void deleteAEL() {
        networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.DELETE_CONNECTION_ID);
        Popup.create(driver, webDriverWait).clickButtonById(DELETE_AEL_BUTTON_ID);
        checkPopupMessageType();
    }

    @Test(priority = 9, description = "Remove Aggregated Ethernet Interfaces.", dependsOnMethods = {"fillAggregatedEthernetInterfaces"})
    @Description("Remove Aggregated Ethernet Interfaces.")
    public void deleteAEI() {
        driver.get(AEI_URL);
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(AEI_NAME_1);
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_AEI_ACTION_ID);
        ConfirmationBox.create(driver, webDriverWait).clickButtonById(CONFIRM_DELETE_AEI_BUTTON_ID);
        checkPopupMessageType();
        waitForPageToLoad();
        newInventoryViewPage.getMainTable().unselectAllRows();
        waitForPageToLoad();
        newInventoryViewPage.searchObject(AEI_NAME_2);
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_AEI_ACTION_ID);
        ConfirmationBox.create(driver, webDriverWait).clickButtonById(CONFIRM_DELETE_AEI_BUTTON_ID);
        checkPopupMessageType();
        waitForPageToLoad();
    }

    @AfterClass
    public void deleteEthernetLinks() {
        if (FIRST_ETHERNET_LINK_ID != null) {
            deleteEthernetLink(FIRST_ETHERNET_LINK_ID);
            LOGGER.debug(String.format(DELETED_ETHERNET_LINKS_PATTERN, FIRST_ETHERNET_LINK_ID));
        }
        if (SECOND_ETHERNET_LINK_ID != null) {
            deleteEthernetLink(SECOND_ETHERNET_LINK_ID);
            LOGGER.debug(String.format(DELETED_ETHERNET_LINKS_PATTERN, SECOND_ETHERNET_LINK_ID));
        }
    }

    private String getOrCreateLocation() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.getOrCreateLocation(LOCATION_NAME, SITE, prepareFirstAddress());
    }

    private Long prepareFirstAddress() {
        AddressRepository addressRepository = new AddressRepository(Environment.getInstance());
        return addressRepository.updateOrCreateAddress(COUNTRY_NAME, POSTAL_CODE_NAME, REGION_NAME, CITY_NAME, DISTRICT_NAME);
    }

    private Long getOrCreateDevice(String locationId, String deviceName) {
        if (!isDevicePresent(locationId, deviceName)) {
            return createDevice(locationId, deviceName);
        }
        return physicalInventoryRepository.getDeviceId(locationId, deviceName);
    }

    private Long createDevice(String locationId, String deviceName) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        Long deviceModelId = resourceCatalogClient.getModelIds(DEVICE_MODEL);
        return physicalInventoryRepository.createDevice(SITE, Long.valueOf(locationId), deviceModelId, deviceName,
                DEVICE_MODEL_TYPE);
    }

    private boolean isDevicePresent(String locationId, String deviceName) {
        return physicalInventoryRepository.isDevicePresent(locationId, deviceName);
    }

    private Long createEthernetLink(String name, Long firstEI, Long secondEI) {
        EthernetCoreRepository ethernetCoreRepository = new EthernetCoreRepository(env);
        return ethernetCoreRepository.createEthernetLink(UUID.randomUUID(), name, firstEI, secondEI);
    }

    public void deleteEthernetLink(Long ethernetLinkId) {
        new EthernetCoreClient(env).deleteEthernetLink(ethernetLinkId);
    }

    private Long getEthernetInterface(List<TerminationPointDetailDTO> list, int number) {
        List<TerminationPointDetailDTO> fiteredList = list.stream()
                .filter(e -> (e.getType().equals(ETHERNET_INTERFACE))).collect(Collectors.toList());
        if (fiteredList.size() < number) {
            Assert.fail(CANNOT_GET_ETHERNET_INTERFACE_EXCEPTION);
        }
        return fiteredList.get(number).getId();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void checkPopupMessageType() {
        SoftAssert softAssert = new SoftAssert();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        Optional<SystemMessageContainer.Message> firstSystemMessage = systemMessage.getFirstMessage();
        softAssert.assertTrue(firstSystemMessage.isPresent(), DELETE_AEL_ASSERTION_ERROR);
        firstSystemMessage.ifPresent(message -> {
            softAssert.assertEquals(message.getMessageType(), SystemMessageContainer.MessageType.SUCCESS, DELETE_AEL_ASSERTION_ERROR);
        });
        systemMessage.close();
        softAssert.assertAll();
    }

    private boolean isNodePresent(RoutingWizardPage wizard, String connectionPath) {
        return wizard.getWizard().getTreeComponent().getVisibleNodes().stream().map(TreeComponent.Node::getLabel).collect(Collectors.toList()).contains(connectionPath);
    }
}