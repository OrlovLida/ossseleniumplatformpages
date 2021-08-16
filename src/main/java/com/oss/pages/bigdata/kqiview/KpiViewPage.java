package com.oss.pages.bigdata.kqiview;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.PopupV2;
import com.oss.framework.mainheader.ButtonPanel;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.dpe.kpichartwidget.KpiChartWidget;
import com.oss.framework.widgets.dpe.toolbarpanel.*;
import com.oss.framework.widgets.dpe.toolbarpanel.ExportPanel.ExportType;
import com.oss.framework.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.oss.configuration.Configuration.CONFIGURATION;
import static com.oss.framework.widgets.dpe.toolbarpanel.OptionsPanel.AggregationMethodOption.SUM;
import static com.oss.framework.widgets.dpe.toolbarpanel.OptionsPanel.MiscellaneousOption.DATA_COMPLETENESS;
import static com.oss.framework.widgets.dpe.toolbarpanel.OptionsPanel.MiscellaneousOption.LAST_SAMPLE_TIME;
import static com.oss.framework.widgets.dpe.toolbarpanel.OptionsPanel.TimePeriodChooserOption.LATEST;
import static com.oss.framework.widgets.dpe.toolbarpanel.OptionsPanel.TimePeriodChooserOption.SMART;
import static com.oss.framework.widgets.dpe.toolbarpanel.OptionsPanel.YAxisOption.MANUAL;

public class KpiViewPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(KpiViewPage.class);

    private static final String INDICATORS_TREE_ID = "_Indicators";
    private static final String DIMENSIONS_TREE_ID = "_Dimensions";
    private static final String CHART_TYPE_BUTTON_ID = "chart-type-button";
    private static final String AREA_CHART_BUTTON_ID = "dropdown-list-button_area";
    private static final String BAR_CHART_BUTTON_ID = "dropdown-list-button_bar";
    private static final String LINE_CHART_BUTTON_ID = "dropdown-list-button_line";
    private static final String CHART_COLOR_BUTTON_ID = "chart-color-button";
    private static final String FULL_SCREEN_BUTTON_ID = "full-screen-button";
    private static final String SAVE_BOOKMAK_BUTTON_ID = "buttonIcon fa fa-floppy-o ";
    private static final String BOOKMARK_NAME_FIELD_ID = "viewName";

    private static final String OPTIONS_BUTTON_ID = "options-menu-button";

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

    public void kpiViewSetup(String indicatorNodesToExpand, String indicatorNodesToSelect, String dimensionNodesToExpand, String dimensionNodesToSelect, String filterName) {
        setFilters(Collections.singletonList(filterName));

        List<String> indicatorNodesToExpandList = Arrays.asList(indicatorNodesToExpand.split(","));
        List<String> indicatorNodesToSelectList = Arrays.asList(indicatorNodesToSelect.split(","));
        selectIndicator(indicatorNodesToExpandList, indicatorNodesToSelectList);

        List<String> dimensionNodesToExpandList = Arrays.asList(dimensionNodesToExpand.split(","));
        List<String> dimensionNodesToSelectList = Arrays.asList(dimensionNodesToSelect.split(","));
        selectDimension(dimensionNodesToExpandList, dimensionNodesToSelectList);

        applyChanges();
        seeChartIsDisplayed();
    }

    public void kpiViewSetup(String indicatorNodesToExpand, String indicatorNodesToSelect, String dimensionNodesToSelect, String filterName) {
        setFilters(Collections.singletonList(filterName));

        List<String> indicatorNodesToExpandList = Arrays.asList(indicatorNodesToExpand.split(","));
        List<String> indicatorNodesToSelectList = Arrays.asList(indicatorNodesToSelect.split(","));
        selectIndicator(indicatorNodesToExpandList, indicatorNodesToSelectList);

        List<String> dimensionNodesToSelectList = Arrays.asList(dimensionNodesToSelect.split(","));
        selectUnfoldedDimension(dimensionNodesToSelectList);

        applyChanges();
        seeChartIsDisplayed();
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
        int pointsCount = (kpiChartWidget.countVisiblePoints()) / 2;
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

    @Step("I click legend")
    public void clickLegend() {
        log.info("Clicking first element from legend");
        KpiChartWidget.create(driver, wait).clickDataSeriesLegend();
    }

    @Step("I click chart type - area")
    public void clickAreaChartType() {
        DelayUtils.waitForPageToLoad(driver, wait);
        KpiChartWidget.create(driver, wait).clickChartActions();
        Button.createById(driver, CHART_TYPE_BUTTON_ID).click();
        Button.createById(driver, AREA_CHART_BUTTON_ID).click();
        log.info("Changing chart type to area");
    }

    @Step("I click chart type - bar")
    public void clickBarChartType() {
        log.info("Changing chart type to bar chart");
        Button.createById(driver, BAR_CHART_BUTTON_ID).click();
    }

    @Step("I click chart type - line")
    public void clickLineChartType() {
        log.info("Changing chart type to line chart");
        Button.createById(driver, LINE_CHART_BUTTON_ID).click();
    }

    @Step("I pick data series color")
    public void chooseDataSeriesColor() {
        log.info("Changing first data series color");
        Button.createById(driver, CHART_COLOR_BUTTON_ID).click();
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
        log.info("Setting value for last option in time period chooser: {} days, {} hours, {} minutes", days, hours, minutes);
        DelayUtils.waitForPageToLoad(driver, wait);
        OptionsPanel optionsPanel = OptionsPanel.create(driver, wait);
        optionsPanel.chooseTimePeriod();
        optionsPanel.setLastPeriodOption(days, hours, minutes);
    }

    @Step("Set SMART option in time period chooser")
    public void chooseSmartOptionInTimePeriodChooser() {
        log.info("Setting smart option in time period chooser");
        DelayUtils.waitForPageToLoad(driver, wait);
        OptionsPanel optionsPanel = OptionsPanel.create(driver, wait);
        optionsPanel.chooseTimePeriod();
        optionsPanel.chooseTimePeriodOption(SMART);
    }

    @Step("Set LATEST option in time period chooser")
    public void chooseLatestOptionInTimePeriodChooser() {
        log.info("Setting latest option in time period chooser");
        DelayUtils.waitForPageToLoad(driver, wait);
        OptionsPanel optionsPanel = OptionsPanel.create(driver, wait);
        optionsPanel.chooseTimePeriod();
        optionsPanel.chooseTimePeriodOption(LATEST);
    }

    // do zmiany nazwy
    @Step("I should see 2 visible Y axis and 1 hidden Y axis")
    public boolean shouldSeeVisibleYaxis(int expectedVisibleYAxisNumber) {
        DelayUtils.waitForPageToLoad(driver, wait);
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        int visibleYaxisNumber = kpiChartWidget.countVisibleYAxis();
        return visibleYaxisNumber == expectedVisibleYAxisNumber;
    }

    @Step("Set SUM aggregation method")
    public void chooseSumAggregationMethod() {
        log.info("Setting sum option in aggregation method");
        DelayUtils.waitForPageToLoad(driver, wait);
        OptionsPanel optionsPanel = OptionsPanel.create(driver, wait);
        optionsPanel.chooseAggregationMethod();
        optionsPanel.chooseAggregationMethodOption(SUM);
    }

    @Step("Set Y axis manual option")
    public void chooseManualYaxis() {
        log.info("Setting manual Y axis");
        DelayUtils.waitForPageToLoad(driver, wait);
        OptionsPanel optionsPanel = OptionsPanel.create(driver, wait);
        optionsPanel.chooseYAxisOption();
        optionsPanel.setYAxisOption(MANUAL);
    }

    @Step("I enable Data Completeness option")
    public void enableDataCompleteness() {
        log.info("Enabling Data Completeness visibility");
        DelayUtils.waitForPageToLoad(driver, wait);
        OptionsPanel optionsPanel = OptionsPanel.create(driver, wait);
        optionsPanel.chooseMiscellaneousOption();
        optionsPanel.setMiscellaneousOption(DATA_COMPLETENESS);
    }

    @Step("I enable Last Sample Time option")
    public void enableLastSampleTime() {
        log.info("Enabling Last Sample Time visibility");
        DelayUtils.waitForPageToLoad(driver, wait);
        OptionsPanel optionsPanel = OptionsPanel.create(driver, wait);
        optionsPanel.chooseMiscellaneousOption();
        optionsPanel.setMiscellaneousOption(LAST_SAMPLE_TIME);
    }

    @Step("I should see last sample time below chart")
    public boolean shouldSeeLastSampleTime(int expectedVisibleLastSampleTimeNumber) {
        DelayUtils.waitForPageToLoad(driver, wait);
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        int visibleLastSampleTimeNumber = kpiChartWidget.countVisibleLastSampleTime();
        return visibleLastSampleTimeNumber == expectedVisibleLastSampleTimeNumber;
    }

    @Step("I should see data completeness displayed in the legend")
    public boolean shouldSeeDataCompleteness() {
        DelayUtils.waitForPageToLoad(driver, wait);
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        int visibleLastSampleTimeNumber = kpiChartWidget.countVisibleDataCompleteness();
        return visibleLastSampleTimeNumber > 0;
    }

    @Step("I enable Compare with Other Period option")
    public void enableCompareWithOtherPeriod() {
        log.info("Enabling Compare with Other Period option");
        DelayUtils.waitForPageToLoad(driver, wait);
        OptionsPanel optionsPanel = OptionsPanel.create(driver, wait);
        optionsPanel.setOtherPeriodOption();
    }

    @Step("I should see other period displayed in the legend")
    public boolean shouldSeeOtherPeriod() {
        DelayUtils.waitForPageToLoad(driver, wait);
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        int visibleOtherPeriodNumber = kpiChartWidget.countVisibleOtherPeriod();
        return visibleOtherPeriodNumber > 0;
    }

    @Step("I click Save bookmark")
    public void clickSaveBookmark() {
        ButtonPanel.create(driver, wait).clickOnIcon(SAVE_BOOKMAK_BUTTON_ID);
    }

    @Step("i fill Save Bookmark wizard")
    public void fillBookmarkWizard(String bookmarkName) {
        PopupV2 bookmarkWizard = PopupV2.create(driver, wait);
        bookmarkWizard.setComponentValue(BOOKMARK_NAME_FIELD_ID, bookmarkName, Input.ComponentType.TEXT_FIELD);
        log.info("I filled bookmark name with: {}", bookmarkName);
        bookmarkWizard.clickButtonByLabel("Save");
        log.info("I click Save");
    }

}
