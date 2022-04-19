package com.oss.pages.servicedesk.issue.ticket;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.servicedesk.GraphQLSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.URLConstants.VIEWS_URL_PATTERN;

public class MyTicketsPage extends GraphQLSearchPage {

    private static final Logger log = LoggerFactory.getLogger(MyTicketsPage.class);

    private static final String MY_GROUP_TICKETS = "ticket-search?type=my-group";
    private static final String MY_TICKETS_VIEW = "ticket-search?type=my";
    private static final String TABLE_ID = "ticket-search-graphql-table";

    public MyTicketsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open My Group Tickets View")
    public MyTicketsPage goToMyGroupTickets(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, MY_GROUP_TICKETS);
        return new MyTicketsPage(driver, wait);
    }

    @Step("I open My Tickets View")
    public MyTicketsPage goToMyTickets(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, MY_TICKETS_VIEW);
        return new MyTicketsPage(driver, wait);
    }

    @Step("Check if current url leads to {1} page")
    public boolean isMyTicketsPageOpened(String basicURL, String pageType) {
        String pageURL = MY_GROUP_TICKETS;
        if (pageType.equals("My Tickets")) {
            pageURL = MY_TICKETS_VIEW;
        }
        log.info("Current URL is: {}", driver.getCurrentUrl());
        return driver.getCurrentUrl().equals(String.format(VIEWS_URL_PATTERN, basicURL, pageURL));
    }

    @Override
    public TableWidget getIssueTable() {
        return TableWidget.createById(driver, TABLE_ID, wait);
    }
}
