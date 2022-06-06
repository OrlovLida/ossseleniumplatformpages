package com.oss.pages.bigdata.dfe.stepwizard.aggregate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;

public class AggregateProcessSettingsPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(AggregateProcessSettingsPage.class);

    private static final String ON_FAILURE_INPUT_ID = "failure";
    private static final String STORAGE_POLICY_INPUT_ID = "storagePolicy";
    private static final String AGGREGATE_WIZARD_ID = "aggregatesWizardWindow";
    private final Wizard processSettingsWizard;

    public AggregateProcessSettingsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        processSettingsWizard = Wizard.createByComponentId(driver, wait, AGGREGATE_WIZARD_ID);
    }

    private void fillOnFailure(String onFailure) {
        processSettingsWizard.setComponentValue(ON_FAILURE_INPUT_ID, onFailure, COMBOBOX);
        log.debug("Setting on failure: {}", onFailure);
    }

    private void fillStoragePolicy(String storagePolicy) {
        processSettingsWizard.setComponentValue(STORAGE_POLICY_INPUT_ID, storagePolicy, COMBOBOX);
        log.debug("Setting storage policy: {}", storagePolicy);
    }

    @Step("I fill Process Settings Step with onFailure: {onFailure} and storage policy: {storagePolicy}")
    public void fillProcessSettingsStep(String onFailure, String storagePolicy) {
        fillOnFailure(onFailure);
        fillStoragePolicy(storagePolicy);
        log.info("Filled Process Settings Step");
    }

    @Step("I click Next Step")
    public void clickNextStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        processSettingsWizard.clickNextStep();
        log.info("I click Next Step");
    }
}
