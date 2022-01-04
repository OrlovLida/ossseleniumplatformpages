package com.oss.pages.platform.viewmanager;

import java.util.List;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.components.portals.ActionsDropdownList;
import com.oss.framework.components.portals.PopupV2;
import com.oss.framework.navigation.ApplicationWizard;
import com.oss.framework.navigation.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.DragAndDrop;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ViewManagerPage extends BasePage {

    private static final String SEARCH_TEST_ID = "search";
    private static final String ADD_APPLICATION_BUTTON_ID = "addTileButton0";
    private static final String APPLICATION_WIZARD_ID = "popup_container";


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

    public void openView(String categoryName, String applicationName){
        toolsManagerWindow.openApplication(categoryName, applicationName);
    }

    public void openCreateCategoryWizard() {
        toolsManagerWindow.clickCreateCategory();
    }

    public void openEditApplicationWizard(String categoryName, String applicationName){
        toolsManagerWindow.callActionApplication(categoryName, applicationName, APPLICATION_WIZARD_ID);
    }

    @Step("Search specific category by name")
    public void searchForCategory(String categoryName) {
    toolsManagerWindow.search(categoryName);
    }

    public void clearSearch() {
        Input searchField =  ComponentFactory.create(SEARCH_TEST_ID, Input.ComponentType.SEARCH_FIELD, driver, wait);
        searchField.clear();

        DelayUtils.sleep(1000);
    }

    public void openEditCategoryWizard(String categoryName){
        toolsManagerWindow.callAction(categoryName,"editCategoryButton0");
    }

    public void openAddApplicationWizard(String categoryName){
        toolsManagerWindow.callAction(categoryName, ADD_APPLICATION_BUTTON_ID);
    }

    public void openAddApplicationWizard(String categoryName, String subcategory) {
        toolsManagerWindow.callActionSubcategory(categoryName, subcategory, ADD_APPLICATION_BUTTON_ID);

    }

    public void openEditSubcategoryWizard(String categoryName, String subcategoryName) {
        toolsManagerWindow.callActionSubcategory(categoryName, subcategoryName, "editCategoryButton0");
    }

    public void deleteSubcategory(String categoryName, String subcategoryName) {
        toolsManagerWindow.callActionSubcategory(categoryName, subcategoryName, "deleteCategoryButton0");
        confirmDeletion();
    }

    public void openCreateSubcategoryWizard(String categoryName){
        toolsManagerWindow.callAction(categoryName, "createSubcategoryButton0");
    }


    public void deleteCategory(String categoryName) {
       toolsManagerWindow.callAction(categoryName, "deleteCategoryButton0");
       confirmDeletion();
    }


    public void expandCategory(String categoryName){
        toolsManagerWindow.expandCategory(categoryName);
    }

    private void confirmDeletion(){
        PopupV2.create(driver, wait).clickButtonByLabel("DELETE");
    }
    public List<String> getSubcategories(String categoryName){
        return toolsManagerWindow.getSubcategories(categoryName);
    }
}
