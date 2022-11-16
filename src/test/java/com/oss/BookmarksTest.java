package com.oss;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.framework.components.prompts.Popup;
import com.oss.framework.widgets.table.TableWidget;

public class BookmarksTest extends BaseTestCase {

    private static final String TABLE_WIDGET_URL =
            String.format("%s/#/views/management/views/inventory-view/Location?perspective=LIVE", BASIC_URL);

    private TableWidget tableWidget;

    @Test
    public void createBookmark() {
        tableWidget = homePage
                .goToInventoryViewPage(TABLE_WIDGET_URL)
                .getMainTable();

        Popup popupV2 = homePage.goToCreateBookmarkPopUp();

        // TODO fix new WebDriver, create Page for bookmarks, try to use wizard class
        popupV2.setComponentValue("viewName", "Selenium Bookmark");
        Assert.assertEquals(popupV2.getComponent("viewName").getStringValue(), "Selenium Bookmark");
        popupV2.setComponentValue("viewCategory", "OSSMFC");
        Assert.assertEquals(popupV2.getComponent("viewCategory").getStringValue(), "OSSMFC");
        popupV2.clickButtonByLabel("Save");

    }
}
