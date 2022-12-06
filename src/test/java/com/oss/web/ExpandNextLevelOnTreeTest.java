package com.oss.web;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.services.ResourceCatalogClient;
import com.oss.untils.Environment;

/**
 * @author Faustyna Szczepanik
 */

public class ExpandNextLevelOnTreeTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(ExpandNextLevelOnTreeTest.class);
    private static final String LOCATION_NAME = "SeleniumExpandTest-Building-OSSWEB";
    private static final String ROOM_NAME_1 = "Room_1";
    private static final String ROOM_NAME_2 = "Room_2";
    private static final String LOCATION_TYPE_BUILDING = "Building";
    private static final String SUB_LOCATION_TYPE_ROOM = "Room";
    private static final String NAME_ATTRIBUTE_ID = "name";
    private static final String DEVICE_MODEL_TYPE = "IPDeviceModel";
    private static final String DEVICE_MODEL = "ASR-9010-AC";
    private static final String CARD_MODEL_TYPE = "CardModel";
    private static final String DEVICE_NAME = "SeleniumExpandTest-Router-OSSWEB";
    private static final String DEVICE_IN_ROOM_PATH = "SeleniumExpandTest-Building-OSSWEB.Locations.Room.Room_1.Hardware.Router.SeleniumExpandTest-Router-OSSWEB";
    private static final String ROOM_1_PATH = LOCATION_NAME + ".Locations.Room." + ROOM_NAME_1;
    private static final String PATH_1ST_LOCATIONS_RELATION = LOCATION_NAME + ".Locations";
    private static final String CHASSIS_NAME = DEVICE_NAME + "/Chassis";
    private static final String CARD_MODEL = "A9K-8T-B";
    private static final String SLOT_NAME_0 = "0";
    private static final String SLOT_NAME_1 = "1";
    public static final String SHOW_ON_GROUP_ID = "NAVIGATION";

    private Environment env = Environment.getInstance();
    private HierarchyViewPage hierarchyViewPage;
    private String locationId;
    private Long deviceId;
    private Long roomId;
    private Long roomId_2;

    @BeforeClass
    public void goToHierarchyViewPage() {
        hierarchyViewPage = HierarchyViewPage.openHierarchyViewPage(driver, BASIC_URL, "Location");
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        if (!locationInventoryRepository.getLocationId(LOCATION_NAME).isPresent()) {
            createDataForTest();
        }
        locationId = locationInventoryRepository.getLocationId(LOCATION_NAME).get();
        hierarchyViewPage.getMainTree().searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, LOCATION_NAME);
        List<String> nodesLabel = hierarchyViewPage.getVisibleNodesLabel();
        Assertions.assertThat(nodesLabel).contains(LOCATION_NAME);

        log.info("Search Location (XID: " + locationId + ") in Hierarchy View");

    }

    private void createDataForTest() {
        Long firstGeographicalAddressId = getGeographicalAddress();
        locationId = createBuilding(firstGeographicalAddressId);
        roomId = createRoom(ROOM_NAME_1, Long.valueOf(locationId), LOCATION_TYPE_BUILDING);
        createRouterWithCards();
        roomId_2 = createRoom(ROOM_NAME_2, Long.valueOf(locationId), LOCATION_TYPE_BUILDING);
    }

    private void createRouterWithCards() {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        Long deviceModelId = resourceCatalogClient.getModelIds(DEVICE_MODEL);
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        deviceId = physicalInventoryRepository.createDevice(SUB_LOCATION_TYPE_ROOM, Long.valueOf(roomId), deviceModelId, DEVICE_NAME,
                DEVICE_MODEL_TYPE);
        Long cardModelId = resourceCatalogClient.getModelIds(CARD_MODEL);
        physicalInventoryRepository.createCard(deviceId, SLOT_NAME_0, cardModelId, CARD_MODEL_TYPE);
    }

    @Test(priority = 1)
    public void expandNextLevelRootWithGroupedRelation() {
        hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        TreeComponent.Node routerInRoom = hierarchyViewPage.getMainTree()
                .getNodeByLabelsPath(DEVICE_IN_ROOM_PATH);
        TreeComponent.Node routerInBuilding = hierarchyViewPage.getMainTree()
                .getNodeByLabelsPath(LOCATION_NAME + ".Hardware.Router." + DEVICE_NAME);
        TreeComponent.Node nodeChassis = hierarchyViewPage.getMainTree()
                .getNode(CHASSIS_NAME);
        Assertions.assertThat(nodeChassis.isExpanded()).isFalse();
        Assertions.assertThat(routerInBuilding.isExpanded()).isTrue();
        Assertions.assertThat(routerInRoom.isExpanded()).isFalse();
    }

    @Test(priority = 2)
    public void checkIfExpandNextLevelExistForNoRelation() {
        TreeComponent.Node room2 = hierarchyViewPage.getMainTree()
                .getNode(ROOM_NAME_2);
        Assertions.assertThat(room2.isExpandNextLevelPresent()).isFalse();
    }

    @Test(priority = 3)
    public void expandNextLevelNode() {
        hierarchyViewPage.expandNextLevel(DEVICE_IN_ROOM_PATH);
        TreeComponent.Node Slot0 = hierarchyViewPage.getMainTree()
                .getNode(SLOT_NAME_0);
        Assertions.assertThat(Slot0.isExpanded()).isFalse();
    }

    @Test(priority = 4)
    public void expandNextLevelLocationAndSublocationAsRoot() {
        hierarchyViewPage.selectNodeByLabelsPath(LOCATION_NAME);
        hierarchyViewPage.selectNodeByLabelsPath(ROOM_1_PATH);
        hierarchyViewPage.getMainTree().callActionById(SHOW_ON_GROUP_ID,
                HierarchyViewPage.OPEN_HIERARCHY_VIEW_CONTEXT_ACTION_ID);
        hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        TreeComponent.Node routerInRoom = hierarchyViewPage.getMainTree()
                .getNodeByLabelsPath(DEVICE_IN_ROOM_PATH);
        Assertions.assertThat(routerInRoom.isExpanded()).isFalse();
        TreeComponent.Node room1 = hierarchyViewPage.getMainTree()
                .getNode(ROOM_NAME_1);
        Assertions.assertThat(room1.isExpanded()).isFalse();
    }

    @Test(priority = 5)
    public void expandNextLevelLocationAfterFiltering() {
        hierarchyViewPage.selectNodeByLabelsPath(LOCATION_NAME);
        hierarchyViewPage.getMainTree().callActionById(SHOW_ON_GROUP_ID,
                HierarchyViewPage.OPEN_HIERARCHY_VIEW_CONTEXT_ACTION_ID);
        hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        TreeComponent.Node location = hierarchyViewPage.getMainTree()
                .getNodeByLabelsPath(PATH_1ST_LOCATIONS_RELATION);
        location.searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, ROOM_NAME_1);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(ROOM_NAME_1).doesNotContain(ROOM_NAME_2);
        location.searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, ROOM_NAME_2);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(ROOM_NAME_2).doesNotContain(ROOM_NAME_1);
        clearFilter(location);
        hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        TreeComponent.Node room1 = hierarchyViewPage.getMainTree()
                .getNodeByLabelsPath(ROOM_1_PATH);
        Assertions.assertThat(room1.isExpanded()).isTrue();
    }

    private String createBuilding(Long addressId) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.createLocation(LOCATION_NAME, LOCATION_TYPE_BUILDING, addressId);
    }

    private Long createRoom(String roomName, Long preciseLocationId, String preciseLocationType) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.createSubLocation(SUB_LOCATION_TYPE_ROOM, roomName, preciseLocationId, preciseLocationType,
                Long.valueOf(locationId), LOCATION_TYPE_BUILDING);
    }

    private Long getGeographicalAddress() {
        AddressRepository addressRepository = new AddressRepository(env);
        return addressRepository.getFirstGeographicalAddressId();
    }

    private void clearFilter(TreeComponent.Node node) {
        AdvancedSearch advancedSearch = node.openAdvancedSearch();
        advancedSearch.clickClearAll();
        advancedSearch.clickApply();
    }

}
