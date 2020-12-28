package com.oss.pages.platform;

import com.oss.framework.components.portals.SaveConfigurationWizard.Field;
import com.oss.framework.mainheader.ButtonPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.propertypanel.PropertiesFilter;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.tabswidget.TabsWidget;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class HierarchyViewPage extends BasePage {

    private static final String TOP_TABS_WIDGET_ID = "HierarchyView_TopDetailTabs_%s";
    private static final String BOTTOM_TABS_WIDGET_ID = "HierarchyView_BottomDetailTabs_%s";
    private static final String TOP_PROPERTY_PANEL_ID = "HierarchyView_TopDetailTabs_%sHierarchyView_PropertyPanelWidget_%s";
    private static final String BOTTOM_PROPERTY_PANEL_ID = "HierarchyView_BottomDetailTabs_%sInventoryView_PropertyPanelWidget_%s";

    public HierarchyViewPage(WebDriver driver) {
        super(driver);
    }

    public TreeWidget getTreeWidget() {
        Widget.waitForWidget(wait, "TreeWidget");
        return TreeWidget.createByClass(driver, "TreeWidget", wait);
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

    @Deprecated
    public PropertiesFilter getBottomPropertiesFilter(String type) {
        Widget.waitForWidget(wait, PropertiesFilter.PROPERTIES_FILTER_CLASS);
        return PropertiesFilter.create(driver, wait);
    }

    @Deprecated
    public PropertiesFilter getTopPropertiesFilter(String type) {
        Widget.waitForWidget(wait, PropertiesFilter.PROPERTIES_FILTER_CLASS);
        return PropertiesFilter.create(driver, wait);
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
        getTreeWidget().selectNode();
        return this;
    }
}
