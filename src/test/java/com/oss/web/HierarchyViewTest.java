package com.oss.web;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.tree.TreeComponent.Node;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.untils.Environment;

public class HierarchyViewTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(HierarchyViewTest.class);
    private static final String REFRESH_TREE = "tree_gql_refresh";
    private Environment env = Environment.getInstance();
    private static final String SUB_LOCATION_TYPE_ROOM = "Room";
    private static final String ROOM_NAME = "Room_RM_ST1";
    private static final String ROOM_NAME_2_UPDATED = "Room_" + LocalDate.now() + "_Update";
    private static final String SUB_LOCATION_TYPE_FLOOR = "Floor";
    private static final String FLOOR_NAME = "Floor_FL_ST1";
    private static final String SUB_LOCATION_TYPE_ROW = "Row";
    private static final String ROW_NAME = "rh01";
    private static final String REFRESH_INLINE = "tree_gql_refresh_relation";
    private static final String ROOM_NAME_2 = "Room_" + LocalDate.now();
    private static final String NAME_ATTRIBUTE_ID = "name";
    private static final String LOCATION_NAME = "SeleniumTest-Building-OSSWEB";
    private static final String LOCATION_TYPE_BUILDING = "Building";
    private HierarchyViewPage hierarchyViewPage;
    private String locationId;
    private Long roomId_2;
    
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
    
    private void createLocationsForTest() {
        Long firstGeographicalAddressId = getGeographicalAddress();
        locationId = createBuilding(firstGeographicalAddressId);
        Long floorId = createFloor(FLOOR_NAME, locationId, LOCATION_TYPE_BUILDING);
        Long roomId = createRoom(ROOM_NAME, floorId, SUB_LOCATION_TYPE_FLOOR);
        Long rowId = createRow(ROW_NAME, roomId, SUB_LOCATION_TYPE_ROOM);
    }
    
    @Test(priority = 1)
    public void selectFirstNode() {
        hierarchyViewPage.selectFirstObject();
        Assertions.assertThat(hierarchyViewPage
                .getFirstNode().isToggled()).isTrue();
    }
    
    @Test(priority = 2)
    public void unselectFirstNode() {
        hierarchyViewPage.unselectFirstObject();
        Assertions.assertThat(hierarchyViewPage
                .getFirstNode().isToggled()).isFalse();
    }
    
    @Test(priority = 3)
    public void expandNode() {
        hierarchyViewPage.expandTreeNode(hierarchyViewPage.getFirstNode().getLabel());
        Assertions.assertThat(hierarchyViewPage.getFirstNode().isExpanded()).isTrue();
    }
    
    @Test(priority = 4)
    public void collapseNode() {
        hierarchyViewPage.getFirstNode().collapseNode();
        Assertions.assertThat(hierarchyViewPage.getFirstNode().isExpanded()).isFalse();
    }
    
    @Test(priority = 5)
    public void expandNextLevel() {
        hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        List<Node> expandedNodes =
                hierarchyViewPage.getMainTree().getVisibleNodes().stream().filter(Node::isExpanded).collect(Collectors.toList());
        Assertions.assertThat(expandedNodes.size()).isEqualTo(6);
        Node nodeRoom = hierarchyViewPage.getMainTree()
                .getNodeByLabelsPath(LOCATION_NAME + ".Locations." + SUB_LOCATION_TYPE_FLOOR + "." + FLOOR_NAME
                        + ".Locations." + SUB_LOCATION_TYPE_ROOM + "." + ROOM_NAME);
        Assertions.assertThat(nodeRoom.isExpanded()).isFalse();
    }
    
    @Test(priority = 6)
    public void refreshRelation() {
        roomId_2 = createRoom(ROOM_NAME_2, Long.valueOf(locationId), LOCATION_TYPE_BUILDING);
        hierarchyViewPage.getMainTree()
                .getNodeByLabelsPath(LOCATION_NAME + ".Locations")
                .callAction(REFRESH_INLINE);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(SUB_LOCATION_TYPE_ROOM);
        hierarchyViewPage.getMainTree().expandNodeWithLabel(SUB_LOCATION_TYPE_ROOM);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(ROOM_NAME_2);
    }
    
    @Test(priority = 7)
    public void refreshTreeWidget() {
        hierarchyViewPage.getMainTree().callActionById(ActionsContainer.KEBAB_GROUP_ID, REFRESH_TREE);
        hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        updateRoom(roomId_2, ROOM_NAME_2_UPDATED);
        hierarchyViewPage.getMainTree().callActionById(ActionsContainer.KEBAB_GROUP_ID, REFRESH_TREE);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(ROOM_NAME_2_UPDATED);
        
    }
    
    @Test(priority = 8)
    public void searchWithNotExistingData() {
        hierarchyViewPage.clearFiltersOnMainTree();
        hierarchyViewPage.searchObject("hjakserzxaseer");
        List<Node> nodes = hierarchyViewPage.getMainTree().getVisibleNodes();
        
        Assertions.assertThat(nodes).isEmpty();
        hierarchyViewPage.clearFiltersOnMainTree();
    }
    
    @Test(priority = 9)
    public void searchWithExistingData() {
        Node node = hierarchyViewPage.getFirstNode();
        String label = node.getLabel();
        
        hierarchyViewPage.searchObject(label);
        List<Node> nodes = hierarchyViewPage.getMainTree().getVisibleNodes();
        
        Assertions.assertThat(nodes).hasSize(1);
        Assertions.assertThat(nodes.get(0).getLabel()).isEqualTo(label);
        hierarchyViewPage.clearFiltersOnMainTree();
    }
    
    @Test(priority = 10)
    public void showOnHierarchyView() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.getFirstNode().callAction(ActionsContainer.SHOW_ON_GROUP_ID,
                HierarchyViewPage.OPEN_HIERARCHY_VIEW_CONTEXT_ACTION_ID);
        List<Node> nodes = hierarchyViewPage.getMainTree().getVisibleNodes();
        Assertions.assertThat(nodes).hasSize(1);
    }
    
    @AfterClass
    private void deleteSubLocation() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationInventoryRepository.deleteSubLocation(roomId_2.toString());
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
    
    private Long createFloor(String floorName, String preciseLocationId, String preciseLocationType) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.createSubLocation(SUB_LOCATION_TYPE_FLOOR, floorName, Long.parseLong(preciseLocationId),
                preciseLocationType,
                Long.valueOf(locationId), LOCATION_TYPE_BUILDING);
    }
    
    private Long createRow(String rowName, Long preciseLocationId, String preciseLocationType) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.createSubLocation(SUB_LOCATION_TYPE_ROW, rowName, preciseLocationId, preciseLocationType,
                Long.valueOf(locationId), LOCATION_TYPE_BUILDING);
    }
    
    private void updateRoom(Long id, String roomName) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationInventoryRepository.updateSubLocation(id, SUB_LOCATION_TYPE_ROOM, roomName, Long.valueOf(locationId),
                LOCATION_TYPE_BUILDING, Long.valueOf(locationId), LOCATION_TYPE_BUILDING);
    }
    
    private Long getGeographicalAddress() {
        AddressRepository addressRepository = new AddressRepository(env);
        return addressRepository.getFirstGeographicalAddressId();
    }
}