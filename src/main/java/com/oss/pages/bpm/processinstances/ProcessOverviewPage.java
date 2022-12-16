package com.oss.pages.bpm.processinstances;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.milestones.MilestoneWizardPage;
import com.oss.pages.bpm.processinstances.creation.ProcessCreationWizardProperties;
import com.oss.pages.bpm.processinstances.creation.ProcessWizardPage;
import com.oss.pages.platform.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Paweł Rother
 */
public class ProcessOverviewPage extends BasePage {
    public static final String NAME_LABEL = "Name";
    public static final String BPM_AND_PLANNING = "BPM and Planning";
    public static final String NETWORK_PLANNING = "Network Planning";
    public static final String PROCESS_OVERVIEW = "Process Overview";
    private static final String PROCESS_VIEW = "bpm_processes_view_processes";
    private static final String PROCESS_TABS = "process-details-window";
    private static final String MILESTONE_TAB_ID = "bpm_processes_view_milestones-tab";
    private static final String MILESTONE_LIST = "bpm_processes_view_process-milestones-list";
    private static final String ADD_MILESTONES_LIST_ID = "CREATE_MILESTONES_EDITABLE_LIST_ID";
    private static final String CREATE_MILESTONES_ACTION_ID = "create-milestones";
    private static final String CHANGE_FDD_ACTION_ID = "inventory-processes_change_finished_due_date";
    private static final String TERMINATE_PROCESS_ACTION_ID = "kill-process";
    private static final String EDIT_PROCESS_ATTRIBUTES_ACTION_ID = "edit-processes";
    private static final String CREATE_GROUP_ACTION_ID = "create";
    private static final String START_PROCESS_ACTION_ID = "start-process";
    private static final String ADD_MILESTONES_WIZARD_ID = "WIZARD_APP_ID";
    private static final String CODE_LABEL = "Code";
    private static final String STATUS_LABEL = "Status";
    private static final String REFRESH_TABLE_ID = "refresh-table";
    private static final String RELOAD_TABLE_ACTION_ID = "refresh-table";
    private static final String FORECAST_TAB_ID = "bpm_processes_view_forecast-tab";
    private static final String PROCESS_ROLES_TAB_ID = "bpm_processes_view_roles-tab";
    private static final String PROCESS_ROLES_LIST = "bpm_processes_view_process-roles-parameters-tab";
    private static final String FORECAST_LIST = "bpm_processes_view_process-forecast-list";
    private static final String GROUPS_LABEL = "Groups";
    private static final String START_PROGRAM_ACTION_ID = "start-program";
    private static final String RELATED_PROCESSES_TAB_ID = "bpm_processes_view_relations-tab";
    private static final String RELATED_PROCESSES_LIST = "bpm_processes_view_related-processes-list";
    private static final String PROPERTY_PANEL_ID = "bpm_processes_view_process-details";
    private static final String PROJECT_ID_PROPERTY_NAME = "Project ID";


    public ProcessOverviewPage(WebDriver driver) {
        super(driver);
    }

    public static ProcessOverviewPage goToProcessOverviewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/bpm/processes" +
                "?perspective=LIVE", basicURL));

        return new ProcessOverviewPage(driver);
    }

    public static ProcessOverviewPage goToProcessOverviewPage(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(PROCESS_OVERVIEW, BPM_AND_PLANNING, NETWORK_PLANNING);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new ProcessOverviewPage(driver);
    }

    public void clearAllColumnFilters() {
        OldTable processTable = OldTable.createById(driver, wait, PROCESS_VIEW);
        processTable.clearAllColumnValues();
    }

    public void selectPredefinedFilter(String filterName) {
        OldTable processTable = OldTable.createById(driver, wait, PROCESS_VIEW);
        processTable.selectPredefinedFilter(filterName);
    }

    private String getProcessAttribute(String columnLabel) {
        OldTable processTable = OldTable.createById(driver, wait, PROCESS_VIEW);
        return processTable.getCellValue(0, columnLabel);
    }

    private OldPropertyPanel getOldPropertyPanel() {
        return OldPropertyPanel.createById(driver, wait, PROPERTY_PANEL_ID);
    }

    public String getPropertyValue(String propertyName) {
        return getOldPropertyPanel().getPropertyValue(propertyName);
    }

    public Long getProjectId(String processCode) {
        return Long.valueOf(selectProcess(processCode).getPropertyValue(PROJECT_ID_PROPERTY_NAME));
    }

    public String getProcessStatus() {
        return getProcessAttribute(STATUS_LABEL);
    }

    public String getProcessName() {
        return getProcessAttribute(NAME_LABEL);
    }

    public String getProcessCode(String processName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable processTable = OldTable.createById(driver, wait, PROCESS_VIEW);
        processTable.searchByAttributeWithLabel(NAME_LABEL, Input.ComponentType.TEXT_FIELD, processName);
        processTable.doRefreshWhileNoData(1000, REFRESH_TABLE_ID);
        int index = processTable.getRowNumber(processName, NAME_LABEL);
        return processTable.getCellValue(index, CODE_LABEL);
    }

    public ProcessOverviewPage selectProcess(String processCode) {
        return selectProcess(CODE_LABEL, processCode);
    }

    public ProcessOverviewPage selectProcess(String attributeName, String value) {
        TableInterface table = OldTable.createById(driver, wait, PROCESS_VIEW);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.searchByAttributeWithLabel(attributeName, Input.ComponentType.TEXT_FIELD, value);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.doRefreshWhileNoData(10000, REFRESH_TABLE_ID);
        table.selectRowByAttributeValueWithLabel(attributeName, value);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    public String getMilestoneValue(String milestoneName, String attributeName) {
        CommonList milestoneList = CommonList.create(driver, wait, MILESTONE_LIST);
        return milestoneList.getRow(NAME_LABEL, milestoneName).getValue(attributeName);
    }

    public String getProcessRoleGroups(String processRoleName) {
        CommonList milestoneList = CommonList.create(driver, wait, PROCESS_ROLES_LIST);
        return milestoneList.getRow(NAME_LABEL, processRoleName).getValue(GROUPS_LABEL);
    }

    public String getForecastValue(String forecastName, String attributeName) {
        CommonList list = CommonList.create(driver, wait, FORECAST_LIST);
        return list.getRow(NAME_LABEL, forecastName).getValue(attributeName);
    }

    public String getRelatedProcessValue(String relatedProcessName, String attributeName) {
        CommonList list = CommonList.create(driver, wait, RELATED_PROCESSES_LIST);
        return list.getRow(NAME_LABEL, relatedProcessName).getValue(attributeName);
    }

    public int getRelatedProcessesAmount() {
        CommonList list = CommonList.create(driver, wait, RELATED_PROCESSES_LIST);
        return list.getRows().size();
    }

    public ProcessOverviewPage openMilestoneTab() {
        return openTab(MILESTONE_TAB_ID);
    }

    public ProcessOverviewPage openProcessRolesTab() {
        return openTab(PROCESS_ROLES_TAB_ID);
    }

    public ProcessOverviewPage openForecastsTab() {
        return openTab(FORECAST_TAB_ID);
    }

    public ProcessOverviewPage openRelatedProcessesTab() {
        return openTab(RELATED_PROCESSES_TAB_ID);
    }

    private ProcessOverviewPage openTab(String tabId) {
        TabsWidget tabsWidget = TabsWidget.createById(driver, wait, PROCESS_TABS);
        tabsWidget.selectTabById(tabId);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    public void callAction(String actionId) {
        TableInterface table = OldTable.createById(driver, wait, PROCESS_VIEW);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.callAction(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void callAction(String groupId, String actionId) {
        TableInterface table = OldTable.createById(driver, wait, PROCESS_VIEW);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.callAction(groupId, actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public String createProcessIPD(String processName, Long plusDays, String processType) {
        return openProcessCreationWizard().createProcessIPD(processName, plusDays, processType);
    }

    public List<Milestone> addMilestonesForProcess(String processCode, List<Milestone> milestones) {
        selectProcess(processCode);
        DelayUtils.waitForPageToLoad(driver, wait);
        callAction(CREATE_MILESTONES_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard addMilestonesWizard = Wizard.createByComponentId(driver, wait, ADD_MILESTONES_WIZARD_ID);
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, ADD_MILESTONES_LIST_ID);
        List<Milestone> out = milestones.stream().map(milestoneWizardPage::addMilestoneRow).collect(Collectors.toList());
        DelayUtils.waitForPageToLoad(driver, wait);
        addMilestonesWizard.clickAccept();
        return out;
    }

    public String createDCPWithPlusDays(Long plusDays) {
        return openProcessCreationWizard().createDCPWithPlusDays(plusDays);
    }

    public ProcessWizardPage openProcessCreationWizard() {
        callAction(CREATE_GROUP_ACTION_ID, START_PROCESS_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new ProcessWizardPage(driver);
    }

    public ProcessWizardPage openProgramCreationWizard() {
        callAction(CREATE_GROUP_ACTION_ID, START_PROGRAM_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new ProcessWizardPage(driver);
    }

    public void createInstance(ProcessCreationWizardProperties properties) {
        if (properties.isProcessCreation())
            openProcessCreationWizard().createInstance(properties);
        else if (properties.isProgramCreation())
            openProgramCreationWizard().createInstance(properties);
    }

    public String createSimpleNRP() {
        return openProcessCreationWizard().createSimpleNRPV2();
    }

    public String createNRPWithPlusDays(Long plusDays) {
        return openProcessCreationWizard().createNRPWithPlusDays(plusDays);
    }

    public String createSimpleDCP() {
        return openProcessCreationWizard().createSimpleDCPV2();
    }

    public ProcessOverviewPage reloadTable() {
        TableInterface table = OldTable.createById(driver, wait, PROCESS_VIEW);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.callAction(OldActionsContainer.KEBAB_GROUP_ID, RELOAD_TABLE_ACTION_ID);
        return this;
    }
}
