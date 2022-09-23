package com.oss.reconciliation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.reconciliation.ConnectionConfigurationObjectsViewerPage;
import com.oss.pages.reconciliation.NEDiscoveryCreateOperationWizardPage;
import com.oss.pages.reconciliation.NetworkElementsDiscoveryPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class NEDiscoveryTest extends BaseTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(NEDiscoveryTest.class);
    private static final String OPERATION_NAME = "test_AG";
    private static final String IP_ADDRESS_RANGE = "10.10.10.100";
    private static final String NETWORK_ELEMENTS_DISCOVERY = "Network Elements Discovery";
    private static final String NETWORK_DISCOVERY_CONTROL = "Network Discovery Control";
    private static final String NETWORK_DISCOVERY_AND_RECONCILIATION = "Network Discovery and Reconciliation";
    private static final String S_161 = "161";
    private static final String READY = "READY";
    private static final String S_10 = "10";
    private static final String CONNECTION_DEFINITION = "Connection Definition";
    private static final String SNMP_CONNECTION_DEFINITION = "Snmp Connection Definition";
    private static final String S_IP = "10.10.10.100";
    private static final String V2C = "V2c";
    SoftAssert softAssert = new SoftAssert();
    private NetworkElementsDiscoveryPage networkElementsDiscoveryPage;
    private ConnectionConfigurationObjectsViewerPage connectionConfigurationObjectsViewerPage;
    private String connection;

    @BeforeClass
    public void openConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        driver.manage().deleteAllCookies();
    }

    @Test(priority = 1, description = "Delete operation if exists")
    @Description("Check if operation exists and delete it")
    public void deleteOperationIfExists() {
        networkElementsDiscoveryPage = new NetworkElementsDiscoveryPage(driver);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel(NETWORK_ELEMENTS_DISCOVERY, NETWORK_DISCOVERY_AND_RECONCILIATION);
        sideMenu.callActionByLabel(NETWORK_DISCOVERY_CONTROL);
        sideMenu.callActionByLabel(NETWORK_ELEMENTS_DISCOVERY);
        networkElementsDiscoveryPage.searchForElement(OPERATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (networkElementsDiscoveryPage.isRowPresent(OPERATION_NAME)) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            networkElementsDiscoveryPage.selectElement(OPERATION_NAME);
            networkElementsDiscoveryPage.deleteOperation();
        } else {
            LOG.info("Operation with name: " + OPERATION_NAME + " doesn't exist");
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertFalse(networkElementsDiscoveryPage.isRowPresent(OPERATION_NAME));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2, description = "Create new operation")
    @Description("Create new operation on Network Elements Discovery")
    public void createNewOperation() {
        networkElementsDiscoveryPage = new NetworkElementsDiscoveryPage(driver);
        networkElementsDiscoveryPage.createNewOperation();
        NEDiscoveryCreateOperationWizardPage neDiscoveryCreateOperationWizardPage = new NEDiscoveryCreateOperationWizardPage(driver);
        neDiscoveryCreateOperationWizardPage.setName(OPERATION_NAME);
        neDiscoveryCreateOperationWizardPage.setIPAddressRange(IP_ADDRESS_RANGE);
        neDiscoveryCreateOperationWizardPage.selectHoldOperationType();
        neDiscoveryCreateOperationWizardPage.clickAccept();
        networkElementsDiscoveryPage.queryAndSelectObjectByName(OPERATION_NAME);
        Assert.assertEquals(networkElementsDiscoveryPage.getIPAddressByRowIndex(0), IP_ADDRESS_RANGE);
        Assert.assertEquals(networkElementsDiscoveryPage.getPortByRowIndex(0), S_161);
        Assert.assertEquals(networkElementsDiscoveryPage.getStateByRowIndex(0), READY);
        Assert.assertTrue(networkElementsDiscoveryPage.isConnectionSetByRowIndex(0));
        connection = networkElementsDiscoveryPage.getConnectionByRowIndex(0);
    }

    @Test(priority = 2, description = "Assert Connection and delete operation", dependsOnMethods = {"createNewOperation"})
    @Description("Go to connection, assert attributes and delete operation")
    public void assertConnectionAndDeleteOperation() {
        networkElementsDiscoveryPage.goToConnection();
        connectionConfigurationObjectsViewerPage = new ConnectionConfigurationObjectsViewerPage(driver);
        connectionConfigurationObjectsViewerPage.queryAndSelectObjectById(connection);
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(0), connection);
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(1), CONNECTION_DEFINITION);
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(2), SNMP_CONNECTION_DEFINITION);
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(3), S_IP);
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(4), S_161);
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(5), S_10);
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(6), S_10);
        softAssert.assertEquals(connectionConfigurationObjectsViewerPage.getValueByRowIndex(7), V2C);
        softAssert.assertAll();
        networkElementsDiscoveryPage = NetworkElementsDiscoveryPage.goToNetworkElementsDiscoveryPage(driver, BASIC_URL);
        networkElementsDiscoveryPage.queryAndSelectObjectByName(OPERATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkElementsDiscoveryPage.deleteOperation();
    }
}
