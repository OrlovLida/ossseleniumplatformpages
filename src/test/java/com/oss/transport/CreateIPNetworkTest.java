package com.oss.transport;

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
public class CreateIPNetworkTest extends IPAMBaseTest {

    private String networkName = "IPNetworkCreationSeleniumTest";
    private String description = "Description";

    @BeforeClass
    public void prepareTest() {
        super.openIPAddressManagementView();
    }

    @Test (priority = 1)
    public void createIPNetwork() {
        ipAddressManagementViewPage.selectFirstTreeRow();
        ipAddressManagementViewPage.useContextAction("CreateOperationsForNetwork", "Create IP Network");
        IPNetworkWizardPage ipNetworkWizardPage = new IPNetworkWizardPage(driver);
        ipNetworkWizardPage.createIPNetwork(networkName, description);
        ipAddressManagementViewPage.selectTreeRow(networkName);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue("Name"), networkName);
        Assert.assertEquals(ipAddressManagementViewPage.getPropertyPanel().getPropertyValue("Description"), description);
    }

    @Test (priority = 2)
    public void checkInventoryView() {
        super.checkInventoryViewForIPNetwork(networkName, description);
    }

    @AfterClass
    public void cleanUp(){
        super.openIPAddressManagementView();
        super.deleteIPNetwork(networkName);
    }
}
