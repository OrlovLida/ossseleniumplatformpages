package com.oss.pages.servicedesk.ticket.wizard;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.HtmlEditor;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.servicedesk.BaseSDPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WizardPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(WizardPage.class);

    private static final String INCIDENT_DESCRIPTION_ID = "TT_WIZARD_INPUT_INCIDENT_DESCRIPTION";
    private final static String CREATE_EXTERNAL_LABEL = "Create External";

    private final MOStep moStep;

    public WizardPage(WebDriver driver) {
        super(driver);
        this.moStep = new MOStep(wait);
    }

    public MOStep getMoStep() {
        return moStep;
    }

    @Step("I click Next button in wizard")
    public void clickNextButtonInWizard(WebDriver driver) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickNext();
        log.info("Clicking Next button in the wizard");
    }

    @Step("I click Accept button in wizard")
    public void clickAcceptButtonInWizard(WebDriver driver) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getWizard().clickAccept();
        log.info("Clicking Accept button in the wizard");
    }

    @Step("I click Create External button in wizard")
    public void clickCreateExternalButtonInWizard(WebDriver driver) {
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
        HtmlEditor htmlEditor = HtmlEditor.create(driver, wait, INCIDENT_DESCRIPTION_ID);
        htmlEditor.clear();
        htmlEditor.setValue(Data.createSingleData(description));
        log.info("Incident description: {} is entered", description);
    }

    public void clickComboBox(String componentId){
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
