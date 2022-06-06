package com.oss.web;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.exportguiwizard.ExportGuiWizardPage;
import com.oss.pages.languageservice.LanguageServicePage;
import com.oss.pages.schedulerservice.SchedulerServicePage;
import com.oss.untils.FakeGenerator;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class ExportByScheduleTest extends BaseTestCase {
    
    private static final String LANGUAGE_SERVICE_PAGE_URL = String.format("%s/#/views/languagesservice/views/translations" +
            "?perspective=LIVE", BASIC_URL);
    private static String TASK_NAME = "Test_Export"+ FakeGenerator.getRandomInt();

    private SchedulerServicePage schedulerServicePage;
    
    @BeforeMethod
    public void openExportGuiWizard() {
        LanguageServicePage languageServicePage = new LanguageServicePage(driver);
        homePage.goToLanguageServicePage(LANGUAGE_SERVICE_PAGE_URL);
        languageServicePage
                .typeIdOfFirstServiceInSearch()
                .clearNotifications()
                .openExportFileWizard();
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
                .setTime("2359")
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
                .setActualTime()
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
                .repeatEveryWeekOn(1)
                .setActualTime()
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
                .setActualTime()
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
                .setActualTime()
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
