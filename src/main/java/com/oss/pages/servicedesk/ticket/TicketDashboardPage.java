package com.oss.pages.servicedesk.ticket;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.ticket.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ticket.TicketDetailsPage.DETAILS_PAGE_URL_PATTERN;

public class TicketDashboardPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(TicketDashboardPage.class);

    private static final String CREATE_TICKET_BUTTON_ID = "TT_WIZARD_INPUT_CREATE_TICKET";
    public static final String ID_ATTRIBUTE = "ID";
    private static final String ASSIGNEE_ATTRIBUTE = "Assignee";
    private static final String DESCRIPTION_ATTRIBUTE = "Incident Description";
    private static final String TABLE_ID = "_TroubleTickets";
    private static final String TROUBLE_TICKETS_TABLE_ID = "_CommonTable_Dashboard_TroubleTickets";

    public TicketDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open ticket dashboard View")
    public static TicketDashboardPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 150);

        String pageUrl = String.format("%s/#/dashboard/predefined/id/_TroubleTickets", basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new TicketDashboardPage(driver, wait);
    }

    @Step("I open create ticket wizard for flow {flowType}")
    public SDWizardPage openCreateTicketWizard(String flowType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, CREATE_TICKET_BUTTON_ID).click();
        DropdownList.create(driver, wait).selectOptionById(flowType);
        log.info("Create ticket wizard for {} is opened", flowType);

        return new SDWizardPage(driver, wait);
    }

    @Step("I check severity table")
    public TableWidget getSeverityTable(String tableId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Create Table widget");
        return TableWidget.createById(driver, tableId, wait);
    }

    @Step("I check Trouble Tickets table")
    public OldTable getTroubleTicketsTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Create Trouble Ticket Table");
        return OldTable.createById(driver, wait, TROUBLE_TICKETS_TABLE_ID);
    }

    public String getIdForNthTicketInTable(int n) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getAttributeFromTable(n, ID_ATTRIBUTE);
    }

    private String getAttributeFromTable(int index, String attributeName) {
        String attributeValue = getSeverityTable(TABLE_ID).getCellValue(index, attributeName);
        log.info("Got value for {} attribute: {}", attributeName, attributeValue);
        return attributeValue;
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

    public String getDescriptionForNthTicketInTTTable(int n) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getAttributeFromTicketsTable(n, DESCRIPTION_ATTRIBUTE);
    }

    @Step("I open details view for {rowIndex} ticket in Ticket table")
    public TicketDetailsPage openTicketDetailsView(String rowIndex, String basicURL) {
        DelayUtils.waitForPageToLoad(driver, wait);
        String ticketId = getTroubleTicketsTable().getCellValue(Integer.parseInt(rowIndex), ID_ATTRIBUTE);
        log.info("Opening ticket details for ticket with id: {}", ticketId);
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.get(String.format(DETAILS_PAGE_URL_PATTERN, basicURL, ticketId));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new TicketDetailsPage(driver, wait);
    }

    @Step("Check if Reminder icon is present in Trouble Ticket Table")
    public boolean isReminderPresent(int cellIndex, String reminderText) {
        String iconTitles = getTroubleTicketsTable().getCellValue(cellIndex, "State");
        log.info("Check if Reminder icon is present in Trouble Ticket Table");
        return iconTitles.contains(reminderText);
    }

    @Step("get row number for ticket with {id}")
    public int getRowForTicketWithID(String id) {
        return getTroubleTicketsTable().getRowNumber(id, "ID");
    }
}