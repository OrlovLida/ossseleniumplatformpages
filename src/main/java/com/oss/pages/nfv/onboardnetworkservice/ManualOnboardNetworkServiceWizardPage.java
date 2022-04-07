package com.oss.pages.nfv.onboardnetworkservice;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.wizard.Wizard;

public class ManualOnboardNetworkServiceWizardPage extends OnboardWizardPage {

    private static final String WIZARD_ID = "nsManualOnboardWizardId";
    private static final String ONBOARD_NFVO_SEARCH_FIELD_ID = "manualOnboardNFVOSearchFieldId";
    private static final String ONBOARD_DEFAULT_REMOTE_FOLDER_TEXT_FIELD_ID = "manualOnboardDefaultRemoteFolderTextFieldId";

    private ManualOnboardNetworkServiceWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait, Wizard.createByComponentId(driver, wait, WIZARD_ID));
    }

    public static ManualOnboardNetworkServiceWizardPage create(WebDriver driver, WebDriverWait wait) {
        return new ManualOnboardNetworkServiceWizardPage(driver, wait);
    }

    @Override
    public String getNFVOSearchFieldIdId() {
        return ONBOARD_NFVO_SEARCH_FIELD_ID;
    }

    @Override
    public String getDefaultRemoteFolderTextFieldId() {
        return ONBOARD_DEFAULT_REMOTE_FOLDER_TEXT_FIELD_ID;
    }
}
