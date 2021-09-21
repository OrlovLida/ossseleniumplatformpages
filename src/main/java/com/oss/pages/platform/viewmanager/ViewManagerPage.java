package com.oss.pages.platform.viewmanager;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.components.portals.ActionsDropdownList;
import com.oss.framework.navigation.ApplicationWizard;
import com.oss.framework.navigation.CategoryWizard;
import com.oss.framework.navigation.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ViewManagerPage extends BasePage {

    private static final String SEARCH_TEST_ID = "search";
    private static final String ADD_APPLICATION_BUTTON_ID = "addTileButton0";
    private static final String CREATE_SUBCATEGORY_BUTTON_ID = "createSubcategoryButton0";
    private static final String EDIT_SUBCATEGORY_BUTTON_ID = "editButton0";
    private static final String DELETE_CATEGORY_BUTTON_ID = "deleteCategoryButton0";
    private static final String EDIT_CATEGORY_BUTTON_ID = "editCategoryButton0";
    private static final String EDIT_APPLICATION_BUTTON_XPATH = "//a[@id='editButton0']";
    private static final String CATEGORY_ROLLOUT_BUTTON_XPATH = "//div[@class='categories__buttons__rollout']/i[@class='fa fa-chevron-down']";
    private static final String FIRST_APPLICATION_THREE_DOTS_BUTTON_XPATH = "(//div[1]/div[@id=\"frameworkObjectButtonsGroup\"])[2]";
    private static final String FIRST_APPLICATION_DRAG_AND_DROP_BUTTON_XPATH = "(//div[1]/div[@class='draggableBox draggableItemNonActive']/div[@class='btn-drag tile-drag'])[1]";
    private static final String SECOND_APPLICATION_DRAG_AND_DROP_BUTTON_XPATH = "(//div[2]/div[@class='draggableBox draggableItemNonActive']/div[@class='btn-drag tile-drag'])[1]";
    private static final String FIRST_CATEGORY_DRAG_AND_DROP_BUTTON_XPATH = "(//div[@class='draggableBox draggableItemNonActive']/div[@class='btn-drag'])[1]";
    private static final String SECOND_CATEGORY_DRAG_AND_DROP_BUTTON_XPATH = "(//div[@class='draggableBox draggableItemNonActive']/div[@class='btn-drag'])[2]";
    private static final String FIRST_APP_FROM_SUBCATEGORY_DRAG_BUTTON_XPATH = "(//div[@class='btn-drag tile-drag'])[3]";
    private static final String FIRST_SUBCATEGORY_DRAG_BUTTON_XPATH = "(//div[@class='btn-drag sub-drag'])[1]";
    private static final String SECOND_SUBCATEGORY_DRAG_BUTTON_XPATH = "(//div[@class='btn-drag sub-drag'])[2]";

    private ToolsManagerWindow toolsManagerWindow;

    public ViewManagerPage(WebDriver driver) {
        super(driver);
        this.toolsManagerWindow = ToolsManagerWindow.create(driver, wait);
    }

    public CategoryWizard goToCategoryPopup() {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        return CategoryWizard.create(driver, wait);
    }

    public ApplicationWizard goToApplicationPopup() {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        return ApplicationWizard.create(driver, wait);
    }

    public String getApplicationsUrl(int numberOfApplication){
        return this.toolsManagerWindow.getGivenApplicationsURL(numberOfApplication);
    }

    public String getMainCategoryName(int numberOfCategory){
        return toolsManagerWindow.getMainCategoryName(numberOfCategory);
    }

    public String getSubcategoryName(int numberOfSubcategory){
        return toolsManagerWindow.getSubcategoryName(numberOfSubcategory);
    }

    private void clickSubcategoryGroupButton(int numberOfButton){
        toolsManagerWindow.clickSubcategoryGroupButton(numberOfButton);
    }

    public void clickThreeDotsButton(int numberOfButton){
        toolsManagerWindow.clickThreeDotsGroupButton(numberOfButton);
    }

    public void clickAddCategoryButton(){
        toolsManagerWindow.clickAddCategoryButton();
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
        toolsManagerWindow.clickThreeDotsGroupButton(0);
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(EDIT_CATEGORY_BUTTON_ID);
    }

    public void enterAddApplicationButtonInFirstMainCategory(){
        toolsManagerWindow.clickThreeDotsGroupButton(0);
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(ADD_APPLICATION_BUTTON_ID);
    }

    public void enterAddApplicationButtonInFirstSubcategory(){
        clickSubcategoryGroupButton(0);
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(ADD_APPLICATION_BUTTON_ID);
    }

    public void enterEditSubcategoryButton(){
        clickSubcategoryGroupButton(0);
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(EDIT_SUBCATEGORY_BUTTON_ID);
    }

    public void removeFirstSubcategory(){
        clickSubcategoryGroupButton(0);
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(DELETE_CATEGORY_BUTTON_ID);

        toolsManagerWindow.clickDeleteButton();
        DelayUtils.sleep(2000);
    }

    public void enterCreateSubcategory(){
        toolsManagerWindow.clickThreeDotsGroupButton(0);
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
        toolsManagerWindow.clickThreeDotsGroupButton(0);
        toolsManagerWindow.clickDeleteButtonInDropdown();
    }

    public void clickDeleteButtonInDropdown(){
        toolsManagerWindow.clickDeleteButtonInDropdown();
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
