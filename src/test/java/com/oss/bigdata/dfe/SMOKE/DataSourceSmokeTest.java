package com.oss.bigdata.dfe.SMOKE;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.DataSource.DataSourcePage;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DataSourceSmokeTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(DataSourceSmokeTest.class);
    private DataSourcePage dataSourcePage;
    private static final String DATA_SOURCE_NAME = "t:SMOKE#DSforMonitoring";

    @BeforeClass
    public void goToDataSourceView() {
        dataSourcePage = DataSourcePage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check if Data Source is working", description = "Check if Data Source is working")
    @Description("Check if Data Source is working")
    public void CheckIfDataSourceIsWorking() {
        boolean dataSourceExists = dataSourcePage.dataSourceExistIntoTable(DATA_SOURCE_NAME);
        if (dataSourceExists) {
            dataSourcePage.selectFoundDataSource();
            dataSourcePage.selectLogsTab();
            dataSourcePage.refreshLogsTable();
            dataSourcePage.setValueInTimePeriodChooser(1, 2, 1);
            dataSourcePage.setSeverityInCombobox("Error");

            Assert.assertTrue(dataSourcePage.isLogsTableEmpty());

            dataSourcePage.setSeverityInCombobox("All");

            Assert.assertTrue(dataSourcePage.IsIfRunsFresh());
            Assert.assertEquals(dataSourcePage.checkStatus(), "Info");
        } else {
            log.error("Data Source with name: {} doesn't exist", DATA_SOURCE_NAME);
            Assert.fail();
        }
    }
}