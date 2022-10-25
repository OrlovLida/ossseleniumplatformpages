package com.oss.pages.platform.bookmarksanddashboards;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EditObjectWizardPage extends CategoryWizardPage {

    private static final String CATEGORIES_ID = "categories";

    public EditObjectWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void setCategory(String categoryName) {
        getPopup().setComponentValue(CATEGORIES_ID, categoryName);
    }
}
