/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2022 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.web;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.SublocationWizardPage;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PlanningRepository;
import com.oss.untils.Environment;
import com.oss.untils.FakeGenerator;

/**
 * @author Gabriela Zaranek
 */
public class LifecycleStateDecoratorsInTreeWidgetTest extends BaseTestCase {
    private static final Logger log = LoggerFactory.getLogger(LifecycleStateDecoratorsInTreeWidgetTest.class);
    private static final String PROJECT_NAME_CODE_1 = "HVLSC-" + FakeGenerator.getIdNumber();
    private static final String BUILDING_NAME = FakeGenerator.getCity() + "-" + FakeGenerator.getIdNumber();
    private static final String LOCATION_TYPE_BUILDING = "Building";
    private static final String CREATE_ACTION_ID = "CREATE";
    private static final String CREATE_SUBLOCATION_ACTION_ID = "CreateSublocationInLocationWizardAction";
    private static final String SUB_LOCATION_TYPE_ROOM = "Room";
    private static final String ROOM_1_CREATE = "R_1";
    private Environment env = Environment.getInstance();
    private HierarchyViewPage hierarchyViewPage;
    
    @BeforeClass
    public void openHierarchyView() {
      //  hierarchyViewPage = HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, LOCATION_TYPE_BUILDING, "117290108", "117290533");
    }
    
    @Test
    public void createNewObjects() {
        Long project1 = createProject(PROJECT_NAME_CODE_1, LocalDate.now());
        log.info("Project id: " + project1 + ", Project Code: " + PROJECT_NAME_CODE_1);
        String building = createBuilding(project1);
        log.info("Building id: " + building);
        hierarchyViewPage =
                HierarchyViewPage.goToHierarchyViewPage(driver, BASIC_URL, LOCATION_TYPE_BUILDING, building, project1.toString());
        hierarchyViewPage.getFirstNode().callAction(CREATE_ACTION_ID, CREATE_SUBLOCATION_ACTION_ID);
        createSublocationWizard();
        hierarchyViewPage.expandTreeNode(BUILDING_NAME);
    }
    
    private Long createProject(String code, LocalDate finishDueDate) {
        PlanningRepository planningRepository = new PlanningRepository(env);
        return planningRepository.createProject(code, code, finishDueDate);
    }
    
    private String createBuilding(long projectId) {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        log.info("Building name: " + BUILDING_NAME);
        return locationInventoryRepository.createLocation(BUILDING_NAME, LOCATION_TYPE_BUILDING, getGeographicalAddress(), projectId);
    }
    
    private Long getGeographicalAddress() {
        AddressRepository addressRepository = new AddressRepository(env);
        return addressRepository.getFirstGeographicalAddressId();
    }

    private void createSublocationWizard(){
        SublocationWizardPage sublocation = new SublocationWizardPage(driver);
        sublocation.setSublocationType(SUB_LOCATION_TYPE_ROOM);
        sublocation.setSublocationName(ROOM_1_CREATE);
        sublocation.setPreciseLocation(BUILDING_NAME);
        sublocation.clickNext();
        sublocation.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
