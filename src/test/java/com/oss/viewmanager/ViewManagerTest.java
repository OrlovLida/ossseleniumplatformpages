package com.oss.viewmanager;

import com.oss.BaseTestCase;
import com.oss.framework.components.portals.CategoryPopup;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.viewmanager.ViewManagerPage;
import com.oss.utils.TestListener;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class ViewManagerTest extends BaseTestCase {

    private ViewManagerPage viewManagerPage;

    @BeforeClass
    public void goToViewManager(){
        viewManagerPage = new ViewManagerPage(driver);
        viewManagerPage.openLoginPanel().changeSwitcherForAlphaMode();
        viewManagerPage.closeLoginPanel();
    }

    @Test
    public void addNewCategoryToViewManagerTest(){
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        viewManagerPage.addCategoryButton.click();
        DelayUtils.sleep(200);
        CategoryPopup createCategoryPopup = viewManagerPage.goToCreateCategoryPopup();
        createCategoryPopup.setNameValue("Test Category");
        createCategoryPopup.setDescriptionValue("Test Category Description");
        createCategoryPopup.clickOnFirstIcon();
        createCategoryPopup.clickOnSaveButton();

        DelayUtils.sleep(2000);

        viewManagerPage.searchForCategory("Test Category");
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Test Category']")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Test Category Description']")).isDisplayed());
    }

    @Test
    public void changeCategoryNameAndDescription(){
        viewManagerPage.enterEditionOfCategory();
        DelayUtils.sleep(1000);
        CategoryPopup editCategoryPopup = viewManagerPage.goToEditCategoryPopup();
        editCategoryPopup.deleteNameValue();
        editCategoryPopup.setNameValue("Name after edition");
        editCategoryPopup.deleteDescriptionValue();
        editCategoryPopup.setDescriptionValue("Description after edition");

        editCategoryPopup.clickOnSaveButton();
        viewManagerPage.clearSearchField();

        DelayUtils.sleep(2000);

        viewManagerPage.searchForCategory("Name after edition");
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Name after edition']")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Description after edition']")).isDisplayed());
    }

    @Test
    public void deleteCategory(){
        viewManagerPage.clearSearchField();
        viewManagerPage.searchForCategory("Name after edition");
        viewManagerPage.deleteFirstCategory();
        DelayUtils.sleep(5000);

        Assert.assertFalse(driver.findElements(By.xpath("//*[text()='Name after edition']")).size()>0);
        viewManagerPage.clearSearchField();
    }
}
