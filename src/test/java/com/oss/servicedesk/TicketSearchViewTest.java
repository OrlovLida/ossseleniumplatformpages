package com.oss.servicedesk;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.TicketSearchPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class TicketSearchViewTest extends BaseTestCase {

    private TicketSearchPage ticketSearchPage;

    @BeforeClass
    public void goToTicketSearchView() {
        ticketSearchPage = TicketSearchPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Open Ticket Details", description = "Open Ticket Details")
    @Description("Open Ticket Details")
    public void openTicketDetails() {
        String ticketId = ticketSearchPage.geIdForNthTicketInTable(0);
        ticketSearchPage.checkIfTicketExists(TicketSearchPage.ID_ATTRIBUTE, ticketId);
        ticketSearchPage.openTicketDetailsView("0", BASIC_URL);
    }

}
