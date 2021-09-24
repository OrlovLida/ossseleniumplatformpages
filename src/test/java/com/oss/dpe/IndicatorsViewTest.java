package com.oss.dpe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.oss.framework.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.*;

@Listeners({TestListener.class})
public class IndicatorsViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(IndicatorsViewTest.class);

    private static final String HIDDEN_DATA_SERIES_VISIBILITY = "hidden";
    private static final String HIGHLIGHTED_DATA_SERIES_WIDTH = "5px";
    private static final String NORMAL_DATA_SERIES_WIDTH = "2px";
    private static final String AREA_CHART_FILL_OPACITY = "0.6";
    private static final String BAR_CHART_FILL_OPACITY = "0.6";
    private static final String LINE_CHART_FILL_OPACITY = "0";
    private static final String FIRST_CHART_COLOR = "rgb(150, 65, 54)";
    private static final String LAYOUT_EXPECTED_STATUS = "active";

    private KpiViewPage kpiViewPage;

    @BeforeMethod
    public void goToKpiView() {
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 1, testName = "Highlighting and hiding data series", description = "Highlighting and hiding data series")
    @Description("I verify if KPI View for DPE data works properly")
    public void verifyIfKpiViewWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect, filterName);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.clickLegend();
            Assert.assertTrue(kpiViewPage.shouldSeeDataSeriesLineWidth(HIGHLIGHTED_DATA_SERIES_WIDTH));
            kpiViewPage.clickLegend();
            Assert.assertTrue(kpiViewPage.shouldNotSeeHiddenLine(HIDDEN_DATA_SERIES_VISIBILITY));
            kpiViewPage.clickLegend();
            Assert.assertTrue(kpiViewPage.shouldSeeDataSeriesLineWidth(NORMAL_DATA_SERIES_WIDTH));
            kpiViewPage.exportChart();
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 2, testName = "Changing chart type", description = "Changing chart type")
    @Description("I verify if changing chart type works properly")
    public void verifyIfChartTypesWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect, filterName);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.clickAreaChartType();
            Assert.assertTrue(kpiViewPage.shouldSeeAreaChart(AREA_CHART_FILL_OPACITY));
            kpiViewPage.clickBarChartType();
            Assert.assertTrue(kpiViewPage.shouldSeeBarChart(BAR_CHART_FILL_OPACITY));
            kpiViewPage.clickLineChartType();
            Assert.assertTrue(kpiViewPage.shouldSeeAreaChart(LINE_CHART_FILL_OPACITY));
            kpiViewPage.chooseDataSeriesColor();
            Assert.assertTrue(kpiViewPage.shouldSeeColorChart(FIRST_CHART_COLOR));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 3, testName = "Checking time period chooser", description = "Checking time period chooser")
    @Description("I verify if Time Period Chooser works properly")
    public void verifyIfPeriodChooserWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect, filterName);
            kpiViewPage.setValueInTimePeriodChooser(1, 2, 3);
            kpiViewPage.applyChanges();
            kpiViewPage.chooseSmartOptionInTimePeriodChooser();
            kpiViewPage.applyChanges();
            kpiViewPage.chooseLatestOptionInTimePeriodChooser();
            kpiViewPage.applyChanges();
            kpiViewPage.clickLegend();
            Assert.assertTrue(kpiViewPage.shouldSeePointsDisplayed(1));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 4, testName = "Changing Y axis option", description = "Changing Y axis option")
    @Description("I verify if Manual Y axis option works properly")
    public void verifyIfYAxisOptionsWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME,AQ_TIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect, filterName);
            kpiViewPage.chooseSumAggregationMethod();
            kpiViewPage.applyChanges();
            Assert.assertTrue(kpiViewPage.shouldSeeVisibleYaxis(2));
            kpiViewPage.chooseManualYaxis();
            Assert.assertTrue(kpiViewPage.shouldSeeVisibleYaxis(1));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 5, testName = "Enabling Miscellaneous options", description = "Enabling Miscellaneous options")
    @Description("I verify if Miscellaneous options work properly")
    public void verifyIfMiscellaneousOptionsWorkProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME,AQ_TIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect, filterName);
            kpiViewPage.enableDataCompleteness();
            kpiViewPage.applyChanges();
            Assert.assertTrue(kpiViewPage.shouldSeeDataCompleteness());
            kpiViewPage.enableLastSampleTime();
            Assert.assertTrue(kpiViewPage.shouldSeeLastSampleTime(1));
            kpiViewPage.applyChanges();
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 6, testName = "Enabling Compare with other period option", description = "Enabling Compare with other period option")
    @Description("I verify if Compare with other period option works properly")
    public void verifyIfCompareWithOtherPeriodWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("AQ_TIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect, filterName);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.enableCompareWithOtherPeriod();
            kpiViewPage.applyChanges();
            Assert.assertTrue(kpiViewPage.shouldSeeOtherPeriod());
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 7, testName = "Verifying resize panel option", description = "Verifying resize panel option")
    @Description("I verify if resize panel option works properly")
    public void verifyIfResizePanelOptionWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("AQ_TIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect, filterName);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.maximizeDataView();
            kpiViewPage.shouldSeeOnlyDataViewDisplayed();
            kpiViewPage.minimizeDataView();
            kpiViewPage.maximizeIndicatorsPanel();
            kpiViewPage.minimizeIndicatorsPanel();
            kpiViewPage.maximizeDimensionsPanel();
            kpiViewPage.minimizeDimensionsPanel();
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 8, testName = "Verifying changing layout option", description = "Verifying changing layout option")
    @Description("I verify if changing layout option works properly")
    public void ChangingChartLayoutOption(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("AQ_TIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect, filterName);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.changeLayout(LAYOUT_1x1);
            Assert.assertTrue(kpiViewPage.layoutButtonStatus(LAYOUT_1x1).equals(LAYOUT_EXPECTED_STATUS));
            kpiViewPage.changeLayout(LAYOUT_2x1);
            Assert.assertTrue(kpiViewPage.layoutButtonStatus(LAYOUT_2x1).equals(LAYOUT_EXPECTED_STATUS));
            kpiViewPage.changeLayout(LAYOUT_2x2);
            Assert.assertTrue(kpiViewPage.layoutButtonStatus(LAYOUT_2x2).equals(LAYOUT_EXPECTED_STATUS));
            kpiViewPage.changeLayout(LAYOUT_4x4);
            Assert.assertTrue(kpiViewPage.layoutButtonStatus(LAYOUT_4x4).equals(LAYOUT_EXPECTED_STATUS));
            kpiViewPage.changeLayout(LAYOUT_3x3);
            Assert.assertTrue(kpiViewPage.layoutButtonStatus(LAYOUT_3x3).equals(LAYOUT_EXPECTED_STATUS));
            kpiViewPage.changeLayout(LAYOUT_3x2);
            Assert.assertTrue(kpiViewPage.layoutButtonStatus(LAYOUT_3x2).equals(LAYOUT_EXPECTED_STATUS));
            kpiViewPage.changeLayout(LAYOUT_AUTO);
            Assert.assertTrue(kpiViewPage.layoutButtonStatus(LAYOUT_AUTO).equals(LAYOUT_EXPECTED_STATUS));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }
}
