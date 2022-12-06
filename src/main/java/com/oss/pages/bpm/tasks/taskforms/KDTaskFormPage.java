package com.oss.pages.bpm.tasks.taskforms;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;

import io.qameta.allure.Step;

public class KDTaskFormPage extends IPDTaskFormPage {

    private static final String PLANNED_OBJECT_WIDGET_ID = "form.planned_objects_group.planned_objects_table";
    private static final String CANCEL_BUTTON_ID = "objectsRemoveAction";
    private static final String PROCEED_WITH_CANCELLATION_ID = "ConfirmationBox_plaCancelObjectsWizard_confirmBox_action_button";

    protected KDTaskFormPage(WebDriver driver, WebDriverWait wait, String tabsTasksViewId) {
        super(driver, wait, tabsTasksViewId);
    }

    public static KDTaskFormPage create(WebDriver driver, WebDriverWait wait, String tabsTasksViewId) {
        return new KDTaskFormPage(driver, wait, tabsTasksViewId);
    }

    @Step("Removing all planned object")
    public void removeAllPlannedObjects() {
        OldTable oldTable = OldTable.createById(driver, wait, PLANNED_OBJECT_WIDGET_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        oldTable.setPageSize(200);
        DelayUtils.waitForPageToLoad(driver, wait);
        oldTable.selectAllRows();
        DelayUtils.waitForPageToLoad(driver, wait);
        oldTable.callAction(CANCEL_BUTTON_ID);
        ConfirmationBox.create(driver, wait).clickButtonById(PROCEED_WITH_CANCELLATION_ID);
    }
}
