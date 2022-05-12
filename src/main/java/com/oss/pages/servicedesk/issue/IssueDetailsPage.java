package com.oss.pages.servicedesk.issue;

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
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.issue.tabs.AttachmentsTab;
import com.oss.pages.servicedesk.issue.tabs.ExternalTab;
import com.oss.pages.servicedesk.issue.tabs.MessagesTab;
import com.oss.pages.servicedesk.issue.tabs.ParticipantsTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedChangesTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedProblemsTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedTicketsTab;
import com.oss.pages.servicedesk.issue.tabs.RootCausesTab;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.COMMON_WIZARD_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.DETAILS_TABS_CONTAINER_ID;

public class IssueDetailsPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(IssueDetailsPage.class);

    public static final String DETAILS_PAGE_URL_PATTERN = "%s/#/view/service-desk/%s/details/%s";

    private static final String EDIT_DETAILS_LABEL = "Edit details";
    private static final String RELEASE_BUTTON_LABEL = "Release";
    private static final String DETAILS_VIEW_ACTIONS_CONTAINER_ID = "_detailsWindow-windowToolbar";
    private static final String ALLOW_EDIT_LABEL = "Edit";
    private static final String ISSUE_ASSIGNEE_ID = "assignee";
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
    private static final String RELATED_CHANGES_TAB_ID = "_relatedChangesTab";
    private static final String MORE_BUTTON_LABEL = "More";
    private static final String TABS_WIDGET_ID = "_tablesWindow";

    public IssueDetailsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Open edit ticket wizard")
    public SDWizardPage openEditTicketWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getDetailsViewOldActionsContainer().callActionByLabel(MORE_BUTTON_LABEL, EDIT_DETAILS_LABEL);
        log.info("Ticket Wizard edit is opened");
        return new SDWizardPage(driver, wait, COMMON_WIZARD_ID);
    }

    @Step("Open edit change wizard")
    public SDWizardPage openEditChangeWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getDetailsViewOldActionsContainer().callActionByLabel(EDIT_DETAILS_LABEL);
        log.info("Edit Change Wizard is opened");
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

    @Step("Click Context action with label {contextActionLabel}")
    public void clickContextAction(String contextActionLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getDetailsViewOldActionsContainer().callActionByLabel(contextActionLabel);
        log.info("Clicking Context action {}", contextActionLabel);
    }

    @Step("Selecting tab {tabId}")
    public void selectTabFromTabsWidget(String widgetId, String tabId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsWidget.createById(driver, wait, widgetId).selectTabById(tabId);
        log.info("Selecting tab {}", tabId);
    }

    @Step("Selecting tab {tabId}")
    public void selectTabFromTablesWindow(String tabId) {
        selectTabFromTabsWidget(TABS_WIDGET_ID, tabId);
    }

    @Step("Selecting tab {tabId}")
    public void selectTabFromDetailsWindow(String tabId) {
        selectTabFromTabsWidget(DETAILS_TABS_CONTAINER_ID, tabId);
    }

    public AttachmentsTab selectAttachmentsTab() {
        selectTabFromDetailsWindow(ATTACHMENTS_TAB_ARIA_CONTROLS);
        log.info("Selecting tab Attachments");

        return new AttachmentsTab(driver, wait);
    }

    public ExternalTab selectExternalTab() {
        selectTabFromDetailsWindow(EXTERNAL_TAB_ARIA_CONTROLS);
        log.info("Selecting tab External");

        return new ExternalTab(driver, wait);
    }

    public MessagesTab selectMessagesTab() {
        selectTabFromTablesWindow(MESSAGES_TAB_ARIA_CONTROLS);
        log.info("Selecting Messages Tab");

        return new MessagesTab(driver, wait);
    }

    public RootCausesTab selectRootCauseTab() {
        selectTabFromTablesWindow(ROOT_CAUSES_TAB_ARIA_CONTROLS);
        log.info("Selecting Root Cause Tab");

        return new RootCausesTab(driver, wait);
    }

    public RelatedTicketsTab selectRelatedTicketsTab() {
        selectTabFromTablesWindow(RELATED_TICKETS_TAB_ARIA_CONTROLS);
        log.info("Selecting Related Tickets Tab");

        return new RelatedTicketsTab(driver, wait);
    }

    public ParticipantsTab selectParticipantsTab() {
        selectTabFromTablesWindow(PARTICIPANTS_TAB_ARIA_CONTROLS);
        log.info("Selecting Participants Tab");

        return new ParticipantsTab(driver, wait);
    }

    public RelatedProblemsTab selectRelatedProblemsTab() {
        selectTabFromTablesWindow(RELATED_PROBLEMS_TAB_ARIA_CONTROLS);
        log.info("Selecting Related Problems Tab");

        return new RelatedProblemsTab(driver, wait);
    }

    public RelatedChangesTab selectRelatedChangesTab() {
        selectTabFromTablesWindow(RELATED_CHANGES_TAB_ID);
        log.info("Selecting Related Changes Tab");

        return new RelatedChangesTab(driver, wait);
    }

    @Step("Skipping all actions on checklist")
    public void skipAllActionsOnCheckList() {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonList.create(driver, wait, CHECKLIST_APP_ID)
                .getRows()
                .forEach(row -> {
                    DelayUtils.waitForPageToLoad(driver, wait);
                    row.callActionIcon(SKIP_BUTTON_LABEL);
                });
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Skipping all actions on checklist");
    }

    @Step("Checking if Skip buttons are present for all actions on checklist")
    public boolean areSkipButtonsActive() {
        return CommonList.create(driver, wait, CHECKLIST_APP_ID)
                .getRows()
                .stream().allMatch(row -> {
                    DelayUtils.waitForPageToLoad(driver, wait);
                    return row.isActionIconPresentByLabel(SKIP_BUTTON_LABEL);
                });
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

    @Step("Change Issue assignee")
    public void changeIssueAssignee(String assignee) {
        ComponentFactory.create(ISSUE_ASSIGNEE_ID, Input.ComponentType.SEARCH_FIELD, driver, wait)
                .setSingleStringValue(assignee);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Check Issue Assignee")
    public String checkAssignee() {
        return ComponentFactory.create(ISSUE_ASSIGNEE_ID, Input.ComponentType.SEARCH_FIELD, driver, wait).getStringValue();
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

    public String getDisplayedText(String windowId, String fieldId) {
        return ListApp.createFromParent(driver, wait, windowId).getValueFromField(fieldId);
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

    @Step("Click More Details")
    public MoreDetailsPage clickMoreDetails() {
        getDetailsViewOldActionsContainer().callActionByLabel(MORE_BUTTON_LABEL, MORE_DETAILS_LABEL);
        log.info("Clicking More Details");
        return new MoreDetailsPage(driver, wait);
    }

    @Step("I check Same MO TT table")
    public boolean checkIfSameMOTTTableIsNotEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check Same MO TT Table");
        return !OldTable.createById(driver, wait, SAME_MO_TT_TABLE_ID).hasNoData();
    }

    private OldActionsContainer getDetailsViewOldActionsContainer() {
        return OldActionsContainer.createById(driver, wait, DETAILS_VIEW_ACTIONS_CONTAINER_ID);
    }
}
