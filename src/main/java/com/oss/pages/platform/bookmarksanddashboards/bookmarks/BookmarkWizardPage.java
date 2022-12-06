package com.oss.pages.platform.bookmarksanddashboards.bookmarks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

public class BookmarkWizardPage extends BasePage {
    private static final String SAVE_BOOKMARK_POP_ID = "POPUP_MANAGEMENT_CONFIGURATION_ID";
    public static final String DESCRIPTION_INPUT_ID = "description";
    public static final String NAME_INPUT_ID = "name";
    public static final String CATEGORIES_INPUT_ID = "input_categories";
    private static final String SAVE_BOOKMARK_ID = "saveManagementConfigurationBtn";

    public BookmarkWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void setName(String name) {
        getWizard().setComponentValue(NAME_INPUT_ID, name);

    }

    public void setDescription(String description) {
        getWizard().setComponentValue(DESCRIPTION_INPUT_ID, description);
    }

    public void setCategory(String category) {
        getWizard().setComponentValue(CATEGORIES_INPUT_ID, category, Input.ComponentType.SEARCH_FIELD);
    }

    public void clickSave() {
        getWizard().clickButtonById(SAVE_BOOKMARK_ID);

    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, SAVE_BOOKMARK_POP_ID);
    }
}
