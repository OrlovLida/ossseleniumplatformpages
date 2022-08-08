package com.oss.pages.iaa.servicedesk.changemanagement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.pages.iaa.servicedesk.BaseSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.CHANGES_TABLE_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.CHANGE_ISSUE_TYPE;

public class MyChangesPage extends BaseSearchPage {

    private static final String MY_CHANGES_VIEW = "change-search?type=my";

    public MyChangesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    @Step("I open My Changes View")
    public MyChangesPage openView(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, MY_CHANGES_VIEW);
        return this;
    }

    public String getSearchPageUrl() {
        return MY_CHANGES_VIEW;
    }

    public String getIssueType() {
        return CHANGE_ISSUE_TYPE;
    }

    @Override
    public String getTableId() {
        return CHANGES_TABLE_ID;
    }
}

