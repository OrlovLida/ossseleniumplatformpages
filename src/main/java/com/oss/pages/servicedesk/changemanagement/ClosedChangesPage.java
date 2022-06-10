package com.oss.pages.servicedesk.changemanagement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.pages.servicedesk.BaseSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.CHANGES_TABLE_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.CHANGE_ISSUE_TYPE;

public class ClosedChangesPage extends BaseSearchPage {

    private static final String CLOSED_CHANGES = "change-search?type=closed";

    public ClosedChangesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    @Step("I open Closed Changes View")
    public ClosedChangesPage openView(WebDriver driver, String basicURL) {
        goToPage(driver, basicURL, CLOSED_CHANGES);
        return this;
    }

    public String getSearchPageUrl() {
        return CLOSED_CHANGES;
    }

    public String getIssueType() {
        return CHANGE_ISSUE_TYPE;
    }

    @Override
    public String getTableId() {
        return CHANGES_TABLE_ID;
    }
}

