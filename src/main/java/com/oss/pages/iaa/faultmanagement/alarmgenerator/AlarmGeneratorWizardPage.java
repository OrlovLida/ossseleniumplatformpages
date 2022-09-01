package com.oss.pages.iaa.faultmanagement.alarmgenerator;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.prompts.Popup;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class AlarmGeneratorWizardPage extends BasePage {

    private static final String MO_IDENTIFIER_ID = "moIdentifier";
    private static final String NOTIFICATION_IDENTIFIER_ID = "notificationIdentifier";
    private static final String CREATE_BUTTON_ID = "wizard-submit-button-alarm-generator-create-edit-wizard";

    private final Popup alarmGeneratorPopup;

    public AlarmGeneratorWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        alarmGeneratorPopup = Popup.create(driver, wait);
    }

    @Step("Set MO Identifier in Alarm Generator Wizard")
    public void setMoIdentifier(String moIdentifier) {
        alarmGeneratorPopup.setComponentValue(MO_IDENTIFIER_ID, moIdentifier);
    }

    @Step("Set Notification Identifier in Alarm Generator Wizard")
    public void setNotificationIdentifier(String notificationIdentifier) {
        alarmGeneratorPopup.setComponentValue(NOTIFICATION_IDENTIFIER_ID, notificationIdentifier);
    }

    @Step("Click Create")
    public void clickCreateButton() {
        DelayUtils.waitForPageToLoad(driver, wait);
        alarmGeneratorPopup.clickButtonById(CREATE_BUTTON_ID);
    }
}
