package com.oss.pages.filtermanager;

import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class FilterManagerPage extends BasePage {

    public FilterManagerPage(WebDriver driver) {
        super(driver);
    }

    private final String NEW_FOLDER_ID = "new_folder";
    private final String COMMON_LIST_APP_ID = "_FilterManagerList";

    private CommonList getCommonList() {
        return CommonList.create(driver, wait, COMMON_LIST_APP_ID);
    }

    @Step("Open Filter Manager Page")
    public static FilterManagerPage goToFilterManagerPage(WebDriver driver, String baseURL) {
        driver.get(String.format("%s/#/view/management/views/filter-view" + "?perspective=LIVE", baseURL));
        return new FilterManagerPage(driver);
    }

    @Step("Delete All Filters")
    public FilterManagerPage deleteAllFilters() {
        expandAllCategories();
        getCommonList().deleteAllListElements();
        return this;
    }

    @Step("Delete All Folders")
    public FilterManagerPage deleteAllFolders() {
        getCommonList().deleteAllCategories();
        return this;
    }

    @Step("Open Edit Filter Wizard")
    public FilterManagerPage clickOnEdit(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().clickOnEditButtonByListElementName(name);
        return this;
    }

    @Step("Expand all categories")
    public FilterManagerPage expandAllCategories() {
        getCommonList().expandAllCategories();
        return this;
    }

    @Step("Collapse all categories")
    public FilterManagerPage collapseAllCategories() {
        getCommonList().collapseAllCategories();
        return this;
    }

    @Step("Create Folder")
    public FilterManagerPage createFolder(String name) {
        return openCreateNewFolderWizard().typeNameOfTheFolder(name).clickAccept();
    }

    @Step("Open Create New Folder Wizard")
    public CreateFolderWizard openCreateNewFolderWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().getActionsContainer().callActionById(NEW_FOLDER_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new CreateFolderWizard(driver);
    }

    @Step("Edit Filter")
    public FilterManagerPage editFilter(String name) {
        getCommonList().expandListElementKebab(name);
        getCommonList().clickOnEditButtonByListElementName(name);
        return this;
    }

    @Step("Delete Filter")
    public FilterManagerPage deleteFilter(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().expandListElementKebab(name);
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().chooseDelete();
        return this;
    }

    @Step("Share Filter for user")
    public FilterManagerPage shareFilter(String filterName, String userName, String permission) {
        openShareFilter(filterName)
                .typeUserNameInSearch(userName)
                .shareForUser(userName, permission)
                .closeShareView();
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Share Folder for user")
    public FilterManagerPage shareFolder(String folderName, String userName) {
        openShareFolder(folderName)
                .typeUserNameInSearch(userName)
                .shareForUser(userName, "R")
                .closeShareView();
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    @Step("Open Share Filter Page")
    public ShareFilterPage openShareFilter(String name) {
        getCommonList().expandListElementKebab(name);
        getCommonList().chooseShare();
        return new ShareFilterPage(driver);
    }

    @Step("Open Share Folder Page")
    public ShareFilterPage openShareFolder(String folderName) {
        getCommonList().expandCategoryKebab(folderName);
        getCommonList().chooseShare();
        return new ShareFilterPage(driver);
    }

    @Step("Mark as a favorite")
    public FilterManagerPage markAsAFavorite(String filterName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().clickOnFavoriteButtonByListElementName(filterName);
        return this;
    }

    @Step("Expand Folder")
    public FilterManagerPage expandFolder(String folderName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().clickOnCategoryByName(folderName);
        return this;
    }

    @Step("How many Filters")
    public int howManyFilters() {
        return getCommonList().howManyListElements();
    }

    @Step("How many Folders")
    public int howManyFolders() {
        return getCommonList().howManyCategories();
    }

    @Step("Checking that Filter is Visible")
    public boolean isFilterVisible(String filterName) {
        return getCommonList().isListElementVisible(filterName);
    }

    @Step("Checking that Folder is Visible")
    public boolean isFolderVisible(String folderName) {
        return getCommonList().isCategoryVisible(folderName);
    }

    @Step("Checking that Edit Action is Visible")
    public boolean isEditActionVisible(String filterName) {
        return getCommonList().isEditActionVisible(filterName);
    }

    @Step("Checking that Filter is Favorite")
    public boolean isFavorite(String name) {
        return getCommonList().isFavorite(name);
    }
}
