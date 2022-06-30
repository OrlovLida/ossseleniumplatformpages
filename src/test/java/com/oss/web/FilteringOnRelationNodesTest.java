/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.web;

import static com.oss.untils.Constants.DEVICE_MODEL_TYPE;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.Popup;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.services.ResourceCatalogClient;
import com.oss.untils.Environment;

/**
 * @author Gabriela Zaranek
 */
public class FilteringOnRelationNodesTest extends BaseTestCase {
    private static final String DONT_SHOW_MESSAGE_AGAIN_CHECKBOX_ID = "dontShowMessageAgainCheckboxId";
    private static final String TRUE = "true";
    private Environment env = Environment.getInstance();
    private static final String DEVICE_NAME = "FORN-123-456";
    private static final String PORT_NAME_10 = "10";
    private static final String PORT_NAME_02 = "02";
    private static final String EXPAND_CLEAR_FILTERS_BUTTON = "Expand & Clear Filters";
    private static final Logger log = LoggerFactory.getLogger(TreeWidgetTest.class);
    private static final String LOCATION_NAME = "FilteringOnRelationNodesTest";
    private static final String PATH_1ST_LOCATIONS_RELATION = LOCATION_NAME + ".Locations";
    private static final String LOCATION_TYPE_BUILDING = "Building";
    private static final String NAME_ATTRIBUTE_ID = "name";
    private static final String SUB_LOCATION_TYPE_ROOM = "Room";
    private static final String ROOM_NAME = "RM_ST1";
    private static final String ROOM_NAME_2 = "RM_ST2";
    private static final String FLOOR_NAME_2 = "FL_ST2";
    private static final String SUB_LOCATION_TYPE_FLOOR = "Floor";
    private static final String PATH_PORTS_RELATION =
            LOCATION_NAME + ".Locations." + SUB_LOCATION_TYPE_FLOOR + "." + FLOOR_NAME_2 + ".Hardware.Switch." + DEVICE_NAME + ".Ports";
    private static final String PORT_10_PATH = PATH_PORTS_RELATION + "." + PORT_NAME_10;
    private static final String PORT_02_PATH = PATH_PORTS_RELATION + "." + PORT_NAME_02;
    private static final String FLOOR_NAME = "FL_ST1";
    private static final String PATH_LOCATIONS_2ST_RELATION =
            LOCATION_NAME + ".Locations." + SUB_LOCATION_TYPE_FLOOR + "." + FLOOR_NAME + ".Locations";
    private static final String ROOM_PATH = LOCATION_NAME + ".Locations." + SUB_LOCATION_TYPE_FLOOR + "." + FLOOR_NAME + ".Locations."
            + SUB_LOCATION_TYPE_ROOM + "." + ROOM_NAME;
    private static final String ROOM_2_PATH = LOCATION_NAME + ".Locations." + SUB_LOCATION_TYPE_FLOOR + "." + FLOOR_NAME + ".Locations."
            + SUB_LOCATION_TYPE_ROOM + "." + ROOM_NAME_2;
    private static final String DEVICE_MODEL = "N9K-C9396PX";
    
    private HierarchyViewPage hierarchyViewPage;
    private String locationId;
    
    @BeforeClass
    public void goToHierarchyViewPage() {
        
        hierarchyViewPage = HierarchyViewPage.openHierarchyViewPage(driver, BASIC_URL, "Location");
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        if (!locationInventoryRepository.getLocationId(LOCATION_NAME).isPresent()) {
            createLocationsForTest();
        }
        locationId = locationInventoryRepository.getLocationId(LOCATION_NAME).get();
        hierarchyViewPage.getMainTree().searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, LOCATION_NAME);
        List<String> nodesLabel = hierarchyViewPage.getVisibleNodesLabel();
        Assertions.assertThat(nodesLabel).contains(LOCATION_NAME);
        
        log.info("Search Location (XID: " + locationId + ") in Hierarchy View");
        
    }
    
    @Test(priority = 1)
    public void filterOnRelation() {
        hierarchyViewPage.getFirstNode().expandNextLevel();
        TreeComponent.Node node = getNode(PATH_1ST_LOCATIONS_RELATION);
        node.searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, FLOOR_NAME_2);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME_2).doesNotContain(FLOOR_NAME);
    }
    
    @Test(priority = 2)
    public void editFilter() {
        TreeComponent.Node node = getNode(PATH_1ST_LOCATIONS_RELATION);
        node.searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, FLOOR_NAME);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).doesNotContain(FLOOR_NAME_2);
    }
    
    @Test(priority = 3)
    public void clearFilter() {
        TreeComponent.Node node = getNode(PATH_1ST_LOCATIONS_RELATION);
        clearFilter(node);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).contains(FLOOR_NAME_2);
    }
    
    @Test(priority = 4)
    public void noDataResults() {
        TreeComponent.Node node = getNode(PATH_1ST_LOCATIONS_RELATION);
        node.searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, "sdfad");
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).doesNotContain(FLOOR_NAME).doesNotContain(FLOOR_NAME_2);
        clearFilter(node);
    }
    
    @Test(priority = 5)
    public void filterOnRelationsAndExpandNextLevel() {
        hierarchyViewPage.getFirstNode().expandNextLevel();
        TreeComponent.Node node = getNode(PATH_1ST_LOCATIONS_RELATION);
        node.searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, FLOOR_NAME);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).doesNotContain(FLOOR_NAME_2);
        Optional<Popup> confirmationBox = hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        Assertions.assertThat(confirmationBox).isPresent();
        confirmationBox.get().setComponentValue(DONT_SHOW_MESSAGE_AGAIN_CHECKBOX_ID, TRUE, Input.ComponentType.CHECKBOX);
        confirmationBox.get().clickButtonByLabel(EXPAND_CLEAR_FILTERS_BUTTON);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).contains(FLOOR_NAME_2);
        getNode(PATH_1ST_LOCATIONS_RELATION).searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, FLOOR_NAME);
        Optional<Popup> confirmationBox2 = hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        Assertions.assertThat(confirmationBox2).isNotPresent();
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).contains(FLOOR_NAME_2);
    }
    
    @Test(priority = 6)
    public void filterOnMoreRelations() {
        TreeComponent.Node location1stLevel = getNode(PATH_1ST_LOCATIONS_RELATION);
        location1stLevel.searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, FLOOR_NAME);
        TreeComponent.Node location2stLevel = getNode(PATH_LOCATIONS_2ST_RELATION);
        location2stLevel.searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, ROOM_NAME);
        
        Assertions.assertThat(hierarchyViewPage.isNodePresent(ROOM_PATH)).isTrue();
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).doesNotContain(FLOOR_NAME_2)
                .contains(ROOM_NAME)
                .doesNotContain(ROOM_NAME_2);
        
        clearFilter(location2stLevel);
        clearFilter(location1stLevel);
    }
    
    @Test(priority = 7)
    public void filterOnRelationsAtDifferentLevels() {
        hierarchyViewPage.expandNextLevel(LOCATION_NAME).ifPresent(popup -> popup.clickButtonByLabel(EXPAND_CLEAR_FILTERS_BUTTON));
        TreeComponent.Node location2stLevel = getNode(PATH_LOCATIONS_2ST_RELATION);
        location2stLevel.searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, ROOM_NAME);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).doesNotContain(ROOM_NAME_2);
        
        TreeComponent.Node nodePathPorts = hierarchyViewPage.getNodeByLabelPath(PATH_PORTS_RELATION);
        nodePathPorts.expandNode();
        nodePathPorts.searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, PORT_NAME_10);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(PORT_NAME_10).doesNotContain(PORT_NAME_02);
        
        TreeComponent.Node location1stLevel = getNode(PATH_1ST_LOCATIONS_RELATION);
        location1stLevel.searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, FLOOR_NAME_2);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME_2).doesNotContain(FLOOR_NAME);
        
        location1stLevel.searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, FLOOR_NAME);
        Assertions.assertThat(hierarchyViewPage.isNodePresent(ROOM_PATH)).isTrue();
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).doesNotContain(FLOOR_NAME_2)
                .contains(ROOM_NAME).doesNotContain(ROOM_NAME_2);
        
        clearFilter(location1stLevel);
        
        Assertions.assertThat(hierarchyViewPage.isNodePresent(PORT_10_PATH)).isTrue();
        Assertions.assertThat(hierarchyViewPage.isNodePresent(PORT_02_PATH)).isFalse();
        Assertions.assertThat(hierarchyViewPage.isNodePresent(ROOM_PATH)).isTrue();
        Assertions.assertThat(hierarchyViewPage.isNodePresent(ROOM_2_PATH)).isFalse();

        clearFilter(hierarchyViewPage.getNodeByLabelPath(PATH_PORTS_RELATION));
        DelayUtils.sleep();
        clearFilter(hierarchyViewPage.getNodeByLabelPath(PATH_LOCATIONS_2ST_RELATION));
    }
    
    private void clearFilter(TreeComponent.Node node) {
        AdvancedSearch advancedSearch = node.openAdvancedSearch();
        advancedSearch.clickClearAll();
        advancedSearch.clickApply();
    }
    
    private void createLocationsForTest() {
        Long firstGeographicalAddressId = getGeographicalAddress();
        locationId = createBuilding(firstGeographicalAddressId);
        Long floorId = createFloor(FLOOR_NAME, locationId);
        Long floor2Id = createFloor(FLOOR_NAME_2, locationId);
        createRoom(ROOM_NAME, floorId);
        createRoom(ROOM_NAME_2, floorId);
        createDevice(floor2Id);
    }
    
    private Long getGeographicalAddress() {
        AddressRepository addressRepository = new AddressRepository(env);
        return addressRepository.getFirstGeographicalAddressId();
    }
    
    private String createBuilding(Long addressId) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.createLocation(LOCATION_NAME, LOCATION_TYPE_BUILDING, addressId);
    }
    
    private Long createRoom(String roomName, Long preciseLocationId) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.createSubLocation(SUB_LOCATION_TYPE_ROOM, roomName, preciseLocationId, SUB_LOCATION_TYPE_FLOOR,
                Long.valueOf(locationId), LOCATION_TYPE_BUILDING);
    }
    
    private Long createFloor(String floorName, String preciseLocationId) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.createSubLocation(SUB_LOCATION_TYPE_FLOOR, floorName, Long.parseLong(preciseLocationId),
                LOCATION_TYPE_BUILDING, Long.valueOf(locationId), LOCATION_TYPE_BUILDING);
    }
    
    private void createDevice(Long locationId) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        Long deviceModelId = resourceCatalogClient.getModelIds(DEVICE_MODEL);
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        physicalInventoryRepository.createDevice(SUB_LOCATION_TYPE_FLOOR, locationId, deviceModelId, DEVICE_NAME,
                DEVICE_MODEL_TYPE);
    }
    
    private TreeComponent.Node getNode(String nodePath) {
        return hierarchyViewPage.getNodeByLabelPath(nodePath);
    }
    
}
