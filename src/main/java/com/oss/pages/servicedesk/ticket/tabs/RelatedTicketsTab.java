package com.oss.pages.servicedesk.ticket.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.ticket.wizard.SDWizardPage;

import io.qameta.allure.Step;

public class RelatedTicketsTab extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(RelatedTicketsTab.class);

    private static final String LINK_TICKETS_ID = "LINK_TICKETS";
    private static final String RELATED_TICKETS_TABLE_ID_ATTRIBUTE_ID = "ID";
    private static final String RELATED_TICKETS_TABLE_ID = "_relatedTicketsTableWidget";
    private static final String UNLINK_TICKET_ID = "UNLINK_TICKET";
    private static final String CONFIRM_UNLINK_TICKET_BUTTON_LABEL = "Unlink";
    private static final String SHOW_ARCHIVED_SWITCHER_ID = "_relatedTicketsSwitcherApp";

    public RelatedTicketsTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Open link ticket prompt")
    public SDWizardPage openLinkTicketWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(LINK_TICKETS_ID);
        log.info("Link Tickets prompt is opened");
        return new SDWizardPage(driver, wait);
    }

    @Step("Check related tickets ID")
    public String checkRelatedTicketsId(int ticketIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check related tickets ID");
        return getRelatedTicketTable().getCellValue(ticketIndex, RELATED_TICKETS_TABLE_ID_ATTRIBUTE_ID);
    }

    @Step("Select ticket with index: {index}")
    public void selectTicket(int index) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Selecting ticket in row: {}", index);
        getRelatedTicketTable().selectRow(index);
    }

    @Step("Check Related Tickets table")
    public boolean isRelatedTicketsTableEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check if Related Tickets Table is empty");
        return getRelatedTicketTable().hasNoData();
    }

    @Step("Click unlink Ticket from Related Tickets Tab")
    public void unlinkTicket() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(UNLINK_TICKET_ID);
        log.info("Unlink Ticket popup is opened");
    }

    @Step("Confirm unlinking ticket")
    public void confirmUnlinking() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ConfirmationBox.create(driver, wait).clickButtonByLabel(CONFIRM_UNLINK_TICKET_BUTTON_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Confirm unlinking ticket");
    }

    @Step("Turn On Show archived switcher")
    public void turnOnShowArchived() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Turn On Show archived switcher");
        ComponentFactory.create(SHOW_ARCHIVED_SWITCHER_ID, Input.ComponentType.SWITCHER, driver, wait)
                .setSingleStringValue(Boolean.TRUE.toString());
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private OldTable getRelatedTicketTable() {
        return OldTable.createById(driver, wait, RELATED_TICKETS_TABLE_ID);
    }
}

