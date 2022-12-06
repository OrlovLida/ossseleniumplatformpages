package com.oss.pages.bpm.processinstances;

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
import com.oss.pages.bpm.processinstances.creation.ProcessWizardPage;
import com.oss.pages.bpm.processinstances.creation.TerminateProcessWizardPage;
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

public class PlannersViewPage extends BasePage {

    private static final String TREE_TABLE_ID = "process_instance_hierarchy_table";

    private static final String TABS_CONTAINER_ID = "process_instance_bottom_tabs";
    private static final String CREATE_GROUP_ACTION_ID = "CREATE";
    private static final String START_PROCESS_ACTION_ID = "bpm_inventory_view_action_create_process";
    private static final String START_PROGRAM_ACTION_ID = "bpm_inventory_view_action_create_program";
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
    private static final String EDIT_GROUP_ID = "EDIT";
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

    public TreeTableWidget getTreeTable() {
        return TreeTableWidget.createById(driver, wait, TREE_TABLE_ID);
    }

    public TreeWidgetV2 getTreeWidget(String dataTestID) {
        return TreeWidgetV2.create(driver, wait, dataTestID);
    }

    public void selectObjectByRowId(int rowId) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.selectRow(rowId);
    }

    public void selectObjectsByRowId(int... indexes) {
        List<Integer> rows = Arrays.stream(indexes).boxed().collect(Collectors.toList());
        rows.forEach(this::selectObjectByRowId);
    }

    public void unselectObjectByRowId(int rowId) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.unselectRow(rowId);
    }

    public void unselectObjectByProcessCode(String processCode) {
        unselectObjectByAttributeValue(CODE_ATTRIBUTE_ID, processCode);
    }

    public void unselectObjectByAttributeValue(String attributeId, String value) {
        getTreeTable().unselectRowByAttributeValue(attributeId, value);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void selectObjectByAttributeValue(String attributeId, String value) {
        getTreeTable().selectRowByAttributeValue(attributeId, value);
        DelayUtils.waitForPageToLoad(driver, wait);
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

    public PropertyPanel getPropertyPanel(int rowId, String propertyPanelId) {
        selectObjectByRowId(rowId);
        return getPropertyPanel(propertyPanelId);
    }

    public PropertyPanel getPropertyPanel(String propertyPanelId) {
        Widget.waitForWidget(wait, PropertyPanel.PROPERTY_PANEL_CLASS);
        return (PropertyPanel) getTabsWidget().getWidget(propertyPanelId, Widget.WidgetType.PROPERTY_PANEL);
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

    public void expandNode(String label, String columnId) {
        getTreeTable().expandRow(label, columnId);
    }

    public void expandNode(int index) {
        getTreeTable().expandRow(index);
    }

    public void collapseNode(int index) {
        getTreeTable().collapseRow(index);
    }

    public boolean isNodeExpanded(int index) {
        return getTreeTable().isRowExpanded(index);
    }

    public List<String> getActiveColumnsHeaders() {
        TreeTableWidget treeTable = getTreeTable();
        return treeTable.getActiveColumnHeaders();
    }

    public void changeColumnsOrderInTreeTable(String columnLabel, int position) {
        getTreeTable().changeColumnsOrder(columnLabel, position);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void enableColumn(String columnLabel, String... path) {
        AttributesChooser attributesChooser = getTreeTable().getAttributesChooser();
        attributesChooser.enableAttributeByLabel(columnLabel, path);
        attributesChooser.clickApply();
    }

    public void disableColumnAndApply(String columnLabel) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.disableColumnByLabel(columnLabel);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public int getColumnSize(String columnId) {
        int columnIndex = getTreeTable().getActiveColumnIds().indexOf(columnId);
        return getTreeTable().getColumnSize(columnIndex);
    }

    public void setPagination(int paginationValue) {
        PaginationComponent pagination = getTreeTable().getPagination();
        pagination.changeRowsCount(paginationValue);
    }

    public void setDefaultSettings() {
        getTreeTable().getAttributesChooser().clickDefaultSettings();
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
            DelayUtils.waitForPageToLoad(driver, wait);
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

    @Step("Canceling process")
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

    public void searchObject(String text) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.fullTextSearch(text);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public Multimap<String, String> searchByAttributeValue(String attributeId, String attributeValue, Input.ComponentType componentType) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.searchByAttribute(attributeId, componentType, attributeValue);
        DelayUtils.waitForPageToLoad(driver, wait);
        Multimap<String, String> filterValues = treeTable.getAppliedFilters();
        DelayUtils.waitForPageToLoad(driver, wait);
        return filterValues;
    }

    public void clearFilters() {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.clearAllFilters();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void callAction(String actionId) {
        TreeTableWidget treeTable = getTreeTable();
        DelayUtils.waitForPageToLoad(driver, wait);
        treeTable.callAction(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void callAction(String groupId, String actionId) {
        TreeTableWidget treeTable = getTreeTable();
        DelayUtils.waitForPageToLoad(driver, wait);
        treeTable.callAction(groupId, actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
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

    public Map<String, String> createProgramWithProcess(String programName, Long plusDays, String programType, String processName, Long plusDaysProcess, String processType) {
        return openProgramCreationWizard().createProgramWithProcess(programName, plusDays, programType, processName, plusDaysProcess, processType);
    }

    public String createSimpleNRP() {
        return openProcessCreationWizard().createSimpleNRPV2();
    }

    public String createSimpleDCP() {
        return openProcessCreationWizard().createSimpleDCPV2();
    }

    public void searchByAttributesValue(Multimap<String, String> idAndValues) {
        TreeTableWidget treeTable = getTreeTable();
        idAndValues.asMap().forEach((id, valueCollection) -> valueCollection.forEach(value -> treeTable.searchByAttribute(id, value)));
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public String createDCPWithPlusDays(Long plusDays) {
        return openProcessCreationWizard().createDCPWithPlusDays(plusDays);
    }

    public String createNRPWithPlusDays(Long plusDays) {
        return openProcessCreationWizard().createNRPWithPlusDays(plusDays);
    }

    public String createProcessIPD(String processName, Long plusDays, String processType) {
        return openProcessCreationWizard().createProcessIPD(processName, plusDays, processType);
    }

}
