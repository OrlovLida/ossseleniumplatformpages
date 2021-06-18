package com.oss.viewmanager;

import com.oss.BaseTestCase;
import com.oss.framework.components.portals.ApplicationPopup;
import com.oss.framework.components.portals.CategoryPopup;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.viewmanager.ViewManagerPage;
import com.oss.utils.TestListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class ViewManagerTest extends BaseTestCase {

    private ViewManagerPage viewManagerPage;

    @BeforeClass
    public void goToViewManager() {
        viewManagerPage = new ViewManagerPage(driver);
        viewManagerPage.openLoginPanel().changeSwitcherForAlphaMode();
        viewManagerPage.closeLoginPanel();
    }

    @Test(priority = 1)
    public void addNewCategoryToViewManagerTest() {
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
    public void changeCategoryNameAndDescription() {
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
    public void addApplicationInCategory() {
        viewManagerPage.enterAddApplicationButtonInFirstMainCategory();
        DelayUtils.sleep(1000);
        ApplicationPopup addApplicationPopup = viewManagerPage.goToApplicationPopup();
        addApplicationPopup.setApplication("Views:GIS View");
        addApplicationPopup.setApplicationName("GIS");
        addApplicationPopup.setDescription("GIS Description");
        addApplicationPopup.clickSaveButton();

        DelayUtils.sleep(1000);

        viewManagerPage.rolloutFirstCategory();
        DelayUtils.sleep(1500);
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='GIS']")).isDisplayed());
    }

    @Test(priority = 4)
    public void editApplication() {
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
    public void addSecondApplication() {
        viewManagerPage.enterAddApplicationButtonInFirstMainCategory();
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
    public void changePlaceOfFirstAndSecondApplication() {
        viewManagerPage.dragAndDropFirstAppInPlaceOfSecond();

        DelayUtils.sleep(1000);

        WebElement firstApplication = viewManagerPage.getApplication(0);
        WebElement secondApplication = viewManagerPage.getApplication(1);

        String gisUrl = secondApplication.getAttribute("href");
        String sublocationWizardUrl = firstApplication.getAttribute("href");

        Assert.assertTrue(gisUrl.contains("gis-view"));
        Assert.assertTrue(sublocationWizardUrl.contains("sublocation/create"));
    }

    @Test(priority = 7)
    public void addSubcategory(){
        viewManagerPage.enterCreateSubcategory();
        CategoryPopup subcategoryPopup = viewManagerPage.goToCreateCategoryPopup();
        subcategoryPopup.setNameValue("Test Subcategory");
        subcategoryPopup.setDescriptionValue("Test Subcategory Description");
        subcategoryPopup.clickOnSaveButton();

        DelayUtils.sleep(1500);
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Test Subcategory']")).isDisplayed());
    }

    @Test(priority = 8)
    public void addApplicationToSubcategory(){
        viewManagerPage.enterAddApplicationButtonInFirstSubcategory();
        DelayUtils.sleep(200);
        ApplicationPopup addApplicationPopup = viewManagerPage.goToApplicationPopup();
        addApplicationPopup.setApplication("Create Cable");
        addApplicationPopup.setApplicationName("Create Cable");
        addApplicationPopup.setDescription("Create Cable Wizard");
        addApplicationPopup.clickSaveButton();

        DelayUtils.sleep(2000);
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Create Cable']")).isDisplayed());
    }

    @Test(priority = 9)
    public void changePlacesOfTwoMainCategories(){
        viewManagerPage.clearSearchField();
        String firstCategory = viewManagerPage.getCategoryName(0);

        viewManagerPage.dragAndDropFirstCategoryInPlaceOfSecond();

        String secondCategory = viewManagerPage.getCategoryName(1);
        Assert.assertEquals(firstCategory, secondCategory);
    }

    @Test(priority = 10)
    public void dragOutApplicationOutOfSubcategory(){
        viewManagerPage.dragAndDropFirstAppToSubcategory();
        DelayUtils.sleep(1000);

        WebElement firstApplication = viewManagerPage.getApplication(0);
        String subLocationWizardUrl = firstApplication.getAttribute("href");

        Assert.assertTrue(subLocationWizardUrl.contains("sublocation/create"));
    }

    @Test(priority = 11)
    public void deleteCategory() {
        viewManagerPage.searchForCategory("Name after edition");
        DelayUtils.sleep(1000);

        viewManagerPage.deleteFirstCategory();
        DelayUtils.sleep(3000);

        Assert.assertFalse(driver.findElements(By.xpath("//*[text()='Name after edition']")).size() > 0);
        viewManagerPage.clearSearchField();
    }
}
