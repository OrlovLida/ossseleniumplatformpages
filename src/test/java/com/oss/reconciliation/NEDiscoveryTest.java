package com.oss.reconciliation;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.ConnectionConfigurationObjectsViewerPage;
import com.oss.pages.reconciliation.NEDiscoveryCreateOperationWizardPage;
import com.oss.pages.reconciliation.NetworkElementsDiscoveryPage;
import com.oss.utils.TestListener;

@Listeners({TestListener.class})
public class NEDiscoveryTest extends BaseTestCase {

    private static final String ENTRY_NAME = "test_AG";
    private static final String IP_ADDRESS_RANGE = "10.10.10.100";
    private NetworkElementsDiscoveryPage networkElementsDiscoveryPage;
    private NEDiscoveryCreateOperationWizardPage neDiscoveryCreateOperationWizardPage;
    private ConnectionConfigurationObjectsViewerPage connectionConfigurationObjectsViewerPage;
    private String connection;

    @BeforeClass
    public void openConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        driver.manage().deleteAllCookies();
    }

    @Test(priority = 1)
    public void createNewEntry() {
        networkElementsDiscoveryPage = NetworkElementsDiscoveryPage.goToNetworkElementsDiscoveryPage(driver, BASIC_URL);
        networkElementsDiscoveryPage.createNewEntry();
        neDiscoveryCreateOperationWizardPage = new NEDiscoveryCreateOperationWizardPage(driver);
        neDiscoveryCreateOperationWizardPage.setName(ENTRY_NAME);
        neDiscoveryCreateOperationWizardPage.setIPAddressRange(IP_ADDRESS_RANGE);
        neDiscoveryCreateOperationWizardPage.selectHoldOperationType();
        neDiscoveryCreateOperationWizardPage.clickAccept();
    }

    @Test(priority = 2)
    public void selectElement() {
        networkElementsDiscoveryPage.searchForElement(ENTRY_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkElementsDiscoveryPage.selectElement(ENTRY_NAME);
    }

    @Test(priority = 3)
    public void assertAttributes() {
        Assert.assertEquals(networkElementsDiscoveryPage.getIPAddressByRowIndex(0), IP_ADDRESS_RANGE);
        Assert.assertEquals(networkElementsDiscoveryPage.getPortByRowIndex(0), "161");
        Assert.assertEquals(networkElementsDiscoveryPage.getStateByRowIndex(0), "READY");
        Assert.assertTrue(networkElementsDiscoveryPage.isConnectionSetByRowIndex(0));
        connection = networkElementsDiscoveryPage.getConnectionByRowIndex(0);
    }

    @Test(priority = 4)
    public void goToConnection() {
        networkElementsDiscoveryPage.goToConnection();
        connectionConfigurationObjectsViewerPage = new ConnectionConfigurationObjectsViewerPage(driver);
        connectionConfigurationObjectsViewerPage.queryAndSelectObjectById(connection);
        System.out.println(connectionConfigurationObjectsViewerPage.getId());
    }

//    @Test(priority = 5)
//    public void deleteEntry() {
//        networkElementsDiscoveryPage.deleteEntry();
//    }
}
