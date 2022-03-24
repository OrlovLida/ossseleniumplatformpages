package com.oss.pages.floorplan;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.widgets.floorplan.FloorPlanTab;
import com.oss.framework.widgets.floorplan.FloorPlanTable;
import com.oss.framework.widgets.floorplan.FloorPlanTree;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import com.oss.pages.platform.HomePage;

import io.qameta.allure.Step;

public class FloorPlanPage extends BasePage {
    private static final String LOCATIONS_TREE_ID = "cell_floorPlanConfTree";
    private static final String LAYERS_TABLE_ID = "cell_floorPlanObjectHome_tab_1";
    private static final String SUMMARY_TABLE_ID = "cell_floorPlanObjectHome_tab_2";
    private static final String RIGHT_PANEL_ID = "objHomeMain";

    public FloorPlanPage(WebDriver driver) {
        super(driver);
    }

    @Step("Expand location {locationName}")
    public void expandLocation(String locationName) {
        getTree(LOCATIONS_TREE_ID).expandNodeByName(locationName);
    }

    @Step("Expand summary")
    public void expandSummary(String name) {
        getTree(SUMMARY_TABLE_ID).expandNodeByName(name);
    }

    @Step("Open {locationName} in this view")
    public void openLocationInThisView(String locationName) {
        getTree(LOCATIONS_TREE_ID).openLocationInThisView(locationName);
    }

    @Step("Import floor plan for selected location")
    public void importFloorPlan(String filePath) {
        Button.createByLabel(driver, "Import").click();
        DelayUtils.waitForPageToLoad(driver, wait);
        URL res = getClass().getClassLoader().getResource(filePath);
        try {
            File file = Paths.get(res.toURI()).toFile();
            WebElement dropzone = driver.findElement(By.xpath("//div[contains(@class, 'FileUploader-dropZone')]"));
            DragAndDropFile(file, dropzone, 0, 0);
            DelayUtils.waitForPageToLoad(driver, wait);
            //TODO change to button when OSSWEB-10577 will be ready
            driver.findElement(By.xpath(".//button[contains(@class, FileUploader-buttonBrowse)]/span[text()='Upload']/parent::button")).click();
            DelayUtils.waitByXPath(wait, "//div[contains(@class,  'FileUploader-responseMessage')]/span[text() = 'Upload succeeded']");
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cant load file", e);
        }
    }

    @Step("Select tab")
    public void selectTab(String tabName) {
        FloorPlanTab tabsWidget = FloorPlanTab.createById(driver, wait, RIGHT_PANEL_ID);
        tabsWidget.selectTabByLabel(tabName);
    }

    @Step("Show layer - {layerName}")
    public void showLayer(String layerName) {
        FloorPlanTable floorPlanTable = FloorPlanTable.createById(driver, wait, LAYERS_TABLE_ID);
        int rowNr = floorPlanTable.getRowNumber(layerName);
        floorPlanTable.selectRow(rowNr, 2);
    }

    @Step("Show label - {labelName}")
    public void showLabels(String labelName) {
        FloorPlanTable floorPlanTable = FloorPlanTable.createById(driver, wait, LAYERS_TABLE_ID);
        int rowNr = floorPlanTable.getRowNumber(labelName);
        floorPlanTable.selectRow(rowNr, 3);
    }

    @Step("Enable snapping - {name}")
    public void enableSnapping(String name) {
        FloorPlanTable floorPlanTable = FloorPlanTable.createById(driver, wait, LAYERS_TABLE_ID);
        int rowNr = floorPlanTable.getRowNumber(name);
        floorPlanTable.selectRow(rowNr, 4);
    }

    @Step("Export floor plan")
    public void exportFloorPlan() {
        Button export = Button.createByLabel(driver, "Export");
        export.click();
        DelayUtils.waitForPageToLoad(driver, wait);
        //TODO change to button when OSSWEB-10577 will be ready
        driver.findElement(By.xpath(".//button[contains(@class, Export-buttonExport)]/span[text()='Export']/parent::button")).click();
        DelayUtils.waitForElementDisappear(wait, driver.findElement(By.xpath(".//div[@class='preloader']")));
        export.click();
        DelayUtils.waitByXPath(wait, "//span[text() = 'Export succeeded']");
    }

    @Step("Go back to Home Page")
    public HomePage goToHomePage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/", basicURL));
        return new HomePage(driver);
    }

    @Step("Get cell value")
    public String getCellValue(String nodeName, int columnNr) {
        return getTree("cell_floorPlanObjectHome_tab_2").geCellValue(nodeName, columnNr);
    }

    private FloorPlanTree getTree(String treeId) {
        return FloorPlanTree.createById(driver, wait, treeId);
    }

    private void DragAndDropFile(File filePath, WebElement target, int offsetX, int offsetY) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String JS_DROP_FILE =
                "var target = arguments[0]," +
                        "    offsetX = arguments[1]," +
                        "    offsetY = arguments[2]," +
                        "    document = target.ownerDocument || document," +
                        "    window = document.defaultView || window;" +
                        "" +
                        "var input = document.createElement('INPUT');" +
                        "input.type = 'file';" +
                        "input.style.display = 'none';" +
                        "input.onchange = function () {" +
                        "  var rect = target.getBoundingClientRect()," +
                        "      x = rect.left + (offsetX || (rect.width >> 1))," +
                        "      y = rect.top + (offsetY || (rect.height >> 1))," +
                        "      dataTransfer = { files: this.files };" +
                        "" +
                        "  ['dragenter', 'dragover', 'drop'].forEach(function (name) {" +
                        "    var evt = document.createEvent('MouseEvent');" +
                        "    evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);" +
                        "    evt.dataTransfer = dataTransfer;" +
                        "    target.dispatchEvent(evt);" +
                        "  });" +
                        "" +
                        "  setTimeout(function () { document.body.removeChild(input); }, 25);" +
                        "};" +
                        "document.body.appendChild(input);" +
                        "return input;";

        WebElement input = (WebElement) jse.executeScript(JS_DROP_FILE, target, offsetX, offsetY);
        input.sendKeys(filePath.getAbsoluteFile().toString());
        wait.until(ExpectedConditions.stalenessOf(input));
    }
}
