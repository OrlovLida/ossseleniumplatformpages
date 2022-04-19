package com.oss.pages.servicedesk.issue.ticket;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.servicedesk.GraphQLSearchPage;
import com.oss.pages.servicedesk.issue.IssueDetailsPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;
import static com.oss.pages.servicedesk.issue.IssueDetailsPage.DETAILS_PAGE_URL_PATTERN;

public class TicketSearchPage extends GraphQLSearchPage {

    private static final Logger log = LoggerFactory.getLogger(TicketSearchPage.class);

    private static final String TABLE_WIDGET_ID = "ticket-search-graphql-table";
    private static final String TICKET_SEARCH = "ticket-search";

    public TicketSearchPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open Ticket Search View")
    public TicketSearchPage goToTicketSearchPage(WebDriver driver, String basicURL, String searchViewName) {
        goToPage(driver, basicURL, TICKET_SEARCH + searchViewName);
        log.info("Ticket Search View is opened");
        return new TicketSearchPage(driver, wait);
    }

    @Step("I open details view for {rowIndex} ticket in Ticket table")
    public IssueDetailsPage openTicketDetailsView(String rowIndex, String basicURL) {
        String ticketId = getIssueTable().getCellValue(Integer.parseInt(rowIndex), ID_ATTRIBUTE);
        openPage(driver, String.format(DETAILS_PAGE_URL_PATTERN, basicURL, "trouble-ticket", ticketId));
        return new IssueDetailsPage(driver, wait);
    }

    public TableWidget getIssueTable() {
        return TableWidget.createById(driver, TABLE_WIDGET_ID, wait);
    }
}

