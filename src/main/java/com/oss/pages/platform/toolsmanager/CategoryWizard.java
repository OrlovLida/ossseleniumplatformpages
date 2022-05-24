package com.oss.pages.platform.toolsmanager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.icons.NavigationIcon;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;

public class CategoryWizard {
    
    private static final String NAME_FIELD_ID_IN_CATEGORY_POPUP = "input_category-popup--input1";
    private static final String DESCRIPTION_FIELD_ID_IN_CATEGORY_POPUP = "input_category-popup--input2";
    
    private WebDriver driver;
    private WebDriverWait wait;
    private Wizard category;
    
    private CategoryWizard(WebDriver driver, WebDriverWait wait, Wizard wizard) {
        this.driver = driver;
        this.wait = wait;
        this.category = wizard;
    }
    
    public static CategoryWizard create(WebDriver driver, WebDriverWait wait) {
        Wizard wizard = Wizard.createByComponentId(driver, wait, "popup_container");
        return new CategoryWizard(driver, wait, wizard);
    }
    
    public Input setName(String name) {
        return category.setComponentValue(NAME_FIELD_ID_IN_CATEGORY_POPUP, name, Input.ComponentType.TEXT_FIELD);
    }
    
    public Input setDescription(String description) {
        return category.setComponentValue(DESCRIPTION_FIELD_ID_IN_CATEGORY_POPUP, description, Input.ComponentType.TEXT_AREA);
    }
    
    public void selectIcon(String iconId) {
        NavigationIcon.create(driver, wait).selectIcon(iconId);
    }
    
    public void clickSave() {
        category.clickSave();
    }
}
