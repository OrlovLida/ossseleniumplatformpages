package com.oss.pages.platform.bookmark;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CategoryWizardPage {

    public static final String
            CATEGORY_POPUP = "category_popup";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String SAVE_BOOKMARK_BTN = "saveBookmarkBtn";
    private final Wizard categoryWizard;
    private final WebDriver driver;

    private CategoryWizardPage(WebDriver driver, Wizard categoryWizard) {
        this.driver = driver;
        this.categoryWizard = categoryWizard;
    }

    public static CategoryWizardPage createWizard(WebDriver driver, WebDriverWait wait) {
        Wizard categoryWizard = Wizard.createByComponentId(driver, wait, CATEGORY_POPUP);
        return new CategoryWizardPage(driver, categoryWizard);

    }

    public void setName(String nameCategory) {
        categoryWizard.setComponentValue(NAME, nameCategory, Input.ComponentType.TEXT_FIELD);
    }

    public void setDescription(String description) {
        categoryWizard.setComponentValue(DESCRIPTION, description, Input.ComponentType.TEXT_AREA);

    }

    public void clickSave() {
        categoryWizard.clickButtonById(SAVE_BOOKMARK_BTN);
    }
}
