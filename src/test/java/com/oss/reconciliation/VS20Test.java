package com.oss.reconciliation;


import com.google.common.collect.ImmutableList;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.CmDomainWizardPage;
import com.oss.pages.reconciliation.MetamodelPage;
import com.oss.pages.reconciliation.NetworkDiscoveryControlViewPage;
import com.oss.pages.reconciliation.SamplesManagementPage;
import com.oss.pages.reconciliation.VS20Page;
import com.sun.javafx.collections.ImmutableObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VS20Test extends BaseTestCase {

    private static final String INTERFACE_NAME = "Comarch";
    private static final String OBJECT_TYPE = "COM_Device";
    private static final String CM_DOMAIN_NAME = "AT-SYS-VS20-TEST";
    private static final String DISTINGUISH_NAME = "Device_VS20_TEST";
    private static final String SKIPPING_TEST_MESSAGE = "Skipping tests because resource was not available.";
    private static final String NOTIFICATION_MESSAGE = "Generation of VS Objects Metamodel for CM Interface: Comarch finished";

//    private List<String> assertionList = Arrays.asList("c64f2100-06e2-4a15-b176-5bf8a6323a68", "AT-SYS-VS20-TEST", "Device@@Device_VS20_TEST", "true", "false", "true", "COM_Device", "VS_Comarch_COM_x_95_x_Device", "", "Cisco IOS XR Software (Cisco ASR9K Series), Version 6.2.3[Default]rnCopyright (c) 2018 by Cisco Systems, Inc.", "", "V01", "Provincia: AMBA; Localidad: Merlo; Centro: MRL; Ubicacion: Riobamba 494 y Sarandi; Piso: 2; Sala: Transmision Nueva; Traza: Guemes-Merlo;: GEO -34.66711, -58.725129:;", "10.193.0.9", "IPv4",  "ChangeModel_test", "2019-01-23T10:40:15.778-03:00", "Cisco Systems Inc.", "ASR9010", "MD=CISCO_EPNM!ND=Device_VS20_TEST", "Device_VS20_TEST", "", "", "", "FOX2141P2PZ", "6.2.3[Default]", "IPDevice", "Device@@Device_VS20_TEST##IPDeviceFunction@@8a864a1e-418b-48ad-a6d4-257204046339", "");

    private List<String> assertionList;
    private VS20Page vs20Page;
    private NetworkDiscoveryControlViewPage networkDiscoveryControlViewPage;
    private MetamodelPage metamodelPage;
    private boolean skipTest = false;


    @BeforeClass
    public void openNetworkDiscoveryControlView() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
    }

    @BeforeMethod
    protected void checkEnvironment() {
        if (skipTest) {
            throw new SkipException(SKIPPING_TEST_MESSAGE);
        }
    }

    @Test(priority = 1)
    public void createCmDomain() {
        networkDiscoveryControlViewPage.openCmDomainWizard();
        CmDomainWizardPage wizard = new CmDomainWizardPage(driver);
        wizard.setName(CM_DOMAIN_NAME);
        wizard.setInterface("Comarch");
        wizard.setDomain("Comarch");
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
        samplesManagementPage.uploadSamples("recoSamples/VS20Viewer/DEVICE_VS20_TEST.json");
    }

    @Test(priority = 3)
    public void runReconciliationWithFullSample() {
        openNetworkDiscoveryControlView();
        DelayUtils.sleep(100);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.runReconciliation();
        checkPopupMessageType();
        Assert.assertEquals(networkDiscoveryControlViewPage.waitForEndOfReco(), "SUCCESS");
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.STARTUP_FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.FATAL));
        Assert.assertTrue(networkDiscoveryControlViewPage.checkIssues(NetworkDiscoveryControlViewPage.IssueLevel.ERROR));
    }

    @Test(priority = 4)
    public void searchForObjectInMetamodelPage() {
        metamodelPage = MetamodelPage.goToMetamodelPage(driver, BASIC_URL);
        metamodelPage.searchInterfaceByName(INTERFACE_NAME);
        metamodelPage.searchForObject(OBJECT_TYPE);
        if (!metamodelPage.checkIfObjectExists(OBJECT_TYPE)) {
            metamodelPage.clearNotification();
            metamodelPage.generateMetamodel();
            Assert.assertEquals(metamodelPage.checkNotificationMessage(), NOTIFICATION_MESSAGE);
            skipTest = true;
        }
    }

    @Test(priority = 5)
    public void getCMDomainId() {
        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkDiscoveryControlViewPage.selectLatestReconciliationState();
        String recoStartingEvent = networkDiscoveryControlViewPage.getRecoStartingEvent();
        String cmDomainId = networkDiscoveryControlViewPage.getCMDomainIdFromRecoStartingEvent(recoStartingEvent);
        assertionList = getAssertionList(cmDomainId);
    }

    @Test(priority = 6)
    public void assertPropertiesOnVS20Viewer() {
        vs20Page = VS20Page.goToVS20Page(driver, BASIC_URL);
        vs20Page.searchItemByCMDomainName(CM_DOMAIN_NAME);
        vs20Page.searchItemByType(OBJECT_TYPE);
        vs20Page.clickFirstItem();
        vs20Page.assertProperties(assertionList);
    }

//    @Test(priority = 7)
//    public void deleteCMDomain() {
//        networkDiscoveryControlViewPage = NetworkDiscoveryControlViewPage.goToNetworkDiscoveryControlViewPage(driver, BASIC_URL);
//        networkDiscoveryControlViewPage.queryAndSelectCmDomain(CM_DOMAIN_NAME);
//        networkDiscoveryControlViewPage.clearOldNotifications();
//        networkDiscoveryControlViewPage.deleteCmDomain();
//        checkPopupMessageType();
//        Assert.assertEquals(networkDiscoveryControlViewPage.checkDeleteCmDomainNotification(), "Deleting CM Domain: " + CM_DOMAIN_NAME + " finished");
//    }

    private void checkPopupMessageType() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.INFO);
    }

    private List<String> getAssertionList(String cmDomainID){
        return new ImmutableList.Builder<String>()
                .add(cmDomainID)
                .add("AT-SYS-VS20-TEST")
                .add("Device@@Device_VS20_TEST")
                .add("true")
                .add("false")
                .add("true")
                .add("COM_Device")
                .add("VS_Comarch_COM_x_95_x_Device")
                .add("")
                .add("Cisco IOS XR Software (Cisco ASR9K Series), Version 6.2.3[Default]rnCopyright (c) 2018 by Cisco Systems, Inc.")
                .add("")
                .add("V01")
                .add("Provincia: AMBA; Localidad: Merlo; Centro: MRL; Ubicacion: Riobamba 494 y Sarandi; Piso: 2; Sala: Transmision Nueva; Traza: Guemes-Merlo;: GEO -34.66711, -58.725129:;")
                .add("10.193.0.9")
                .add("IPv4")
                .add("ChangeModel_test")
                .add("2019-01-23T10:40:15.778-03:00")
                .add("Cisco Systems Inc.")
                .add("ASR9010")
                .add("MD=CISCO_EPNM!ND=Device_VS20_TEST")
                .add("Device_VS20_TEST")
                .add("")
                .add("")
                .add("")
                .add("FOX2141P2PZ")
                .add("6.2.3[Default]")
                .add("IPDevice")
                .add("Device@@Device_VS20_TEST##IPDeviceFunction@@8a864a1e-418b-48ad-a6d4-257204046339")
                .add("")
                .build();
    }
}