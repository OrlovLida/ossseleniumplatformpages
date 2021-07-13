package com.oss.pages.bigdata.kqiview;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.dpe.kpichartwidget.KpiChartWidget;
import com.oss.framework.widgets.dpe.toolbarpanel.ExportPanel;
import com.oss.framework.widgets.dpe.toolbarpanel.ExportPanel.ExportType;
import com.oss.framework.widgets.dpe.toolbarpanel.FiltersPanel;
import com.oss.framework.widgets.dpe.toolbarpanel.KpiToolbarPanel;
import com.oss.framework.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType;
import com.oss.framework.widgets.dpe.toolbarpanel.OptionsPanel;
import com.oss.framework.widgets.dpe.toolbarpanel.TopNPanel;
import com.oss.framework.widgets.dpe.treewidget.KpiTreeWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class KpiViewPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(KpiViewPage.class);

    private static final String INDICATORS_TREE_ID = "_Indicators";
    private static final String DIMENSIONS_TREE_ID = "_Dimensions";
    private static final String CHART_TYPE_BUTTON_ID = "chart-type-button";

    public KpiViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open KPI View")
    public static KpiViewPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 90);

        String pageUrl = String.format("%s/#/view/Assurance/KPIView", basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new KpiViewPage(driver, wait);
    }

    private void selectTreeNodes(List<String> nodesToExpand, List<String> nodesToSelect, String componentId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        KpiTreeWidget indicatorsTree = KpiTreeWidget.create(driver, wait, componentId);
        indicatorsTree.selectNodes(nodesToExpand, nodesToSelect);
        log.debug("Expanded nodes: {}", nodesToExpand);
        log.debug("Selecting: {}", nodesToSelect);
    }

    private void selectUnfoldedTreeNodes(List<String> nodesToSelect, String componentId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        KpiTreeWidget indicatorsTree = KpiTreeWidget.create(driver, wait, componentId);
        indicatorsTree.selectExpandedObjects(nodesToSelect);
//        log.debug("Expanded nodes: {}", nodesToExpand);
        log.debug("Selecting: {}", nodesToSelect);
    }

    @Step("I set filters: {enabledFilters}")
    public void setFilters(List<String> enabledFilters) {
        DelayUtils.waitForPageToLoad(driver, wait);
        KpiToolbarPanel toolbar = KpiToolbarPanel.create(driver, wait);
        FiltersPanel filtersPanel = toolbar.getFiltersPanel();
        filtersPanel.openFilters();
        filtersPanel.clearFilters();
        filtersPanel.turnOnFilters(enabledFilters);
        filtersPanel.clickConfirm();

        log.info("Selected filters: {}", enabledFilters);
    }

    @Step("I select indicator")
    public void selectIndicator(List<String> nodesToExpand, List<String> nodesToSelect) {
        log.info("Select indicator nodes: " + nodesToSelect);
        selectTreeNodes(nodesToExpand, nodesToSelect, INDICATORS_TREE_ID);
    }

    @Step("I select dimension")
    public void selectDimension(List<String> nodesToExpand, List<String> nodesToSelect) {
        log.info("Select dimension nodes: " + nodesToSelect);
        selectTreeNodes(nodesToExpand, nodesToSelect, DIMENSIONS_TREE_ID);
    }

    @Step("I select unfolded dimension")
    public void selectUnfoldedDimension(List<String> nodesToSelect) {
        log.info("Select dimension nodes: " + nodesToSelect);
        selectUnfoldedTreeNodes(nodesToSelect, DIMENSIONS_TREE_ID);
    }

    @Step("I apply changes")
    public void applyChanges() {
        log.info("Apply changes");
        KpiToolbarPanel toolbar = KpiToolbarPanel.create(driver, wait);
        toolbar.clickApply();
    }

    //    @Step("I see chart is displayed")
//    public void seeChartIsDisplayed(){
//        log.info("Waiting for chart presence");
//        KpiChartWidget.create(driver, wait).waitForPresenceAndVisibility();
//    }
    @Step("I see chart is displayed")
    public void seeChartIsDisplayed() {
        log.info("Waiting for chart presence");
        KpiChartWidget.create(driver, wait).waitForPresenceAndVisibility();
    }

    @Step("I hover over some point")
    public void hoverOverPoint() {
        log.info("Hovering over some point");
        KpiChartWidget.create(driver, wait).hoverMouseOverPoint();
    }

    @Step("I export chart")
    public void exportChart() {
        KpiToolbarPanel toolbar = KpiToolbarPanel.create(driver, wait);
        ExportPanel exportPanel = toolbar.getExportPanel();
        log.info("Exporting chart to JPG");
        exportPanel.exportKpiToFile(ExportType.JPG);
        log.info("Exporting chart to PNG");
        exportPanel.exportKpiToFile(ExportType.PNG);
        log.info("Exporting chart to PDF");
        exportPanel.exportKpiToFile(ExportType.PDF);
        log.info("Exporting chart to XLSX");
        exportPanel.exportKpiToFile(ExportType.XLSX);
    }

    @Step("Attach exported chart to report")
    public void attachExportedChartToReport() {
        if (ifDownloadDirExists()) {
            File directory = new File(CONFIGURATION.getDownloadDir());
            List<File> listFiles = (List<File>) FileUtils.listFiles(directory, new WildcardFileFilter("kpi_view_export_*.*"), null);
            try {
                for (File file : listFiles) {
                    log.info("Attaching file: {}", file.getCanonicalPath());
                    attachFile(file);
                }
            } catch (IOException e) {
                log.error("Failed attaching files: {}", e.getMessage());
            }

        }
    }

    @Attachment(value = "Exported chart")
    public byte[] attachFile(File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }

    @Step("I change layout")
    public void changeLayout() {
        KpiToolbarPanel toolbar = KpiToolbarPanel.create(driver, wait);
        toolbar.getLayoutPanel().changeLayout(LayoutType.LAYOUT_2x2);
    }

    @Step("I maximize chart")
    public void maximizeChart() {
        KpiChartWidget.create(driver, wait).maximizeChart();
    }

    @Step("I minimize chart")
    public void minimizeChart() {
        KpiChartWidget.create(driver, wait).minimizeChart();
    }

    private boolean ifDownloadDirExists() {
        File tmpDir = new File(CONFIGURATION.getDownloadDir());
        if (tmpDir.exists()) {
            log.info("Download directory was successfully created");
            return true;
        }
        return false;
    }

    @Step("I set TopN options with dimension: {dimension}")
    public void setTopNOptions(String dimension) {
        TopNPanel topNPanel = KpiToolbarPanel.create(driver, wait).getTopNPanel();
        topNPanel.openTopNPanel();
        topNPanel.setTopNDimension(dimension);
        topNPanel.clickPerform();
    }

    @Step("I should see {expectedColumnsCount} columns and {expectedLinesCount} lines displayed")
    public boolean shouldSeeBoxesAndCurvesDisplayed(int expectedColumnsCount, int expectedLinesCount) {
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        int columnsCount = kpiChartWidget.countColumns();
        int linesCount = kpiChartWidget.countLines();
        return columnsCount == expectedColumnsCount && linesCount == expectedLinesCount;
    }

    @Step("I should see {expectedLinesCount} lines displayed")
    public boolean shouldSeeCurvesDisplayed(int expectedLinesCount) {
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        int linesCount = kpiChartWidget.countLines();
        return linesCount == expectedLinesCount;
    }

    @Step("I should see {expectedPointsCount} highlighted points displayed")
    public boolean shouldSeePointsDisplayed(int expectedPointsCount) {
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        int pointsCount = (kpiChartWidget.countHighlightedPoints()) / 2;
        return pointsCount == expectedPointsCount;
    }

    @Step("I set TopN options with dimension: {dimension} and {nthLevel} level")
    public void setTopNOptions(String dimension, String nthLevel) {
        TopNPanel topNPanel = KpiToolbarPanel.create(driver, wait).getTopNPanel();
        topNPanel.openTopNPanel();
        topNPanel.setTopNDimension(dimension);
        topNPanel.setNthLevel(nthLevel);
        topNPanel.clickPerform();
    }

    // DO WYWALENIA Z FRAMEWORKA - PRZENIES NA PAGE - choc nie, bo xpath
    @Step("I click legend")
    public void clickLegend() {
        KpiChartWidget.create(driver, wait).clickDataSeriesLegend();
    }

    // do wywalenia - jak wejdzie id to jedna metoda zapieta na id buttona
    @Step("I click chart type - area")
    public void clickAreaChartType() {
        DelayUtils.waitForPageToLoad(driver, wait);
        KpiChartWidget.create(driver, wait).clickChartActions();
//        DelayUtils.waitForPageToLoad(driver, wait);
        Button chartTypeButton = Button.createById(driver, CHART_TYPE_BUTTON_ID);
//        DelayUtils.waitForPageToLoad(driver, wait);
        chartTypeButton.click();
        KpiChartWidget.create(driver, wait).clickAreaChartButton();
    }

    @Step("I click chart type - bar")
    public void clickBarChartType() {
//        KpiChartWidget.create(driver, wait).clickChartActions();
//        Button chartTypeButton = Button.createById(driver, CHART_TYPE_BUTTON_ID);
//        chartTypeButton.click();
        KpiChartWidget.create(driver, wait).clickBarChartButton();
    }

    @Step("I click chart type - line")
    public void clickLineChartType() {
//        KpiChartWidget.create(driver, wait).clickChartActions();
//        Button chartTypeButton = Button.createById(driver, CHART_TYPE_BUTTON_ID);
//        chartTypeButton.click();
        KpiChartWidget.create(driver, wait).clickLineChartButton();
    }

    @Step("I pick data series color")
    public void chooseDataSeriesColor() {
        KpiChartWidget.create(driver, wait).clickChartColorButton();
        KpiChartWidget.create(driver, wait).pickDataSeriesColorButton();
    }

    @Step("I should see {expectedLineWidth} width line displayed")
    public boolean shouldSeeDataSeriesLineWidth(String expectedLineWidth) {
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        String lineWidth = kpiChartWidget.dataSeriesLineWidth();
        return lineWidth.equals(expectedLineWidth);
    }

    @Step("I should not see any lines on chart displayed")
    public boolean shouldNotSeeHiddenLine(String expectedLineVisibility) {
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        String lineVisibility = kpiChartWidget.dataSeriesVisibility();
        return lineVisibility.equals(expectedLineVisibility);
    }

    @Step("I should see area (0.6) or line (0) chart displayed")
    public boolean shouldSeeAreaChart(String expectedFillOpacity) {
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        String fillOpacity = kpiChartWidget.dataSeriesFillOpacity();
        return fillOpacity.equals(expectedFillOpacity);
    }

    @Step("I should see bar chart displayed")
    public boolean shouldSeeBarChart(String expectedFillOpacity) {
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        String fillOpacity = kpiChartWidget.barDataSeriesFillOpacity();
        return fillOpacity.equals(expectedFillOpacity);
    }

    @Step("I should see bar chart displayed")
    public boolean shouldSeeColorChart(String expectedColor) {
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        String color = kpiChartWidget.dataSeriesColor();
        return color.equals(expectedColor);
    }

    @Step("Set value in time period chooser")
    public void setValueInTimePeriodChooser(int days, int hours, int minutes) {
        DelayUtils.waitForPageToLoad(driver, wait);
        OptionsPanel optionsPanel = OptionsPanel.create(driver, wait);
        optionsPanel.chooseTimePeriod();
        optionsPanel.setLastPeriodOption(days, hours, minutes);
    }

    @Step("Set SMART option in time period chooser")
    public void chooseSmartOptionInTimePeriodChooser() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OptionsPanel optionsPanel = OptionsPanel.create(driver, wait);
        optionsPanel.chooseTimePeriod();
        optionsPanel.chooseTimePeriodOption(OptionsPanel.TimePeriodChooserOption.SMART);
    }

    @Step("Set LATEST option in time period chooser")
    public void chooseLatestOptionInTimePeriodChooser() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OptionsPanel optionsPanel = OptionsPanel.create(driver, wait);
        optionsPanel.chooseTimePeriod();
        optionsPanel.chooseTimePeriodOption(OptionsPanel.TimePeriodChooserOption.LATEST);
    }

}
