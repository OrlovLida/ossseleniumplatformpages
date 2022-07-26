package com.oss.pages.bpm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Multimap;
import com.oss.framework.components.inputs.Input;
import com.oss.pages.bpm.processinstances.ProcessWizardPage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.attributechooser.AttributesChooser;
import com.oss.framework.components.pagination.PaginationComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.table.TableRow;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.treetable.TreeTableWidget;
import com.oss.pages.BasePage;

public class PlannersViewPage extends BasePage {

    private static final String TREE_TABLE_ID = "process_instance_hierarchy_table";
    private static final String TABS_CONTAINER_ID = "process_instance_hierarchy_bottom_tabs";
    private static final String CREATE_GROUP_ACTION_ID = "CREATE";
    private static final String START_PROCESS_ACTION_ID = "bpm_inventory_view_action_create_process";
    private static final String START_PROGRAM_ACTION_ID = "bpm_inventory_view_action_create_program";

    public PlannersViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static PlannersViewPage goToPlannersViewPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/views/management/views/ProcessInstances?perspective=LIVE", basicURL));
        WebDriverWait wait = new WebDriverWait(driver, 45);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new PlannersViewPage(driver, wait);
    }

    public TreeTableWidget getTreeTable() {
        return TreeTableWidget.createById(driver, wait, TREE_TABLE_ID);
    }

    public void selectObjectByRowId(int rowId) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.selectNode(rowId);
    }

    public void selectSeveralObjectsByRowId(int... indexes) {
        List<Integer> rows = Arrays.stream(indexes).boxed().collect(Collectors.toList());
        rows.forEach(this::selectObjectByRowId);
    }

    public void unselectObjectByRowId(int rowId) {
        TreeTableWidget treeTable = getTreeTable();
        treeTable.unselectNode(rowId);
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

    public TabsWidget getTabsWidget() {
        if (getSelectedRows().isEmpty()) {
            throw new UnsupportedOperationException("Only single selection is supported");
        }
        Widget.waitForWidget(wait, TabsWidget.TABS_WIDGET_CLASS);
        return TabsWidget.createById(driver, wait, TABS_CONTAINER_ID);
    }

    public String getAttributeValue(String columnId, int rowId) {
        TreeTableWidget treeTable = getTreeTable();
        return treeTable.getCellValue(rowId, columnId);
    }

    public void expandNode(String label, String columnId) {
        getTreeTable().expandNode(label, columnId);
    }

    public void expandNode(int index) {
        getTreeTable().expandNode(index);
    }

    public void collapseNode(int index) {
        getTreeTable().collapseNode(index);
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

    public String createProgramWithProcess(String programName, Long plusDays, String programType, String processName, Long plusDaysProcess, String processType) {
        return openProgramCreationWizard().createProgramWithProcess(programName, plusDays, programType, processName, plusDaysProcess, processType);
    }

    public String createSimpleNRP() {
        return openProcessCreationWizard().createSimpleNRPV2();
    }

    public String createSimpleDCP() {
        return openProcessCreationWizard().createSimpleDCPV2();
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
