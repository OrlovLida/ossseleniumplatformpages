package com.oss.bigdata.dfe.SMOKE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.XDRBrowserPage;
import com.oss.pages.platform.NotificationWrapperPage;

import io.qameta.allure.Description;

public class XDRBrowserTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(XDRBrowserTest.class);

    private XDRBrowserPage xdrBrowserPage;
    private NotificationWrapperPage notificationWrapperPage;
    private static final String ETL_NAME = "t:SMOKE#ETLforKqis";
    private static final String DOWNLOADED_FILE = "*.csv";

    @BeforeClass
    public void goToXDRBrowserView() {
        xdrBrowserPage = XDRBrowserPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Search in XDR Browser", description = "Search in XDR Browser for ETL")
    @Description("Search in XDR Browser for ETL")
    public void searchInXDRBrowser() {
        xdrBrowserPage.selectETLName(ETL_NAME);
        xdrBrowserPage.setValueInTimePeriodChooser(3, 1, 1);
        xdrBrowserPage.clickSearch();

        Assert.assertFalse(xdrBrowserPage.checkIfTableIsEmpty(), "XDR Table is empty!");
    }

    @Test(priority = 2, testName = "exportXDRFile", description = "Export XDR File")
    @Description("Export XDR File")
    public void exportXDRFile() {
        if (!xdrBrowserPage.checkIfTableIsEmpty()) {
            notificationWrapperPage = new NotificationWrapperPage(driver);
            notificationWrapperPage.clearNotifications();
            xdrBrowserPage.clickExport();
            notificationWrapperPage.openNotificationPanel().waitForExportFinish();

            Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 1);
            notificationWrapperPage.clickDownload();
            xdrBrowserPage.attachFileToReport(DOWNLOADED_FILE);
            notificationWrapperPage.clearNotifications();

            Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
            Assert.assertTrue(xdrBrowserPage.checkIfFileIsNotEmpty(DOWNLOADED_FILE));
        } else {
            log.error("ETL with name: {} has no data in XDR Table", ETL_NAME);
            Assert.fail();
        }
    }
}
