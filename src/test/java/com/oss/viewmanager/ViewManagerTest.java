package com.oss.viewmanager;

import com.oss.BaseTestCase;
import com.oss.framework.components.portals.ApplicationPopup;
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

    @Test(priority = 1)
    public void addNewCategoryToViewManagerTest(){
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        viewManagerPage.addCategoryButton.click();
        DelayUtils.sleep(200);
        CategoryPopup createCategoryPopup = viewManagerPage.goToCreateCategoryPopup();
        createCategoryPopup.setNameValue("Test Category");
        createCategoryPopup.setDescriptionValue("Test Category Description");
        createCategoryPopup.clickOnAdministrationPanelIcon();
        createCategoryPopup.clickOnSaveButton();

        DelayUtils.sleep(1500);

        viewManagerPage.searchForCategory("Test Category");
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Test Category']")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Test Category Description']")).isDisplayed());
    }

    @Test(priority = 2)
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

    @Test(priority = 3)
    public void addApplicationInCategory(){
        viewManagerPage.enterAddApplicationButton();
        DelayUtils.sleep(1000);
        ApplicationPopup addApplicationPopup = viewManagerPage.goToApplicationPopup();
        addApplicationPopup.setApplication("Views:GIS View");
        addApplicationPopup.setApplicationName("GIS");
        addApplicationPopup.setDescription("GIS Description");
        addApplicationPopup.clickSaveButton();

        DelayUtils.sleep(1000);

        viewManagerPage.rolloutFirstCategory();
        DelayUtils.sleep(700);
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='GIS']")).isDisplayed());
    }

    @Test(priority = 4)
    public void editApplication(){
        DelayUtils.sleep(200);
        viewManagerPage.clickButtonsGroupOnFirstApplication();
        DelayUtils.sleep(300);
        viewManagerPage.clickEditButton();
        ApplicationPopup editApplicationPopup = viewManagerPage.goToApplicationPopup();
        editApplicationPopup.setApplication("Create Sublocation");
        editApplicationPopup.setApplicationName("Sublocation");
        editApplicationPopup.setDescription("Sublocation Creation");
        editApplicationPopup.clickSaveButton();

        DelayUtils.sleep(4000);
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Sublocation']")).isDisplayed());
        String url = driver.findElement(By.xpath("//*[text()='Sublocation']")).getAttribute("href");
        Assert.assertTrue(url.contains("sublocation/create"));
    }

    @Test(priority = 5)
    public void addSecondApplication(){
        viewManagerPage.enterAddApplicationButton();
        DelayUtils.sleep(200);
        ApplicationPopup addApplicationPopup = viewManagerPage.goToApplicationPopup();
        addApplicationPopup.setApplication("Views:GIS View");
        addApplicationPopup.setApplicationName("GIS");
        addApplicationPopup.setDescription("GIS Description");
        addApplicationPopup.clickSaveButton();

        DelayUtils.sleep(4000);
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='GIS']")).isDisplayed());
    }

    @Test(priority = 6)
    public void deleteCategory(){
        viewManagerPage.deleteFirstCategory();
        DelayUtils.sleep(5000);

        Assert.assertFalse(driver.findElements(By.xpath("//*[text()='Name after edition']")).size()>0);
        viewManagerPage.clearSearchField();
    }

}
