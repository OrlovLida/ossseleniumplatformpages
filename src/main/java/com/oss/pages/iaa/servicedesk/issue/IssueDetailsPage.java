package com.oss.pages.iaa.servicedesk.issue;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.iaa.widgets.list.ListApp;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.iaa.servicedesk.BaseSDPage;
import com.oss.pages.iaa.servicedesk.issue.tabs.AffectedTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.AttachmentsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.DescriptionTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ExternalTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ImprovementTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.MessagesTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.OverviewTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ParticipantsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ProblemSolutionTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RelatedChangesTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RelatedProblemsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RelatedTicketsTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.ResolutionTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RolesTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.RootCausesTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.SummaryTab;
import com.oss.pages.iaa.servicedesk.issue.tabs.TasksTab;
import com.oss.pages.iaa.servicedesk.issue.ticket.TicketOverviewTab;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.AFFECTED_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.ATTACHMENTS_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.CHANGE_WIZARD_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.COMMON_WIZARD_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.CREATE_CHANGE_BUTTON_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DESCRIPTION_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DETAILS_TABS_CONTAINER_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.EXTERNAL_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.MESSAGES_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.NORMAL_BUTTON_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.OVERVIEW_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.PARTICIPANTS_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.PROBLEM_SOLUTION_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.RELATED_CHANGES_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.RELATED_PROBLEMS_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.RELATED_TICKETS_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.ROLES_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.ROOT_CAUSES_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.SUMMARY_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TABS_WIDGET_ID;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TASKS_TAB_LABEL;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;

public class IssueDetailsPage extends BaseSDPage {

    public static final String DETAILS_PAGE_URL_PATTERN = "%s/#/view/service-desk/%s/details/%s";
    private static final Logger log = LoggerFactory.getLogger(IssueDetailsPage.class);
    private static final String DETAILS_VIEW_ACTIONS_CONTAINER_ID = "_detailsWindow-windowToolbar";
    private static final String CHECKLIST_APP_ID = "_checklistApp";
    private static final String SKIP_BUTTON_LABEL = "SKIP";
    private static final String EXTERNAL_TAB_ARIA_CONTROLS = "_detailsExternalTab";
    private static final String DICTIONARIES_TABLE_ID = "_dictionariesTableId";
    private static final String DICTIONARY_VALUE_TABLE_LABEL = "Dictionary Value";
    private static final String DICTIONARY_NAME_VALUE_TABLE_LABEL = "Dictionary Name";
    private static final String SAME_MO_TT_TABLE_ID = "_sameMOTTTableWidget";
    private static final String ATTACHMENTS_TAB_ARIA_CONTROLS = "attachmentManager";
    private static final String MESSAGES_TAB_ARIA_CONTROLS = "_messagesTab";
    private static final String ROOT_CAUSES_TAB_ARIA_CONTROLS = "_rootCausesTab";
    private static final String RELATED_TICKETS_TAB_ARIA_CONTROLS = "_relatedTicketsTab";
    private static final String PARTICIPANTS_TAB_ARIA_CONTROLS = "_participantsTabApp";
    private static final String RELATED_PROBLEMS_TAB_ARIA_CONTROLS = "_relatedProblems";
    private static final String RELATED_CHANGES_TAB_ID = "_relatedChangesTab";
    private static final String ROLES_TAB_ID = "_detailsRoles";
    private static final String DESCRIPTIONS_WINDOW_ID = "_descriptionsWindow";
    private static final String DESCRIPTION_TAB_ID = "_descriptionTab";
    private static final String TICKET_OVERVIEW_TAB_ARIA_CONTROLS = "most-wanted";
    private static final String ISSUE_OVERVIEW_TAB_ARIA_CONTROLS = "_detailsOverviewTab";
    private static final String PROBLEM_SOLUTION_TAB_ID = "_problemSolutionTab";
    private static final String TASK_TAB_WIDGET_ID = "_taskWindow";
    private static final String TASK_TAB_ID = "_tasksWindow";
    private static final String AFFECTED_TAB_ID = "_affectedServicesTab";
    private static final String SUMMARY_TAB_ID = "_summaryTab";
    private static final String MORE_BUTTON_ID = "frameworkCustomMore";
    private static final String EDIT_DETAILS_ID = "EditDetailsButtonId";
    private static final String RESOLUTION_TAB_LABEL = "Resolution";
    private static final String IMPROVEMENT_TAB_LABEL = "Improvement";
    private static final String RESOLUTION_TAB_ARIA_CONTROLS = "_resolutionDescriptionTab";
    private static final String IMPROVEMENT_TAB_ARIA_CONTROLS = "_improvementNotesTab";

    public IssueDetailsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Click Context action with label {contextActionLabel}")
    public void clickContextAction(String contextActionLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getDetailsViewOldActionsContainer().callActionByLabel(contextActionLabel);
        log.info("Clicking Context action {}", contextActionLabel);
    }

    @Step("Selecting tab {tabId}")
    public void selectTabFromTabsWidget(String widgetId, String tabId, String tabLabel) {
        if (!isTabActive(widgetId, tabLabel)) {
            DelayUtils.waitForPageToLoad(driver, wait);
            TabsWidget.createById(driver, wait, widgetId).selectTabById(tabId);
        }
        log.info("Selecting tab {}", tabId);
    }

    public boolean isTabActive(String widgetId, String tabLabel) {
        return TabsWidget.createById(driver, wait, widgetId).getActiveTabLabel().equals(tabLabel);
    }

    @Step("Selecting tab {tabId}")
    public void selectTabFromTablesWindow(String tabId, String tabLabel) {
        selectTabFromTabsWidget(TABS_WIDGET_ID, tabId, tabLabel);
    }

    @Step("Selecting tab {tabId}")
    public void selectTabFromDetailsWindow(String tabId, String tabLabel) {
        selectTabFromTabsWidget(DETAILS_TABS_CONTAINER_ID, tabId, tabLabel);
    }

    public AttachmentsTab selectAttachmentsTab(String widgetId) {
        selectTabFromTabsWidget(widgetId, ATTACHMENTS_TAB_ARIA_CONTROLS, ATTACHMENTS_TAB_LABEL);
        log.info("Selecting tab Attachments");

        return new AttachmentsTab(driver, wait);
    }

    public OverviewTab selectOverviewTab(String issueType) {
        log.info("Selecting tab Overview");

        if (issueType.equals(TROUBLE_TICKET_ISSUE_TYPE)) {
            selectTabFromDetailsWindow(TICKET_OVERVIEW_TAB_ARIA_CONTROLS, OVERVIEW_TAB_LABEL);
            return new TicketOverviewTab(driver, wait);
        } else {
            selectTabFromDetailsWindow(ISSUE_OVERVIEW_TAB_ARIA_CONTROLS, OVERVIEW_TAB_LABEL);
            return new OverviewTab(driver, wait);
        }
    }

    public ExternalTab selectExternalTab() {
        selectTabFromDetailsWindow(EXTERNAL_TAB_ARIA_CONTROLS, EXTERNAL_TAB_LABEL);
        log.info("Selecting tab External");

        return new ExternalTab(driver, wait);
    }

    public MessagesTab selectMessagesTab() {
        selectTabFromTablesWindow(MESSAGES_TAB_ARIA_CONTROLS, MESSAGES_TAB_LABEL);
        log.info("Selecting Messages Tab");

        return new MessagesTab(driver, wait);
    }

    public RootCausesTab selectRootCauseTab() {
        selectTabFromTablesWindow(ROOT_CAUSES_TAB_ARIA_CONTROLS, ROOT_CAUSES_TAB_LABEL);
        log.info("Selecting Root Cause Tab");

        return new RootCausesTab(driver, wait);
    }

    public RelatedTicketsTab selectRelatedTicketsTab() {
        selectTabFromTablesWindow(RELATED_TICKETS_TAB_ARIA_CONTROLS, RELATED_TICKETS_TAB_LABEL);
        log.info("Selecting Related Tickets Tab");

        return new RelatedTicketsTab(driver, wait);
    }

    public ParticipantsTab selectParticipantsTab() {
        selectTabFromTablesWindow(PARTICIPANTS_TAB_ARIA_CONTROLS, PARTICIPANTS_TAB_LABEL);
        log.info("Selecting Participants Tab");

        return new ParticipantsTab(driver, wait);
    }

    public RelatedProblemsTab selectRelatedProblemsTab() {
        selectTabFromTablesWindow(RELATED_PROBLEMS_TAB_ARIA_CONTROLS, RELATED_PROBLEMS_TAB_LABEL);
        log.info("Selecting Related Problems Tab");

        return new RelatedProblemsTab(driver, wait);
    }

    public RelatedChangesTab selectRelatedChangesTab() {
        selectTabFromTablesWindow(RELATED_CHANGES_TAB_ID, RELATED_CHANGES_TAB_LABEL);
        log.info("Selecting Related Changes Tab");

        return new RelatedChangesTab(driver, wait);
    }

    public DescriptionTab selectDescriptionTab() {
        selectTabFromTablesWindow(DESCRIPTION_TAB_ID, DESCRIPTION_TAB_LABEL);
        log.info("Selecting Description Tab");

        return new DescriptionTab(driver, wait);
    }

    public SummaryTab selectSummaryTab() {
        selectTabFromTabsWidget(DESCRIPTIONS_WINDOW_ID, SUMMARY_TAB_ID, SUMMARY_TAB_LABEL);
        log.info("Selecting Description Tab");

        return new SummaryTab(driver, wait);
    }

    public ProblemSolutionTab selectProblemSolutionTab() {
        selectTabFromTabsWidget(DESCRIPTIONS_WINDOW_ID, PROBLEM_SOLUTION_TAB_ID, PROBLEM_SOLUTION_TAB_LABEL);
        log.info("Selecting Problem Solution Tab");

        return new ProblemSolutionTab(driver, wait);
    }

    public TasksTab selectTasksTab() {
        selectTabFromTabsWidget(TASK_TAB_WIDGET_ID, TASK_TAB_ID, TASKS_TAB_LABEL);
        log.info("Selecting Tasks Tab");

        return new TasksTab(driver, wait);
    }

    public RolesTab selectRolesTab() {
        selectTabFromDetailsWindow(ROLES_TAB_ID, ROLES_TAB_LABEL);
        log.info("Selecting Roles Tab");
        return new RolesTab(driver, wait);
    }

    public AffectedTab selectAffectedTab() {
        selectTabFromTablesWindow(AFFECTED_TAB_ID, AFFECTED_TAB_LABEL);
        log.info("Selecting Affected Tab");
        return new AffectedTab(driver, wait);
    }

    public ResolutionTab selectResolutionTab() {
        selectTabFromDetailsWindow(RESOLUTION_TAB_ARIA_CONTROLS, RESOLUTION_TAB_LABEL);
        log.info("Selecting tab Resolution");

        return new ResolutionTab(driver, wait);
    }

    public ImprovementTab selectImprovementTab() {
        selectTabFromDetailsWindow(IMPROVEMENT_TAB_ARIA_CONTROLS, IMPROVEMENT_TAB_LABEL);
        log.info("Selecting tab Improvement");

        return new ImprovementTab(driver, wait);
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

    @Step("I open Edit details wizard")
    public SDWizardPage openEditDetailsWizard() {
        return SDWizardPage.openCreateWizard(driver, wait, EDIT_DETAILS_ID, MORE_BUTTON_ID, COMMON_WIZARD_ID);
    }

    public String checkExistingDictionary() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createById(driver, wait, DICTIONARIES_TABLE_ID).getCellValue(0, DICTIONARY_VALUE_TABLE_LABEL);
    }

    public boolean checkDictionaryPresence(String dictionaryNameValue) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createById(driver, wait, DICTIONARIES_TABLE_ID).isValuePresent(DICTIONARY_NAME_VALUE_TABLE_LABEL, dictionaryNameValue);
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

    @Step("I check Same MO TT table")
    public boolean checkIfSameMOTTTableIsNotEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check Same MO TT Table");
        return !OldTable.createById(driver, wait, SAME_MO_TT_TABLE_ID).hasNoData();
    }

    @Step("I check if {tableId} table exists")
    public boolean checkIfTableExists(String tableId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I check if {} table exists", tableId);
        if (!getOldTable(tableId).hasNoData()) {
            log.info("Table exists and has some data");
            return true;
        } else if (getOldTable(tableId).hasNoData()) {
            log.info("Table exists and it's empty");
            return true;
        } else {
            log.error("Table doesn't exist");
            return false;
        }
    }

    @Step("Click Edit Details")
    public SDWizardPage clickEditDetails() {
        getDetailsViewOldActionsContainer().callActionById(EDIT_DETAILS_ID);
        log.info("Clicking Edit Details");
        return new SDWizardPage(driver, wait, COMMON_WIZARD_ID);
    }

    public OldTable getOldTable(String tableId) {
        return OldTable.createById(driver, wait, tableId);
    }

    protected OldActionsContainer getDetailsViewOldActionsContainer() {
        return OldActionsContainer.createById(driver, wait, DETAILS_VIEW_ACTIONS_CONTAINER_ID);
    }
}