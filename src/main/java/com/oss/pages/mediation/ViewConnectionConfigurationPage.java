package com.oss.pages.mediation;

import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

public class ViewConnectionConfigurationPage extends BasePage {

    private final Wizard wizard;

    public ViewConnectionConfigurationPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }
}
