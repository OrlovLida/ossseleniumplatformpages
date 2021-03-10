package com.oss.pages.bigdata.dfe.stepwizard.aggregate;

import com.oss.framework.widgets.Wizard;
import com.oss.pages.bigdata.dfe.stepwizard.commons.BasicInformationPage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.StepWizardPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AggregateStepWizardPage extends StepWizardPage {

    final private String AGGREGATE_STEP_WIZARD_ID = "aggregatesWizardWindow";
    final private BasicInformationPage basicInformationStep;
    final private AggregateProcessSettingsPage processSettingsStep;
    final private AggregateConfigurationPage aggregatesConfigurationStep;

    public AggregateStepWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

        wizard = Wizard.createByComponentId(driver, wait, AGGREGATE_STEP_WIZARD_ID);
        basicInformationStep = new BasicInformationPage(driver, wait, wizard);
        processSettingsStep = new AggregateProcessSettingsPage(driver, wait, wizard);
        aggregatesConfigurationStep = new AggregateConfigurationPage(driver, wait);
    }


    public BasicInformationPage getBasicInformationStep() {
        return basicInformationStep;
    }

    public AggregateProcessSettingsPage getProcessSettingsStep() {
        return processSettingsStep;
    }

    public AggregateConfigurationPage getAggregatesConfigurationStep() {
        return aggregatesConfigurationStep;
    }
}
