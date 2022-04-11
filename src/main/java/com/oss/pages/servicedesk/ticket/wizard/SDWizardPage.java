package com.oss.pages.servicedesk.ticket.wizard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.servicedesk.BaseSDPage;

import io.qameta.allure.Step;

public class SDWizardPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(SDWizardPage.class);

    private static final String INCIDENT_DESCRIPTION_ID = "TT_WIZARD_INPUT_INCIDENT_DESCRIPTION_LABEL";
    private static final String EMAIL_MESSAGE_ID = "message-component";
    private static final String CREATE_EXTERNAL_LABEL = "Create External";
    private static final String EXPECTED_RESOLUTION_DATE_ID = "TT_WIZARD_INPUT_EXPECTED_RESOLUTION_DATE_LABEL";
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String LINK_TICKETS_BUTTON_ID = "_buttonsApp-1";
    private static final String UNLINK_CONFIRMATION_BUTTON_ID = "ConfirmationBox__confirmUnlinkProblemApp_action_button";

    private final MOStep moStep;

    public SDWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        this.moStep = new MOStep(driver, wait);
    }

    public MOStep getMoStep() {
        return moStep;
    }

    @Step("I click Next button in wizard")
    public void clickNextButtonInWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickNext();
        log.info("Clicking Next button in the wizard");
    }

    @Step("I click Accept button in wizard")
    public void clickAcceptButtonInWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Accept button in the wizard");
    }

    @Step("I click {buttonID} button in wizard")
    public void clickButton(String buttonID) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickButtonById(buttonID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking {} button in the wizard", buttonID);
    }

    @Step("I click Create External button in wizard")
    public void clickCreateExternalButtonInWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonContainer.create(driver, wait).callActionByLabel(CREATE_EXTERNAL_LABEL);
        log.info("Clicking Create External button in the wizard");
    }

    @Step("I insert {text} to multi combo box component with id {componentId}")
    public void insertValueToMultiComboBoxComponent(String text, String componentId) {
        insertValueToComponent(text, componentId, Input.ComponentType.MULTI_COMBOBOX);
        log.info("Value {} inserted to multi combobox", text);
    }

    @Step("I insert {text} to combo box component with id {componentId}")
    public void insertValueToComboBoxComponent(String text, String componentId) {
        insertValueToComponent(text, componentId, Input.ComponentType.COMBOBOX);
        log.info("Value {} inserted to combobox", text);
    }

    @Step("I insert {text} to search component with id {componentId}")
    public void insertValueToSearchComponent(String text, String componentId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        insertValueToComponent(text, componentId, Input.ComponentType.SEARCH_FIELD);
        log.info("Value {} inserted to searchfield", text);
    }

    @Step("I insert {text} to multi search component with id {componentId}")
    public void insertValueToMultiSearchComponent(String text, String componentId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().getComponent(componentId, Input.ComponentType.MULTI_SEARCH_FIELD).setValueContains(Data.createSingleData(text));
        log.info("Value {} inserted to multi searchfield", text);
    }

    @Step("I insert {text} to text component with id {componentId}")
    public void insertValueToTextComponent(String text, String componentId) {
        insertValueToComponent(text, componentId, Input.ComponentType.TEXT_FIELD);
        log.info("Value {} inserted to textfield", text);
    }

    @Step("I insert {text} to text area component with id {componentId}")
    public void insertValueToTextAreaComponent(String text, String componentId) {
        insertValueToComponent(text, componentId, Input.ComponentType.TEXT_AREA);
        log.info("Value {} inserted to textarea", text);
    }

    @Step("I insert {description} to Incident Description field")
    public void enterIncidentDescription(String description) {
        DelayUtils.waitForPageToLoad(driver, wait);
        setValueInHtmlEditor(description, INCIDENT_DESCRIPTION_ID);
        log.info("Incident description: {} is entered", description);
    }

    @Step("Insert Expected resolution date: plus 5 days from now")
    public void enterExpectedResolutionDate() {
        String date = LocalDateTime.now().plusDays(5).format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
        ComponentFactory.create(EXPECTED_RESOLUTION_DATE_ID, Input.ComponentType.TEXT_FIELD, driver, wait).setSingleStringValue(date);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Insert Expected resolution date: plus 5 days from now");
    }

    @Step("I insert {message} to Email Message field")
    public void enterEmailMessage(String message) {
        DelayUtils.waitForPageToLoad(driver, wait);
        setValueInHtmlEditor(message, EMAIL_MESSAGE_ID);
        log.info("Incident description: {} is entered", message);
    }

    @Step("Click Link Button")
    public void clickLinkButton() {
        clickButton(LINK_TICKETS_BUTTON_ID);
    }

    @Step("Click Unlink Button")
    public void clickUnlinkConfirmationButton() {
        clickButton(UNLINK_CONFIRMATION_BUTTON_ID);
    }

    public void clickComboBox(String componentId) {
        getWizard().getComponent(componentId, Input.ComponentType.COMBOBOX).click();
        log.info("Clicking {} combobox", componentId);
    }

    private void insertValueToComponent(String text, String componentId, Input.ComponentType componentType) {
        getWizard().setComponentValue(componentId, text, componentType);
    }

    private Wizard getWizard() {
        return Wizard.createWizard(driver, wait);
    }
}
