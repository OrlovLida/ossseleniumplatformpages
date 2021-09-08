package com.oss.pages.bookmarkManager.BookmarkManagerWizards;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryWizardPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(CategoryWizardPage.class);

    private final Wizard createCategoryWizard;
    private final String CREATE_CATEGORY_WIZARD_ID = "createCategoryForm";
    private final String NAME_CATEGORY_LABEL = "Name";
    private final String CATEGORY_NAME_ID = "categoryName";


    public CategoryWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        createCategoryWizard = Wizard.createByComponentId(driver, wait, CREATE_CATEGORY_WIZARD_ID);
    }

    public void fillCategoryName(String categoryName) {
        createCategoryWizard.setComponentValue(CATEGORY_NAME_ID, categoryName, Input.ComponentType.TEXT_FIELD);
    }

    public void clickSave() {
        createCategoryWizard.clickSave();
    }
}
