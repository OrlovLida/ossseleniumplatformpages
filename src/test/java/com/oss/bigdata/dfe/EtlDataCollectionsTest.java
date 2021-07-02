package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.EtlDataCollectionsPage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.BasicInformationPage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.DataSourceAndProcessingPage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.StoragePage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.TransformationsPage;
import com.oss.pages.bigdata.dfe.stepwizard.etlprocess.EtlProcessColumnMappingPage;
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
public class EtlDataCollectionsTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(EtlDataCollectionsTest.class);

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
    public void goToEtlDataCollectionsView() {
        etlDataCollectionsPage = EtlDataCollectionsPage.goToPage(driver, BASIC_URL);

        etlProcessName = ConstantsDfe.createName() + "_EtlTest";
        updatedEtlProcessName = etlProcessName + "_updated";
        tableName = ConstantsDfe.createName();
    }

    @Test(priority = 1, testName = "Add new ETL Process", description = "Add new ETL Process")
    @Description("Add new ETL Process")
    public void addEtlProcess() {
        etlDataCollectionsPage.clickAddNewEtlProcess();

        BasicInformationPage etlBasicInfoWizard = new BasicInformationPage(driver, webDriverWait);
        etlBasicInfoWizard.fillName(etlProcessName);
        etlBasicInfoWizard.clickNextStep();

        DataSourceAndProcessingPage dsAndProcessingWizard = new DataSourceAndProcessingPage(driver, webDriverWait);
        dsAndProcessingWizard.fillFeed(DATASOURCE_NAME);
        dsAndProcessingWizard.clickNextStep();

        TransformationsPage transformationStep = new TransformationsPage(driver, webDriverWait);
        transformationStep.fillTransformationsStep(TRANSFORMATION_TYPE);
        transformationStep.clickNextStep();

        EtlProcessColumnMappingPage columnMappingStep = new EtlProcessColumnMappingPage(driver, webDriverWait);
        columnMappingStep.fillColumnMappingStep(COLUMN_MAPPING_HOST_NM_NAME, DIMENSION_COLUMN_ROLE, DEGENERATED_DIMENSION_TABLE);
        columnMappingStep.clickNextStep();

        StoragePage storageStep = new StoragePage(driver, webDriverWait);
        storageStep.fillStorageStep(tableName);
        storageStep.clickAccept();

        boolean etlProcessIsCreated = etlDataCollectionsPage.etlProcessExistsIntoTable(etlProcessName);
        if (!etlProcessIsCreated) {
            log.info("Cannot find created ETL configuration");
        }
        Assert.assertTrue(etlProcessIsCreated);
    }

    @Test(priority = 2, testName = "Edit ETL Process", description = "Edit ETL Process")
    @Description("Edit ETL Process")
    public void editEtlProcess() {
        Boolean etlProcessExists = etlDataCollectionsPage.etlProcessExistsIntoTable(etlProcessName);
        if (etlProcessExists) {
            etlDataCollectionsPage.selectFoundEtlProcess();
            etlDataCollectionsPage.clickEditEtlProcess();

            BasicInformationPage etlBasicInfoWizard = new BasicInformationPage(driver, webDriverWait);
            etlBasicInfoWizard.fillName(updatedEtlProcessName);
            etlBasicInfoWizard.clickNextStep();

            DataSourceAndProcessingPage dsAndProcessingWizard = new DataSourceAndProcessingPage(driver, webDriverWait);
            dsAndProcessingWizard.clickNextStep();

            TransformationsPage transformationStep = new TransformationsPage(driver, webDriverWait);
            transformationStep.clickNextStep();

            EtlProcessColumnMappingPage columnMappingStep = new EtlProcessColumnMappingPage(driver, webDriverWait);
            columnMappingStep.fillColumnMappingStep(COLUMN_MAPPING_HOST_NM_NAME, HOST_DIMENSION_TABLE);
            columnMappingStep.clickNextStep();

            StoragePage storageStep = new StoragePage(driver, webDriverWait);
            storageStep.clickAccept();

            boolean etlProcessIsEdited = etlDataCollectionsPage.etlProcessExistsIntoTable(updatedEtlProcessName);
            if (!etlProcessIsEdited) {
                log.info("Cannot find existing edited ETL {}", updatedEtlProcessName);
            }
            Assert.assertTrue(etlProcessIsEdited);
        } else {
            log.info("Cannot find existing ETL {}", etlProcessName);
            Assert.fail("Cannot find existing ETL " + etlProcessName);
        }
    }

    @Test(priority = 3, testName = "Delete ETL Process", description = "Delete ETL Process")
    @Description("Delete ETL Process")
    public void deleteEtlProcess() {
        boolean aggregateExists = etlDataCollectionsPage.etlProcessExistsIntoTable(updatedEtlProcessName);
        if (aggregateExists) {
            etlDataCollectionsPage.selectFoundEtlProcess();
            etlDataCollectionsPage.clickDeleteEtlProcess();
            etlDataCollectionsPage.confirmDelete();
            boolean aggregateDeleted = !etlDataCollectionsPage.etlProcessExistsIntoTable(updatedEtlProcessName);

            Assert.assertTrue(aggregateDeleted);
        } else {
            log.info("Cannot find existing edited ETL {}", updatedEtlProcessName);
            Assert.fail("Cannot find existing edited ETL " + updatedEtlProcessName);
        }
    }
}
