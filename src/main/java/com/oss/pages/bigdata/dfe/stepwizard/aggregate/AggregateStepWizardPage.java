package com.oss.pages.bigdata.dfe.stepwizard.aggregate;

import com.oss.pages.bigdata.dfe.stepwizard.commons.StepWizardPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AggregateStepWizardPage extends StepWizardPage {

    final private String AGGREGATE_STEP_WIZARD_ID = "aggregatesWizardWindow";
    final private AggregatesBasicInformationPage basicInformationStep;
    final private AggregateProcessSettingsPage processSettingsStep;
    final private AggregateConfigurationPage aggregatesConfigurationStep;

    public AggregateStepWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

        basicInformationStep = new AggregatesBasicInformationPage(driver, wait, getWizardId());
        processSettingsStep = new AggregateProcessSettingsPage(driver, wait, getWizardId());
        aggregatesConfigurationStep = new AggregateConfigurationPage(driver, wait);
    }

    public AggregatesBasicInformationPage getBasicInformationStep() {
        return basicInformationStep;
    }

    public AggregateProcessSettingsPage getProcessSettingsStep() {
        return processSettingsStep;
    }

    public AggregateConfigurationPage getAggregatesConfigurationStep() {
        return aggregatesConfigurationStep;
    }

    @Override
    public String getWizardId() {
        return AGGREGATE_STEP_WIZARD_ID;
    }
}
