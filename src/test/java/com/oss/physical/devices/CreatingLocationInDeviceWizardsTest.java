package com.oss.physical.devices;

import java.sql.Timestamp;
import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.ChangeLocationWizardPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.physical.ModalLocationWizardPage;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.PlanningRepository;
import com.oss.untils.Environment;

import io.qameta.allure.Step;

public class CreatingLocationInDeviceWizardsTest extends BaseTestCase {
    private static final String LOCATION_NAME = "testBuilding";
    private static final String ADDRESS_TYPE = "Address";
    private static final String LOCATION_TYPE = "Building";

    private static final String DEVICE_NAME = "SeleniumTestDevice";
    private static final String DEVICE_EQUIPMENT_TYPE = "Router";
    private static final String DEVICE_MODEL_TYPE = "IPDeviceModel";
    private static final String DEVICE_MODEL_MANUFACTURER = "Cisco Systems Inc. ";
    private static final String DEVICE_MODEL_NAME = "ASR9001";

    private static final String DEVICE_MODEL_NOT_FOUND_VALIDATION = "Couldn't find model: %s";
    private static final String ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION = "Attributes step in %s wizard is not visible";
    private static final String LOCATIONS_STEP_NOT_VISIBLE_VALIDATION = "Locations step in create device wizard is not visible";
    private static final String NOT_EMPTY_FIELD_VALIDATION = "%s is not empty";
    private static final String EMPTY_FIELD_VALIDATION = "%s is empty";
    private static final String PARENT_LOCATION_NOT_GREYED_OUT_VALIDATION = "Parent Location field is not greyed out in modal wizard";

    private static final String WIZARD_ATTRIBUTES_STEP_TITLE = "1. Attributes";
    private static final String DEVICE_WIZARD_LOCATIONS_STEP_TITLE = "2. Locations";

    private static final String LOCATION_STEP_LOCATION_ATTRIBUTE_LABEL = "Location";
    private static final String LOCATION_STEP_PHYSICAL_LOCATION_ATTRIBUTE_LABEL = "Physical Location";
    private static final String LOCATION_STEP_PRECISE_LOCATION_ATTRIBUTE_LABEL = "Precise Location";
    private static final String LOCATION_STEP_LOGICAL_LOCATION_ATTRIBUTE_LABEL = "Logical Location";

    private static final String EDIT_GROUP_ID = "EDIT";
    private static final String UPDATE_DEVICE_ACTION_ID = "UpdateDeviceWizardAction";
    private static final String CHANGE_DEVICE_LOCATION_ACTION_ID = "DeviceChangeLocationAction";

    private static final String CHANGE_LOCATION_WIZARD_NAME = "Change Location";

    private final PhysicalInventoryRepository physicalInventoryRepository;
    private final LocationInventoryRepository locationInventoryRepository;
    private final PlanningRepository planningRepository;

    private DeviceWizardPage deviceWizard;
    private ModalLocationWizardPage modalLocationWizard;
    private ChangeLocationWizardPage changeLocationWizard;
    private HierarchyViewPage hierarchyViewPage;

    private final String createDeviceWizardLocationName;
    private final String updateDeviceWizardLocationName;
    private final String changeLocationWizardLocationName;
    private String deviceId;
    private String locationId;

    {
        physicalInventoryRepository = new PhysicalInventoryRepository(Environment.getInstance());
        locationInventoryRepository = new LocationInventoryRepository(Environment.getInstance());
        planningRepository = new PlanningRepository(Environment.getInstance());

        createDeviceWizardLocationName = "firstTestLocation" + getTimestamp();
        updateDeviceWizardLocationName = "secondTestLocation" + getTimestamp();
        changeLocationWizardLocationName = "thirdTestLocation" + getTimestamp();
    }

    @BeforeClass
    public void createPrerequisites() {
        Optional<Long> deviceModelId = planningRepository.getObjectIdByTypeAndName(DEVICE_MODEL_TYPE, DEVICE_MODEL_NAME);

        if (!deviceModelId.isPresent()) {
            Assert.fail(String.format(DEVICE_MODEL_NOT_FOUND_VALIDATION, DEVICE_MODEL_NAME));
        }

        locationId = createPhysicalLocation();
        deviceId = createPhysicalDevice(deviceModelId.get());
        setLivePerspective();
    }

    @Test(priority = 1)
    public void createDeviceWizardIsOpened() {
        deviceWizard = new DeviceWizardPage(driver);

        homePage.chooseFromLeftSideMenu("Create Physical Device", "Infrastructure Management", "Create Infrastructure");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(deviceWizard.getCurrentStateTitle(), WIZARD_ATTRIBUTES_STEP_TITLE, String.format(ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION, "Create Device"));
    }

    @Test(priority = 2, dependsOnMethods = "createDeviceWizardIsOpened")
    public void mandatoryAttributesAreCompleted() {
        deviceWizard.setEquipmentType(DEVICE_EQUIPMENT_TYPE);
        deviceWizard.setModel(DEVICE_MODEL_MANUFACTURER + DEVICE_MODEL_NAME);
    }

    @Test(priority = 3, dependsOnMethods = "mandatoryAttributesAreCompleted")
    public void locationsStepIsOpenedCreateWizard() {
        deviceWizard.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(deviceWizard.getCurrentStateTitle(), DEVICE_WIZARD_LOCATIONS_STEP_TITLE, LOCATIONS_STEP_NOT_VISIBLE_VALIDATION);
    }

    @Test(priority = 4, dependsOnMethods = "locationsStepIsOpenedCreateWizard")
    public void allLocationFieldsAreEmptyInCreateDeviceWizard() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(deviceWizard.getLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, LOCATION_STEP_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertEquals(deviceWizard.getPhysicalLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, LOCATION_STEP_PHYSICAL_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertEquals(deviceWizard.getPreciseLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, LOCATION_STEP_PRECISE_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertEquals(deviceWizard.getLogicalLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, LOCATION_STEP_LOGICAL_LOCATION_ATTRIBUTE_LABEL));

        softAssert.assertAll();
    }

    @Test(priority = 5, dependsOnMethods = "allLocationFieldsAreEmptyInCreateDeviceWizard")
    public void modalLocationWizardIsOpenedCreateWizard() {
        modalLocationWizard = new ModalLocationWizardPage(driver);

        deviceWizard.createObjectsForLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(modalLocationWizard.getCurrentStepTitle(), WIZARD_ATTRIBUTES_STEP_TITLE, String.format(ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION, "Modal Create Location"));
    }

    @Test(priority = 6, dependsOnMethods = "modalLocationWizardIsOpenedCreateWizard")
    public void parentLocationIsEmptyAndGreyedOutCreateWizard() {
        SoftAssert softAssert = new SoftAssert();

        checkIfParentLocationFieldIsEmptyAndGreyedOutInModalWizard(softAssert);

        softAssert.assertAll();
    }

    @Test(priority = 7, dependsOnMethods = "modalLocationWizardIsOpenedCreateWizard")
    public void locationIsCreatedCreateWizard() {
        fillInMandatoryAttributesInModalWizard(createDeviceWizardLocationName);

        modalLocationWizard.create();

        Assert.assertTrue(getMessageWithSuccessType().isPresent());
    }

    @Test(priority = 8, dependsOnMethods = "locationIsCreatedCreateWizard")
    public void locationAttributesAreFilledInInCreateDeviceWizard() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(deviceWizard.getLocation().contains(createDeviceWizardLocationName), String.format(EMPTY_FIELD_VALIDATION, LOCATION_STEP_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertTrue(deviceWizard.getPhysicalLocation().contains(createDeviceWizardLocationName), String.format(EMPTY_FIELD_VALIDATION, LOCATION_STEP_PHYSICAL_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertTrue(deviceWizard.getPreciseLocation().contains(createDeviceWizardLocationName), String.format(EMPTY_FIELD_VALIDATION, LOCATION_STEP_PRECISE_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertEquals(deviceWizard.getLogicalLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, LOCATION_STEP_LOGICAL_LOCATION_ATTRIBUTE_LABEL));

        softAssert.assertAll();
    }

    @Test(priority = 9)
    public void hierarchyViewForDeviceIsOpened() {
        openHierarchyViewForDevice();
        closeSystemMessage();

        Assert.assertEquals(hierarchyViewPage.getMainTreeSize(), 1);
    }

    @Test(priority = 10, dependsOnMethods = "hierarchyViewForDeviceIsOpened")
    public void updateDeviceWizardIsOpened() {
        hierarchyViewPage.selectNodeByPath(deviceId);

        hierarchyViewPage.callAction(EDIT_GROUP_ID, UPDATE_DEVICE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(deviceWizard.getCurrentStateTitle(), WIZARD_ATTRIBUTES_STEP_TITLE, String.format(ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION, "Update Device"));
    }

    @Test(priority = 11, dependsOnMethods = "updateDeviceWizardIsOpened")
    public void locationsStepIsOpenedUpdateWizard() {
        deviceWizard.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(deviceWizard.getCurrentStateTitle(), DEVICE_WIZARD_LOCATIONS_STEP_TITLE, LOCATIONS_STEP_NOT_VISIBLE_VALIDATION);
    }

    @Test(priority = 12, dependsOnMethods = "locationsStepIsOpenedUpdateWizard")
    public void locationsStepIsFilledInWithExistingValues() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(deviceWizard.getLocation().contains(LOCATION_NAME), String.format(EMPTY_FIELD_VALIDATION, LOCATION_STEP_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertTrue(deviceWizard.getPhysicalLocation().contains(LOCATION_NAME), String.format(EMPTY_FIELD_VALIDATION, LOCATION_STEP_PHYSICAL_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertTrue(deviceWizard.getPreciseLocation().contains(LOCATION_NAME), String.format(EMPTY_FIELD_VALIDATION, LOCATION_STEP_PRECISE_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertEquals(deviceWizard.getLogicalLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, LOCATION_STEP_LOGICAL_LOCATION_ATTRIBUTE_LABEL));

        softAssert.assertAll();
    }

    @Test(priority = 13, dependsOnMethods = "locationsStepIsOpenedUpdateWizard")
    public void modalLocationWizardIsOpenedUpdateWizard() {
        deviceWizard.createObjectsForLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(modalLocationWizard.getCurrentStepTitle(), WIZARD_ATTRIBUTES_STEP_TITLE, String.format(ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION, "Modal Create Location"));
    }

    @Test(priority = 14, dependsOnMethods = "modalLocationWizardIsOpenedUpdateWizard")
    public void parentLocationIsEmptyAndGreyedOutUpdateWizard() {
        SoftAssert softAssert = new SoftAssert();

        checkIfParentLocationFieldIsEmptyAndGreyedOutInModalWizard(softAssert);

        softAssert.assertAll();
    }

    @Test(priority = 15, dependsOnMethods = "modalLocationWizardIsOpenedUpdateWizard")
    public void locationIsCreatedUpdateWizard() {
        fillInMandatoryAttributesInModalWizard(updateDeviceWizardLocationName);

        modalLocationWizard.create();

        Assert.assertTrue(getMessageWithSuccessType().isPresent());
    }

    @Test(priority = 16, dependsOnMethods = "locationIsCreatedUpdateWizard")
    public void locationAttributesAreFilledInInUpdateWizard() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(deviceWizard.getLocation().contains(updateDeviceWizardLocationName), String.format(EMPTY_FIELD_VALIDATION, LOCATION_STEP_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertTrue(deviceWizard.getPhysicalLocation().contains(updateDeviceWizardLocationName), String.format(EMPTY_FIELD_VALIDATION, LOCATION_STEP_PHYSICAL_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertTrue(deviceWizard.getPreciseLocation().contains(updateDeviceWizardLocationName), String.format(EMPTY_FIELD_VALIDATION, LOCATION_STEP_PRECISE_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertEquals(deviceWizard.getLogicalLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, LOCATION_STEP_LOGICAL_LOCATION_ATTRIBUTE_LABEL));
        closeSystemMessage();
        deviceWizard.cancel();

        softAssert.assertAll();
    }

    @Test(priority = 17, dependsOnMethods = "hierarchyViewForDeviceIsOpened")
    public void changeLocationWizardIsOpened() {
        changeLocationWizard = new ChangeLocationWizardPage(driver);

        hierarchyViewPage.callAction(EDIT_GROUP_ID, CHANGE_DEVICE_LOCATION_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(changeLocationWizard.getWizardName(), CHANGE_LOCATION_WIZARD_NAME);
    }

    @Test(priority = 18, dependsOnMethods = "changeLocationWizardIsOpened")
    public void allLocationFieldsAreEmptyInChangeLocationWizard() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(changeLocationWizard.getLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, LOCATION_STEP_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertEquals(changeLocationWizard.getPhysicalLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, LOCATION_STEP_PHYSICAL_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertEquals(changeLocationWizard.getPreciseLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, LOCATION_STEP_PRECISE_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertEquals(changeLocationWizard.getLogicalLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, LOCATION_STEP_LOGICAL_LOCATION_ATTRIBUTE_LABEL));

        softAssert.assertAll();
    }

    @Test(priority = 19, dependsOnMethods = "changeLocationWizardIsOpened")
    public void modalLocationWizardIsOpenedChangeLocationWizard() {
        changeLocationWizard.createObjectsForLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(modalLocationWizard.getCurrentStepTitle(), WIZARD_ATTRIBUTES_STEP_TITLE, String.format(ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION, "Modal Create Location"));
    }

    @Test(priority = 20, dependsOnMethods = "modalLocationWizardIsOpenedChangeLocationWizard")
    public void parentLocationIsEmptyAndGreyedOutChangeLocationWizard() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(modalLocationWizard.isParentLocationGreyedOut(), PARENT_LOCATION_NOT_GREYED_OUT_VALIDATION);
        softAssert.assertEquals(modalLocationWizard.getParentLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, "Parent Location"));

        softAssert.assertAll();
    }

    @Test(priority = 21, dependsOnMethods = "modalLocationWizardIsOpenedChangeLocationWizard")
    public void locationIsCreatedChangeLocationWizard() {
        fillInMandatoryAttributesInModalWizard(changeLocationWizardLocationName);

        modalLocationWizard.create();

        Assert.assertTrue(getMessageWithSuccessType().isPresent());
    }

    @Test(priority = 22, dependsOnMethods = "locationIsCreatedChangeLocationWizard")
    public void locationAttributesAreFilledInInChangeLocationWizard() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(changeLocationWizard.getLocation().contains(changeLocationWizardLocationName), String.format(EMPTY_FIELD_VALIDATION, LOCATION_STEP_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertTrue(changeLocationWizard.getPhysicalLocation().contains(changeLocationWizardLocationName), String.format(EMPTY_FIELD_VALIDATION, LOCATION_STEP_PHYSICAL_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertTrue(changeLocationWizard.getPreciseLocation().contains(changeLocationWizardLocationName), String.format(EMPTY_FIELD_VALIDATION, LOCATION_STEP_PRECISE_LOCATION_ATTRIBUTE_LABEL));
        softAssert.assertEquals(changeLocationWizard.getLogicalLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, LOCATION_STEP_LOGICAL_LOCATION_ATTRIBUTE_LABEL));

        softAssert.assertAll();
    }

    @AfterClass
    public void removePrerequisites() {
        physicalInventoryRepository.deleteDevice(deviceId);
        Long location = Long.parseLong(locationId);
        locationInventoryRepository.deleteLocation(location, LOCATION_TYPE);
    }

    @Step("Set perspective to LIVE")
    private void setLivePerspective() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        if (!perspectiveChooser.getCurrentPerspective().equals("Live")) {
            perspectiveChooser.setLivePerspective();
        }
    }

    @Step("Open Hierarchy View for first device")
    private void openHierarchyViewForDevice() {
        HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, DEVICE_EQUIPMENT_TYPE, deviceId);
        hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
    }

    @Step("Close system message")
    private void closeSystemMessage() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SystemMessageContainer.create(driver, webDriverWait).close();
    }

    private String createPhysicalLocation() {
        Long addressId = planningRepository.getFirstObjectWithType(ADDRESS_TYPE);
        String nameWithTimestamp = LOCATION_NAME + "_" + getTimestamp();
        return locationInventoryRepository.createLocation(nameWithTimestamp, LOCATION_TYPE, addressId);
    }

    private String createPhysicalDevice(Long modelId) {
        String nameWithTimestamp = DEVICE_NAME + "_" + getTimestamp();
        Long deviceId = physicalInventoryRepository.createDevice(LOCATION_TYPE, Long.parseLong(locationId), modelId, nameWithTimestamp, DEVICE_MODEL_TYPE);
        return Long.toString(deviceId);
    }

    private String getTimestamp() {
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        return Long.toString(timestamp);
    }

    private void checkIfParentLocationFieldIsEmptyAndGreyedOutInModalWizard(SoftAssert softAssert) {
        softAssert.assertTrue(modalLocationWizard.isParentLocationGreyedOut(), PARENT_LOCATION_NOT_GREYED_OUT_VALIDATION);
        softAssert.assertEquals(modalLocationWizard.getParentLocation(), "", String.format(NOT_EMPTY_FIELD_VALIDATION, "Parent Location"));
    }

    private void fillInMandatoryAttributesInModalWizard(String locationName) {
        modalLocationWizard.setLocationType(LOCATION_TYPE);
        modalLocationWizard.setLocationName(locationName);
        modalLocationWizard.clickNext();
        modalLocationWizard.setFirstGeographicalAddress("");
        modalLocationWizard.clickNext();
    }

    private Optional<SystemMessageContainer.Message> getMessageWithSuccessType() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .stream()
                .filter(m -> SystemMessageContainer.MessageType.SUCCESS.equals(m.getMessageType()))
                .findFirst();
    }

}