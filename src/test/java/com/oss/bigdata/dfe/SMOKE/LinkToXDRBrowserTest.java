package com.oss.bigdata.dfe.SMOKE;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.XDRBrowserPage;
import com.oss.pages.bigdata.kqiview.ChartActionsPanelPage;
import com.oss.pages.bigdata.kqiview.KpiViewPage;
import com.oss.pages.bigdata.kqiview.KpiViewSetupPage;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LinkToXDRBrowserTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(LinkToXDRBrowserTest.class);
    private static final String ETL_NAME = "t:SMOKE#ETLforKqis";
    private static final String ETL_IN_XDR_FILTER = "t:SMOKE#DimHierSelenium.MAIN_TYPE";
    private static final String DIMENSION_IN_XDR_FILTER = "D3_01";

    private KpiViewPage kpiViewPage;
    private KpiViewSetupPage kpiViewSetup;
    private ChartActionsPanelPage chartActionsPanel;

    @Parameters({"kpiViewType"})
    @BeforeClass
    public void goToKpiView(
            @Optional("INDICATORS_VIEW") KpiViewPage.KpiViewType kpiViewType
    ) {
        kpiViewPage = KpiViewPage.goToPage(driver, BASIC_URL, kpiViewType);
        kpiViewSetup = new KpiViewSetupPage(driver, webDriverWait);
        chartActionsPanel = new ChartActionsPanelPage(driver, webDriverWait);
    }

    @Parameters({"indicatorNodesToExpand", "indicatorNodesToSelect", "dimensionNodesToSelect", "dimensionNodesToExpand", "filterName"})
    @Test(priority = 1, testName = "Link to XDR Browser", description = "Opening Link to XDR Browser from Indicators View")
    @Description("Opening Link to XDR Browser from Indicators View")
    public void checkLinkToXDR(
            @Optional("DFE Tests,DFE Product Tests,Selenium Tests") String indicatorNodesToExpand,
            @Optional("SUCCESS_LONG") String indicatorNodesToSelect,
            @Optional("t:SMOKE#DimHierSelenium") String dimensionNodesToExpand,
            @Optional("D3_01") String dimensionNodesToSelect,
            @Optional("DFE Tests") String filterName
    ) {
        try {
            kpiViewSetup.kpiViewSetup(indicatorNodesToExpand, indicatorNodesToSelect, dimensionNodesToExpand, dimensionNodesToSelect, filterName);

            Assert.assertTrue(kpiViewPage.shouldSeeCurvesDisplayed(1));

            chartActionsPanel.clickLinkToXDRBrowser();
            XDRBrowserPage xdrBrowserPage = new XDRBrowserPage(driver, webDriverWait);

            Assert.assertEquals(xdrBrowserPage.getETLName(), ETL_NAME);
            Assert.assertTrue(xdrBrowserPage.checkIfFilterExist(ETL_IN_XDR_FILTER));
            Assert.assertTrue(xdrBrowserPage.checkIfFilterExist(DIMENSION_IN_XDR_FILTER));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assert.fail();
        }
    }
}