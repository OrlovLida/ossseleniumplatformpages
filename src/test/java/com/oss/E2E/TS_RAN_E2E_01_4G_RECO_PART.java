package com.oss.E2E;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.utils.TestListener;

@Listeners({ TestListener.class })
public class TS_RAN_E2E_01_4G_RECO_PART extends BaseTestCase {

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private String cmDomainName = "Selenium-TS-RAN-E2E-01-4G";
    private String cmInterface = "Huawei U2000 RAN";
    private String preciseLocation = "Poznan-BU1";
    private String[] inconsistenciesNames = {
            "ENODEB-DXBL0858UAT6-Burj-Dubai_The-Palace-Hotel_West-Wing",
            "PhysicalElement-eNodeB,DXBL0858UAT6-Burj-Dubai_The-Palace-Hotel_West-Wing/0/BBU3900,0",
            "PhysicalElement-eNodeB,DXBL0858UAT6-Burj-Dubai_The-Palace-Hotel_West-Wing/0/MRRU,60"
    };

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
    }

    @Test(priority = 1)
    public void createCmDomain() {
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(cmDomainName);
        wizard.setInterface(cmInterface);
        wizard.setDomain("RAN");
        wizard.save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    public void uploadSamples() {
        DelayUtils.sleep(1000);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(cmDomainName);
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/huaweiRan/TS_RAN_E2E_01_4G/AIM_NodeB_Inventory_DXBL0858UAT6-Burj-Dubai_The-Palace-Hotel_West-Wing_20200414_151916.xml");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/huaweiRan/TS_RAN_E2E_01_4G/SRANNBIExport_XML_DXBL0858UAT6-Burj-Dubai_The-Palace-Hotel_West-Wing_4G.xml");
    }

    @Test(priority = 3)
    public void runReconciliationWithFullSample() {
        openNetworkDiscoveryControlView();
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        networkDiscoveryControlViewPage.checkReconciliationStartedSystemMessage();
        networkDiscoveryControlViewPage.waitForEndOfReco();
    }

    @Test(priority = 4)
    public void assignLocationAndApplyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expantTree();
        for (String inconsistencieName : inconsistenciesNames) {
            networkInconsistenciesViewPage.assignLocation(preciseLocation);
            networkInconsistenciesViewPage.checkUpdateDeviceSystemMessage();
            networkInconsistenciesViewPage.clearOldNotification();
            networkInconsistenciesViewPage.applySelectedInconsistencies();
            DelayUtils.sleep(5000);
            networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(inconsistencieName);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    @Test(priority = 5)
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        networkDiscoveryControlViewPage.checkDeleteCmDomainSystemMessage();
        networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(cmDomainName);
    }
}