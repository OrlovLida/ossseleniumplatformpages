package com.oss.pages.servicedesk.ticket;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.servicedesk.BaseSDPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.URLConstants.VIEWS_URL_PATTERN;
import static com.oss.pages.servicedesk.ticket.TicketDetailsPage.DETAILS_PAGE_URL_PATTERN;

public class TicketSearchPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(TicketSearchPage.class);

    public static final String ID_ATTRIBUTE = "id";
    public static final String ASSIGNEE_ATTRIBUTE = "ticketOut.issueOut.assignee.name";
    public static final String CREATION_TIME_ATTRIBUTE = "createDate";
    public static final String STATUS_ATTRIBUTE = "ticketOut.issueOut.status.name";
    public static final String DESCRIPTION_ATTRIBUTE = "incidentDescription";

    private static final String TABLE_WIDGET_ID = "ticket-search-graphql-table";
    private static final String TICKET_SEARCH = "ticket-search";

    public TicketSearchPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open Ticket Search View")
    public TicketSearchPage goToPage(WebDriver driver, String basicURL) {
        openPage(driver, String.format(VIEWS_URL_PATTERN, basicURL, TICKET_SEARCH));
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Ticket Search View is opened");
        return new TicketSearchPage(driver, wait);
    }

    @Step("I filter tickets by text attribute {attributeName} set to {attributeValue}")
    public void filterByTextField(String attributeName, String attributeValue) {
        log.info("Filtering tickets by text attribute {} set to {}", attributeName, attributeValue);
        filterBy(attributeName, attributeValue, Input.ComponentType.TEXT_FIELD);
    }

    @Step("I filter tickets by combo-box attribute {attributeName} set to {attributeValue}")
    public void filterByComboBox(String attributeName, String attributeValue) {
        log.info("Filtering tickets by combo-box attribute {} set to {}", attributeName, attributeValue);
        filterBy(attributeName, attributeValue, Input.ComponentType.MULTI_COMBOBOX);
    }

    @Step("I open details view for {rowIndex} ticket in Ticket table")
    public TicketDetailsPage openTicketDetailsView(String rowIndex, String basicURL) {
        // TODO: for now we cannot just click link - not implemented yet
        // (see TableWidget#clickLink)
        String ticketId = getTicketTable().getCellValue(Integer.parseInt(rowIndex), ID_ATTRIBUTE);
        log.info("Opening ticket details for ticket with id: {}", ticketId);
        openPage(driver, String.format(DETAILS_PAGE_URL_PATTERN, basicURL, ticketId));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new TicketDetailsPage(driver, wait);
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

    public void filterBy(String attributeName, String attributeValue, Input.ComponentType componentType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTicketTable().searchByAttribute(attributeName, componentType, attributeValue);
    }
}
