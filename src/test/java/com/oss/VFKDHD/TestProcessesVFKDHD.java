package com.oss.VFKDHD;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.bpm.PlannersViewPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.bpm.processinstances.ProcessRolesStepWizardPage;
import com.oss.pages.logical.LogicalLocationWizard;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.physical.LocationOverviewPage;
import com.oss.pages.physical.LocationWizardPage;
import com.oss.pages.physical.SublocationWizardPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;

import io.qameta.allure.Description;

public class TestProcessesVFKDHD extends BaseTestCase {
    private static final String LOGICAL_LOCATION_CREATED_SUCCESSFULLY_MASSAGE = "Logical Location created successfully, click here to open Inventory View.";
    private static final String LOCATION_UPDATED_SUCCESSFULLY_MASSAGE = "Location has been updated successfully, click here to open Location Overview";
    private static final String LOCATION_CREATED_SUCCESSFULLY_MASSAGE = "Location has been created successfully, click here to open Location Overview";
    private static final String SUBLOCATION_CREATED_SUCCESSFULLY_MASSAGE = "Sublocations created successfully, click here to open them in Inventory View.";
    private static final String DEVICE_CREATED_SUCCESSFULLY_MASSAGE = "Device has been created successfully, click here to open Hierarchy View.";
    private static final String BPM_TASKS = "Tasks";
    private static final String LOCATION_TYPE = "Building Complex";
    private static final String LOCATION_NAME = "Selenium Location " + Math.random();
    private static final String TASK_NAME = "Correct data";
    private static final String SUB_LOCATION_NAME = "Floor 1";
    private static final String DEVICE_MODEL = "Cisco Systems Inc. WS-C6513-E";
    private static final String RESOURCE_OWNER = "SeleniumTests";
    private static final String END_BRACKED_AFTER_PROCES_CODE = ")";
    private static final String NAME_ATTRIBUTE_LABEL = "Name";
    private static final String STATE_FILTER_ID = "state";
    private static final String NAME_ATTRIBUTE_ID = "name";
    private static final String[] OBJECT_CREATION_PATH = {"KDHD Dashboard", "Object Creation"};
    private final static String DEVICE_NAME = DEVICE_MODEL + " " + SUB_LOCATION_NAME;
    private static final String LOGICAL_LOCATION_NAME = "Selenium Logical Location";
    private static final Random RANDOM = new Random();
    private static final String STREET = "Przemyslowa";
    private static final String THE_TASK_PROPERLY_ASSIGNED_MASSAGE = "The task properly assigned.";
    private static final String TASK_PROPERLY_COMPLETED_MASSAGE = "Task properly completed.";
    private static final String CREATE_PHYSICAL_DEVICE_BUTTON_NAME = "Create Physical Device";
    private static final String XID_ATTRIBUTE_LABEL = "XId";
    private static final String REMARKS_FOR_LOGICAL_LOCATION = "Remarks for logical location";
    private static final String DESCRIPTION_FOR_LOGICAL_LOCATION = "This ia a logical location";
    private static final String LOGICAL_LOCATION_TYPE = "PoP";
    private static final String LOGICAL_LOCATION_SUBTYPE = "PPoP";
    private static final String CREATE_LOGICAL_LOCATION_BUTTON_LABEL = "Create Logical Location";
    private static final String SUBLOCATION_TYPE = "Floor";
    private static final String CREATE_SUBLOCATION_BUTTON_LABEL = "Create Sublocation";
    private static final String EDIT_LOCATION_BUTTON_LABEL = "Edit Location";
    private static final String OPEN_LOCATION_OVERVIEW_ACTION_ID = "OpenLocationOverviewAction";
    private static final String STATIC_PART_OF_NAME_OF_TESTS = "Selenium Test";
    private static final String NAME_FILTER_IN_IV_ID = "Naming.name";
    private static final String PLANNERS_ID = "Planner";
    private static final String CREATE_LOCATION = "Create Location";
    private static final String INVENTORY_VIEW = "Inventory View";
    private static final String INVENTORY_VIEW_PATH = "Resource Inventory ";
    private static final String OPENING_BRACKED_BEFORE_PROCES_CODE = "(";
    private static final String PLANNER = RESOURCE_OWNER;
    private static final String OLD_PROPERTY_PANEL_ID = "propertyPanelAppAttributesId";
    private static final String EXPECTED_STATUS_AFTER_FINISHING_TASK = "Completed";
    private static final String PROCESS_CREATED_SUCCESSFULLY_FIRST_STATIC_PART_OF_MASSAGE = "Process Selenium Test";
    private static final String[] PROCESS_CREATED_SUCCESSFULLY_REST_STATIC_PARTS_OF_MASSAGE = new String[]{"(DCP-", ") was created"};
    private final SoftAssert softAssert = new SoftAssert();
    private String processDCPCode;
    private SystemMessageInterface systemMessage;
    private LocationOverviewPage locationOverviewPage;
    private static final String PLANNERS_VIEW = "Planners View";
    private static final String[] BUSINESS_PROCESS_MANAGEMENT_PATH = {"BPM and Planning", "Business Process Management"};
    static private final Multimap<String, String> FILTERS_USED_TO_CHECK_IF_PROCESS_IS_COMPLETED;

    static {
        FILTERS_USED_TO_CHECK_IF_PROCESS_IS_COMPLETED = HashMultimap.create();
        FILTERS_USED_TO_CHECK_IF_PROCESS_IS_COMPLETED.put(NAME_ATTRIBUTE_ID, STATIC_PART_OF_NAME_OF_TESTS);
        FILTERS_USED_TO_CHECK_IF_PROCESS_IS_COMPLETED.put(STATE_FILTER_ID, EXPECTED_STATUS_AFTER_FINISHING_TASK);
    }

    @Test(description = "Create DCP")
    @Description("Create new Data Correction Process")
    public void createNewProcess() {
        homePage.chooseFromLeftSideMenu(PLANNERS_VIEW, BUSINESS_PROCESS_MANAGEMENT_PATH);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PlannersViewPage plannersViewPage = new PlannersViewPage(driver, webDriverWait);
        ProcessRolesStepWizardPage processRolesStepWizardPage = plannersViewPage.openProcessCreationWizard().defineSimpleDCPAndGoToProcessRolesStep();
        processRolesStepWizardPage.addPlanner(PLANNERS_ID, PLANNER);
        processRolesStepWizardPage.clickAcceptButton();
        processDCPCode = getDCPProcessCodeFromFirstMassage();
        checkPopup(PROCESS_CREATED_SUCCESSFULLY_FIRST_STATIC_PART_OF_MASSAGE, PROCESS_CREATED_SUCCESSFULLY_REST_STATIC_PARTS_OF_MASSAGE);
        systemMessage.close();
        //TODO check if process was created
    }

    @Test(priority = 1, description = "Start DCP", dependsOnMethods = {"createNewProcess"})
    @Description("Start newly created Data Correction Process")
    public void startDCP() {
        TasksPage tasksPage = getTasksPage();
        tasksPage.clearAllColumnFilters();
        tasksPage.startTask(processDCPCode, TASK_NAME);
        checkPopup(THE_TASK_PROPERLY_ASSIGNED_MASSAGE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        systemMessage.close();
        //TODO check if process is actually started
    }

    @Test(priority = 2, description = "Create New Location", dependsOnMethods = {"createNewProcess", "startDCP"})
    @Description
    public void createNewLocation() {
        homePage.chooseFromLeftSideMenu(CREATE_LOCATION, OBJECT_CREATION_PATH);
        createLocation();
        checkPopup(LOCATION_CREATED_SUCCESSFULLY_MASSAGE);
        systemMessage.close();
        //TODO check if location was created
    }

    @Test(priority = 3, description = "Find location in new Inventory View and open location in Location Overview view", dependsOnMethods = {"startDCP", "createNewProcess", "createNewLocation"})
    @Description("Find location in new Inventory View and open location in Location Overview view")
    public void findLocation() {
        homePage.chooseFromLeftSideMenu(INVENTORY_VIEW, INVENTORY_VIEW_PATH);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(LOCATION_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        AdvancedSearch advancedSearch = newInventoryViewPage.getAdvancedSearch();
        advancedSearch.openSearchPanel();
        if (!advancedSearch.getAllVisibleFilters().contains(NAME_ATTRIBUTE_LABEL)) {
            advancedSearch.selectAttributes(Collections.singletonList(NAME_FILTER_IN_IV_ID));
        }
        advancedSearch.setFilter(NAME_FILTER_IN_IV_ID, LOCATION_NAME);
        advancedSearch.clickApply();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.selectRow(NAME_ATTRIBUTE_ID, LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, OPEN_LOCATION_OVERVIEW_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        OldPropertyPanel oldPropertyPanel = OldPropertyPanel.createById(driver, webDriverWait, OLD_PROPERTY_PANEL_ID);
        softAssert.assertEquals(oldPropertyPanel.getPropertyValue(NAME_ATTRIBUTE_LABEL), LOCATION_NAME);
    }

    @Test(priority = 4, description = "Change Location Street Number", dependsOnMethods = {"startDCP", "createNewProcess", "findLocation", "createNewLocation"})
    @Description("Change Location Street Number")
    public void changeStreetNumber() {
        locationOverviewPage = new LocationOverviewPage(driver);
        locationOverviewPage.clickButton(EDIT_LOCATION_BUTTON_LABEL);
        changeStreetNumberWizardOperations();
        checkPopup(LOCATION_UPDATED_SUCCESSFULLY_MASSAGE);
        systemMessage.close();
    }

    @Test(priority = 5, description = "Creates SubLocation in Location", dependsOnMethods = {"startDCP", "createNewProcess", "findLocation", "changeStreetNumber", "createNewLocation"})
    @Description("Creates SubLocation")
    public void CreateSubLocation() {
        locationOverviewPage.clickButton(CREATE_SUBLOCATION_BUTTON_LABEL);
        createSubLocationWizardOperations();
        checkPopup(SUBLOCATION_CREATED_SUCCESSFULLY_MASSAGE);
        systemMessage.close();
        //TODO check if sublocation was created
    }

    @Test(priority = 6, description = "Creates Logical Location in Location", dependsOnMethods = {"startDCP", "createNewProcess", "findLocation", "changeStreetNumber", "createNewLocation", "CreateSubLocation"})
    @Description("Creates Logical Location in Location")
    public void CreateLogicalLocation() {
        locationOverviewPage.clickButton(CREATE_LOGICAL_LOCATION_BUTTON_LABEL);
        createLogicalLocationWizardOperations();
        checkPopup(LOGICAL_LOCATION_CREATED_SUCCESSFULLY_MASSAGE);
        systemMessage.close();
        //TODO check if logical location wash created
    }

    @Test(priority = 7, description = "Creates Device in Location", dependsOnMethods = {"startDCP", "createNewProcess", "findLocation", "changeStreetNumber", "createNewLocation", "CreateLogicalLocation", "CreateSubLocation"})
    @Description("Creates Device in Location")
    public void CreateDevice() {
        locationOverviewPage.selectTab("Logical Locations");
        OldTable logicalLocationTab = locationOverviewPage.getTabTable(LocationOverviewPage.TabName.POP);
        int rowWithLogicalLocation = logicalLocationTab.getRowNumber(LOGICAL_LOCATION_NAME, NAME_ATTRIBUTE_LABEL);
        String XIdOfLogicalLocation = logicalLocationTab.getCellValue(rowWithLogicalLocation, XID_ATTRIBUTE_LABEL);
        locationOverviewPage.clickButton(CREATE_PHYSICAL_DEVICE_BUTTON_NAME);
        createDeviceWizardOperations(XIdOfLogicalLocation);
        checkPopup(DEVICE_CREATED_SUCCESSFULLY_MASSAGE);
        systemMessage.close();
        //TODO Check if device relly was created.
    }

    @Test(priority = 8, description = "Finish DCP", dependsOnMethods = {"startDCP", "createNewProcess", "findLocation", "changeStreetNumber", "createNewLocation", "CreateLogicalLocation", "CreateSubLocation", "CreateDevice"})
    @Description("Finish Data Correction Process")
    public void finnishDCP() {
        TasksPage tasksPage = getTasksPage();
        tasksPage.completeTask(processDCPCode, TASK_NAME);
        checkPopup(TASK_PROPERLY_COMPLETED_MASSAGE);
        systemMessage.close();
        homePage.chooseFromLeftSideMenu(PLANNERS_VIEW, BUSINESS_PROCESS_MANAGEMENT_PATH);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PlannersViewPage plannersViewPage = new PlannersViewPage(driver, webDriverWait);
        plannersViewPage.searchByAttributesValue(FILTERS_USED_TO_CHECK_IF_PROCESS_IS_COMPLETED);
        softAssert.assertEquals(plannersViewPage.getProcessState(processDCPCode), EXPECTED_STATUS_AFTER_FINISHING_TASK);
    }

    private void createLocation() {
        LocationWizardPage locationWizardPage = new LocationWizardPage(driver);
        locationWizardPage.setStreet(STREET);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setStreetNumber(String.valueOf(RANDOM.nextInt(Integer.MAX_VALUE)));
        locationWizardPage.clickNext();
        locationWizardPage.setLocationType(LOCATION_TYPE);
        locationWizardPage.clickNext();
        locationWizardPage.setLocationNameInList(0, LOCATION_NAME);
        locationWizardPage.clickRecalculateNaming();
        locationWizardPage.create();
    }

    private void changeStreetNumberWizardOperations() {
        LocationWizardPage locationWizardPage = new LocationWizardPage(driver);
        locationWizardPage.setStreetNumber(String.valueOf(RANDOM.nextInt(Integer.MAX_VALUE)));
        locationWizardPage.clickNext();
        locationWizardPage.clickNext();
        locationWizardPage.accept();
        //TODO check if street number has changed
    }

    private void createSubLocationWizardOperations() {
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        sublocationWizardPage.setSublocationType(SUBLOCATION_TYPE);
        sublocationWizardPage.clickNext();
        sublocationWizardPage.clickNext();
        sublocationWizardPage.setSubLocationNameInList(0, SUB_LOCATION_NAME);
        sublocationWizardPage.clickRecalculateNaming();
        sublocationWizardPage.create();
    }

    private void createLogicalLocationWizardOperations() {
        LogicalLocationWizard logicalLocationWizard = new LogicalLocationWizard(driver);
        logicalLocationWizard.setLocationType(LOGICAL_LOCATION_TYPE);
        logicalLocationWizard.setType(LOGICAL_LOCATION_SUBTYPE);
        logicalLocationWizard.setDescription(DESCRIPTION_FOR_LOGICAL_LOCATION);
        logicalLocationWizard.setRemarks(REMARKS_FOR_LOGICAL_LOCATION);
        logicalLocationWizard.clickNext();
        logicalLocationWizard.clickNext();
        logicalLocationWizard.setLogicalLocationNameInList(0, LOGICAL_LOCATION_NAME);
        logicalLocationWizard.clickRecalculateNaming();
        logicalLocationWizard.clickCreate();
    }

    private void createDeviceWizardOperations(String XIdOfLogicalLocation) {
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(DEVICE_MODEL);
        deviceWizardPage.next();
        deviceWizardPage.setPreciseLocation(SUB_LOCATION_NAME);
        deviceWizardPage.setLogicalLocation(XIdOfLogicalLocation);
        deviceWizardPage.next();
        deviceWizardPage.setResourceOwner(RESOURCE_OWNER);
        deviceWizardPage.next();
        deviceWizardPage.setLocationNameInList(0, DEVICE_NAME);
        deviceWizardPage.clickRecalculateNaming();
        deviceWizardPage.accept();
    }

    private TasksPage getTasksPage() {
        homePage.chooseFromLeftSideMenu(BPM_TASKS, BUSINESS_PROCESS_MANAGEMENT_PATH);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return new TasksPage(driver);
    }

    private String getDCPProcessCodeFromFirstMassage() {
        String massage = getPopupMassages().get(0).getText();
        int startOfDCP = massage.indexOf(OPENING_BRACKED_BEFORE_PROCES_CODE) + 1;
        int endOfDCP = massage.indexOf(END_BRACKED_AFTER_PROCES_CODE);
        return massage.substring(startOfDCP, endOfDCP);
    }

    private List<SystemMessageContainer.Message> getPopupMassages() {
        systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        return systemMessage.getMessages();
    }

    private void checkPopup(String text, String... moreTexts) {
        List<SystemMessageContainer.Message> messages = getPopupMassages();
        softAssert.assertEquals(messages.size(), 1);
        softAssert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
        String massage = messages.get(0).getText();
        softAssert.assertTrue(massage.contains(text));
        for (String nextText : moreTexts) {
            softAssert.assertTrue(massage.contains(nextText));
        }
    }

}