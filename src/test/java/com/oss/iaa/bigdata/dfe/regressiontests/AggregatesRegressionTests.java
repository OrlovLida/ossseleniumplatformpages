package com.oss.iaa.bigdata.dfe.regressiontests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.AggregatePage;

import io.qameta.allure.Description;

public class AggregatesRegressionTests extends BaseTestCase {

    private static final String AGGREGATES_VIEW_TITLE = "Aggregates";
    private AggregatePage aggregatePage;

    @BeforeMethod
    public void goToAggregatesView() {
        aggregatePage = AggregatePage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check Aggregates View", description = "Check if Aggregates View is opened")
    @Description("Check if Aggregates View is opened")
    public void checkAggregatesView() {
        Assert.assertEquals(aggregatePage.getViewTitle(), AGGREGATES_VIEW_TITLE);
    }

    @Parameters({"aggregateName"})
    @Test(priority = 2, testName = "Check Details Tab", description = "Check Aggregate Details Tab")
    @Description("Check Aggregate Details Tab")
    public void checkDetailsTab(
            @Optional("t:SMOKE#AGGRforMonitoring") String aggregateName
    ) {
        boolean aggregateExists = aggregatePage.aggregateExistsIntoTable(aggregateName);
        if (aggregateExists) {
            aggregatePage.selectFirstAggregateInTable();
            aggregatePage.selectDetailsTab();
            Assert.assertEquals(aggregatePage.getNameFromPropertyPanel(), aggregateName);
        } else {
            Assert.fail("Cannot find Aggregate " + aggregateName);
        }
    }

    @Parameters({"aggregateName"})
    @Test(priority = 3, testName = "Check Configurations Tab", description = "Check Configurations Tab")
    @Description("Check Configurations Tab")
    public void checkConfigurationsTab(
            @Optional("t:SMOKE#AGGRforMonitoring") String aggregateName
    ) {
        boolean aggregateExists = aggregatePage.aggregateExistsIntoTable(aggregateName);
        if (aggregateExists) {
            aggregatePage.selectFirstAggregateInTable();
            aggregatePage.selectConfigurationsTab();
            Assert.assertFalse(aggregatePage.isConfigurationsTabTableEmpty());
        } else {
            Assert.fail("Cannot find Aggregate: " + aggregateName);
        }
    }

    @Parameters({"aggregateName"})
    @Test(priority = 4, testName = "Check Rebuild Status", description = "Check Rebuild Status")
    @Description("Check if Rebuild Status is valid")
    public void checkRebuildStatus(
            @Optional("t:SMOKE#AGGRforMonitoring") String aggregateName
    ) {
        boolean aggregateExists = aggregatePage.aggregateExistsIntoTable(aggregateName);
        if (aggregateExists) {
            aggregatePage.selectFirstAggregateInTable();
            aggregatePage.selectConfigurationsTab();
            Assert.assertTrue(aggregatePage.isRebuildStatusValid());
        } else {
            Assert.fail("Cannot find Aggregate: " + aggregateName);
        }
    }

    @Parameters({"aggregateName"})
    @Test(priority = 5, testName = "Check Measures Tab", description = "Check Measures Tab")
    @Description("Check Measures Tab")
    public void checkMeasuresTab(
            @Optional("t:SMOKE#AGGRforMonitoring") String aggregateName
    ) {
        boolean aggregateExists = aggregatePage.aggregateExistsIntoTable(aggregateName);
        if (aggregateExists) {
            aggregatePage.selectFirstAggregateInTable();
            aggregatePage.selectMeasuresTab();
            Assert.assertFalse(aggregatePage.isMeasuresTabTableEmpty());
        } else {
            Assert.fail("Cannot find Aggregate: " + aggregateName);
        }
    }
}
