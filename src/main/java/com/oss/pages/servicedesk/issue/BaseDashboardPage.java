package com.oss.pages.servicedesk.issue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.COMMON_WIZARD_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.ID_UPPERCASE_ATTRIBUTE;
import static com.oss.pages.servicedesk.ServiceDeskConstants.PREDEFINED_DASHBOARD_URL_PATTERN;

public class BaseDashboardPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(BaseDashboardPage.class);

    private static final String CREATE_TICKET_BUTTON_ID = "TT_WIZARD_INPUT_CREATE_TICKET";
    private static final String NAME_ATTRIBUTE = "Name";
    private static final String ASSIGNEE_ATTRIBUTE = "Assignee";
    private static final String PROBLEM_ID_ATTRIBUTE = "Problem ID";
    private static final String STATUS_ATTRIBUTE = "Status";
    private static final String STATE_ATTRIBUTE = "State";
    private static final String TROUBLE_TICKETS_TABLE_ID = "_CommonTable_Dashboard_TroubleTickets";
    private static final String CREATE_PROBLEM_BUTTON_ID = "PM_WIZARD_CREATE_TITLE";
    private static final String PROBLEM_BUTTON_ID = "Problem";
    private static final String PROBLEMS_TABLE_ID = "_tableProblems";

    public BaseDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open ticket dashboard View")
    public BaseDashboardPage goToPage(WebDriver driver, String basicURL, String dashboardName) {
        String pageUrl = String.format(PREDEFINED_DASHBOARD_URL_PATTERN, basicURL, dashboardName);
        openPage(driver, pageUrl);

        return new BaseDashboardPage(driver, wait);
    }

    @Step("Check if current url leads to Trouble Ticket dashboard")
    public boolean isDashboardOpen(String basicURL, String dashboardName) {
        return driver.getCurrentUrl().equals(String.format(PREDEFINED_DASHBOARD_URL_PATTERN, basicURL, dashboardName));
    }

    @Step("I open create ticket wizard for flow {flowType}")
    public SDWizardPage openCreateTicketWizard(String flowType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, CREATE_TICKET_BUTTON_ID).click();
        DropdownList.create(driver, wait).selectOptionById(flowType);
        log.info("Create ticket wizard for {} is opened", flowType);

        return new SDWizardPage(driver, wait, COMMON_WIZARD_ID);
    }

    @Step("I open create problem wizard")
    public SDWizardPage clickCreateProblem() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, CREATE_PROBLEM_BUTTON_ID).click();
        DropdownList.create(driver, wait).selectOptionById(PROBLEM_BUTTON_ID);
        log.info("Create Problem wizard is opened");

        return new SDWizardPage(driver, wait, COMMON_WIZARD_ID);
    }

    @Step("Click Export")
    public void clickExportFromTTTable() {
        clickExportFromTable(TROUBLE_TICKETS_TABLE_ID);
    }

    private String getAttributeFromTicketsTable(int index, String attributeName) {
        String attributeValue = getTroubleTicketsTable().getCellValue(index, attributeName);
        log.info("Got value for {} attribute: {} from Trouble Tickets Table", attributeName, attributeValue);
        return attributeValue;
    }

    public String getAssigneeForNthTicketInTTTable(int n) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getAttributeFromTicketsTable(n, ASSIGNEE_ATTRIBUTE);
    }

    public String getTicketIdWithAssignee(String assignee) {
        return getTroubleTicketsTable().getCellValue(getRowWithTicketAssignee(assignee), ID_UPPERCASE_ATTRIBUTE);
    }

    public String getFirstTicketId(String tableSuffix) {
        return OldTable.createById(driver, wait, TROUBLE_TICKETS_TABLE_ID + tableSuffix).getCellValue(0, ID_UPPERCASE_ATTRIBUTE);
    }

    @Step("Check if Reminder icon is present in Trouble Ticket Table")
    public boolean isReminderPresent(int cellIndex, String reminderText) {
        String iconTitles = getTroubleTicketsTable().getCellValue(cellIndex, STATE_ATTRIBUTE);
        log.info("Check if Reminder icon is present in Trouble Ticket Table");
        return iconTitles.contains(reminderText);
    }

    @Step("get row number for ticket with {id}")
    public int getRowForTicketWithID(String id) {
        return getTroubleTicketsTable().getRowNumber(id, ID_UPPERCASE_ATTRIBUTE);
    }

    private int getRowWithTicketAssignee(String assignee) {
        return getTroubleTicketsTable().getRowNumber(assignee, ASSIGNEE_ATTRIBUTE);
    }

    @Step("Check if problem with {problemName} is in the table")
    public boolean isProblemCreated(String problemName) {
        return getProblemsTable().getRowNumber(problemName, NAME_ATTRIBUTE) >= 0;
    }

    @Step("Get problem Id with Problem Name: {problemName}")
    public String getProblemIdWithProblemName(String problemName) {
        return getProblemsTable().getCellValue(getRowWithProblemName(problemName), PROBLEM_ID_ATTRIBUTE);
    }

    @Step("Get problem assignee")
    public String getProblemAssignee(String problemName) {
        return getProblemsTable().getCellValue(getRowWithProblemName(problemName), ASSIGNEE_ATTRIBUTE);
    }

    @Step("Get problem status")
    public String getProblemStatus(String problemName) {
        return getProblemsTable().getCellValue(getRowWithProblemName(problemName), STATUS_ATTRIBUTE);
    }

    public String getMessageFromPrompt() {
        return SystemMessageContainer.create(driver, wait)
                .getFirstMessage()
                .map(SystemMessageContainer.Message::getText)
                .orElse(null);
    }

    public String getIdFromMessage() {
        if (!getMessageFromPrompt().isEmpty()) {
            String[] splitMessage = getMessageFromPrompt().split(" ");
            log.info("id is: {}", splitMessage[1]);
            return splitMessage[1];
        }
        return "No message is shown";
    }

    private int getRowWithProblemName(String problemName) {
        return getProblemsTable().getRowNumber(problemName, NAME_ATTRIBUTE);
    }

    private OldTable getProblemsTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Create Problems Table");
        return OldTable.createById(driver, wait, PROBLEMS_TABLE_ID);
    }

    private OldTable getTroubleTicketsTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Create Trouble Ticket Table");
        return OldTable.createById(driver, wait, TROUBLE_TICKETS_TABLE_ID);
    }
}