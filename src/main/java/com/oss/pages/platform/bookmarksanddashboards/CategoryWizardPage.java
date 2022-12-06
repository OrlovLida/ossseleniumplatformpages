package com.oss.pages.platform.bookmarksanddashboards;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.prompts.Popup;
import com.oss.pages.BasePage;

public class CategoryWizardPage extends BasePage {

    private static final String NAME_ID = "name";
    private static final String DESCRIPTION_ID = "description";
    private static final String SAVE_BUTTON_LABEL = "Save";

    public CategoryWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void setName(String categoryName) {
        getPopup().setComponentValue(NAME_ID, categoryName);
    }

    public void setDescription(String description) {
        getPopup().setComponentValue(DESCRIPTION_ID, description);
    }

    public void clickSave() {
        getPopup().clickButtonByLabel(SAVE_BUTTON_LABEL);
    }

    public void clickButton(String buttonId) {
        getPopup().clickButtonById(buttonId);
    }

    protected Popup getPopup() {
        return Popup.create(driver, wait);
    }
}
