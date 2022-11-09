package com.oss.bpm;

import com.oss.BaseTestCase;
import com.oss.framework.components.mainheader.ToolbarWidget;
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

/**
 * @author Paweł Rother
 */

@Listeners({TestListener.class})
public class ImportExportModelTest extends BaseTestCase {
    private static final String BPM_USER_LOGIN = "bpm_webselenium";
    private static final String BPM_USER_PASSWORD = "Webtests123!";
    private static final String BPM_ADMIN_USER_LOGIN = "bpm_admin_webselenium";
    private static final String BPM_ADMIN_USER_PASSWORD = "Webtests123!";

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
    private final String modelKeyword = "Selenium " + (int) (Math.random() * 100001);

    @BeforeClass
    public void openBrw() {
        ProcessModelsPage processModelsPage = new ProcessModelsPage(driver);
        ToolbarWidget toolbarWidget = ToolbarWidget.create(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        if (!toolbarWidget.getUserName().equals(BPM_USER_LOGIN)) {
            processModelsPage.changeUser(BPM_USER_LOGIN, BPM_USER_PASSWORD);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }


    @Test(priority = 1, description = "Import model from BAR")
    @Description("Import model from BAR")
    public void importModel() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.chooseDomain(DOMAIN);
        processModelsPage.callAction(OTHER_GROUP_ID, IMPORT_ID);
        ImportModelWizardPage importModelWizardPage = new ImportModelWizardPage(driver);
        try {
            URL resource = ImportExportModelTest.class.getClassLoader().getResource(IMPORT_PATH);
            String absolutePatch = Paths.get(Objects.requireNonNull(resource).toURI()).toFile().getAbsolutePath();
            importModelWizardPage.attachFile(absolutePatch);
            DelayUtils.sleep(1000);
            Assert.assertEquals(importModelWizardPage.getImportStatus(), SUCCESS_FILE_UPLOAD_MESSAGE);
            importModelWizardPage.importButton();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            Assert.assertFalse(importModelWizardPage.isImportWizardVisible());
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
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.chooseDomain(DOMAIN).selectModel(MODEL_NAME, modelKeyword).exportSelectedModelAsBAR();
        //export bar
        try {
            Assert.assertTrue(processModelsPage.isFileDownloaded(FILE_NAME + BAR_EXTENSION));
        } catch (IOException e) {
            throw new RuntimeException(FILE_NOT_DELETED_EXCEPTION);
        }
    }

    @Test(priority = 3, description = "Export model to XML", dependsOnMethods = {"importModel"})
    @Description("Export model to XML")
    public void exportModelXml() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.chooseDomain(DOMAIN).selectModel(MODEL_NAME, modelKeyword).exportSelectedModelAsXML();
        //export xml
        try {
            Assert.assertTrue(processModelsPage.isFileDownloaded(FILE_NAME + ZIP_EXTENSION));
        } catch (IOException e) {
            throw new RuntimeException(FILE_NOT_DELETED_EXCEPTION);
        }
    }

    @Test(priority = 4, description = "Delete Model", dependsOnMethods = {"importModel"})
    @Description("Delete model")
    public void deleteModel() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.chooseDomain(DOMAIN).selectModel(MODEL_NAME, modelKeyword).deleteSelectedModel();
        Assert.assertFalse(processModelsPage.isModelExists(MODEL_NAME, modelKeyword));
    }
}
