package com.oss.iaa.bigdata.dfe.SMOKE;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.datasource.DataSourcePage;

import io.qameta.allure.Description;

public class DataSourceSmokeTest extends BaseTestCase {

    private static final String SEVERITY_INFO = "Info";
    private static final String SEVERITY_ERROR = "Error";
    private static final String SEVERITY_WARNING = "Warn";
    private static final String SEVERITY_ALL = "All";
    private static final String DATA_SOURCE_NAME = "t:SMOKE#DSforKqis";
    private static final String FAIL_MESSAGE = String.format("Data Source with name: %s doesn't exist", DATA_SOURCE_NAME);
    private DataSourcePage dataSourcePage;

    @BeforeMethod
    public void goToDataSourceView() {
        dataSourcePage = DataSourcePage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check if Data Source is working", description = "Check if Data Source is working")
    @Description("Check if Data Source is working")
    public void CheckIfDataSourceIsWorking() {
        boolean dataSourceExists = dataSourcePage.dataSourceExistIntoTable(DATA_SOURCE_NAME);
        if (dataSourceExists) {
            dataSourcePage.selectFirstDataSourceInTable();
            dataSourcePage.selectLogsTab();
            dataSourcePage.refreshLogsTable();
            dataSourcePage.setValueInTimePeriodChooser(1, 2, 1);
            dataSourcePage.setSeverity(SEVERITY_ERROR);

            Assert.assertTrue(dataSourcePage.isLogsTableEmpty(), "In logs tab is at least one log with status Error");

            dataSourcePage.setSeverity(SEVERITY_ALL);
            Assert.assertTrue(dataSourcePage.isIfRunsFresh());

            String actualStatus = dataSourcePage.checkStatus();
            boolean statusIsAcceptable = actualStatus.equals(SEVERITY_INFO) || actualStatus.equals(SEVERITY_WARNING);
            Assert.assertTrue(statusIsAcceptable);
        } else {
            Assert.fail(FAIL_MESSAGE);
        }
    }

    @Test(priority = 2, testName = "Download processed file", description = "Download processed file and check if it is not empty")
    @Description("Download processed file and check if it is not empty")
    public void downloadProcessFile() {
        boolean dataSourceExists = dataSourcePage.dataSourceExistIntoTable(DATA_SOURCE_NAME);
        if (dataSourceExists) {
            dataSourcePage.selectFirstDataSourceInTable();
            dataSourcePage.selectProcessedFilesTab();
            dataSourcePage.selectFirstFileInTheTable();
            String fileName = dataSourcePage.getNameOfFirstFileInTheTable();
            dataSourcePage.clickDownloadFile();
            dataSourcePage.attachFileToReport(fileName);

            Assert.assertTrue(dataSourcePage.checkIfFileIsNotEmpty(fileName));
        } else {
            Assert.fail(FAIL_MESSAGE);
        }
    }

    @Test(priority = 3, testName = "checkShowFile", description = "Check option show file and check if table with data is displayed")
    @Description("Check option show file and check if table with data is displayed")
    public void checkShowFile() {
        boolean dataSourceExists = dataSourcePage.dataSourceExistIntoTable(DATA_SOURCE_NAME);
        if (dataSourceExists) {
            dataSourcePage.selectFirstDataSourceInTable();
            dataSourcePage.selectProcessedFilesTab();
            dataSourcePage.selectFirstFileInTheTable();
            dataSourcePage.showFile();

            Assert.assertFalse(dataSourcePage.isShowFileTableEmpty(), "Show file Table is empty!");
        } else {
            Assert.fail(FAIL_MESSAGE);
        }
    }
}
