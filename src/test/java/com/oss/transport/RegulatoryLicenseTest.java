package com.oss.transport;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.regulatoryLicense.RegulatoryLicenseAssignmentWizardPage;
import com.oss.pages.transport.regulatoryLicense.RegulatoryLicenseWizardPage;

import io.qameta.allure.Step;

import static com.oss.configuration.Configuration.CONFIGURATION;

/**
 * @author Kamil Szota
 */
public class RegulatoryLicenseTest extends BaseTestCase {

    private static final String NUMBER = "Selenium987";
    private static final String REGULATORY_AGENCY = "SELENIUM_AGENCY";
    private static final String STARTING_DATE = "2030-08-12";
    private static final String EXPIRATION_DATE = "2030-09-12";
    private static final String OPERATING_HOURS = "30h";
    private static final String STATUS = "EXPIRED";
    private static final String TYPE = "INTERNATIONAL";
    private static final String DESCRIPTION = "Opis3";

    private static final String LOCATION = "SELENIUM_LOCATION_RL";
    private static final String MICROWAVE_ANTENNA = "SELENIUM_MWANT_RL";
    private static final String MICROWAVE_LINK = "MicrowaveLink 8420";
    private static final String MICROWAVE_CHANNEL = "MicrowaveChannel 26562";

    private static final String NUMBER2 = "234SELENIUM";
    private static final String REGULATORY_AGENCY2 = "SELENIUM_AGENCY2";
    private static final String STARTING_DATE2 = "2041-01-02";
    private static final String EXPIRATION_DATE2 = "2042-03-30";
    private static final String OPERATING_HOURS2 = "48h";
    private static final String STATUS2 = "DELETED";
    private static final String DESCRIPTION2 = "DESC78";

    private static final String LOCATIONS_TAB_LABEL = "Locations";
    private static final String LOCATIONS_TABLE_COMPONENT_ID = "RegulatoryLicenseLocationsWidget";
    private static final String MICROWAVE_ANTENNAS_TAB_LABEL = "Microwave Antennas";
    private static final String MICROWAVE_ANTENNAS_TABLE_COMPONENT_ID = "RegulatoryLicenseAntennasWidget";
    private static final String MICROWAVE_LINKS_TAB_LABEL = "Microwave Links";
    private static final String MICROWAVE_LINKS_TABLE_COMPONENT_ID = "RegulatoryLicenseMicrowaveLinksWidget";
    private static final String MICROWAVE_CHANNELS_TAB_LABEL = "Microwave Channels";
    private static final String MICROWAVE_CHANNELS_TABLE_COMPONENT_ID = "RegulatoryLicenseMicrowaveChannelsWidget";
    private static final String PROPERTIES_TAB_LABEL = "Properties";
    private static final String LOCATION_DETACHMENT_CLOSE_BUTTON_ID = "ConfirmationBox_confirmationBoxAppRemoveLocation_action_button";
    private static final String MICROWAVE_ANTENNA_DETACHMENT_CLOSE_BUTTON_ID = "ConfirmationBox_confirmationBoxAppRemoveLocation_action_button";
    private static final String MICROWAVE_LINK__DETACHMENT_CLOSE_BUTTON_ID = "ConfirmationBox_confirmationBoxAppRemoveLocation_action_button";
    private static final String MICROWAVE_CHANNEL_DETACHMENT_CLOSE_BUTTON_ID = "ConfirmationBox_confirmationBoxAppRemoveLocation_action_button";
    private static final String REGULATORY_LICENSE_REMOVAL_CLOSE_BUTTON_ID = "ConfirmationBox_confirmationBoxApp_close_button";

    private static final String CREATE_REGULATORY_LICENSE_ACTION_ID = "CreateRegulatoryLicenseApplicationContextAction";
    private static final String EDIT_REGULATORY_LICENSE_ACTION_ID = "ModifyRegulatoryLicenseApplicationContextAction";
    private static final String ASSIGN_REGULATORY_LICENSE_TO_OBJECT_ACTION_ID = "AssignToRegulatoryLicenseApplicationContextAction";
    private static final String DETACH_LOCATION_FROM_REGULATORY_LICENSE_ACTION_ID = "DetachLocationFromRegulatoryLicenseApplicationContextAction";
    private static final String DETACH_MICROWAVE_ANTENNA_FROM_REGULATORY_LICENSE_ACTION_ID = "DetachAntennaFromRegulatoryLicenseApplicationContextAction";
    private static final String DETACH_MICROWAVE_LINK_FROM_REGULATORY_LICENSE_ACTION_ID = "DetachMicrowaveLinkFromRegulatoryLicenseApplicationContextAction";
    private static final String DETACH_MICROWAVE_CHANNEL_FROM_REGULATORY_LICENSE_ACTION_ID = "DetachMicrowaveChannelFromRegulatoryLicenseApplicationContextAction";
    private static final String DELETE_REGULATORY_LICENSE_ACTION_ID = "DeleteRegulatoryLicenseApplicationContextAction";

    private static final String LOCATION_NAME_FIELD_ID = "assign-regulatory-license-location-field_OSF";
    private static final String MICROWAVE_ANTENNA_NAME_FIELD_ID = "assign-regulatory-license-antenna-field_OSF";
    private static final String MICROWAVE_LINK_NAME_FIELD_ID = "assign-regulatory-license-microwave-link-field_OSF";
    private static final String MICROWAVE_CHANNEL_NAME_FIELD_ID = "assign-regulatory-license-microwave-channel-field_OSF";

    private SoftAssert softAssert;
    private NewInventoryViewPage newInventoryViewPage;
    private static Random rand = new Random();

    @BeforeClass
    public void openConsole() {
        softAssert = new SoftAssert();
        waitForPageToLoad();
    }

    @Test(priority = 1)
    @Step("Create Regulatory License")
    public void createRegulatoryLicense() {
        goToRegulatoryLicenseInventoryView();
        RegulatoryLicenseAttributes regulatoryLicenseAttributes = getRegulatoryLicenseAttributesToCreate();
        newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        RegulatoryLicenseWizardPage regulatoryLicenseWizard = clickCreate();

        goThroughWizard(regulatoryLicenseWizard, regulatoryLicenseAttributes);
        regulatoryLicenseWizard.clickAccept();
        checkSystemMessage();
        SystemMessageContainer.create(driver, webDriverWait).clickMessageLink();

        newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        assertRegulatoryLicenseAttributes(newInventoryViewPage, regulatoryLicenseAttributes);
    }

    @Test(priority = 2)
    @Step("Assign Location")
    public void assignLocation() {
        clickAssign();
        assignObjectToRegulatoryLicense("Location", LOCATION, LOCATION_NAME_FIELD_ID);
        openTab(LOCATIONS_TAB_LABEL);
        assertAssignedLocations();
    }

    @Test(priority = 3)
    @Step("Assign Microwave Antenna")
    public void assignMicrowaveAntenna() {
        clickAssign();
        assignObjectToRegulatoryLicense("Microwave Antenna", MICROWAVE_ANTENNA, MICROWAVE_ANTENNA_NAME_FIELD_ID);
        openTab(MICROWAVE_ANTENNAS_TAB_LABEL);
        assertAssignedMicrowaveAntennas();
    }

    @Test(priority = 4)
    @Step("Assign Microwave Link")
    public void assignMicrowaveLink() {
        clickAssign();
        assignObjectToRegulatoryLicense("Microwave Link", MICROWAVE_LINK, MICROWAVE_LINK_NAME_FIELD_ID);
        openTab(MICROWAVE_LINKS_TAB_LABEL);
        assertAssignedMicrowaveLinks();
    }

    @Test(priority = 5)
    @Step("Assign Microwave Channel")
    public void assignMicrowaveChannel() {
        clickAssign();
        assignObjectToRegulatoryLicense("Microwave Channel", MICROWAVE_CHANNEL, MICROWAVE_CHANNEL_NAME_FIELD_ID);
        openTab(MICROWAVE_CHANNELS_TAB_LABEL);
        assertAssignedMicrowaveChannels();
    }

    @Test(priority = 6)
    @Step("Update Regulatory License attributes")
    public void updateRegulatoryLicense() {
        RegulatoryLicenseAttributes regulatoryLicenseAttributes = getRegulatoryLicenseAttributesToUpdate();
        newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        RegulatoryLicenseWizardPage regulatoryLicenseWizard = clickEdit();

        goThroughWizard(regulatoryLicenseWizard, regulatoryLicenseAttributes);
        regulatoryLicenseWizard.clickAccept();
        checkSystemMessage();

        openTab(PROPERTIES_TAB_LABEL);
        assertRegulatoryLicenseAttributes(newInventoryViewPage, regulatoryLicenseAttributes);
    }

    @Test(priority = 7)
    @Step("Detach assigned Location")
    public void detachLocation() {
        openTab(LOCATIONS_TAB_LABEL);
        detachLocationFromRegulatoryLicense();
        checkIfLocationsTabIsEmpty();
    }

    @Test(priority = 8)
    @Step("Detach assigned Microwave Antenna")
    public void detachMicrowaveAntenna() {
        openTab(MICROWAVE_ANTENNAS_TAB_LABEL);
        detachMicrowaveAntennaFromRegulatoryLicense();
        checkIfMicrowaveAntennasTabIsEmpty();
    }

    @Test(priority = 9)
    @Step("Detach assigned Microwave Link")
    public void detachMicrowaveLink() {
        openTab(MICROWAVE_LINKS_TAB_LABEL);
        detachMicrowaveLinkFromRegulatoryLicense();
        checkIfMicrowaveLinksTabIsEmpty();
    }

    @Test(priority = 10)
    @Step("Detach assigned Microwave Channel")
    public void detachMicrowaveChannel() {
        openTab(MICROWAVE_CHANNELS_TAB_LABEL);
        detachMicrowaveChannelFromRegulatoryLicense();
        checkIfMicrowaveChannelsTabIsEmpty();
    }

    @Test(priority = 10)
    @Step("Remove Regulatory License")
    public void removeRegulatoryLicense() {
        deleteRegulatoryLicense();
        checkRegulatoryLicenseRemoval();
    }

    public void goToRegulatoryLicenseInventoryView() {
        driver.get(String.format("%s/#/views/management/views/inventory-view/RegulatoryLicense?perspective=LIVE", CONFIGURATION.getUrl()));
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private RegulatoryLicenseAttributes getRegulatoryLicenseAttributesToCreate() {
        RegulatoryLicenseAttributes attributes = new RegulatoryLicenseAttributes();
        attributes.number = NUMBER + rand.nextInt(99 + 1) * 100;
        attributes.regulatoryAgency = REGULATORY_AGENCY;
        attributes.startingDate = STARTING_DATE;
        attributes.expirationDate = EXPIRATION_DATE;
        attributes.operatingHours = OPERATING_HOURS;
        attributes.status = STATUS;
        attributes.type = TYPE;
        attributes.description = DESCRIPTION;
        return attributes;
    }

    private RegulatoryLicenseAttributes getRegulatoryLicenseAttributesToUpdate() {
        RegulatoryLicenseAttributes regLicAttributes = new RegulatoryLicenseAttributes();
        regLicAttributes.number = NUMBER2 + rand.nextInt(99 + 1) * 100;
        regLicAttributes.regulatoryAgency = REGULATORY_AGENCY2;
        regLicAttributes.startingDate = STARTING_DATE2;
        regLicAttributes.expirationDate = EXPIRATION_DATE2;
        regLicAttributes.operatingHours = OPERATING_HOURS2;
        regLicAttributes.status = STATUS2;
        regLicAttributes.type = TYPE;
        regLicAttributes.description = DESCRIPTION2;
        return regLicAttributes;
    }

    private TableWidget getLocationsTable() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return TableWidget.createById(driver, LOCATIONS_TABLE_COMPONENT_ID, webDriverWait);
    }

    private TableWidget getMicrowaveAntennasTable() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return TableWidget.createById(driver, MICROWAVE_ANTENNAS_TABLE_COMPONENT_ID, webDriverWait);
    }

    private TableWidget getMicrowaveLinksTable() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return TableWidget.createById(driver, MICROWAVE_LINKS_TABLE_COMPONENT_ID, webDriverWait);
    }

    private TableWidget getMicrowaveChannelsTable() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return TableWidget.createById(driver, MICROWAVE_CHANNELS_TABLE_COMPONENT_ID, webDriverWait);
    }

    @Step("Click create button")
    private RegulatoryLicenseWizardPage clickCreate() {
        newInventoryViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_REGULATORY_LICENSE_ACTION_ID);
        return new RegulatoryLicenseWizardPage(driver);
    }

    @Step("Click edit button")
    private RegulatoryLicenseWizardPage clickEdit() {
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, EDIT_REGULATORY_LICENSE_ACTION_ID);
        return new RegulatoryLicenseWizardPage(driver);
    }

    @Step("Click assign object button")
    private RegulatoryLicenseAssignmentWizardPage clickAssign() {
        newInventoryViewPage.callAction(ActionsContainer.ASSIGN_GROUP_ID, ASSIGN_REGULATORY_LICENSE_TO_OBJECT_ACTION_ID);
        return new RegulatoryLicenseAssignmentWizardPage(driver);
    }

    @Step("Click detach object button")
    private RegulatoryLicenseAssignmentWizardPage clickDetach(String objectType) {
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, objectType);
        waitForPageToLoad();
        newInventoryViewPage.clickConfirmationRemovalButton();
        return new RegulatoryLicenseAssignmentWizardPage(driver);
    }

    private void goThroughWizard(RegulatoryLicenseWizardPage regulatoryLicenseWizardPage, RegulatoryLicenseAttributes regulatoryLicenseAttributes) {
        fillRegulatoryLicenseWizard(regulatoryLicenseWizardPage, regulatoryLicenseAttributes);
    }

    private void fillRegulatoryLicenseWizard(RegulatoryLicenseWizardPage regulatoryLicenseWizardPage, RegulatoryLicenseAttributes regulatoryLicenseAttributes) {
        regulatoryLicenseWizardPage.setNumber(regulatoryLicenseAttributes.number);
        regulatoryLicenseWizardPage.setRegulatoryAgency(regulatoryLicenseAttributes.regulatoryAgency);
        regulatoryLicenseWizardPage.setStartingDate(regulatoryLicenseAttributes.startingDate);
        regulatoryLicenseWizardPage.setExpirationDate(regulatoryLicenseAttributes.expirationDate);
        regulatoryLicenseWizardPage.setOperatingHours(regulatoryLicenseAttributes.operatingHours);
        regulatoryLicenseWizardPage.setStatus(regulatoryLicenseAttributes.status);
        regulatoryLicenseWizardPage.setType(regulatoryLicenseAttributes.type);
        regulatoryLicenseWizardPage.setDescription(regulatoryLicenseAttributes.description);
    }

    private void assignObjectToRegulatoryLicense(String objectType, String objectName, String objectNameFieldId) {
        RegulatoryLicenseAssignmentWizardPage regulatoryLicenseAssignmentWizard = new RegulatoryLicenseAssignmentWizardPage(driver);
        regulatoryLicenseAssignmentWizard.setObjectType(objectType);
        waitForPageToLoad();
        regulatoryLicenseAssignmentWizard.setObjectName(objectName, objectNameFieldId);
        waitForPageToLoad();
        regulatoryLicenseAssignmentWizard.clickAccept();
        waitForPageToLoad();
    }

    private void assertRegulatoryLicenseAttributes(NewInventoryViewPage newInventoryViewPage, RegulatoryLicenseAttributes regulatoryLicenseAttributes) {
        String number = newInventoryViewPage.getPropertyPanelValue("licenseNumber");
        String startingDate = newInventoryViewPage.getPropertyPanelValue("startingDate");
        String expirationDate = newInventoryViewPage.getPropertyPanelValue("expirationDate");
        String operatingHours = newInventoryViewPage.getPropertyPanelValue("operatingHours");
        String type = newInventoryViewPage.getPropertyPanelValue("type");
        String status = newInventoryViewPage.getPropertyPanelValue("status");

        Assert.assertEquals(number, regulatoryLicenseAttributes.number);
        Assert.assertEquals(startingDate, regulatoryLicenseAttributes.startingDate);
        Assert.assertEquals(expirationDate, regulatoryLicenseAttributes.expirationDate);
        Assert.assertEquals(operatingHours, regulatoryLicenseAttributes.operatingHours);
        Assert.assertEquals(type.toLowerCase(), regulatoryLicenseAttributes.type.toLowerCase());
        Assert.assertEquals(status.toLowerCase(), regulatoryLicenseAttributes.status.toLowerCase());
    }

    private void openTab(String tabLabel) {
        newInventoryViewPage.selectTabByLabel(tabLabel);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void assertAssignedLocations() {
        String assignedLocation = selectObjectInTab(0, "name", LOCATIONS_TABLE_COMPONENT_ID);

        Assert.assertEquals(assignedLocation, LOCATION);
    }

    private void assertAssignedMicrowaveAntennas() {
        String assignedMicrowaveAntenna = selectObjectInTab(0, "name", MICROWAVE_ANTENNAS_TABLE_COMPONENT_ID);

        Assert.assertEquals(assignedMicrowaveAntenna, MICROWAVE_ANTENNA);
    }

    private void assertAssignedMicrowaveLinks() {
        String assignedMicrowaveLink = selectObjectInTab(0, "label", MICROWAVE_LINKS_TABLE_COMPONENT_ID);

        Assert.assertEquals(assignedMicrowaveLink, MICROWAVE_LINK);
    }

    private void assertAssignedMicrowaveChannels() {
        String assignedMicrowaveChannels = selectObjectInTab(0, "label", MICROWAVE_CHANNELS_TABLE_COMPONENT_ID);

        Assert.assertEquals(assignedMicrowaveChannels, MICROWAVE_CHANNEL);
    }

    private void checkRegulatoryLicenseRemoval() {
        newInventoryViewPage.checkIfTableIsEmpty();
    }

    private boolean checkIfLocationsTabIsEmpty() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return getLocationsTable().hasNoData();
    }

    private boolean checkIfMicrowaveAntennasTabIsEmpty() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return getMicrowaveAntennasTable().hasNoData();
    }

    private boolean checkIfMicrowaveLinksTabIsEmpty() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return getMicrowaveLinksTable().hasNoData();
    }

    private boolean checkIfMicrowaveChannelsTabIsEmpty() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return getMicrowaveChannelsTable().hasNoData();
    }

    private void checkSystemMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        softAssert.assertEquals((systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType()), SystemMessageContainer.MessageType.SUCCESS);
    }

    private void detachLocationFromRegulatoryLicense() {
        callActionInLocationsTab(ActionsContainer.EDIT_GROUP_ID, DETACH_LOCATION_FROM_REGULATORY_LICENSE_ACTION_ID);
        newInventoryViewPage.clickConfirmationBox(LOCATION_DETACHMENT_CLOSE_BUTTON_ID);
    }

    private void detachMicrowaveAntennaFromRegulatoryLicense() {
        callActionInMicrowaveAntennasTab(ActionsContainer.EDIT_GROUP_ID, DETACH_MICROWAVE_ANTENNA_FROM_REGULATORY_LICENSE_ACTION_ID);
        newInventoryViewPage.clickConfirmationBox(MICROWAVE_ANTENNA_DETACHMENT_CLOSE_BUTTON_ID);
    }

    private void detachMicrowaveLinkFromRegulatoryLicense() {
        callActionInMicrowaveLinksTab(ActionsContainer.EDIT_GROUP_ID, DETACH_MICROWAVE_LINK_FROM_REGULATORY_LICENSE_ACTION_ID);
        newInventoryViewPage.clickConfirmationBox(MICROWAVE_LINK__DETACHMENT_CLOSE_BUTTON_ID);
    }

    private void detachMicrowaveChannelFromRegulatoryLicense() {
        callActionInMicrowaveChannelsTab(ActionsContainer.EDIT_GROUP_ID, DETACH_MICROWAVE_CHANNEL_FROM_REGULATORY_LICENSE_ACTION_ID);
        newInventoryViewPage.clickConfirmationBox(MICROWAVE_CHANNEL_DETACHMENT_CLOSE_BUTTON_ID);
    }

    private NewInventoryViewPage callActionInLocationsTab(String groupId, String actionId) {
        getLocationsTable().callAction(groupId, actionId);
        return new NewInventoryViewPage(driver, webDriverWait);
    }

    private NewInventoryViewPage callActionInMicrowaveAntennasTab(String groupId, String actionId) {
        getMicrowaveAntennasTable().callAction(groupId, actionId);
        return new NewInventoryViewPage(driver, webDriverWait);
    }

    private NewInventoryViewPage callActionInMicrowaveLinksTab(String groupId, String actionId) {
        getMicrowaveLinksTable().callAction(groupId, actionId);
        return new NewInventoryViewPage(driver, webDriverWait);
    }

    private NewInventoryViewPage callActionInMicrowaveChannelsTab(String groupId, String actionId) {
        getMicrowaveChannelsTable().callAction(groupId, actionId);
        return new NewInventoryViewPage(driver, webDriverWait);
    }

    @Step("Delete Regulatory License")
    private void deleteRegulatoryLicense() {
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_REGULATORY_LICENSE_ACTION_ID);
        waitForPageToLoad();
        newInventoryViewPage.clickConfirmationBox(REGULATORY_LICENSE_REMOVAL_CLOSE_BUTTON_ID);
        waitForPageToLoad();
    }

    private static class RegulatoryLicenseAttributes {
        private String number;
        private String regulatoryAgency;
        private String startingDate;
        private String expirationDate;
        private String operatingHours;
        private String status;
        private String type;
        private String description;
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private String selectObjectInTab(Integer index, String column, String componentId) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TableComponent tableComponent = TableComponent.create(driver, webDriverWait, componentId);
        tableComponent.selectRow(index);
        return tableComponent.getCellValue(index, column);
    }
}
