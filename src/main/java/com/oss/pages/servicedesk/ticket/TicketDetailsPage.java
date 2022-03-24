package com.oss.pages.servicedesk.ticket;

import java.util.List;

import com.oss.framework.components.prompts.ConfirmationBox;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.layout.Card;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.iaa.widgets.list.ListApp;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabWindowWidget;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.ticket.wizard.AttachmentWizardPage;
import com.oss.pages.servicedesk.ticket.wizard.SDWizardPage;

import io.qameta.allure.Step;

public class TicketDetailsPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(TicketDetailsPage.class);

    public static final String DETAILS_PAGE_URL_PATTERN = "%s/#/view/service-desk/trouble-ticket/details/%s";
    private static final String EDIT_DETAILS_LABEL = "Edit details";
    private static final String RELEASE_ID = "_detailsOverviewContextActions-7";
    private static final String ALLOW_EDIT_LABEL = "Edit";
    private static final String CREATE_SUB_TICKET = "TT_DETAILS_SUBTICKET_CREATE_PROMPT_TITLE";
    private static final String CHECKLIST_APP_ID = "_checklistApp";
    private static final String SKIP_BUTTON_LABEL = "SKIP";
    private static final String EXTERNAL_LIST_ID = "_detailsExternalsListApp";
    private static final String EXTERNAL_INFO_LABEL = "External Info";
    private static final String DICTIONARIES_TABLE_ID = "_dictionariesTableId";
    private static final String DICTIONARY_VALUE_TABLE_LABEL = "Dictionary Value";
    private static final String CHANGE_TICKET_STATUS_COMBOBOX_ID = "change-ticket-status-combobox-input";
    private static final String ACTIONS_CONTAINER_ID = "_detailsWindow-windowToolbar";
    private static final String ADD_REMAINDER_LABEL = "Add Reminder";
    private static final String EDIT_REMAINDER_LABEL = "Edit Reminder";
    private static final String REMOVE_REMAINDER_LABEL = "Remove Reminder";
    private static final String MORE_DETAILS_LABEL = "More details";
    private static final String SAME_MO_TT_TABLE_ID = "_sameMOTTTableWidget";
    private static final String LINK_TICKETS_ID = "LINK_TICKETS";
    private static final String UNLINK_TICKET_ID = "UNLINK_TICKET";
    private static final String CONFIRM_UNLINK_TICKET_BUTTON_LABEL = "Unlink";
    private static final String SHOW_ARCHIVED_SWITCHER_ID = "_relatedTicketsSwitcherApp";
    private static final String ADD_ROOT_CAUSE_ID = "_addRootCause";
    private static final String ADD_PARTICIPANT_ID = "_createParticipant";
    private static final String PARTICIPANTS_TABLE_ID = "_participantsTableApp";
    private static final String PARTICIPANTS_TABLE_FIRST_NAME_ATTRIBUTE_ID = "First Name";
    private static final String PARTICIPANTS_TABLE_SURNAME_ATTRIBUTE_ID = "Surname";
    private static final String PARTICIPANTS_TABLE_ROLE_ATTRIBUTE_ID = "Role";
    private static final String CREATE_PROBLEM_ID = "_createProblem";
    private static final String RELATED_PROBLEMS_TABLE_ID = "_relatedProblemsApp";
    private static final String RELATED_PROBLEMS_TABLE_NAME_ATTRIBUTE_ID = "Name";
    private static final String RELATED_PROBLEMS_TABLE_ASSIGNEE_ATTRIBUTE_ID = "Assignee";
    private static final String RELATED_PROBLEMS_TABLE_LABEL_ATTRIBUTE_ID = "Label";
    private static final String RELATED_TICKETS_TABLE_ID = "_relatedTicketsTableWidget";
    private static final String RELATED_TICKETS_TABLE_ID_ATTRIBUTE_ID = "ID";
    private static final String ROOT_CAUSES_TABLE_ID = "_rootCausesApp";
    private static final String ROOT_CAUSES_TABLE_ID_MO_IDENTIFIER_ID = "MO Identifier";
    private static final String ATTACHMENT_WIZARD_ID = "addFileComponentId";
    private static final String ATTACH_FILE_LABEL = "Attach file";
    private static final String ATTACHMENTS_LIST_ID = "attachmentManagerBusinessView_commonList";
    private static final String OWNER_COLUMN_NAME = "Owner";
    private static final String DOWNLOAD_ATTACHMENT_LABEL = "DOWNLOAD";
    private static final String DELETE_ATTACHMENT_LABEL = "Delete";
    private static final String CONFIRM_DELETE_ATTACHMENT_BUTTON_ID = "ConfirmationBox_removeAttachmentPopup_confirmationBox_action_button";

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
        OldActionsContainer.createById(driver,wait, ACTIONS_CONTAINER_ID).callActionById(RELEASE_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking release button");
    }

    @Step("Allow ticket editing")
    public void allowEditingTicket() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldActionsContainer.createById(driver, wait,ACTIONS_CONTAINER_ID).callActionByLabel(ALLOW_EDIT_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking edit button");
    }

    @Step("Click Context action with label {contextActionLabel}")
    public void clickContextAction(String contextActionLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabWindowWidget.create(driver, wait).callActionByLabel(contextActionLabel);
        log.info("Clicking Context action {}", contextActionLabel);
    }

    @Step("Click Context action from button with label {contextActionLabel}")
    public void clickContextActionFromButtonContainer(String contextActionLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonContainer.create(driver, wait).callActionById(contextActionLabel);
        log.info("Clicking Context action {}", contextActionLabel);
    }

    @Step("Selecting tab {tabAriaControls}")
    public void selectTab(String tabAriaControls) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabWindowWidget.create(driver, wait).selectTabById(tabAriaControls);
        log.info("Selecting tab {}", tabAriaControls);
    }

    @Step("I open create subticket wizard for flow {flowType}")
    public SDWizardPage openCreateSubTicketWizard(String flowType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, CREATE_SUB_TICKET).click();
        DropdownList.create(driver, wait).selectOptionById(flowType);
        log.info("Create subticket wizard for {} is opened", flowType);
        return new SDWizardPage(driver, wait);
    }

    @Step("Skipping all actions on checklist")
    public void skipAllActionsOnCheckList() {
        CommonList.create(driver, wait, CHECKLIST_APP_ID)
                .getRows()
                .forEach(row -> {
                    DelayUtils.waitForPageToLoad(driver, wait);
                    row.callActionIcon(SKIP_BUTTON_LABEL);
                });
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Skipping all actions on checklist");
    }

    @Step("Changing status to {statusName}")
    public void changeStatus(String statusName) {
        ComponentFactory.create(CHANGE_TICKET_STATUS_COMBOBOX_ID, Input.ComponentType.COMBOBOX, driver, wait).setSingleStringValue(statusName);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Changing status to {}", statusName);
    }

    @Step("Check status in ticket status combobox")
    public String checkStatus() {
        return ComponentFactory.create(CHANGE_TICKET_STATUS_COMBOBOX_ID, Input.ComponentType.COMBOBOX, driver, wait).getStringValue();
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

    @Step("Checking if expected external {expectedExistingExternal} exists on the list")
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
        return OldTable.createById(driver, wait, DICTIONARIES_TABLE_ID).getCellValue(0, DICTIONARY_VALUE_TABLE_LABEL);
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
        List<CommonList.Row> allRows = CommonList.create(driver, wait, CHECKLIST_APP_ID).getRows();
        for (CommonList.Row row : allRows) {
            if (!row.getValue("Status").equals("Skipped")) {
                return false;
            }
        }
        return true;
    }

    @Step("get id of opened ticket")
    public String getOpenedTicketId() {
        String viewTitle = ToolbarWidget.create(driver, wait).getViewTitle();
        String[] viewWithID = viewTitle.split("#");
        return viewWithID[1];
    }

    @Step("Click Add Remainder")
    public RemainderForm clickAddRemainder() {
        clickContextAction(ADD_REMAINDER_LABEL);
        log.info("Clicking Add Remainder");
        return new RemainderForm(driver, wait);
    }

    @Step("Click Edit Remainder")
    public RemainderForm clickEditRemainder() {
        clickContextAction(EDIT_REMAINDER_LABEL);
        log.info("Clicking Edit Remainder");
        return new RemainderForm(driver, wait);
    }

    @Step("Click Remove Remainder")
    public void clickRemoveRemainder() {
        clickContextAction(REMOVE_REMAINDER_LABEL);
        log.info("Clicking Remove Remainder");
    }

    @Step("Click More Details")
    public MoreDetailsPage clickMoreDetails() {
        clickContextAction(MORE_DETAILS_LABEL);
        log.info("Clicking More Details");
        return new MoreDetailsPage(driver, wait);
    }

    @Step("I check Same MO TT table")
    public boolean checkIfSameMOTTTableIsNotEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check Same MO TT Table");
        return !OldTable.createById(driver, wait, SAME_MO_TT_TABLE_ID).hasNoData();
    }

    @Step("I open link ticket wizard")
    public SDWizardPage openLinkTicketWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(LINK_TICKETS_ID);
        log.info("Link Tickets Wizard is opened");
        return new SDWizardPage(driver, wait);
    }

    @Step("I open add root cause wizard")
    public SDWizardPage openAddRootCauseWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(ADD_ROOT_CAUSE_ID);
        log.info("Add Root Cause Wizard is opened");
        return new SDWizardPage(driver, wait);
    }

    @Step("I open add participant wizard")
    public SDWizardPage openAddParticipantWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(ADD_PARTICIPANT_ID);
        log.info("Add Participant Wizard is opened");
        return new SDWizardPage(driver, wait);
    }

    @Step("I check participant first name")
    public String checkParticipantFirstName(int participantIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check participant first name");
        return checkParticipantData(participantIndex, PARTICIPANTS_TABLE_FIRST_NAME_ATTRIBUTE_ID);
    }

    @Step("I check participant surname")
    public String checkParticipantSurname(int participantIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check participant surname");
        return checkParticipantData(participantIndex, PARTICIPANTS_TABLE_SURNAME_ATTRIBUTE_ID);
    }

    @Step("I check participant role")
    public String checkParticipantRole(int participantIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check participant role");
        return checkParticipantData(participantIndex, PARTICIPANTS_TABLE_ROLE_ATTRIBUTE_ID);
    }

    private String checkParticipantData(int participantIndex, String attributeId) {
        return checkOldTableData(PARTICIPANTS_TABLE_ID, participantIndex, attributeId);
    }

    @Step("I open Create Problem wizard")
    public SDWizardPage openCreateProblemWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(CREATE_PROBLEM_ID);
        log.info("Create Problem Wizard is opened");
        return new SDWizardPage(driver, wait);
    }

    @Step("I check related problem name")
    public String checkRelatedProblemName(int problemIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check related problem name");
        return checkRelatedProblemsData(problemIndex, RELATED_PROBLEMS_TABLE_NAME_ATTRIBUTE_ID);
    }

    @Step("I check related problem assignee")
    public String checkRelatedProblemAssignee(int problemIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check related problem assignee");
        return checkRelatedProblemsData(problemIndex, RELATED_PROBLEMS_TABLE_ASSIGNEE_ATTRIBUTE_ID);
    }

    @Step("I check related problem label")
    public String checkRelatedProblemLabel(int problemIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check related problem label");
        return checkRelatedProblemsData(problemIndex, RELATED_PROBLEMS_TABLE_LABEL_ATTRIBUTE_ID);
    }

    private String checkRelatedProblemsData(int problemIndex, String attributeId) {
        return checkOldTableData(RELATED_PROBLEMS_TABLE_ID, problemIndex, attributeId);
    }

    @Step("I check related tickets ID")
    public String checkRelatedTicketsId(int ticketIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check related tickets ID");
        return checkRelatedTicketsData(ticketIndex, RELATED_TICKETS_TABLE_ID_ATTRIBUTE_ID);
    }

    private String checkRelatedTicketsData(int ticketIndex, String attributeId) {
        return checkOldTableData(RELATED_TICKETS_TABLE_ID, ticketIndex, attributeId);
    }

    @Step("I check root cause  - MO Identifier")
    public String checkRootCausesMOIdentifier(int objectIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check root cause  - MO Identifier");
        return checkRootCausesData(objectIndex, ROOT_CAUSES_TABLE_ID_MO_IDENTIFIER_ID);
    }

    @Step("I check if MO Identifier is present on Root Causes tab")
    public boolean checkIfMOIdentifierIsPresentOnRootCauses(String MOIdentifier) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I check if MO Identifier is present on Root Causes tab");
        int numberOfRows = OldTable.createById(driver, wait, ROOT_CAUSES_TABLE_ID).countRows(ROOT_CAUSES_TABLE_ID_MO_IDENTIFIER_ID);
        for (int i = 0; i < numberOfRows; i++) {
            if (checkRootCausesData(i, ROOT_CAUSES_TABLE_ID_MO_IDENTIFIER_ID).equals(MOIdentifier)) {
                return true;
            }
        }
        return false;
    }

    @Step("Click Attach File")
    public AttachmentWizardPage clickAttachFile() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextAction(ATTACH_FILE_LABEL);
        return new AttachmentWizardPage(driver, wait, ATTACHMENT_WIZARD_ID);
    }

    @Step("Check if Attachment List is empty")
    public boolean isAttachmentListEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getAttachmentList().hasNoData();
    }

    @Step("Check if Attachment owner is visible")
    public String getAttachmentOwner() {
        return getAttachmentList().getRows().get(0).getValue(OWNER_COLUMN_NAME);
    }

    @Step("Click Download Attachment")
    public void clickDownloadAttachment() {
        getAttachmentList().getRows().get(0).callActionIcon(DOWNLOAD_ATTACHMENT_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click Delete Attachment")
    public void clickDeleteAttachment() {
        getAttachmentList().getRows().get(0).callAction(DELETE_ATTACHMENT_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createById(driver, CONFIRM_DELETE_ATTACHMENT_BUTTON_ID).click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private String checkRootCausesData(int objectIndex, String attributeId) {
        return checkOldTableData(ROOT_CAUSES_TABLE_ID, objectIndex, attributeId);
    }

    private String checkOldTableData(String tableId, int index, String attributeId) {
        return OldTable.createById(driver, wait, tableId).getCellValue(index, attributeId);
    }

    private CommonList getAttachmentList() {
        return CommonList.create(driver, wait, ATTACHMENTS_LIST_ID);
    }

    public void selectTicketInRelatedTicketsTab(int index) {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable.createById(driver, wait, RELATED_TICKETS_TABLE_ID).selectRow(index);
    }

    public void unlinkTicketFromRelatedTicketsTab() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(UNLINK_TICKET_ID);
        log.info("Unlink Ticket popup is opened");
    }

    public void confirmUnlinkingTicketFromRelatedTicketsTab() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ConfirmationBox.create(driver, wait).clickButtonByLabel(CONFIRM_UNLINK_TICKET_BUTTON_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I check Related Tickets table")
    public boolean checkIfRelatedTicketsTableIsEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check if Related Tickets Table is empty");
        return OldTable.createById(driver, wait, RELATED_TICKETS_TABLE_ID).hasNoData();
    }

    public void turnOnShowArchived() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Turn On Show archived switcher");
        ComponentFactory.create(SHOW_ARCHIVED_SWITCHER_ID, Input.ComponentType.SWITCHER, driver, wait).setSingleStringValue("true");
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
