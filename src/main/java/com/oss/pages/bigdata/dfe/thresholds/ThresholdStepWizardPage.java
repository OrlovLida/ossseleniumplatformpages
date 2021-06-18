package com.oss.pages.bigdata.dfe.thresholds;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.stepwizard.commons.StepWizardPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ThresholdStepWizardPage extends StepWizardPage {

    private final String THRESHOLD_STEP_WIZARD_ID = "thresholdWizardWindowId";
    private final ThresholdsConfigurationPage thresholdsConfigurationStep;
    private final ThresholdsDimensionsFilteringPage thresholdsDimensionsFilteringStep;

    public ThresholdStepWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

        thresholdsConfigurationStep = new ThresholdsConfigurationPage(driver, wait, getWizardId());
        thresholdsDimensionsFilteringStep = new ThresholdsDimensionsFilteringPage(driver, wait, getWizardId());
    }

    public ThresholdsConfigurationPage getThresholdsConfigurationStep() {
        return thresholdsConfigurationStep;
    }

    public ThresholdsDimensionsFilteringPage getThresholdsDimensionsFilteringStep() {
        return thresholdsDimensionsFilteringStep;
    }

    public void clickNext() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep();
        Button.create(driver, "Next").click();
    }

    public void clickAccept() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep();
        Button.create(driver, "Accept").click();
    }

    @Override
    public String getWizardId() {
        return THRESHOLD_STEP_WIZARD_ID;
    }
}
