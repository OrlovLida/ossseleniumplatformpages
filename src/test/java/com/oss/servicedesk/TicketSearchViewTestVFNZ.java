package com.oss.servicedesk;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.ticket.TicketSearchPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.format.DateTimeFormatter;

@Listeners({TestListener.class})
public class TicketSearchViewTestVFNZ extends BaseTestCase {

    private TicketSearchPage ticketSearchPage;

    private static final String TT_ASSIGNEE = "ca_kodrobinska";
    public static final String TT_DESCRIPTION = "TestSelenium";

    private static final String CREATE_DATE_FILTER_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter CREATE_DATE_FILTER_DATE_FORMATTER = DateTimeFormatter.ofPattern(CREATE_DATE_FILTER_DATE_PATTERN);

    @BeforeClass
    public void goToTicketSearchView() {
        ticketSearchPage = new TicketSearchPage(driver);
        ticketSearchPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Open Ticket Details from Tickets Search view", description = "Open Ticket Details from Tickets Search view")
    @Description("Open Ticket Details from Tickets Search view")
    public void openTicketDetailsFromTicketsSearchView() {
        ticketSearchPage.clickFilterButton();
        ticketSearchPage.filterByTextField(TicketSearchPage.ASSIGNEE_ATTRIBUTE, TT_ASSIGNEE);
//      TODO Do odkomentowania po naprawieniu aktualizacji widoku Ticket Search (OSSSD-2605)
//        String date = LocalDateTime.now().minusMinutes(10).format(CREATE_DATE_FILTER_DATE_FORMATTER);
//        ticketSearchPage.clickFilterButton();
//        ticketSearchPage.filterByTextField(TicketSearchPage.CREATION_TIME_ATTRIBUTE, date);
        ticketSearchPage.clickFilterButton();
        ticketSearchPage.filterByTextField(TicketSearchPage.DESCRIPTION_ATTRIBUTE, TT_DESCRIPTION);
        ticketSearchPage.clickFilterButton();
        ticketSearchPage.filterByComboBox(TicketSearchPage.STATUS_ATTRIBUTE, "New");
        ticketSearchPage.openTicketDetailsView("0", BASIC_URL);
    }
}
