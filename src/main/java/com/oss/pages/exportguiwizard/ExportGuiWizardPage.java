package com.oss.pages.exportguiwizard;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ExportGuiWizardPage extends BasePage {

    public ExportGuiWizardPage(WebDriver driver) {
        super(driver);
    }

    public ExportGuiWizardPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait);
        WIZARD_ID = wizardId;
    }

    private static final String CHECKBOX_EXPORT_WITH_HEADERS_ID = "exportgui-components-withheadercheckbox";
    private static final String CHECKBOX_GENERATE_PDF_ID = "exportgui-components-generatepdfcheckbox";
    private static final String CHECKBOX_COMPRESS_FILE_ID = "exportgui-components-compressfilecheckbox";
    private static final String CHECKBOX_SCHEDULE_EXPORT_TASK_ID = "exportgui-components-scheduleexportcheckbox";
    private static final String CHECKBOX_SEND_BY_EMAIL_ID = "exportgui-components-sendbyemailcheckbox";
    private static final String CHECKBOX_REMOTE_UPLOAD_ID = "exportgui-components-remoteuploadcheckbox";
    private static final String COMBOBOX_FILE_TYPE_ID = "exportgui-components-filetypechoose";
    private static final String COMBOBOX_DATE_MASK_ID = "exportgui-components-dateMaskchoose";
    private static final String COMBOBOX_CSV_DELIMITER_ID = "exportgui-components-csvdelimitertxt";
    private static final String COMBOBOX_QUOTE_CHARACTER_ID = "exportgui-components-csvquotechartxt";
    private static final String TEXTFIELD_FILE_NAME_ID = "exportgui-components-filenametxt";
    private String WIZARD_ID = "exportgui-wizard-widget";

    public Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Fill file name")
    public ExportGuiWizardPage fillFileName(String fileName) {
        getWizard().setComponentValue(TEXTFIELD_FILE_NAME_ID, fileName);
        return this;
    }

    @Step("Choose CSV File Type")
    public ExportGuiWizardPage chooseCSV() {
        setComboboxValue(COMBOBOX_FILE_TYPE_ID, "CSV");
        return this;
    }

    @Step("Choose XLSX File Type")
    public ExportGuiWizardPage chooseXLSX() {
        setComboboxValue(COMBOBOX_FILE_TYPE_ID, "XLSX");
        return this;
    }

    @Step("Choose XLS File Type")
    public ExportGuiWizardPage chooseXLS() {
        setComboboxValue(COMBOBOX_FILE_TYPE_ID, "XLS");
        return this;
    }

    @Step("Choose XML File Type")
    public ExportGuiWizardPage chooseXML() {
        setComboboxValue(COMBOBOX_FILE_TYPE_ID, "XML");
        return this;
    }

    @Step("Choose Export to PDF checkbox")
    public ExportGuiWizardPage chooseExportToPDF() {
        checkTheCheckbox(CHECKBOX_GENERATE_PDF_ID);
        return this;
    }

    @Step("Choose compressed file checkbox")
    public ExportGuiWizardPage chooseCompressedFile() {
        checkTheCheckbox(CHECKBOX_COMPRESS_FILE_ID);
        return this;
    }

    @Step("Choose export to file with headers checkbox")
    public ExportGuiWizardPage chooseExportToFileWithHeaders() {
        checkTheCheckbox(CHECKBOX_EXPORT_WITH_HEADERS_ID);
        return this;
    }

    @Step("Choose send by email checkbox")
    public ExportGuiWizardPage chooseSendByEmail() {
        checkTheCheckbox(CHECKBOX_SEND_BY_EMAIL_ID);
        return this;
    }

    @Step("Choose schedule export checkbox")
    public ExportGuiWizardPage chooseScheduleExport() {
        checkTheCheckbox(CHECKBOX_SCHEDULE_EXPORT_TASK_ID);
        return this;
    }

    @Step("Choose remote upload checkbox")
    public ExportGuiWizardPage chooseRemoteUpload() {
        checkTheCheckbox(CHECKBOX_REMOTE_UPLOAD_ID);
        return this;
    }

    @Step("Close the wizard")
    public void closeTheWizard() {
        DelayUtils.sleep(300);
        clickOnAccept();
    }

    @Step("Go to next step of wizard - Fill Server Data")
    public FillServerDataPage goToTheFillServerData() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickOnNext();
        return new FillServerDataPage(driver);
    }

    @Step("Go to next step of wizard - Schedule Task")
    public ScheduleTaskPage goToTheScheduleTask() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickOnNext();
        return new ScheduleTaskPage(driver);
    }

    @Step("Go to next step of wizard - Send File By Email")
    public SendFileByEmailPage goToSendFileByEmailPage() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickOnNext();
        return new SendFileByEmailPage(driver);
    }

    @Step("Change Quote Character on Combobox")
    public ExportGuiWizardPage changeQuoteCharacter(String value) {
        setComboboxValue(COMBOBOX_QUOTE_CHARACTER_ID, value);
        return this;
    }

    @Step("Change CSV Delimiter on Combobox")
    public ExportGuiWizardPage changeCSVDelimiter(String value) {
        setComboboxValue(COMBOBOX_CSV_DELIMITER_ID, value);
        return this;
    }

    @Step("Change Date Mask on Combobox")
    public ExportGuiWizardPage changeDateMask(String value) {
        setComboboxValue(COMBOBOX_DATE_MASK_ID, value);
        return this;
    }

    @Step("Change Date Mask on Combobox")
    public ExportGuiWizardPage changeDateMaskContains(String dateMask) {
        ComponentFactory.create(COMBOBOX_DATE_MASK_ID, driver, wait).setSingleStringValueContains(dateMask);
        return this;
    }

    void setComboboxValue(String comboboxId, String value) {
        getWizard().setComponentValue(comboboxId, value, ComponentType.COMBOBOX);
    }

    void setTextValue(String textFieldId, String value) {
        getWizard().setComponentValue(textFieldId, value);
    }

    void checkTheCheckbox(String checkboxId) {
        getWizard().setComponentValue(checkboxId, "true");
    }

    private void clickOnAccept() {
        getWizard().clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void clickOnNext() {
        getWizard().clickNext();
    }
}

