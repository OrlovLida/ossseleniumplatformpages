package com.oss.iaa.acd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.acd.settingsview.TimeSeriesViewPage;

import io.qameta.allure.Description;

public class TimeSeriesViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(TimeSeriesViewTest.class);

    private TimeSeriesViewPage timeSeriesViewPage;

    private final String timeSeriesViewSuffixUrl = "%s/#/view/acd/abgadSettings";
    private final String ADD_NEW_TIME_SERIES_BUTTON_ID = "timeSeriesButtons-3";
    private final String SOURCE_SYSTEM_COMBOBOX_ID = "sourceSystem";
    private final String SOURCE_SYSTEM_VALUE = "PM";
    private final String INDICATOR_NAME_ID = "indicatorIdentifiers";
    private final String INDICATOR_NAME_VALUE = "new_BSS 3G KPIs_Accessibility_T.RAB_HSDPA_DN";
    private final String TIME_SERIES_INDICATOR_VALUE = "PM/new_BSS 3G KPIs_Accessibility_T.RAB_HSDPA_DN";
    private final String BUSINESS_INDICATOR_TYPE_ID = "indicatorBusinessComboBoxId";
    private final String BUSINESS_INDICATOR_TYPE_VALUE = "HW";
    private final String MO_ID = "dimensionIdentifiers";
    private final String MO_VALUE = "Test Service Statistics ETL";
    private final String TIME_SERIES_MO_VALUE = "PM/Test Service Statistics ETL";
    private final String MO_DOMAIN_ID = "monitoredObjectDomainComboBoxId";
    private final String MO_DOMAIN_VALUE = "IP";
    private final String ADD_TIME_SERIES_WIZARD_ID = "addTimeSeries_prompt-card";
    private final String TREND_ANALYSIS_CHECKBOX_ID = "checkBoxTrendAnalysisId";
    private final String FORECAST_ANALYSIS_CHECKBOX_ID = "checkBoxForecastAnalysisId";
    private final String SOURCE_SYSTEM_MULTI_COMBOBOX_ID = "source_system";
    private final String INDICATORS_NAME_MULTI_SEARCHBOX_ID = "ABGAD_INDICATOR.identifier";
    private final String MO_NAME_MULTI_SEARCHBOX_ID = "ABGAD_MONITORED_OBJECT.identifier";
    private final String DELETE_BUTTON_ID = "ConfirmationBox_confirmationBox_action_button";
    private final String YES_LABEL = "yes";
    private final String DELETE_TIME_SERIES_BUTTON_ID = "timeSeriesButtons-1";

    public String timeSeriesId;

    @BeforeClass
    public void goToTimeSeriesView() {
        timeSeriesViewPage = TimeSeriesViewPage.goToPage(driver, timeSeriesViewSuffixUrl, BASIC_URL);
    }

    @Test(priority = 1, testName = "Add new Time Series", description = "Add new Time Series")
    @Description("Add new Time Series")
    public void addNewTimeSeries() {
        timeSeriesViewPage.addNewTimeSeries(ADD_NEW_TIME_SERIES_BUTTON_ID);
        timeSeriesViewPage.setAttributeValue(SOURCE_SYSTEM_COMBOBOX_ID, SOURCE_SYSTEM_VALUE);
        timeSeriesViewPage.setValueInTextField(INDICATOR_NAME_ID, INDICATOR_NAME_VALUE);
        timeSeriesViewPage.setAttributeValue(BUSINESS_INDICATOR_TYPE_ID, BUSINESS_INDICATOR_TYPE_VALUE);
        timeSeriesViewPage.setValueInTextField(MO_ID, MO_VALUE);
        timeSeriesViewPage.setAttributeValue(MO_DOMAIN_ID, MO_DOMAIN_VALUE);
        timeSeriesViewPage.selectRadioButton();
        timeSeriesViewPage.selectCheckbox(TREND_ANALYSIS_CHECKBOX_ID, ADD_TIME_SERIES_WIZARD_ID);
        timeSeriesViewPage.selectCheckbox(FORECAST_ANALYSIS_CHECKBOX_ID, ADD_TIME_SERIES_WIZARD_ID);
        log.info("Form has been completed. I try create time series");
        try {
            timeSeriesViewPage.clickButtonByLabel("Accept");
            log.info("I clicked Accept button");
        } catch (Exception e) {
            Assert.fail("I couldn't click Accept button\n" + e.getMessage());
        }
    }

    @Test(priority = 2, testName = "Verify if created Time Series exists", description = "Verify if created Time Series exists")
    @Description("Verify if created Time Series exists")
    public void searchingForCreatedTS() {

        if (!timeSeriesViewPage.isDataInTimeSeriesTable()) {
            log.error("TS table is empty");
            Assert.fail();
        }

        timeSeriesViewPage.setAttributeValue(SOURCE_SYSTEM_MULTI_COMBOBOX_ID, SOURCE_SYSTEM_VALUE);
        timeSeriesViewPage.setAttributeValue(INDICATORS_NAME_MULTI_SEARCHBOX_ID, TIME_SERIES_INDICATOR_VALUE);
        timeSeriesViewPage.setAttributeValue(MO_NAME_MULTI_SEARCHBOX_ID, TIME_SERIES_MO_VALUE);
        log.info("Time Series have been created with ID: {}", timeSeriesViewPage.getTimeSeriesId());
        timeSeriesId = timeSeriesViewPage.getTimeSeriesId();
        Assert.assertFalse(timeSeriesViewPage.getTimeSeriesId().isEmpty());
    }

    @Test(priority = 3, testName = "I delete created Time Series", description = "I delete created Time Series")
    @Description("I delete created TS")
    public void deleteCreatedTS() {
        
        if (!timeSeriesViewPage.getTimeSeriesId().equals(timeSeriesId)) {
            log.info("First row in the table doesn't contain object created in the 1st test");
            Assert.fail();
        }

        timeSeriesViewPage.selectFirstTSFromTable();
        timeSeriesViewPage.clickContextButton(DELETE_TIME_SERIES_BUTTON_ID);
        timeSeriesViewPage.confirmTimeSeriesDeletion(DELETE_BUTTON_ID);
        Assert.assertEquals(timeSeriesViewPage.getTimeSeriesStatus(), YES_LABEL);
    }
}