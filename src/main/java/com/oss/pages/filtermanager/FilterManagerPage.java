package com.oss.pages.filtermanager;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.categorylist.CategoryList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class FilterManagerPage extends BasePage {

    public FilterManagerPage(WebDriver driver) {
        super(driver);
    }

    private static final String NEW_FOLDER_ID = "new_folder";
    private static final String COMMON_LIST_APP_ID = "_FilterManagerList";
    private static final String REMOVE_ACTION_ID = "remove_action";

    @Step("Open Filter Manager Page")
    public static FilterManagerPage goToFilterManagerPage(WebDriver driver, String baseURL) {
        driver.get(String.format("%s/#/view/management/views/filter-view" + "?perspective=LIVE", baseURL));
        return new FilterManagerPage(driver);
    }

    @Step("Delete All Filters")
    public FilterManagerPage deleteAllFilters() {
        expandAllCategories();
        getCommonList().getRows().forEach(row -> row.callAction(REMOVE_ACTION_ID));
        return this;
    }

    @Step("Delete All Folders")
    public FilterManagerPage deleteAllFolders() {
        List<CategoryList> categories =
                getCommonList().getCategories().stream().filter(category -> !category.getValue().equals("Uncategorized"))
                        .collect(Collectors.toList());
        categories.forEach(category -> category.callAction(REMOVE_ACTION_ID));
        return this;
    }

    @Step("Open Edit Filter Wizard")
    public FilterManagerPage clickOnEdit(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonList.Row row = getCommonList().getRow("name", name);
        row.callAction("Edit");

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
        getCommonList().callAction(NEW_FOLDER_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new CreateFolderWizard(driver);
    }

    @Step("Edit Filter")
    public FilterManagerPage editFilter(String name) {
        getCommonList().getRow("name", name).callAction("Edit");
        return this;
    }

    @Step("Delete Filter")
    public FilterManagerPage deleteFilter(String name) {
        getCommonList().getRow("name", name).callAction(REMOVE_ACTION_ID);
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
    private ShareFilterPage openShareFilter(String name) {
        getCommonList().getRow("name", name).callAction("share_action");
        return new ShareFilterPage(driver);
    }

    @Step("Open Share Folder Page")
    private ShareFilterPage openShareFolder(String folderName) {
        getCommonList().getCategory(folderName).callAction("share_action");
        return new ShareFilterPage(driver);
    }

    @Step("Mark as a favorite")
    public FilterManagerPage markAsAFavorite(String filterName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().getRow("name", filterName).setFavorite();
        return this;
    }

    @Step("Expand Folder")
    public FilterManagerPage expandFolder(String folderName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getCommonList().expandCategory(folderName);
        return this;
    }

    @Step("How many Filters")
    public int howManyFilters() {
        return getCommonList().countRows();
    }

    @Step("How many Folders")
    public int howManyFolders() {
        return getCommonList().countCategories();
    }

    @Step("Checking that Filter is Visible")
    public boolean isFilterVisible(String filterName) {
        return getCommonList().isRowDisplayed("name", filterName);
    }

    @Step("Checking that Folder is Visible")
    public boolean isFolderVisible(String folderName) {
        return getCommonList().isCategoryDisplayed(folderName);
    }

    @Step("Checking that Edit Action is Visible")
    public boolean isEditActionVisible(String filterName) {
        return getCommonList().getRow("name", filterName).isActionIconPresent("Edit");
    }

    @Step("Checking that Filter is Favorite")
    public boolean isFavorite(String name) {
        return getCommonList().getRow("name", name).isFavorite();
    }

    private CommonList getCommonList() {
        return CommonList.create(driver, wait, COMMON_LIST_APP_ID);
    }
}
