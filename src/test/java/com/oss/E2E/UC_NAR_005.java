package com.oss.E2E;

import java.net.URISyntaxException;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.mainheader.Notifications;
import com.oss.framework.mainheader.NotificationsInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage.IssueLevel;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({ TestListener.class })
public class UC_NAR_005 extends BaseTestCase {

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private static final String CM_DOMAIN_NAME = "UC-NAR-005";
    private static final String CM_INTERFACE_NAME = "CISCO IOS 12/15/XE without mediation";
    private static final String DOMAIN_NAME = "IP";
    private static final String STOP_ON = "AUTO_ACCEPTANCE";
    private static final String SAVE_PERSPECTIVE = "NETWORK_AA";
    private static final String DEVICE_NAME = "UCNAR05";
    private static final String SERIAL_NUMBER_BEFORE = "SERIAL_NUMBER_BEFORE";
    private static final String SERIAL_NUMBER_AFTER = "SERIAL_NUMBER_AFTER";

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        waitForPageToLoad();
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    @Description("Create CM Domain")
    public void createCmDomain() {
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

    @Test(priority = 2)
    @Description("Upload reconciliation samples")
    public void uploadSamples() throws URISyntaxException {
        waitForPageToLoad();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_005/First/UCNAR05_10.20.0.50_20170707_1300_sh_inventory_raw");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_005/First/UCNAR05_10.20.0.50_20170707_1300_sh_version");
        waitForPageToLoad();
    }

    @Test(priority = 3)
    @Description("Run reconciliation and after its finish check warnings and errors")
    public void runReconciliationWithFullSample() {
        openNetworkDiscoveryControlView();
        waitForPageToLoad();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.runReconciliation();
        checkMessageType(MessageType.INFO);
        Assert.assertEquals(networkDiscoveryControlViewPage.waitForEndOfReco(), "SUCCESS");
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.STARTUP_FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.ERROR));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.WARNING));
    }

    @Test(priority = 4)
    @Description("Move to Network Inconsistencies View, assign location to device and Apply inconsistencies")
    public void assignLocationAndApplyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTree();
        networkInconsistenciesViewPage.assignLocation(DEVICE_NAME, "1");
        checkMessageType(MessageType.SUCCESS);
        networkInconsistenciesViewPage.clearOldNotification();
        networkInconsistenciesViewPage.applyInconsistencies();
        DelayUtils.sleep(5000);
        Assert.assertEquals(networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(), "Accepting discrepancies related to " + DEVICE_NAME + " finished");
    }

    @Test(priority = 5)
    @Description("Delete old reconciliation samples and upload a new ones")
    public void deleteOldSamplesAndPutNewOne() throws URISyntaxException {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.deleteDirectoryContent();
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_005/Second/UCNAR05_10.20.0.50_20170707_1300_sh_inventory_raw");
        waitForPageToLoad();
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_005/Second/UCNAR05_10.20.0.50_20170707_1300_sh_version");
        waitForPageToLoad();
    }

    @Test(priority = 6)
    @Description("Open New Inventory View and Check value of Serial Number")
    public void openNewInventoryViewAndCheckSerialNumber() {
        homePage.goToHomePage(driver, BASIC_URL);
        waitForPageToLoad();
        homePage.chooseFromLeftSideMenu("Legacy Inventory Dashboard", "Resource Inventory ");
        waitForPageToLoad();
        homePage.setNewObjectType("Router");
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(DEVICE_NAME);
        waitForPageToLoad();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
        Assert.assertEquals(newInventoryViewPage.getMainTable().getCellValue(0, "serialNumber"), SERIAL_NUMBER_BEFORE);
    }

    @Test(priority = 7)
    @Description("Run narrow reconciliation and after its finish check value of Serial Number")
    public void runNarrowReconciliation() {
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        NotificationsInterface notifications = Notifications.create(driver, new WebDriverWait(driver, 180));
        notifications.clearAllNotification();
        newInventoryViewPage.callAction(ActionsContainer.OTHER_GROUP_ID, "run-narrow-reconciliation");
        DelayUtils.sleep(3000);
        Assert.assertEquals(notifications.waitAndGetFinishedNotificationText(), "Narrow reconciliation for GMOCs IPDevice finished");
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        Assert.assertEquals(newInventoryViewPage.getMainTable().getCellValue(0, "serialNumber"), SERIAL_NUMBER_AFTER);
    }

    @Test(priority = 8)
    @Description("Delete device")
    public void deleteDevice() {
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, "DeleteDeviceWizardAction");
        waitForPageToLoad();
        Wizard.createWizard(driver, webDriverWait).clickActionById("ConfirmationBox_object_delete_wizard_confirmation_box_action_button");
        checkMessageType(MessageType.SUCCESS);
        newInventoryViewPage.refreshMainTable();
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 9)
    @Description("Delete CM Domain")
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkMessageType(MessageType.INFO);
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void checkMessageType(MessageType messageType) {
        Assert.assertEquals((getFirstMessage().getMessageType()), messageType);
    }

    private Message getFirstMessage() {
        return SystemMessageContainer.create(driver, new WebDriverWait(driver, 90))
                .getFirstMessage()
                .orElseThrow(() -> new RuntimeException("The list is empty"));
    }
}
