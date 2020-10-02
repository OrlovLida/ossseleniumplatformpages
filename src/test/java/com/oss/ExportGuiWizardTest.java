package com.oss;

import com.oss.pages.exportguiwizard.ExportGuiWizardPage;
import com.oss.pages.languageservice.LanguageServicePage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.*;
import com.oss.utils.*;


@Listeners({TestListener.class})
public class ExportGuiWizardTest extends BaseTestCase {

    private LanguageServicePage languageServicePage;
    private String emailAddress = "testExport@mail.com";

    @BeforeClass
    public void prepareTests() {
        this.languageServicePage = LanguageServicePage.goToLanguageServicePage(driver, BASIC_URL);
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
    @Description("Export to CSV File from Language Service using Export Gui Wizard")
    public void exportCSVFile() {
        new ExportGuiWizardPage(driver)
                .chooseCSV()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    @Description("Export to XLS File from Language Service using Export Gui Wizard")
    public void exportXLSFile() {
        new ExportGuiWizardPage(driver)
                .chooseXLS()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    @Description("Export to XLSX File from Language Service using Export Gui Wizard")
    public void exportXLSXFile() {
        new ExportGuiWizardPage(driver)
                .chooseXLSX()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    @Description("Export to XML File from Language Service using Export Gui Wizard")
    public void exportXMLFile() {
        new ExportGuiWizardPage(driver)
                .chooseXML()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    @Description("Export to PDF File from Language Service using Export Gui Wizard")
    public void exportToPDFFile() {
        new ExportGuiWizardPage(driver)
                .chooseExportToPDF()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    @Description("Export to PDF File from Language Service using Export Gui Wizard")
    public void exportToCompressedFile() {
        new ExportGuiWizardPage(driver)
                .chooseCompressedFile()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    @Description("Export to File with Headers from Language Service using Export Gui Wizard")
    public void exportToFileWithHeaders() {
        new ExportGuiWizardPage(driver)
                .chooseExportToFileWithHeaders()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    @Description("Export to File with Headers from Language Service using Export Gui Wizard")
    public void exportToFileWithChangedDataMask() {
        new ExportGuiWizardPage(driver)
                .chooseCSV()
                .changeDateMask("Basic ISO Date")
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    @Description("Export to File with Headers from Language Service using Export Gui Wizard")
    public void exportToFileWithChangedFormat() {
        new ExportGuiWizardPage(driver)
                .chooseCSV()
                .changeQuoteCharacter("Single Quote")
                .changeCSVDelimiter("Comma")
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    @Description("Export to File with Sending Email from Language Service using Export Gui Wizard")
    public void exportWithSendingEmail() {
        new ExportGuiWizardPage(driver)
                .chooseSendByEmail()
                .goToSendFileByEmailPage()
                .chooseEmail(emailAddress)
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    @Description("Export to File with Sending Email with Attachment from Language Service using Export Gui Wizard")
    public void exportWithSendingEmailWithAttachment() {
        new ExportGuiWizardPage(driver)
                .chooseSendByEmail()
                .goToSendFileByEmailPage()
                .chooseEmail(emailAddress)
                .chooseAttachExportedFile()
                .closeTheWizard();
        Assert.assertEquals(languageServicePage.howManyNotifications(), 1);
    }

    @Test
    @Description("Export to Remote Location By SCP from Language Service using Export Gui Wizard")
    public void exportToRemoteLocationBySCP() {
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
    @Description("Export to Remote Location By SFTP from Language Service using Export Gui Wizard")
    public void exportToRemoteLocationBySFTP() {
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
