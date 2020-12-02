package com.oss.E2E;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.Notifications;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.bpm.IntegrationProcessWizardPage;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.radio.AntennaArrayWizardPage;
import com.oss.pages.radio.CellBulkWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.GNodeBWizardPage;
import com.oss.pages.radio.HostingWizardPage;
import com.oss.pages.radio.RanAntennaWizardPage;
import com.oss.utils.RandomGenerator;
import com.oss.utils.TestListener;

@Listeners({ TestListener.class })
public class TP_OSS_RM_RAN_002 extends BaseTestCase {

    private String processNRPCode;
    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private AntennaArrayWizardPage antennaArrayWizardPage;
    private String LOCATION_NAME = "Poznan-BU1";
    private String ANTENNA_NAME_0 = "TP_OSS_RM_RAN_002_ANTENNA_0";
    private String ANTENNA_NAME_1 = "TP_OSS_RM_RAN_002_ANTENNA_1";
    private String ANTENNA_NAME_2 = "TP_OSS_RM_RAN_002_ANTENNA_2";
    private String[] ANTENNA_NAMES = { ANTENNA_NAME_0, ANTENNA_NAME_1, ANTENNA_NAME_2 };
    private String RAN_ANTENNA_MODEL = "HUAWEI Technology Co.,Ltd AAU5614";
    private String GNODEB_NAME = "TP_OSS_RM_RAN_002_GNODEB";
    private String BBU_NAME = "TP_OSS_RM_RAN_002_BBU";
    private String GNODEB_MODEL = "HUAWEI Technology Co.,Ltd gNodeB";
    private String randomGNodeBId = RandomGenerator.generateRandomGNodeBId();
    private String CELL5G_NAME_0 = "TP_OSS_RM_RAN_002_CELL5G_0";
    private String CELL5G_NAME_1 = "TP_OSS_RM_RAN_002_CELL5G_1";
    private String CELL5G_NAME_2 = "TP_OSS_RM_RAN_002_CELL5G_2";
    private String[] CELL5G_NAMES = { CELL5G_NAME_0, CELL5G_NAME_1, CELL5G_NAME_2 };
    private String CELL5G_CARRIER = "NR3600-n78 (64200)";
    private String perspectiveContext;
    private String processIPCode;
    private String processIPName = "TP_OSS_RM_RAN_002_" + (int) (Math.random() * 1001);


    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        openCellSiteConfigurationView();
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
    }

    @Test(priority = 1)
    public void createProcessNRP() {
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processNRPCode = processWizardPage.createSimpleNRP();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(messages.get(0).getText()).contains(processNRPCode);
    }

    @Test(priority = 2)
    public void startHLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, "High Level Planning");
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 3)
    public void create5Gnode() {
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create gNodeB");
        new GNodeBWizardPage(driver)
                .createGNodeB(GNODEB_NAME, randomGNodeBId, GNODEB_MODEL, "DU [mcc: 424, mnc: 03]");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Created gNodeB"));
    }

    @Test(priority = 4)
    public void create5Gcells() {
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", LOCATION_NAME, GNODEB_NAME);
        cellSiteConfigurationPage.selectTab("Cells 5G");
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Cell 5G Bulk Wizard");
        CellBulkWizardPage cellBulkWizardPage = new CellBulkWizardPage(driver);
        cellBulkWizardPage.createCellBulkWizard(3, CELL5G_CARRIER, true, CELL5G_NAMES);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Cells 5G created success"));
    }

    @Test(priority = 5)
    public void createRanAntenna() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        for (String ranAntenna : ANTENNA_NAMES) {
            cellSiteConfigurationPage.selectTab("Devices");
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create RAN Antenna");
            RanAntennaWizardPage ranAntennaWizardPage = new RanAntennaWizardPage(driver);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            ranAntennaWizardPage.setName(ranAntenna);
            ranAntennaWizardPage.setModel(RAN_ANTENNA_MODEL);
            DelayUtils.sleep(1000);
            ranAntennaWizardPage.setPreciseLocation(LOCATION_NAME);
            ranAntennaWizardPage.clickAccept();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            antennaArrayWizardPage = new AntennaArrayWizardPage(driver);
            antennaArrayWizardPage.clickAccept();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            checkPopup();
        }
    }

    @Test(priority = 6)
    public void createHostingRelation() {
        cellSiteConfigurationPage.selectTreeRow(GNODEB_NAME);
        cellSiteConfigurationPage.selectTab("Hosting");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clickPlusIconByLabel("Host on Device");
        HostingWizardPage hostOnDeviceWizard = new HostingWizardPage(driver);
        hostOnDeviceWizard.onlyCompatible("false");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hostOnDeviceWizard.selectDevice(BBU_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hostOnDeviceWizard.clickAccept();
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        cellSiteConfigurationPage.getTree().expandTreeRow(GNODEB_NAME);
        for (int i = 0; i < CELL5G_NAMES.length; i++) {
            cellSiteConfigurationPage.selectTreeRow(CELL5G_NAMES[i]);
            cellSiteConfigurationPage.selectTab("Hosting");
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Host on Antenna Array");
            HostingWizardPage hostOnAntennaWizard = new HostingWizardPage(driver);
            hostOnAntennaWizard.selectArray(ANTENNA_NAMES[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            hostOnAntennaWizard.clickAccept();
            checkPopup();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    @Test(priority = 7)
    public void completeHLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "High Level Planning");
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 8)
    public void startLLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, "Low Level Planning");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
        String currentUrl = driver.getCurrentUrl();
        String[] split = currentUrl.split(Pattern.quote("?"));
        perspectiveContext = split[1];
        Assertions.assertThat(perspectiveContext).contains("PLAN");
    }

    @Test(priority = 9)
    public void completeLLPTask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "Low Level Planning");
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 10)
    public void startRFITask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processNRPCode, "Ready for Integration");
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 11)
    public void setupIntegration() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.setupIntegration(processNRPCode);
        IntegrationProcessWizardPage integrationWizard = new IntegrationProcessWizardPage(driver);
        integrationWizard.defineIntegrationProcess(processIPName, LocalDate.now().plusDays(0).toString(), 1);
        integrationWizard.clickNext();
        integrationWizard.dragAndDrop(GNODEB_NAME, processIPName);
        integrationWizard.clickAccept();
    }

    @Test(priority = 12)
    public void getIPCode() {
        DelayUtils.sleep(3000);
        TableInterface ipTable = OldTable.createByComponentId(driver, webDriverWait, "ip_involved_nrp_group1");
        int rowNumber = ipTable.getRowNumber(processIPName, "Name");
        processIPCode = ipTable.getValueCell(rowNumber, "Code");
        System.out.println(processIPCode);
    }

    @Test(priority = 13)
    public void completeRFITask() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processNRPCode, "Ready for Integration");
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 14)
    public void startSDTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processIPCode, "Scope definition");
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 15)
    public void completeSDTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processIPCode, "Scope definition");
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 16)
    public void startImplementationTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processIPCode, "Implementation");
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Notifications.create(driver, webDriverWait).clearAllNotification();
        tasksPage.findTask(processIPCode, "Implementation");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 17)
    public void completeImplementationTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processIPCode, "Implementation");
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 18)
    public void startAcceptanceTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.startTask(processIPCode, "Acceptance");
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .isEqualTo("The task properly assigned.");
    }

    @Test(priority = 19)
    public void completeAcceptanceTaskIP() {
        TasksPage tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        tasksPage.completeTask(processIPCode, "Acceptance");
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages.get(0).getMessageType()).isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getText())
                .contains("Task properly completed.");
    }

    @Test(priority = 20)
    public void deleteHostingRelation() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        cellSiteConfigurationPage.selectTab("Hosting");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Hosted Resource", GNODEB_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.removeObject();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
        for (String cell : CELL5G_NAMES) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.selectTab("Hosting");
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.filterObject("Hosted Resource", cell);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.removeObject();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            checkPopup();
        }
    }

    @Test(priority = 21)
    public void deleteRanAntenna() {
        for (String ranAntenna : ANTENNA_NAMES) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.selectTab("Devices");
            cellSiteConfigurationPage.filterObject("Name", ranAntenna);
            cellSiteConfigurationPage.removeObject();
            checkPopup();
        }
    }

    @Test(priority = 22)
    public void delete5Gcells() {
        cellSiteConfigurationPage.selectTreeRow(GNODEB_NAME);
        for (String cell : CELL5G_NAMES) {
            cellSiteConfigurationPage.selectTab("Cells 5G");
            cellSiteConfigurationPage.filterObject("Name", cell);
            cellSiteConfigurationPage.removeObject();
            checkPopup();
        }
    }

    @Test(priority = 23)
    public void delete5Gnode() {
        cellSiteConfigurationPage.selectTreeRow(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Name", GNODEB_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();
    }

    private void openCellSiteConfigurationView() {
//        HomePage homePage = new HomePage(driver);
//        homePage.goToHomePage(driver, BASIC_URL);
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("Cell Site Configuration", "Favourites", "SeleniumTests");
    }

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }
}
