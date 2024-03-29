package com.oss.pages.iaa.servicedesk.changemanagement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.pages.iaa.servicedesk.BaseSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.CHANGES_TABLE_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.CHANGE_ISSUE_TYPE;

public class ChangeSearchPage extends BaseSearchPage {

    private static final String CHANGES_SEARCH = "change-search";

    public ChangeSearchPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    @Step("I open Changes Search View")
    public ChangeSearchPage openView(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, CHANGES_SEARCH);
        return this;
    }

    public String getSearchPageUrl() {
        return CHANGES_SEARCH;
    }

    public String getIssueType() {
        return CHANGE_ISSUE_TYPE;
    }

    @Override
    public String getTableId() {
        return CHANGES_TABLE_ID;
    }
}

