package com.oss.pages.iaa.servicedesk.issue.problem;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.pages.iaa.servicedesk.BaseSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.PROBLEMS_TABLE_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.PROBLEM_ISSUE_TYPE;

public class ClosedProblemsPage extends BaseSearchPage {

    private static final String CLOSED_PROBLEMS = "problem-search?type=closed";

    public ClosedProblemsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    @Step("I open Closed Problems View")
    public ClosedProblemsPage openView(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, CLOSED_PROBLEMS);
        return this;
    }

    public String getSearchPageUrl() {
        return CLOSED_PROBLEMS;
    }

    public String getIssueType() {
        return PROBLEM_ISSUE_TYPE;
    }

    @Override
    public String getTableId() {
        return PROBLEMS_TABLE_ID;
    }
}

