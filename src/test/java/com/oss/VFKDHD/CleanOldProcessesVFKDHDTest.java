package com.oss.VFKDHD;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.pagination.PaginationComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.treetable.TreeTableWidget;
import com.oss.pages.bpm.processinstances.PlannersViewPage;
import com.oss.pages.bpm.tasks.TasksPage;

import io.qameta.allure.Description;

public class CleanOldProcessesVFKDHDTest extends BaseTestCase {
    private static final String CORRECTION_REASON = "Automatic Test";
    private static final String TASK_NAME = "Correct data";

    private static final String PROCESS_CODE_COLUMN_ID = "code";
    private static final String PLANNED_OBJECTS_TAB_ID = "panel_planned_objects_groupundefined";
    private static final String PLANNERS_VIEW = "Planners View";
    private static final String[] BUSINESS_PROCESS_MANAGEMENT_PATH = {"BPM and Planning", "Business Process Management"};
    private static final String BPM_TASKS = "Tasks";
    private static final String NAME_ATTRIBUTE_ID = "name";

    static private final Multimap<String, String> USED_FILTERS;
    private static final String STATIC_PART_OF_NAME_OF_TESTS = "Selenium Test";
    private static final String STATE_FILTER_ID = "state";
    private final static String IN_PROGRESS_STATE = "In Progress";

    static {
        USED_FILTERS = HashMultimap.create();
        USED_FILTERS.put(NAME_ATTRIBUTE_ID, STATIC_PART_OF_NAME_OF_TESTS);
        USED_FILTERS.put(STATE_FILTER_ID, IN_PROGRESS_STATE);
    }

    @Test(description = "Cancel Old Data Correction Processes that were failed")
    @Description("Cancel Old Data Correction Processes that were failed")
    public void cancelOldDCP() {
        SoftAssert softAssert = new SoftAssert();
        homePage.chooseFromLeftSideMenu(PLANNERS_VIEW, BUSINESS_PROCESS_MANAGEMENT_PATH);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PlannersViewPage plannersViewPage = new PlannersViewPage(driver, webDriverWait);
        plannersViewPage.searchByAttributesValue(USED_FILTERS);
        List<String> notCancellableProcesses = new ArrayList<>();
        if (!isPageEmpty(plannersViewPage)) {
            int numberOfProcesses = plannersViewPage.getTreeTable().getPagination().getTotalCount();
            cancelCancellableProcessesAndCreteListOfNotCancellableOnes(plannersViewPage, notCancellableProcesses, numberOfProcesses);
            numberOfProcesses = notCancellableProcesses.size();
            findAndRemoveAllElementsFromDataCorrectionProcesses(notCancellableProcesses);
            homePage.chooseFromLeftSideMenu(PLANNERS_VIEW, BUSINESS_PROCESS_MANAGEMENT_PATH);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            plannersViewPage = new PlannersViewPage(driver, webDriverWait);
            plannersViewPage.searchByAttributesValue(USED_FILTERS);
            cancelProcessesWhileNotCheckingForTheirCancellability(plannersViewPage, numberOfProcesses);
            SystemMessageContainer systemMessage = SystemMessageContainer.create(driver, webDriverWait);
            systemMessage.close();
            softAssert.assertTrue(isPageEmpty(plannersViewPage));
            softAssert.assertAll();
        }
    }

    private void cancelProcessesWhileNotCheckingForTheirCancellability(PlannersViewPage plannersViewPage, int numberOfProcesses) {
        for (int canceledCounter = 0; canceledCounter < numberOfProcesses; canceledCounter++) {
            plannersViewPage.getTreeTable().clickRow(0);
            plannersViewPage.terminateProcess(CORRECTION_REASON);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    private void cancelCancellableProcessesAndCreteListOfNotCancellableOnes(PlannersViewPage plannersViewPage, List<String> notCancellableProcesses, int numberOfProcesses) {
        List<String> processCodes = getAllProcessCodes(plannersViewPage, numberOfProcesses);
        boolean canTerminateProcess;
        for (int canceledCounter = 0; canceledCounter < numberOfProcesses; canceledCounter++) {
            try {
                canTerminateProcess = plannersViewPage.isProcessTerminable(processCodes.get(canceledCounter));
            } catch (NoSuchElementException e) {
                plannersViewPage.getTreeTable().getPagination().goOnNextPage();
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
                canTerminateProcess = plannersViewPage.isProcessTerminable(processCodes.get(canceledCounter));
            }
            if (canTerminateProcess) {
                plannersViewPage.terminateProcess(CORRECTION_REASON);
            } else {
                notCancellableProcesses.add(processCodes.get(canceledCounter));
                plannersViewPage.unselectObjectByProcessCode(processCodes.get(canceledCounter));
            }
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    private boolean isPageEmpty(PlannersViewPage plannersViewPage) {
        return plannersViewPage.getTreeTable().hasNoData();
    }

    private void findAndRemoveAllElementsFromDataCorrectionProcesses(List<String> codes) {
        homePage.chooseFromLeftSideMenu(BPM_TASKS, BUSINESS_PROCESS_MANAGEMENT_PATH);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TasksPage tasksPage = new TasksPage(driver);
        tasksPage.clearAllColumnFilters();
        for (String code : codes) {
            if (!tasksPage.isTaskStarted(code, TASK_NAME)) {
                tasksPage.startTask(code, TASK_NAME);
            }
            tasksPage.getKDTaskForm().expandPanel(PLANNED_OBJECTS_TAB_ID);
            tasksPage.getKDTaskForm().removeAllPlannedObjects();
        }
    }

    private List<String> getAllProcessCodes(PlannersViewPage plannersViewPage, int numberOfProcesses) {
        TreeTableWidget treeTable = plannersViewPage.getTreeTable();
        PaginationComponent paginationComponent = treeTable.getPagination();
        int paginationOption = 500, maxRowIndexOnPage = paginationOption - 1, indexOdLastProcess = numberOfProcesses - 1;
        int pageNumber = 1;
        paginationComponent.changeRowsCount(paginationOption);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        List<String> activeProcesses = new ArrayList<>(numberOfProcesses);
        for (int actualRowNumber = 0; activeProcesses.size() < indexOdLastProcess; actualRowNumber++) {
            activeProcesses.add(treeTable.getCellValue(actualRowNumber, PROCESS_CODE_COLUMN_ID));
            if (paginationComponent.isNextPageButtonPresent() && actualRowNumber == maxRowIndexOnPage) {
                paginationComponent.goOnNextPage();
                pageNumber++;
                actualRowNumber = 0;
                DelayUtils.waitForPageToLoad(driver, webDriverWait);
                treeTable = plannersViewPage.getTreeTable();
            }
        }
        if (pageNumber > 1) {
            paginationComponent.goOnFirstPage();
        }
        return activeProcesses;
    }
}
