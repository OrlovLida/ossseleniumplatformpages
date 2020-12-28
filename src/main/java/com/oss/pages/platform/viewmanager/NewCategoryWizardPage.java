package com.oss.pages.platform.viewmanager;

import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

public class NewCategoryWizardPage extends BasePage {

    private final Wizard wizard;

    NewCategoryWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }
}
