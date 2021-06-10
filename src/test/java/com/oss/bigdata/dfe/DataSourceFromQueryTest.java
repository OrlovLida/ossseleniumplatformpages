package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.DataSource.DSWizard.DataSourceStepWizardPage;
import com.oss.pages.bigdata.dfe.DataSource.DataSourcePage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners({TestListener.class})
public class DataSourceFromQueryTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(DataSourceFromQueryTest.class);

    private DataSourcePage dataSourcePage;
    private String dataSourceName;
    private String updatedDataSourceName;

    private static final String DATABASE = "DFE Data-Model DB";
    private final String QUERY = "SELECT SYSDATE stime, CASE WHEN aa = 1 THEN 'D1_01' WHEN aa = 2 THEN 'D1_02' WHEN aa = 3 THEN 'D1_01' WHEN aa = 4 THEN 'D1_02' ELSE 'D1_01' END d1, CASE WHEN aa = 1 THEN 'D2_01' WHEN aa = 2 THEN 'D2_02' WHEN aa = 3 THEN 'D2_02' WHEN aa = 4 THEN 'D2_01' ELSE 'D2_01' END d2, CASE WHEN aa = 1 THEN 'D3_01_01_01' WHEN aa = 2 THEN 'D3_01_02_01' WHEN aa = 3 THEN 'D3_01_03_01' WHEN aa = 4 THEN 'D3_02_01_01' WHEN aa = 5 THEN 'D3_02_02_01' WHEN aa = 6 THEN 'D3_02_03_01' WHEN aa = 7 THEN 'D3_03_01_01' WHEN aa = 8 THEN 'D3_03_02_01' WHEN aa = 9 THEN 'D3_03_03_01' END d3, t.attempts, round(dbms_random.value(0, 100) * t.attempts / 100, 2) success FROM (SELECT level aa, round(dbms_random.value(0, 100), 2) attempts FROM dual CONNECT BY level < 10 ) t";
    private final String OFFSET = "No offset";
    private final String UNIT = "Minute";
    private final String INTERVAL_AMOUNT = "1";

    @BeforeClass
    public void goToDataSourceView() {
        dataSourcePage = DataSourcePage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        dataSourceName = "Selenium_" + date + "_DSTest";
        updatedDataSourceName = dataSourceName + "_updated";
    }

    @Test(priority = 1, testName = "Add new Data Source from query result", description = "Add new Data Source from query result")
    @Description("Add new Data Source from query result")
    public void addDataSourceFromQuery() {
        dataSourcePage.clickAddNewDS();
        dataSourcePage.selectDSFromQuery();
        DataSourceStepWizardPage dsStepWizard = new DataSourceStepWizardPage(driver, webDriverWait);
        dsStepWizard.getBasicInfoStep().fillBasicInformationStep(dataSourceName);
        dsStepWizard.clickNextStep();
        dsStepWizard.getSourceInfoStep().fillDatabase(DATABASE);
        dsStepWizard.getSourceInfoStep().fillQuery(QUERY);
        dsStepWizard.clickNextStep();
        dsStepWizard.getSpecificInfoStep().fillSpecificInfo(OFFSET, UNIT, INTERVAL_AMOUNT);
        dsStepWizard.clickAccept();
        Boolean dataSourceIsCreated = dataSourcePage.dataSourceExistIntoTable(dataSourceName);

        Assert.assertTrue(dataSourceIsCreated);
    }

    @Test(priority = 2, testName = "Edit Data Source", description = "Edit Data Source")
    @Description("Edit Data Source")
    public void editDataSourceFromQuery() {
        Boolean dataSourceExists = dataSourcePage.dataSourceExistIntoTable(dataSourceName);
        if (dataSourceExists) {
            dataSourcePage.selectFoundDataSource();
            dataSourcePage.clickEditDS();
            DataSourceStepWizardPage dsStepWizard = new DataSourceStepWizardPage(driver, webDriverWait);
            dsStepWizard.getBasicInfoStep().fillBasicInformationStep(updatedDataSourceName);
            dsStepWizard.clickNextStep();
            dsStepWizard.getSourceInfoStep();
            dsStepWizard.clickNextStep();
            dsStepWizard.getSpecificInfoStep();
            dsStepWizard.clickAccept();
            Boolean dataSourceIsEdited = dataSourcePage.dataSourceExistIntoTable(updatedDataSourceName);

            Assert.assertTrue(dataSourceIsEdited);
        } else {
            log.error("Data Source with name: {} doesn't exist", dataSourceName);
            Assert.fail();
        }
    }

    @Test(priority = 3, testName = "Delete Data Source", description = "Delete Data Source")
    @Description("Delete Data Source")
    public void deleteDataSourceFromQuery() {
        Boolean dataSourceExists = dataSourcePage.dataSourceExistIntoTable(dataSourceName);
        if (dataSourceExists) {
            dataSourcePage.selectFoundDataSource();
            dataSourcePage.clickDeleteDS();
            dataSourcePage.clickConfirmDelete();
            Boolean dataSourceIsDeleted = !dataSourcePage.dataSourceExistIntoTable(dataSourceName);

            Assert.assertTrue(dataSourceIsDeleted);
        } else {
            log.error("Data Source with name {} doesn't exist, cannot perform delete action", dataSourceName);
            Assert.fail();
        }
    }
}
