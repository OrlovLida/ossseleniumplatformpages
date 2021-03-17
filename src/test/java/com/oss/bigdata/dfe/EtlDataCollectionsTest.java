package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.EtlDataCollectionsPage;
import com.oss.pages.bigdata.dfe.stepwizard.etlprocess.EtlProcessStepWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners({TestListener.class})
public class EtlDataCollectionsTest extends BaseTestCase {

    private EtlDataCollectionsPage etlDataCollectionsPage;
    private String etlProcessName;
    private String updatedEtlProcessName;
    private String tableName;

    private final static String DATASOURCE_NAME = "t:CRUD#DSforEtl";
    private final static String TRANSFORMATION_TYPE = "SQL Transformation";
    private final static String COLUMN_MAPPING_HOST_NM_NAME = "HOST_NM";
    private final static String DIMENSION_COLUMN_ROLE = "Dimension";
    private final static String DEGENERATED_DIMENSION_TABLE = "Degenerated";
    private final static String HOST_DIMENSION_TABLE = "t:SMOKE#D_HOST";

    @BeforeClass
    public void goToEtlDataCollectionsView(){
        etlDataCollectionsPage = EtlDataCollectionsPage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        etlProcessName = "Selenium_" + date + "_EtlTest";
        updatedEtlProcessName = etlProcessName + "_updated";
        tableName = "selenium_" + date;
    }

    @Test(priority = 1)
    @Description("Add new ETL Process")
    public void addEtlProcess(){
        etlDataCollectionsPage.clickAddNewEtlProcess();
        WebDriverWait wait = new WebDriverWait(driver, 45);

        EtlProcessStepWizardPage etlProcessStepWizard = new EtlProcessStepWizardPage(driver, wait);

        etlProcessStepWizard.getBasicInformationStep().fillBasicInformationStep(etlProcessName);
        etlProcessStepWizard.clickNextStep();
        etlProcessStepWizard.getDataSourceAndProcessingStep().fillFeed(DATASOURCE_NAME);
        etlProcessStepWizard.clickNextStep();
        etlProcessStepWizard.getTransformationsStep().fillTransformationsStep(TRANSFORMATION_TYPE);
        etlProcessStepWizard.clickNextStep();

        etlProcessStepWizard.getColumnMappingStep().fillColumnMappingStep(COLUMN_MAPPING_HOST_NM_NAME, DIMENSION_COLUMN_ROLE, DEGENERATED_DIMENSION_TABLE);
        etlProcessStepWizard.clickNextStep();
        etlProcessStepWizard.getStorageStep().fillStorageStep(tableName);
        etlProcessStepWizard.clickAccept();
        Boolean etlProcessIsCreated = etlDataCollectionsPage.etlProcessExistsIntoTable(etlProcessName);
        Assert.assertTrue(etlProcessIsCreated);

    }

    @Test(priority = 2)
    @Description("Edit ETL Process")
    public void editEtlProcess(){
        Boolean etlProcessExists = etlDataCollectionsPage.etlProcessExistsIntoTable(etlProcessName);
        if(etlProcessExists){
            etlDataCollectionsPage.selectFoundEtlProcess();
            etlDataCollectionsPage.clickEditEtlProcess();

            WebDriverWait wait = new WebDriverWait(driver, 45);
            EtlProcessStepWizardPage etlProcessStepWizard = new EtlProcessStepWizardPage(driver, wait);
            etlProcessStepWizard.getBasicInformationStep().fillName(updatedEtlProcessName);
            etlProcessStepWizard.clickNextStep();
            etlProcessStepWizard.clickNextStep();
            etlProcessStepWizard.clickNextStep();
            etlProcessStepWizard.getColumnMappingStep().fillColumnMappingStep(COLUMN_MAPPING_HOST_NM_NAME, HOST_DIMENSION_TABLE);
            etlProcessStepWizard.clickNextStep();
            etlProcessStepWizard.clickAccept();

            Boolean etlProcessIsEdited = etlDataCollectionsPage.etlProcessExistsIntoTable(updatedEtlProcessName);
            Assert.assertTrue(etlProcessIsEdited);
        } else {
            Assert.fail();
        }
    }

    @Test(priority = 3)
    @Description("Delete ETL Process")
    public void deleteEtlProcess(){
        Boolean aggregateExists = etlDataCollectionsPage.etlProcessExistsIntoTable(updatedEtlProcessName);
        if(aggregateExists){
            etlDataCollectionsPage.selectFoundEtlProcess();
            etlDataCollectionsPage.clickDeleteEtlProcess();
            etlDataCollectionsPage.confirmDelete();
            Boolean aggregateDeleted = !etlDataCollectionsPage.etlProcessExistsIntoTable(updatedEtlProcessName);

            Assert.assertTrue(aggregateDeleted);
        } else {
            Assert.fail();
        }
    }

}
