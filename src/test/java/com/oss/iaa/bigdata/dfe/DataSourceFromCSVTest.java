package com.oss.iaa.bigdata.dfe;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.datasource.DataSourcePage;
import com.oss.pages.iaa.bigdata.dfe.datasource.dswizard.DataSourceStepWizardPage;
import com.oss.pages.iaa.bigdata.utils.ConstantsDfe;

import io.qameta.allure.Description;

public class DataSourceFromCSVTest extends BaseTestCase {

    private DataSourcePage dataSourcePage;
    private String dataSourceName;
    private String dataSourceName_updated;
    private static final String DS_TYPE = "ServerGroup";
    private static final String SERVER_GROUP_NAME = "t:GENERAL#Test Server Group";
    private static final String INTERVAL = "60";

    @BeforeClass
    public void goToDataSourceView() {
        dataSourcePage = DataSourcePage.goToPage(driver, BASIC_URL);
        dataSourceName = ConstantsDfe.createName() + "_DSFromCSVTest";
        dataSourceName_updated = dataSourceName + "_updated";
    }

    @Test(priority = 1, testName = "Add new Data Source from CSV", description = "Add new Data Source from CSV")
    @Description("Add new Data Source from CSV")
    public void addDataSourceFromCSV() {
        dataSourcePage.clickAddNewDS();
        dataSourcePage.selectDSFromCSV();
        DataSourceStepWizardPage dsStepWizard = new DataSourceStepWizardPage(driver, webDriverWait);
        dsStepWizard.getBasicInfoStep().fillBasicInformationStep(dataSourceName);
        dsStepWizard.clickNext();
        dsStepWizard.getSourceInfoStep().uploadCSVFile("DataSourceCSV/CPU_USAGE_INFO_RAW-MAP.xlsx");
        dsStepWizard.getSourceInfoStep().selectDSType(DS_TYPE);
        dsStepWizard.clickNext();
        dsStepWizard.getSpecificInfoStep().fillSpecificInfoForCSV(SERVER_GROUP_NAME, INTERVAL);
        dsStepWizard.clickAccept();
        boolean dataSourceIsCreated = dataSourcePage.dataSourceExistIntoTable(dataSourceName);

        Assert.assertTrue(dataSourceIsCreated);
    }

    @Test(priority = 2, testName = "Edit Data Source from CSV", description = "Edit Data Source from CSV")
    @Description("Edit Data Source from CSV")
    public void editDSFromCSV() {
        boolean dataSourceExists = dataSourcePage.dataSourceExistIntoTable(dataSourceName);
        if (dataSourceExists) {
            dataSourcePage.selectFirstDataSourceInTable();
            dataSourcePage.clickEditDS();
            DataSourceStepWizardPage dsStepWizard = new DataSourceStepWizardPage(driver, webDriverWait);
            dsStepWizard.getBasicInfoStep().fillBasicInformationStep(dataSourceName_updated);
            dsStepWizard.clickNext();
            dsStepWizard.clickNext();
            dsStepWizard.clickAccept();
            boolean dataSourceIsCreated = dataSourcePage.dataSourceExistIntoTable(dataSourceName_updated);

            Assert.assertTrue(dataSourceIsCreated);
        } else {
            Assert.fail("Data Source with name: " + dataSourceName + " doesn't exist");
        }
    }

    @Test(priority = 3, testName = "Delete Data Source from CSV", description = "Delete Data Source from CSV")
    @Description("Delete Data Source from CSV")
    public void deleteDsFromCSV() {
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
