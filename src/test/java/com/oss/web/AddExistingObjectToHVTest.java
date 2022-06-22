/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.web;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.pages.platform.HierarchyViewPage;
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
    //private static final String BUILDING_NAME_1 = FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String BUILDING_NAME_2 = FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String BUILDING_NAME_3 = FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String ROOM_NAME_1 = FakeGenerator.getIdNumber();
    private static final String ROOM_NAME_2 = FakeGenerator.getIdNumber();
    private static final String BUILDING_NAME_1 = "South Lynnborough-BU45";
    private static final String ADVANCED_SEARCH_ID = "advancedSearch";
    private static final String BUILDING_TYPE = "Building";
    private static final String ROOM_TYPE = "Room";
    private Environment env = Environment.getInstance();
    private String buildingId1;
    private String buildingId2;
    private String buildingId3;
    private HierarchyViewPage hierarchyView;
    private AdvancedSearchWidget advancedSearch;

    @BeforeClass
    public void openHierarchyView() {
        createLocations();
        hierarchyView = HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, LOCATION_TYPE, buildingId2);
        advancedSearch = AdvancedSearchWidget.createById(driver, webDriverWait, ADVANCED_SEARCH_ID);

    }

    private void createLocations(){
        buildingId1 = createBuilding(BUILDING_NAME_1);
        buildingId2 = createBuilding(BUILDING_NAME_2);
        buildingId3 = createBuilding(BUILDING_NAME_3);
        Long roomId1 = createRoom(buildingId2, ROOM_NAME_1);
        Long roomId2 = createRoom(buildingId2, ROOM_NAME_2);
    }

    @Test
    public void addObjects() {
        hierarchyView.getMainTree().callActionById(ADD_OBJECT_BUTTON);
        advancedSearch.setFilter("name", BUILDING_NAME_1);
        DelayUtils.sleep();
        TableComponent tableComponent = advancedSearch.getTableComponent();
        tableComponent.selectRow(0);
        String label = tableComponent.getCellValue(0, "name");
        advancedSearch.clickAdd();
        List<String> visibleNodesLabel = hierarchyView.getVisibleNodesLabel();
        Assertions.assertThat(visibleNodesLabel).contains(label);

        hierarchyView.getMainTree().callActionById(ADD_OBJECT_BUTTON);
        advancedSearch.setFilter("name", BUILDING_NAME_1);
        DelayUtils.sleep();
        Input.MouseCursor cursor = ComponentFactory.create("cell-row-0-col-checkbox", Input.ComponentType.COMBOBOX, driver, webDriverWait).cursor();
        DelayUtils.sleep();
        advancedSearch.clickCancel();
        Assertions.assertThat(cursor).isEqualTo(Input.MouseCursor.NOT_ALLOWED);
        //deleteBuilding(buildingId);
    }

    @Test
    public void expandNodeAndAddObject() {
        hierarchyView.expandNextLevel(BUILDING_NAME_2);

    }

    @Test
    public void setFilterAndAddObject() {
    }

    @Test
    public void addMoreThan500Objects() {
    }


    @Test
    public void addSubtypeOfRootObject() {
    }

    @Test
    @Description("Hierarchy View is opened for Location")
    public void addObjectsToHierarchyViewOpenedFromOtherView() {
    }

    @Test
    @Description("Hierarchy View is opened for Building")
    public void addObjectsForOneType() {
    }

    private Long getGeographicalAddress() {
        AddressRepository addressRepository = new AddressRepository(env);
        return addressRepository.getFirstGeographicalAddressId();
    }

    private String createBuilding(String buildingName){
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
       return locationInventoryRepository.createLocation(buildingName, BUILDING_TYPE, getGeographicalAddress());

    }

    private Long createRoom(String locationId, String roomName){
        long locationIdL = Long.parseLong(locationId);
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.createSubLocation(ROOM_TYPE,roomName, locationIdL, BUILDING_TYPE, locationIdL, BUILDING_TYPE);

    }

    private void deleteBuilding(String locationId){
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationInventoryRepository.deleteLocation(Long.valueOf(locationId), "Building");
    }

    private int getLocationId(int index){
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        return locationInventoryRepository.getLocationsIds().get(index);
    }
}
