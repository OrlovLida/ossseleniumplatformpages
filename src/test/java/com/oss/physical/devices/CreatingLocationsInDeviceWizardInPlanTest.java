package com.oss.physical.devices;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.physical.ModalLocationWizardPage;
import com.oss.pages.physical.ModalLogicalLocationWizardPage;
import com.oss.pages.physical.ModalSublocationWizardPage;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.PlanningRepository;
import com.oss.transport.infrastructure.planning.PlanningContext;
import com.oss.untils.Environment;

import io.qameta.allure.Step;

public class CreatingLocationsInDeviceWizardInPlanTest extends BaseTestCase {
    private static final String LIVE = "Live";
    private static final String PLAN = "Plan";

    private static final String LOCATION_TYPE = "Building";
    private static final String SUBLOCATION_TYPE = "Floor";

    private static final String DEVICE_EQUIPMENT_TYPE = "DWDM Device";
    private static final String DEVICE_MODEL_NAME = "Alcatel 1626LM Compact";

    private static final String EMPTY_FIELD_VALUE = "";

    private static final String ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION = "Attributes step in %s wizard is not visible";
    private static final String LOCATIONS_STEP_NOT_VISIBLE_VALIDATION = "Locations step in create device wizard is not visible";
    private static final String NOT_EMPTY_FIELD_VALIDATION = "%s is not empty";
    private static final String EMPTY_FIELD_VALIDATION = "%s is empty";
    private static final String FIELD_NOT_GREYED_OUT_VALIDATION = "%s field is not greyed out in modal wizard";
    private static final String FIELD_GREYED_OUT_VALIDATION = "%s field is greyed out in modal wizard";
    private static final String WRONG_PERSPECTIVE_VALIDATION = "Found perspective other than %s";
    private static final String FIELD_CONTAINS_ID_VALIDATION = "Found id in %s field instead of identifier";
    private static final String NO_SUCCESS_MESSAGE_VALIDATION = "No success message after %s creation";
    private static final String NO_VALIDATION_MESSAGE_VALIDATION = "There was no validation message after creating Location without privileges";
    private static final String WRONG_VALUE_VALIDATION = "Wrong value for %s field";
    private static final String UNSUPPORTED_TYPE_VALIDATION = "Unsupported type: %s";
    private static final String NO_ACTIVE_LINK_VALIDATION = "There is no active link in system message";

    private static final String CANNOT_CREATE_LOCATION_MESSAGE = "Cannot create Physical Location because of validation restrictions:";

    private static final String WIZARD_ATTRIBUTES_STEP_TITLE = "1. Attributes";
    private static final String DEVICE_WIZARD_LOCATIONS_STEP_TITLE = "2. Locations";

    private static final Pattern ID_PATTERN_URL = Pattern.compile("[&?]id=[\\d]+");
    private static final Pattern ID_PATTERN_WIZARD_WITH_TEXT = Pattern.compile("XId: \\d+");
    private static final Pattern ID_PATTERN_WIZARD = Pattern.compile("\\d+");

    private final PlanningRepository planningRepository;
    private final LocationInventoryRepository locationInventoryRepository;
    private final PhysicalInventoryRepository physicalInventoryRepository;

    private DeviceWizardPage deviceWizard;
    private ModalLocationWizardPage modalLocationWizard;
    private ModalLogicalLocationWizardPage modalLogicalLocationWizard;
    private ModalSublocationWizardPage modalSublocationWizard;

    private static String timestamp;

    private String projectCode;
    private Long projectId;

    private String locationId;
    private String deviceId;

    {
        planningRepository = new PlanningRepository(Environment.getInstance());
        locationInventoryRepository = new LocationInventoryRepository(Environment.getInstance());
        physicalInventoryRepository = new PhysicalInventoryRepository(Environment.getInstance());
        timestamp = getTimestamp();
    }

    @BeforeClass
    public void init() {
        String projectName = "SeleniumTestProject_" + timestamp;
        projectCode = "PST_" + timestamp;
        LocalDate finishDueDate = LocalDate.now().plusDays(1);
        projectId = planningRepository.createProject(projectName, projectCode, finishDueDate);
    }

    @Test(priority = 1)
    public void perspectiveIsSetToPlan() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.openPerspectiveChooser().setPlanPerspective(projectCode);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(perspectiveChooser.getCurrentPerspective().contains(PLAN), String.format(WRONG_PERSPECTIVE_VALIDATION, PLAN));
    }

    @Test(priority = 2, dependsOnMethods = "perspectiveIsSetToPlan")
    public void createDeviceWizardIsOpenedInPlan() {
        deviceWizard = new DeviceWizardPage(driver);

        homePage.chooseFromLeftSideMenu("Create Physical Device", "Infrastructure Management", "Create Infrastructure");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(deviceWizard.getCurrentStateTitle(), WIZARD_ATTRIBUTES_STEP_TITLE, String.format(ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION, "Create Device"));
    }

    @Test(priority = 3, dependsOnMethods = "createDeviceWizardIsOpenedInPlan")
    public void mandatoryAttributesAreCompleted() {
        deviceWizard.setEquipmentType(DEVICE_EQUIPMENT_TYPE);
        deviceWizard.setModel(DEVICE_MODEL_NAME);
    }

    @Test(priority = 4, dependsOnMethods = "mandatoryAttributesAreCompleted")
    public void locationsStepIsOpenedInCreateDeviceWizard() {
        deviceWizard.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(deviceWizard.getCurrentStateTitle(), DEVICE_WIZARD_LOCATIONS_STEP_TITLE, LOCATIONS_STEP_NOT_VISIBLE_VALIDATION);
    }

    @Test(priority = 5, dependsOnMethods = "locationsStepIsOpenedInCreateDeviceWizard")
    public void modalLocationWizardIsOpenedInPlan() {
        modalLocationWizard = new ModalLocationWizardPage(driver);

        deviceWizard.createObjectsForLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(modalLocationWizard.getCurrentStepTitle(), WIZARD_ATTRIBUTES_STEP_TITLE, String.format(ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION, "Modal Create Location"));
    }

    @Test(priority = 6, dependsOnMethods = "modalLocationWizardIsOpenedInPlan")
    public void parentLocationIsEmptyAndGreyedOutInModalLocationWizard() {
        SoftAssert softAssert = new SoftAssert();

        String parentLocation = modalLocationWizard.getParentLocation();
        softAssert.assertTrue(modalLocationWizard.isParentLocationGreyedOut(), String.format(FIELD_NOT_GREYED_OUT_VALIDATION, ModalLocationWizardFields.PARENT_LOCATION.name));
        softAssert.assertTrue(parentLocation.equals(EMPTY_FIELD_VALUE), String.format(NOT_EMPTY_FIELD_VALIDATION, ModalLocationWizardFields.PARENT_LOCATION.name));
        softAssert.assertFalse(ID_PATTERN_WIZARD.matcher(parentLocation).matches(), String.format(FIELD_CONTAINS_ID_VALIDATION, ModalLocationWizardFields.PARENT_LOCATION.name));
        softAssert.assertFalse(ID_PATTERN_WIZARD_WITH_TEXT.matcher(parentLocation).matches(), String.format(FIELD_CONTAINS_ID_VALIDATION, ModalLocationWizardFields.PARENT_LOCATION.name));

        softAssert.assertAll();
    }

    @Test(priority = 7, dependsOnMethods = "modalLocationWizardIsOpenedInPlan")
    public void locationIsCreated() {
        modalLocationWizard.setLocationType(LOCATION_TYPE);
        modalLocationWizard.setLocationName(LocationFields.LOCATION.value);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        modalLocationWizard.clickNext();
        modalLocationWizard.setFirstGeographicalAddress(EMPTY_FIELD_VALUE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        modalLocationWizard.clickNext();

        modalLocationWizard.create();

        locationId = getCreatedObjectIdFromActiveLink();
        Assert.assertTrue(getMessageWithSuccessType().isPresent(), String.format(NO_SUCCESS_MESSAGE_VALIDATION, LocationFields.LOCATION.name));
    }

    @Test(priority = 8, dependsOnMethods = "locationIsCreated")
    public void locationAttributesAreFilledInInCreateDeviceWizard() {
        SoftAssert softAssert = new SoftAssert();

        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.LOCATION, LocationFields.LOCATION.value, softAssert);
        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.PHYSICAL_LOCATION, LocationFields.LOCATION.value, softAssert);
        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.PRECISE_LOCATION, LocationFields.LOCATION.value, softAssert);
        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.LOGICAL_LOCATION, EMPTY_FIELD_VALUE, softAssert);

        softAssert.assertAll();
    }

    @Test(priority = 9, dependsOnMethods = "locationIsCreated")
    public void modalPhysicalLocationWizardIsOpenedInPlan() {
        modalLocationWizard = new ModalLocationWizardPage(driver);

        deviceWizard.createObjectsForPhysicalLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(modalLocationWizard.getCurrentStepTitle(), WIZARD_ATTRIBUTES_STEP_TITLE, String.format(ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION, "Modal Create Physical Location"));
    }

    @Test(priority = 10, dependsOnMethods = "modalPhysicalLocationWizardIsOpenedInPlan")
    public void parentLocationIsFilledAndGreyedOutInModalPhysicalLocationWizard() {
        SoftAssert softAssert = new SoftAssert();

        String parentLocation = modalLocationWizard.getParentLocation();
        softAssert.assertNotEquals(parentLocation, EMPTY_FIELD_VALUE, String.format(EMPTY_FIELD_VALIDATION, ModalLocationWizardFields.PARENT_LOCATION.name));
        softAssert.assertTrue(modalLocationWizard.isParentLocationGreyedOut(), String.format(FIELD_NOT_GREYED_OUT_VALIDATION, ModalLocationWizardFields.PARENT_LOCATION.name));
        softAssert.assertTrue(parentLocation.contains(LocationFields.LOCATION.value), String.format(WRONG_VALUE_VALIDATION, ModalLocationWizardFields.PARENT_LOCATION.name));
        softAssert.assertFalse(ID_PATTERN_WIZARD.matcher(parentLocation).matches(), String.format(FIELD_CONTAINS_ID_VALIDATION, ModalLocationWizardFields.PARENT_LOCATION.name));
        softAssert.assertFalse(ID_PATTERN_WIZARD_WITH_TEXT.matcher(parentLocation).matches(), String.format(FIELD_CONTAINS_ID_VALIDATION, ModalLocationWizardFields.PARENT_LOCATION.name));

        softAssert.assertAll();
    }

    @Test(priority = 11, dependsOnMethods = "modalPhysicalLocationWizardIsOpenedInPlan")
    public void physicalLocationIsCreated() {
        modalLocationWizard.setLocationType(LOCATION_TYPE);
        modalLocationWizard.setLocationName(LocationFields.PHYSICAL_LOCATION.value);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        modalLocationWizard.clickNext();
        modalLocationWizard.clickNext();

        modalLocationWizard.create();

        Assert.assertTrue(getMessageWithSuccessType().isPresent(), String.format(NO_SUCCESS_MESSAGE_VALIDATION, LocationFields.PHYSICAL_LOCATION.name));
    }

    @Test(priority = 12, dependsOnMethods = "physicalLocationIsCreated")
    public void physicalLocationAttributesAreFilledInInCreateDeviceWizard() {
        SoftAssert softAssert = new SoftAssert();

        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.LOCATION, LocationFields.LOCATION.value, softAssert);
        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.PHYSICAL_LOCATION, LocationFields.PHYSICAL_LOCATION.value, softAssert);
        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.PRECISE_LOCATION, LocationFields.PHYSICAL_LOCATION.value, softAssert);
        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.LOGICAL_LOCATION, EMPTY_FIELD_VALUE, softAssert);

        softAssert.assertAll();
    }

    @Test(priority = 13, dependsOnMethods = "physicalLocationIsCreated")
    public void modalLogicalLocationWizardIsOpenedInPlan() {
        modalLogicalLocationWizard = new ModalLogicalLocationWizardPage(driver);

        deviceWizard.createObjectsForLogicalLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(modalLogicalLocationWizard.getCurrentStepTitle(), WIZARD_ATTRIBUTES_STEP_TITLE, String.format(ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION, "Modal Create Logical Location"));
    }

    @Test(priority = 14, dependsOnMethods = "modalLogicalLocationWizardIsOpenedInPlan")
    public void locationAndPhysicalLocationAreFilledAndGreyedOutInModalLogicalLocationWizard() {
        SoftAssert softAssert = new SoftAssert();

        checkIfModalLogicalLocationWizardFieldHaveCorrectState(ModalLogicalLocationWizardFields.LOCATION, LocationFields.LOCATION.value, softAssert);
        checkIfModalLogicalLocationWizardFieldHaveCorrectState(ModalLogicalLocationWizardFields.DIRECT_PHYSICAL_LOCATION, LocationFields.PHYSICAL_LOCATION.value, softAssert);

        softAssert.assertAll();
    }

    @Test(priority = 15, dependsOnMethods = "modalLogicalLocationWizardIsOpenedInPlan")
    public void logicalLocationIsCreated() {
        modalLogicalLocationWizard.setName(LocationFields.LOGICAL_LOCATION.value);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        modalLogicalLocationWizard.next();

        modalLogicalLocationWizard.create();

        Assert.assertTrue(getMessageWithSuccessType().isPresent(), String.format(NO_SUCCESS_MESSAGE_VALIDATION, LocationFields.LOGICAL_LOCATION.name));
    }

    @Test(priority = 16, dependsOnMethods = "logicalLocationIsCreated")
    public void logicalLocationAttributesAreFilledInInCreateDeviceWizard() {
        SoftAssert softAssert = new SoftAssert();

        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.LOCATION, LocationFields.LOCATION.value, softAssert);
        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.PHYSICAL_LOCATION, LocationFields.PHYSICAL_LOCATION.value, softAssert);
        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.PRECISE_LOCATION, LocationFields.PHYSICAL_LOCATION.value, softAssert);
        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.LOGICAL_LOCATION, LocationFields.LOGICAL_LOCATION.value, softAssert);

        softAssert.assertAll();
    }

    @Test(priority = 17, dependsOnMethods = "logicalLocationIsCreated")
    public void modalSublocationWizardIsOpenedInPlan() {
        modalSublocationWizard = new ModalSublocationWizardPage(driver);

        deviceWizard.createObjectsForPreciseLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(modalSublocationWizard.getCurrentStepTitle(), WIZARD_ATTRIBUTES_STEP_TITLE, String.format(ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION, "Modal Create Sublocation"));
    }

    @Test(priority = 18, dependsOnMethods = "modalSublocationWizardIsOpenedInPlan")
    public void locationAndPhysicalLocationAreFilledAndGreyedOutInModalSublocationWizard() {
        SoftAssert softAssert = new SoftAssert();

        checkIfModalSublocationWizardFieldHasCorrectState(ModalSublocationWizardFields.LOCATION, LocationFields.LOCATION.value, softAssert);
        checkIfModalSublocationWizardFieldHasCorrectState(ModalSublocationWizardFields.DIRECT_PHYSICAL_LOCATION, LocationFields.PHYSICAL_LOCATION.value, softAssert);
        checkIfModalSublocationWizardFieldHasCorrectState(ModalSublocationWizardFields.PRECISE_LOCATION, LocationFields.PHYSICAL_LOCATION.value, softAssert);

        softAssert.assertAll();
    }

    @Test(priority = 19, dependsOnMethods = "modalSublocationWizardIsOpenedInPlan")
    public void sublocationIsCreated() {
        modalSublocationWizard.setSublocationName(LocationFields.PRECISE_LOCATION.value);
        modalSublocationWizard.setSublocationType(SUBLOCATION_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        modalSublocationWizard.clickNext();

        modalSublocationWizard.create();

        Assert.assertTrue(getMessageWithSuccessType().isPresent(), String.format(NO_SUCCESS_MESSAGE_VALIDATION, LocationFields.PRECISE_LOCATION.name));
    }

    @Test(priority = 20, dependsOnMethods = "sublocationIsCreated")
    public void sublocationAttributesAreFilledInInCreateDeviceWizard() {
        SoftAssert softAssert = new SoftAssert();

        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.LOCATION, LocationFields.LOCATION.value, softAssert);
        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.PHYSICAL_LOCATION, LocationFields.PHYSICAL_LOCATION.value, softAssert);
        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.PRECISE_LOCATION, LocationFields.PRECISE_LOCATION.value, softAssert);
        checkIfDeviceWizardFieldHasCorrectValue(LocationFields.LOGICAL_LOCATION, LocationFields.LOGICAL_LOCATION.value, softAssert);

        softAssert.assertAll();
    }

    @Test(priority = 21, dependsOnMethods = "sublocationIsCreated")
    public void deviceIsCreated() {
        SoftAssert softAssert = new SoftAssert();

        deviceWizard.accept();
        softAssert.assertTrue(getMessageWithSuccessType().isPresent(), String.format(NO_SUCCESS_MESSAGE_VALIDATION, DEVICE_EQUIPMENT_TYPE));
        deviceId = getCreatedObjectIdFromActiveLink();
        closeSystemMessage();

        softAssert.assertAll();
    }

    @Test(priority = 22)
    public void perspectiveIsSetToLive() {
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        perspectiveChooser.setLivePerspective();

        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(perspectiveChooser.getCurrentPerspective().contains(LIVE), String.format(WRONG_PERSPECTIVE_VALIDATION, LIVE));
    }

    @Test(priority = 23, dependsOnMethods = "perspectiveIsSetToLive")
    public void createDeviceWizardIsOpenedInLive() {
        homePage.chooseFromLeftSideMenu("Create Physical Device", "Infrastructure Management", "Create Infrastructure");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(deviceWizard.getCurrentStateTitle(), WIZARD_ATTRIBUTES_STEP_TITLE, String.format(ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION, "Create Device"));
    }

    @Test(priority = 24, dependsOnMethods = "createDeviceWizardIsOpenedInLive")
    public void locationsStepIsVisible() {
        deviceWizard.setEquipmentType(DEVICE_EQUIPMENT_TYPE);
        deviceWizard.setModel(DEVICE_MODEL_NAME);

        deviceWizard.next();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(deviceWizard.getCurrentStateTitle(), DEVICE_WIZARD_LOCATIONS_STEP_TITLE, LOCATIONS_STEP_NOT_VISIBLE_VALIDATION);
    }

    @Test(priority = 25, dependsOnMethods = "locationsStepIsVisible")
    public void modalLocationWizardIsOpenedInLive() {
        deviceWizard.createObjectsForLocation();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertEquals(modalLocationWizard.getCurrentStepTitle(), WIZARD_ATTRIBUTES_STEP_TITLE, String.format(ATTRIBUTES_STEP_NOT_VISIBLE_VALIDATION, "Modal Create Location"));
    }

    @Test(priority = 26, dependsOnMethods = "modalLocationWizardIsOpenedInLive")
    public void locationIsNotCreated() {
        modalLocationWizard.setLocationType(LOCATION_TYPE);
        modalLocationWizard.setLocationName(LocationFields.LOCATION.value);
        modalLocationWizard.clickNext();
        modalLocationWizard.setFirstGeographicalAddress(EMPTY_FIELD_VALUE);
        modalLocationWizard.clickNext();

        modalLocationWizard.create();

        Assert.assertTrue(getMessageWithValidationRestrictions().isPresent(), NO_VALIDATION_MESSAGE_VALIDATION);
        closeSystemMessage();
        modalLocationWizard.clickCancel();
    }

    @AfterClass
    public void deleteCreatedObjects() {
        physicalInventoryRepository.deleteDeviceV3(Collections.singletonList(deviceId), PlanningContext.projectContext(projectId));
        locationInventoryRepository.deleteLocation(Long.parseLong(locationId), LOCATION_TYPE, projectId);
        planningRepository.cancelProject(projectId);
    }

    @Step("Close system message")
    private void closeSystemMessage() {
        SystemMessageContainer.create(driver, webDriverWait).close();
    }

    private String getTimestamp() {
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        return Long.toString(timestamp);
    }

    private String getCreatedObjectIdFromActiveLink() {
        SystemMessageContainer systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        if (systemMessage.getLinkURL().isPresent()) {
            String linkUrl = systemMessage.getLinkURL().get();
            List<String> createdObjects = getIdsFromUrl(linkUrl);

            return createdObjects.get(0);
        }
        throw new NoSuchElementException(NO_ACTIVE_LINK_VALIDATION);
    }

    private static List<String> getIdsFromUrl(String string) {
        List<String> ids = new ArrayList<>();
        Matcher matcher = ID_PATTERN_URL.matcher(string);
        while (matcher.find()) {
            String stringId = matcher.group();
            String id = stringId.split("=")[1];
            ids.add(id);
        }
        return ids;
    }

    private Optional<SystemMessageContainer.Message> getMessageWithSuccessType() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .stream()
                .filter(m -> SystemMessageContainer.MessageType.SUCCESS.equals(m.getMessageType()))
                .findFirst();
    }

    private Optional<SystemMessageContainer.Message> getMessageWithValidationRestrictions() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .stream()
                .filter(m -> m.getText().contains(CANNOT_CREATE_LOCATION_MESSAGE))
                .findFirst();
    }

    private void checkIfDeviceWizardFieldHasCorrectValue(LocationFields field,
                                                         String expectedValue,
                                                         SoftAssert softAssert) {
        String valueInWizard;
        switch (field) {
            case LOCATION:
                valueInWizard = deviceWizard.getLocation();
                break;
            case PHYSICAL_LOCATION:
                valueInWizard = deviceWizard.getPhysicalLocation();
                break;
            case PRECISE_LOCATION:
                valueInWizard = deviceWizard.getPreciseLocation();
                break;
            case LOGICAL_LOCATION:
                valueInWizard = deviceWizard.getLogicalLocation();
                break;
            default:
                throw new IllegalArgumentException(String.format(UNSUPPORTED_TYPE_VALIDATION, field.name));
        }

        if (!expectedValue.equals(EMPTY_FIELD_VALUE)) {
            softAssert.assertNotEquals(valueInWizard, EMPTY_FIELD_VALUE, String.format(EMPTY_FIELD_VALIDATION, field.name));
        }
        softAssert.assertTrue(valueInWizard.contains(expectedValue), String.format(WRONG_VALUE_VALIDATION, field.name));
        softAssert.assertFalse(ID_PATTERN_WIZARD_WITH_TEXT.matcher(valueInWizard).matches(), String.format(FIELD_CONTAINS_ID_VALIDATION, field.name));
        softAssert.assertFalse(ID_PATTERN_WIZARD.matcher(valueInWizard).matches(), String.format(FIELD_CONTAINS_ID_VALIDATION, field.name));
    }

    private void checkIfModalLogicalLocationWizardFieldHaveCorrectState(ModalLogicalLocationWizardFields field,
                                                                        String expectedValue,
                                                                        SoftAssert softAssert) {
        String valueInModalWizard;
        switch (field) {
            case LOCATION:
                valueInModalWizard = modalLogicalLocationWizard.getLocation();
                softAssert.assertTrue(modalLogicalLocationWizard.isLocationGreyedOut(), String.format(FIELD_NOT_GREYED_OUT_VALIDATION, field.name));
                break;
            case DIRECT_PHYSICAL_LOCATION:
                valueInModalWizard = modalLogicalLocationWizard.getDirectPhysicalLocation();
                softAssert.assertTrue(modalLogicalLocationWizard.isDirectPhysicalLocationGreyedOut(), String.format(FIELD_NOT_GREYED_OUT_VALIDATION, field.name));
                break;
            default:
                throw new IllegalArgumentException(String.format(UNSUPPORTED_TYPE_VALIDATION, field.name));
        }

        softAssert.assertNotEquals(valueInModalWizard, EMPTY_FIELD_VALUE, String.format(EMPTY_FIELD_VALIDATION, field.name));
        softAssert.assertTrue(valueInModalWizard.contains(expectedValue), String.format(WRONG_VALUE_VALIDATION, field.name));
        softAssert.assertFalse(ID_PATTERN_WIZARD_WITH_TEXT.matcher(valueInModalWizard).matches(), String.format(FIELD_CONTAINS_ID_VALIDATION, field.name));
        softAssert.assertFalse(ID_PATTERN_WIZARD.matcher(valueInModalWizard).matches(), String.format(FIELD_CONTAINS_ID_VALIDATION, field.name));
    }

    private void checkIfModalSublocationWizardFieldHasCorrectState(ModalSublocationWizardFields field,
                                                                   String expectedValue,
                                                                   SoftAssert softAssert) {
        String valueInModalWizard;
        switch (field) {
            case LOCATION:
                valueInModalWizard = modalSublocationWizard.getLocation();
                softAssert.assertTrue(modalSublocationWizard.isLocationGreyedOut(), String.format(FIELD_NOT_GREYED_OUT_VALIDATION, field.name));
                break;
            case DIRECT_PHYSICAL_LOCATION:
                valueInModalWizard = modalSublocationWizard.getDirectPhysicalLocation();
                softAssert.assertTrue(modalSublocationWizard.isDirectPhysicalLocationGreyedOut(), String.format(FIELD_NOT_GREYED_OUT_VALIDATION, field.name));
                break;
            case PRECISE_LOCATION:
                valueInModalWizard = modalSublocationWizard.getPreciseLocation();
                softAssert.assertFalse(modalSublocationWizard.isPreciseLocationGreyedOut(), String.format(FIELD_GREYED_OUT_VALIDATION, field.name));
                break;
            default:
                throw new IllegalArgumentException(String.format(UNSUPPORTED_TYPE_VALIDATION, field.name));
        }

        softAssert.assertNotEquals(valueInModalWizard, EMPTY_FIELD_VALUE, String.format(EMPTY_FIELD_VALIDATION, field.name));
        softAssert.assertTrue(valueInModalWizard.contains(expectedValue), String.format(WRONG_VALUE_VALIDATION, field.name));
        softAssert.assertFalse(ID_PATTERN_WIZARD_WITH_TEXT.matcher(valueInModalWizard).matches(), String.format(FIELD_CONTAINS_ID_VALIDATION, field.name));
        softAssert.assertFalse(ID_PATTERN_WIZARD.matcher(valueInModalWizard).matches(), String.format(FIELD_CONTAINS_ID_VALIDATION, field.name));
    }

    private enum LocationFields {
        LOCATION("Location", "testLocation_" + timestamp),
        PHYSICAL_LOCATION("Physical Location", "testPhysicalLocation_" + timestamp),
        PRECISE_LOCATION("Precise Location", "testPreciseLocation_" + timestamp),
        LOGICAL_LOCATION("Logical Location", "testLogicalLocation_" + timestamp);

        private final String value;
        private final String name;

        LocationFields(String name,
                       String value) {
            this.name = name;
            this.value = value;
        }
    }

    private enum ModalLocationWizardFields {
        PARENT_LOCATION("Parent Location");

        private final String name;

        ModalLocationWizardFields(String name) {
            this.name = name;
        }
    }

    private enum ModalLogicalLocationWizardFields {
        LOCATION("Location"),
        DIRECT_PHYSICAL_LOCATION("Direct Physical Location");

        private final String name;

        ModalLogicalLocationWizardFields(String name) {
            this.name = name;
        }
    }

    private enum ModalSublocationWizardFields {
        LOCATION("Location"),
        DIRECT_PHYSICAL_LOCATION("Direct Physical Location"),
        PRECISE_LOCATION("Precise Location");

        private final String name;

        ModalSublocationWizardFields(String name) {
            this.name = name;
        }
    }

}