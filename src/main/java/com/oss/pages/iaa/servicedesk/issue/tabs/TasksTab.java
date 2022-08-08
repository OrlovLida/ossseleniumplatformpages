package com.oss.pages.iaa.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.iaa.servicedesk.BaseSDPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

public class TasksTab extends BaseSDPage {

    private static final String TASK_OLD_ACTIONS_CONTAINER_ID = "_taskWindow-windowToolbar";
    private static final String ADD_TASK_ID = "add_task";
    private static final String CREATE_TASK_PROMPT_ID = "_createTask_prompt-card";
    private static final String TASK_DETAIL_PROMPT_ID = "_taskDetailsFormId";
    private static final String COMMON_TASK_LIST_ID = "_tasksWidget";
    private static final String DETAILS_BUTTON_ID = "Details";

    public TasksTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Add Task in Problem Details View")
    public SDWizardPage clickAddTaskInProblemView() {
        OldActionsContainer.createById(driver, wait, TASK_OLD_ACTIONS_CONTAINER_ID)
                .callActionById(ADD_TASK_ID);
        return new SDWizardPage(driver, wait, CREATE_TASK_PROMPT_ID);
    }

    @Step("Get task name")
    public String getTaskName() {
        return getFirstTask().getValue("Task Name");
    }

    @Step("Click Details button")
    public SDWizardPage clickDetailsButtonInFirstTask() {
        getFirstTask().callAction(DETAILS_BUTTON_ID);
        log.info("Click Details Button in first Task on the List");
        return new SDWizardPage(driver, wait, TASK_DETAIL_PROMPT_ID);
    }

    private CommonList.Row getFirstTask() {
        return getTaskList().getRows().get(0);
    }

    private CommonList getTaskList() {
        return CommonList.create(driver, wait, COMMON_TASK_LIST_ID);
    }
}
