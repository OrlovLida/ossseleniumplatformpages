package com.oss.E2E;

import java.net.URISyntaxException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage.IssueLevel;
import com.oss.pages.reconciliation.NetworkInconsistenciesViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.utils.TestListener;

@Listeners({ TestListener.class })
public class TP_OSS_RM_RAN_001_RECO_PART extends BaseTestCase {

    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private static final String cmDomainName = "Selenium-TP-OSS-RM-RAN-001";
    private static final String cmInterface = "Huawei U2000 RAN";
    private static final String[] inconsistenciesNames = {
            "ENODEB-GBM055TST",
            "PhysicalElement-BTS5900,GBM055TST/0/BBU5900,0",
            "PhysicalElement-BTS5900,GBM055TST/0/MRRU,60",
            "PhysicalElement-BTS5900,GBM055TST/0/MRRU,70",
            "PhysicalElement-BTS5900,GBM055TST/0/MRRU,80"
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
    public void uploadSamples() throws URISyntaxException {
        DelayUtils.sleep(1000);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.moveToSamplesManagement();
        SamplesManagementPage samplesManagementPage = new SamplesManagementPage(driver);
        samplesManagementPage.selectPath();
        samplesManagementPage.createDirectory(cmDomainName);
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/huaweiRan/TP_OSS_RM_RAN_001/AIM_BTS5900_GBM055.xml");
        DelayUtils.sleep(1000);
        samplesManagementPage.uploadSamples("recoSamples/huaweiRan/TP_OSS_RM_RAN_001/SRANNBIExport_XML_Co-MPT BTS_RT_06_17_2020_22_38_11_629_198_19_191_6.xml");
    }

    @Test(priority = 3)
    public void runReconciliationWithFullSample() {
        openNetworkDiscoveryControlView();
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType();
        Assert.assertEquals(networkDiscoveryControlViewPage.waitForEndOfReco(), "SUCCESS");
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.STARTUP_FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.ERROR));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(IssueLevel.WARNING));
    }

    @Test(priority = 4)
    public void checkOperationTypeAndAcceptDiscrepancies() {
        networkDiscoveryControlViewPage.moveToNivFromNdcv();
        NetworkInconsistenciesViewPage networkInconsistenciesViewPage = new NetworkInconsistenciesViewPage(driver);
        networkInconsistenciesViewPage.expandTree();
        for (String inconsistencieName : inconsistenciesNames) {
            Assert.assertEquals(networkInconsistenciesViewPage.checkInconsistenciesOperationType(), "MODIFICATION");
            networkInconsistenciesViewPage.clearOldNotification();
            networkInconsistenciesViewPage.applySelectedInconsistencies();
            DelayUtils.sleep(5000);
            Assert.assertEquals(networkInconsistenciesViewPage.checkNotificationAfterApplyInconsistencies(), "Accepting discrepancies related to " + inconsistencieName + " finished");
        }
    }

    @Test(priority = 5)
    public void deleteCmDomain() {
        openNetworkDiscoveryControlView();
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(cmDomainName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.clearOldNotifications();
        networkDiscoveryControlViewPage.deleteCmDomain();
        checkPopupMessageType();
        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + cmDomainName + " finished");
    }

    private void checkPopupMessageType() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                MessageType.INFO);
    }
}