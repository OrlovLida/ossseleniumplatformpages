package com.oss.pages.nfv.vnfpkg;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;

import static com.oss.framework.components.inputs.Input.ComponentType.OBJECT_SEARCH_FIELD;

public class VNFPKGManualOnboardingWizardFirstStep extends VNFPKGManualOnboardingWizardStep {

    private static final String SELECT_NFVO_COMPONENT_ID = "onboardNFVOSearchFieldId";
    private static final String SEARCH_VNFM_COMPONENT_ID = "onboardVNFMSearchFieldId";
    private static final String SEARCH_VIM_COMPONENT_ID = "onboardVIMSearchFieldId";
    private static final String SELECT_DEFAULT_REMOTE_FOLDER_COMPONENT_ID = "onboardDefaultRemoteFolderTextFieldId";
    private static final String SEARCH_MARKETPLACE_COMPONENT_ID = "onboardMarketplaceSearchFieldId";

    private VNFPKGManualOnboardingWizardFirstStep(WebDriver driver, WebDriverWait wait, Wizard vnfpkgWizard) {
        super(driver, wait, vnfpkgWizard);
    }

    public static VNFPKGManualOnboardingWizardFirstStep create(WebDriver driver, WebDriverWait wait, Wizard vnfpkgWizard) {
        return new VNFPKGManualOnboardingWizardFirstStep(driver, wait, vnfpkgWizard);
    }

    public void selectNfvo(String nfvoIdentifier) {
        vnfpkgWizard.setComponentValue(SELECT_NFVO_COMPONENT_ID, nfvoIdentifier, OBJECT_SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public boolean vnfmSearchFieldIsDisplayed() {
        return this.vnfpkgWizard.isElementPresentById(SEARCH_VNFM_COMPONENT_ID);
    }

    public boolean vimSearchFieldIsDisplayed() {
        return this.vnfpkgWizard.isElementPresentById(SEARCH_VIM_COMPONENT_ID);
    }

    public boolean defaultRemoteFieldIsDisplayed() {
        return this.vnfpkgWizard.isElementPresentById(SELECT_DEFAULT_REMOTE_FOLDER_COMPONENT_ID);
    }

    public boolean marketplaceSearchFieldIsDisplayed() {
        return this.vnfpkgWizard.isElementPresentById(SEARCH_MARKETPLACE_COMPONENT_ID);
    }
}
