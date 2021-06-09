package com.oss.pages.bigdata.dfe.stepwizard.etlprocess;

import com.oss.pages.bigdata.dfe.stepwizard.commons.ColumnMappingPage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.DataSourceAndProcessingPage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.StepWizardPage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.StoragePage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.TransformationsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EtlProcessStepWizardPage extends StepWizardPage {

    final private String ETL_PROCESS_STEP_WIZARD_ID = "etlWizardWindow";

    final private EtlProcessBasicInformationPage basicInformationStep;
    final private DataSourceAndProcessingPage dataSourceAndProcessingStep;
    final private TransformationsPage transformationsStep;
    final private EtlProcessColumnMappingPage columnMappingStep;
    final private StoragePage storageStep;

    public EtlProcessStepWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        basicInformationStep = new EtlProcessBasicInformationPage(driver, wait, getWizardId());
        dataSourceAndProcessingStep = new DataSourceAndProcessingPage(driver, wait, getWizardId());
        transformationsStep = new TransformationsPage(driver, wait);
        columnMappingStep = new EtlProcessColumnMappingPage(driver, wait, getWizardId());
        storageStep = new StoragePage(driver, wait, getWizardId());
    }

    @Override
    public String getWizardId() {
        return ETL_PROCESS_STEP_WIZARD_ID;
    }

    public EtlProcessBasicInformationPage getBasicInformationStep() {
        return basicInformationStep;
    }

    public DataSourceAndProcessingPage getDataSourceAndProcessingStep() {
        return dataSourceAndProcessingStep;
    }

    public TransformationsPage getTransformationsStep() {
        return transformationsStep;
    }

    public EtlProcessColumnMappingPage getColumnMappingStep() {
        return columnMappingStep;
    }

    public StoragePage getStorageStep() {
        return storageStep;
    }
}
