package com.oss.pages.bigdata.kqiview;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.mainheader.ButtonPanel;
import com.oss.framework.mainheader.Share;
import com.oss.framework.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.view.Card;
import com.oss.framework.widgets.dpe.contextaction.ContextActionPanel;
import com.oss.framework.widgets.dpe.kpichartwidget.KpiChartWidget;
import com.oss.framework.widgets.dpe.toolbarpanel.*;
import com.oss.framework.widgets.dpe.toolbarpanel.ExportPanel.ExportType;
import com.oss.framework.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType;
import com.oss.framework.widgets.dpe.treewidget.KpiTreeWidget;
import com.oss.framework.widgets.tablewidget.TableWidget;
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
    private static final String DATA_VIEW_ID = "_Data_View";
    private static final String SAVE_BOOKMARK_BUTTON_ID = "fa fa-floppy-o";
    private static final String COLOR_PICKER_CLASS = "colorPickerWrapper";
    private static final String CHART_ACTIONS_LINKS_ID = "external-links-button";
    private static final String LINK_TO_XDR_LABEL = "Open xDR for t:SMOKE#ETLforKqis. Time condition limited to last 1 hour(s) from chosen period.";
    private static final String LINK_TO_INDICATORS_VIEW_CHART_LABEL = "Indicators View - Chart";
    private static final String DIMENSION_OPTIONS_BUTTON_ID = "dimension-options-button";
    private static final String CHILD_OBJECT_LEVEL_INPUT_ID = "SelectChildMOLevelChanged";
    private static final String IND_VIEW_TABLE_ID = "ind-view-table";
    private static final String TOP_N_BARCHART_DFE_ID = "amchart-series-DFE_y-selected";
    private static final String TOP_N_BARCHART_DPE_ID = "amchart-series-DPE_y-selected";


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
    public void changeLayout(LayoutType layoutType) {
        KpiToolbarPanel toolbar = KpiToolbarPanel.create(driver, wait);
        toolbar.getLayoutPanel().changeLayout(layoutType);
    }

    @Step("I minimize data View")
    public void minimizeDataView() {
        Card card = Card.createCard(driver, wait, DATA_VIEW_ID);
        card.minimizeCard();
    }

    @Step("I maximize data View")
    public void maximizeDataView() {
        Card card = Card.createCard(driver, wait, DATA_VIEW_ID);
        card.maximizeCard();
    }

    @Step("I minimize Indicators Panel")
    public void minimizeIndicatorsPanel() {
        Card card = Card.createCard(driver, wait, INDICATORS_TREE_ID);
        card.minimizeCard();
    }

    @Step("I maximize Indicators Panel")
    public void maximizeIndicatorsPanel() {
        Card card = Card.createCard(driver, wait, INDICATORS_TREE_ID);
        card.maximizeCard();
    }

    @Step("I minimize Dimensions Panel")
    public void minimizeDimensionsPanel() {
        Card card = Card.createCard(driver, wait, DIMENSIONS_TREE_ID);
        card.minimizeCard();
    }

    @Step("I maximize Dimensions Panel")
    public void maximizeDimensionsPanel() {
        Card card = Card.createCard(driver, wait, DIMENSIONS_TREE_ID);
        card.maximizeCard();
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
        return TableWidget.createById(driver, IND_VIEW_TABLE_ID, wait).hasNoData();
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
       getTopNPanel().setDimension(dimensionId);
    }

    @Step("I set topN level in TopN panel")
    public void setTopNLevel(String levelId) {
        getTopNPanel().setLevel(levelId);
    }

    @Step("I click Perform in TopN panel")
    public void clickPerformTopN() {
        getTopNPanel().clickPerform();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private TopNPanel getTopNPanel() {
        return KpiToolbarPanel.create(driver, wait).openTopNPanel();
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
        return LayoutPanel.create(driver, wait).chartLayoutButtonStatus(layout);
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

        applyChanges();
        seeChartIsDisplayed();
    }

    @Step("I click Save bookmark")
    public void clickSaveBookmark() {
        ButtonPanel.create(driver, wait).clickOnIcon(SAVE_BOOKMARK_BUTTON_ID);
    }

    @Step("Check if node is selected in the tree")
    public boolean isNodeInTreeSelected(String objectName, String treeId) {
        log.info("Checking if node: {} is selected in the tree", objectName);
        return KpiTreeWidget.create(driver, wait, treeId).isNodeSelected(objectName);
    }

    public String activeAggMethod() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OptionsPanel optionsPanel = OptionsPanel.create(driver, wait);
        String activeAggMethod = optionsPanel.getActiveAggregationMethod();

        return activeAggMethod;
    }

    @Step("I click Share View icon")
    public void clickShare() {
        ToolbarWidget.create(driver, wait).openSharePanel();
        log.info("Click in Share icon");
    }

    //TODO add this function after fix in OSSWEB-14686
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
        KpiToolbarPanel.create(driver, wait).selectDisplayType(displayTypeId);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Setting display type to: {}", displayTypeId);
    }

    @Step("I check if expected number of charts is visible")
    public boolean isExpectedNumberOfChartsVisible(int expectedNumberOfCharts) {
        return KpiChartWidget.create(driver, wait).countCharts() == expectedNumberOfCharts;
    }
}