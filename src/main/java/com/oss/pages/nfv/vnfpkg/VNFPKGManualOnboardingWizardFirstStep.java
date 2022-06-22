package com.oss.pages.nfv.vnfpkg;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;

import static com.oss.framework.components.inputs.Input.ComponentType.OBJECT_SEARCH_FIELD;

public class VNFPKGManualOnboardingWizardFirstStep extends VNFPKGManualOnboardingWizardStep {

    private static final String SELECT_NFVO_COMPONENT_ID = "manualOnboardNFVOSearchFieldId_OSF";
    private static final String SEARCH_VNFM_COMPONENT_ID = "manualOnboardVNFMSearchFieldId_OSF";
    private static final String SEARCH_VIM_COMPONENT_ID = "manualOnboardVIMSearchFieldId_OSF";
    private static final String SELECT_DEFAULT_REMOTE_FOLDER_COMPONENT_ID = "manualOnboardDefaultRemoteFolderTextFieldId";
    private static final String SEARCH_MARKETPLACE_COMPONENT_ID = "manualOnboardMarketplaceSearchFieldId_OSF";

    private VNFPKGManualOnboardingWizardFirstStep(WebDriver driver, WebDriverWait wait, Wizard vnfpkgWizard) {
        super(driver, wait, vnfpkgWizard);
    }

    public static VNFPKGManualOnboardingWizardFirstStep create(WebDriver driver, WebDriverWait wait, Wizard vnfpkgWizard) {
        return new VNFPKGManualOnboardingWizardFirstStep(driver, wait, vnfpkgWizard);
    }

    public void selectNfvo(Long nfvoId) {
        vnfpkgWizard.setComponentValue(SELECT_NFVO_COMPONENT_ID, nfvoId.toString(), OBJECT_SEARCH_FIELD);
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

    public String getVnfmSearchFieldValue() {
        return getComponentStringValue(SEARCH_VNFM_COMPONENT_ID, OBJECT_SEARCH_FIELD);
    }

    public String getMarketplaceSearchFieldValue() {
        return getComponentStringValue(SEARCH_MARKETPLACE_COMPONENT_ID, OBJECT_SEARCH_FIELD);
    }

    public String getVimSearchFieldValue() {
        return getComponentStringValue(SEARCH_VIM_COMPONENT_ID, OBJECT_SEARCH_FIELD);
    }

    private String getComponentStringValue(String componentId, ComponentType componentType) {
        return vnfpkgWizard.getComponent(componentId, componentType).getStringValue();
    }
}
