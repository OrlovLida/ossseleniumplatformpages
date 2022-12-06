package com.oss.iaa.bigdata.dfe;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.datasource.DataSourcePage;
import com.oss.pages.iaa.bigdata.dfe.datasource.dswizard.DataSourceStepWizardPage;
import com.oss.pages.iaa.bigdata.utils.ConstantsDfe;

import io.qameta.allure.Description;

public class DataSourceFromQueryTest extends BaseTestCase {

    private DataSourcePage dataSourcePage;
    private String dataSourceName;
    private String updatedDataSourceName;

    private static final String DATABASE = "DFE Data-Model DB";
    private static final String QUERY = "SELECT SYSDATE stime, CASE WHEN aa = 1 THEN 'D1_01' WHEN aa = 2 THEN 'D1_02' WHEN aa = 3 THEN 'D1_01' WHEN aa = 4 THEN 'D1_02' ELSE 'D1_01' END d1, CASE WHEN aa = 1 THEN 'D2_01' WHEN aa = 2 THEN 'D2_02' WHEN aa = 3 THEN 'D2_02' WHEN aa = 4 THEN 'D2_01' ELSE 'D2_01' END d2, CASE WHEN aa = 1 THEN 'D3_01_01_01' WHEN aa = 2 THEN 'D3_01_02_01' WHEN aa = 3 THEN 'D3_01_03_01' WHEN aa = 4 THEN 'D3_02_01_01' WHEN aa = 5 THEN 'D3_02_02_01' WHEN aa = 6 THEN 'D3_02_03_01' WHEN aa = 7 THEN 'D3_03_01_01' WHEN aa = 8 THEN 'D3_03_02_01' WHEN aa = 9 THEN 'D3_03_03_01' END d3, t.attempts, round(dbms_random.value(0, 100) * t.attempts / 100, 2) success FROM (SELECT level aa, round(dbms_random.value(0, 100), 2) attempts FROM dual CONNECT BY level < 10 ) t";
    private static final String OFFSET = "No offset";
    private static final String UNIT = "Minute";
    private static final String INTERVAL_AMOUNT = "1";

    @BeforeClass
    public void goToDataSourceView() {
        dataSourcePage = DataSourcePage.goToPage(driver, BASIC_URL);

        dataSourceName = ConstantsDfe.createName() + "_DSTest";
        updatedDataSourceName = dataSourceName + "_updated";
    }

    @Test(priority = 1, testName = "Add new Data Source from query result", description = "Add new Data Source from query result")
    @Description("Add new Data Source from query result")
    public void addDataSourceFromQuery() {
        dataSourcePage.clickAddNewDS();
        dataSourcePage.selectDSFromQuery();
        DataSourceStepWizardPage dsStepWizard = new DataSourceStepWizardPage(driver, webDriverWait);
        dsStepWizard.getBasicInfoStep().fillBasicInformationStep(dataSourceName);
        dsStepWizard.clickNext();
        dsStepWizard.getSourceInfoStep().fillDatabase(DATABASE);
        dsStepWizard.getSourceInfoStep().fillQuery(QUERY);
        dsStepWizard.clickNext();
        dsStepWizard.getSpecificInfoStep().fillSpecificInfo(OFFSET, UNIT, INTERVAL_AMOUNT);
        dsStepWizard.clickAccept();
        boolean dataSourceIsCreated = dataSourcePage.dataSourceExistIntoTable(dataSourceName);

        Assert.assertTrue(dataSourceIsCreated);
    }

    @Test(priority = 2, testName = "Edit Data Source", description = "Edit Data Source")
    @Description("Edit Data Source")
    public void editDataSourceFromQuery() {
        boolean dataSourceExists = dataSourcePage.dataSourceExistIntoTable(dataSourceName);
        if (dataSourceExists) {
            dataSourcePage.selectFirstDataSourceInTable();
            dataSourcePage.clickEditDS();
            DataSourceStepWizardPage dsStepWizard = new DataSourceStepWizardPage(driver, webDriverWait);
            dsStepWizard.getBasicInfoStep().fillBasicInformationStep(updatedDataSourceName);
            dsStepWizard.clickNext();
            dsStepWizard.clickNext();
            dsStepWizard.clickAccept();
            boolean dataSourceIsEdited = dataSourcePage.dataSourceExistIntoTable(updatedDataSourceName);

            Assert.assertTrue(dataSourceIsEdited);
        } else {
            Assert.fail("Data Source with name: " + dataSourceName + " doesn't exist");
        }
    }

    @Test(priority = 3, testName = "Delete Data Source", description = "Delete Data Source")
    @Description("Delete Data Source")
    public void deleteDataSourceFromQuery() {
        boolean dataSourceExists = dataSourcePage.dataSourceExistIntoTable(dataSourceName);
        if (dataSourceExists) {
            dataSourcePage.selectFirstDataSourceInTable();
            dataSourcePage.clickDeleteDS();
            dataSourcePage.clickConfirmDelete();
            boolean dataSourceIsDeleted = !dataSourcePage.dataSourceExistIntoTable(dataSourceName);

            Assert.assertTrue(dataSourceIsDeleted);
        } else {
            Assert.fail("Data Source with name: " + dataSourceName + " doesn't exist, cannot perform delete action");
        }
    }
}
