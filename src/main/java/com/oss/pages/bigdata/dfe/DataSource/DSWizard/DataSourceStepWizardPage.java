package com.oss.pages.bigdata.dfe.DataSource.DSWizard;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.stepwizard.commons.StepWizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceStepWizardPage extends StepWizardPage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceStepWizardPage.class);

    private final String DATA_SOURCE_STEP_WIZARD_ID = "dataSourcesWizardWindow";
    private final DataSourceBasicInfoPage basicInfoStep;
    private final DataSourceSourceInformationPage sourceInfoStep;
    private final DataSourceSpecificInfoPage specificInfoStep;

    public DataSourceStepWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

        basicInfoStep = new DataSourceBasicInfoPage(driver, wait, getWizardId());
        sourceInfoStep = new DataSourceSourceInformationPage(driver, wait, getWizardId());
        specificInfoStep = new DataSourceSpecificInfoPage(driver, wait, getWizardId());
    }

    public DataSourceBasicInfoPage getBasicInfoStep() {
        return basicInfoStep;
    }

    public DataSourceSourceInformationPage getSourceInfoStep() {
        return sourceInfoStep;
    }

    public DataSourceSpecificInfoPage getSpecificInfoStep() {
        return specificInfoStep;
    }

    @Override
    public String getWizardId() {
        return DATA_SOURCE_STEP_WIZARD_ID;
    }

    @Step("I click Next Step")
    public void clickNextStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep();
        Button.create(driver, "Next").click();
    }

    @Step("I click Accept")
    public void clickAccept() {
        getWizard(driver, wait).clickAccept();
        log.info("Finishing Step Wizard by clicking 'Accept'");
    }
}
