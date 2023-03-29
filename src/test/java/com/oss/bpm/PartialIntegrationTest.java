package com.oss.bpm;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.planning.PartialIntegrationWizardPage;
import com.oss.pages.bpm.planning.ProcessDetailsPage;
import com.oss.pages.bpm.processinstances.PlannersViewPage;
import com.oss.pages.bpm.tasks.SetupIntegrationProperties;
import com.oss.pages.bpm.tasks.TasksPageV2;
import com.oss.planning.PlanningContext;
import com.oss.planning.validationresults.ValidationResult;
import io.qameta.allure.Description;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.oss.bpm.BpmPhysicalDataCreator.CARD_NAME;
import static com.oss.bpm.BpmPhysicalDataCreator.CHASSIS_NAME;
import static com.oss.bpm.BpmPhysicalDataCreator.IP_DEVICE_NAME;
import static com.oss.bpm.BpmPhysicalDataCreator.LOCATION_TYPE_BUILDING;
import static com.oss.bpm.BpmPhysicalDataCreator.cancelProject;
import static com.oss.bpm.BpmPhysicalDataCreator.createBuilding;
import static com.oss.bpm.BpmPhysicalDataCreator.createCardForDevice;
import static com.oss.bpm.BpmPhysicalDataCreator.createIPDevice;
import static com.oss.bpm.BpmPhysicalDataCreator.createTechnicalIPDeviceInPlan;
import static com.oss.bpm.BpmPhysicalDataCreator.createValidationResultForRouter;
import static com.oss.bpm.BpmPhysicalDataCreator.deleteBuilding;
import static com.oss.bpm.BpmPhysicalDataCreator.deleteIPDevice;
import static com.oss.bpm.BpmPhysicalDataCreator.getDeviceChassisId;
import static com.oss.bpm.BpmPhysicalDataCreator.isDeviceVisibleInLIVE;
import static com.oss.bpm.BpmPhysicalDataCreator.nextMaxInt;
import static com.oss.bpm.BpmPhysicalDataCreator.nextRandomBuildingName;
import static com.oss.bpm.BpmPhysicalDataCreator.suppressValidationResult;
import static com.oss.bpm.BpmPhysicalDataCreator.updateBuildingInPlan;
import static com.oss.bpm.BpmPhysicalDataCreator.updateIPDeviceSerialNumberInPlan;
import static com.oss.pages.bpm.planning.ProcessDetailsPage.ACTIVATED_STATUS;
import static com.oss.pages.bpm.planning.ProcessDetailsPage.OBJECT_TYPE_ATTRIBUTE_NAME;
import static com.oss.pages.bpm.processinstances.PlannersViewPage.COMPLETED_STATUS;
import static com.oss.pages.bpm.processinstances.creation.ProcessWizardPage.DCP;
import static com.oss.pages.bpm.processinstances.creation.ProcessWizardPage.NRP;


public class PartialIntegrationTest extends BaseTestCase {
    private static final LocalDate TODAY = LocalDate.now();
    private static final PlanningContext LIVE = PlanningContext.live();
    private static final String BUILDING_NAME_TC_MAIN = nextRandomBuildingName();
    private static final String BUILDING_NAME_TC1_1 = nextRandomBuildingName();
    private static final String BUILDING_NAME_TC1_2 = nextRandomBuildingName();
    private static final String BUILDING_NAME_TC1_3 = nextRandomBuildingName();
    private static final String NRP_TC_MAIN_NAME = "Partial_Integration_BPM_Selenium_" + nextMaxInt();
    private static final String NRP_TC5_NAME = "TC_5_Partial_Integration_BPM_Selenium_" + nextMaxInt();
    private static final String DRP_TC5_NAME = "TC_5_Partial_Integration_BPM_Selenium_" + nextMaxInt();
    private static final String DCP_TC5_NAME = "TC_5_Partial_Integration_BPM_Selenium_" + nextMaxInt();
    private static final String IP_TC1_NAME = "TC_1_Partial_Integration_BPM_Selenium_" + nextMaxInt();
    private static final String IP_TC2_NAME = "TC_2_Partial_Integration_BPM_Selenium_" + nextMaxInt();
    private static final String IP_TC3_1_NAME = "TC_3_1_Partial_Integration_BPM_Selenium_" + nextMaxInt();
    private static final String IP_TC3_2_NAME = "TC_3_2_Partial_Integration_BPM_Selenium_" + nextMaxInt();
    private static final String IP_TC4_NAME = "TC_4_Partial_Integration_BPM_Selenium_" + nextMaxInt();
    private static final String IP_TC6_NAME = "TC_6_Partial_Integration_BPM_Selenium_" + nextMaxInt();
    private static final String ROUTER_TC2_1_NAME = "TC_2_1_Partial_I_Router_BPM_Selenium_" + nextMaxInt();
    private static final String ROUTER_TC2_2_NAME = "TC_2_2_Partial_I_Router_BPM_Selenium_" + nextMaxInt();
    private static final String ROUTER_TC2_3_NAME = "TC_2_3_Partial_I_Router_BPM_Selenium_" + nextMaxInt();
    private static final String ROUTER_TC3_1_NAME = "TC_3_1_Partial_I_Router_BPM_Selenium_" + nextMaxInt();
    private static final String ROUTER_TC3_2_NAME = "TC_3_2_Partial_I_Router_BPM_Selenium_" + nextMaxInt();
    private static final String ROUTER_TC3_3_NAME = "TC_3_3_Partial_I_Router_BPM_Selenium_" + nextMaxInt();
    private static final String ROUTER_TC4_1_NAME = "TC_4_1_Partial_I_Router_BPM_Selenium_" + nextMaxInt();
    private static final String ROUTER_TC4_T1_NAME = "TC_4_T1_Partial_I_Router_BPM_Selenium_" + nextMaxInt();
    private static final String ROUTER_TC5_1_NAME = "TC_5_1_Partial_I_Router_BPM_Selenium_" + nextMaxInt();
    private static final String ROUTER_TC5_2_NAME = "TC_5_2_Partial_I_Router_BPM_Selenium_" + nextMaxInt();
    private static final String ROUTER_TC5_3_NAME = "TC_5_3_Partial_I_Router_BPM_Selenium_" + nextMaxInt();
    private static final String ROUTER_TC6_1_NAME = "TC_6_1_Partial_I_Router_BPM_Selenium_" + nextMaxInt();
    private static final String ROUTER_TC6_2_NAME = "TC_6_2_Partial_I_Router_BPM_Selenium_" + nextMaxInt();
    private static final String SELENIUM_VR_TEST_TYPE = "Selenium_VR_TYPE_";
    private static final String SELENIUM_VR_TEST_DESCRIPTION = "Selenium vr test description ";
    private static final String UPDATE_DESCRIPTION = "BPM Selenium Update description";
    private static final String UPDATE_SERIAL_NUMBER = "BPM Selenium Update serialNumber " + nextMaxInt();
    private static final String CHASSIS_IDENTIFIER1 = CHASSIS_NAME + " (%s )";
    private static final String CARD_IDENTIFIER1 = CARD_NAME + " (%s )";
    private static final String CHASSIS_IDENTIFIER = CHASSIS_NAME + " (%s)";
    private static final String CARD_IDENTIFIER = CARD_NAME + " (%s)";
    private static final String DEVICE_IDENTIFIER = "IP Device (%s)";
    private static final String BUILDING_IDENTIFIER = LOCATION_TYPE_BUILDING + " (%s)";
    private static final String CHECKING_PLANNED_OBJECTS_PRESENCE_PATTERN =
            "Object %1$s is not present on Planned Objects Table in '%2$s' test.";
    private static final String CHECKING_OBJECTS_TO_INTEGRATE_PRESENCE_PATTERN =
            "Object %1$s is not present on Objects To Integrate Table in '%2$s' test.";
    private static final String SUCCESS_INTEGRATE_MESSAGE = "You've integrated Planned Changes";
    private static final String INVALID_PI_SYSTEM_MESSAGE_LOG_PATTERN = "Invalid System Message after Partial Integration wizard accept in '%s' test.";
    private static final String INVALID_TASK_COMPLETE_SYSTEM_MESSAGE_LOG_PATTERN = "Invalid System Message after complete task from %s.";
    private static final String INVALID_OBJECT_STATUS_LOG_PATTERN = "Object %1$s has invalid status in '%2$s' test.";
    private static final String INVALID_OBJECTS_TO_INTEGRATE_SIZE_PATTERN = "Invalid size of Objects to Integrate table in '%s' test.";
    private static final String INVALID_PLANNED_OBJECTS_SIZE_PATTERN = "Invalid size of Planned Objects table in '%s' test.";
    private static final String TASK_COMPLETED_MESSAGE = "Task properly completed.";
    private static final String DEVICE_NOT_FOUND_IN_LIVE_LOG_PATTERN = "Device %1$s is not found in LIVE perspective in %2$s.";
    private static final String PARTIAL_INTEGRATION_ACTION_VISIBLE_LOG_PATTERN = "'Integrate planned changes' context action is available for %s.";
    private static final String UNABLE_ACTIVATE_OBJECTS_MESSAGE = "Unable to accept objects to LIVE perspective due to:\n" +
            "Exist blocking validation results related with objects:\n" +
            "{%1$s-%2$s(%3$s)}\n" +
            "See them on Process Details";
    private static final String SUPPRESSION_REASON = "Selenium suppression reason";
    private static final String INVALID_PROCESS_STATUS_LOG_PATTERN = "Invalid Process Status for %s.";
    private static final String DEVICE_MODEL = "7705 SAR-8";
    private static final String DEVICE_SLOT_NAME = "MDA 1";
    private static final String DEVICE_IDENTIFIER1 = IP_DEVICE_NAME + "(%s)";
    private static final String PARTIAL_INTEGRATION_WIZARD_OPEN = "Partial Integration wizard is still opened after '%s' test. It will be closed now.";
    private static final String OBJECTS_NOT_MOVED_TO_IP_MESSAGE = "Some objects are not moved from %s to IPs.";
    private static final String INVALID_OBJECTS_AMOUNT = "Invalid objects amount in Process Details View for %1$s in '%2$s' test.";
    private final Logger log = LoggerFactory.getLogger(PartialIntegrationTest.class);
    private String testName;
    private SoftAssert softAssert;
    private String nrp_Code_TC_MAIN;
    private String buildingId_TC_MAIN;
    //For TC1
    private String ip_Code_TC1;
    private String buildingId_TC1_1;        //plan create
    private String buildingId_TC1_2;        //live create -> plan update
    private String buildingId_TC1_3;        //live create -> plan delete
    //For TC2
    private String ip_Code_TC2;
    private String deviceId_TC2_1;          //D1 plan create
    private String chassisId_TC2_1;
    private String cardId_TC2_1;            //C1 plan create with deviceId_TC2_1
    private String deviceId_TC2_2;          //D2 live create -> plan update
    private String cardId_TC2_2;            //C2 plan add deviceId_TC2_2
    private String deviceId_TC2_3;          //D3 live create -> plan delete
    private String chassisId_TC2_3;
    //For TC3
    private String ip_Code_TC3_1;           //IP1 FDD = today + 2
    private String ip_Code_TC3_2;           //IP1 FDD = today + 3
    private String deviceId_TC3_1;          //D1 plan create (IP1)
    private String chassisId_TC3_1;         //CH1 created with D1 (IP2)
    private String deviceId_TC3_2;          //D2 live create -> plan update (IP1)
    private String deviceId_TC3_3;          //D3 live create -> plan delete (IP2)
    private String chassisId_TC3_3;         //CH3 IP1
    private String cardId_TC3_2;            //C2 plan add deviceId_TC3_2 (IP2)
    //For TC4
    private String ip_Code_TC4;
    private String deviceId_TC4_1;          //D1 plan create
    private String chassisId_TC4_1;
    private String deviceId_TC4_T1;         //D1_1 plan create (TPA for D1)
    //For TC5
    private String nrp_Code_TC5_1;
    private PlanningContext nrp_TC5_1_plan;
    private String dcp_Code_TC5_2;
    private PlanningContext dcp_TC5_2_plan;
    private String drp_Code_TC5_3;
    private PlanningContext drp_TC5_3_plan;
    //For TC6
    private String ip_Code_TC6;
    private String deviceId_TC6_1;          //D1 plan create
    private String chassisId_TC6_1;
    private String deviceId_TC6_2;          //D2 live create -> plan update
    private String cardId_TC6_2;            //C2 plan add deviceId_TC6_2
    private UUID vrId_TC6_1;
    private ValidationResult vr_TC6_1;

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @BeforeClass
    public void prepareObjectsAndProcesses() {
        softAssert = new SoftAssert();
        PlannersViewPage plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        //create NRP,DCP processes
        nrp_Code_TC_MAIN = plannersViewPage.createProcessIPD(NRP_TC_MAIN_NAME, 5L, NRP);
        log.info("Main NRP Code: " + nrp_Code_TC_MAIN);
        PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        nrp_Code_TC5_1 = plannersViewPage.createProcessIPD(NRP_TC5_NAME, 5L, NRP);
        log.info("TC5_1 NRP Code: " + nrp_Code_TC5_1);
        PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        dcp_Code_TC5_2 = plannersViewPage.createProcessIPD(DCP_TC5_NAME, 5L, DCP);
        log.info("TC5_2 DCP Code: " + dcp_Code_TC5_2);
        PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);

        PlanningContext nrp_TC_MAIN_plan = PlanningContext.plan(plannersViewPage.getProjectId(nrp_Code_TC_MAIN));
        log.info("Main NRP project ID: " + nrp_TC_MAIN_plan.getProjectId());
        nrp_TC5_1_plan = PlanningContext.plan(plannersViewPage.getProjectId(nrp_Code_TC5_1));
        log.info("TC5_1 NRP project ID: " + nrp_TC5_1_plan.getProjectId());
        dcp_TC5_2_plan = PlanningContext.plan(plannersViewPage.getProjectId(dcp_Code_TC5_2));
        log.info("TC5_2 DCP project ID: " + dcp_TC5_2_plan.getProjectId());

        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        buildingId_TC_MAIN = createBuilding(BUILDING_NAME_TC_MAIN, LIVE);
        log.info("Main Building id: " + buildingId_TC_MAIN);
        tasksPage.startTask(nrp_Code_TC_MAIN, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        homePage.goToHomePage(driver, BASIC_URL);

        //FOR TC1
        waitForPageToLoad();
        buildingId_TC1_1 = createBuilding(BUILDING_NAME_TC1_1, nrp_TC_MAIN_plan);
        log.info("TC1_1 Building id: " + buildingId_TC1_1);
        buildingId_TC1_2 = createBuilding(BUILDING_NAME_TC1_2, LIVE);
        log.info("TC1_2 Building id: " + buildingId_TC1_2);
        updateBuildingInPlan(BUILDING_NAME_TC1_2, buildingId_TC1_2, UPDATE_DESCRIPTION, nrp_TC_MAIN_plan);
        buildingId_TC1_3 = createBuilding(BUILDING_NAME_TC1_3, LIVE);
        log.info("TC1_3 Building id: " + buildingId_TC1_3);
        deleteBuilding(buildingId_TC1_3, nrp_TC_MAIN_plan);

        //FOR TC2
        deviceId_TC2_1 = createIPDevice(ROUTER_TC2_1_NAME, DEVICE_MODEL, buildingId_TC_MAIN, nrp_TC_MAIN_plan);
        chassisId_TC2_1 = getDeviceChassisId(deviceId_TC2_1, nrp_TC_MAIN_plan);
        cardId_TC2_1 = createCardForDevice(deviceId_TC2_1, DEVICE_SLOT_NAME, nrp_TC_MAIN_plan);
        log.info(String.format("TC2_1 Device ID: %1$s, Chassis ID: %2$s, Card ID: %3$s", deviceId_TC2_1, chassisId_TC2_1, cardId_TC2_1));
        deviceId_TC2_2 = createIPDevice(ROUTER_TC2_2_NAME, DEVICE_MODEL, buildingId_TC_MAIN, LIVE);
        updateIPDeviceSerialNumberInPlan(ROUTER_TC2_2_NAME, deviceId_TC2_2, DEVICE_MODEL, UPDATE_SERIAL_NUMBER, buildingId_TC_MAIN, nrp_TC_MAIN_plan);
        cardId_TC2_2 = createCardForDevice(deviceId_TC2_2, DEVICE_SLOT_NAME, nrp_TC_MAIN_plan);
        log.info(String.format("TC2_2 Device ID: %1$s, Card ID: %2$s", deviceId_TC2_2, cardId_TC2_2));
        deviceId_TC2_3 = createIPDevice(ROUTER_TC2_3_NAME, DEVICE_MODEL, buildingId_TC_MAIN, LIVE);
        chassisId_TC2_3 = getDeviceChassisId(deviceId_TC2_3, LIVE);
        log.info(String.format("TC2_3 Device ID: %1$s, Chassis ID: %2$s", deviceId_TC2_3, chassisId_TC2_3));
        deleteIPDevice(deviceId_TC2_3, nrp_TC_MAIN_plan);

        //FOR TC3
        deviceId_TC3_1 = createIPDevice(ROUTER_TC3_1_NAME, DEVICE_MODEL, buildingId_TC_MAIN, nrp_TC_MAIN_plan);
        chassisId_TC3_1 = getDeviceChassisId(deviceId_TC3_1, nrp_TC_MAIN_plan);
        log.info(String.format("TC3_1 Device ID: %1$s, Chassis ID: %2$s", deviceId_TC3_1, chassisId_TC3_1));
        deviceId_TC3_2 = createIPDevice(ROUTER_TC3_2_NAME, DEVICE_MODEL, buildingId_TC_MAIN, LIVE);
        updateIPDeviceSerialNumberInPlan(ROUTER_TC3_2_NAME, deviceId_TC3_2, DEVICE_MODEL, UPDATE_SERIAL_NUMBER, buildingId_TC_MAIN, nrp_TC_MAIN_plan);
        cardId_TC3_2 = createCardForDevice(deviceId_TC3_2, DEVICE_SLOT_NAME, nrp_TC_MAIN_plan);
        log.info(String.format("TC3_2 Device ID: %1$s, Card ID: %2$s", deviceId_TC3_2, cardId_TC3_2));
        deviceId_TC3_3 = createIPDevice(ROUTER_TC3_3_NAME, DEVICE_MODEL, buildingId_TC_MAIN, LIVE);
        chassisId_TC3_3 = getDeviceChassisId(deviceId_TC3_3, LIVE);
        log.info(String.format("TC3_3 Device ID: %1$s, Chassis ID: %2$s", deviceId_TC3_3, chassisId_TC3_3));
        deleteIPDevice(deviceId_TC3_3, nrp_TC_MAIN_plan);

        //FOR TC4
        deviceId_TC4_1 = createIPDevice(ROUTER_TC4_1_NAME, DEVICE_MODEL, buildingId_TC_MAIN, nrp_TC_MAIN_plan);
        chassisId_TC4_1 = getDeviceChassisId(deviceId_TC4_1, nrp_TC_MAIN_plan);
        deviceId_TC4_T1 = createTechnicalIPDeviceInPlan(ROUTER_TC4_T1_NAME, DEVICE_MODEL, buildingId_TC_MAIN, deviceId_TC4_1, nrp_TC_MAIN_plan);
        log.info(String.format("TC4_1 Device ID: %1$s, Chassis ID: %2$s, Technical Device ID: %3$s", deviceId_TC4_1, chassisId_TC4_1, deviceId_TC4_T1));

        //FOR TC6
        deviceId_TC6_1 = createIPDevice(ROUTER_TC6_1_NAME, DEVICE_MODEL, buildingId_TC_MAIN, nrp_TC_MAIN_plan);
        chassisId_TC6_1 = getDeviceChassisId(deviceId_TC6_1, nrp_TC_MAIN_plan);
        log.info(String.format("TC6_1 Device ID: %1$s, Chassis ID: %2$s", deviceId_TC6_1, chassisId_TC6_1));
        deviceId_TC6_2 = createIPDevice(ROUTER_TC6_2_NAME, DEVICE_MODEL, buildingId_TC_MAIN, LIVE);
        updateIPDeviceSerialNumberInPlan(ROUTER_TC6_2_NAME, deviceId_TC6_2, DEVICE_MODEL, UPDATE_SERIAL_NUMBER, buildingId_TC_MAIN, nrp_TC_MAIN_plan);
        cardId_TC6_2 = createCardForDevice(deviceId_TC6_2, DEVICE_SLOT_NAME, nrp_TC_MAIN_plan);
        log.info(String.format("TC6_2 Device ID: %1$s, Card ID: %2$s", deviceId_TC6_2, cardId_TC6_2));

        TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.proceedNRPToReadyForIntegrationTask(nrp_Code_TC_MAIN);

        SetupIntegrationProperties setupIntegrationProperties_IP_TC1 = SetupIntegrationProperties.builder()
                .integrationProcessName(IP_TC1_NAME)
                .finishedDueDate(TODAY)
                .objectIdentifiers(Arrays.asList(BUILDING_NAME_TC1_1, BUILDING_NAME_TC1_2, BUILDING_NAME_TC1_3))
                .build();

        SetupIntegrationProperties setupIntegrationProperties_IP_TC2 = SetupIntegrationProperties.builder()
                .integrationProcessName(IP_TC2_NAME)
                .finishedDueDate(TODAY)
                .objectIdentifiers(Arrays.asList(
                        ROUTER_TC2_1_NAME,
                        String.format(CHASSIS_IDENTIFIER1, chassisId_TC2_1),
                        String.format(CARD_IDENTIFIER1, cardId_TC2_1),
                        ROUTER_TC2_2_NAME,
                        String.format(CARD_IDENTIFIER1, cardId_TC2_2),
                        ROUTER_TC2_3_NAME,
                        String.format(CHASSIS_IDENTIFIER1, chassisId_TC2_3)))
                .build();

        SetupIntegrationProperties setupIntegrationProperties_IP_TC3_1 = SetupIntegrationProperties.builder()
                .integrationProcessName(IP_TC3_1_NAME)
                .finishedDueDate(TODAY.plusDays(2L))
                .objectIdentifiers(Arrays.asList(
                        ROUTER_TC3_1_NAME,
                        ROUTER_TC3_2_NAME,
                        String.format(CHASSIS_IDENTIFIER1, chassisId_TC3_3)))
                .build();

        SetupIntegrationProperties setupIntegrationProperties_IP_TC3_2 = SetupIntegrationProperties.builder()
                .integrationProcessName(IP_TC3_2_NAME)
                .finishedDueDate(TODAY.plusDays(3L))
                .objectIdentifiers(Arrays.asList(
                        String.format(CHASSIS_IDENTIFIER1, chassisId_TC3_1),
                        String.format(CARD_IDENTIFIER1, cardId_TC3_2),
                        ROUTER_TC3_3_NAME))
                .build();

        SetupIntegrationProperties setupIntegrationProperties_IP_TC4 = SetupIntegrationProperties.builder()
                .integrationProcessName(IP_TC4_NAME)
                .finishedDueDate(TODAY)
                .objectIdentifiers(Arrays.asList(ROUTER_TC4_1_NAME, String.format(CHASSIS_IDENTIFIER1, chassisId_TC4_1)))
                .build();

        SetupIntegrationProperties setupIntegrationProperties_IP_TC6 = SetupIntegrationProperties.builder()
                .integrationProcessName(IP_TC6_NAME)
                .finishedDueDate(TODAY)
                .objectIdentifiers(Arrays.asList(
                        ROUTER_TC6_1_NAME,
                        String.format(CHASSIS_IDENTIFIER1, chassisId_TC6_1),
                        ROUTER_TC6_2_NAME,
                        String.format(CARD_IDENTIFIER1, cardId_TC6_2)))
                .build();

        List<String> ipCodes = tasksPage.setupIntegration(nrp_Code_TC_MAIN, NRP_TC_MAIN_NAME,
                Arrays.asList(
                        setupIntegrationProperties_IP_TC1,
                        setupIntegrationProperties_IP_TC2,
                        setupIntegrationProperties_IP_TC3_1,
                        setupIntegrationProperties_IP_TC3_2,
                        setupIntegrationProperties_IP_TC4,
                        setupIntegrationProperties_IP_TC6));
        ipCodes.forEach(ipCode -> log.info(String.format("IP Code: %s \n", ipCode)));
        ip_Code_TC1 = ipCodes.get(0);
        ip_Code_TC2 = ipCodes.get(1);
        ip_Code_TC3_1 = ipCodes.get(2);
        ip_Code_TC3_2 = ipCodes.get(3);
        ip_Code_TC4 = ipCodes.get(4);
        ip_Code_TC6 = ipCodes.get(5);
        ProcessDetailsPage processDetailsPage = tasksPage.clickPlanViewButton();
        Assert.assertEquals(processDetailsPage.getObjectsAmount(), 0, String.format(OBJECTS_NOT_MOVED_TO_IP_MESSAGE, nrp_Code_TC_MAIN));
        processDetailsPage.closeProcessDetailsPromt();
        tasksPage.completeTask(nrp_Code_TC_MAIN, TasksPageV2.READY_FOR_INTEGRATION_TASK);

        //FOR TC5
        tasksPage.startTask(dcp_Code_TC5_2, TasksPageV2.CORRECT_DATA_TASK);
        waitForPageToLoad();
        //D2 plan create in dcp
        createIPDevice(ROUTER_TC5_2_NAME, DEVICE_MODEL, buildingId_TC_MAIN, dcp_TC5_2_plan);
        tasksPage.startTask(nrp_Code_TC5_1, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        waitForPageToLoad();
        //D1 plan create in nrp
        createIPDevice(ROUTER_TC5_1_NAME, DEVICE_MODEL, buildingId_TC_MAIN, nrp_TC5_1_plan);
        drp_Code_TC5_3 = tasksPage.createDRPProcess(nrp_Code_TC5_1, TasksPageV2.HIGH_LEVEL_PLANNING_TASK, DRP_TC5_NAME);
        log.info(String.format("TC5_3 DRP Code: %s", drp_Code_TC5_3));
        tasksPage.startTask(drp_Code_TC5_3, TasksPageV2.PLANNING_TASK);
        waitForPageToLoad();
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        drp_TC5_3_plan = PlanningContext.plan(plannersViewPage.getProjectId(drp_Code_TC5_3));
        //D3 plan create in drp
        createIPDevice(ROUTER_TC5_3_NAME, DEVICE_MODEL, buildingId_TC_MAIN, drp_TC5_3_plan);

        //GET IP6 plan context
        PlanningContext ip_TC6_plan = PlanningContext.plan(plannersViewPage.getProjectId(ip_Code_TC6));

        //create VR for TC6
        vr_TC6_1 = ValidationResult.builder()
                .type(SELENIUM_VR_TEST_TYPE + nextMaxInt())
                .description(SELENIUM_VR_TEST_DESCRIPTION + nextMaxInt())
                .build();

        ValidationResult vr_TC6_2 = ValidationResult.builder()
                .type(SELENIUM_VR_TEST_TYPE + nextMaxInt())
                .description(SELENIUM_VR_TEST_DESCRIPTION + nextMaxInt())
                .severity(ValidationResult.Severity.LOW)
                .build();
        vrId_TC6_1 = createValidationResultForRouter(deviceId_TC6_1, vr_TC6_1, ip_TC6_plan);
        log.info("TC6_1 VR uuid: " + vrId_TC6_1);
        UUID vrId_TC6_2 = createValidationResultForRouter(deviceId_TC6_2, vr_TC6_2, ip_TC6_plan);
        log.info("TC6_2 VR uuid: " + vrId_TC6_2);
    }

    @BeforeMethod
    public void getTestName(ITestResult iTestResult) {
        testName = iTestResult.getMethod().getConstructorOrMethod().getName();
    }


    @Test(priority = 1, description = "Integrate Object without prerequisites")
    @Description("User is able to integrate to LIVE objects without any prerequisites.")
    public void integrateObjectsWithoutAnyPrerequisites() {
        /*
                Objects:
                - Building 1_1 CREATE
                - Building 1_2 UPDATE
                - Building 1_3 DELETE
         */
        PlannersViewPage plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        PartialIntegrationWizardPage partialIntegrationWizardPage =
                plannersViewPage.selectProcess(ip_Code_TC1).openIntegratePlannedChangesWizard();

        List<String> plannedObjectsIdentifiers = partialIntegrationWizardPage.getPlannedObjectsIdentifiers();
        Assert.assertEquals(plannedObjectsIdentifiers.size(), 3,
                String.format(INVALID_PLANNED_OBJECTS_SIZE_PATTERN, testName));
        assertPlannedObjectPresence(plannedObjectsIdentifiers, buildingId_TC1_1, BUILDING_IDENTIFIER);
        assertPlannedObjectPresence(plannedObjectsIdentifiers, buildingId_TC1_2, BUILDING_IDENTIFIER);
        assertPlannedObjectPresence(plannedObjectsIdentifiers, buildingId_TC1_3, BUILDING_IDENTIFIER);

        partialIntegrationWizardPage.moveObjectsToIntegration(
                Arrays.asList(BUILDING_NAME_TC1_1, BUILDING_NAME_TC1_2, BUILDING_NAME_TC1_3));

        List<String> objectsToIntegrateIdentifiers = partialIntegrationWizardPage.getObjectsToIntegrationIdentifiers();
        Assert.assertEquals(objectsToIntegrateIdentifiers.size(), 3,
                String.format(INVALID_OBJECTS_TO_INTEGRATE_SIZE_PATTERN, testName));
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, buildingId_TC1_1, BUILDING_IDENTIFIER);
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, buildingId_TC1_2, BUILDING_IDENTIFIER);
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, buildingId_TC1_3, BUILDING_IDENTIFIER);

        partialIntegrationWizardPage.apply();
        assertSystemMessage(SUCCESS_INTEGRATE_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_PI_SYSTEM_MESSAGE_LOG_PATTERN, testName));

        //check Object statuses
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(ip_Code_TC1, TasksPageV2.SCOPE_DEFINITION_TASK);
        ProcessDetailsPage processDetailsPage =
                tasksPage.findTask(ip_Code_TC1, TasksPageV2.SCOPE_DEFINITION_TASK).clickPlanViewButton();
        assertActivatedObjectStatus(buildingId_TC1_1, BUILDING_IDENTIFIER);
        assertActivatedObjectStatus(buildingId_TC1_2, BUILDING_IDENTIFIER);
        assertActivatedObjectStatus(buildingId_TC1_3, BUILDING_IDENTIFIER);
        processDetailsPage.closeProcessDetailsPromt();

        //Complete IP
        completeIP(ip_Code_TC1);
    }

    @Test(priority = 2, description = "Integrate Objects with prerequisites in the same project.")
    @Description("User is able to integrate to LIVE objects with their subsequents/prerequisites in the same project.")
    public void integrateObjectsWithInternalPrerequisites() {
        /*
                Objects:
                - Router 2_1 CREATE
                    - Chassis 2_1 CREATE
                        - Card 2_1 CREATE
                - Router 2_2 UPDATE
                - Card 2_2 CREATE
                - Chassis 2_3 DELETE
                    ROUTER 2_3 DELETE
         */
        PlannersViewPage plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        PartialIntegrationWizardPage partialIntegrationWizardPage = plannersViewPage.selectProcess(ip_Code_TC2).openIntegratePlannedChangesWizard();

        List<String> plannedObjectsIdentifiers = partialIntegrationWizardPage.getPlannedObjectsIdentifiers();
        Assert.assertEquals(plannedObjectsIdentifiers.size(), 4,
                String.format(INVALID_PLANNED_OBJECTS_SIZE_PATTERN, testName));
        assertPlannedObjectPresence(plannedObjectsIdentifiers, deviceId_TC2_1, DEVICE_IDENTIFIER);
        assertPlannedObjectPresence(plannedObjectsIdentifiers, deviceId_TC2_2, DEVICE_IDENTIFIER);
        assertPlannedObjectPresence(plannedObjectsIdentifiers, cardId_TC2_2, CARD_IDENTIFIER);
        assertPlannedObjectPresence(plannedObjectsIdentifiers, chassisId_TC2_3, CHASSIS_IDENTIFIER);

        partialIntegrationWizardPage.moveObjectToIntegration(cardId_TC2_1);

        // check if the 2_1 router and chassis moved also with card
        List<String> objectsToIntegrateIdentifiers = partialIntegrationWizardPage.getObjectsToIntegrationIdentifiers();
        Assert.assertEquals(objectsToIntegrateIdentifiers.size(), 3,
                String.format(INVALID_OBJECTS_TO_INTEGRATE_SIZE_PATTERN, testName));
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, cardId_TC2_1, CARD_IDENTIFIER);
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, chassisId_TC2_1, CHASSIS_IDENTIFIER);
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, deviceId_TC2_1, DEVICE_IDENTIFIER);

        //activate router, chassis and card 2_1
        partialIntegrationWizardPage.apply();
        assertSystemMessage(SUCCESS_INTEGRATE_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_PI_SYSTEM_MESSAGE_LOG_PATTERN, testName));

        partialIntegrationWizardPage = plannersViewPage.selectProcess(ip_Code_TC2).openIntegratePlannedChangesWizard();
        partialIntegrationWizardPage.moveObjectsToIntegration(Arrays.asList(deviceId_TC2_2, chassisId_TC2_3));

        // check if the 2_2 router and 2_3 chassis moved alone
        objectsToIntegrateIdentifiers = partialIntegrationWizardPage.getObjectsToIntegrationIdentifiers();
        Assert.assertEquals(objectsToIntegrateIdentifiers.size(), 2,
                String.format(INVALID_OBJECTS_TO_INTEGRATE_SIZE_PATTERN, testName));
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, deviceId_TC2_2, DEVICE_IDENTIFIER);
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, chassisId_TC2_3, CHASSIS_IDENTIFIER);

        //activate router 2_2 and chassis 2_3
        partialIntegrationWizardPage.apply();
        assertSystemMessage(SUCCESS_INTEGRATE_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_PI_SYSTEM_MESSAGE_LOG_PATTERN, testName));

        partialIntegrationWizardPage = plannersViewPage.selectProcess(ip_Code_TC2).openIntegratePlannedChangesWizard();

        //activate rest objects
        partialIntegrationWizardPage.moveObjectsToIntegration(Arrays.asList(cardId_TC2_2, deviceId_TC2_3));
        objectsToIntegrateIdentifiers = partialIntegrationWizardPage.getObjectsToIntegrationIdentifiers();
        Assert.assertEquals(objectsToIntegrateIdentifiers.size(), 2,
                String.format(INVALID_OBJECTS_TO_INTEGRATE_SIZE_PATTERN, testName));
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, cardId_TC2_2, CARD_IDENTIFIER);
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, deviceId_TC2_3, DEVICE_IDENTIFIER);

        partialIntegrationWizardPage.apply();
        assertSystemMessage(SUCCESS_INTEGRATE_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_PI_SYSTEM_MESSAGE_LOG_PATTERN, testName));

        //check Object statuses
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(ip_Code_TC2, TasksPageV2.SCOPE_DEFINITION_TASK);
        ProcessDetailsPage processDetailsPage =
                tasksPage.findTask(ip_Code_TC2, TasksPageV2.SCOPE_DEFINITION_TASK).clickPlanViewButton();
        assertActivatedObjectStatus(deviceId_TC2_1, DEVICE_IDENTIFIER);
        assertActivatedObjectStatus(chassisId_TC2_1, CHASSIS_IDENTIFIER);
        assertActivatedObjectStatus(cardId_TC2_1, CARD_IDENTIFIER);
        assertActivatedObjectStatus(deviceId_TC2_2, DEVICE_IDENTIFIER);
        assertActivatedObjectStatus(cardId_TC2_2, CARD_IDENTIFIER);
        assertActivatedObjectStatus(deviceId_TC2_3, DEVICE_IDENTIFIER);
        assertActivatedObjectStatus(chassisId_TC2_3, CHASSIS_IDENTIFIER);
        processDetailsPage.closeProcessDetailsPromt();

        //Complete IP
        completeIP(ip_Code_TC2);
    }

    @Test(priority = 3, description = "Integrate objects with prerequisites/subsequents in other project.")
    @Description("User is not able to integrate to LIVE objects which have prerequisites in the other planning project, but user can integrate objects which have subsequents in the other project.")
    public void integrateObjectsWithExternalPrerequisites() {
        /*
                IP 3_1 (FDD = today+2) Objects:
                - Router 3_1 CREATE (chassis 3_1 prerequisite)
                - Router 3_2 UPDATE
                - Chassis 3_3 DELETE (router 3_3 prerequisite)

                IP 3_2 (FDD = today+3) Objects:
                - Chassis 3_1 CREATE (router 3_1 subsequent)
                - Card 3_2 CREATE
                - Router 3_3 DELETE (chassis 3_3 subsequent)
         */
        PlannersViewPage plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);

        // check if subsequents: chassis 3_1 and router 3_3 are not present in wizard
        PartialIntegrationWizardPage partialIntegrationWizardPage = plannersViewPage.selectProcess(ip_Code_TC3_2).openIntegratePlannedChangesWizard();
        List<String> plannedObjectsIdentifiers = partialIntegrationWizardPage.getPlannedObjectsIdentifiers();
        Assert.assertEquals(plannedObjectsIdentifiers.size(), 1,
                String.format(INVALID_PLANNED_OBJECTS_SIZE_PATTERN, testName));
        assertPlannedObjectPresence(plannedObjectsIdentifiers, cardId_TC3_2, CARD_IDENTIFIER);
        partialIntegrationWizardPage.closePrompt();

        //check if prerequisites: router 3_1 and chassis 3_3 are visible in wizard
        partialIntegrationWizardPage = plannersViewPage.selectProcess(ip_Code_TC3_1).openIntegratePlannedChangesWizard();
        plannedObjectsIdentifiers = partialIntegrationWizardPage.getPlannedObjectsIdentifiers();
        Assert.assertEquals(plannedObjectsIdentifiers.size(), 3,
                String.format(INVALID_PLANNED_OBJECTS_SIZE_PATTERN, testName));
        assertPlannedObjectPresence(plannedObjectsIdentifiers, deviceId_TC3_1, DEVICE_IDENTIFIER);
        assertPlannedObjectPresence(plannedObjectsIdentifiers, deviceId_TC3_2, DEVICE_IDENTIFIER);
        assertPlannedObjectPresence(plannedObjectsIdentifiers, chassisId_TC3_3, CHASSIS_IDENTIFIER);

        //activate objects from IP1
        partialIntegrationWizardPage.moveObjectsToIntegration(Arrays.asList(deviceId_TC3_1, deviceId_TC3_2, chassisId_TC3_3));
        List<String> objectsToIntegrateIdentifiers = partialIntegrationWizardPage.getObjectsToIntegrationIdentifiers();
        Assert.assertEquals(objectsToIntegrateIdentifiers.size(), 3,
                String.format(INVALID_OBJECTS_TO_INTEGRATE_SIZE_PATTERN, testName));
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, deviceId_TC3_1, DEVICE_IDENTIFIER);
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, deviceId_TC3_2, DEVICE_IDENTIFIER);
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, chassisId_TC3_3, CHASSIS_IDENTIFIER);
        partialIntegrationWizardPage.apply();
        assertSystemMessage(SUCCESS_INTEGRATE_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_PI_SYSTEM_MESSAGE_LOG_PATTERN, testName));

        // check if subsequents: chassis 3_1 and router 3_3 are visible now in wizard
        partialIntegrationWizardPage = plannersViewPage.selectProcess(ip_Code_TC3_2).openIntegratePlannedChangesWizard();
        plannedObjectsIdentifiers = partialIntegrationWizardPage.getPlannedObjectsIdentifiers();
        Assert.assertEquals(plannedObjectsIdentifiers.size(), 3,
                String.format(INVALID_PLANNED_OBJECTS_SIZE_PATTERN, testName));
        assertPlannedObjectPresence(plannedObjectsIdentifiers, chassisId_TC3_1, CHASSIS_IDENTIFIER);
        assertPlannedObjectPresence(plannedObjectsIdentifiers, cardId_TC3_2, CARD_IDENTIFIER);
        assertPlannedObjectPresence(plannedObjectsIdentifiers, deviceId_TC3_3, DEVICE_IDENTIFIER);

        //activate objects from IP2
        partialIntegrationWizardPage.moveObjectsToIntegration(Arrays.asList(chassisId_TC3_1, cardId_TC3_2, deviceId_TC3_3));
        objectsToIntegrateIdentifiers = partialIntegrationWizardPage.getObjectsToIntegrationIdentifiers();
        Assert.assertEquals(objectsToIntegrateIdentifiers.size(), 3,
                String.format(INVALID_OBJECTS_TO_INTEGRATE_SIZE_PATTERN, testName));
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, chassisId_TC3_1, CHASSIS_IDENTIFIER);
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, cardId_TC3_2, CARD_IDENTIFIER);
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, deviceId_TC3_3, DEVICE_IDENTIFIER);
        partialIntegrationWizardPage.apply();
        assertSystemMessage(SUCCESS_INTEGRATE_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_PI_SYSTEM_MESSAGE_LOG_PATTERN, testName));

        //check Object statuses and complete IP1
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(ip_Code_TC3_1, TasksPageV2.SCOPE_DEFINITION_TASK);
        ProcessDetailsPage processDetailsPage =
                tasksPage.findTask(ip_Code_TC3_1, TasksPageV2.SCOPE_DEFINITION_TASK).clickPlanViewButton();
        assertActivatedObjectStatus(deviceId_TC3_1, DEVICE_IDENTIFIER);
        assertActivatedObjectStatus(deviceId_TC3_2, DEVICE_IDENTIFIER);
        assertActivatedObjectStatus(chassisId_TC3_3, CHASSIS_IDENTIFIER);
        processDetailsPage.closeProcessDetailsPromt();
        completeIP(ip_Code_TC3_1);

        //check Object statuses and complete IP2
        tasksPage.startTask(ip_Code_TC3_2, TasksPageV2.SCOPE_DEFINITION_TASK);
        processDetailsPage =
                tasksPage.findTask(ip_Code_TC3_2, TasksPageV2.SCOPE_DEFINITION_TASK).clickPlanViewButton().clearAllColumnFilters();
        assertActivatedObjectStatus(chassisId_TC3_1, CHASSIS_IDENTIFIER);
        assertActivatedObjectStatus(cardId_TC3_2, CARD_IDENTIFIER);
        assertActivatedObjectStatus(deviceId_TC3_3, DEVICE_IDENTIFIER);
        processDetailsPage.closeProcessDetailsPromt();
        completeIP(ip_Code_TC3_2);
    }

    @Test(priority = 4, description = "Integrate objects with technical Planned Actions.")
    @Description("User is able to integrate to LIVE objects which have technical PAs.")
    public void integrateObjectsWithTechnicalPlannedActions() {
        /*
                Objects:
                - Router 2_1 CREATE
                    - Chassis 2_1 CREATE
                - Router 2_2 CREATE (hidden, as TechnicalPA of Router 2_1)
                    - Chassis 2_1 CREATE (hidden, as TechnicalPA of Router 2_1)
         */
        PlannersViewPage plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        PartialIntegrationWizardPage partialIntegrationWizardPage = plannersViewPage.selectProcess(ip_Code_TC4).openIntegratePlannedChangesWizard();

        //check if technical PAs are not visible in Planned Objects table
        List<String> plannedObjectsIdentifiers = partialIntegrationWizardPage.getPlannedObjectsIdentifiers();
        Assert.assertEquals(plannedObjectsIdentifiers.size(), 1,
                String.format(INVALID_PLANNED_OBJECTS_SIZE_PATTERN, testName));
        assertPlannedObjectPresence(plannedObjectsIdentifiers, deviceId_TC4_1, DEVICE_IDENTIFIER);

        //activate objects
        partialIntegrationWizardPage.moveObjectToIntegration(chassisId_TC4_1);
        List<String> objectsToIntegrateIdentifiers = partialIntegrationWizardPage.getObjectsToIntegrationIdentifiers();
        Assert.assertEquals(objectsToIntegrateIdentifiers.size(), 2,
                String.format(INVALID_OBJECTS_TO_INTEGRATE_SIZE_PATTERN, testName));
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, deviceId_TC4_1, DEVICE_IDENTIFIER);
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, chassisId_TC4_1, CHASSIS_IDENTIFIER);
        partialIntegrationWizardPage.apply();
        assertSystemMessage(SUCCESS_INTEGRATE_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_PI_SYSTEM_MESSAGE_LOG_PATTERN, testName));

        //check objects status
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(ip_Code_TC4, TasksPageV2.SCOPE_DEFINITION_TASK);
        ProcessDetailsPage processDetailsPage =
                tasksPage.findTask(ip_Code_TC4, TasksPageV2.SCOPE_DEFINITION_TASK).clickPlanViewButton();
        softAssert.assertEquals(processDetailsPage.getObjectsAmount(), 2,
                String.format(INVALID_OBJECTS_AMOUNT, ip_Code_TC4, testName));
        assertActivatedObjectStatus(deviceId_TC4_1, DEVICE_IDENTIFIER);
        assertActivatedObjectStatus(chassisId_TC4_1, CHASSIS_IDENTIFIER);
        processDetailsPage.closeProcessDetailsPromt();

        //check if Technical router 2_2 is activated too
        softAssert.assertTrue(isDeviceVisibleInLIVE(ROUTER_TC4_T1_NAME, buildingId_TC_MAIN),
                String.format(DEVICE_NOT_FOUND_IN_LIVE_LOG_PATTERN, ROUTER_TC4_T1_NAME, testName));

        completeIP(ip_Code_TC4);
    }

    @Test(priority = 5, description = " Try to Integrate objects to LIVE from NRP, DRP, DCP processes.")
    @Description("User is not able to integrate to LIVE objects which are processing in NRP, DRP, DCP process.")
    public void TryToIntegrateObjectsFromDRP_NRP_DCP() {
        PlannersViewPage plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        softAssert.assertFalse(plannersViewPage.selectProcess(drp_Code_TC5_3).isIntegratePlannedChangesActionVisible(),
                String.format(PARTIAL_INTEGRATION_ACTION_VISIBLE_LOG_PATTERN, drp_Code_TC5_3));
        softAssert.assertFalse(plannersViewPage.selectProcess(nrp_Code_TC5_1).isIntegratePlannedChangesActionVisible(),
                String.format(PARTIAL_INTEGRATION_ACTION_VISIBLE_LOG_PATTERN, nrp_Code_TC5_1));
        softAssert.assertFalse(plannersViewPage.selectProcess(dcp_Code_TC5_2).isIntegratePlannedChangesActionVisible(),
                String.format(PARTIAL_INTEGRATION_ACTION_VISIBLE_LOG_PATTERN, dcp_Code_TC5_2));
    }

    @Test(priority = 6, description = "Integrate objects with Validation Results.")
    @Description("User is not able to integrate to LIVE objects which have unresolved Validation Results.")
    public void integrateObjectsWithValidationResults() {
        /*
                Objects:
                - Router 6_1 CREATE (VR HIGH)
                    - Chassis 6_1 CREATE
                - Router 6_2 UPDATE (VR LOW)
                - Card 6_2 CREATE
         */
        PlannersViewPage plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        PartialIntegrationWizardPage partialIntegrationWizardPage = plannersViewPage.selectProcess(ip_Code_TC6).openIntegratePlannedChangesWizard();

        //try to activate Router 6_1 (Error message should appear)
        partialIntegrationWizardPage.moveObjectToIntegration(deviceId_TC6_1);
        List<String> objectsToIntegrateIdentifiers = partialIntegrationWizardPage.getObjectsToIntegrationIdentifiers();
        Assert.assertEquals(objectsToIntegrateIdentifiers.size(), 1,
                String.format(INVALID_OBJECTS_TO_INTEGRATE_SIZE_PATTERN, testName));
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, deviceId_TC6_1, DEVICE_IDENTIFIER);
        partialIntegrationWizardPage.partialIntegrationWizard.clickButtonById(PartialIntegrationWizardPage.APPLY_BUTTON_ID);

        assertSystemMessage(String.format(UNABLE_ACTIVATE_OBJECTS_MESSAGE, String.format(DEVICE_IDENTIFIER1, deviceId_TC6_1),
                        vr_TC6_1.getType(), vr_TC6_1.getDescription()),
                SystemMessageContainer.MessageType.DANGER, String.format(INVALID_PI_SYSTEM_MESSAGE_LOG_PATTERN, testName));

        //suppress vr 6_1 and try again
        suppressValidationResult(vrId_TC6_1, SUPPRESSION_REASON);
        partialIntegrationWizardPage.apply();
        assertSystemMessage(SUCCESS_INTEGRATE_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_PI_SYSTEM_MESSAGE_LOG_PATTERN, testName));

        //activate rest objects
        partialIntegrationWizardPage = plannersViewPage.selectProcess(ip_Code_TC6).openIntegratePlannedChangesWizard();
        partialIntegrationWizardPage.moveObjectsToIntegration(Arrays.asList(chassisId_TC6_1, deviceId_TC6_2, cardId_TC6_2));
        objectsToIntegrateIdentifiers = partialIntegrationWizardPage.getObjectsToIntegrationIdentifiers();
        Assert.assertEquals(objectsToIntegrateIdentifiers.size(), 3,
                String.format(INVALID_OBJECTS_TO_INTEGRATE_SIZE_PATTERN, testName));
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, chassisId_TC6_1, CHASSIS_IDENTIFIER);
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, deviceId_TC6_2, DEVICE_IDENTIFIER);
        assertObjectToIntegratePresence(objectsToIntegrateIdentifiers, cardId_TC6_2, CARD_IDENTIFIER);
        partialIntegrationWizardPage.apply();
        assertSystemMessage(SUCCESS_INTEGRATE_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_PI_SYSTEM_MESSAGE_LOG_PATTERN, testName));

        //check objects status and complete IP
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(ip_Code_TC6, TasksPageV2.SCOPE_DEFINITION_TASK);
        ProcessDetailsPage processDetailsPage =
                tasksPage.findTask(ip_Code_TC6, TasksPageV2.SCOPE_DEFINITION_TASK).clickPlanViewButton();
        assertActivatedObjectStatus(deviceId_TC6_1, DEVICE_IDENTIFIER);
        assertActivatedObjectStatus(chassisId_TC6_1, CHASSIS_IDENTIFIER);
        assertActivatedObjectStatus(deviceId_TC6_2, DEVICE_IDENTIFIER);
        assertActivatedObjectStatus(cardId_TC6_2, CARD_IDENTIFIER);
        processDetailsPage.closeProcessDetailsPromt();
        completeIP(ip_Code_TC6);
    }

    @Test(priority = 7, description = "Complete NRP Process", dependsOnMethods = {
            "integrateObjectsWithoutAnyPrerequisites", "integrateObjectsWithInternalPrerequisites",
            "integrateObjectsWithExternalPrerequisites", "integrateObjectsWithTechnicalPlannedActions",
            "integrateObjectsWithValidationResults"})
    @Description("User is able to complete NRP Process without any planned objects.")
    public void completeNRP() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(nrp_Code_TC_MAIN, TasksPageV2.VERIFICATION_TASK);

        //check if activated objects planned actions stayed in IP processes
        ProcessDetailsPage processDetailsPage = tasksPage.clickPlanViewButton();
        softAssert.assertEquals(processDetailsPage.getObjectsAmount(), 0,
                String.format(INVALID_OBJECTS_AMOUNT, nrp_Code_TC_MAIN, testName));
        processDetailsPage.closeProcessDetailsPromt();
        tasksPage.completeTask(nrp_Code_TC_MAIN, TasksPageV2.VERIFICATION_TASK);
        assertSystemMessage(TASK_COMPLETED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_COMPLETE_SYSTEM_MESSAGE_LOG_PATTERN, nrp_Code_TC_MAIN));

        //Assert NRP main complete Status
        PlannersViewPage plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        Assert.assertEquals(plannersViewPage.getProcessState(nrp_Code_TC_MAIN), COMPLETED_STATUS,
                String.format(INVALID_PROCESS_STATUS_LOG_PATTERN, nrp_Code_TC_MAIN));
    }

    @Test(priority = 8, description = "Checking asserts")
    @Description("Checking asserts")
    public void checkSoftAsserts() {
        softAssert.assertAll();
    }

    @AfterMethod
    public void closeWizard() {
        if (PartialIntegrationWizardPage.isWizardVisible(driver)) {
            new PartialIntegrationWizardPage(driver).closePrompt();
            log.warn(String.format(PARTIAL_INTEGRATION_WIZARD_OPEN, testName));
        }
    }

    @AfterClass
    public void clean() {
        PlannersViewPage plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);

        //TC1 clean
        deleteBuilding(buildingId_TC1_1, LIVE);
        deleteBuilding(buildingId_TC1_2, LIVE);

        //TC2 clean
        deleteIPDevice(deviceId_TC2_1, LIVE);
        deleteIPDevice(deviceId_TC2_2, LIVE);

        //TC3 clean
        deleteIPDevice(deviceId_TC3_1, LIVE);
        deleteIPDevice(deviceId_TC3_2, LIVE);

        //TC4 clean
        deleteIPDevice(deviceId_TC4_1, LIVE);
        deleteIPDevice(deviceId_TC4_T1, LIVE);

        //TC5 clean
        cancelProject(String.valueOf(drp_TC5_3_plan.getProjectId()));
        cancelProject(String.valueOf(nrp_TC5_1_plan.getProjectId()));
        cancelProject(String.valueOf(dcp_TC5_2_plan.getProjectId()));
        plannersViewPage.selectProcess(drp_Code_TC5_3).terminateProcess("CLEAN");
        plannersViewPage.selectProcess(nrp_Code_TC5_1).terminateProcess("CLEAN");
        plannersViewPage.selectProcess(dcp_Code_TC5_2).terminateProcess("CLEAN");

        //TC6 clean
        deleteIPDevice(deviceId_TC6_1, LIVE);
        deleteIPDevice(deviceId_TC6_2, LIVE);

    }

    private void completeIP(String ipCode) {
        new TasksPageV2(driver).completeIP(ipCode);
        assertSystemMessage(TASK_COMPLETED_MESSAGE, SystemMessageContainer.MessageType.SUCCESS,
                String.format(INVALID_TASK_COMPLETE_SYSTEM_MESSAGE_LOG_PATTERN, ipCode));
    }

    private void assertObjectToIntegratePresence(List<String> objetsIdentifiers, String objectId, String identifier) {
        Assert.assertTrue(objetsIdentifiers.contains(String.format(identifier, objectId)),
                String.format(CHECKING_OBJECTS_TO_INTEGRATE_PRESENCE_PATTERN, String.format(identifier, objectId), testName));
    }

    private void assertPlannedObjectPresence(List<String> objetsIdentifiers, String objectId, String identifier) {
        Assert.assertTrue(objetsIdentifiers.contains(String.format(identifier, objectId)),
                String.format(CHECKING_PLANNED_OBJECTS_PRESENCE_PATTERN, String.format(identifier, objectId), testName));
    }

    private void assertActivatedObjectStatus(String objectId, String identifier) {
        ProcessDetailsPage processDetailsPage = new ProcessDetailsPage(driver);
        String objectStatus = processDetailsPage.selectObject(OBJECT_TYPE_ATTRIBUTE_NAME, String.format(identifier, objectId)).getObjectStatus();
        softAssert.assertEquals(objectStatus, ACTIVATED_STATUS,
                String.format(INVALID_OBJECT_STATUS_LOG_PATTERN, String.format(identifier, objectId), testName));
    }

    private void assertSystemMessage(String messageContent, SystemMessageContainer.MessageType messageType, String systemMessageLog) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, new WebDriverWait(driver, Duration.ofSeconds(30)));
        Optional<SystemMessageContainer.Message> messageOptional = systemMessage.getFirstMessage();
        softAssert.assertTrue(messageOptional.isPresent(), systemMessageLog);
        messageOptional.ifPresent(message -> {
            softAssert.assertEquals(message.getText(), messageContent, systemMessageLog);
            softAssert.assertEquals(message.getMessageType(), messageType, systemMessageLog);
            systemMessage.close();
        });
        waitForPageToLoad();
    }
}
