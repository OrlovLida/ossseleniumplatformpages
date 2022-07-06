package com.oss.pages.transport.trail.v2;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class MicrowaveLinkWizardPage extends BasePage {

    private static final String WIZARD_ID = "trailWizardId_prompt-card";

    private static final String USER_LABEL_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.UserLabel";
    private static final String LINK_ID_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.LinkID";
    private static final String IQLINK_ID_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.iQlinkID";
    private static final String TECHNOLOGY_TYPE_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.TechnologyType";
    private static final String AGGREGATION_CONFIGURATION_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.AggregationConfiguration";
    private static final String NUMBER_OF_WORKING_CHANNELS_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.NumberOfWorkingChannels";
    private static final String NUMBER_OF_PROTECTING_CHANNELS_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.NumberOfProtectingChannels";
    private static final String CAPACITY_VALUE_FIELD_ID = "trailCapacityValueComponent";
    private static final String NETWORK_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.Network";
    private static final String PATH_LENGTH_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.PathLength";
    private static final String START_SITE_IQLINK_ID_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.StartSiteIQlinkID";
    private static final String END_SITE_IQLINK_ID_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.EndSiteIQlinkID";
    private static final String DESCRIPTION_FIELD_ID = "trailDescriptionComponent";

    public MicrowaveLinkWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    @Step("Set User Label = {userLabel}")
    public void setUserLabel(String userLabel) {
        wizard.setComponentValue(USER_LABEL_FIELD_ID, userLabel, TEXT_FIELD);
        waitForPageToLoad();
    }

    @Step("Set Link ID = {linkId}")
    public void setLinkId(String linkId) {
        wizard.setComponentValue(LINK_ID_FIELD_ID, linkId, TEXT_FIELD);
        waitForPageToLoad();
    }

    @Step("Set iQlink ID = {iqlinkId}")
    public void setIqlinkId(String iqlinkId) {
        wizard.setComponentValue(IQLINK_ID_FIELD_ID, iqlinkId, TEXT_FIELD);
        waitForPageToLoad();
    }

    @Step("Set Technology Type = {technologyType}")
    public void setTechnologyType(String technologyType) {
        wizard.setComponentValue(TECHNOLOGY_TYPE_FIELD_ID, technologyType, COMBOBOX);
        waitForPageToLoad();
    }

    @Step("Set Aggregation Configuration = {aggregationConfiguration}")
    public void setAggregationConfiguration(String aggregationConfiguration) {
        wizard.setComponentValue(AGGREGATION_CONFIGURATION_FIELD_ID, aggregationConfiguration, TEXT_FIELD);
        waitForPageToLoad();
    }

    @Step("Set Number of working channels = {numberOfWorkingChannels}")
    public void setNumberOfWorkingChannels(String numberOfWorkingChannels) {
        wizard.setComponentValue(NUMBER_OF_WORKING_CHANNELS_FIELD_ID, numberOfWorkingChannels, TEXT_FIELD);
        waitForPageToLoad();
    }

    @Step("Set Number of protecting channels = {numberOfProtectingChannels}")
    public void setNumberOfProtectingChannels(String numberOfProtectingChannels) {
        wizard.setComponentValue(NUMBER_OF_PROTECTING_CHANNELS_FIELD_ID, numberOfProtectingChannels, TEXT_FIELD);
        waitForPageToLoad();
    }

    @Step("Set Capacity Value = {capacityValue}")
    public void setCapacityValue(String capacityValue) {
        wizard.setComponentValue(CAPACITY_VALUE_FIELD_ID, capacityValue, TEXT_FIELD);
        waitForPageToLoad();
    }

    @Step("Set Network = {network}")
    public void setNetwork(String network) {
        wizard.setComponentValue(NETWORK_FIELD_ID, network, COMBOBOX);
        waitForPageToLoad();
    }

    @Step("Set Path Length = {pathLength}")
    public void setPathLength(String pathLength) {
        wizard.setComponentValue(PATH_LENGTH_FIELD_ID, pathLength, TEXT_FIELD);
        waitForPageToLoad();
    }

    @Step("Set Start Site iQlink ID = {startSiteIqlinkId}")
    public void setStartSiteIqlinkId(String startSiteIqlinkId) {
        wizard.setComponentValue(START_SITE_IQLINK_ID_FIELD_ID, startSiteIqlinkId, TEXT_FIELD);
        waitForPageToLoad();
    }

    @Step("Set End Site iQlink ID = {endSiteIqlinkId}")
    public void setEndSiteIqlinkId(String endSiteIqlinkId) {
        wizard.setComponentValue(END_SITE_IQLINK_ID_FIELD_ID, endSiteIqlinkId, TEXT_FIELD);
        waitForPageToLoad();
    }

    @Step("Set Description = {description}")
    public void setDescription(String description) {
        wizard.setComponentValue(DESCRIPTION_FIELD_ID, description, TEXT_FIELD);
        waitForPageToLoad();
    }

    @Step("Click Next button")
    public void clickNext() {
        getWizard().clickNext();
        waitForPageToLoad();
    }

    @Step("Click Accept button")
    public void clickAccept() {
        getWizard().clickAccept();
        waitForPageToLoad();
    }

    private Wizard getWizard() {
        return wizard;
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
