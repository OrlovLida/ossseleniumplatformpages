package com.oss.E2E;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.ProcessWizardPage;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.EditCell4GWizardPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

public class TP_OSS_RM_RAN_003 extends BaseTestCase {

    private String processDCPCode;
    private String locationName = "XYZ";
    private String eNodeBName = "TP_OSS_RM_RAN_003_ENODEB";
    private String cellNames[] = new String[]{"TP_OSS_RM_RAN_003_CELL4G_1", "TP_OSS_RM_RAN_003_CELL4G_2", "TP_OSS_RM_RAN_003_CELL4G_3"};
    Random r = new Random();
    private String pci = Integer.toString(r.nextInt(503));
    private String rsi = Integer.toString(r.nextInt(503));

    @Test(priority = 1)
    public void createNewProcess() {
        openView("Process Instances", "Views", "Business Process Management");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processDCPCode = processWizardPage.createSimpleDCP();
        checkPopup(processDCPCode);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    public void startDCP() {
        openView("Tasks", "Views", "Business Process Management");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TasksPage tasksPage = new TasksPage(driver);
        tasksPage.startTask(processDCPCode, "Correct data");
        checkPopup("The task properly assigned.");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 3)
    public void findLocation() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.setOldObjectType("Site");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.filterObject("Name", locationName, "Site");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        oldInventoryViewPage.expandShowOnAndChooseView("Cell Site Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    public void modifyCell4Gparameters() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", locationName, eNodeBName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Cells 4G");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        for (int i = 0; i < 3; i++) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.selectRowByAttributeValueWithLabel("Name", cellNames[i]);
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
        openView("Tasks", "Views", "Business Process Management");
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
