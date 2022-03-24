package com.oss.pages.bookmarkmanager.bookmarkmanagerwizards;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class CategoryWizardPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(CategoryWizardPage.class);

    private final Wizard createCategoryWizard;
    private static final String CREATE_CATEGORY_WIZARD_ID = "createCategoryForm";
    private static final String CATEGORY_NAME_ID = "categoryName";

    public CategoryWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        createCategoryWizard = Wizard.createByComponentId(driver, wait, CREATE_CATEGORY_WIZARD_ID);
    }

    @Step("Fill category name")
    public void fillCategoryName(String categoryName) {
        createCategoryWizard.setComponentValue(CATEGORY_NAME_ID, categoryName, Input.ComponentType.TEXT_FIELD);
        log.info("Category name filled with name: " + categoryName);
    }

    @Step("Click Save")
    public void clickSave() {
        createCategoryWizard.clickSave();
        log.info("Clicking Save button");
    }
}
