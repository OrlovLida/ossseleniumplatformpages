package com.oss.bpm;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bpm.processmodels.ImportModelWizardPage;
import com.oss.pages.bpm.processmodels.ProcessModelsPage;
import com.oss.utils.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
/**
 * @author Paweł Rother
 */

@Listeners({TestListener.class})
public class ImportExportModelTest extends BaseTestCase {

    private String BPM_USER_LOGIN = "bpm_webselenium";
    private String BPM_USER_PASSWORD = "bpmweb";
    private String BPM_ADMIN_USER_LOGIN = "bpm_admin_webselenium";
    private String BPM_ADMIN_USER_PASSWORD = "bpmweb";

    private static final String DOMAIN = "Inventory Processes";
    private static final String MODEL_NAME = "bpm_selenium_test_process";
    private static final String FILE_NAME = MODEL_NAME.replaceAll(" ", "+");
    private static final String OTHER_GROUP_ID = "other";
    private static final String IMPORT_ID = "import";
    private static final String EXPORT_PATH_LOCAL = "C:\\Projects\\ossseleniumplatformpages\\target\\downloadFiles";  //działa tylko lokalnie
    private static final String IMPORT_PATH = "bpm/bpm_selenium_test_process.bar";

    @BeforeClass
    public void openBrw() {
        ProcessModelsPage processModelsPage = new ProcessModelsPage(driver);
        //processModelsPage.changeUser(BPM_USER_LOGIN,BPM_USER_PASSWORD);
    }


    @Test(priority = 1)
    public void importModel() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.chooseDomain(DOMAIN);
        processModelsPage.callAction(OTHER_GROUP_ID,IMPORT_ID);
        ImportModelWizardPage importModelWizardPage = new ImportModelWizardPage(driver);
        try {
            URL resource = ImportExportModelTest.class.getClassLoader().getResource(IMPORT_PATH);
            String absolutePatch = Paths.get(resource.toURI()).toFile().getAbsolutePath();
            importModelWizardPage.attachFile(absolutePatch);
            DelayUtils.sleep(1000);
            Assert.assertEquals("Upload success",importModelWizardPage.getImportStatus());
            importModelWizardPage.importButton();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot load file", e);
        }
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    public void exportModel() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.chooseDomain(DOMAIN);

        //export bar
        processModelsPage.exportModelAsBAR(MODEL_NAME);
        Assert.assertTrue(processModelsPage.isFileDownloaded(EXPORT_PATH_LOCAL, FILE_NAME));

        //export xml
        processModelsPage.exportModelAsXML(MODEL_NAME);
        Assert.assertTrue(processModelsPage.isFileDownloaded(EXPORT_PATH_LOCAL, FILE_NAME));

    }

    @Test(priority = 3)
    public void deleteModel() {
        ProcessModelsPage processModelsPage = ProcessModelsPage.goToProcessModelsPage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        processModelsPage.chooseDomain(DOMAIN);
        processModelsPage.deleteModel(MODEL_NAME);
    }
}
