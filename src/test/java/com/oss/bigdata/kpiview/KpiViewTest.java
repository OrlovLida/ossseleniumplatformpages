package com.oss.bigdata.kpiview;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.oss.utils.AttachmentsManager.attachConsoleLogs;
import static com.oss.utils.AttachmentsManager.saveScreenshotPNG;

@Listeners({TestListener.class})
public class KpiViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(KpiViewTest.class);
    private KpiViewPage kpiViewPage;

    @BeforeMethod
    public void goToKpiView(){
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 1)
    @Description("I verify if KPI View works properly")
    public void verifyIfKpiViewWorksProperly(
            @Optional("DFE Tests,DFE Product Tests,Selenium Tests") String indicatorNodesToExpand,
            @Optional("CPU_NICE_USAGE,CPU_TOTAL_USAGE") String indicatorNodesToSelect,
            @Optional("t:SMOKE#D_HOST") String dimensionNodesToExpand,
            @Optional("192.168.128.172,192.168.128.173") String dimensionNodesToSelect,
            @Optional("Selenium Tests") String filterName
    ){
        try{
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

            //TODO move mouse over point
            kpiViewPage.exportChart();
            kpiViewPage.attachExportedChartToReport();
            attachConsoleLogs(driver);
        } catch(Exception e){
            log.error(e.getMessage());
        }

    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 2)
    @Description("I verify panels resize tool")
    public void verifyPanelsResizeTool(
            @Optional("DFE Tests,DFE Product Tests,Selenium Tests") String indicatorNodesToExpand,
            @Optional("CPU_NICE_USAGE,CPU_TOTAL_USAGE") String indicatorNodesToSelect,
            @Optional("t:SMOKE#D_HOST") String dimensionNodesToExpand,
            @Optional("192.168.128.172,192.168.128.173") String dimensionNodesToSelect,
            @Optional("Selenium Tests") String filterName
    ){
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            kpiViewPage.changeLayout();
            saveScreenshotPNG(driver);

            kpiViewPage.maximizeDataView();
            saveScreenshotPNG(driver);

            kpiViewPage.minimizeDataView();
            saveScreenshotPNG(driver);
            attachConsoleLogs(driver);
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 3)
    @Description("I verify a number of ranges and line")
    public void verifyNumberOfRangesAndLine(
            @Optional("DFE Tests,DFE Product Tests,Selenium Tests") String indicatorNodesToExpand,
            @Optional("t:SMOKE#Attempts") String indicatorNodesToSelect,
            @Optional("d1") String dimensionNodesToExpand,
            @Optional("D1_01") String dimensionNodesToSelect,
            @Optional("Selenium Tests") String filterName
    ){
        try{
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

            kpiViewPage.setTopNOptions("d1");
            Assert.assertTrue(kpiViewPage.shouldSeeBoxesAndCurvesDisplayed(1,1));

            kpiViewPage.setTopNOptions("d2");
            Assert.assertTrue(kpiViewPage.shouldSeeBoxesAndCurvesDisplayed(2,2));

            kpiViewPage.setTopNOptions("t:SMOKE#DimHierSelenium", "3");
            Assert.assertTrue(kpiViewPage.shouldSeeBoxesAndCurvesDisplayed(6,5));
        } catch(Exception e){
            log.error(e.getMessage());
        }

    }
}
