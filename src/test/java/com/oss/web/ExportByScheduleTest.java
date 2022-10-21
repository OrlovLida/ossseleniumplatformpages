package com.oss.web;

import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.pages.exportguiwizard.ExportGuiWizardPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.schedulerservice.SchedulerServicePage;
import com.oss.untils.FakeGenerator;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class ExportByScheduleTest extends BaseTestCase {
    
    private static final String EXPORT_BUTTON_ID = "exportButton";
    private static final String TEST_MOVIE_TYPE = "TestMovie";
    private static final String TASK_NAME = "Test_Export" + FakeGenerator.getRandomInt();
    private static final String TIME_INPUT = "timeInput";
    private static final String TIME_SINGLE = "timeSingle";
    private SchedulerServicePage schedulerServicePage;
    
    @BeforeMethod
    public void openExportGuiWizard() {
        NewInventoryViewPage inventoryView = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_MOVIE_TYPE);
        inventoryView.callAction(ActionsContainer.KEBAB_GROUP_ID, EXPORT_BUTTON_ID);
    }
    
    @Test(priority = 1)
    @Description("Single Export using schedule export task")
    public void singleExportUsingScheduleExportTask() {
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseSingleSchedule()
                .setActualDate()
                .setTime(TIME_SINGLE, "2359")
                .closeTheWizard();
        schedulerServicePage = SchedulerServicePage.goToSchedulerServicePage(driver, BASIC_URL);
        schedulerServicePage
                .search(TASK_NAME);
        Assertions.assertThat(schedulerServicePage.getVisibleJobNames()).contains(TASK_NAME);
        
    }
    
    @Test(priority = 2)
    @Description("Daily Export using schedule export task")
    public void dailyExportUsingScheduleExportTask() {
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseDailySchedule()
                .setActualTime(TIME_INPUT)
                .closeTheWizard();
        schedulerServicePage = SchedulerServicePage.goToSchedulerServicePage(driver, BASIC_URL);
        schedulerServicePage
                .search(TASK_NAME);
        Assertions.assertThat(schedulerServicePage.getVisibleJobNames()).contains(TASK_NAME);
    }
    
    @Test(priority = 3)
    @Description("Weekly Export Using Schedule Export Task")
    public void weeklyExportUsingScheduleExportTask() {
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseWeeklySchedule()
                .repeatEveryWeekOn("Mon")
                .setActualTime("timeInputWeekly")
                .closeTheWizard();
        schedulerServicePage = SchedulerServicePage.goToSchedulerServicePage(driver, BASIC_URL);
        schedulerServicePage
                .search(TASK_NAME);
        Assertions.assertThat(schedulerServicePage.getVisibleJobNames()).contains(TASK_NAME);
    }
    
    @Test(priority = 4)
    @Description("Monthly Export Using Schedule Export Task")
    public void monthlyExportUsingScheduleExportTask() {
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseMonthlySchedule()
                .repeatEveryDayMonthly("1")
                .setActualTime(TIME_INPUT)
                .closeTheWizard();
        schedulerServicePage = SchedulerServicePage.goToSchedulerServicePage(driver, BASIC_URL);
        schedulerServicePage
                .search(TASK_NAME);
        Assertions.assertThat(schedulerServicePage.getVisibleJobNames()).contains(TASK_NAME);
    }
    
    @Test(priority = 5)
    @Description("Yearly Export Using Schedule Export Task")
    public void yearlyExportUsingScheduleExportTask() {
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseYearlySchedule()
                .repeatEveryMonth("June")
                .repeatEveryDayYearly("1")
                .setActualTime(TIME_INPUT)
                .closeTheWizard();
        schedulerServicePage = SchedulerServicePage.goToSchedulerServicePage(driver, BASIC_URL);
        schedulerServicePage
                .search(TASK_NAME);
        Assertions.assertThat(schedulerServicePage.getVisibleJobNames()).contains(TASK_NAME);
    }
    
    @AfterMethod
    public void deleteJobFromScheduleService() {
        schedulerServicePage
                .selectJob(TASK_NAME)
                .retireJob()
                .permanentlyDeleteJob();
    }
}
