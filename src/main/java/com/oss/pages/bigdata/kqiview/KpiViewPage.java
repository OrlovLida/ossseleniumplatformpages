package com.oss.pages.bigdata.kqiview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.attributechooser.ListAttributesChooser;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.layout.Card;
import com.oss.framework.components.mainheader.ButtonPanel;
import com.oss.framework.components.mainheader.Share;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.iaa.widgets.dpe.kpichartwidget.KpiChartWidget;
import com.oss.pages.BasePage;
import com.oss.pages.administration.managerwizards.BookmarkWizardPage;
import com.oss.untils.FileDownload;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class KpiViewPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(KpiViewPage.class);

    private static final String INDICATORS_TREE_ID = "_Indicators";
    private static final String DIMENSIONS_TREE_ID = "_Dimensions";
    private static final String DATA_VIEW_ID = "_Data_View";
    private static final String SAVE_BOOKMARK_BUTTON_ID = "ButtonSaveBookmark";
    private static final String CHILD_OBJECT_LEVEL_INPUT_ID = "SelectChildMOLevelChanged";
    private static final String IND_VIEW_TABLE_ID = "ind-view-table";
    private static final String TOP_N_BARCHART_DFE_ID = "amchart-series-DFE_y-selected";
    private static final String TOP_N_BARCHART_DPE_ID = "amchart-series-DPE_y-selected";
    private static final String INDICATORS_VIEW_URL = "Assurance/KPIView";
    private static final String STANDALONE_INDICATORS_VIEW_URL = "indicators-view/indicators-view";

    public KpiViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open KPI View")
    public static KpiViewPage goToPage(WebDriver driver, String basicURL, KpiViewType kpiViewType) {
        WebDriverWait wait = new WebDriverWait(driver, 90);

        String pageUrl = String.format("%s/#/view/%s", basicURL, chooseKpiView(kpiViewType));
        driver.get(pageUrl);
        waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new KpiViewPage(driver, wait);
    }

    public enum KpiViewType {
        INDICATORS_VIEW, STANDALONE_INDICATORS_VIEW
    }

    private static String chooseKpiView(KpiViewType kpiType) {
        if (kpiType == null) {
            return INDICATORS_VIEW_URL;
        } else if (kpiType == KpiViewType.STANDALONE_INDICATORS_VIEW) {
            return STANDALONE_INDICATORS_VIEW_URL;
        }
        return INDICATORS_VIEW_URL;
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

    @Step("Attach exported chart to report")
    public void attachExportedChartToReport() {
        FileDownload.attachDownloadedFileToReport("kpi_view_export_*.*");
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
        waitForPageToLoad(driver, wait);
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

    @Step("I double click on bar in TopN BarChart")
    public void doubleClickTopNDPE() {
        KpiChartWidget.create(driver, wait).doubleClickTopNBar(TOP_N_BARCHART_DPE_ID);
        waitForPageToLoad(driver, wait);
    }

    @Step("I check if TopN navigation bar is visible")
    public boolean isTopNNavigationBarVisible() {
        log.info("Checking visibility of TopN navigation bar");
        return KpiChartWidget.create(driver, wait).isTopNNavigationBarPresent();
    }

    @Step("I click legend")
    public void clickLegend() {
        log.info("Clicking first element from legend");
        KpiChartWidget.create(driver, wait).clickDataSeriesLegend();
    }

    @Step("I should see {expectedLineWidth} width line displayed")
    public boolean shouldSeeDataSeriesLineWidth(String expectedLineWidth) {
        String lineWidth = KpiChartWidget.create(driver, wait).getDataSeriesLineWidth();
        return lineWidth.equals(expectedLineWidth);
    }

    @Step("I should not see any lines on chart displayed")
    public boolean shouldNotSeeHiddenLine(String expectedLineVisibility) {
        String lineVisibility = KpiChartWidget.create(driver, wait).getDataSeriesVisibility();
        return lineVisibility.equals(expectedLineVisibility);
    }

    @Step("Check if data-series-type is {dataSeriesType}")
    public boolean isDataSeriesType(String dataSeriesType) {
        return KpiChartWidget.create(driver, wait).getDataSeriesType().equals(dataSeriesType);
    }

    @Step("I should see topN bar chart displayed with DFE data")
    public boolean dfeTopNBarChartIsDisplayed() {
        return KpiChartWidget.create(driver, wait).isTopNBarChartIsPresent(TOP_N_BARCHART_DFE_ID);
    }

    @Step("I should see topN bar chart displayed with DPE data")
    public boolean dpeTopNBarChartIsDisplayed() {
        return KpiChartWidget.create(driver, wait).isTopNBarChartIsPresent(TOP_N_BARCHART_DPE_ID);
    }

    @Step("I should see bar chart displayed")
    public boolean shouldSeeColorChart(String expectedColor) {
        String color = KpiChartWidget.create(driver, wait).getDataSeriesColor();
        return color.equals(expectedColor);
    }

    @Step("I should see 2 visible Y axis and 1 hidden Y axis")
    public boolean shouldSeeVisibleYaxis(int expectedVisibleYAxisNumber) {
        waitForPageToLoad(driver, wait);
        int visibleYaxisNumber = KpiChartWidget.create(driver, wait).countVisibleYAxis();
        return visibleYaxisNumber == expectedVisibleYAxisNumber;
    }

    @Step("I should see last sample time below chart")
    public boolean shouldSeeLastSampleTime(int expectedVisibleLastSampleTimeNumber) {
        waitForPageToLoad(driver, wait);
        int visibleLastSampleTimeNumber = KpiChartWidget.create(driver, wait).countVisibleLastSampleTime();
        return visibleLastSampleTimeNumber == expectedVisibleLastSampleTimeNumber;
    }

    @Step("Checking visibility of Time Zone option")
    public boolean isTimeZoneDisplayed() {
        log.info("Checking visibility of Time Zone option");
        waitForPageToLoad(driver, wait);
        return KpiChartWidget.create(driver, wait).isTimeZonePresent();
    }

    @Step("I should see data completeness displayed in the legend")
    public boolean shouldSeeDataCompleteness() {
        waitForPageToLoad(driver, wait);
        int visibleLastSampleTimeNumber = KpiChartWidget.create(driver, wait).countVisibleDataCompleteness();
        return visibleLastSampleTimeNumber > 0;
    }

    @Step("I should see other period displayed in the legend")
    public boolean shouldSeeOtherPeriod() {
        waitForPageToLoad(driver, wait);
        int visibleOtherPeriodNumber = KpiChartWidget.create(driver, wait).countVisibleOtherPeriod();
        return visibleOtherPeriodNumber > 0;
    }

    @Step("I should see only Data View Panel displayed")
    public boolean shouldSeeOnlyDataViewDisplayed() {
        KpiChartWidget kpiChartWidget = KpiChartWidget.create(driver, wait);
        boolean dataViewDisplayed = kpiChartWidget.isDataViewPanelPresent();
        boolean indicatorsTreeDisplayed = kpiChartWidget.isIndicatorsTreePresent();
        boolean dimensionsTreeDisplayed = kpiChartWidget.isDimensionsTreePresent();
        if (dataViewDisplayed && !indicatorsTreeDisplayed && !dimensionsTreeDisplayed) {
            log.info("Only Data View Panel is displayed");
            return true;
        } else {
            log.error("Other Panels are also visible");
            return false;
        }
    }

    @Step("Click Save bookmark")
    public BookmarkWizardPage clickSaveBookmark() {
        ButtonPanel.create(driver, wait).clickButton(SAVE_BOOKMARK_BUTTON_ID);
        return new BookmarkWizardPage(driver, wait);
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
        waitForPageToLoad(driver, wait);
        log.info("Redirecting to page from link in Share panel");
    }

    @Step("I fill field: Display series for child objects from level")
    public void fillLevelOfChildObjects(String level) {
        ComponentFactory.create(CHILD_OBJECT_LEVEL_INPUT_ID, Input.ComponentType.TEXT_FIELD, driver, wait)
                .setSingleStringValue(level);
    }

    @Step("I check if expected number of charts is visible")
    public boolean isExpectedNumberOfChartsVisible(int expectedNumberOfCharts) {
        return KpiChartWidget.create(driver, wait).countCharts() == expectedNumberOfCharts;
    }

    @Step("I zoom the data view")
    public void zoomChart() {
        KpiChartWidget.create(driver, wait).zoomDataView();
        waitForPageToLoad(driver, wait);
    }

    @Step("I check if Zoom Out button is visible")
    public boolean isZoomOutButtonVisible() {
        return KpiChartWidget.create(driver, wait).isZoomOutButtonPresent();
    }

    @Step("I click Zoom Out Button")
    public void clickZoomOutButton() {
        KpiChartWidget.create(driver, wait).clickZoomOut();
        waitForPageToLoad(driver, wait);
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
        waitForPageToLoad(driver, wait);
        log.info("Dragging column with id: {} to position: {}", columnToDragId, position);
    }

    public void sortColumnASC(String columnId) {
        TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).sortColumnByASC(columnId);
        waitForPageToLoad(driver, wait);
        log.info("Sorting column with id: {} in ASC order", columnId);
    }

    public void sortColumnDESC(String columnId) {
        TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).sortColumnByDESC(columnId);
        waitForPageToLoad(driver, wait);
        log.info("Sorting column with id: {} in DESC order", columnId);
    }

    public boolean isValueInGivenRow(String value, int row, String columnId) {
        return TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).getCellValue(row, columnId).equals(value);
    }

    @Step("Get Bookmark Title from header")
    public String getBookmarkTitle() {
        waitForPageToLoad(driver, wait);
        return ToolbarWidget.create(driver, wait).getViewTitle();
    }
}