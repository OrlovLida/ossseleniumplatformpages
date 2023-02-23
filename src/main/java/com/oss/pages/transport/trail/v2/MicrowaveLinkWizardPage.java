package com.oss.pages.transport.trail.v2;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class MicrowaveLinkWizardPage extends BasePage {

    private static final String WIZARD_ID = "trailWizardId_prompt-card";

    private static final String USER_LABEL_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.UserLabel";
    private static final String LINK_ID_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.LinkID";
    private static final String TECHNOLOGY_TYPE_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.TechnologyType";
    private static final String AGGREGATION_CONFIGURATION_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.AggregationConfiguration";
    private static final String NUMBER_OF_WORKING_CHANNELS_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.NumberOfWorkingChannels";
    private static final String NUMBER_OF_PROTECTING_CHANNELS_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.NumberOfProtectingChannels";
    private static final String CAPACITY_VALUE_FIELD_ID = "trailCapacityValueComponent";
    private static final String NETWORK_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.Network";
    private static final String PATH_LENGTH_FIELD_ID = "oss.transport.trail.type.MicrowaveLink.PathLength";
    private static final String DESCRIPTION_FIELD_ID = "trailDescriptionComponent";

    public MicrowaveLinkWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    @Step("Set User Label = {userLabel}")
    public void setUserLabel(String userLabel) {
        wizard.setComponentValue(USER_LABEL_FIELD_ID, userLabel);
        waitForPageToLoad();
    }

    @Step("Set Link ID = {linkId}")
    public void setLinkId(String linkId) {
        wizard.setComponentValue(LINK_ID_FIELD_ID, linkId);
        waitForPageToLoad();
    }

    @Step("Set Technology Type = {technologyType}")
    public void setTechnologyType(String technologyType) {
        wizard.setComponentValue(TECHNOLOGY_TYPE_FIELD_ID, technologyType);
        waitForPageToLoad();
    }

    @Step("Set Aggregation Configuration = {aggregationConfiguration}")
    public void setAggregationConfiguration(String aggregationConfiguration) {
        wizard.setComponentValue(AGGREGATION_CONFIGURATION_FIELD_ID, aggregationConfiguration);
        waitForPageToLoad();
    }

    @Step("Set Number of working channels = {numberOfWorkingChannels}")
    public void setNumberOfWorkingChannels(String numberOfWorkingChannels) {
        wizard.setComponentValue(NUMBER_OF_WORKING_CHANNELS_FIELD_ID, numberOfWorkingChannels);
        waitForPageToLoad();
    }

    @Step("Set Number of protecting channels = {numberOfProtectingChannels}")
    public void setNumberOfProtectingChannels(String numberOfProtectingChannels) {
        wizard.setComponentValue(NUMBER_OF_PROTECTING_CHANNELS_FIELD_ID, numberOfProtectingChannels);
        waitForPageToLoad();
    }

    @Step("Set Capacity Value = {capacityValue}")
    public void setCapacityValue(String capacityValue) {
        wizard.setComponentValue(CAPACITY_VALUE_FIELD_ID, capacityValue);
        waitForPageToLoad();
    }

    @Step("Set Network = {network}")
    public void setNetwork(String network) {
        wizard.setComponentValue(NETWORK_FIELD_ID, network);
        waitForPageToLoad();
    }

    @Step("Set Path Length = {pathLength}")
    public void setPathLength(String pathLength) {
        wizard.setComponentValue(PATH_LENGTH_FIELD_ID, pathLength);
        waitForPageToLoad();
    }

    @Step("Set Description = {description}")
    public void setDescription(String description) {
        wizard.setComponentValue(DESCRIPTION_FIELD_ID, description);
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