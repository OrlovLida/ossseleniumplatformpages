package com.oss.servicedesk;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.platform.NotificationWrapperPage;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.GraphQLSearchPage;
import com.oss.pages.servicedesk.issue.BaseDashboardPage;
import com.oss.pages.servicedesk.issue.ticket.TicketSearchPage;
import com.oss.pages.servicedesk.issue.wizard.ExportWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.DATE_MASK;
import static com.oss.pages.servicedesk.ServiceDeskConstants.DOWNLOAD_FILE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.TICKET_DASHBOARD;

public class ExportTest extends BaseTestCase {

    private BaseDashboardPage baseDashboardPage;
    private TicketSearchPage ticketSearchPage;
    private ExportWizardPage exportWizardPage;
    private NotificationWrapperPage notificationWrapperPage;
    private static int minutes = 60;
    private static final int maxSearchTime6hours = 360;
    private static final String EXPORT_FILE_NAME = "Selenium test " + BaseSDPage.getDateFormat();
    private static final String TT_DOWNLOAD_FILE = "TroubleTicket*.xlsx";
    private static final String EMPTY_SEARCH_FILTER = "";

    @BeforeMethod
    public void goToTicketDashboardPage() {
        baseDashboardPage = new BaseDashboardPage(driver, webDriverWait).goToPage(driver, BASIC_URL, TICKET_DASHBOARD);
    }

    @Test(priority = 1, testName = "Export from Ticket Search View", description = "Export from Ticket Search View")
    @Description("Export from Ticket Search View")
    public void exportFromTicketSearch() {
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait).goToTicketSearchPage(driver, BASIC_URL, EMPTY_SEARCH_FILTER);
        notificationWrapperPage = ticketSearchPage.openNotificationPanel();
        notificationWrapperPage.clearNotifications();
        if (!ticketSearchPage.isIssueTableEmpty()) {
            ticketSearchPage.filterByTextField(GraphQLSearchPage.CREATION_TIME_ATTRIBUTE, ticketSearchPage.getTimePeriodForLastNMinutes(minutes));
            while (ticketSearchPage.isIssueTableEmpty()) {
                minutes += 30;
                if (minutes > maxSearchTime6hours) {
                    Assert.fail("No tickets to export created within last 6 hours");
                }
                ticketSearchPage.filterByTextField(GraphQLSearchPage.CREATION_TIME_ATTRIBUTE, ticketSearchPage.getTimePeriodForLastNMinutes(minutes));
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
        ticketSearchPage = new TicketSearchPage(driver, webDriverWait).goToTicketSearchPage(driver, BASIC_URL, EMPTY_SEARCH_FILTER);
        if (!ticketSearchPage.isIssueTableEmpty()) {
            int ticketsInTable = ticketSearchPage.countIssuesInTable();
            ticketSearchPage.clickRefresh();
            int ticketsAfterRefresh = ticketSearchPage.countIssuesInTable();

            Assert.assertFalse(ticketSearchPage.isIssueTableEmpty());
            Assert.assertTrue(ticketsInTable >= ticketsAfterRefresh);
        } else {
            Assert.fail("No data in ticket search - cannot check refresh function");
        }
    }

    @Test(priority = 3, testName = "Export form Ticket Dashboard", description = "Export form Ticket Dashboard")
    @Description("Export form Ticket Dashboard")
    public void exportFromTicketDashboard() {
        notificationWrapperPage = baseDashboardPage.openNotificationPanel();
        notificationWrapperPage.clearNotifications();
        notificationWrapperPage.close();
        baseDashboardPage.clickExportFromTTTable();
        notificationWrapperPage = baseDashboardPage.openNotificationPanel();
        notificationWrapperPage.waitForExportFinish();
        notificationWrapperPage.clickDownload();
        notificationWrapperPage.waitAndGetFinishedNotificationText();
        notificationWrapperPage.clearNotifications();
        baseDashboardPage.attachFileToReport(TT_DOWNLOAD_FILE);

        Assert.assertEquals(notificationWrapperPage.amountOfNotifications(), 0);
        Assert.assertTrue(baseDashboardPage.checkIfFileIsNotEmpty(TT_DOWNLOAD_FILE));
    }
}
