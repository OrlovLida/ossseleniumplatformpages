package com.oss.pages.administration.managerwizards;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Step;

public class CategoryWizardPage extends BaseWizardPage {

    private static final Logger log = LoggerFactory.getLogger(CategoryWizardPage.class);

    private static final String CREATE_CATEGORY_WIZARD_ID = "createCategoryForm";
    private static final String CATEGORY_NAME_ID = "categoryName";

    public CategoryWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Fill category name")
    public void fillCategoryName(String categoryName) {
        fillName(CATEGORY_NAME_ID, categoryName);
        log.info("Category name filled with name: {}", categoryName);
    }

    @Override
    public String getWizardId() {
        return CREATE_CATEGORY_WIZARD_ID;
    }
}
