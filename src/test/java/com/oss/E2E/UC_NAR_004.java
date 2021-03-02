package com.oss.E2E;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.GlobalSearchPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({ TestListener.class })
public class UC_NAR_004 extends BaseTestCase {

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private static final String CM_DOMAIN_NAME = "KRK-SSE8-45";
    private static final String INTERFACE_NAME = "CISCO IOS 12/15/XE without mediation";
    private static final String DOMAIN = "IP";
    private static final String EQUIPMENT_TYPE = "Physical Device";
    private static final String ROUTER_NAME = "KRK-SSE8-45";

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    @Description("Create CM Domain")
    public void createCmDomain() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(CM_DOMAIN_NAME);
        wizard.setInterface(INTERFACE_NAME);
        wizard.setDomain(DOMAIN);
        wizard.save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    @Description("Upload reconciliation samples")
    public void uploadSamples() {
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_004/KRK-SSE8-45_10.166.10.1_20181107_1306_running-config");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_004/KRK-SSE8-45_10.166.10.1_20181107_1306_sh_inventory_raw");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/UC_NAR_004/KRK-SSE8-45_10.166.10.1_20181107_1306_sh_version");
    }

    @Test(priority = 3)
    @Description("Run reconciliation and check if it ended without errors")
    public void runReconciliationWithFullSample() {
        openNetworkDiscoveryControlView();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.runReconciliation();
        networkDiscoveryControlViewPage.checkReconciliationStartedSystemMessage();
        networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
    }

    @Test(priority = 4)
    @Description("Check discrepancy type (Creation)")
    public void checkOperationType() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkInconsistenciesViewPage.expandTree();
        Assert.assertTrue(networkInconsistenciesViewPage.checkInconsistenciesOperationType().contentEquals("CREATION"));
    }

    @Test(priority = 5)
    @Description("Search for router in Global Search and check it in New Inventory View")
    public void searchInGlobalSearchAndOpenInventoryView() {
        PerspectiveChooser.create(driver, webDriverWait).setNetworkPerspective();
        networkDiscoveryControlViewPage.searchInGlobalSearch(ROUTER_NAME);
        GlobalSearchPage globalSearchPage = new GlobalSearchPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        globalSearchPage.filterObjectType(EQUIPMENT_TYPE);
        globalSearchPage.expandShowOnAndChooseView(ROUTER_NAME, "NAVIGATION", "OpenInventoryView");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        NewInventoryViewPage newInventoryViewPage = NewInventoryViewPage.getInventoryViewPage(driver, webDriverWait);
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 6)
    @Description("Delete old reconciliation samples and upload empty samples")
    public void deleteOldSamplesAndPutNewOne() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.deleteDirectoryContent();
        DelayUtils.sleep(2000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoIOS/empty/Selenium1_10.252.255.201_20170707_1324_running-config");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoIOS/empty/Selenium1_10.252.255.201_20170707_1324_sh_inventory_raw");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoIOS/empty/Selenium1_10.252.255.201_20170707_1324_sh_version");
    }

    @Test(priority = 7)
    @Description("Run reconciliation and check if it ended without errors")
    public void runReconciliationWithEmptySample() {
        openNetworkDiscoveryControlView();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.runReconciliation();
        networkDiscoveryControlViewPage.checkReconciliationStartedSystemMessage();
        networkDiscoveryControlViewPage.waitForEndOfReco();
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.WARNING));
    }

    @Test(priority = 8)
    @Description("Delete CM Domain")
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        networkDiscoveryControlViewPage.checkDeleteCmDomainSystemMessage();
        networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(CM_DOMAIN_NAME);
    }
}
