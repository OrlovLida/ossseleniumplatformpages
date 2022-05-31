package com.oss.pages.servicedesk.issue.problem;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.servicedesk.BaseSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.PROBLEMS_TABLE_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.PROBLEM_ISSUE_TYPE;

public class ProblemSearchPage extends BaseSearchPage {

    private static final String PROBLEMS_SEARCH = "problem-search";

    public ProblemSearchPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    @Step("I open Problems Search View")
    public ProblemSearchPage openView(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, PROBLEMS_SEARCH);
        return this;
    }

    public String getSearchPageUrl() {
        return PROBLEMS_SEARCH;
    }

    public String getIssueType() {
        return PROBLEM_ISSUE_TYPE;
    }

    @Override
    public TableWidget getIssueTable() {
        return TableWidget.createById(driver, PROBLEMS_TABLE_ID, wait);
    }
}

