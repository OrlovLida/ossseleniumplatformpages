package com.oss.pages.bpm.processinstances;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.oss.framework.components.attributechooser.AttributesChooser;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.pagination.PaginationComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.table.TableRow;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.tree.TreeWidgetV2;
import com.oss.framework.widgets.treetable.TreeTableWidget;
import com.oss.pages.BasePage;
import com.oss.pages.bpm.planning.PartialIntegrationWizardPage;
import com.oss.pages.bpm.processinstances.creation.ProcessCreationWizardProperties;
import com.oss.pages.bpm.processinstances.creation.ProcessWizardPage;
import com.oss.pages.bpm.processinstances.creation.TerminateProcessWizardPage;
import com.oss.pages.platform.HomePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.oss.pages.bpm.processinstances.ProcessOverviewPage.BPM_AND_PLANNING;
import static com.oss.pages.bpm.processinstances.ProcessOverviewPage.NETWORK_PLANNING;

public class PlannersViewPage extends BasePage {
    public static final String INTEGRATE_PLANNED_CHANGES_CONTEXT_ACTION_ID = "ipd_inventory_view_action_partial_activation";
    public static final String PLANNERS_VIEW = "Planners View";
    public static final String CREATE_GROUP_ACTION_ID = "CREATE";
    public static final String EDIT_GROUP_ID = "EDIT";
    public static final String START_PROCESS_ACTION_ID = "bpm_inventory_view_action_create_process";
    public static final String START_PROGRAM_ACTION_ID = "bpm_inventory_view_action_create_program";
    private static final String PROJECT_ID_PROPERTY_ID = "bpmPlanningAttributes.projectId";
    private static final String PROJECT_ID_LABEL = "Project ID";
    private static final String PLANNING_ATTRIBUTES_LABEL = "Planning Attributes";
    private static final String PROPERTY_PANEL_ID = "PropertyPanelWidget";
    private static final String TREE_TABLE_ID = "process_instance_hierarchy_table";
    private static final String TABS_CONTAINER_ID = "process_instance_bottom_tabs";
    private static final String PROCESS_INSTANCE_TOP_TABS_ID = "process_instance_top_tabs";
    private static final String FRAMEWORK_CUSTOM_BUTTONS_GROUP_ID = "frameworkCustomButtonsGroup";
    private static final String REFRESH_BUTTON_ID = "refreshButton";
    private static final String ONLY_SINGLE_SELECTION_IS_SUPPORTED_EXCEPTION = "Only single selection is supported";
    private static final Map<String, String> TAB_LABELS_AND_THEIR_TREEWIGET_ID = Stream.of(
                    new AbstractMap.SimpleEntry<>("Locations", "process_instance_top_tabs_locations_widget"),
                    new AbstractMap.SimpleEntry<>("Devices", "process_instance_top_tabs_devices_widget"),
                    new AbstractMap.SimpleEntry<>("Connections", "process_instance_top_tabs_connections_widget"))
            .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    private static final String CANCEL_PROCESS_BUTTON_ID = "bpm_inventory_view_action_terminate_process";
    private static final String CANCELABLE_PROCESS_STATE = "In Progress";
    private static final String CODE_ATTRIBUTE_ID = "code";
    private static final String STATE_COLUMN_ID = "state";

    public PlannersViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static PlannersViewPage goToPlannersViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/views/planning/planners-view?perspective=LIVE", basicURL));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new PlannersViewPage(driver, wait);
    }

    public static PlannersViewPage goToPlannersViewPage(WebDriver driver, WebDriverWait wait) {
        DelayUtils.waitForPageToLoad(driver, wait);
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(PLANNERS_VIEW, BPM_AND_PLANNING, NETWORK_PLANNING);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new PlannersViewPage(driver, wait);
    }

    public TreeTableWidget getTreeTable() {
        return TreeTableWidget.createById(driver, wait, TREE_TABLE_ID);
    }

    public TreeWidgetV2 getTreeWidget(String dataTestID) {
        return TreeWidgetV2.create(driver, wait, dataTestID);
    }

    public PlannersViewPage selectObjectByRowId(int row) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.selectRow(row);
        return this;
    }

    public PlannersViewPage selectObjectsByRowId(int... indexes) {
        List<Integer> rows = Arrays.stream(indexes).boxed().collect(Collectors.toList());
        rows.forEach(this::selectObjectByRowId);
        return this;
    }

    public PlannersViewPage unselectObjectByRowId(int rowId) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.unselectRow(rowId);
        return this;
    }

    public PlannersViewPage unselectAllRows() {
        TreeTableWidget processesTreeTable = getTreeTable();
        processesTreeTable.unselectAllRows();
        return this;
    }

    public PlannersViewPage unselectObjectByProcessCode(String processCode) {
        unselectObjectByAttributeValue(CODE_ATTRIBUTE_ID, processCode);
        return this;
    }

    public PlannersViewPage unselectObjectByAttributeValue(String attributeId, String value) {
        getTreeTable().unselectRowByAttributeValue(attributeId, value);
        waitForPageToLoad();
        return this;
    }

    public PlannersViewPage selectObjectByAttributeValue(String attributeId, String value) {
        getTreeTable().selectRowByAttributeValue(attributeId, value);
        waitForPageToLoad();
        return this;
    }

    public List<TableRow> getSelectedRows() {
        return getTreeTable().getSelectedRows();
    }

    public List<TableRow> getRows() {
        return getTreeTable().getAllRows();
    }

    public TableRow getFirstRow() {
        return getTreeTable().getAllRows().get(0);
    }

    private PropertyPanel getPropertyPanel() {
        Widget.waitForWidget(wait, PropertyPanel.PROPERTY_PANEL_CLASS);
        return (PropertyPanel) getTabsWidget().getWidget(PROPERTY_PANEL_ID, Widget.WidgetType.PROPERTY_PANEL);
    }

    @Step("Getting process instances Tob Tab Widget")
    public TabsWidget getProcessInstancesTopTabsWidget() {
        if (getSelectedRows().size() != 1) {
            throw new UnsupportedOperationException(ONLY_SINGLE_SELECTION_IS_SUPPORTED_EXCEPTION);
        }
        return TabsWidget.createById(driver, wait, PROCESS_INSTANCE_TOP_TABS_ID);
    }

    public TabsWidget getTabsWidget() {
        if (getSelectedRows().isEmpty()) {
            throw new UnsupportedOperationException(ONLY_SINGLE_SELECTION_IS_SUPPORTED_EXCEPTION);
        }
        Widget.waitForWidget(wait, TabsWidget.TABS_WIDGET_CLASS);
        return TabsWidget.createById(driver, wait, TABS_CONTAINER_ID);
    }

    public String getAttributeValue(String columnId, int rowId) {
        TreeTableWidget treeTable = getTreeTable();
        return treeTable.getCellValue(rowId, columnId);
    }

    public PlannersViewPage expandNode(String label, String columnId) {
        getTreeTable().expandRow(label, columnId);
        return this;
    }

    public PlannersViewPage expandNode(int index) {
        getTreeTable().expandRow(index);
        return this;
    }

    public PlannersViewPage collapseNode(int index) {
        getTreeTable().collapseRow(index);
        return this;
    }

    public boolean isNodeExpanded(int index) {
        return getTreeTable().isRowExpanded(index);
    }

    public List<String> getActiveColumnsHeaders() {
        TreeTableWidget treeTable = getTreeTable();
        return treeTable.getActiveColumnHeaders();
    }

    public PlannersViewPage changeColumnsOrderInTreeTable(String columnLabel, int position) {
        getTreeTable().changeColumnsOrder(columnLabel, position);
        waitForPageToLoad();
        return this;
    }

    public PlannersViewPage enableColumn(String columnLabel, String... path) {
        AttributesChooser attributesChooser = getTreeTable().getAttributesChooser();
        attributesChooser.enableAttributeByLabel(columnLabel, path);
        attributesChooser.clickApply();
        return this;
    }

    public PlannersViewPage disableColumnAndApply(String columnLabel) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.disableColumnByLabel(columnLabel);
        waitForPageToLoad();
        return this;
    }

    public int getColumnSize(String columnId) {
        int columnIndex = getTreeTable().getActiveColumnIds().indexOf(columnId);
        return getTreeTable().getColumnSize(columnIndex);
    }

    public PlannersViewPage setPagination(int paginationValue) {
        PaginationComponent pagination = getTreeTable().getPagination();
        pagination.changeRowsCount(paginationValue);
        return this;
    }

    public PlannersViewPage setDefaultSettings() {
        getTreeTable().getAttributesChooser().clickDefaultSettings();
        return this;
    }

    @Step("Checking if process is not cancellable")
    public boolean isProcessTerminable(String processCode) {
        if (!getProcessState(processCode).equalsIgnoreCase(CANCELABLE_PROCESS_STATE)) {
            return false;
        }
        selectObjectByAttributeValue(CODE_ATTRIBUTE_ID, processCode);
        TabsWidget upperTabsWidget = getProcessInstancesTopTabsWidget();
        for (Map.Entry<String, String> tab : TAB_LABELS_AND_THEIR_TREEWIGET_ID.entrySet()) {
            upperTabsWidget.selectTabByLabel(tab.getKey());
            waitForPageToLoad();
            TreeWidgetV2 treeWidgetV2 = getTreeWidget(tab.getValue());
            if (!treeWidgetV2.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Step("Getting state of process")
    public String getProcessState(String processCode) {
        TreeTableWidget treeTable = getTreeTable();
        int rowNumber = treeTable.getRowNumber(processCode, CODE_ATTRIBUTE_ID);
        return treeTable.getCellValue(rowNumber, STATE_COLUMN_ID);
    }

    @Step("Terminate process")
    public void terminateProcess(String reason) {
        getTreeTable().callAction(EDIT_GROUP_ID, CANCEL_PROCESS_BUTTON_ID);
        TerminateProcessWizardPage terminateProcessWizardPage = new TerminateProcessWizardPage(driver);
        terminateProcessWizardPage.setTerminationReason(reason);
        terminateProcessWizardPage.clickAccept();
    }

    @Step("Clicking on refresh button")
    public void refresh() {
        getTreeTable().callAction(FRAMEWORK_CUSTOM_BUTTONS_GROUP_ID, REFRESH_BUTTON_ID);
    }

    public PlannersViewPage searchObject(String text) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.fullTextSearch(text);
        waitForPageToLoad();
        return this;
    }

    public String getPropertyValue(String propertyId) {
        return getPropertyPanel().getPropertyValue(propertyId);
    }

    public Long getProjectId(String processCode) {
        String projectId;
        try {
            projectId = selectProcess(processCode).getPropertyValue(PROJECT_ID_PROPERTY_ID);
        } catch (IllegalArgumentException e) {
            projectId = enablePropertyByLabel(PROJECT_ID_LABEL, PLANNING_ATTRIBUTES_LABEL)
                    .getPropertyValue(PROJECT_ID_PROPERTY_ID);
        }
        return Long.valueOf(projectId);
    }

    public PlannersViewPage enablePropertyByLabel(String propertyLabel, String... path) {
        getPropertyPanel().enableAttributeByLabel(propertyLabel, path);
        return this;
    }

    public PlannersViewPage selectProcess(String processCode) {
        final Multimap<String, String> filterValue = ImmutableMultimap.<String, String>builder()
                .put(CODE_ATTRIBUTE_ID, processCode)
                .build();
        return selectProcess(filterValue);
    }

    public PlannersViewPage selectProcess(Multimap<String, String> filterValues) {
        TreeTableWidget processesTreeTable = getTreeTable();
        unselectAllRows();
        waitForPageToLoad();
        processesTreeTable.clearAllFilters();
        waitForPageToLoad();
        searchByAttributesValue(filterValues);
        processesTreeTable.selectFirstRow();
        return this;
    }

    public Multimap<String, String> searchByAttributeValue(String attributeId, String attributeValue, Input.ComponentType componentType) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.searchByAttribute(attributeId, componentType, attributeValue);
        waitForPageToLoad();
        Multimap<String, String> filterValues = treeTable.getAppliedFilters();
        waitForPageToLoad();
        return filterValues;
    }

    public Multimap<String, String> searchByAttributeValue(String attributeId, String attributeValue) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.searchByAttribute(attributeId, attributeValue);
        waitForPageToLoad();
        Multimap<String, String> filterValues = treeTable.getAppliedFilters();
        waitForPageToLoad();
        return filterValues;
    }

    public PlannersViewPage clearFilters() {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.clearAllFilters();
        waitForPageToLoad();
        return this;
    }

    public void callAction(String actionId) {
        TreeTableWidget treeTable = getTreeTable();
        waitForPageToLoad();
        treeTable.callAction(actionId);
        waitForPageToLoad();
    }

    public void callAction(String groupId, String actionId) {
        TreeTableWidget treeTable = getTreeTable();
        waitForPageToLoad();
        treeTable.callAction(groupId, actionId);
        waitForPageToLoad();
    }

    public boolean isActionVisible(String groupId, String actionId) {
        return getTreeTable().isActionVisible(groupId, actionId);
    }

    public boolean isActionVisible(String actionId) {
        return getTreeTable().isActionVisible(actionId);
    }

    public boolean isIntegratePlannedChangesActionVisible() {
        return isActionVisible(EDIT_GROUP_ID, INTEGRATE_PLANNED_CHANGES_CONTEXT_ACTION_ID);
    }

    public ProcessWizardPage openProcessCreationWizard() {
        callAction(CREATE_GROUP_ACTION_ID, START_PROCESS_ACTION_ID);
        waitForPageToLoad();
        return new ProcessWizardPage(driver);
    }

    public ProcessWizardPage openProgramCreationWizard() {
        callAction(CREATE_GROUP_ACTION_ID, START_PROGRAM_ACTION_ID);
        waitForPageToLoad();
        return new ProcessWizardPage(driver);
    }

    public PartialIntegrationWizardPage openIntegratePlannedChangesWizard() {
        callAction(EDIT_GROUP_ID, INTEGRATE_PLANNED_CHANGES_CONTEXT_ACTION_ID);
        waitForPageToLoad();
        return new PartialIntegrationWizardPage(driver);
    }

    public void createInstance(ProcessCreationWizardProperties properties) {
        unselectAllRows();
        if (properties.isProcessCreation())
            openProcessCreationWizard().createInstance(properties);
        else if (properties.isProgramCreation())
            openProgramCreationWizard().createInstance(properties);
    }

    public Map<String, String> createProgramWithProcess(String programName, Long plusDays, String programType, String processName, Long plusDaysProcess, String processType) {
        unselectAllRows();
        return openProgramCreationWizard().createProgramWithProcess(programName, plusDays, programType, processName, plusDaysProcess, processType);
    }

    public String createSimpleNRP() {
        unselectAllRows();
        return openProcessCreationWizard().createSimpleNRPV2();
    }

    public String createSimpleDCP() {
        unselectAllRows();
        return openProcessCreationWizard().createSimpleDCPV2();
    }

    public PlannersViewPage searchByAttributesValue(Multimap<String, String> idAndValues) {
        TreeTableWidget treeTable = getTreeTable();
        idAndValues.asMap().forEach((id, valueCollection) -> valueCollection.forEach(value -> treeTable.searchByAttribute(id, value)));
        waitForPageToLoad();
        return this;
    }

    public String createDCPWithPlusDays(Long plusDays) {
        unselectAllRows();
        return openProcessCreationWizard().createDCPWithPlusDays(plusDays);
    }

    public String createNRPWithPlusDays(Long plusDays) {
        unselectAllRows();
        return openProcessCreationWizard().createNRPWithPlusDays(plusDays);
    }

    public String createProcessIPD(String processName, Long plusDays, String processType) {
        unselectAllRows();
        return openProcessCreationWizard().createProcessIPD(processName, plusDays, processType);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}
