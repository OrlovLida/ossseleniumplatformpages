package com.oss.pages.iaa.servicedesk.issue.ticket;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.pages.iaa.servicedesk.BaseSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TICKETS_TABLE_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;

public class MyGroupTicketsPage extends BaseSearchPage {

    private static final String MY_GROUP_TICKETS = "ticket-search?type=my-group";

    public MyGroupTicketsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    @Step("I open My Group Tickets View")
    public MyGroupTicketsPage openView(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, MY_GROUP_TICKETS);
        return this;
    }

    public String getSearchPageUrl() {
        return MY_GROUP_TICKETS;
    }

    public String getIssueType() {
        return TROUBLE_TICKET_ISSUE_TYPE;
    }

    @Override
    public String getTableId() {
        return TICKETS_TABLE_ID;
    }
}

