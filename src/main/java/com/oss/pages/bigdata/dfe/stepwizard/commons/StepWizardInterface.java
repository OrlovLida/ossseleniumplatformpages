package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.widgets.Wizard;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface StepWizardInterface {

    String getWizardId();

    default Wizard getWizard(WebDriver driver, WebDriverWait wait) {
        return Wizard.createByComponentId(driver, wait, getWizardId());
    }


}
