package com.oss.iaa.bigdata.dfe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.DimensionsPage;
import com.oss.pages.iaa.bigdata.dfe.stepwizard.commons.BasicInformationPage;
import com.oss.pages.iaa.bigdata.dfe.stepwizard.commons.DataSourceAndProcessingPage;
import com.oss.pages.iaa.bigdata.dfe.stepwizard.commons.StoragePage;
import com.oss.pages.iaa.bigdata.dfe.stepwizard.commons.TransformationsPage;
import com.oss.pages.iaa.bigdata.dfe.stepwizard.dimension.DimensionColumnMappingPage;
import com.oss.pages.iaa.bigdata.utils.ConstantsDfe;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class DimensionsViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(DimensionsViewTest.class);

    private static final String DATA_SOURCE_NAME = "t:CRUD#DSforDim";
    private static final String TRANSFORMATION_TYPE_NAME = "SQL Transformation";
    private static final String COLUMN_NAME = "HOST_NM";
    private static final String COLUMN_ROLE = "Primary Key";
    private static final String DIMENSION_WIZARD_ID = "dimensionsWizardWindow";

    private DimensionsPage dimensionsPage;
    private String dimensionName;
    private String updatedDimensionName;

    @BeforeClass
    public void goToDimensionsView() {
        dimensionsPage = DimensionsPage.goToPage(driver, BASIC_URL);

        dimensionName = ConstantsDfe.createName() + "_DimTest";
        updatedDimensionName = dimensionName + "_updated";
    }

    @Test(priority = 1, testName = "Add new Dimension", description = "Add new Dimension")
    @Description("Add new Dimension")
    public void addDimension() {
        dimensionsPage.clickAddNewDimension();
        handleAddDimensionWizard();
        boolean dimensionIsCreated = dimensionsPage.dimensionExistsIntoTable(dimensionName);

        if (!dimensionIsCreated) {
            log.info("Cannot find created dimension");
        }
        Assert.assertTrue(dimensionIsCreated);
    }

    @Test(priority = 2, testName = "Edit Dimension", description = "Edit Dimension")
    @Description("Edit Dimension")
    public void editDimension() {
        boolean dimensionExists = dimensionsPage.dimensionExistsIntoTable(dimensionName);
        if (dimensionExists) {
            dimensionsPage.selectFoundDimension();
            dimensionsPage.clickEditDimension();
            handleEditDimensionWizard();
            boolean dimensionIsCreated = dimensionsPage.dimensionExistsIntoTable(updatedDimensionName);

            Assert.assertTrue(dimensionIsCreated);

        } else {
            Assert.fail("Dimension with name: {} doesn't exist"+ updatedDimensionName);
        }
    }

    @Test(priority = 3, testName = "Delete Dimension", description = "Delete Dimension")
    @Description("Delete Dimension")
    public void deleteDimension() {
        boolean dimensionExists = dimensionsPage.dimensionExistsIntoTable(updatedDimensionName);
        if (dimensionExists) {
            dimensionsPage.selectFoundDimension();
            dimensionsPage.clickDeleteDimension();
            dimensionsPage.confirmDelete();
            boolean dimensionDeleted = !dimensionsPage.dimensionExistsIntoTable(updatedDimensionName);

            Assert.assertTrue(dimensionDeleted);
        } else {
            Assert.fail("Dimension with name: {} was not deleted"+ updatedDimensionName);
        }
    }

    private void handleAddDimensionWizard() {

        BasicInformationPage dimensionBasicInfoWizard = new BasicInformationPage(driver, webDriverWait, DIMENSION_WIZARD_ID);
        dimensionBasicInfoWizard.fillName(dimensionName);
        dimensionBasicInfoWizard.clickNextStep();

        DataSourceAndProcessingPage dimensionDsAndProcessingWizard = new DataSourceAndProcessingPage(driver, webDriverWait, DIMENSION_WIZARD_ID);
        dimensionDsAndProcessingWizard.fillFeed(DATA_SOURCE_NAME);
        dimensionDsAndProcessingWizard.clickNextStep();

        TransformationsPage dimTransformationWizard = new TransformationsPage(driver, webDriverWait, DIMENSION_WIZARD_ID);
        dimTransformationWizard.fillTransformationsStep(TRANSFORMATION_TYPE_NAME);
        dimTransformationWizard.clickNextStep();

        DimensionColumnMappingPage dimColMapWizard = new DimensionColumnMappingPage(driver, webDriverWait);
        dimColMapWizard.fillDimensionColumnMappingStep(COLUMN_NAME, COLUMN_ROLE);
        dimColMapWizard.clickNextStep();

        StoragePage dimStoragePage = new StoragePage(driver, webDriverWait, DIMENSION_WIZARD_ID);
        dimStoragePage.clickAccept();
    }

    private void handleEditDimensionWizard() {
        BasicInformationPage dimensionBasicInfoWizard = new BasicInformationPage(driver, webDriverWait, DIMENSION_WIZARD_ID);
        dimensionBasicInfoWizard.fillName(updatedDimensionName);
        dimensionBasicInfoWizard.clickNextStep();

        DataSourceAndProcessingPage dimensionDsAndProcessingWizard = new DataSourceAndProcessingPage(driver, webDriverWait, DIMENSION_WIZARD_ID);
        dimensionDsAndProcessingWizard.clickNextStep();

        TransformationsPage dimTransformationWizard = new TransformationsPage(driver, webDriverWait, DIMENSION_WIZARD_ID);
        dimTransformationWizard.clickNextStep();

        DimensionColumnMappingPage dimColMapWizard = new DimensionColumnMappingPage(driver, webDriverWait);
        dimColMapWizard.clickNextStep();

        StoragePage dimStorageWizard = new StoragePage(driver, webDriverWait, DIMENSION_WIZARD_ID);
        dimStorageWizard.clickAccept();
    }
}
