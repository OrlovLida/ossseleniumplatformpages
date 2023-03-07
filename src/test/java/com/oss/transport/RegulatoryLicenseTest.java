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
import com.comarch.oss.web.pages.NewInventoryViewPage;
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

    private static final String LOCATION = "SELENIUM_LOCATION1";
    private static final String MICROWAVE_ANTENNA = "SELENIUM_MWANT_RL";
    private static final String MICROWAVE_LINK = "MicrowaveLink 906";
    private static final String MICROWAVE_LINK_ID = "25149092";
    private static final String MICROWAVE_CHANNEL = "MicrowaveChannel 1716";
    private static final String MICROWAVE_CHANNEL_ID = "25149093";

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
    private static final String TAB_ISN_T_EMPTY_EXCEPTION = "Tab isn't empty";
    private static final String ASSIGN_TO_OBJECT = " was not assigned correctly";
    private static final String EQUAL_VALUE_OF = " values aren't equals";
    private static final String ATTRIBUTE_NAME_LICENCE_NUMBER = "licenseNumber";
    private static final String ATTRIBUTE_NAME_STARTING_DATE = "startingDate";
    private static final String ATTRIBUTE_NAME_EXPIRATION_DATE = "expirationDate";
    private static final String ATTRIBUTE_NAME_OPERATING_HOURS = "operatingHours";
    private static final String ATTRIBUTE_NAME_TYPE = "type";
    private static final String ATTRIBUTE_NAME_STATUS = "status";
    private static final String EMPTY_LIST_EXCEPTION = "The list is empty";
    private static final String COLUMN_NAME= "name";
    private static final String COLUMN_LABEL = "label";
    private static final String REGULATORY_LICENSE_VIEW_PATH = "%s/#/views/management/views/inventory-view/RegulatoryLicense?perspective=LIVE";

    private SoftAssert softAssert;
    private NewInventoryViewPage newInventoryViewPage;
    private static final Random rand = new Random();

    private static final int randNumber = rand.nextInt(101) + 100;

    private int timer = 0;

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

        waitForPageToLoad();
        newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.doRefreshWhileNoData();
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        assertRegulatoryLicenseAttributes(newInventoryViewPage, regulatoryLicenseAttributes);
    }

    @Test(priority = 2)
    @Step("Assign Location")
    public void assignLocation() {
        RegulatoryLicenseAssignmentWizardPage regulatoryLicenseAssignmentWizardPage = clickAssign();
        regulatoryLicenseAssignmentWizardPage.assignLocationToRegulatoryLicense(LOCATION);
        openTab(LOCATIONS_TAB_LABEL);
        assertAssignedLocations();
    }

    @Test(priority = 3)
    @Step("Assign Microwave Antenna")
    public void assignMicrowaveAntenna() {
        RegulatoryLicenseAssignmentWizardPage regulatoryLicenseAssignmentWizardPage = clickAssign();
        regulatoryLicenseAssignmentWizardPage.assignMicrowaveAntennaToRegulatoryLicense(MICROWAVE_ANTENNA);
        openTab(MICROWAVE_ANTENNAS_TAB_LABEL);
        assertAssignedMicrowaveAntennas();
    }

    @Test(priority = 4)
    @Step("Assign Microwave Link")
    public void assignMicrowaveLink() {
        RegulatoryLicenseAssignmentWizardPage regulatoryLicenseAssignmentWizardPage = clickAssign();
        regulatoryLicenseAssignmentWizardPage.assignMicrowaveLinkToRegulatoryLicense(MICROWAVE_LINK_ID);
        openTab(MICROWAVE_LINKS_TAB_LABEL);
        assertAssignedMicrowaveLinks();
    }

    @Test(priority = 5)
    @Step("Assign Microwave Channel")
    public void assignMicrowaveChannel() {
        RegulatoryLicenseAssignmentWizardPage regulatoryLicenseAssignmentWizardPage = clickAssign();
        regulatoryLicenseAssignmentWizardPage.assignMicrowaveChannelToRegulatoryLicense(MICROWAVE_CHANNEL_ID);
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
        Assert.assertTrue(checkIfLocationsTabIsEmpty(), TAB_ISN_T_EMPTY_EXCEPTION);
    }

    @Test(priority = 8)
    @Step("Detach assigned Microwave Antenna")
    public void detachMicrowaveAntenna() {
        openTab(MICROWAVE_ANTENNAS_TAB_LABEL);
        detachMicrowaveAntennaFromRegulatoryLicense();
        Assert.assertTrue(checkIfMicrowaveAntennasTabIsEmpty(), TAB_ISN_T_EMPTY_EXCEPTION);
    }

    @Test(priority = 9)
    @Step("Detach assigned Microwave Link")
    public void detachMicrowaveLink() {
        openTab(MICROWAVE_LINKS_TAB_LABEL);
        detachMicrowaveLinkFromRegulatoryLicense();
        Assert.assertTrue(checkIfMicrowaveLinksTabIsEmpty(), TAB_ISN_T_EMPTY_EXCEPTION);
    }

    @Test(priority = 10)
    @Step("Detach assigned Microwave Channel")
    public void detachMicrowaveChannel() {
        openTab(MICROWAVE_CHANNELS_TAB_LABEL);
        detachMicrowaveChannelFromRegulatoryLicense();
        Assert.assertTrue(checkIfMicrowaveChannelsTabIsEmpty(), TAB_ISN_T_EMPTY_EXCEPTION);
    }

    @Test(priority = 11)
    @Step("Remove Regulatory License")
    public void removeRegulatoryLicense() {
        deleteRegulatoryLicense();
        waitForPageToLoad();
        checkRegulatoryLicenseRemoval();
    }

    private void goToRegulatoryLicenseInventoryView() {
        driver.get(String.format(REGULATORY_LICENSE_VIEW_PATH, CONFIGURATION.getUrl()));
        waitForPageToLoad();
    }

    private RegulatoryLicenseAttributes getRegulatoryLicenseAttributesToCreate() {
        RegulatoryLicenseAttributes attributes = new RegulatoryLicenseAttributes();
        attributes.number = NUMBER + randNumber;
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
        regLicAttributes.number = NUMBER2 + randNumber;
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
        waitForPageToLoad();
        return TableWidget.createById(driver, LOCATIONS_TABLE_COMPONENT_ID, webDriverWait);
    }

    private TableWidget getMicrowaveAntennasTable() {
        waitForPageToLoad();
        return TableWidget.createById(driver, MICROWAVE_ANTENNAS_TABLE_COMPONENT_ID, webDriverWait);
    }

    private TableWidget getMicrowaveLinksTable() {
        waitForPageToLoad();
        return TableWidget.createById(driver, MICROWAVE_LINKS_TABLE_COMPONENT_ID, webDriverWait);
    }

    private TableWidget getMicrowaveChannelsTable() {
        waitForPageToLoad();
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

    private void assertRegulatoryLicenseAttributes(NewInventoryViewPage newInventoryViewPage, RegulatoryLicenseAttributes regulatoryLicenseAttributes) {
        String number = newInventoryViewPage.getPropertyPanelValue(ATTRIBUTE_NAME_LICENCE_NUMBER);
        String startingDate = newInventoryViewPage.getPropertyPanelValue(ATTRIBUTE_NAME_STARTING_DATE);
        String expirationDate = newInventoryViewPage.getPropertyPanelValue(ATTRIBUTE_NAME_EXPIRATION_DATE);
        String operatingHours = newInventoryViewPage.getPropertyPanelValue(ATTRIBUTE_NAME_OPERATING_HOURS);
        String type = newInventoryViewPage.getPropertyPanelValue(ATTRIBUTE_NAME_TYPE);
        String status = newInventoryViewPage.getPropertyPanelValue(ATTRIBUTE_NAME_STATUS);

        Assert.assertEquals(number, regulatoryLicenseAttributes.number, ATTRIBUTE_NAME_LICENCE_NUMBER + EQUAL_VALUE_OF);
        Assert.assertEquals(startingDate, regulatoryLicenseAttributes.startingDate, ATTRIBUTE_NAME_STARTING_DATE +  EQUAL_VALUE_OF);
        Assert.assertEquals(expirationDate, regulatoryLicenseAttributes.expirationDate, ATTRIBUTE_NAME_EXPIRATION_DATE +  EQUAL_VALUE_OF);
        Assert.assertEquals(operatingHours, regulatoryLicenseAttributes.operatingHours,ATTRIBUTE_NAME_OPERATING_HOURS +   EQUAL_VALUE_OF);
        Assert.assertEquals(type.toLowerCase(), regulatoryLicenseAttributes.type.toLowerCase(), ATTRIBUTE_NAME_TYPE +  EQUAL_VALUE_OF);
        Assert.assertEquals(status.toLowerCase(), regulatoryLicenseAttributes.status.toLowerCase(), ATTRIBUTE_NAME_STATUS +  EQUAL_VALUE_OF);
    }

    private void openTab(String tabLabel) {
        newInventoryViewPage.selectTabByLabel(tabLabel);
        waitForPageToLoad();
    }

    private void assertAssignedLocations() {
        String assignedLocation = selectObjectInTab(COLUMN_NAME, LOCATIONS_TABLE_COMPONENT_ID);

        Assert.assertEquals(assignedLocation, LOCATION, LOCATION + ASSIGN_TO_OBJECT);
    }

    private void assertAssignedMicrowaveAntennas() {
        String assignedMicrowaveAntenna = selectObjectInTab(COLUMN_NAME, MICROWAVE_ANTENNAS_TABLE_COMPONENT_ID);

        Assert.assertEquals(assignedMicrowaveAntenna, MICROWAVE_ANTENNA, MICROWAVE_ANTENNA + ASSIGN_TO_OBJECT);
    }

    private void assertAssignedMicrowaveLinks() {
        String assignedMicrowaveLink = selectObjectInTab(COLUMN_LABEL, MICROWAVE_LINKS_TABLE_COMPONENT_ID);

        Assert.assertEquals(assignedMicrowaveLink, MICROWAVE_LINK, MICROWAVE_LINK + ASSIGN_TO_OBJECT);
    }

    private void assertAssignedMicrowaveChannels() {
        String assignedMicrowaveChannels = selectObjectInTab(COLUMN_LABEL, MICROWAVE_CHANNELS_TABLE_COMPONENT_ID);

        Assert.assertEquals(assignedMicrowaveChannels, MICROWAVE_CHANNEL, MICROWAVE_CHANNEL + ASSIGN_TO_OBJECT);
    }

    private void checkRegulatoryLicenseRemoval() {
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
    }

    private boolean checkIfLocationsTabIsEmpty() {

        if (!getLocationsTable().hasNoData()) {
            while (!getLocationsTable().hasNoData() && timer < 10) {
                DelayUtils.sleep(1000);
                timer++;
            }
        }
        return true;

    }

    private boolean checkIfMicrowaveAntennasTabIsEmpty() {

        if (!getMicrowaveAntennasTable().hasNoData()) {
            while (!getMicrowaveAntennasTable().hasNoData() && timer < 10) {
                DelayUtils.sleep(1000);
                timer++;
            }
        }
        return true;

    }

    private boolean checkIfMicrowaveLinksTabIsEmpty() {

        if (!getMicrowaveLinksTable().hasNoData()) {
            while (!getMicrowaveLinksTable().hasNoData() && timer < 10) {
                DelayUtils.sleep(1000);
                timer++;
            }
        }
        return true;
    }

    private boolean checkIfMicrowaveChannelsTabIsEmpty() {

        if (!getMicrowaveChannelsTable().hasNoData()) {
            while (!getMicrowaveChannelsTable().hasNoData() && timer < 10) {
                DelayUtils.sleep(1000);
                timer++;
            }
        }
        return true;
    }

    private void checkSystemMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        softAssert.assertEquals((systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException(EMPTY_LIST_EXCEPTION)).getMessageType()), SystemMessageContainer.MessageType.SUCCESS);
    }

    private void detachLocationFromRegulatoryLicense() {
        callActionInLocationsTab();
        newInventoryViewPage.clickConfirmationBox(LOCATION_DETACHMENT_CLOSE_BUTTON_ID);
    }

    private void detachMicrowaveAntennaFromRegulatoryLicense() {
        callActionInMicrowaveAntennasTab();
        newInventoryViewPage.clickConfirmationBox(MICROWAVE_ANTENNA_DETACHMENT_CLOSE_BUTTON_ID);
    }

    private void detachMicrowaveLinkFromRegulatoryLicense() {
        callActionInMicrowaveLinksTab();
        newInventoryViewPage.clickConfirmationBox(MICROWAVE_LINK__DETACHMENT_CLOSE_BUTTON_ID);
    }

    private void detachMicrowaveChannelFromRegulatoryLicense() {
        callActionInMicrowaveChannelsTab();
        newInventoryViewPage.clickConfirmationBox(MICROWAVE_CHANNEL_DETACHMENT_CLOSE_BUTTON_ID);
    }

    private void callActionInLocationsTab() {
        getLocationsTable().callAction(ActionsContainer.EDIT_GROUP_ID, DETACH_LOCATION_FROM_REGULATORY_LICENSE_ACTION_ID);
    }

    private void callActionInMicrowaveAntennasTab() {
        getMicrowaveAntennasTable().callAction(ActionsContainer.EDIT_GROUP_ID, DETACH_MICROWAVE_ANTENNA_FROM_REGULATORY_LICENSE_ACTION_ID);
    }

    private void callActionInMicrowaveLinksTab() {
        getMicrowaveLinksTable().callAction(ActionsContainer.EDIT_GROUP_ID, DETACH_MICROWAVE_LINK_FROM_REGULATORY_LICENSE_ACTION_ID);
    }

    private void callActionInMicrowaveChannelsTab() {
        getMicrowaveChannelsTable().callAction(ActionsContainer.EDIT_GROUP_ID, DETACH_MICROWAVE_CHANNEL_FROM_REGULATORY_LICENSE_ACTION_ID);
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

    private String selectObjectInTab(String column, String componentId) {
        waitForPageToLoad();
        TableComponent tableComponent = TableComponent.create(driver, webDriverWait, componentId);
        tableComponent.selectRow(0);
        return tableComponent.getCellValue(0, column);
    }
}
