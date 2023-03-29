package com.oss.pages.transport.trail;

import org.openqa.selenium.WebDriver;

import io.qameta.allure.Step;

public class AELWizardPage extends ConnectionWizardPage {

    private static final String AGGREGATION_PROTOCOL_FIELD_ID = "oss.transport.trail.type.Aggregated Ethernet Link.Aggregation Protocol";
    private static final String SPEED_ID = "oss.transport.trail.type.Aggregated Ethernet Link.Speed [Mbps]";
    private static final String CAPACITY_ID = "trailEffectiveCapacityComponent";
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

    public AELWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Set aggregation protocol to {aggregationProtocol}")
    public void setAggregationProtocol(String aggregationProtocol) {
        getWizard().setComponentValue(AGGREGATION_PROTOCOL_FIELD_ID, aggregationProtocol);
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
