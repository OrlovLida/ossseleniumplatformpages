package com.oss.pages.gisview;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.dockedpanel.DockedPanel;
import com.oss.framework.widgets.dockedpanel.DockedPanelInterface;
import com.oss.framework.widgets.gismap.GisMap;
import com.oss.framework.widgets.gismap.GisMapInterface;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;

public class GisViewPage extends BasePage {
    private static final String GIS_VIEW_TABS_ID = "gis_view_tabs";

    public GisViewPage(WebDriver driver) {
        super(driver);
    }

    public void searchResult(String value) {
        GisMapInterface gisMap = GisMap.create(driver, wait);
        gisMap.setValue(value);
    }

    public void chooseOptionFromDropDownList(String buttonId, String optionId) {
        Button.createById(driver, buttonId).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        DropdownList.create(driver, wait).selectOptionById(optionId);
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
        gisMap.clickMapByCoordinates(x, y);
    }

    public void doubleClickOnMapByCoordinates(int x, int y) {
        GisMapInterface gisMap = GisMap.create(driver, wait);
        gisMap.doubleClickMapByCoordinates(x, y);
    }

    public void chooseObjectFromList(String name, int x, int y) {
        GisMapInterface gisMap = GisMap.create(driver, wait);
        gisMap.clickMapByCoordinatesWithShift(x, y);
        DelayUtils.waitForPageToLoad(driver, wait);
        //Nie wygląda mi to na button
//        Button button = Button.create(driver, name);
//        button.click();
    }

    public void dragAndDropObject(int xSource, int ySource, int xDestination, int yDestination) {
        GisMapInterface gisMap = GisMap.create(driver, wait);
        gisMap.dragAndDropObject(xSource, ySource, xDestination, yDestination);
    }

    public void clickButtonInPopupByLabel(String label) {
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(label);
    }

    public void enableLayerInTree(String layer) {
        GisMapInterface gisMap = GisMap.create(driver, wait);
        Button layersTreeButton = Button.createByLabel(driver, "template-gisview", "Layers tree");
        layersTreeButton.click();
        gisMap.getLayersTree().toggleNodeByPath(layer);
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
        TabsWidget tab = TabsWidget.createById(driver, wait, GIS_VIEW_TABS_ID);
        tab.selectTabById(id);
    }

    public String getCellValue(int row, String label) {
        TableInterface table = OldTable.createById(driver, wait, GIS_VIEW_TABS_ID);
        return table.getCellValue(row, label);
    }
}
