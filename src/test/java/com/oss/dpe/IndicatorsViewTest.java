package com.oss.dpe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    private KpiViewPage kpiViewPage;

    @BeforeMethod
    public void goToKpiView() {
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect"})
    @Test(priority = 1, testName = "Highlighting and hiding data series", description = "Highlighting and hiding data series")
    @Description("I verify if KPI View for DPE data works properly")
    public void verifyIfKpiViewWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect
    ) {
        try {
            kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect);
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

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect"})
    @Test(priority = 2, testName = "Changing chart type", description = "Changing chart type")
    @Description("I verify if changing chart type works properly")
    public void verifyIfChartTypesWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME,AQ_TIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect
    ) {
        try {
            kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));
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

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect"})
    @Test(priority = 3, testName = "Checking time period chooser", description = "Checking time period chooser")
    @Description("I verify if Time Period Chooser works properly")
    public void verifyIfPeriodChooserWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME,AQ_TIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect
    ) {
        try {
            kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect);
            kpiViewPage.setValueInTimePeriodChooser(1, 2, 3);
            kpiViewPage.applyChanges();
            kpiViewPage.chooseSmartOptionInTimePeriodChooser();
            kpiViewPage.applyChanges();
            kpiViewPage.chooseLatestOptionInTimePeriodChooser();
            kpiViewPage.applyChanges();
            kpiViewPage.clickLegend();
            Assert.assertTrue(kpiViewPage.shouldSeePointsDisplayed(2));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect"})
    @Test(priority = 4, testName = "Changing Y axis option", description = "Changing Y axis option")
    @Description("I verify if Manual Y axis option works properly")
    public void verifyIfYAxisOptionsWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME,AQ_TIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect
    ) {
        try {
            kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect);
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

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect"})
    @Test(priority = 5, testName = "Enabling Miscellaneous options", description = "Enabling Miscellaneous options")
    @Description("I verify if Miscellaneous options work properly")
    public void verifyIfMiscellaneousOptionsWorkProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME,AQ_TIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect
    ) {
        try {
            kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect);
            kpiViewPage.enableDataCompleteness();
            kpiViewPage.applyChanges();
            Assert.assertTrue(kpiViewPage.shouldSeeDataCompleteness());
            kpiViewPage.enableLastSampleTime();
            Assert.assertTrue(kpiViewPage.shouldSeeLastSampleTime(1));
            kpiViewPage.applyChanges();
            kpiViewPage.enableCompareWithOtherPeriod();
            kpiViewPage.applyChanges();
            Assert.assertTrue(kpiViewPage.shouldSeeOtherPeriod());
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    private void kpiViewSetup(String indicatorNodesToExpand, String indicatorNodesToSelect, String dimensionNodesToSelect) {
        kpiViewPage.setFilters(Collections.singletonList("Data Collection Statistics"));

        List<String> indicatorNodesToExpandList = Arrays.asList(indicatorNodesToExpand.split(","));
        List<String> indicatorNodesToSelectList = Arrays.asList(indicatorNodesToSelect.split(","));
        kpiViewPage.selectIndicator(indicatorNodesToExpandList, indicatorNodesToSelectList);

        List<String> dimensionNodesToSelectList = Arrays.asList(dimensionNodesToSelect.split(","));
        kpiViewPage.selectUnfoldedDimension(dimensionNodesToSelectList);

        kpiViewPage.applyChanges();
        kpiViewPage.seeChartIsDisplayed();
    }
}
