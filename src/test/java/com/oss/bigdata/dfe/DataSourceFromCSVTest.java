package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.DataSource.DSWizard.DataSourceStepWizardPage;
import com.oss.pages.bigdata.dfe.DataSource.DataSourcePage;
import com.oss.pages.bigdata.utils.ConstantsDfe;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class DataSourceFromCSVTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(DataSourceFromCSVTest.class);
    private DataSourcePage dataSourcePage;
    private String dataSourceName;

    @BeforeClass
    public void goToDataSourceView() {
        dataSourcePage = DataSourcePage.goToPage(driver, BASIC_URL);

        dataSourceName = "Selenium_" + ConstantsDfe.createName() + "_DSFromCSVTest";
    }

    @Test(priority = 1, testName = "Add new Data Source from CSV", description = "Add new Data Source from CSV")
    @Description("Add new Data Source from CSV")
    public void addDataSourceFromCSV() {
        dataSourcePage.clickAddNewDS();
        dataSourcePage.selectDSFromCSV();
        DataSourceStepWizardPage dsStepWizard = new DataSourceStepWizardPage(driver, webDriverWait);
        dsStepWizard.getBasicInfoStep().fillBasicInformationStep(dataSourceName);
        dsStepWizard.clickNextStep();
        dsStepWizard.getSourceInfoStep().clickSelectFile();
    }

}
