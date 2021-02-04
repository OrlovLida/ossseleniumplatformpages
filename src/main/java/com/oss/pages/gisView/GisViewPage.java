package com.oss.pages.gisView;


import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.dockedPanel.DockedPanel;
import com.oss.framework.widgets.dockedPanel.DockedPanelInterface;
import com.oss.framework.widgets.gisMap.GisMap;
import com.oss.framework.widgets.gisMap.GisMapInterface;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.TabsWidget;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

public class GisViewPage extends BasePage {
    private static final String GIS_VIEW_TABS_DATA_ATTRIBUTE_NAME = "gis_view_tabs";

    public GisViewPage(WebDriver driver) {
        super(driver);
    }

    public static GisViewPage goToGisViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/gis-view/opd" +
                "?perspective=LIVE", basicURL));
        return new GisViewPage(driver);
    }

    public void searchFirstResult(String value) {
        GisMapInterface gisMap = GisMap.create(driver, wait);
        gisMap.searchFirstResult(value);
    }

    public void searchResult(String value) {
        GisMapInterface gisMap = GisMap.create(driver, wait);
        gisMap.searchResult(value);
    }

    public void chooseOptionFromDropDownList(String buttonId, String optionId) {
        Button.createById(driver, buttonId).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        DropdownList.create(driver, wait).selectOptionWithId(optionId);
    }

    public void useContextActionByLabel(String actionLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        GisMapInterface gisMap = GisMap.create(driver, wait);
        DelayUtils.waitForPageToLoad(driver, wait);
        gisMap.callActionByLabel(actionLabel);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clickOnMapByCoordinates(int x, int y) {
        GisMapInterface gisMap = GisMap.create(driver, wait);
        gisMap.clickOnMapByCoordinates(x, y);
    }

    public void doubleClickOnMapByCoordinates(int x, int y) {
        GisMapInterface gisMap = GisMap.create(driver, wait);
        gisMap.doubleClickOnMapByCoordinates(x, y);
    }

    public void chooseObjectFromList(String name, int x, int y) {
        GisMapInterface gisMap = GisMap.create(driver, wait);
        gisMap.clickOnMapByCoordinatesWithShift(x, y);
        DelayUtils.waitForPageToLoad(driver, wait);
        Button button = Button.create(driver, name);
        button.click();
    }

    public void dragAndDropObject(int xSource, int ySource, int xDestination, int yDestination) {
        GisMapInterface gisMap = GisMap.create(driver, wait);
        gisMap.dragAndDropObject(xSource, ySource, xDestination, yDestination);
    }

    public void clickButtonInPopupByLabel(String label) {
        getWizard().clickButtonByLabel(label);
    }

    //TODO Change on creating button by id when data-attributename will be added
    public void enableLayerInTree(String layer) {
        Button layersTreeButton = Button.createByIcon(driver, "fa fa-fw fa-bars", "");
        layersTreeButton.click();
        TreeWidget tree = TreeWidget.createByClass(driver, "tree-component", wait);
        tree.setValueOnCheckboxByNodeLabel(layer, true);
        layersTreeButton.click();
    }

    public void expandDockedPanel(String position) {
        DelayUtils.waitForPageToLoad(driver, wait);
        DockedPanelInterface dockedPanel = DockedPanel.createDockedPanelByPosition(driver, wait, position);
        dockedPanel.expandDockedPanel(position);
    }

    public void hideDockedPanel(String position) {
        DelayUtils.waitForPageToLoad(driver, wait);
        DockedPanelInterface dockedPanel = DockedPanel.createDockedPanelByPosition(driver, wait, position);
        dockedPanel.hideDockedPanel(position);
    }

    public void selectTab(String id) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsWidget tab = TabsWidget.createById(driver, wait, GIS_VIEW_TABS_DATA_ATTRIBUTE_NAME);
        tab.selectTabById(id);
    }

    public String getCellValue(int row, String label) {
        TableInterface table = OldTable.createByComponentId(driver, wait, "gis_view_tabs");
        return table.getCellValue(row, label);
    }

    public Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, "Popup");
    }
}
