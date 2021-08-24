package com.oss.web;

import com.oss.BaseTestCase;
import com.oss.pages.platform.NewInventoryViewPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.oss.utils.*;


@Listeners({TestListener.class})
public class LayoutTest extends BaseTestCase {

    private NewInventoryViewPage inventoryViewPage;

    @BeforeClass
    public void goToInventoryView() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "TestMovie");
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
