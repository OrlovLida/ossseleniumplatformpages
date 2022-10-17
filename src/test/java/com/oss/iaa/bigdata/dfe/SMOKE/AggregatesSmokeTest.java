package com.oss.iaa.bigdata.dfe.SMOKE;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.AggregatePage;

import io.qameta.allure.Description;

public class AggregatesSmokeTest extends BaseTestCase {

    private static final String STATUS_SUCCESS = "Success";
    private static final String AGGREGATE_NAME = "t:SMOKE#AGGRForKqis";
    private AggregatePage aggregatePage;

    @BeforeClass
    public void goToAggregateView() {
        aggregatePage = AggregatePage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check if Aggregate is working", description = "Check if Aggregate is working")
    @Description("Check if Aggregate is working")
    public void checkIfAggregateIsWorking() {
        boolean aggregateExists = aggregatePage.aggregateExistsIntoTable(AGGREGATE_NAME);
        if (aggregateExists) {
            aggregatePage.selectFirstAggregateInTable();
            aggregatePage.selectExecutionHistoryTab();
            aggregatePage.clickRefreshInTabTable();

            boolean ifRunsExists = aggregatePage.ifRunsNotEmpty();
            Assert.assertTrue(ifRunsExists);

            boolean isIfRunsFresh = aggregatePage.isIfRunsFresh();
            Assert.assertTrue(isIfRunsFresh);

            Assert.assertEquals(aggregatePage.checkStatus(), STATUS_SUCCESS);
        } else {
            Assert.fail("Cannot find existing aggregate " + AGGREGATE_NAME);
        }
    }
}
