package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.DataSource.DSWizard.DataSourceStepWizardPage;
import com.oss.pages.bigdata.dfe.DataSource.DataSourcePage;
import com.oss.pages.bigdata.utils.ConstantsDfe;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class DataSourceFromCSVTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(DataSourceFromCSVTest.class);
    private DataSourcePage dataSourcePage;
    private String dataSourceName;
    private String dataSourceName_updated;
    private final String DS_TYPE = "ServerGroup";
    private final String SERVER_GROUP_NAME = "t:GENERAL#Test Server Group";
    private final String INTERVAL = "60";

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
        dsStepWizard.getSpecificInfoStep().fillSpecyficInfoForCSV(SERVER_GROUP_NAME, INTERVAL);
        dsStepWizard.clickAccept();
        Boolean dataSourceIsCreated = dataSourcePage.dataSourceExistIntoTable(dataSourceName);

        Assert.assertTrue(dataSourceIsCreated);
    }

    @Test(priority = 2, testName = "Edit Data Source from CSV", description = "Edit Data Source from CSV")
    @Description("Edit Data Source from CSV")
    public void editDSFromCSV() {
        Boolean dataSourceExists = dataSourcePage.dataSourceExistIntoTable(dataSourceName);
        if (dataSourceExists) {
            dataSourcePage.selectFoundDataSource();
            dataSourcePage.clickEditDS();
            DataSourceStepWizardPage dsStepWizard = new DataSourceStepWizardPage(driver, webDriverWait);
            dsStepWizard.getBasicInfoStep().fillBasicInformationStep(dataSourceName_updated);
            dsStepWizard.clickNext();
            dsStepWizard.clickNext();
            dsStepWizard.clickAccept();
            Boolean dataSourceIsCreated = dataSourcePage.dataSourceExistIntoTable(dataSourceName_updated);

            Assert.assertTrue(dataSourceIsCreated);
        } else {
            log.error("Data Source with name: {} doesn't exist", dataSourceName);
            Assert.fail();
        }
    }

    @Test(priority = 3, testName = "Delete Data Source from CSV", description = "Delete Data Source from CSV")
    @Description("Delete Data Source from CSV")
    public void deleteDsFromCSV() {
        Boolean dataSourceExists = dataSourcePage.dataSourceExistIntoTable(dataSourceName);
        if (dataSourceExists) {
            dataSourcePage.selectFoundDataSource();
            dataSourcePage.clickDeleteDS();
            dataSourcePage.clickConfirmDelete();
            boolean dataSourceIsDeleted = !dataSourcePage.dataSourceExistIntoTable(dataSourceName);

            Assert.assertTrue(dataSourceIsDeleted);
        } else {
            log.error("Data Source with name {} doesn't exist, cannot perform delete action", dataSourceName);
            Assert.fail();
        }
    }
}
