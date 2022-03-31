package com.oss.web;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.tree.TreeComponent.Node;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.CreatePluggableModuleWizardPage;
import com.oss.pages.physical.SublocationWizardPage;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.services.ResourceCatalogClient;
import com.oss.untils.Environment;
import com.oss.untils.FakeGenerator;

public class TreeWidgetTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(TreeWidgetTest.class);
    private static final String REFRESH_TREE = "tree_gql_refresh";
    private static final String CREATE_SUBLOCATION_ACTION = "CreateSublocationInLocationWizardAction";
    private static final String UPDATE_SUBLOCATION_ACTION = "UpdateSublocationWizardAction";
    private static final String REMOVE_SUBLOCATION_ACTION = "RemoveSublocationWizardAction";
    private static final String CONFIRM_DELETE_BUTTON = "Delete";
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
    private static final String PATH_RELATION_LOCATIONS = LOCATION_NAME + ".Locations";
    private static final String PATH_ROOM_1 = PATH_RELATION_LOCATIONS + "." + SUB_LOCATION_TYPE_FLOOR + "." + FLOOR_NAME
            + ".Locations." + SUB_LOCATION_TYPE_ROOM + "." + ROOM_NAME;
    private static final String LOCATION_TYPE_BUILDING = "Building";
    private static final String ROOM_3_CREATE = "Room_" + FakeGenerator.getIdNumber();
    private static final String PATH_ROOM_3 = PATH_RELATION_LOCATIONS + "." + SUB_LOCATION_TYPE_ROOM + "." + ROOM_3_CREATE;
    private static final String ROOM_3_UPDATE = FakeGenerator.getLocation(FakeGenerator.FilmTitle.LORD_OF_THE_RING);
    private static final String PATH_ROOM_3_UPDATE = PATH_RELATION_LOCATIONS + "." + SUB_LOCATION_TYPE_ROOM + "." + ROOM_3_UPDATE;
    private static final String DEVICE_NAME = FakeGenerator.getIdNumber();
    private static final String PORT_01_PATH = LOCATION_NAME + ".Hardware.Switch." + DEVICE_NAME + ".Ports.01";
    private static final String PORT_02_PATH = LOCATION_NAME + ".Hardware.Switch." + DEVICE_NAME + ".Ports.02";
    private static final String DEVICE_MODEL_TYPE = "IPDeviceModel";
    private static final String PLUGGABLE_MODULE_01_PATH =
            PORT_01_PATH + "." + "Pluggable Module Slot.Slot.Pluggable Module.10GBASE-ER SFP+";
    private static final String PLUGGABLE_MODULE_02_PATH =
            PORT_02_PATH + "." + "Pluggable Module Slot.Slot.Pluggable Module.10GBASE-ER SFP+";
    private static final String CREATE_PM_ACTION = "CreatePluggableModuleOnPortAction";
    private static final String DEVICE_MODEL = "N9K-C9396PX";
    private static final String PLUGGABLE_MODULE_MODEL = "Generic 10GBASE-ER SFP+";
    private static final String PATH_LOCATIONS_RELATIONS_3TH_LEVEL = PATH_ROOM_1 + ".Locations";
    private static final String PATH_ROW = PATH_LOCATIONS_RELATIONS_3TH_LEVEL + ".Row.rh01";
    private static final String ROOM_NAME_4 = "Room_" + FakeGenerator.getIdNumber();
    private static final String PATH_ROOM_4 = PATH_RELATION_LOCATIONS + "." + SUB_LOCATION_TYPE_FLOOR + "." + FLOOR_NAME
            + ".Locations." + SUB_LOCATION_TYPE_ROOM + "." + ROOM_NAME_4;
    private Environment env = Environment.getInstance();
    private HierarchyViewPage hierarchyViewPage;
    private String locationId;
    private Long roomId_2;
    private Long roomId_4;
    private Long deviceId;
    
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
        Long floorId = createFloor(locationId);
        Long roomId = createRoom(ROOM_NAME, floorId, SUB_LOCATION_TYPE_FLOOR);
        createRow(roomId);
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
        Node nodeRoom = hierarchyViewPage.getMainTree()
                .getNodeByLabelsPath(PATH_ROOM_1);
        Assertions.assertThat(nodeRoom.isExpanded()).isFalse();
    }
    
    @Test(priority = 6)
    public void refreshRelation() {
        roomId_2 = createRoom(ROOM_NAME_2, Long.valueOf(locationId), LOCATION_TYPE_BUILDING);
        hierarchyViewPage.getMainTree()
                .getNodeByLabelsPath(PATH_RELATION_LOCATIONS).callAction(REFRESH_INLINE);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(SUB_LOCATION_TYPE_ROOM);
        hierarchyViewPage.getMainTree().expandNodeWithLabel(SUB_LOCATION_TYPE_ROOM);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(ROOM_NAME_2);
    }
    
    @Test(priority = 7)
    public void refreshTreeWidget() {
        hierarchyViewPage.getMainTree().callActionById(ActionsContainer.KEBAB_GROUP_ID, REFRESH_TREE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        updateRoom(roomId_2, ROOM_NAME_2_UPDATED);
        hierarchyViewPage.getMainTree().callActionById(ActionsContainer.KEBAB_GROUP_ID, REFRESH_TREE);
        hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        Assertions.assertThat(hierarchyViewPage.getVisibleNodesLabel()).contains(ROOM_NAME_2_UPDATED);
        hierarchyViewPage.getNodeByLabelPath(PATH_ROW);
        hierarchyViewPage.getNodeByLabelPath(PATH_LOCATIONS_RELATIONS_3TH_LEVEL).callAction(REFRESH_INLINE);
        roomId_4 = createRoom(ROOM_NAME_4, getFloorId(), SUB_LOCATION_TYPE_FLOOR);
        hierarchyViewPage.getMainTree().callActionById(ActionsContainer.KEBAB_GROUP_ID, REFRESH_TREE);
        Assertions.assertThat(hierarchyViewPage.isNodePresent(PATH_ROOM_4)).isTrue();
    }
    
    @Test(priority = 8)
    public void createRoom() {
        hierarchyViewPage.selectNodeByLabel(LOCATION_NAME);
        hierarchyViewPage.getMainTree().callActionById(ActionsContainer.CREATE_GROUP_ID, CREATE_SUBLOCATION_ACTION);
        SublocationWizardPage sublocation = new SublocationWizardPage(driver);
        sublocation.setSublocationType(SUB_LOCATION_TYPE_ROOM);
        sublocation.setSublocationName(ROOM_3_CREATE);
        sublocation.setPreciseLocation(LOCATION_NAME);
        DelayUtils.sleep(5000);
        sublocation.clickNext();
        sublocation.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.unselectFirstObject();
        hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        List<String> nodes = hierarchyViewPage.getVisibleNodesLabel();
        Assertions.assertThat(nodes).contains(ROOM_3_CREATE);
        
    }
    
    @Test(priority = 9, enabled = false)
    public void updateRoom() {
        hierarchyViewPage.unselectFirstObject();
        hierarchyViewPage.selectNodeByLabelsPath(PATH_ROOM_3);
        hierarchyViewPage.getMainTree().callActionById(ActionsContainer.EDIT_GROUP_ID, UPDATE_SUBLOCATION_ACTION);
        SublocationWizardPage sublocation = new SublocationWizardPage(driver);
        sublocation.setSublocationName(ROOM_3_UPDATE);
        sublocation.clickAccept();
        List<String> nodes = hierarchyViewPage.getVisibleNodesLabel();
        Assertions.assertThat(nodes).contains(ROOM_3_UPDATE);
    }
    
    @Test(priority = 10)
    public void deleteRoom() {
        hierarchyViewPage.selectNodeByLabelsPath(PATH_ROOM_3);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.getMainTree().callActionById(ActionsContainer.EDIT_GROUP_ID, REMOVE_SUBLOCATION_ACTION);
        ConfirmationBox.create(driver, webDriverWait).clickButtonByLabel(CONFIRM_DELETE_BUTTON);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        List<String> nodes = hierarchyViewPage.getVisibleNodesLabel();
        Assertions.assertThat(nodes).doesNotContain(ROOM_3_UPDATE).isNotEmpty();
    }
    
    @Test(priority = 11)
    public void multiPluggableModuleCreation() {
        createDevice();
        driver.navigate().refresh();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.getMainTree().searchByAttribute(NAME_ATTRIBUTE_ID, Input.ComponentType.TEXT_FIELD, LOCATION_NAME);
        hierarchyViewPage.expandNextLevel(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.selectNodeByLabelsPath(PORT_01_PATH);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.selectNodeByLabelsPath(PORT_02_PATH);
        hierarchyViewPage.getMainTree().callActionById(ActionsContainer.CREATE_GROUP_ID, CREATE_PM_ACTION);
        CreatePluggableModuleWizardPage pmWizard = new CreatePluggableModuleWizardPage(driver);
        pmWizard.setModel(PLUGGABLE_MODULE_MODEL);
        pmWizard.accept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.getMainTree().unselectAllNodes();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assertions.assertThat(hierarchyViewPage.isNodePresent(PLUGGABLE_MODULE_01_PATH)).isTrue();
        Assertions.assertThat(hierarchyViewPage.isNodePresent(PLUGGABLE_MODULE_02_PATH)).isTrue();
        
    }
    
    @Test(priority = 12)
    public void searchWithNotExistingData() {
        hierarchyViewPage.clearFiltersOnMainTree();
        hierarchyViewPage.searchObject("hjakserzxaseer");
        List<Node> nodes = hierarchyViewPage.getMainTree().getVisibleNodes();
        
        Assertions.assertThat(nodes).isEmpty();
        hierarchyViewPage.clearFiltersOnMainTree();
    }
    
    @Test(priority = 13)
    public void searchWithExistingData() {
        Node node = hierarchyViewPage.getFirstNode();
        String label = node.getLabel();
        
        hierarchyViewPage.searchObject(label);
        List<Node> nodes = hierarchyViewPage.getMainTree().getVisibleNodes();
        
        Assertions.assertThat(nodes).hasSize(1);
        Assertions.assertThat(nodes.get(0).getLabel()).isEqualTo(label);
        hierarchyViewPage.clearFiltersOnMainTree();
    }
    
    @Test(priority = 14)
    public void showOnHierarchyView() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.getFirstNode().callAction(ActionsContainer.SHOW_ON_GROUP_ID,
                HierarchyViewPage.OPEN_HIERARCHY_VIEW_CONTEXT_ACTION_ID);
        List<Node> nodes = hierarchyViewPage.getMainTree().getVisibleNodes();
        Assertions.assertThat(nodes).hasSize(1);
    }
    
    @AfterClass
    private void deleteObjects() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationInventoryRepository.deleteSubLocation(roomId_2.toString());
        locationInventoryRepository.deleteSubLocation(roomId_4.toString());
        deleteDevice();
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
    
    private Long createFloor(String preciseLocationId) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.createSubLocation(SUB_LOCATION_TYPE_FLOOR, FLOOR_NAME, Long.parseLong(preciseLocationId),
                LOCATION_TYPE_BUILDING,
                Long.valueOf(locationId), LOCATION_TYPE_BUILDING);
    }
    
    private Long createRow(Long preciseLocationId) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.createSubLocation(SUB_LOCATION_TYPE_ROW, ROW_NAME, preciseLocationId, SUB_LOCATION_TYPE_ROOM,
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
    
    private void createDevice() {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        Long deviceModelId = resourceCatalogClient.getModelIds(DEVICE_MODEL);
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        deviceId = physicalInventoryRepository.createDevice(LOCATION_TYPE_BUILDING, Long.valueOf(locationId), deviceModelId, DEVICE_NAME,
                DEVICE_MODEL_TYPE);
    }
    
    private void deleteDevice() {
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        physicalInventoryRepository.deleteDevice(Long.toString(deviceId));
    }
    
    private Long getFloorId() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.getSublocationId(locationId, FLOOR_NAME);
    }
}
