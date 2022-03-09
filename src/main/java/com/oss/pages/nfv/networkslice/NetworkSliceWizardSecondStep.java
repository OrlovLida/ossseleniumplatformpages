package com.oss.pages.nfv.networkslice;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.oss.framework.components.inputs.Input.ComponentType.NUMBER_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class NetworkSliceWizardSecondStep extends NetworkSliceWizardStep {
    private static final String ADD_SERVICE_PROFILE_BUTTON_LABEL = "Add Service Profile";
    private static final String ADD_PLMN_INFO_BUTTON_LABEL = "Add PLMN Info";
    private static final String NAME_COMPONENT_ID = "attribute-networkSliceNameTextFieldnetworkSliceStep2Id";
    private static final String MCC_COMPONENT_ID = "##PLMNInfo-mccnetworkSliceStep2Id";
    private static final String MNC_COMPONENT_ID = "##PLMNInfo-mncnetworkSliceStep2Id";
    private static final String SD_COMPONENT_ID = "##PLMNInfo-sdnetworkSliceStep2Id";
    private static final String SST_COMPONENT_ID = "##PLMNInfo-sstnetworkSliceStep2Id";

    private NetworkSliceWizardSecondStep(WebDriver driver, WebDriverWait wait, Wizard networkSliceWizard) {
        super(driver, wait, networkSliceWizard);
    }

    public static final NetworkSliceWizardSecondStep create(WebDriver driver, WebDriverWait wait, Wizard networkSliceWizard) {
        return new NetworkSliceWizardSecondStep(driver, wait, networkSliceWizard);
    }

    public void clickAddServiceProfile() {
        networkSliceWizard.clickButtonByLabel(ADD_SERVICE_PROFILE_BUTTON_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clickAddPLMNInfo() {
        networkSliceWizard.clickButtonByLabel(ADD_PLMN_INFO_BUTTON_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void setServiceProfileName(String name) {
        networkSliceWizard.setComponentValue(NAME_COMPONENT_ID, name, TEXT_FIELD);
    }

    public String getServiceProfileName() {
        return networkSliceWizard.getComponent(NAME_COMPONENT_ID, TEXT_FIELD).getStringValue();
    }

    public void setPLMNInfoMCC(String value) {
        networkSliceWizard.setComponentValue(MCC_COMPONENT_ID, value, NUMBER_FIELD);
    }

    public String getPLMNInfoMCC() {
        return StringUtils.deleteWhitespace(networkSliceWizard.getComponent(MCC_COMPONENT_ID, NUMBER_FIELD).getStringValue());
    }

    public void setPLMNInfoMNC(String value) {
        networkSliceWizard.setComponentValue(MNC_COMPONENT_ID, value, NUMBER_FIELD);
    }

    public String getPLMNInfoMNC() {
        return StringUtils.deleteWhitespace(networkSliceWizard.getComponent(MNC_COMPONENT_ID, NUMBER_FIELD).getStringValue());
    }

    public void setPLMNInfoSD(String value) {
        networkSliceWizard.setComponentValue(SD_COMPONENT_ID, value, NUMBER_FIELD);
    }

    public String getPLMNInfoSD() {
        return StringUtils.deleteWhitespace(networkSliceWizard.getComponent(SD_COMPONENT_ID, NUMBER_FIELD).getStringValue());
    }

    public void setPLMNInfoSST(String value) {
        networkSliceWizard.setComponentValue(SST_COMPONENT_ID, value, NUMBER_FIELD);
    }

    public String getPLMNInfoSST() {
        return StringUtils.deleteWhitespace(networkSliceWizard.getComponent(SST_COMPONENT_ID, NUMBER_FIELD).getStringValue());
    }
}