package com.oss.dpe;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.iaa.widgets.dpe.toolbarpanel.OptionsPanel;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.pages.bigdata.kqiview.KpiViewSetupPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.LAYOUT_1x1;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.LAYOUT_2x1;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.LAYOUT_2x2;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.LAYOUT_3x2;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.LAYOUT_3x3;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.LAYOUT_4x4;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.LAYOUT_AUTO;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Listeners({TestListener.class})
public class IndicatorsViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(IndicatorsViewTest.class);

    private static final String HIDDEN_DATA_SERIES_VISIBILITY = "hidden";
    private static final String HIGHLIGHTED_DATA_SERIES_WIDTH = "5px";
    private static final String NORMAL_DATA_SERIES_WIDTH = "2px";
    private static final String FIRST_CHART_COLOR = "rgb(150, 65, 54)";
    private static final String LAYOUT_EXPECTED_STATUS = "active";
    private static final String INDICATORS_TREE_ID = "_Indicators";
    private static final String DIMENSIONS_TREE_ID = "_Dimensions";
    private static final String IDENTIFIER_COLUMN_ID = "DPE_DIMENSION_ATTR_MANAGED_OBJECT_IDENTIFIER";
    private static final String IDENTIFIER_COLUMN_HEADER = "IDENTIFIER";
    private static final String SAMPLE_TIME_COLUMN_ID = "STIME";
    private static final String SAMPLE_TIME_COLUMN_HEADER = "Sample Time";
    private static final String MANAGED_OBJECT_COLUMN_DATA_COL_ID = "DPE_DIMENSION_managed_object";

    private KpiViewPage kpiViewPage;
    private KpiViewSetupPage kpiViewSetup;

    @Parameters({"kpiViewType"})
    @BeforeMethod
    public void goToKpiView(
            @Optional("INDICATORS_VIEW") KpiViewPage.KpiViewType kpiViewType
    ) {
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL, kpiViewType);
        kpiViewSetup = new KpiViewSetupPage(driver, webDriverWait);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 1, testName = "Highlighting and hiding data series", description = "Highlighting and hiding data series")
    @Description("I verify if KPI View works properly")
    public void verifyIfKpiViewWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
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
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.clickAreaChartType();
            assertTrue(kpiViewPage.isDataSeriesType("area"));
            kpiViewPage.clickBarChartType();
            assertTrue(kpiViewPage.isDataSeriesType("bar"));
            kpiViewPage.clickLineChartType();
            assertTrue(kpiViewPage.isDataSeriesType("line"));
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
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
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
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
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
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            kpiViewPage.enableDataCompleteness();
            kpiViewPage.applyChanges();
            assertTrue(kpiViewPage.shouldSeeDataCompleteness());

            kpiViewPage.enableShowTimeZone();
            kpiViewPage.applyChanges();
            assertTrue(kpiViewPage.isTimeZoneDisplayed());

            kpiViewPage.enableLastSampleTime();
            assertTrue(kpiViewPage.shouldSeeLastSampleTime(1));
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
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

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
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.maximizeDataView();
            assertTrue(kpiViewPage.shouldSeeOnlyDataViewDisplayed());

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
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
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
    @Test(priority = 9, testName = "Search in indicators and dimensions trees", description = "Verify search from indicators and dimensions trees")
    @Description("Verify search from indicators and dimensions trees")
    public void searchIndicators(
            @Optional("Data Collection Statistics") String filterName,
            @Optional("DBTIME") String indicator,
            @Optional("DC Type: PMSTA_DC") String dimension
    ) {
        kpiViewSetup.setFilters(Collections.singletonList(filterName));
        kpiViewSetup.searchInToolbarPanel(indicator, INDICATORS_TREE_ID);
        kpiViewSetup.searchInToolbarPanel(dimension, DIMENSIONS_TREE_ID);
        kpiViewPage.selectAggregationMethod(OptionsPanel.AggregationMethodOption.SUM);
        kpiViewPage.unselectEveryAggMethodOtherThan(OptionsPanel.AggregationMethodOption.SUM);
        kpiViewPage.applyChanges();

        assertTrue(kpiViewSetup.isNodeInTreeSelected(indicator, INDICATORS_TREE_ID));
        assertTrue(kpiViewSetup.isNodeInTreeSelected(dimension, DIMENSIONS_TREE_ID));
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
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            String activeAggMethod = kpiViewPage.activeAggMethod();
            kpiViewPage.closeOptionsPanel();

            kpiViewPage.clickLinkToChart();

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            assertTrue(kpiViewSetup.isNodeInTreeSelected("AQ_TIME 1h " + activeAggMethod, INDICATORS_TREE_ID));
            assertTrue(kpiViewSetup.isNodeInTreeSelected(dimensionNodesToSelect, DIMENSIONS_TREE_ID));
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
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.clickShare();
            kpiViewPage.goToLink();
            kpiViewPage.clickCloseShare();

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            assertTrue(kpiViewSetup.isNodeInTreeSelected(indicatorNodesToSelect, INDICATORS_TREE_ID));
            assertTrue(kpiViewSetup.isNodeInTreeSelected(dimensionNodesToSelect, DIMENSIONS_TREE_ID));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName", "dimensionFolderWithOptions"})
    @Test(priority = 12, testName = "Display Child Objects", description = "Display series for child objects")
    @Description("Display series for child objects")
    public void childObjectTest(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional("Self Monitoring Package,MOs: self:DPE:IF Run Analysis") String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("All Self Monitoring") String filterName,
            @Optional("Managed Objects") String dimensionFolderWithOptions
    ) {
        try {
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewSetup.clickDimensionOptions(dimensionFolderWithOptions);
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
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
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
    @Test(priority = 14, testName = "Check topN Panel", description = "Check topN Panel")
    @Description("Check topN Panel")
    public void checkTopNPanelForDpe(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.clickPerformTopN();

            assertTrue(kpiViewPage.dpeTopNBarChartIsDisplayed());
            assertTrue(kpiViewPage.isExpectedNumberOfChartsVisible(2));

            kpiViewPage.doubleClickTopNDPE();
            assertTrue(kpiViewPage.isTopNNavigationBarVisible());
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 15, testName = "Aggregation Method Check", description = "Aggregation Method Check")
    @Description("Aggregation Method Check")
    public void aggregationMethodCheck(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME,AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));

            kpiViewPage.selectAggregationMethod(OptionsPanel.AggregationMethodOption.MAX);
            kpiViewPage.selectAggregationMethod(OptionsPanel.AggregationMethodOption.MIN);
            kpiViewPage.selectAggregationMethod(OptionsPanel.AggregationMethodOption.SUM);
            kpiViewPage.unselectEveryAggMethodOtherThan(OptionsPanel.AggregationMethodOption.MAX);
            kpiViewPage.applyChanges();
            String selectedAggMethod = kpiViewPage.activeAggMethod();
            int numOfActiveAggMethods = kpiViewPage.numberOfActiveAggMethods();

            assertEquals(selectedAggMethod, "MAX");
            assertEquals(numOfActiveAggMethods, 1);
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 16, testName = "Zoom in/zoom out check", description = "Zoom in/zoom out check")
    @Description("Zoom in/zoom out check")
    public void zoomInChartCheck(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.zoomChart();
            assertTrue(kpiViewPage.isZoomOutButtonVisible());

            kpiViewPage.clickZoomOutButton();
            assertFalse(kpiViewPage.isZoomOutButtonVisible());
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 17, testName = "table settings: sorting, adding/removing attribute columns", description = "table settings: sorting, adding/removing attribute columns")
    @Description("table settings: sorting, adding/removing attribute columns")
    public void checkTableSettings(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC,DC Type: PMSTA_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));

            kpiViewPage.setDisplayType("Table");
            assertFalse(kpiViewPage.isIndicatorsViewTableEmpty());

            kpiViewPage.enableColumnInTheTable(IDENTIFIER_COLUMN_ID);
            assertTrue(kpiViewPage.isColumnInTable(IDENTIFIER_COLUMN_HEADER));
            assertFalse(kpiViewPage.isIndicatorsViewTableEmpty());

            kpiViewPage.disableColumnInTheTable(IDENTIFIER_COLUMN_ID);
            assertFalse(kpiViewPage.isColumnInTable(IDENTIFIER_COLUMN_HEADER));
            assertFalse(kpiViewPage.isIndicatorsViewTableEmpty());

            kpiViewPage.dragColumnToTarget(IDENTIFIER_COLUMN_ID, SAMPLE_TIME_COLUMN_ID);
            assertTrue(kpiViewPage.isColumnFirstInTable(IDENTIFIER_COLUMN_HEADER));
            assertFalse(kpiViewPage.isIndicatorsViewTableEmpty());

            kpiViewPage.changeColumnsOrderInTable(SAMPLE_TIME_COLUMN_ID, 0);
            assertTrue(kpiViewPage.isColumnFirstInTable(SAMPLE_TIME_COLUMN_HEADER));
            assertFalse(kpiViewPage.isIndicatorsViewTableEmpty());

            kpiViewPage.sortColumnASC(MANAGED_OBJECT_COLUMN_DATA_COL_ID);
            assertTrue(kpiViewPage.isValueInGivenRow("DC Type: PMSTA_DC", 0, MANAGED_OBJECT_COLUMN_DATA_COL_ID));

            kpiViewPage.sortColumnDESC(MANAGED_OBJECT_COLUMN_DATA_COL_ID);
            assertTrue(kpiViewPage.isValueInGivenRow("DC Type: THRES_DC", 0, MANAGED_OBJECT_COLUMN_DATA_COL_ID));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }
}