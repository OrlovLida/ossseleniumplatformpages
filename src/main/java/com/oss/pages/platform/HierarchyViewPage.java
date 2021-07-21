package com.oss.pages.platform;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.portals.SaveConfigurationWizard.Field;
import com.oss.framework.mainheader.ButtonPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.TreeWidgetV2.TreeWidgetV2;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.tabswidget.TabsWidget;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class HierarchyViewPage extends BasePage {

    private static final String TOP_TABS_WIDGET_ID = "HierarchyView_TopDetailTabs_%s";
    private static final String BOTTOM_TABS_WIDGET_ID = "HierarchyView_BottomDetailTabs_%s";
    private static final String TOP_PROPERTY_PANEL_ID = "HierarchyView_TopDetailTabs_%sHierarchyView_PropertyPanelWidget_%s";
    private static final String BOTTOM_PROPERTY_PANEL_ID = "HierarchyView_BottomDetailTabs_%sInventoryView_PropertyPanelWidget_%s";
    private static final String HIERARCHY_VIEW_TREE_WIDGET_ID = "HierarchyTreeWidget";

    public static HierarchyViewPage openHierarchyViewPage(WebDriver driver, String basicURL, String type) {
        driver.get(String.format("%s/#/views/management/views/hierarchy-view/" + type +
                "?perspective=LIVE", basicURL));
        WebDriverWait wait = new WebDriverWait(driver, 45);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new HierarchyViewPage(driver, wait);
    }

    public HierarchyViewPage(WebDriver driver) {
        super(driver);
    }

    private HierarchyViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public TreeWidgetV2 getTreeWidget() {
//        Widget.waitForWidget(wait, "TreeWidget");
        return TreeWidgetV2.create(driver, wait, HIERARCHY_VIEW_TREE_WIDGET_ID);
    }

    public TabsWidget getTopTabsWidget(String type) {
        Widget.waitForWidget(wait, TabsWidget.TABS_WIDGET_CLASS);
        return TabsWidget.createById(driver, wait, String.format(TOP_TABS_WIDGET_ID, type));
    }

    public TabsWidget getBottomTabsWidget(String type) {
        Widget.waitForWidget(wait, TabsWidget.TABS_WIDGET_CLASS);
        return TabsWidget.createById(driver, wait, String.format(BOTTOM_TABS_WIDGET_ID, type));
    }

    public PropertyPanel getTopPropertyPanel(String type) {
        Widget.waitForWidget(wait, PropertyPanel.PROPERTY_PANEL_CLASS);
        return PropertyPanel.createById(driver, String.format(TOP_PROPERTY_PANEL_ID, type, type));
    }

    public PropertyPanel getBottomPropertyPanel(String type) {
        Widget.waitForWidget(wait, PropertyPanel.PROPERTY_PANEL_CLASS);
        return PropertyPanel.createById(driver, String.format(BOTTOM_PROPERTY_PANEL_ID, type, type));
    }

    @Step("Open Hierarchy View")
    public static HierarchyViewPage goToHierarchyViewPage(WebDriver driver, String basicURL, String type, String xid) {
        driver.get(String.format("%s/#/views/management/views/hierarchy-view/" + type + "?" + xid +
                "&perspective=LIVE", basicURL));
        return new HierarchyViewPage(driver);
    }

    @Step("Save configuration for page")
    public HierarchyViewPage savePageConfiguration(Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).openSaveConfigurationWizard().save(fields);
        return this;
    }

    @Step("Save new configuration for page")
    public HierarchyViewPage saveNewPageConfiguration(String configurationName, Field... fields) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).openSaveConfigurationWizard().saveAsNew(configurationName, fields);
        return this;
    }

    @Step("Apply configuration for page")
    public HierarchyViewPage applyConfigurationForPage(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).openChooseConfigurationWizard().chooseConfiguration(configurationName).apply();
        return this;
    }

    @Step("Download configuration for page")
    public HierarchyViewPage downloadConfigurationForPage(String configurationName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).openDownloadConfigurationWizard().chooseConfiguration(configurationName).apply();
        return this;
    }

    @Step("Select First Object on Tree Widget")
    public HierarchyViewPage selectFirstObject() {
        getTreeWidget().selectFirstNode();
        return this;
    }

    @Step("Use tree context action")
    public void useTreeContextAction(String groupId, String actionId) {
        getTreeWidget().callActionById(groupId, actionId);
    }

    @Step("Click {label} in Confirmation box")
    public void clickButtonInConfirmationBox(String label) {
        Wizard wizard = Wizard.createPopupWizard(driver, wait);
        wizard.clickButtonByLabel(label);
    }

    @Step("Search in Hierarchy View for {text}")
    public void performSearch(String text) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeWidget().performSearch(text);
    }

    @Step("Expand tree node by label - {label}")
    public void expandTreeNode(String label) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeWidget().expandNodeWithLabel(label);
    }

    @Step("Expand first collapsed tree node")
    public void expandFirstCollapsedTreeNode() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeWidget().expandNode();
    }

    @Step("Select tree node by position - {position}")
    public void selectNodeByPosition(int position) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeWidget()
                .selectNodeByPosition(position);
    }

    @Step("Select tree node by label - {label}")
    public void selectNodeByLabel(String label) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTreeWidget()
                .selectNodeByLabel(label);
    }
}
