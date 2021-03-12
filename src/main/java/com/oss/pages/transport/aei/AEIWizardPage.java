package com.oss.pages.transport.aei;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.CommonHierarchyApp;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.platform.OldInventoryViewPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * @author Kamil Jacko
 */
public class AEIWizardPage extends BasePage {

    private static final String NUMBER_FIELD_ID = "uid-number";
    private static final String NAME_FIELD_ID = "uid-name";
    private static final String DESCRIPTION_FIELD_ID = "uid-description";
    private static final String AGGREGATION_PROTOCOL_FIELD_ID = "uid-aggregationProtocol";
    private static final String LACP_MODE_FIELD_ID = "uid-lacpMode";
    private static final String LACP_SHORT_PERIOD_PATH = "uid-lacpShortPeriod";
    private static final String MINIMUM_ACTIVE_LINKS_FIELD_ID = "uid-minimumActiveLinks";
    private static final String MINIMUM_BANDWIDTH_FIELD_ID = "uid-minimumBandwidthMbps";
    private static final String MTU_FIELD_ID = "uid-mtu";
    private static final String MAC_ADDRESS_FIELD_ID = "uid-macAddress";
    private static final String ADMINISTRATIVE_STATE_FIELD_ID = "uid-administrativeState";
    private static final String ENCAPSULATION_FIELD_ID = "uid-encapsulation";

    private final Wizard wizard;

    public AEIWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Set number to {number}")
    public void setNumber(String number) {
        setTextFieldComponentValue(NUMBER_FIELD_ID, number);
    }

    @Step("Set name to {name}")
    public void setName(String name) {
        setTextFieldComponentValue(NAME_FIELD_ID, name);
    }

    @Step("Set description to {description}")
    public void setDescription(String description) {
        setTextFieldComponentValue(DESCRIPTION_FIELD_ID, description);
    }

    @Step("Set minimum active links to {minimumActiveLinks}")
    public void setMinimumActiveLinks(String minimumActiveLinks) {
        setTextFieldComponentValue(MINIMUM_ACTIVE_LINKS_FIELD_ID, minimumActiveLinks);
    }

    @Step("Set Minimum bandwidtn to {minimumBandwidth}")
    public void setMinimumBandwidth(String minimumBandwidth) {
        setTextFieldComponentValue(MINIMUM_BANDWIDTH_FIELD_ID, minimumBandwidth);
    }

    @Step("Set MTU to {mtu}")
    public void setMTU(String mtu) {
        setTextFieldComponentValue(MTU_FIELD_ID, mtu);
    }

    @Step("Set mac address to {macAddress}")
    public void setMACAddress(String macAddress) {
        setTextFieldComponentValue(MAC_ADDRESS_FIELD_ID, macAddress);
    }

    private void setTextFieldComponentValue(String componentId, String value){
        wizard.setComponentValue(componentId, value, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set administrative state to {administrativeState}")
    public void setAdministrativeState(String administrativeState) {
        setComboBoxComponentValue(ADMINISTRATIVE_STATE_FIELD_ID, administrativeState);
    }

    @Step("Set encapsulation to {encapsulation}")
    public void setEncapsulation(String encapsulation) {
        setComboBoxComponentValue(ENCAPSULATION_FIELD_ID, encapsulation);
    }

    @Step("Set aggregation protocol to {aggregationProtocol}")
    public void setAggregationProtocol(String aggregationProtocol) {
        setComboBoxComponentValue(AGGREGATION_PROTOCOL_FIELD_ID, aggregationProtocol);
    }

    @Step("Set LACP mode to {lacpMode}")
    public void setLACPMode(String lacpMode) {
        setComboBoxComponentValue(LACP_MODE_FIELD_ID, lacpMode);
    }

    private void setComboBoxComponentValue(String componentId, String value){
        wizard.setComponentValue(componentId, value, Input.ComponentType.COMBOBOX);
    }

    @Step("Set LACP short period to {value}")
    public void setLACPShortPeriod(String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.setComponentValue(LACP_SHORT_PERIOD_PATH, value, Input.ComponentType.CHECKBOX);
    }

    @Step("Click next step button")
    public void clickNextStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickNextStep();
    }

    @Step("Navigate through Common Hierarchy App widget selecting {location} -> {device} and interface names")
    public void assignEthernetInterfaces(List<String> interfacesNamesToSelect, String location, String device) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonHierarchyApp commonHierarchyApp = CommonHierarchyApp.createByClass(driver, wait);
        commonHierarchyApp.callAvailableAction(interfacesNamesToSelect, location, device);
    }

    @Step("Click accept button")
    public OldInventoryViewPage clickAccept() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickAcceptOldWizard();
        return new OldInventoryViewPage(driver);
    }
}
