package com.oss.pages.servicedesk.task;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.servicedesk.GraphQLSearchPage;

import io.qameta.allure.Step;

public class MyTasksPage extends GraphQLSearchPage {

    private static final Logger log = LoggerFactory.getLogger(MyTasksPage.class);

    private static final String MY_TASKS = "problem-search?type=my-tasks";
    private static final String TABLE_WIDGET_ID = "problem-search-graphql-table";

    public MyTasksPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open My Tasks View")
    public MyTasksPage goToPage(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, MY_TASKS);
        log.info("My Tasks View is opened");
        return new MyTasksPage(driver, wait);
    }

    public TableWidget getIssueTable() {
        return TableWidget.createById(driver, TABLE_WIDGET_ID, wait);
    }
}

