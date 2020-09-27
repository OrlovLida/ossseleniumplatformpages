package com.oss.transport;

import java.util.UUID;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.transport.IPNetworkWizardPage;
import com.oss.utils.TestListener;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * @author Ewa Frączek
 */

@Listeners({ TestListener.class })
public class EditIPNetworkTest extends IPAMBaseTest {
    private String networkName = UUID.randomUUID().toString();
    private String networkNameUpdated = "IPNetworkEdition10SeleniumTestUpdated";
    private String descriptionUpdated = "DescriptionUpdated";

    //TODO: uncomment commented lines after bug fixes

    @BeforeClass
    public void prepareTest() {
        super.openIPAddressManagementView();
        super.createIPNetwork(networkName);
        super.createIPv4Subnet(networkName, "10.000", "11.000", "24", "Network");
        super.createIPv6Subnet(networkName, "::100", "::110", "121", "Network");
//        super.ipAddressManagementViewPage.selectTreeRow(networkName);
//        super.ipAddressManagementViewPage.selectTreeRow(networkName);
//        super.createIPv4Host(networkName, "10.0.0.0");
//        super.ipAddressManagementViewPage.selectTreeRow("10.0.0.1/24");
//        super.ipAddressManagementViewPage.selectTreeRow("10.0.0.1/24");
//        super.createIPv6Host(networkName, "::100");
//        super.ipAddressManagementViewPage.selectTreeRow("::101/121");
//        super.ipAddressManagementViewPage.selectTreeRow("::101/121");
    }

    @Test(priority = 1)
    public void editIPNetwork() {
        super.ipAddressManagementViewPage.selectTreeRow(networkName);
        super.ipAddressManagementViewPage.useContextAction("EditOperationsForNetwork", "Edit IP Network");
        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
        ipNetworkWizardPage.editIPNetwork(networkNameUpdated, descriptionUpdated);
        super.ipAddressManagementViewPage.selectTreeRow(networkNameUpdated);
        Assert.assertEquals(super.ipAddressManagementViewPage.getPropertyPanel().getPropertyValue("Name"), networkNameUpdated);
        Assert.assertEquals(super.ipAddressManagementViewPage.getPropertyPanel().getPropertyValue("Description"), descriptionUpdated);
//        super.ipAddressManagementViewPage.selectTreeRow(networkNameUpdated);
//        super.ipAddressManagementViewPage.expandTreeRow(networkNameUpdated);
//        super.ipAddressManagementViewPage.selectTreeRowContains("10.0.0.0");
//        Assert.assertEquals(super.ipAddressManagementViewPage.getPropertyPanel().getPropertyValue("Identifier"), "10.0.0.0/24 ["+networkNameUpdated+"]");
//        Assert.assertEquals(super.ipAddressManagementViewPage.getPropertyPanel().getPropertyValue("IP Network name"), networkNameUpdated);
//        super.ipAddressManagementViewPage.selectTreeRowContains("::100");
//        Assert.assertEquals(super.ipAddressManagementViewPage.getPropertyPanel().getPropertyValue("Identifier"), "::100/121 ["+networkNameUpdated+"]");
//        Assert.assertEquals(super.ipAddressManagementViewPage.getPropertyPanel().getPropertyValue("IP Network name"), networkNameUpdated);
    }

    @Test (priority = 2)
    public void checkInventoryView() {
        super.checkInventoryViewForIPNetwork(networkNameUpdated, descriptionUpdated);
    }

    @AfterClass
    public void cleanUp(){
        super.openIPAddressManagementView();
        DelayUtils.sleep(1000);
        super.ipAddressManagementViewPage.expandTreeRow(networkNameUpdated);
//        super.ipAddressManagementViewPage.expandTreeRowContains("10.0.0.0");
//        super.deleteIPAddress("10.0.0.1");
//        super.ipAddressManagementViewPage.expandTreeRowContains("::100");
//        super.deleteIPAddress("::101");
        super.deleteIPSubnet( "10.0.0.0");
        super.ipAddressManagementViewPage.expandTreeRow(networkNameUpdated);
        super.deleteIPSubnet("::100");
        super.deleteIPNetwork(networkNameUpdated);
    }
}
