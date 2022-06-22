package com.oss.pages.schedulerservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebDriver;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class SchedulerServicePage extends BasePage {
    
    private static final String SCHEDULER_SERVICE_LIST_ID = "schedulerServiceListAppId";
    private static final String JOB_NAME_COLUMN = "Job name";
    private static final String EDIT_ACTION_ID = "EDIT";
    private static final String RETIRE_JOB_ACTION_ID = "schedulerServiceRetireJobAct";
    private static final String RETIRE_CONFIRMATION_BUTTON = "ConfirmationBox_retireJobConfirmationBox_action_button";
    private static final String PERMANENTLY_DELETE_JOB_ACTION_ID = "schedulerServicePermanentlyDeleteJobAct";
    private static final String PERMANENTLY_REMOVE_JOB_CONFIRMATION_BUTTON =
            "ConfirmationBox_permanentlyRemoveJobConfirmationBox_action_button";
    
    public SchedulerServicePage(WebDriver driver) {
        super(driver);
    }
    
    @Step("Open Scheduler Service Page")
    public static SchedulerServicePage goToSchedulerServicePage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/scheduler-service-view/main/global" +
                "?perspective=LIVE", basicURL));
        return new SchedulerServicePage(driver);
    }
    
    public List<String> getVisibleJobNames() {
        OldTable table = getTable();
        List<String> names = new ArrayList<>(Collections.emptyList());
        int rowNumber = table.countRows(JOB_NAME_COLUMN);
        for (int i = 0; i < rowNumber; i++) {
            String jobNameValue = table.getCellValue(i, JOB_NAME_COLUMN);
            names.add(jobNameValue);
        }
        return names;
    }
    
    @Step("Find created Job and click on it")
    public SchedulerServicePage search(String name) {
        typeInSearchField(name);
        return this;
    }
    
    @Step("Delete created Job")
    public SchedulerServicePage retireJob() {
        getTable().callAction(EDIT_ACTION_ID, RETIRE_JOB_ACTION_ID);
        ConfirmationBox.create(driver, wait).clickButtonById(RETIRE_CONFIRMATION_BUTTON);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }
    
    @Step("Permanently delete created Job")
    public SchedulerServicePage permanentlyDeleteJob() {
        getTable().callAction(EDIT_ACTION_ID, PERMANENTLY_DELETE_JOB_ACTION_ID);
        ConfirmationBox.create(driver, wait).clickButtonById(PERMANENTLY_REMOVE_JOB_CONFIRMATION_BUTTON);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }
    
    private void typeInSearchField(String value) {
        getTable().fullTextSearch(value);
    }
    
    public SchedulerServicePage selectJob(String text) {
        getTable().selectRowByAttributeValueWithLabel(JOB_NAME_COLUMN, text);
        return this;
        
    }
    
    private OldTable getTable() {
        return OldTable.createById(driver, wait, SCHEDULER_SERVICE_LIST_ID);
    }
}
