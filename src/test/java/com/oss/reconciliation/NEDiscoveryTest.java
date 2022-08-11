package com.oss.reconciliation;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.ConnectionConfigurationObjectsViewerPage;
import com.oss.pages.reconciliation.NEDiscoveryCreateOperationWizardPage;
import com.oss.pages.reconciliation.NetworkElementsDiscoveryPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class NEDiscoveryTest extends BaseTestCase {

    private static final String ENTRY_NAME = "test_AG";
    private static final String IP_ADDRESS_RANGE = "10.10.10.100";
    private NetworkElementsDiscoveryPage networkElementsDiscoveryPage;
    private ConnectionConfigurationObjectsViewerPage connectionConfigurationObjectsViewerPage;
    private String connection;
    SoftAssert softAssert = new SoftAssert();

    @BeforeClass
    public void openConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        driver.manage().deleteAllCookies();
    }

    @Test(priority = 1, description = "Create new entry")
    @Description("Create new entry on Network Elements Discovery")
    public void createNewEntry() {
        networkElementsDiscoveryPage = NetworkElementsDiscoveryPage.goToNetworkElementsDiscoveryPage(driver, BASIC_URL);
        networkElementsDiscoveryPage.createNewEntry();
        NEDiscoveryCreateOperationWizardPage neDiscoveryCreateOperationWizardPage = new NEDiscoveryCreateOperationWizardPage(driver);
        neDiscoveryCreateOperationWizardPage.setName(ENTRY_NAME);
        neDiscoveryCreateOperationWizardPage.setIPAddressRange(IP_ADDRESS_RANGE);
        neDiscoveryCreateOperationWizardPage.selectHoldOperationType();
        neDiscoveryCreateOperationWizardPage.clickAccept();
    }

    @Test(priority = 2, description = "Select element")
    @Description("Select created element")
    public void selectElement() {
        networkElementsDiscoveryPage.queryAndSelectObjectByName(ENTRY_NAME);
    }

    @Test(priority = 3, description = "Assert attributes on NEDiscovery Page")
    @Description("Assert attributes on NEDiscovery Page")
    public void assertAttributes() {
        Assert.assertEquals(networkElementsDiscoveryPage.getIPAddressByRowIndex(0), IP_ADDRESS_RANGE);
        Assert.assertEquals(networkElementsDiscoveryPage.getPortByRowIndex(0), "161");
        Assert.assertEquals(networkElementsDiscoveryPage.getPingableByRowIndex(0), "false");
        Assert.assertEquals(networkElementsDiscoveryPage.getStateByRowIndex(0), "READY");
        Assert.assertTrue(networkElementsDiscoveryPage.isConnectionSetByRowIndex(0));
        connection = networkElementsDiscoveryPage.getConnectionByRowIndex(0);
    }

    @Test(priority = 4, description = "Go to connection")
    @Description("Go to connection created with created entry")
    public void goToConnection() {
        networkElementsDiscoveryPage.goToConnection();
        connectionConfigurationObjectsViewerPage = new ConnectionConfigurationObjectsViewerPage(driver);
        connectionConfigurationObjectsViewerPage.queryAndSelectObjectById(connection);
    }

    @Test(priority = 5, description = "Assert Attributes On Connection Configuration Objects Page")
    @Description("Assert Attributes On Connection Configuration Objects Page")
    public void assertAttributesOnConnectionPage() {
        connectionConfigurationObjectsViewerPage = new ConnectionConfigurationObjectsViewerPage(driver);
        connectionConfigurationObjectsViewerPage.queryAndSelectObjectById(connection);
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(0), String.format("\"%s\"", connection));
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(1), "\"ConnectionDefinition\"");
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(2), "\"SnmpConnectionDefinition\"");
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(3), "\"10.10.10.100\"");
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(4), "161");
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(5), "10");
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(6), "10");
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(7), "\"V2c\"");
        softAssert.assertAll();
    }

    @Test(priority = 6, description = "Delete Entry")
    @Description("Delete Entry")
    public void deleteEntry() {
        networkElementsDiscoveryPage = NetworkElementsDiscoveryPage.goToNetworkElementsDiscoveryPage(driver, BASIC_URL);
        networkElementsDiscoveryPage.queryAndSelectObjectByName(ENTRY_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkElementsDiscoveryPage.deleteEntry();
    }
}
