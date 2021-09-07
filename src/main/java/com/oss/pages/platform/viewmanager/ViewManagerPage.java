package com.oss.pages.platform.viewmanager;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.components.portals.ActionsDropdownList;
import com.oss.framework.components.portals.ApplicationPopup;
import com.oss.framework.components.portals.CategoryPopup;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ViewManagerPage extends BasePage {

    private static final String SEARCH_TEST_ID = "search";
    private static final String ADD_APPLICATION_BUTTON_ID = "addTileButton0";
    private static final String CREATE_SUBCATEGORY_BUTTON_ID = "createSubcategoryButton0";
    private static final String EDIT_SUBCATEGORY_BUTTON_ID = "editButton0";
    private static final String DELETE_CATEGORY_BUTTON_ID = "deleteCategoryButton0";
    private static final String EDIT_CATEGORY_BUTTON_ID = "editCategoryButton0";
    private static final String DRAG_CATEGORY_BUTTON_ID = "dragCategoryButton0";
    private static final String EDIT_APPLICATION_BUTTON_XPATH = "//a[@id='editButton0']";
    private static final String THREE_DOTS_BUTTONS_XPATH = "//*[@id=\"frameworkObjectButtonsGroup\"]";
    private static final String DELETE_BUTTON_IN_POPUP_XPATH = "//*[@class='CommonButton btn btn-danger btn-md']";
    private static final String EDIT_CATEGORY_POPUP_XPATH = "//*[@class='popupContainer']";
    private static final String CATEGORY_ROLLOUT_BUTTON_XPATH = "//div[@class='categories__buttons__rollout']/i[@class='fa fa-chevron-down']";
    private static final String FIRST_APPLICATION_THREE_DOTS_BUTTON_XPATH = "(//div[1]/div[@id=\"frameworkObjectButtonsGroup\"])[2]";
    private static final String FIRST_APPLICATION_DRAG_AND_DROP_BUTTON_XPATH = "(//div[1]/div[@class='draggableBox draggableItemNonActive']/div[@class='btn-drag tile-drag'])[1]";
    private static final String SECOND_APPLICATION_DRAG_AND_DROP_BUTTON_XPATH = "(//div[2]/div[@class='draggableBox draggableItemNonActive']/div[@class='btn-drag tile-drag'])[1]";
    private static final String APPLICATIONS_LINKS_XPATH = "//div[@class='category-box__content']/a[@class='category-box__content__link']";
    private static final String SUBCATEGORIES_GROUP_BUTTONS_XPATH = "//div[@class='subcategories__buttons']//div[@id='frameworkObjectButtonsGroup']";
    private static final String MAIN_CATEGORIES_NAMES_XPATH = "//div[@class='draggableBox draggableItemNonActive']/div[@class='categories']";
    private static final String FIRST_CATEGORY_DRAG_AND_DROP_BUTTON_XPATH = "(//div[@class='draggableBox draggableItemNonActive']/div[@class='btn-drag'])[1]";
    private static final String SECOND_CATEGORY_DRAG_AND_DROP_BUTTON_XPATH = "(//div[@class='draggableBox draggableItemNonActive']/div[@class='btn-drag'])[2]";
    private static final String FIRST_APP_FROM_SUBCATEGORY_DRAG_BUTTON_XPATH = "(//div[@class='btn-drag tile-drag'])[3]";
    private static final String FIRST_SUBCATEGORY_DRAG_BUTTON_XPATH = "(//div[@class='btn-drag sub-drag'])[1]";
    private static final String SECOND_SUBCATEGORY_DRAG_BUTTON_XPATH = "(//div[@class='btn-drag sub-drag'])[2]";
    private static final String SUBCATEGORIES_NAMES_XPATH = "//div[@class='subcategories__name']";

    @FindBy(className = "views-manager__bar__add-category")
    public WebElement addCategoryButton;

    @FindBy(xpath = THREE_DOTS_BUTTONS_XPATH)
    public WebElement threeDotsFirstButton;

    @FindBy(xpath = DRAG_CATEGORY_BUTTON_ID)
    public WebElement dragButton;

    public ViewManagerPage(WebDriver driver) {
        super(driver);
    }

    public CategoryPopup goToCategoryPopup() {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        return new CategoryPopup(driver, wait);
    }

    public ApplicationPopup goToApplicationPopup() {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        return new ApplicationPopup(driver, wait);
    }

    public WebElement getApplication(int numberOfApplication){
        List<WebElement> applicationLinks = driver.findElements(By.xpath(APPLICATIONS_LINKS_XPATH));
        return applicationLinks.get(numberOfApplication);
    }

    public String getCategoryName(int numberOfCategory){
        List<WebElement> categoriesLinks = driver.findElements(By.xpath(MAIN_CATEGORIES_NAMES_XPATH));
        return categoriesLinks.get(numberOfCategory).getText();
    }

    public String getSubcategoryName(int numberOfSubcategory){
        List<WebElement> categoriesLinks = driver.findElements(By.xpath(SUBCATEGORIES_NAMES_XPATH));
        return categoriesLinks.get(numberOfSubcategory).getText();
    }

    public WebElement getSubcategoryGroupButton(int numberOfButton){
        List<WebElement> applicationLinks = driver.findElements(By.xpath(SUBCATEGORIES_GROUP_BUTTONS_XPATH));
        return applicationLinks.get(numberOfButton);
    }

    public WebElement getThreeDotsGroupButton(int numberOfButton){
        List<WebElement> threeDotsGroupButtons = driver.findElements(By.xpath(THREE_DOTS_BUTTONS_XPATH));
        return threeDotsGroupButtons.get(numberOfButton);
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

    public void enterAddApplicationButtonInFirstMainCategory(){
        threeDotsFirstButton.click();
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(ADD_APPLICATION_BUTTON_ID);
    }

    public void enterAddApplicationButtonInFirstSubcategory(){
        getSubcategoryGroupButton(0).click();
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(ADD_APPLICATION_BUTTON_ID);
    }

    public void enterEditSubcategoryButton(){
        getSubcategoryGroupButton(0).click();
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(EDIT_SUBCATEGORY_BUTTON_ID);
    }

    public void removeFirstSubcategory(){
        getSubcategoryGroupButton(0).click();
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(DELETE_CATEGORY_BUTTON_ID);

        WebElement deleteButton = driver.findElement(By.xpath(DELETE_BUTTON_IN_POPUP_XPATH));
        deleteButton.click();
        DelayUtils.sleep(2000);
    }

    public void enterCreateSubcategory(){
        threeDotsFirstButton.click();
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(CREATE_SUBCATEGORY_BUTTON_ID);
    }

    public void clickButtonsGroupOnFirstApplication(){
        driver.findElement(By.xpath(FIRST_APPLICATION_THREE_DOTS_BUTTON_XPATH)).click();
    }

    public void clickEditButton(){
        driver.findElement(By.xpath(EDIT_APPLICATION_BUTTON_XPATH)).click();
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

    public void clickDeleteButton(){
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(DELETE_CATEGORY_BUTTON_ID);
        DelayUtils.sleep(1500);
        WebElement deleteButton = driver.findElement(By.xpath(DELETE_BUTTON_IN_POPUP_XPATH));
        deleteButton.click();
    }

    public void dragAndDropFirstAppInPlaceOfSecond(){
        DragAndDrop.dragAndDrop(FIRST_APPLICATION_DRAG_AND_DROP_BUTTON_XPATH, SECOND_APPLICATION_DRAG_AND_DROP_BUTTON_XPATH,driver);
    }

    public void dragAndDropFirstCategoryInPlaceOfSecond(){
        DragAndDrop.dragAndDrop(FIRST_CATEGORY_DRAG_AND_DROP_BUTTON_XPATH, SECOND_CATEGORY_DRAG_AND_DROP_BUTTON_XPATH, driver);
    }

    public void dragAndDropFirstAppToSubcategory(){
        DragAndDrop.dragAndDrop(FIRST_APPLICATION_DRAG_AND_DROP_BUTTON_XPATH, FIRST_APP_FROM_SUBCATEGORY_DRAG_BUTTON_XPATH, driver);
    }

    public void dragAndDropFirstSubcategoryToPlaceOfSecondSubcategory(){
        DragAndDrop.dragAndDrop(FIRST_SUBCATEGORY_DRAG_BUTTON_XPATH, SECOND_SUBCATEGORY_DRAG_BUTTON_XPATH, driver);
    }

    public void rolloutFirstCategory(){
        driver.findElement(By.xpath(CATEGORY_ROLLOUT_BUTTON_XPATH)).click();
    }
}
