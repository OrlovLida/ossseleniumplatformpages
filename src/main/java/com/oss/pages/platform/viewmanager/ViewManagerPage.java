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
    private static final String APPLICATION_WIZARD_ID = "popup_container";
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

//    public CategoryWizard goToCategoryPopup() {
//        WebDriverWait wait = new WebDriverWait(driver, 45);
//        return CategoryWizard.create(driver, wait);
//    }
//
//    public ApplicationWizard goToApplicationPopup() {
//        WebDriverWait wait = new WebDriverWait(driver, 45);
//        return ApplicationWizard.create(driver, wait);
//    }

    public String getApplicationUrl(String applicationName) {
        return toolsManagerWindow.getApplicationURL(applicationName);
    }

//    //do usunięcia
//    public String getMainCategoryName(int numberOfCategory) {
//        return toolsManagerWindow.getMainCategoryName(numberOfCategory);
//    }
//
//    //do usunięcia
//    public String getSubcategoryName(int numberOfSubcategory) {
//        return toolsManagerWindow.getSubcategoryName(numberOfSubcategory);
//    }

    public void expandSubcategoryGroupButton(String subcategory) {
        toolsManagerWindow.expandSubcategoryGroupButton(subcategory);
    }

    public void clickApplicationGroupButton(String applicationName){
        toolsManagerWindow.expandGroupButtonOnApplication(applicationName);
    }

    public void clickAddCategoryButton() {
        toolsManagerWindow.clickAddCategoryButton();
    }

    public ApplicationWizard enterEditionOfApplication(String categoryName, String applicationName){
        return toolsManagerWindow.getEditApplicationWizard(categoryName, applicationName, APPLICATION_WIZARD_ID);
    }

    @Step("Search specific category by name")
    public void searchForCategory(String categoryName) {
        SearchField searchField = (SearchField) ComponentFactory.create(SEARCH_TEST_ID, Input.ComponentType.SEARCH_FIELD, driver, wait);
        searchField.typeValue(categoryName);

        DelayUtils.sleep(1000);
    }

    public void clearSearchField() {
        SearchField searchField = (SearchField) ComponentFactory.create(SEARCH_TEST_ID, Input.ComponentType.SEARCH_FIELD, driver, wait);
        searchField.clear();

        DelayUtils.sleep(1000);
    }

    public void enterEditionOfCategory(String categoryName){
        toolsManagerWindow.enterEditionOfCategory(categoryName);
    }

    public ApplicationWizard enterAddApplicationWizardInCategory(String categoryName, String wizardId){
        return toolsManagerWindow.getCreateApplicationWizard(categoryName, wizardId);
    }

    public void enterAddApplicationButtonInSubcategory(String subcategory) {
        toolsManagerWindow.expandSubcategoryGroupButton(subcategory);
        ActionsDropdownList actionsDropdownList = new ActionsDropdownList(driver);
        actionsDropdownList.clickOnActionById(ADD_APPLICATION_BUTTON_ID);
    }

    public void enterEditSubcategoryButton(String subcategoryName) {
        toolsManagerWindow.enterEditSubcategoryButton(subcategoryName);
    }

    public void removeSubcategory(String subcategoryName) {
        ToolsManagerWindow.Subcategory subcategory = toolsManagerWindow.getSubcategoryByName(subcategoryName);
        subcategory.removeSubcategory();
        DelayUtils.sleep(2000);
    }

    public void enterCreateSubcategoryInMainCategory(String mainCategoryName){
        toolsManagerWindow.enterCreateSubcategoryInMainCategory(mainCategoryName);
    }

    public void clickButtonsGroupOnFirstApplication() {
        driver.findElement(By.xpath(FIRST_APPLICATION_THREE_DOTS_BUTTON_XPATH)).click();
    }

    public void clickEditButton() {
        driver.findElement(By.xpath(EDIT_APPLICATION_BUTTON_XPATH)).click();
    }

    public void deleteTestCategory() {
        ToolsManagerWindow.Category category = toolsManagerWindow.getCategoryByName("Name After Edition");
        category.removeCategory();
    }

    public void clickDeleteButtonInDropdown() {
        toolsManagerWindow.clickDeleteButtonInDropdown();
    }

    public void clickDeleteButtonInConfirmationPopup() {
        toolsManagerWindow.clickDeleteButtonInConfirmationPopup();
    }

    public void dragAndDropFirstAppInPlaceOfSecond() {
        DragAndDrop.dragAndDrop(FIRST_APPLICATION_DRAG_AND_DROP_BUTTON_XPATH, SECOND_APPLICATION_DRAG_AND_DROP_BUTTON_XPATH, driver);
    }

    public void dragAndDropFirstCategoryInPlaceOfSecond() {
        DragAndDrop.dragAndDrop(FIRST_CATEGORY_DRAG_AND_DROP_BUTTON_XPATH, SECOND_CATEGORY_DRAG_AND_DROP_BUTTON_XPATH, driver);
    }

    public void dragAndDropFirstAppToSubcategory() {
        DragAndDrop.dragAndDrop(FIRST_APPLICATION_DRAG_AND_DROP_BUTTON_XPATH, FIRST_APP_FROM_SUBCATEGORY_DRAG_BUTTON_XPATH, driver);
    }

    public void dragAndDropFirstSubcategoryToPlaceOfSecondSubcategory() {
        DragAndDrop.dragAndDrop(FIRST_SUBCATEGORY_DRAG_BUTTON_XPATH, SECOND_SUBCATEGORY_DRAG_BUTTON_XPATH, driver);
    }

    public void expandCategory(String categoryName){
        ToolsManagerWindow.Category category = toolsManagerWindow.getCategoryByName(categoryName);
        category.expandCategory();
    }
}
