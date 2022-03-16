package com.oss.pages.nfv.networkslicesubnet;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.oss.framework.components.inputs.Input.ComponentType.NUMBER_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class NetworkSliceSubnetWizardSecondStep extends NetworkSliceSubnetWizardStep {
    private static final String ADD_SLICE_PROFILE_BUTTON_LABEL = "Add Slice Profile";
    private static final String ADD_PLMN_INFO_BUTTON_LABEL = "Add PLMN Info";
    private static final String NAME_COMPONENT_ID = "attribute-networkSliceSubnetNameTextFieldnetworkSliceSubnetStep2Id";
    private static final String MCC_COMPONENT_ID = "##PLMNInfo-mccnetworkSliceSubnetStep2Id";
    private static final String MNC_COMPONENT_ID = "##PLMNInfo-mncnetworkSliceSubnetStep2Id";
    private static final String SD_COMPONENT_ID = "##PLMNInfo-sdnetworkSliceSubnetStep2Id";
    private static final String SST_COMPONENT_ID = "##PLMNInfo-sstnetworkSliceSubnetStep2Id";

    private NetworkSliceSubnetWizardSecondStep(WebDriver driver, WebDriverWait wait, Wizard networkSliceWizard) {
        super(driver, wait, networkSliceWizard);
    }

    public static final NetworkSliceSubnetWizardSecondStep create(WebDriver driver, WebDriverWait wait, Wizard networkSliceWizard) {
        return new NetworkSliceSubnetWizardSecondStep(driver, wait, networkSliceWizard);
    }

    public void clickAddSliceProfile() {
        networkSliceSubnetWizard.clickButtonByLabel(ADD_SLICE_PROFILE_BUTTON_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clickAddPLMNInfo() {
        networkSliceSubnetWizard.clickButtonByLabel(ADD_PLMN_INFO_BUTTON_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void setSliceProfileName(String name) {
        networkSliceSubnetWizard.setComponentValue(NAME_COMPONENT_ID, name, TEXT_FIELD);
    }

    public String getSliceProfileName() {
        return networkSliceSubnetWizard.getComponent(NAME_COMPONENT_ID, TEXT_FIELD).getStringValue();
    }

    public void setPLMNInfoMCC(String value) {
        networkSliceSubnetWizard.setComponentValue(MCC_COMPONENT_ID, value, NUMBER_FIELD);
    }

    public String getPLMNInfoMCC() {
        return StringUtils.deleteWhitespace(networkSliceSubnetWizard.getComponent(MCC_COMPONENT_ID, NUMBER_FIELD).getStringValue());
    }

    public void setPLMNInfoMNC(String value) {
        networkSliceSubnetWizard.setComponentValue(MNC_COMPONENT_ID, value, NUMBER_FIELD);
    }

    public String getPLMNInfoMNC() {
        return StringUtils.deleteWhitespace(networkSliceSubnetWizard.getComponent(MNC_COMPONENT_ID, NUMBER_FIELD).getStringValue());
    }

    public void setPLMNInfoSD(String value) {
        networkSliceSubnetWizard.setComponentValue(SD_COMPONENT_ID, value, NUMBER_FIELD);
    }

    public String getPLMNInfoSD() {
        return StringUtils.deleteWhitespace(networkSliceSubnetWizard.getComponent(SD_COMPONENT_ID, NUMBER_FIELD).getStringValue());
    }

    public void setPLMNInfoSST(String value) {
        networkSliceSubnetWizard.setComponentValue(SST_COMPONENT_ID, value, NUMBER_FIELD);
    }

    public String getPLMNInfoSST() {
        return StringUtils.deleteWhitespace(networkSliceSubnetWizard.getComponent(SST_COMPONENT_ID, NUMBER_FIELD).getStringValue());
    }
}