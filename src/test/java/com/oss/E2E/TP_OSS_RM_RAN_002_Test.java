package com.oss.E2E;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessOverviewPage;
import com.oss.pages.bpm.TasksPageV2;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.utils.RandomGenerator;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class TP_OSS_RM_RAN_002_Test extends BaseTestCase {

    private static final String LOCATION_NAME = "XYZ_SeleniumTests";
    private static final String SITE = "Site";
    private static final String ANTENNA_NAME_0 = "TP_OSS_RM_RAN_002_ANTENNA_0";
    private static final String ANTENNA_NAME_1 = "TP_OSS_RM_RAN_002_ANTENNA_1";
    private static final String ANTENNA_NAME_2 = "TP_OSS_RM_RAN_002_ANTENNA_2";
    private static final String[] ANTENNA_NAMES = {ANTENNA_NAME_0, ANTENNA_NAME_1, ANTENNA_NAME_2};
    private static final String RAN_ANTENNA_MODEL = "HUAWEI Technology Co.,Ltd AAU5614";
    private static final String GNODEB_NAME = "TP_OSS_RM_RAN_002_GNODEB";
    private static final String GNODEB_DU_NAME = "TP_OSS_RM_RAN_002_GNODEB_DU";
    private static final String BBU_NAME = "TP_OSS_RM_RAN_002_BBU";
    private static final String GNODEB_MODEL = "HUAWEI Technology Co.,Ltd gNodeB";
    private static final String GNODEB_DU_MODEL = "HUAWEI Technology Co.,Ltd gNodeB DU";
    private static final String randomGNodeBId = RandomGenerator.generateRandomGNodeBId();
    private static final String randomGNodeBDUId = RandomGenerator.generateRandomGNodeBId();
    private static final String CELL5G_NAME_0 = "TP_OSS_RM_RAN_002_CELL5G_0";
    private static final String CELL5G_NAME_1 = "TP_OSS_RM_RAN_002_CELL5G_1";
    private static final String CELL5G_NAME_2 = "TP_OSS_RM_RAN_002_CELL5G_2";
    private static final String[] CELL5G_NAMES = {CELL5G_NAME_0, CELL5G_NAME_1, CELL5G_NAME_2};
    private static final String CELL5G_CARRIER = "E2E Carrier 5G (11)";
    private static final String MCCMNC_PRIMARY = "E2ETests [mcc: 0001, mnc: 01]";
    private static final int[] LOCAL_CELLS_ID = {7, 8, 9};
    private static final String MANUFACTURER = "HUAWEI Technology Co.,Ltd";
    private final static String SYSTEM_MESSAGE_PATTERN = "%s. Checking system message after %s.";
    private String processNRPCode;
    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private SoftAssert softAssert;

    @BeforeClass
    public void openConsole() {
        waitForPageToLoad();
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        softAssert = new SoftAssert();
    }

    @Test(priority = 1, description = "Create NRP Process")
    @Description("Create NRP Process")
    public void createProcessNRP() {
        ProcessOverviewPage processInstancesPage = ProcessOverviewPage.goToProcessOverviewPage(driver, webDriverWait);
        processNRPCode = processInstancesPage.createSimpleNRP();
        closeMessage();
    }

    @Test(priority = 2, description = "Start High Level Planning Task", dependsOnMethods = {"createProcessNRP"})
    @Description("Start High Level Planning Task")
    public void startHLPTask() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, TasksPageV2.HIGH_LEVEL_PLANNING_TASK);
        checkTaskAssignment(String.format(SYSTEM_MESSAGE_PATTERN, "Start HLP task", "HLP task start"));
        closeMessage();
    }

    @Test(priority = 3, description = "Create gNodeB", dependsOnMethods = {"startHLPTask"})
    @Description("Create gNodeB")
    public void createGNodeB() {
        openCellSiteConfigurationView();
        cellSiteConfigurationPage.createGNodeB(GNODEB_NAME, randomGNodeBId, GNODEB_MODEL, MCCMNC_PRIMARY);
        checkMessageContainsText("GNodeB was created", String.format(SYSTEM_MESSAGE_PATTERN, "Create GNodeB", "GNodeB create"));
        closeMessage();
    }

    @Test(priority = 4, description = "Create gNodeB DU", dependsOnMethods = {"createGNodeB"})
    @Description("Create GNodeB DU")
    public void createGNodeBDU() {
        cellSiteConfigurationPage.createGNodeBDU(GNODEB_DU_NAME, randomGNodeBDUId, GNODEB_DU_MODEL, GNODEB_NAME);
        checkMessageContainsText("GNodeB DU was created", String.format(SYSTEM_MESSAGE_PATTERN, "Create GNodeB DU", "GNodeB DU create"));
        closeMessage();
    }

    @Test(priority = 5, description = "Create three cells 5G", dependsOnMethods = {"createGNodeBDU"})
    @Description("Create three cells 5G")
    public void create5Gcells() {
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, GNODEB_NAME);
        cellSiteConfigurationPage.createCell5GBulk(3, CELL5G_CARRIER, CELL5G_NAMES, LOCAL_CELLS_ID);
        checkMessageContainsText("Cells 5G created success", String.format(SYSTEM_MESSAGE_PATTERN, "Create 5G cells", "cells 5G create"));
        closeMessage();
    }

    @Test(priority = 6, description = "Create three ran antenna", dependsOnMethods = {"create5Gcells"})
    @Description("Create three ran antenna")
    public void createRanAntenna() {
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTab("Devices");
        for (String ranAntenna : ANTENNA_NAMES) {
            waitForPageToLoad();
            cellSiteConfigurationPage.createRanAntennaAndArray(ranAntenna, RAN_ANTENNA_MODEL, LOCATION_NAME);
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Create ran antenna", "ran antenna create"));
            closeMessage();
        }
    }

    @Test(priority = 7, description = "Create hosting on BBU and hostings on antenna arrays", dependsOnMethods = {"createRanAntenna"})
    @Description("Create hosting on BBU and hostings on antenna arrays")
    public void createHostingRelation() {
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTreeRow(GNODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.createHostingOnDevice(BBU_NAME, false);
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Create hosting relation", "GNodeB hosting on device create"));
        closeMessage();
        cellSiteConfigurationPage.selectTreeRow(GNODEB_DU_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.createHostingOnDevice(BBU_NAME, false);
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Create hosting relation", "GNodeB DU hosting on device create"));
        closeMessage();
        for (int i = 0; i < CELL5G_NAMES.length; i++) {
            cellSiteConfigurationPage.selectTreeRow(CELL5G_NAMES[i]);
            cellSiteConfigurationPage.createHostingOnAntennaArray(ANTENNA_NAMES[i]);
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Create hosting relation", "cell 5G hosting on antenna array create"));
            closeMessage();
        }
    }

    @Test(priority = 8, description = "Finish rest of NRP and IP Tasks", dependsOnMethods = {"createHostingRelation"})
    @Description("Finish rest of NRP and IP Tasks")
    public void finishProcessesTasks() {
        TasksPageV2 tasksPage = TasksPageV2.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeNRP(processNRPCode);
        waitForPageToLoad();
    }

    @Test(priority = 9, description = "Delete hosting relations", dependsOnMethods = {"createHostingRelation"})
    @Description("Delete hosting relations")
    public void deleteHostingRelation() {
        openCellSiteConfigurationView();
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, GNODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTab("Hosting");
        waitForPageToLoad();
        cellSiteConfigurationPage.filterObject("Hosted Resource", GNODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.removeObject();
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete hosting relation", "hosting of GNodeB delete"));
        closeMessage();
        waitForPageToLoad();
        cellSiteConfigurationPage.getTree().expandTreeRow(GNODEB_NAME);
        for (String cell : CELL5G_NAMES) {
            waitForPageToLoad();
            cellSiteConfigurationPage.selectTreeRow(cell);
            waitForPageToLoad();
            cellSiteConfigurationPage.selectTab("Hosting");
            waitForPageToLoad();
            cellSiteConfigurationPage.filterObject("Hosted Resource", cell);
            waitForPageToLoad();
            cellSiteConfigurationPage.removeObject();
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete hosting relation", "hosting of cell 5G delete"));
            closeMessage();
            waitForPageToLoad();
        }
    }

    @Test(priority = 10, description = "Delete ran antennas", dependsOnMethods = {"createRanAntenna"})
    @Description("Delete ran antennas")
    public void deleteRanAntenna() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTab("Devices");
        for (String ranAntenna : ANTENNA_NAMES) {
            waitForPageToLoad();
            cellSiteConfigurationPage.removeDevice("Antennas", MANUFACTURER, ranAntenna);
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete ran antenna", "ran antenna delete"));
            closeMessage();
            waitForPageToLoad();
        }
    }

    @Test(priority = 11, description = "Delete cells 5G", dependsOnMethods = {"create5Gcells"})
    @Description("Delete cells 5G")
    public void delete5Gcells() {
        cellSiteConfigurationPage.selectTreeRow(GNODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTab("Cells 5G");
        waitForPageToLoad();
        for (String cell : CELL5G_NAMES) {
            cellSiteConfigurationPage.filterObject("Name", cell);
            waitForPageToLoad();
            cellSiteConfigurationPage.removeObject();
            checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete 5G cells", "cells 5G delete"));
            closeMessage();
            waitForPageToLoad();
        }
    }

    @Test(priority = 12, description = "Delete GNodeB DU", dependsOnMethods = {"createGNodeBDU"})
    @Description("Delete GNodeB DU")
    public void deleteGNodeBDU() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTab("Base Stations");
        waitForPageToLoad();
        cellSiteConfigurationPage.filterObject("Name", GNODEB_DU_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.removeObject();
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete GNodeB DU", "GNodeB DU delete"));
        closeMessage();
    }

    @Test(priority = 13, description = "Delete GNodeB", dependsOnMethods = {"createGNodeB"})
    @Description("Delete GNodeB")
    public void deleteGNodeB() {
        waitForPageToLoad();
        cellSiteConfigurationPage.filterObject("Name", GNODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.removeObject();
        checkMessageType(String.format(SYSTEM_MESSAGE_PATTERN, "Delete GNodeB", "GNodeB delete"));
        closeMessage();
    }

    @Test(priority = 14, description = "Checking system message summary")
    @Description("Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    private void openCellSiteConfigurationView() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory");
        waitForPageToLoad();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(SITE);
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(LOCATION_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "Cell Site Configuration");
        waitForPageToLoad();
    }

    private void checkMessageType(String systemMessageLog) {
        softAssert.assertEquals((getFirstMessage().getMessageType()), SystemMessageContainer.MessageType.SUCCESS, systemMessageLog);
    }

    private void checkMessageContainsText(String message, String systemMessageLog) {
        softAssert.assertTrue((getFirstMessage().getText())
                .contains(message), systemMessageLog);
    }

    private void checkMessageText(String systemMessageLog) {
        softAssert.assertEquals((getFirstMessage().getText()), "The task properly assigned.", systemMessageLog);
    }

    private SystemMessageContainer.Message getFirstMessage() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getFirstMessage()
                .orElseThrow(() -> new RuntimeException("The list is empty"));
    }

    private void checkTaskAssignment(String systemMessageLog) {
        checkMessageType(systemMessageLog);
        checkMessageText(systemMessageLog);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void closeMessage() {
        SystemMessageContainer.create(driver, webDriverWait).close();
        waitForPageToLoad();
    }
}
