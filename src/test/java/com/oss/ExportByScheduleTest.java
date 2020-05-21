package com.oss;

import com.oss.pages.exportguiwizard.ExportGuiWizardPage;
import com.oss.pages.languageservice.LanguageServicePage;
import com.oss.pages.schedulerservice.SchedulerServicePage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class ExportByScheduleTest extends BaseTestCase{

    private static final String LANGUAGE_SERVICE_PAGE_URL = String.format("%s/#/views/languagesservice/views/translations" +
            "?perspective=LIVE", BASIC_URL);
    private static final String SCHEDULER_SERVICE_PAGE_URL = String.format("%s/#/view/scheduler-service-view/main/global?perspective=LIVE" +
            "?perspective=LIVE", CONFIGURATION.getValue("baseUrl"));
    private static  String TASK_NAME = "Test_Export123";

    private LanguageServicePage languageServicePage;
    private SchedulerServicePage schedulerServicePage;


    @BeforeMethod
    public void openExportGuiWizard() {
        languageServicePage = new LanguageServicePage(driver);
        homePage.goToLanguageServicePage(LANGUAGE_SERVICE_PAGE_URL);
        languageServicePage
                .typeIdOfFirstServiceInSearch()
                .clearNotifications()
                .openExportFileWizard();
    }

    @Test
    public void singleExportUsingScheduleExportTask(){
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseSingleSchedule()
                .setActualDate()
                .setTime("2359")
                .closeTheWizard();
        schedulerServicePage = homePage.goToSchedulerServicePage(SCHEDULER_SERVICE_PAGE_URL);
        schedulerServicePage
                .findJobAndClickOnIT(TASK_NAME);
        Assert.assertEquals(schedulerServicePage.getTextOfJob(TASK_NAME), TASK_NAME);
    }

    @Test
    public void dailyExportUsingScheduleExportTask(){
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseDailySchedule()
                .setActualTime()
                .closeTheWizard();
        schedulerServicePage = homePage.goToSchedulerServicePage(SCHEDULER_SERVICE_PAGE_URL);
        schedulerServicePage
                .findJobAndClickOnIT(TASK_NAME);
        Assert.assertEquals(schedulerServicePage.getTextOfJob(TASK_NAME), TASK_NAME);
    }

    @Test
    public void weeklyExportUsingScheduleExportTask(){
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseWeeklySchedule()
                .repeatEveryWeekOn(1)
                .setActualTime()
                .closeTheWizard();
        schedulerServicePage = homePage.goToSchedulerServicePage(SCHEDULER_SERVICE_PAGE_URL);
        schedulerServicePage
                .findJobAndClickOnIT(TASK_NAME);
        Assert.assertEquals(schedulerServicePage.getTextOfJob(TASK_NAME), TASK_NAME);
    }

    @Test
    public void monthlyExportUsingScheduleExportTask(){
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseMonthlySchedule()
                .repeatEveryDay("11")
                .setActualTime()
                .closeTheWizard();
        schedulerServicePage = homePage.goToSchedulerServicePage(SCHEDULER_SERVICE_PAGE_URL);
        schedulerServicePage
                .findJobAndClickOnIT(TASK_NAME);
        Assert.assertEquals(schedulerServicePage.getTextOfJob(TASK_NAME), TASK_NAME);
    }

    @Test
    public void yearlyExportUsingScheduleExportTask(){
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseYearlySchedule()
                .repeatEveryMonth("June")
                .repeatEveryDay("11")
                .setActualTime()
                .closeTheWizard();
        schedulerServicePage = homePage.goToSchedulerServicePage(SCHEDULER_SERVICE_PAGE_URL);
        schedulerServicePage
                .findJobAndClickOnIT(TASK_NAME);
        Assert.assertEquals(schedulerServicePage.getTextOfJob(TASK_NAME), TASK_NAME);
    }

    @AfterMethod
    public void deleteJobFromScheduleService(){
        schedulerServicePage
                .deleteSelectedJob()
                .selectDeletedJob(TASK_NAME)
                .permanentlyRemoveJob();
    }
}
