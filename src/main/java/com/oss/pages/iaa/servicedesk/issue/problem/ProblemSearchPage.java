package com.oss.pages.iaa.servicedesk.issue.problem;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.pages.iaa.servicedesk.BaseSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.PROBLEMS_TABLE_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.PROBLEM_ISSUE_TYPE;

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
    public String getTableId() {
        return PROBLEMS_TABLE_ID;
    }
}

