package com.oss.E2E;

import java.util.Random;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.physical.CardCreateWizardPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.HierarchyViewPage;
import com.oss.pages.platform.toolsmanager.ToolsManagerPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.trail.v2.MicrowaveChannelWizardPage;
import com.oss.pages.transport.trail.v2.MicrowaveLinkWizardPage;

import io.qameta.allure.Description;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class TP_OSS_MicrowaveE2ETest extends BaseTestCase {

    private static final String NAME_COMPONENT_ID = "name";
    private static final String NAME_COLUMN_NAME = "Name";
    private static final String MICROWAVE_CHANNEL_TRAIL_TYPE = "Microwave Channel";
    private static final String MICROWAVE_CHANNEL_PARTIAL_NAME = "MicrowaveChannel";
    private static final String MICROWAVE_LINK_TRAIL_TYPE = "Microwave Link";
    private static final String INFRASTRUCTURE_MANAGEMENT_CATEGORY_NAME = "Infrastructure Management";
    private static final String CREATE_DEVICE_APPLICATION_NAME = "Create Physical Device";
    private static final String RESOURCE_INVENTORY_CATEGORY_NAME = "Resource Inventory";
    private static final String NETWORK_VIEW_APPLICATION_NAME = "Network View";
    private static final String CREATE_CARD_BUTTON_ID = "CreateCardOnDeviceAction";
    private static final String CONENT_TAB_ID = "LeftPanelWidget";
    private static final String CARD_SHORT_IDENTIFIER_COLUMN = "card.shortIdentifier";
    private static final String PORT_SHORT_IDENTIFIER_COLUMN = "port.shortIdentifier";
    private static final String TERMINATIONS_TAB_ID = "TerminationWidget";
    private static final String CONTENT_DOCKED_PANEL_POSITION = "left";
    private static final String DETAILS_DOCKED_PANEL_POSITION = "bottom";

    private static final String NAME_ATTRIBUTE_LABEL = "Name";
    private static final String BAND_ATTRIBUTE_LABEL = "Band";
    private static final String CHANNEL_BANDWIDTH_ATTRIBUTE_LABEL = "ChannelBandwidth";
    private static final String LOW_FREQUENCY_ATTRIBUTE_LABEL = "LowFrequency";
    private static final String HIGH_FREQUENCY_ATTRIBUTE_LABEL = "HighFrequency";
    private static final String POLARIZATION_ATTRIBUTE_LABEL = "Polarization";
    private static final String WORKING_STATUS_ATTRIBUTE_LABEL = "WorkingStatus";
    private static final String HSB_FLAG_ATTRIBUTE_LABEL = "HSBFlag";
    private static final String XPOL_FLAG_ATTRIBUTE_LABEL = "XPOLFlag";
    private static final String ADM_FLAG_ATTRIBUTE_LABEL = "ADMFlag";
    private static final String REFERENCE_CHANNEL_MODULATION_ATTRIBUTE_LABEL = "ReferenceChannelModulation";
    private static final String LOWEST_CHANNEL_MODULATION_ATTRIBUTE_LABEL = "LowestChannelModulation";
    private static final String HIGHEST_CHANNEL_MODULATION_ATTRIBUTE_LABEL = "HighestChannelModulation";
    private static final String START_TX_POWER_ATTRIBUTE_LABEL = "StartTxPower";
    private static final String END_TX_POWER_ATTRIBUTE_LABEL = "EndTxPower";
    private static final String START_RX_POWER_ATTRIBUTE_LABEL = "StartRxPower";
    private static final String END_RX_POWER_ATTRIBUTE_LABEL = "EndRxPower";
    private static final String ATPC_ATTRIBUTE_LABEL = "ATPC";
    private static final String ATPC_RX_MIN_LEVEL_ATTRIBUTE_LABEL = "ATPCRxMinLevel";
    private static final String ATPC_RX_MAX_LEVEL_ATTRIBUTE_LABEL = "ATPCRxMaxLevel";
    private static final String DCN_LOCATION_ATTRIBUTE_LABEL = "DCNLocation";
    private static final String END_ATTENUATOR_MANUFACTURER_ATTRIBUTE_LABEL = "EndAttenuator1Manufacturer";
    private static final String END_ATTENUATOR_MODEL_ATTRIBUTE_LABEL = "EndAttenuator1Model";
    private static final String END_DIVERSITY_WAVEGUIDE_LENGTH_ATTRIBUTE_LABEL = "EndDiversityWaveguide1Length";
    private static final String END_DIVERSITY_WAVEGUIDE_MANUFACTURER_ATTRIBUTE_LABEL = "EndDiversityWaveguide1Manufacturer";
    private static final String END_DIVERSITY_WAVEGUIDE_MODEL_ATTRIBUTE_LABEL = "EndDiversityWaveguide1Model";
    private static final String START_ATTENUATOR_MANUFACTURER_ATTRIBUTE_LABEL = "StartAttenuator1Manufacturer";
    private static final String START_ATTENUATOR_MODEL_ATTRIBUTE_LABEL = "StartAttenuator1Model";
    private static final String START_DIVERSITY_WAVEGUIDE_LENGTH_ATTRIBUTE_LABEL = "StartDiversityWaveguide1Length";
    private static final String START_DIVERSITY_WAVEGUIDE_MANUFACTURER_ATTRIBUTE_LABEL = "StartDiversityWaveguide1Manufacturer";
    private static final String START_DIVERSITY_WAVEGUIDE_MODEL_ATTRIBUTE_LABEL = "StartDiversityWaveguide1Model";
    private static final String START_DIVERSITY_WAVEGUIDE_TOTAL_LOSS_ATTRIBUTE_LABEL = "StartDiversityWaveguide1TotalLoss";
    private static final String START_RADIO_MODEL_ATTRIBUTE_LABEL = "StartRadioModel";
    private static final String START_WAVEGUIDE_MANUFACTURER_ATTRIBUTE_LABEL = "StartWaveguide1Manufacturer";
    private static final String START_WAVEGUIDE_MODEL_ATTRIBUTE_LABEL = "StartWaveguide1Model";
    private static final String START_WAVEGUIDE_TOTAL_LOSS_ATTRIBUTE_LABEL = "StartWaveguide1TotalLoss";
    private static final String DESCRIPTION_ATTRIBUTE_LABEL = "description";
    private static final String CHANNEL_NUMBER_ATTRIBUTE_LABEL = "ChannelNumber";
    private static final String CONFIGURATION_ATTRIBUTE_LABEL = "Configuration";
    private static final String END_ATTENUATOR_LOSS_ATTRIBUTE_LABEL = "EndAttenuator1Loss";
    private static final String END_DIVERSITY_WAVEGUIDE_TOTAL_LOSS_ATTRIBUTE_LABEL = "EndDiversityWaveguide1TotalLoss";
    private static final String END_RADIO_MODEL_ATTRIBUTE_LABEL = "EndRadioModel";
    private static final String END_WAVEGUIDE_LENGTH_ATTRIBUTE_LABEL = "EndWaveguide1Length";
    private static final String END_WAVEGUIDE_MANUFACTURER_ATTRIBUTE_LABEL = "EndWaveguide1Manufacturer";
    private static final String END_WAVEGUIDE_MODEL_ATTRIBUTE_LABEL = "EndWaveguide1Model";
    private static final String END_WAVEGUIDE_TOTAL_LOSS_ATTRIBUTE_LABEL = "EndWaveguide1TotalLoss";
    private static final String START_ATTENUATOR_LOSS_ATTRIBUTE_LABEL = "StartAttenuator1Loss";
    private static final String START_WAVEGUIDE_LENGTH_ATTRIBUTE_LABEL = "StartWaveguide1Length";
    private static final String END_ATTENUATOR_MODE_ATTRIBUTE_LABEL = "EndAttenuator1Mode";
    private static final String HIGH_FREQUENCY_SITE_ATTRIBUTE_LABEL = "HighFrequencySite";
    private static final String START_ATTENUATOR_MODE_ATTRIBUTE_LABEL = "StartAttenuator1Mode";

    private static final String CAPACITY_VALUE_ATTRIBUTE_LABEL = "capacityValue";
    private static final String PATH_LENGTH_ATTRIBUTE_LABEL = "PathLength";
    private static final String NETWORK_ATTRIBUTE_LABEL = "Network";
    private static final String TECHNOLOGY_TYPE_ATTRIBUTE_LABEL = "TechnologyType";
    private static final String AGGREGATION_CONFIGURATION_ATTRIBUTE_LABEL = "AggregationConfiguration";
    private static final String USER_LABEL_ATTRIBUTE_LABEL = "UserLabel";
    private static final String LINK_ID_ATTRIBUTE_LABEL = "LinkID";
    private static final String NUMBER_OF_WORKING_CHANNELS_ATTRIBUTE_LABEL = "NumberOfWorkingChannels";
    private static final String NUMBER_OF_PROTECTING_CHANNELS_ATTRIBUTE_LABEL = "NumberOfProtectingChannels";

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

    private static final String SLOT_NAME = "AMM 6p D\\02";
    private static final String CARD_MODEL_NAME = "Ericsson MMU2 H";
    private static final String CARD_NAME = "AMM 6p D\\02\\MMU2 H";
    private static final String START_CARD_FIELD_ID = "terminationCardComponent_1";
    private static final String END_CARD_FIELD_ID = "terminationCardComponent_2";

    private static final String START_PORT_FIELD_ID = "terminationPortComponent_1";
    private static final String END_PORT_FIELD_ID = "terminationPortComponent_2";
    private static final String PORT_NAME = "RAU";
    private static final String PORT_LABEL = "AMM 6p D\\02\\MMU2 H\\RAU";

    private static final String MWC_DESCRIPTION = "Desc1";
    private static final String BAND = "23";
    private static final String CHANNEL_BANDWIDTH = "28";
    private static final String HIGH_FREQUENCY_SITE = "Start Site";
    private static final String POLARIZATION = "Vertical";
    private static final String WORKING_STATUS = "Working";
    private static final String DCN_LOCATION = "Start Site";
    private static final String CHANNEL_NUMBER = "40";
    private static final String CONFIGURATION = "1 + 2";
    private static final String CHANNEL_NAME = "A001";
    private static final String LOW_FREQUENCY = "21819";
    private static final String HIGH_FREQUENCY = "23051";

    private static final String MWC_DESCRIPTION2 = "Opis2";
    private static final String BAND2 = "38";
    private static final String CHANNEL_BANDWIDTH2 = "14";
    private static final String HIGH_FREQUENCY_SITE2 = "End Site";
    private static final String POLARIZATION2 = "Vertical+Horizontal";
    private static final String WORKING_STATUS2 = "Protecting";
    private static final String DCN_LOCATION2 = "End Site";
    private static final String CHANNEL_NUMBER2 = "1";
    private static final String CONFIGURATION2 = "A + B";
    private static final String CHANNEL_NAME2 = "001";
    private static final String LOW_FREQUENCY2 = "37009";
    private static final String HIGH_FREQUENCY2 = "38269";

    private static final String RADIO_MODEL = "HYB_TNMMU2H/R2N_23G28M46_180";
    private static final String ADM_FLAG = "true";
    private static final String REFERENCE_CHANNEL_MODULATION = "64QAM";
    private static final String HIGHEST_CHANNEL_MODULATION = "128QAM";
    private static final String LOWEST_CHANNEL_MODULATION = "16QAM";

    private static final String RADIO_MODEL2 = "HYB_TNMMU2H/R2N_38G14M21_84";
    private static final String ADM_FLAG2 = "true";
    private static final String REFERENCE_CHANNEL_MODULATION2 = "128QAM";
    private static final String HIGHEST_CHANNEL_MODULATION2 = "256QAM";
    private static final String LOWEST_CHANNEL_MODULATION2 = "64QAM";

    private static final String START_TX_POWER = "9";
    private static final String END_TX_POWER = "8";
    private static final String START_RX_POWER = "7";
    private static final String END_RX_POWER = "6";
    private static final String ATPC = "true";
    private static final String ATPC_RX_MAX_LEVEL = "5";
    private static final String ATPC_RX_MIN_LEVEL = "4";
    private static final String XPOL_FLAG = "true";
    private static final String HSB_FLAG = "true";

    private static final String START_TX_POWER2 = "87";
    private static final String END_TX_POWER2 = "76";
    private static final String START_RX_POWER2 = "65";
    private static final String END_RX_POWER2 = "54";
    private static final String ATPC2 = "true";
    private static final String ATPC_RX_MAX_LEVEL2 = "43";
    private static final String ATPC_RX_MIN_LEVEL2 = "32";
    private static final String XPOL_FLAG2 = "false";
    private static final String HSB_FLAG2 = "false";

    private static final String START_WAVEGUIDE_MODEL = "E220J";
    private static final String START_WAVEGUIDE_MANUFACTURER = "RFS";
    private static final String START_WAVEGUIDE_LENGTH = "10";
    private static final String START_WAVEGUIDE_TOTAL_LOSS = "2.817";
    private static final String END_WAVEGUIDE_MODEL = "E220J";
    private static final String END_WAVEGUIDE_MANUFACTURER = "RFS";
    private static final String END_WAVEGUIDE_LENGTH = "20";
    private static final String END_WAVEGUIDE_TOTAL_LOSS = "5.634";

    private static final String START_WAVEGUIDE_MODEL2 = "Flex 38";
    private static final String START_WAVEGUIDE_MANUFACTURER2 = "Generic";
    private static final String START_WAVEGUIDE_LENGTH2 = "1";
    private static final String START_WAVEGUIDE_TOTAL_LOSS2 = "2.5";
    private static final String END_WAVEGUIDE_MODEL2 = "Flex 38";
    private static final String END_WAVEGUIDE_MANUFACTURER2 = "Generic";
    private static final String END_WAVEGUIDE_LENGTH2 = "2";
    private static final String END_WAVEGUIDE_TOTAL_LOSS2 = "5";

    private static final String START_DIVERSITY_WAVEGUIDE_MODEL = "Flex 23";
    private static final String START_DIVERSITY_WAVEGUIDE_MANUFACTURER = "Generic";
    private static final String START_DIVERSITY_WAVEGUIDE_LENGTH = "30";
    private static final String START_DIVERSITY_WAVEGUIDE_TOTAL_LOSS = "45";
    private static final String END_DIVERSITY_WAVEGUIDE_MODEL = "Flex 23";
    private static final String END_DIVERSITY_WAVEGUIDE_MANUFACTURER = "Generic";
    private static final String END_DIVERSITY_WAVEGUIDE_LENGTH = "40";
    private static final String END_DIVERSITY_WAVEGUIDE_TOTAL_LOSS = "60";

    private static final String START_DIVERSITY_WAVEGUIDE_MODEL2 = "Flex 38";
    private static final String START_DIVERSITY_WAVEGUIDE_MANUFACTURER2 = "Generic";
    private static final String START_DIVERSITY_WAVEGUIDE_LENGTH2 = "5";
    private static final String START_DIVERSITY_WAVEGUIDE_TOTAL_LOSS2 = "12.5";
    private static final String END_DIVERSITY_WAVEGUIDE_MODEL2 = "Flex 38";
    private static final String END_DIVERSITY_WAVEGUIDE_MANUFACTURER2 = "Generic";
    private static final String END_DIVERSITY_WAVEGUIDE_LENGTH2 = "6";
    private static final String END_DIVERSITY_WAVEGUIDE_TOTAL_LOSS2 = "15";

    private static final String START_ATTENUATOR_MODEL = "20dB 23GHz";
    private static final String START_ATTENUATOR_MANUFACTURER = "Generic";
    private static final String START_ATTENUATOR_MODE = "Rx";
    private static final String START_ATTENUATOR_LOSS = "20";
    private static final String END_ATTENUATOR_MODEL = "20dB 23GHz";
    private static final String END_ATTENUATOR_MANUFACTURER = "Generic";
    private static final String END_ATTENUATOR_MODE = "Common";
    private static final String END_ATTENUATOR_LOSS = "20";

    private static final String START_ATTENUATOR_MODEL2 = "20dB 38GHz";
    private static final String START_ATTENUATOR_MANUFACTURER2 = "Generic";
    private static final String START_ATTENUATOR_MODE2 = "Common";
    private static final String START_ATTENUATOR_LOSS2 = "20";
    private static final String END_ATTENUATOR_MODEL2 = "20dB 38GHz";
    private static final String END_ATTENUATOR_MANUFACTURER2 = "Generic";
    private static final String END_ATTENUATOR_MODE2 = "Tx";
    private static final String END_ATTENUATOR_LOSS2 = "20";

    private static final String USER_LABEL = "userLabel987";
    private static final String TECHNOLOGY_TYPE = "PDH";
    private static final String AGGREGATION_CONFIGURATION = "1+1";
    private static final String NUMBER_OF_WORKING_CHANNELS = "1";
    private static final String NUMBER_OF_PROTECTING_CHANNELS = "1";
    private static final String CAPACITY_VALUE = "234";
    private static final String NETWORK = "Fixed";
    private static final String PATH_LENGTH = "127";
    private static final String MWL_DESCRIPTION = "desc691";

    private static final String MICROWAVE_CHANNEL_CONFIGURATION = "SeleniumAttributesPanelMicrowaveChannel";

    private SoftAssert softAssert;
    private String secondMicrowaveChannel;
    private String firstMicrowaveChannel;

    private static Random rand = new Random();

    @Test(priority = 1, description = "Create Physical Devices")
    @Description("Create All Physical Devices from prerequisites")
    public void createDevices() {
        createIndoorUnit(INDOOR_UNIT_MODEL, FIRST_INDOOR_UNIT_NAME, FIRST_LOCATION_NAME);

        createIndoorUnit(INDOOR_UNIT_MODEL, SECOND_INDOOR_UNIT_NAME, SECOND_LOCATION_NAME);

//        createMicrowaveAntenna(MICROWAVE_ANTENNA_MODEL, FIRST_MICROWAVE_ANTENNA_NAME, FIRST_LOCATION_NAME);
//
//        createMicrowaveAntenna(MICROWAVE_ANTENNA_MODEL, SECOND_MICROWAVE_ANTENNA_NAME, SECOND_LOCATION_NAME);
//
//        createOutdoorUnit(OUTDOOR_UNIT_MODEL, FIRST_OUTDOOR_UNIT_NAME, FIRST_LOCATION_NAME);
//
//        createOutdoorUnit(OUTDOOR_UNIT_MODEL, SECOND_OUTDOOR_UNIT_NAME, SECOND_LOCATION_NAME);
    }

    @Test(priority = 2, description = "Create Microwave Channels with Terminations")
    @Description("Add Indoor Units to View and create Microwave Channel on them")
    public void createMicrowaveChannelWithTerminations() {
        openNetworkView();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        addObjectToView(NAME_COMPONENT_ID, FIRST_INDOOR_UNIT_NAME);
        waitForPageToLoad();
        addObjectToView(NAME_COMPONENT_ID, SECOND_INDOOR_UNIT_NAME);
        waitForPageToLoad();
        networkViewPage.expandViewContentPanel();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, FIRST_INDOOR_UNIT_NAME);
        waitForPageToLoad();

        networkViewPage.openConnectionWizard(MICROWAVE_CHANNEL_TRAIL_TYPE);
        waitForPageToLoad();
        MicrowaveChannelAttributes firstMicrowaveChannelAttributes = getFirstMicrowaveChannelAttributes();
        MicrowaveChannelWizardPage firstMicrowaveChannelWizardPage = new MicrowaveChannelWizardPage(driver);
        fillMicrowaveChannelWizardWithTerminationsStep(firstMicrowaveChannelWizardPage, firstMicrowaveChannelAttributes);

        networkViewPage.expandAttributesPanel();
        // Czekamy na: OSSWEB-18896
        //networkViewPage.applyConfigurationForAttributesPanel(MICROWAVE_CHANNEL_CONFIGURATION);
        assertMicrowaveChannel(networkViewPage, firstMicrowaveChannelAttributes);

        networkViewPage.openTerminationsTab();
        assertPresenceOfObjectInTab(0, CARD_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ID, CARD_NAME);
        assertPresenceOfObjectInTab(1, PORT_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ID, PORT_LABEL);
    }

    @Test(priority = 3, description = "Create Microwave Channel and add Terminations")
    @Description("Create Microwave Channel and add terminations using Terminations Tab")
    public void createMicrowaveChannelAndAddTerminations() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        OldTable table = OldTable.createById(driver, webDriverWait, CONENT_TAB_ID);
        table.searchByAttributeWithLabel(NAME_COLUMN_NAME, TEXT_FIELD, MICROWAVE_CHANNEL_PARTIAL_NAME);
        firstMicrowaveChannel = getMicrowaveChannel(0);
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, firstMicrowaveChannel);
        waitForPageToLoad();

        networkViewPage.openConnectionWizard(MICROWAVE_CHANNEL_TRAIL_TYPE);
        waitForPageToLoad();
        MicrowaveChannelAttributes secondMicrowaveChannelAttributes = getSecondMicrowaveChannelAttributes();
        MicrowaveChannelWizardPage secondMicrowaveChannelWizardPage = new MicrowaveChannelWizardPage(driver);
        fillMicrowaveChannelWizard(secondMicrowaveChannelWizardPage, secondMicrowaveChannelAttributes);
        waitForPageToLoad();
        assertMicrowaveChannel(networkViewPage, secondMicrowaveChannelAttributes);
        TODO:
        //Add Terminations using Terminations Tab

        networkViewPage.startEditingSelectedTrail();
        waitForPageToLoad();
        secondMicrowaveChannel = getMicrowaveChannel(1);
        waitForPageToLoad();
        table.clearColumnValue(NAME_COLUMN_NAME);
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, FIRST_INDOOR_UNIT_NAME);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, SECOND_INDOOR_UNIT_NAME);
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, secondMicrowaveChannel);
        waitForPageToLoad();
        networkViewPage.addSelectedObjectsToTermination();
        waitForPageToLoad();
        fillTerminationWizardPage();

        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, secondMicrowaveChannel);
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, FIRST_INDOOR_UNIT_NAME);
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, SECOND_INDOOR_UNIT_NAME);
        waitForPageToLoad();
        networkViewPage.hideDockedPanel(CONTENT_DOCKED_PANEL_POSITION);
        waitForPageToLoad();
        networkViewPage.openTerminationsTab();
        assertPresenceOfObjectInTab(0, CARD_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ID, CARD_NAME);
        assertPresenceOfObjectInTab(1, PORT_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ID, PORT_LABEL);
    }

    @Test(priority = 4, description = "Create Microwave Link")
    @Description("Create Microwave Link and add Microwave Channel to routing")
    public void createMicrowaveLinkAndAddMicrowaveChannelToRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.hideDockedPanel(DETAILS_DOCKED_PANEL_POSITION);
        waitForPageToLoad();
        networkViewPage.expandViewContentPanel();
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, secondMicrowaveChannel);

        networkViewPage.openConnectionWizard(MICROWAVE_LINK_TRAIL_TYPE);
        waitForPageToLoad();
        MicrowaveLinkAttributes microwaveLinkAttributes = getMicrowaveLinkAttributes();
        MicrowaveLinkWizardPage microwaveLinkWizardPage = new MicrowaveLinkWizardPage(driver);
        fillMicrowaveLinkWizard(microwaveLinkWizardPage, microwaveLinkAttributes);
        waitForPageToLoad();
        networkViewPage.expandAttributesPanel();
        waitForPageToLoad();
        assertMicrowaveLink(networkViewPage, microwaveLinkAttributes);
    }

    private void assertPresenceOfObjectInTab(Integer index, String columnId, String tabId, String objectName) {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        String objectValue = networkViewPage.getObjectValueFromTab(index, columnId, tabId);

        Assert.assertEquals(objectValue, objectName);
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
        waitForPageToLoad();
        ToolsManagerWindow toolsManagerWindow = toolsManagerPage.getToolsManager();
        waitForPageToLoad();
        toolsManagerWindow.openApplication(categoryName, applicationName);
    }

    private void addObjectToView(String componentId, String value) {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        networkViewPage.queryElementAndAddItToView(componentId, value);
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
        clickMessage();
        waitForPageToLoad();
        addCardToPhysicalDevice(CARD_MODEL_NAME, SLOT_NAME);
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

    private void clickMessage() {
        SystemMessageContainer.create(driver, webDriverWait).clickMessageLink();
    }

    private MicrowaveChannelAttributes getFirstMicrowaveChannelAttributes() {
        MicrowaveChannelAttributes attributes = new MicrowaveChannelAttributes();
        attributes.band = BAND;
        attributes.channelBandwidth = CHANNEL_BANDWIDTH;
        attributes.lowFrequency = LOW_FREQUENCY;
        attributes.highFrequency = HIGH_FREQUENCY;
        attributes.polarization = POLARIZATION;
        attributes.workingStatus = WORKING_STATUS;
        attributes.hsbFlag = HSB_FLAG;
        attributes.xpolFlag = XPOL_FLAG;
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
        attributes.dcnLocation = DCN_LOCATION;
        attributes.endAttenuatorManufacturer = END_ATTENUATOR_MANUFACTURER;
        attributes.endAttenuatorModel = END_ATTENUATOR_MODEL;
        attributes.endDiversityWaveguideLength = END_DIVERSITY_WAVEGUIDE_LENGTH;
        attributes.endDiversityWaveguideManufacturer = END_DIVERSITY_WAVEGUIDE_MANUFACTURER;
        attributes.endDiversityWaveguideModel = END_DIVERSITY_WAVEGUIDE_MODEL;
        attributes.startAttenuatorManufacturer = START_ATTENUATOR_MANUFACTURER;
        attributes.startAttenuatorModel = START_ATTENUATOR_MODEL;
        attributes.startDiversityWaveguideLength = START_DIVERSITY_WAVEGUIDE_LENGTH;
        attributes.startDiversityWaveguideManufacturer = START_DIVERSITY_WAVEGUIDE_MANUFACTURER;
        attributes.startDiversityWaveguideModel = START_DIVERSITY_WAVEGUIDE_MODEL;
        attributes.startDiversityWaveguideTotalLoss = START_DIVERSITY_WAVEGUIDE_TOTAL_LOSS;
        attributes.startRadioModel = RADIO_MODEL;
        attributes.startWaveguideManufacturer = START_WAVEGUIDE_MANUFACTURER;
        attributes.startWaveguideModel = START_WAVEGUIDE_MODEL;
        attributes.startWaveguideTotalLoss = START_WAVEGUIDE_TOTAL_LOSS;
        attributes.description = MWC_DESCRIPTION;
        attributes.channelNumber = CHANNEL_NUMBER;
        attributes.configuration = CONFIGURATION;
        attributes.endAttenuatorLoss = END_ATTENUATOR_LOSS;
        attributes.endDiversityWaveguideTotalLoss = END_DIVERSITY_WAVEGUIDE_TOTAL_LOSS;
        attributes.endRadioModel = RADIO_MODEL;
        attributes.endWaveguideLength = END_WAVEGUIDE_LENGTH;
        attributes.endWaveguideManufacturer = END_WAVEGUIDE_MANUFACTURER;
        attributes.endWaveguideModel = END_WAVEGUIDE_MODEL;
        attributes.endWaveguideTotalLoss = END_WAVEGUIDE_TOTAL_LOSS;
        attributes.startAttenuatorLoss = START_ATTENUATOR_LOSS;
        attributes.startWaveguideLength = START_WAVEGUIDE_LENGTH;
        attributes.endAttenuatorMode = END_ATTENUATOR_MODE;
        attributes.highFrequencySite = HIGH_FREQUENCY_SITE;
        attributes.startAttenuatorMode = START_ATTENUATOR_MODE;

        return attributes;
    }

    private MicrowaveChannelAttributes getSecondMicrowaveChannelAttributes() {
        MicrowaveChannelAttributes attributes = new MicrowaveChannelAttributes();
        attributes.band = BAND2;
        attributes.channelBandwidth = CHANNEL_BANDWIDTH2;
        attributes.lowFrequency = LOW_FREQUENCY2;
        attributes.highFrequency = HIGH_FREQUENCY2;
        attributes.polarization = POLARIZATION2;
        attributes.workingStatus = WORKING_STATUS2;
        attributes.hsbFlag = HSB_FLAG2;
        attributes.xpolFlag = XPOL_FLAG2;
        attributes.admFlag = ADM_FLAG2;
        attributes.referenceChannelModulation = REFERENCE_CHANNEL_MODULATION2;
        attributes.highestChannelModulation = HIGHEST_CHANNEL_MODULATION2;
        attributes.lowestChannelModulation = LOWEST_CHANNEL_MODULATION2;
        attributes.startTxPower = START_TX_POWER2;
        attributes.endTxPower = END_TX_POWER2;
        attributes.startRxPower = START_RX_POWER2;
        attributes.endRxPower = END_RX_POWER2;
        attributes.atpc = ATPC2;
        attributes.atpcRxMaxLevel = ATPC_RX_MAX_LEVEL2;
        attributes.atpcRxMinLevel = ATPC_RX_MIN_LEVEL2;
        attributes.dcnLocation = DCN_LOCATION2;
        attributes.endAttenuatorManufacturer = END_ATTENUATOR_MANUFACTURER2;
        attributes.endAttenuatorModel = END_ATTENUATOR_MODEL2;
        attributes.endDiversityWaveguideLength = END_DIVERSITY_WAVEGUIDE_LENGTH2;
        attributes.endDiversityWaveguideManufacturer = END_DIVERSITY_WAVEGUIDE_MANUFACTURER2;
        attributes.endDiversityWaveguideModel = END_DIVERSITY_WAVEGUIDE_MODEL2;
        attributes.startAttenuatorManufacturer = START_ATTENUATOR_MANUFACTURER2;
        attributes.startAttenuatorModel = START_ATTENUATOR_MODEL2;
        attributes.startDiversityWaveguideLength = START_DIVERSITY_WAVEGUIDE_LENGTH2;
        attributes.startDiversityWaveguideManufacturer = START_DIVERSITY_WAVEGUIDE_MANUFACTURER2;
        attributes.startDiversityWaveguideModel = START_DIVERSITY_WAVEGUIDE_MODEL2;
        attributes.startDiversityWaveguideTotalLoss = START_DIVERSITY_WAVEGUIDE_TOTAL_LOSS2;
        attributes.startRadioModel = RADIO_MODEL2;
        attributes.startWaveguideManufacturer = START_WAVEGUIDE_MANUFACTURER2;
        attributes.startWaveguideModel = START_WAVEGUIDE_MODEL2;
        attributes.startWaveguideTotalLoss = START_WAVEGUIDE_TOTAL_LOSS2;
        attributes.description = MWC_DESCRIPTION2;
        attributes.channelNumber = CHANNEL_NUMBER2;
        attributes.configuration = CONFIGURATION2;
        attributes.endAttenuatorLoss = END_ATTENUATOR_LOSS2;
        attributes.endDiversityWaveguideTotalLoss = END_DIVERSITY_WAVEGUIDE_TOTAL_LOSS2;
        attributes.endRadioModel = RADIO_MODEL2;
        attributes.endWaveguideLength = END_WAVEGUIDE_LENGTH2;
        attributes.endWaveguideManufacturer = END_WAVEGUIDE_MANUFACTURER2;
        attributes.endWaveguideModel = END_WAVEGUIDE_MODEL2;
        attributes.endWaveguideTotalLoss = END_WAVEGUIDE_TOTAL_LOSS2;
        attributes.startAttenuatorLoss = START_ATTENUATOR_LOSS2;
        attributes.startWaveguideLength = START_WAVEGUIDE_LENGTH2;
        attributes.endAttenuatorMode = END_ATTENUATOR_MODE2;
        attributes.highFrequencySite = HIGH_FREQUENCY_SITE2;
        attributes.startAttenuatorMode = START_ATTENUATOR_MODE2;

        return attributes;
    }

    private MicrowaveLinkAttributes getMicrowaveLinkAttributes() {
        MicrowaveLinkAttributes attributes = new MicrowaveLinkAttributes();
        attributes.userLabel = USER_LABEL;
        attributes.linkId = UUID.randomUUID().toString();
        attributes.technologyType = TECHNOLOGY_TYPE;
        attributes.aggregationConfiguration = AGGREGATION_CONFIGURATION;
        attributes.numberOfWorkingChannels = NUMBER_OF_WORKING_CHANNELS;
        attributes.numberOfProtectingChannels = NUMBER_OF_PROTECTING_CHANNELS;
        attributes.capacityValue = CAPACITY_VALUE;
        attributes.network = NETWORK;
        attributes.pathLength = PATH_LENGTH;
        attributes.description = MWL_DESCRIPTION;

        return attributes;
    }

    private String getMicrowaveChannel(Integer index) {
        OldTable table = OldTable.createById(driver, webDriverWait, CONENT_TAB_ID);
        return table.getCellValue(index, NAME_ATTRIBUTE_LABEL);
    }

    private static class MicrowaveChannelAttributes {
        private String band;
        private String channelBandwidth;
        private String lowFrequency;
        private String highFrequency;
        private String polarization;
        private String workingStatus;
        private String hsbFlag;
        private String xpolFlag;
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
        private String dcnLocation;
        private String endAttenuatorManufacturer;
        private String endAttenuatorModel;
        private String endDiversityWaveguideLength;
        private String endDiversityWaveguideManufacturer;
        private String endDiversityWaveguideModel;
        private String startAttenuatorManufacturer;
        private String startAttenuatorModel;
        private String startDiversityWaveguideLength;
        private String startDiversityWaveguideManufacturer;
        private String startDiversityWaveguideModel;
        private String startDiversityWaveguideTotalLoss;
        private String startRadioModel;
        private String startWaveguideManufacturer;
        private String startWaveguideModel;
        private String startWaveguideTotalLoss;
        private String description;
        private String channelNumber;
        private String configuration;
        private String endAttenuatorLoss;
        private String endDiversityWaveguideTotalLoss;
        private String endRadioModel;
        private String endWaveguideLength;
        private String endWaveguideManufacturer;
        private String endWaveguideModel;
        private String endWaveguideTotalLoss;
        private String startAttenuatorLoss;
        private String startWaveguideLength;
        private String endAttenuatorMode;
        private String highFrequencySite;
        private String startAttenuatorMode;
    }

    private static class MicrowaveLinkAttributes {
        private String userLabel;
        private String linkId;
        private String technologyType;
        private String aggregationConfiguration;
        private String numberOfWorkingChannels;
        private String numberOfProtectingChannels;
        private String capacityValue;
        private String network;
        private String pathLength;
        private String description;
    }

    private void fillMicrowaveChannelWizard(MicrowaveChannelWizardPage microwaveChannelWizardPage, MicrowaveChannelAttributes microwaveChannelAttributes) {
        fillFirstStepOfMicrowaveChannelWizard(microwaveChannelWizardPage, microwaveChannelAttributes);
        fillSecondStepOfMicrowaveChannelWizard(microwaveChannelWizardPage, microwaveChannelAttributes);
        fillThirdStepOfMicrowaveChannelWizard(microwaveChannelWizardPage, microwaveChannelAttributes);
        fillForthStepOfMicrowaveChannelWizard(microwaveChannelWizardPage, microwaveChannelAttributes);
    }

    private void fillMicrowaveChannelWizardWithTerminationsStep(MicrowaveChannelWizardPage microwaveChannelWizardPage, MicrowaveChannelAttributes microwaveChannelAttributes) {
        fillFirstStepOfMicrowaveChannelWizard(microwaveChannelWizardPage, microwaveChannelAttributes);
        fillSecondStepOfMicrowaveChannelWizard(microwaveChannelWizardPage, microwaveChannelAttributes);
        fillThirdStepOfMicrowaveChannelWizard(microwaveChannelWizardPage, microwaveChannelAttributes);
        fillForthStepOfMicrowaveChannelWizardWithTerminationsStep(microwaveChannelWizardPage, microwaveChannelAttributes);
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
        microwaveChannelWizardPage.clickNext();
    }

    private void fillSecondStepOfMicrowaveChannelWizard(MicrowaveChannelWizardPage microwaveChannelWizardPage, MicrowaveChannelAttributes microwaveChannelAttributes) {
        microwaveChannelWizardPage.setStartRadioModel(microwaveChannelAttributes.startRadioModel);
        microwaveChannelWizardPage.setAdmFlag(microwaveChannelAttributes.admFlag);
        microwaveChannelWizardPage.setReferenceChannelModulation(microwaveChannelAttributes.referenceChannelModulation);
        microwaveChannelWizardPage.setHighestChannelModulation(microwaveChannelAttributes.highestChannelModulation);
        microwaveChannelWizardPage.setLowestChannelModulation(microwaveChannelAttributes.lowestChannelModulation);
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
        microwaveChannelWizardPage.clickAccept();
    }

    private void fillForthStepOfMicrowaveChannelWizardWithTerminationsStep(MicrowaveChannelWizardPage microwaveChannelWizardPage, MicrowaveChannelAttributes microwaveChannelAttributes) {
        microwaveChannelWizardPage.setStartWaveguideModel(microwaveChannelAttributes.startWaveguideModel);
        microwaveChannelWizardPage.setStartWaveguideLength(microwaveChannelAttributes.startWaveguideLength);
        microwaveChannelWizardPage.setEndWaveguideLength(microwaveChannelAttributes.endWaveguideLength);
        microwaveChannelWizardPage.setStartDiversityWaveguideModel(microwaveChannelAttributes.startDiversityWaveguideModel);
        microwaveChannelWizardPage.setStartDiversityWaveguideLength(microwaveChannelAttributes.startDiversityWaveguideLength);
        microwaveChannelWizardPage.setEndDiversityWaveguideLength(microwaveChannelAttributes.endDiversityWaveguideLength);
        microwaveChannelWizardPage.setStartAttenuatorModel(microwaveChannelAttributes.startAttenuatorModel);
        microwaveChannelWizardPage.setStartAttenuatorMode(microwaveChannelAttributes.startAttenuatorMode);
        microwaveChannelWizardPage.setEndAttenuatorMode(microwaveChannelAttributes.endAttenuatorMode);
        microwaveChannelWizardPage.clickNext();
    }

    private void fillFifthStepOfMicrowaveChannelWizard(MicrowaveChannelWizardPage microwaveChannelWizardPage, MicrowaveChannelAttributes microwaveChannelAttributes) {
        microwaveChannelWizardPage.setTermination(START_CARD_FIELD_ID, CARD_NAME);
        microwaveChannelWizardPage.setTermination(END_CARD_FIELD_ID, CARD_NAME);
        microwaveChannelWizardPage.setTermination(END_PORT_FIELD_ID, PORT_NAME);
        microwaveChannelWizardPage.clickAccept();
    }

    private void fillMicrowaveLinkWizard(MicrowaveLinkWizardPage microwaveLinkWizardPage, MicrowaveLinkAttributes microwaveLinkAttributes) {
        microwaveLinkWizardPage.clickNext();
        microwaveLinkWizardPage.setUserLabel(microwaveLinkAttributes.userLabel);
        microwaveLinkWizardPage.setLinkId(microwaveLinkAttributes.linkId);
        microwaveLinkWizardPage.setTechnologyType(microwaveLinkAttributes.technologyType);
        microwaveLinkWizardPage.setAggregationConfiguration(microwaveLinkAttributes.aggregationConfiguration);
        microwaveLinkWizardPage.setNumberOfWorkingChannels(microwaveLinkAttributes.numberOfWorkingChannels);
        microwaveLinkWizardPage.setNumberOfProtectingChannels(microwaveLinkAttributes.numberOfProtectingChannels);
        microwaveLinkWizardPage.setCapacityValue(microwaveLinkAttributes.capacityValue);
        microwaveLinkWizardPage.setNetwork(microwaveLinkAttributes.network);
        microwaveLinkWizardPage.setPathLength(microwaveLinkAttributes.pathLength);
        microwaveLinkWizardPage.setDescription(microwaveLinkAttributes.description);
        microwaveLinkWizardPage.clickAccept();
    }

    private void fillTerminationWizardPage() {
        MicrowaveChannelWizardPage terminationWizardPage = new MicrowaveChannelWizardPage(driver);
        terminationWizardPage.setTermination(START_CARD_FIELD_ID, CARD_NAME);
        waitForPageToLoad();
        terminationWizardPage.setTermination(END_CARD_FIELD_ID, CARD_NAME);
        waitForPageToLoad();
        terminationWizardPage.setTermination(END_PORT_FIELD_ID, PORT_NAME);
        waitForPageToLoad();
        terminationWizardPage.clickAccept();
        waitForPageToLoad();
    }

    private void assertMicrowaveChannel(NetworkViewPage networkViewPage, MicrowaveChannelAttributes microwaveChannelAttributes) {
        String band = networkViewPage.getAttributeValue(BAND_ATTRIBUTE_LABEL);
        String channelBandwidth = networkViewPage.getAttributeValue(CHANNEL_BANDWIDTH_ATTRIBUTE_LABEL);
        String lowFrequency = networkViewPage.getAttributeValue(LOW_FREQUENCY_ATTRIBUTE_LABEL);
        String highFrequency = networkViewPage.getAttributeValue(HIGH_FREQUENCY_ATTRIBUTE_LABEL);
        String polarization = networkViewPage.getAttributeValue(POLARIZATION_ATTRIBUTE_LABEL);
        String workingStatus = networkViewPage.getAttributeValue(WORKING_STATUS_ATTRIBUTE_LABEL);
        String hsbFlag = networkViewPage.getAttributeValue(HSB_FLAG_ATTRIBUTE_LABEL);
        String xpolFlag = networkViewPage.getAttributeValue(XPOL_FLAG_ATTRIBUTE_LABEL);
        String admFlag = networkViewPage.getAttributeValue(ADM_FLAG_ATTRIBUTE_LABEL);
        String referenceChannelModulation = networkViewPage.getAttributeValue(REFERENCE_CHANNEL_MODULATION_ATTRIBUTE_LABEL);
        String lowestChannelModulation = networkViewPage.getAttributeValue(LOWEST_CHANNEL_MODULATION_ATTRIBUTE_LABEL);
        String highestChannelModulation = networkViewPage.getAttributeValue(HIGHEST_CHANNEL_MODULATION_ATTRIBUTE_LABEL);
        String startTxPower = networkViewPage.getAttributeValue(START_TX_POWER_ATTRIBUTE_LABEL);
        String endTxPower = networkViewPage.getAttributeValue(END_TX_POWER_ATTRIBUTE_LABEL);
        String startRxPower = networkViewPage.getAttributeValue(START_RX_POWER_ATTRIBUTE_LABEL);
        String endRxPower = networkViewPage.getAttributeValue(END_RX_POWER_ATTRIBUTE_LABEL);
        String atpc = networkViewPage.getAttributeValue(ATPC_ATTRIBUTE_LABEL);
        String atpcRxMinLevel = networkViewPage.getAttributeValue(ATPC_RX_MIN_LEVEL_ATTRIBUTE_LABEL);
        String atpcRxMaxLevel = networkViewPage.getAttributeValue(ATPC_RX_MAX_LEVEL_ATTRIBUTE_LABEL);
        String dcnLocation = networkViewPage.getAttributeValue(DCN_LOCATION_ATTRIBUTE_LABEL);
        String endAttenuatorManufacturer = networkViewPage.getAttributeValue(END_ATTENUATOR_MANUFACTURER_ATTRIBUTE_LABEL);
        String endAttenuatorModel = networkViewPage.getAttributeValue(END_ATTENUATOR_MODEL_ATTRIBUTE_LABEL);
        String endDiversityWaveguideLength = networkViewPage.getAttributeValue(END_DIVERSITY_WAVEGUIDE_LENGTH_ATTRIBUTE_LABEL);
        String endDiversityWaveguideManufacturer = networkViewPage.getAttributeValue(END_DIVERSITY_WAVEGUIDE_MANUFACTURER_ATTRIBUTE_LABEL);
        String endDiversityWaveguideModel = networkViewPage.getAttributeValue(END_DIVERSITY_WAVEGUIDE_MODEL_ATTRIBUTE_LABEL);
        String startAttenuatorManufacturer = networkViewPage.getAttributeValue(START_ATTENUATOR_MANUFACTURER_ATTRIBUTE_LABEL);
        String startAttenuatorModel = networkViewPage.getAttributeValue(START_ATTENUATOR_MODEL_ATTRIBUTE_LABEL);
        String startDiversityWaveguideLength = networkViewPage.getAttributeValue(START_DIVERSITY_WAVEGUIDE_LENGTH_ATTRIBUTE_LABEL);
        String startDiversityWaveguideManufacturer = networkViewPage.getAttributeValue(START_DIVERSITY_WAVEGUIDE_MANUFACTURER_ATTRIBUTE_LABEL);
        String startDiversityWaveguideModel = networkViewPage.getAttributeValue(START_DIVERSITY_WAVEGUIDE_MODEL_ATTRIBUTE_LABEL);
        String startDiversityWaveguideTotalLoss = networkViewPage.getAttributeValue(START_DIVERSITY_WAVEGUIDE_TOTAL_LOSS_ATTRIBUTE_LABEL);
        String startRadioModel = networkViewPage.getAttributeValue(START_RADIO_MODEL_ATTRIBUTE_LABEL);
        String startWaveguideManufacturer = networkViewPage.getAttributeValue(START_WAVEGUIDE_MANUFACTURER_ATTRIBUTE_LABEL);
        String startWaveguideModel = networkViewPage.getAttributeValue(START_WAVEGUIDE_MODEL_ATTRIBUTE_LABEL);
        String startWaveguideTotalLoss = networkViewPage.getAttributeValue(START_WAVEGUIDE_TOTAL_LOSS_ATTRIBUTE_LABEL);
        String description = networkViewPage.getAttributeValue(DESCRIPTION_ATTRIBUTE_LABEL);
        String channelNumber = networkViewPage.getAttributeValue(CHANNEL_NUMBER_ATTRIBUTE_LABEL);
        String configuration = networkViewPage.getAttributeValue(CONFIGURATION_ATTRIBUTE_LABEL);
        String endAttenuatorLoss = networkViewPage.getAttributeValue(END_ATTENUATOR_LOSS_ATTRIBUTE_LABEL);
        String endDiversityWaveguideTotalLoss = networkViewPage.getAttributeValue(END_DIVERSITY_WAVEGUIDE_TOTAL_LOSS_ATTRIBUTE_LABEL);
        String endRadioModel = networkViewPage.getAttributeValue(END_RADIO_MODEL_ATTRIBUTE_LABEL);
        String endWaveguideLength = networkViewPage.getAttributeValue(END_WAVEGUIDE_LENGTH_ATTRIBUTE_LABEL);
        String endWaveguideManufacturer = networkViewPage.getAttributeValue(END_WAVEGUIDE_MANUFACTURER_ATTRIBUTE_LABEL);
        String endWaveguideModel = networkViewPage.getAttributeValue(END_WAVEGUIDE_MODEL_ATTRIBUTE_LABEL);
        String endWaveguideTotalLoss = networkViewPage.getAttributeValue(END_WAVEGUIDE_TOTAL_LOSS_ATTRIBUTE_LABEL);
        String startAttenuatorLoss = networkViewPage.getAttributeValue(START_ATTENUATOR_LOSS_ATTRIBUTE_LABEL);
        String startWaveguideLength = networkViewPage.getAttributeValue(START_WAVEGUIDE_LENGTH_ATTRIBUTE_LABEL);
        String endAttenuatorMode = networkViewPage.getAttributeValue(END_ATTENUATOR_MODE_ATTRIBUTE_LABEL);
        String highFrequencySite = networkViewPage.getAttributeValue(HIGH_FREQUENCY_SITE_ATTRIBUTE_LABEL);
        String startAttenuatorMode = networkViewPage.getAttributeValue(START_ATTENUATOR_MODE_ATTRIBUTE_LABEL);

        Assert.assertEquals(band, microwaveChannelAttributes.band);
        Assert.assertEquals(channelBandwidth, microwaveChannelAttributes.channelBandwidth);
        Assert.assertEquals(lowFrequency, microwaveChannelAttributes.lowFrequency);
        Assert.assertEquals(highFrequency, microwaveChannelAttributes.highFrequency);
        Assert.assertEquals(polarization, microwaveChannelAttributes.polarization);
        Assert.assertEquals(workingStatus, microwaveChannelAttributes.workingStatus);
        Assert.assertEquals(hsbFlag, microwaveChannelAttributes.hsbFlag);
        Assert.assertEquals(xpolFlag, microwaveChannelAttributes.xpolFlag);
        Assert.assertEquals(admFlag, microwaveChannelAttributes.admFlag);
        Assert.assertEquals(referenceChannelModulation, microwaveChannelAttributes.referenceChannelModulation);
        Assert.assertEquals(lowestChannelModulation, microwaveChannelAttributes.lowestChannelModulation);
        Assert.assertEquals(highestChannelModulation, microwaveChannelAttributes.highestChannelModulation);
        Assert.assertEquals(startTxPower, microwaveChannelAttributes.startTxPower);
        Assert.assertEquals(endTxPower, microwaveChannelAttributes.endTxPower);
        Assert.assertEquals(startRxPower, microwaveChannelAttributes.startRxPower);
        Assert.assertEquals(endRxPower, microwaveChannelAttributes.endRxPower);
        Assert.assertEquals(atpc, microwaveChannelAttributes.atpc);
        Assert.assertEquals(atpcRxMinLevel, microwaveChannelAttributes.atpcRxMinLevel);
        Assert.assertEquals(atpcRxMaxLevel, microwaveChannelAttributes.atpcRxMaxLevel);
        Assert.assertEquals(dcnLocation, microwaveChannelAttributes.dcnLocation);
        Assert.assertEquals(endAttenuatorManufacturer, microwaveChannelAttributes.endAttenuatorManufacturer);
        Assert.assertEquals(endAttenuatorModel, microwaveChannelAttributes.endAttenuatorModel);
        Assert.assertEquals(endDiversityWaveguideLength, microwaveChannelAttributes.endDiversityWaveguideLength);
        Assert.assertEquals(endDiversityWaveguideManufacturer, microwaveChannelAttributes.endDiversityWaveguideManufacturer);
        Assert.assertEquals(endDiversityWaveguideModel, microwaveChannelAttributes.endDiversityWaveguideModel);
        Assert.assertEquals(startAttenuatorManufacturer, microwaveChannelAttributes.startAttenuatorManufacturer);
        Assert.assertEquals(startAttenuatorModel, microwaveChannelAttributes.startAttenuatorModel);
        Assert.assertEquals(startDiversityWaveguideLength, microwaveChannelAttributes.startDiversityWaveguideLength);
        Assert.assertEquals(startDiversityWaveguideManufacturer, microwaveChannelAttributes.startDiversityWaveguideManufacturer);
        Assert.assertEquals(startDiversityWaveguideModel, microwaveChannelAttributes.startDiversityWaveguideModel);
        Assert.assertEquals(startDiversityWaveguideTotalLoss, microwaveChannelAttributes.startDiversityWaveguideTotalLoss);
        Assert.assertEquals(startRadioModel, microwaveChannelAttributes.startRadioModel);
        Assert.assertEquals(startWaveguideManufacturer, microwaveChannelAttributes.startWaveguideManufacturer);
        Assert.assertEquals(startWaveguideModel, microwaveChannelAttributes.startWaveguideModel);
        Assert.assertEquals(startWaveguideTotalLoss, microwaveChannelAttributes.startWaveguideTotalLoss);
        Assert.assertEquals(description, microwaveChannelAttributes.description);
        Assert.assertEquals(channelNumber, microwaveChannelAttributes.channelNumber);
        Assert.assertEquals(configuration, microwaveChannelAttributes.configuration);
        Assert.assertEquals(endAttenuatorLoss, microwaveChannelAttributes.endAttenuatorLoss);
        Assert.assertEquals(endDiversityWaveguideTotalLoss, microwaveChannelAttributes.endDiversityWaveguideTotalLoss);
        Assert.assertEquals(endRadioModel, microwaveChannelAttributes.endRadioModel);
        Assert.assertEquals(endWaveguideLength, microwaveChannelAttributes.endWaveguideLength);
        Assert.assertEquals(endWaveguideManufacturer, microwaveChannelAttributes.endWaveguideManufacturer);
        Assert.assertEquals(endWaveguideModel, microwaveChannelAttributes.endWaveguideModel);
        Assert.assertEquals(endWaveguideTotalLoss, microwaveChannelAttributes.endWaveguideTotalLoss);
        Assert.assertEquals(startAttenuatorLoss, microwaveChannelAttributes.startAttenuatorLoss);
        Assert.assertEquals(startWaveguideLength, microwaveChannelAttributes.startWaveguideLength);
        Assert.assertEquals(endAttenuatorMode, microwaveChannelAttributes.endAttenuatorMode);
        Assert.assertEquals(highFrequencySite, microwaveChannelAttributes.highFrequencySite);
        Assert.assertEquals(startAttenuatorMode, microwaveChannelAttributes.startAttenuatorMode);
    }

    private void assertMicrowaveLink(NetworkViewPage networkViewPage, MicrowaveLinkAttributes microwaveLinkAttributes
    ) {
        String capacityValue = networkViewPage.getAttributeValue(CAPACITY_VALUE_ATTRIBUTE_LABEL);
        String pathLength = networkViewPage.getAttributeValue(PATH_LENGTH_ATTRIBUTE_LABEL);
        String network = networkViewPage.getAttributeValue(NETWORK_ATTRIBUTE_LABEL);
        String technologyType = networkViewPage.getAttributeValue(TECHNOLOGY_TYPE_ATTRIBUTE_LABEL);
        String aggregationConfiguration = networkViewPage.getAttributeValue(AGGREGATION_CONFIGURATION_ATTRIBUTE_LABEL);
        String userLabel = networkViewPage.getAttributeValue(USER_LABEL_ATTRIBUTE_LABEL);
        String linkId = networkViewPage.getAttributeValue(LINK_ID_ATTRIBUTE_LABEL);
        String numberOfWorkingChannels = networkViewPage.getAttributeValue(NUMBER_OF_WORKING_CHANNELS_ATTRIBUTE_LABEL);
        String numberOfProtectingChannels = networkViewPage.getAttributeValue(NUMBER_OF_PROTECTING_CHANNELS_ATTRIBUTE_LABEL);
        String description = networkViewPage.getAttributeValue(DESCRIPTION_ATTRIBUTE_LABEL);

        Assert.assertEquals(capacityValue, microwaveLinkAttributes.capacityValue);
        Assert.assertEquals(pathLength, microwaveLinkAttributes.pathLength);
        Assert.assertEquals(network, microwaveLinkAttributes.network);
        Assert.assertEquals(technologyType, microwaveLinkAttributes.technologyType);
        Assert.assertEquals(aggregationConfiguration, microwaveLinkAttributes.aggregationConfiguration);
        Assert.assertEquals(userLabel, microwaveLinkAttributes.userLabel);
        Assert.assertEquals(linkId, microwaveLinkAttributes.linkId);
        Assert.assertEquals(numberOfWorkingChannels, microwaveLinkAttributes.numberOfWorkingChannels);
        Assert.assertEquals(numberOfProtectingChannels, microwaveLinkAttributes.numberOfProtectingChannels);
        Assert.assertEquals(description, microwaveLinkAttributes.description);
    }

    private void addCardToPhysicalDevice(String modelName, String slotName) {
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
        waitForPageToLoad();
        hierarchyViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_CARD_BUTTON_ID);
        CardCreateWizardPage createWizardPage = new CardCreateWizardPage(driver);
        waitForPageToLoad();
        createWizardPage.setModel(modelName);
        waitForPageToLoad();
        createWizardPage.setSlot(slotName);
        waitForPageToLoad();
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
