package com.oss.physical.devices;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.resourcehierarchy.api.dto.ResourceHierarchyDTO;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.NumberField;
import com.oss.framework.components.inputs.TextField;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.PlanningRepository;
import com.oss.untils.Environment;
import com.oss.untils.ResourceHierarchyConverter;

import io.qameta.allure.Step;

public class CreateMultipleInstancesOfDeviceTest extends BaseTestCase {

    private static final String DEVICE_WIZARD_DEFAULT_QUANTITY_VALUE = "1";
    private static final String DEVICE_WIZARD_ALLOWED_QUANTITY_VALUE = "5";
    private static final int NUMBER_OF_CREATED_DEVICES = 5;
    private static final int NUMBER_OF_SLOTS_IN_DEVICE_STRUCTURE = 13;
    private static final int NUMBER_OF_CHASSIS_IN_DEVICE_STRUCTURE = 1;
    private static final String DEVICE_WIZARD_NOT_ALLOWED_QUANTITY_VALUE = "21";
    private static final String EQUIPMENT_TYPE_VALUE = "DWDM Device";
    private static final String MODEL_VALUE = "Alcatel 1626LM Compact";
    private static final String NAME_ATTRIBUTE = "Name";
    private static final String SERIAL_NUMBER_ATTRIBUTE = "Serial Number";
    private static final String HOSTNAME_ATTRIBUTE = "Hostname";
    private static final String TEST_LOCATION_TYPE = "Building";
    private static final String NAME_FIELD_IN_WIZARD_ID = "name";
    private static final String HOSTNAME_FIELD_IN_WIZARD_ID = "hostname";
    private static final String SERIAL_NUMBER_FIELD_IN_WIZARD_ID = "serialNumber";
    private static final String LIVE_PERSPECTIVE_TEXT = "Live";
    private static final String QUANTITY_FIELD_ID = "quantity_field_uuid";
    private static final String DEVICE_WIZARD_FIRST_STEP_TITLE = "1. Attributes";
    private static final String ADDRESS_TYPE = "Address";

    private static final String DEVICES_HAVE_BEEN_CREATED_MESSAGE = "Devices have been created successfully";
    private static final String VALUE_TOO_LARGE_MESSAGE = "Value too large. Maximum: 20";

    private static final String NO_ATTRIBUTE_VALUES_FOR_INDEX_CHECK_VALIDATION = "Cannot check index because some of required test attributes might have no value ind device with od: %s";
    private static final String WRONG_VALUE_FOR_ATTRIBUTE_VALIDATION = "There is wrong %s attribute value for device with id: %s";
    private static final String ATTRIBUTE_DOES_NOT_EXIST_ON_PROPERTY_PANEL_VALIDATION = "Attribute %s has no value on Property Panel for device with id: %s";
    private static final String NO_AVAILABLE_VALUES_LIST_FOR_ATTRIBUTE_VALIDATION = "There is no availableValues list for attribute: %s";
    private static final String INDEX_IS_NOT_EQUAL_FOR_ALL_ATTRIBUTES_VALIDATION = "Index: %s is not the same for %s and %s in device with id: %s";
    private static final String SPECIFIC_ATTRIBUTES_TABLE_HAS_WRONG_SIZE_VALIDATION = "Specific Attributes table has wrong size";

    private static final String SPECIFIC_ATTRIBUTES_TABLE_ID = "ExtendedList-AttributesTable";
    private static final String PROPERTY_PANEL_WIDGET_ID = "PropertyPanelWidget";

    private static final String ID_PATTERN = "id=[\\d]+";
    private static final Pattern PATTERN = Pattern.compile(ID_PATTERN);

    private DeviceWizardPage deviceWizard;
    private HierarchyViewPage hierarchyViewPage;
    private SoftAssert softAssert;
    private int initialNumberOfSteps;
    private String testLocationId;
    private HashMap<String, String> attributesLabelToValueMap;
    private List<String> availableNameValues;
    private List<String> availableSerialNumberValues;
    private List<String> availableHostnameValues;
    private PhysicalInventoryRepository physicalInventoryRepository;
    private PlanningRepository planningRepository;
    private LocationInventoryRepository locationInventoryRepository;

    @BeforeClass
    private void init() {
        physicalInventoryRepository = new PhysicalInventoryRepository(Environment.getInstance());
        planningRepository = new PlanningRepository(Environment.getInstance());
        locationInventoryRepository = new LocationInventoryRepository(Environment.getInstance());
        deviceWizard = new DeviceWizardPage(driver);
        hierarchyViewPage = new HierarchyViewPage(driver);
        softAssert = new SoftAssert();
        attributesLabelToValueMap = new HashMap<>();
        availableNameValues = new LinkedList<>(Arrays.asList("testName1", "testName2", "testName3", "testName4", "testName5"));
        availableSerialNumberValues = new LinkedList<>(Arrays.asList("testSerialNumber1", "testSerialNumber2", "testSerialNumber3", "testSerialNumber4", "testSerialNumber5"));
        availableHostnameValues = new LinkedList<>(Arrays.asList("testHostname1", "testHostname2", "testHostname3", "testHostname4", "testHostname5"));
        setLivePerspective();
        openCreateDeviceWizardFromLeftSideMenu();
        initialNumberOfSteps = deviceWizard.countNumberOfSteps();
        testLocationId = createPhysicalLocationAPI();
    }

    @Test(priority = 1)
    public void validateInitialQuantity() {
        Assert.assertEquals(deviceWizard.getQuantity(), DEVICE_WIZARD_DEFAULT_QUANTITY_VALUE);
    }

    @Test(priority = 2)
    public void increaseQuantityToNotAllowedValue() {
        deviceWizard.setQuantity(DEVICE_WIZARD_NOT_ALLOWED_QUANTITY_VALUE);
        validateQuantityMessage();
        validateIfCorrectFieldsAreGreyedOut();
    }

    @Test(priority = 3)
    public void fulfillFirstStepInWizard() {
        deviceWizard.setEquipmentType(EQUIPMENT_TYPE_VALUE);
        deviceWizard.setModel(MODEL_VALUE);
        tryToMoveToNextStep();
    }

    @Test(priority = 4)
    public void increaseQuantityToAllowedValue() {
        deviceWizard.setQuantity(DEVICE_WIZARD_ALLOWED_QUANTITY_VALUE);
        checkNumberOfWizardSteps();
        validateIfCorrectFieldsAreGreyedOut();
        deviceWizard.next();
    }

    @Test(priority = 5)
    public void fulfillSecondStepInWizard() {
        deviceWizard.setPreciseLocation(testLocationId);
        deviceWizard.next();
    }

    @Test(priority = 6)
    public void setSpecificAttributesForDevices() {
        fillSpecificAttributesForDevices();
        deviceWizard.accept();
    }

    @Test(priority = 7, dependsOnMethods = {"setSpecificAttributesForDevices"})
    public void openHierarchyViewFromActiveLink() {
        validateSystemMessage();
        clickSystemMessageLink();
        closeSystemMessage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8, dependsOnMethods = {"openHierarchyViewFromActiveLink"})
    public void checkNumberOfDevicesOnHierarchyView() {
        Assert.assertEquals(getMainTreeSize(), NUMBER_OF_CREATED_DEVICES);
    }

    @Test(priority = 9, dependsOnMethods = {"checkNumberOfDevicesOnHierarchyView"})
    public void checkCreatedDevicesStructures() {
        List<String> createdDevicesIds = getCreatedDevicesIds();
        createdDevicesIds.forEach(this::performStructureValidation);
    }

    @Test(priority = 10, dependsOnMethods = {"checkCreatedDevicesStructures"})
    public void checkCreatedDevicesAttributes() {
        List<String> createdDevicesIds = getCreatedDevicesIds();
        createdDevicesIds.forEach(this::performSelectionWithValidation);
        softAssert.assertAll();
    }

    @AfterClass
    private void deleteCreatedObjects() {
        String devicesToDelete = String.join(",", getCreatedDevicesIds());
        physicalInventoryRepository.deleteDevice(devicesToDelete);
        Long locationToDelete = Long.parseLong(testLocationId);
        locationInventoryRepository.deleteLocation(locationToDelete, TEST_LOCATION_TYPE);
    }

    @Step("Set perspective to LIVE")
    private void setLivePerspective() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        if(!perspectiveChooser.getCurrentPerspective().equals(LIVE_PERSPECTIVE_TEXT)) {
            perspectiveChooser.setLivePerspective();
        }
    }

    @Step("Open Create Device wizard from left side menu")
    private void openCreateDeviceWizardFromLeftSideMenu() {
        homePage.chooseFromLeftSideMenu("Create Physical Device", "Infrastructure Management", "Create Infrastructure");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Step("Validate if Name, Hostname and Serial Number fields are greyed out")
    private void validateIfCorrectFieldsAreGreyedOut() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        validateField(NAME_FIELD_IN_WIZARD_ID);
        validateField(HOSTNAME_FIELD_IN_WIZARD_ID);
        validateField(SERIAL_NUMBER_FIELD_IN_WIZARD_ID);
    }

    private void validateField(String fieldId) {
        TextField textField = (TextField) ComponentFactory.create(fieldId, Input.ComponentType.TEXT_FIELD, driver, webDriverWait);
        Input.MouseCursor mouseCursor = textField.cursor();
        Assert.assertEquals(mouseCursor, Input.MouseCursor.NOT_ALLOWED);
    }

    @Step("Click system message link")
    private void clickSystemMessageLink() {
        SystemMessageContainer.create(driver, webDriverWait).clickMessageLink();
    }

    @Step("Validate system message")
    private void validateSystemMessage() {
        Assert.assertTrue(getMessageWithText().isPresent());
    }

    @Step("Close system message")
    private void closeSystemMessage() {
        SystemMessageContainer.create(driver, webDriverWait).close();
    }

    @Step("Load device attributes and values")
    private void loadPropertyPanelAttributesAndValues() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);

        List<String> propertyPanelAttributesList = propertyPanel.getVisibleAttributes();
        List<String> propertyPanelAttributesLabels = propertyPanel.getPropertyLabels();

        for (int i = 0; i < propertyPanelAttributesList.size(); i++) {
            attributesLabelToValueMap.put(propertyPanelAttributesLabels.get(i), propertyPanel.getPropertyValue(propertyPanelAttributesList.get(i)));
        }
    }

    @Step("Check correctness of device structure")
    private void validateDeviceStructure(String deviceId, String chassisId) {
        Assert.assertEquals(hierarchyViewPage.getNodeChildrenByPath(getChassisContainerPath(deviceId)).size(), NUMBER_OF_CHASSIS_IN_DEVICE_STRUCTURE);
        Assert.assertEquals(hierarchyViewPage.getNodeChildrenByPath(getSlotContainerPath(deviceId, chassisId)).size(), NUMBER_OF_SLOTS_IN_DEVICE_STRUCTURE);
        hierarchyViewPage.collapseNodeByPath(deviceId);
    }

    @Step("Check correctness of saved attributes for created device")
    private void validateDeviceAttributesAndValues(String deviceId) {
        validateAttribute(NAME_ATTRIBUTE, deviceId);
        validateAttribute(SERIAL_NUMBER_ATTRIBUTE, deviceId);
        validateAttribute(HOSTNAME_ATTRIBUTE, deviceId);
        validateIndex(deviceId);
    }

    private void validateAttribute(String attributeName, String deviceId) {
        String attributeValue = getAttributeValue(attributeName);
        if (attributeValue != null) {
            List<String> availableAttributeValues = getAvailableValuesForAttribute(attributeName);
            validateValue(attributeName, deviceId, attributeValue, availableAttributeValues);
        } else {
            softAssert.fail(String.format(ATTRIBUTE_DOES_NOT_EXIST_ON_PROPERTY_PANEL_VALIDATION, attributeName, deviceId));
        }
    }

    private String getAttributeValue(String attributeName) {
        if (NAME_ATTRIBUTE.equals(attributeName)) {
            return attributesLabelToValueMap.get(attributeName).split("_")[0];
        }
        return attributesLabelToValueMap.get(attributeName);
    }

    private List<String> getAvailableValuesForAttribute(String attributeName) {
        switch (attributeName) {
            case NAME_ATTRIBUTE:
                return availableNameValues;
            case SERIAL_NUMBER_ATTRIBUTE:
                return availableSerialNumberValues;
            case HOSTNAME_ATTRIBUTE:
                return availableHostnameValues;
            default:
                throw new IllegalArgumentException(String.format(NO_AVAILABLE_VALUES_LIST_FOR_ATTRIBUTE_VALIDATION, attributeName));
        }
    }

    private void validateValue(String attributeName, String deviceId, @Nonnull String attributeValue, List<String> availableAttributeValues) {
        if (availableAttributeValues.contains(attributeValue)) {
            availableAttributeValues.remove(attributeValue);
        } else {
            softAssert.fail(String.format(WRONG_VALUE_FOR_ATTRIBUTE_VALIDATION, attributeName, deviceId));
        }
    }

    private void validateIndex(String deviceId) {
        String nameValue = getAttributeValue(NAME_ATTRIBUTE);
        String serialNumberValue = getAttributeValue(SERIAL_NUMBER_ATTRIBUTE);
        String hostnameValue = getAttributeValue(HOSTNAME_ATTRIBUTE);

        if (nameValue != null && serialNumberValue != null && hostnameValue != null) {
            String index = getLastCharacterFromString(nameValue);
            softAssert.assertEquals(getLastCharacterFromString(serialNumberValue), index, String.format(INDEX_IS_NOT_EQUAL_FOR_ALL_ATTRIBUTES_VALIDATION, index, SERIAL_NUMBER_ATTRIBUTE, NAME_ATTRIBUTE, deviceId));
            softAssert.assertEquals(getLastCharacterFromString(hostnameValue), index, String.format(INDEX_IS_NOT_EQUAL_FOR_ALL_ATTRIBUTES_VALIDATION, index, HOSTNAME_ATTRIBUTE, NAME_ATTRIBUTE, deviceId));
        } else {
            softAssert.fail(NO_ATTRIBUTE_VALUES_FOR_INDEX_CHECK_VALIDATION);
        }
    }

    private static String getLastCharacterFromString(String string) {
        return string.substring(string.length()-1);
    }

    @Step("Fill Name, Hostname and Serial Number for all devices in the last step of wizard")
    private void fillSpecificAttributesForDevices() {
        List<EditableList.Row> specificAttributesList = EditableList.createById(driver, webDriverWait, SPECIFIC_ATTRIBUTES_TABLE_ID).getVisibleRows();
        Assert.assertEquals(specificAttributesList.size(), 5, SPECIFIC_ATTRIBUTES_TABLE_HAS_WRONG_SIZE_VALIDATION);

        for (int index = 0; index < specificAttributesList.size(); index++) {
            deviceWizard.setDeviceNameInList(index, availableNameValues.get(index) + "_" + getTimestamp());
            deviceWizard.setDeviceHostnameInList(index, availableHostnameValues.get(index));
            deviceWizard.setDeviceSerialNumberInList(index, availableSerialNumberValues.get(index));
        }
    }

    @Step("Check the number of steps in wizard")
    private void checkNumberOfWizardSteps() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(deviceWizard.countNumberOfSteps(), initialNumberOfSteps + 1);
    }

    @Step("Select device on Hierarchy View")
    private void selectDeviceOnHierarchyView(String devicePath) {
        hierarchyViewPage.getMainTree().getNodeByPath(devicePath).toggleNode();
    }

    @Step("Check if correct warning is displayed for Quantity")
    private void validateQuantityMessage() {
        NumberField quantityField = (NumberField) ComponentFactory.create(QUANTITY_FIELD_ID, Input.ComponentType.NUMBER_FIELD, driver, webDriverWait);
        Assert.assertNotNull(quantityField.getMessages());
        Assert.assertEquals(quantityField.getMessages().get(0), VALUE_TOO_LARGE_MESSAGE);
    }

    @Step("Try to move to next step when there is not allowed value for Quantity")
    private void tryToMoveToNextStep() {
        deviceWizard.next();
        Assert.assertEquals(deviceWizard.getCurrentStateTitle(), DEVICE_WIZARD_FIRST_STEP_TITLE);
    }

    private int getMainTreeSize() {
        return hierarchyViewPage.getMainTree().getVisibleNodes().size();
    }

    private String createPhysicalLocationAPI() {
        Long addressId = planningRepository.getFirstObjectWithType(ADDRESS_TYPE);
        return locationInventoryRepository.createLocation("testBuilding_" + getTimestamp(), TEST_LOCATION_TYPE, addressId);
    }

    private Optional<SystemMessageContainer.Message> getMessageWithText() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .stream()
                .filter(m -> m.getText().contains(DEVICES_HAVE_BEEN_CREATED_MESSAGE))
                .findFirst();
    }

    private String getTimestamp() {
       long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
       return Long.toString(timestamp);
    }

    private List<String> getCreatedDevicesIds() {
        return getIdFromString(driver.getCurrentUrl()).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private void performSelectionWithValidation(String deviceId) {
        selectDeviceOnHierarchyView(deviceId);
        loadPropertyPanelAttributesAndValues();
        validateDeviceAttributesAndValues(deviceId);
        selectDeviceOnHierarchyView(deviceId);
    }

    private void performStructureValidation(String deviceId) {
        String chassisId = getFirstChassisIdFromHierarchy(deviceId);
        validateDeviceStructure(deviceId, chassisId);
    }

    private ResourceHierarchyDTO getResourceHierarchyAPI(String deviceId) {
        return physicalInventoryRepository.getResourceHierarchy("PhysicalDevice", deviceId, "Root");
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

    private String getFirstChassisIdFromHierarchy(String deviceId) {
        ResourceHierarchyDTO resourceHierarchy = getResourceHierarchyAPI(deviceId);
        Map<String, List<String>> hierarchy = ResourceHierarchyConverter.getHierarchiesByParent(resourceHierarchy);

        return hierarchy.keySet().stream()
                .filter(element -> element.contains("Chassis"))
                .findFirst()
                .map(chassis -> chassis.split("_")[1])
                .orElseThrow(RuntimeException::new);
    }

    private String getChassisPath(String deviceId, String chassisId) {
        return deviceId + ".chassis." + chassisId;
    }

    private String getSlotContainerPath(String deviceId, String chassisId) {
        return getChassisPath(deviceId, chassisId) + ".slots";
    }

    private String getChassisContainerPath(String deviceId) {
        return deviceId + ".chassis";
    }

}