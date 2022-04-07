package com.oss.bigdata.kpiview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

import static com.oss.utils.AttachmentsManager.attachConsoleLogs;
import static com.oss.utils.AttachmentsManager.saveScreenshotPNG;

@Listeners({TestListener.class})
public class KpiViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(KpiViewTest.class);
    private KpiViewPage kpiViewPage;

    private static final String INDICATORS_TREE_ID = "_Indicators";
    private static final String DIMENSIONS_TREE_ID = "_Dimensions";

    @Parameters({"kpiViewType"})
    @BeforeMethod
    public void goToKpiView(
            @Optional("INDICATORS_VIEW") KpiViewPage.KpiViewType kpiViewType
    ) {
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL, kpiViewType);
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
            kpiViewPage.exportChart();
            kpiViewPage.attachExportedChartToReport();
            attachConsoleLogs(driver);
        } catch(Exception e){
            log.error(e.getMessage());
            Assert.fail();
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
            kpiViewPage.changeLayout(LayoutPanel.LayoutType.LAYOUT_2x2);
            saveScreenshotPNG(driver);

            kpiViewPage.maximizeDataView();
            saveScreenshotPNG(driver);

            kpiViewPage.minimizeDataView();
            saveScreenshotPNG(driver);
            attachConsoleLogs(driver);
        } catch (Exception e){
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "dimensionNodesToExpand", "filterName"})
    @Test(priority = 3, testName = "Link to Indicators View - chart from chart actions", description = "Opening Link to Indicators View - chart from chart actions")
    @Description("Opening Link to Indicators View - chart from chart actions")
    public void checkLinkToKPIViewChart(
            @Optional("DFE Tests,DFE Product Tests,Selenium Tests") String indicatorNodesToExpand,
            @Optional("SUCCESS_LONG") String indicatorNodesToSelect,
            @Optional("t:SMOKE#DimHierSelenium") String dimensionNodesToExpand,
            @Optional("D3_01") String dimensionNodesToSelect,
            @Optional("DFE Tests") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.clickLinkToChart();
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));
            Assert.assertTrue(kpiViewPage.isNodeInTreeSelected(indicatorNodesToSelect, INDICATORS_TREE_ID));
            Assert.assertTrue(kpiViewPage.isNodeInTreeSelected(dimensionNodesToSelect, DIMENSIONS_TREE_ID));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "dimensionNodesToExpand", "filterName"})
    @Test(priority = 4, testName = "Check topN Panel for DFE data", description = "Check topN Panel for DFE data")
    @Description("Check topN Panel for DFE data")
    public void checkTopNPanelForDfe(
            @Optional("DFE Tests,DFE Product Tests,Selenium Tests") String indicatorNodesToExpand,
            @Optional("SUCCESS_LONG") String indicatorNodesToSelect,
            @Optional("t:SMOKE#DimHierSelenium") String dimensionNodesToExpand,
            @Optional("D3_01") String dimensionNodesToSelect,
            @Optional("DFE Tests") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.setTopNDimension("t:SMOKE#DimHierSelenium");
            kpiViewPage.setTopNLevel("1st");
            kpiViewPage.clickPerformTopN();

            Assert.assertTrue(kpiViewPage.dfeTopNBarChartIsDisplayed());
            Assert.assertTrue(kpiViewPage.isExpectedNumberOfChartsVisible(2));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }
}