package com.oss.E2E;

import java.util.List;
import java.util.Random;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.TasksPage;
import com.oss.pages.bpm.processinstances.ProcessWizardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.EditCell4GBulkWizardPage;

import io.qameta.allure.Description;

public class TP_OSS_RM_RAN_003_Test extends BaseTestCase {

    private static final String LOCATION_NAME = "XYZ";
    private static final String E_NODE_B_NAME = "TP_OSS_RM_RAN_003_ENODEB";
    private static final String[] CELL_NAMES = new String[]{"TP_OSS_RM_RAN_003_CELL4G_1", "TP_OSS_RM_RAN_003_CELL4G_2", "TP_OSS_RM_RAN_003_CELL4G_3"};
    private static final String BUSINESS_PROCESS_MANAGEMENT = "Business Process Management";
    private static final String BPM_AND_PLANNING = "BPM and Planning";
    private static final String PROCESS_INSTANCES = "Process Instances";
    private static final String BPM_TASKS = "BPM Tasks";
    private static final String NAME = "Name";
    private static final String SITE = "Site";
    private String processDCPCode;
    private Random r = new Random();
    private final String pci = Integer.toString(r.nextInt(503));
    private final String rsi = Integer.toString(r.nextInt(503));
    private SoftAssert softAssert;

    @Test(priority = 1, description = "Create DCP")
    @Description("Create new Data Correction Process")
    public void createNewProcess() {
        softAssert = new SoftAssert();
        openView(PROCESS_INSTANCES, BPM_AND_PLANNING, BUSINESS_PROCESS_MANAGEMENT);
        waitForPageToLoad();
        ProcessWizardPage processWizardPage = new ProcessWizardPage(driver);
        processDCPCode = processWizardPage.createSimpleDCP();
        checkPopup(processDCPCode);
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Start DCP")
    @Description("Start newly created Data Correction Process")
    public void startDCP() {
        openView(BPM_TASKS, BPM_AND_PLANNING, BUSINESS_PROCESS_MANAGEMENT);
        waitForPageToLoad();
        TasksPage tasksPage = new TasksPage(driver);
        tasksPage.startTask(processDCPCode, "Correct data");
        checkPopup("The task properly assigned.");
        waitForPageToLoad();
    }

    @Test(priority = 3, description = "Find location and open it in Cell Site Configuration view")
    @Description("Find location in old Inventory View and open location in Cell Site Configuration view")
    public void findLocation() {
        openView("Inventory View", "Resource Inventory ");
        waitForPageToLoad();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(SITE);
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(LOCATION_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectRow("col-name", LOCATION_NAME);
        waitForPageToLoad();
        newInventoryViewPage.callAction(ActionsContainer.SHOW_ON_GROUP_ID, "Cell Site Configuration");
        waitForPageToLoad();
    }

    @Test(priority = 4, description = "Modify cells")
    @Description("Modify Cells 4G parameters in bulk wizard")
    public void modifyCell4Gparameters() {
        CellSiteConfigurationPage cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
        cellSiteConfigurationPage.expandTreeToBaseStation(SITE, LOCATION_NAME, E_NODE_B_NAME);
        waitForPageToLoad();
        cellSiteConfigurationPage.selectTab("Cells 4G");
        waitForPageToLoad();
        cellSiteConfigurationPage.clearColumnFilter(NAME);
        waitForPageToLoad();

        for (int i = 0; i < 3; i++) {
            waitForPageToLoad();
            cellSiteConfigurationPage.selectRowByAttributeValueWithLabel(NAME, CELL_NAMES[i]);
        }
        waitForPageToLoad();
        cellSiteConfigurationPage.clickEditIcon();
        EditCell4GBulkWizardPage editCell4GBulkWizardPage = new EditCell4GBulkWizardPage(driver);
        waitForPageToLoad();
        editCell4GBulkWizardPage.setPCIBulk(pci);
        waitForPageToLoad();
        editCell4GBulkWizardPage.setRSIBulk(rsi);
        waitForPageToLoad();
        editCell4GBulkWizardPage.accept();
        checkPopup("Cells 4G updated successfully");
    }

    @Test(priority = 5, description = "Finish DCP")
    @Description("Finish Data Correction Process")
    public void finnishDCP() {
        openView(BPM_TASKS, BPM_AND_PLANNING, BUSINESS_PROCESS_MANAGEMENT);
        waitForPageToLoad();
        TasksPage tasksPage = new TasksPage(driver);
        tasksPage.completeTask(processDCPCode, "Correct data");
        checkPopup("Task properly completed.");
        waitForPageToLoad();
    }

    private void openView(String actionLabel, String... path) {
        waitForPageToLoad();
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu(actionLabel, path);
    }

    private void checkPopup(String text) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        softAssert.assertEquals(messages.size(), 1);
        softAssert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
        softAssert.assertTrue((messages.get(0).getText()).contains(text));
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

}
