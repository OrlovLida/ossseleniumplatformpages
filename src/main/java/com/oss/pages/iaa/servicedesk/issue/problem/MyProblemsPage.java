package com.oss.pages.iaa.servicedesk.issue.problem;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.iaa.servicedesk.BaseSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.PROBLEMS_TABLE_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.PROBLEM_ISSUE_TYPE;

public class MyProblemsPage extends BaseSearchPage {

    private static final String MY_PROBLEMS_VIEW = "problem-search?type=my";

    public MyProblemsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    @Step("I open My Problems View")
    public MyProblemsPage openView(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, MY_PROBLEMS_VIEW);
        return this;
    }

    public String getSearchPageUrl() {
        return MY_PROBLEMS_VIEW;
    }

    public String getIssueType() {
        return PROBLEM_ISSUE_TYPE;
    }

    @Override
    public String getTableId() {
        return PROBLEMS_TABLE_ID;
    }

    @Override
    public TableWidget getIssueTable() {
        return TableWidget.createById(driver, getTableId(), wait);
    }
}

