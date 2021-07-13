package com.oss.E2E;

import java.net.URISyntaxException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage.IssueLevel;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.utils.TestListener;

@Listeners({ TestListener.class })
public class TS_RAN_E2E_02_5G_RECO_PART extends BaseTestCase {

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private static final String GNODEB_NAME = "DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District";
    private static final String PHYSICAL_ELEMENT_1 = "BTS5900,DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District/0/MPMU,100";
    private static final String PHYSICAL_ELEMENT_2 = "BTS5900,DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District/0/MPMU,200";
    private static final String PHYSICAL_ELEMENT_3 = "BTS5900,DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District/0/RHUB,60";
    private static final String PHYSICAL_ELEMENT_4 = "BTS5900,DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District/0/RHUB,70";
    private static final String CM_DOMAIN_NAME = "Selenium-TS-RAN-E2E-02-5G";
    private static final String CM_INTERFACE_NAME = "Huawei U2000 RAN";
    private static final String LOCATION = "Poznan-BU1";
    private static final String[] RAN_INCONSISTENCIES_NAMES = {
            "GNODEB-" + GNODEB_NAME,
            "GNODEBDU-" + GNODEB_NAME,
            "GNODEBCUUP-" + GNODEB_NAME
    };
    private static final String[] PHYSICAL_INCONSISTENCIES_NAMES = {
            "PhysicalElement-" + PHYSICAL_ELEMENT_1,
            "PhysicalElement-" + PHYSICAL_ELEMENT_2,
            "PhysicalElement-" + PHYSICAL_ELEMENT_3,
            "PhysicalElement-" + PHYSICAL_ELEMENT_4,
    };

    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        PerspectiveChooser.create(driver, webDriverWait).setNetworkPerspective();
    }

    @Test(priority = 1)
    public void createCmDomain() {
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(CM_DOMAIN_NAME);
        wizard.setInterface(CM_INTERFACE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.setDomain("RAN");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizard.save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    public void uploadSamples() throws URISyntaxException {
        DelayUtils.sleep(1000);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(CM_DOMAIN_NAME);
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/huaweiRan/TS_RAN_E2E_02_5G/Inventory_DXNNR0599UAT6-Expo-Multilateral-Buildings-Mobility-District_20200414_151757.xml");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/huaweiRan/TS_RAN_E2E_02_5G/SRANNBIExport_XML_DXNNR0599UAT6_Co-MPT BTS_RT_04_09_2020_14_20_24_322_10_17_4_8.xml");
    }

    @Test(priority = 3)
    public void runReconciliationWithFullSample() {
        openNetworkDiscoveryControlView();
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType(MessageType.INFO);
        Assert.assertEquals(networkDiscoveryControlViewPage.waitForEndOfReco(), "SUCCESS");
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.STARTUP_FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.ERROR));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.WARNING));
    }

    @Test(priority = 4)
    public void assignLocationAndApplyInconsistencies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTree();
        for (String inconsistencieName : PHYSICAL_INCONSISTENCIES_NAMES) {
            networkInconsistenciesViewPage.assignLocation(inconsistencieName, LOCATION);
            checkPopupMessageType(MessageType.SUCCESS);
            networkInconsistenciesViewPage.clearOldNotification();
            networkInconsistenciesViewPage.applySelectedInconsistencies();
            DelayUtils.sleep(5000);
            Assert.assertEquals(networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(), "Accepting discrepancies related to " + inconsistencieName + " finished");
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
        for (String inconsistencieName : RAN_INCONSISTENCIES_NAMES) {
            networkInconsistenciesViewPage.assignRanLocation(inconsistencieName, LOCATION);
            checkPopupMessageType(MessageType.SUCCESS);
            networkInconsistenciesViewPage.clearOldNotification();
            networkInconsistenciesViewPage.applySelectedInconsistencies();
            DelayUtils.sleep(5000);
            Assert.assertEquals(networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(), "Accepting discrepancies related to " + inconsistencieName + " finished");
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    @Test(priority = 5)
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType(MessageType.INFO);
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
    }

    private void checkPopupMessageType(MessageType messageType) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                messageType);
    }
}