package com.oss.viewmanager;

import com.oss.BaseTestCase;
import com.oss.framework.components.portals.CreateCategoryPopup;
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
        CreateCategoryPopup popup = viewManagerPage.goToCreateCategoryPopup();
        popup.setNameValue("Test Category");
        popup.setDescriptionValue("Test Category Description");
        popup.clickOnFirstIcon();
        popup.clickOnSaveButton();

        DelayUtils.sleep(1000);

        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Test Category']")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Test Category Description']")).isDisplayed());
    }

    @Test
    public void deleteCategory(){

        viewManagerPage.searchForCategory("Test Category");
        viewManagerPage.deleteFirstCategory();

        DelayUtils.sleep(1000);

        Assert.assertFalse(driver.findElements(By.xpath("//*[text()='Test Category']")).size()>0);
    }
}
