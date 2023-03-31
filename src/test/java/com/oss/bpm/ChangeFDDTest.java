package com.oss.bpm;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.planning.ProcessDetailsPage;
import com.oss.pages.bpm.processinstances.PlannersViewPage;
import com.oss.pages.bpm.processinstances.edition.ChangeFDDWizardPage;
import com.oss.pages.bpm.tasks.SetupIntegrationProperties;
import com.oss.pages.bpm.tasks.TasksPageV2;
import com.oss.planning.PlanningContext;
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

import static com.oss.bpm.BpmPhysicalDataCreator.CHASSIS_NAME;
import static com.oss.bpm.BpmPhysicalDataCreator.createBuilding;
import static com.oss.bpm.BpmPhysicalDataCreator.createIPDevice;
import static com.oss.bpm.BpmPhysicalDataCreator.deleteBuilding;
import static com.oss.bpm.BpmPhysicalDataCreator.deleteIPDevice;
import static com.oss.bpm.BpmPhysicalDataCreator.getDeviceChassisId;
import static com.oss.bpm.BpmPhysicalDataCreator.nextMaxInt;
import static com.oss.bpm.BpmPhysicalDataCreator.nextRandomBuildingName;
import static com.oss.bpm.BpmPhysicalDataCreator.updateIPDeviceSerialNumberInPlan;
import static com.oss.pages.bpm.planning.ProcessDetailsPage.ACTIVATED_STATUS;
import static com.oss.pages.bpm.planning.ProcessDetailsPage.OBJECT_TYPE_ATTRIBUTE_NAME;
import static com.oss.pages.bpm.processinstances.PlannersViewPage.CANCELED_STATUS;
import static com.oss.pages.bpm.processinstances.PlannersViewPage.COMPLETED_STATUS;
import static com.oss.pages.bpm.processinstances.creation.ProcessWizardPage.DCP;
import static com.oss.pages.bpm.processinstances.creation.ProcessWizardPage.NRP;

public class ChangeFDDTest extends BaseTestCase {
    private static final LocalDate TODAY = LocalDate.now();
    private static final PlanningContext LIVE = PlanningContext.live();
    private static final String BUILDING_NAME_TC_MAIN = nextRandomBuildingName();
    private static final String DEVICE_1_NAME = "D1_Change_FDD_Selenium_Test_" + nextMaxInt();
    private static final String DEVICE_2_NAME = "D2_Change FDD_Selenium_Test_" + nextMaxInt();
    private static final String DEVICE_3_NAME = "D3_Change FDD_Selenium_Test_" + nextMaxInt();
    private static final String DEVICE_MODEL = "7705 SAR-8";
    private static final String NRP_1_NAME = "NRP 1 Change FDD Selenium Test " + nextMaxInt();
    private static final String DRP_1_NAME = "DRP 1 Change FDD Selenium Test " + nextMaxInt();
    private static final String DRP_2_NAME = "DRP 2 Change FDD Selenium Test " + nextMaxInt();
    private static final String DCP_1_NAME = "DCP 1 Change FDD Selenium Test " + nextMaxInt();
    private static final String DCP_2_NAME = "DCP 2 Change FDD Selenium Test " + nextMaxInt();
    private static final String IP_1_NAME = "IP 1 Change FDD Selenium Test " + nextMaxInt();
    private static final String IP_2_NAME = "IP 2 Change FDD Selenium Test " + nextMaxInt();
    private static final String AUDIT_1_PROCESS_NAME = "Audit 1 Change FDD Selenium Test " + nextMaxInt();

    public static final String CHANGE_FDD_INFO_EARLIER_PATTERN = "Finish Due Date can not be ealier than %s.";
    public static final String CHANGE_FDD_INFO_BETWEEN_PATTERN = "Finish Due Date could be set to dates between %1$s - %2$s.";
    public static final String CHANGE_FDD_INFO_TOGETHER_PROCESSES_PATTERN = "Together with selected processes following processes Finished Due Dates will be changed to the same value: %s";
    private static final String INVALID_CHANGE_FDD_WIZARD_INFO = "Invalid change FDD wizard info for %1$s in %2$s test.";
    private static final String CHANGE_FDD_REASON = "Selenium change FDD test reason";
    private static final String CHANGE_FDD_MESSAGE_PATTERN = "Finished Due Date successfully changed to %1$s for processes %2$s";
    private static final String INVALID_CHANGE_FDD_MESSAGE = "Invalid system message after change FDD for %1$s in %2$s test.";
    private static final String PROCESS_IDENTIFIER_PATTERN = "%1$s (%2$s)";
    private static final String INVALID_PROCESS_FDD_PATTERN = "Invalid Finished Due Date for %1$s process in %2$s test.";
    private static final String INVALID_PROJECT_FDD_PATTERN = "Invalid Finished Due Date for project with id: %1$s in %2$s test.";
    private static final String UPDATE_SERIAL_NUMBER = "BPM Selenium Update in %s";
    private static final String CHANGE_FDD_WIZARD_OPEN = "Change Finished Due Date wizard is still opened after %s test. It will be closed now.";
    private static final String OBJECTS_NOT_MOVED_TO_IP_MESSAGE = "Some objects are not moved from %s to IPs.";
    private static final String INVALID_OBJECTS_AMOUNT = "Invalid objects amount in %1$s project in %2$s.";
    private static final String INVALID_PROCESS_STATUS_LOG_PATTERN = "Invalid Process Status for %1$s in %2$s test.";
    private static final String INVALID_OBJECT_STATUS_LOG_PATTERN = "Object %1$s has invalid status in '%2$s' test.";
    private static final String CHANGE_FDD_ACTION_VISIBLE_LOG_PATTERN = "'Change Finished Due Date' context action is available for %s.";
    private static final String CHASSIS_IDENTIFIER1 = CHASSIS_NAME + " (%s )";
    private static final String CHASSIS_IDENTIFIER = CHASSIS_NAME + " (%s)";
    private static final String DEVICE_IDENTIFIER = "IP Device (%s)";
    private static final String SHIFT_UNAVAILABLE_MESSAGE = "Shift operation for given process is not available.";
    private static final String NEW_FDD_MESSAGE = "From %1$s Finished Due Date is changed to %2$s. FDD changed for processes: %3$s";
    private final Logger log = LoggerFactory.getLogger(ChangeFDDTest.class);
    PlannersViewPage plannersViewPage = new PlannersViewPage(driver, webDriverWait);
    private SoftAssert softAssert;
    private String testName;

    private String building_main_ID;
    private String device_1_ID;
    private String chassis_1_ID;
    private String device_2_ID;
    private String chassis_2_ID;


    private String nrp_1_code;
    private String nrp_1_ID;
    private PlanningContext nrp_1_plan;

    private String drp_1_code;
    private String drp_1_ID;
    private PlanningContext drp_1_plan;

    private String drp_2_code;
    private String drp_2_ID;
    private PlanningContext drp_2_plan;

    private String dcp_1_code;
    private String dcp_1_ID;
    private PlanningContext dcp_1_plan;

    private String dcp_2_code;
    private String audit1_1_code;

    private String ip_1_code;
    private String ip_1_ID;
    private PlanningContext ip_1_plan;

    private String ip_2_code;
    private PlanningContext ip_2_plan;

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @BeforeClass
    public void prepare() {
        softAssert = new SoftAssert();
        waitForPageToLoad();
        building_main_ID = createBuilding(BUILDING_NAME_TC_MAIN, LIVE);
    }

    @BeforeMethod
    public void getTestName(ITestResult iTestResult) {
        testName = iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    @Test(priority = 1, description = "Change FDD of simple NRP")
    @Description("User is able to change Finished Due Date for NRP without any planned objects")
    public void shiftSimpleNRP() {
        //create NRP 1 FDD = TODAY+5
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        nrp_1_code = plannersViewPage.createProcessIPD(NRP_1_NAME, 5L, NRP);
        //get NRP 1 ID , project ID
        nrp_1_ID = plannersViewPage.getProcessId(nrp_1_code);
        nrp_1_plan = PlanningContext.plan(plannersViewPage.getProjectId(nrp_1_code));
        log.info("NRP 1 Process: " + String.format(PROCESS_IDENTIFIER_PATTERN, nrp_1_code, nrp_1_ID) + " FDD: " + TODAY.plusDays(5));

        //check FDD shift wizard info for NRP1 (earlier than TODAY)
        openChangeFDDWizard(nrp_1_code);
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_EARLIER_PATTERN, TODAY), nrp_1_code);

        //shift NRP FDD to TODAY+3
        changeFDDAndAssertMessage(TODAY.plusDays(3), nrp_1_code, nrp_1_ID);
        assertFDD(nrp_1_code, nrp_1_plan, TODAY.plusDays(3));
    }

    @Test(priority = 2, description = "Change FDD of NRP with planned object", dependsOnMethods = {"shiftSimpleNRP"})
    @Description("User is able to change Finished Due Date for NRP with planned objects")
    public void shiftNRPWithDevice() {
         /*
            Current FDDs:
                - NRP 1: TODAY + 3
         */
        //start HLP task in NRP1
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(nrp_1_code, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        waitForPageToLoad();

        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);

        //create device D1 in NRP 1 plan
        device_1_ID = createIPDevice(DEVICE_1_NAME, DEVICE_MODEL, building_main_ID, nrp_1_plan);
        chassis_1_ID = getDeviceChassisId(device_1_ID, nrp_1_plan);
        log.info(String.format("D1 Device ID: %1$s, Chassis ID: %2$s", device_1_ID, chassis_1_ID));

        //check FDD shift wizard info for NRP1 (earlier than TODAY)
        openChangeFDDWizard(nrp_1_code);
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_EARLIER_PATTERN, TODAY), nrp_1_code);

        //shift NRP FDD to TODAY+4
        changeFDDAndAssertMessage(TODAY.plusDays(4), nrp_1_code, nrp_1_ID);
        assertFDD(nrp_1_code, nrp_1_plan, TODAY.plusDays(4));
    }

    @Test(priority = 3, description = "Change FDD of NRP with DRPs", dependsOnMethods = {"shiftNRPWithDevice"})
    @Description("FDD shifted for NRP should change together with another DRPs.")
    public void shiftNRPWithDRPs() {
        /*
            Current FDDs:
                - NRP 1: TODAY + 4
         */
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);

        //create DRP1 and DRP2 processes in NRP 1 HLP task
        drp_1_code = tasksPage.createDRPProcess(nrp_1_code, TasksPageV2.HIGH_LEVEL_PLANNING_TASK, DRP_1_NAME);
        drp_2_code = tasksPage.createDRPProcess(nrp_1_code, TasksPageV2.HIGH_LEVEL_PLANNING_TASK, DRP_2_NAME);
        tasksPage.startTask(drp_1_code, TasksPageV2.PLANNING_TASK);
        tasksPage.startTask(drp_2_code, TasksPageV2.PLANNING_TASK);
        waitForPageToLoad();

        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);

        //create D2 and D3 devices in DRP1, DRP2 plans
        drp_1_ID = plannersViewPage.getProcessId(drp_1_code);
        drp_1_plan = PlanningContext.plan(plannersViewPage.getProjectId(drp_1_code));
        log.info("DRP 1 Process: " + String.format(PROCESS_IDENTIFIER_PATTERN, drp_1_code, drp_1_ID) + " FDD: " + TODAY.plusDays(3));

        drp_2_ID = plannersViewPage.getProcessId(drp_2_code);
        drp_2_plan = PlanningContext.plan(plannersViewPage.getProjectId(drp_2_code));
        log.info("DRP 2 Process: " + String.format(PROCESS_IDENTIFIER_PATTERN, drp_2_code, drp_2_ID) + " FDD: " + TODAY.plusDays(3));

        device_2_ID = createIPDevice(DEVICE_2_NAME, DEVICE_MODEL, building_main_ID, drp_1_plan);
        chassis_2_ID = getDeviceChassisId(device_2_ID, drp_1_plan);
        log.info(String.format("D1 Device ID: %1$s, Chassis ID: %2$s", device_2_ID, chassis_2_ID));

        String device_3_ID = createIPDevice(DEVICE_3_NAME, DEVICE_MODEL, building_main_ID, drp_2_plan);
        String chassis_3_ID = getDeviceChassisId(device_3_ID, drp_2_plan);
        log.info(String.format("D2 Device ID: %1$s, Chassis ID: %2$s", device_3_ID, chassis_3_ID));

        //check FDD shift wizard info for NRP1 (earlier than TODAY together with DRP1, DRP2)
        openChangeFDDWizard(nrp_1_code);
        List<String> drpIdentifiers = Arrays.asList(
                String.format(PROCESS_IDENTIFIER_PATTERN, DRP_1_NAME, drp_1_code),
                String.format(PROCESS_IDENTIFIER_PATTERN, DRP_2_NAME, drp_2_code));
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_EARLIER_PATTERN, TODAY) + " " +
                String.format(CHANGE_FDD_INFO_TOGETHER_PROCESSES_PATTERN, String.join(", ", drpIdentifiers)), nrp_1_code);

        //shift NRP FDD to TODAY+6
        changeFDDAndAssertMessage(TODAY.plusDays(6), nrp_1_code, nrp_1_ID, drp_1_ID, drp_2_ID);
        //check if together with NRP1 FDD changed for DRP1 and DRP2
        assertFDD(nrp_1_code, nrp_1_plan, TODAY.plusDays(6));
        PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        assertFDD(drp_1_code, drp_1_plan, TODAY.plusDays(6));
        PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        assertFDD(drp_2_code, drp_2_plan, TODAY.plusDays(6));
    }

    @Test(priority = 4, description = "Change FDD of DRP with parent NRP", dependsOnMethods = {"shiftNRPWithDRPs"})
    @Description("FDD shifted for DRP should change together with NRP and another DRPs.")
    public void shiftDRPWithNRP() {
        /*
            Current FDDs:
                - NRP 1: TODAY + 6
                    - DRP 1: TODAY + 6
                    - DRP 2: TODAY + 6
         */
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);

        //check FDD shift wizard info for DRP1 (earlier than TODAY together with NRP1, DRP2)
        openChangeFDDWizard(drp_1_code);
        List<String> identifiers = Arrays.asList(
                String.format(PROCESS_IDENTIFIER_PATTERN, NRP_1_NAME, nrp_1_code),
                String.format(PROCESS_IDENTIFIER_PATTERN, DRP_2_NAME, drp_2_code));
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_EARLIER_PATTERN, TODAY) + " " +
                String.format(CHANGE_FDD_INFO_TOGETHER_PROCESSES_PATTERN, String.join(", ", identifiers)), drp_1_code);

        //shift DRP1 FDD to TODAY+8
        changeFDDAndAssertMessage(TODAY.plusDays(8), drp_1_code, nrp_1_ID, drp_1_ID, drp_2_ID);
        //check if together with DRP1 FDD changed for NRP1 and DRP2
        assertFDD(nrp_1_code, nrp_1_plan, TODAY.plusDays(8));
        PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        assertFDD(drp_1_code, drp_1_plan, TODAY.plusDays(8));
        PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        assertFDD(drp_2_code, drp_2_plan, TODAY.plusDays(8));
    }

    @Test(priority = 5, description = "Change FDD of NRP with completed/canceled DRPs", dependsOnMethods = {"shiftNRPWithDRPs"})
    @Description("User is able to change Finished Due Date for NRP with completed/canceled DRPs")
    public void shiftNRPWithCompleteDRPs() {
        /*
            Current FDDs:
                - NRP 1: TODAY + 8
                    - DRP 1: TODAY + 8
                    - DRP 2: TODAY + 8
         */
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);

        //complete DRP1
        tasksPage.completeTask(drp_1_code, TasksPageV2.PLANNING_TASK);

        //reject DRP2
        tasksPage.changeTransitionAndCompleteTask(drp_2_code, TasksPageV2.PLANNING_TASK, TasksPageV2.NEEDS_CLARIFICATION_TRANSITION);
        tasksPage.startAndCompleteTask(drp_2_code, TasksPageV2.UPDATE_REQUIREMENTS_TASK);
        waitForPageToLoad();

        //check DRP1, DRP2 statuses
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        Assert.assertEquals(plannersViewPage.getProcessState(drp_1_code), COMPLETED_STATUS,
                String.format(INVALID_PROCESS_STATUS_LOG_PATTERN, drp_1_code, testName));
        Assert.assertEquals(plannersViewPage.getProcessState(drp_2_code), CANCELED_STATUS,
                String.format(INVALID_PROCESS_STATUS_LOG_PATTERN, drp_2_code, testName));

        //check FDD shift wizard info for NRP1 (earlier than TODAY)
        openChangeFDDWizard(nrp_1_code);
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_EARLIER_PATTERN, TODAY), nrp_1_code);

        //shift NRP1 FDD to TODAY + 5
        changeFDDAndAssertMessage(TODAY.plusDays(5), nrp_1_code, nrp_1_ID);
        assertFDD(nrp_1_code, nrp_1_plan, TODAY.plusDays(5));
    }

    @Test(priority = 6, description = "Change FDD of NRP with with objects in Ready For Integration status",
            dependsOnMethods = {"shiftNRPWithCompleteDRPs"})
    @Description("User is able to change Finished Due Date for with objects in Ready For Integration status")
    public void shiftNRPWithReadyToIntegrationObjects() {
        /*
            Current FDDs:
                - NRP 1: TODAY + 5
         */
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);

        //move NRP 1 to Ready For Integration task
        tasksPage.proceedNRPToReadyForIntegrationTask(nrp_1_code);
        waitForPageToLoad();

        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);

        //check FDD shift wizard info for NRP1 (earlier than TODAY)
        openChangeFDDWizard(nrp_1_code);
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_EARLIER_PATTERN, TODAY), nrp_1_code);

        //shift NRP1 FDD to TODAY + 10
        changeFDDAndAssertMessage(TODAY.plusDays(10), nrp_1_code, nrp_1_ID);
        assertFDD(nrp_1_code, nrp_1_plan, TODAY.plusDays(10));
    }

    @Test(priority = 7, description = "Change FDD of NRP with objects which have subsequents in another plan.",
            dependsOnMethods = {"shiftNRPWithReadyToIntegrationObjects"})
    @Description("User is able to change Finished Due Date for NRP with objects which have subsequents in another plan.")
    public void shiftNRPWithSubsequent() {
        /*
            Current FDDs:
                - NRP 1: TODAY + 10
         */
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);

        //create DCP 1 FDD = TODAY + 15
        dcp_1_code = plannersViewPage.createProcessIPD(DCP_1_NAME, 15L, DCP);
        dcp_1_ID = plannersViewPage.getProcessId(dcp_1_code);
        dcp_1_plan = PlanningContext.plan(plannersViewPage.getProjectId(dcp_1_code));
        log.info("DCP 1 Process: " + String.format(PROCESS_IDENTIFIER_PATTERN, dcp_1_code, dcp_1_ID) + " FDD: " + TODAY.plusDays(15));

        //start DCP 1 Correct Data task
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(dcp_1_code, TasksPageV2.CORRECT_DATA_TASK);
        waitForPageToLoad();

        //edit D1 device in DCP 1 context
        updateIPDeviceSerialNumberInPlan(DEVICE_1_NAME, device_1_ID, DEVICE_MODEL,
                String.format(UPDATE_SERIAL_NUMBER, dcp_1_code), building_main_ID, dcp_1_plan);

        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        //check FDD shift wizard info for NRP1 (between TODAY and DRP1)
        openChangeFDDWizard(nrp_1_code);
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_BETWEEN_PATTERN, TODAY, TODAY.plusDays(15)), nrp_1_code);

        //shift NRP1 FDD to TODAY + 12
        changeFDDAndAssertMessage(TODAY.plusDays(12), nrp_1_code, nrp_1_ID);
        assertFDD(nrp_1_code, nrp_1_plan, TODAY.plusDays(12));
        //check if DCP 1 FDD is still TODAY + 15
        PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        assertFDD(dcp_1_code, dcp_1_plan, TODAY.plusDays(15));
    }

    @Test(priority = 8, description = "Change FDD of DCP with objects which have prerequisites in another plan.",
            dependsOnMethods = {"shiftNRPWithSubsequent"})
    @Description("User is able to change Finished Due Date for DCP with objects which have prerequisites in another plan.")
    public void shiftDCPWithPrerequisite() {
        /*
            Current FDDs:
                - NRP 1: TODAY + 12 (D1 device create)
                - DCP 1: TODAY + 15 (D1 device update)
         */
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);

        //check FDD shift wizard info for DCP1 (earlier than NRP1)
        openChangeFDDWizard(dcp_1_code);
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_EARLIER_PATTERN, TODAY.plusDays(12)), dcp_1_code);

        //shift DCP1 FDD to TODAY + 17
        changeFDDAndAssertMessage(TODAY.plusDays(17), dcp_1_code, dcp_1_ID);
        assertFDD(dcp_1_code, dcp_1_plan, TODAY.plusDays(17));
    }

    @Test(priority = 9, description = "Change FDD of DCP with objects which have prerequisites and subsequents in another plans.",
            dependsOnMethods = {"shiftDCPWithPrerequisite"})
    @Description("User is able to change Finished Due Date for DCP with objects which have prerequisites and subsequents in another plans.")
    public void shiftDCPWithPrerequisiteAndSubsequent() {
        /*
            Current FDDs:
                - NRP 1: TODAY + 12 (D1 device create)
                - DCP 1: TODAY + 17 (D1 device update)
         */
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);

        //create DCP 2 FDD = TODAY + 20
        dcp_2_code = plannersViewPage.createProcessIPD(DCP_2_NAME, 20L, DCP);
        PlanningContext dcp_2_plan = PlanningContext.plan(plannersViewPage.getProjectId(dcp_2_code));
        log.info("DCP 2 Process: " + dcp_2_code + " FDD: " + TODAY.plusDays(15));

        //start DCP 2 Correct Data task
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(dcp_2_code, TasksPageV2.CORRECT_DATA_TASK);
        waitForPageToLoad();

        //edit D1 device in DCP 2 context
        updateIPDeviceSerialNumberInPlan(DEVICE_1_NAME, device_1_ID, DEVICE_MODEL,
                String.format(UPDATE_SERIAL_NUMBER, dcp_2_code), building_main_ID, dcp_2_plan);

        //check FDD shift wizard info for DCP 1 (between NRP1 and DCP2)
        openChangeFDDWizard(dcp_1_code);
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_BETWEEN_PATTERN, TODAY.plusDays(12), TODAY.plusDays(20)), dcp_1_code);

        //shift DCP1 FDD to TODAY + 14
        changeFDDAndAssertMessage(TODAY.plusDays(14), dcp_1_code, dcp_1_ID);
        assertFDD(dcp_1_code, dcp_1_plan, TODAY.plusDays(14));
    }

    @Test(priority = 9, description = "Change FDD of NRP in wait state with two IPs",
            dependsOnMethods = {"shiftDCPWithPrerequisiteAndSubsequent"})
    @Description("User is able to change Finished Due Date for NRP with open IPs")
    public void shiftNRPWithOpenIPs() {
        /*
            Current FDDs:
                - NRP 1: TODAY + 12 (D1 device create)
                - DCP 1: TODAY + 14 (D1 device update)
                - DCP 2: TODAY + 20 (D1 device update)
         */

        /*
            create 2 IPs from NRP 1:
                - IP1: TODAY + 5 (D1 device and CH1 chassis)
                - IP2: TODAY + 7 (D2 device and CH2 chassis)
         */
        SetupIntegrationProperties setupIntegrationProperties_IP_1 = SetupIntegrationProperties.builder()
                .integrationProcessName(IP_1_NAME)
                .finishedDueDate(TODAY.plusDays(5))
                .objectIdentifiers(Arrays.asList(DEVICE_1_NAME, String.format(CHASSIS_IDENTIFIER1, chassis_1_ID)))
                .build();
        SetupIntegrationProperties setupIntegrationProperties_IP_2 = SetupIntegrationProperties.builder()
                .integrationProcessName(IP_2_NAME)
                .finishedDueDate(TODAY.plusDays(7))
                .objectIdentifiers(Arrays.asList(DEVICE_2_NAME, String.format(CHASSIS_IDENTIFIER1, chassis_2_ID)))
                .build();
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        List<String> ipCodes = tasksPage.setupIntegration(nrp_1_code, NRP_1_NAME,
                Arrays.asList(setupIntegrationProperties_IP_1, setupIntegrationProperties_IP_2));
        ip_1_code = ipCodes.get(0);
        ip_2_code = ipCodes.get(1);
        ProcessDetailsPage processDetailsPage = tasksPage.clickPlanViewButton();
        Assert.assertEquals(processDetailsPage.getObjectsAmount(), 0,
                String.format(INVALID_OBJECTS_AMOUNT, nrp_1_code, testName) + "\n" +
                        String.format(OBJECTS_NOT_MOVED_TO_IP_MESSAGE, nrp_1_code));
        processDetailsPage.closeProcessDetailsPromt();
        tasksPage.completeTask(nrp_1_code, TasksPageV2.READY_FOR_INTEGRATION_TASK);
        waitForPageToLoad();

        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        //get IP1, IP2 process ID and projectID
        ip_1_ID = plannersViewPage.getProcessId(ip_1_code);
        ip_1_plan = PlanningContext.plan(plannersViewPage.getProjectId(ip_1_code));
        ip_2_plan = PlanningContext.plan(plannersViewPage.getProjectId(ip_2_code));
        log.info("IP 1 Process: " + String.format(PROCESS_IDENTIFIER_PATTERN, ip_1_code, ip_1_ID) + " FDD: " + TODAY.plusDays(5));
        log.info("IP 2 Process: " + ip_2_code + " FDD: " + TODAY.plusDays(7));

        //check FDD shift wizard info for NRP1 (between IP2 and DCP1)
        openChangeFDDWizard(nrp_1_code);
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_BETWEEN_PATTERN, TODAY.plusDays(7), TODAY.plusDays(14)), nrp_1_code);

        //shift NRP1 FDD to TODAY + 10
        changeFDDAndAssertMessage(TODAY.plusDays(10), nrp_1_code, nrp_1_ID);
        assertFDD(nrp_1_code, nrp_1_plan, TODAY.plusDays(10));
        //check if IP1 and IP2 FDD no change
        PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        assertFDD(ip_1_code, ip_1_plan, TODAY.plusDays(5));
        PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        assertFDD(ip_2_code, ip_2_plan, TODAY.plusDays(7));
    }

    @Test(priority = 10, description = "Change FDD of open IP (with objects) associated with NRP.",
            dependsOnMethods = {"shiftNRPWithOpenIPs"})
    @Description("User is able to change Finished Due Date for IP with objects, associated with NRP. Data range: TODAY - NRP FDD")
    public void shiftIPUnderNRP() {
        /*
            Current FDDs:
                - NRP 1: TODAY + 10
                    - IP1: TODAY + 5 (D1 device and CH1 chassis)
                    - IP2: TODAY + 7 (D2 device and CH2 chassis)
                - DCP 1: TODAY + 14 (D1 device update)
                - DCP 2: TODAY + 20 (D1 device update)
         */
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);

        //check FDD shift wizard info for IP1 (between TODAY and NRP1)
        openChangeFDDWizard(ip_1_code);
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_BETWEEN_PATTERN, TODAY, TODAY.plusDays(10)), ip_1_code);

        //shift IP1 FDD to TODAY + 3
        changeFDDAndAssertMessage(TODAY.plusDays(3), ip_1_code, ip_1_ID);
        assertFDD(ip_1_code, ip_1_plan, TODAY.plusDays(3));
        //check if NRP1 and IP2 FDD no change
        PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        assertFDD(nrp_1_code, nrp_1_plan, TODAY.plusDays(10));
        PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        assertFDD(ip_2_code, ip_2_plan, TODAY.plusDays(7));
    }

    @Test(priority = 11, description = "Change FDD of NRP with one completed IP.",
            dependsOnMethods = {"shiftIPUnderNRP"})
    @Description("User is able to change Finished Due Date for NRP with one completed IP.")
    public void shiftNRPWithOneCompletedIP() {
        /*
            Current FDDs:
                - NRP 1: TODAY + 10
                    - IP1: TODAY + 3 (D1 device and CH1 chassis)
                    - IP2: TODAY + 7 (D2 device and CH2 chassis)
                - DCP 1: TODAY + 14 (D1 device update)
                - DCP 2: TODAY + 20 (D1 device update)
         */
        //complete IP2
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeIP(ip_2_code);
        waitForPageToLoad();
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        Assert.assertEquals(plannersViewPage.getProcessState(ip_2_code), COMPLETED_STATUS,
                String.format(INVALID_PROCESS_STATUS_LOG_PATTERN, ip_2_code, testName));

        //check FDD shift wizard info for NRP1 (between IP1 and DCP1)
        openChangeFDDWizard(nrp_1_code);
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_BETWEEN_PATTERN,
                TODAY.plusDays(3), TODAY.plusDays(14)), nrp_1_code);

        //shift NRP1 FDD to TODAY + 8
        changeFDDAndAssertMessage(TODAY.plusDays(8), nrp_1_code, nrp_1_ID);
        assertFDD(nrp_1_code, nrp_1_plan, TODAY.plusDays(8));
    }

    @Test(priority = 12, description = "Change FDD of NRP with activated all objects",
            dependsOnMethods = {"shiftNRPWithOneCompletedIP"})
    @Description("User is able to change Finished Due Date for NRP with activated all objects")
    public void shiftNRPWithActivatedObjects() {
        /*
            Current FDDs:
                - NRP 1: TODAY + 8
                    - IP1: TODAY + 3 (D1 device and CH1 chassis)
                - DCP 1: TODAY + 14 (D1 device update)
                - DCP 2: TODAY + 20 (D1 device update)
         */
        //complete IP1
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeIP(ip_1_code);
        waitForPageToLoad();
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        Assert.assertEquals(plannersViewPage.getProcessState(ip_1_code), COMPLETED_STATUS,
                String.format(INVALID_PROCESS_STATUS_LOG_PATTERN, ip_1_code, testName));

        //start NRP1 Verification task and check D1,D2 devices and chassis "Activated" status
        tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(nrp_1_code, TasksPageV2.VERIFICATION_TASK);
        ProcessDetailsPage processDetailsPage = tasksPage.findTask(nrp_1_code, TasksPageV2.VERIFICATION_TASK).clickPlanViewButton();
        Assert.assertEquals(processDetailsPage.getObjectsAmount(), 4,
                String.format(INVALID_OBJECTS_AMOUNT, nrp_1_code, testName));
        assertActivatedObjectStatus(device_1_ID, DEVICE_IDENTIFIER);
        assertActivatedObjectStatus(chassis_1_ID, CHASSIS_IDENTIFIER);
        assertActivatedObjectStatus(device_2_ID, DEVICE_IDENTIFIER);
        assertActivatedObjectStatus(chassis_2_ID, CHASSIS_IDENTIFIER);

        //check FDD shift wizard info for NRP1 (between TODAY and DCP1)
        openChangeFDDWizard(nrp_1_code);
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_BETWEEN_PATTERN, TODAY, TODAY.plusDays(14)), nrp_1_code);

        //shift NRP1 FDD to TODAY + 8
        changeFDDAndAssertMessage(TODAY.plusDays(12), nrp_1_code, nrp_1_ID);
        assertFDD(nrp_1_code, nrp_1_plan, TODAY.plusDays(12));
    }

    @Test(priority = 13, description = "Change FDD of DCP with prerequisite moved to Live",
            dependsOnMethods = {"shiftNRPWithActivatedObjects"})
    @Description("User is able to change Finished Due Date for DCP with prerequisite moved to Live")
    public void shiftDCPAfterNRPComplete() {
        /*
            Current FDDs:
                - NRP 1: TODAY + 12
                - DCP 1: TODAY + 14 (D1 device update)
                - DCP 2: TODAY + 20 (D1 device update)
         */
        //complete NRP1
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(nrp_1_code, TasksPageV2.VERIFICATION_TASK);
        waitForPageToLoad();
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        Assert.assertEquals(plannersViewPage.getProcessState(nrp_1_code), COMPLETED_STATUS,
                String.format(INVALID_PROCESS_STATUS_LOG_PATTERN, nrp_1_code, testName));

        //check FDD shift wizard info for DCP1 (between TODAY and DCP2)
        openChangeFDDWizard(dcp_1_code);
        assertChangeFDDInfo(String.format(CHANGE_FDD_INFO_BETWEEN_PATTERN, TODAY, TODAY.plusDays(20)), dcp_1_code);

        //shift NRP1 FDD to TODAY + 10
        changeFDDAndAssertMessage(TODAY.plusDays(10), dcp_1_code, dcp_1_ID);
        assertFDD(dcp_1_code, dcp_1_plan, TODAY.plusDays(10));
    }

    @Test(priority = 14, description = "Check FDD change possibility for DRP/NRP/IP/DCP in Completed/Canceled status",
            dependsOnMethods = {"shiftDCPAfterNRPComplete"})
    @Description("Change Finished Due Date action is unavailable for completed/canceled status.")
    public void shiftCompletedProcesses() {
        //complete DCP1 and cancel DCP2
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(dcp_1_code, TasksPageV2.CORRECT_DATA_TASK);
        tasksPage.changeTransitionAndCompleteTask(dcp_2_code, TasksPageV2.CORRECT_DATA_TASK, TasksPageV2.CANCEL_TRANSITION);
        waitForPageToLoad();
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        softAssert.assertEquals(plannersViewPage.getProcessState(dcp_1_code), COMPLETED_STATUS,
                String.format(INVALID_PROCESS_STATUS_LOG_PATTERN, dcp_1_code, testName));
        softAssert.assertEquals(plannersViewPage.getProcessState(dcp_2_code), CANCELED_STATUS,
                String.format(INVALID_PROCESS_STATUS_LOG_PATTERN, dcp_2_code, testName));

        // check context action visibility for DRP1, DRP2, NRP1, IP1, IP2, DCP1, DCP2
        softAssert.assertFalse(plannersViewPage.selectProcess(drp_1_code).isChangeFDDActionVisible(),
                String.format(CHANGE_FDD_ACTION_VISIBLE_LOG_PATTERN, drp_1_code));
        softAssert.assertFalse(plannersViewPage.selectProcess(drp_2_code).isChangeFDDActionVisible(),
                String.format(CHANGE_FDD_ACTION_VISIBLE_LOG_PATTERN, drp_2_code));
        softAssert.assertFalse(plannersViewPage.selectProcess(nrp_1_code).isChangeFDDActionVisible(),
                String.format(CHANGE_FDD_ACTION_VISIBLE_LOG_PATTERN, nrp_1_code));
        softAssert.assertFalse(plannersViewPage.selectProcess(ip_1_code).isChangeFDDActionVisible(),
                String.format(CHANGE_FDD_ACTION_VISIBLE_LOG_PATTERN, ip_1_code));
        softAssert.assertFalse(plannersViewPage.selectProcess(ip_2_code).isChangeFDDActionVisible(),
                String.format(CHANGE_FDD_ACTION_VISIBLE_LOG_PATTERN, ip_2_code));
        softAssert.assertFalse(plannersViewPage.selectProcess(dcp_1_code).isChangeFDDActionVisible(),
                String.format(CHANGE_FDD_ACTION_VISIBLE_LOG_PATTERN, dcp_1_code));
        softAssert.assertFalse(plannersViewPage.selectProcess(dcp_2_code).isChangeFDDActionVisible(),
                String.format(CHANGE_FDD_ACTION_VISIBLE_LOG_PATTERN, dcp_2_code));
    }

    @Test(priority = 15, description = "Check FDD change possibility for process without FDD")
    @Description("User is able to change Finished Due Date for process without FDD.")
    public void shiftProcessWithoutFDD() {
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        audit1_1_code = plannersViewPage.createProcessIPD(AUDIT_1_PROCESS_NAME, 0L, "audit1");
        ChangeFDDWizardPage wizard = openChangeFDDWizard(audit1_1_code);
        Assert.assertEquals(wizard.getInformation(), SHIFT_UNAVAILABLE_MESSAGE,
                String.format(INVALID_CHANGE_FDD_WIZARD_INFO, audit1_1_code, testName));
        wizard.close();
    }

    @Test(priority = 20, description = "Checking asserts")
    @Description("Checking asserts")
    public void checkSoftAsserts() {
        softAssert.assertAll();
    }

    @AfterMethod
    public void closeWizard() {
        if (ChangeFDDWizardPage.isWizardVisible(driver)) {
            new ChangeFDDWizardPage(driver).close();
            log.warn(String.format(CHANGE_FDD_WIZARD_OPEN, testName));
        }
    }

    @AfterClass
    public void clean() {
        deleteIPDevice(device_1_ID, LIVE);
        deleteIPDevice(device_2_ID, LIVE);
        deleteBuilding(building_main_ID, LIVE);
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        plannersViewPage.selectProcess(audit1_1_code).terminateProcess("CLEAN");
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

    private ChangeFDDWizardPage openChangeFDDWizard(String processCode) {
        return plannersViewPage.selectProcess(processCode).openChangeFDDWizard();
    }

    private void assertChangeFDDInfo(String info, String processCode) {
        softAssert.assertEquals(new ChangeFDDWizardPage(driver).getInformation(), info,
                String.format(INVALID_CHANGE_FDD_WIZARD_INFO, processCode, testName));
    }

    private void changeFDDAndAssertMessage(LocalDate newFDD, String processCode, String... processesIds) {
        log.info(String.format("Changing FDD for %1$s process to %2$s.", processCode, newFDD));
        new ChangeFDDWizardPage(driver).setNewFDD(newFDD).setReason(CHANGE_FDD_REASON).accept();
        assertSystemMessage(String.format(CHANGE_FDD_MESSAGE_PATTERN, newFDD, String.join(", ", processesIds)),
                SystemMessageContainer.MessageType.SUCCESS, String.format(INVALID_CHANGE_FDD_MESSAGE, processCode, testName));
        log.info(String.format(NEW_FDD_MESSAGE, processCode, newFDD, String.join(", ", processesIds)));
    }

    private void assertFDD(String processCode, PlanningContext planContext, LocalDate fdd) {
        Assert.assertEquals(plannersViewPage.getFDD(processCode), fdd, String.format(INVALID_PROCESS_FDD_PATTERN, processCode, testName));
        String projectFDD = ProcessDetailsPage.goToProcessDetailsView(driver, BASIC_URL, planContext.getProjectId())
                .getProjectAttribute(ProcessDetailsPage.INTEGRATION_DATE_ATTRIBUTE_NAME);
        Assert.assertEquals(projectFDD, fdd.toString(), String.format(INVALID_PROJECT_FDD_PATTERN, planContext.getProjectId(), testName));
    }

    private void assertActivatedObjectStatus(String objectId, String identifier) {
        ProcessDetailsPage processDetailsPage = new ProcessDetailsPage(driver);
        String objectStatus = processDetailsPage.selectObject(OBJECT_TYPE_ATTRIBUTE_NAME, String.format(identifier, objectId)).getObjectStatus();
        Assert.assertEquals(objectStatus, ACTIVATED_STATUS,
                String.format(INVALID_OBJECT_STATUS_LOG_PATTERN, String.format(identifier, objectId), testName));
    }
}
