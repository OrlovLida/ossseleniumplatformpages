package com.oss.pages.platform.viewmanager;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ViewManagerPage extends BasePage {

    @FindBy(className = "views-manager__bar__add-category")
    public WebElement addCategoryButton;

    public ViewManagerPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Add Category button")
    public void clickSetParameters() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
