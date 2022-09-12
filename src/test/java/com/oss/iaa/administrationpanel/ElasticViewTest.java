package com.oss.iaa.administrationpanel;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.administration.administrationpanel.ElasticPage;

import io.qameta.allure.Description;

public class ElasticViewTest extends BaseTestCase {

    private ElasticPage elasticPage;

    @BeforeMethod
    public void goToAdminPanelElasticPage() {
        elasticPage = ElasticPage.goToElasticPage(driver);
    }

    @Parameters("clusterName")
    @Test(priority = 1, testName = "Check Cluster Name", description = "Check Cluster Name")
    @Description("Check Cluster Name")
    public void checkClusterName(
            @Optional("IAARC_25000") String clusterName
    ) {
        Assert.assertEquals(elasticPage.getValueFromPanel("Cluster Name"), clusterName);
    }

    @Test(priority = 2, testName = "Check Help button in Elastic Table", description = "Check Help button in Elastic Table")
    @Description("Check Help button in Elastic Table")
    public void checkHelpButton() {
        elasticPage.clickHelp();
        Assert.assertTrue(elasticPage.isTextInHelp());
        elasticPage.clickAccept();
        Assert.assertFalse(elasticPage.isElasticTableEmpty());
    }

    @Test(priority = 3, testName = "Check Refresh button in Elastic table", description = "Check Refresh button in Elastic table")
    @Description("Check Refresh button in Elastic table")
    public void checkRefreshElastic() {
        Assert.assertFalse(elasticPage.isElasticTableEmpty());
        elasticPage.clickRefreshInElasticTable();
        Assert.assertFalse(elasticPage.isElasticTableEmpty());
    }

    @Parameters("columnLabel")
    @Test(priority = 4, testName = "Change columns order", description = "Change columns order")
    @Description("Change columns order")
    public void changeColumnsOrder(
            @Optional("Size") String columnLabel
    ) {
        elasticPage.changeFirstColumnInElasticTable(columnLabel);
        Assert.assertEquals(elasticPage.getFirstColumnLabelInElasticTable(), columnLabel);
    }
}