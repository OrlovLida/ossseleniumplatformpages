package com.oss.pages.servicedesk.ticket;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.listwidget.iaa.ListApp;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.view.Card;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.ticket.wizard.SDWizardPage;

import io.qameta.allure.Step;

public class TicketDetailsPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(TicketDetailsPage.class);

    public static final String DETAILS_PAGE_URL_PATTERN = "%s/#/view/service-desk/trouble-ticket/details/%s";

    private static final String EDIT_DETAILS_LABEL = "Edit details";
    private static final String RELEASE_LABEL = "Release";
    private static final String ALLOW_EDIT_LABEL = "Edit";
    private static final String CREATE_SUB_TICKET = "TT_DETAILS_SUBTICKET_CREATE_PROMPT_TITLE";
    private static final String CHECKLIST_APP_ID = "_checklistApp";
    private static final String SKIP_BUTTON_LABEL = "SKIP";
    private static final String EXTERNAL_LIST_ID = "_detailsExternalsListApp";
    private static final String EXTERNAL_INFO_LABEL = "External Info";
    private static final String DICTIONARIES_TABLE_ID = "_dictionariesTableId";
    private static final String DICTIONARY_VALUE_TABLE_LABEL = "Dictionary Value";
    private static final String CHANGE_TICKET_STATUS_COMBOBOX_ID = "change-ticket-status-combobox-input";

    public TicketDetailsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open edit ticket wizard")
    public SDWizardPage openEditTicketWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextAction(EDIT_DETAILS_LABEL);
        log.info("Ticket Wizard edit is opened");
        return new SDWizardPage(driver, wait);
    }

    @Step("Click release ticket")
    public void releaseTicket() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.create(driver, RELEASE_LABEL).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking release button");
    }

    @Step("Allow ticket editing")
    public void allowEditingTicket() {
        Button.create(driver, ALLOW_EDIT_LABEL).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking edit button");
    }

    public void clickContextAction(String contextActionLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabWindowWidget.create(driver, wait).callActionByLabel(contextActionLabel);
        log.info("Clicking Context action {}", contextActionLabel);
    }

    public void selectTab(String tabAriaControls) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabWindowWidget.create(driver, wait).selectTabById(tabAriaControls);
        log.info("Selecting tab {}", tabAriaControls);
    }

    @Step("I open create subticket wizard for flow {flowType}")
    public SDWizardPage openCreateSubTicketWizard(String flowType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createBySelectorAndId(driver, "button", CREATE_SUB_TICKET).click();
        DropdownList.create(driver, wait).selectOptionWithId(flowType);
        log.info("Create subticket wizard for {} is opened", flowType);
        return new SDWizardPage(driver, wait);
    }

    public void skipAllActionsOnCheckList() {
        CommonList.create(driver, wait, CHECKLIST_APP_ID)
                .getAllRows()
                .forEach(row -> row.callActionIcon(SKIP_BUTTON_LABEL));
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Skipping all actions on checklist");
    }

    public void changeStatus(String statusName) {
        ComponentFactory.create(CHANGE_TICKET_STATUS_COMBOBOX_ID, Input.ComponentType.COMBOBOX, driver, wait).setSingleStringValue(statusName);
        log.info("Changing status to {}", statusName);
    }

    @Step("Maximize window")
    public void maximizeWindow(String windowId) {
        Card.createCard(driver, wait, windowId).maximizeCard();
        log.info("Maximizing window");
    }

    @Step("Minimize window")
    public void minimizeWindow(String windowId) {
        Card.createCard(driver, wait, windowId).minimizeCard();
        log.info("Minimizing window");
    }

    public boolean checkExistingExternal(String expectedExistingExternal) {
        DelayUtils.sleep(5000);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Checking if expected external '{}' exists on the list", expectedExistingExternal);
        if (CommonList.create(driver, wait, EXTERNAL_LIST_ID).isRowDisplayed(EXTERNAL_INFO_LABEL, expectedExistingExternal)) {
            log.info("Expected external '{}' exists on the list", expectedExistingExternal);
            return true;
        } else {
            log.info("Expected external '{}' does not exists on the list", expectedExistingExternal);
            return false;
        }
    }

    public String checkExistingDictionary() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentId(driver, wait, DICTIONARIES_TABLE_ID).getCellValue(0, DICTIONARY_VALUE_TABLE_LABEL);
    }

    public boolean checkDisplayedText(String expectedText, String windowId) {
        List<String> text = ListApp.createFromParent(driver, wait, windowId).getValue();
        if (text.contains(expectedText)) {
            log.debug("Expected text {} is displayed", expectedText);
            return true;
        } else {
            log.debug("Expected text {} is not displayed", expectedText);
            return false;
        }
    }

    public boolean isAllActionsSkipped() {
        List<CommonList.Row> allRows = CommonList.create(driver, wait, CHECKLIST_APP_ID).getAllRows();
        for (CommonList.Row row : allRows) {
            if (!row.getValue("Status").equals("Skipped")) {
                return false;
            }
        }
        return true;
    }
}
