package com.oss.pages.iaa.acd.settingsview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.prompts.Popup;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.acd.BaseACDPage;

import io.qameta.allure.Step;

public class NotificationWizardPage extends BaseACDPage {

    private static final String NAME_FIELD_ID = "notificationNameId";
    private static final String STATUS_COMBOBOX_ID = "notificationStatusId";
    private static final String SEND_TYPE_COMBOBOX_ID = "notificationSendTypeId";
    private static final String CREATION_TYPE_COMBOBOX_ID = "notificationIssueCreationTypeId";
    private static final String NOTIFICATION_TYPE_COMBOBOX_ID = "notificationTypeId";
    private static final String TITLE_ID = "notificationMessageTitleId";
    private static final String RECIPIENT_ID = "notificationMessageAddressId";
    private static final String BODY_ID = "notificationMessageBodyId";
    private static final String SCENARIO_ID = "notificationScenarioId";
    private static final String EVENT_TYPE_ID = "notificationEventTypeId";
    private static final String SEARCHING_CLASS_ID = "notificationSearchingClassId";
    private static final String QUERY_STRING_ID = "notificationQueryStringId";
    private static final String SAVE_BUTTON_LABEL = "Save";

    private final Popup notificationWizardPopup;

    public NotificationWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        notificationWizardPopup = Popup.create(driver, wait);
    }

    @Step("Set Name")
    public void setName(String name) {
        notificationWizardPopup.setComponentValue(NAME_FIELD_ID, name);
    }

    @Step("Set Status")
    public void setStatus(String status) {
        notificationWizardPopup.setComponentValue(STATUS_COMBOBOX_ID, status);
    }

    @Step("Set Send Type")
    public void setSendType(String sendType) {
        notificationWizardPopup.setComponentValue(SEND_TYPE_COMBOBOX_ID, sendType);
    }

    @Step("Set Creation Type")
    public void setCreationType(String creationType) {
        notificationWizardPopup.setComponentValue(CREATION_TYPE_COMBOBOX_ID, creationType);
    }

    @Step("Set Notification Type")
    public void setNotificationType(String notificationType) {
        notificationWizardPopup.setComponentValue(NOTIFICATION_TYPE_COMBOBOX_ID, notificationType);
    }

    @Step("Set Title")
    public void setTitle(String title) {
        notificationWizardPopup.setComponentValue(TITLE_ID, title);
    }

    @Step("Set Recipient")
    public void setRecipient(String recipient) {
        notificationWizardPopup.setComponentValue(RECIPIENT_ID, recipient);
    }

    @Step("Set Body")
    public void setBody(String body) {
        notificationWizardPopup.setComponentValue(BODY_ID, body);
    }

    @Step("Set Scenario")
    public void setScenario(String scenario) {
        notificationWizardPopup.setComponentValue(SCENARIO_ID, scenario);
    }

    @Step("Set Event Type")
    public void setEventType(String eventType) {
        notificationWizardPopup.setComponentValue(EVENT_TYPE_ID, eventType);
    }

    @Step("Set Searching Class")
    public void setSearchingClass(String searchingClass) {
        notificationWizardPopup.setComponentValue(SEARCHING_CLASS_ID, searchingClass);
    }

    @Step("Set Query String")
    public void setQueryString(String queryString) {
        notificationWizardPopup.setComponentValue(QUERY_STRING_ID, queryString);
    }

    @Step("Click save button")
    public void clickSaveButton() {
        DelayUtils.waitForPageToLoad(driver, wait);
        notificationWizardPopup.clickButtonByLabel(SAVE_BUTTON_LABEL);
    }
}