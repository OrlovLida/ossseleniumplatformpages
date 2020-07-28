package com.oss.reconciliation;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;

public class ReconciliationE2ETest extends BaseTestCase {

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private String cmDomainName = "SeleniumTestDomain";

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    public void createCmDomain() {
        networkDiscoveryControlViewPage.createCmDomain(cmDomainName, "CISCO IOS 12/15", "IP");
    }

    @Test(priority = 2)
    public void uploadSamples() {
        DelayUtils.sleep(500);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(cmDomainName);
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoIOS/full/CiscoSeleniumTest_10.20.0.50_20170707_1300_running-config");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoIOS/full/CiscoSeleniumTest_10.20.0.50_20170707_1300_sh_inventory_raw");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoIOS/full/CiscoSeleniumTest_10.20.0.50_20170707_1300_sh_version");
    }

    @Test(priority = 3)
    public void runReconciliationWithFullSample() {
        openNetworkDiscoveryControlView();
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.runReconciliation();
        networkDiscoveryControlViewPage.waitForEndOfReco();
    }

    @Test(priority = 4)
    public void assignLocationAndApplyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expantTree();
        networkInconsistenciesViewPage.assignLocation();
        networkInconsistenciesViewPage.applyInconsistencies();
    }

    @Test(priority = 5)
    public void deleteOldSamplesAndPutNewOne() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.deleteDirectoryContent();
        DelayUtils.sleep(2000);
        samplesManagementPage.selectPath();
        samplesManagementPage.selectPath();
        samplesManagementPage.uploadSamples("recoSamples/ciscoIOS/empty/Selenium1_10.252.255.201_20170707_1324_running-config");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoIOS/empty/Selenium1_10.252.255.201_20170707_1324_sh_inventory_raw");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/ciscoIOS/empty/Selenium1_10.252.255.201_20170707_1324_sh_version");
    }

    @Test(priority = 6)
    public void runReconciliationWithEmptySample() {
        openNetworkDiscoveryControlView();
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.runReconciliation();
        networkDiscoveryControlViewPage.waitForEndOfReco();
    }

    @Test(priority = 7)
    public void applyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expantTree();
        networkInconsistenciesViewPage.applyInconsistencies();
    }

    @Test(priority = 8)
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        networkDiscoveryControlViewPage.deleteCmDomain(cmDomainName);
    }
}