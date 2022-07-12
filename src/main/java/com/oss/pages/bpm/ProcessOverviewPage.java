package com.oss.pages.bpm;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.milestones.MilestoneWizardPage;
import com.oss.pages.bpm.processinstances.ProcessWizardPage;
import com.oss.pages.platform.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Paweł Rother
 */
public class ProcessOverviewPage extends BasePage {
    private static final String PROCESS_VIEW = "bpm_processes_view_processes";
    private static final String PROCESS_TABS = "bpm_processes_view_process-details-tabs-container";
    private static final String MILESTONE_TAB = "bpm_processes_view_milestones-tab";
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
    private static final String NAME_LABEL = "Name";
    private static final String REFRESH_TABLE_ID = "refresh-table";
    private static final String BPM_AND_PLANNING = "BPM and Planning";
    private static final String NETWORK_PLANNING = "Network Planning";
    private static final String PROCESS_OVERVIEW = "Process Overview";

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

    public String getProcessStatus(String code) {
        OldTable processTable = OldTable.createById(driver, wait, PROCESS_VIEW);
        processTable.searchByAttributeWithLabel(CODE_LABEL, Input.ComponentType.TEXT_FIELD, code);
        int index = processTable.getRowNumber(code, CODE_LABEL);
        return processTable.getCellValue(index, STATUS_LABEL);
    }

    public String getProcessName(String code) {
        OldTable processTable = OldTable.createById(driver, wait, PROCESS_VIEW);
        processTable.searchByAttributeWithLabel(CODE_LABEL, Input.ComponentType.TEXT_FIELD, code);
        int index = processTable.getRowNumber(code, CODE_LABEL);
        return processTable.getCellValue(index, NAME_LABEL);
    }

    public String getProcessCode(String processName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable processTable = OldTable.createById(driver, wait, PROCESS_VIEW);
        processTable.searchByAttributeWithLabel(NAME_LABEL, Input.ComponentType.TEXT_FIELD, processName);
        processTable.doRefreshWhileNoData(1000, REFRESH_TABLE_ID);
        int index = processTable.getRowNumber(processName, NAME_LABEL);
        return processTable.getCellValue(index, CODE_LABEL);
    }

    public void findProcess(String processCode) {
        findProcess(CODE_LABEL, processCode);
    }

    private void findProcess(String attributeName, String value) {
        TableInterface table = OldTable.createById(driver, wait, PROCESS_VIEW);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.searchByAttributeWithLabel(attributeName, Input.ComponentType.TEXT_FIELD, value);
        DelayUtils.waitForPageToLoad(driver, wait);
        table.doRefreshWhileNoData(10000, REFRESH_TABLE_ID);
        table.selectRowByAttributeValueWithLabel(attributeName, value);
    }

    public String getMilestoneValue(String milestoneName, String attributeName) {
        CommonList milestoneList = CommonList.create(driver, wait, MILESTONE_LIST);
        return milestoneList.getRow(NAME_LABEL, milestoneName).getValue(attributeName);
    }

    public void selectMilestoneTab(String processAttributeName, String value) {
        findProcess(processAttributeName, value);
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsWidget milestoneTab = TabsWidget.createById(driver, wait, PROCESS_TABS);
        milestoneTab.selectTabById(MILESTONE_TAB);
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
        findProcess(processCode);
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

    public String createSimpleNRP() {
        return openProcessCreationWizard().createSimpleNRPV2();
    }

    public String createNRPWithPlusDays(Long plusDays) {
        return openProcessCreationWizard().createNRPWithPlusDays(plusDays);
    }

    public String createSimpleDCP() {
        return openProcessCreationWizard().createSimpleDCPV2();
    }

}