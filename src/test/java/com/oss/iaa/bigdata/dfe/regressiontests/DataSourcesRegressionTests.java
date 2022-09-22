package com.oss.iaa.bigdata.dfe.regressiontests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.datasource.DataSourcePage;

import io.qameta.allure.Description;

public class DataSourcesRegressionTests extends BaseTestCase {

    private static final String DATA_SOURCE_VIEW_TITLE = "Data Sources";
    private static final String ACTIVE_YES = "Yes";
    private static final String DATABASE_FOR_DATA_SOURCE = "DFE Data-Model DB";
    private static final String CATEGORY_COLUMN = "Category";
    private static final String STATUS_COLUMN = "Status";
    private static final String DATA_FORMAT_COLUMN = "Data Format";
    private static final String WORKING_STATUS = "Working";
    private static final String ICON_SUCCESS = "SUCCESS";
    private DataSourcePage dataSourcePage;

    @BeforeMethod
    public void goToDataSourcePage() {
        dataSourcePage = DataSourcePage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check if data Source View is opened", description = "Check if data Source View is opened")
    @Description("Check if data Source View is opened")
    public void checkDataSourceView() {
        Assert.assertEquals(dataSourcePage.getViewTitle(), DATA_SOURCE_VIEW_TITLE);
    }

    @Parameters({"dataSourceName"})
    @Test(priority = 2, testName = "Check Details Tab", description = "Check if data in details tab is accurate")
    @Description("Check if data in details tab is accurate")
    public void checkDetails(
            @Optional("t:SMOKE#DSforKqis") String dataSourceName
    ) {
        boolean dataSourceExist = dataSourcePage.dataSourceExistIntoTable(dataSourceName);
        if (dataSourceExist) {
            dataSourcePage.selectFirstDataSourceInTable();
            dataSourcePage.selectDetailsTab();
            Assert.assertEquals(dataSourcePage.getIsActiveFromPropertyPanel(), ACTIVE_YES);
            Assert.assertEquals(dataSourcePage.getDatabaseFromPropertyPanel(), DATABASE_FOR_DATA_SOURCE);
        } else {
            Assert.fail("Cannot find Data Source " + dataSourceName);
        }
    }

    @Parameters({"categoryName"})
    @Test(priority = 3, testName = "Category search", description = "Check Category Search on Data Source View")
    @Description("Check Category Search on Data Source View")
    public void categorySearch(
            @Optional("Selenium Tests") String categoryName
    ) {
        dataSourcePage.setCategory(categoryName);
        dataSourcePage.selectFirstDataSourceInTable();
        Assert.assertEquals(dataSourcePage.getValueFromFirstRow(CATEGORY_COLUMN), categoryName);
    }

    @Test(priority = 3, testName = "Status search", description = "Check Status Search on Data Source View")
    @Description("Check Status Search on Data Source View")
    public void statusSearch() {
        dataSourcePage.setStatus(WORKING_STATUS);
        dataSourcePage.selectFirstDataSourceInTable();
        Assert.assertTrue(dataSourcePage.getValueFromFirstRow(STATUS_COLUMN).contains(ICON_SUCCESS));
    }

    @Parameters({"dataFormat"})
    @Test(priority = 4, testName = "Data Format search", description = "Check Data Format Search on Data Source View")
    @Description("Check Data Format Search on Data Source View")
    public void dataFormatSearch(
            @Optional("CSV File") String dataFormat
    ) {
        dataSourcePage.setDataFormat(dataFormat);
        dataSourcePage.selectFirstDataSourceInTable();
        Assert.assertEquals(dataSourcePage.getValueFromFirstRow(DATA_FORMAT_COLUMN), dataFormat);
    }

    @Test(priority = 5, testName = "Refresh Test", description = "Refresh Data Source table")
    @Description("Refresh Data Source Table")
    public void refreshTest() {
        Assert.assertFalse(dataSourcePage.isDataSourceTableEmpty());
        dataSourcePage.selectFirstDataSourceInTable();
        dataSourcePage.clickRefresh();
        Assert.assertFalse(dataSourcePage.isDataSourceTableEmpty(), "Data Source table is empty");
    }

    @Parameters({"dataSourceName"})
    @Test(priority = 6, testName = "Check Format Tab", description = "Check if Format Table is not empty")
    @Description("Check if Format Table is not empty")
    public void checkFormatTab(
            @Optional("t:SMOKE#DSforKqis") String dataSourceName
    ) {
        boolean dataSourceExist = dataSourcePage.dataSourceExistIntoTable(dataSourceName);
        if (dataSourceExist) {
            dataSourcePage.selectFirstDataSourceInTable();
            dataSourcePage.selectFormatTab();
            Assert.assertFalse(dataSourcePage.isFormatTableEmpty(), "Format Table is Empty");

        } else {
            Assert.fail("Cannot find Data Source " + dataSourceName);
        }
    }
}
