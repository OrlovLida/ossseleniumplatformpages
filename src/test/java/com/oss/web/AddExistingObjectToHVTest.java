/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.web;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.untils.Environment;
import com.oss.untils.FakeGenerator;

import io.qameta.allure.Description;

/**
 * @author Gabriela Zaranek
 */
public class AddExistingObjectToHVTest extends BaseTestCase {
    private static final String ADD_OBJECT_BUTTON = "addRootNode";
    private static final String LOCATION_TYPE = "Location";
    private static final String BUILDING_NAME_1 = FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String BUILDING_NAME_2 = FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String LOCATION_RELATION_PATH = BUILDING_NAME_2 + ".Locations";
    private static final String ROOM_RELATION_PATH = BUILDING_NAME_2 + ".Locations.Room";
    private static final String BUILDING_NAME_3 = FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String BUILDING_NAME_4 = FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String ROOM_NAME_1 = FakeGenerator.getIdNumber();
    private static final String ROOM_NAME_2 = FakeGenerator.getIdNumber();
    private static final String ADVANCED_SEARCH_ID = "advancedSearch";
    private static final String BUILDING_TYPE = "Building";
    private static final String ROOM_TYPE = "Room";
    private static final String NAME_ATTRIBUTE_ID = "name";
    private static final String TYPE_ATTRIBUTE_ID = "type";
    private static final String SELENIUM_VALUE = "Selenium";
    private static final String RESOURCE_INVENTORY = "Resource Inventory ";
    private static final String HIERARCHY_VIEW = "Hierarchy View";
    private static final String CHECKBOX_ROW_ID = "cell-row-0-col-checkbox";
    private Environment env = Environment.getInstance();
    private String buildingId1;
    private String buildingId2;
    private String buildingId3;
    private String buildingId4;
    private HierarchyViewPage hierarchyView;
    
    @BeforeClass
    public void openHierarchyView() {
        createLocations();
        hierarchyView = HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, LOCATION_TYPE, buildingId2);
        
    }
    
    private void createLocations() {
        buildingId1 = createBuilding(BUILDING_NAME_1);
        buildingId2 = createBuilding(BUILDING_NAME_2);
//        buildingId3 = createBuilding(BUILDING_NAME_3);
//        buildingId4 = createBuilding(BUILDING_NAME_4);
        createRoom(buildingId2, ROOM_NAME_1);
        createRoom(buildingId2, ROOM_NAME_2);
    }
    
    @Test
    public void addObjects() {
        hierarchyView.getMainTree().callActionById(ADD_OBJECT_BUTTON);
        addExistingObject(BUILDING_NAME_1);
        List<String> visibleNodesLabel = hierarchyView.getVisibleNodesLabel();
        Assertions.assertThat(visibleNodesLabel).contains(BUILDING_NAME_1);
        
        hierarchyView.getMainTree().callActionById(ADD_OBJECT_BUTTON);
        AdvancedSearchWidget advancedSearch = setFilter(BUILDING_NAME_1);
        DelayUtils.sleep();
        Input.MouseCursor cursor =
                ComponentFactory.create(CHECKBOX_ROW_ID, Input.ComponentType.COMBOBOX, driver, webDriverWait).cursor();
        DelayUtils.sleep();
        advancedSearch.clickCancel();
        Assertions.assertThat(cursor).isEqualTo(Input.MouseCursor.NOT_ALLOWED);
    }
    
    @Test
    public void addSubtypeObjectOfRoot() {
        hierarchyView.getMainTree().callActionById(ADD_OBJECT_BUTTON);
        addExistingObject(ROOM_NAME_1);
        List<String> visibleNodesLabel = hierarchyView.getVisibleNodesLabel();
        Assertions.assertThat(visibleNodesLabel).contains(ROOM_NAME_1);
    }
    
    @Test
    public void expandNodeAndAddObject() {
        TreeComponent.Node roomRelation = hierarchyView.getNodeByLabelPath(ROOM_RELATION_PATH);
        roomRelation.expandNode();
        List<String> visibleNodesLabel = hierarchyView.getVisibleNodesLabel();
        Assertions.assertThat(visibleNodesLabel).contains(ROOM_NAME_1, ROOM_NAME_2);
        
        hierarchyView.getMainTree().callActionById(ADD_OBJECT_BUTTON);
        addExistingObject(BUILDING_NAME_3);
        hierarchyView.getVisibleNodesLabel();
        Assertions.assertThat(hierarchyView.getVisibleNodesLabel()).contains(ROOM_NAME_1, ROOM_NAME_2, BUILDING_NAME_3);
    }
    
    @Test
    public void setFilterAndAddObject() {
        hierarchyView.getNodeByLabelPath(LOCATION_RELATION_PATH).searchByAttribute(NAME_ATTRIBUTE_ID, ROOM_NAME_1);
        hierarchyView.getMainTree().callActionById(ADD_OBJECT_BUTTON);
        addExistingObject(BUILDING_NAME_4);
        
        TreeComponent.Node roomRelation = hierarchyView.getNodeByLabelPath(ROOM_RELATION_PATH);
        roomRelation.expandNode();
        Assertions.assertThat(hierarchyView.getVisibleNodesLabel()).contains(ROOM_NAME_1, BUILDING_NAME_4).doesNotContain(ROOM_NAME_2);
    }
    
    @Test
    public void addMoreThan500Objects() {
        hierarchyView.getMainTree().callActionById(ADD_OBJECT_BUTTON);
        AdvancedSearchWidget advancedSearch = AdvancedSearchWidget.createById(driver, webDriverWait, ADVANCED_SEARCH_ID);
        TableComponent tableComponent = advancedSearch.getTableComponent();
        tableComponent.getPaginationComponent().changeRowsCount(500);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tableComponent.selectAll();
        advancedSearch.clickAdd();
        List<SystemMessageContainer.Message> errors = SystemMessageContainer.create(driver, webDriverWait).getMessages();
        advancedSearch.clickCancel();
        Assertions.assertThat(errors.size()).isEqualTo(1);
        Assertions.assertThat(errors.get(0).getText()).isEqualTo("The maximum number of objects is 500.");
    }
    
    @Test
    public void checkIsAddObjectButtonIsUnavailableForSelectedObject() {
        hierarchyView.selectFirstObject();
        Assertions.assertThat(CSSUtils.isElementPresent(driver, ADD_OBJECT_BUTTON)).isFalse();
        hierarchyView.getMainTree().unselectAllNodes();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assertions.assertThat(CSSUtils.isElementPresent(driver, ADD_OBJECT_BUTTON)).isTrue();
    }
    
    @Test
    public void checkIsAddObjectButtonIsUnavailableWithHVQuery() {
        homePage.chooseFromLeftSideMenu(HIERARCHY_VIEW, RESOURCE_INVENTORY);
        SearchObjectTypePage searchObjectType = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectType.searchType(LOCATION_TYPE);
        AdvancedSearchWidget advancedSearch = setFilter(SELENIUM_VALUE);
        DelayUtils.sleep();
        advancedSearch.clickAdd();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assertions.assertThat(CSSUtils.isElementPresent(driver, ADD_OBJECT_BUTTON)).isFalse();
        
    }
    
    @Test
    @Description("Hierarchy View is opened for Location")
    public void addObjectToHierarchyViewOpenedFromOtherView() {
        NewInventoryViewPage testPerson = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, LOCATION_TYPE);
        testPerson.searchByAttributeValue(TYPE_ATTRIBUTE_ID, ROOM_TYPE, Input.ComponentType.MULTI_COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        testPerson.selectFirstRow();
        testPerson.goToHierarchyViewForSelectedObject();
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        hierarchyViewPage.getMainTree().callActionById(ADD_OBJECT_BUTTON);
        AdvancedSearchWidget advancedSearch = AdvancedSearchWidget.createById(driver, webDriverWait, ADVANCED_SEARCH_ID);
        DelayUtils.sleep();
        TableComponent tableComponent = advancedSearch.getTableComponent();
        tableComponent.selectRow(3);
        String type = tableComponent.getCellValue(3, TYPE_ATTRIBUTE_ID);
        String roomName = tableComponent.getCellValue(3, NAME_ATTRIBUTE_ID);
        advancedSearch.clickAdd();
        Assertions.assertThat(hierarchyView.getVisibleNodesLabel()).contains(roomName);
        Assertions.assertThat(hierarchyView.getVisibleNodesLabel().size()).isEqualTo(2);
        Assertions.assertThat(type).isEqualTo(ROOM_TYPE);
        
    }
    
    @AfterClass
    private void deleteBuildings() {
        deleteBuilding(buildingId1);
        deleteBuilding(buildingId2);
//        deleteBuilding(buildingId3);
//        deleteBuilding(buildingId4);
    }
    
    private void addExistingObject(String objectName) {
        AdvancedSearchWidget advancedSearch = setFilter(objectName);
        DelayUtils.sleep();
        TableComponent tableComponent = advancedSearch.getTableComponent();
        tableComponent.selectRow(0);
        advancedSearch.clickAdd();
    }
    
    private AdvancedSearchWidget setFilter(String objectName) {
        AdvancedSearchWidget advancedSearch = AdvancedSearchWidget.createById(driver, webDriverWait, ADVANCED_SEARCH_ID);
        advancedSearch.setFilter(NAME_ATTRIBUTE_ID, objectName);
        return advancedSearch;
    }
    
    private Long getGeographicalAddress() {
        AddressRepository addressRepository = new AddressRepository(env);
        return addressRepository.getFirstGeographicalAddressId();
    }
    
    private String createBuilding(String buildingName) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.createLocation(buildingName, BUILDING_TYPE, getGeographicalAddress());
        
    }
    
    private Long createRoom(String locationId, String roomName) {
        long locationIdL = Long.parseLong(locationId);
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.createSubLocation(ROOM_TYPE, roomName, locationIdL, BUILDING_TYPE, locationIdL, BUILDING_TYPE);
        
    }
    
    private void deleteBuilding(String locationId) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationInventoryRepository.deleteLocation(Long.valueOf(locationId), "Building");
    }
    
}
