//package com.oss.viewmanager;
//
//import com.oss.BaseTestCase;
//import com.oss.framework.navigation.ApplicationWizard;
//import com.oss.framework.navigation.CategoryWizard;
//import com.oss.framework.utils.DelayUtils;
//import com.oss.pages.platform.viewmanager.ViewManagerPage;
//import com.oss.utils.TestListener;
//import org.openqa.selenium.By;
//import org.testng.Assert;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//@Listeners({TestListener.class})
//public class ViewManagerTest extends BaseTestCase {
//
//    private ViewManagerPage viewManagerPage;
//
//    @BeforeClass
//    public void goToViewManager() {
//        viewManagerPage = new ViewManagerPage(driver);
//        viewManagerPage.closeLoginPanel();
//    }
//
//    @Test(priority = 1)
//    public void addNewCategoryToViewManagerTest() {
//        DelayUtils.waitForPageToLoad(driver, webDriverWait);
//        viewManagerPage.clickAddCategoryButton();
//        DelayUtils.sleep(200);
//        CategoryWizard createCategoryWizard = viewManagerPage.goToCategoryPopup();
//        createCategoryWizard.setNameValue("Test Category");
//        createCategoryWizard.setDescriptionValue("Test Category Description");
////        createCategoryWizard.clickOnAdministrationPanelIcon();   tutaj do poprawy z selectIcon i id ikonki konkretnym
//        createCategoryWizard.clickOnSaveButton();
//
//        DelayUtils.sleep(2500);
//
//        viewManagerPage.searchForCategory("Test Category");
//        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Test Category']")).isDisplayed());
//        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Test Category Description']")).isDisplayed());
//    }
//
//    @Test(priority = 2)
//    public void changeCategoryNameAndDescription() {
//        viewManagerPage.clearSearchField();
//        DelayUtils.sleep(1000);
//        viewManagerPage.openEditionOfCategory("Test Category");
//        CategoryWizard editCategoryWizard = viewManagerPage.goToCategoryPopup();
//        editCategoryWizard.cleanValue();
//        editCategoryWizard.setNameValue("Name after edition");
//        editCategoryWizard.cleanDescriptionValue();
//        editCategoryWizard.setDescriptionValue("Description after edition");
//
//        editCategoryWizard.clickOnSaveButton();
//        viewManagerPage.clearSearchField();
//
//        DelayUtils.sleep(2500);
//
//        viewManagerPage.searchForCategory("Name after edition");
//        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Name after edition']")).isDisplayed());
//        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Description after edition']")).isDisplayed());
//    }
//
//    @Test(priority = 3)
//    public void addApplicationInCategory() {
//        viewManagerPage.clearSearchField();
//        DelayUtils.sleep(1000);
//        ApplicationWizard addApplicationWizard = viewManagerPage.enterAddApplicationWizardInCategory("Name after edition", "XXXXXXX");
//
//        addApplicationWizard.setApplication("Legacy left menu:Views:GIS View");
//        addApplicationWizard.setApplicationName("GIS");
//        addApplicationWizard.setDescription("GIS Description");
//        addApplicationWizard.clickSaveButton();
//
//        DelayUtils.sleep(2000);
//
//        viewManagerPage.expandCategory("Name after edition");
//        DelayUtils.sleep(2500);
//        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='GIS']")).isDisplayed());
//    }
//
//    @Test(priority = 4)
//    public void editApplication() {
//        DelayUtils.sleep(200);
//        ApplicationWizard editApplicationWizard = viewManagerPage.enterEditionOfApplication("Name after edition", "GIS");
//        editApplicationWizard.setApplication("Create Sublocation");
//        editApplicationWizard.setApplicationName("Sublocation");
//        editApplicationWizard.setDescription("Sublocation Creation");
//        editApplicationWizard.clickSaveButton();
//
//        DelayUtils.sleep(4000);
//        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Sublocation']")).isDisplayed());
//        String url = driver.findElement(By.xpath("//*[text()='Sublocation']")).getAttribute("href");
//        Assert.assertTrue(url.contains("sublocation/create"));
//    }
//
//    @Test(priority = 5)
//    public void addSecondApplication() {
//        ApplicationWizard addApplicationWizard = viewManagerPage.enterAddApplicationWizardInCategory("Name after edition", "XXXXXXX");
//        addApplicationWizard.setApplication("Legacy left menu:Views:GIS View");
//        addApplicationWizard.setApplicationName("GIS");
//        addApplicationWizard.setDescription("GIS Description");
//        addApplicationWizard.clickSaveButton();
//
//        DelayUtils.sleep(4000);
//        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='GIS']")).isDisplayed());
//    }
//
//    @Test(priority = 6)
//    public void changePlaceOfFirstAndSecondApplication() {
//        viewManagerPage.dragAndDropFirstAppInPlaceOfSecond();
//
//        DelayUtils.sleep(2500);
//
//        String sublocationApplicationUrl = viewManagerPage.getApplicationUrl("Sublocation");
//        String gisUrl = viewManagerPage.getApplicationUrl("GIS");
//
//        Assert.assertTrue(gisUrl.contains("gis-view"));
//        Assert.assertTrue(sublocationApplicationUrl.contains("sublocation/create"));
//    }
//
//    @Test(priority = 7)
//    public void addSubcategory() {
//        viewManagerPage.openCreateSubcategoryInMainCategory("Name after edition");
//        CategoryWizard subcategoryWizard = viewManagerPage.goToCategoryPopup();
//        subcategoryWizard.setNameValue("Test Subcategory");
//        subcategoryWizard.setDescriptionValue("Test Subcategory Description");
//        subcategoryWizard.clickOnSaveButton();
//
//        DelayUtils.sleep(3000);
//        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Test Subcategory']")).isDisplayed());
//    }
//
//    @Test(priority = 8)
//    public void addApplicationToSubcategory() {
//        viewManagerPage.enterAddApplicationButtonInSubcategory("Test Subcategory");
//        DelayUtils.sleep(200);
//        ApplicationWizard addApplicationWizard = viewManagerPage.goToApplicationPopup();
//        addApplicationWizard.setApplication("Create Cable");
//        addApplicationWizard.setApplicationName("Create Cable");
//        addApplicationWizard.setDescription("Create Cable Wizard");
//        addApplicationWizard.clickSaveButton();
//
//        DelayUtils.sleep(3000);
//        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Create Cable']")).isDisplayed());
//    }
//
//    @Test(priority = 9)
//    public void changePlacesOfTwoMainCategories() {
//        viewManagerPage.clearSearchField();
//        String firstCategory = viewManagerPage.getMainCategoryName(0);
//
//        viewManagerPage.dragAndDropFirstCategoryInPlaceOfSecond();
//
//        String secondCategory = viewManagerPage.getMainCategoryName(1);
//        Assert.assertEquals(firstCategory, secondCategory);
//    }
//
//    @Test(priority = 10)
//    public void dragOutApplicationOutOfSubcategory() {
//        viewManagerPage.searchForCategory("Name after edition");
//        viewManagerPage.dragAndDropFirstAppToSubcategory();
//        DelayUtils.sleep(1000);
//
//        String subLocationWizardUrl = viewManagerPage.getApplicationUrl("Sublocation");
//
//        Assert.assertTrue(subLocationWizardUrl.contains("sublocation/create"));
//    }
//
//    @Test(priority = 11)
//    public void changePlaceOfTwoSubcategories() {
//        viewManagerPage.openCreateSubcategoryInMainCategory("Name after edition");
//        CategoryWizard subcategoryWizard = viewManagerPage.goToCategoryPopup();
//        subcategoryWizard.setNameValue("Second Subcategory");
//        subcategoryWizard.setDescriptionValue("Second Subcategory Description");
//        subcategoryWizard.clickOnSaveButton();
//        DelayUtils.sleep(2000);
//
//        String firstSubcategoryName = viewManagerPage.getSubcategoryName(0);
//
//        viewManagerPage.dragAndDropFirstSubcategoryToPlaceOfSecondSubcategory();
//        DelayUtils.sleep(2500);
//
//        String secondSubcategoryName = viewManagerPage.getSubcategoryName(1);
//
//        Assert.assertEquals(secondSubcategoryName, firstSubcategoryName);
//    }
//
//    @Test(priority = 12)
//    public void addQueryParamToApplication() {
//        DelayUtils.sleep(200);
//        viewManagerPage.clickButtonsGroupOnFirstApplication();
//        DelayUtils.sleep(300);
//        viewManagerPage.clickEditButton();
//        ApplicationWizard editApplicationWizard = viewManagerPage.goToApplicationPopup();
//        DelayUtils.sleep(300);
//        editApplicationWizard.openQueryParamsTable();
//
//        editApplicationWizard.addQueryParam("testParameter", "testValue");
//        editApplicationWizard.clickSaveButton();
//
//        DelayUtils.sleep(2500);
//
//        String subLocationWizardUrl = viewManagerPage.getApplicationUrl("Sublocation");
//        Assert.assertTrue(subLocationWizardUrl.contains("?testParameter=testValue"));
//    }
//
//    @Test(priority = 13)
//    public void editSubcategory() {
//        viewManagerPage.openEditSubcategory("Second Subcategory");
//        CategoryWizard editSubcategoryWizard = viewManagerPage.goToCategoryPopup();
//        editSubcategoryWizard.setNameValue("Subcategory After Edition");
//        editSubcategoryWizard.setDescriptionValue("Description After Edition");
//        editSubcategoryWizard.clickOnSaveButton();
//
//        DelayUtils.sleep(2000);
//        Assert.assertTrue(driver.findElement(By.xpath("//*[text()='Subcategory After Edition']")).isDisplayed());
//    }
//
//    @Test(priority = 14)
//    public void deleteApplicationFromSubcategory() {
//        viewManagerPage.clickApplicationGroupButton("Create Cable");
//        DelayUtils.sleep(300);
//        viewManagerPage.clickDelete();
//
//        DelayUtils.sleep(2500);
//        try {
//            driver.findElement(By.xpath("//*[text()='Create Cable']"));
//            Assert.fail();
//        } catch (org.openqa.selenium.NoSuchElementException e) {
//        }
//    }
//
//    @Test(priority = 15)
//    public void deleteApplicationFromMainCategory() {
//        DelayUtils.sleep(200);
//        viewManagerPage.clickButtonsGroupOnFirstApplication();
//        DelayUtils.sleep(300);
//        viewManagerPage.clickDelete();
//
//        DelayUtils.sleep(2500);
//        try {
//            driver.findElement(By.xpath("//*[text()='Sublocation']"));
//            Assert.fail();
//        } catch (org.openqa.selenium.NoSuchElementException e) {
//        }
//    }
//
//    @Test(priority = 16)
//    public void deleteSubcategory() {
//        viewManagerPage.removeSubcategory("Subcategory After Edition");
//        DelayUtils.sleep(1000);
//
//        try {
//            driver.findElement(By.xpath("//*[text()='Subcategory After Edition']"));
//            Assert.fail();
//        } catch (org.openqa.selenium.NoSuchElementException e) {
//        }
//    }
//
//    @Test(priority = 17)
//    public void deleteCategory() {
//        DelayUtils.sleep(1000);
//
//        viewManagerPage.deleteTestCategory();
//        DelayUtils.sleep(3000);
//
//        Assert.assertFalse(driver.findElements(By.xpath("//*[text()='Name after edition']")).size() > 0);
//        viewManagerPage.clearSearchField();
//    }
//}
