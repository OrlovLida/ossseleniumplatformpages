package com.oss.pages.resourcecatalog;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.treetable.OldTreeTableWidget;
import com.oss.pages.BasePage;

public class ResourceSpecificationsViewPage extends BasePage {

    private static final String RESOURCE_SPECIFICATIONS_TREE_TABLE_ID = "rsTreeTableId";

    private final OldTreeTableWidget treeTable;

    private ResourceSpecificationsViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        this.treeTable = OldTreeTableWidget.create(driver, wait, RESOURCE_SPECIFICATIONS_TREE_TABLE_ID);
    }

    public static ResourceSpecificationsViewPage create(WebDriver driver, WebDriverWait wait) {
        Widget.waitForWidgetById(wait, RESOURCE_SPECIFICATIONS_TREE_TABLE_ID);
        return new ResourceSpecificationsViewPage(driver, wait);
    }

    public void setSearchText(String searchText) {
        this.treeTable.fullTextSearch(searchText);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void selectTreeNode(String value, String attributeNameLabel) {
        this.treeTable.selectNode(value, attributeNameLabel);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void callActionByLabel(String groupLabel, String actionLabel) {
        this.treeTable.callActionByLabel(groupLabel, actionLabel);
    }
}
