package com.oss.pages.transport.regulatoryLicense;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class RegulatoryLicenseOverviewPage extends BasePage {

    private static final String NUMBER = "Number";
    private static final String STATUS = "Status";
    private static final String STARTING_DATE = "Starting Date";
    private static final String EXPIRATION_DATE = "Expiration Date";
    private static final String OPERATING_HOURS = "Operating Hours";
    private static final String REGULATORY_AGENCY = "Regulatory Agency";
    private static final String TYPE = "Type";
    private static final String DESCRIPTION = "Description";

    private static final String BOTTOM_TABLE_LOCATIONS_LABEL = "Locations";
    private static final String BOTTOM_TABLE_LOCATION_COLUMN_LABEL = "Name";
    private static final String BOTTOM_TABLE_MICROWAVE_ANTENNAS_LABEL = "Microwave Antennas";
    private static final String BOTTOM_TABLE_MICROWAVE_ANTENNA_COLUMN_LABEL = "Name";
    private static final String BOTTOM_TABLE_MICROWAVE_CHANNELS_LABEL = "Microwave Channels";
    private static final String BOTTOM_TABLE_MICROWAVE_CHANNEL_COLUMN_LABEL = "Label";
    private static final String BOTTOM_TABLE_MICROWAVE_LINKS_LABEL = "Microwave Links";
    private static final String BOTTOM_TABLE_MICROWAVE_LINK_COLUMN_LABEL = "Label";
    private static final String BOTTOM_LOCATIONS_TABLE_ID = "locationsTab";
    private static final String BOTTOM_MICROWAVE_ANTENNAS_TABLE_ID = "antenasTab";
    private static final String BOTTOM_MICROWAVE_CHANNELS_TABLE_ID = "microwaveChannelsTab";
    private static final String BOTTOM_MICROWAVE_LINKS_TABLE_ID = "microwaveLinksTab";

    private static final String ADD_BUTTON_DATA_ATTRIBUTENAME = "create";
    private static final String EDIT_BUTTON_DATA_ATTRIBUTENAME = "licenseEdit-0";
    private static final String REMOVE_REGULATORY_LICENSE_BUTTON_DATA_ATTRIBUTENAME = "licenseEdit-1";
    private static final String REGULATORY_LICENSE_REMOVAL_CONFIRMATION_BUTTON_DATA_ATTRIBUTENAME = "ConfirmationBox_confirmationBoxApp_close_button";

    private static final String BOTTOM_MICROWAVE_LINKS_TABLE_DATA_ATTRIBUTENAME = "microwaveLinksTab";
    private static final String REMOVE_MICROWAVE_LINK_BUTTON_DATA_ATTRIBUTENAME = "remove";

    private static final String BOTTOM_MICROWAVE_CHANNELS_TABLE_DATA_ATTRIBUTENAME = "microwaveChannelsTab";
    private static final String REMOVE_MICROWAVE_CHANNEL_BUTTON_DATA_ATTRIBUTENAME = "remove";

    private static final String BOTTOM_MICROWAVE_ANTENNAS_TABLE_DATA_ATTRIBUTENAME = "antenasTab";
    private static final String REMOVE_MICROWAVE_ANTENNA_BUTTON_DATA_ATTRIBUTENAME = "remove";

    private static final String REMOVAL_CONFIRMATION_BUTTON_DATA_ATTRIBUTENAME = "ConfirmationBox_confirmationBoxAppRemoveLocation_action_button";

    private final OldPropertyPanel propertyPanel;

    public RegulatoryLicenseOverviewPage(WebDriver driver) {
        super(driver);
        propertyPanel = OldPropertyPanel.create(driver, wait);
    }

    @Step("Click edit button")
    public RegulatoryLicenseWizardPage clickEdit() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button editButton = Button.createBySelectorAndId(driver, "a", EDIT_BUTTON_DATA_ATTRIBUTENAME);
        editButton.click();
        return new RegulatoryLicenseWizardPage(driver);
    }

    @Step("Click remove button and confirm removal")
    public void removeRegulatoryLicense() {
        clickRemoveRegulatoryLicense();
        confirmRegulatoryLicenseRemoval();
    }

    private void clickRemoveRegulatoryLicense() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button removeButton = Button.createBySelectorAndId(driver, "a", REMOVE_REGULATORY_LICENSE_BUTTON_DATA_ATTRIBUTENAME);
        removeButton.click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void confirmRegulatoryLicenseRemoval() {
        Button confirmationButton = Button.createById(driver, REGULATORY_LICENSE_REMOVAL_CONFIRMATION_BUTTON_DATA_ATTRIBUTENAME);
        confirmationButton.click();
    }

    public String getNumberValue() {
        return getAttributeValue(NUMBER);
    }

    public String getStatusValue() {
        return getAttributeValue(STATUS);
    }

    public String getStartingDateValue() {
        return getAttributeValue(STARTING_DATE);
    }

    public String getExpirationDateValue() {
        return getAttributeValue(EXPIRATION_DATE);
    }

    public String getOperatingHoursValue() {
        return getAttributeValue(OPERATING_HOURS);
    }

    public String getRegulatoryAgencyValue() {
        return getAttributeValue(REGULATORY_AGENCY);
    }

    public String getTypeValue() {
        return getAttributeValue(TYPE);
    }

    public String getDescriptionValue() {
        return getAttributeValue(DESCRIPTION);
    }

    private String getAttributeValue(String attributeName) {
        return propertyPanel.getPropertyValue(attributeName);
    }

    @Step("Open bottom Locations tab")
    public void openLocationsTab() {
        selectTab(BOTTOM_TABLE_LOCATIONS_LABEL);
    }

    @Step("Open bottom Microwave Antennas tab")
    public void openMicrowaveAntennasTab() {
        selectTab(BOTTOM_TABLE_MICROWAVE_ANTENNAS_LABEL);
    }

    @Step("Open bottom Microwave Channels tab")
    public void openMicrowaveChannelsTab() {
        selectTab(BOTTOM_TABLE_MICROWAVE_CHANNELS_LABEL);
    }

    @Step("Open bottom Microwave Links tab")
    public void openMicrowaveLinksTab() {
        selectTab(BOTTOM_TABLE_MICROWAVE_LINKS_LABEL);
    }

    @Step("Click add Location button")
    public RegulatoryLicenseLocationsWizardPage clickAddLocationButton() {
        openLocationsTab();
        addObjectInSpecificTable(BOTTOM_LOCATIONS_TABLE_ID);
        return new RegulatoryLicenseLocationsWizardPage(driver);
    }

    @Step("Click add Microwave Antenna button")
    public RegulatoryLicenseMicrowaveAntennasWizardPage clickAddMicrowaveAntennaButton() {
        openMicrowaveAntennasTab();
        addObjectInSpecificTable(BOTTOM_MICROWAVE_ANTENNAS_TABLE_ID);
        return new RegulatoryLicenseMicrowaveAntennasWizardPage(driver);
    }

    @Step("Click add Microwave Channel button")
    public RegulatoryLicenseMicrowaveChannelsWizardPage clickAddMicrowaveChannelButton() {
        openMicrowaveChannelsTab();
        addObjectInSpecificTable(BOTTOM_MICROWAVE_CHANNELS_TABLE_ID);
        return new RegulatoryLicenseMicrowaveChannelsWizardPage(driver);
    }

    @Step("Click add Microwave Link button")
    public RegulatoryLicenseMicrowaveLinksWizardPage clickAddMicrowaveLinkButton() {
        openMicrowaveLinksTab();
        addObjectInSpecificTable(BOTTOM_MICROWAVE_LINKS_TABLE_ID);
        return new RegulatoryLicenseMicrowaveLinksWizardPage(driver);
    }

    private OldTable getTableWidget(String tableId) {
        return OldTable.createByComponentDataAttributeName(driver, wait, tableId);
    }

    @Step("Get all assigned Locations values")
    public List<String> getAssignedLocations() {
        openLocationsTab();
        OldTable locationsTabTable = getTableWidget(BOTTOM_LOCATIONS_TABLE_ID);
        return getAllElementsInColumn(locationsTabTable, BOTTOM_TABLE_LOCATION_COLUMN_LABEL);
    }


    @Step("Get all assigned Microwave Antennas values")
    public List<String> getAssignedMicrowaveAntennas() {
        openMicrowaveAntennasTab();
        OldTable microwaveAntennasTabTable = getTableWidget(BOTTOM_MICROWAVE_ANTENNAS_TABLE_ID);
        return getAllElementsInColumn(microwaveAntennasTabTable, BOTTOM_TABLE_MICROWAVE_ANTENNA_COLUMN_LABEL);
    }

    @Step("Get all assigned Microwave Channels values")
    public List<String> getAssignedMicrowaveChannels() {
        openMicrowaveChannelsTab();
        OldTable microwaveChannelsTabTable = getTableWidget(BOTTOM_MICROWAVE_CHANNELS_TABLE_ID);
        return getAllElementsInColumn(microwaveChannelsTabTable, BOTTOM_TABLE_MICROWAVE_CHANNEL_COLUMN_LABEL);
    }

    @Step("Get all assigned Microwave Links values")
    public List<String> getAssignedMicrowaveLinks() {
        openMicrowaveLinksTab();
        OldTable microwaveLinksTabTable = getTableWidget(BOTTOM_MICROWAVE_LINKS_TABLE_ID);
        return getAllElementsInColumn(microwaveLinksTabTable, BOTTOM_TABLE_MICROWAVE_LINK_COLUMN_LABEL);
    }

    @Step("Select first Microwave Link, click remove and confirm removal")
    public void removeFirstMicrowaveLink() {
        selectFirstMicrowaveLink();
        OldTable microwaveLinkTable = getTableWidget(BOTTOM_MICROWAVE_LINKS_TABLE_DATA_ATTRIBUTENAME);
        microwaveLinkTable.callAction(REMOVE_MICROWAVE_LINK_BUTTON_DATA_ATTRIBUTENAME);
        DelayUtils.waitForPageToLoad(driver, wait);
        confirmMicrowaveLinkRemoval();
    }

    @Step("Select first Microwave Channel, click remove and confirm removal")
    public void removeFirstMicrowaveChannel() {
        selectFirstMicrowaveChannel();
        OldTable microwaveLinkTable = getTableWidget(BOTTOM_MICROWAVE_CHANNELS_TABLE_DATA_ATTRIBUTENAME);
        microwaveLinkTable.callAction(REMOVE_MICROWAVE_CHANNEL_BUTTON_DATA_ATTRIBUTENAME);
        DelayUtils.waitForPageToLoad(driver, wait);
        confirmMicrowaveChannelRemoval();
    }

    @Step("Select first Microwave Antenna, click remove and confirm removal")
    public void removeFirstMicrowaveAntenna() {
        selectFirstMicrowaveAntenna();
        OldTable microwaveLinkTable = getTableWidget(BOTTOM_MICROWAVE_ANTENNAS_TABLE_DATA_ATTRIBUTENAME);
        microwaveLinkTable.callAction(REMOVE_MICROWAVE_ANTENNA_BUTTON_DATA_ATTRIBUTENAME);
        DelayUtils.waitForPageToLoad(driver, wait);
        confirmMicrowaveAntennaRemoval();
    }

    public void selectTab(String tabName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabByLabel(tabName);
    }

    private void addObjectInSpecificTable(String tableId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable tabTable = getTableWidget(tableId);
        tabTable.callAction(ADD_BUTTON_DATA_ATTRIBUTENAME);
    }

    private void selectFirstMicrowaveLink() {
        OldTable table = OldTable.createByComponentDataAttributeName(driver, wait, BOTTOM_MICROWAVE_LINKS_TABLE_DATA_ATTRIBUTENAME);
        table.selectRowByAttributeValueWithLabel("Technology Type", "HYB");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void confirmMicrowaveLinkRemoval() {
        Button confirmationButton = Button.createById(driver, REMOVAL_CONFIRMATION_BUTTON_DATA_ATTRIBUTENAME);
        confirmationButton.click();
    }

    private void selectFirstMicrowaveChannel() {
        OldTable table = OldTable.createByComponentId(driver, wait, BOTTOM_MICROWAVE_CHANNELS_TABLE_DATA_ATTRIBUTENAME);
        table.selectRowByAttributeValueWithLabel("Type", "MicrowaveChannel");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void confirmMicrowaveChannelRemoval() {
        Button confirmationButton = Button.createById(driver, REMOVAL_CONFIRMATION_BUTTON_DATA_ATTRIBUTENAME);
        confirmationButton.click();
    }

    private void selectFirstMicrowaveAntenna() {
        OldTable table = OldTable.createByComponentId(driver, wait, BOTTOM_MICROWAVE_ANTENNAS_TABLE_DATA_ATTRIBUTENAME);
        table.selectRowByAttributeValueWithLabel("Manufacturer", "ARTA");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void confirmMicrowaveAntennaRemoval() {
        Button confirmationButton = Button.createById(driver, REMOVAL_CONFIRMATION_BUTTON_DATA_ATTRIBUTENAME);
        confirmationButton.click();
    }

    private List<String> getAllElementsInColumn(OldTable table, String columnName) {
        int rows = table.getNumberOfRowsInTable(columnName);
        List<String> elementsInColumn = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            elementsInColumn.add(table.getCellValue(i, columnName));
        }
        return elementsInColumn;
    }
}