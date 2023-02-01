package com.oss.E2E;

import java.util.Random;
import java.util.UUID;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.comarch.oss.web.pages.HierarchyViewPage;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.comarch.oss.web.pages.SearchObjectTypePage;
import com.comarch.oss.web.pages.toolsmanager.ToolsManagerPage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.navigation.toolsmanager.ToolsManagerWindow;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.physical.CardCreateWizardPage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.trail.ConnectionWizardPage;
import com.oss.pages.transport.trail.EthernetLinkWizardPage;
import com.oss.pages.transport.trail.RoutingOverElementsWizardPage;
import com.oss.pages.transport.trail.RoutingWizardPage;
import com.oss.pages.transport.trail.v2.MicrowaveChannelWizardPage;
import com.oss.pages.transport.trail.v2.MicrowaveLinkWizardPage;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.untils.Environment;

import io.qameta.allure.Description;

import static com.oss.framework.components.contextactions.ActionsContainer.EDIT_GROUP_ID;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class BucOssTpt001Test extends BaseTestCase {

    private static final Random rand = new Random();
    private final Environment env = Environment.getInstance();

    private String secondMicrowaveChannel;
    private String secondMicrowaveChannelLabel;
    private String firstMicrowaveChannel;
    private String firstMicrowaveChannelLabel;
    private String microwaveLink;
    private String microwaveLinkLabel;

    private static final String SITE = "Site";
    private static final String COUNTRY_NAME = "Polska";
    private static final String REGION_NAME_1 = "Woj. Pomorskie";
    private static final String REGION_NAME_2 = "Woj. Lubelskie";
    private static final String DISTRICT_NAME_1 = "District 1";
    private static final String DISTRICT_NAME_2 = "District 2";
    private static final String CITY_NAME_1 = "Bobowo";
    private static final String CITY_NAME_2 = "Frampol";
    private static final String POSTAL_CODE_NAME_1 = "80011";
    private static final String POSTAL_CODE_NAME_2 = "80012";

    private static final String NAME_COMPONENT_ID = "name";
    private static final String NAME_COLUMN_NAME = "Name";
    private static final String MICROWAVE_CHANNEL_TRAIL_TYPE = "Microwave Channel";
    private static final String MICROWAVE_ANTENNA_OBJECT_TYPE = "Microwave Antenna";
    private static final String OUTDOOR_UNIT_OBJECT_TYPE = "Outdoor Unit";
    private static final String INDOOR_UNIT_OBJECT_TYPE = "Indoor Unit";
    private static final String ETHERNET_LINK_TRAIL_TYPE = "Ethernet Link";
    private static final String MICROWAVE_CHANNEL_PARTIAL_NAME = "MicrowaveChannel";
    private static final String MICROWAVE_LINK_TRAIL_TYPE = "Microwave Link";
    private static final String MICROWAVE_LINK_PARTIAL_NAME = "MicrowaveLink";
    private static final String ETHERNET_LINK_PARTIAL_NAME = "EthLinkE2ESelenium";
    private static final String INFRASTRUCTURE_MANAGEMENT_CATEGORY_NAME = "Infrastructure Management";
    private static final String CREATE_DEVICE_APPLICATION_NAME = "Create Physical Device";
    private static final String RESOURCE_INVENTORY_CATEGORY_NAME = "Resource Inventory";
    private static final String NETWORK_VIEW_APPLICATION_NAME = "Network View";
    private static final String INVENTORY_VIEW_APPLICATION_NAME = "Inventory View";
    private static final String CREATE_CARD_BUTTON_ID = "CreateCardOnDeviceAction";
    private static final String CONTENT_TAB_ID = "objectsApp";
    private static final String ROUTING_ELEMENTS_TAB_ID = "routing-elements-table-app";
    private static final String CARD_SHORT_IDENTIFIER_COLUMN = "card.shortIdentifier";
    private static final String PORT_SHORT_IDENTIFIER_COLUMN = "port.shortIdentifier";
    private static final String CONNECTION_LABEL_COLUMN = "trail.identifier";
    private static final String TERMINATIONS_TAB_ID = "TerminationIndexedWidget";
    private static final String TERMINATIONS_TAB_ON_NEW_IV_ID = "TerminationWidget";
    private static final String OCCUPATION_TAB_ID = "OccupationWidget";
    private static final String ROUTED_TRAILS_TAB_ID = "RoutedTrailsWidget";
    private static final String FIRST_LEVEL_ROUTING_TAB_ID = "RoutingSegmentIndexedWidget";
    private static final String DOCKED_PANEL_POSITION = "left";
    private static final String CONTENT_DOCKED_PANEL_POSITION = "left";
    private static final String DETAILS_DOCKED_PANEL_POSITION = "bottom";

    private static final String LABEL_ATTRIBUTE_LABEL = "label";
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
    private static final String CHANNEL_NAME_ATTRIBUTE_LABEL = "ChannelName";
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

    private static final String FIRST_LOCATION_NAME = "1SeleniumE2ETestLocation";
    private static final String SECOND_LOCATION_NAME = "2SeleniumE2ETestLocation";
    private static final String INDOOR_UNIT_MODEL = "Ericsson Mini-Link TN AMM 6p D";
    private static final String FIRST_OUTDOOR_UNIT_MODEL = "Ericsson RAU2 N 23";
    private static final String SECOND_OUTDOOR_UNIT_MODEL = "Ericsson RAU2 N 38";
    private static final String MICROWAVE_ANTENNA_MODEL = "VHLP200-220";
    private static final String FIRST_INDOOR_UNIT_NAME = "1SeleniumE2ETestIDU";
    private static final String SECOND_INDOOR_UNIT_NAME = "2SeleniumE2ETestIDU";
    private static final String FIRST_MICROWAVE_ANTENNA_NAME = "1SeleniumE2ETestMWANT";
    private static final String SECOND_MICROWAVE_ANTENNA_NAME = "2SeleniumE2ETestMWANT";
    private static final String FIRST_OUTDOOR_UNIT_NAME = "1SeleniumE2ETestODU";
    private static final String SECOND_OUTDOOR_UNIT_NAME = "2SeleniumE2ETestODU";
    private static final String THIRD_OUTDOOR_UNIT_NAME = "3SeleniumE2ETestODU";
    private static final String FORTH_OUTDOOR_UNIT_NAME = "4SeleniumE2ETestODU";

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
    private static final String MICROWAVE_FREQUENCY_PLAN_NAME = "23_28_A001";
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
    private static final String MICROWAVE_FREQUENCY_PLAN2_NAME = "001";
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
    private static final String TECHNOLOGY_TYPE = "ETH";
    private static final String AGGREGATION_CONFIGURATION = "1+1";
    private static final String NUMBER_OF_WORKING_CHANNELS = "1";
    private static final String NUMBER_OF_PROTECTING_CHANNELS = "1";
    private static final String FIRST_MWC_CAPACITY_VALUE = "234";
    private static final String FIRST_AND_SECOND_MWC_CAPACITY_VALUE = "80000234";
    private static final String NETWORK = "Fixed";
    private static final String PATH_LENGTH = "127";
    private static final String MWL_DESCRIPTION = "desc691";

    private static final String WORKING_LINE_TYPE = "Working";
    private static final String FIRST_MWL_SEQUENCE_NUMBER = "seqNumberMwl1";
    private static final String SECOND_MWL_SEQUENCE_NUMBER = "2MWLSequenceNum";
    private static final String FIRST_MW_ANTENNA_SEQUENCE_NUMBER = "1MicroAntSequenceNum";
    private static final String FIRST_MW_ANTENNA_RELATION_TYPE = "Start Primary Antenna";
    private static final String SECOND_MW_ANTENNA_SEQUENCE_NUMBER = "2MwAntennaSeqNumber";
    private static final String SECOND_MW_ANTENNA_RELATION_TYPE = "End Primary Antenna";
    private static final String FIRST_ODU_SEQUENCE_NUMBER = "oduSeqNum1";
    private static final String FIRST_ODU_RELATION_TYPE = "Start Site ODU";
    private static final String SECOND_ODU_SEQUENCE_NUMBER = "OutdoorUnitSequenceNumber2";
    private static final String SECOND_ODU_RELATION_TYPE = "End Site ODU";
    private static final String THIRD_ODU_SEQUENCE_NUMBER = "oduSeqNum3";
    private static final String THIRD_ODU_RELATION_TYPE = "Start Site ODU";
    private static final String FORTH_ODU_SEQUENCE_NUMBER = "OutdoorUnitSequenceNumber4";
    private static final String FORTH_ODU_RELATION_TYPE = "End Site ODU";
    private static final String ETHERNET_LINK_NAME = "EthLinkE2ESelenium" + rand.nextInt(10000);

    //    TODO: Odkomentować po rozwiązaniu: OSSWEB-18896
//    private static final String MICROWAVE_CHANNEL_CONFIGURATION = "SeleniumAttributesPanelMicrowaveChannel";
    private static final String ELEMENT_ROUTING_TAB_ID = "routing-elements-table-app";
    private static final String NAME_COLUMN_LABEL = "Name";
    private static final String RELATION_TYPE_COLUMN_LABEL = "Relation type";

    private static final String TERMINATIONS_TAB_LABEL = "Terminations";
    private static final String OCCUPATION_TAB_LABEL = "Occupation";
    private static final String ROUTED_TRAILS_TAB_LABEL = "Routed Trails";
    private static final String ELEMENT_ROUTING_TAB_LABEL = "Element Routing";
    private static final String MICROWAVE_FREQUENCY_PLAN_LABEL = "Microwave Frequency Plan";
    private static final String DELETE_CONNECTION_ACTION_ID = "DeleteTrailWizardActionId";
    private static final String DELETE_SUBMIT_BUTTON_ID = "wizard-submit-button-deleteWidgetId";
    private static final String DELETE_DEVICE_ACTION_ID = "DeleteDeviceWizardAction";
    private static final String DELETE_DEVICE_CONFIRMATION_ID = "ConfirmationBox_object_delete_wizard_confirmation_box_action_button";
    private static final String DELETE_MW_CONNECTION_CONFIRMATION_ID = "ConfirmationBox_DeletePopupConfirmationBox_action_button";
    private static final String SPEED_VALUE = "Unknown";
    private static final String ACTION_CONTAINER_ID = "logicalview-windowToolbar";
    private static final String ROUTING_ACTION_ID = "EDIT_Add to Routing-null";
    private static final String CONFIRMATION_WIZARD_ID = "deleteWidgetId";
    private static final String EXPECTED_MWL_CAPACITY_VALUE = "234000000";

//    @BeforeClass
//    public void checkPrerequisites() {
////        getOrCreateFirstLocations();
////        getOrCreateSecondLocations();
//        getMicrowaveFrequencyPlans();
//    }

    @Test(priority = 1, description = "Create Physical Devices")
    @Description("Create All Physical Devices from prerequisites")
    public void createDevices() {
//        createIndoorUnit(FIRST_INDOOR_UNIT_NAME, FIRST_LOCATION_NAME);
//
//        createIndoorUnit(SECOND_INDOOR_UNIT_NAME, SECOND_LOCATION_NAME);
//
//        createMicrowaveAntenna(FIRST_MICROWAVE_ANTENNA_NAME, FIRST_LOCATION_NAME);
//
//        createMicrowaveAntenna(SECOND_MICROWAVE_ANTENNA_NAME, SECOND_LOCATION_NAME);
//
//        createOutdoorUnit(FIRST_OUTDOOR_UNIT_MODEL, FIRST_OUTDOOR_UNIT_NAME, FIRST_LOCATION_NAME);
//
//        createOutdoorUnit(FIRST_OUTDOOR_UNIT_MODEL, SECOND_OUTDOOR_UNIT_NAME, SECOND_LOCATION_NAME);
//
//        createOutdoorUnit(SECOND_OUTDOOR_UNIT_MODEL, THIRD_OUTDOOR_UNIT_NAME, FIRST_LOCATION_NAME);
//
//        createOutdoorUnit(SECOND_OUTDOOR_UNIT_MODEL, FORTH_OUTDOOR_UNIT_NAME, SECOND_LOCATION_NAME);
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Create Microwave Channels with Terminations", dependsOnMethods = {"createDevices"})
    @Description("Add Indoor Units to View and create Microwave Channel on them")
    public void createMicrowaveChannelWithTerminations() {
        openNetworkView();
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        addObjectToView(FIRST_INDOOR_UNIT_NAME);
        waitForPageToLoad();
        addObjectToView(SECOND_INDOOR_UNIT_NAME);
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
        // TODO: Odkomentować po rozwiązaniu: OSSWEB-18896
        //networkViewPage.applyConfigurationForAttributesPanel(MICROWAVE_CHANNEL_CONFIGURATION);
        waitForPageToLoad();
        assertMicrowaveChannel(networkViewPage, firstMicrowaveChannelAttributes);

        networkViewPage.openTerminationsTab();
        assertPresenceOfObjectInTab(0, PORT_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ID, PORT_LABEL);
        assertPresenceOfObjectInTab(1, PORT_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ID, PORT_LABEL);
    }

    @Test(priority = 3, description = "Create Microwave Channel and add Terminations", dependsOnMethods = {"createMicrowaveChannelWithTerminations"})
    @Description("Create Microwave Channel and add terminations using Terminations Tab")
    public void createMicrowaveChannelAndAddTerminations() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        OldTable table = OldTable.createById(driver, webDriverWait, CONTENT_TAB_ID);
        table.searchByAttributeWithLabel(NAME_COLUMN_NAME, TEXT_FIELD, MICROWAVE_CHANNEL_PARTIAL_NAME);
        firstMicrowaveChannel = getMicrowaveChannel(0);
        waitForPageToLoad();
        firstMicrowaveChannelLabel = getConnectionLabel();
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, firstMicrowaveChannel);
        waitForPageToLoad();
        table.clearColumnValue(NAME_COLUMN_NAME);
        waitForPageToLoad();

        networkViewPage.openConnectionWizard(MICROWAVE_CHANNEL_TRAIL_TYPE);
        waitForPageToLoad();
        MicrowaveChannelAttributes secondMicrowaveChannelAttributes = getSecondMicrowaveChannelAttributes();
        MicrowaveChannelWizardPage secondMicrowaveChannelWizardPage = new MicrowaveChannelWizardPage(driver);
        fillMicrowaveChannelWizard(secondMicrowaveChannelWizardPage, secondMicrowaveChannelAttributes);
        waitForPageToLoad();
        assertMicrowaveChannel(networkViewPage, secondMicrowaveChannelAttributes);

        networkViewPage.startEditingSelectedTrail();
        waitForPageToLoad();
        table.searchByAttributeWithLabel(NAME_COLUMN_NAME, TEXT_FIELD, MICROWAVE_CHANNEL_PARTIAL_NAME);
        secondMicrowaveChannel = getMicrowaveChannel(1);
        waitForPageToLoad();
        secondMicrowaveChannelLabel = getConnectionLabel();
        waitForPageToLoad();
        table.clearColumnValue(NAME_COLUMN_NAME);
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, secondMicrowaveChannel);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, FIRST_INDOOR_UNIT_NAME);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, SECOND_INDOOR_UNIT_NAME);
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

    @Test(priority = 4, description = "Create Microwave Link", dependsOnMethods = {"createMicrowaveChannelAndAddTerminations"})
    @Description("Create Microwave Link and add Microwave Channel to routing")
    public void createMicrowaveLink() {
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

    @Test(priority = 5, description = "Add Microwave Channel to Microwave Link routing", dependsOnMethods = {"createMicrowaveLink"})
    @Description("Add Microwave Channel to Microwave Link routing")
    public void addMicrowaveChannelToMicrowaveLinkRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.stopEditingTrail();
        waitForPageToLoad();
        networkViewPage.startEditingSelectedTrail();
        waitForPageToLoad();
        networkViewPage.expandDockedPanel(DOCKED_PANEL_POSITION);
        OldTable table = OldTable.createById(driver, webDriverWait, CONTENT_TAB_ID);
        table.searchByAttributeWithLabel(NAME_COLUMN_NAME, TEXT_FIELD, MICROWAVE_LINK_PARTIAL_NAME);
        microwaveLink = getConnectionName();
        waitForPageToLoad();
        microwaveLinkLabel = getConnectionLabel();
        waitForPageToLoad();
        table.clearColumnValue(NAME_COLUMN_NAME);
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, microwaveLink);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, firstMicrowaveChannel);
        waitForPageToLoad();

        networkViewPage.addSelectedObjectsToRouting();
        fillAddRoutingSegmentWizard(WORKING_LINE_TYPE, FIRST_MWL_SEQUENCE_NUMBER);
        assertMicrowaveChannelWorkingStatus(networkViewPage);

        table.searchByAttributeWithLabel(NAME_COLUMN_NAME, TEXT_FIELD, MICROWAVE_CHANNEL_PARTIAL_NAME);
        waitForPageToLoad();
        firstMicrowaveChannel = getMicrowaveChannel(0);
        waitForPageToLoad();
        table.clearColumnValue(NAME_COLUMN_NAME);
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, firstMicrowaveChannel);
        waitForPageToLoad();

        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, microwaveLink);
        waitForPageToLoad();
        assertMicrowaveLinkCapacityValue(networkViewPage, FIRST_MWC_CAPACITY_VALUE);
        networkViewPage.hideDockedPanel(DOCKED_PANEL_POSITION);
        waitForPageToLoad();
        networkViewPage.expandDockedPanel(DETAILS_DOCKED_PANEL_POSITION);

        networkViewPage.openTerminationsTab();
        assertPresenceOfObjectInTab(0, PORT_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ID, PORT_LABEL);
        assertPresenceOfObjectInTab(1, PORT_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ID, PORT_LABEL);

        networkViewPage.openFirstLevelRoutingTab();
        assertPresenceOfObjectInTab(0, CONNECTION_LABEL_COLUMN, FIRST_LEVEL_ROUTING_TAB_ID, firstMicrowaveChannelLabel);
    }

    @Test(priority = 6, description = "Add second Microwave Channel to Microwave Link routing", dependsOnMethods = {"addMicrowaveChannelToMicrowaveLinkRouting"})
    @Description("Add second Microwave Channel to Microwave Link routing")
    public void addSecondMicrowaveChannelToMicrowaveLinkRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandDockedPanel(DOCKED_PANEL_POSITION);
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, microwaveLink);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, secondMicrowaveChannel);
        waitForPageToLoad();

        networkViewPage.addSelectedObjectsToRouting();
        fillAddRoutingSegmentWizard(WORKING_LINE_TYPE, SECOND_MWL_SEQUENCE_NUMBER);
        assertMicrowaveChannelWorkingStatus(networkViewPage);

        OldTable table = OldTable.createById(driver, webDriverWait, CONTENT_TAB_ID);
        table.searchByAttributeWithLabel(NAME_COLUMN_NAME, TEXT_FIELD, MICROWAVE_CHANNEL_PARTIAL_NAME);
        waitForPageToLoad();
        secondMicrowaveChannel = getMicrowaveChannel(1);
        waitForPageToLoad();
        table.clearColumnValue(NAME_COLUMN_NAME);
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, secondMicrowaveChannel);
        waitForPageToLoad();

        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, microwaveLink);
        waitForPageToLoad();
        assertMicrowaveLinkCapacityValue(networkViewPage, FIRST_AND_SECOND_MWC_CAPACITY_VALUE);
        networkViewPage.hideDockedPanel(DOCKED_PANEL_POSITION);
        waitForPageToLoad();

        networkViewPage.openTerminationsTab();
        assertPresenceOfObjectInTab(0, CARD_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ID, CARD_NAME);
        assertPresenceOfObjectInTab(1, CARD_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ID, CARD_NAME);

        networkViewPage.openFirstLevelRoutingTab();
        assertPresenceOfObjectInTab(1, CONNECTION_LABEL_COLUMN, FIRST_LEVEL_ROUTING_TAB_ID, secondMicrowaveChannelLabel);
    }

    @Test(priority = 7, description = "Add two Microwave Antennas to Microwave Link element routing", dependsOnMethods = {"addSecondMicrowaveChannelToMicrowaveLinkRouting"})
    @Description("Add two Microwave Antennas to Microwave Link element routing")
    public void addTwoMicrowaveAntennasToMicrowaveLinkElementRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);

        addObjectToView(FIRST_MICROWAVE_ANTENNA_NAME);
        waitForPageToLoad();
        networkViewPage.addSelectedObjectsToElementRouting();
        fillRoutingOverElementsWizard(FIRST_MW_ANTENNA_SEQUENCE_NUMBER, FIRST_MW_ANTENNA_RELATION_TYPE, 0);
        waitForPageToLoad();
        clickAcceptInElementRoutingWizard();

        addObjectToView(SECOND_MICROWAVE_ANTENNA_NAME);
        waitForPageToLoad();
        networkViewPage.addSelectedObjectsToElementRouting();
        fillRoutingOverElementsWizard(SECOND_MW_ANTENNA_SEQUENCE_NUMBER, SECOND_MW_ANTENNA_RELATION_TYPE, 0);
        waitForPageToLoad();
        clickAcceptInElementRoutingWizard();

        networkViewPage.expandViewContentPanel();
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, SECOND_MICROWAVE_ANTENNA_NAME);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, microwaveLink);
        waitForPageToLoad();
        networkViewPage.stopEditingTrail();
        waitForPageToLoad();
        networkViewPage.hideDockedPanel(DOCKED_PANEL_POSITION);
        waitForPageToLoad();
        networkViewPage.openElementRoutingTab();
        waitForPageToLoad();

        OldTable table = OldTable.createById(driver, webDriverWait, ROUTING_ELEMENTS_TAB_ID);
        table.searchByAttributeWithLabel(NAME_COLUMN_NAME, TEXT_FIELD, FIRST_MICROWAVE_ANTENNA_NAME);
        assertPresenceOfObjectInElementRoutingTab(0, FIRST_MICROWAVE_ANTENNA_NAME, FIRST_MW_ANTENNA_RELATION_TYPE);
        table.clearColumnValue(NAME_COLUMN_NAME);

        table.searchByAttributeWithLabel(NAME_COLUMN_NAME, TEXT_FIELD, SECOND_MICROWAVE_ANTENNA_NAME);
        assertPresenceOfObjectInElementRoutingTab(0, SECOND_MICROWAVE_ANTENNA_NAME, SECOND_MW_ANTENNA_RELATION_TYPE);
        table.clearColumnValue(NAME_COLUMN_NAME);
    }

    @Test(priority = 8, description = "Add two Outdoor Units to each Microwave Channel element routing", dependsOnMethods = {"createMicrowaveChannelWithTerminations"})
    @Description("Add two Outdoor Units to each Microwave Channel element routing")
    public void addTwoOutdoorUnitsToEachMicrowaveChannelElementRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandViewContentPanel();
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, microwaveLink);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, firstMicrowaveChannel);
        waitForPageToLoad();

        networkViewPage.startEditingSelectedTrail();
        waitForPageToLoad();

        addObjectToView(SECOND_OUTDOOR_UNIT_NAME);
        waitForPageToLoad();
        addObjectToView(FIRST_OUTDOOR_UNIT_NAME);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, SECOND_OUTDOOR_UNIT_NAME);
        waitForPageToLoad();

        networkViewPage.addSelectedObjectsToElementRouting();
        fillRoutingOverElementsWizard(FIRST_ODU_SEQUENCE_NUMBER, FIRST_ODU_RELATION_TYPE, 0);
        fillRoutingOverElementsWizard(SECOND_ODU_SEQUENCE_NUMBER, SECOND_ODU_RELATION_TYPE, 1);
        waitForPageToLoad();
        clickAcceptInElementRoutingWizard();

        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, SECOND_OUTDOOR_UNIT_NAME);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, firstMicrowaveChannel);
        waitForPageToLoad();
        networkViewPage.stopEditingTrail();
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, FIRST_OUTDOOR_UNIT_NAME);
        waitForPageToLoad();
        networkViewPage.hideDockedPanel(DOCKED_PANEL_POSITION);
        waitForPageToLoad();
        networkViewPage.openElementRoutingTab();
        waitForPageToLoad();

        OldTable table = OldTable.createById(driver, webDriverWait, ROUTING_ELEMENTS_TAB_ID);
        table.searchByAttributeWithLabel(NAME_COLUMN_NAME, TEXT_FIELD, FIRST_OUTDOOR_UNIT_NAME);
        assertPresenceOfObjectInElementRoutingTab(0, FIRST_OUTDOOR_UNIT_NAME, FIRST_ODU_RELATION_TYPE);
        table.clearColumnValue(NAME_COLUMN_NAME);
        table.searchByAttributeWithLabel(NAME_COLUMN_NAME, TEXT_FIELD, SECOND_OUTDOOR_UNIT_NAME);
        assertPresenceOfObjectInElementRoutingTab(0, SECOND_OUTDOOR_UNIT_NAME, SECOND_ODU_RELATION_TYPE);
        table.clearColumnValue(NAME_COLUMN_NAME);

        networkViewPage.expandViewContentPanel();
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, firstMicrowaveChannel);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, secondMicrowaveChannel);
        waitForPageToLoad();
        networkViewPage.startEditingSelectedTrail();
        waitForPageToLoad();

        addObjectToView(FORTH_OUTDOOR_UNIT_NAME);
        waitForPageToLoad();
        addObjectToView(THIRD_OUTDOOR_UNIT_NAME);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, FORTH_OUTDOOR_UNIT_NAME);
        waitForPageToLoad();

        networkViewPage.addSelectedObjectsToElementRouting();
        fillRoutingOverElementsWizard(THIRD_ODU_SEQUENCE_NUMBER, THIRD_ODU_RELATION_TYPE, 0);
        fillRoutingOverElementsWizard(FORTH_ODU_SEQUENCE_NUMBER, FORTH_ODU_RELATION_TYPE, 1);
        waitForPageToLoad();
        clickAcceptInElementRoutingWizard();

        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, FORTH_OUTDOOR_UNIT_NAME);
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, THIRD_OUTDOOR_UNIT_NAME);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, secondMicrowaveChannel);
        waitForPageToLoad();
        networkViewPage.stopEditingTrail();
        waitForPageToLoad();
        networkViewPage.hideDockedPanel(DOCKED_PANEL_POSITION);
        waitForPageToLoad();
        networkViewPage.openElementRoutingTab();
        waitForPageToLoad();

        OldTable table2 = OldTable.createById(driver, webDriverWait, ROUTING_ELEMENTS_TAB_ID);
        table2.searchByAttributeWithLabel(NAME_COLUMN_NAME, TEXT_FIELD, THIRD_OUTDOOR_UNIT_NAME);
        assertPresenceOfObjectInElementRoutingTab(0, THIRD_OUTDOOR_UNIT_NAME, THIRD_ODU_RELATION_TYPE);
        table2.clearColumnValue(NAME_COLUMN_NAME);

        table2.searchByAttributeWithLabel(NAME_COLUMN_NAME, TEXT_FIELD, FORTH_OUTDOOR_UNIT_NAME);
        assertPresenceOfObjectInElementRoutingTab(0, FORTH_OUTDOOR_UNIT_NAME, FORTH_ODU_RELATION_TYPE);
        table2.clearColumnValue(NAME_COLUMN_NAME);
    }

    @Test(priority = 9, description = "Create Ethernet Link", dependsOnMethods = {"createMicrowaveLink"})
    @Description("Create Ethernet Link")
    public void createEthernetLink() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.expandViewContentPanel();
        waitForPageToLoad();
        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, secondMicrowaveChannel);
        waitForPageToLoad();

        networkViewPage.openWizardPage(ETHERNET_LINK_TRAIL_TYPE);
        waitForPageToLoad();
        EthernetLinkWizardPage ethernetLinkWizardPage = new EthernetLinkWizardPage(driver);
        fillEthernetLinkWizard(ethernetLinkWizardPage);
    }

    @Test(priority = 10, description = "Add Microwave Link to Ethernet Link routing", dependsOnMethods = {"createEthernetLink"})
    @Description("Add Microwave Link to Ethernet Link routing")
    public void addMicrowaveLinkToEthernetLinkRouting() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.startEditingSelectedTrail();
        waitForPageToLoad();

        OldTable table = OldTable.createById(driver, webDriverWait, CONTENT_TAB_ID);
        table.searchByAttributeWithLabel(NAME_COLUMN_NAME, TEXT_FIELD, ETHERNET_LINK_PARTIAL_NAME);
        String ethernetLink = getConnectionName();
        waitForPageToLoad();
        table.clearColumnValue(NAME_COLUMN_NAME);
        waitForPageToLoad();

        networkViewPage.unselectObjectInViewContent(NAME_COLUMN_NAME, ethernetLink);
        waitForPageToLoad();
        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, microwaveLink);
        waitForPageToLoad();

        addMicrowaveLinkToEthLinkRouting();
        waitForPageToLoad();

        networkViewPage.selectObjectInViewContent(NAME_COLUMN_NAME, ethernetLink);
        waitForPageToLoad();
        networkViewPage.hideDockedPanel(DOCKED_PANEL_POSITION);
        waitForPageToLoad();

        networkViewPage.openFirstLevelRoutingTab();
        assertPresenceOfObjectInTab(0, CONNECTION_LABEL_COLUMN, FIRST_LEVEL_ROUTING_TAB_ID, microwaveLinkLabel);
    }

    @Test(priority = 11, description = "Check Microwave Link attributes and assigned objects on New Inventory View", dependsOnMethods = {"createMicrowaveLink"})
    @Description("Check Microwave Link attributes and assigned objects on New Inventory View")
    public void checkMicrowaveLinkAttributesAndAssignedObjectsOnNewInventoryView() {
        homePage.goToHomePage(driver, BASIC_URL);
        expandTiles(RESOURCE_INVENTORY_CATEGORY_NAME, INVENTORY_VIEW_APPLICATION_NAME);
        waitForPageToLoad();
        openInventoryViewForGivenObjectType(MICROWAVE_LINK_TRAIL_TYPE);
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(microwaveLinkLabel);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();

        MicrowaveLinkAttributes microwaveLinkAttributes = getMicrowaveLinkAttributes();
        assertMicrowaveLinkAttributesOnNewInventoryView(newInventoryViewPage, microwaveLinkAttributes);

        openTab(TERMINATIONS_TAB_LABEL);
        assertPresenceOfObjectInTab(0, CARD_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ID, CARD_NAME);
        assertPresenceOfObjectInTab(1, CARD_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ID, CARD_NAME);

        openTab(OCCUPATION_TAB_LABEL);
        assertPresenceOfObjectInTab(0, CONNECTION_LABEL_COLUMN, OCCUPATION_TAB_ID, ETHERNET_LINK_NAME);

        openTab(ROUTED_TRAILS_TAB_LABEL);
        assertPresenceOfObjectInTab(0, CONNECTION_LABEL_COLUMN, ROUTED_TRAILS_TAB_ID, firstMicrowaveChannelLabel);
        assertPresenceOfObjectInTab(1, CONNECTION_LABEL_COLUMN, ROUTED_TRAILS_TAB_ID, secondMicrowaveChannelLabel);

        openTab(ELEMENT_ROUTING_TAB_LABEL);
        assertPresenceOfObjectInElementRoutingTab(0, FIRST_MICROWAVE_ANTENNA_NAME, FIRST_MW_ANTENNA_RELATION_TYPE);
        assertPresenceOfObjectInElementRoutingTab(1, SECOND_MICROWAVE_ANTENNA_NAME, SECOND_MW_ANTENNA_RELATION_TYPE);
    }

    @Test(priority = 12, description = "Check first Microwave Channel attributes and assigned objects on New Inventory View", dependsOnMethods = {"createMicrowaveChannelWithTerminations"})
    @Description("Check first Microwave Channel attributes and assigned objects on New Inventory View")
    public void checkFirstMicrowaveChannelAttributesAndAssignedObjectsOnNewInventoryView() {
        homePage.goToHomePage(driver, BASIC_URL);
        expandTiles(RESOURCE_INVENTORY_CATEGORY_NAME, INVENTORY_VIEW_APPLICATION_NAME);
        waitForPageToLoad();
        openInventoryViewForGivenObjectType(MICROWAVE_CHANNEL_TRAIL_TYPE);
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(firstMicrowaveChannelLabel);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();

        MicrowaveChannelAttributes firstMicrowaveChannelAttributes = getFirstMicrowaveChannelAttributes();
        assertMicrowaveChannelAttributesOnNewInventoryView(newInventoryViewPage, firstMicrowaveChannelAttributes);

        openTab(TERMINATIONS_TAB_LABEL);
        assertPresenceOfObjectInTab(0, PORT_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ON_NEW_IV_ID, PORT_LABEL);
        assertPresenceOfObjectInTab(1, PORT_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ON_NEW_IV_ID, PORT_LABEL);

        openTab(OCCUPATION_TAB_LABEL);
        assertPresenceOfObjectInTab(0, CONNECTION_LABEL_COLUMN, OCCUPATION_TAB_ID, microwaveLinkLabel);

        openTab(ELEMENT_ROUTING_TAB_LABEL);
        assertPresenceOfObjectInElementRoutingTab(0, FIRST_OUTDOOR_UNIT_NAME, FIRST_ODU_RELATION_TYPE);
        assertPresenceOfObjectInElementRoutingTab(1, SECOND_OUTDOOR_UNIT_NAME, SECOND_ODU_RELATION_TYPE);
    }

    @Test(priority = 13, description = "Check second Microwave Channel attributes and assigned objects on New Inventory View", dependsOnMethods = {"createMicrowaveChannelAndAddTerminations"})
    @Description("Check second Microwave Channel attributes and assigned objects on New Inventory View")
    public void checkSecondMicrowaveChannelAttributesAndAssignedObjectsOnNewInventoryView() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.unselectObjectByRowId(0);
        newInventoryViewPage.searchObject(secondMicrowaveChannelLabel);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        MicrowaveChannelAttributes secondMicrowaveChannelAttributes = getSecondMicrowaveChannelAttributes();
        assertMicrowaveChannelAttributesOnNewInventoryView(newInventoryViewPage, secondMicrowaveChannelAttributes);

        openTab(TERMINATIONS_TAB_LABEL);
        assertPresenceOfObjectInTab(0, CARD_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ON_NEW_IV_ID, CARD_NAME);
        assertPresenceOfObjectInTab(1, CARD_SHORT_IDENTIFIER_COLUMN, TERMINATIONS_TAB_ON_NEW_IV_ID, CARD_NAME);

        openTab(OCCUPATION_TAB_LABEL);
        assertPresenceOfObjectInTab(0, CONNECTION_LABEL_COLUMN, OCCUPATION_TAB_ID, microwaveLinkLabel);

        openTab(ELEMENT_ROUTING_TAB_LABEL);
        assertPresenceOfObjectInElementRoutingTab(0, THIRD_OUTDOOR_UNIT_NAME, THIRD_ODU_RELATION_TYPE);
        assertPresenceOfObjectInElementRoutingTab(1, FORTH_OUTDOOR_UNIT_NAME, FORTH_ODU_RELATION_TYPE);
    }

    @Test(priority = 14, description = "Delete Ethernet Link on New Inventory View", dependsOnMethods = {"createEthernetLink"})
    @Description("Delete Ethernet Link")
    public void deleteEthernetLink() {
        homePage.goToHomePage(driver, BASIC_URL);
        expandTiles(RESOURCE_INVENTORY_CATEGORY_NAME, INVENTORY_VIEW_APPLICATION_NAME);
        waitForPageToLoad();
        openInventoryViewForGivenObjectType(ETHERNET_LINK_TRAIL_TYPE);

        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(ETHERNET_LINK_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();

        newInventoryViewPage.callAction(EDIT_GROUP_ID, DELETE_CONNECTION_ACTION_ID);
        waitForPageToLoad();
        getConfirmationWizard().clickButtonById(DELETE_SUBMIT_BUTTON_ID);

        waitForPageToLoad();
        newInventoryViewPage.checkIfTableIsEmpty();
    }

    @Test(priority = 15, description = "Delete Microwave Link on New Inventory View", dependsOnMethods = {"checkMicrowaveLinkAttributesAndAssignedObjectsOnNewInventoryView"})
    @Description("Delete Microwave Link")
    public void deleteMicrowaveLink() {
        homePage.goToHomePage(driver, BASIC_URL);
        expandTiles(RESOURCE_INVENTORY_CATEGORY_NAME, INVENTORY_VIEW_APPLICATION_NAME);
        waitForPageToLoad();
        openInventoryViewForGivenObjectType(MICROWAVE_LINK_TRAIL_TYPE);

        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(microwaveLink);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(EDIT_GROUP_ID, DELETE_MW_CONNECTION_CONFIRMATION_ID);
        waitForPageToLoad();
        newInventoryViewPage.checkIfTableIsEmpty();
    }

    @Test(priority = 16, description = "Delete first and second Microwave Channel on New Inventory View", dependsOnMethods = {"checkFirstMicrowaveChannelAttributesAndAssignedObjectsOnNewInventoryView"})
    @Description("Delete first and second Microwave Channel")
    public void deleteBothMicrowaveChannels() {
        homePage.goToHomePage(driver, BASIC_URL);
        expandTiles(RESOURCE_INVENTORY_CATEGORY_NAME, INVENTORY_VIEW_APPLICATION_NAME);
        waitForPageToLoad();
        openInventoryViewForGivenObjectType(MICROWAVE_CHANNEL_TRAIL_TYPE);

        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(firstMicrowaveChannel);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(EDIT_GROUP_ID, DELETE_CONNECTION_ACTION_ID);
        waitForPageToLoad();
        clickConfirmationBox(DELETE_MW_CONNECTION_CONFIRMATION_ID);
        waitForPageToLoad();
        newInventoryViewPage.checkIfTableIsEmpty();

        newInventoryViewPage.searchObject(secondMicrowaveChannel);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(EDIT_GROUP_ID, DELETE_CONNECTION_ACTION_ID);
        waitForPageToLoad();
        clickConfirmationBox(DELETE_MW_CONNECTION_CONFIRMATION_ID);
        waitForPageToLoad();
        newInventoryViewPage.checkIfTableIsEmpty();
    }

    @Test(priority = 17, description = "Delete first and second Microwave Antenna on New Inventory View", dependsOnMethods = {"createDevices"})
    @Description("Delete first and second Microwave Antenna")
    public void deleteBothMicrowaveAntennas() {
        homePage.goToHomePage(driver, BASIC_URL);
        expandTiles(RESOURCE_INVENTORY_CATEGORY_NAME, INVENTORY_VIEW_APPLICATION_NAME);
        waitForPageToLoad();
        openInventoryViewForGivenObjectType(MICROWAVE_ANTENNA_OBJECT_TYPE);

        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(FIRST_MICROWAVE_ANTENNA_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(EDIT_GROUP_ID, DELETE_DEVICE_ACTION_ID);
        waitForPageToLoad();
        clickConfirmationBox(DELETE_DEVICE_CONFIRMATION_ID);
        waitForPageToLoad();
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        newInventoryViewPage.checkIfTableIsEmpty();
        newInventoryViewPage.clearFilters();

        driver.navigate().refresh();
        newInventoryViewPage.searchObject(SECOND_MICROWAVE_ANTENNA_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(EDIT_GROUP_ID, DELETE_DEVICE_ACTION_ID);
        waitForPageToLoad();
        clickConfirmationBox(DELETE_DEVICE_CONFIRMATION_ID);
        waitForPageToLoad();
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        newInventoryViewPage.checkIfTableIsEmpty();
    }

    @Test(priority = 18, description = "Delete first and second Indoor Units on New Inventory View", dependsOnMethods = {"createDevices"})
    @Description("Delete first and second Indoor Units")
    public void deleteBothIndoorUnits() {
        homePage.goToHomePage(driver, BASIC_URL);
        expandTiles(RESOURCE_INVENTORY_CATEGORY_NAME, INVENTORY_VIEW_APPLICATION_NAME);
        waitForPageToLoad();
        openInventoryViewForGivenObjectType(INDOOR_UNIT_OBJECT_TYPE);

        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(FIRST_INDOOR_UNIT_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(EDIT_GROUP_ID, DELETE_DEVICE_ACTION_ID);
        waitForPageToLoad();
        clickConfirmationBox(DELETE_DEVICE_CONFIRMATION_ID);
        waitForPageToLoad();
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        newInventoryViewPage.checkIfTableIsEmpty();

        driver.navigate().refresh();
        newInventoryViewPage.searchObject(SECOND_INDOOR_UNIT_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(EDIT_GROUP_ID, DELETE_DEVICE_ACTION_ID);
        waitForPageToLoad();
        clickConfirmationBox(DELETE_DEVICE_CONFIRMATION_ID);
        waitForPageToLoad();
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        newInventoryViewPage.checkIfTableIsEmpty();
    }

    @Test(priority = 19, description = "Delete first and second Outdoor Units on New Inventory View", dependsOnMethods = {"createDevices"})
    @Description("Delete first and second Outdoor Units")
    public void deleteBothOutdoorUnits() {
        homePage.goToHomePage(driver, BASIC_URL);
        expandTiles(RESOURCE_INVENTORY_CATEGORY_NAME, INVENTORY_VIEW_APPLICATION_NAME);
        waitForPageToLoad();
        openInventoryViewForGivenObjectType(OUTDOOR_UNIT_OBJECT_TYPE);

        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(FIRST_OUTDOOR_UNIT_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(EDIT_GROUP_ID, DELETE_DEVICE_ACTION_ID);
        waitForPageToLoad();
        clickConfirmationBox(DELETE_DEVICE_CONFIRMATION_ID);
        waitForPageToLoad();
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        newInventoryViewPage.checkIfTableIsEmpty();

        driver.navigate().refresh();
        newInventoryViewPage.searchObject(SECOND_OUTDOOR_UNIT_NAME);
        waitForPageToLoad();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(EDIT_GROUP_ID, DELETE_DEVICE_ACTION_ID);
        waitForPageToLoad();
        clickConfirmationBox(DELETE_DEVICE_CONFIRMATION_ID);
        waitForPageToLoad();
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        newInventoryViewPage.checkIfTableIsEmpty();
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

    private void openInventoryViewForGivenObjectType(String objectType) {
        waitForPageToLoad();
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType(objectType);
    }

    private void expandTiles(String categoryName, String applicationName) {
        ToolsManagerPage toolsManagerPage = new ToolsManagerPage(driver);
        waitForPageToLoad();
        ToolsManagerWindow toolsManagerWindow = toolsManagerPage.getToolsManager();
        waitForPageToLoad();
        toolsManagerWindow.openApplication(categoryName, applicationName);
    }

    private void addObjectToView(String objectName) {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        networkViewPage.useContextAction(NetworkViewPage.ADD_TO_VIEW_ACTION, NetworkViewPage.DEVICE_ACTION);
        networkViewPage.queryElementAndAddItToView(NAME_COMPONENT_ID, objectName);
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
        DelayUtils.sleep(5000);
        deviceWizardPage.setPreciseLocation(locationName);
        waitForPageToLoad();
        deviceWizardPage.accept();
    }

    private void createIndoorUnit(String indoorUnitName, String locationName) {
        createPhysicalDevice(INDOOR_UNIT_MODEL, indoorUnitName, locationName);
        checkSystemMessage();
        clickMessage();
        waitForPageToLoad();
        addCardToPhysicalDevice();
        waitForPageToLoad();
    }

    private void createMicrowaveAntenna(String microwaveAntennaName, String locationName) {
        createPhysicalDevice(MICROWAVE_ANTENNA_MODEL, microwaveAntennaName, locationName);
        checkSystemMessage();
        waitForPageToLoad();
    }

    private void createOutdoorUnit(String outdoorUnitModel, String outdoorUnitName, String locationName) {
        createPhysicalDevice(outdoorUnitModel, outdoorUnitName, locationName);
        checkSystemMessage();
        waitForPageToLoad();
    }

    private Wizard getConfirmationWizard() {
        return Wizard.createByComponentId(driver, webDriverWait, CONFIRMATION_WIZARD_ID);
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
        attributes.channelName = CHANNEL_NAME;
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
        attributes.channelName = CHANNEL_NAME2;
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
        attributes.capacityValue = FIRST_MWC_CAPACITY_VALUE;
        attributes.network = NETWORK;
        attributes.pathLength = PATH_LENGTH;
        attributes.description = MWL_DESCRIPTION;

        return attributes;
    }

    private String getMicrowaveChannel(Integer index) {
        OldTable table = OldTable.createById(driver, webDriverWait, CONTENT_TAB_ID);
        return table.getCellValue(index, NAME_ATTRIBUTE_LABEL);
    }

    private String getConnectionName() {
        OldTable table = OldTable.createById(driver, webDriverWait, CONTENT_TAB_ID);
        return table.getCellValue(0, NAME_ATTRIBUTE_LABEL);
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
        private String channelName;
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

    private void addMicrowaveLinkToEthLinkRouting() {
        OldActionsContainer.createById(driver, webDriverWait, ACTION_CONTAINER_ID).callActionById(EDIT_GROUP_ID, ROUTING_ACTION_ID);
        waitForPageToLoad();
        ConnectionWizardPage connectionWizardPage = new ConnectionWizardPage(driver);
        connectionWizardPage.clickAccept();
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
        fillFifthStepOfMicrowaveChannelWizard(microwaveChannelWizardPage);
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
        microwaveChannelWizardPage.setMicrowaveFrequencyPlan(microwaveChannelAttributes.channelName);
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

    private void fillFifthStepOfMicrowaveChannelWizard(MicrowaveChannelWizardPage microwaveChannelWizardPage) {
        microwaveChannelWizardPage.setTermination(START_CARD_FIELD_ID, CARD_NAME);
        microwaveChannelWizardPage.setTermination(START_PORT_FIELD_ID, PORT_NAME);
        microwaveChannelWizardPage.setTermination(END_CARD_FIELD_ID, CARD_NAME);
        microwaveChannelWizardPage.setTermination(END_PORT_FIELD_ID, PORT_NAME);
        microwaveChannelWizardPage.clickAccept();
    }

    private void fillMicrowaveLinkWizard(MicrowaveLinkWizardPage microwaveLinkWizardPage, MicrowaveLinkAttributes microwaveLinkAttributes) {
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

    private void fillAddRoutingSegmentWizard(String lineType, String sequenceNumber) {
        RoutingWizardPage routingWizardPage = new RoutingWizardPage(driver);
        waitForPageToLoad();
//        TODO: odkomentować po rozwiązaniu OSSTRAIL-7974
//        routingWizardPage.setLineType(lineType);
//        routingWizardPage.setSequenceNumber(sequenceNumber);
        routingWizardPage.proceed();
    }

    private void fillRoutingOverElementsWizard(String sequenceNumber, String relationType, int rowNumber) {
        RoutingOverElementsWizardPage routingOverElementsWizardPage = new RoutingOverElementsWizardPage(driver);
        waitForPageToLoad();
        routingOverElementsWizardPage.setSequenceNumber(rowNumber, sequenceNumber);
        waitForPageToLoad();
        routingOverElementsWizardPage.setRelationType(rowNumber, relationType);
        waitForPageToLoad();
    }

    private void clickAcceptInElementRoutingWizard() {
        RoutingOverElementsWizardPage routingOverElementsWizardPage = new RoutingOverElementsWizardPage(driver);
        routingOverElementsWizardPage.clickAccept();
        waitForPageToLoad();
    }

    private void fillEthernetLinkWizard(EthernetLinkWizardPage ethernetLinkWizardPage) {
        ethernetLinkWizardPage.setName(ETHERNET_LINK_NAME);
        waitForPageToLoad();
        ethernetLinkWizardPage.setSpeed(SPEED_VALUE);
        waitForPageToLoad();
        ethernetLinkWizardPage.clickNext();
        waitForPageToLoad();
        ethernetLinkWizardPage.clickAccept();
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
        String channelName = networkViewPage.getAttributeValue(CHANNEL_NAME_ATTRIBUTE_LABEL);
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

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(band, microwaveChannelAttributes.band, "Band value is other than expected.");
        softAssert.assertEquals(channelBandwidth, microwaveChannelAttributes.channelBandwidth, "Channel Bandwidth value is other than expected.");
        softAssert.assertEquals(lowFrequency, microwaveChannelAttributes.lowFrequency, "Low Frequency value is other than expected.");
        softAssert.assertEquals(highFrequency, microwaveChannelAttributes.highFrequency, "High Frequency value is other than expected.");
        softAssert.assertEquals(polarization, microwaveChannelAttributes.polarization, "Polarization value is other than expected.");
        softAssert.assertEquals(workingStatus, microwaveChannelAttributes.workingStatus, "Working Status value is other than expected.");
        softAssert.assertEquals(hsbFlag, microwaveChannelAttributes.hsbFlag, "HSB Flag value is other than expected.");
        softAssert.assertEquals(xpolFlag, microwaveChannelAttributes.xpolFlag, "XPOL Flag value is other than expected.");
        softAssert.assertEquals(admFlag, microwaveChannelAttributes.admFlag, "ADM Flag value is other than expected.");
        softAssert.assertEquals(referenceChannelModulation, microwaveChannelAttributes.referenceChannelModulation, "Reference Channel Modulation value is other than expected.");
        softAssert.assertEquals(lowestChannelModulation, microwaveChannelAttributes.lowestChannelModulation, "Lowest Channel Modulation value is other than expected.");
        softAssert.assertEquals(highestChannelModulation, microwaveChannelAttributes.highestChannelModulation, "Highest Channel Modulation value is other than expected.");
        softAssert.assertEquals(startTxPower, microwaveChannelAttributes.startTxPower, "Start Tx Power value is other than expected.");
        softAssert.assertEquals(endTxPower, microwaveChannelAttributes.endTxPower, "End Tx Power value is other than expected.");
        softAssert.assertEquals(startRxPower, microwaveChannelAttributes.startRxPower, "Start Rx Power value is other than expected.");
        softAssert.assertEquals(endRxPower, microwaveChannelAttributes.endRxPower, "End Rx Power value is other than expected.");
        softAssert.assertEquals(atpc, microwaveChannelAttributes.atpc, "ATPC value is other than expected.");
        softAssert.assertEquals(atpcRxMinLevel, microwaveChannelAttributes.atpcRxMinLevel, "ATPC Rx Min Level value is other than expected.");
        softAssert.assertEquals(atpcRxMaxLevel, microwaveChannelAttributes.atpcRxMaxLevel, "ATPC Rx Max Level value is other than expected.");
        softAssert.assertEquals(dcnLocation, microwaveChannelAttributes.dcnLocation, "DCN Location value is other than expected.");
        softAssert.assertEquals(endAttenuatorManufacturer, microwaveChannelAttributes.endAttenuatorManufacturer, "End Attenuator Manufacturer value is other than expected.");
        softAssert.assertEquals(endAttenuatorModel, microwaveChannelAttributes.endAttenuatorModel, "End Attenuator Model value is other than expected.");
        softAssert.assertEquals(endDiversityWaveguideLength, microwaveChannelAttributes.endDiversityWaveguideLength, "End Diversity Waveguide Length value is other than expected.");
        softAssert.assertEquals(endDiversityWaveguideManufacturer, microwaveChannelAttributes.endDiversityWaveguideManufacturer, "End Diversity Waveguide Manufacturer value is other than expected.");
        softAssert.assertEquals(endDiversityWaveguideModel, microwaveChannelAttributes.endDiversityWaveguideModel, "End Diversity Waveguide Model value is other than expected.");
        softAssert.assertEquals(startAttenuatorManufacturer, microwaveChannelAttributes.startAttenuatorManufacturer, "Start Attenuator Manufacturer value is other than expected.");
        softAssert.assertEquals(startAttenuatorModel, microwaveChannelAttributes.startAttenuatorModel, "Start Attenuator Model value is other than expected.");
        softAssert.assertEquals(startDiversityWaveguideLength, microwaveChannelAttributes.startDiversityWaveguideLength, "Start Diversity Waveguide Length value is other than expected.");
        softAssert.assertEquals(startDiversityWaveguideManufacturer, microwaveChannelAttributes.startDiversityWaveguideManufacturer, "Start Diversity Waveguide Manufacturer value is other than expected.");
        softAssert.assertEquals(startDiversityWaveguideModel, microwaveChannelAttributes.startDiversityWaveguideModel, "Start Diversity Waveguide Model value is other than expected.");
        softAssert.assertEquals(startDiversityWaveguideTotalLoss, microwaveChannelAttributes.startDiversityWaveguideTotalLoss, "Start Diversity Waveguide Total Loss value is other than expected.");
        softAssert.assertEquals(startRadioModel, microwaveChannelAttributes.startRadioModel, "Start Radio Model value is other than expected.");
        softAssert.assertEquals(startWaveguideManufacturer, microwaveChannelAttributes.startWaveguideManufacturer, "Start Waveguide Manufacturer value is other than expected.");
        softAssert.assertEquals(startWaveguideModel, microwaveChannelAttributes.startWaveguideModel, "Start Waveguide Model value is other than expected.");
        softAssert.assertEquals(startWaveguideTotalLoss, microwaveChannelAttributes.startWaveguideTotalLoss, "Start Waveguide Total Loss value is other than expected.");
        softAssert.assertEquals(description, microwaveChannelAttributes.description, "Description value is other than expected.");
        softAssert.assertEquals(channelName, microwaveChannelAttributes.channelName, "Channel Name value is other than expected.");
        softAssert.assertEquals(channelNumber, microwaveChannelAttributes.channelNumber, "Channel Number value is other than expected.");
        softAssert.assertEquals(configuration, microwaveChannelAttributes.configuration, "Configuration value is other than expected.");
        softAssert.assertEquals(endAttenuatorLoss, microwaveChannelAttributes.endAttenuatorLoss, "End Attenuator Loss value is other than expected.");
        softAssert.assertEquals(endDiversityWaveguideTotalLoss, microwaveChannelAttributes.endDiversityWaveguideTotalLoss, "End Diversity Waveguide Total Loss value is other than expected.");
        softAssert.assertEquals(endRadioModel, microwaveChannelAttributes.endRadioModel, "End Radio Model value is other than expected.");
        softAssert.assertEquals(endWaveguideLength, microwaveChannelAttributes.endWaveguideLength, "End Waveguide Length value is other than expected.");
        softAssert.assertEquals(endWaveguideManufacturer, microwaveChannelAttributes.endWaveguideManufacturer, "End Waveguide Manufacturer value is other than expected.");
        softAssert.assertEquals(endWaveguideModel, microwaveChannelAttributes.endWaveguideModel, "End Waveguide Model value is other than expected.");
        softAssert.assertEquals(endWaveguideTotalLoss, microwaveChannelAttributes.endWaveguideTotalLoss, "End Waveguide Total Loss value is other than expected.");
        softAssert.assertEquals(startAttenuatorLoss, microwaveChannelAttributes.startAttenuatorLoss, "Start Attenuator Loss value is other than expected.");
        softAssert.assertEquals(startWaveguideLength, microwaveChannelAttributes.startWaveguideLength, "Start Waveguide Length value is other than expected.");
        softAssert.assertEquals(endAttenuatorMode, microwaveChannelAttributes.endAttenuatorMode, "End Attenuator Mode value is other than expected.");
        softAssert.assertEquals(highFrequencySite, microwaveChannelAttributes.highFrequencySite, "High Frequency Site value is other than expected.");
        softAssert.assertEquals(startAttenuatorMode, microwaveChannelAttributes.startAttenuatorMode, "Start Attenuator Mode value is other than expected.");
        softAssert.assertAll();
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

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(capacityValue, microwaveLinkAttributes.capacityValue, "Capacity value is other than expected.");
        softAssert.assertEquals(pathLength, microwaveLinkAttributes.pathLength, "Path Length value is other than expected.");
        softAssert.assertEquals(network, microwaveLinkAttributes.network, "Network value is other than expected.");
        softAssert.assertEquals(technologyType, microwaveLinkAttributes.technologyType, "Technology Type value is other than expected.");
        softAssert.assertEquals(aggregationConfiguration, microwaveLinkAttributes.aggregationConfiguration, "Aggregation Configuration value is other than expected.");
        softAssert.assertEquals(userLabel, microwaveLinkAttributes.userLabel, "User Label value is other than expected.");
        softAssert.assertEquals(linkId, microwaveLinkAttributes.linkId, "Link ID value is other than expected.");
        softAssert.assertEquals(numberOfWorkingChannels, microwaveLinkAttributes.numberOfWorkingChannels, "Number Of Working Channels value is other than expected.");
        softAssert.assertEquals(numberOfProtectingChannels, microwaveLinkAttributes.numberOfProtectingChannels, "Number Of Protecting Channels value is other than expected.");
        softAssert.assertEquals(description, microwaveLinkAttributes.description, "Description value is other than expected.");
        softAssert.assertAll();
    }

    private void assertPresenceOfObjectInTab(Integer index, String columnId, String tabId, String objectName) {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        String objectValue = networkViewPage.getObjectValueFromTab(index, columnId, tabId);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(objectValue, objectName, "Expected object isn't present in current Tab.");
        softAssert.assertAll();
    }

    private void assertPresenceOfObjectInElementRoutingTab(Integer index, String name, String relationType) {
        OldTable routingElements = OldTable.createById(driver, webDriverWait, ELEMENT_ROUTING_TAB_ID);
        String actualNameValue = routingElements.getCellValue(index, NAME_COLUMN_LABEL);
        String actualRelationTypeValue = routingElements.getCellValue(index, RELATION_TYPE_COLUMN_LABEL);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualNameValue, name, "Expected object isn't present in Element Routing Tab.");
        softAssert.assertEquals(actualRelationTypeValue, relationType, "Relation Type Value is other than expected.");
        softAssert.assertAll();
    }

    private void assertMicrowaveLinkCapacityValue(NetworkViewPage networkViewPage, String expectedCapacityValue) {
        String actualCapacityValue = networkViewPage.getAttributeValue(CAPACITY_VALUE_ATTRIBUTE_LABEL);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualCapacityValue, expectedCapacityValue, "Capacity Value is other than expected.");
        softAssert.assertAll();
    }

    private void assertMicrowaveChannelWorkingStatus(NetworkViewPage networkViewPage) {
        String actualWorkingStatus = networkViewPage.getAttributeValue(WORKING_STATUS_ATTRIBUTE_LABEL);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualWorkingStatus, WORKING_LINE_TYPE, "Working Status is other than expected.");
        softAssert.assertAll();
    }

    private void assertMicrowaveLinkAttributesOnNewInventoryView(NewInventoryViewPage newInventoryViewPage, MicrowaveLinkAttributes microwaveLinkAttributes) {
        String capacityValue = newInventoryViewPage.getPropertyPanelValue(CAPACITY_VALUE_ATTRIBUTE_LABEL);
        String pathLength = newInventoryViewPage.getPropertyPanelValue(PATH_LENGTH_ATTRIBUTE_LABEL);
        String network = newInventoryViewPage.getPropertyPanelValue(NETWORK_ATTRIBUTE_LABEL);
        String technologyType = newInventoryViewPage.getPropertyPanelValue(TECHNOLOGY_TYPE_ATTRIBUTE_LABEL);
        String aggregationConfiguration = newInventoryViewPage.getPropertyPanelValue(AGGREGATION_CONFIGURATION_ATTRIBUTE_LABEL);
        String userLabel = newInventoryViewPage.getPropertyPanelValue(USER_LABEL_ATTRIBUTE_LABEL);
        String linkId = newInventoryViewPage.getPropertyPanelValue(LINK_ID_ATTRIBUTE_LABEL);
        String numberOfWorkingChannels = newInventoryViewPage.getPropertyPanelValue(NUMBER_OF_WORKING_CHANNELS_ATTRIBUTE_LABEL);
        String numberOfProtectingChannels = newInventoryViewPage.getPropertyPanelValue(NUMBER_OF_PROTECTING_CHANNELS_ATTRIBUTE_LABEL);
        String description = newInventoryViewPage.getPropertyPanelValue(DESCRIPTION_ATTRIBUTE_LABEL);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(capacityValue, EXPECTED_MWL_CAPACITY_VALUE, "Capacity value is other than expected.");
        softAssert.assertEquals(pathLength, microwaveLinkAttributes.pathLength, "Path Length value is other than expected.");
        softAssert.assertEquals(network, microwaveLinkAttributes.network, "Network value is other than expected.");
        softAssert.assertEquals(technologyType, microwaveLinkAttributes.technologyType, "Technology Type value is other than expected.");
        softAssert.assertEquals(aggregationConfiguration, microwaveLinkAttributes.aggregationConfiguration, "Aggregation Configuration value is other than expected.");
        softAssert.assertEquals(userLabel, microwaveLinkAttributes.userLabel, "User Label value is other than expected.");
        softAssert.assertNotNull(linkId, "Link ID value is null.");
        softAssert.assertEquals(numberOfWorkingChannels, microwaveLinkAttributes.numberOfWorkingChannels, "Number Of Working Channels value is other than expected.");
        softAssert.assertEquals(numberOfProtectingChannels, microwaveLinkAttributes.numberOfProtectingChannels, "Number Of Protecting Channels value is other than expected.");
        softAssert.assertEquals(description, microwaveLinkAttributes.description, "Description value is other than expected.");
        softAssert.assertAll();
    }

    private void assertMicrowaveChannelAttributesOnNewInventoryView(NewInventoryViewPage newInventoryViewPage, MicrowaveChannelAttributes microwaveChannelAttributes) {
        String band = newInventoryViewPage.getPropertyPanelValue(BAND_ATTRIBUTE_LABEL);
        String channelBandwidth = newInventoryViewPage.getPropertyPanelValue(CHANNEL_BANDWIDTH_ATTRIBUTE_LABEL);
        String lowFrequency = newInventoryViewPage.getPropertyPanelValue(LOW_FREQUENCY_ATTRIBUTE_LABEL);
        String highFrequency = newInventoryViewPage.getPropertyPanelValue(HIGH_FREQUENCY_ATTRIBUTE_LABEL);
        String polarization = newInventoryViewPage.getPropertyPanelValue(POLARIZATION_ATTRIBUTE_LABEL);
        String workingStatus = newInventoryViewPage.getPropertyPanelValue(WORKING_STATUS_ATTRIBUTE_LABEL);
        String hsbFlag = newInventoryViewPage.getPropertyPanelValue(HSB_FLAG_ATTRIBUTE_LABEL);
        String xpolFlag = newInventoryViewPage.getPropertyPanelValue(XPOL_FLAG_ATTRIBUTE_LABEL);
        String admFlag = newInventoryViewPage.getPropertyPanelValue(ADM_FLAG_ATTRIBUTE_LABEL);
        String referenceChannelModulation = newInventoryViewPage.getPropertyPanelValue(REFERENCE_CHANNEL_MODULATION_ATTRIBUTE_LABEL);
        String lowestChannelModulation = newInventoryViewPage.getPropertyPanelValue(LOWEST_CHANNEL_MODULATION_ATTRIBUTE_LABEL);
        String highestChannelModulation = newInventoryViewPage.getPropertyPanelValue(HIGHEST_CHANNEL_MODULATION_ATTRIBUTE_LABEL);
        String startTxPower = newInventoryViewPage.getPropertyPanelValue(START_TX_POWER_ATTRIBUTE_LABEL);
        String endTxPower = newInventoryViewPage.getPropertyPanelValue(END_TX_POWER_ATTRIBUTE_LABEL);
        String startRxPower = newInventoryViewPage.getPropertyPanelValue(START_RX_POWER_ATTRIBUTE_LABEL);
        String endRxPower = newInventoryViewPage.getPropertyPanelValue(END_RX_POWER_ATTRIBUTE_LABEL);
        String atpc = newInventoryViewPage.getPropertyPanelValue(ATPC_ATTRIBUTE_LABEL);
        String atpcRxMinLevel = newInventoryViewPage.getPropertyPanelValue(ATPC_RX_MIN_LEVEL_ATTRIBUTE_LABEL);
        String atpcRxMaxLevel = newInventoryViewPage.getPropertyPanelValue(ATPC_RX_MAX_LEVEL_ATTRIBUTE_LABEL);
        String dcnLocation = newInventoryViewPage.getPropertyPanelValue(DCN_LOCATION_ATTRIBUTE_LABEL);
        String endAttenuatorManufacturer = newInventoryViewPage.getPropertyPanelValue(END_ATTENUATOR_MANUFACTURER_ATTRIBUTE_LABEL);
        String endAttenuatorModel = newInventoryViewPage.getPropertyPanelValue(END_ATTENUATOR_MODEL_ATTRIBUTE_LABEL);
        String endDiversityWaveguideLength = newInventoryViewPage.getPropertyPanelValue(END_DIVERSITY_WAVEGUIDE_LENGTH_ATTRIBUTE_LABEL);
        String endDiversityWaveguideManufacturer = newInventoryViewPage.getPropertyPanelValue(END_DIVERSITY_WAVEGUIDE_MANUFACTURER_ATTRIBUTE_LABEL);
        String endDiversityWaveguideModel = newInventoryViewPage.getPropertyPanelValue(END_DIVERSITY_WAVEGUIDE_MODEL_ATTRIBUTE_LABEL);
        String startAttenuatorManufacturer = newInventoryViewPage.getPropertyPanelValue(START_ATTENUATOR_MANUFACTURER_ATTRIBUTE_LABEL);
        String startAttenuatorModel = newInventoryViewPage.getPropertyPanelValue(START_ATTENUATOR_MODEL_ATTRIBUTE_LABEL);
        String startDiversityWaveguideLength = newInventoryViewPage.getPropertyPanelValue(START_DIVERSITY_WAVEGUIDE_LENGTH_ATTRIBUTE_LABEL);
        String startDiversityWaveguideManufacturer = newInventoryViewPage.getPropertyPanelValue(START_DIVERSITY_WAVEGUIDE_MANUFACTURER_ATTRIBUTE_LABEL);
        String startDiversityWaveguideModel = newInventoryViewPage.getPropertyPanelValue(START_DIVERSITY_WAVEGUIDE_MODEL_ATTRIBUTE_LABEL);
        String startDiversityWaveguideTotalLoss = newInventoryViewPage.getPropertyPanelValue(START_DIVERSITY_WAVEGUIDE_TOTAL_LOSS_ATTRIBUTE_LABEL);
        String startRadioModel = newInventoryViewPage.getPropertyPanelValue(START_RADIO_MODEL_ATTRIBUTE_LABEL);
        String startWaveguideManufacturer = newInventoryViewPage.getPropertyPanelValue(START_WAVEGUIDE_MANUFACTURER_ATTRIBUTE_LABEL);
        String startWaveguideModel = newInventoryViewPage.getPropertyPanelValue(START_WAVEGUIDE_MODEL_ATTRIBUTE_LABEL);
        String startWaveguideTotalLoss = newInventoryViewPage.getPropertyPanelValue(START_WAVEGUIDE_TOTAL_LOSS_ATTRIBUTE_LABEL);
        String description = newInventoryViewPage.getPropertyPanelValue(DESCRIPTION_ATTRIBUTE_LABEL);
        String channelNumber = newInventoryViewPage.getPropertyPanelValue(CHANNEL_NUMBER_ATTRIBUTE_LABEL);
        String configuration = newInventoryViewPage.getPropertyPanelValue(CONFIGURATION_ATTRIBUTE_LABEL);
        String endAttenuatorLoss = newInventoryViewPage.getPropertyPanelValue(END_ATTENUATOR_LOSS_ATTRIBUTE_LABEL);
        String endDiversityWaveguideTotalLoss = newInventoryViewPage.getPropertyPanelValue(END_DIVERSITY_WAVEGUIDE_TOTAL_LOSS_ATTRIBUTE_LABEL);
        String endRadioModel = newInventoryViewPage.getPropertyPanelValue(END_RADIO_MODEL_ATTRIBUTE_LABEL);
        String endWaveguideLength = newInventoryViewPage.getPropertyPanelValue(END_WAVEGUIDE_LENGTH_ATTRIBUTE_LABEL);
        String endWaveguideManufacturer = newInventoryViewPage.getPropertyPanelValue(END_WAVEGUIDE_MANUFACTURER_ATTRIBUTE_LABEL);
        String endWaveguideModel = newInventoryViewPage.getPropertyPanelValue(END_WAVEGUIDE_MODEL_ATTRIBUTE_LABEL);
        String endWaveguideTotalLoss = newInventoryViewPage.getPropertyPanelValue(END_WAVEGUIDE_TOTAL_LOSS_ATTRIBUTE_LABEL);
        String startAttenuatorLoss = newInventoryViewPage.getPropertyPanelValue(START_ATTENUATOR_LOSS_ATTRIBUTE_LABEL);
        String startWaveguideLength = newInventoryViewPage.getPropertyPanelValue(START_WAVEGUIDE_LENGTH_ATTRIBUTE_LABEL);
        String endAttenuatorMode = newInventoryViewPage.getPropertyPanelValue(END_ATTENUATOR_MODE_ATTRIBUTE_LABEL);
        String highFrequencySite = newInventoryViewPage.getPropertyPanelValue(HIGH_FREQUENCY_SITE_ATTRIBUTE_LABEL);
        String startAttenuatorMode = newInventoryViewPage.getPropertyPanelValue(START_ATTENUATOR_MODE_ATTRIBUTE_LABEL);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(band, microwaveChannelAttributes.band, "Band value is other than expected.");
        softAssert.assertEquals(channelBandwidth, microwaveChannelAttributes.channelBandwidth, "Channel Bandwidth value is other than expected.");
        softAssert.assertEquals(lowFrequency, microwaveChannelAttributes.lowFrequency, "Low Frequency value is other than expected.");
        softAssert.assertEquals(highFrequency, microwaveChannelAttributes.highFrequency, "High Frequency value is other than expected.");
        softAssert.assertEquals(polarization, microwaveChannelAttributes.polarization, "Polarization value is other than expected.");
        softAssert.assertEquals(workingStatus, microwaveChannelAttributes.workingStatus, "Working Status value is other than expected.");
        softAssert.assertEquals(hsbFlag, microwaveChannelAttributes.hsbFlag, "HSB Flag value is other than expected.");
        softAssert.assertEquals(xpolFlag, microwaveChannelAttributes.xpolFlag, "XPOL Flag value is other than expected.");
        softAssert.assertEquals(admFlag, microwaveChannelAttributes.admFlag, "ADM Flag value is other than expected.");
        softAssert.assertEquals(referenceChannelModulation, microwaveChannelAttributes.referenceChannelModulation, "Reference Channel Modulation value is other than expected.");
        softAssert.assertEquals(lowestChannelModulation, microwaveChannelAttributes.lowestChannelModulation, "Lowest Channel Modulation value is other than expected.");
        softAssert.assertEquals(highestChannelModulation, microwaveChannelAttributes.highestChannelModulation, "Highest Channel Modulation value is other than expected.");
        softAssert.assertEquals(startTxPower, microwaveChannelAttributes.startTxPower, "Start Tx Power value is other than expected.");
        softAssert.assertEquals(endTxPower, microwaveChannelAttributes.endTxPower, "End Tx Power value is other than expected.");
        softAssert.assertEquals(startRxPower, microwaveChannelAttributes.startRxPower, "Start Rx Power value is other than expected.");
        softAssert.assertEquals(endRxPower, microwaveChannelAttributes.endRxPower, "End Rx Power value is other than expected.");
        softAssert.assertEquals(atpc, microwaveChannelAttributes.atpc, "ATPC value is other than expected.");
        softAssert.assertEquals(atpcRxMinLevel, microwaveChannelAttributes.atpcRxMinLevel, "ATPC Rx Min Level value is other than expected.");
        softAssert.assertEquals(atpcRxMaxLevel, microwaveChannelAttributes.atpcRxMaxLevel, "ATPC Rx Max Level value is other than expected.");
        softAssert.assertEquals(dcnLocation, microwaveChannelAttributes.dcnLocation, "DCN Location value is other than expected.");
        softAssert.assertEquals(endAttenuatorManufacturer, microwaveChannelAttributes.endAttenuatorManufacturer, "End Attenuator Manufacturer value is other than expected.");
        softAssert.assertEquals(endAttenuatorModel, microwaveChannelAttributes.endAttenuatorModel, "End Attenuator Model value is other than expected.");
        softAssert.assertEquals(endDiversityWaveguideLength, microwaveChannelAttributes.endDiversityWaveguideLength, "End Diversity Waveguide Length value is other than expected.");
        softAssert.assertEquals(endDiversityWaveguideManufacturer, microwaveChannelAttributes.endDiversityWaveguideManufacturer, "End Diversity Waveguide Manufacturer value is other than expected.");
        softAssert.assertEquals(endDiversityWaveguideModel, microwaveChannelAttributes.endDiversityWaveguideModel, "End Diversity Waveguide Model value is other than expected.");
        softAssert.assertEquals(startAttenuatorManufacturer, microwaveChannelAttributes.startAttenuatorManufacturer, "Start Attenuator Manufacturer value is other than expected.");
        softAssert.assertEquals(startAttenuatorModel, microwaveChannelAttributes.startAttenuatorModel, "Start Attenuator Model value is other than expected.");
        softAssert.assertEquals(startDiversityWaveguideLength, microwaveChannelAttributes.startDiversityWaveguideLength, "Start Diversity Waveguide Length value is other than expected.");
        softAssert.assertEquals(startDiversityWaveguideManufacturer, microwaveChannelAttributes.startDiversityWaveguideManufacturer, "Start Diversity Waveguide Manufacturer value is other than expected.");
        softAssert.assertEquals(startDiversityWaveguideModel, microwaveChannelAttributes.startDiversityWaveguideModel, "Start Diversity Waveguide Model value is other than expected.");
        softAssert.assertEquals(startDiversityWaveguideTotalLoss, microwaveChannelAttributes.startDiversityWaveguideTotalLoss, "Start Diversity Waveguide Total Loss value is other than expected.");
        softAssert.assertEquals(startRadioModel, microwaveChannelAttributes.startRadioModel, "Start Radio Model value is other than expected.");
        softAssert.assertEquals(startWaveguideManufacturer, microwaveChannelAttributes.startWaveguideManufacturer, "Start Waveguide Manufacturer value is other than expected.");
        softAssert.assertEquals(startWaveguideModel, microwaveChannelAttributes.startWaveguideModel, "Start Waveguide Model value is other than expected.");
        softAssert.assertEquals(startWaveguideTotalLoss, microwaveChannelAttributes.startWaveguideTotalLoss, "Start Waveguide Total Loss value is other than expected.");
        softAssert.assertEquals(description, microwaveChannelAttributes.description, "Description value is other than expected.");
        softAssert.assertEquals(channelNumber, microwaveChannelAttributes.channelNumber, "Channel Number value is other than expected.");
        softAssert.assertEquals(configuration, microwaveChannelAttributes.configuration, "Configuration value is other than expected.");
        softAssert.assertEquals(endAttenuatorLoss, microwaveChannelAttributes.endAttenuatorLoss, "End Attenuator Loss value is other than expected.");
        softAssert.assertEquals(endDiversityWaveguideTotalLoss, microwaveChannelAttributes.endDiversityWaveguideTotalLoss, "End Diversity Waveguide Total Loss value is other than expected.");
        softAssert.assertEquals(endRadioModel, microwaveChannelAttributes.endRadioModel, "End Radio Model value is other than expected.");
        softAssert.assertEquals(endWaveguideLength, microwaveChannelAttributes.endWaveguideLength, "End Waveguide Length value is other than expected.");
        softAssert.assertEquals(endWaveguideManufacturer, microwaveChannelAttributes.endWaveguideManufacturer, "End Waveguide Manufacturer value is other than expected.");
        softAssert.assertEquals(endWaveguideModel, microwaveChannelAttributes.endWaveguideModel, "End Waveguide Model value is other than expected.");
        softAssert.assertEquals(endWaveguideTotalLoss, microwaveChannelAttributes.endWaveguideTotalLoss, "End Waveguide Total Loss value is other than expected.");
        softAssert.assertEquals(startAttenuatorLoss, microwaveChannelAttributes.startAttenuatorLoss, "Start Attenuator Loss value is other than expected.");
        softAssert.assertEquals(startWaveguideLength, microwaveChannelAttributes.startWaveguideLength, "Start Waveguide Length value is other than expected.");
        softAssert.assertEquals(endAttenuatorMode, microwaveChannelAttributes.endAttenuatorMode, "End Attenuator Mode value is other than expected.");
        softAssert.assertEquals(highFrequencySite, microwaveChannelAttributes.highFrequencySite, "High Frequency Site value is other than expected.");
        softAssert.assertEquals(startAttenuatorMode, microwaveChannelAttributes.startAttenuatorMode, "Start Attenuator Mode value is other than expected.");
        softAssert.assertAll();
    }

    private String getConnectionLabel() {
        NetworkViewPage networkViewPage = new NetworkViewPage(driver);
        return networkViewPage.getAttributeValue(LABEL_ATTRIBUTE_LABEL);
    }

    private void openTab(String tabLabel) {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectTabByLabel(tabLabel);
        waitForPageToLoad();
    }

    private void addCardToPhysicalDevice() {
        HierarchyViewPage hierarchyViewPage = HierarchyViewPage.getHierarchyViewPage(driver, webDriverWait);
        hierarchyViewPage.selectFirstObject();
        waitForPageToLoad();
        hierarchyViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_CARD_BUTTON_ID);
        CardCreateWizardPage createWizardPage = new CardCreateWizardPage(driver);
        waitForPageToLoad();
        createWizardPage.setModel(CARD_MODEL_NAME);
        waitForPageToLoad();
        createWizardPage.setSlot(SLOT_NAME);
        waitForPageToLoad();
        createWizardPage.clickAccept();
    }

    private void checkSystemMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals((systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType()), SystemMessageContainer.MessageType.SUCCESS);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void getOrCreateFirstLocations() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationInventoryRepository.getOrCreateLocation(FIRST_LOCATION_NAME, SITE, prepareFirstAddress());
    }

    private Long prepareFirstAddress() {
        AddressRepository addressRepository = new AddressRepository(Environment.getInstance());
        return addressRepository.updateOrCreateAddress(COUNTRY_NAME, POSTAL_CODE_NAME_1, REGION_NAME_1, CITY_NAME_1, DISTRICT_NAME_1);
    }

    private void getOrCreateSecondLocations() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationInventoryRepository.getOrCreateLocation(SECOND_LOCATION_NAME, SITE, prepareSecondAddress());
    }

    private Long prepareSecondAddress() {
        AddressRepository addressRepository = new AddressRepository(Environment.getInstance());
        return addressRepository.updateOrCreateAddress(COUNTRY_NAME, POSTAL_CODE_NAME_2, REGION_NAME_2, CITY_NAME_2, DISTRICT_NAME_2);
    }

    private void clickConfirmationBox(String actionId) {
        ConfirmationBox.create(driver, webDriverWait).clickButtonById(actionId);
    }

    private void getMicrowaveFrequencyPlans() {
        homePage.goToHomePage(driver, BASIC_URL);
        expandTiles(RESOURCE_INVENTORY_CATEGORY_NAME, INVENTORY_VIEW_APPLICATION_NAME);
        waitForPageToLoad();
        SearchObjectTypePage searchObjectType = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectType.searchType(MICROWAVE_FREQUENCY_PLAN_LABEL);
        waitForPageToLoad();
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(MICROWAVE_FREQUENCY_PLAN_NAME);
        waitForPageToLoad();
        boolean isTableEmpty = newInventoryViewPage.checkIfTableIsEmpty();
        SoftAssert assertions = new SoftAssert();
        assertions.assertFalse(isTableEmpty);
        newInventoryViewPage.clearFilters();
        waitForPageToLoad();
        newInventoryViewPage.searchObject(MICROWAVE_FREQUENCY_PLAN2_NAME);
        waitForPageToLoad();
        assertions.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }
}
