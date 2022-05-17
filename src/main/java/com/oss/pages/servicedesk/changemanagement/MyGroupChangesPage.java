package com.oss.pages.servicedesk.changemanagement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.servicedesk.BaseSearchPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.CHANGES_TABLE_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.CHANGE_ISSUE_TYPE;

public class MyGroupChangesPage extends BaseSearchPage {

    private static final String MY_CHANGES_VIEW = "change-search?type=my-group";

    public MyGroupChangesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    @Step("I open My Group Changes View")
    public MyGroupChangesPage openView(WebDriver driver, String basicURL) {
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
    public TableWidget getIssueTable() {
        return TableWidget.createById(driver, CHANGES_TABLE_ID, wait);
    }
}

