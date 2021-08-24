package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.configuration.Configuration;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.transport.regulatoryLicense.*;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kamil Szota
 */
public class RegulatoryLicenseTest extends BaseTestCase {

    private static final String NUMBER = "Selenium987";
    private static final String REGULATORY_AGENCY = "SELENIUM_AGENCY";
    private static final String STARTING_DATE = "2030-08-12";
    private static final String EXPIRATION_DATE = "2030-09-12";
    private static final String OPERATING_HOURS = "30h";
    private static final String STATUS = "REQUESTED";
    private static final String TYPE = "INTERNATIONAL";
    private static final String DESCRIPTION = "Opis3";

    private static final String LOCATION = "SELENIUM_LOCATION_RL";
    private static final String MICROWAVE_ANTENNA = "SELENIUM_MWANT_RL";
    private static final String MICROWAVE_LINK = "SELENIUM_LOCATION_RL - SELENIUM_LOCATION2_RL 242";
    private static final String MICROWAVE_LINK_LABEL = "MicrowaveLink 2911";
    private static final String MICROWAVE_CHANNEL = "29 MHz  (Working) (43501422)";
    private static final String MICROWAVE_CHANNEL_LABEL = "MicrowaveChannel 9376";

    private static final String NUMBER2 = "234SELENIUM";
    private static final String REGULATORY_AGENCY2 = "SELENIUM_AGENCY2";
    private static final String STARTING_DATE2 = "2041-01-02";
    private static final String EXPIRATION_DATE2 = "2042-03-30";
    private static final String OPERATING_HOURS2 = "48h";
    private static final String STATUS2 = "DELETED";
    private static final String TYPE2 = "NATIONAL";
    private static final String DESCRIPTION2 = "DESC78";

    private static final String ENVIRONMENT_INDEPENDENT_URL_PART_AFTER_REMOVAL_REDIRECT = "/#/view/transport/microwave/licenses?perspective=LIVE&withRemoved=false";

    @Test(priority = 1)
    @Step("Create Regulatory License")
    public void createRegulatoryLicense(){
        RegulatoryLicenseAttributes regulatoryLicenseAttributes = getRegulatoryLicenseAttributesToCreate();

        RegulatoryLicenseWizardPage regulatoryLicenseWizard = goToRegulatoryLicenseWizard();
        regulatoryLicenseWizard.clickCreate();
        fillRegulatoryLicenseWizardToCreate(regulatoryLicenseAttributes, regulatoryLicenseWizard);
        regulatoryLicenseWizard.clickAccept();
        regulatoryLicenseWizard.openRegulatoryLicenseOverview();

        assertRegulatoryLicenseAttributes(regulatoryLicenseAttributes, new RegulatoryLicenseOverviewPage(driver));
    }

    @Test(priority = 2)
    @Step("Assign Location")
    public void addLocation() {
        RegulatoryLicenseOverviewPage rlOverviewBeforeAssign = new RegulatoryLicenseOverviewPage(driver);
        rlOverviewBeforeAssign.openLocationsTab();
        RegulatoryLicenseLocationsWizardPage locationsWizard = rlOverviewBeforeAssign.clickAddLocationButton();
        assignLocation(locationsWizard);
        RegulatoryLicenseOverviewPage regulatoryLicenseOverviewAfterLocationAssignment = locationsWizard.clickAccept();

        assertAssignedLocations(regulatoryLicenseOverviewAfterLocationAssignment, LOCATION);
    }

    @Test(priority = 3)
    @Step("Assign Microwave Antenna")
    public void addMicrowaveAntenna() {
        RegulatoryLicenseOverviewPage rlOverviewBeforeAssign = new RegulatoryLicenseOverviewPage(driver);
        rlOverviewBeforeAssign.openMicrowaveAntennasTab();
        RegulatoryLicenseMicrowaveAntennasWizardPage microwaveAntennasWizard = rlOverviewBeforeAssign.clickAddMicrowaveAntennaButton();
        assignMicrowaveAntenna(microwaveAntennasWizard);
        RegulatoryLicenseOverviewPage regulatoryLicenseOverviewAfterMicrowaveAntennaAssignment = microwaveAntennasWizard.clickAccept();

        assertAssignedMicrowaveAntennas(regulatoryLicenseOverviewAfterMicrowaveAntennaAssignment, MICROWAVE_ANTENNA);
    }

    @Test(priority = 4)
    @Step("Assign Microwave Channel")
    public void addMicrowaveChannel() {
        RegulatoryLicenseOverviewPage rlOverviewBeforeAssign = new RegulatoryLicenseOverviewPage(driver);
        rlOverviewBeforeAssign.openMicrowaveChannelsTab();
        RegulatoryLicenseMicrowaveChannelsWizardPage microwaveChannelsWizard = rlOverviewBeforeAssign.clickAddMicrowaveChannelButton();
        assignMicrowaveChannel(microwaveChannelsWizard);
        RegulatoryLicenseOverviewPage regulatoryLicenseOverviewAfterMicrowaveChannelAssignment = microwaveChannelsWizard.clickAccept();

        assertAssignedMicrowaveChannels(regulatoryLicenseOverviewAfterMicrowaveChannelAssignment, MICROWAVE_CHANNEL_LABEL);
    }

    @Test(priority = 5)
    @Step("Assign Microwave Link")
    public void addMicrowaveLink() {
        RegulatoryLicenseOverviewPage rlOverviewBeforeAssign = new RegulatoryLicenseOverviewPage(driver);
        rlOverviewBeforeAssign.openMicrowaveLinksTab();
        RegulatoryLicenseMicrowaveLinksWizardPage microwaveLinksWizard = rlOverviewBeforeAssign.clickAddMicrowaveLinkButton();
        assignMicrowaveLinks(microwaveLinksWizard);
        RegulatoryLicenseOverviewPage regulatoryLicenseOverviewAfterMicrowaveLinkAssignment = microwaveLinksWizard.clickAccept();

        assertAssignedMicrowaveLinks(regulatoryLicenseOverviewAfterMicrowaveLinkAssignment, MICROWAVE_LINK_LABEL);
    }

    @Test(priority = 5)
    @Step("Update Regulatory License attributes")
    public void updateRegulatoryLicense() {
        RegulatoryLicenseAttributes regulatoryLicenseAttributes = getRegulatoryLicenseAttributesToUpdate();

        RegulatoryLicenseOverviewPage regulatoryLicenseOverviewBeforeUpdate = new RegulatoryLicenseOverviewPage(driver);
        RegulatoryLicenseWizardPage regulatoryLicenseWizard = regulatoryLicenseOverviewBeforeUpdate.clickEdit();
        fillRegulatoryLicenseWizardToUpdate(regulatoryLicenseAttributes, regulatoryLicenseWizard);
        regulatoryLicenseWizard.clickAccept();

        assertRegulatoryLicenseAttributes(regulatoryLicenseAttributes, new RegulatoryLicenseOverviewPage(driver));
    }

    @Test(priority = 6)
    @Step("Detach assigned Microwave Link")
    public void detachMicrowaveLink(){
        RegulatoryLicenseOverviewPage regulatoryLicenseOverview = new RegulatoryLicenseOverviewPage(driver);
        regulatoryLicenseOverview.openMicrowaveLinksTab();
        regulatoryLicenseOverview.removeFirstMicrowaveLink();

        assertAssignedMicrowaveLinks(regulatoryLicenseOverview);
    }

    @Test(priority = 7)
    @Step("Detach assigned Microwave Channel")
    public void detachMicrowaveChannel(){
        RegulatoryLicenseOverviewPage regulatoryLicenseOverview = new RegulatoryLicenseOverviewPage(driver);
        regulatoryLicenseOverview.openMicrowaveChannelsTab();
        regulatoryLicenseOverview.removeFirstMicrowaveChannel();

        assertAssignedMicrowaveChannels(regulatoryLicenseOverview);
    }

    @Test(priority = 8)
    @Step("Detach assigned Microwave Antenna")
    public void detachMicrowaveAntenna(){
        RegulatoryLicenseOverviewPage regulatoryLicenseOverview = new RegulatoryLicenseOverviewPage(driver);
        regulatoryLicenseOverview.openMicrowaveAntennasTab();
        regulatoryLicenseOverview.removeFirstMicrowaveAntenna();

        assertAssignedMicrowaveAntennas(regulatoryLicenseOverview);
    }

    @Test(priority = 9)
    @Step("Check if all Locations were detached")
    public void checkLocations(){
        RegulatoryLicenseOverviewPage regulatoryLicenseOverview = new RegulatoryLicenseOverviewPage(driver);
        regulatoryLicenseOverview.openLocationsTab();

        assertAssignedLocations(regulatoryLicenseOverview);
    }

    @Test(priority = 10)
    @Step("Remove Regulatory License")
    public void removeRegulatoryLicense(){
        RegulatoryLicenseOverviewPage regulatoryLicenseOverview = new RegulatoryLicenseOverviewPage(driver);
        regulatoryLicenseOverview.removeRegulatoryLicense();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        assertRegulatoryLicenseRemoval();
    }

    private RegulatoryLicenseAttributes getRegulatoryLicenseAttributesToCreate() {
        RegulatoryLicenseAttributes regLicAttributes = new RegulatoryLicenseAttributes();
        regLicAttributes.number = NUMBER;
        regLicAttributes.regulatoryAgency = REGULATORY_AGENCY;
        regLicAttributes.startingDate = STARTING_DATE;
        regLicAttributes.expirationDate = EXPIRATION_DATE;
        regLicAttributes.operatingHours = OPERATING_HOURS;
        regLicAttributes.status = STATUS;
        regLicAttributes.type = TYPE;
        regLicAttributes.description = DESCRIPTION;
        return regLicAttributes;
    }

    private RegulatoryLicenseAttributes getRegulatoryLicenseAttributesToUpdate() {
        RegulatoryLicenseAttributes regLicAttributes = new RegulatoryLicenseAttributes();
        regLicAttributes.number = NUMBER2;
        regLicAttributes.regulatoryAgency = REGULATORY_AGENCY2;
        regLicAttributes.startingDate = STARTING_DATE2;
        regLicAttributes.expirationDate = EXPIRATION_DATE2;
        regLicAttributes.operatingHours = OPERATING_HOURS2;
        regLicAttributes.status = STATUS2;
        regLicAttributes.type = TYPE2;
        regLicAttributes.description = DESCRIPTION2;
        return regLicAttributes;
    }

    private void fillRegulatoryLicenseWizardToCreate(RegulatoryLicenseAttributes regulatoryLicenseAttributes, RegulatoryLicenseWizardPage regulatoryLicenseWizard) {
        fulfillWizard(regulatoryLicenseWizard, regulatoryLicenseAttributes);
    }

    private void fillRegulatoryLicenseWizardToUpdate(RegulatoryLicenseAttributes regulatoryLicenseAttributes, RegulatoryLicenseWizardPage regulatoryLicenseWizard) {
        fulfillWizard(regulatoryLicenseWizard, regulatoryLicenseAttributes);
    }

    private RegulatoryLicenseWizardPage goToRegulatoryLicenseWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        //SideMenu sidemenu = SideMenu.create(driver, webDriverWait);
        //sidemenu.callActionByLabel(REGULATORY_LICENSE, VIEWS, TRANSPORT);
        driver.get(String.format("%s/#/view/transport/microwave/licenses?perspective=LIVE", BASIC_URL));

        return new RegulatoryLicenseWizardPage(driver);
    }

    private void fulfillWizard(RegulatoryLicenseWizardPage rlWizard, RegulatoryLicenseTest.RegulatoryLicenseAttributes rlAttributes) {
        rlWizard.setNumber(rlAttributes.number);
        rlWizard.setRegulatoryAgency(rlAttributes.regulatoryAgency);
        rlWizard.setStartingDate(rlAttributes.startingDate);
        rlWizard.setExpirationDate(rlAttributes.expirationDate);
        rlWizard.setOperatingHours(rlAttributes.operatingHours);
        rlWizard.setStatus(rlAttributes.status);
        rlWizard.setType(rlAttributes.type);
        rlWizard.setDescription(rlAttributes.description);
    }

    private void assignLocation(RegulatoryLicenseLocationsWizardPage locationWizard) {
        locationWizard.selectLocation(LOCATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void assignMicrowaveAntenna(RegulatoryLicenseMicrowaveAntennasWizardPage microwaveAntennasWizard) {
        microwaveAntennasWizard.selectMicrowaveAntenna(MICROWAVE_ANTENNA);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void assignMicrowaveChannel(RegulatoryLicenseMicrowaveChannelsWizardPage microwaveChannelsWizard) {
        microwaveChannelsWizard.selectMicrowaveChannel(MICROWAVE_CHANNEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void assignMicrowaveLinks(RegulatoryLicenseMicrowaveLinksWizardPage microwaveLinksWizard) {
        microwaveLinksWizard.selectMicrowaveLinks(MICROWAVE_LINK);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void assertRegulatoryLicenseAttributes(RegulatoryLicenseTest.RegulatoryLicenseAttributes regulatoryLicenseAttributes, RegulatoryLicenseOverviewPage regulatoryLicenseOverview){
        String numberValue = regulatoryLicenseOverview.getNumberValue();
        String statusValue = regulatoryLicenseOverview.getStatusValue();
        String startingDateValue = regulatoryLicenseOverview.getStartingDateValue();
        String expirationDateValue = regulatoryLicenseOverview.getExpirationDateValue();
        String operatingHoursValue = regulatoryLicenseOverview.getOperatingHoursValue();
        String regulatoryAgencyValue = regulatoryLicenseOverview.getRegulatoryAgencyValue();
        String typeValue = regulatoryLicenseOverview.getTypeValue();
        String descriptionValue = regulatoryLicenseOverview.getDescriptionValue();

        Assert.assertEquals(regulatoryLicenseAttributes.number, numberValue);
        Assert.assertEquals(regulatoryLicenseAttributes.status, statusValue);
        Assert.assertEquals(regulatoryLicenseAttributes.startingDate, startingDateValue);
        Assert.assertEquals(regulatoryLicenseAttributes.expirationDate, expirationDateValue);
        Assert.assertEquals(regulatoryLicenseAttributes.operatingHours, operatingHoursValue);
        Assert.assertEquals(regulatoryLicenseAttributes.regulatoryAgency, regulatoryAgencyValue);
        Assert.assertEquals(regulatoryLicenseAttributes.type, typeValue);
        Assert.assertEquals(regulatoryLicenseAttributes.description, descriptionValue);
    }

    private void assertAssignedLocations(RegulatoryLicenseOverviewPage regulatoryLicenseOverview, String... expectedLocations) {
        List<String> assignedLocations = regulatoryLicenseOverview.getAssignedLocations();

        boolean doesAllLocationsMatch = assignedLocations.containsAll(Arrays.asList(expectedLocations));

        Assert.assertTrue(doesAllLocationsMatch);
    }

    private void assertAssignedMicrowaveAntennas(RegulatoryLicenseOverviewPage regulatoryLicenseOverview, String... expectedMicrowaveAntennas) {
        List<String> assignedMicrowaveAntennas = regulatoryLicenseOverview.getAssignedMicrowaveAntennas();

        boolean doesAllMicrowaveAntennasMatch = assignedMicrowaveAntennas.containsAll(Arrays.asList(expectedMicrowaveAntennas));

        Assert.assertTrue(doesAllMicrowaveAntennasMatch);
    }

    private void assertAssignedMicrowaveChannels(RegulatoryLicenseOverviewPage regulatoryLicenseOverview, String... expectedMicrowaveChannels) {
        List<String> assignedMicrowaveChannels = regulatoryLicenseOverview.getAssignedMicrowaveChannels();

        boolean doesAllMicrowaveChannelsMatch = assignedMicrowaveChannels.containsAll(Arrays.asList(expectedMicrowaveChannels));

        Assert.assertTrue(doesAllMicrowaveChannelsMatch);
    }

    private void assertAssignedMicrowaveLinks(RegulatoryLicenseOverviewPage regulatoryLicenseOverview, String... expectedMicrowaveLinks) {
        List<String> assignedMicrowaveLinks = regulatoryLicenseOverview.getAssignedMicrowaveLinks();

        boolean doesAllMicrowaveLinksMatch = assignedMicrowaveLinks.containsAll(Arrays.asList(expectedMicrowaveLinks));

        Assert.assertTrue(doesAllMicrowaveLinksMatch);
    }

    private void assertRegulatoryLicenseRemoval() {
        String properUrlAfterRemoval = Configuration.CONFIGURATION.getUrl() + ENVIRONMENT_INDEPENDENT_URL_PART_AFTER_REMOVAL_REDIRECT;
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(properUrlAfterRemoval, driver.getCurrentUrl());
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
}
