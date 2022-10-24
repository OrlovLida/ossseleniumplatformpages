package com.oss.pages.iaa.servicedesk.issue.ticket;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.servicedesk.issue.RemainderForm;
import com.oss.pages.iaa.servicedesk.issue.tabs.OverviewTab;
import com.oss.pages.iaa.servicedesk.issue.wizard.ChangeAttributeWizardPage;

import io.qameta.allure.Step;

public class TicketOverviewTab extends OverviewTab {

    private static final Logger log = LoggerFactory.getLogger(TicketOverviewTab.class);

    private static final String RELEASE_BUTTON_ID = "CAN_SAVE";
    private static final String ALLOW_EDIT_ID = "CAN_EDIT";
    private static final String ADD_REMAINDER_ID = "AddReminderButtonId";
    private static final String EDIT_REMAINDER_ID = "EditReminderButtonId";
    private static final String REMOVE_REMAINDER_ID = "RemoveReminderButtonId";
    private static final String CHANGE_TICKET_STATUS_COMBOBOX_ID = "change-ticket-status-combobox";
    private static final String TEST_REASON = "Test_Selenium";
    private static final String ATTRIBUTE_WIZARD_ID = "_configureStepPrompt_prompt-card";

    private ChangeAttributeWizardPage changeAttributeWizardPage;

    public TicketOverviewTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Click release ticket")
    public void releaseTicket() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getDetailsViewOldActionsContainer().callActionById(RELEASE_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking release button");
    }

    @Step("Allow ticket editing")
    public void allowEditingTicket() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getDetailsViewOldActionsContainer().callActionById(ALLOW_EDIT_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking edit button");
    }

    @Step("Click Add Remainder")
    public RemainderForm clickAddRemainder() {
        getDetailsViewOldActionsContainer().callActionById(ADD_REMAINDER_ID);
        log.info("Clicking Add Remainder");
        return new RemainderForm(driver, wait);
    }

    @Step("Click Edit Remainder")
    public RemainderForm clickEditRemainder() {
        getDetailsViewOldActionsContainer().callActionById(EDIT_REMAINDER_ID);
        log.info("Clicking Edit Remainder");
        return new RemainderForm(driver, wait);
    }

    @Step("Click Remove Remainder")
    public void clickRemoveRemainder() {
        getDetailsViewOldActionsContainer().callActionById(REMOVE_REMAINDER_ID);
        log.info("Clicking Remove Remainder");
    }

    @Step("Changing status to {statusName}")
    public void changeTicketStatus(String statusName) {
        ComponentFactory.create(CHANGE_TICKET_STATUS_COMBOBOX_ID, driver, wait).setSingleStringValue(statusName);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Changing status to {}", statusName);
    }

    @Step("Check status in ticket status combobox")
    public String checkTicketStatus() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return ComponentFactory.create(CHANGE_TICKET_STATUS_COMBOBOX_ID, driver, wait).getStringValue();
    }

    @Step("Fill Reason for Change in attribute wizzard and Save changes")
    public void fillReasonChange (WebDriverWait webDriverWait, WebDriver driver){
        ChangeAttributeWizardPage changeAttributeWizard = new ChangeAttributeWizardPage (driver, webDriverWait, ATTRIBUTE_WIZARD_ID);
        changeAttributeWizard.fillReasonforChange(TEST_REASON);
        changeAttributeWizard.clickSave();
    }
}
