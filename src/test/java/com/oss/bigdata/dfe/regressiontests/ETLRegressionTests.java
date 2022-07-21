package com.oss.bigdata.dfe.regressiontests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.EtlDataCollectionsPage;

import io.qameta.allure.Description;

public class ETLRegressionTests extends BaseTestCase {

    private EtlDataCollectionsPage etlDataCollectionsPage;

    @BeforeMethod
    public void goToEtlDataCollectionsView() {
        etlDataCollectionsPage = EtlDataCollectionsPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Check ETL View", description = "ETL Data Collections View is opening")
    @Description("ETL Data Collections View is opening")
    public void checkETLView() {
        Assert.assertEquals(etlDataCollectionsPage.getViewTitle(), "ETL Data Collections");
    }

    @Parameters({"etlName"})
    @Test(priority = 2, testName = "Check ETL Format Tab", description = "Check ETL Format Tab")
    @Description("Check ETL Format Tab")
    public void checkETLFormatTab(
            @Optional("t:SMOKE#ETLforMonitoring") String etlName
    ) {
        boolean etlExists = etlDataCollectionsPage.etlProcessExistsIntoTable(etlName);
        if (etlExists) {
            etlDataCollectionsPage.selectFoundEtlProcess();
            etlDataCollectionsPage.selectFormatTab();
            Assert.assertFalse(etlDataCollectionsPage.isFormatTabTableEmpty());
        } else {
            Assert.fail("Cannot find ETL: " + etlName);
        }
    }

    @Parameters({"etlName"})
    @Test(priority = 3, testName = "Check ETL Details Tab", description = "Check ETL Details Tab")
    @Description("Check ETL Details Tab")
    public void checkETLDetailsTab(
            @Optional("t:SMOKE#ETLforMonitoring") String etlName
    ) {
        boolean etlExists = etlDataCollectionsPage.etlProcessExistsIntoTable(etlName);
        if (etlExists) {
            etlDataCollectionsPage.selectFoundEtlProcess();
            etlDataCollectionsPage.selectDetailsTab();
            Assert.assertEquals(etlDataCollectionsPage.checkNameInPropertyPanel(), etlName);
        } else {
            Assert.fail("Cannot find ETL: " + etlName);

        }
    }

    @Parameters({"etlName"})
    @Test(priority = 4, testName = "Check Measures Tab", description = "Check Measures Tab")
    @Description("Check Measures Tab")
    public void checkMeasuresTab(
            @Optional("t:SMOKE#ETLforMonitoring") String etlName
    ) {
        boolean etlExists = etlDataCollectionsPage.etlProcessExistsIntoTable(etlName);
        if (etlExists) {
            etlDataCollectionsPage.selectFoundEtlProcess();
            etlDataCollectionsPage.selectMeasuresTab();
            Assert.assertFalse(etlDataCollectionsPage.isMeasuresTabTableEmpty());
        } else {
            Assert.fail("Cannot find ETL: " + etlName);

        }
    }
}