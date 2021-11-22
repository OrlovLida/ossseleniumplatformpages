package com.oss.dpe;

import com.oss.BaseTestCase;
import com.oss.framework.widgets.dpe.toolbarpanel.OptionsPanel;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.util.Collections;

import static com.oss.framework.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.*;
import static org.testng.Assert.*;

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
    private static final String INDICATORS_TREE_ID = "_Indicators";
    private static final String DIMENSIONS_TREE_ID = "_Dimensions";

    private KpiViewPage kpiViewPage;

    @BeforeMethod
    public void goToKpiView() {
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 1, testName = "Highlighting and hiding data series", description = "Highlighting and hiding data series")
    @Description("I verify if KPI View for DPE data works properly")
    public void verifyIfKpiViewWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.clickLegend();
            assertTrue(kpiViewPage.shouldSeeDataSeriesLineWidth(HIGHLIGHTED_DATA_SERIES_WIDTH));
            kpiViewPage.clickLegend();
            assertTrue(kpiViewPage.shouldNotSeeHiddenLine(HIDDEN_DATA_SERIES_VISIBILITY));
            kpiViewPage.clickLegend();
            assertTrue(kpiViewPage.shouldSeeDataSeriesLineWidth(NORMAL_DATA_SERIES_WIDTH));
            kpiViewPage.exportChart();
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 2, testName = "Changing chart type", description = "Changing chart type")
    @Description("I verify if changing chart type works properly")
    public void verifyIfChartTypesWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.clickAreaChartType();
            assertTrue(kpiViewPage.shouldSeeAreaChart(AREA_CHART_FILL_OPACITY));
            kpiViewPage.clickBarChartType();
            assertTrue(kpiViewPage.shouldSeeBarChart(BAR_CHART_FILL_OPACITY));
            kpiViewPage.clickLineChartType();
            assertTrue(kpiViewPage.shouldSeeAreaChart(LINE_CHART_FILL_OPACITY));
            kpiViewPage.chooseDataSeriesColor();
            assertTrue(kpiViewPage.shouldSeeColorChart(FIRST_CHART_COLOR));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 3, testName = "Checking time period chooser", description = "Checking time period chooser")
    @Description("I verify if Time Period Chooser works properly")
    public void verifyIfPeriodChooserWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            kpiViewPage.setValueInTimePeriodChooser(1, 2, 3);
            kpiViewPage.applyChanges();
            kpiViewPage.chooseSmartOptionInTimePeriodChooser();
            kpiViewPage.applyChanges();
            kpiViewPage.chooseLatestOptionInTimePeriodChooser();
            kpiViewPage.applyChanges();
            kpiViewPage.clickLegend();
            assertTrue(kpiViewPage.shouldSeePointsDisplayed(1));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 4, testName = "Changing Y axis option", description = "Changing Y axis option")
    @Description("I verify if Manual Y axis option works properly")
    public void verifyIfYAxisOptionsWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME,LOADED") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeVisibleYaxis(2));

            kpiViewPage.chooseManualYaxis();
            assertTrue(kpiViewPage.shouldSeeVisibleYaxis(1));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 5, testName = "Enabling Miscellaneous options", description = "Enabling Miscellaneous options")
    @Description("I verify if Miscellaneous options work properly")
    public void verifyIfMiscellaneousOptionsWorkProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME,AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            kpiViewPage.enableDataCompleteness();
            kpiViewPage.applyChanges();
            assertTrue(kpiViewPage.shouldSeeDataCompleteness());
            kpiViewPage.enableLastSampleTime();
            assertTrue(kpiViewPage.shouldSeeLastSampleTime(1));
            kpiViewPage.applyChanges();
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 6, testName = "Enabling Compare with other period option", description = "Enabling Compare with other period option")
    @Description("I verify if Compare with other period option works properly")
    public void verifyIfCompareWithOtherPeriodWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.enableCompareWithOtherPeriod();
            kpiViewPage.applyChanges();
            assertTrue(kpiViewPage.shouldSeeOtherPeriod());
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 7, testName = "Verifying resize panel option", description = "Verifying resize panel option")
    @Description("I verify if resize panel option works properly")
    public void verifyIfResizePanelOptionWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.maximizeDataView();
            kpiViewPage.shouldSeeOnlyDataViewDisplayed();
            kpiViewPage.minimizeDataView();
            kpiViewPage.maximizeIndicatorsPanel();
            kpiViewPage.minimizeIndicatorsPanel();
            kpiViewPage.maximizeDimensionsPanel();
            kpiViewPage.minimizeDimensionsPanel();
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 8, testName = "Verifying changing layout option", description = "Verifying changing layout option")
    @Description("I verify if changing layout option works properly")
    public void changingChartLayoutOption(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.changeLayout(LAYOUT_1x1);
            assertEquals(kpiViewPage.layoutButtonStatus(LAYOUT_1x1), LAYOUT_EXPECTED_STATUS);
            kpiViewPage.changeLayout(LAYOUT_2x1);
            assertEquals(kpiViewPage.layoutButtonStatus(LAYOUT_2x1), LAYOUT_EXPECTED_STATUS);
            kpiViewPage.changeLayout(LAYOUT_2x2);
            assertEquals(kpiViewPage.layoutButtonStatus(LAYOUT_2x2), LAYOUT_EXPECTED_STATUS);
            kpiViewPage.changeLayout(LAYOUT_4x4);
            assertEquals(kpiViewPage.layoutButtonStatus(LAYOUT_4x4), LAYOUT_EXPECTED_STATUS);
            kpiViewPage.changeLayout(LAYOUT_3x3);
            assertEquals(kpiViewPage.layoutButtonStatus(LAYOUT_3x3), LAYOUT_EXPECTED_STATUS);
            kpiViewPage.changeLayout(LAYOUT_3x2);
            assertEquals(kpiViewPage.layoutButtonStatus(LAYOUT_3x2), LAYOUT_EXPECTED_STATUS);
            kpiViewPage.changeLayout(LAYOUT_AUTO);
            assertEquals(kpiViewPage.layoutButtonStatus(LAYOUT_AUTO), LAYOUT_EXPECTED_STATUS);
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"filterName", "indicator", "dimension"})
    @Test(priority = 9, testName = "Search in indicators and dimensions trees", description = "Verify search from indicators and dimensions trees for DPE data")
    @Description("Verify search from indicators and dimensions trees for DPE data")
    public void searchIndicators(
            @Optional("Data Collection Statistics") String filterName,
            @Optional("DBTIME") String indicator,
            @Optional("DC Type: PMSTA_DC") String dimension
    ) {
        kpiViewPage.setFilters(Collections.singletonList(filterName));
        kpiViewPage.searchInToolbarPanel(indicator, INDICATORS_TREE_ID);
        kpiViewPage.searchInToolbarPanel(dimension, DIMENSIONS_TREE_ID);
        kpiViewPage.applyChanges();

        assertTrue(kpiViewPage.isNodeInTreeSelected(indicator, INDICATORS_TREE_ID));
        assertTrue(kpiViewPage.isNodeInTreeSelected(dimension, DIMENSIONS_TREE_ID));
        assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 10, testName = "Clicking link to chart from chart actions", description = "Clicking link to chart from chart actions")
    @Description("Clicking link to chart from chart actions")
    public void checkLinkToIndicatorsView(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            String activeAggMethod = kpiViewPage.activeAggMethod();
            kpiViewPage.closeOptionsPanel();

            kpiViewPage.clickLinkToChart();

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            assertTrue(kpiViewPage.isNodeInTreeSelected("AQ_TIME 1h " + activeAggMethod, INDICATORS_TREE_ID));
            assertTrue(kpiViewPage.isNodeInTreeSelected(dimensionNodesToSelect, DIMENSIONS_TREE_ID));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 11, testName = "Sharing view", description = "Sharing view by created link")
    @Description("Sharing view by created link")
    public void sharePanelTest(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.clickShare();
            kpiViewPage.goToLink();
            kpiViewPage.clickCloseShare();

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            assertTrue(kpiViewPage.isNodeInTreeSelected(indicatorNodesToSelect, INDICATORS_TREE_ID));
            assertTrue(kpiViewPage.isNodeInTreeSelected(dimensionNodesToSelect, DIMENSIONS_TREE_ID));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 12, testName = "Display Child Objects", description = "Display series for child objects")
    @Description("Display series for child objects")
    public void childObjectTest(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.clickDimensionOptions(dimensionNodesToExpand);
            kpiViewPage.fillLevelOfChildObjects("1");
            kpiViewPage.applyChanges();

            assertTrue(kpiViewPage.shouldSeeMoreThanOneCurveDisplayed());
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 13, testName = "Change display type chart/table/pieChart", description = "Change display type chart/table/pieChart")
    @Description("Change display type chart/table/pieChart")
    public void changeDisplayType(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.setDisplayType("Pie Chart");
            assertTrue(kpiViewPage.shouldSeePieChartsDisplayed(1));

            kpiViewPage.setDisplayType("Chart");
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.setDisplayType("Table");
            assertFalse(kpiViewPage.isIndicatorsViewTableEmpty());
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 14, testName = "Check topN Panel for DPE data", description = "Check topN Panel for DPE data")
    @Description("Check topN Panel for DPE data")
    public void checkTopNPanelForDpe(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.clickPerformTopN();

            assertTrue(kpiViewPage.dpeTopNBarChartIsDisplayed());
            assertTrue(kpiViewPage.isExpectedNumberOfChartsVisible(2));
            assertTrue(kpiViewPage.shouldSeeBoxesAndCurvesDisplayed(6, 5));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 15, testName = "Aggregation Method Check", description = "Aggregation Method Check")
    @Description("Aggregation Method Check")
    public void aggregationMethodCheck(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME,AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));

            kpiViewPage.selectAggregationMethod(OptionsPanel.AggregationMethodOption.MAX);
            kpiViewPage.selectAggregationMethod(OptionsPanel.AggregationMethodOption.MIN);
            kpiViewPage.selectAggregationMethod(OptionsPanel.AggregationMethodOption.SUM);
            kpiViewPage.unselectEveryAggMethodOtherThan(OptionsPanel.AggregationMethodOption.MAX);
            kpiViewPage.applyChanges();
            String selectedAggMethod = kpiViewPage.activeAggMethod();
            int numOfActiveAggMethods = kpiViewPage.numberOfActiveAggMethods();

            assertTrue(selectedAggMethod.equals("MAX"));
            assertEquals(numOfActiveAggMethods, 1);
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }
}