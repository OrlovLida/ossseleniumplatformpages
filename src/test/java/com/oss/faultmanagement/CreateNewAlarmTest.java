package com.oss.faultmanagement;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.faultmanagement.alarmgenerator.AlarmGeneratorPage;
import com.oss.pages.faultmanagement.alarmgenerator.AlarmGeneratorWizardPage;

import io.qameta.allure.Description;

public class CreateNewAlarmTest extends BaseTestCase {

    private AlarmGeneratorPage alarmGeneratorPage;
    private AlarmGeneratorWizardPage alarmGeneratorWizard;

    private final String date = new SimpleDateFormat("dd-MM-yyyy_HH:mm").format(new Date());
    private static final String EXPECTED_MESSAGE_TYPE = "success";

    @BeforeMethod
    public void goToAlarmGeneratorPage() {
        alarmGeneratorPage = AlarmGeneratorPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"moIdentifier", "notificationIdentifier"})
    @Test(priority = 1, testName = "Create new alarm manually and terminate it", description = "Create new alarm manually and terminate it")
    @Description("I verify if alarm can be created manually and then terminated")
    public void createNewAlarm(
            @Optional("Test_Selenium") String moIdentifier,
            @Optional("SeleniumTestAlarm:Device_F ") String notificationIdentifier
    ) {
        alarmGeneratorPage.clickAddButton();
        alarmGeneratorWizard = new AlarmGeneratorWizardPage(driver, webDriverWait);
        alarmGeneratorWizard.setMoIdentifier(moIdentifier);
        alarmGeneratorWizard.setNotificationIdentifier(notificationIdentifier + date);
        alarmGeneratorWizard.clickCreateButton();
        alarmGeneratorPage.selectFirstRowInTable();
        alarmGeneratorPage.clickGenerateAlarmButton();
        Assert.assertEquals(alarmGeneratorPage.getMessageTypeFromPrompt().toLowerCase(), EXPECTED_MESSAGE_TYPE);
        alarmGeneratorPage.clickTerminateAlarmButton();
        Assert.assertEquals(alarmGeneratorPage.getMessageTypeFromPrompt().toLowerCase(), EXPECTED_MESSAGE_TYPE);
    }
}