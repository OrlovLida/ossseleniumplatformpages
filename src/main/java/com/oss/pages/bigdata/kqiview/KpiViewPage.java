package com.oss.pages.bigdata.kqiview;

import com.oss.framework.components.common.ListAttributesChooser;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.mainheader.ButtonPanel;
import com.oss.framework.mainheader.Share;
import com.oss.framework.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.view.Card;
import com.oss.framework.widgets.dpe.contextaction.ContextActionPanel;
import com.oss.framework.widgets.dpe.kpichartwidget.KpiChartWidget;
import com.oss.framework.widgets.dpe.toolbarpanel.ExportPanel.ExportType;
import com.oss.framework.widgets.dpe.toolbarpanel.FiltersPanel;
import com.oss.framework.widgets.dpe.toolbarpanel.KpiToolbarPanel;
import com.oss.framework.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType;
import com.oss.framework.widgets.dpe.toolbarpanel.OptionsPanel;
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
import static com.oss.framework.widgets.dpe.toolbarpanel.OptionsPanel.MiscellaneousOption.*;
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
    private static final String DATA_VIEW_ID = "_Data_View";
    private static final String SAVE_BOOKMARK_BUTTON_ID = "ButtonSaveBookmark";
    private static final String COLOR_PICKER_CLASS = "colorPickerWrapper";
    private static final String CHART_ACTIONS_LINKS_ID = "external-links-button";
    private static final String LINK_TO_XDR_LABEL = "Open xDR for t:SMOKE#ETLforKqis. Time condition limited to last 1 hour(s) from chosen period.";
    private static final String LINK_TO_INDICATORS_VIEW_CHART_LABEL = "Indicators View - Chart";
    private static final String DIMENSION_OPTIONS_BUTTON_ID = "dimension-options-button";
    private static final String CHILD_OBJECT_LEVEL_INPUT_ID = "SelectChildMOLevelChanged";
    private static final String IND_VIEW_TABLE_ID = "ind-view-table";
    private static final String TOP_N_BARCHART_DFE_ID = "amchart-series-DFE_y-selected";
    private static final String TOP_N_BARCHART_DPE_ID = "amchart-series-DPE_y-selected";

    private final KpiToolbarPanel kpiToolbarPanel;

    public KpiViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        kpiToolbarPanel = KpiToolbarPanel.create(driver, wait);
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
        KpiTreeWidget.create(driver, wait, componentId).selectExpandedObjects(nodesToSelect);
        log.debug("Selecting: {}", nodesToSelect);
    }

    @Step("I set filters: {enabledFilters}")
    public void setFilters(List<String> enabledFilters) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getFiltersPanel().clearFilters();
        getFiltersPanel().turnOnFilters(enabledFilters);
        getFiltersPanel().clickConfirm();

        log.info("Selected filters: {}", enabledFilters);
    }

    private FiltersPanel getFiltersPanel() {
        return kpiToolbarPanel.openFilterPanel();
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
        kpiToolbarPanel.clickApply();
        DelayUtils.waitForPageToLoad(driver, wait);
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
        kpiToolbarPanel.openExportPanel().exportKpiToFile(ExportType.JPG);
        log.info("Exporting chart to JPG");
        kpiToolbarPanel.openExportPanel().exportKpiToFile(ExportType.PNG);
        log.info("Exporting chart to PNG");
        kpiToolbarPanel.openExportPanel().exportKpiToFile(ExportType.PDF);
        log.info("Exporting chart to PDF");
        kpiToolbarPanel.openExportPanel().exportKpiToFile(ExportType.XLSX);
        log.info("Exporting chart to XLSX");
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
    public void changeLayout(LayoutType layoutType) {
        kpiToolbarPanel.openLayoutPanel().changeLayout(layoutType);
    }

    @Step("I minimize data View")
    public void minimizeDataView() {
        Card.createCard(driver, wait, DATA_VIEW_ID).minimizeCard();
    }

    @Step("I maximize data View")
    public void maximizeDataView() {
        Card.createCard(driver, wait, DATA_VIEW_ID).maximizeCard();
    }

    @Step("I minimize Indicators Panel")
    public void minimizeIndicatorsPanel() {
        Card.createCard(driver, wait, INDICATORS_TREE_ID).minimizeCard();
    }

    @Step("I maximize Indicators Panel")
    public void maximizeIndicatorsPanel() {
        Card.createCard(driver, wait, INDICATORS_TREE_ID).maximizeCard();
    }

    @Step("I minimize Dimensions Panel")
    public void minimizeDimensionsPanel() {
        Card.createCard(driver, wait, DIMENSIONS_TREE_ID).minimizeCard();
    }

    @Step("I maximize Dimensions Panel")
    public void maximizeDimensionsPanel() {
        Card.createCard(driver, wait, DIMENSIONS_TREE_ID).maximizeCard();
    }

    private boolean ifDownloadDirExists() {
        File tmpDir = new File(CONFIGURATION.getDownloadDir());
        if (tmpDir.exists()) {
            log.info("Download directory was successfully created");
            return true;
        }
        return false;
    }

    @Step("I should see {expectedColumnsCount} columns and {expectedLinesCount} lines displayed")
    public boolean shouldSeeBoxesAndCurvesDisplayed(int expectedColumnsCount, int expectedLinesCount) {
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        return kpiChartWidget.countColumns() == expectedColumnsCount && kpiChartWidget.countLines() == expectedLinesCount;
    }

    @Step("I should see {expectedLinesCount} lines displayed")
    public boolean shouldSeeCurvesDisplayed(int expectedLinesCount) {
        return KpiChartWidget.create(driver, wait).countLines() == expectedLinesCount;
    }

    @Step("I should see {expectedPieCharts} Pie Charts displayed")
    public boolean shouldSeePieChartsDisplayed(int expectedPieCharts) {
        return KpiChartWidget.create(driver, wait).countPieCharts() == expectedPieCharts;
    }

    @Step("I check if Indicators View Table is empty")
    public boolean isIndicatorsViewTableEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).hasNoData();
    }

    @Step("I should see more than one line displayed")
    public boolean shouldSeeMoreThanOneCurveDisplayed() {
        return KpiChartWidget.create(driver, wait).countLines() > 1;
    }

    @Step("I should see {expectedPointsCount} highlighted points displayed")
    public boolean shouldSeePointsDisplayed(int expectedPointsCount) {
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        int pointsCount = (kpiChartWidget.countVisiblePoints()) / 2;
        return pointsCount == expectedPointsCount;
    }

    @Step("I set topN dimension in TopN panel")
    public void setTopNDimension(String dimensionId) {
        kpiToolbarPanel.openTopNPanel().setDimension(dimensionId);
    }

    @Step("I set topN level in TopN panel")
    public void setTopNLevel(String levelId) {
        kpiToolbarPanel.openTopNPanel().setLevel(levelId);
    }

    @Step("I click Perform in TopN panel")
    public void clickPerformTopN() {
        kpiToolbarPanel.openTopNPanel().clickPerform();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I double click on bar in TopN BarChart")
    public void doubleClickTopNDPE() {
        KpiChartWidget.create(driver, wait).doubleClickOnTopNBar(TOP_N_BARCHART_DPE_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I check if TopN navigation bar is visible")
    public boolean isTopNNavigationBarVisible() {
        log.info("Checking visibility of TopN navigation bar");
        return KpiChartWidget.create(driver, wait).isTopNNavigationBarVisible();
    }

    @Step("I click legend")
    public void clickLegend() {
        log.info("Clicking first element from legend");
        KpiChartWidget.create(driver, wait).clickDataSeriesLegend();
    }

    @Step("I go to Chart Actions Panel")
    public ContextActionPanel getChartActionsPanel() {
        return ContextActionPanel.create(driver, wait);
    }

    @Step("I click chart type - area")
    public void clickAreaChartType() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getChartActionsPanel().callAction(CHART_TYPE_BUTTON_ID, AREA_CHART_BUTTON_ID);
        log.info("Changing chart type to area");
    }

    @Step("I click chart type - bar")
    public void clickBarChartType() {
        log.info("Changing chart type to bar chart");
        getChartActionsPanel().callAction(CHART_TYPE_BUTTON_ID, BAR_CHART_BUTTON_ID);
    }

    @Step("I click chart type - line")
    public void clickLineChartType() {
        log.info("Changing chart type to line chart");
        getChartActionsPanel().callAction(CHART_TYPE_BUTTON_ID, LINE_CHART_BUTTON_ID);
    }

    @Step("I pick data series color")
    public void chooseDataSeriesColor() {
        log.info("Changing first data series color");
        getChartActionsPanel().callAction(CHART_COLOR_BUTTON_ID, COLOR_PICKER_CLASS, "rgb(150, 65, 54)");
    }

    @Step("I click link to XDR Browser")
    public void clickLinkToXDRBrowser() {
        getChartActionsPanel().callAction(CHART_ACTIONS_LINKS_ID, LINK_TO_XDR_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking on link to XDR Browser");
    }

    @Step("I click link to chart")
    public void clickLinkToChart() {
        getChartActionsPanel().callAction(CHART_ACTIONS_LINKS_ID, LINK_TO_INDICATORS_VIEW_CHART_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking on link to Indicators View - Chart");
    }

    @Step("I should see {expectedLineWidth} width line displayed")
    public boolean shouldSeeDataSeriesLineWidth(String expectedLineWidth) {
        String lineWidth = KpiChartWidget.create(driver, wait).dataSeriesLineWidth();
        return lineWidth.equals(expectedLineWidth);
    }

    @Step("I should not see any lines on chart displayed")
    public boolean shouldNotSeeHiddenLine(String expectedLineVisibility) {
        String lineVisibility = KpiChartWidget.create(driver, wait).dataSeriesVisibility();
        return lineVisibility.equals(expectedLineVisibility);
    }

    @Step("I should see area (0.6) or line (0) chart displayed")
    public boolean shouldSeeAreaChart(String expectedFillOpacity) {
        String fillOpacity = KpiChartWidget.create(driver, wait).dataSeriesFillOpacity();
        return fillOpacity.equals(expectedFillOpacity);
    }

    @Step("I should see bar chart displayed")
    public boolean shouldSeeBarChart(String expectedFillOpacity) {
        String fillOpacity = KpiChartWidget.create(driver, wait).barDataSeriesFillOpacity();
        return fillOpacity.equals(expectedFillOpacity);
    }

    @Step("I should see topN bar chart displayed with DFE data")
    public boolean dfeTopNBarChartIsDisplayed() {
        return KpiChartWidget.create(driver, wait).topNBarChartIsDisplayed(TOP_N_BARCHART_DFE_ID);
    }

    @Step("I should see topN bar chart displayed with DPE data")
    public boolean dpeTopNBarChartIsDisplayed() {
        return KpiChartWidget.create(driver, wait).topNBarChartIsDisplayed(TOP_N_BARCHART_DPE_ID);
    }

    @Step("I should see bar chart displayed")
    public boolean shouldSeeColorChart(String expectedColor) {
        String color = KpiChartWidget.create(driver, wait).dataSeriesColor();
        return color.equals(expectedColor);
    }

    @Step("Set value in time period chooser")
    public void setValueInTimePeriodChooser(int days, int hours, int minutes) {
        log.info("Setting value for last option in time period chooser: {} days, {} hours, {} minutes", days, hours, minutes);
        DelayUtils.waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().setLastPeriodOption(days, hours, minutes);
    }

    @Step("Set SMART option in time period chooser")
    public void chooseSmartOptionInTimePeriodChooser() {
        log.info("Setting smart option in time period chooser");
        DelayUtils.waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().chooseTimePeriodOption(SMART);
    }

    @Step("Set LATEST option in time period chooser")
    public void chooseLatestOptionInTimePeriodChooser() {
        log.info("Setting latest option in time period chooser");
        DelayUtils.waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().chooseTimePeriodOption(LATEST);
    }

    @Step("I should see 2 visible Y axis and 1 hidden Y axis")
    public boolean shouldSeeVisibleYaxis(int expectedVisibleYAxisNumber) {
        DelayUtils.waitForPageToLoad(driver, wait);
        int visibleYaxisNumber = KpiChartWidget.create(driver, wait).countVisibleYAxis();
        return visibleYaxisNumber == expectedVisibleYAxisNumber;
    }

    @Step("I select Aggregation Method")
    public void selectAggregationMethod(OptionsPanel.AggregationMethodOption option) {
        log.info("Setting: {} option in aggregation method", option);
        DelayUtils.waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().chooseAggregationMethodOption(option);
    }

    public void unselectEveryAggMethodOtherThan(OptionsPanel.AggregationMethodOption option) {
        List<OptionsPanel.AggregationMethodOption> toUnselect = kpiToolbarPanel.openOptionsPanel().getActiveAggregationMethods();
        toUnselect.remove(option);
        for (OptionsPanel.AggregationMethodOption aggOption : toUnselect) {
            kpiToolbarPanel.openOptionsPanel().chooseAggregationMethodOption(aggOption);
        }
    }

    @Step("Set Y axis manual option")
    public void chooseManualYaxis() {
        log.info("Setting manual Y axis");
        DelayUtils.waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().setYAxisOption(MANUAL);
    }

    @Step("I enable Data Completeness option")
    public void enableDataCompleteness() {
        log.info("Enabling Data Completeness visibility");
        DelayUtils.waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().setMiscellaneousOption(DATA_COMPLETENESS);
    }

    @Step("I enable Last Sample Time option")
    public void enableLastSampleTime() {
        log.info("Enabling Last Sample Time visibility");
        DelayUtils.waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().setMiscellaneousOption(LAST_SAMPLE_TIME);
    }

    @Step("I should see last sample time below chart")
    public boolean shouldSeeLastSampleTime(int expectedVisibleLastSampleTimeNumber) {
        DelayUtils.waitForPageToLoad(driver, wait);
        int visibleLastSampleTimeNumber = KpiChartWidget.create(driver, wait).countVisibleLastSampleTime();
        return visibleLastSampleTimeNumber == expectedVisibleLastSampleTimeNumber;
    }

    @Step("I enable Show Time Zone option")
    public void enableShowTimeZone() {
        log.info("Enabling Show Time Zone");
        DelayUtils.waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().setMiscellaneousOption(SHOW_TIME_ZONE);
    }

    @Step("Checking visibility of Time Zone option")
    public boolean isTimeZoneDisplayed() {
        log.info("Checking visibility of Time Zone option");
        DelayUtils.waitForPageToLoad(driver, wait);
        return KpiChartWidget.create(driver, wait).isTimeZoneDisplayed();
    }

    @Step("I should see data completeness displayed in the legend")
    public boolean shouldSeeDataCompleteness() {
        DelayUtils.waitForPageToLoad(driver, wait);
        int visibleLastSampleTimeNumber = KpiChartWidget.create(driver, wait).countVisibleDataCompleteness();
        return visibleLastSampleTimeNumber > 0;
    }

    @Step("I enable Compare with Other Period option")
    public void enableCompareWithOtherPeriod() {
        log.info("Enabling Compare with Other Period option");
        DelayUtils.waitForPageToLoad(driver, wait);
        kpiToolbarPanel.openOptionsPanel().setOtherPeriodOption();
    }

    @Step("I should see other period displayed in the legend")
    public boolean shouldSeeOtherPeriod() {
        DelayUtils.waitForPageToLoad(driver, wait);
        int visibleOtherPeriodNumber = KpiChartWidget.create(driver, wait).countVisibleOtherPeriod();
        return visibleOtherPeriodNumber > 0;
    }

    @Step("I should see only Data View Panel displayed")
    public boolean shouldSeeOnlyDataViewDisplayed() {
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        boolean dataViewDisplayed = kpiChartWidget.dataViewPanelVisibility();
        boolean indicatorsTreeDisplayed = kpiChartWidget.indicatorsTreeVisibility();
        boolean dimensionsTreeDisplayed = kpiChartWidget.dimensionsTreeVisibility();
        if (dataViewDisplayed & !indicatorsTreeDisplayed & !dimensionsTreeDisplayed) {
            log.info("Only Data View Panel is displayed");
            return true;
        } else {
            log.error("Other Panels are also visible");
            return false;
        }
    }

    @Step("I check status of chosen layout button")
    public String layoutButtonStatus(LayoutType layout) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return kpiToolbarPanel.openLayoutPanel().chartLayoutButtonStatus(layout);
    }

    @Step("I search for Object in tree search toolbar")
    public void searchInToolbarPanel(String objectName, String treeId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        KpiTreeWidget kpiTreeWidget = KpiTreeWidget.create(driver, wait, treeId);
        kpiTreeWidget.searchInToolbarPanel(objectName);
        DelayUtils.waitForPageToLoad(driver, wait);
        kpiTreeWidget.selectFirstSearchResult();
        kpiTreeWidget.closeSearchToolbar();
    }

    @Step("I setup Indicators View")
    public void kpiViewSetup(String indicatorNodesToExpand, String indicatorNodesToSelect,
                             String dimensionNodesToExpand, String dimensionNodesToSelect, String filterName) {
        setFilters(Collections.singletonList(filterName));

        List<String> indicatorNodesToExpandList = Arrays.asList(indicatorNodesToExpand.split(","));
        List<String> indicatorNodesToSelectList = Arrays.asList(indicatorNodesToSelect.split(","));
        selectIndicator(indicatorNodesToExpandList, indicatorNodesToSelectList);
        List<String> dimensionNodesToSelectList = Arrays.asList(dimensionNodesToSelect.split(","));

        if (dimensionNodesToExpand != null) {
            List<String> dimensionNodesToExpandList = Arrays.asList(dimensionNodesToExpand.split(","));
            selectDimension(dimensionNodesToExpandList, dimensionNodesToSelectList);
        } else {
            selectUnfoldedDimension(dimensionNodesToSelectList);
        }

        selectAggregationMethod(OptionsPanel.AggregationMethodOption.SUM);
        unselectEveryAggMethodOtherThan(OptionsPanel.AggregationMethodOption.SUM);
        closeOptionsPanel();

        applyChanges();
        seeChartIsDisplayed();
    }

    @Step("I click Save bookmark")
    public void clickSaveBookmark() {
        ButtonPanel.create(driver, wait).clickButton(SAVE_BOOKMARK_BUTTON_ID);
    }

    @Step("Check if node is selected in the tree")
    public boolean isNodeInTreeSelected(String objectName, String treeId) {
        log.info("Checking if node: {} is selected in the tree", objectName);
        return KpiTreeWidget.create(driver, wait, treeId).isNodeSelected(objectName);
    }

    public String activeAggMethod() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return kpiToolbarPanel.openOptionsPanel().getActiveAggregationMethods().get(0).toString();
    }

    public int numberOfActiveAggMethods() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return kpiToolbarPanel.openOptionsPanel().getActiveAggregationMethods().size();
    }

    @Step("I close Options Panel")
    public void closeOptionsPanel() {
        kpiToolbarPanel.closeOptionsPanel();
    }

    @Step("I click Share View icon")
    public void clickShare() {
        ToolbarWidget.create(driver, wait).openSharePanel();
        log.info("Click in Share icon");
    }

    @Step("I click close panel")
    public void clickCloseShare() {
        ToolbarWidget.create(driver, wait).closeSharePanel();
        log.info("Closing Share panel");
    }

    @Step("Copy link from Share panel")
    public String copyLink() {
        log.info("Copy link from Share panel");
        return Share.create(driver, wait).copyLink();
    }

    @Step("Go to link copied from Share panel")
    public void goToLink() {
        driver.get(copyLink());
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Redirecting to page from link in Share panel");
    }

    @Step("I fill field: Display series for child objects from level")
    public void fillLevelOfChildObjects(String level) {
        ComponentFactory.create(CHILD_OBJECT_LEVEL_INPUT_ID, Input.ComponentType.TEXT_FIELD, driver, wait)
                .setSingleStringValue(level);
    }

    @Step("I click on dimension node options button")
    public void clickDimensionOptions(String dimensionNodeName) {
        if (dimensionNodeName != null) {
            KpiTreeWidget.create(driver, wait, DIMENSIONS_TREE_ID).clickNodeOptions(dimensionNodeName);
            log.info("I Click on options button in: {}", dimensionNodeName);
        } else {
            Button.createById(driver, DIMENSION_OPTIONS_BUTTON_ID).click();
            log.info("I click on option button in first dimension node");
        }
    }

    @Step("I select display type from toolbar panel")
    public void setDisplayType(String displayTypeId) {
        kpiToolbarPanel.selectDisplayType(displayTypeId);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Setting display type to: {}", displayTypeId);
    }

    @Step("I check if expected number of charts is visible")
    public boolean isExpectedNumberOfChartsVisible(int expectedNumberOfCharts) {
        return KpiChartWidget.create(driver, wait).countCharts() == expectedNumberOfCharts;
    }

    @Step("I zoom the data view")
    public void zoomChart() {
        KpiChartWidget.create(driver, wait).zoomDataView();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I check if Zoom Out button is visible")
    public boolean isZoomOutButtonVisible() {
        return KpiChartWidget.create(driver, wait).isZoomOutButtonVisible();
    }

    @Step("I click Zoom Out Button")
    public void clickZoomOutButton() {
        KpiChartWidget.create(driver, wait).clickZoomOutButton();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I enable column {columnId} into table")
    public void enableColumnInTheTable(String columnId) {
        ListAttributesChooser listAttributesChooser = TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).getListAttributesChooser();
        listAttributesChooser.enableAttributeById(columnId);
        listAttributesChooser.clickApply();
        log.info("Enabling column with id: {}", columnId);
    }

    @Step("I disable column {columnId} into table")
    public void disableColumnInTheTable(String columnId) {
        ListAttributesChooser listAttributesChooser = TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).getListAttributesChooser();
        listAttributesChooser.disableAttributeById(columnId);
        listAttributesChooser.clickApply();
        log.info("Disabling column with id: {}", columnId);
    }

    @Step("I check if column with Header {columnHeader} is present in the Table")
    public boolean isColumnInTable(String columnHeader) {
        return TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).getColumnHeaders().contains(columnHeader);
    }

    @Step("I change columns order to: first column - {columnId}")
    public void dragColumnToTarget(String columnId, String targetColumnId) {
        ListAttributesChooser listAttributesChooser = TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).getListAttributesChooser();
        listAttributesChooser.enableAttributeById(columnId);
        listAttributesChooser.dragColumnToTarget(columnId, targetColumnId);
        listAttributesChooser.clickApply();
        log.info("Changing columns by dragging in table options menu. First column is column with id: {}", columnId);
    }

    @Step("I check if column with Header {columnHeader} is first column in the Table")
    public boolean isColumnFirstInTable(String columnHeader) {
        return TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).getColumnHeaders().stream()
                .findFirst()
                .orElse("")
                .equals(columnHeader);
    }

    @Step("I drag column in Table")
    public void changeColumnsOrderInTable(String columnToDragId, int position) {
        TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).changeColumnsOrderById(columnToDragId, position);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Dragging column with id: {} to position: {}", columnToDragId, position);
    }

    public void sortColumnASC(String columnId) {
        TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).sortColumnByASC(columnId);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Sorting column with id: {} in ASC order", columnId);
    }

    public void sortColumnDESC(String columnId) {
        TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).sortColumnByDESC(columnId);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Sorting column with id: {} in DESC order", columnId);
    }

    public boolean isValueInGivenRow(String value, int row, String columnId) {
        return TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).getCellValue(row, columnId).equals(value);
    }
}