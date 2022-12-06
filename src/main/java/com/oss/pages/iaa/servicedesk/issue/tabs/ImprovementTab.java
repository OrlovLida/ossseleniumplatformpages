package com.oss.pages.iaa.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ImprovementTab extends NotesTab {

    private static final String IMPROVEMENT_COMPONENT_ID = "OLD_TEXT_FIELD_APP-_improvementNotesApp";

    public ImprovementTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    public String getTextFieldId() {
        return IMPROVEMENT_COMPONENT_ID;
    }
}
