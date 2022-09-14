package com.oss.web;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;

import com.oss.framework.widgets.table.TableRow;
import com.oss.framework.widgets.treetable.TreeTableWidget;
import com.oss.pages.bpm.PlannersViewPage;

/**
 * @author Faustyna Szczepanik
 */

public class SelectionBarOnTreeTableTest extends BaseTestCase {

    private static final String THREE_SELECTED = "3 selected";
    private static final String TWO_SELECTED = "2 selected";
    private static final String ZERO_SELECTED = "0 selected";
    private static final String ONE_SELECTED = "1 selected";
    private static final String NAME_ID = "name";
    private static final String PROGRAM_NAME = "Program Selenium " + (Math.random() * 1001);
    private static final String PROCESS_NAME = "Process Selenium " + (Math.random() * 1001);
    private static final String PROGRAM_TYPE = "audit_program";

    private static final String PROCESS_TYPE = "Data Correction Process";
    private static final String CHILD_PROCESSES = "Child Processes";
    private static final String REFRESH_BUTTON_ID = "refreshButton";
    private PlannersViewPage plannersViewPage;
    private TreeTableWidget treeTableWidget;

    @BeforeClass
    public void goToPlannersView() {
        plannersViewPage = PlannersViewPage.goToPlannersViewPage(driver, BASIC_URL);
        treeTableWidget = plannersViewPage.getTreeTable();
    }

    @Test(priority = 1)
    public void selectRoots() {
        plannersViewPage.selectObjectByRowId(0);
        selectRowsOnNextPage(1, 3);
        String selectedObjectCount = treeTableWidget.getSelectedObjectCount();
        Assert.assertEquals(selectedObjectCount, THREE_SELECTED);
    }

    @Test(priority = 2)
    public void showSelectedForRoots() {
        plannersViewPage.expandNode(1);
        plannersViewPage.expandNode(2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        treeTableWidget.showOnlySelectedRows();
        Assert.assertEquals(treeTableWidget.getPagination().getTotalCount(), 3);
        Assert.assertFalse(treeTableWidget.isExpandPresent(0));
        Assert.assertFalse(treeTableWidget.isExpandPresent(1));
        Assert.assertFalse(treeTableWidget.isExpandPresent(2));
    }

    @Test(priority = 3)
    public void unselectAllObjects() {
        treeTableWidget.unselectAllRows();
        String selectedObjectCount = treeTableWidget.getSelectedObjectCount();
        Assert.assertEquals(selectedObjectCount, ZERO_SELECTED);
        treeTableWidget.showAllRows();
    }

    @Test(priority = 4)
    public void showAllRows() {
        int allRows = treeTableWidget.getPagination().getTotalCount();
        plannersViewPage.selectObjectByRowId(0);
        plannersViewPage.selectObjectByRowId(3);
        treeTableWidget.showOnlySelectedRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        treeTableWidget.showAllRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(treeTableWidget.getPagination().getTotalCount(), allRows);
        treeTableWidget.unselectAllRows();
    }

    @Test(priority = 5)
    public void selectChildProcess() {
        createProgramWithProcessForTest(PROGRAM_NAME, PROCESS_NAME);
        SystemMessageContainer.create(driver, webDriverWait).close();
        plannersViewPage.searchObject(PROGRAM_NAME);
        plannersViewPage.expandNode(0);
        plannersViewPage.callAction(ActionsContainer.KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        plannersViewPage.expandNode(2);
        plannersViewPage.selectObjectByAttributeValue(NAME_ID, PROCESS_NAME);
        plannersViewPage.clearFilters();
        treeTableWidget.showOnlySelectedRows();
        String selectedObjectCount = treeTableWidget.getSelectedObjectCount();
        Assert.assertEquals(treeTableWidget.getPagination().getTotalCount(), 2);
        Assert.assertEquals(selectedObjectCount, ONE_SELECTED);
    }

    @Test(priority = 6)
    public void unselectChildProcess() {
        plannersViewPage.unselectObjectByRowId(3);
        treeTableWidget.showAllRows();
        String selectedObjectCount = treeTableWidget.getSelectedObjectCount();
        Assert.assertEquals(selectedObjectCount, ZERO_SELECTED);
    }

    @Test(priority = 7, enabled = false, groups = "group1")
    public void checkIfUnselectedChildProcessAreNotVisible() {
        driver.navigate().refresh();
        plannersViewPage.searchObject(PROGRAM_NAME);
        plannersViewPage.expandNode(0);
        plannersViewPage.expandNode(1);
        plannersViewPage.selectObjectByRowId(2);
        plannersViewPage.clearFilters();
        selectRowsOnNextPage(2);
        treeTableWidget.showOnlySelectedRows();
        String selectedObjectCount = treeTableWidget.getSelectedObjectCount();
        plannersViewPage.expandNode(0);
        Assert.assertEquals(selectedObjectCount, TWO_SELECTED);
        Assert.assertEquals(treeTableWidget.getPagination().getTotalCount(), 2);
        Assert.assertNotEquals(treeTableWidget.getCellValue(2, NAME_ID), CHILD_PROCESSES);
    }

    @AfterGroups("group1")
    public void afterMethod() {
        treeTableWidget.unselectAllRows();
        treeTableWidget.showAllRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8)
    public void filterAndShowSelected() {
        plannersViewPage.selectObjectByAttributeValue(NAME_ID, PROGRAM_NAME);
        plannersViewPage.searchByAttributeValue("program", "False", Input.ComponentType.MULTI_COMBOBOX);
        treeTableWidget.showOnlySelectedRows();
        String selectedObjectCount = treeTableWidget.getSelectedObjectCount();
        Assert.assertEquals(selectedObjectCount, ONE_SELECTED);
        List<TableRow> allRows = plannersViewPage.getRows();
        Assert.assertTrue(allRows.isEmpty());
    }

    @Test(priority = 9)
    public void unselectUnfilteredObject() {
        treeTableWidget.unselectAllRows();
        treeTableWidget.showAllRows();
        String selectedObjectCount = treeTableWidget.getSelectedObjectCount();
        Assert.assertEquals(selectedObjectCount, ZERO_SELECTED);
    }

    private void selectRowsOnNextPage(int... index) {
        treeTableWidget.getPagination().goOnNextPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        plannersViewPage.selectObjectsByRowId(index);
    }

    private void createProgramWithProcessForTest(String programName, String processName) {
        plannersViewPage.createProgramWithProcess(programName, 5L, PROGRAM_TYPE, processName, 5L, PROCESS_TYPE);

    }
}

