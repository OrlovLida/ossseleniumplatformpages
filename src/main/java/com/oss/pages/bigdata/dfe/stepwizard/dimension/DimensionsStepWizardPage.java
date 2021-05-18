package com.oss.pages.bigdata.dfe.stepwizard.dimension;

import com.oss.pages.bigdata.dfe.stepwizard.commons.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DimensionsStepWizardPage extends StepWizardPage {

    private final String DIMENSION_STEP_WIZARD_ID = "dimensionsWizardWindow";
    private final BasicInformationPage basicInformationStep;
    private final DataSourceAndProcessingPage dataSourceAndProcessingStep;
    private final TransformationsPage transformationsStep;
    private final DimensionColumnMappingPage columnMappingStep;

    public DimensionsStepWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

        basicInformationStep = new BasicInformationPage(driver, wait, getWizardId());
        dataSourceAndProcessingStep = new DataSourceAndProcessingPage(driver, wait, getWizardId());
        transformationsStep = new TransformationsPage(driver, wait);
        columnMappingStep = new DimensionColumnMappingPage(driver, wait, getWizardId());
    }

    public BasicInformationPage getBasicInformationStep() {
        return basicInformationStep;
    }

    public DataSourceAndProcessingPage getDataSourceAndProcessingStep() {
        return dataSourceAndProcessingStep;
    }

    public TransformationsPage getTransformationsStep() {
        return transformationsStep;
    }

    public DimensionColumnMappingPage getColumnMappingStep() {
        return columnMappingStep;
    }

    @Override
    public String getWizardId() {
        return DIMENSION_STEP_WIZARD_ID;
    }
}

