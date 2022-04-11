package com.oss.pages.servicedesk.ticket;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.iaa.widgets.list.ListApp;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabWindowWidget;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.ticket.tabs.AttachmentsTab;
import com.oss.pages.servicedesk.ticket.tabs.ExternalTab;
import com.oss.pages.servicedesk.ticket.tabs.MessagesTab;
import com.oss.pages.servicedesk.ticket.tabs.ParticipantsTab;
import com.oss.pages.servicedesk.ticket.tabs.RelatedProblemsTab;
import com.oss.pages.servicedesk.ticket.tabs.RelatedTicketsTab;
import com.oss.pages.servicedesk.ticket.tabs.RootCausesTab;
import com.oss.pages.servicedesk.ticket.wizard.SDWizardPage;

import io.qameta.allure.Step;

public class IssueDetailsPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(IssueDetailsPage.class);

    public static final String DETAILS_PAGE_URL_PATTERN = "%s/#/view/service-desk/%s/details/%s";

    private static final String EDIT_DETAILS_LABEL = "Edit details";
    private static final String RELEASE_ID = "_detailsOverviewContextActions-7";
    private static final String ACTIONS_CONTAINER_ID = "_detailsWindow-windowToolbar";
    private static final String CREATE_SUB_TICKET = "TT_DETAILS_SUBTICKET_CREATE_PROMPT_TITLE";
    private static final String ALLOW_EDIT_LABEL = "Edit";
    private static final String PROBLEM_ASSIGNEE_ID = "assignee";
    private static final String PROBLEM_STATUS_ID = "status-input";
    private static final String CHECKLIST_APP_ID = "_checklistApp";
    private static final String SKIP_BUTTON_LABEL = "SKIP";
    private static final String EXTERNAL_TAB_ARIA_CONTROLS = "_detailsExternalTab";
    private static final String DICTIONARIES_TABLE_ID = "_dictionariesTableId";
    private static final String DICTIONARY_VALUE_TABLE_LABEL = "Dictionary Value";
    private static final String CHANGE_TICKET_STATUS_COMBOBOX_ID = "change-ticket-status-combobox-input";
    private static final String ADD_REMAINDER_LABEL = "Add Reminder";
    private static final String EDIT_REMAINDER_LABEL = "Edit Reminder";
    private static final String REMOVE_REMAINDER_LABEL = "Remove Reminder";
    private static final String MORE_DETAILS_LABEL = "More details";
    private static final String SAME_MO_TT_TABLE_ID = "_sameMOTTTableWidget";
    private static final String ATTACHMENTS_TAB_ARIA_CONTROLS = "attachmentManager";
    private static final String MESSAGES_TAB_ARIA_CONTROLS = "_messagesTab";
    private static final String ROOT_CAUSES_TAB_ARIA_CONTROLS = "_rootCausesTab";
    private static final String RELATED_TICKETS_TAB_ARIA_CONTROLS = "_relatedTicketsTab";
    private static final String PARTICIPANTS_TAB_ARIA_CONTROLS = "_participantsTabApp";
    private static final String RELATED_PROBLEMS_TAB_ARIA_CONTROLS = "_relatedProblems";

    public IssueDetailsPage(WebDriver driver, WebDriverWait wait) {
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
        OldActionsContainer.createById(driver, wait, ACTIONS_CONTAINER_ID).callActionById(RELEASE_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking release button");
    }

    @Step("Allow ticket editing")
    public void allowEditingTicket() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldActionsContainer.createById(driver, wait, ACTIONS_CONTAINER_ID).callActionByLabel(ALLOW_EDIT_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking edit button");
    }

    @Step("Click Context action with label {contextActionLabel}")
    public void clickContextAction(String contextActionLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabWindowWidget.create(driver, wait).callActionByLabel(contextActionLabel);
        log.info("Clicking Context action {}", contextActionLabel);
    }

    @Step("Selecting tab {tabAriaControls}")
    public void selectTab(String tabAriaControls) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabWindowWidget.create(driver, wait).selectTabById(tabAriaControls);
        log.info("Selecting tab {}", tabAriaControls);
    }

    public AttachmentsTab selectAttachmentsTab() {
        selectTab(ATTACHMENTS_TAB_ARIA_CONTROLS);
        log.info("Selecting tab Attachments");

        return new AttachmentsTab(driver, wait);
    }

    public ExternalTab selectExternalTab() {
        selectTab(EXTERNAL_TAB_ARIA_CONTROLS);
        log.info("Selecting tab External");

        return new ExternalTab(driver, wait);
    }

    public MessagesTab selectMessagesTab() {
        selectTab(MESSAGES_TAB_ARIA_CONTROLS);
        log.info("Selecting Messages Tab");

        return new MessagesTab(driver, wait);
    }

    public RootCausesTab selectRootCauseTab() {
        selectTab(ROOT_CAUSES_TAB_ARIA_CONTROLS);
        log.info("Selecting Root Cause Tab");

        return new RootCausesTab(driver, wait);
    }

    public RelatedTicketsTab selectRelatedTicketsTab() {
        selectTab(RELATED_TICKETS_TAB_ARIA_CONTROLS);
        log.info("Selecting Related Tickets Tab");

        return new RelatedTicketsTab(driver, wait);
    }

    public ParticipantsTab selectParticipantsTab() {
        selectTab(PARTICIPANTS_TAB_ARIA_CONTROLS);
        log.info("Selecting Participants Tab");

        return new ParticipantsTab(driver, wait);
    }

    public RelatedProblemsTab selectRelatedProblemsTab() {
        selectTab(RELATED_PROBLEMS_TAB_ARIA_CONTROLS);
        log.info("Selecting Related Problems Tab");

        return new RelatedProblemsTab(driver, wait);
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
    public void changeTicketStatus(String statusName) {
        ComponentFactory.create(CHANGE_TICKET_STATUS_COMBOBOX_ID, Input.ComponentType.COMBOBOX, driver, wait).setSingleStringValue(statusName);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Changing status to {}", statusName);
    }

    @Step("Check status in ticket status combobox")
    public String checkTicketStatus() {
        return ComponentFactory.create(CHANGE_TICKET_STATUS_COMBOBOX_ID, Input.ComponentType.COMBOBOX, driver, wait).getStringValue();
    }

    @Step("Change problem assignee")
    public void changeProblemAssignee(String assignee) {
        ComponentFactory.create(PROBLEM_ASSIGNEE_ID, Input.ComponentType.SEARCH_FIELD, driver, wait)
                .setSingleStringValue(assignee);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Change problem status")
    public void changeProblemStatus(String status) {
        ComponentFactory.create(PROBLEM_STATUS_ID, Input.ComponentType.COMBOBOX, driver, wait)
                .setSingleStringValue(status);
        DelayUtils.waitForPageToLoad(driver, wait);
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
    public String getOpenedIssueId() {
        return getViewTitle().split("#")[1];
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
}
