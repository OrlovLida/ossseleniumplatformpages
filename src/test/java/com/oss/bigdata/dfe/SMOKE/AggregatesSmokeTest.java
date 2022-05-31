package com.oss.bigdata.dfe.SMOKE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.AggregatePage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class AggregatesSmokeTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(AggregatesSmokeTest.class);
    private AggregatePage aggregatePage;
    private static final String AGGREGATE_NAME = "t:SMOKE#AGGRforMonitoring";


    @BeforeClass
    public void goToAggregateView() {
        aggregatePage = AggregatePage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check if Aggregate is working", description = "Check if Aggregate is working")
    @Description("Check if Aggregate is working")
    public void checkIfAggregateIsWorking() {
        boolean aggregateExists = aggregatePage.aggregateExistsIntoTable(AGGREGATE_NAME);
        if (aggregateExists) {
            aggregatePage.selectFoundAggregate();
            aggregatePage.selectExecutionHistoryTab();
            aggregatePage.clickRefreshInTabTable();

            boolean ifRunsExists = aggregatePage.ifRunsNotEmpty();
            Assert.assertTrue(ifRunsExists);

            boolean isIfRunsFresh = aggregatePage.isIfRunsFresh();
            Assert.assertTrue(isIfRunsFresh);

            Assert.assertEquals(aggregatePage.checkStatus(), "Success");
        } else {
            log.info("Cannot find existing aggregate {}", AGGREGATE_NAME);
            Assert.fail("Cannot find existing aggregate " + AGGREGATE_NAME);
        }
    }
}
