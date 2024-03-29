package com.oss.bpm;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.processmodels.ImportModelWizardPage;
import com.oss.pages.bpm.processmodels.ProcessModelsPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;

import static com.oss.bpm.BpmPhysicalDataCreator.nextMaxInt;

/**
 * @author Paweł Rother
 */

@Listeners({TestListener.class})
public class ImportExportModelTest extends BaseTestCase {
    private static final String SUCCESS_FILE_UPLOAD_MESSAGE = "Upload success";
    private static final String CANNOT_LOAD_FILE_EXCEPTION = "Cannot load file";
    private static final String FILE_NOT_DELETED_EXCEPTION = "File is not properly deleted";
    private static final String DOMAIN = "Inventory Processes";
    private static final String MODEL_NAME = "bpm_selenium_import_export_model";
    private static final String FILE_NAME = MODEL_NAME.replaceAll(" ", "+");
    private static final String BAR_EXTENSION = ".bar";
    private static final String ZIP_EXTENSION = ".zip";
    private static final String OTHER_GROUP_ID = "other";
    private static final String IMPORT_ID = "import";
    private static final String IMPORT_PATH = "bpm/bpm_selenium_import_export_model.bar";
    private static final String INVALID_UPLOAD_FILE_MESSAGE = "Invalid upload file status in import model wizard.";
    private static final String INVALID_IMPORT_WIZARD_VISIBILITY = "Invalid import model wizard visibility.";
    private static final String INVALID_DOWNLOADED_FILE_VISIBILITY = "Invalid downloaded '%s' file visibility.";
    private static final String INVALID_MODEL_VISIBILITY = "Invalid process model '%1$s (%2$s)' visibility.";
    private final String modelKeyword = "Selenium." + nextMaxInt();

    @BeforeClass
    public void openBrw() {
        ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
    }


    @Test(priority = 1, description = "Import model from BAR")
    @Description("Import model from BAR")
    public void importModel() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        processModelsPage.chooseDomain(DOMAIN);
        processModelsPage.callAction(OTHER_GROUP_ID, IMPORT_ID);
        ImportModelWizardPage importModelWizardPage = new ImportModelWizardPage(driver);
        try {
            URL resource = ImportExportModelTest.class.getClassLoader().getResource(IMPORT_PATH);
            String absolutePatch = Paths.get(Objects.requireNonNull(resource).toURI()).toFile().getAbsolutePath();
            importModelWizardPage.attachFile(absolutePatch);
            DelayUtils.sleep(1000);
            Assert.assertEquals(importModelWizardPage.getImportStatus(), SUCCESS_FILE_UPLOAD_MESSAGE, INVALID_UPLOAD_FILE_MESSAGE);
            importModelWizardPage.importButton();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertFalse(importModelWizardPage.isImportWizardVisible(), INVALID_IMPORT_WIZARD_VISIBILITY);
        } catch (URISyntaxException e) {
            throw new RuntimeException(CANNOT_LOAD_FILE_EXCEPTION, e);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.selectModelByName(MODEL_NAME).setKeywordForSelectedModel(modelKeyword);
    }

    @Test(priority = 2, description = "Export model to BAR", dependsOnMethods = {"importModel"})
    @Description("Export model to BAR")
    public void exportModelBar() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        processModelsPage.chooseDomain(DOMAIN).selectModel(MODEL_NAME, modelKeyword).exportSelectedModelAsBAR();
        //export bar
        try {
            Assert.assertTrue(processModelsPage.isFileDownloaded(FILE_NAME + BAR_EXTENSION),
                    String.format(INVALID_DOWNLOADED_FILE_VISIBILITY, FILE_NAME + BAR_EXTENSION));
        } catch (IOException e) {
            throw new RuntimeException(FILE_NOT_DELETED_EXCEPTION);
        }
    }

    @Test(priority = 3, description = "Export model to XML", dependsOnMethods = {"importModel"})
    @Description("Export model to XML")
    public void exportModelXml() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        processModelsPage.chooseDomain(DOMAIN).selectModel(MODEL_NAME, modelKeyword).exportSelectedModelAsXML();
        //export xml
        try {
            Assert.assertTrue(processModelsPage.isFileDownloaded(FILE_NAME + ZIP_EXTENSION),
                    String.format(INVALID_DOWNLOADED_FILE_VISIBILITY, FILE_NAME + ZIP_EXTENSION));
        } catch (IOException e) {
            throw new RuntimeException(FILE_NOT_DELETED_EXCEPTION);
        }
    }

    @Test(priority = 4, description = "Delete Model", dependsOnMethods = {"importModel"})
    @Description("Delete model")
    public void deleteModel() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        processModelsPage.chooseDomain(DOMAIN).selectModel(MODEL_NAME, modelKeyword).deleteSelectedModel();
        Assert.assertFalse(processModelsPage.isModelExists(MODEL_NAME, modelKeyword),
                String.format(INVALID_MODEL_VISIBILITY, MODEL_NAME, modelKeyword));
    }
}
