package com.oss;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.SideMenuPage;
import com.oss.utils.RandomGenerator;
import com.oss.utils.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class ThreeUKRegressionTests extends BaseTestCase {

    String randomLocationName = RandomGenerator.generateRandomName();
    String locationType = "Site";

    @BeforeMethod
    public void goToInventoryView() {
        HomePage homePage = HomePage.goToHomePage(driver, BASIC_URL);
    }


    @Test(groups = {"Physical tests"})
    public void tS01CreateNewSiteSideMenu() {

        new SideMenuPage(driver)
                .chooseCreateLocation()
                .createLocation(locationType, randomLocationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait).getMessages().get(0).getText().contains("Location has been created successfully"));
    }

    @DataProvider
    public Object[][] getObjectType() {
        return new Object[][]{
                {"Location"},
                {"Site"}
        };
    }

    @Test(groups = {"Physical tests"}, dataProvider = "getObjectType")
    public void tS02BrowseSiteInInventoryView(String objectTypes) {
        new HomePage(driver)
                .typeObjectType(objectTypes)
                .confirmObjectType(objectTypes);
     //           .filterObjectName(randomLocationName); //waiting for Ewa's change (inventory view)

    }
}

