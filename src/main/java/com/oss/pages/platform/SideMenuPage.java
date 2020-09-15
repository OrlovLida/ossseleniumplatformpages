package com.oss.pages.platform;

import com.oss.framework.sidemenu.SideMenu;
import com.oss.pages.BasePage;
import com.oss.pages.physical.LocationWizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class SideMenuPage extends BasePage {

    public SideMenuPage (WebDriver driver) {
        super(driver);
    }

    @Step("Choose Create Location from Left Side Menu")
    public LocationWizardPage chooseCreateLocation() {
        SideMenu sideMenu = new SideMenu(driver, wait);
        sideMenu.callActionByLabel("Create Location", "Wizards", "Physical Inventory");
        return new LocationWizardPage(driver);
    }

}
