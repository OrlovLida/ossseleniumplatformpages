package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.IPAddressManagementViewPage;
import com.oss.pages.transport.IPNetworkWizardPage;
import com.oss.pages.transport.IPSubnetWizardPage;
import com.oss.pages.transport.ReserveIPAddressWizardPage;
import org.testng.Assert;

/**
 * @author Ewa Frączek
 */

public class IPAMBaseTest extends BaseTestCase {
    protected IPAddressManagementViewPage ipAddressManagementViewPage;
    protected NewInventoryViewPage newInventoryViewPage;

    public IPAddressManagementViewPage openIPAddressManagementView() {
        DelayUtils.sleep(1000);
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementViewPageLive(driver, BASIC_URL);
        return ipAddressManagementViewPage;
    }

    public void createIPNetwork(String networkName) {
        ipAddressManagementViewPage.selectFirstTreeRow();
        ipAddressManagementViewPage.useContextAction("CreateOperationsForNetwork", "Create IP Network");
        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
        ipNetworkWizardPage.createIPNetwork(networkName);
    }

    public void createIPv4Subnet(String networkName, String lowestAddress, String highestAddress, String mask, String type) {
        ipAddressManagementViewPage.selectTreeRow(networkName);
        ipAddressManagementViewPage.useContextAction("CreateOperationsForNetwork", "Create IPv4 Subnet");
        IPSubnetWizardPage ipSubnetWizardPage = new IPSubnetWizardPage(driver);
        ipSubnetWizardPage.createIPSubnet(lowestAddress, highestAddress, mask, type);
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
    }

    public void createIPv6Subnet(String networkName, String lowestAddress, String highestAddress, String mask, String type) {
        ipAddressManagementViewPage.selectTreeRow(networkName);
        ipAddressManagementViewPage.useContextAction("CreateOperationsForNetwork", "Create IPv6 Subnet");
        IPSubnetWizardPage ipSubnetWizardPage = new IPSubnetWizardPage(driver);
        ipSubnetWizardPage.createIPSubnet(lowestAddress, highestAddress, mask, type);
        ipAddressManagementViewPage = new IPAddressManagementViewPage(driver);
    }

    public void createIPv4Host(String subnetIpAddress) {
        ipAddressManagementViewPage.selectTreeRowContains(subnetIpAddress);
        ipAddressManagementViewPage.useContextAction("__more-group", "Create", "Reserve IPv4 Address");
        ReserveIPAddressWizardPage reserveIPAddressWizardPage = new ReserveIPAddressWizardPage(driver);
        reserveIPAddressWizardPage.reserveIPAddress();
    }

    public void createIPv6Host(String subnetIpAddress) {
        ipAddressManagementViewPage.selectTreeRowContains(subnetIpAddress);
        ipAddressManagementViewPage.useContextAction("__more-group", "Create", "Reserve IPv6 Address");
        ReserveIPAddressWizardPage reserveIPAddressWizardPage = new ReserveIPAddressWizardPage(driver);
        reserveIPAddressWizardPage.reserveIPAddress();
    }

    public void deleteIPNetwork(String networkName){
        ipAddressManagementViewPage.selectTreeRow(networkName);
        ipAddressManagementViewPage.useButton("Delete");
        ipAddressManagementViewPage.acceptConfirmationBox();
    }

    public void deleteIPSubnet(String subnetIpAddress){
        ipAddressManagementViewPage.selectTreeRowContains(subnetIpAddress);
        ipAddressManagementViewPage.useButton("Delete");
        ipAddressManagementViewPage.acceptConfirmationBox();
    }

    public void deleteIPAddress(String ipAddress){
        ipAddressManagementViewPage.selectTreeRow(ipAddress);
        ipAddressManagementViewPage.useButton("Delete");
        ipAddressManagementViewPage.acceptConfirmationBox();
    }

    public void checkInventoryViewForIPNetwork(String networkName, String description) {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "IPNetwork");
        newInventoryViewPage.openFilterPanel().setValue(networkName, "name").applyFilter();
        newInventoryViewPage.getTableWidget().selectFirstRow();
        DelayUtils.sleep(100);
        Assert.assertEquals(newInventoryViewPage.getPropertyPanel().getPropertyValue("name"), networkName);
        Assert.assertEquals(newInventoryViewPage.getPropertyPanel().getPropertyValue("description"), description);
    }
}
