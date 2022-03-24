package com.oss.servicedesk;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.platform.NotificationWrapperPage;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.ticket.TicketDashboardPage;
import com.oss.pages.servicedesk.ticket.TicketSearchPage;
import com.oss.pages.servicedesk.ticket.wizard.ExportWizardPage;

import io.qameta.allure.Description;

public class ExportTest extends BaseTestCase {

    private TicketDashboardPage ticketDashboardPage;
    private TicketSearchPage ticketSearchPage;
    private ExportWizardPage exportWizardPage;
    private NotificationWrapperPage notificationWrapperPage;
    private static int minutes = 60;
    private static final int maxSearchTime6hours = 360;
    private static final String EXPORT_FILE_NAME = "Selenium test " + BaseSDPage.getDateFormat();
    private static final String DATE_MASK = "ISO Local Date";
    private static final String DOWNLOAD_FILE = "*.csv";

    @BeforeMethod
    public void goToTicketDashboardPage() {
        ticketDashboardPage = TicketDashboardPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Export from Ticket Search View", description = "Export from Ticket Search View")
    @Description("Export from Ticket Search View")
    public void exportFromTicketSearch() {
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait);
        ticketSearchPage.goToPage(driver, BASIC_URL);
        notificationWrapperPage = ticketSearchPage.openNotificationPanel();
        notificationWrapperPage.clearNotifications();
        if (!ticketSearchPage.isTicketSearchTableEmpty()) {
            ticketSearchPage.filterByTextField(TicketSearchPage.CREATION_TIME_ATTRIBUTE, ticketSearchPage.getTimePeriodForLastNMinutes(minutes));
            while (ticketSearchPage.isTicketSearchTableEmpty()) {
                minutes += 30;
                if (minutes > maxSearchTime6hours) {
                    Assert.fail("No tickets to export created within last 6 hours");
                }
                ticketSearchPage.filterByTextField(TicketSearchPage.CREATION_TIME_ATTRIBUTE, ticketSearchPage.getTimePeriodForLastNMinutes(minutes));
            }
            exportWizardPage = ticketSearchPage.clickExportInTicketSearch();
            exportWizardPage.fillFileName(EXPORT_FILE_NAME);
            exportWizardPage.fillDateMask(DATE_MASK);
            exportWizardPage.clickAccept();
            ticketSearchPage = new TicketSearchPage(driver, webDriverWait);
            notificationWrapperPage = ticketSearchPage.openNotificationPanel();
            notificationWrapperPage.waitForExportFinish();
            notificationWrapperPage.clickDownload();
            notificationWrapperPage.clearNotifications();
            ticketSearchPage.attachFileToReport(DOWNLOAD_FILE);

            Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
            Assert.assertTrue(ticketSearchPage.checkIfFileIsNotEmpty(DOWNLOAD_FILE));
        } else {
            Assert.fail("Ticket Search Table is Empty - no tickets to Export");
        }
    }

    @Test(priority = 2, testName = "Refresh test", description = "Click on refresh button and check if data is visible")
    @Description("Click on refresh button and check if data is visible")
    public void refreshOnTicketSearchTest() {
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait);
        ticketSearchPage.goToPage(driver, BASIC_URL);
        if (!ticketSearchPage.isTicketSearchTableEmpty()) {
            int ticketsInTable = ticketSearchPage.countTicketsInTable();
            ticketSearchPage.clickRefresh();
            int ticketsAfterRefresh = ticketSearchPage.countTicketsInTable();

            Assert.assertTrue(!ticketSearchPage.isTicketSearchTableEmpty());
            Assert.assertTrue(ticketsInTable >= ticketsAfterRefresh);
        } else {
            Assert.fail("No data in ticket search - cannot check refresh function");
        }
    }
}
