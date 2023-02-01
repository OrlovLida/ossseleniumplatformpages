package com.oss.physical.devices;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.physicalinventory.api.dto.DeviceSlotDTO;
import com.comarch.oss.resourcehierarchy.api.dto.ResourceHierarchyDTO;
import com.comarch.oss.web.pages.HierarchyViewPage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.physical.CardCreateWizardPage;
import com.oss.pages.physical.CreatePluggableModuleWizardPage;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.PlanningRepository;
import com.oss.transport.infrastructure.planning.PlanningContext;
import com.oss.untils.Environment;
import com.oss.untils.ResourceHierarchyConverter;

import io.qameta.allure.Step;

public class CreateCardWithPluggableModuleTest extends BaseTestCase {
    private static final String ADDRESS_TYPE = "Address";
    private static final String LOCATION_TYPE = "Building";
    private static final String LOCATION_NAME = "testBuilding";

    private static final String DEVICE_MODEL_TYPE = "ODFModel";
    private static final String DEVICE_MODEL_NAME = "PS-19/72 3U";
    private static final String DEVICE_TYPE = "ODF";
    private static final String DEVICE_NAME = "SeleniumTestDevice";

    private static final String CARD_MODEL_TYPE = "CardModel";
    private static final String CARD_MODEL_NAME = "Switching Panel PS-19/72 3U E2000/SC/LC duplex";

    private static final String PLUGGABLE_MODULE_MODEL_TYPE = "PluggableModuleModel";
    private static final String PLUGGABLE_MODULE_MODEL_NAME = "SC/APC Front/Back";

    private static final String DEVICE_MODEL_NOT_FOUND_VALIDATION = "Could not find device model: %s";
    private static final String CARD_MODEL_NOT_FOUND_VALIDATION = "Could not find card model: %s";
    private static final String PLUGGABLE_MODULE_MODEL_NOT_FOUND_VALIDATION = "Could not find pluggable module model: %s";
    private static final String NO_OBJECTS_ON_HIERARCHY_VIEW_VALIDATION = "No visible objects on Hierarchy View";
    private static final String NO_PANEL_SLOT_VALIDATION = "No slot with model 'Panel' found in structure of device with id: %s";
    private static final String CARD_NOT_CREATED_VALIDATION = "Card was not created";
    private static final String CARD_NOT_FOUND_VALIDATION = "Card was not found in device structure";
    private static final String CARD_NOT_VISIBLE_ON_HIERARCHY_VIEW_VALIDATION = "Created card is not visible on Hierarchy View";
    private static final String NO_ATTRIBUTES_LABELS_ON_PROPERTY_PANEL_VALIDATION = "Could not get attributes labels from Property Panel";
    private static final String WRONG_CARD_NAME_VALIDATION = "Wrong pluggable module name found on Property Panel";
    private static final String PORT_NOT_FOUND_VALIDATION = "Port was not found in device structure";
    private static final String WRONG_PORT_LIST_SIZE_VALIDATION = "Wrong number of ports in port list in Create Pluggable Module wizard. Found: %s ports";
    private static final String WRONG_PORT_LABEL_VALUE_VALIDATION = "Port label in wizard did not contain expected value, which is: \\{portName}";
    private static final String PLUGGABLE_MODULE_NOT_CREATED_VALIDATION = "Pluggable Module was not created";
    private static final String PLUGGABLE_MODULE_NOT_FOUND_VALIDATION = "Pluggable Module was not found in device structure";
    private static final String PORT_HOLDER_NOT_FOUND_VALIDATION = "Port Holder was not found in device structure";
    private static final String PLUGGABLE_MODULE_NOT_VISIBLE_ON_HIERARCHY_VIEW_VALIDATION = "Created pluggable module is not visible on Hierarchy View";
    private static final String WRONG_NUMBER_OF_CONNECTORS_VALIDATION = "Wrong number of connectors in device structure";
    private static final String WRONG_PLUGGABLE_MODULE_NAME_VALIDATION = "Wrong pluggable module name found on Property Panel";

    private static final String CARD_CREATED_SUCCESS_MESSAGE = "Card has been created successfully.";
    private static final String PLUGGABLE_MODULE_CREATED_SUCCESS_MESSAGE = "Pluggable Module has been created successfully.";

    private static final String SLOT_PATH_FORMAT = "%s.chassis.%s.slots.%s";
    private static final String CARD_PATH_FORMAT = "%s.chassis.%s.slots.%s.card.%s";
    private static final String PORT_PATH_FORMAT = "%s.chassis.%s.slots.%s.card.%s.ports.%s";
    private static final String PORT_CONTAINER_PATH_FORMAT = "%s.chassis.%s.slots.%s.card.%s.ports";
    private static final String PLUGGABLE_MODULE_PATH_FORMAT = "%s.chassis.%s.slots.%s.card.%s.ports.%s.pluggable module slot.%s.pluggable module.%s";
    private static final String CONNECTOR_PATH_FORMAT = "%s.chassis.%s.slots.%s.card.%s.ports.%s.termination points.connector";

    private static final String CREATE_GROUP_ID = "CREATE";
    private static final String CREATE_CARD_ON_SLOT_ACTION_ID = "CreateCardOnSlotAction";
    private static final String CREATE_PLUGGABLE_MODULE_ON_PORT_ACTION_ID = "CreatePluggableModuleOnPortAction";
    private static final String PROPERTY_PANEL_WIDGET_ID = "PropertyPanelWidget";
    private static final String PROPERTY_PANEL_NAME_ATTRIBUTE_ID = "name";

    private static final String CREATE_CARD_WIZARD_NAME = "Create Card";
    private static final String CREATE_PLUGGABLE_MODULE_WIZARD_NAME = "Create Pluggable Module";

    private final PhysicalInventoryRepository physicalInventoryRepository;
    private final PlanningRepository planningRepository;
    private final LocationInventoryRepository locationInventoryRepository;

    private HierarchyViewPage hierarchyViewPage;
    private CardCreateWizardPage cardCreateWizardPage;
    private CreatePluggableModuleWizardPage createPluggableModuleWizardPage;

    private String locationId;
    private Long deviceId;
    private Long chassisId;
    private Long slotId;
    private Long cardId;
    private Long portId;
    private Long portHolderId;
    private Long pluggableModuleId;

    {
        planningRepository = new PlanningRepository(Environment.getInstance());
        physicalInventoryRepository = new PhysicalInventoryRepository(Environment.getInstance());
        locationInventoryRepository = new LocationInventoryRepository(Environment.getInstance());
    }

    @BeforeClass
    public void init() {
        Long deviceModelId = planningRepository.getObjectIdByTypeAndName(DEVICE_MODEL_TYPE, DEVICE_MODEL_NAME)
                .orElseThrow(() -> new NoSuchElementException(String.format(DEVICE_MODEL_NOT_FOUND_VALIDATION, DEVICE_MODEL_NAME)));
        validateCardModelExistence();
        validatePluggableModuleModelExistence();

        locationId = createPhysicalLocation();
        deviceId = createPhysicalDevice(deviceModelId);
    }

    @Test(priority = 1)
    public void deviceIsVisibleOnHierarchyView() {
        HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, DEVICE_TYPE, String.valueOf(deviceId));
        hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        setLivePerspective();

        Assert.assertEquals(hierarchyViewPage.getMainTreeSize(), 1, NO_OBJECTS_ON_HIERARCHY_VIEW_VALIDATION);
    }

    @Test(priority = 2, dependsOnMethods = "deviceIsVisibleOnHierarchyView")
    public void createCardWizardIsOpened() {
        DeviceSlotDTO panelSlot = getPanelSlot(Long.toString(deviceId));
        slotId = panelSlot.getId();
        chassisId = panelSlot.getPositioningElement();
        hierarchyViewPage.selectNodeByPath(getSlotPath());

        hierarchyViewPage.callAction(CREATE_GROUP_ID, CREATE_CARD_ON_SLOT_ACTION_ID);

        cardCreateWizardPage = new CardCreateWizardPage(driver);
        Assert.assertEquals(cardCreateWizardPage.getWizardName(), CREATE_CARD_WIZARD_NAME);
    }

    @Test(priority = 3, dependsOnMethods = "createCardWizardIsOpened")
    public void cardModelIsSetInCreateCardWizard() {
        cardCreateWizardPage.setModel(CARD_MODEL_NAME);
    }

    @Test(priority = 4, dependsOnMethods = "cardModelIsSetInCreateCardWizard")
    public void cardIsCreated() {
        cardCreateWizardPage.clickAccept();
        if (getMessageWithText(CARD_CREATED_SUCCESS_MESSAGE).isPresent()) {
            closeSystemMessage();
            hierarchyViewPage.unselectNodeByPath(getSlotPath());
        } else {
            Assert.fail(CARD_NOT_CREATED_VALIDATION);
        }
    }

    @Test(priority = 5, dependsOnMethods = "cardIsCreated")
    public void cardIsAddedToDeviceStructure() {
        Map<String, List<String>> hierarchy = getDeviceHierarchy();
        cardId = getCardIdFromHierarchy(hierarchy);

        Assert.assertTrue(hierarchyViewPage.isNodePresentByPath(getCardPath()), CARD_NOT_VISIBLE_ON_HIERARCHY_VIEW_VALIDATION);
    }

    @Test(priority = 6, dependsOnMethods = "cardIsAddedToDeviceStructure")
    public void cardNameIsVisibleOnPropertyPanel() {
        SoftAssert softAssert = new SoftAssert();

        hierarchyViewPage.selectNodeByPath(getCardPath());
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        softAssert.assertFalse(propertyPanel.getPropertyLabels().isEmpty(), NO_ATTRIBUTES_LABELS_ON_PROPERTY_PANEL_VALIDATION);
        softAssert.assertEquals(propertyPanel.getPropertyValue(PROPERTY_PANEL_NAME_ATTRIBUTE_ID), CARD_MODEL_NAME, WRONG_CARD_NAME_VALIDATION);
        hierarchyViewPage.unselectNodeByPath(getCardPath());

        softAssert.assertAll();
    }

    @Test(priority = 7, dependsOnMethods = "cardIsAddedToDeviceStructure")
    public void createPluggableModuleWizardIsOpened() {
        Map<String, List<String>> hierarchy = getDeviceHierarchy();
        portId = getPortIdFromHierarchy(hierarchy);
        hierarchyViewPage.setFilterForNodeByPath(getPortContainerPath(), "id", Long.toString(portId));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.selectNodeByPath(getPortPath());

        hierarchyViewPage.callAction(CREATE_GROUP_ID, CREATE_PLUGGABLE_MODULE_ON_PORT_ACTION_ID);

        createPluggableModuleWizardPage = new CreatePluggableModuleWizardPage(driver);
        Assert.assertEquals(createPluggableModuleWizardPage.getWizardName(), CREATE_PLUGGABLE_MODULE_WIZARD_NAME);
    }

    @Test(priority = 8, dependsOnMethods = "createPluggableModuleWizardIsOpened")
    public void portFieldIsFilledInCreatePluggableModuleWizard() {
        List<String> portsInComboBox = createPluggableModuleWizardPage.getPorts();
        Assert.assertEquals(portsInComboBox.size(), 1, String.format(WRONG_PORT_LIST_SIZE_VALIDATION, portsInComboBox.size()));
    }

    @Test(priority = 9, dependsOnMethods = "portFieldIsFilledInCreatePluggableModuleWizard")
    public void portLabelIsVisibleInCreatePluggableModuleWizard() {
        String portName = physicalInventoryRepository.getFirstPortName(Collections.singletonList(Long.toString(portId)), PlanningContext.perspectiveLive());
        List<String> portsInComboBox = createPluggableModuleWizardPage.getPorts();

        Assert.assertTrue(portsInComboBox.get(0).contains("\\" + portName), WRONG_PORT_LABEL_VALUE_VALIDATION);
    }

    @Test(priority = 10, dependsOnMethods = "portFieldIsFilledInCreatePluggableModuleWizard")
    public void pluggableModuleModelIsSetInCreatePluggableModuleWizard() {
        createPluggableModuleWizardPage.setModel(PLUGGABLE_MODULE_MODEL_NAME);
    }

    @Test(priority = 11, dependsOnMethods = "pluggableModuleModelIsSetInCreatePluggableModuleWizard")
    public void pluggableModuleIsCreated() {
        createPluggableModuleWizardPage.accept();
        if (getMessageWithText(PLUGGABLE_MODULE_CREATED_SUCCESS_MESSAGE).isPresent()) {
            closeSystemMessage();
            hierarchyViewPage.unselectNodeByPath(getPortPath());
        } else {
            Assert.fail(PLUGGABLE_MODULE_NOT_CREATED_VALIDATION);
        }
    }

    @Test(priority = 12, dependsOnMethods = "pluggableModuleIsCreated")
    public void pluggableModuleIsAddedToDeviceStructure() {
        Map<String, List<String>> hierarchy = getDeviceHierarchy();
        pluggableModuleId = getPluggableModuleIdFromHierarchy(hierarchy);
        portHolderId = getPortHolderIdFromHierarchy(hierarchy);

        Assert.assertTrue(hierarchyViewPage.isNodePresentByPath(getPluggableModulePath()), PLUGGABLE_MODULE_NOT_VISIBLE_ON_HIERARCHY_VIEW_VALIDATION);
    }

    @Test(priority = 13, dependsOnMethods = "pluggableModuleIsAddedToDeviceStructure")
    public void pluggableModuleHasTerminationPointsInStructure() {
        int numberOfConnectorsOnHierarchyView = hierarchyViewPage.getNodeChildrenByPath(getConnectorPath()).size();
        Assert.assertEquals(numberOfConnectorsOnHierarchyView, 2, WRONG_NUMBER_OF_CONNECTORS_VALIDATION);
    }

    @Test(priority = 14, dependsOnMethods = "pluggableModuleIsAddedToDeviceStructure")
    public void pluggableModuleNameIsVisibleOnPropertyPanel() {
        SoftAssert softAssert = new SoftAssert();

        hierarchyViewPage.selectNodeByPath(getPluggableModulePath());
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        softAssert.assertFalse(propertyPanel.getPropertyLabels().isEmpty(), NO_ATTRIBUTES_LABELS_ON_PROPERTY_PANEL_VALIDATION);
        softAssert.assertEquals(propertyPanel.getPropertyValue(PROPERTY_PANEL_NAME_ATTRIBUTE_ID), PLUGGABLE_MODULE_MODEL_NAME, WRONG_PLUGGABLE_MODULE_NAME_VALIDATION);
        hierarchyViewPage.unselectNodeByPath(getCardPath());

        softAssert.assertAll();
    }

    @AfterClass
    public void deleteCreatedObjects() {
        physicalInventoryRepository.deleteDeviceV3(Collections.singletonList(Long.toString(deviceId)), PlanningContext.perspectiveLive());
        locationInventoryRepository.deleteLocation(Long.parseLong(locationId), LOCATION_TYPE);
    }

    @Step("Set perspective to LIVE")
    private void setLivePerspective() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        if (!perspectiveChooser.getCurrentPerspective().equals("Live")) {
            perspectiveChooser.setLivePerspective();
        }
    }

    @Step("Close system message")
    private void closeSystemMessage() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SystemMessageContainer.create(driver, webDriverWait).close();
    }

    private void validateCardModelExistence() {
        planningRepository.getObjectIdByTypeAndName(CARD_MODEL_TYPE, CARD_MODEL_NAME)
                .orElseThrow(() -> new NoSuchElementException(String.format(CARD_MODEL_NOT_FOUND_VALIDATION, CARD_MODEL_NAME)));
    }

    private void validatePluggableModuleModelExistence() {
        planningRepository.getObjectIdByTypeAndName(PLUGGABLE_MODULE_MODEL_TYPE, PLUGGABLE_MODULE_MODEL_NAME)
                .orElseThrow(() -> new NoSuchElementException(String.format(PLUGGABLE_MODULE_MODEL_NOT_FOUND_VALIDATION, PLUGGABLE_MODULE_MODEL_NAME)));
    }

    private String createPhysicalLocation() {
        Long addressId = planningRepository.getFirstObjectWithType(ADDRESS_TYPE);
        String nameWithTimestamp = LOCATION_NAME + "_" + getTimestamp();
        return locationInventoryRepository.createLocation(nameWithTimestamp, LOCATION_TYPE, addressId);
    }

    private Long createPhysicalDevice(Long modelId) {
        String nameWithTimestamp = DEVICE_NAME + "_" + getTimestamp();
        return physicalInventoryRepository.createDevice(LOCATION_TYPE, Long.parseLong(locationId), modelId, nameWithTimestamp, DEVICE_MODEL_TYPE);
    }

    private String getTimestamp() {
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        return Long.toString(timestamp);
    }

    private DeviceSlotDTO getPanelSlot(String deviceId) {
        List<DeviceSlotDTO> slots = physicalInventoryRepository.getDeviceSlots(deviceId, PlanningContext.perspectiveLive());
        return slots.stream().filter(s -> s.getName().isPresent())
                .filter(s -> s.getName().get().equals("Panel"))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format(NO_PANEL_SLOT_VALIDATION, deviceId)));
    }

    private Optional<SystemMessageContainer.Message> getMessageWithText(String text) {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .stream()
                .filter(m -> m.getText().contains(text))
                .findFirst();
    }

    private Map<String, List<String>> getDeviceHierarchy() {
        ResourceHierarchyDTO resourceHierarchy = physicalInventoryRepository.getResourceHierarchy("PhysicalDevice", Long.toString(deviceId), "Root");
        return ResourceHierarchyConverter.getHierarchiesByParent(resourceHierarchy);
    }

    private Long getCardIdFromHierarchy(Map<String, List<String>> hierarchy) {
        Collection<String> cards = getChildrenOfObjectWithId(slotId, hierarchy);
        return getFirstObjectIdAsLong(cards, CARD_NOT_FOUND_VALIDATION);
    }

    private Long getPortIdFromHierarchy(Map<String, List<String>> hierarchy) {
        Collection<String> ports = getChildrenOfObjectWithId(cardId, hierarchy);
        return getFirstObjectIdAsLong(ports, PORT_NOT_FOUND_VALIDATION);
    }

    private Long getPluggableModuleIdFromHierarchy(Map<String, List<String>> hierarchy) {
        Collection<String> pluggableModules = hierarchy.values().stream()
                .flatMap(Collection::stream)
                .filter(element -> element.contains("PluggableModule"))
                .collect(Collectors.toList());
        return getFirstObjectIdAsLong(pluggableModules, PLUGGABLE_MODULE_NOT_FOUND_VALIDATION);
    }

    private Long getPortHolderIdFromHierarchy(Map<String, List<String>> hierarchy) {
        Collection<String> portHolders = hierarchy.entrySet().stream()
                .filter(element -> element.getValue().stream()
                        .anyMatch(pluggableModule -> pluggableModule.contains(Long.toString(pluggableModuleId))))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return getFirstObjectIdAsLong(portHolders, PORT_HOLDER_NOT_FOUND_VALIDATION);
    }

    private static Collection<String> getChildrenOfObjectWithId(Long objectId,
                                                                Map<String, List<String>> hierarchy) {
        return hierarchy.entrySet().stream()
                .filter(element -> element.getKey().contains(Long.toString(objectId)))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static Long getFirstObjectIdAsLong(Collection<String> objects,
                                               String validation) {
        return objects.stream()
                .map(object -> object.split("_")[1])
                .map(Long::parseLong)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(validation));
    }

    private String getSlotPath() {
        return String.format(SLOT_PATH_FORMAT, deviceId, chassisId, slotId);
    }

    private String getCardPath() {
        return String.format(CARD_PATH_FORMAT, deviceId, chassisId, slotId, cardId);
    }

    private String getPortContainerPath() {
        return String.format(PORT_CONTAINER_PATH_FORMAT, deviceId, chassisId, slotId, cardId);
    }

    private String getPortPath() {
        return String.format(PORT_PATH_FORMAT, deviceId, chassisId, slotId, cardId, portId);
    }

    private String getPluggableModulePath() {
        return String.format(PLUGGABLE_MODULE_PATH_FORMAT, deviceId, chassisId, slotId, cardId, portId, portHolderId, pluggableModuleId);
    }

    private String getConnectorPath() {
        return String.format(CONNECTOR_PATH_FORMAT, deviceId, chassisId, slotId, cardId, portId);
    }

}