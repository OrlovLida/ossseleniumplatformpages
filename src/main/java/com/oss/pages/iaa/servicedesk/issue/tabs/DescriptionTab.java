package com.oss.pages.iaa.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DescriptionTab extends NotesTab {

    private static final String DESCRIPTION_COMPONENT_ID = "OLD_TEXT_FIELD_APP-_descriptionWidget";

    public DescriptionTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    public String getTextFieldId() {
        return DESCRIPTION_COMPONENT_ID;
    }
}
