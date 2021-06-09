package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.DimensionsPage;
import com.oss.pages.bigdata.dfe.stepwizard.dimension.DimensionsStepWizardPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners({TestListener.class})
public class DimensionsViewTest extends BaseTestCase {

    private static final Logger log = LoggerFactory.getLogger(DimensionsViewTest.class);

    private static final String DATA_SOURCE_NAME = "Selenium_2020_09_29_DsQuery";
    private static final String TRANSFORMATION_TYPE_NAME = "SQL Transformation";
    private static final String COLUMN_NAME = "STIME";
    private static final String COLUMN_ROLE = "Primary Key";

    private DimensionsPage dimensionsPage;
    private String dimensionName;
    private String updatedDimensionName;

    @BeforeClass
    public void goToDimensionsView() {
        dimensionsPage = DimensionsPage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        dimensionName = "Selenium_" + date + "_DimTest";
        updatedDimensionName = dimensionName + "_updated";
    }

    @Test(priority = 1, testName = "Add new Dimension", description = "Add new Dimension")
    @Description("Add new Dimension")
    public void addDimension() {
        dimensionsPage.clickAddNewDimension();
        handleAddDimensionWizard();
        Boolean dimensionIsCreated = dimensionsPage.dimensionExistsIntoTable(dimensionName);

        if (!dimensionIsCreated) {
            log.info("Cannot find created dimension");
        }
        Assert.assertTrue(dimensionIsCreated);
    }

    private void handleAddDimensionWizard() {
        WebDriverWait wait = new WebDriverWait(driver, 45);
        DimensionsStepWizardPage dimensionStepWizard = new DimensionsStepWizardPage(driver, wait);
        dimensionStepWizard.getBasicInformationStep().fillBasicInformationStep(dimensionName);
        dimensionStepWizard.clickNextStep();
        dimensionStepWizard.getDataSourceAndProcessingStep().fillFeed(DATA_SOURCE_NAME);
        dimensionStepWizard.clickNextStep();
        dimensionStepWizard.getTransformationsStep().fillTransformationsStep(TRANSFORMATION_TYPE_NAME);
        dimensionStepWizard.clickNextStep();
        dimensionStepWizard.getColumnMappingStep().fillDimensionColumnMappingStep(COLUMN_NAME, COLUMN_ROLE);
        dimensionStepWizard.clickNextStep();
        dimensionStepWizard.clickAccept();
    }

    @Test(priority = 2, testName = "Edit Dimension", description = "Edit Dimension")
    @Description("Edit Dimension")
    public void editDimension() {
        Boolean dimensionExists = dimensionsPage.dimensionExistsIntoTable(dimensionName);
        if (dimensionExists) {
            dimensionsPage.selectFoundDimension();
            dimensionsPage.clickEditDimension();
            handleEditDimensionWizard();
            Boolean dimensionIsCreated = dimensionsPage.dimensionExistsIntoTable(updatedDimensionName);

            Assert.assertTrue(dimensionIsCreated);

        } else {
            log.error("Dimension with name: {} doesn't exist", updatedDimensionName);
            Assert.fail();
        }
    }

    private void handleEditDimensionWizard() {
        WebDriverWait wait = new WebDriverWait(driver, 65);
        DimensionsStepWizardPage dimensionStepWizard = new DimensionsStepWizardPage(driver, wait);
        dimensionStepWizard.getBasicInformationStep().fillName(updatedDimensionName);
        dimensionStepWizard.clickNextStep();
        dimensionStepWizard.clickNextStep();
        dimensionStepWizard.clickNextStep();
        dimensionStepWizard.clickNextStep();
        dimensionStepWizard.clickAccept();
    }

    @Test(priority = 3, testName = "Delete Dimension", description = "Delete Dimension")
    @Description("Delete Dimension")
    public void deleteDimension() {
        Boolean dimensionExists = dimensionsPage.dimensionExistsIntoTable(dimensionName);
        if (dimensionExists) {
            dimensionsPage.selectFoundDimension();
            dimensionsPage.clickDeleteDimension();
            dimensionsPage.confirmDelete();
            Boolean dimensionDeleted = !dimensionsPage.dimensionExistsIntoTable(dimensionName);

            Assert.assertTrue(dimensionDeleted);
        } else {
            log.error("Dimension with name: {} was not deleted", dimensionName);
            Assert.fail();
        }
    }
}
