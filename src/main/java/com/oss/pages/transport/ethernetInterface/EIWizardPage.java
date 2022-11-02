package com.oss.pages.transport.ethernetInterface;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

/**
 * @author Kamil Jacko
 */
public class EIWizardPage extends BasePage {

    private static final String ADMINISTRATIVE_STATE_FIELD_ID = "uid-administrativeState";
    private static final String AUTO_NEGOTIATION_FILED_ID = "AutoNegotiationFieldID";
    private static final String ADMINISTRATIVE_SPEED_FIELD_ID = "AdministrativeSpeedFieldID";
    private static final String ADMINISTRATIVE_DUPLEX_MODE_FIELD_ID = "AdministrativeDuplexModeFieldID";
    private static final String AUTO_ADVERTISEMENT_MAX_CAPACITY_ID = "AutoAdvertisementMaxCapacityFieldID";
    private static final String MAXIMUM_FRAME_SIZE_FIELD_DATA_ATTRIBUTE_NAME = "MaxFrameSizeFieldID";
    private static final String FLOW_CONTROL_FIELD_ID = "FlowControlFieldID";
    private static final String MTU_FIELD_DATA_ATTRIBUTE_NAME = "MTUFieldID";
    private static final String ENCAPSULATION_FIELD_ID = "EncapsulationFieldID";
    private static final String BANDWIDTH_FIELD_DATA_ATTRIBUTE_NAME = "BandwidthFieldID";
    private static final String SWITCH_PORT_FIELD_ID = "SwitchportFieldID";
    private static final String SWITCH_MODE_FIELD_ID = "SwitchportModeFieldID";
    private static final String ACCESS_FUNCTION_FIELD_ID = "AccessFunctionFieldID";
    private static final String DESCRIPTION_FIELD_DATA_ATTRIBUTE_NAME = "DescriptionFieldID";
    private static final String EI_WIZARD_ID = "EthernetInterfaceWizardWidgetId";
    private final Wizard wizard;

    public EIWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, EI_WIZARD_ID);
    }

    public Wizard getWizard() {
        return wizard;
    }

    public void setAdministrativeState(String administrativeState) {
        getWizard().setComponentValue(ADMINISTRATIVE_STATE_FIELD_ID, administrativeState);
    }

    public void setAutoNegotiation(String autoNegotiation) {
        getWizard().setComponentValue(AUTO_NEGOTIATION_FILED_ID, autoNegotiation);
    }

    public void setAdministrativeSpeed(String administrativeSpeed) {
        getWizard().setComponentValue(ADMINISTRATIVE_SPEED_FIELD_ID, administrativeSpeed);
    }

    public void setAdministrativeDuplexMode(String administrativeDuplexMode) {
        getWizard().setComponentValue(ADMINISTRATIVE_DUPLEX_MODE_FIELD_ID, administrativeDuplexMode);
    }

    public void setAutoAdvertisementMaxCapacity(String autoAdvertisementMaxCapacity) {
        getWizard().setComponentValue(AUTO_ADVERTISEMENT_MAX_CAPACITY_ID, autoAdvertisementMaxCapacity);
    }

    public void setMaximumFrameSize(String maximumFrameSize) {
        getWizard().setComponentValue(MAXIMUM_FRAME_SIZE_FIELD_DATA_ATTRIBUTE_NAME, maximumFrameSize);
    }

    public void setFlowControl(String flowControl) {
        getWizard().setComponentValue(FLOW_CONTROL_FIELD_ID, flowControl);
    }

    public void setMTU(String mtu) {
        getWizard().setComponentValue(MTU_FIELD_DATA_ATTRIBUTE_NAME, mtu);
    }

    public void setEncapsulation(String encapsulation) {
        getWizard().setComponentValue(ENCAPSULATION_FIELD_ID, encapsulation);
    }

    public void setBandwidth(String bandwidth) {
        getWizard().setComponentValue(BANDWIDTH_FIELD_DATA_ATTRIBUTE_NAME, bandwidth);
    }

    public void setSwitchPort(String switchPort) {
        getWizard().setComponentValue(SWITCH_PORT_FIELD_ID, switchPort);
    }

    public void setSwitchMode(String switchMode) {
        getWizard().setComponentValue(SWITCH_MODE_FIELD_ID, switchMode);
    }

    public void setAccessFunction(String accessFunction) {
        getWizard().setComponentValue(ACCESS_FUNCTION_FIELD_ID, accessFunction);
    }

    public void setDescription(String description) {
        getWizard().setComponentValue(DESCRIPTION_FIELD_DATA_ATTRIBUTE_NAME, description);
    }

    public void clearMaximumFrameSize() {
        Input maximumFrameSizeComponent = wizard.getComponent(MAXIMUM_FRAME_SIZE_FIELD_DATA_ATTRIBUTE_NAME, Input.ComponentType.TEXT_FIELD);
        maximumFrameSizeComponent.clearByAction();
    }

    public void clearFlowControl() {
        Combobox flowControlComponent = (Combobox) wizard.getComponent(FLOW_CONTROL_FIELD_ID, Input.ComponentType.COMBOBOX);
        flowControlComponent.clear();
    }

    public void clearMTU() {
        Input mtuComponent = wizard.getComponent(MTU_FIELD_DATA_ATTRIBUTE_NAME, Input.ComponentType.TEXT_FIELD);
        mtuComponent.clearByAction();
    }

    public void clearBandwidth() {
        Input bandwidthComponent = wizard.getComponent(BANDWIDTH_FIELD_DATA_ATTRIBUTE_NAME, Input.ComponentType.TEXT_FIELD);
        bandwidthComponent.clearByAction();
    }

    public void clearSwitchPort() {
        Combobox switchPortComponent = (Combobox) wizard.getComponent(SWITCH_PORT_FIELD_ID, Input.ComponentType.COMBOBOX);
        switchPortComponent.clear();
    }

    public void clearSwitchMode() {
        Combobox switchModeComponent = (Combobox) wizard.getComponent(SWITCH_MODE_FIELD_ID, Input.ComponentType.COMBOBOX);
        switchModeComponent.clear();
    }

    public void clearAccessFunction() {
        Combobox accessFunctionComponent = (Combobox) wizard.getComponent(ACCESS_FUNCTION_FIELD_ID, Input.ComponentType.COMBOBOX);
        accessFunctionComponent.clear();
    }

    public void clearDescription() {
        Input descriptionComponent = wizard.getComponent(DESCRIPTION_FIELD_DATA_ATTRIBUTE_NAME, Input.ComponentType.TEXT_FIELD);
        descriptionComponent.clearByAction();
    }

    public EIWizardPage clickAccept() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickAccept();
        return new EIWizardPage(driver);
    }

    public EIWizardPage clickNext() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickNext();
        return new EIWizardPage(driver);
    }

}
