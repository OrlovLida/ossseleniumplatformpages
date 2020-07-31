package com.oss;

import com.oss.pages.exportguiwizard.ExportGuiWizardPage;
import com.oss.pages.languageservice.LanguageServicePage;
import com.oss.pages.schedulerservice.SchedulerServicePage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import com.oss.utils.*;

@Listeners({TestListener.class})
public class ExportByScheduleTest extends BaseTestCase{

    private static final String LANGUAGE_SERVICE_PAGE_URL = String.format("%s/#/views/languagesservice/views/translations" +
            "?perspective=LIVE", BASIC_URL);
    private static  String TASK_NAME = "!Test_Export123";

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

    @Test(priority = 1)
    @Description("Single Export using schedule export task")
    public void singleExportUsingScheduleExportTask(){
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseSingleSchedule()
                .setActualDate()
                .setTime("2359")
                .closeTheWizard();
        schedulerServicePage=SchedulerServicePage.goToSchedulerServicePage(driver, BASIC_URL);
        schedulerServicePage
                .findJobAndClickOnIt(TASK_NAME);
        Assert.assertEquals(schedulerServicePage.getTextOfJob(TASK_NAME), TASK_NAME);
    }

    @Test(priority = 2)
    @Description("Daily Export using schedule export task")
    public void dailyExportUsingScheduleExportTask(){
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseDailySchedule()
                .setActualTime()
                .closeTheWizard();
        schedulerServicePage=SchedulerServicePage.goToSchedulerServicePage(driver, BASIC_URL);
        schedulerServicePage
                .findJobAndClickOnIt(TASK_NAME);
        Assert.assertEquals(schedulerServicePage.getTextOfJob(TASK_NAME), TASK_NAME);
    }

    @Test(priority = 3)
    @Description("Weekly Export Using Schedule Export Task")
    public void weeklyExportUsingScheduleExportTask(){
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseWeeklySchedule()
                .repeatEveryWeekOn(1)
                .setActualTime()
                .closeTheWizard();
        schedulerServicePage=SchedulerServicePage.goToSchedulerServicePage(driver, BASIC_URL);
        schedulerServicePage
                .findJobAndClickOnIt(TASK_NAME);
        Assert.assertEquals(schedulerServicePage.getTextOfJob(TASK_NAME), TASK_NAME);
    }

    @Test(priority = 4)
    @Description("Monthly Export Using Schedule Export Task")
    public void monthlyExportUsingScheduleExportTask(){
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseMonthlySchedule()
                .repeatEveryDayMonthly("1")
                .setActualTime()
                .closeTheWizard();
        schedulerServicePage=SchedulerServicePage.goToSchedulerServicePage(driver, BASIC_URL);
        schedulerServicePage
                .findJobAndClickOnIt(TASK_NAME);
        Assert.assertEquals(schedulerServicePage.getTextOfJob(TASK_NAME), TASK_NAME);
    }

    @Test(priority = 5)
    @Description("Yearly Export Using Schedule Export Task")
    public void yearlyExportUsingScheduleExportTask(){
        new ExportGuiWizardPage(driver)
                .chooseScheduleExport()
                .goToTheScheduleTask()
                .typeTaskName(TASK_NAME)
                .chooseYearlySchedule()
                .repeatEveryMonth("June")
                .repeatEveryDayYearly("1")
                .setActualTime()
                .closeTheWizard();
        schedulerServicePage=SchedulerServicePage.goToSchedulerServicePage(driver, BASIC_URL);
        schedulerServicePage
                .findJobAndClickOnIt(TASK_NAME);
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
