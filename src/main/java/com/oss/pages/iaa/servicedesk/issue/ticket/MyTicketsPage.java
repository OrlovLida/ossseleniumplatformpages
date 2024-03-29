package com.oss.pages.iaa.servicedesk.issue.ticket;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.pages.iaa.servicedesk.BaseSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TICKETS_TABLE_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;

public class MyTicketsPage extends BaseSearchPage {

    private static final String MY_TICKETS_VIEW = "ticket-search?type=my";

    public MyTicketsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    @Step("Open My Tickets View")
    public MyTicketsPage openView(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, MY_TICKETS_VIEW);
        return this;
    }

    public String getSearchPageUrl() {
        return MY_TICKETS_VIEW;
    }

    public String getIssueType() {
        return TROUBLE_TICKET_ISSUE_TYPE;
    }

    @Override
    public String getTableId() {
        return TICKETS_TABLE_ID;
    }
}
