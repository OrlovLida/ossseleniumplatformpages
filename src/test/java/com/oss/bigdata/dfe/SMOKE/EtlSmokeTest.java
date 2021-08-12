package com.oss.bigdata.dfe.SMOKE;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.EtlDataCollectionsPage;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class EtlSmokeTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(EtlSmokeTest.class);
    private EtlDataCollectionsPage etlDataCollectionsPage;
    private static final String ETL_NAME = "t:SMOKE#ETLforMonitoring";

    @BeforeClass
    public void goToEtlDataCollectionsView() {
        etlDataCollectionsPage = EtlDataCollectionsPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check if ETL is working", description = "Check if ETL is working")
    @Description("Check if ETL is working")
    public void checkIfEtlIsWorking() {
        boolean etlExists = etlDataCollectionsPage.etlProcessExistsIntoTable(ETL_NAME);
        if (etlExists) {
            etlDataCollectionsPage.selectFoundEtlProcess();
            etlDataCollectionsPage.selectExecutionHistoryTab();
            etlDataCollectionsPage.clickRefreshInTabTable();

            boolean ifRunsFresh = etlDataCollectionsPage.IsIfRunsFresh();
            Assert.assertTrue(ifRunsFresh);

            Assert.assertEquals(etlDataCollectionsPage.checkStatus(), "Success");
        } else {
            log.info("Cannot find existing ETL {}", ETL_NAME);
            Assert.fail("Cannot find existing ETL " + ETL_NAME);
        }
    }
}
