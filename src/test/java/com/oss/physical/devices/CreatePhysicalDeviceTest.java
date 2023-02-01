package com.oss.physical.devices;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.resourcehierarchy.api.dto.ResourceHierarchyDTO;
import com.comarch.oss.web.pages.HierarchyViewPage;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.attributechooser.AttributesChooser;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.PlanningRepository;
import com.oss.transport.infrastructure.planning.PlanningContext;
import com.oss.untils.Environment;
import com.oss.untils.ResourceHierarchyConverter;

import io.qameta.allure.Step;

public class CreatePhysicalDeviceTest extends BaseTestCase {

    private static final String LIVE_PERSPECTIVE_TEXT = "Live";
    private static final String DEVICE_WIZARD_ATTRIBUTES_STEP_TITLE = "1. Attributes";
    private static final String DEVICE_WIZARD_LOCATIONS_STEP_TITLE = "2. Locations";
    private static final String DEVICE_WIZARD_ARRAYS_STEP_TITLE = "3. Antenna Array";
    private static final String ADDRESS_TYPE = "Address";
    private static final String TEST_LOCATION_TYPE = "Building";

    private static final String PRODUCT_PROPERTY_PANEL_CONFIGURATION = "85d9f1c4-3bfc-4061-b5bc-751285dd4539";
    private static final String PROPERTY_PANEL_WIDGET_ID = "PropertyPanelWidget";
    private static final Pattern ID_PATTERN = Pattern.compile("id=[\\d]+");

    private static final String NO_SUCCESSFUL_MESSAGE_VALIDATION = "No message about successful device creation";
    private static final String WRONG_STEP_TITLE = "Wrong title of %s wizard step for equipment type: %s";
    private static final String WRONG_ATTRIBUTE_VALUE_VALIDATION = "Wrong value of %s attribute for type: %s";
    private static final String ATTRIBUTE_DOES_NOT_EXIST_ON_PROPERTY_PANEL_VALIDATION = "Attribute %s does not exist on Property Panel for type: %s";
    private static final String UNSUPPORTED_EQUIPMENT_TYPE_VALIDATION = "Unsupported equipment type: %s";
    private static final String NO_DEVICE_ON_HIERARCHY_VIEW_VALIDATION = "Device with equipment type: %s is not visible on Hierarchy View";
    private static final String NO_DEVICE_ON_INVENTORY_VIEW_VALIDATION = "Device with equipment type: %s is not visible on Inventory View";
    private static final String NUMBER_OF_CHASSIS_DOES_NOT_MATCH_MODEL_VALIDATION = "Number of Chassis does not match the model for device with equipment type: %s";
    private static final String NUMBER_OF_SLOTS_DOES_NOT_MATCH_MODEL_VALIDATION = "Number of Slots does not match the model for device with equipment type: %s";
    private static final String NUMBER_OF_PORTS_DOES_NOT_MATCH_MODEL_VALIDATION = "Number of Ports does not match the model for device with equipment type: %s";
    private static final String NUMBER_OF_CONNECTORS_DOES_NOT_MATCH_MODEL_VALIDATION = "Number of Connectors does not match the model for device with equipment type: %s";
    private static final String NUMBER_OF_LOGICAL_FUNCTIONS_DOES_NOT_MATCH_MODEL_VALIDATION = "Number of Logical Functions does not match the model for device with equipment type: %s";
    private static final String CANNOT_ENABLE_ATTRIBUTE_VALIDATION = "Cannot enable attribute with label: %s in Attributes Chooser";

    private static final String DEVICE_HAS_BEEN_CREATED_MESSAGE = "Device has been created successfully";

    private DeviceWizardPage deviceWizard;
    private SoftAssert softAssert;
    private HierarchyViewPage hierarchyViewPage;
    private NewInventoryViewPage newInventoryViewPage;
    private Map<String, String> attributesLabelToValueMap;
    private String testLocationId;
    private String timestamp;

    private PhysicalInventoryRepository physicalInventoryRepository;
    private PlanningRepository planningRepository;
    private LocationInventoryRepository locationInventoryRepository;

    private final String equipmentType;
    private final String model;

    @Parameters({"equipmentType", "model"})
    public CreatePhysicalDeviceTest(String equipmentType, String model) {
        this.equipmentType = equipmentType;
        this.model = model;
    }

    @BeforeClass
    private void init() {
        deviceWizard = new DeviceWizardPage(driver);
        hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        attributesLabelToValueMap = new HashMap<>();
        physicalInventoryRepository = new PhysicalInventoryRepository(Environment.getInstance());
        planningRepository = new PlanningRepository(Environment.getInstance());
        locationInventoryRepository = new LocationInventoryRepository(Environment.getInstance());
        timestamp = getTimestamp();
        testLocationId = createPhysicalLocation();
        setLivePerspective();
    }

    @Test(priority = 1)
    public void openDeviceWizardFromLeftSideMenu() {
        homePage.chooseFromLeftSideMenu("Create Physical Device", "Infrastructure Management", "Create Infrastructure");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(deviceWizard.getCurrentStateTitle(), DEVICE_WIZARD_ATTRIBUTES_STEP_TITLE, String.format(WRONG_STEP_TITLE, "first", equipmentType));
    }

    @Test(priority = 2, dependsOnMethods = "openDeviceWizardFromLeftSideMenu")
    public void setEquipmentType() {
        deviceWizard.setEquipmentType(equipmentType);
    }

    @Test(priority = 3, dependsOnMethods = "setEquipmentType")
    public void setModel() {
        deviceWizard.setModel(model);
    }

    @Test(priority = 4, dependsOnMethods = "setModel")
    public void setOptionalAttributes() {
        deviceWizard.setName(getAttributeValueWithTimestamp(DeviceAttributes.NAME));
        deviceWizard.setSerialNumber(DeviceAttributes.SERIALNUMBER.value);
        deviceWizard.setHostname(DeviceAttributes.HOSTNAME.value);
        deviceWizard.setFirmwareVersion(DeviceAttributes.FIRMWARE_VERSION.value);
        deviceWizard.setHardwareVersion(DeviceAttributes.HARDWARE_VERSION.value);
        deviceWizard.setDescription(DeviceAttributes.DESCRIPTION.value);
        deviceWizard.setRemarks(DeviceAttributes.REMARKS.value);

        switch (equipmentType) {
            case "DWDM Device":
                deviceWizard.setHeatEmission(DWDMAdditionalAttributes.HEAT_EMISSION.value);
                deviceWizard.setPowerConsumption(DWDMAdditionalAttributes.POWER_CONSUMPTION.value);
                deviceWizard.setMac(DWDMAdditionalAttributes.MAC.value);
                break;
            case "ODF":
                deviceWizard.setHeatEmission(ODFAdditionalAttributes.HEAT_EMISSION.value);
                deviceWizard.setPowerConsumption(ODFAdditionalAttributes.POWER_CONSUMPTION.value);
                break;
            case "RAN Antenna":
                deviceWizard.setMechanicalTilt(RANAntennaAdditionalAttributes.MECHANICAL_TILT.value);
                deviceWizard.setAzimuth(RANAntennaAdditionalAttributes.AZIMUTH.value);
                deviceWizard.setHeight(RANAntennaAdditionalAttributes.HEIGHT.value);
                deviceWizard.setSideTilt(RANAntennaAdditionalAttributes.SIDE_TILT.value);
                deviceWizard.setMountingType(RANAntennaAdditionalAttributes.MOUNTING_TYPE.value);
                break;
            default:
                throw new IllegalArgumentException(String.format(UNSUPPORTED_EQUIPMENT_TYPE_VALIDATION, equipmentType));
        }
    }

    @Test(priority = 5, dependsOnMethods = "setModel")
    public void moveToTheLocationsStep() {
        deviceWizard.next();
        Assert.assertEquals(deviceWizard.getCurrentStateTitle(), DEVICE_WIZARD_LOCATIONS_STEP_TITLE, String.format(WRONG_STEP_TITLE, "second", equipmentType));
    }

    @Test(priority = 6, dependsOnMethods = "moveToTheLocationsStep")
    public void setPreciseLocation() {
        deviceWizard.setFirstPreciseLocation(testLocationId);
    }

    @Test(priority = 7, dependsOnMethods = "setPreciseLocation")
    public void moveToTheAntennaArrayStep() {
        if (equipmentType.equals("RAN Antenna")) {
            deviceWizard.next();
            Assert.assertEquals(deviceWizard.getCurrentStateTitle(), DEVICE_WIZARD_ARRAYS_STEP_TITLE, String.format(WRONG_STEP_TITLE, "third", equipmentType));
        } else {
            throw new SkipException("Antenna Array step is unavailable for equipment type other than RAN Antenna");
        }
    }

    @Test(priority = 8, dependsOnMethods = "setPreciseLocation")
    public void submitDeviceWizard() {
        deviceWizard.accept();
    }

    @Test(priority = 9, dependsOnMethods = {"submitDeviceWizard"})
    public void openHierarchyViewFromActiveLink() {
        checkSystemMessage();
        clickSystemMessageLink();
        closeSystemMessage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 10, dependsOnMethods = "openHierarchyViewFromActiveLink")
    public void checkIfDeviceIsVisibleOnHierarchyView() {
        Assert.assertEquals(getMainTreeSize(), 1, String.format(NO_DEVICE_ON_HIERARCHY_VIEW_VALIDATION, equipmentType));
    }

    @Test(priority = 11, dependsOnMethods = "checkIfDeviceIsVisibleOnHierarchyView")
    public void addAdditionalAttributesToPropertyPanelOnHierarchyView() {
        hierarchyViewPage.selectFirstObject();
        hierarchyViewPage.setPropertyPanelConfiguration(PRODUCT_PROPERTY_PANEL_CONFIGURATION);
        addAdditionalAttributesToPropertyPanel();
    }

    @Test(priority = 12, dependsOnMethods = "checkIfDeviceIsVisibleOnHierarchyView")
    public void checkAttributesOfCreatedDeviceOnHierarchyView() {
        softAssert = new SoftAssert();

        loadPropertyPanelAttributesAndValues();
        checkDeviceAttributesAndValues();
        checkDeviceAdditionalAttributesAndValues();

        softAssert.assertAll();
    }

    @Test(priority = 13, dependsOnMethods = {"checkIfDeviceIsVisibleOnHierarchyView"})
    public void checkStructureOfCreatedDeviceOnHierarchyView() {
        String deviceId = getIdsFromURL().get(0);
        ResourceHierarchyDTO resourceHierarchy = physicalInventoryRepository.getResourceHierarchy("PhysicalDevice", deviceId, "Root");
        Map<String, List<String>> deviceHierarchy = ResourceHierarchyConverter.getHierarchiesByParent(resourceHierarchy);

        Optional<String> chassisId = getIdOfFirstComponentInHierarchyWithType("Chassis", deviceHierarchy);
        Optional<String> portId = getIdOfFirstComponentInHierarchyWithType("Port", deviceHierarchy);

        switch (equipmentType) {
            case "DWDM Device":
                if (chassisId.isPresent()) {
                    Assert.assertEquals(hierarchyViewPage.getNodeChildrenByPath(getChassisContainerPath(deviceId)).size(), 1, String.format(NUMBER_OF_CHASSIS_DOES_NOT_MATCH_MODEL_VALIDATION, equipmentType));
                    Assert.assertEquals(hierarchyViewPage.getNodeChildrenByPath(getSlotContainerPath(deviceId, chassisId.get())).size(), 13, String.format(NUMBER_OF_SLOTS_DOES_NOT_MATCH_MODEL_VALIDATION, equipmentType));
                }
                break;
            case "ODF":
                if (chassisId.isPresent()) {
                    Assert.assertEquals(hierarchyViewPage.getNodeChildrenByPath(getChassisContainerPath(deviceId)).size(), 1, String.format(NUMBER_OF_CHASSIS_DOES_NOT_MATCH_MODEL_VALIDATION, equipmentType));
                    Assert.assertEquals(hierarchyViewPage.getNodeChildrenByPath(getSlotContainerPath(deviceId, chassisId.get())).size(), 7, String.format(NUMBER_OF_SLOTS_DOES_NOT_MATCH_MODEL_VALIDATION, equipmentType));
                }
                break;
            case "RAN Antenna":
                if (portId.isPresent()) {
                    Assert.assertEquals(hierarchyViewPage.getNodeChildrenByPath(getPortContainerPath(deviceId)).size(), 1, String.format(NUMBER_OF_PORTS_DOES_NOT_MATCH_MODEL_VALIDATION, equipmentType));
                    Assert.assertEquals(hierarchyViewPage.getNodeChildrenByPath(getConnectorContainerPath(deviceId, portId.get())).size(), 2, String.format(NUMBER_OF_CONNECTORS_DOES_NOT_MATCH_MODEL_VALIDATION, equipmentType));
                    Assert.assertEquals(hierarchyViewPage.getNodeChildrenByPath(getHostedLogicalFunctionContainerPath(deviceId)).size(), 1, String.format(NUMBER_OF_LOGICAL_FUNCTIONS_DOES_NOT_MATCH_MODEL_VALIDATION, equipmentType));
                }
                break;
            default:
                throw new IllegalArgumentException(String.format(UNSUPPORTED_EQUIPMENT_TYPE_VALIDATION, equipmentType));
        }
    }

    @Test(priority = 14, dependsOnMethods = "checkIfDeviceIsVisibleOnHierarchyView")
    public void moveToInventoryView() {
        hierarchyViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "InventoryView");
        Assert.assertFalse(newInventoryViewPage.getMainTable().hasNoData(), String.format(NO_DEVICE_ON_INVENTORY_VIEW_VALIDATION, equipmentType));
    }

    @Test(priority = 15, dependsOnMethods = "moveToInventoryView")
    public void addAdditionalAttributesToPropertyPanelOnInventoryView() {
        newInventoryViewPage.applyConfigurationForProperties(0, PROPERTY_PANEL_WIDGET_ID, PRODUCT_PROPERTY_PANEL_CONFIGURATION);
        addAdditionalAttributesToPropertyPanel();
    }

    @Test(priority = 16, dependsOnMethods = "moveToInventoryView")
    public void checkAttributesOfCreatedDeviceOnInventoryView() {
        softAssert = new SoftAssert();

        loadPropertyPanelAttributesAndValues();
        checkDeviceAttributesAndValues();
        checkDeviceAdditionalAttributesAndValues();

        softAssert.assertAll();
    }

    @AfterClass
    public void deleteCreatedObjects() {
        List<String> deviceToDelete = getIdsFromURL();
        physicalInventoryRepository.deleteDeviceV3(deviceToDelete, PlanningContext.perspectiveLive());
        Long locationToDelete = Long.parseLong(testLocationId);
        locationInventoryRepository.deleteLocation(locationToDelete, TEST_LOCATION_TYPE);
    }

    @Step("Check system message")
    private void checkSystemMessage() {
        Assert.assertTrue(getMessageWithText().isPresent(), NO_SUCCESSFUL_MESSAGE_VALIDATION);
    }

    @Step("Click system message link")
    private void clickSystemMessageLink() {
        SystemMessageContainer.create(driver, webDriverWait).clickMessageLink();
    }

    @Step("Close system message")
    private void closeSystemMessage() {
        SystemMessageContainer.create(driver, webDriverWait).close();
    }

    @Step("Load device attributes and values")
    private void loadPropertyPanelAttributesAndValues() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);

        List<String> propertyPanelVisibleAttributes = propertyPanel.getVisibleAttributes();
        List<String> propertyPanelVisibleAttributesLabels = propertyPanel.getPropertyLabels();

        for (int i = 0; i < propertyPanelVisibleAttributes.size(); i++) {
            attributesLabelToValueMap.put(propertyPanelVisibleAttributesLabels.get(i), propertyPanel.getPropertyValue(propertyPanelVisibleAttributes.get(i)));
        }
    }

    @Step("Check correctness of saved attributes for created device")
    private void checkDeviceAttributesAndValues() {
        for (DeviceAttributes attribute : DeviceAttributes.values()) {
            String expectedAttributeValue = getExpectedAttributeValue(attribute);
            validateAttributeValue(attribute.name, expectedAttributeValue);
        }
    }

    @Step("Check correctness of saved additional attributes for created device")
    private void checkDeviceAdditionalAttributesAndValues() {
        switch (equipmentType) {
            case "DWDM Device":
                for (DWDMAdditionalAttributes additionalAttribute : DWDMAdditionalAttributes.values()) {
                    validateAttributeValue(additionalAttribute.name, additionalAttribute.value);
                }
                break;
            case "ODF":
                for (ODFAdditionalAttributes additionalAttribute : ODFAdditionalAttributes.values()) {
                    validateAttributeValue(additionalAttribute.name, additionalAttribute.value);
                }
                break;
            case "RAN Antenna":
                for (RANAntennaAdditionalAttributes additionalAttribute : RANAntennaAdditionalAttributes.values()) {
                    validateAttributeValue(additionalAttribute.name, additionalAttribute.value);
                }
                break;
            default:
                throw new IllegalArgumentException(String.format(UNSUPPORTED_EQUIPMENT_TYPE_VALIDATION, equipmentType));
        }
    }

    private String getExpectedAttributeValue(DeviceAttributes attributes) {
        if (attributes.name.equals("Name")) {
            return getAttributeValueWithTimestamp(attributes);
        } else if (attributes.name.equals("Location")) {
            return getAttributeValueWithTimestamp(attributes);
        } else {
            return attributes.value;
        }
    }

    private void validateAttributeValue(String name,
                                        String value) {
        String valueOnPropertyPanel = attributesLabelToValueMap.get(name);
        if (valueOnPropertyPanel != null) {
            softAssert.assertEquals(valueOnPropertyPanel, value, String.format(WRONG_ATTRIBUTE_VALUE_VALIDATION, name, equipmentType));
        } else {
            softAssert.fail(String.format(ATTRIBUTE_DOES_NOT_EXIST_ON_PROPERTY_PANEL_VALIDATION, name, equipmentType));
        }
    }

    private String getAttributeValueWithTimestamp(DeviceAttributes attributes) {
        return attributes.value + "_" + timestamp;
    }

    private Optional<SystemMessageContainer.Message> getMessageWithText() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .stream()
                .filter(m -> m.getText().contains(DEVICE_HAS_BEEN_CREATED_MESSAGE))
                .findFirst();
    }

    private String getTimestamp() {
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        return Long.toString(timestamp);
    }

    @Step("Set perspective to LIVE")
    private void setLivePerspective() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        if (!perspectiveChooser.getCurrentPerspective().equals(LIVE_PERSPECTIVE_TEXT)) {
            perspectiveChooser.setLivePerspective();
        }
    }

    private String createPhysicalLocation() {
        Long addressId = planningRepository.getFirstObjectWithType(ADDRESS_TYPE);
        String locationName = getAttributeValueWithTimestamp(DeviceAttributes.LOCATION);
        return locationInventoryRepository.createLocation(locationName, TEST_LOCATION_TYPE, addressId);
    }

    private int getMainTreeSize() {
        return hierarchyViewPage.getMainTree().getVisibleNodes().size();
    }

    private void addAdditionalAttributesToPropertyPanel() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);
        SoftAssert softAssert = new SoftAssert();

        switch (equipmentType) {
            case "DWDM Device":
                for (DWDMAdditionalAttributes additionalAttribute : DWDMAdditionalAttributes.values()) {
                    tryToEnableAttributeByLabel(additionalAttribute.name, propertyPanel, softAssert);
                    DelayUtils.waitForPageToLoad(driver, webDriverWait);
                }
                break;
            case "ODF":
                for (ODFAdditionalAttributes additionalAttribute : ODFAdditionalAttributes.values()) {
                    tryToEnableAttributeByLabel(additionalAttribute.name, propertyPanel, softAssert);
                    DelayUtils.waitForPageToLoad(driver, webDriverWait);
                }
                break;
            case "RAN Antenna":
                for (RANAntennaAdditionalAttributes additionalAttribute : RANAntennaAdditionalAttributes.values()) {
                    tryToEnableAttributeByLabel(additionalAttribute.name, propertyPanel, softAssert);
                    DelayUtils.waitForPageToLoad(driver, webDriverWait);
                }
                break;
            default:
                throw new IllegalArgumentException(String.format(UNSUPPORTED_EQUIPMENT_TYPE_VALIDATION, equipmentType));
        }
        softAssert.assertAll();
    }

    private void tryToEnableAttributeByLabel(String label,
                                             PropertyPanel propertyPanel,
                                             SoftAssert softAssert) {
        try {
            propertyPanel.enableAttributeByLabel(label);
        } catch (NoSuchElementException exception) {
            AttributesChooser.create(driver, webDriverWait).clickCancel();
            softAssert.fail(String.format(CANNOT_ENABLE_ATTRIBUTE_VALIDATION, label));
        }
    }

    private static Set<Long> getIdFromString(String string) {
        Set<Long> ids = new HashSet<>();
        Matcher matcher = ID_PATTERN.matcher(string);
        while (matcher.find()) {
            String stringId = matcher.group();
            Long id = Long.parseLong(stringId.split("=")[1]);
            ids.add(id);
        }
        return ids;
    }

    private List<String> getIdsFromURL() {
        return getIdFromString(driver.getCurrentUrl()).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private String getChassisContainerPath(String deviceId) {
        return deviceId + ".chassis";
    }

    private String getSlotContainerPath(String deviceId,
                                        String chassisId) {
        return getChassisPath(deviceId, chassisId) + ".slots";
    }

    private String getChassisPath(String deviceId,
                                  String chassisId) {
        return deviceId + ".chassis." + chassisId;
    }

    private String getPortContainerPath(String deviceId) {
        return deviceId + ".ports";
    }

    private String getConnectorContainerPath(String deviceId,
                                             String portId) {
        return getPortContainerPath(deviceId) + "." + portId + ".termination points.connector";
    }

    private String getHostedLogicalFunctionContainerPath(String deviceId) {
        return deviceId + ".hosted logical functions";
    }

    private Optional<String> getIdOfFirstComponentInHierarchyWithType(String type,
                                                                      Map<String, List<String>> hierarchy) {
        return hierarchy.values().stream()
                .flatMap(Collection::stream)
                .filter(comp -> comp.startsWith(type))
                .findFirst()
                .map(port -> port.split("_")[1]);
    }

    private enum DeviceAttributes {
        NAME("Name", "SeleniumTestDevice"),
        SERIALNUMBER("Serial Number", "serial number"),
        HOSTNAME("Hostname", "hostname"),
        FIRMWARE_VERSION("Firmware Licence", "firmware version"),
        HARDWARE_VERSION("Hardware Licence", "hardware version"),
        DESCRIPTION("Description", "description"),
        REMARKS("Remarks", "remarks"),
        LOCATION("Location", "testBuilding");

        private final String name;
        private final String value;

        DeviceAttributes(String name,
                         String value) {
            this.name = name;
            this.value = value;
        }
    }

    private enum DWDMAdditionalAttributes {
        HEAT_EMISSION("Heat Emission [kW]", "12"),
        POWER_CONSUMPTION("Power Consumption [kW]", "24"),
        MAC("MAC", "00:00:00:00:00:00");

        private final String name;
        private final String value;

        DWDMAdditionalAttributes(String name,
                                 String value) {
            this.name = name;
            this.value = value;
        }
    }

    private enum ODFAdditionalAttributes {
        HEAT_EMISSION("Heat Emission [kW]", "36"),
        POWER_CONSUMPTION("Power Consumption [kW]", "48");

        private final String name;
        private final String value;

        ODFAdditionalAttributes(String name,
                                String value) {
            this.name = name;
            this.value = value;
        }
    }

    private enum RANAntennaAdditionalAttributes {
        MECHANICAL_TILT("Mechanical Tilt [deg]", "1"),
        AZIMUTH("Azimuth [deg]", "2"),
        HEIGHT("Height [m]", "3"),
        SIDE_TILT("Side Tilt [deg]", "4"),
        MOUNTING_TYPE("Mounting Type", "Wall");

        private final String name;
        private final String value;

        RANAntennaAdditionalAttributes(String name,
                                       String value) {
            this.name = name;
            this.value = value;
        }
    }
}