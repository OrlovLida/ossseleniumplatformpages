package com.oss.pages.transport.trail;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class MicrowaveChannelRoutingWizard extends BasePage {

    private static final String ID = "RoutingPopupViewId_prompt-card";
    private static final String PROTECTION_TYPE = "routingFormProtectionTypeComponent";
    //    TODO: Poprawić po rozwiązaniu OSSTRAIL-7974
    private static final String LINE_TYPE = "__lineType";
    private static final String SEQUENCE_NUMBER = "__sequenceNumber";

    public MicrowaveChannelRoutingWizard(WebDriver driver) {
        super(driver);
        getWizard();
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, ID);
    }

    @Step("Click proceed button")
    public void proceed() {
        getWizard().clickProceed();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click accept button")
    public void accept() {
        getWizard().clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Set Protection Type = {value}")
    public void setProtectionType(String value) {
        getWizard().setComponentValue(PROTECTION_TYPE, value);
    }

    @Step("Set Line Type = {value}")
    public void setLineType(String value) {
        getWizard().setComponentValue(LINE_TYPE, value);
    }

    @Step("Set Sequence Number = {value}")
    public void setSequenceNumber(String value) {
        getWizard().setComponentValue(SEQUENCE_NUMBER, value);
    }
}
