package com.oss.pages.platform.viewmanager;

import com.oss.framework.components.portals.CreateCategoryPopup;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ViewManagerPage extends BasePage {

    @FindBy(className = "views-manager__bar__add-category")
    public WebElement addCategoryButton;

    public ViewManagerPage(WebDriver driver) {
        super(driver);
    }

    public CreateCategoryPopup goToCreateCategoryPopup() {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        return new CreateCategoryPopup(driver, wait);
    }

}
