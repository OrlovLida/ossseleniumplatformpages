package com.oss;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.portals.PopupV2;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

public class BookmarksTest extends BaseTestCase {
    
    private static final String TABLE_WIDGET_URL =
            String.format("%s/#/views/management/views/inventory-view/Location?perspective=LIVE", BASIC_URL);
    
    private TableWidget tableWidget;
    
    @Test
    public void createBookmark() {
        tableWidget = homePage
                .goToInventoryViewPage(TABLE_WIDGET_URL)
                .getMainTable();
        
        PopupV2 popupV2 = homePage.goToCreateBookmarkPopUp();
        
        // TODO fix new WebDriver, create Page for bookmarks, try to use wizard class
        popupV2.setComponentValue("viewName", "Selenium Bookmark", ComponentType.TEXT_FIELD);
        Assert.assertEquals(popupV2.getComponent("viewName", ComponentType.TEXT_FIELD).getStringValue(),"Selenium Bookmark");
        popupV2.setComponentValue("viewCategory", "OSSMFC", ComponentType.COMBOBOX);
        Assert.assertEquals(popupV2.getComponent("viewCategory", ComponentType.COMBOBOX).getStringValue(),"OSSMFC");
        popupV2.clickButtonByLabel("Save");
        
    }
}
