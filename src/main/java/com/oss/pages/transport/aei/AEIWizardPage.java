package com.oss.pages.transport.aei;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.commonhierarchy.CommonHierarchyApp;
import com.oss.pages.transport.trail.ConnectionWizardPage;

import io.qameta.allure.Step;

/**
 * @author Kamil Jacko
 */
public class AEIWizardPage extends ConnectionWizardPage {

    private static final String NUMBER_FIELD_ID = "uid-number";
    private static final String AGGREGATION_PROTOCOL_FIELD_ID = "oss.transport.trail.type.Aggregated Ethernet Link.Aggregation Protocol";
    private static final String LACP_MODE_FIELD_ID = "uid-lacpMode";
    private static final String LACP_SHORT_PERIOD_PATH = "uid-lacpShortPeriod";
    private static final String MINIMUM_ACTIVE_LINKS_FIELD_ID = "uid-minimumActiveLinks";
    private static final String MINIMUM_BANDWIDTH_FIELD_ID = "uid-minimumBandwidthMbps";
    private static final String MTU_FIELD_ID = "uid-mtu";
    private static final String MAC_ADDRESS_FIELD_ID = "uid-macAddress";
    private static final String ADMINISTRATIVE_STATE_FIELD_ID = "uid-administrativeState";
    private static final String SPEED_ID = "oss.transport.trail.type.Aggregated Ethernet Link.Speed [Mbps]";
    private static final String CAPACITY_ID = "oss.transport.trail.type.Aggregated Ethernet Link.Effective Capacity [Mbps]";
    private static final String ENCAPSULATION_FIELD_ID = "uid-encapsulation";
    private static final String CREATE_START_AEI_CHECKBOX_ID = "AEIFormStart_CreateInterface";
    private static final String START_AEI_NAME_ID = "AEIFormStart_Name";
    private static final String START_AEI_NUMBER_ID = "AEIFormStart_Number";
    private static final String START_AEI_AGGREGATION_PROTOCOL_ID = "AEIFormStart_AggregationProtocol";
    private static final String START_AEI_ENCAPSULATION_ID = "AEIFormStart_Encapsulation";
    private static final String START_AEI_LACP_MODE_ID = "AEIFormStart_LACPMode";
    private static final String START_AEI_LACP_SHORT_PERIOD_CHECKBOX_ID = "AEIFormStart_LACPShortPeriod";
    private static final String START_AEI_MTU_ID = "AEIFormStart_MTU";
    private static final String START_AEI_MAC_ADDRESS_ID = "AEIFormStart_MACAddress";
    private static final String START_AEI_MINIMUM_ACTIVE_LINKS_ID = "AEIFormStart_MinimumActiveLinks";
    private static final String START_AEI_MINIMUM_BANDWIDTH_ID = "AEIFormStart_MinimumBandwidth";
    private static final String END_AEI_NAME_ID = "AEIFormEnd_Name";
    private static final String END_AEI_NUMBER_ID = "AEIFormEnd_Number";
    private static final String END_AEI_AGGREGATION_PROTOCOL_ID = "AEIFormEnd_AggregationProtocol";
    private static final String END_AEI_ENCAPSULATION_ID = "AEIFormEnd_Encapsulation";
    private static final String END_AEI_LACP_MODE_ID = "AEIFormEnd_LACPMode";
    private static final String END_AEI_LACP_SHORT_PERIOD_CHECKBOX_ID = "AEIFormEnd_LACPShortPeriod";
    private static final String END_AEI_MTU_ID = "AEIFormEnd_MTU";
    private static final String END_AEI_MAC_ADDRESS_ID = "AEIFormEnd_MACAddress";
    private static final String END_AEI_MINIMUM_ACTIVE_LINKS_ID = "AEIFormEnd_MinimumActiveLinks";
    private static final String END_AEI_MINIMUM_BANDWIDTH_ID = "AEIFormEnd_MinimumBandwidth";
    private static final String CREATE_END_AEI_CHECKBOX_ID = "AEIFormEnd_CreateInterface";
    private static final String TRUE = "true";
    private static final String WIDGET_ID = "CommonHierarchyApp-hierarchyAppId";

    public AEIWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Set number to {number}")
    public void setNumber(String number) {
        getWizard().setComponentValue(NUMBER_FIELD_ID, number);
    }

    @Step("Set minimum active links to {minimumActiveLinks}")
    public void setMinimumActiveLinks(String minimumActiveLinks) {
        getWizard().setComponentValue(MINIMUM_ACTIVE_LINKS_FIELD_ID, minimumActiveLinks);
    }

    @Step("Set Minimum bandwidtn to {minimumBandwidth}")
    public void setMinimumBandwidth(String minimumBandwidth) {
        getWizard().setComponentValue(MINIMUM_BANDWIDTH_FIELD_ID, minimumBandwidth);
    }

    @Step("Set MTU to {mtu}")
    public void setMTU(String mtu) {
        getWizard().setComponentValue(MTU_FIELD_ID, mtu);
    }

    @Step("Set mac address to {macAddress}")
    public void setMACAddress(String macAddress) {
        getWizard().setComponentValue(MAC_ADDRESS_FIELD_ID, macAddress);
    }

    @Step("Set administrative state to {administrativeState}")
    public void setAdministrativeState(String administrativeState) {
        getWizard().setComponentValue(ADMINISTRATIVE_STATE_FIELD_ID, administrativeState);
    }

    @Step("Set encapsulation to {encapsulation}")
    public void setEncapsulation(String encapsulation) {
        getWizard().setComponentValue(ENCAPSULATION_FIELD_ID, encapsulation);
    }

    @Step("Set aggregation protocol to {aggregationProtocol}")
    public void setAggregationProtocol(String aggregationProtocol) {
        getWizard().setComponentValue(AGGREGATION_PROTOCOL_FIELD_ID, aggregationProtocol);
    }

    @Step("Set LACP mode to {lacpMode}")
    public void setLACPMode(String lacpMode) {
        getWizard().setComponentValue(LACP_MODE_FIELD_ID, lacpMode);
    }

    @Step("Set LACP short period to {value}")
    public void setLACPShortPeriod(String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().setComponentValue(LACP_SHORT_PERIOD_PATH, value);
    }

    @Step("Navigate through Common Hierarchy App widget selecting {location} -> {device} and interface names")
    public void assignEthernetInterfaces(List<String> interfacesNamesToSelect, String location, String device) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonHierarchyApp commonHierarchyApp = CommonHierarchyApp.create(driver, wait, WIDGET_ID);
        commonHierarchyApp.callAvailableAction(interfacesNamesToSelect, location, device);
    }

    @Step("Set Aggregated Ethernet Link Speed = {speed}")
    public void setSpeed(String speed) {
        getWizard().setComponentValue(SPEED_ID, speed);
    }

    @Step("Set Effective Capacity = {capacity}")
    public void setEffectiveCapacity(String capacity) {
        getWizard().setComponentValue(CAPACITY_ID, capacity);
    }

    @Step("Set Create Start Aggregated Ethernet Interface chechbox to true")
    public void setCreateStartAggregatedEthernetInterface() {
        getWizard().setComponentValue(CREATE_START_AEI_CHECKBOX_ID, TRUE);
    }

    @Step("Set Create End Aggregated Ethernet Interface chechbox to true")
    public void setCreateEndAggregatedEthernetInterface() {
        getWizard().setComponentValue(CREATE_END_AEI_CHECKBOX_ID, TRUE);
    }

    @Step("Set Start Aggregated Ethernet Interface Name = {value}")
    public void setStartAeiName(String value) {
        getWizard().setComponentValue(START_AEI_NAME_ID, value);
    }

    @Step("Set Start Aggregated Ethernet Interface Number = {value}")
    public void setStartAeiNumber(String value) {
        getWizard().setComponentValue(START_AEI_NUMBER_ID, value);
    }

    @Step("Set Start Aggregated Ethernet Interface Aggregation Protocol = {value}")
    public void setStartAeiAggregationProtocol(String value) {
        getWizard().setComponentValue(START_AEI_AGGREGATION_PROTOCOL_ID, value);
    }

    @Step("Set Start Aggregated Ethernet Interface Encapsulation = {value}")
    public void setStartAeiEncapsulation(String value) {
        getWizard().setComponentValue(START_AEI_ENCAPSULATION_ID, value);
    }

    @Step("Set Start Aggregated Ethernet Interface LACP Mode = {value}")
    public void setStartAeiLacpMode(String value) {
        getWizard().setComponentValue(START_AEI_LACP_MODE_ID, value);
    }

    @Step("Set Start Aggregated Ethernet Interface LACP Short Period to true")
    public void setStartAeiLacpShortPeriod() {
        getWizard().setComponentValue(START_AEI_LACP_SHORT_PERIOD_CHECKBOX_ID, TRUE);
    }

    @Step("Set Start Aggregated Ethernet Interface MTU = {value}")
    public void setStartAeiMtu(String value) {
        getWizard().setComponentValue(START_AEI_MTU_ID, value);
    }

    @Step("Set Start Aggregated Ethernet Interface MAC Address = {value}")
    public void setStartAeiMacAddress(String value) {
        getWizard().setComponentValue(START_AEI_MAC_ADDRESS_ID, value);
    }

    @Step("Set Start Aggregated Ethernet Interface Minimum Active Links = {value}")
    public void setStartAeiMinimumActiveLinks(String value) {
        getWizard().setComponentValue(START_AEI_MINIMUM_ACTIVE_LINKS_ID, value);
    }

    @Step("Set Start Aggregated Ethernet Interface Minimum Bandwidth = {value}")
    public void setStartAeiMinimumBandwidth(String value) {
        getWizard().setComponentValue(START_AEI_MINIMUM_BANDWIDTH_ID, value);
    }

    @Step("Set End Aggregated Ethernet Interface Name = {value}")
    public void setEndAeiName(String value) {
        getWizard().setComponentValue(END_AEI_NAME_ID, value);
    }

    @Step("Set End Aggregated Ethernet Interface Number = {value}")
    public void setEndAeiNumber(String value) {
        getWizard().setComponentValue(END_AEI_NUMBER_ID, value);
    }

    @Step("Set End Aggregated Ethernet Interface Aggregation Protocol = {value}")
    public void setEndAeiAggregationProtocol(String value) {
        getWizard().setComponentValue(END_AEI_AGGREGATION_PROTOCOL_ID, value);
    }

    @Step("Set End Aggregated Ethernet Interface Encapsulation = {value}")
    public void setEndAeiEncapsulation(String value) {
        getWizard().setComponentValue(END_AEI_ENCAPSULATION_ID, value);
    }

    @Step("Set End Aggregated Ethernet Interface LACP Mode = {value}")
    public void setEndAeiLacpMode(String value) {
        getWizard().setComponentValue(END_AEI_LACP_MODE_ID, value);
    }

    @Step("Set End Aggregated Ethernet Interface LACP Short Period to true")
    public void setEndAeiLacpShortPeriod() {
        getWizard().setComponentValue(END_AEI_LACP_SHORT_PERIOD_CHECKBOX_ID, TRUE);
    }

    @Step("Set End Aggregated Ethernet Interface MTU = {value}")
    public void setEndAeiMtu(String value) {
        getWizard().setComponentValue(END_AEI_MTU_ID, value);
    }

    @Step("Set End Aggregated Ethernet Interface MAC Address = {value}")
    public void setEndAeiMacAddress(String value) {
        getWizard().setComponentValue(END_AEI_MAC_ADDRESS_ID, value);
    }

    @Step("Set End Aggregated Ethernet Interface Minimum Active Links = {value}")
    public void setEndAeiMinimumActiveLinks(String value) {
        getWizard().setComponentValue(END_AEI_MINIMUM_ACTIVE_LINKS_ID, value);
    }

    @Step("Set End Aggregated Ethernet Interface Minimum Bandwidth = {value}")
    public void setEndAeiMinimumBandwidth(String value) {
        getWizard().setComponentValue(END_AEI_MINIMUM_BANDWIDTH_ID, value);
    }
}
