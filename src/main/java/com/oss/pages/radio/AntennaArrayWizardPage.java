package com.oss.pages.radio;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class AntennaArrayWizardPage extends BasePage {
    public AntennaArrayWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard antennaArrayWizard = Wizard.createByComponentId(driver, wait, "antenna-array-wizard");

    @Step("Click Accept button")
    public void clickAccept() {
        antennaArrayWizard.clickAccept();
    }
}
