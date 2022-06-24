package com.oss.E2E;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.CardCreateWizardPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.pages.platform.toolsmanager.ToolsManagerPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.trail.v2.MicrowaveChannelWizardPage;

import io.qameta.allure.Description;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class TP_OSS_MicrowaveE2ETest extends BaseTestCase {
    private static final String INFRASTRUCTURE_MANAGEMENT_CATEGORY_NAME = "Infrastructure management";
    private static final String CREATE_DEVICE_APPLICATION_NAME = "Create Device";
    private static final String RESOURCE_INVENTORY_CATEGORY_NAME = "Resource Inventory";
    private static final String NETWORK_VIEW_APPLICATION_NAME = "Network View";
    private static final String CREATE_CARD_BUTTON_ID = "CreateCardOnDeviceAction";

    private static final String FIRST_LOCATION_NAME = "1_SeleniumE2ETest_Location";
    private static final String SECOND_LOCATION_NAME = "2_SeleniumE2ETest_Location";
    private static final String INDOOR_UNIT_MODEL = "Ericsson Mini-Link TN AMM 6p D";
    private static final String OUTDOOR_UNIT_MODEL = "Ericsson RAU2 N 38";
    private static final String MICROWAVE_ANTENNA_MODEL = "Ericsson Mini-Link TN AMM 6p D";
    private static final String FIRST_INDOOR_UNIT_NAME = "1_SeleniumE2ETest_IDU";
    private static final String SECOND_INDOOR_UNIT_NAME = "2_SeleniumE2ETest_IDU";
    private static final String FIRST_MICROWAVE_ANTENNA_NAME = "1_SeleniumE2ETest_MWANT";
    private static final String SECOND_MICROWAVE_ANTENNA_NAME = "2_SeleniumE2ETest_MWANT";
    private static final String FIRST_OUTDOOR_UNIT_NAME = "1_SeleniumE2ETest_ODU";
    private static final String SECOND_OUTDOOR_UNIT_NAME = "2_SeleniumE2ETest_ODU";
    private static final String CARD_MODEL_NAME = "Ericsson MMU2 H";
    private static final String SLOT_NAME = "AMM 6p D\\02";

    private static final String DESCRIPTION = "Desc1";
    private static final String BAND = "23";
    private static final String CHANNEL_BANDWIDTH = "28";
    private static final String HIGH_FREQUENCY_SITE = "Start Site";
    private static final String POLARIZATION = "Vertical";
    private static final String WORKING_STATUS = "Working";
    private static final String DCN_LOCATION = "Start Site";
    private static final String CHANNEL_NUMBER = "40";
    private static final String CONFIGURATION = "1 + 2";
    private static final String CHANNEL_NAME = "A001";

    private static final String RADIO_MODEL = "HYBR9IF1HP23G28M35E1";
    private static final String ADM_FLAG = "true";
    private static final String REFERENCE_CHANNEL_MODULATION = "64QAM";
    private static final String HIGHEST_CHANNEL_MODULATION = "128QAM";
    private static final String LOWEST_CHANNEL_MODULATION = "16QAM";

    private static final String START_TX_POWER = "9";
    private static final String END_TX_POWER = "8";
    private static final String START_RX_POWER = "7";
    private static final String END_RX_POWER = "6";
    private static final String ATPC = "true";
    private static final String ATPC_RX_MAX_LEVEL = "5";
    private static final String ATPC_RX_MIN_LEVEL = "4";
    private static final String XPOL_FLAG = "true";
    private static final String HSB_FLAG = "true";
    private static final String START_WAVEGUIDE_MODEL = "E220J";
    private static final String END_WAVEGUIDE_MODEL = "Flex 23";
    private static final String START_WAVEGUIDE_LENGTH = "10";
    private static final String END_WAVEGUIDE_LENGTH = "20";
    private static final String START_DIVERSITY_WAVEGUIDE_MODEL = "Flex 23";
    private static final String END_DIVERSITY_WAVEGUIDE_MODEL = "E220J";
    private static final String START_DIVERSITY_WAVEGUIDE_LENGTH = "30";
    private static final String END_DIVERSITY_WAVEGUIDE_LENGTH = "40";
    private static final String START_ATTENUATOR_MODEL = "20dB 23GHz";
    private static final String END_ATTENUATOR_MODEL = "20dB 23GHz";
    private static final String START_ATTENUATOR_MODE = "Rx";
    private static final String END_ATTENUATOR_MODE = "Tx";

    private static final String CARD_NAME = "AMM 6p D\\02\\MMU2 H";
    private static final String START_CARD_FIELD_ID = "terminationCardComponent_1";
    private static final String END_CARD_FIELD_ID = "terminationCardComponent_2";

    private SoftAssert softAssert;

    @Test(priority = 1, description = "Open Network View")
    @Description("Open Network View")
    public void createDevices() {
        createIndoorUnit(INDOOR_UNIT_MODEL, FIRST_INDOOR_UNIT_NAME, FIRST_LOCATION_NAME);

        createIndoorUnit(INDOOR_UNIT_MODEL, SECOND_INDOOR_UNIT_NAME, SECOND_LOCATION_NAME);

        //createMicrowaveAntenna(MICROWAVE_ANTENNA_MODEL, FIRST_MICROWAVE_ANTENNA_NAME, FIRST_LOCATION_NAME);

        //createMicrowaveAntenna(MICROWAVE_ANTENNA_MODEL, SECOND_MICROWAVE_ANTENNA_NAME, SECOND_LOCATION_NAME);

        //createOutdoorUnit(OUTDOOR_UNIT_MODEL, FIRST_OUTDOOR_UNIT_NAME, FIRST_LOCATION_NAME);

        //createOutdoorUnit(OUTDOOR_UNIT_MODEL, SECOND_OUTDOOR_UNIT_NAME, SECOND_LOCATION_NAME);
    }

    @Test(priority = 2, description = "Create Microwave Channels")
    @Description("Add Indoor Units to View and create Microwave Channels on them")
    public void createMicrowaveChannels() {
        openNetworkView();
        addObjectToView("name", TEXT_FIELD, FIRST_INDOOR_UNIT_NAME);
        addObjectToView("name", TEXT_FIELD, SECOND_INDOOR_UNIT_NAME);
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandContentPanel();
        networkViewPage.selectObjectInViewContent("Name", FIRST_INDOOR_UNIT_NAME);
        waitForPageToLoad();
        networkViewPage.openWizardPage("Microwave Channel");

        MicrowaveChannelAttributes microwaveChannelAttributes = getFirstMicrowaveChannelAttributesToCreate();
        MicrowaveChannelWizardPage microwaveChannelWizardPage = new MicrowaveChannelWizardPage(driver);
        fillMicrowaveChannelWizard(microwaveChannelWizardPage, microwaveChannelAttributes);
    }

    private void openNetworkView() {
        homePage.goToHomePage(driver, BASIC_URL);
        expandTiles(RESOURCE_INVENTORY_CATEGORY_NAME, NETWORK_VIEW_APPLICATION_NAME);
        waitForPageToLoad();
    }

    private void openPhysicalDeviceWizard() {
        homePage.goToHomePage(driver, BASIC_URL);
        expandTiles(INFRASTRUCTURE_MANAGEMENT_CATEGORY_NAME, CREATE_DEVICE_APPLICATION_NAME);
        waitForPageToLoad();
    }

    private void expandTiles(String categoryName, String applicationName) {
        ToolsManagerPage toolsManagerPage = new ToolsManagerPage(driver);
        ToolsManagerWindow toolsManagerWindow = toolsManagerPage.getToolsManager();
        waitForPageToLoad();
        toolsManagerWindow.openApplication(categoryName, applicationName);
    }

    private void addObjectToView(String componentId, Input.ComponentType componentType, String value) {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        networkViewPage.queryElementAndAddItToView(componentId, componentType, value);
    }

    private void createPhysicalDevice(String deviceModel, String deviceName, String locationName) {
        openPhysicalDeviceWizard();
        waitForPageToLoad();
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(deviceModel);
        waitForPageToLoad();
        deviceWizardPage.setName(deviceName);
        waitForPageToLoad();
        deviceWizardPage.next();
        waitForPageToLoad();
        deviceWizardPage.setPreciseLocation(locationName);
        waitForPageToLoad();
        deviceWizardPage.accept();
    }

    private void createIndoorUnit(String indoorUnitModel, String indoorUnitName, String locationName) {
        createPhysicalDevice(indoorUnitModel, indoorUnitName, locationName);
        waitForPageToLoad();
        addCardToPhysicalDevice(indoorUnitName, CARD_MODEL_NAME, SLOT_NAME);
        waitForPageToLoad();
    }

    private void createMicrowaveAntenna(String microwaveAntennaModel, String microwaveAntennaName, String locationName) {
        createPhysicalDevice(microwaveAntennaModel, microwaveAntennaName, locationName);
        waitForPageToLoad();
    }

    private void createOutdoorUnit(String outdoorUnitModel, String outdoorUnitName, String locationName) {
        createPhysicalDevice(outdoorUnitModel, outdoorUnitName, locationName);
        waitForPageToLoad();
    }

    private MicrowaveChannelAttributes getFirstMicrowaveChannelAttributesToCreate() {
        MicrowaveChannelAttributes attributes = new MicrowaveChannelAttributes();
        attributes.description = DESCRIPTION;
        attributes.band = BAND;
        attributes.channelBandwidth = CHANNEL_BANDWIDTH;
        attributes.highFrequencySite = HIGH_FREQUENCY_SITE;
        attributes.polarization = POLARIZATION;
        attributes.workingStatus = WORKING_STATUS;
        attributes.dcnLocation = DCN_LOCATION;
        attributes.channelNumber = CHANNEL_NUMBER;
        attributes.configuration = CONFIGURATION;
        attributes.channelName = CHANNEL_NAME;
        attributes.startRadioModel = RADIO_MODEL;
        attributes.endRadioModel = RADIO_MODEL;
        attributes.admFlag = ADM_FLAG;
        attributes.referenceChannelModulation = REFERENCE_CHANNEL_MODULATION;
        attributes.highestChannelModulation = HIGHEST_CHANNEL_MODULATION;
        attributes.lowestChannelModulation = LOWEST_CHANNEL_MODULATION;
        attributes.startTxPower = START_TX_POWER;
        attributes.endTxPower = END_TX_POWER;
        attributes.startRxPower = START_RX_POWER;
        attributes.endRxPower = END_RX_POWER;
        attributes.atpc = ATPC;
        attributes.atpcRxMaxLevel = ATPC_RX_MAX_LEVEL;
        attributes.atpcRxMinLevel = ATPC_RX_MIN_LEVEL;
        attributes.xpolFlag = XPOL_FLAG;
        attributes.hsbFlag = HSB_FLAG;
        attributes.startWaveguideModel = START_WAVEGUIDE_MODEL;
        attributes.endWaveguideModel = END_WAVEGUIDE_MODEL;
        attributes.startWaveguideLength = START_WAVEGUIDE_LENGTH;
        attributes.endWaveguideLength = END_WAVEGUIDE_LENGTH;
        attributes.startDiversityWaveguideModel = START_DIVERSITY_WAVEGUIDE_MODEL;
        attributes.endDiversityWaveguideModel = END_DIVERSITY_WAVEGUIDE_MODEL;
        attributes.startDiversityWaveguideLength = START_DIVERSITY_WAVEGUIDE_LENGTH;
        attributes.endDiversityWaveguideLength = END_DIVERSITY_WAVEGUIDE_LENGTH;
        attributes.startAttenuatorModel = START_ATTENUATOR_MODEL;
        attributes.endAttenuatorModel = END_ATTENUATOR_MODEL;
        attributes.startAttenuatorMode = START_ATTENUATOR_MODE;
        attributes.endAttenuatorMode = END_ATTENUATOR_MODE;

        return attributes;
    }

    private static class MicrowaveChannelAttributes {
        private String description;
        private String band;
        private String channelBandwidth;
        private String highFrequencySite;
        private String polarization;
        private String workingStatus;
        private String dcnLocation;
        private String channelNumber;
        private String configuration;
        private String channelName;
        private String startRadioModel;
        private String endRadioModel;
        private String admFlag;
        private String referenceChannelModulation;
        private String highestChannelModulation;
        private String lowestChannelModulation;
        private String startTxPower;
        private String endTxPower;
        private String startRxPower;
        private String endRxPower;
        private String atpc;
        private String atpcRxMaxLevel;
        private String atpcRxMinLevel;
        private String xpolFlag;
        private String hsbFlag;
        private String startWaveguideModel;
        private String endWaveguideModel;
        private String startWaveguideLength;
        private String endWaveguideLength;
        private String startDiversityWaveguideModel;
        private String endDiversityWaveguideModel;
        private String startDiversityWaveguideLength;
        private String endDiversityWaveguideLength;
        private String startAttenuatorModel;
        private String endAttenuatorModel;
        private String startAttenuatorMode;
        private String endAttenuatorMode;
    }

    private void fillMicrowaveChannelWizard(MicrowaveChannelWizardPage microwaveChannelWizardPage, MicrowaveChannelAttributes microwaveChannelAttributes) {
        fillFirstStepOfMicrowaveChannelWizard(microwaveChannelWizardPage, microwaveChannelAttributes);
        fillSecondStepOfMicrowaveChannelWizard(microwaveChannelWizardPage, microwaveChannelAttributes);
        fillThirdStepOfMicrowaveChannelWizard(microwaveChannelWizardPage, microwaveChannelAttributes);
        fillForthStepOfMicrowaveChannelWizard(microwaveChannelWizardPage, microwaveChannelAttributes);
        fillFifthStepOfMicrowaveChannelWizard(microwaveChannelWizardPage, microwaveChannelAttributes);
    }

    private void fillFirstStepOfMicrowaveChannelWizard(MicrowaveChannelWizardPage microwaveChannelWizardPage, MicrowaveChannelAttributes microwaveChannelAttributes) {
        microwaveChannelWizardPage.setDescription(microwaveChannelAttributes.description);
        microwaveChannelWizardPage.setBand(microwaveChannelAttributes.band);
        microwaveChannelWizardPage.setChannelBandwidth(microwaveChannelAttributes.channelBandwidth);
        microwaveChannelWizardPage.setHighFrequencySite(microwaveChannelAttributes.highFrequencySite);
        microwaveChannelWizardPage.setPolarization(microwaveChannelAttributes.polarization);
        microwaveChannelWizardPage.setWorkingStatus(microwaveChannelAttributes.workingStatus);
        microwaveChannelWizardPage.setDCNLocation(microwaveChannelAttributes.dcnLocation);
        microwaveChannelWizardPage.setChannelNumber(microwaveChannelAttributes.channelNumber);
        microwaveChannelWizardPage.setConfiguration(microwaveChannelAttributes.configuration);
        microwaveChannelWizardPage.setMicrowaveFrequencyPlan();
        waitForPageToLoad();
        microwaveChannelWizardPage.clickNext();
    }

    private void fillSecondStepOfMicrowaveChannelWizard(MicrowaveChannelWizardPage microwaveChannelWizardPage, MicrowaveChannelAttributes microwaveChannelAttributes) {
        microwaveChannelWizardPage.setStartRadioModel(microwaveChannelAttributes.startRadioModel);
        microwaveChannelWizardPage.setAdmFlag(microwaveChannelAttributes.admFlag);
        //microwaveChannelWizardPage.setReferenceChannelModulation(microwaveChannelAttributes.referenceChannelModulation);
        //microwaveChannelWizardPage.setHighestChannelModulation(microwaveChannelAttributes.highestChannelModulation);
        //microwaveChannelWizardPage.setLowestChannelModulation(microwaveChannelAttributes.lowestChannelModulation);
        waitForPageToLoad();
        microwaveChannelWizardPage.clickNext();
    }

    private void fillThirdStepOfMicrowaveChannelWizard(MicrowaveChannelWizardPage microwaveChannelWizardPage, MicrowaveChannelAttributes microwaveChannelAttributes) {
        microwaveChannelWizardPage.setStartTxPower(microwaveChannelAttributes.startTxPower);
        microwaveChannelWizardPage.setEndTxPower(microwaveChannelAttributes.endTxPower);
        microwaveChannelWizardPage.setStartRxPower(microwaveChannelAttributes.startRxPower);
        microwaveChannelWizardPage.setEndRxPower(microwaveChannelAttributes.endRxPower);
        microwaveChannelWizardPage.setATPC(microwaveChannelAttributes.atpc);
        microwaveChannelWizardPage.setATPCRxMaxLevel(microwaveChannelAttributes.atpcRxMaxLevel);
        microwaveChannelWizardPage.setATPCRxMinLevel(microwaveChannelAttributes.atpcRxMinLevel);
        microwaveChannelWizardPage.setXPOLflag(microwaveChannelAttributes.xpolFlag);
        microwaveChannelWizardPage.setHSBflag(microwaveChannelAttributes.hsbFlag);
        microwaveChannelWizardPage.clickNext();
    }

    private void fillForthStepOfMicrowaveChannelWizard(MicrowaveChannelWizardPage microwaveChannelWizardPage, MicrowaveChannelAttributes microwaveChannelAttributes) {
        microwaveChannelWizardPage.setStartWaveguideModel(microwaveChannelAttributes.startWaveguideModel);
        microwaveChannelWizardPage.setStartWaveguideLength(microwaveChannelAttributes.startWaveguideLength);
        microwaveChannelWizardPage.setEndWaveguideLength(microwaveChannelAttributes.endWaveguideLength);
        microwaveChannelWizardPage.setStartDiversityWaveguideModel(microwaveChannelAttributes.startDiversityWaveguideModel);
        microwaveChannelWizardPage.setStartDiversityWaveguideLength(microwaveChannelAttributes.startDiversityWaveguideLength);
        microwaveChannelWizardPage.setEndDiversityWaveguideLength(microwaveChannelAttributes.endDiversityWaveguideLength);
        microwaveChannelWizardPage.setStartAttenuatorModel(microwaveChannelAttributes.startAttenuatorModel);
        microwaveChannelWizardPage.setStartAttenuatorMode(microwaveChannelAttributes.startAttenuatorMode);
        microwaveChannelWizardPage.setEndAttenuatorMode(microwaveChannelAttributes.endAttenuatorMode);
        waitForPageToLoad();
        microwaveChannelWizardPage.clickNext();
    }

    private void fillFifthStepOfMicrowaveChannelWizard(MicrowaveChannelWizardPage microwaveChannelWizardPage, MicrowaveChannelAttributes microwaveChannelAttributes){
        microwaveChannelWizardPage.setTermination(START_CARD_FIELD_ID, CARD_NAME);
        microwaveChannelWizardPage.setTermination(END_CARD_FIELD_ID, CARD_NAME);
        waitForPageToLoad();
        microwaveChannelWizardPage.clickAccept();
    }

    private void addCardToPhysicalDevice(String deviceName, String modelName, String slotName) {
        HierarchyViewPage hierarchyViewPage = new HierarchyViewPage(driver);
        hierarchyViewPage.selectNodeByLabel(deviceName);
        hierarchyViewPage.useTreeContextAction(ActionsContainer.CREATE_GROUP_ID, CREATE_CARD_BUTTON_ID);
        CardCreateWizardPage createWizardPage = new CardCreateWizardPage(driver);
        createWizardPage.setModel(modelName);
        createWizardPage.setSlot(slotName);
        createWizardPage.clickAccept();
    }

    private void checkSystemMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        softAssert.assertEquals((systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType()), SystemMessageContainer.MessageType.SUCCESS);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

}
