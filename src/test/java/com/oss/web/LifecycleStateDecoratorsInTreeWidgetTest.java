/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.web;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.PlanningRepository;
import com.oss.services.ResourceCatalogClient;
import com.oss.untils.Environment;
import com.oss.untils.FakeGenerator;

/**
 * @author Gabriela Zaranek
 */
public class LifecycleStateDecoratorsInTreeWidgetTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(LifecycleStateDecoratorsInTreeWidgetTest.class);
    private static final String PROJECT_NAME_CODE_1 = "HVLSC-" + FakeGenerator.getIdNumber();
    private static final String PROJECT_NAME_CODE_2 = "HVLSC2-" + FakeGenerator.getIdNumber();
    private static final String BUILDING_NAME = FakeGenerator.getCity() + "-BU" + FakeGenerator.getRandomInt();
    private static final String HARDWARE_RELATION_PATH = BUILDING_NAME + ".Hardware";
    private static final String LOCATION_TYPE_BUILDING = "Building";
    private static final String CREATE_ACTION_ID = "CREATE";
    private static final String DEVICE_1_MODEL = "N9K-C9396PX";
    private static final String DEVICE_1_NAME = "Device_1_" + FakeGenerator.getRandomInt();
    private static final String DEVICE_1_PATH = BUILDING_NAME + ".Hardware.Switch." + DEVICE_1_NAME;
    private static final String PORT_01_PATH = DEVICE_1_PATH + ".Ports.01";
    private static final TreeComponent.Node.DecoratorStatus GREEN = TreeComponent.Node.DecoratorStatus.GREEN;
    private static final TreeComponent.Node.DecoratorStatus PURPLE = TreeComponent.Node.DecoratorStatus.PURPLE;
    private static final String CREATE_DEVICE_ACTION_ID = "CreateDeviceOnLocationWizardAction";
    private static final String REFRESH_ACTION_ID = "tree_gql_refresh_relation";
    private static final String DEVICE_MODEL_TYPE = "IPDeviceModel";
    private static final String DEVICE_2_NAME = "Device_2";
    private static final String DEVICE_3_NAME = "Device_3";
    private static final String DEVICE_2_PATH = BUILDING_NAME + ".Hardware.Switch." + DEVICE_2_NAME;
    private static final String PORT_02_PATH = DEVICE_2_PATH + ".Ports.01";
    private static final String REFRESH_TREE_ACTION_ID = "tree_gql_refresh";
    private static final String DEVICE_3_PATH = BUILDING_NAME + ".Hardware.Switch." + DEVICE_3_NAME;
    private static final String PORT_03_PATH = DEVICE_3_PATH + ".Ports.01";
    private static final String UPDATE = "Update";
    private static final String UPDATE_DEVICE_ACTION_ID = "UpdateDeviceWizardAction";
    private static final String PORT_01_RELATION = DEVICE_1_PATH + ".Ports";
    private static final String SWITCH_RELATION_PATH = BUILDING_NAME + ".Hardware.Switch";
    private Environment env = Environment.getInstance();
    private HierarchyViewPage hierarchyViewPage;
    private Long project1;
    private Long project2;
    private String buildingId;
    private Long device2Id;
    private Long device3Id;
    
    @BeforeClass
    public void createProjects() {
        project1 = createProject(PROJECT_NAME_CODE_1, LocalDate.now());
        log.info("Project id: " + project1 + ", Project Code: " + PROJECT_NAME_CODE_1);
        
        project2 = createProject(PROJECT_NAME_CODE_2, LocalDate.now().plusDays(2));
        log.info("Project id 2: " + project2 + ", Project Code: " + PROJECT_NAME_CODE_2);
    }
    
    @Test(priority = 0)
    public void createNewObjects() {
        buildingId = createBuilding(project1);
        log.info("Building id: " + buildingId);
        hierarchyViewPage =
                HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, LOCATION_TYPE_BUILDING, buildingId, project1.toString());
        hierarchyViewPage.getFirstNode().callAction(CREATE_ACTION_ID, CREATE_DEVICE_ACTION_ID);
        createDeviceWizard();
        Assertions.assertThat(getNode(BUILDING_NAME).getDecoratorStatus()).isEqualTo(GREEN);
        Assertions.assertThat(getNode(DEVICE_1_PATH).getDecoratorStatus()).isEqualTo(GREEN);
        Assertions.assertThat(getNode(PORT_01_PATH).getDecoratorStatus()).isEqualTo(GREEN);
    }
    
    @Test(priority = 1)
    public void createObjectsAndRefreshRelation() {
        device2Id = createDevice(DEVICE_2_NAME, project1);
        getNode(HARDWARE_RELATION_PATH).callAction(REFRESH_ACTION_ID);
        Assertions.assertThat(getNode(DEVICE_2_PATH).getDecoratorStatus()).isEqualTo(GREEN);
        Assertions.assertThat(getNode(PORT_02_PATH).getDecoratorStatus()).isEqualTo(GREEN);
    }
    
    @Test(priority = 2)
    public void createObjectsAndRefreshTree() {
        device3Id = createDevice(DEVICE_3_NAME, project1);
        hierarchyViewPage.getMainTree().callActionById(ActionsContainer.KEBAB_GROUP_ID, REFRESH_TREE_ACTION_ID);
        Assertions.assertThat(getNode(DEVICE_3_PATH).getDecoratorStatus()).isEqualTo(GREEN);
        Assertions.assertThat(getNode(PORT_03_PATH).getDecoratorStatus()).isEqualTo(GREEN);
    }
    
    @Test(priority = 3)
    public void updateObjectsWizard() {
        
        hierarchyViewPage =
                HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, LOCATION_TYPE_BUILDING, buildingId, project2.toString());
        getNode(DEVICE_1_PATH).callAction(ActionsContainer.EDIT_GROUP_ID, UPDATE_DEVICE_ACTION_ID);
        updateDeviceWizard();
        TreeComponent.Node nodeDevice1 = getNode(DEVICE_1_PATH);
        Assertions.assertThat(nodeDevice1.countDecorators()).isEqualTo(1);
        Assertions.assertThat(nodeDevice1.getDecoratorStatus()).isEqualTo(PURPLE);
    }
    
    @Test(priority = 4)
    public void updateObjectsAndRefreshRelation() {
        updateDevice(device2Id, project2);
        getNode(HARDWARE_RELATION_PATH).callAction(REFRESH_ACTION_ID);
        TreeComponent.Node node = getNode(DEVICE_2_PATH);
        Assertions.assertThat(node.countDecorators()).as("Check number of decorators").isEqualTo(1);
        Assertions.assertThat(node.getDecoratorStatus()).as("Check color of decorator").isEqualTo(PURPLE);
    }
    
    @Test(priority = 5)
    public void updateObjectsAndRefreshTree() {
        updateBuilding();
        updateDevice(device3Id, project2);
        hierarchyViewPage.getMainTree().callActionById(ActionsContainer.KEBAB_GROUP_ID, REFRESH_TREE_ACTION_ID);
        TreeComponent.Node node = getNode(DEVICE_3_PATH);
        Assertions.assertThat(node.countDecorators()).isEqualTo(1);
        Assertions.assertThat(node.getDecoratorStatus()).isEqualTo(PURPLE);
    }
    
    @Test(priority = 6)
    public void completeProjectAndRefreshRelation() {
        hierarchyViewPage =
                HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, LOCATION_TYPE_BUILDING, buildingId, project1.toString());
        getNode(PORT_01_RELATION).expandNode();
        completeProject(project1);
        getNode(PORT_01_RELATION).callAction(REFRESH_ACTION_ID);
        Assertions.assertThat(getNode(PORT_01_PATH).countDecorators()).isZero();
    }
    
    @Test(priority = 7)
    public void completeProjectAndRefresh() {
        hierarchyViewPage =
                HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, LOCATION_TYPE_BUILDING, buildingId, project2.toString());
        getNode(SWITCH_RELATION_PATH).expandNode();
        completeProject(project2);
        hierarchyViewPage.getMainTree().callActionById(ActionsContainer.KEBAB_GROUP_ID, REFRESH_TREE_ACTION_ID);
        TreeComponent.Node device1 = getNode(DEVICE_1_PATH);
        TreeComponent.Node device2 = getNode(DEVICE_2_PATH);
        TreeComponent.Node device3 = getNode(DEVICE_3_PATH);
        Assertions.assertThat(device1.countDecorators()).isZero();
        Assertions.assertThat(device2.countDecorators()).isZero();
        Assertions.assertThat(device3.countDecorators()).isZero();
    }
    
    @AfterClass
    public void clear() {
        deleteDevice(device3Id);
        deleteDevice(device2Id);
        deleteDevice(getDeviceId(DEVICE_1_NAME, buildingId));
        deleteBuilding();
    }
    
    private Long createProject(String code, LocalDate finishDueDate) {
        PlanningRepository planningRepository = new PlanningRepository(env);
        return planningRepository.createProject(code, code, finishDueDate);
    }
    
    private void completeProject(long projectId) {
        PlanningRepository planningRepository = new PlanningRepository(env);
        planningRepository.moveToLive(projectId);
        
    }
    
    private String createBuilding(long projectId) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        log.info("Building name: " + BUILDING_NAME);
        return locationInventoryRepository.createLocation(BUILDING_NAME, LOCATION_TYPE_BUILDING, getGeographicalAddress(), projectId);
    }
    
    private void updateBuilding() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationInventoryRepository.updateLocation(BUILDING_NAME, LOCATION_TYPE_BUILDING, buildingId, getGeographicalAddress(),
                FakeGenerator.getAddress(), project2);
    }
    
    private void deleteBuilding() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationInventoryRepository.deleteLocation(buildingId, LOCATION_TYPE_BUILDING);
    }
    
    private Long getGeographicalAddress() {
        AddressRepository addressRepository = new AddressRepository(env);
        return addressRepository.getFirstGeographicalAddressId();
    }
    
    private void createDeviceWizard() {
        DeviceWizardPage deviceWizard = new DeviceWizardPage(driver);
        deviceWizard.setModel(DEVICE_1_MODEL);
        deviceWizard.setName(DEVICE_1_NAME);
        deviceWizard.next();
        deviceWizard.setPreciseLocation(BUILDING_NAME);
        deviceWizard.accept();
    }
    
    private Long createDevice(String deviceName, Long projectId) {
        Long deviceModelId = getDeviceModelId();
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        return physicalInventoryRepository.createDevice(LOCATION_TYPE_BUILDING, Long.valueOf(buildingId), deviceModelId, deviceName,
                DEVICE_MODEL_TYPE, projectId);
    }
    
    private Long getDeviceModelId() {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        return resourceCatalogClient.getModelIds(DEVICE_1_MODEL);
    }
    
    private void updateDeviceWizard() {
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setDescription(UPDATE);
        deviceWizardPage.nextUpdateWizard();
        deviceWizardPage.acceptUpdateWizard();
    }
    
    private void updateDevice(Long deviceId, Long projectId) {
        Long deviceModelId = getDeviceModelId();
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        physicalInventoryRepository.updateDeviceSerialNumber(deviceId, LOCATION_TYPE_BUILDING, Long.valueOf(buildingId),
                FakeGenerator.getIdNumber(), deviceModelId, DEVICE_MODEL_TYPE,
                projectId);
    }
    
    private void deleteDevice(long deviceId) {
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        physicalInventoryRepository.deleteDevice(String.valueOf(deviceId));
    }
    
    private Long getDeviceId(String deviceName, String locationId) {
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        return physicalInventoryRepository.getDeviceId(locationId, deviceName);
    }
    
    private TreeComponent.Node getNode(String nodePath) {
        return hierarchyViewPage.getNodeByLabelPath(nodePath);
    }
}
