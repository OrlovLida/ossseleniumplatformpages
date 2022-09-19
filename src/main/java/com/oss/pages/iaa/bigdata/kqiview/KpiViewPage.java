package com.oss.pages.iaa.bigdata.kqiview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.attributechooser.ListAttributesChooser;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.layout.Card;
import com.oss.framework.components.mainheader.ButtonPanel;
import com.oss.framework.components.mainheader.Share;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.iaa.widgets.dpe.kpichartwidget.KpiChartWidget;
import com.oss.pages.administration.managerwizards.BookmarkWizardPage;
import com.oss.untils.FileDownload;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class KpiViewPage extends KpiViewSetupPage {

    private static final Logger log = LoggerFactory.getLogger(KpiViewPage.class);

    private static final String INDICATORS_TREE_ID = "_Indicators";
    private static final String DIMENSIONS_TREE_ID = "_Dimensions";
    private static final String DATA_VIEW_ID = "_Data_View";
    private static final String SAVE_BOOKMARK_BUTTON_ID = "ButtonSaveBookmark";
    private static final String CHILD_OBJECT_LEVEL_INPUT_ID = "SelectChildMOLevelChanged";
    private static final String IND_VIEW_TABLE_ID = "ind-view-table0";

    public KpiViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Go to Toolbar Panel")
    public KpiToolbarPanelPage getKpiToolbar() {
        return new KpiToolbarPanelPage(driver, wait);
    }

    @Step("Go to Chart Action")
    public ChartActionsPanelPage getChartActionPanel() {
        return new ChartActionsPanelPage(driver, wait);
    }

    @Step("I Open KPI View")
    public static KpiViewPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 90);

        String pageUrl = String.format("%s/#/view/indicators-view/indicators-view", basicURL);
        driver.get(pageUrl);
        waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new KpiViewPage(driver, wait);
    }

    private KpiChartWidget getKpiChartWidget() {
        return KpiChartWidget.create(driver, wait);
    }

    @Step("Attach exported chart to report")
    public void attachExportedChartToReport() {
        FileDownload.attachDownloadedFileToReport("kpi_view_export_*.*");
    }

    @Step("I minimize data View")
    public void minimizeDataView() {
        Card.createCard(driver, wait, DATA_VIEW_ID).minimizeCard();
    }

    private Card getCard(String cardId) {
        return Card.createCard(driver, wait, cardId);
    }

    @Step("I maximize data View")
    public void maximizeDataView() {
        getCard(DATA_VIEW_ID).maximizeCard();
    }

    @Step("I should see only Data View Panel displayed")
    public boolean isDataViewMaximized() {
        return getCard(DATA_VIEW_ID).isCardMaximized();
    }

    @Step("I minimize Indicators Panel")
    public void minimizeIndicatorsPanel() {
        getCard(INDICATORS_TREE_ID).minimizeCard();
    }

    @Step("I maximize Indicators Panel")
    public void maximizeIndicatorsPanel() {
        getCard(INDICATORS_TREE_ID).maximizeCard();
    }

    @Step("Indicators Panel should be maximized")
    public boolean isIndicatorsPanelMaximized() {
        return getCard(INDICATORS_TREE_ID).isCardMaximized();
    }

    @Step("I minimize Dimensions Panel")
    public void minimizeDimensionsPanel() {
        getCard(DIMENSIONS_TREE_ID).minimizeCard();
    }

    @Step("I maximize Dimensions Panel")
    public void maximizeDimensionsPanel() {
        getCard(DIMENSIONS_TREE_ID).maximizeCard();
    }

    @Step("Dimensions Panel should be maximized")
    public boolean isDimensionPanelMaximized() {
        return getCard(DIMENSIONS_TREE_ID).isCardMaximized();
    }

    @Step("I should see {expectedLinesCount} lines displayed")
    public boolean shouldSeeCurvesDisplayed(int expectedLinesCount) {
        waitForChartPresence();
        return getKpiChartWidget().countLines() == expectedLinesCount;
    }

    private void waitForChartPresence() {
        log.info("Waiting for chart presence");
        getKpiChartWidget().waitForPresenceAndVisibility();
    }

    @Step("I should see {expectedPieCharts} Pie Charts displayed")
    public boolean shouldSeePieChartsDisplayed(int expectedPieCharts) {
        return getKpiChartWidget().countPieCharts() == expectedPieCharts;
    }

    @Step("I check if Indicators View Table is empty")
    public boolean isIndicatorsViewTableEmpty() {
        waitForPageToLoad(driver, wait);
        return TableComponent.create(driver, wait, IND_VIEW_TABLE_ID).hasNoData();
    }

    @Step("I should see more than one line displayed")
    public boolean shouldSeeMoreThanOneCurveDisplayed() {
        return getKpiChartWidget().countLines() > 1;
    }

    @Step("I should see {expectedPointsCount} highlighted points displayed")
    public boolean shouldSeePointsDisplayed(int expectedPointsCount) {
        int pointsCount = (getKpiChartWidget().countVisiblePoints()) / 2;
        return pointsCount == expectedPointsCount;
    }

    @Step("I double click on bar in TopN BarChart")
    public void doubleClickTopNDPE() {
        getKpiChartWidget().doubleClickTopNBar();
        waitForPageToLoad(driver, wait);
    }

    @Step("I check if TopN navigation bar is visible")
    public boolean isTopNNavigationBarVisible() {
        log.info("Checking visibility of TopN navigation bar");
        return getKpiChartWidget().isTopNNavigationBarPresent();
    }

    @Step("I click legend")
    public void clickLegend() {
        log.info("Clicking first element from legend");
        getKpiChartWidget().clickDataSeriesLegend();
    }

    @Step("I should see {expectedLineWidth} width line displayed")
    public boolean shouldSeeDataSeriesLineWidth(String expectedLineWidth) {
        return getKpiChartWidget().getDataSeriesLineWidth().equals(expectedLineWidth);
    }

    @Step("I should not see any lines on chart displayed")
    public boolean shouldNotSeeHiddenLine(String expectedLineVisibility) {
        return getKpiChartWidget().getDataSeriesVisibility().equals(expectedLineVisibility);
    }

    @Step("Check if data-series-type is {dataSeriesType}")
    public boolean isDataSeriesType(String dataSeriesType) {
        return KpiChartWidget.create(driver, wait).getDataSeriesType().equals(dataSeriesType);
    }

    @Step("I should see topN bar chart displayed with DFE data")
    public boolean dfeTopNBarChartIsDisplayed() {
        return getKpiChartWidget().isTopNBarChartIsPresent();
    }

    @Step("I should see topN bar chart displayed with DPE data")
    public boolean dpeTopNBarChartIsDisplayed() {
        return getKpiChartWidget().isTopNBarChartIsPresent();
    }

    @Step("I should see bar chart displayed")
    public boolean shouldSeeColorChart(String expectedColor) {
        return getKpiChartWidget().getDataSeriesColor().equals(expectedColor);
    }

    @Step("I should see last sample time below chart")
    public boolean shouldSeeLastSampleTime(int expectedVisibleLastSampleTimeNumber) {
        waitForPageToLoad(driver, wait);
        return getKpiChartWidget().countVisibleLastSampleTime() == expectedVisibleLastSampleTimeNumber;
    }

    @Step("Checking visibility of Time Zone option")
    public boolean isTimeZoneDisplayed() {
        log.info("Checking visibility of Time Zone option");
        waitForPageToLoad(driver, wait);
        return getKpiChartWidget().isTimeZonePresent();
    }

    @Step("I should see data completeness displayed in the legend")
    public boolean shouldSeeDataCompleteness() {
        waitForPageToLoad(driver, wait);
        return getKpiChartWidget().countVisibleDataCompleteness() > 0;
    }

    @Step("I should see other period displayed in the legend")
    public boolean isLegendWithOtherPeriodDisplayed() {
        waitForPageToLoad(driver, wait);
        return getKpiChartWidget().isLegendPresent("Other period");
    }

    @Step("Check visibility of Common Legend")
    public boolean isCommonLegendPresent() {
        log.info("Checking visibility of Common Legend");
        return getKpiChartWidget().isCommonLegendPresent();
    }

    @Step("Click Save bookmark")
    public BookmarkWizardPage clickSaveBookmark() {
        ButtonPanel.create(driver, wait).clickButton(SAVE_BOOKMARK_BUTTON_ID);
        return new BookmarkWizardPage(driver, wait);
    }

    @Step("I click Share View icon")
    public void clickShare() {
        getToolbar().openSharePanel();
        log.info("Click in Share icon");
    }

    private ToolbarWidget getToolbar() {
        return ToolbarWidget.create(driver, wait);
    }

    @Step("I click close panel")
    public void clickCloseShare() {
        getToolbar().closeSharePanel();
        log.info("Closing Share panel");
    }

    @Step("Copy link from Share panel")
    public String copyLink() {
        log.info("Copy link from Share panel");
        return Share.create(driver, wait).copyLink();
    }

    @Step("Go to link copied from Share panel")
    public void goToLink() {
        String link = copyLink();
        clickCloseShare();
        driver.get(link);
        waitForPageToLoad(driver, wait);
        log.info("Redirecting to page from link in Share panel");
    }

    @Step("I fill field: Display series for child objects from level")
    public void fillLevelOfChildObjects(String level) {
        ComponentFactory.create(CHILD_OBJECT_LEVEL_INPUT_ID, driver, wait).setSingleStringValue(level);
    }

    @Step("I check if expected number of charts is visible")
    public boolean isExpectedNumberOfChartsVisible(int expectedNumberOfCharts) {
        return getKpiChartWidget().countCharts() == expectedNumberOfCharts;
    }

    @Step("I zoom the data view")
    public void zoomChart() {
        getKpiChartWidget().zoomDataView();
        waitForPageToLoad(driver, wait);
    }

    @Step("I check if Zoom Out button is visible")
    public boolean isZoomOutButtonVisible() {
        return !getKpiChartWidget().isZoomOutButtonHidden();
    }

    @Step("I click Zoom Out Button")
    public void clickZoomOutButton() {
        getKpiChartWidget().clickZoomOut();
        waitForPageToLoad(driver, wait);
    }

    @Step("I enable column {columnId} into table")
    public void enableColumnInTheTable(String columnId) {
        ListAttributesChooser listAttributesChooser = getListAttributesChooser();
        listAttributesChooser.enableAttributeById(columnId);
        listAttributesChooser.clickApply();
        log.info("Enabling column with id: {}", columnId);
    }

    private ListAttributesChooser getListAttributesChooser() {
        return getIndViewTable().getListAttributesChooser();
    }

    private TableComponent getIndViewTable() {
        return TableComponent.create(driver, wait, IND_VIEW_TABLE_ID);
    }

    @Step("I disable column {columnId} into table")
    public void disableColumnInTheTable(String columnId) {
        ListAttributesChooser listAttributesChooser = getListAttributesChooser();
        listAttributesChooser.disableAttributeById(columnId);
        listAttributesChooser.clickApply();
        log.info("Disabling column with id: {}", columnId);
    }

    @Step("I check if column with Header {columnHeader} is present in the Table")
    public boolean isColumnInTable(String columnHeader) {
        return getIndViewTable().getColumnHeaders().contains(columnHeader);
    }

    @Step("I change columns order to: first column - {columnId}")
    public void dragColumnToTarget(String columnId, String targetColumnId) {
        getListAttributesChooser().enableAttributeById(columnId);
        getListAttributesChooser().dragColumnToTarget(columnId, targetColumnId);
        getListAttributesChooser().clickApply();
        log.info("Changing columns by dragging in table options menu. First column is column with id: {}", columnId);
    }

    @Step("I check if column with Header {columnHeader} is first column in the Table")
    public boolean isColumnFirstInTable(String columnHeader) {
        return getIndViewTable()
                .getColumnHeaders()
                .stream()
                .findFirst()
                .orElse("")
                .equals(columnHeader);
    }

    @Step("I drag column in Table")
    public void changeColumnsOrderInTable(String columnToDragId, int position) {
        getIndViewTable().changeColumnsOrderById(columnToDragId, position);
        waitForPageToLoad(driver, wait);
        log.info("Dragging column with id: {} to position: {}", columnToDragId, position);
    }

    public void sortColumnASC(String columnId) {
        getIndViewTable().sortColumnByASC(columnId);
        waitForPageToLoad(driver, wait);
        log.info("Sorting column with id: {} in ASC order", columnId);
    }

    public void sortColumnDESC(String columnId) {
        getIndViewTable().sortColumnByDESC(columnId);
        waitForPageToLoad(driver, wait);
        log.info("Sorting column with id: {} in DESC order", columnId);
    }

    public boolean isValueInGivenRow(String value, int row, String columnId) {
        return getIndViewTable().getCellValue(row, columnId).equals(value);
    }

    @Step("Get Bookmark Title from header")
    public String getBookmarkTitle() {
        waitForPageToLoad(driver, wait);
        return getToolbar().getViewTitle();
    }

    @Step("Check if Value: {value} is visible on Yaxis")
    public boolean isYaxisValueVisible(String value) {
        return getKpiChartWidget().allYaxisVisibleValues().contains(value);
    }
}