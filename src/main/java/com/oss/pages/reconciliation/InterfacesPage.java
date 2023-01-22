package com.oss.pages.reconciliation;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;

public class InterfacesPage extends BasePage {

    private static final String TABLE_WIDGET_ID = "MainTableWidget";
    private static final String TAB_WIDGET_ID = "DetailTabsCard";
    private static final String PROPERTY_PANEL_ID = "PropertyPanelWidget";

    private InterfacesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static InterfacesPage getInterfacesPage(WebDriver driver, WebDriverWait wait) {
        return new InterfacesPage(driver, wait);
    }

    public boolean hasNoData() {
        return getTableWidget().hasNoData();
    }

    public void selectFirstInterface() {
        getTableWidget().selectFirstRow();
    }

    public List<String> getColumnsHeaders() {
        return getTableWidget().getActiveColumnHeaders();
    }

    public String getTabLabel(int tab) {
        return getTabWidget().getTabLabel(tab);
    }

    public List<String> getPropertiesNames() {
        return getPropertyPanel().getPropertyLabels();
    }

    private TableWidget getTableWidget() {
        return TableWidget.createById(driver, TABLE_WIDGET_ID, wait);
    }

    private TabsWidget getTabWidget() {
        return TabsWidget.createById(driver, wait, TAB_WIDGET_ID);
    }

    private PropertyPanel getPropertyPanel() {
        return PropertyPanel.createById(driver, wait, PROPERTY_PANEL_ID);
    }
}
