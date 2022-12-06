package com.oss.iaa.bigdata.dfe;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.bigdata.dfe.datasource.DataSourcePage;
import com.oss.pages.iaa.bigdata.dfe.datasource.dswizard.DataSourceStepWizardPage;
import com.oss.pages.iaa.bigdata.utils.ConstantsDfe;

import io.qameta.allure.Description;

public class DataSourceFromKafkaTest extends BaseTestCase {

    private DataSourcePage dataSourcePage;
    private String dataSourceName;
    private String dataSourceName_updated;
    private static final String TOPIC = "Selenium Test Topic";
    private static final String EVENT_TYPE = "Text Line";

    @BeforeClass
    public void goToDataSourceView() {
        dataSourcePage = DataSourcePage.goToPage(driver, BASIC_URL);
        dataSourceName = ConstantsDfe.createName() + "_DSFromKafkaTest";
        dataSourceName_updated = dataSourceName + "_updated";
    }

    @Test(priority = 1, testName = "Add new Data Source from Kafka", description = "Add new Data Source from Kafka")
    @Description("Add new Data Source from Kafka")
    public void addDataSourceFromKafka() {
        dataSourcePage.clickAddNewDS();
        dataSourcePage.selectDSFromKafka();
        DataSourceStepWizardPage dsStepWizard = new DataSourceStepWizardPage(driver, webDriverWait);
        dsStepWizard.getBasicInfoStep().fillBasicInformationStep(dataSourceName);
        dsStepWizard.clickNext();
        dsStepWizard.getSourceInfoStep().uploadCSVFile("DataSourceCSV/CPU_USAGE_INFO_RAW-MAP.xlsx");
        dsStepWizard.clickNext();
        dsStepWizard.getSpecificInfoStep().fillSpecificInfoForKafka(TOPIC, EVENT_TYPE);
        dsStepWizard.clickAccept();
        boolean dataSourceIsCreated = dataSourcePage.dataSourceExistIntoTable(dataSourceName);

        Assert.assertTrue(dataSourceIsCreated);
    }

    @Test(priority = 2, testName = "Edit Data Source from Kafka", description = "Edit Data Source from Kafka")
    @Description("Edit Data Source from Kafka")
    public void editDataSourceFromKafka() {
        boolean dataSourceExists = dataSourcePage.dataSourceExistIntoTable(dataSourceName);
        if (dataSourceExists) {
            dataSourcePage.selectFirstDataSourceInTable();
            dataSourcePage.clickEditDS();
            DataSourceStepWizardPage dsStepWizard = new DataSourceStepWizardPage(driver, webDriverWait);
            dsStepWizard.getBasicInfoStep().fillBasicInformationStep(dataSourceName_updated);
            dsStepWizard.clickNext();
            dsStepWizard.clickNext();
            dsStepWizard.clickAccept();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            boolean dataSourceIsCreated = dataSourcePage.dataSourceExistIntoTable(dataSourceName_updated);

            Assert.assertTrue(dataSourceIsCreated);
        } else {
            Assert.fail("Data Source with name: " + dataSourceName + " doesn't exist");
        }
    }

    @Test(priority = 3, testName = "Delete Data Source from Kafka", description = "Delete Data Source from Kafka")
    @Description("Delete Data Source from Kafka")
    public void deleteDsFromKafka() {
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
