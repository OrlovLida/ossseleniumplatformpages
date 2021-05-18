package com.oss.E2E;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.utils.RandomGenerator;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({ TestListener.class })
public class TP_OSS_RM_RAN_002 extends BaseTestCase {

    private String processNRPCode;
    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private static final String LOCATION_NAME = "Poznan-BU1";
    private static final String ANTENNA_NAME_0 = "TP_OSS_RM_RAN_002_ANTENNA_0";
    private static final String ANTENNA_NAME_1 = "TP_OSS_RM_RAN_002_ANTENNA_1";
    private static final String ANTENNA_NAME_2 = "TP_OSS_RM_RAN_002_ANTENNA_2";
    private static final String[] ANTENNA_NAMES = { ANTENNA_NAME_0, ANTENNA_NAME_1, ANTENNA_NAME_2 };
    private static final String RAN_ANTENNA_MODEL = "HUAWEI Technology Co.,Ltd AAU5614";
    private static final String GNODEB_NAME = "TP_OSS_RM_RAN_002_GNODEB";
    private static final String BBU_NAME = "TP_OSS_RM_RAN_002_BBU";
    private static final String GNODEB_MODEL = "HUAWEI Technology Co.,Ltd gNodeB";
    private static final String randomGNodeBId = RandomGenerator.generateRandomGNodeBId();
    private static final String CELL5G_NAME_0 = "TP_OSS_RM_RAN_002_CELL5G_0";
    private static final String CELL5G_NAME_1 = "TP_OSS_RM_RAN_002_CELL5G_1";
    private static final String CELL5G_NAME_2 = "TP_OSS_RM_RAN_002_CELL5G_2";
    private static final String[] CELL5G_NAMES = { CELL5G_NAME_0, CELL5G_NAME_1, CELL5G_NAME_2 };
    private static final String CELL5G_CARRIER = "NR3600-n78-140 (642000)";
    private static final String MCCMNC_PRIMARY = "DU03 [mcc: 424, mnc: 03]";
    private static final int[] LOCAL_CELLS_ID = { 7, 8, 9 };

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        waitForPageToLoad();
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("Process Instances", "Views", "Business Process Management");
        waitForPageToLoad();
    }

    @Test(priority = 1)
    @Description("Create NRP Process")
    public void createProcessNRP() {
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        checkMessageSize();
        checkMessageType();
        checkMessageContainsText(processNRPCode);
    }

    @Test(priority = 2)
    @Description("Start High Level Planning Task")
    public void startHLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, TasksPage.HIGH_LEVEL_PLANNING_TASK);
        checkTaskAssignment();
    }

    @Test(priority = 3)
    @Description("Create gNodeB")
    public void create5Gnode() {
        openCellSiteConfigurationView();
        cellSiteConfigurationPage.createGNodeB(GNODEB_NAME, randomGNodeBId, GNODEB_MODEL, MCCMNC_PRIMARY);
        checkMessageContainsText("Created gNodeB");
    }

    @Test(priority = 4)
    @Description("Create three cells 5G")
    public void create5Gcells() {
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, GNODEB_NAME);
        cellSiteConfigurationPage.createCell5GBulk(3, CELL5G_CARRIER, CELL5G_NAMES, LOCAL_CELLS_ID);
        checkMessageContainsText("Cells 5G created success");
    }

    @Test(priority = 5)
    @Description("Create three ran antenna")
    public void createRanAntenna() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        waitForPageToLoad();
        for (String ranAntenna : ANTENNA_NAMES) {
            cellSiteConfigurationPage.selectTab("Devices");
            cellSiteConfigurationPage.createRanAntennaAndArray(ranAntenna, RAN_ANTENNA_MODEL, LOCATION_NAME);
            checkMessageType();
        }
    }

    @Test(priority = 6)
    @Description("Create hosting on BBU and hostings on antenna arrays")
    public void createHostingRelation() {
        cellSiteConfigurationPage.selectTreeRow(GNODEB_NAME);
        cellSiteConfigurationPage.createHostingOnDevice(BBU_NAME, false);
        checkMessageType();
        waitForPageToLoad();
        for (int i = 0; i < CELL5G_NAMES.length; i++) {
            cellSiteConfigurationPage.selectTreeRow(CELL5G_NAMES[i]);
            cellSiteConfigurationPage.createHostingOnAntennaArray(ANTENNA_NAMES[i]);
            checkMessageType();
            waitForPageToLoad();
        }
    }

    @Test(priority = 7)
    @Description("Finish rest of NRP and IP Tasks")
    public void finishProcessesTasks() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeNRP(processNRPCode);
    }

    @Test(priority = 8)
    @Description("Delete hosting relations")
    public void deleteHostingRelation() {
        openCellSiteConfigurationView();
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, GNODEB_NAME);
        cellSiteConfigurationPage.selectTab("Hosting");
        waitForPageToLoad();
        cellSiteConfigurationPage.filterObject("Hosted Resource", GNODEB_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.removeObject();
        waitForPageToLoad();
        checkMessageType();
        waitForPageToLoad();
        cellSiteConfigurationPage.getTree().expandTreeRow(GNODEB_NAME);
        for (String cell : CELL5G_NAMES) {
            waitForPageToLoad();
            cellSiteConfigurationPage.selectTreeRow(cell);
            cellSiteConfigurationPage.selectTab("Hosting");
            waitForPageToLoad();
            cellSiteConfigurationPage.filterObject("Hosted Resource", cell);
            waitForPageToLoad();
            cellSiteConfigurationPage.removeObject();
            waitForPageToLoad();
            checkMessageType();
        }
    }

    @Test(priority = 9)
    @Description("Delete ran antennas")
    public void deleteRanAntenna() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        for (String ranAntenna : ANTENNA_NAMES) {
            waitForPageToLoad();
            cellSiteConfigurationPage.selectTab("Devices");
            cellSiteConfigurationPage.filterObject("Name", ranAntenna);
            cellSiteConfigurationPage.removeObject();
            checkMessageType();
        }
    }

    @Test(priority = 10)
    @Description("Delete cells 5G")
    public void delete5Gcells() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        for (String cell : CELL5G_NAMES) {
            cellSiteConfigurationPage.selectTab("Cells 5G");
            cellSiteConfigurationPage.filterObject("Name", cell);
            cellSiteConfigurationPage.removeObject();
            checkMessageType();
        }
    }

    @Test(priority = 11)
    @Description("Delete gNodeB")
    public void delete5Gnode() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        cellSiteConfigurationPage.selectTab("Base Stations");
        waitForPageToLoad();
        cellSiteConfigurationPage.filterObject("Name", GNODEB_NAME);
        cellSiteConfigurationPage.removeObject();
        checkMessageType();
    }

    private void openCellSiteConfigurationView() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("Cell Site Configuration", "Favourites", "SeleniumTests");
    }

    private void checkMessageType() {
        Assert.assertEquals(MessageType.SUCCESS, (getFirstMessage().getMessageType()));
    }

    private void checkMessageContainsText(String message) {
        Assert.assertTrue((getFirstMessage().getText())
                .contains(message));
    }

    private void checkMessageText() {
        Assert.assertEquals("The task properly assigned.", (getFirstMessage().getText()));
    }

    private void checkMessageSize() {
        Assert.assertEquals((SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .size()), 1);
    }

    private Message getFirstMessage() {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getFirstMessage()
                .orElseThrow(() -> new RuntimeException("The list is empty"));
    }

    private void checkTaskAssignment() {
        checkMessageType();
        checkMessageText();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

}
