package com.oss.pages.servicedesk.issue;

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
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.issue.tabs.AttachmentsTab;
import com.oss.pages.servicedesk.issue.tabs.DescriptionTab;
import com.oss.pages.servicedesk.issue.tabs.ExternalTab;
import com.oss.pages.servicedesk.issue.tabs.MessagesTab;
import com.oss.pages.servicedesk.issue.tabs.OverviewTab;
import com.oss.pages.servicedesk.issue.tabs.ParticipantsTab;
import com.oss.pages.servicedesk.issue.tabs.ProblemSolutionTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedChangesTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedProblemsTab;
import com.oss.pages.servicedesk.issue.tabs.RelatedTicketsTab;
import com.oss.pages.servicedesk.issue.tabs.RolesTab;
import com.oss.pages.servicedesk.issue.tabs.RootCausesTab;
import com.oss.pages.servicedesk.issue.tabs.TasksTab;
import com.oss.pages.servicedesk.issue.ticket.TicketOverviewTab;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.DETAILS_TABS_CONTAINER_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.TABS_WIDGET_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.TROUBLE_TICKET_ISSUE_TYPE;

public class IssueDetailsPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(IssueDetailsPage.class);

    public static final String DETAILS_PAGE_URL_PATTERN = "%s/#/view/service-desk/%s/details/%s";

    private static final String DETAILS_VIEW_ACTIONS_CONTAINER_ID = "_detailsWindow-windowToolbar";
    private static final String CHECKLIST_APP_ID = "_checklistApp";
    private static final String SKIP_BUTTON_LABEL = "SKIP";
    private static final String EXTERNAL_TAB_ARIA_CONTROLS = "_detailsExternalTab";
    private static final String DICTIONARIES_TABLE_ID = "_dictionariesTableId";
    private static final String DICTIONARY_VALUE_TABLE_LABEL = "Dictionary Value";
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

    public AttachmentsTab selectAttachmentsTab(String widgetId) {
        selectTabFromTabsWidget(widgetId, ATTACHMENTS_TAB_ARIA_CONTROLS);
        log.info("Selecting tab Attachments");

        return new AttachmentsTab(driver, wait);
    }

    public OverviewTab selectOverviewTab(String issueType) {
        log.info("Selecting tab Overview");

        if (issueType.equals(TROUBLE_TICKET_ISSUE_TYPE)) {
            selectTabFromDetailsWindow(TICKET_OVERVIEW_TAB_ARIA_CONTROLS);
            return new TicketOverviewTab(driver, wait);
        } else {
            selectTabFromDetailsWindow(ISSUE_OVERVIEW_TAB_ARIA_CONTROLS);
            return new OverviewTab(driver, wait);
        }
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

    public DescriptionTab selectDescriptionTab() {
        selectTabFromTabsWidget(DESCRIPTIONS_WINDOW_ID, DESCRIPTION_TAB_ID);
        log.info("Selecting Description Tab");

        return new DescriptionTab(driver, wait);
    }

    public ProblemSolutionTab selectProblemSolutionTab() {
        selectTabFromTabsWidget(DESCRIPTIONS_WINDOW_ID, PROBLEM_SOLUTION_TAB_ID);
        log.info("Selecting Problem Solution Tab");

        return new ProblemSolutionTab(driver, wait);
    }

    public TasksTab selectTaskTab() {
        selectTabFromTabsWidget(TASK_TAB_WIDGET_ID, TASK_TAB_ID);
        log.info("Selecting Task Tab");

        return new TasksTab(driver, wait);
    }

    public RolesTab selectRolesTab() {
        selectTabFromDetailsWindow(ROLES_TAB_ID);
        log.info("Selecting Roles Tab");
        return new RolesTab(driver, wait);
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


    @Step("I check Same MO TT table")
    public boolean checkIfSameMOTTTableIsNotEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check Same MO TT Table");
        return !OldTable.createById(driver, wait, SAME_MO_TT_TABLE_ID).hasNoData();
    }

    protected OldActionsContainer getDetailsViewOldActionsContainer() {
        return OldActionsContainer.createById(driver, wait, DETAILS_VIEW_ACTIONS_CONTAINER_ID);
    }
}
