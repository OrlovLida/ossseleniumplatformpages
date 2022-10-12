package com.oss.pages.resourcecatalog;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.treetable.OldTreeTableWidget;
import com.oss.pages.BasePage;

public class ResourceSpecificationsViewPage extends BasePage {

    private static final String RESOURCE_SPECIFICATIONS_TREE_TABLE_ID = "rsTreeTableId";
    private static final String SPECIFICATION_NAME_COLUMN = "Specification Name";

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

    public List<String> getAllVisibleSpecificationNames() {
        return this.treeTable.getAllVisibleNodes(SPECIFICATION_NAME_COLUMN);
    }

    public boolean isSpecificationNamePresent(String value) {
        return this.treeTable.isValuePresent(SPECIFICATION_NAME_COLUMN, value);
    }

    public void searchByAttribute(String attributeId, String value) {
        this.treeTable.searchByAttribute(attributeId, value);
    }

    public void clickClearAll() {
        this.treeTable.clickClearAll();
    }

    public void collapseFirstNodeIfPresent() {
        this.treeTable.collapseNodeIfPresent(0);
    }

    public void setPageSize(int pageOption) {
        this.treeTable.setPageSize(pageOption);
    }
}
