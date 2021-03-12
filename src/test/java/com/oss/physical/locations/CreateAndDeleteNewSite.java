package com.oss.physical.locations;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.LocationWizardPage;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateAndDeleteNewSite extends BaseTestCase {

    private final String locationName = "Site_ptest1_"+new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());

    @Test
    public void runCreateAndDeleteNewSite() {
        String locationTypeSite="Site";

        homePage.chooseFromLeftSideMenu("Create Location", "Wizards", "Physical Inventory");
        createLocation(locationTypeSite, locationName);
        Assert.assertTrue(SystemMessageContainer.create(driver, webDriverWait)
                .getMessages().get(0).getText().contains("Location has been created successfully"));

        DelayUtils.sleep(10000);
    }

    @Step("Create Location with mandatory fields (Location type, Name, Address) filled in")
    public void createLocation(String locationType, String locationName) {
        LocationWizardPage locationWizardPage = new LocationWizardPage(driver);
        locationWizardPage.setLocationType(locationType);
        locationWizardPage.setLocationName(locationName);
        locationWizardPage.clickNext();
        locationWizardPage.setGeographicalAddress("War");
        DelayUtils.sleep();
        locationWizardPage.clickNext();
        locationWizardPage.accept();
    }
}
