package com.oss.pages.filtermanager;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.portals.DropdownList;
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
    }

    @FindBy(xpath = "//div[@data-attributename='_FilterManagerList']")
    private WebElement filterManagerList;


    private final String CATEGORY_LIST_XPATH ="//div[contains(@class, 'ExtendedList')]//li[contains(@class, 'categoryListElement')]";
    private final String ALL_FILTER_KEBABS_XPATH="//div[@class='contextActions']//div[@id='frameworkObjectButtonsGroup']";
    private final String ALL_FOLDER_KEBABS_XPATH="//div[@class='DropdownList']//div[@id='frameworkObjectButtonsGroup']";
    private final String KEBAB_ID="frameworkObjectButtonsGroup";
    private final String NEW_FOLDER_ID = "new_folder";
    private final String COMMON_LIST_APP_ID = "_FilterManagerList";

    @Step("Open Filter Manager Page")
    public static FilterManagerPage goToFilterManagerPage(WebDriver driver, String baseURL){
        driver.get(String.format("%s/#/view/management/views/filter-view" + "?perspective=LIVE", baseURL));
        return new FilterManagerPage(driver);
    }

    @Step("Delete All Filters")
    public FilterManagerPage deleteAllFilters(){
        expandAllCategories();
        deleteAllFilter();
        return this;
    }

    @Step("Delete All Folders")
    public FilterManagerPage deleteAllFolders(){
        deleteAllFolder();
        return this;
    }

    @Step("Open Edit Filter Wizard")
    public FilterManagerPage clickOnEdit(String name){
        DelayUtils.waitForPageToLoad(driver,wait);
        getEditButtonByFilterName(name).click();
        return this;
    }

    @Step("Expand all categories")
    public FilterManagerPage expandAllCategories(){
        expandAllFolders();
        return this;
    }

    @Step("Collapse all categories")
    public FilterManagerPage collapseAllCategories(){
        collapseAllFolders();
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
        expandFilterKebab(name);
        getEditButtonByFilterName(name).click();
        return this;
    }

    @Step("Delete Filter")
    public FilterManagerPage deleteFilter(String name){
        DelayUtils.waitForPageToLoad(driver, wait);
        expandFilterKebab(name);
        DelayUtils.waitForPageToLoad(driver, wait);
        chooseDelete();
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
        expandFilterKebab(name);
        chooseShare();
        return new ShareFilterPage(driver);
    }

    @Step("Open Share Folder Page")
    public ShareFilterPage openShareFolder(String folderName){
        expandFolderKebab(folderName);
        chooseShare();
        return new ShareFilterPage(driver);
    }

    @Step("Mark as a favorite")
    public FilterManagerPage markAsAFavorite(String filterName){
        DelayUtils.waitForPageToLoad(driver,wait);
        getFavoriteButtonByFilterName(filterName).click();
        return this;
    }

    private void expandFilterKebab(String filerName){
        DelayUtils.waitForPageToLoad(driver,wait);
        getFilterByName(filerName).findElement(By.xpath(".//*[contains(@id, '" + KEBAB_ID + "')]")).click();
    }

    private void expandFolderKebab(String folderName){
        DelayUtils.waitForPageToLoad(driver,wait);
        getFolderByName(folderName).findElement(By.xpath(".//*[contains(@id, '" + KEBAB_ID + "')]")).click();
    }

    private void deleteAllFilter(){
        DelayUtils.waitForPageToLoad(driver,wait);
        List<WebElement> kebabs = driver.findElements(By.xpath(ALL_FILTER_KEBABS_XPATH));
        for (int i=kebabs.size(); i>0; i--){
            DelayUtils.waitForPageToLoad(driver,wait);
            kebabs.get(i-1).click();
            DelayUtils.waitForPageToLoad(driver,wait);
            chooseDelete();
            DelayUtils.waitForPageToLoad(driver,wait);
        }
    }

    private void deleteAllFolder(){
        DelayUtils.waitForPageToLoad(driver,wait);
        List<WebElement> kebabs = driver.findElements(By.xpath(ALL_FOLDER_KEBABS_XPATH));
        for (int i=kebabs.size(); i>1; i--){
            DelayUtils.waitForPageToLoad(driver,wait);
            kebabs.get(i-1).click();
            DelayUtils.waitForPageToLoad(driver,wait);
            chooseDelete();
            DelayUtils.waitForPageToLoad(driver,wait);
        }
    }

    public FilterManagerPage expandFolder(String folderName){
        DelayUtils.waitForPageToLoad(driver,wait);
        getFolderByName(folderName).click();
        return this;
    }

    private void expandAllFolders(){
        DelayUtils.waitForPageToLoad(driver,wait);
        List<WebElement> categoryLists = driver.findElements(By.xpath(CATEGORY_LIST_XPATH + "//i[contains (@class, 'chevron-down')]"));
        for (int i=categoryLists.size(); i>0; i--) {
            categoryLists.get(i - 1).click();
        }
    }

    private void collapseAllFolders(){
        DelayUtils.waitForPageToLoad(driver,wait);
        List<WebElement> categoryLists = driver.findElements(By.xpath(CATEGORY_LIST_XPATH + "//i[contains (@class, 'chevron-up')]"));
        for (int i=categoryLists.size(); i>0; i--) {
            categoryLists.get(i - 1).click();
            DelayUtils.waitForPageToLoad(driver,wait);
        }
    }

    private WebElement getFilterByName(String name){
        return driver.findElement(By.xpath("//div[contains(@id,'name') and text()='"+ name +"']/../../../.."));
    }

    private WebElement getFolderByName(String folderName) {
        return driver.findElement(By.xpath("//div[contains(@class,'categoryLabel') and text()='" + folderName + "']/../.."));
    }

    public boolean isFilterVisible(String name){
        DelayUtils.waitForPageToLoad(driver, wait);
        return driver.findElements(By.xpath("//div[contains(@id,'name') and text()='"+ name +"']")).size()>0;
    }

    public boolean isFolderVisible(String folderName){
        return driver.findElements(By.xpath("//div[contains(@class,'categoryLabel') and text()='" + folderName + "']")).size()>0;
    }

    public boolean isEditActionVisible(String filterName){
        return driver.findElements(By.xpath("//div[contains(@id,'name') and text()='"+ filterName +"']/../../../..//button[contains(@class, 'square')]")).size()>0;
    }

    public boolean isFavorite(String name){
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.waitForVisibility(wait, getFilterByName(name));
        return getFavoriteButtonByFilterName(name).findElements(By.xpath(".//i[contains(@class, 'star-o')]")).size()==0;
    }

    public int howManyFilters(){
        return driver.findElements(By.xpath("//div[contains(@id,'name')]")).size();
    }

    public int howManyFolders(){
        return driver.findElements(By.xpath("//div[contains(@class,'categoryLabel')]")).size();
    }

    private WebElement getEditButtonByFilterName(String name){
        return getFilterByName(name).findElement(By.xpath(".//button[contains(@class, 'square')]"));
    }

    private WebElement getFavoriteButtonByFilterName(String name){
        return getFilterByName(name).findElement(By.xpath(".//button[contains(@class, 'favourite')]"));
    }

    private void chooseShare(){
        DropdownList.create(driver,wait).selectOptionWithId("share_action");
    }

    private void chooseDelete() {
        DropdownList.create(driver,wait).selectOptionWithId("remove_action");
    }

    private void expandKebabByParent(WebElement parent){
        parent.findElement(By.xpath(".//*[@id='" + parent+ "']"));
    }

}
