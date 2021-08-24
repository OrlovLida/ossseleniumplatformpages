package com.oss.pages.transport.regulatoryLicense;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class RegulatoryLicenseMicrowaveChannelsWizardPage extends BasePage {
    private static final String MICROWAVE_CHANNEL_ID = "assign-regulatory-license-assignment-object-field";

    private final Wizard wizard;

    public RegulatoryLicenseMicrowaveChannelsWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, "assign-regulatory-license-view");
    }

    @Step("Set Microwave Channel value to {microwaveChannel}")
    public void selectMicrowaveChannel(String microwaveChannel) {
        //Input microwaveChannelComponent = wizard.getComponent(MICROWAVE_CHANNEL_ID, Input.ComponentType.SEARCH_FIELD);
        //microwaveChannelComponent.clear();
        //microwaveChannelComponent.setSingleStringValue(microwaveChannel);
        wizard.setComponentValue(MICROWAVE_CHANNEL_ID, microwaveChannel, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Click accept button")
    public RegulatoryLicenseOverviewPage clickAccept() {
        wizard.clickAcceptOldWizard();
        DelayUtils.waitForPageToLoad(driver, wait);
        return new RegulatoryLicenseOverviewPage(driver);
    }
}

