package com.oss.pages.filtermanager;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;


public class FilterManagerPage extends BasePage {

    public FilterManagerPage(WebDriver driver){
        super(driver);
        commonList = CommonList.create(driver, wait, COMMON_LIST_APP_ID);
    }

    @FindBy(xpath = "//div[@class='OssWindow']")
    private WebElement filterManagerList;

    private final String NEW_FOLDER_ID = "new_folder";
    private final String COMMON_LIST_APP_ID = "_FilterManagerList";
    private CommonList commonList;

    @Step("Open Filter Manager Page")
    public static FilterManagerPage goToFilterManagerPage(WebDriver driver, String baseURL){
        driver.get(String.format("%s/#/view/management/views/filter-view" + "?perspective=LIVE", baseURL));
        return new FilterManagerPage(driver);
    }

    @Step("Delete All Filters")
    public FilterManagerPage deleteAllFilters(){
        expandAllCategories();
        commonList.deleteAllListElements();
        return this;
    }

    @Step("Delete All Folders")
    public FilterManagerPage deleteAllFolders(){
        commonList.deleteAllCategories();
        return this;
    }

    @Step("Open Edit Filter Wizard")
    public FilterManagerPage clickOnEdit(String name){
        DelayUtils.waitForPageToLoad(driver,wait);
        commonList.getEditButtonByListElementName(name).click();
        return this;
    }

    @Step("Expand all categories")
    public FilterManagerPage expandAllCategories(){
        commonList.expandAllCategories();
        return this;
    }

    @Step("Collapse all categories")
    public FilterManagerPage collapseAllCategories(){
        commonList.collapseAllCategories();
        return this;
    }

    @Step("Create Folder")
    public FilterManagerPage createFolder(String name){
        return openCreateNewFolderWizard().typeNameOfTheFolder(name).clickAccept();
    }

    @Step("Open Create New Folder Wizard")
    public CreateFolderWizard openCreateNewFolderWizard(){
        DelayUtils.waitForPageToLoad(driver, wait);
        ActionsContainer.createFromParent(filterManagerList,driver,wait).callAction(NEW_FOLDER_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new CreateFolderWizard(driver);
    }

    @Step("Edit Filter")
    public FilterManagerPage editFilter(String name){
        commonList.expandListElementKebab(name);
        commonList.getEditButtonByListElementName(name).click();
        return this;
    }

    @Step("Delete Filter")
    public FilterManagerPage deleteFilter(String name){
        DelayUtils.waitForPageToLoad(driver, wait);
        commonList.expandListElementKebab(name);
        DelayUtils.waitForPageToLoad(driver, wait);
        commonList.chooseDelete();
        return this;
    }

    @Step("Share Filter for user")
    public FilterManagerPage shareFilter(String filterName, String userName, String permission){
        openShareFilter(filterName)
                .typeUserNameInSearch(userName)
                .shareForUser(userName, permission)
                .closeShareView();
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Share Folder for user")
    public FilterManagerPage shareFolder(String folderName, String userName){
        openShareFolder(folderName)
                .typeUserNameInSearch(userName)
                .shareForUser(userName, "R")
                .closeShareView();
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Open Share Filter Page")
    public ShareFilterPage openShareFilter(String name){
        commonList.expandListElementKebab(name);
        commonList.chooseShare();
        return new ShareFilterPage(driver);
    }

    @Step("Open Share Folder Page")
    public ShareFilterPage openShareFolder(String folderName){
        commonList.expandCategoryKebab(folderName);
        commonList.chooseShare();
        return new ShareFilterPage(driver);
    }

    @Step("Mark as a favorite")
    public FilterManagerPage markAsAFavorite(String filterName){
        DelayUtils.waitForPageToLoad(driver,wait);
        commonList.getFavoriteButtonByListElementName(filterName).click();
        return this;
    }

    @Step("Expand Folder")
    public FilterManagerPage expandFolder(String folderName){
        DelayUtils.waitForPageToLoad(driver,wait);
        commonList.getCategoryByName(folderName).click();
        return this;
    }

    @Step("How many Filters")
    public int howManyFilters(){
        return commonList.howManyListElements();
    }

    @Step("How many Folders")
    public int howManyFolders(){
        return commonList.howManyCategories();
    }

    @Step("Checking that Filter is Visible")
    public boolean isFilterVisible(String filterName){
        return commonList.isListElementVisible(filterName);
    }

    @Step("Checking that Folder is Visible")
    public boolean isFolderVisible(String folderName){
        return commonList.isCategoryVisible(folderName);
    }

    @Step("Checking that Edit Action is Visible")
    public boolean isEditActionVisible(String filterName){
        return commonList.isEditActionVisible(filterName);
    }

    @Step("Checking that Filter is Favorite")
    public boolean isFavorite(String name){
        return commonList.isFavorite(name);
    }
}
