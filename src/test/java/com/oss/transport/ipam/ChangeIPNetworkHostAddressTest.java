package com.oss.transport.ipam;

import com.oss.BaseTestCase;
import com.oss.pages.bpm.ProcessInstancesPage;
import com.oss.pages.transport.ipam.IPAddressManagementViewPage;
import com.oss.pages.transport.ipam.IPSubnetWizardPage;
import com.oss.pages.transport.ipam.helper.IPSubnetFilterProperties;
import com.oss.pages.transport.ipam.helper.IPSubnetWizardProperties;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.oss.pages.transport.ipam.helper.IPAMTreeConstants.NETWORK_PROPERTY_NAME;

@Listeners({TestListener.class})
public class ChangeIPNetworkHostAddressTest extends BaseTestCase {
    private static final String FIRST_NETWORK_NAME = "ChangeIPNetworkSelenium1";
    private static final String DESCRIPTION = "Host address case";
    private static final String SECOND_NETWORK_NAME = "ChangeIPNetworkSelenium2";
    private static final String BLOCK_SUBNET_TYPE = "Block";
    private static final String NETWORK_SUBNET_TYPE = "Network";
    private IPAddressManagementViewPage ipAddressManagementViewPage;
    private static final int AMOUNT_OF_SELECTED_SUBNETS = 2;
    private static final String FIXED_SUBNET_ADDRESS = "20.000";
    private static final String HIGHER_OR_EQUAL = ">=";
    private static final String NETWORK_MASK = "24";

    @Test(priority = 1)
    @Description("Create IP networks")
    public void createIPNetworks() {
        ipAddressManagementViewPage = IPAddressManagementViewPage.goToIPAddressManagementPage(driver, BASIC_URL);
        ipAddressManagementViewPage.createIPNetwork(FIRST_NETWORK_NAME, DESCRIPTION);
        ipAddressManagementViewPage.selectTreeRow(FIRST_NETWORK_NAME);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(NETWORK_PROPERTY_NAME), FIRST_NETWORK_NAME);
        ipAddressManagementViewPage.unselectTreeRow(FIRST_NETWORK_NAME);
        ipAddressManagementViewPage.createIPNetwork(SECOND_NETWORK_NAME, DESCRIPTION);
        ipAddressManagementViewPage.selectTreeRow(SECOND_NETWORK_NAME);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyValue(NETWORK_PROPERTY_NAME), SECOND_NETWORK_NAME);
        ipAddressManagementViewPage.unselectTreeRow(SECOND_NETWORK_NAME);
    }

    @Test(priority = 2)
    @Description("Creating subnets for first network")
    public void createFirstNetworkSubnets() {
        IPSubnetWizardPage ipSubnetWizardPage = ipAddressManagementViewPage.createIPv4Subnet();
        IPSubnetFilterProperties subnetFilterProperties = new IPSubnetFilterProperties(FIXED_SUBNET_ADDRESS, FIXED_SUBNET_ADDRESS, HIGHER_OR_EQUAL, NETWORK_MASK);
        ipSubnetWizardPage.ipSubnetWizardSelectStep(subnetFilterProperties, AMOUNT_OF_SELECTED_SUBNETS);
        IPSubnetWizardProperties firstIpSubnetWizardProperties = new IPSubnetWizardProperties(BLOCK_SUBNET_TYPE);
        IPSubnetWizardProperties secondIpSubnetWizardProperties = new IPSubnetWizardProperties(NETWORK_SUBNET_TYPE);
        ipSubnetWizardPage.ipSubnetWizardPropertiesStep(firstIpSubnetWizardProperties, secondIpSubnetWizardProperties);
        ipSubnetWizardPage.ipSubnetWizardSummaryStep();
    }
}
