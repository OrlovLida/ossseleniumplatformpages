package com.oss.smoketests;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.GlobalNotificationContainer;
import com.oss.framework.components.attributechooser.AttributesChooser;
import com.oss.framework.components.layout.ErrorCard;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.bpm.TasksPageV2;
import com.oss.pages.platform.HomePage;

import io.qameta.allure.Description;

public class Tasks_Smoke_Test extends BaseTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(Tasks_Smoke_Test.class);
    private static final String UNCOMPLETED_TASKS = "Uncompleted Tasks";
    private static final String PROCESS_NAME = "Name (Process)";
    private static final String STATUS = "Status";
    private static final String ASSIGNEE = "Assignee";
    private static final String PROCESS_INSTANCE_ID = "Process Instance Id";
    private static final String TASKS = "Tasks";
    private static final String BPM_AND_PLANNING = "BPM and Planning";
    private static final String PROCESS_OPERATIONS = "Process Operations";
    private static final String END_TIME = "End Time";
    private static final String START_TIME = "Start Time";
    private static final String PROCESS_CODE = "Process Code";
    private static final String PRIORITY = "Priority";
    private static final String TASK_NAME = "Task Name";
    private static final String SAVE_TASK_ID = "saveTask";
    private static final String SAVE_TASK_LABEL = "Save task";
    private static final String NOT_LOADED_SAVE_TASK_LABEL = "webAppTaskDetailsSaveTaskLabel";
    private static final String ATTRIBUTES = "Attributes";
    private static final String NO_CONTEXT_ACTIONS_EXCEPTION = "No context actions are available.";
    private static final String PROCESS_INSTANCE_COLUMND_ID = "processInstanceId";
    private static final String PROCESS_INSTANCE_ATTRIBUTE = "Process instance ID";

    @Test(priority = 1, description = "Open Tasks View")
    @Description("Open Tasks View")
    public void openTasksView() {
        waitForPageToLoad();
        checkErrorPage();
        checkGlobalNotificationContainer();
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(TASKS, BPM_AND_PLANNING, PROCESS_OPERATIONS);
        waitForPageToLoad();
    }

    @Test(priority = 2, description = "Select Uncompleted Tasks from Quick Filters", dependsOnMethods = {"openTasksView"})
    @Description("Select Uncompleted Tasks from Quick Filters")
    public void useQuickFilters() {
        checkErrorPage();
        checkGlobalNotificationContainer();
        TasksPageV2 tasksPage = new TasksPageV2(driver);
        tasksPage.setQuickFilter(UNCOMPLETED_TASKS);
        waitForPageToLoad();
        Assert.assertEquals(tasksPage.getAppliedQuickFilters().size(), 1);
        Assert.assertEquals(tasksPage.getAppliedQuickFilters().stream().findFirst().orElseThrow(NoSuchElementException::new), UNCOMPLETED_TASKS);
        waitForPageToLoad();
    }

    @Test(priority = 3, description = "Use Column management to change displayed columns", dependsOnMethods = {"useQuickFilters"})
    @Description("Use Column management to change displayed columns")
    public void changeDisplayedColumns() {
        TasksPageV2 tasksPage = new TasksPageV2(driver);
        AttributesChooser attributesChooser = tasksPage.getAttributesChooser();
        attributesChooser.disableAttributeByLabel(END_TIME);
        attributesChooser.disableAttributeByLabel(START_TIME);
        attributesChooser.disableAttributeByLabel(PROCESS_CODE);
        attributesChooser.disableAttributeByLabel(PRIORITY);
        attributesChooser.disableAttributeByLabel(TASK_NAME);
        attributesChooser.enableAttributeByLabel(PROCESS_INSTANCE_ID);
        waitForPageToLoad();
        attributesChooser.clickApply();
        waitForPageToLoad();
        List<String> columnsList = tasksPage.getActiveColumnHeaders();
        Assert.assertEquals(columnsList.size(), tasksViewTestColumnsList.size());
        for (int i = 0; i < tasksViewTestColumnsList.size(); i++) {
            LOGGER.info("Checking attribute with index: {}, which equals: '{}' on declared assertionList, and equals '{}' on properties list taken from GUI", i, tasksViewTestColumnsList.get(i), columnsList.get(i));
            Assert.assertEquals(tasksViewTestColumnsList.get(i), columnsList.get(i));
        }
    }

    @Test(priority = 4, description = "Check Form tab", dependsOnMethods = {"changeDisplayedColumns"})
    @Description("Check Form tab")
    public void checkFormTab() {
        TasksPageV2 tasksPage = new TasksPageV2(driver);
        tasksPage.selectFirstTask();
        waitForPageToLoad();
        try {
            TabsWidget tabsWidget = TabsWidget.createById(driver, new WebDriverWait(driver, 5), TasksPageV2.TABS_TASKS_VIEW_ID);
            String actionLabel = tabsWidget.getActionsInterface().getActionLabel(SAVE_TASK_ID);
            Assert.assertTrue(actionLabel.equals(SAVE_TASK_LABEL) || actionLabel.equals(NOT_LOADED_SAVE_TASK_LABEL));
        } catch (TimeoutException ex) {
            Assert.fail(NO_CONTEXT_ACTIONS_EXCEPTION);
        }
    }

    @Test(priority = 5, description = "Check Attributes tab", dependsOnMethods = {"changeDisplayedColumns"})
    @Description("Check Attributes tab")
    public void checkAttributesTab() {
        TasksPageV2 tasksPage = new TasksPageV2(driver);
        String valueFromTable = tasksPage.getColumnValueFromFirstRow(PROCESS_INSTANCE_COLUMND_ID);
        tasksPage.getIPDTaskForm().selectTabByLabel(ATTRIBUTES);
        waitForPageToLoad();
        String valueFromAttributes = tasksPage.getIPDTaskForm().getAttributeValue(PROCESS_INSTANCE_ATTRIBUTE);
        Assert.assertEquals(valueFromAttributes, valueFromTable);
    }

    private void checkErrorPage() {
        ErrorCard errorCard = ErrorCard.create(driver, webDriverWait);
        if (errorCard.isErrorPagePresent()) {
            ErrorCard.ErrorInformation errorInformation = errorCard.getErrorInformation();
            LOGGER.error(errorInformation.getErrorText());
            LOGGER.error(errorInformation.getErrorDescription());
            LOGGER.error(errorInformation.getErrorMessage());
            Assert.fail("Error Page is shown.");
        }
    }

    private void checkGlobalNotificationContainer() {
        GlobalNotificationContainer globalNotificationContainer = GlobalNotificationContainer.create(driver, webDriverWait);
        if (globalNotificationContainer.isErrorNotificationPresent()) {
            GlobalNotificationContainer.NotificationInformation information = globalNotificationContainer.getNotificationInformation();
            LOGGER.error(information.getMessage());
            Assert.fail("Global Notification shows error.");
        }
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private static final List<String> tasksViewTestColumnsList = new ImmutableList.Builder<String>()
            .add(PROCESS_NAME)
            .add(STATUS)
            .add(ASSIGNEE)
            .add(PROCESS_INSTANCE_ID)
            .build();
}