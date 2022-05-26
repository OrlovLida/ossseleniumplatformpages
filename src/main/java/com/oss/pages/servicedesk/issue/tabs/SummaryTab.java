package com.oss.pages.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SummaryTab extends NotesTab {

    private static final String SUMMARY_TEXT_FIELD_ID = "_summaryApp";

    public SummaryTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    public String getTextFieldId() {
        return SUMMARY_TEXT_FIELD_ID;
    }
}
