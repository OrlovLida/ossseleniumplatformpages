package com.oss.pages.servicedesk.ticket;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.ticket.wizard.SDWizardPage;

import io.qameta.allure.Step;

public class BaseDashboardPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(BaseDashboardPage.class);

    private static final String CREATE_TICKET_BUTTON_ID = "TT_WIZARD_INPUT_CREATE_TICKET";
    private static final String ID_ATTRIBUTE = "ID";
    private static final String NAME_ATTRIBUTE = "Name";
    private static final String ASSIGNEE_ATTRIBUTE = "Assignee";
    private static final String PROBLEM_ID_ATTRIBUTE = "Problem ID";
    private static final String STATUS_ATTRIBUTE = "Status";
    private static final String STATE_ATTRIBUTE = "State";
    private static final String TROUBLE_TICKETS_TABLE_ID = "_CommonTable_Dashboard_TroubleTickets";
    private static final String CREATE_PROBLEM_BUTTON_ID = "PM_WIZARD_CREATE_TITLE";
    private static final String PROBLEM_BUTTON_ID = "Problem";
    private static final String PROBLEMS_TABLE_ID = "_tableProblems";
    private static final String GROUP_ID_OLD_ACTIONS_CONTAINER = "frameworkCustomEllipsis";
    private static final String EXPORT_BUTTON_ID = "EXPORT";

    public BaseDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open ticket dashboard View")
    public BaseDashboardPage goToPage(WebDriver driver, String basicURL, String dashboardName) {
        String pageUrl = String.format("%s/#/dashboard/predefined/id/%s", basicURL, dashboardName);
        openPage(driver, pageUrl);

        return new BaseDashboardPage(driver, wait);
    }

    @Step("I open create ticket wizard for flow {flowType}")
    public SDWizardPage openCreateTicketWizard(String flowType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, CREATE_TICKET_BUTTON_ID).click();
        DropdownList.create(driver, wait).selectOptionById(flowType);
        log.info("Create ticket wizard for {} is opened", flowType);

        return new SDWizardPage(driver, wait);
    }

    @Step("I open create problem wizard")
    public SDWizardPage clickCreateProblem() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, CREATE_PROBLEM_BUTTON_ID).click();
        DropdownList.create(driver, wait).selectOptionById(PROBLEM_BUTTON_ID);
        log.info("Create Problem wizard is opened");

        return new SDWizardPage(driver, wait);
    }

    @Step("Click Export")
    public void clickExport() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTroubleTicketsTable().callAction(GROUP_ID_OLD_ACTIONS_CONTAINER, EXPORT_BUTTON_ID);

        log.info("Exporting XLSX file");
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
        return getTroubleTicketsTable().getCellValue(getRowWithTicketAssignee(assignee), ID_ATTRIBUTE);
    }

    @Step("Check if Reminder icon is present in Trouble Ticket Table")
    public boolean isReminderPresent(int cellIndex, String reminderText) {
        String iconTitles = getTroubleTicketsTable().getCellValue(cellIndex, STATE_ATTRIBUTE);
        log.info("Check if Reminder icon is present in Trouble Ticket Table");
        return iconTitles.contains(reminderText);
    }

    @Step("get row number for ticket with {id}")
    public int getRowForTicketWithID(String id) {
        return getTroubleTicketsTable().getRowNumber(id, ID_ATTRIBUTE);
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