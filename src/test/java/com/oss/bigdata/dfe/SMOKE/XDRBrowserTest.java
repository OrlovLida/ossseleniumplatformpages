package com.oss.bigdata.dfe.SMOKE;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.XDRBrowserPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class XDRBrowserTest extends BaseTestCase {

    private XDRBrowserPage xdrBrowserPage;
    private static final String ETL_NAME = "self:AGGR#AggregationProcesses";

    @BeforeClass
    public void goToXDRBrowserView() {
        xdrBrowserPage = XDRBrowserPage.goToPage(driver, BASIC_URL);
    }


    @Test(priority = 1, testName = "Search in XDR Browser", description = "Search in XDR Browser for ETL")
    @Description("Search in XDR Browser for ETL")
    public void searchInXDRBrowser() {
        xdrBrowserPage.selectETLName(ETL_NAME);
        xdrBrowserPage.setValueInTimePeriodChooser(1, 1, 1);
        xdrBrowserPage.clickSearch();

        Assert.assertFalse(xdrBrowserPage.checkIfTableIsEmpty(), "XDR Table is empty!");
    }

    @Test(priority = 2, testName = "exportXDRFile", description = "Export XDR File")
    @Description("Export XDR File")
    public void exportXDRFile() {
        xdrBrowserPage.clickExport();
        xdrBrowserPage.clickOnNotificationIcon();
        xdrBrowserPage.clickDownload();


    }
}
