package com.oss.dpe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Listeners({TestListener.class})
public class IndicatorsViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(IndicatorsViewTest.class);

    private static final String HIDDEN_DATA_SERIES_VISIBILITY = "hidden";
    private static final String HIGHLIGHTED_DATA_SERIES_WIDTH = "5px";
    private static final String NORMAL_DATA_SERIES_WIDTH = "2px";

    private KpiViewPage kpiViewPage;

    @BeforeMethod
    public void goToKpiView(){
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect"})
    @Test(priority = 1)
    @Description("I verify if KPI View for DPE data works properly")
    public void verifyIfKpiViewWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect
    ){
        try{
            kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.clickLegend();
            Assert.assertTrue(kpiViewPage.shouldSeeDataSeriesLineWidth(HIGHLIGHTED_DATA_SERIES_WIDTH));
            kpiViewPage.clickLegend();
            Assert.assertTrue(kpiViewPage.shouldNotSeeHiddenLine(HIDDEN_DATA_SERIES_VISIBILITY));
            kpiViewPage.clickLegend();
            Assert.assertTrue(kpiViewPage.shouldSeeDataSeriesLineWidth(NORMAL_DATA_SERIES_WIDTH));
            kpiViewPage.exportChart();
//            kpiViewPage.attachExportedChartToReport();
//            attachConsoleLogs(driver);
        } catch(Exception e){
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect"})
    @Test(priority = 2)
    @Description("I verify if changing chart type works properly")
    public void verifyIfChartTypesWorksProperly(
            @Optional("self:extPM:DC Indicators") String indicatorNodesToExpand,
            @Optional("DBTIME") String indicatorNodesToSelect,
            @Optional("DC Type: ETL_DC") String dimensionNodesToSelect
    ){
        try{
            kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToSelect);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            kpiViewPage.clickAreaChartType();
            Assert.assertTrue(kpiViewPage.shouldSeeAreaChart("0.6"));
            kpiViewPage.clickBarChartType();
            Assert.assertTrue(kpiViewPage.shouldSeeBarChart("0.6"));
            kpiViewPage.clickLineChartType();
            Assert.assertTrue(kpiViewPage.shouldSeeAreaChart("0"));


//            kpiViewPage.clickLegend();
//            Assert.assertTrue(kpiViewPage.shouldSeeDataSeriesLineWidth(HIGHLIGHTED_DATA_SERIES_WIDTH));
//            kpiViewPage.clickLegend();
//            Assert.assertTrue(kpiViewPage.shouldNotSeeHiddenLine(HIDDEN_DATA_SERIES_VISIBILITY));
//            kpiViewPage.clickLegend();
//            Assert.assertTrue(kpiViewPage.shouldSeeDataSeriesLineWidth(NORMAL_DATA_SERIES_WIDTH));

        } catch(Exception e){
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
