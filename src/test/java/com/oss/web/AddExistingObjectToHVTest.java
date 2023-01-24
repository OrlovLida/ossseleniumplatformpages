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
import org.testng.annotations.AfterMethod;
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
import com.comarch.oss.web.pages.HierarchyViewPage;
import com.comarch.oss.web.pages.NewInventoryViewPage;
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
    private static final String BUILDING_NAME_2 = "1_" + FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String LOCATION_RELATION_PATH = BUILDING_NAME_2 + ".Locations";
    private static final String ROOM_RELATION_PATH = BUILDING_NAME_2 + ".Locations.Room";
    private static final String BUILDING_NAME_3 = FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String BUILDING_NAME_4 = FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String BUILDING_NAME_5 = "Z_" + FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String BUILDING_NAME_6 = "0_" + FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String BUILDING_NAME_7 = "Z1_" + FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String BUILDING_NAME_8 = "Z2_" + FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String BUILDING_NAME_9 = "Z3_" + FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String ROOM_NAME_1 = "2_" + FakeGenerator.getIdNumber();
    private static final String ROOM_NAME_2 = FakeGenerator.getIdNumber();
    private static final String ROOM_NAME_3 = FakeGenerator.getIdNumber();
    private static final String ADVANCED_SEARCH_ID = "advancedSearch";
    private static final String BUILDING_TYPE = "Building";
    private static final String ROOM_TYPE = "Room";
    private static final String NAME_ATTRIBUTE_ID = "name";
    private static final String TYPE_ATTRIBUTE_ID = "type";
    private static final String CHECKBOX_ROW_ID = "cell-row-0-col-checkbox";
    private static final String THE_MAXIMUM_NUMBER_OF_OBJECTS_IS_500_ERROR_MESSAGE = "The maximum number of all existing and new selected objects is 500";
    private static final String HV_URL_WITH_QUERY = "%s/#/views/management/views/hierarchy-view/Location?query=name=match='Selenium'&perspective=LIVE";

    private final Environment env = Environment.getInstance();
    private String buildingId1;
    private String buildingId2;
    private String buildingId3;
    private String buildingId4;
    private String buildingId5;
    private String buildingId6;
    private HierarchyViewPage hierarchyView;
    private String buildingId7;
    private String buildingId8;
    private String buildingId9;

    @BeforeClass
    public void openHierarchyView() {
        createLocations();
        hierarchyView = HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, LOCATION_TYPE, buildingId2);
    }

    private void createLocations() {
        buildingId1 = createBuilding(BUILDING_NAME_1);
        buildingId2 = createBuilding(BUILDING_NAME_2);
        buildingId3 = createBuilding(BUILDING_NAME_3);
        buildingId4 = createBuilding(BUILDING_NAME_4);
        buildingId6 = createBuilding(BUILDING_NAME_6);
        createRoom(buildingId2, ROOM_NAME_1);
        createRoom(buildingId2, ROOM_NAME_2);
        createRoom(buildingId6, ROOM_NAME_3);
    }

    @Test(priority = 1)
    public void addObjects() {
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

    @Test(priority = 2)
    public void addSubtypeObjectOfRoot() {
        addExistingObject(ROOM_NAME_1);
        List<String> visibleNodesLabel = hierarchyView.getVisibleNodesLabel();
        Assertions.assertThat(visibleNodesLabel).contains(ROOM_NAME_1);
    }

    @Test(priority = 3)
    public void expandNodeAndAddObject() {
        TreeComponent.Node roomRelation = hierarchyView.getNodeByLabelPath(ROOM_RELATION_PATH);
        roomRelation.expandNode();
        List<String> visibleNodesLabel = hierarchyView.getVisibleNodesLabel();
        Assertions.assertThat(visibleNodesLabel).contains(ROOM_NAME_1, ROOM_NAME_2);

        addExistingObject(BUILDING_NAME_3);
        hierarchyView.getVisibleNodesLabel();
        Assertions.assertThat(hierarchyView.getVisibleNodesLabel()).contains(ROOM_NAME_1, ROOM_NAME_2, BUILDING_NAME_3);
    }

    @Test(priority = 4)
    public void addObjectAndExpandNextLevel() {
        hierarchyView.expandNextLevel(BUILDING_NAME_2);
        addExistingObject(BUILDING_NAME_6);
        hierarchyView.expandNextLevel(BUILDING_NAME_6);
        hierarchyView.getVisibleNodesLabel();
        Assertions.assertThat(hierarchyView.getVisibleNodesLabel()).contains(ROOM_NAME_3, BUILDING_NAME_6);
    }

    @Test(priority = 5)
    public void setFilterOnNodeAndAddObject() {
        hierarchyView.getNodeByLabelPath(LOCATION_RELATION_PATH).searchByAttribute(NAME_ATTRIBUTE_ID, ROOM_NAME_1);
        addExistingObject(BUILDING_NAME_4);

        TreeComponent.Node roomRelation = hierarchyView.getNodeByLabelPath(ROOM_RELATION_PATH);
        roomRelation.expandNode();
        Assertions.assertThat(hierarchyView.getVisibleNodesLabel()).contains(ROOM_NAME_1, BUILDING_NAME_4).doesNotContain(ROOM_NAME_2);
    }

    @Test(priority = 6)
    public void addMoreThan500Objects() {
        hierarchyView.getMainTree().callActionById(ADD_OBJECT_BUTTON);
        AdvancedSearchWidget advancedSearch = AdvancedSearchWidget.createById(driver, webDriverWait, ADVANCED_SEARCH_ID);
        TableComponent tableComponent = advancedSearch.getTableComponent();
        tableComponent.getPaginationComponent().changeRowsCount(500);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tableComponent.selectAll();
        advancedSearch.clickAdd();
        SystemMessageContainer systemMessageContainer = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> errors = systemMessageContainer.getMessages();
        systemMessageContainer.close();
        advancedSearch.clickCancel();
        Assertions.assertThat(errors).hasSize(1);
        Assertions.assertThat(errors.get(0).getText()).isEqualTo(THE_MAXIMUM_NUMBER_OF_OBJECTS_IS_500_ERROR_MESSAGE);
    }

    @Test(priority = 7)
    public void checkIsAddObjectButtonIsUnavailableForSelectedObject() {
        hierarchyView.selectFirstObject();
        Assertions.assertThat(CSSUtils.isElementPresent(driver, ADD_OBJECT_BUTTON)).isFalse();
        hierarchyView.getMainTree().unselectAllNodes();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assertions.assertThat(CSSUtils.isElementPresent(driver, ADD_OBJECT_BUTTON)).isTrue();
    }

    @Test(priority = 8)
    public void checkIsAddObjectButtonIsUnavailableWithHVQuery() {
        driver.get(String.format(HV_URL_WITH_QUERY, BASIC_URL));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assertions.assertThat(CSSUtils.isElementPresent(driver, ADD_OBJECT_BUTTON)).isFalse();
    }

    @Test(priority = 9)
    @Description("Hierarchy View is opened from Inventory View")
    public void addObjectToHierarchyViewOpenedFromOtherView() {
        NewInventoryViewPage location = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, LOCATION_TYPE);
        location.searchByAttributeValue(TYPE_ATTRIBUTE_ID, ROOM_TYPE, Input.ComponentType.MULTI_COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        location.selectFirstRow();
        location.goToHierarchyViewForSelectedObject();
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.getMainTree().callActionById(ADD_OBJECT_BUTTON);
        AdvancedSearchWidget advancedSearch = AdvancedSearchWidget.createById(driver, webDriverWait, ADVANCED_SEARCH_ID);
        DelayUtils.sleep();
        TableComponent tableComponent = advancedSearch.getTableComponent();
        tableComponent.selectRow(3);
        String type = tableComponent.getCellValue(3, TYPE_ATTRIBUTE_ID);
        advancedSearch.clickAdd();
        Assertions.assertThat(hierarchyView.getVisibleNodesLabel()).hasSize(2);
        Assertions.assertThat(type).isEqualTo(ROOM_TYPE);
    }

    @Test(priority = 10)
    public void addObjectAndCheckIfScrolledToElement() {
        hierarchyView = HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, LOCATION_TYPE, buildingId1);
        hierarchyView.getMainTree().getPagination().changeRowsCount(250);
        hierarchyView.getMainTree().callActionById(ADD_OBJECT_BUTTON);
        AdvancedSearchWidget advancedSearch = AdvancedSearchWidget.createById(driver, webDriverWait, ADVANCED_SEARCH_ID);
        TableComponent tableComponent = advancedSearch.getTableComponent();
        tableComponent.getPaginationComponent().changeRowsCount(100);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        tableComponent.selectAll();
        advancedSearch.clickAdd();
        buildingId5 = createBuilding(BUILDING_NAME_5);
        addExistingObject(BUILDING_NAME_5);
        Assertions.assertThat(hierarchyView.getVisibleNodesLabel()).contains(BUILDING_NAME_5);
    }

    @Test(priority = 11)
    public void addObjectAndCheckIfScrolledToElementAfterRemoveFilterNoData() {
        buildingId7 = createBuilding(BUILDING_NAME_7);
        hierarchyView.searchObject("asfasfafafs");
        addExistingObject(BUILDING_NAME_7);
        hierarchyView.clearFiltersOnMainTree();
        Assertions.assertThat(hierarchyView.getVisibleNodesLabel()).contains(BUILDING_NAME_7);

    }

    @Test(priority = 12)
    public void addObjectAndCheckIfScrolledToElementWithSetFilter() {
        buildingId8 = createBuilding(BUILDING_NAME_8);
        hierarchyView.getMainTree().searchByAttribute(TYPE_ATTRIBUTE_ID, Input.ComponentType.MULTI_COMBOBOX, BUILDING_TYPE);
        addExistingObject(BUILDING_NAME_8);
        boolean isVisible = hierarchyView.getVisibleNodesLabel().contains(BUILDING_NAME_8);
        Assertions.assertThat(isVisible).isTrue();
    }

    @Test(priority = 13)
    public void AddObjectAndCheckIfScrolledToElementWhenChangeFilter() {
        buildingId9 = createBuilding(BUILDING_NAME_9);
        hierarchyView.getMainTree().searchByAttribute(TYPE_ATTRIBUTE_ID, Input.ComponentType.MULTI_COMBOBOX, ROOM_TYPE);
        addExistingObject(BUILDING_NAME_9);
        hierarchyView.getMainTree().searchByAttribute(TYPE_ATTRIBUTE_ID, Input.ComponentType.MULTI_COMBOBOX, BUILDING_TYPE);
        boolean isVisible = hierarchyView.getVisibleNodesLabel().contains(BUILDING_NAME_9);
        Assertions.assertThat(isVisible).isTrue();
    }

    @AfterMethod
    private void clearFilters() {
        hierarchyView.clearFiltersOnMainTree();
    }

    @AfterClass
    private void deleteBuildings() {
        deleteBuilding(buildingId1);
        deleteBuilding(buildingId2);
        deleteBuilding(buildingId3);
        deleteBuilding(buildingId4);
        deleteBuilding(buildingId5);
        deleteBuilding(buildingId6);
        deleteBuilding(buildingId7);
        deleteBuilding(buildingId8);
        deleteBuilding(buildingId9);
    }

    private void addExistingObject(String objectName) {
        hierarchyView.getMainTree().callActionById(ADD_OBJECT_BUTTON);
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
        locationInventoryRepository.deleteLocation(Long.valueOf(locationId), BUILDING_TYPE);
    }

}
