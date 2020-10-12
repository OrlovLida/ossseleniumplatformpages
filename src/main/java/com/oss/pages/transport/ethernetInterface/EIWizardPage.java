package com.oss.pages.transport.ethernetInterface;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

/**
 * @author Kamil Jacko
 */
public class EIWizardPage extends BasePage {

    private final Wizard wizard;

    private static final String ADMINISTRATIVE_STATE_FIELD_ID = "uid-administrativeState-input";
    private static final String AUTO_NEGOTIATION_FILED_ID = "uid-autoNegotiation-input";
    private static final String ADMINISTRATIVE_SPEED_FIELD_ID = "uid-administrativeSpeed-input";
    private static final String ADMINISTRATIVE_DUPLEX_MODE_FIELD_ID = "uid-administrativeDuplexMode-input";
    private static final String MAXIMUM_FRAME_SIZE_FIELD_DATA_ATTRIBUTE_NAME = "uid-maxFrameSize";
    private static final String FLOW_CONTROL_FIELD_ID = "uid-flowControl-input";
    private static final String MTU_FIELD_DATA_ATTRIBUTE_NAME = "uid-mtu";
    private static final String ENCAPSULATION_FIELD_ID = "uid-encapsulation-input";
    private static final String BANDWIDTH_FIELD_DATA_ATTRIBUTE_NAME = "uid-bandwidth";
    private static final String SWITCH_PORT_FIELD_ID = "uid-switchport-input";
    private static final String SWITCH_MODE_FIELD_ID = "uid-switchportMode-input";
    private static final String ACCESS_FUNCTION_FIELD_ID = "uid-accessFunction-input";
    private static final String DESCRIPTION_FIELD_DATA_ATTRIBUTE_NAME = "uid-description";

    public EIWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    public void setAdministrativeState(String administrativeState) {
        Combobox administrativeStateComponent = (Combobox) wizard.getComponent(ADMINISTRATIVE_STATE_FIELD_ID, Input.ComponentType.COMBOBOX);
        administrativeStateComponent.clear();
        administrativeStateComponent.setSingleStringValue(administrativeState);
    }

    public void setAutoNegotiation(String autoNegotiation) {
        Combobox administrativeStateComponent = (Combobox) wizard.getComponent(AUTO_NEGOTIATION_FILED_ID, Input.ComponentType.COMBOBOX);
        administrativeStateComponent.clear();
        administrativeStateComponent.setSingleStringValue(autoNegotiation);
    }

    public void setAdministrativeSpeed(String administrativeSpeed) {
        Combobox administrativeSpeedComponent = (Combobox) wizard.getComponent(ADMINISTRATIVE_SPEED_FIELD_ID, Input.ComponentType.COMBOBOX);
        administrativeSpeedComponent.clear();
        administrativeSpeedComponent.setSingleStringValue(administrativeSpeed);
    }

    public void setAdministrativeDuplexMode(String administrativeDuplexMode) {
        Combobox administrativeDuplexModeComponent =
                (Combobox) wizard.getComponent(ADMINISTRATIVE_DUPLEX_MODE_FIELD_ID, Input.ComponentType.COMBOBOX);
        administrativeDuplexModeComponent.clear();
        administrativeDuplexModeComponent.setSingleStringValue(administrativeDuplexMode);
    }

    public void setMaximumFrameSize(String maximumFrameSize) {
        Input maximumFrameSizeComponent = wizard.getComponent(MAXIMUM_FRAME_SIZE_FIELD_DATA_ATTRIBUTE_NAME, Input.ComponentType.TEXT_FIELD);
        maximumFrameSizeComponent.clear();
        maximumFrameSizeComponent.setSingleStringValue(maximumFrameSize);
    }

    public void setFlowControl(String flowControl) {
        Combobox flowControlComponent = (Combobox) wizard.getComponent(FLOW_CONTROL_FIELD_ID, Input.ComponentType.COMBOBOX);
        flowControlComponent.clear();
        flowControlComponent.setSingleStringValue(flowControl);
    }

    public void setMTU(String mtu) {
        Input MTUComponent = wizard.getComponent(MTU_FIELD_DATA_ATTRIBUTE_NAME, Input.ComponentType.TEXT_FIELD);
        MTUComponent.clear();
        MTUComponent.setSingleStringValue(mtu);
    }

    public void setEncapsulation(String encapsulation) {
        Combobox encapsulationComponent = (Combobox) wizard.getComponent(ENCAPSULATION_FIELD_ID, Input.ComponentType.COMBOBOX);
        encapsulationComponent.clear();
        encapsulationComponent.setSingleStringValue(encapsulation);
    }

    public void setBandwidth(String bandwidth) {
        Input bandwidthComponent = wizard.getComponent(BANDWIDTH_FIELD_DATA_ATTRIBUTE_NAME, Input.ComponentType.TEXT_FIELD);
        bandwidthComponent.clear();
        bandwidthComponent.setSingleStringValue(bandwidth);
    }

    public void setSwitchPort(String switchPort) {
        Combobox switchPortComponent = (Combobox) wizard.getComponent(SWITCH_PORT_FIELD_ID, Input.ComponentType.COMBOBOX);
        switchPortComponent.clear();
        switchPortComponent.setSingleStringValue(switchPort);
    }

    public void setSwitchMode(String switchMode) {
        Combobox switchModeComponent = (Combobox) wizard.getComponent(SWITCH_MODE_FIELD_ID, Input.ComponentType.COMBOBOX);
        switchModeComponent.clear();
        switchModeComponent.setSingleStringValue(switchMode);
    }

    public void setAccessFunction(String accessFunction) {
        Combobox accessFunctionComponent = (Combobox) wizard.getComponent(ACCESS_FUNCTION_FIELD_ID, Input.ComponentType.COMBOBOX);
        accessFunctionComponent.clear();
        accessFunctionComponent.setSingleStringValue(accessFunction);
    }

    public void setDescription(String description) {
        Input descriptionComponent = wizard.getComponent(DESCRIPTION_FIELD_DATA_ATTRIBUTE_NAME, Input.ComponentType.TEXT_FIELD);
        descriptionComponent.clear();
        descriptionComponent.setSingleStringValue(description);
    }

    public void clearAdministrativeState() {
        Combobox administrativeStateComponent = (Combobox) wizard.getComponent(ADMINISTRATIVE_STATE_FIELD_ID, Input.ComponentType.COMBOBOX);
        administrativeStateComponent.clear();
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
        Input MTUComponent = wizard.getComponent(MTU_FIELD_DATA_ATTRIBUTE_NAME, Input.ComponentType.TEXT_FIELD);
        MTUComponent.clearByAction();
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

    public EIInventoryViewPage clickAccept() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickAcceptOldWizard();
        return new EIInventoryViewPage(driver);
    }
}
