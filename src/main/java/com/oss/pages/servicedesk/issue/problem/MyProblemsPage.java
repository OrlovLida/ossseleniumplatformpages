package com.oss.pages.servicedesk.issue.problem;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.servicedesk.GraphQLSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.URLConstants.VIEWS_URL_PATTERN;

public class MyProblemsPage extends GraphQLSearchPage {

    private static final Logger log = LoggerFactory.getLogger(MyProblemsPage.class);

    private static final String MY_GROUP_PROBLEMS = "problem-search?type=my-group";
    private static final String MY_PROBLEMS_VIEW = "problem-search?type=my";
    private static final String TABLE_ID = "table-problem-search-graphql-table";

    public MyProblemsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open My Group Problems View")
    public MyProblemsPage goToMyGroupProblems(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, MY_GROUP_PROBLEMS);
        return new MyProblemsPage(driver, wait);
    }

    @Step("I open My Problems View")
    public MyProblemsPage goToMyProblems(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, MY_PROBLEMS_VIEW);
        return new MyProblemsPage(driver, wait);
    }

    @Step("Check if current url leads to {1} page")
    public boolean isMyProblemPageOpened(String basicURL, String pageType) {
        String pageURL = MY_GROUP_PROBLEMS;
        if (pageType.equals("My Problems")) {
            pageURL = MY_PROBLEMS_VIEW;
        }
        log.info("Current URL is: {}", driver.getCurrentUrl());
        return driver.getCurrentUrl().equals(String.format(VIEWS_URL_PATTERN, basicURL, pageURL));
    }

    @Override
    public TableWidget getIssueTable() {
        return TableWidget.createById(driver, TABLE_ID, wait);
    }
}

