package com.oss.pages.servicedesk.issue.ticket;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.servicedesk.issue.RemainderForm;
import com.oss.pages.servicedesk.issue.tabs.OverviewTab;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.COMMON_WIZARD_ID;

public class TicketOverviewTab extends OverviewTab {

    private static final Logger log = LoggerFactory.getLogger(TicketOverviewTab.class);

    private static final String EDIT_DETAILS_LABEL = "Edit details";
    private static final String RELEASE_BUTTON_LABEL = "Release";
    private static final String ALLOW_EDIT_LABEL = "Edit";
    private static final String ADD_REMAINDER_LABEL = "Add Reminder";
    private static final String EDIT_REMAINDER_LABEL = "Edit Reminder";
    private static final String REMOVE_REMAINDER_LABEL = "Remove Reminder";
    private static final String MORE_BUTTON_LABEL = "More";
    private static final String CHANGE_TICKET_STATUS_COMBOBOX_ID = "change-ticket-status-combobox-input";

    public TicketOverviewTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Open edit ticket wizard")
    public SDWizardPage openEditTicketWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getDetailsViewOldActionsContainer().callActionByLabel(MORE_BUTTON_LABEL, EDIT_DETAILS_LABEL);
        log.info("Ticket Wizard edit is opened");
        return new SDWizardPage(driver, wait, COMMON_WIZARD_ID);
    }

    @Step("Click release ticket")
    public void releaseTicket() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getDetailsViewOldActionsContainer().callActionByLabel(RELEASE_BUTTON_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking release button");
    }

    @Step("Allow ticket editing")
    public void allowEditingTicket() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getDetailsViewOldActionsContainer().callActionByLabel(ALLOW_EDIT_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking edit button");
    }

    @Step("Click Add Remainder")
    public RemainderForm clickAddRemainder() {
        getDetailsViewOldActionsContainer().callActionByLabel(MORE_BUTTON_LABEL, ADD_REMAINDER_LABEL);
        log.info("Clicking Add Remainder");
        return new RemainderForm(driver, wait);
    }

    @Step("Click Edit Remainder")
    public RemainderForm clickEditRemainder() {
        getDetailsViewOldActionsContainer().callActionByLabel(MORE_BUTTON_LABEL, EDIT_REMAINDER_LABEL);
        log.info("Clicking Edit Remainder");
        return new RemainderForm(driver, wait);
    }

    @Step("Click Remove Remainder")
    public void clickRemoveRemainder() {
        getDetailsViewOldActionsContainer().callActionByLabel(MORE_BUTTON_LABEL, REMOVE_REMAINDER_LABEL);
        log.info("Clicking Remove Remainder");
    }

    @Step("Changing status to {statusName}")
    public void changeTicketStatus(String statusName) {
        ComponentFactory.create(CHANGE_TICKET_STATUS_COMBOBOX_ID, Input.ComponentType.COMBOBOX, driver, wait).setSingleStringValue(statusName);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Changing status to {}", statusName);
    }

    @Step("Check status in ticket status combobox")
    public String checkTicketStatus() {
        return ComponentFactory.create(CHANGE_TICKET_STATUS_COMBOBOX_ID, Input.ComponentType.COMBOBOX, driver, wait).getStringValue();
    }
}
