package com.oss.pages.transport.regulatoryLicense;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class RegulatoryLicenseLocationsWizardPage extends BasePage {
    private static final String LOCATION_ID = "assign-regulatory-license-assignment-object-field";

    private final Wizard wizard;

    public RegulatoryLicenseLocationsWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, "assign-regulatory-license-view");
    }

    @Step("Set Location value to {location}")
    public void selectLocation(String location) {
        //Input locationComponent = wizard.getComponent(LOCATION_ID, Input.ComponentType.SEARCH_FIELD);
        //locationComponent.clear();
        //locationComponent.setSingleStringValue(location);
        wizard.setComponentValue(LOCATION_ID, location, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Click accept button")
    public RegulatoryLicenseOverviewPage clickAccept() {
        wizard.clickAcceptOldWizard();
        DelayUtils.waitForPageToLoad(driver, wait);
        return new RegulatoryLicenseOverviewPage(driver);
    }
}
