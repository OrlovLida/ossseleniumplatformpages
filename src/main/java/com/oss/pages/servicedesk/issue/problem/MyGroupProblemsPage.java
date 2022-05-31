package com.oss.pages.servicedesk.issue.problem;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.servicedesk.BaseSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.PROBLEMS_TABLE_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.PROBLEM_ISSUE_TYPE;

public class MyGroupProblemsPage extends BaseSearchPage {

    private static final String MY_GROUP_PROBLEMS = "problem-search?type=my-group";

    public MyGroupProblemsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    @Step("I open My Group Problems View")
    public MyGroupProblemsPage openView(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, MY_GROUP_PROBLEMS);
        return this;
    }

    public String getSearchPageUrl() {
        return MY_GROUP_PROBLEMS;
    }

    public String getIssueType() {
        return PROBLEM_ISSUE_TYPE;
    }

    @Override
    public TableWidget getIssueTable() {
        return TableWidget.createById(driver, PROBLEMS_TABLE_ID, wait);
    }
}

