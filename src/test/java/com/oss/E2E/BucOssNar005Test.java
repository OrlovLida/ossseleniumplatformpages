package com.oss.E2E;

import java.net.URISyntaxException;
import java.time.Duration;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageContainer.Message;
import com.oss.framework.components.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.components.mainheader.NotificationsInterface;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.logicalfunction.LogicalFunctionWizardPreStep;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class BucOssNar005Test extends BaseTestCase {

    private static final String CM_DOMAIN_NAME = "UC-NAR-005";
    private static final String CM_INTERFACE_NAME = "CISCO IOS 12/15/XE without mediation";
    private static final String DOMAIN_NAME = "IP";
    private static final String STOP_ON = "AUTO_ACCEPTANCE";
    private static final String SAVE_PERSPECTIVE = "NETWORK_AA";
    private static final String OBJECT_NAME = "UCNAR05";
    private static final String SERIAL_NUMBER_BEFORE = "SERIAL_NUMBER_BEFORE";
    private static final String SERIAL_NUMBER_AFTER = "SERIAL_NUMBER_AFTER";
    private static final String CONFIRM_ID = "ConfirmationBox_object_delete_wizard_confirmation_box_action_button";
    private static final String DEVICE_TYPE = "Physical Device";
    private static final String LF_TYPE = "Logical Function";
    private static final String DELETE_LF_BUTTON_ID = "logicalInventory_DeleteLogicalFunctionActionModifyId";
    private static final String DELETE_DEVICE_BUTTON_ID = "DeleteDeviceWizardAction";
    private final static String SYSTEM_MESSAGE_PATTERN = "%s. Checking system message after %s.";
    private SoftAssert softAssert;

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        softAssert = new SoftAssert();
        waitForPageToLoad();
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        waitForPageToLoad();
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1, description = "Create CM Domain")
    @Description("Create CM Domain in Network Discovery Control View")
    public void createCmDomain() {
        waitForPageToLoad();
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(CM_DOMAIN_NAME);
        wizard.setInterface(CM_INTERFACE_NAME);
        wizard.setDomain(DOMAIN_NAME);
        wizard.setStopOn(STOP_ON);
        wizard.setSavePerspective(SAVE_PERSPECTIVE);
        wizard.save();
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Upload reconciliation samples", dependsOnMethods = {"createCmDomain"})
    @Description("Go to Samples Management View and upload reconciliation samples")
    public void uploadSamples() throws URISyntaxException {
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        waitForPageToLoad();
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_005/First/UCNAR05_10.20.0.50_20170707_1300_sh_inventory_raw");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_005/First/UCNAR05_10.20.0.50_20170707_1300_sh_version");
        waitForPageToLoad();
    }

    @Test(priority = 3, description = "Run reconciliation and check results", dependsOnMethods = {"uploadSamples"})
    @Description("Go to Network Discovery Control View, run reconciliation and check if it ended without errors")
    public void runReconciliationWithFullSample() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.runReconciliation();
        checkMessageType(MessageType.INFO, String.format(SYSTEM_MESSAGE_PATTERN, "Run reconciliation with full sample", "starting reconciliation"));
        waitForPageToLoad();
        String status = networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        waitForPageToLoad();
        networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.INFO);
        if (status.equals("SUCCESS")) {
            waitForPageToLoad();
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
            waitForPageToLoad();
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.WARNING));
        } else {
            waitForPageToLoad();
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
            waitForPageToLoad();
            Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        }
        Assert.assertEquals(status, "SUCCESS");
    }

    @Test(priority = 4, description = "Assign location and accept inconsistencies", dependsOnMethods = {"runReconciliationWithFullSample"})
    @Description("Move to Network Inconsistencies View, assign location to device and apply inconsistencies")
    public void assignLocationAndApplyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTree();
        waitForPageToLoad();
        networkInconsistenciesViewPage.assignLocation(OBJECT_NAME, "1");
        checkMessageType(MessageType.SUCCESS, String.format(SYSTEM_MESSAGE_PATTERN, "Assign location and apply inconsistencies", "assigning location"));
        waitForPageToLoad();
        networkInconsistenciesViewPage.clearOldNotification();
        networkInconsistenciesViewPage.applyInconsistencies();
        DelayUtils.sleep(3000);
        waitForPageToLoad();
        Assert.assertEquals(networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(), "Accepting discrepancies related to " + OBJECT_NAME + " finished");
    }

    @Test(priority = 5, description = "Change reconciliation samples", dependsOnMethods = {"assignLocationAndApplyInconsistencies"})
    @Description("Go to Samples Management from Network Discovery Control View, delete old reconciliation samples and upload a new ones")
    public void deleteOldSamplesAndPutNewOne() throws URISyntaxException {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        waitForPageToLoad();
        samplesManagementPage.deleteDirectoryContent();
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_005/Second/UCNAR05_10.20.0.50_20170707_1300_sh_inventory_raw");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_005/Second/UCNAR05_10.20.0.50_20170707_1300_sh_version");
        waitForPageToLoad();
    }

    @Test(priority = 6, description = "Open router in New Inventory View", dependsOnMethods = {"deleteOldSamplesAndPutNewOne"})
    @Description("Open router in New Inventory View and check value of serial number")
    public void openNewInventoryViewAndCheckSerialNumber() {
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory");
        waitForPageToLoad();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(DEVICE_TYPE);
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(OBJECT_NAME);
        waitForPageToLoad();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
        Assert.assertEquals(newInventoryViewPage.getMainTable().getCellValue(0, "serialNumber"), SERIAL_NUMBER_BEFORE);
    }

    @Test(priority = 7, description = "Run narrow reconciliation", dependsOnMethods = {"openNewInventoryViewAndCheckSerialNumber"})
    @Description("Run narrow reconciliation")
    public void runNarrowReconciliation() {
        DelayUtils.sleep(5000);
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        NotificationsInterface notifications = Notifications.create(driver, new WebDriverWait(driver, Duration.ofSeconds(180)));
        notifications.clearAllNotification();
        newInventoryViewPage.callAction(ActionsContainer.OTHER_GROUP_ID, "run-narrow-reconciliation");
        DelayUtils.sleep(3000);
        waitForPageToLoad();
        Assert.assertEquals(notifications.getNotificationMessage(), "Narrow reconciliation for GMOCs IPDevice finished");
        waitForPageToLoad();
    }

    @Test(priority = 8, description = "Check serial number", dependsOnMethods = {"runNarrowReconciliation"})
    @Description("Refresh view, select first row and check if value of serial number is updated")
    public void checkSerialNumber() {
        DelayUtils.sleep(45000);
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        Assert.assertEquals(newInventoryViewPage.getMainTable().getCellValue(0, "serialNumber"), SERIAL_NUMBER_AFTER);
    }

    @Test(priority = 9, description = "Delete Logical Function", dependsOnMethods = {"assignLocationAndApplyInconsistencies"})
    @Description("Open new Inventory View, query Logical Function, delete Logical Function and check confirmation system message")
    public void deleteLogicalFunction() {
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory");
        waitForPageToLoad();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(LF_TYPE);
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        waitForPageToLoad();
        newInventoryViewPage.searchObject(OBJECT_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow().callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_LF_BUTTON_ID);
        LogicalFunctionWizardPreStep logicalFunctionWizardPreStep = LogicalFunctionWizardPreStep.create(driver, webDriverWait);
        logicalFunctionWizardPreStep.clickAccept();
        checkMessageType(MessageType.SUCCESS, String.format(SYSTEM_MESSAGE_PATTERN, "Delete logical function", "logical function delete"));
    }

    @Test(priority = 10, description = "Delete router", dependsOnMethods = {"assignLocationAndApplyInconsistencies"})
    @Description("Delete router")
    public void deleteRouter() {
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory");
        waitForPageToLoad();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(DEVICE_TYPE);
        DelayUtils.sleep(45000);
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        waitForPageToLoad();
        newInventoryViewPage.searchObject(OBJECT_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow().callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_DEVICE_BUTTON_ID);
        waitForPageToLoad();
        newInventoryViewPage.clickConfirmationBox(CONFIRM_ID);
        checkMessageType(MessageType.SUCCESS, String.format(SYSTEM_MESSAGE_PATTERN, "Delete router", "router delete"));
        waitForPageToLoad();
        DelayUtils.sleep(15000);
        waitForPageToLoad();
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 11, description = "Delete CM Domain", dependsOnMethods = {"createCmDomain"})
    @Description("Go to Network Discovery Control View, delete CM Domain and check notification")
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        waitForPageToLoad();
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkMessageType(MessageType.INFO, String.format(SYSTEM_MESSAGE_PATTERN, "Delete CM Domain", "CM Domain delete"));
        waitForPageToLoad();
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
    }

    @Test(priority = 12, description = "Checking system message summary")
    @Description("Checking system message summary")
    public void systemMessageSummary() {
        softAssert.assertAll();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void checkMessageType(MessageType messageType, String systemMessageLog) {
        softAssert.assertEquals((getFirstMessage().getMessageType()), messageType, systemMessageLog);
    }

    private Message getFirstMessage() {
        return SystemMessageContainer.create(driver, new WebDriverWait(driver, Duration.ofSeconds(90)))
                .getFirstMessage()
                .orElseThrow(() -> new RuntimeException("The list is empty"));
    }
}
