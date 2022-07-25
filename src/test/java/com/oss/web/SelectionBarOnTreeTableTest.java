package com.oss.web;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;

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
    private PlannersViewPage plannersViewPage;
    private TreeTableWidget treeTableWidget;
    private static final String BPM_USER_LOGIN = "bpm_webselenium";
    private static final String BPM_USER_PASSWORD = "Webtests123!";
    private static final String USER1_LOGIN = "webseleniumtests";

    private static final String NRP = "Network Resource Process";
    private static final String PROGRAM_NAME = "Program Selenium " + (Math.random() * 1001);
    private static final String PROCESS_NAME = "Process Selenium " + (Math.random() * 1001);
    private static final String PROGRAM_TYPE = "pr_program";
    private static final String PROCESS_TYPE = "Data Correction Process";
    private static final String RELATED_TASK = "Related Tasks";

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
        treeTableWidget.showOnlySelectedRows();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(treeTableWidget.getPagination().getTotalCount(), 3);
        Assert.assertFalse(treeTableWidget.canRowBeExpanded(0));
        Assert.assertFalse(treeTableWidget.canRowBeExpanded(1));
        Assert.assertFalse(treeTableWidget.canRowBeExpanded(2));
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
        treeTableWidget.showAllRows();

        Assert.assertEquals(treeTableWidget.getPagination().getTotalCount(), allRows);
    }

    @Test(priority = 5)
    public void selectChildProcess() {
        createProgramWithProcessForTest(PROGRAM_NAME, PROCESS_NAME);
        plannersViewPage.searchObject(PROGRAM_NAME);
        plannersViewPage.expandNode(0);
        plannersViewPage.expandNode(2);
        plannersViewPage.clearFilters();
        plannersViewPage.selectNodeByLabelsPath(PROCESS_NAME);
        treeTableWidget.showOnlySelectedRows();
        String selectedObjectCount = treeTableWidget.getSelectedObjectCount();
        Assert.assertEquals(treeTableWidget.getPagination().getTotalCount(), 2);
        Assert.assertEquals(selectedObjectCount, ONE_SELECTED);
    }

    @Test(priority = 6)
    public void unselectChildProcess() {
        plannersViewPage.unselectObjectByRowId(0);
        treeTableWidget.showAllRows();
        String selectedObjectCount = treeTableWidget.getSelectedObjectCount();
        Assert.assertEquals(selectedObjectCount, ZERO_SELECTED);
    }

    @Test(priority = 7)
    public void checkIfUnselectedChildProcessAreNotVisible() {
        driver.navigate().refresh();
        plannersViewPage.searchObject(PROGRAM_NAME);
        plannersViewPage.expandNode(0);
        plannersViewPage.expandNode(1);
        plannersViewPage.selectSeveralObjectsByRowId(2);
        plannersViewPage.clearFilters();
        selectRowsOnNextPage(2);
        treeTableWidget.showOnlySelectedRows();
        String selectedObjectCount = treeTableWidget.getSelectedObjectCount();
        plannersViewPage.expandNode(0);
        Assert.assertEquals(selectedObjectCount, TWO_SELECTED);
        Assert.assertEquals(treeTableWidget.getPagination().getTotalCount(), 2);
        Assert.assertFalse(plannersViewPage.isNodePresent(RELATED_TASK));

    }

    private void selectRowsOnNextPage(int... index) {
        treeTableWidget.getPagination().goOnNextPage();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        plannersViewPage.selectSeveralObjectsByRowId(index);
    }

    public void createProgramWithProcessForTest(String programName, String processName) {
        plannersViewPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
        plannersViewPage.createProgramWithProcess(programName, 5L, PROGRAM_TYPE, processName, 5L, PROCESS_TYPE);
        plannersViewPage.changeUser(USER1_LOGIN, BPM_USER_PASSWORD);

    }

}

