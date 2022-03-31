package com.oss.web;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class LayoutTest extends BaseTestCase {

    private NewInventoryViewPage inventoryViewPage;
    private static final String HORIZONTAL_60_40_BUTTON_ID = "TWO_ROWS_60_40";

    @BeforeClass
    public void goToInventoryView() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, "TestMovie");
    }

    @Test(priority = 1)
    @Description("Change the layout on the Inventory View to vertical orientation")
    public void changeLayoutToVertical() {
        inventoryViewPage.
                setVerticalLayout();
        Assert.assertTrue(inventoryViewPage.isVertical());
    }

    @Test(priority = 2)
    @Description("Change the layout on the Inventory View to horizontal orientation")
    public void changeLayoutToHorizontal() {
        inventoryViewPage.
                setHorizontalLayout(HORIZONTAL_60_40_BUTTON_ID);
        Assert.assertTrue(inventoryViewPage.isHorizontal(HORIZONTAL_60_40_BUTTON_ID));
    }
}
