package com.oss.pages.servicedesk.issue.wizard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.issue.problem.ProblemDashboardPage;
import com.oss.pages.servicedesk.issue.ticket.TicketDashboardPage;

import io.qameta.allure.Step;

public class SDWizardPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(SDWizardPage.class);

    private static final String INCIDENT_DESCRIPTION_ID = "TT_WIZARD_INPUT_INCIDENT_DESCRIPTION";
    private static final String CHANGE_INCIDENT_DESCRIPTION_ID = "TT_WIZARD_INPUT_INCIDENT_DESCRIPTION_LABEL";
    private static final String EMAIL_MESSAGE_ID = "message-component";
    private static final String EXPECTED_RESOLUTION_DATE_ID = "TT_WIZARD_INPUT_EXPECTED_RESOLUTION_DATE_LABEL";
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String TT_WIZARD_ASSIGNEE = "TT_WIZARD_INPUT_ASSIGNEE_LABEL";
    private static final String TT_WIZARD_REQUESTER = "TT_WIZARD_INPUT_REQUESTER_LABEL";
    private static final String TEST_SELENIUM_ID = "12345";
    private static final String TT_WIZARD_CORRELATION_ID = "ISSUE_CORRELATION_ID";
    private static final String TT_WIZARD_REFERENCE_ID = "TT_WIZARD_INPUT_REFERENCE_ID_LABEL";
    private static final String TT_WIZARD_ISSUE_START_DATE_ID = "IssueStartDate";
    private static final String TT_WIZARD_MESSAGE_DATE_ID = "PleaseProvideTheTimeOnTheHandsetTheTxtMessageArrived";
    private static final String PROBLEM_NAME_DESCRIPTION_ID = "TT_WIZARD_INPUT_PROBLEM_NAME_DESCRIPTION";
    private static final String CHANGE_RISK_ASSESSMENT_ID = "TT_WIZARD_INPUT_RISK_ASSESSMENT_LABEL";
    private static final String NOTIFICATION_CHANNEL_INTERNAL = "Internal";
    private static final String NOTIFICATION_WIZARD_CHANNEL_ID = "channel-component-input";
    private static final String NOTIFICATION_WIZARD_MESSAGE_ID = "message-component";
    private static final String NOTIFICATION_WIZARD_INTERNAL_TO_ID = "internal-to-component";
    private static final String NOTIFICATION_WIZARD_TYPE_ID = "internal-type-component";
    private static final String NOTIFICATION_TYPE = "Success";
    private static final String NOTIFICATION_WIZARD_TO_ID = "to-component";
    private static final String NOTIFICATION_WIZARD_FROM_ID = "from-component";
    private static final String NOTIFICATION_WIZARD_SUBJECT_ID = "subject-component";
    private static final String NOTIFICATION_CHANNEL_EMAIL = "E-mail";
    private static final String NOTIFICATION_SUBJECT = "Email notification test";
    private static final String TASK_WIZARD_NAME = "name";
    private static final String TASK_WIZARD_ASSIGNEE = "assignee";
    private static final String TASK_WIZARD_LABEL = "label";
    private static final String TASK_WIZARD_CREATE_TASK_BUTTON = "_createTaskSubmitId-1";

    private final MOStep moStep;
    private final Wizard wizard;

    public SDWizardPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait);
        this.wizard = Wizard.createByComponentId(driver, wait, wizardId);
        this.moStep = new MOStep(driver, wait);
    }

    public MOStep getMoStep() {
        return moStep;
    }

    public Wizard getWizard() {
        return wizard;
    }

    @Step("I click Next button in wizard")
    public void clickNextButtonInWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickNext();
        log.info("Clicking Next button in the wizard");
    }

    @Step("I click Accept button in wizard")
    public void clickAcceptButtonInWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickAcceptOldWizard();
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Accept button in the wizard");
    }

    @Step("I click {buttonID} button in wizard")
    public void clickButton(String buttonID) {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickButtonById(buttonID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking {} button in the wizard", buttonID);
    }

    @Step("I insert {text} to multi combo box component with id {componentId}")
    public void insertValueToMultiComboBoxComponent(String text, String componentId) {
        wizard.getComponent(componentId, Input.ComponentType.MULTI_COMBOBOX).setValueContains(Data.createSingleData(text));
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
    public SDWizardPage insertValueToMultiSearchComponent(String text, String componentId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.getComponent(componentId, Input.ComponentType.MULTI_SEARCH_FIELD).setValueContains(Data.createSingleData(text));
        log.info("Value {} inserted to multi searchfield", text);
        return this;
    }

    @Step("I insert {text} to multi search box component with id {componentId}")
    public void insertValueToSearchBoxComponent(String text, String componentId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.getComponent(componentId, Input.ComponentType.SEARCH_BOX).setValueContains(Data.createSingleData(text));
        log.info("Value {} inserted to search box", text);
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
        wizard.setComponentValue(EXPECTED_RESOLUTION_DATE_ID, date, Input.ComponentType.TEXT_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Insert Expected resolution date: plus 5 days from now");
    }

    @Step("I insert {message} to Email Message field")
    public void enterEmailMessage(String message) {
        DelayUtils.waitForPageToLoad(driver, wait);
        setValueInHtmlEditor(message, EMAIL_MESSAGE_ID);
        log.info("Incident description: {} is entered", message);
    }

    public void clickComboBox(String componentId) {
        wizard.getComponent(componentId, Input.ComponentType.COMBOBOX).click();
        log.info("Clicking {} combobox", componentId);
    }

    public TicketDashboardPage createTicket(String moIdentifier, String assignee) {
        getMoStep().enterTextIntoSearchComponent(moIdentifier);
        getMoStep().selectObjectInMOTable(moIdentifier);
        clickNextButtonInWizard();
        insertValueToSearchComponent(assignee, TT_WIZARD_ASSIGNEE);
        insertValueToTextComponent(TEST_SELENIUM_ID, TT_WIZARD_REFERENCE_ID);
        enterIncidentDescription("Selenium Test ticket");
        enterExpectedResolutionDate();
        insertValueToTextComponent(TEST_SELENIUM_ID, TT_WIZARD_CORRELATION_ID);
        clickNextButtonInWizard();
        String date = LocalDateTime.now().minusMinutes(5).format(CREATE_DATE_FILTER_DATE_FORMATTER);
        insertValueToTextComponent(date, TT_WIZARD_ISSUE_START_DATE_ID);
        insertValueToTextComponent(date, TT_WIZARD_MESSAGE_DATE_ID);
        clickAcceptButtonInWizard();
        return new TicketDashboardPage(driver, wait);
    }

    public ProblemDashboardPage createProblem(String moIdentifier, String assignee, String description) {
        getMoStep().enterTextIntoSearchComponent(moIdentifier);
        getMoStep().selectObjectInMOTable(moIdentifier);
        clickNextButtonInWizard();
        insertValueToTextAreaComponent(description, PROBLEM_NAME_DESCRIPTION_ID);
        insertValueToSearchComponent(assignee, TT_WIZARD_ASSIGNEE);
        clickNextButtonInWizard();
        clickAcceptButtonInWizard();
        return new ProblemDashboardPage(driver, wait);
    }

    public void createChange(String requester, String assignee, String description) {
        insertValueToTextComponent("LOW", CHANGE_RISK_ASSESSMENT_ID);
        insertValueToSearchComponent(requester, TT_WIZARD_REQUESTER);
        insertValueToSearchComponent(assignee, TT_WIZARD_ASSIGNEE);
        insertValueToTextAreaComponent(description, CHANGE_INCIDENT_DESCRIPTION_ID);
        clickNextButtonInWizard();
        clickAcceptButtonInWizard();
    }

    public void createTask(String taskName, String taskAssignee, String taskLabel) {
        insertValueToTextComponent(taskName, TASK_WIZARD_NAME);
        insertValueToSearchComponent(taskAssignee, TASK_WIZARD_ASSIGNEE);
        insertValueToTextComponent(taskLabel, TASK_WIZARD_LABEL);
        clickButton(TASK_WIZARD_CREATE_TASK_BUTTON);
    }

    public void createInternalNotification(String textMessage, String messageTo) {
        insertValueToComboBoxComponent(NOTIFICATION_CHANNEL_INTERNAL, NOTIFICATION_WIZARD_CHANNEL_ID);
        insertValueToTextAreaComponent(textMessage, NOTIFICATION_WIZARD_MESSAGE_ID);
        insertValueToMultiComboBoxComponent(messageTo, NOTIFICATION_WIZARD_INTERNAL_TO_ID);
        insertValueToComboBoxComponent(NOTIFICATION_TYPE, NOTIFICATION_WIZARD_TYPE_ID);
        clickAcceptButtonInWizard();
    }

    public void createEmailNotification(String notificationEmailTo, String notificationEmailFrom, String textMessage) {
        insertValueToComboBoxComponent(NOTIFICATION_CHANNEL_EMAIL, NOTIFICATION_WIZARD_CHANNEL_ID);
        insertValueToMultiSearchComponent(notificationEmailTo, NOTIFICATION_WIZARD_TO_ID);
        insertValueToComboBoxComponent(notificationEmailFrom, NOTIFICATION_WIZARD_FROM_ID);
        insertValueToTextComponent(NOTIFICATION_SUBJECT, NOTIFICATION_WIZARD_SUBJECT_ID);
        enterEmailMessage(textMessage);
        clickAcceptButtonInWizard();
    }

    private void insertValueToComponent(String text, String componentId, Input.ComponentType componentType) {
        wizard.setComponentValue(componentId, text, componentType);
    }

    public static SDWizardPage openCreateWizard(WebDriver driver, WebDriverWait wait, String flowType, String createButtonId, String wizardId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, createButtonId).click();
        DropdownList.create(driver, wait).selectOptionById(flowType);
        return new SDWizardPage(driver, wait, wizardId);
    }
}
