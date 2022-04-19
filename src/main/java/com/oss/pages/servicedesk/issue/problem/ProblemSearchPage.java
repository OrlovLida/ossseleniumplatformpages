package com.oss.pages.servicedesk.issue.problem;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.servicedesk.GraphQLSearchPage;

public class ProblemSearchPage extends GraphQLSearchPage {

    private static final String TABLE_ID = "problem-search-graphql-table";

    public ProblemSearchPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    public TableWidget getIssueTable() {
        return TableWidget.createById(driver, TABLE_ID, wait);
    }
}

