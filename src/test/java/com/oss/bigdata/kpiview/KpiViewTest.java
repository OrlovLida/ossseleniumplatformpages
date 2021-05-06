package com.oss.bigdata.kpiview;

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

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect"})
    @Test(priority = 1)
    @Description("I verify if KPI View works properly")
    public void verifyIfKpiViewWorksProperly(
            @Optional("DFE Tests,DFE Product Tests,Selenium Tests") String indicatorNodesToExpand,
            @Optional("CPU_NICE_USAGE,CPU_TOTAL_USAGE") String indicatorNodesToSelect,
            @Optional("t:SMOKE#D_HOST") String dimensionNodesToExpand,
            @Optional("192.168.128.172,192.168.128.173") String dimensionNodesToSelect
    ){
        try{
            kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect);

            //TODO move mouse over point
            kpiViewPage.exportChart();
            kpiViewPage.attachExportedChartToReport();
            attachConsoleLogs(driver);
        } catch(Exception e){
            log.error(e.getMessage());
        }

    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect"})
    @Test(priority = 2)
    @Description("I verify panels resize tool")
    public void verifyPanelsResizeTool(
            @Optional("DFE Tests,DFE Product Tests,Selenium Tests") String indicatorNodesToExpand,
            @Optional("CPU_NICE_USAGE,CPU_TOTAL_USAGE") String indicatorNodesToSelect,
            @Optional("t:SMOKE#D_HOST") String dimensionNodesToExpand,
            @Optional("192.168.128.172,192.168.128.173") String dimensionNodesToSelect
    ){
        try{
            kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect);
            kpiViewPage.changeLayout();
            saveScreenshotPNG(driver);

            kpiViewPage.maximizeChart();
            saveScreenshotPNG(driver);
            kpiViewPage.minimizeChart();
            saveScreenshotPNG(driver);
            attachConsoleLogs(driver);
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect"})
    @Test(priority = 3)
    @Description("I verify a number of ranges and line")
    public void verifyNumberOfRangesAndLine(
            @Optional("DFE Tests,DFE Product Tests,Selenium Tests") String indicatorNodesToExpand,
            @Optional("t:SMOKE#Attempts") String indicatorNodesToSelect,
            @Optional("d1") String dimensionNodesToExpand,
            @Optional("D1_01") String dimensionNodesToSelect
    ){
        try{
            kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect);

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

    private void kpiViewSetup(String indicatorNodesToExpand, String indicatorNodesToSelect, String dimensionNodesToExpand, String dimensionNodesToSelect) {
        kpiViewPage.setFilters(Collections.singletonList("Selenium Tests"));

        List<String> indicatorNodesToExpandList = Arrays.asList(indicatorNodesToExpand.split(","));
        List<String> indicatorNodesToSelectList = Arrays.asList(indicatorNodesToSelect.split(","));
        kpiViewPage.selectIndicator(indicatorNodesToExpandList, indicatorNodesToSelectList);

        List<String> dimensionNodesToExpandList = Arrays.asList(dimensionNodesToExpand.split(","));
        List<String> dimensionNodesToSelectList = Arrays.asList(dimensionNodesToSelect.split(","));
        kpiViewPage.selectDimension(dimensionNodesToExpandList, dimensionNodesToSelectList);

        kpiViewPage.applyChanges();
        kpiViewPage.seeChartIsDisplayed();
    }

}
