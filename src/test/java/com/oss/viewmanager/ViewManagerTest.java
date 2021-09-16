package com.oss.viewmanager;

import com.oss.BaseTestCase;
import com.oss.framework.navigation.ApplicationWizard;
import com.oss.framework.navigation.CategoryWizard;
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
        viewManagerPage.closeLoginPanel();
    }

    @Test(priority = 1)
    public void addNewCategoryToViewManagerTest() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        viewManagerPage.addCategoryButton.click();
        DelayUtils.sleep(200);
        CategoryWizard createCategoryWizard = viewManagerPage.goToCategoryPopup();
        createCategoryWizard.setNameValue("Test Category");
        createCategoryWizard.setDescriptionValue("Test Category Description");
        createCategoryWizard.clickOnAdministrationPanelIcon();
        createCategoryWizard.clickOnSaveButton();

        DelayUtils.sleep(2500);

        viewManagerPage.searchForCategory("Test Category");
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Test Category']")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Test Category Description']")).isDisplayed());
    }

    @Test(priority = 2)
    public void changeCategoryNameAndDescription() {
        viewManagerPage.enterEditionOfCategory();
        DelayUtils.sleep(1000);
        CategoryWizard editCategoryWizard = viewManagerPage.goToCategoryPopup();
        editCategoryWizard.deleteNameValue();
        editCategoryWizard.setNameValue("Name after edition");
        editCategoryWizard.deleteDescriptionValue();
        editCategoryWizard.setDescriptionValue("Description after edition");

        editCategoryWizard.clickOnSaveButton();
        viewManagerPage.clearSearchField();

        DelayUtils.sleep(2500);

        viewManagerPage.searchForCategory("Name after edition");
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Name after edition']")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Description after edition']")).isDisplayed());
    }

    @Test(priority = 3)
    public void addApplicationInCategory() {
        viewManagerPage.enterAddApplicationButtonInFirstMainCategory();
        DelayUtils.sleep(1000);
        ApplicationWizard addApplicationWizard = viewManagerPage.goToApplicationPopup();
        addApplicationWizard.setApplication("Legacy left menu:Views:GIS View");
        addApplicationWizard.setApplicationName("GIS");
        addApplicationWizard.setDescription("GIS Description");
        addApplicationWizard.clickSaveButton();

        DelayUtils.sleep(2000);

        viewManagerPage.rolloutFirstCategory();
        DelayUtils.sleep(2500);
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='GIS']")).isDisplayed());
    }

    @Test(priority = 4)
    public void editApplication() {
        DelayUtils.sleep(200);
        viewManagerPage.clickButtonsGroupOnFirstApplication();
        DelayUtils.sleep(300);
        viewManagerPage.clickEditButton();
        ApplicationWizard editApplicationWizard = viewManagerPage.goToApplicationPopup();
        editApplicationWizard.setApplication("Create Sublocation");
        editApplicationWizard.setApplicationName("Sublocation");
        editApplicationWizard.setDescription("Sublocation Creation");
        editApplicationWizard.clickSaveButton();

        DelayUtils.sleep(4000);
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Sublocation']")).isDisplayed());
        String url = driver.findElement(By.xpath("//*[text()='Sublocation']")).getAttribute("href");
        Assert.assertTrue(url.contains("sublocation/create"));
    }

    @Test(priority = 5)
    public void addSecondApplication() {
        viewManagerPage.enterAddApplicationButtonInFirstMainCategory();
        DelayUtils.sleep(200);
        ApplicationWizard addApplicationWizard = viewManagerPage.goToApplicationPopup();
        addApplicationWizard.setApplication("Legacy left menu:Views:GIS View");
        addApplicationWizard.setApplicationName("GIS");
        addApplicationWizard.setDescription("GIS Description");
        addApplicationWizard.clickSaveButton();

        DelayUtils.sleep(4000);
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='GIS']")).isDisplayed());
    }

    @Test(priority = 6)
    public void changePlaceOfFirstAndSecondApplication() {
        viewManagerPage.dragAndDropFirstAppInPlaceOfSecond();

        DelayUtils.sleep(2500);

        String sublocationWizardUrl = viewManagerPage.getApplicationsUrl(0);
        String gisUrl = viewManagerPage.getApplicationsUrl(1);

        Assert.assertTrue(gisUrl.contains("gis-view"));
        Assert.assertTrue(sublocationWizardUrl.contains("sublocation/create"));
    }

    @Test(priority = 7)
    public void addSubcategory(){
        viewManagerPage.enterCreateSubcategory();
        CategoryWizard subcategoryWizard = viewManagerPage.goToCategoryPopup();
        subcategoryWizard.setNameValue("Test Subcategory");
        subcategoryWizard.setDescriptionValue("Test Subcategory Description");
        subcategoryWizard.clickOnSaveButton();

        DelayUtils.sleep(3000);
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Test Subcategory']")).isDisplayed());
    }

    @Test(priority = 8)
    public void addApplicationToSubcategory(){
        viewManagerPage.enterAddApplicationButtonInFirstSubcategory();
        DelayUtils.sleep(200);
        ApplicationWizard addApplicationWizard = viewManagerPage.goToApplicationPopup();
        addApplicationWizard.setApplication("Create Cable");
        addApplicationWizard.setApplicationName("Create Cable");
        addApplicationWizard.setDescription("Create Cable Wizard");
        addApplicationWizard.clickSaveButton();

        DelayUtils.sleep(3000);
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
        viewManagerPage.searchForCategory("Name after edition");
        viewManagerPage.dragAndDropFirstAppToSubcategory();
        DelayUtils.sleep(1000);

        WebElement firstApplication = viewManagerPage.getApplication(0);
        String subLocationWizardUrl = firstApplication.getAttribute("href");

        Assert.assertTrue(subLocationWizardUrl.contains("sublocation/create"));
    }

    @Test(priority = 11)
    public void changePlaceOfTwoSubcategories(){
        viewManagerPage.enterCreateSubcategory();
        CategoryWizard subcategoryWizard = viewManagerPage.goToCategoryPopup();
        subcategoryWizard.setNameValue("Second Subcategory");
        subcategoryWizard.setDescriptionValue("Second Subcategory Description");
        subcategoryWizard.clickOnSaveButton();
        DelayUtils.sleep(2000);

        String firstSubcategoryName = viewManagerPage.getSubcategoryName(0);

        viewManagerPage.dragAndDropFirstSubcategoryToPlaceOfSecondSubcategory();
        DelayUtils.sleep(2500);

        String secondSubcategoryName = viewManagerPage.getSubcategoryName(1);

        Assert.assertEquals(secondSubcategoryName, firstSubcategoryName);
    }

    @Test(priority = 12)
    public void addQueryParamToApplication(){
        DelayUtils.sleep(200);
        viewManagerPage.clickButtonsGroupOnFirstApplication();
        DelayUtils.sleep(300);
        viewManagerPage.clickEditButton();
        ApplicationWizard editApplicationWizard = viewManagerPage.goToApplicationPopup();
        DelayUtils.sleep(300);
        editApplicationWizard.openQueryParamsTable();

        editApplicationWizard.addTestQueryParams();
        editApplicationWizard.clickSaveButton();

        DelayUtils.sleep(2500);

        WebElement firstApplication = viewManagerPage.getApplication(0);
        String subLocationWizardUrl = firstApplication.getAttribute("href");

        Assert.assertTrue(subLocationWizardUrl.contains("?testParameter=testValue"));
    }

    @Test(priority = 13)
    public void editSubcategory(){
        viewManagerPage.enterEditSubcategoryButton();
        CategoryWizard editSubcategoryWizard = viewManagerPage.goToCategoryPopup();
        editSubcategoryWizard.setNameValue("Subcategory After Edition");
        editSubcategoryWizard.setDescriptionValue("Description After Edition");
        editSubcategoryWizard.clickOnSaveButton();

        DelayUtils.sleep(2000);
        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Subcategory After Edition']")).isDisplayed());
    }

    @Test(priority = 14)
    public void deleteApplicationFromSubcategory(){
        int numberOfGroupButtonForFirstAppInFirstSubcategory = 5;
        viewManagerPage.getThreeDotsGroupButton(numberOfGroupButtonForFirstAppInFirstSubcategory).click();
        DelayUtils.sleep(300);
        viewManagerPage.clickDeleteButton();

        DelayUtils.sleep(2500);
        try {
            driver.findElement(By.xpath("//*[text()='Create Cable']"));
            Assert.fail();
        }
        catch (org.openqa.selenium.NoSuchElementException e) {
        }
    }

    @Test(priority = 15)
    public void deleteApplicationFromMainCategory(){
        DelayUtils.sleep(200);
        viewManagerPage.clickButtonsGroupOnFirstApplication();
        DelayUtils.sleep(300);
        viewManagerPage.clickDeleteButton();

        DelayUtils.sleep(2500);
        try {
            driver.findElement(By.xpath("//*[text()='Sublocation']"));
            Assert.fail();
        }
        catch (org.openqa.selenium.NoSuchElementException e) {
        }
    }

    @Test(priority = 16)
    public void deleteSubcategory(){
        viewManagerPage.removeFirstSubcategory();
        DelayUtils.sleep(200);

        try {
            driver.findElement(By.xpath("//*[text()='Subcategory After Edition']"));
            Assert.fail();
        }
        catch (org.openqa.selenium.NoSuchElementException e) {
        }
    }

    @Test(priority = 17)
    public void deleteCategory() {
        DelayUtils.sleep(1000);

        viewManagerPage.deleteFirstCategory();
        DelayUtils.sleep(3000);

        Assert.assertFalse(driver.findElements(By.xpath("//*[text()='Name after edition']")).size() > 0);
        viewManagerPage.clearSearchField();
    }
}
