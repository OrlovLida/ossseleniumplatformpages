package com.oss.pages.platform;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.ColumnsManagement;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

@Deprecated
public class ColumnsManagementPage extends BasePage {

    public ColumnsManagementPage (WebDriver driver){ super(driver);
    }

    TreeWidget treeWidget = TreeWidget.createByClass(driver, "management_attributes", wait);

    public ColumnsManagementPage checkTheCheckbox(String nodeLabel){
        DelayUtils.waitForPageToLoad(driver, wait);
        treeWidget.setValueOnCheckboxByNodeLabel(nodeLabel, true);
        return this;
    }

    public ColumnsManagementPage uncheckTheCheckbox(String nodeLabel){
        DelayUtils.waitForPageToLoad(driver, wait);
        treeWidget.setValueOnCheckboxByNodeLabel(nodeLabel, false);
        return this;
    }

    public void applyChanges(){
        ColumnsManagement.create(driver).clickApply();
    }
}
