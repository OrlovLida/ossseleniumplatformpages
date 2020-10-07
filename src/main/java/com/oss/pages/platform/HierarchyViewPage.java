package com.oss.pages.platform;

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

    private TreeWidget mainTree;

    public HierarchyViewPage(WebDriver driver) {
        super(driver);
    }


    private TabsWidget topTabsWidget;
    private TabsWidget bottomTabsWidget;
    private PropertyPanel topPropertyPanel;
    private PropertyPanel bottomPropertyPanel;
    private PropertiesFilter bottomPropertiesFilter;
    private PropertiesFilter topPropertiesFilter;

    private final String TOP_TABS_WIDGET_ID = "HierarchyView_TopDetailTabs_%s";
    private final String BOTTOM_TABS_WIDGET_ID = "HierarchyView_BottomDetailTabs_%s";
    private final String TOP_PROPERTY_PANEL_ID = "HierarchyView_TopDetailTabs_%sHierarchyView_PropertyPanelWidget_%s";
    private final String BOTTOM_PROPERTY_PANEL_ID = "HierarchyView_BottomDetailTabs_%sInventoryView_PropertyPanelWidget_%s";

    public TreeWidget getTreeWidget() {
        if(mainTree == null){
            Widget.waitForWidget(wait, "TreeWidget");
            mainTree = TreeWidget.createByClass(driver, "TreeWidget", wait);
        }
        return mainTree;
    }

    public TabsWidget getTopTabsWidget(String type) {
        if (topTabsWidget == null) {
            Widget.waitForWidget(wait, TabsWidget.TABS_WIDGET_CLASS);
            topTabsWidget = TabsWidget.createById(driver, wait, String.format(TOP_TABS_WIDGET_ID, type));
        }
        return topTabsWidget;
    }

    public TabsWidget getBottomTabsWidget(String type) {
        if (bottomTabsWidget == null) {
            Widget.waitForWidget(wait, TabsWidget.TABS_WIDGET_CLASS);
            bottomTabsWidget = TabsWidget.createById(driver, wait, String.format(BOTTOM_TABS_WIDGET_ID, type));
        }
        return bottomTabsWidget;
    }

    public PropertyPanel getTopPropertyPanel(String type) {
        if (topPropertyPanel == null) {
            Widget.waitForWidget(wait, PropertyPanel.PROPERTY_PANEL_CLASS);
            topPropertyPanel = PropertyPanel.createById(driver, String.format(TOP_PROPERTY_PANEL_ID, type, type));
        }
        return topPropertyPanel;
    }

    public PropertyPanel getBottomPropertyPanel(String type) {
        if (bottomPropertyPanel == null) {
            Widget.waitForWidget(wait, PropertyPanel.PROPERTY_PANEL_CLASS);
            bottomPropertyPanel = PropertyPanel.createById(driver, String.format(BOTTOM_PROPERTY_PANEL_ID, type, type));
        }
        return bottomPropertyPanel;
    }

    public PropertiesFilter getBottomPropertiesFilter(String type) {
        if (bottomPropertiesFilter == null) {
            Widget.waitForWidget(wait, PropertiesFilter.PROPERTIES_FILTER_CLASS);
            bottomPropertiesFilter = PropertiesFilter.createByPropertyPanel(driver, wait, getBottomPropertyPanel(type));
        }
        return bottomPropertiesFilter;
    }

    public PropertiesFilter getTopPropertiesFilter(String type) {
        if (topPropertiesFilter == null) {
            Widget.waitForWidget(wait, PropertiesFilter.PROPERTIES_FILTER_CLASS);
            topPropertiesFilter = PropertiesFilter.createByPropertyPanel(driver, wait, getTopPropertyPanel(type));
        }
        return topPropertiesFilter;
    }

    @Step("Open Hierarchy View")
    public static HierarchyViewPage goToHierarchyViewPage(WebDriver driver, String basicURL, String type, String xid) {
        driver.get(String.format("%s/#/views/management/views/hierarchy-view/" + type + "?" + xid +
                "&perspective=LIVE" , basicURL));
        return new HierarchyViewPage(driver);
    }

    @Step("Open save configuration wizard for page")
    public SaveConfigurationWizard openSavePageConfigurationWizard(){
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickOnIcon("fa fa-fw fa-floppy-o");
        return new SaveConfigurationWizard(driver);
    }

    @Step("Open choose configuration wizard for page")
    public ChooseConfigurationWizard openChooseConfigurationForPageWizard(){
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickOnIcon("fa fa-fw fa-cog");
        return new ChooseConfigurationWizard(driver);
    }

    @Step("Open download configuration wizard for page")
    public ChooseConfigurationWizard openDownloadConfigurationForPageWizard(){
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonPanel.create(driver, wait).clickOnIcon("fa fa-fw fa-download");
        return new ChooseConfigurationWizard(driver);
    }

    @Step("Select First Object on Tree Widget")
    public HierarchyViewPage selectFirstObject() {
        getTreeWidget().selectNode();
        return this;
    }
}
