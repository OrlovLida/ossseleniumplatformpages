package com.oss.pages.nfv.onboardvnf;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

public class OnboardVNFWizardPage extends BasePage {

    private static final String WIZARD_ID = "onboardWizardId";
    private static final String ONBOARD_RADIO_BUTTONS_COMPONENT_ID = "onboardTypeRadioButtonsId";
    private static final String ONBOARD_DOWNLOAD_NFVO_COMPONENT_ID = "onboardDownloadNFVOSearchFieldId";
    private static final String ONBOARD_DOWNLOAD_VNFPKG_COMPONENT_ID = "onboardDownloadVNFPKGSearchFieldId";
    private static final String ONBOARD_NFVO_COMPONENT_ID = "onboardNFVOSearchFieldId";
    private static final String ONBOARD_MARKETPLACE_COMPONENT_ID = "onboardMarketplaceSearchFieldId";
    private static final String ONBOARD_VIM_SEARCH_COMPONENT_ID = "onboardVIMSearchFieldId";
    private static final String ONBOARD_DEFAULT_REMOTE_FOLDER_COMPONENT_ID = "onboardDefaultRemoteFolderTextFieldId";

    private final Wizard wizard;

    private OnboardVNFWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public static OnboardVNFWizardPage create(WebDriver driver, WebDriverWait wait) {
        return new OnboardVNFWizardPage(driver, wait);
    }

    public void selectRadioButton(String label) {
        wizard.setComponentValue(ONBOARD_RADIO_BUTTONS_COMPONENT_ID, label);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void searchDownloadNFVO(String name) {
        wizard.setComponentValue(ONBOARD_DOWNLOAD_NFVO_COMPONENT_ID, name);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public String getSelectedDownloadNFVO() {
        return wizard.getComponent(ONBOARD_DOWNLOAD_NFVO_COMPONENT_ID).getStringValue();
    }

    public String getSelectedDownloadVNFPKG() {
        return wizard.getComponent(ONBOARD_DOWNLOAD_VNFPKG_COMPONENT_ID).getStringValue();
    }

    public void searchNFVO(String name) {
        wizard.setComponentValue(ONBOARD_NFVO_COMPONENT_ID, name);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public String getSelectedNFVO() {
        return wizard.getComponent(ONBOARD_NFVO_COMPONENT_ID).getStringValue();
    }

    public String getSelectedMarketplace() {
        return wizard.getComponent(ONBOARD_MARKETPLACE_COMPONENT_ID).getStringValue();
    }

    public String getSelectedVIM() {
        return wizard.getComponent(ONBOARD_VIM_SEARCH_COMPONENT_ID).getStringValue();
    }

    public void fillDefaultRemoteFolder(String value) {
        wizard.setComponentValue(ONBOARD_DEFAULT_REMOTE_FOLDER_COMPONENT_ID, value);
    }

    public String getDefaultRemoteFolder() {
        return wizard.getComponent(ONBOARD_DEFAULT_REMOTE_FOLDER_COMPONENT_ID).getStringValue();
    }
}
