package com.oss.pages.gisView;


import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.gisMap.GisMap;
import com.oss.framework.widgets.gisMap.GisMapInterface;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

public class GisViewPage extends BasePage {

    protected GisViewPage(WebDriver driver) {
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

    public void useContextAction(String group, String label) {
        GisMapInterface gisMap = GisMap.create(driver, wait);
        gisMap.callActionByLabel(group, label);
    }

    public void useContextActionById(String actionId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        GisMapInterface gisMap = GisMap.create(driver, wait);
        DelayUtils.waitForPageToLoad(driver, wait);
        gisMap.callActionById(actionId);
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
//TODO Change on creating button by id when data-attributename will be added
    public void enableLayerInTree(String layer) {
        Button layersTreeButton = Button.createByIcon(driver, "fa fa-fw fa-bars", "");
        layersTreeButton.click();
        TreeWidget tree = TreeWidget.createByClass(driver, "tree-component", wait);
        tree.setValueOnCheckboxByNodeLabel(layer, true);
    }
}
