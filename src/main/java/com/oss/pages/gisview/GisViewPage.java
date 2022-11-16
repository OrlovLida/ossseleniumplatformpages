package com.oss.pages.gisview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

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
    private static final String GIS_MAP_ID = "template-gisview";

    private GisViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static GisViewPage getGisViewPage(WebDriver driver, WebDriverWait wait) {
        return new GisViewPage(driver, wait);
    }

    public boolean isCanvasPresent() {
        return getGisMapInterface().isCanvasPresent();
    }

    public void setMap(String mapLabel) {
        getGisMapInterface().setMap(mapLabel);
    }

    public String getCanvasObject() {
        return getGisMapInterface().getCanvasObject();
    }

    public void searchResult(String value) {
        getGisMapInterface().setValue(value);
    }

    public void chooseOptionFromDropDownList(String buttonId, String optionId) {
        Button.createById(driver, buttonId).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        DropdownList.create(driver, wait).selectOptionById(optionId);
    }

    public void useContextActionByLabel(String actionLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getGisMapInterface().callActionByLabel(actionLabel);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clickOnMapByCoordinates(int x, int y) {
        getGisMapInterface().clickMapByCoordinates(x, y);
    }

    public void doubleClickOnMapByCoordinates(int x, int y) {
        getGisMapInterface().doubleClickMapByCoordinates(x, y);
    }

    public void dragAndDropObject(int xSource, int ySource, int xDestination, int yDestination) {
        getGisMapInterface().dragAndDropObject(xSource, ySource, xDestination, yDestination);
    }

    public void clickButtonInPopupByLabel(String label) {
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(label);
    }

    public void enableLayerInTree(String layer) {
        Button layersTreeButton = Button.createByLabel(driver, GIS_MAP_ID, "Layers tree");
        layersTreeButton.click();
        getGisMapInterface().getLayersTree().toggleNodeByPath(layer);
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

    private GisMapInterface getGisMapInterface() {
        return GisMap.create(driver, wait, GIS_MAP_ID);
    }
}
