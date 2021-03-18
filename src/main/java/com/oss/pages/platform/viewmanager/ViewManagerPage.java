package com.oss.pages.platform.viewmanager;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.components.portals.ActionsDropdownList;
import com.oss.framework.components.portals.CategoryPopup;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ViewManagerPage extends BasePage {

    private static final String SEARCH_TEST_ID = "search";
    private static final String ADD_APPLICATION_BUTTON_ID = "addTileButton0";
    private static final String CREATE_SUBCATEGORY_BUTTON_ID = "createSubcategoryButton0";
    private static final String DELETE_CATEGORY_BUTTON_ID = "deleteCategoryButton0";
    private static final String EDIT_CATEGORY_BUTTON_ID = "editCategoryButton0";
    private static final String THREE_DOTS_FIRST_BUTTON_FULL_XPATH = "//*[@id=\"frameworkObjectButtonsGroup\"]";
    private static final String DELETE_BUTTON_IN_POPUP_XPATH = "//*[@class='CommonButton btn btn-danger btn-md']";
    private static final String EDIT_CATEGORY_POPUP_XPATH = "//*[@class='popupContainer']";


    @FindBy(className = "views-manager__bar__add-category")
    public WebElement addCategoryButton;

    @FindBy(xpath = THREE_DOTS_FIRST_BUTTON_FULL_XPATH)
    public WebElement threeDotsFirstButton;

    public ViewManagerPage(WebDriver driver) {
        super(driver);
    }

    public CategoryPopup goToCreateCategoryPopup() {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        return new CategoryPopup(driver, wait);
    }

    public CategoryPopup goToEditCategoryPopup() {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        return new CategoryPopup(driver, wait);
    }

    @Step("Search specific category by name")
    public void searchForCategory(String categoryName){
        SearchField searchField = (SearchField) ComponentFactory.create(SEARCH_TEST_ID, Input.ComponentType.SEARCH_FIELD, driver, wait);
        searchField.typeValue(categoryName);

        DelayUtils.sleep(1000);
    }

    public void clearSearchField(){
        SearchField searchField = (SearchField) ComponentFactory.create(SEARCH_TEST_ID, Input.ComponentType.SEARCH_FIELD, driver, wait);
        searchField.clear();

        DelayUtils.sleep(1000);
    }

    public void enterEditionOfCategory(){
        threeDotsFirstButton.click();
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(EDIT_CATEGORY_BUTTON_ID);
    }

    public void deleteFirstCategory(){
        threeDotsFirstButton.click();
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(DELETE_CATEGORY_BUTTON_ID);
        DelayUtils.sleep(1000);

        WebElement deleteButton = driver.findElement(By.xpath(DELETE_BUTTON_IN_POPUP_XPATH));
        deleteButton.click();
        DelayUtils.sleep(2000);
    }
}
