package com.oss.pages.servicedesk.ticket;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.servicedesk.BaseSDPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.pages.servicedesk.URLConstants.VIEWS_URL_PATTERN;
import static com.oss.pages.servicedesk.ticket.TicketDetailsPage.DETAILS_PAGE_URL_PATTERN;

public class TicketSearchPage extends BaseSDPage {

    public static final String ID_ATTRIBUTE = "id";
    public static final String ASSIGNEE_ATTRIBUTE = "assignee";
    public static final String CREATION_TIME_ATTRIBUTE = "createDate";
    public static final String SEVERITY_ATTRIBUTE = "severity";
    public static final String STATUS_ATTRIBUTE = "ticketOut.issueOut.status.name";

    private static final Logger log = LoggerFactory.getLogger(TicketSearchPage.class);
    private static final String TABLE_WIDGET_ID = "ticket-search-graphql-table";
    private static final String TICKET_SEARCH = "ticket-search";

    public TicketSearchPage(WebDriver driver) {
        super(driver);
    }

    @Step("I Open Ticket Search View")
    public TicketSearchPage goToPage(WebDriver driver, String basicURL) {
        openPage(driver, String.format(VIEWS_URL_PATTERN, basicURL, TICKET_SEARCH));
        log.info("Ticket Search View is opened");
        return new TicketSearchPage(driver);
    }

    @Step("I filter tickets by text attribute {attributeName} set to {attributeValue}")
    public Boolean filterByTextField(String attributeName, String attributeValue) {
        log.info("Filtering tickets by text attribute {} set to {}", attributeName, attributeValue);
        return filterBy(attributeName, attributeValue, Input.ComponentType.TEXT_FIELD);
    }

    @Step("I filter tickets by combo-box attribute {attributeName} set to {attributeValue}")
    public Boolean filterByComboBox(String attributeName, String attributeValue) {
        log.info("Filtering tickets by combo-box attribute {} set to {}", attributeName, attributeValue);
        return filterBy(attributeName, attributeValue, Input.ComponentType.COMBOBOXV2);
    }

    @Step("I open details view for {rowIndex} ticket in Ticket table")
    public TicketDetailsPage openTicketDetailsView(String rowIndex, String basicURL) {
        // TODO: for now we cannot just click link - not implemented yet
        // (see TableWidget#selectLinkInSpecificColumn)
        String ticketId = getTicketTable().getCellValue(Integer.parseInt(rowIndex), ID_ATTRIBUTE);
        log.info("Opening ticket details for ticket with id: {}", ticketId);
        openPage(driver, String.format(DETAILS_PAGE_URL_PATTERN, basicURL, ticketId));
        return new TicketDetailsPage(driver);
    }

    public String getAssigneeForNthTicketInTable(int n) {
        return getAttributeFromTable(n, ASSIGNEE_ATTRIBUTE);
    }

    public String getIdForNthTicketInTable(int n) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getAttributeFromTable(n, ID_ATTRIBUTE);
    }

    private String getAttributeFromTable(int index, String attributeName) {
        String attributeValue = getTicketTable().getCellValue(index, attributeName);
        log.info("Got value for {} attribute: {}", attributeName, attributeValue);
        return attributeValue;
    }

    private TableWidget getTicketTable() {
        return getTable(driver, wait, TABLE_WIDGET_ID);
    }

    public TableWidget getTable(WebDriver driver, WebDriverWait wait, String tableWidgetId) {
        return TableWidget.createById(driver, tableWidgetId, wait);
    }

    public Boolean filterBy(String attributeName, String attributeValue, Input.ComponentType componentType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableWidget table = getTicketTable();
        table.searchByAttribute(attributeName, componentType, attributeValue);
        DelayUtils.waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = table.getRowsNumber();
        return numberOfRowsInTable == 1;
    }
}
