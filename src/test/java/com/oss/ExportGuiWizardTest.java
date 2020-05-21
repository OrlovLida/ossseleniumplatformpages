package com.oss;

import com.oss.pages.exportguiwizard.ExportGuiWizardPage;
import com.oss.pages.languageservice.LanguageServicePage;
import org.testng.Assert;
import org.testng.annotations.*;

public class ExportGuiWizardTest extends BaseTestCase {

    private static final String LANGUAGE_SERVICE_PAGE_URL = String.format("%s/#/views/languagesservice/views/translations" +
            "?perspective=LIVE", BASIC_URL);

    private LanguageServicePage languageServicePage;
    private String emailAddress="testExport@mail.com";

    @BeforeClass
    public void prepareTests() {
        languageServicePage = homePage.goToLanguageServicePage(LANGUAGE_SERVICE_PAGE_URL);
        languageServicePage
                .typeIdOfFirstServiceInSearch();
    }

    @BeforeMethod
    public void openExportGuiWizard() {
        languageServicePage
                .clearNotifications()
                .openExportFileWizard();
    }

    @Test
    public void exportCSVFile(){
        new ExportGuiWizardPage(driver)
                .chooseCSV()
                .uncheckTheExportToFileWithHeaders()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    public void exportXLSFile(){
        new ExportGuiWizardPage(driver)
                .chooseXLS()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    public void exportXLSXFile(){
        new ExportGuiWizardPage(driver)
                .chooseXLSX()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    public void exportXMLFile(){
        new ExportGuiWizardPage(driver)
                .chooseXML()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    public void exportToPDFFile(){
        new ExportGuiWizardPage(driver)
                .chooseExportToPDF()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    public void exportToCompressedFile(){
        new ExportGuiWizardPage(driver)
                .chooseCompressedFile()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    public void exportToFileWithHeaders(){
        new ExportGuiWizardPage(driver)
                .chooseExportToFileWithHeaders()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    public void exportToFileWithChangedDataMask(){
        new ExportGuiWizardPage(driver)
                .chooseCSV()
                .changeDateMask("Basic ISO Date")
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    public void exportToFileWithChangedFormat(){
        new ExportGuiWizardPage(driver)
                .chooseCSV()
                .changeQuoteCharacter("Single Quote")
                .changeCSVDelimiter("Comma")
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    public void exportWithSendingEmail(){
        new ExportGuiWizardPage(driver)
                .chooseSendByEmail()
                .goToSendFileByEmailPage()
                .chooseEmail(emailAddress)
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    public void exportWithSendingEmailWithAttachment(){
        new ExportGuiWizardPage(driver)
                .chooseSendByEmail()
                .goToSendFileByEmailPage()
                .chooseEmail(emailAddress)
                .chooseAttachExportedFile()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    public void exportToRemoteLocationBySCP(){
        new ExportGuiWizardPage(driver)
                .chooseRemoteUpload()
                .goToTheFillServerData()
                .chooseProtocoleType("SCP")
                .typeServerAddress("10.132.118.88")
                .typeUserName("oss")
                .typePassword("oss")
                .typeRemoteDirectoryPath("/home/oss/ExportGUITests")
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    public void exportToRemoteLocationBySFTP(){
        new ExportGuiWizardPage(driver)
                .chooseRemoteUpload()
                .goToTheFillServerData()
                .chooseProtocoleType("SFTP")
                .typeServerAddress("10.132.118.88")
                .typeUserName("oss")
                .typePassword("oss")
                .typeRemoteDirectoryPath("/home/oss/ExportGUITests")
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }
}
