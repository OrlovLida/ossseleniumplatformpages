package com.oss.pages.transport.trail.v2;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class MicrowaveChannelWizardPage extends BasePage {

    private static final String WIZARD_ID = "trailWizardId_prompt-card";

    private static final String DESCRIPTION_FIELD_ID = "trailDescriptionComponent";
    private static final String BAND_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.Band";
    private static final String CHANNEL_BANDWIDTH_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.ChannelBandwidth";
    private static final String HIGH_FREQUENCY_SITE_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.HighFrequencySite";
    private static final String POLARIZATION_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.Polarization";
    private static final String WORKING_STATUS_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.WorkingStatus";
    private static final String DCN_LOCATION_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.DCNLocation";
    private static final String CHANNEL_NUMBER_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.ChannelNumber";
    private static final String CONFIGURATION_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.Configuration";
    private static final String MICROWAVE_FREQUENCY_PLANS_COMPONENT_ID = "table-TableComponentId";
    private static final String CHANNEL_NAME_COLUMN_ID = "ChannelName";

    private static final String START_RADIO_MODEL_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.StartRadioModel";
    private static final String END_RADIO_MODEL_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.EndRadioModel";
    private static final String ADM_FLAG_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.ADMFlag";
    private static final String REFERENCE_CHANNEL_MODULATION_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.ReferenceChannelModulation";
    private static final String HIGHEST_CHANNEL_MODULATION_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.HighestChannelModulation";
    private static final String LOWEST_CHANNEL_MODULATION_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.LowestChannelModulation";

    private static final String START_TX_POWER_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.StartTxPower";
    private static final String END_TX_POWER_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.EndTxPower";
    private static final String START_RX_POWER_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.StartRxPower";
    private static final String END_RX_POWER_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.EndRxPower";
    private static final String ATPC_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.ATPC";
    private static final String ATPC_RX_MAX_LEVEL_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.ATPCRxMaxLevel";
    private static final String ATPC_RX_MIN_LEVEL_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.ATPCRxMinLevel";
    private static final String XPOL_FLAG_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.XPOLFlag";
    private static final String HSB_FLAG_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.HSBFlag";
    private static final String START_WAVEGUIDE_MODEL_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.StartWaveguide1Model";
    private static final String END_WAVEGUIDE_MODEL_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.EndWaveguide1Model";
    private static final String START_WAVEGUIDE_LENGTH_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.StartWaveguide1Length";
    private static final String END_WAVEGUIDE_LENGTH_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.EndWaveguide1Length";
    private static final String START_DIVERSITY_WAVEGUIDE_MODEL_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.StartDiversityWaveguide1Model";
    private static final String END_DIVERSITY_WAVEGUIDE_MODEL_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.StartDiversityWaveguide1Model";
    private static final String START_DIVERSITY_WAVEGUIDE_LENGTH_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.StartDiversityWaveguide1Length";
    private static final String END_DIVERSITY_WAVEGUIDE_LENGTH_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.EndDiversityWaveguide1Length";
    private static final String START_ATTENUATOR_MODEL_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.StartAttenuator1Model";
    private static final String END_ATTENUATOR_MODEL_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.EndAttenuator1Model";
    private static final String START_ATTENUATOR_MODE_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.StartAttenuator1Mode";
    private static final String END_ATTENUATOR_MODE_FIELD_ID = "oss.transport.trail.type.MicrowaveChannel.EndAttenuator1Mode";

    public MicrowaveChannelWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    @Step("Set Description = {description}")
    public void setDescription(String description) {
        wizard.setComponentValue(DESCRIPTION_FIELD_ID, description);
        waitForPageToLoad();
    }

    @Step("Set Band = {band}")
    public void setBand(String band) {
        wizard.setComponentValue(BAND_FIELD_ID, band);
        waitForPageToLoad();
    }

    @Step("Set Channel Bandwidth = {channelBandwidth}")
    public void setChannelBandwidth(String channelBandwidth) {
        wizard.setComponentValue(CHANNEL_BANDWIDTH_FIELD_ID, channelBandwidth);
        waitForPageToLoad();
    }

    @Step("Set High Frequency Site = {highFrequencySite}")
    public void setHighFrequencySite(String highFrequencySite) {
        wizard.setComponentValue(HIGH_FREQUENCY_SITE_FIELD_ID, highFrequencySite);
        waitForPageToLoad();
    }

    @Step("Set Polarization = {polarization}")
    public void setPolarization(String polarization) {
        wizard.setComponentValue(POLARIZATION_FIELD_ID, polarization);
        waitForPageToLoad();
    }

    @Step("Set Working Status = {workingStatus}")
    public void setWorkingStatus(String workingStatus) {
        wizard.setComponentValue(WORKING_STATUS_FIELD_ID, workingStatus);
        waitForPageToLoad();
    }

    @Step("Set DCN Location = {dcnLocation}")
    public void setDCNLocation(String dcnLocation) {
        wizard.setComponentValue(DCN_LOCATION_FIELD_ID, dcnLocation);
        waitForPageToLoad();
    }

    @Step("Set Channel Number = {channelNumber}")
    public void setChannelNumber(String channelNumber) {
        wizard.setComponentValue(CHANNEL_NUMBER_FIELD_ID, channelNumber);
        waitForPageToLoad();
    }

    @Step("Set Configuration = {configuration}")
    public void setConfiguration(String configuration) {
        wizard.setComponentValue(CONFIGURATION_FIELD_ID, configuration);
        waitForPageToLoad();
    }

    @Step("Set Microwave Frequency Plan")
    public void setMicrowaveFrequencyPlan(String channelNameValue) {
        waitForPageToLoad();
        TableComponent tableComponent = TableComponent.createById(driver, wait, MICROWAVE_FREQUENCY_PLANS_COMPONENT_ID);
        tableComponent.getRow(channelNameValue, CHANNEL_NAME_COLUMN_ID).selectRow();
        waitForPageToLoad();
    }

    @Step("Set Start Radio Model = {startRadioModel}")
    public void setStartRadioModel(String startRadioModel) {
        wizard.setComponentValue(START_RADIO_MODEL_FIELD_ID, startRadioModel);
        waitForPageToLoad();
    }

    @Step("Set End Radio Model = {endRadioModel}")
    public void setEndRadioModel(String endRadioModel) {
        wizard.setComponentValue(END_RADIO_MODEL_FIELD_ID, endRadioModel);
        waitForPageToLoad();
    }

    @Step("Set ADM flag to {admFlag}")
    public void setAdmFlag(String admFlag) {
        wizard.setComponentValue(ADM_FLAG_FIELD_ID, admFlag);
        waitForPageToLoad();
    }

    @Step("Set Reference Channel Modulation = {referenceChannelModulation}")
    public void setReferenceChannelModulation(String referenceChannelModulation) {
        wizard.setComponentValue(REFERENCE_CHANNEL_MODULATION_FIELD_ID, referenceChannelModulation);
        waitForPageToLoad();
    }

    @Step("Set Highest Channel Modulation = {highestChannelModulation}")
    public void setHighestChannelModulation(String highestChannelModulation) {
        wizard.setComponentValue(HIGHEST_CHANNEL_MODULATION_FIELD_ID, highestChannelModulation);
        waitForPageToLoad();
    }

    @Step("Set Lowest Channel Modulation = {lowestChannelModulation}")
    public void setLowestChannelModulation(String lowestChannelModulation) {
        wizard.setComponentValue(LOWEST_CHANNEL_MODULATION_FIELD_ID, lowestChannelModulation);
        waitForPageToLoad();
    }

    @Step("Set Start Tx Power = {startTxPower}")
    public void setStartTxPower(String startTxPower) {
        wizard.setComponentValue(START_TX_POWER_FIELD_ID, startTxPower);
        waitForPageToLoad();
    }

    @Step("Set End Tx Power = {endTxPower}")
    public void setEndTxPower(String endTxPower) {
        wizard.setComponentValue(END_TX_POWER_FIELD_ID, endTxPower);
        waitForPageToLoad();
    }

    @Step("Set Start Rx Power = {startRxPower}")
    public void setStartRxPower(String startRxPower) {
        wizard.setComponentValue(START_RX_POWER_FIELD_ID, startRxPower);
        waitForPageToLoad();
    }

    @Step("Set End Rx Power = {endRxPower}")
    public void setEndRxPower(String endRxPower) {
        wizard.setComponentValue(END_RX_POWER_FIELD_ID, endRxPower);
        waitForPageToLoad();
    }

    @Step("Set ATPC flag = {atpc}")
    public void setATPC(String atpc) {
        wizard.setComponentValue(ATPC_FIELD_ID, atpc);
        waitForPageToLoad();
    }

    @Step("Set ATPC Rx Max Level = {atpcRxMaxLevel}")
    public void setATPCRxMaxLevel(String atpcRxMaxLevel) {
        wizard.setComponentValue(ATPC_RX_MAX_LEVEL_FIELD_ID, atpcRxMaxLevel);
        waitForPageToLoad();
    }

    @Step("Set ATPC Rx Min Level = {atpcRxMinLevel}")
    public void setATPCRxMinLevel(String atpcRxMinLevel) {
        wizard.setComponentValue(ATPC_RX_MIN_LEVEL_FIELD_ID, atpcRxMinLevel);
        waitForPageToLoad();
    }

    @Step("Set XPOL flag = {xpolFlag}")
    public void setXPOLflag(String xpolFlag) {
        wizard.setComponentValue(XPOL_FLAG_FIELD_ID, xpolFlag);
        waitForPageToLoad();
    }

    @Step("Set HSB flag = {hsbFlag}")
    public void setHSBflag(String hsbFlag) {
        wizard.setComponentValue(HSB_FLAG_FIELD_ID, hsbFlag);
        waitForPageToLoad();
    }

    @Step("Set Start Waveguide Model = {startWaveguideModel}")
    public void setStartWaveguideModel(String startWaveguideModel) {
        wizard.setComponentValue(START_WAVEGUIDE_MODEL_FIELD_ID, startWaveguideModel);
        waitForPageToLoad();
    }

    @Step("Set End Waveguide Model = {endWaveguideModel}")
    public void setEndWaveguideModel(String endWaveguideModel) {
        wizard.setComponentValue(END_WAVEGUIDE_MODEL_FIELD_ID, endWaveguideModel);
        waitForPageToLoad();
    }

    @Step("Set Start Waveguide Length = {startWaveguideLength}")
    public void setStartWaveguideLength(String startWaveguideLength) {
        wizard.setComponentValue(START_WAVEGUIDE_LENGTH_FIELD_ID, startWaveguideLength);
        waitForPageToLoad();
    }

    @Step("Set End Waveguide Length = {endWaveguideLength}")
    public void setEndWaveguideLength(String endWaveguideLength) {
        wizard.setComponentValue(END_WAVEGUIDE_LENGTH_FIELD_ID, endWaveguideLength);
        waitForPageToLoad();
    }

    @Step("Set Start Diversity Waveguide Model = {startDiversityWaveguideModel}")
    public void setStartDiversityWaveguideModel(String startDiversityWaveguideModel) {
        wizard.setComponentValue(START_DIVERSITY_WAVEGUIDE_MODEL_FIELD_ID, startDiversityWaveguideModel);
        waitForPageToLoad();
    }

    @Step("Set End Diversity Waveguide Model = {endDiversityWaveguideModel}")
    public void setEndDiversityWaveguideModel(String endDiversityWaveguideModel) {
        wizard.setComponentValue(END_DIVERSITY_WAVEGUIDE_MODEL_FIELD_ID, endDiversityWaveguideModel);
        waitForPageToLoad();
    }

    @Step("Set Start Diversity Waveguide Length = {startDiversityWaveguideLength}")
    public void setStartDiversityWaveguideLength(String startDiversityWaveguideLength) {
        wizard.setComponentValue(START_DIVERSITY_WAVEGUIDE_LENGTH_FIELD_ID, startDiversityWaveguideLength);
        waitForPageToLoad();
    }

    @Step("Set End Diversity Waveguide Length = {endDiversityWaveguideLength}")
    public void setEndDiversityWaveguideLength(String endDiversityWaveguideLength) {
        wizard.setComponentValue(END_DIVERSITY_WAVEGUIDE_LENGTH_FIELD_ID, endDiversityWaveguideLength);
        waitForPageToLoad();
    }

    @Step("Set Start Attenuator Model = {startAttenuatorModel}")
    public void setStartAttenuatorModel(String startAttenuatorModel) {
        wizard.setComponentValue(START_ATTENUATOR_MODEL_FIELD_ID, startAttenuatorModel);
        waitForPageToLoad();
    }

    @Step("Set End Attenuator Model = {endAttenuatorModel}")
    public void setEndAttenuatorModel(String endAttenuatorModel) {
        wizard.setComponentValue(END_ATTENUATOR_MODEL_FIELD_ID, endAttenuatorModel);
        waitForPageToLoad();
    }

    @Step("Set Start Attenuator Mode = {startAttenuatorMode}")
    public void setStartAttenuatorMode(String startAttenuatorMode) {
        wizard.setComponentValue(START_ATTENUATOR_MODE_FIELD_ID, startAttenuatorMode);
        waitForPageToLoad();
    }

    @Step("Set End Attenuator Mode = {endAttenuatorMode}")
    public void setEndAttenuatorMode(String endAttenuatorMode) {
        wizard.setComponentValue(END_ATTENUATOR_MODE_FIELD_ID, endAttenuatorMode);
        waitForPageToLoad();
    }

    @Step("Set Start Termination on {terminationName}")
    public void setTermination(String terminationFieldId, String terminationName) {
        wizard.setComponentValue(terminationFieldId, terminationName);
        waitForPageToLoad();
    }

    @Step("Click Accept button")
    public void clickAccept() {
        getWizard().clickAccept();
        waitForPageToLoad();
    }

    @Step("Click Next button")
    public void clickNext() {
        getWizard().clickNext();
        waitForPageToLoad();
    }

    private Wizard getWizard() {
        return wizard;
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
