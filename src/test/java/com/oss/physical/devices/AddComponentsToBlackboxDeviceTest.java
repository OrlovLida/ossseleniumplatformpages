package com.oss.physical.devices;

import com.comarch.oss.resourcehierarchy.api.dto.ResourceHierarchyDTO;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.physical.*;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.PlanningRepository;
import com.oss.untils.Environment;
import com.oss.untils.ResourceHierarchyConverter;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddComponentsToBlackboxDeviceTest extends BaseTestCase {

    private static final String MODEL_VALUE = "blackbox";
    private static final String NAME_VALUE = "Selenium Test Device";
    private static final String CREATE_CHASSIS_WIZARD_ACTION_ID = "CreateChassisAction";
    private static final String CREATE_SLOT_WIZARD_ACTION_ID = "CreateSlotAction";
    private static final String CREATE_PORT_WIZARD_ACTION_ID = "CreatePortInDeviceAction";
    private static final String CREATE_PLUGGABLE_MODULE_ACTION_ID = "CreatePluggableModuleOnDeviceApp";
    private static final String CREATE_CARD_ACTION_ID = "CreateCardOnDeviceAction";
    private static final String CREATE_ACTION_ID = "Create";
    private static final String CREATE_DEVICE_ACTION_ID = "Create Physical Device";
    private static final String INFRASTRUCTURE_MANAGEMENT_ACTION_ID = "Infrastructure Management";
    private static final String CREATE_INFRASTRUCTURE_ACTION_ID = "Create Infrastructure";
    private static final String PROPERTY_PANEL_WIDGET_ID = "PropertyPanelWidget";
    private static final String ID_PATTERN = "id=[\\d]+";
    private static final String TEST_LOCATION_TYPE = "Building";
    private static final String ADDRESS_TYPE = "Address";
    private static final String GENERIC_MODEL = "Generic";
    private static final String GENERIC_CHASSIS_MODEL = "Generic Chassis";
    private static final String GENERIC_SLOT_MODEL = "Generic Slot";
    private static final String GENERIC_PORT_WITH_PORT_HOLDER_MODEL = "Generic Pluggable";
    private static final String CHASSIS_NAME = "Chassis";
    private static final String FIRST_ELEMENT_NAME = "1";
    private static final String PHYSICAL_DEVICE = "PhysicalDevice";
    private static final String CHASSIS_COMPONENT = "Chassis";
    private static final String SLOT_COMPONENT = "Slot";
    private static final String PORT_COMPONENT = "Port";
    private static final String CARD_COMPONENT = "Card";
    private static final String PLUGGABLE_MODULE_COMPONENT = "PluggableModule";
    private static final String PLUGGABLE_MODULE_PORT_COMPONENT = "Port_";
    private static final String PLUGGABLE_MODULE_PORT_HOLDER_COMPONENT = "PortHolder";
    private static final Pattern PATTERN = Pattern.compile(ID_PATTERN);
    private static final String NAME_ATTRIBUTE_TO_CHECK = "name";
    private static final String CREATE_DEVICE_SUCCESS_MESSAGE = "Device has been created successfully";
    private static final String CREATE_CHASSIS_SUCCESS_MESSAGE = "Chassis with instance name: " + CHASSIS_NAME + " and model: " + GENERIC_CHASSIS_MODEL + " has been created successfully.";
    private static final String CREATE_SLOT_SUCCESS_MESSAGE = "Slot has been created successfully";
    private static final String CREATE_PORT_SUCCESS_MESSAGE = "Port has been created successfully";
    private static final String CREATE_CARD_SUCCESS_MESSAGE = "Card has been created successfully.";
    private static final String CREATE_PLUGGABLE_MODULE_SUCCESS_MESSAGE = "Pluggable Module has been created successfully.";
    private String testLocationId;
    private PlanningRepository planningRepository;
    private String deviceId;
    private String slotId;
    private String portId;
    private DeviceWizardPage deviceWizard;
    private ChassisWizardPage chassisWizard;
    private SlotWizardPage slotWizard;
    private PortWizardPage portWizard;
    private CardCreateWizardPage cardWizard;
    private CreatePluggableModuleWizardPage pluggableModuleWizard;
    private HierarchyViewPage hierarchyViewPage;
    private PhysicalInventoryRepository physicalInventoryRepository;
    private LocationInventoryRepository locationInventoryRepository;
    private SoftAssert softAssert;

    @BeforeClass
    private void init() {
        planningRepository = new PlanningRepository(Environment.getInstance());
        locationInventoryRepository = new LocationInventoryRepository(Environment.getInstance());
        physicalInventoryRepository = new PhysicalInventoryRepository(Environment.getInstance());
        deviceWizard = new DeviceWizardPage(driver);
        hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);

        chassisWizard = new ChassisWizardPage(driver);
        slotWizard = new SlotWizardPage(driver);
        portWizard = new PortWizardPage(driver);
        cardWizard = new CardCreateWizardPage(driver);
        pluggableModuleWizard = new CreatePluggableModuleWizardPage(driver);
        testLocationId = createPhysicalLocation();
    }

    @Test(description = "Create a blackbox device", priority = 1)
    public void createDevice() {
        openCreateDeviceWizardFromLeftSideMenu();

        fulfillFirstStep();
        deviceWizard.next();
        fulfillSecondStep();
        deviceWizard.accept();
        checkSystemMessage(CREATE_DEVICE_SUCCESS_MESSAGE);
        clickSystemMessageLink();
        closeSystemMessage();
        deviceId = getCreatedDeviceIdFromURL();
    }

    @Test(description = "Create chassis within a device", priority = 2, dependsOnMethods = "createDevice")
    public void createChassis() {
        selectFirstNode();
        hierarchyViewPage.callAction(CREATE_ACTION_ID, CREATE_CHASSIS_WIZARD_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        chassisWizard.setModel(GENERIC_CHASSIS_MODEL);
        chassisWizard.setName(CHASSIS_NAME);
        chassisWizard.clickAccept();
        checkSystemMessage(CREATE_CHASSIS_SUCCESS_MESSAGE);
        closeSystemMessage();
    }

    @Test(description = "Check device hierarchy for chassis", priority = 3, dependsOnMethods = "createChassis")
    public void checkHierarchyForChassis() {
        String chassisId = getFirstComponentUnderRootObject(deviceId, PHYSICAL_DEVICE, CHASSIS_COMPONENT);
        String chassisPath = deviceId + ".chassis." + chassisId;
        checkHierarchyForComponent(chassisPath, NAME_ATTRIBUTE_TO_CHECK);
        softAssert.assertAll();
    }

    @Test(description = "Create slot within a device", priority = 4, dependsOnMethods = "createDevice")
    public void createSlot() {
        selectFirstNode();
        hierarchyViewPage.callAction(CREATE_ACTION_ID, CREATE_SLOT_WIZARD_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        slotWizard.setModel(GENERIC_SLOT_MODEL);
        slotWizard.setName(FIRST_ELEMENT_NAME);
        slotWizard.clickCreate();
        checkSystemMessage(CREATE_SLOT_SUCCESS_MESSAGE);
        closeSystemMessage();
    }

    @Test(description = "Check device hierarchy for slot", priority = 5, dependsOnMethods = "createSlot")
    public void checkHierarchyForSlot() {
        slotId = getFirstComponentUnderRootObject(deviceId, PHYSICAL_DEVICE, SLOT_COMPONENT);
        String slotPath = deviceId + ".slots." + slotId;
        checkHierarchyForComponent(slotPath, NAME_ATTRIBUTE_TO_CHECK);
        softAssert.assertAll();
    }

    @Test(description = "Create port within a device", priority = 6, dependsOnMethods = "createDevice")
    public void createPort() {
        selectFirstNode();
        hierarchyViewPage.callAction(CREATE_ACTION_ID, CREATE_PORT_WIZARD_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        portWizard.setModel(GENERIC_PORT_WITH_PORT_HOLDER_MODEL);
        portWizard.clickNext();
        portWizard.clickAccept();
        checkSystemMessage(CREATE_PORT_SUCCESS_MESSAGE);
        closeSystemMessage();
    }

    @Test(description = "Check device hierarchy for port", priority = 7, dependsOnMethods = "createPort")
    public void checkHierarchyForPort() {
        portId = getFirstComponentUnderRootObject(deviceId, PHYSICAL_DEVICE, PORT_COMPONENT);
        String portPath = deviceId + ".ports." + portId;
        checkHierarchyForComponent(portPath, NAME_ATTRIBUTE_TO_CHECK);
        softAssert.assertAll();
    }

    @Test(description = "Create card within a slot", priority = 8, dependsOnMethods = "createSlot")
    public void createCard() {
        selectFirstNode();
        hierarchyViewPage.callAction(CREATE_ACTION_ID, CREATE_CARD_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        cardWizard.setModel(GENERIC_MODEL);
        cardWizard.setSlot(FIRST_ELEMENT_NAME);
        cardWizard.clickAccept();
        checkSystemMessage(CREATE_CARD_SUCCESS_MESSAGE);
        closeSystemMessage();
    }

    @Test(description = "Check device hierarchy for card", priority = 9, dependsOnMethods = "createCard")
    public void checkHierarchyForCard() {
        String cardId = getFirstComponentUnderRootObject(deviceId, slotId, CARD_COMPONENT);
        String cardPath = deviceId + ".slots." + slotId + ".card." + cardId;
        checkHierarchyForComponent(cardPath, NAME_ATTRIBUTE_TO_CHECK);
        softAssert.assertAll();
    }

    @Test(description = "Create pluggable module within a port", priority = 10, dependsOnMethods = "createPort")
    public void createPluggableModule() {
        selectFirstNode();
        hierarchyViewPage.callAction(CREATE_ACTION_ID, CREATE_PLUGGABLE_MODULE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        pluggableModuleWizard.setPort(FIRST_ELEMENT_NAME);
        pluggableModuleWizard.setModel(GENERIC_MODEL);
        pluggableModuleWizard.accept();
        checkSystemMessage(CREATE_PLUGGABLE_MODULE_SUCCESS_MESSAGE);
        closeSystemMessage();
    }

    @Test(description = "Check device hierarchy for pluggable module", priority = 11, dependsOnMethods = "createPluggableModule")
    public void checkHierarchyForPluggableModule() {
        String portHolderId = getFirstComponentUnderRootObject(deviceId, PLUGGABLE_MODULE_PORT_COMPONENT, PLUGGABLE_MODULE_PORT_HOLDER_COMPONENT);
        String pluggableModuleId = getFirstComponentUnderRootObject(deviceId, PLUGGABLE_MODULE_PORT_HOLDER_COMPONENT, PLUGGABLE_MODULE_COMPONENT);
        String pluggableModulePath = deviceId + ".ports." + portId + ".pluggable module slot." + portHolderId + ".pluggable module." + pluggableModuleId;
        checkHierarchyForComponent(pluggableModulePath, NAME_ATTRIBUTE_TO_CHECK);
        softAssert.assertAll();
    }

    @AfterClass
    private void deleteCreatedObjects() {
        physicalInventoryRepository.deleteDevice(deviceId);
        Long locationToDelete = Long.parseLong(testLocationId);
        locationInventoryRepository.deleteLocation(locationToDelete, TEST_LOCATION_TYPE);
    }

    @AfterMethod
    private void unselectNode() {
        if (!hierarchyViewPage.getMainTree().getSelectedObjectCount().equals("0 selected")) {
            hierarchyViewPage.getMainTree().unselectAllNodes();
        }
    }

    private String createPhysicalLocation() {
        Long addressId = planningRepository.getFirstObjectWithType(ADDRESS_TYPE);
        return locationInventoryRepository.createLocation("testBuilding_" + getTimestamp(), TEST_LOCATION_TYPE, addressId);
    }

    @Step("Open Create Device wizard from left side menu")
    private void openCreateDeviceWizardFromLeftSideMenu() {
        homePage.chooseFromLeftSideMenu(CREATE_DEVICE_ACTION_ID, INFRASTRUCTURE_MANAGEMENT_ACTION_ID, CREATE_INFRASTRUCTURE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Step("Fulfill device attributes in first step")
    private void fulfillFirstStep() {
        deviceWizard.setFirstModel(MODEL_VALUE);
        deviceWizard.setName(NAME_VALUE + "_" + new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(Calendar.getInstance().getTime()));
    }

    @Step("Fulfill location attributes in second step")
    private void fulfillSecondStep() {
        deviceWizard.setFirstPreciseLocation(testLocationId);
    }

    @Step("Check system message")
    private void checkSystemMessage(String message) {
        Assert.assertTrue(getMessageWithText(message).isPresent(), "System message '" + message + "' is not present");
    }

    @Step("Check visibility of Name attribute")
    private void checkAttributeValue(String attributeName) {
        softAssert = new SoftAssert();
        try {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            PropertyPanel propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);
            String value = propertyPanel.getPropertyValue(attributeName);
            softAssert.assertFalse(value.equals("-"), "Attribute value does not match pattern");
        } catch (IllegalArgumentException e) {
            softAssert.fail("Failed due to an error:" + e);
        } catch (org.openqa.selenium.TimeoutException f) {
            softAssert.fail("Failed to load Property Panel:" + f);
        }
    }

    @Step("Select First Object")
    private void selectFirstNode() {
        if (!hierarchyViewPage.getMainTree().getNode(0).isToggled()) {
            hierarchyViewPage.selectFirstObject();
        }
    }

    @Step("Click system message link")
    private void clickSystemMessageLink() {
        SystemMessageContainer.create(driver, webDriverWait).clickMessageLink();
    }

    @Step("Close system message")
    private void closeSystemMessage() {
        SystemMessageContainer.create(driver, webDriverWait).close();
    }

    @Step("Check hierarchy for component")
    public void checkHierarchyForComponent(String path,
                                           String attributeName) {
        Assert.assertTrue(hierarchyViewPage.isNodePresentByPath(path), "Node with path " + path + " is not visible on Hierarchy View");
        hierarchyViewPage.selectNodeByPath(path);
        checkAttributeValue(attributeName);
    }

    private Optional<SystemMessageContainer.Message> getMessageWithText(String message) {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .stream()
                .filter(m -> m.getText().contains(message))
                .findFirst();
    }

    private static Set<Long> getIdFromString(String string) {
        Set<Long> ids = new HashSet<>();
        Matcher matcher = PATTERN.matcher(string);
        while (matcher.find()) {
            String stringId = matcher.group();
            Long id = Long.parseLong(stringId.split("=")[1]);
            ids.add(id);
        }
        return ids;
    }

    private String getCreatedDeviceIdFromURL() {
        return getIdFromString(driver.getCurrentUrl()).stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cannot get list of components from device"))
                .toString();
    }

    private String getFirstComponentUnderRootObject(String rootId,
                                                    String parent,
                                                    String component) {
        ResourceHierarchyDTO resourceHierarchy = getResourceHierarchyAPI(rootId);
        Map<String, List<String>> hierarchy = ResourceHierarchyConverter.getHierarchiesByParent(resourceHierarchy);

        List<String> componentsUnderDevice = hierarchy.entrySet().stream()
                .filter(element -> element.getKey().contains(parent))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cannot get list of components from device"));

        return componentsUnderDevice.stream()
                .filter(element -> element.contains(component))
                .findFirst()
                .map(child -> child.split("_")[1])
                .orElseThrow(() -> new NoSuchElementException("Cannot get component id from device hierarchy"));
    }

    private ResourceHierarchyDTO getResourceHierarchyAPI(String deviceId) {
        return physicalInventoryRepository.getResourceHierarchy("PhysicalDevice", deviceId, "Root");
    }

    private String getTimestamp() {
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        return Long.toString(timestamp);
    }
}
