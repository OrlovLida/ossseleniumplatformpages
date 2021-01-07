package com.oss.pages.platform.viewmanager;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.components.portals.CreateCategoryPopup;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ViewManagerPage extends BasePage {

    private final static String SEARCH_TEST_ID = "search";

    @FindBy(className = "views-manager__bar__add-category")
    public WebElement addCategoryButton;

    public ViewManagerPage(WebDriver driver) {
        super(driver);
    }

    public CreateCategoryPopup goToCreateCategoryPopup() {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        return new CreateCategoryPopup(driver, wait);
    }

    @Step("Search specific category by name")
    public void searchForCategory(){
        SearchField searchField = (SearchField) ComponentFactory.create(SEARCH_TEST_ID, Input.ComponentType.SEARCH_FIELD, driver, wait);
        searchField.typeValue("Test Category");

        System.out.println(searchField.getLabel());
    }
}
