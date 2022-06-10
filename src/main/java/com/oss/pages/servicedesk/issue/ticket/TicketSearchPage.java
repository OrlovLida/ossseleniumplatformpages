package com.oss.pages.servicedesk.issue.ticket;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.servicedesk.BaseSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.TICKETS_TABLE_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;

public class TicketSearchPage extends BaseSearchPage {

    private static final String TICKET_SEARCH = "ticket-search";

    public TicketSearchPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    @Step("I Open Ticket Search View")
    public TicketSearchPage openView(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, TICKET_SEARCH);
        return this;
    }

    public String getSearchPageUrl() {
        return TICKET_SEARCH;
    }

    public String getIssueType() {
        return TROUBLE_TICKET_ISSUE_TYPE;
    }

    @Override
    public String getTableId() {
        return TICKETS_TABLE_ID;
    }

    public TableWidget getIssueTable() {
        return TableWidget.createById(driver, getTableId(), wait);
    }
}

