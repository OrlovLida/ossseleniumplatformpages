package com.oss.iaa.bigdata.kpiview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.iaa.widgets.dpe.toolbarpanel.ExportPanel;
import com.oss.framework.iaa.widgets.dpe.toolbarpanel.LayoutPanel;
import com.oss.pages.iaa.bigdata.kqiview.KpiViewPage;
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

    @BeforeMethod
    public void goToKpiView() {
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToExpand", "dimensionNodesToSelect", "filterName"})
    @Test(priority = 1, testName = "Check exporting chart", description = "Check exporting chart")
    @Description("Check exporting chart")
    public void checkExport(
            @Optional("DFE Tests,DFE Product Tests,Selenium Tests") String indicatorNodesToExpand,
            @Optional("CPU_NICE_USAGE,CPU_TOTAL_USAGE") String indicatorNodesToSelect,
            @Optional("t:SMOKE#D_HOST") String dimensionNodesToExpand,
            @Optional("192.168.128.172,192.168.128.173") String dimensionNodesToSelect,
            @Optional("Selenium Tests") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            kpiViewPage.getKpiToolbar().exportChart(ExportPanel.ExportType.JPG);
            kpiViewPage.getKpiToolbar().exportChart(ExportPanel.ExportType.PNG);
            kpiViewPage.getKpiToolbar().exportChart(ExportPanel.ExportType.PDF);
            kpiViewPage.getKpiToolbar().exportChart(ExportPanel.ExportType.XLSX);
            kpiViewPage.attachExportedChartToReport();
            attachConsoleLogs(driver);
        } catch (Exception e) {
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
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            kpiViewPage.getKpiToolbar().changeLayout(LayoutPanel.LayoutType.LAYOUT_2X2);
            saveScreenshotPNG(driver);

            kpiViewPage.maximizeDataView();
            saveScreenshotPNG(driver);

            kpiViewPage.minimizeDataView();
            saveScreenshotPNG(driver);
            attachConsoleLogs(driver);
        } catch (Exception e) {
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

            kpiViewPage.getChartActionPanel().clickLinkToChart();
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

            kpiViewPage.getKpiToolbar().setTopNDimension("t:SMOKE#DimHierSelenium");
            kpiViewPage.getKpiToolbar().setTopNLevel("1st");
            kpiViewPage.getKpiToolbar().clickPerformTopN();

            Assert.assertTrue(kpiViewPage.dfeTopNBarChartIsDisplayed());
            Assert.assertTrue(kpiViewPage.isExpectedNumberOfChartsVisible(2));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "dimensionNodesToExpand", "filterName"})
    @Test(priority = 5, testName = "Aggregation on the fly Dim Option", description = "Aggregation on the fly Dim Option")
    @Description("Aggregation on the fly Dim Option")
    public void checkAggOnTheFlyDimOption(
            @Optional("DFE Tests,DFE Product Tests,Selenium Tests") String indicatorNodesToExpand,
            @Optional("SUCCESS_LONG") String indicatorNodesToSelect,
            @Optional("t:SMOKE#DimHierSelenium") String dimensionNodesToExpand,
            @Optional("D3_01,D3_02") String dimensionNodesToSelect,
            @Optional("DFE Tests") String filterName
    ) {
        try {
            kpiViewPage.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);
            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(2));

            kpiViewPage.clickDimensionOptions(dimensionNodesToExpand);
            kpiViewPage.selectDisplaySeriesMode("Aggregation on the Fly");
            kpiViewPage.getKpiToolbar().applyChanges();

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            kpiViewPage.clickDimensionOptions(dimensionNodesToExpand);
            kpiViewPage.selectDisplaySeriesMode("Original Series and Aggregation on the Fly");
            kpiViewPage.getKpiToolbar().applyChanges();

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(3));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }
}