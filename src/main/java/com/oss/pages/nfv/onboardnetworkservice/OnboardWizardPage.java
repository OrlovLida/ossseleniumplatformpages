package com.oss.pages.nfv.onboardnetworkservice;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public abstract class OnboardWizardPage extends BasePage {

    private final Wizard wizard;

    protected OnboardWizardPage(WebDriver driver, WebDriverWait wait, Wizard wizard) {
        super(driver, wait);
        this.wizard = wizard;
    }

    public void searchNFVO(String name) {
        wizard.setComponentValue(getNFVOSearchFieldIdId(), name, SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public String getSelectedNFVO() {
        return wizard.getComponent(getNFVOSearchFieldIdId(), SEARCH_FIELD).getStringValue();
    }

    public void fillDefaultRemoteFolder(String value) {
        wizard.setComponentValue(getDefaultRemoteFolderTextFieldId(), value, TEXT_FIELD);
    }

    public String getDefaultRemoteFolder() {
        return wizard.getComponent(getDefaultRemoteFolderTextFieldId(), TEXT_FIELD).getStringValue();
    }

    public Wizard getWizard() {
        return wizard;
    }

    public abstract String getNFVOSearchFieldIdId();

    public abstract String getDefaultRemoteFolderTextFieldId();
}
