package com.oss.iaa.bigdata.dfe.SMOKE;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.EtlDataCollectionsPage;

import io.qameta.allure.Description;

public class EtlSmokeTest extends BaseTestCase {

    private static final String STATUS_SUCCESS = "Success";
    private static final String ETL_NAME = "t:SMOKE#ETLforMonitoring";
    private EtlDataCollectionsPage etlDataCollectionsPage;

    @BeforeClass
    public void goToEtlDataCollectionsView() {
        etlDataCollectionsPage = EtlDataCollectionsPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check if ETL is working", description = "Check if ETL is working")
    @Description("Check if ETL is working")
    public void checkIfEtlIsWorking() {
        boolean etlExists = etlDataCollectionsPage.etlProcessExistsIntoTable(ETL_NAME);
        if (etlExists) {
            etlDataCollectionsPage.selectFirstEtlInTable();
            etlDataCollectionsPage.selectExecutionHistoryTab();
            etlDataCollectionsPage.clickRefreshInTabTable();

            boolean ifRunsFresh = etlDataCollectionsPage.isIfRunsFresh();
            Assert.assertTrue(ifRunsFresh);

            Assert.assertEquals(etlDataCollectionsPage.checkStatus(), STATUS_SUCCESS);
        } else {
            Assert.fail("Cannot find existing ETL " + ETL_NAME);
        }
    }
}
