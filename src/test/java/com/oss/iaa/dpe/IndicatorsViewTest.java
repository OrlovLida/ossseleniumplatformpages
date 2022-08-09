package com.oss.iaa.dpe;

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
import com.oss.pages.iaa.bigdata.kqiview.KpiViewPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.LAYOUT_1X1;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.LAYOUT_2X1;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.LAYOUT_2X2;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.LAYOUT_3X2;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.LAYOUT_3X3;
import static com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel.LayoutType.LAYOUT_4X4;
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

    @BeforeMethod
    public void goToKpiView(
    ) {
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 1, testName = "Highlighting and hiding data series", description = "Highlighting and hiding data series")
    @Description("I verify if KPI View works properly")
    public void verifyIfKpiViewWorksProperly(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
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
            kpiViewPage.getKpiToolbar().exportChart();
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 2, testName = "Changing chart type", description = "Changing chart type")
    @Description("I verify if changing chart type works properly")
    public void verifyIfChartTypesWorksProperly(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.getChartActionPanel().clickAreaChartType();
            assertTrue(kpiViewPage.isDataSeriesType("area"));
            kpiViewPage.getChartActionPanel().clickBarChartType();
            assertTrue(kpiViewPage.isDataSeriesType("bar"));
            kpiViewPage.getChartActionPanel().clickLineChartType();
            assertTrue(kpiViewPage.isDataSeriesType("line"));
            kpiViewPage.getChartActionPanel().chooseDataSeriesColor();
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
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            kpiViewPage.getKpiToolbar().setValueInTimePeriodChooser(1, 2, 3);
            kpiViewPage.getKpiToolbar().applyChanges();
            kpiViewPage.getKpiToolbar().chooseSmartOptionInTimePeriodChooser();
            kpiViewPage.getKpiToolbar().applyChanges();
            kpiViewPage.getKpiToolbar().chooseLatestOptionInTimePeriodChooser();
            kpiViewPage.getKpiToolbar().applyChanges();
            kpiViewPage.clickLegend();
            assertTrue(kpiViewPage.shouldSeePointsDisplayed(1));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 4, testName = "Enabling Miscellaneous options", description = "Enabling Miscellaneous options")
    @Description("I verify if Miscellaneous options work properly")
    public void verifyIfMiscellaneousOptionsWorkProperly(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME,AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            kpiViewPage.getKpiToolbar().enableDataCompleteness();
            kpiViewPage.getKpiToolbar().applyChanges();
            assertTrue(kpiViewPage.shouldSeeDataCompleteness());

            kpiViewPage.getKpiToolbar().enableShowTimeZone();
            kpiViewPage.getKpiToolbar().applyChanges();
            assertTrue(kpiViewPage.isTimeZoneDisplayed());

            kpiViewPage.getKpiToolbar().enableLastSampleTime();
            assertTrue(kpiViewPage.shouldSeeLastSampleTime(1));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 5, testName = "Enabling Compare with other period option", description = "Enabling Compare with other period option")
    @Description("I verify if Compare with other period option works properly")
    public void verifyIfCompareWithOtherPeriodWorksProperly(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.getKpiToolbar().enableCompareWithOtherPeriod();
            kpiViewPage.getKpiToolbar().applyChanges();
            assertTrue(kpiViewPage.isLegendWithOtherPeriodDisplayed());
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 6, testName = "Verifying resize panel option", description = "Verifying resize panel option")
    @Description("I verify if resize panel option works properly")
    public void verifyIfResizePanelOptionWorksProperly(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.maximizeDataView();
            assertTrue(kpiViewPage.isDataViewMaximized());

            kpiViewPage.minimizeDataView();
            kpiViewPage.maximizeIndicatorsPanel();
            assertTrue(kpiViewPage.isIndicatorsPanelMaximized());

            kpiViewPage.minimizeIndicatorsPanel();
            kpiViewPage.maximizeDimensionsPanel();
            assertTrue(kpiViewPage.isDimensionPanelMaximized());

            kpiViewPage.minimizeDimensionsPanel();
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 7, testName = "Verifying changing layout option", description = "Verifying changing layout option")
    @Description("I verify if changing layout option works properly")
    public void changingChartLayoutOption(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.getKpiToolbar().changeLayout(LAYOUT_1X1);
            assertEquals(kpiViewPage.getKpiToolbar().layoutButtonStatus(LAYOUT_1X1), LAYOUT_EXPECTED_STATUS);
            kpiViewPage.getKpiToolbar().changeLayout(LAYOUT_2X1);
            assertEquals(kpiViewPage.getKpiToolbar().layoutButtonStatus(LAYOUT_2X1), LAYOUT_EXPECTED_STATUS);
            kpiViewPage.getKpiToolbar().changeLayout(LAYOUT_2X2);
            assertEquals(kpiViewPage.getKpiToolbar().layoutButtonStatus(LAYOUT_2X2), LAYOUT_EXPECTED_STATUS);
            kpiViewPage.getKpiToolbar().changeLayout(LAYOUT_4X4);
            assertEquals(kpiViewPage.getKpiToolbar().layoutButtonStatus(LAYOUT_4X4), LAYOUT_EXPECTED_STATUS);
            kpiViewPage.getKpiToolbar().changeLayout(LAYOUT_3X3);
            assertEquals(kpiViewPage.getKpiToolbar().layoutButtonStatus(LAYOUT_3X3), LAYOUT_EXPECTED_STATUS);
            kpiViewPage.getKpiToolbar().changeLayout(LAYOUT_3X2);
            assertEquals(kpiViewPage.getKpiToolbar().layoutButtonStatus(LAYOUT_3X2), LAYOUT_EXPECTED_STATUS);
            kpiViewPage.getKpiToolbar().changeLayout(LAYOUT_AUTO);
            assertEquals(kpiViewPage.getKpiToolbar().layoutButtonStatus(LAYOUT_AUTO), LAYOUT_EXPECTED_STATUS);
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"filterName", "indicator", "dimension"})
    @Test(priority = 8, testName = "Search in indicators and dimensions trees", description = "Verify search from indicators and dimensions trees")
    @Description("Verify search from indicators and dimensions trees")
    public void searchIndicators(
            @Optional("Data Collection Statistics") String filterName,
            @Optional("DBTIME") String indicator,
            @Optional("DC Type: PMSTA_DC") String dimension
    ) {
        kpiViewPage.setFilters(Collections.singletonList(filterName));
        kpiViewPage.searchInToolbarPanel(indicator, INDICATORS_TREE_ID);
        kpiViewPage.searchInToolbarPanel(dimension, DIMENSIONS_TREE_ID);
        kpiViewPage.getKpiToolbar().selectAggregationMethod(OptionsPanel.AggregationMethodOption.SUM);
        kpiViewPage.getKpiToolbar().unselectEveryAggMethodOtherThan(OptionsPanel.AggregationMethodOption.SUM);
        kpiViewPage.getKpiToolbar().applyChanges();

        assertTrue(kpiViewPage.isNodeInTreeSelected(indicator, INDICATORS_TREE_ID));
        assertTrue(kpiViewPage.isNodeInTreeSelected(dimension, DIMENSIONS_TREE_ID));
        assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 9, testName = "Clicking link to chart from chart actions", description = "Clicking link to chart from chart actions")
    @Description("Clicking link to chart from chart actions")
    public void checkLinkToIndicatorsView(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            String activeAggMethod = kpiViewPage.getKpiToolbar().activeAggMethod();
            kpiViewPage.getKpiToolbar().closeOptionsPanel();

            kpiViewPage.getChartActionPanel().clickLinkToChart();

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            assertTrue(kpiViewPage.isNodeInTreeSelected("AQ_TIME 1h " + activeAggMethod, INDICATORS_TREE_ID));
            assertTrue(kpiViewPage.isNodeInTreeSelected(dimensionNodesToSelect, DIMENSIONS_TREE_ID));
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 10, testName = "Sharing view", description = "Sharing view by created link")
    @Description("Sharing view by created link")
    public void sharePanelTest(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
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

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName", "dimensionFolderWithOptions"})
    @Test(priority = 11, testName = "Display Child Objects", description = "Display series for child objects")
    @Description("Display series for child objects")
    public void childObjectTest(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName,
            @Optional("Managed Objects") String dimensionFolderWithOptions
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.clickDimensionOptions(dimensionFolderWithOptions);
            kpiViewPage.fillLevelOfChildObjects("1");
            kpiViewPage.getKpiToolbar().applyChanges();

            assertTrue(kpiViewPage.shouldSeeMoreThanOneCurveDisplayed());
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 12, testName = "Change display type chart/table/pieChart", description = "Change display type chart/table/pieChart")
    @Description("Change display type chart/table/pieChart")
    public void changeDisplayType(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.getKpiToolbar().setDisplayType("Pie Chart");
            assertTrue(kpiViewPage.shouldSeePieChartsDisplayed(1));

            kpiViewPage.getKpiToolbar().setDisplayType("Chart");
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.getKpiToolbar().setDisplayType("Table");
            assertFalse(kpiViewPage.isIndicatorsViewTableEmpty());
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 13, testName = "Check topN Panel", description = "Check topN Panel")
    @Description("Check topN Panel")
    public void checkTopNPanelForDpe(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.getKpiToolbar().clickPerformTopN();

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
    @Test(priority = 14, testName = "Aggregation Method Check", description = "Aggregation Method Check")
    @Description("Aggregation Method Check")
    public void aggregationMethodCheck(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME,AQ_TIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));

            kpiViewPage.getKpiToolbar().selectAggregationMethod(OptionsPanel.AggregationMethodOption.MAX);
            kpiViewPage.getKpiToolbar().selectAggregationMethod(OptionsPanel.AggregationMethodOption.MIN);
            kpiViewPage.getKpiToolbar().selectAggregationMethod(OptionsPanel.AggregationMethodOption.SUM);
            kpiViewPage.getKpiToolbar().unselectEveryAggMethodOtherThan(OptionsPanel.AggregationMethodOption.MAX);
            kpiViewPage.getKpiToolbar().applyChanges();
            String selectedAggMethod = kpiViewPage.getKpiToolbar().activeAggMethod();
            int numOfActiveAggMethods = kpiViewPage.getKpiToolbar().numberOfActiveAggMethods();

            assertEquals(selectedAggMethod, "MAX");
            assertEquals(numOfActiveAggMethods, 1);
        } catch (Exception e) {
            log.error(e.getMessage());
            fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 15, testName = "Zoom in/zoom out check", description = "Zoom in/zoom out check")
    @Description("Zoom in/zoom out check")
    public void zoomInChartCheck(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
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
    @Test(priority = 16, testName = "Table settings: sorting, adding/removing attribute columns", description = "table settings: sorting, adding/removing attribute columns")
    @Description("Table settings: sorting, adding/removing attribute columns")
    public void checkTableSettings(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC,DC Type: PMSTA_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));

            kpiViewPage.getKpiToolbar().setDisplayType("Table");
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

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName", "yAxis"})
    @Test(priority = 16, testName = "Check Manual Y axis", description = "Check Manual Y axis")
    @Description("Check Manual Y axis")
    public void checkYaxisOptions(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName,
            @Optional("[days]") String yAxisName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.getKpiToolbar().selectYaxis(yAxisName);
            kpiViewPage.getKpiToolbar().selectManualYaxisParameters("5", "0", false);
            assertTrue(kpiViewPage.isYaxisValueVisible("5"));
            assertTrue(kpiViewPage.isYaxisValueVisible("0"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 17, testName = "Stacked chart", description = "Check setting to stacked chart")
    @Description("Check setting to stacked chart")
    public void checkStackingChart(
            @Optional("All Self Monitoring,self:DPE Monitoring,self:DPE:DC Indicators") String indicatorNodesToExpand,
            @Optional("LOADED,EXTRACTED") String indicatorNodesToSelect,
            @Optional() String dimensionNodesToExpand,
            @Optional("DC Type: THRES_DC") String dimensionNodesToSelect,
            @Optional("Data Collection Statistics") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));

            kpiViewPage.getChartActionPanel().clickBarChartType();
            kpiViewPage.getChartActionPanel().clickStackedButton();
            assertEquals(kpiViewPage.getChartActionPanel().getStackedButtonTitle(), "Change to 100% stacked chart");

            kpiViewPage.getChartActionPanel().clickStackedButton();
            assertEquals(kpiViewPage.getChartActionPanel().getStackedButtonTitle(), "Change to not stacked chart");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}