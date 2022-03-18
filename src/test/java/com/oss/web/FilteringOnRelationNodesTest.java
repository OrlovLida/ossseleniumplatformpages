/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2021 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.web;

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
import com.oss.untils.Environment;

/**
 * @author Gabriela Zaranek
 */
public class FilteringOnRelationNodesTest extends BaseTestCase {
    private static final String DEVICE_NAME = "FORN-123-456";
    private static final String PORT_NAME_10 = "10";
    private static final String PORT_NAME_02 = "02";
    private static final String EXPAND_CLEAR_FILTERS_BUTTON = "Expand & Clear Filters";
    private Environment env = Environment.getInstance();
    private static final Logger log = LoggerFactory.getLogger(TreeWidgetTest.class);
    private static final String LOCATION_NAME = "FilteringOnRelationNodesTest";
    private static final String LOCATION_TYPE_BUILDING = "Building";
    private static final String NAME_ATTRIBUTE_ID = "name";
    private static final String SUB_LOCATION_TYPE_ROOM = "Room";
    private static final String ROOM_NAME = "RM_ST1";
    private static final String ROOM_NAME_2 = "RM_ST2";
    private static final String FLOOR_NAME_2 = "FL_ST2";
    private static final String SUB_LOCATION_TYPE_FLOOR = "Floor";
    private static final String FLOOR_NAME = "FL_ST1";
    private static final String ROOM_PATH = LOCATION_NAME + ".Locations." + SUB_LOCATION_TYPE_FLOOR + "." + FLOOR_NAME + ".Locations."
            + SUB_LOCATION_TYPE_ROOM + "." + ROOM_NAME;
    
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
        TreeComponent.Node node = hierarchyViewPage.getNodeByLabelPath(LOCATION_NAME + ".Locations");
        AdvancedSearch advancedSearch = node.openAdvancedSearch();
        advancedSearch.setFilter(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, FLOOR_NAME_2);
        advancedSearch.clickApply();
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME_2).doesNotContain(FLOOR_NAME);
    }
    
    @Test(priority = 2)
    public void editFilter() {
        TreeComponent.Node node = hierarchyViewPage.getNodeByLabelPath(LOCATION_NAME + ".Locations");
        AdvancedSearch advancedSearch = node.openAdvancedSearch();
        advancedSearch.setFilter(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, FLOOR_NAME);
        advancedSearch.clickApply();
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).doesNotContain(FLOOR_NAME_2);

    }
    
    @Test(priority = 3)
    public void clearFilter() {
        TreeComponent.Node node = hierarchyViewPage.getNodeByLabelPath(LOCATION_NAME + ".Locations");
        clearFilter(node);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).contains(FLOOR_NAME_2);
    }
    
    @Test(priority = 4)
    public void noDataResults() {
        TreeComponent.Node node = hierarchyViewPage.getNodeByLabelPath(LOCATION_NAME + ".Locations");
        AdvancedSearch advancedSearch = node.openAdvancedSearch();
        advancedSearch.setFilter(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, "sdfad");
        advancedSearch.clickApply();
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).doesNotContain(FLOOR_NAME).doesNotContain(FLOOR_NAME_2);
        clearFilter(node);
    }

    @Test(priority = 5)
    public void filterOnRelationsAndExpandNextLevel() {
        hierarchyViewPage.getFirstNode().expandNextLevel();
        TreeComponent.Node node = hierarchyViewPage.getNodeByLabelPath(PATH_1ST_LOCATIONS_RELATION);
        node.searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, FLOOR_NAME);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).doesNotContain(FLOOR_NAME_2);
        hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        Popup.create(driver,webDriverWait).clickButtonByLabel(EXPAND_CLEAR_FILTERS_BUTTON);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).contains(FLOOR_NAME_2);
    }


    @Test(priority = 6)
    public void filterOnMoreRelations() {
        TreeComponent.Node location1stLevel = hierarchyViewPage.getNodeByLabelPath(LOCATION_NAME + ".Locations");
        AdvancedSearch advancedSearch = location1stLevel.openAdvancedSearch();
        advancedSearch.setFilter(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, FLOOR_NAME);
        advancedSearch.clickApply();
        
        TreeComponent.Node location2stLevel = hierarchyViewPage
                .getNodeByLabelPath(LOCATION_NAME + ".Locations." + SUB_LOCATION_TYPE_FLOOR + "." + FLOOR_NAME + ".Locations");
        advancedSearch = location2stLevel.openAdvancedSearch();
        advancedSearch.setFilter(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, ROOM_NAME);
        advancedSearch.clickApply();
        Optional<TreeComponent.Node> roomNode = hierarchyViewPage.getMainTree().findNodeByLabelsPath(ROOM_PATH);
        Assertions.assertThat(roomNode).isPresent();
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).doesNotContain(FLOOR_NAME_2)
                .contains(ROOM_NAME)
                .doesNotContain(ROOM_NAME_2);
        
        clearFilter(location2stLevel);
        clearFilter(location1stLevel);
    }
    
    @Test(priority = 7)
    public void filterOnRelationsAtDifferentLevels() {
        String nodePathLabel = LOCATION_NAME + ".Locations." + SUB_LOCATION_TYPE_FLOOR + "." + FLOOR_NAME + ".Locations";
        hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        TreeComponent.Node location2stLevel = hierarchyViewPage
                .getNodeByLabelPath(nodePathLabel);
        AdvancedSearch advancedSearch = location2stLevel.openAdvancedSearch();
        advancedSearch.setFilter(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, ROOM_NAME);
        advancedSearch.clickApply();
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).doesNotContain(ROOM_NAME_2);
        
        TreeComponent.Node location1stLevel = hierarchyViewPage.getNodeByLabelPath(LOCATION_NAME + ".Locations");
        advancedSearch = location1stLevel.openAdvancedSearch();
        advancedSearch.setFilter(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, FLOOR_NAME_2);
        advancedSearch.clickApply();
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME_2).doesNotContain(FLOOR_NAME);
        
        advancedSearch = location1stLevel.openAdvancedSearch();
        advancedSearch.setFilter(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, FLOOR_NAME);
        advancedSearch.clickApply();
        Optional<TreeComponent.Node> roomNode = hierarchyViewPage.getMainTree().findNodeByLabelsPath(ROOM_PATH);
        Assertions.assertThat(roomNode).isPresent();
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(FLOOR_NAME).doesNotContain(FLOOR_NAME_2)
                .contains(ROOM_NAME).doesNotContain(ROOM_NAME_2);
        
        clearFilter(hierarchyViewPage.getNodeByLabelPath(nodePathLabel));
        clearFilter(location1stLevel);
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
        createFloor(FLOOR_NAME_2, locationId);
        createRoom(ROOM_NAME, floorId);
        createRoom(ROOM_NAME_2, floorId);
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

}
