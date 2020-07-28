package com.oss;

import com.oss.pages.platform.InventoryViewPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LayoutTest extends BaseTestCase{

    private InventoryViewPage inventoryViewPage;

    @BeforeClass
    public void goToInventoryView() {
        inventoryViewPage = InventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "Location");
    }

    @Test(priority = 1)
    @Description("Change the layout on the Inventory View to vertical orientation")
    public void changeLayoutToVertical() {
        inventoryViewPage.
                changeLayoutToVertical();
        Assert.assertEquals(inventoryViewPage.howManyRows(),1);
    }

    @Test(priority = 2)
    @Description("Change the layout on the Inventory View to horizontal orientation")
    public void changeLayoutToHorizontal() {
        inventoryViewPage.
                changeLayoutToHorizontal();
        Assert.assertEquals(inventoryViewPage.howManyRows(),2);
    }
}
