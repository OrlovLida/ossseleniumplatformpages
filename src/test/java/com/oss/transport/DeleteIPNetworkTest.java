package com.oss.transport;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.utils.TestListener;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * @author Ewa Frączek
 */

@Listeners({ TestListener.class })
public class DeleteIPNetworkTest extends IPAMBaseTest {
    private String networkName = "IPNetworkDeletionSeleniumTest";

    @BeforeClass
    public void prepareTest() {
        super.openIPAddressManagementView();
        DelayUtils.sleep(500);
        super.createIPNetwork(networkName);
    }

    @Test(priority = 1, expectedExceptions = { NoSuchElementException.class })
    public void deleteIPNetwork() {
        super.ipAddressManagementViewPage.selectTreeRow(networkName);
        super.ipAddressManagementViewPage.useButton("Delete");
        super.ipAddressManagementViewPage.acceptConfirmationBox();
        super.ipAddressManagementViewPage.selectTreeRow(networkName);
    }

    @Test (priority = 2)
    public void checkInventoryView() {
        newInventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "IPNetwork");
        newInventoryViewPage.openFilterPanel().setValue(Input.ComponentType.TEXT_FIELD, "name", networkName).applyFilter();
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
    }
}
