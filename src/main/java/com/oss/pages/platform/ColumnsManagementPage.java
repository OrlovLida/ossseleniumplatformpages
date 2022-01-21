package com.oss.pages.platform;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.pages.BasePage;

@Deprecated
public class ColumnsManagementPage extends BasePage {

    TreeWidget treeWidget = TreeWidget.createByClass(driver, "management_attributes", wait);

    public ColumnsManagementPage(WebDriver driver) {
        super(driver);
    }

    public ColumnsManagementPage checkTheCheckbox(String nodeLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        treeWidget.setValueOnCheckboxByNodeLabel(nodeLabel, true);
        return this;
    }

    public ColumnsManagementPage uncheckTheCheckbox(String nodeLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        treeWidget.setValueOnCheckboxByNodeLabel(nodeLabel, false);
        return this;
    }

}
