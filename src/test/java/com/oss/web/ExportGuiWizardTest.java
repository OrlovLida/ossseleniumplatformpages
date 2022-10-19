package com.oss.web;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.pages.exportguiwizard.ExportGuiWizardPage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class ExportGuiWizardTest extends BaseTestCase {
    private static final String EXPORT_BUTTON_ID = "exportButton";
    private static final String TEST_MOVIE_TYPE = "TestMovie";
    private static final String CSV = "CSV";
    private static final String XLS = "XLS";
    private static final String XLSX = "XLSX";
    private static final String XML = "XML";
    private NewInventoryViewPage inventoryView;
    private final String emailAddress = "testExport@mail.com";

    @BeforeClass
    public void prepareTests() {
        inventoryView = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, TEST_MOVIE_TYPE);
    }

    @BeforeMethod
    public void openExportGuiWizard() {
        inventoryView.openNotificationPanel().clearNotifications();
        inventoryView.callAction(ActionsContainer.KEBAB_GROUP_ID, EXPORT_BUTTON_ID);

    }

    @Test
    @Description("Export to CSV File from Language Service using Export Gui Wizard")
    public void exportCSVFile() {
        new ExportGuiWizardPage(driver)
                .chooseFileType(CSV)
                .closeTheWizard();
        Assert.assertEquals(countNotification(), 1);
    }

    @Test
    @Description("Export to XLS File from Language Service using Export Gui Wizard")
    public void exportXLSFile() {
        new ExportGuiWizardPage(driver)
                .chooseFileType(XLS)
                .closeTheWizard();
        Assert.assertEquals(countNotification(), 1);
    }

    @Test
    @Description("Export to XLSX File from Language Service using Export Gui Wizard")
    public void exportXLSXFile() {
        new ExportGuiWizardPage(driver)
                .chooseFileType(XLSX)
                .closeTheWizard();
        Assert.assertEquals(countNotification(), 1);
    }

    @Test
    @Description("Export to XML File from Language Service using Export Gui Wizard")
    public void exportXMLFile() {
        new ExportGuiWizardPage(driver)
                .chooseFileType(XML)
                .closeTheWizard();
        Assert.assertEquals(countNotification(), 1);
    }

    @Test
    @Description("Export to PDF File from Language Service using Export Gui Wizard")
    public void exportToPDFFile() {
        new ExportGuiWizardPage(driver)
                .chooseExportToPDF()
                .closeTheWizard();
        Assert.assertEquals(countNotification(), 1);
    }

    @Test
    @Description("Export to PDF File from Language Service using Export Gui Wizard")
    public void exportToCompressedFile() {
        new ExportGuiWizardPage(driver)
                .chooseCompressedFile()
                .closeTheWizard();
        Assert.assertEquals(countNotification(), 1);
    }

    @Test
    @Description("Export to File with Headers from Language Service using Export Gui Wizard")
    public void exportToFileWithHeaders() {
        new ExportGuiWizardPage(driver)
                .chooseExportToFileWithHeaders()
                .closeTheWizard();
        Assert.assertEquals(countNotification(), 1);
    }

    @Test
    @Description("Export to File with Headers from Language Service using Export Gui Wizard")
    public void exportToFileWithChangedDataMask() {
        new ExportGuiWizardPage(driver)
                .chooseFileType(CSV)
                .changeDateMask("Basic")
                .closeTheWizard();
        Assert.assertEquals(countNotification(), 1);
    }

    @Test
    @Description("Export to File with Headers from Language Service using Export Gui Wizard")
    public void exportToFileWithChangedFormat() {
        new ExportGuiWizardPage(driver)
                .chooseFileType(CSV)
                .changeQuoteCharacter("Single")
                .changeCSVDelimiter("Comma")
                .closeTheWizard();
        Assert.assertEquals(countNotification(), 1);
    }

    @Test
    @Description("Export to File with Sending Email from Language Service using Export Gui Wizard")
    public void exportWithSendingEmail() {
        new ExportGuiWizardPage(driver)
                .chooseSendByEmail()
                .goToSendFileByEmailPage()
                .chooseEmail(emailAddress)
                .closeTheWizard();
        Assert.assertEquals(countNotification(), 1);
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
        Assert.assertEquals(countNotification(), 1);
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
        Assert.assertEquals(countNotification(), 1);
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
        Assert.assertEquals(countNotification(), 1);
    }

    private int countNotification() {
        return inventoryView.openNotificationPanel().amountOfNotifications();
    }
}
