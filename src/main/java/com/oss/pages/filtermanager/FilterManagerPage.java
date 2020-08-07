package com.oss.pages.filtermanager;

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

    @FindBy(id = "new_folder")
    private WebElement folderCreateButton;


    private final String CATEGORY_LIST_XPATH ="//div[contains(@class, 'ExtendedList')]//li[contains(@class, 'categoryListElement')]";
    private final String ALL_KEBABS_XPATH="//div[@class='contextActions']//div[@id='frameworkObjectButtonsGroup']";
    private final String KEBAB_ID="frameworkObjectButtonsGroup";

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

    @Step("Open Edit Filter Wizard")
    public FilterManagerPage clickOnEdit(String name){
        DelayUtils.waitForPageToLoad(driver,wait);
        getEditButtonByFilterName(name).click();
        return this;
    }

    @Step("Expand all categories")
    public FilterManagerPage expandAllCategories(){
        expandAllCategorie();
        return this;
    }

    @Step("Open Create New Folder Wizard")
    public FilterManagerPage openCreateNewFolderWizard(){
        folderCreateButton.click();
        return this;
    }

    @Step("Delete Filter")
    public FilterManagerPage deleteFilter(String name){
        expandKebab(name);
        chooseDelete();
        return this;
    }

    private void expandKebab(String name){
        DelayUtils.waitForPageToLoad(driver,wait);
        getFilterByName(name).findElement(By.xpath(".//*[contains(@id, '" + KEBAB_ID + "')]")).click();
    }

    private void deleteAllFilter(){
        DelayUtils.waitForPageToLoad(driver,wait);
        List<WebElement> kebabs = driver.findElements(By.xpath(ALL_KEBABS_XPATH));
        for (int i=kebabs.size(); i>0; i--){
            DelayUtils.waitForPageToLoad(driver,wait);
            kebabs.get(i-1).click();
            DelayUtils.waitForPageToLoad(driver,wait);
            chooseDelete();
            DelayUtils.waitForPageToLoad(driver,wait);
        }
    }

    private void expandCategory(int categoryNumber){
        DelayUtils.waitForPageToLoad(driver,wait);
        driver.findElements(By.xpath(CATEGORY_LIST_XPATH)).get(categoryNumber-1).click();
    }

    private void expandAllCategorie(){
        DelayUtils.waitForPageToLoad(driver,wait);
        List<WebElement> categoryLists = driver.findElements(By.xpath(CATEGORY_LIST_XPATH));
        for (int i=categoryLists.size(); i>0; i--) {
            categoryLists.get(i - 1).click();
        }
    }

    private WebElement getFilterByName(String name){
        return driver.findElement(By.xpath("//div[contains(@id,'name') and text()='"+ name +"']/../../../.."));
    }

    private WebElement getEditButtonByFilterName(String name){
        return getFilterByName(name).findElement(By.xpath("//button[contains(@class, 'square')]"));
    }

    private WebElement getFavoriteButtonByFilterName(String name){
        return getFilterByName(name).findElement(By.xpath("//button[contains(@class, 'favourite')]"));
    }

    public boolean isFavorite(String name){
        DelayUtils.waitForPageToLoad(driver, wait);
        return getFavoriteButtonByFilterName(name).findElements(By.xpath(".//i[contains(@class, 'star-o')]")).size()==1;
    }

    private void chooseShare(){
        DropdownList.create(driver,wait).selectOptionWithId("share_action ");
    }

    private void chooseDelete(){
        DropdownList.create(driver,wait).selectOptionWithId("remove_action");
    }

    private WebElement getFolderByName(String folderName) {
     return driver.findElement(By.xpath("//div[contains(@class,'categoryLabel') and text()='" + folderName + "']/../.."));
    }

    private void expandKebabByParent(WebElement parent){
        parent.findElement(By.xpath(".//*[@id='" + parent+ "']"));
    }
    // public void getTextOf
   // name-0-2

    //share_action
}
