package com.oss.physical.devices;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.resourcehierarchy.api.dto.ResourceHierarchyDTO;
import com.comarch.oss.transport.trail.api.dto.TerminationLevelDTO;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.PlanningRepository;
import com.oss.repositories.TrailCoreRepository;
import com.oss.transport.infrastructure.planning.PlanningContext;
import com.oss.untils.Environment;
import com.oss.untils.ResourceHierarchyConverter;

import io.qameta.allure.Step;

public class RemovePhysicalDeviceTest extends BaseTestCase {

    private static final String ADDRESS_TYPE = "Address";
    private static final String LOCATION_TYPE = "Building";

    private static final String DEVICE_MODEL_TYPE = "IPDeviceModel";
    private static final String DEVICE_MODEL_NAME = "ASR9001";
    private static final String DEVICE_TYPE = "Router";

    private static final String TRAIL_TYPE = "3rd Party Network";

    private static final String FIRST_DEVICE_NAME = "FirstSeleniumTestDevice";
    private static final String SECOND_DEVICE_NAME = "SecondSeleniumTestDevice";
    private static final String LOCATION_NAME = "testBuilding";

    private static final String DEVICE_MODEL_NOT_FOUND_VALIDATION = "Couldn't find model: %s";
    private static final String PORT_NOT_FOUND_VALIDATION = "Could not find Port in device with id: %s";
    private static final String WRONG_TREE_SIZE_VALIDATION = "Wrong number of objects on Hierarchy View. Found: %d objects";
    private static final String DEVICE_WAS_NOT_REMOVED_FROM_VIEW_VALIDATION = "Device with id: %s was not removed from hierarchy view";
    private static final String NOTIFICATION_INFORMATION_NOT_VISIBLE_VALIDATION = "There was no validation message during occupied device removal. Device id: %s";

    private static final String DEVICE_CANNOT_BE_DELETED_MESSAGE = "1. IPDevice with id: %d cannot be deleted. Following objects block deletion: [Trail(%d)].";
    private static final String ELEMENT_DELETED_SUCCESSFULLY_MESSAGE = "Element deleted successfully.";

    private static final String ADD_OBJECT_BUTTON_ID = "addRootNode";
    private static final String ADVANCED_SEARCH_ID = "advancedSearch";
    private static final String ID_FILTER_ID = "id";
    private static final String EDIT_GROUP_ID = "EDIT";
    private static final String DELETE_DEVICE_ACTION_ID = "DeleteDeviceWizardAction";
    private static final String DELETE_TRAIL_ACTION_ID = "DeleteTrailWizardActionId";
    private static final String DELETE_TERMINATION_WIZARD_ID = "deleteWizardId_prompt-card";

    private static final String HIERARCHY_TREE_TRAIL_PATH = "%d.connections.3rd Party Network.%d";

    private final PhysicalInventoryRepository physicalInventoryRepository;
    private final PlanningRepository planningRepository;
    private final LocationInventoryRepository locationInventoryRepository;
    private final TrailCoreRepository trailCoreRepository;

    private HierarchyViewPage hierarchyViewPage;

    private String locationId;
    private Long firstDeviceId;
    private Long secondDeviceId;
    private Long firstTrailId;
    private Long secondTrailId;

    {
        planningRepository = new PlanningRepository(Environment.getInstance());
        physicalInventoryRepository = new PhysicalInventoryRepository(Environment.getInstance());
        locationInventoryRepository = new LocationInventoryRepository(Environment.getInstance());
        trailCoreRepository = new TrailCoreRepository(Environment.getInstance());
    }

    @BeforeClass
    public void init() {
        Optional<Long> deviceModelId = planningRepository.getObjectIdByTypeAndName(DEVICE_MODEL_TYPE, DEVICE_MODEL_NAME);

        if (!deviceModelId.isPresent()) {
            Assert.fail(String.format(DEVICE_MODEL_NOT_FOUND_VALIDATION, DEVICE_MODEL_NAME));
        }

        locationId = createPhysicalLocation();
        firstDeviceId = createPhysicalDevice(FIRST_DEVICE_NAME, deviceModelId.get());
        firstTrailId = createTrail(TerminationLevelDTO.Device, firstDeviceId);
        secondDeviceId = createPhysicalDevice(SECOND_DEVICE_NAME, deviceModelId.get());
        Long portId = getPortIdFromDeviceStructure(secondDeviceId);
        secondTrailId = createTrail(TerminationLevelDTO.Port, portId);
    }

    @Test(priority = 1)
    public void hierarchyViewIsOpened() {
        setLivePerspective();
        openHierarchyViewForFirstDevice();
        addSecondDeviceToHierarchyView();
        closeSystemMessage();

        int treeSize = hierarchyViewPage.getMainTreeSize();

        Assert.assertEquals(treeSize, 2, String.format(WRONG_TREE_SIZE_VALIDATION, treeSize));
    }

    @Test(priority = 2, dependsOnMethods = "hierarchyViewIsOpened")
    public void itIsNotPossibleToRemoveFirstDeviceWithTerminatedTrail() {
        hierarchyViewPage.selectNodeByPath(String.valueOf(firstDeviceId));
        hierarchyViewPage.callAction(EDIT_GROUP_ID, DELETE_DEVICE_ACTION_ID);

        hierarchyViewPage.clickButtonInConfirmationBox("Yes");

        Assert.assertFalse(getNotificationInformationList().isEmpty(), String.format(NOTIFICATION_INFORMATION_NOT_VISIBLE_VALIDATION, firstDeviceId));
    }

    @Test(priority = 3, dependsOnMethods = "itIsNotPossibleToRemoveFirstDeviceWithTerminatedTrail")
    public void correctValidationIsDisplayedDuringFirstDeviceRemoval() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(getNotificationInformationMessage(), String.format(DEVICE_CANNOT_BE_DELETED_MESSAGE, firstDeviceId, firstTrailId));
        hierarchyViewPage.clickButtonInConfirmationBox("No");
        hierarchyViewPage.unselectNodeByPath(String.valueOf(firstDeviceId));

        softAssert.assertAll();
    }

    @Test(priority = 4, dependsOnMethods = "hierarchyViewIsOpened")
    public void trailFromFirstDeviceIsRemoved() {
        String trailPath = getTrailPath(firstDeviceId, firstTrailId);
        hierarchyViewPage.selectNodeByPath(trailPath);
        hierarchyViewPage.callAction(EDIT_GROUP_ID, DELETE_TRAIL_ACTION_ID);

        confirmTrailDeletion();

        Assert.assertTrue(getMessageWithSuccessType().isPresent());
        closeSystemMessage();
    }

    @Test(priority = 5, dependsOnMethods = "trailFromFirstDeviceIsRemoved")
    public void firstDeviceIsRemoved() {
        hierarchyViewPage.selectNodeByPath(String.valueOf(firstDeviceId));
        hierarchyViewPage.callAction(EDIT_GROUP_ID, DELETE_DEVICE_ACTION_ID);

        hierarchyViewPage.clickButtonInConfirmationBox("Yes");

        Assert.assertTrue(getMessageWithSuccessText().isPresent());
        closeSystemMessage();
    }

    @Test(priority = 6, dependsOnMethods = "firstDeviceIsRemoved")
    public void firstDeviceIsNotVisibleOnHierarchyView() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(hierarchyViewPage.getMainTreeSize(), 1, String.format(DEVICE_WAS_NOT_REMOVED_FROM_VIEW_VALIDATION, firstDeviceId));
    }

    @Test(priority = 7, dependsOnMethods = "hierarchyViewIsOpened")
    public void itIsNotPossibleToRemoveSecondDeviceWithTerminatedTrail() {
        hierarchyViewPage.selectNodeByPath(String.valueOf(secondDeviceId));
        hierarchyViewPage.callAction(EDIT_GROUP_ID, DELETE_DEVICE_ACTION_ID);

        hierarchyViewPage.clickButtonInConfirmationBox("Yes");

        Assert.assertFalse(getNotificationInformationList().isEmpty(), String.format(NOTIFICATION_INFORMATION_NOT_VISIBLE_VALIDATION, secondDeviceId));
    }

    @Test(priority = 8, dependsOnMethods = "itIsNotPossibleToRemoveSecondDeviceWithTerminatedTrail")
    public void correctValidationIsDisplayedDuringSecondDeviceRemoval() {
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(getNotificationInformationMessage(), String.format(DEVICE_CANNOT_BE_DELETED_MESSAGE, secondDeviceId, secondTrailId));
        hierarchyViewPage.clickButtonInConfirmationBox("No");
        hierarchyViewPage.unselectNodeByPath(String.valueOf(secondDeviceId));

        softAssert.assertAll();
    }

    @Test(priority = 9, dependsOnMethods = "hierarchyViewIsOpened")
    public void trailFromSecondDeviceIsRemoved() {
        String trailPath = getTrailPath(secondDeviceId, secondTrailId);
        hierarchyViewPage.selectNodeByPath(trailPath);
        hierarchyViewPage.callAction(EDIT_GROUP_ID, DELETE_TRAIL_ACTION_ID);

        confirmTrailDeletion();

        Assert.assertTrue(getMessageWithSuccessType().isPresent());
        closeSystemMessage();
    }

    @Test(priority = 10, dependsOnMethods = "trailFromSecondDeviceIsRemoved")
    public void secondDeviceIsRemoved() {
        hierarchyViewPage.selectNodeByPath(String.valueOf(secondDeviceId));
        hierarchyViewPage.callAction(EDIT_GROUP_ID, DELETE_DEVICE_ACTION_ID);

        hierarchyViewPage.clickButtonInConfirmationBox("Yes");

        Assert.assertTrue(getMessageWithSuccessText().isPresent());
        closeSystemMessage();
    }

    @Test(priority = 11, dependsOnMethods = "secondDeviceIsRemoved")
    public void secondDeviceIsNotVisibleOnHierarchyView() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(hierarchyViewPage.getMainTreeSize(), 0, String.format(DEVICE_WAS_NOT_REMOVED_FROM_VIEW_VALIDATION, secondDeviceId));
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
    private void openHierarchyViewForFirstDevice() {
        HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, DEVICE_TYPE, String.valueOf(firstDeviceId));
        hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
    }

    @Step("Add second device to Hierarchy View")
    private void addSecondDeviceToHierarchyView() {
        hierarchyViewPage.callActionById(ADD_OBJECT_BUTTON_ID);

        AdvancedSearchWidget advancedSearchWidget = AdvancedSearchWidget.createById(driver, webDriverWait, ADVANCED_SEARCH_ID);
        advancedSearchWidget.setFilter(ID_FILTER_ID, String.valueOf(secondDeviceId));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        advancedSearchWidget.getTableComponent().selectRow(0);
        advancedSearchWidget.clickAdd();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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

    private String getTimestamp() {
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        return Long.toString(timestamp);
    }

    private Long createPhysicalDevice(String name,
                                      Long modelId) {
        String nameWithTimestamp = name + "_" + getTimestamp();
        return physicalInventoryRepository.createDevice(LOCATION_TYPE, Long.parseLong(locationId), modelId, nameWithTimestamp, DEVICE_MODEL_TYPE);
    }

    private Long createTrail(TerminationLevelDTO terminationLevel,
                             Long terminationId) {
        return trailCoreRepository.createUnidirectionalTrail(TRAIL_TYPE, terminationLevel, terminationId, PlanningContext.perspectiveLive());
    }

    private Long getPortIdFromDeviceStructure(Long deviceId) {
        ResourceHierarchyDTO resourceHierarchy = physicalInventoryRepository.getResourceHierarchy("PhysicalDevice", String.valueOf(deviceId), "root");
        Map<String, List<String>> hierarchy = ResourceHierarchyConverter.getHierarchiesByParent(resourceHierarchy);
        String id = hierarchy.values().stream()
                .flatMap(Collection::stream)
                .filter(element -> element.contains("Port_"))
                .findFirst()
                .map(port -> port.split("_")[1])
                .orElseThrow(() -> new NoSuchElementException(String.format(PORT_NOT_FOUND_VALIDATION, deviceId)));
        return Long.parseLong(id);
    }

    private String getTrailPath(Long deviceId,
                                Long trailId) {
        return String.format(HIERARCHY_TREE_TRAIL_PATH, deviceId, trailId);
    }

    private List<GlobalNotificationContainer.NotificationInformation> getNotificationInformationList() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return GlobalNotificationContainer.create(driver, webDriverWait).getNotificationInformations();
    }

    private String getNotificationInformationMessage() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return GlobalNotificationContainer.create(driver, webDriverWait)
                .getNotificationInformation()
                .getMessage();
    }

    private void confirmTrailDeletion() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Wizard.createByComponentId(driver, webDriverWait, DELETE_TERMINATION_WIZARD_ID).clickButtonByLabel("Delete");
    }

    private Optional<SystemMessageContainer.Message> getMessageWithSuccessType() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .stream()
                .filter(m -> SystemMessageContainer.MessageType.SUCCESS.equals(m.getMessageType()))
                .findFirst();
    }

    private Optional<SystemMessageContainer.Message> getMessageWithSuccessText() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .stream()
                .filter(m -> m.getText().contains(ELEMENT_DELETED_SUCCESSFULLY_MESSAGE))
                .findFirst();
    }
}