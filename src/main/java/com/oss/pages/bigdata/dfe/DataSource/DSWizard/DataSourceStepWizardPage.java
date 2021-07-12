package com.oss.pages.bigdata.dfe.DataSource.DSWizard;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceStepWizardPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceStepWizardPage.class);

    private final DataSourceBasicInfoPage basicInfoStep;
    private final DataSourceSourceInformationPage sourceInfoStep;
    private final DataSourceSpecificInfoPage specificInfoStep;
    private final String WIZARD_ID = "dataSourcesWizardId";

    private final Wizard wizard;
    private final String WIZARD_ID = "dataSourcesWizardId";

    public DataSourceStepWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
        basicInfoStep = new DataSourceBasicInfoPage(driver, wait);
        sourceInfoStep = new DataSourceSourceInformationPage(driver, wait);
        specificInfoStep = new DataSourceSpecificInfoPage(driver, wait);
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

    @Step("I click Next Step")
    public void clickNext() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickNext();
        log.info("I click Next Step");
    }

    @Step("I click Accept")
    public void clickAccept() {
        wizard.clickAccept();
        log.info("Finishing Step Wizard by clicking 'Accept'");
    }
}
