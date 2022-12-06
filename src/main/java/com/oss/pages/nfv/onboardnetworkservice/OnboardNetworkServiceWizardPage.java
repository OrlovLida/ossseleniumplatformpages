package com.oss.pages.nfv.onboardnetworkservice;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;

public class OnboardNetworkServiceWizardPage extends OnboardWizardPage {

    private static final String WIZARD_ID = "nsOnboardWizardId";
    private static final String ONBOARD_RADIO_BUTTONS_COMPONENT_ID = "onboardTypeRadioButtonsId";
    private static final String ONBOARD_NFVO_SEARCH_FIELD_ID = "onboardNFVOSearchFieldId_OSF";
    private static final String ONBOARD_DEFAULT_REMOTE_FOLDER_TEXT_FIELD_ID = "onboardDefaultRemoteFolderTextFieldId";

    private OnboardNetworkServiceWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait, Wizard.createByComponentId(driver, wait, WIZARD_ID));
    }

    public static OnboardNetworkServiceWizardPage create(WebDriver driver, WebDriverWait wait) {
        return new OnboardNetworkServiceWizardPage(driver, wait);
    }

    public void selectRadioButton(String label) {
        getWizard().setComponentValue(ONBOARD_RADIO_BUTTONS_COMPONENT_ID, label);
        DelayUtils.waitForPageToLoad(driver, wait);
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
