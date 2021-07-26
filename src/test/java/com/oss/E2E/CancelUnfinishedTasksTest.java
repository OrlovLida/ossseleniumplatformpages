package com.oss.E2E;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.TasksPage;

public class CancelUnfinishedTasksTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(CancelUnfinishedTasksTest.class);
    private static final String USERNAME = "webseleniumtests";
    private static final String CANCEL = "cancel";
    private static final String NEEDS_CLARIFICATION = "needsClarification";
    private static TasksPage tasksPage;

    @BeforeClass
    public void openConsole() {
        waitForPageToLoad();
    }

    @Test(priority = 1)
    public void cancelUnfinishedTasks() {
        tasksPage = TasksPage.goToTasksPage(driver, webDriverWait, BASIC_URL);
        waitForPageToLoad();
        tasksPage.clearFilter();
        String[] tasks = { TasksPage.HIGH_LEVEL_PLANNING_TASK, TasksPage.LOW_LEVEL_PLANNING_TASK, TasksPage.CORRECT_DATA_TASK };
        for (String taskName : tasks) {
            log.debug("Started canceling {} tasks", taskName);
            String processCode = tasksPage.startTaskByUsernameAndTaskName(USERNAME, taskName);
            while (!processCode.equals("There is no task for specified values")) {
                log.debug("Cancel task with Process Code = {}", processCode);
                cancelProcess(processCode, taskName);
                processCode = tasksPage.startTaskByUsernameAndTaskName(USERNAME, taskName);
            }
            log.debug("Finished canceling {} tasks", taskName);
        }
    }

    private static void cancelProcess(String processCode, String taskName) {
        switch (taskName) {
            case TasksPage.CORRECT_DATA_TASK:
                tasksPage.changeTransitionAndCompleteTask(processCode, TasksPage.CORRECT_DATA_TASK, CANCEL);
                break;
            case TasksPage.HIGH_LEVEL_PLANNING_TASK:
                tasksPage.changeTransitionAndCompleteTask(processCode, TasksPage.HIGH_LEVEL_PLANNING_TASK, NEEDS_CLARIFICATION);
                tasksPage.startTask(processCode, TasksPage.UPDATE_REQUIREMENTS_TASK);
                tasksPage.changeTransitionAndCompleteTask(processCode, TasksPage.UPDATE_REQUIREMENTS_TASK, CANCEL);
                break;
            case TasksPage.LOW_LEVEL_PLANNING_TASK:
                tasksPage.changeTransitionAndCompleteTask(processCode, TasksPage.LOW_LEVEL_PLANNING_TASK, CANCEL);
                break;
            default:
                throw new IllegalArgumentException("Cannot map task name = " + taskName);
        }
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
