package com.oss.iaa.acd;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.acd.settingsview.TimeSeriesViewPage;
import com.oss.pages.iaa.acd.settingsview.TimeSeriesWizardPage;

import io.qameta.allure.Description;

public class TimeSeriesViewTest extends BaseTestCase {

    private TimeSeriesViewPage timeSeriesViewPage;

    private static final String SOURCE_SYSTEM_VALUE = "PM";
    private static final String BUSINESS_INDICATOR_TYPE_VALUE = "HW";
    private static final String YES_LABEL = "yes";
    private static final String INDICATOR_TYPE_RADIO_BUTTON_VALUE = "Any - All values";
    private static final String MO_DOMAIN_VALUE = "Transport";

    public String timeSeriesId;

    @BeforeMethod
    public void goToTimeSeriesView() {
        timeSeriesViewPage = TimeSeriesViewPage.goToPage(driver, BASIC_URL);
    }

    @Parameters({"indicatorName", "monitoredObject", "indicatorNameInTable", "monitoredObjectInTable"})
    @Test(priority = 1, testName = "Add new Time Series", description = "Add new Time Series")
    @Description("Add new Time Series")
    public void addNewTimeSeries(
            @Optional("pm group.jeden") String indicatorName,
            @Optional("Test") String monitoredObject,
            @Optional("PM/pm group.jeden") String indicatorNameInTable,
            @Optional("PM/Test") String monitoredObjectInTable
    ) {
        TimeSeriesWizardPage timeSeriesWizardPage = timeSeriesViewPage.addNewTimeSeries();
        timeSeriesWizardPage.setDataSource(SOURCE_SYSTEM_VALUE);
        timeSeriesWizardPage.setIndicatorName(indicatorName);
        timeSeriesWizardPage.setIndicatorBusinessType(BUSINESS_INDICATOR_TYPE_VALUE);
        timeSeriesWizardPage.setMoIdentifier(monitoredObject);
        timeSeriesWizardPage.setMoDomainIdentifier(MO_DOMAIN_VALUE);
        timeSeriesWizardPage.setIndicatorTechnicalType(INDICATOR_TYPE_RADIO_BUTTON_VALUE);
        timeSeriesWizardPage.selectTrendAnalysis();
        timeSeriesWizardPage.selectForecastAnalysis();
        timeSeriesWizardPage.clickAcceptButton();
        timeSeriesViewPage.turnOnShowDeletedSwitcher();
        timeSeriesViewPage.searchBySourceSystem(SOURCE_SYSTEM_VALUE);
        timeSeriesViewPage.searchByIndicatorName(indicatorNameInTable);
        timeSeriesViewPage.searchByMoName(monitoredObjectInTable);
        Assert.assertTrue(timeSeriesViewPage.isDataInTimeSeriesTable());
        timeSeriesId = timeSeriesViewPage.getFirstTimeSeriesId();
        Assert.assertFalse(timeSeriesId.isEmpty());
    }

    @Parameters({"indicatorNameInTable", "monitoredObjectInTable"})
    @Test(priority = 2, testName = "I delete created Time Series", description = "I delete created Time Series")
    @Description("I delete created Time Series")
    public void deleteCreatedTS(
            @Optional("PM/pm group.jeden") String indicatorNameInTable,
            @Optional("PM/Test") String monitoredObjectInTable
    ) {
        timeSeriesViewPage.turnOnShowDeletedSwitcher();
        timeSeriesViewPage.searchBySourceSystem(SOURCE_SYSTEM_VALUE);
        timeSeriesViewPage.searchByIndicatorName(indicatorNameInTable);
        timeSeriesViewPage.searchByMoName(monitoredObjectInTable);
        Assert.assertTrue(timeSeriesViewPage.getFirstTimeSeriesId().equals(timeSeriesId));
        timeSeriesViewPage.selectFirstTSFromTable();
        timeSeriesViewPage.deleteTimeSeries();
        timeSeriesViewPage.confirmTimeSeriesDeletion();
        Assert.assertEquals(timeSeriesViewPage.getFirstTimeSeriesStatus(), YES_LABEL);
    }
}