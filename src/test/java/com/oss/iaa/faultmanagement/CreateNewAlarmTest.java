package com.oss.iaa.faultmanagement;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.faultmanagement.alarmgenerator.AlarmGeneratorPage;
import com.oss.pages.iaa.faultmanagement.alarmgenerator.AlarmGeneratorWizardPage;

import io.qameta.allure.Description;

public class CreateNewAlarmTest extends BaseTestCase {

    private AlarmGeneratorPage alarmGeneratorPage;
    private AlarmGeneratorWizardPage alarmGeneratorWizard;

    private final String date = new SimpleDateFormat("dd-MM-yyyy_HH:mm").format(new Date());
    private static final String EXPECTED_MESSAGE_TYPE = "success";
    private static final String NOTIFICATION_COLUMN_ID = "notificationIdentifier";
    private static final String EDITED_SUFFIX = "_edited_";
    private static final String ACKNOWLEDGE_COLUMN_ID = "acknowledge";
    private static final String ACKNOWLEDGE_COLUMN_HEADER = "Acknowledge State";
    private static final String ADAPTER_NAME_COLUMN_ID = "adapterName";

    @BeforeClass
    public void goToAlarmGeneratorPage() {
        alarmGeneratorPage = AlarmGeneratorPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Enable and disable column", description = "Enable and disable column")
    @Description("Enable and disable column")
    public void enableDisableColumnToAlarmTable(
    ) {
        alarmGeneratorPage.enableColumnInTheTable(ACKNOWLEDGE_COLUMN_ID);
        Assert.assertTrue(alarmGeneratorPage.isColumnInTable(ACKNOWLEDGE_COLUMN_HEADER));
        alarmGeneratorPage.disableColumnInTheTable(ACKNOWLEDGE_COLUMN_ID);
        Assert.assertFalse(alarmGeneratorPage.isColumnInTable(ACKNOWLEDGE_COLUMN_HEADER));
    }

    @Parameters({"moIdentifier", "notificationIdentifier"})
    @Test(priority = 2, testName = "Create new alarm manually", description = "Create new alarm manually and check its visibility in the alarm table")
    @Description("Create new alarm manually and check its visibility in the alarm table")
    public void createNewAlarm(
            @Optional("Test_Selenium") String moIdentifier,
            @Optional("SeleniumTestAlarm:Device_F ") String notificationIdentifier
    ) {
        alarmGeneratorWizard = alarmGeneratorPage.clickAddButton();
        alarmGeneratorWizard.setMoIdentifier(moIdentifier);
        String uniqueNotificationIdentifier = notificationIdentifier + date;
        alarmGeneratorWizard.setNotificationIdentifier(uniqueNotificationIdentifier);
        alarmGeneratorWizard.clickCreateButton();
        Assert.assertEquals(alarmGeneratorPage.getFirstCellValueInColumn(NOTIFICATION_COLUMN_ID), uniqueNotificationIdentifier);
    }

    @Parameters({"notificationIdentifier"})
    @Test(priority = 3, testName = "Edit created alarm", description = "Edit created alarm and check its visibility in the alarm table")
    @Description("Edit created alarm and check its visibility in the alarm table")
    public void editCreatedAlarm(
            @Optional("SeleniumTestAlarm:Device_F ") String notificationIdentifier
    ) {
        String editedUniqueNotificationIdentifier = notificationIdentifier + EDITED_SUFFIX + date;
        alarmGeneratorPage.selectFirstRowInTable();
        alarmGeneratorWizard = alarmGeneratorPage.clickEditButton();
        alarmGeneratorWizard.setNotificationIdentifier(editedUniqueNotificationIdentifier);
        alarmGeneratorWizard.clickCreateButton();
        Assert.assertEquals(alarmGeneratorPage.getFirstCellValueInColumn(NOTIFICATION_COLUMN_ID), editedUniqueNotificationIdentifier);
    }

    @Parameters({"adapterName"})
    @Test(priority = 4, testName = "Add Adapter Name", description = "Add adapter name column and set it's value")
    @Description("Add adapter name column and set it's value")
    public void addAdapterName(
            @Optional("SeleniumAdapter") String adapterName
    ) {
        alarmGeneratorPage.enableColumnInTheTable(ADAPTER_NAME_COLUMN_ID);
        alarmGeneratorPage.selectFirstRowInTable();
        alarmGeneratorWizard = alarmGeneratorPage.clickEditButton();
        alarmGeneratorWizard.setAdapterName(adapterName);
        alarmGeneratorWizard.clickCreateButton();
        Assert.assertEquals(alarmGeneratorPage.getFirstCellValueInColumn(ADAPTER_NAME_COLUMN_ID), adapterName);
    }

    @Test(priority = 5, testName = "Generate created alarm", description = "Generate created alarm")
    @Description("Generate created alarm")
    public void generateCreatedAlarm(
    ) {
        alarmGeneratorPage.clickGenerateAlarmButton();
        Assert.assertEquals(alarmGeneratorPage.getMessageTypeFromPrompt().toLowerCase(), EXPECTED_MESSAGE_TYPE);
    }

    @Test(priority = 6, testName = "Terminate created alarm", description = "Terminate created alarm")
    @Description("Terminate created alarm")
    public void terminateCreatedAlarm(
    ) {
        alarmGeneratorPage.clickTerminateAlarmButton();
        Assert.assertEquals(alarmGeneratorPage.getMessageTypeFromPrompt().toLowerCase(), EXPECTED_MESSAGE_TYPE);
    }

    @Test(priority = 7, testName = "Remove created alarm", description = "Remove created alarm")
    @Description("Remove created alarm")
    public void removeCreatedAlarm(
    ) {
        alarmGeneratorPage.clickRemoveButton();
        Assert.assertTrue(alarmGeneratorPage.isAlarmTableEmpty());
    }
}