package com.oss.bigdata.kpiview;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.oss.utils.AttachmentsManager.attachConsoleLogs;
import static com.oss.utils.AttachmentsManager.saveScreenshotPNG;

@Listeners({TestListener.class})
public class KpiViewTest extends BaseTestCase {

    private KpiViewPage kpiViewPage;

    @BeforeMethod
    public void goToKpiView(){
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect"})
    @Test(priority = 1)
    @Description("I verify if KPI View works properly")
    public void verifyIfKpiViewWorksProperly(String indicatorNodesToExpand, String indicatorNodesToSelect, String dimensionNodesToExpand, String dimensionNodesToSelect){
        kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect);

        //TODO move mouse over point
        kpiViewPage.exportChart();
        kpiViewPage.attachExportedChartToReport();
        attachConsoleLogs(driver);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect"})
    @Test(priority = 2)
    @Description("I verify panels resize tool")
    public void verifyPanelsResizeTool(String indicatorNodesToExpand, String indicatorNodesToSelect, String dimensionNodesToExpand, String dimensionNodesToSelect){
        kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect);

        kpiViewPage.changeLayout();
        saveScreenshotPNG(driver);

        kpiViewPage.maximizeChart();
        saveScreenshotPNG(driver);
        kpiViewPage.minimizeChart();
        saveScreenshotPNG(driver);
        attachConsoleLogs(driver);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect"})
    @Test(priority = 3)
    @Description("I verify a number of ranges and line")
    public void verifyNumberOfRangesAndLine(String indicatorNodesToExpand, String indicatorNodesToSelect, String dimensionNodesToExpand, String dimensionNodesToSelect){
        kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect);

        kpiViewPage.setTopNOptions("d1");
        Assert.assertTrue(kpiViewPage.shouldSeeBoxesAndCurvesDisplayed(1,1));

        kpiViewPage.setTopNOptions("d2");
        Assert.assertTrue(kpiViewPage.shouldSeeBoxesAndCurvesDisplayed(2,2));

        kpiViewPage.setTopNOptions("t:SMOKE#DimHierSelenium", "3");
        Assert.assertTrue(kpiViewPage.shouldSeeBoxesAndCurvesDisplayed(6,5));
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
