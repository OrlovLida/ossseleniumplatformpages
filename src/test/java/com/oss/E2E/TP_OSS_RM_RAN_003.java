package com.oss.E2E;

import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.EditCell4GWizardPage;

public class TP_OSS_RM_RAN_003 extends BaseTestCase {

    private String processDCPCode;
    private Random r = new Random();
    private static final String LOCATION_NAME = "XYZ";
    private static final String E_NODE_B_NAME = "TP_OSS_RM_RAN_003_ENODEB";
    private static final String[] CELL_NAMES = new String[] { "TP_OSS_RM_RAN_003_CELL4G_1", "TP_OSS_RM_RAN_003_CELL4G_2", "TP_OSS_RM_RAN_003_CELL4G_3" };
    private static final String BUSINESS_PROCESS_MANAGEMENT = "Business Process Management";
    private static final String BPM_AND_PLANNING = "BPM and Planning";
    private static final String PROCESS_INSTANCES = "Process Instances";
    private static final String BPM_TASKS = "BPM Tasks";
    private static final String NAME = "Name";
    private static final String SITE = "Site";
    private final String pci = Integer.toString(r.nextInt(503));
    private final String rsi = Integer.toString(r.nextInt(503));

    @Test(priority = 1)
    public void createNewProcess() {
        openView(PROCESS_INSTANCES, BPM_AND_PLANNING, BUSINESS_PROCESS_MANAGEMENT);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processDCPCode = processWizardPage.createSimpleDCP();
        checkPopup(processDCPCode);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    public void startDCP() {
        openView(BPM_TASKS, BPM_AND_PLANNING, BUSINESS_PROCESS_MANAGEMENT);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TasksPage tasksPage = new TasksPage(driver);
        tasksPage.startTask(processDCPCode, "Correct data");
        checkPopup("The task properly assigned.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    public void findLocation() {
        openView("Legacy Inventory Dashboard", "Resource Inventory");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.setOldObjectType(SITE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.filterObject(NAME, LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        oldInventoryViewPage.expandShowOnAndChooseView("Cell Site Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    public void modifyCell4Gparameters() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation(SITE, LOCATION_NAME, E_NODE_B_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Cells 4G");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clearColumnFilter(NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        for (int i = 0; i < 3; i++) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.selectRowByAttributeValueWithLabel(NAME, CELL_NAMES[i]);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clickEditIcon();
        EditCell4GWizardPage editCell4GWizardPage = new EditCell4GWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setPCIBulk(pci);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.setRSIBulk(rsi);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        editCell4GWizardPage.accept();
        checkPopup("Cells 4G updated successfully");
    }

    @Test(priority = 5)
    public void finnishDCP() {
        openView(BPM_TASKS, BPM_AND_PLANNING, BUSINESS_PROCESS_MANAGEMENT);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TasksPage tasksPage = new TasksPage(driver);
        tasksPage.completeTask(processDCPCode, "Correct data");
        checkPopup("Task properly completed.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void openView(String actionLabel, String... path) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.chooseFromLeftSideMenu(actionLabel, path);
    }

    private void checkPopup(String text) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertTrue((messages.get(0).getText()).contains(text));
    }
}
