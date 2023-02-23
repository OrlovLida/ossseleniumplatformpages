package com.oss.physical.devices;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.physicalinventory.api.dto.DeviceSlotDTO;
import com.comarch.oss.resourcecatalog.api.dto.CompatibilityDTO;
import com.comarch.oss.resourcehierarchy.api.dto.ResourceHierarchyDTO;
import com.comarch.oss.web.pages.HierarchyViewPage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.CardCreateWizardPage;
import com.oss.repositories.CompatibilityRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.PlanningRepository;
import com.oss.transport.infrastructure.planning.PlanningContext;
import com.oss.untils.Environment;
import com.oss.untils.ResourceHierarchyConverter;

import io.qameta.allure.Step;

public class CreateCardsUsingMultiselectionOnSlotsTest extends BaseTestCase {

    private static final String LIVE_PERSPECTIVE = "Live";

    private static final String ADDRESS_TYPE = "Address";
    private static final String BUILDING_TYPE = "Building";
    private static final String LOCATION_NAME = "testBuilding";

    private static final String DEVICE_MODEL_NAME = "LiSA-SA-600";
    private static final String DEVICE_MODEL_TYPE = "ODFModel";
    private static final String DEVICE_NAME = "SeleniumTestDevice";
    private static final String ODF_TYPE = "ODF";

    private static final String SINGLE_SLOT_CARD_MODEL_NAME = "PMU 19/1U";
    private static final String MULTI_SLOT_CARD_MODEL_NAME = "PMU 19/2U";
    private static final String INCOMPATIBLE_CARD_MODEL_NAME = "KSQ";
    private static final String CARD_MODEL_TYPE = "CardModel";

    private static final String SLOT_MODEL_NAME = "LiSA-SA-600 Shelf Slot";
    private static final String SLOT_MODEL_TYPE = "SlotModel";

    private static final int AMOUNT_OF_DEVICES_ON_HIERARCHY_VIEW = 1;
    private static final int AMOUNT_OF_SLOTS_IN_SINGLE_SLOT_CARD_STRUCTURE = 2;
    private static final int AMOUNT_OF_SLOTS_IN_MULTI_SLOT_CARD_STRUCTURE = 4;

    private static final String SLOT_PATH_FORMAT = "%s.chassis.%s.slots.%s";
    private static final String CARD_PATH_FORMAT = "%s.chassis.%s.slots.%s.card.%s";
    private static final String CARD_SLOTS_CONTAINER_PATH_FORMAT = "%s.chassis.%s.slots.%s.card.%s.slots";

    private static final String MODEL_NOT_FOUND_VALIDATION = "Model %s was not found";
    private static final String WRONG_AMOUNT_OF_DEVICES_ON_HIERARCHY_VIEW_VALIDATION = "There is wrong amount of devices on Hierarchy View";
    private static final String CANNOT_FIND_SLOT_ID_VALIDATION = "Cannot find slot id";
    private static final String CANNOT_FIND_CHASSIS_ID_VALIDATION = "Cannot find chassis id";
    private static final String WRONG_AMOUNT_OF_SLOTS_IN_CARD_WIZARD_VALIDATION = "Wrong amount of available slots in Create Card Wizard";
    private static final String SLOTS_FIELD_NOTE_GREYED_OUT_VALIDATION = "Available slots field is not greyed out";
    private static final String NO_SUCCESS_MESSAGE_AFTER_CARD_CREATION_VALIDATION = "No success message after card creation";
    private static final String INCOMPATIBLE_CARD_MODEL_IS_AVAILABLE_VALIDATION = "Incompatible card model is available in Create Card Wizard";
    private static final String WRONG_AMOUNT_OF_CARD_MODELS_VALIDATION = "Wrong amount of compatible card models in Create Card Wizard for model: %s";
    private static final String CANNOT_FIND_COMPONENT_VALIDATION = "Cannot find any component under object with id: %s";
    private static final String WRONG_AMOUNT_OF_CHILDREN_NODES_VALIDATION = "Wrong amount of children nodes under node with path: %s";
    private static final String CARD_NOT_PRESENT_VALIDATION = "Card with id: %s is not visible on Hierarchy View";
    private static final String WRONG_MESSAGE_IN_AVAILABLE_SLOTS_FIELD_VALIDATION = "Available slots field in Create Card Wizard has incorrect message";
    private static final String AVAILABLE_SLOTS_NO_MESSAGE_VALIDATION = "No message in available slots field in Create Card Wizard";

    private static final String CARD_CREATED_MESSAGE = "Card has been created successfully";
    private static final String SINGLE_SLOT_CARD_ADDED_TO_SLOTS_MESSAGE = "Card will be added to slots: [1], [2] in parent:";
    private static final String MULTI_SLOT_CARD_ADDED_TO_SLOTS_MESSAGE = "Card will be added to slots: [4, 5], [6, 7] in parent:";

    private static final String CREATE_ACTION_ID = "CREATE";
    private static final String CREATE_CARD_ON_SLOT_ACTION_ID = "CreateCardOnSlotAction";

    private static final String CREATE_CARD_WIZARD_NAME = "Create Card";

    private final LocationInventoryRepository locationInventoryRepository;
    private final PhysicalInventoryRepository physicalInventoryRepository;
    private final PlanningRepository planningRepository;
    private final CompatibilityRepository compatibilityRepository;

    private HierarchyViewPage hierarchyViewPage;
    private CardCreateWizardPage cardCreateWizardPage;

    private Map<String, String> slotIdByName;
    private final List<String> testDeviceSlots = Arrays.asList("1", "2", "4", "5", "6", "7");
    private final List<String> slotsForSingleSlotCard = Arrays.asList("1", "2");
    private final Map<String, List<String>> slotsByMainSlotForMultiSlotCard = new HashMap<String, List<String>>() {{
        put("4", Arrays.asList("4", "5"));
        put("6", Arrays.asList("6", "7"));
    }};

    private String locationId;
    private Long deviceId;
    private Long chassisId;
    private Long slotModelId;

    {
        locationInventoryRepository = new LocationInventoryRepository(Environment.getInstance());
        physicalInventoryRepository = new PhysicalInventoryRepository(Environment.getInstance());
        planningRepository = new PlanningRepository(Environment.getInstance());
        compatibilityRepository = new CompatibilityRepository(Environment.getInstance());
    }

    @BeforeClass
    public void init() {
        Long deviceModelId = planningRepository.getObjectIdByTypeAndName(DEVICE_MODEL_TYPE, DEVICE_MODEL_NAME)
                .orElseThrow(() -> new NoSuchElementException(String.format(MODEL_NOT_FOUND_VALIDATION, DEVICE_MODEL_NAME)));
        slotModelId = planningRepository.getObjectIdByTypeAndName(SLOT_MODEL_TYPE, SLOT_MODEL_NAME)
                .orElseThrow(() -> new NoSuchElementException(String.format(MODEL_NOT_FOUND_VALIDATION, SLOT_MODEL_NAME)));
        validateCardModelExistence(SINGLE_SLOT_CARD_MODEL_NAME);
        validateCardModelExistence(MULTI_SLOT_CARD_MODEL_NAME);
        validateCardModelExistence(INCOMPATIBLE_CARD_MODEL_NAME);

        locationId = createPhysicalLocation();
        deviceId = createPhysicalDevice(deviceModelId);

        List<DeviceSlotDTO> allDeviceSlots = physicalInventoryRepository.getDeviceSlots(Long.toString(deviceId), PlanningContext.perspectiveLive());
        slotIdByName = getSlotIdByName(allDeviceSlots);

        chassisId = getChassisIdFromDevice(allDeviceSlots);
    }

    @Test(priority = 1)
    public void hierarchyViewForDeviceIsOpened() {
        setLivePerspective();
        HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, ODF_TYPE, Long.toString(deviceId));
        hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);

        Assert.assertEquals(hierarchyViewPage.getMainTreeSize(), AMOUNT_OF_DEVICES_ON_HIERARCHY_VIEW, WRONG_AMOUNT_OF_DEVICES_ON_HIERARCHY_VIEW_VALIDATION);
    }

    @Test(priority = 2, dependsOnMethods = "hierarchyViewForDeviceIsOpened")
    public void createCardWizardIsOpenedForSingleSlotCard() {
        selectSlotsOnHierarchyView(slotsForSingleSlotCard);

        hierarchyViewPage.callAction(CREATE_ACTION_ID, CREATE_CARD_ON_SLOT_ACTION_ID);

        cardCreateWizardPage = new CardCreateWizardPage(driver);
        Assert.assertEquals(cardCreateWizardPage.getWizardName(), CREATE_CARD_WIZARD_NAME);
    }

    @Test(priority = 3, dependsOnMethods = "createCardWizardIsOpenedForSingleSlotCard")
    public void slotsFieldIsFilledInCreateCardWizardForSingleSlotCard() {
        int amountOfAvailableSlotsInWizard = cardCreateWizardPage.getAvailableSlots().size();
        Assert.assertEquals(slotsForSingleSlotCard.size(), amountOfAvailableSlotsInWizard, WRONG_AMOUNT_OF_SLOTS_IN_CARD_WIZARD_VALIDATION);
    }

    @Test(priority = 4, dependsOnMethods = "createCardWizardIsOpenedForSingleSlotCard")
    public void slotsFieldIsInactiveInCreateCardWizardForSingleSlotCard() {
        Assert.assertEquals(cardCreateWizardPage.getAvailableSlotsCursor(), Input.MouseCursor.NOT_ALLOWED, SLOTS_FIELD_NOTE_GREYED_OUT_VALIDATION);
    }

    @Test(priority = 5, dependsOnMethods = "createCardWizardIsOpenedForSingleSlotCard")
    public void notCompatibleModelsAreNotAvailableInCreateCardWizardForSingleSlotCard() {
        Set<String> modelsInWizard = cardCreateWizardPage.getModelsContains(INCOMPATIBLE_CARD_MODEL_NAME);
        Assert.assertTrue(modelsInWizard.isEmpty(), INCOMPATIBLE_CARD_MODEL_IS_AVAILABLE_VALIDATION);
    }

    @Test(priority = 6, dependsOnMethods = "createCardWizardIsOpenedForSingleSlotCard")
    public void compatibleModelsAreAvailableInCreateCardWizardForSingleSlotCard() {
        SoftAssert softAssert = new SoftAssert();

        List<CompatibilityDTO> slotModelCompatibilities = compatibilityRepository.getSlotModelCompatibilities(slotModelId);
        List<String> compatibleCardModels = getCompatibleCardModels(slotModelCompatibilities);

        compatibleCardModels.forEach(cardModel -> {
            int amountOfAvailableModels = cardCreateWizardPage.getModelsContains(cardModel).size();
            softAssert.assertEquals(amountOfAvailableModels, 1, String.format(WRONG_AMOUNT_OF_CARD_MODELS_VALIDATION, cardModel));
        });

        softAssert.assertAll();
    }

    @Test(priority = 7, dependsOnMethods = "createCardWizardIsOpenedForSingleSlotCard")
    public void singleSlotCardModelIsSetInCreateCardWizard() {
        cardCreateWizardPage.setModel(SINGLE_SLOT_CARD_MODEL_NAME);
    }

    @Test(priority = 8, dependsOnMethods = "singleSlotCardModelIsSetInCreateCardWizard")
    public void slotsFieldHasCorrectMessageForSingleSlotCard() {
        String message = getAvailableSlotsFieldMessage();
        Assert.assertTrue(message.contains(SINGLE_SLOT_CARD_ADDED_TO_SLOTS_MESSAGE), WRONG_MESSAGE_IN_AVAILABLE_SLOTS_FIELD_VALIDATION);
    }

    @Test(priority = 9, dependsOnMethods = "singleSlotCardModelIsSetInCreateCardWizard")
    public void singleSlotCardsAreCreated() {
        cardCreateWizardPage.clickAccept();
        Assert.assertTrue(getSuccessMessage().isPresent(), NO_SUCCESS_MESSAGE_AFTER_CARD_CREATION_VALIDATION);
    }

    @Test(priority = 10, dependsOnMethods = "singleSlotCardsAreCreated")
    public void singleSlotCardsAreAddedToDeviceStructure() {
        SoftAssert softAssert = new SoftAssert();

        Map<String, List<String>> hierarchyByParent = getDeviceHierarchyByParent();
        slotsForSingleSlotCard.forEach(slotName -> validateCardHierarchy(softAssert, hierarchyByParent, slotName, slotName, AMOUNT_OF_SLOTS_IN_SINGLE_SLOT_CARD_STRUCTURE));

        unselectSlotsOnHierarchyView(slotsForSingleSlotCard);
        collapseSlotNodes(slotsForSingleSlotCard);

        softAssert.assertAll();
    }

    @Test(priority = 11, dependsOnMethods = "hierarchyViewForDeviceIsOpened")
    public void createCardWizardIsOpenedForMultiSlotCard() {
        selectSlotsOnHierarchyView(slotsByMainSlotForMultiSlotCard.keySet());

        hierarchyViewPage.callAction(CREATE_ACTION_ID, CREATE_CARD_ON_SLOT_ACTION_ID);

        cardCreateWizardPage = new CardCreateWizardPage(driver);
        Assert.assertEquals(cardCreateWizardPage.getWizardName(), CREATE_CARD_WIZARD_NAME);
    }

    @Test(priority = 12, dependsOnMethods = "createCardWizardIsOpenedForMultiSlotCard")
    public void multiSlotCardModelIsSetInCreateCardWizard() {
        cardCreateWizardPage.setModel(MULTI_SLOT_CARD_MODEL_NAME);
    }

    @Test(priority = 13, dependsOnMethods = "multiSlotCardModelIsSetInCreateCardWizard")
    public void slotsFieldHasCorrectMessageForMultiSlotCard() {
        String message = getAvailableSlotsFieldMessage();
        Assert.assertTrue(message.contains(MULTI_SLOT_CARD_ADDED_TO_SLOTS_MESSAGE), WRONG_MESSAGE_IN_AVAILABLE_SLOTS_FIELD_VALIDATION);
    }

    @Test(priority = 14, dependsOnMethods = "multiSlotCardModelIsSetInCreateCardWizard")
    public void multiSlotCardsAreCreated() {
        cardCreateWizardPage.clickAccept();
        Assert.assertTrue(getSuccessMessage().isPresent(), NO_SUCCESS_MESSAGE_AFTER_CARD_CREATION_VALIDATION);
    }

    @Test(priority = 15, dependsOnMethods = "multiSlotCardsAreCreated")
    public void multiSlotCardsAreAddedToDeviceStructure() {
        SoftAssert softAssert = new SoftAssert();

        Map<String, List<String>> hierarchyByParent = getDeviceHierarchyByParent();
        slotsByMainSlotForMultiSlotCard.forEach((mainSlotName, slotNames) ->
                slotNames.forEach(slotName -> validateCardHierarchy(softAssert, hierarchyByParent, mainSlotName, slotName, AMOUNT_OF_SLOTS_IN_MULTI_SLOT_CARD_STRUCTURE)));

        Set<String> slotsWithInsertedCards = extractValues(slotsByMainSlotForMultiSlotCard);
        unselectSlotsOnHierarchyView(slotsWithInsertedCards);
        collapseSlotNodes(slotsWithInsertedCards);

        softAssert.assertAll();
    }

    @AfterClass
    public void deleteCreatedObjects() {
        physicalInventoryRepository.deleteDeviceV3(Collections.singletonList(Long.toString(deviceId)), PlanningContext.perspectiveLive());
        locationInventoryRepository.deleteLocation(Long.parseLong(locationId), BUILDING_TYPE);
    }

    @Step("Validate existence of card model with name: {cardModelName}")
    private void validateCardModelExistence(String cardModelName) {
        planningRepository.getObjectIdByTypeAndName(CARD_MODEL_TYPE, cardModelName)
                .orElseThrow(() -> new NoSuchElementException(String.format(MODEL_NOT_FOUND_VALIDATION, cardModelName)));
    }

    @Step("Create Physical Location")
    private String createPhysicalLocation() {
        Long addressId = planningRepository.getFirstObjectWithType(ADDRESS_TYPE);
        String nameWithTimestamp = LOCATION_NAME + "_" + getTimestamp();
        return locationInventoryRepository.createLocation(nameWithTimestamp, BUILDING_TYPE, addressId);
    }

    @Step("Create Physical Device")
    private Long createPhysicalDevice(Long modelId) {
        String nameWithTimestamp = DEVICE_NAME + "_" + getTimestamp();
        return physicalInventoryRepository.createDevice(BUILDING_TYPE, Long.parseLong(locationId), modelId, nameWithTimestamp, DEVICE_MODEL_TYPE);
    }

    private String getTimestamp() {
        long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        return Long.toString(timestamp);
    }

    @Step("Get slots name to id map")
    private Map<String, String> getSlotIdByName(Collection<DeviceSlotDTO> slots) {
        return testDeviceSlots.stream()
                .collect(Collectors.toMap(
                        slot -> slot,
                        slot -> getSlotId(slot, slots)
                ));
    }

    private static String getSlotId(String slotName,
                                    Collection<DeviceSlotDTO> slots) {
        return slots.stream()
                .filter(object -> object.getName().isPresent())
                .filter(slot -> slot.getName().get().equals(slotName))
                .map(DeviceSlotDTO::getId)
                .map(Objects::toString)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(CANNOT_FIND_SLOT_ID_VALIDATION));
    }

    @Step("Set LIVE perspective")
    private void setLivePerspective() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser perspectiveChooser = PerspectiveChooser.create(driver, webDriverWait);
        if (!perspectiveChooser.getCurrentPerspective().equals(LIVE_PERSPECTIVE)) {
            perspectiveChooser.setLivePerspective();
        }
    }

    @Step("Get Available Slots field message")
    private String getAvailableSlotsFieldMessage() {
        return cardCreateWizardPage.getAvailableSlotMessages().stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(AVAILABLE_SLOTS_NO_MESSAGE_VALIDATION));
    }

    @Step("Select slots: {slots}")
    private void selectSlotsOnHierarchyView(Collection<String> slots) {
        slots.forEach(slot -> {
            String slotId = slotIdByName.get(slot);
            String slotPath = getSlotPath(slotId);
            hierarchyViewPage.selectNodeByPath(slotPath);
        });
    }

    @Step("Unselect slots: {slots}")
    private void unselectSlotsOnHierarchyView(Collection<String> slots) {
        slots.forEach(slot -> {
            String slotId = slotIdByName.get(slot);
            String slotPath = getSlotPath(slotId);
            hierarchyViewPage.unselectNodeByPath(slotPath);
        });
    }

    @Step("Collapse slots: {slots}")
    private void collapseSlotNodes(Collection<String> slots) {
        slots.forEach(slot -> {
            String slotId = slotIdByName.get(slot);
            String slotPath = getSlotPath(slotId);
            hierarchyViewPage.collapseNodeByPath(slotPath);
        });
    }

    @Step("Get chassis id")
    private Long getChassisIdFromDevice(List<DeviceSlotDTO> deviceSlotDTOs) {
        return deviceSlotDTOs.stream()
                .map(DeviceSlotDTO::getPositioningElement)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(CANNOT_FIND_CHASSIS_ID_VALIDATION));
    }

    @Step("Get success message")
    private Optional<SystemMessageContainer.Message> getSuccessMessage() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .stream()
                .filter(message -> message.getText().contains(CARD_CREATED_MESSAGE))
                .findFirst();
    }

    @Step("Get compatible card models")
    private List<String> getCompatibleCardModels(List<CompatibilityDTO> compatibilities) {
        return compatibilities.stream()
                .map(CompatibilityDTO::getModelName)
                .collect(Collectors.toList());
    }

    @Step("Get device hierarchy by parent")
    private Map<String, List<String>> getDeviceHierarchyByParent() {
        ResourceHierarchyDTO resourceHierarchy = physicalInventoryRepository.getResourceHierarchy("PhysicalDevice", Long.toString(deviceId), "Root");
        return ResourceHierarchyConverter.getHierarchiesByParent(resourceHierarchy);
    }

    private String getIdOfFirstComponentUnderObjectWithId(String parentId,
                                                          Map<String, List<String>> hierarchy) {
        return hierarchy.entrySet().stream()
                .filter(object -> object.getKey().contains(parentId))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .map(component -> component.split("_")[1])
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format(CANNOT_FIND_COMPONENT_VALIDATION, parentId)));
    }

    private Set<String> extractValues(Map<String, List<String>> map) {
        return map.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Step("Validate card hierarchy in slot: {slotName}")
    private void validateCardHierarchy(SoftAssert softAssert,
                                       Map<String, List<String>> hierarchyByParent,
                                       String mainSlotName,
                                       String slotName,
                                       int amountOfSlotsInCardStructure) {
        String slotId = slotIdByName.get(slotName);
        String mainSlotId = slotIdByName.get(mainSlotName);
        String cardId = getIdOfFirstComponentUnderObjectWithId(mainSlotId, hierarchyByParent);

        String cardPath = getCardPath(slotId, cardId);
        String cardSlotContainerPath = getCardSlotsContainerPath(slotId, cardId);

        Assert.assertTrue(hierarchyViewPage.isNodePresentByPath(cardPath), String.format(CARD_NOT_PRESENT_VALIDATION, cardId));
        softAssert.assertEquals(hierarchyViewPage.getNodeChildrenByPath(cardPath).size(), 1, String.format(WRONG_AMOUNT_OF_CHILDREN_NODES_VALIDATION, cardPath));
        softAssert.assertEquals(hierarchyViewPage.getNodeChildrenByPath(cardSlotContainerPath).size(), amountOfSlotsInCardStructure, String.format(WRONG_AMOUNT_OF_CHILDREN_NODES_VALIDATION, cardSlotContainerPath));
    }

    private String getSlotPath(String slotId) {
        return String.format(SLOT_PATH_FORMAT, deviceId, chassisId, slotId);
    }

    private String getCardPath(String slotId, String cardId) {
        return String.format(CARD_PATH_FORMAT, deviceId, chassisId, slotId, cardId);
    }

    private String getCardSlotsContainerPath(String slotId, String cardId) {
        return String.format(CARD_SLOTS_CONTAINER_PATH_FORMAT, deviceId, chassisId, slotId, cardId);
    }

}