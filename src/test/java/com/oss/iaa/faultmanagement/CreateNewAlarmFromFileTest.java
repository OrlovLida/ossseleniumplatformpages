package com.oss.iaa.faultmanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.faultmanagement.alarmgenerator.AlarmGeneratorFromFileWizardPage;
import com.oss.pages.iaa.faultmanagement.alarmgenerator.AlarmGeneratorPage;

import io.qameta.allure.Description;

public class CreateNewAlarmFromFileTest extends BaseTestCase {

    private AlarmGeneratorPage alarmGeneratorPage;
    private AlarmGeneratorFromFileWizardPage alarmGeneratorFromFileWizard;

    private static final String EXPECTED_MESSAGE_TYPE = "success";
    private static final String ALARMS_FILE_PATH = "AlarmGeneratorXML/Selenium_AG_test.xml";
    private static final String FILE_FORMAT_TYPE = "xml";

    @BeforeClass
    public void goToAlarmGeneratorPage() {
        alarmGeneratorPage = AlarmGeneratorPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Create new alarm from imported file", description = "Create new alarm from imported file")
    @Description("Create new alarm from imported file")
    public void createNewAlarmFromFile(
    ) {
        alarmGeneratorFromFileWizard = alarmGeneratorPage.clickImportButton();
        alarmGeneratorFromFileWizard.uploadAlarmXmlFile(ALARMS_FILE_PATH);
        alarmGeneratorFromFileWizard.setFileFormatType(FILE_FORMAT_TYPE);
        alarmGeneratorFromFileWizard.clickAcceptButton();
    }

    @Test(priority = 2, testName = "Generate created alarm", description = "Generate created alarm from file")
    @Description("Generate created alarm from file")
    public void generateCreatedAlarmFromFile(
    ) {
        alarmGeneratorPage.selectFirstRowInTable();
        alarmGeneratorPage.clickGenerateAlarmButton();
        Assert.assertEquals(alarmGeneratorPage.getMessageTypeFromPrompt().toLowerCase(), EXPECTED_MESSAGE_TYPE);
    }

    @Test(priority = 3, testName = "Terminate created alarm", description = "Terminate created alarm from file")
    @Description("Terminate created alarm from file")
    public void terminateCreatedAlarmFromFile(
    ) {
        alarmGeneratorPage.clickTerminateAlarmButton();
        Assert.assertEquals(alarmGeneratorPage.getMessageTypeFromPrompt().toLowerCase(), EXPECTED_MESSAGE_TYPE);
    }

    @Test(priority = 4, testName = "Remove created alarm", description = "Remove created alarm from file")
    @Description("Remove created alarm from file")
    public void removeCreatedAlarmFromFile(
    ) {
        alarmGeneratorPage.clickRemoveButton();
        Assert.assertTrue(alarmGeneratorPage.isAlarmTableEmpty());
    }
}