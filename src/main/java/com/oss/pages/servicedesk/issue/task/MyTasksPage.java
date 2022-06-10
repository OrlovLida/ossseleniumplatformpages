package com.oss.pages.servicedesk.issue.task;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.pages.servicedesk.BaseSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.PROBLEMS_TABLE_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.PROBLEM_ISSUE_TYPE;

public class MyTasksPage extends BaseSearchPage {

    private static final String MY_TASKS = "problem-search?type=my-tasks";

    public MyTasksPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    @Step("I Open My Tasks View")
    public MyTasksPage openView(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, MY_TASKS);
        return this;
    }

    public String getSearchPageUrl() {
        return MY_TASKS;
    }

    public String getIssueType() {
        return PROBLEM_ISSUE_TYPE;
    }

    @Override
    public String getTableId() {
        return PROBLEMS_TABLE_ID;
    }
}
