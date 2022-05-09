package com.oss.pages.servicedesk.issue.tabs;

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
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

public class RelatedTicketsTab extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(RelatedTicketsTab.class);

    private static final String LINK_ISSUE_BUTTON_ID = "LINK_TICKETS_ACTION";
    private static final String RELATED_TICKETS_TABLE_ID_ATTRIBUTE_ID = "ID";
    private static final String RELATED_TICKETS_TABLE_ID = "_relatedTicketsTableWidget";
    private static final String UNLINK_ISSUE_BUTTON_ID = "UNLINK_TICKET";
    private static final String CONFIRM_UNLINK_TICKET_BUTTON_LABEL = "Unlink";
    private static final String SHOW_ARCHIVED_SWITCHER_ID = "_relatedTicketsSwitcherApp";
    private static final String RELATED_TICKETS_EXPORT_FILE = "TroubleTicket*.xlsx";
    private static final String TABS_CONTAINER_ID = "_tablesWindow";
    private static final String LINK_TICKET_PROMPT_ID = "_linkSubTicketModal_prompt-card";

    public RelatedTicketsTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Open link ticket prompt")
    public SDWizardPage openLinkTicketWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(LINK_ISSUE_BUTTON_ID);
        log.info("Link Tickets prompt is opened");
        return new SDWizardPage(driver, wait, LINK_TICKET_PROMPT_ID);
    }

    public boolean isLinkTicketButtonActive() {
        return isActionPresent(LINK_ISSUE_BUTTON_ID);
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
        clickContextActionFromButtonContainer(UNLINK_ISSUE_BUTTON_ID);
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

    @Step("Click Export")
    public void clickExport() {
        clickExportFromTab(TABS_CONTAINER_ID);
    }

    @Step("Attach file with Related Tickets to report")
    public void attachRelatedTicketsFile() {
        attachFileToReport(RELATED_TICKETS_EXPORT_FILE);
    }

    @Step("Check if exported file with Related Tickets is not empty")
    public boolean isRelatedTicketsFileNotEmpty() {
        return checkIfFileIsNotEmpty(RELATED_TICKETS_EXPORT_FILE);
    }

    private OldTable getRelatedTicketTable() {
        return OldTable.createById(driver, wait, RELATED_TICKETS_TABLE_ID);
    }
}

