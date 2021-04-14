package com.oss.bigdata.kpiview;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
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

    @Test(priority = 1)
    @Description("I verify if KPI View works properly")
    public void verifyIfKpiViewWorksProperly(){
        kpiViewSetup();

        kpiViewPage.exportChart();
        kpiViewPage.attachExportedChartToReport();
        attachConsoleLogs(driver);
    }

    @Test(priority = 2)
    @Description("I verify panels resize tool")
    public void verifyPanelsResizeTool(){
        kpiViewSetup();

        kpiViewPage.changeLayout();
        saveScreenshotPNG(driver);

        kpiViewPage.maximizeChart();
        saveScreenshotPNG(driver);
        kpiViewPage.minimizeChart();
        saveScreenshotPNG(driver);
        attachConsoleLogs(driver);
    }

    private void kpiViewSetup() {
        kpiViewPage.setFilters(Collections.singletonList("Selenium Tests"));

        List<String> indicatorNodesToExpand = Arrays.asList("DFE Tests,DFE Product Tests,Selenium Tests".split(","));
        List<String> indicatorNodesToSelect = Arrays.asList("CPU_NICE_USAGE,CPU_TOTAL_USAGE".split(","));
        kpiViewPage.selectIndicator(indicatorNodesToExpand, indicatorNodesToSelect);

        List<String> dimensionNodesToExpand = Arrays.asList("t:SMOKE#D_HOST".split(","));
        List<String> dimensionNodesToSelect = Arrays.asList("192.168.128.172,192.168.128.173".split(","));
        kpiViewPage.selectDimension(dimensionNodesToExpand, dimensionNodesToSelect);

        kpiViewPage.applyChanges();
        kpiViewPage.seeChartIsDisplayed();
    }

}
