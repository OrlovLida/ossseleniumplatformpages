package com.oss.pages.bigdata.dfe.thresholds;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThresholdStepWizardPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ThresholdStepWizardPage.class);

    private final ThresholdsConfigurationPage thresholdsConfigurationStep;
    private final ThresholdsDimensionsFilteringPage thresholdsDimensionsFilteringStep;
    private final Wizard thresholdsWizard;

    public ThresholdStepWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

        thresholdsWizard = Wizard.createWizard(driver, wait);
        thresholdsConfigurationStep = new ThresholdsConfigurationPage(driver, wait);
        thresholdsDimensionsFilteringStep = new ThresholdsDimensionsFilteringPage(driver, wait);
    }

    public ThresholdsConfigurationPage getThresholdsConfigurationStep() {
        return thresholdsConfigurationStep;
    }

    public ThresholdsDimensionsFilteringPage getThresholdsDimensionsFilteringStep() {
        return thresholdsDimensionsFilteringStep;
    }

    public void clickNext() {
        DelayUtils.waitForPageToLoad(driver, wait);
        thresholdsWizard.clickNext();
        log.info("I click Next Step");
    }

    public void clickAccept() {
        DelayUtils.waitForPageToLoad(driver, wait);
        thresholdsWizard.clickAccept();
        log.info("I click Accept");
    }
}
