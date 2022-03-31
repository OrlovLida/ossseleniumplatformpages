package com.oss.pages.exportguiwizard;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.inputs.TextField;
import com.oss.framework.components.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.languageservice.LanguageServicePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class ExportGuiWizardPage extends BasePage {

    public ExportGuiWizardPage(WebDriver driver) {
        super(driver);
    }

    private static final String CHECKBOX_EXPORT_WITH_HEADERS_ID = "exportgui-components-withheadercheckbox";
    private static final String CHECKBOX_GENERATE_PDF_ID = "exportgui-components-generatepdfcheckbox";
    private static final String CHECKBOX_COMPRESS_FILE_ID = "exportgui-components-compressfilecheckbox";
    private static final String CHECKBOX_SCHEDULE_EXPORT_TASK_ID = "exportgui-components-scheduleexportcheckbox";
    private static final String CHECKBOX_SEND_BY_EMAIL_ID = "exportgui-components-sendbyemailcheckbox";
    private static final String CHECKBOX_REMOTE_UPLOAD_ID = "exportgui-components-remoteuploadcheckbox";
    private static final String COMBOBOX_FILE_TYPE_ID = "exportgui-components-filetypechoose-input";
    private static final String TEXT_FIELD_FILE_NAME_ID = "exportgui-components-filenametxt";
    private static final String COMBOBOX_DATE_MASK_ID = "exportgui-components-dateMaskchoose-input";
    private static final String COMBOBOX_CSV_DELIMITER_ID = "exportgui-components-csvdelimitertxt-input";
    private static final String COMBOBOX_QUOTE_CHARACTER_ID = "exportgui-components-csvquotechartxt-input";
    private static final String WIZARD_ID = "exportgui-wizard-widget";

    public Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Choose CSV File Type")
    public ExportGuiWizardPage chooseCSV() {
        setValueOnCombobox(COMBOBOX_FILE_TYPE_ID, "CSV");
        return this;
    }

    @Step("Choose XLSX File Type")
    public ExportGuiWizardPage chooseXLSX() {
        setValueOnCombobox(COMBOBOX_FILE_TYPE_ID, "XLSX");
        return this;
    }

    @Step("Choose XLS File Type")
    public ExportGuiWizardPage chooseXLS() {
        setValueOnCombobox(COMBOBOX_FILE_TYPE_ID, "XLS");
        return this;
    }

    @Step("Choose XML File Type")
    public ExportGuiWizardPage chooseXML() {
        setValueOnCombobox(COMBOBOX_FILE_TYPE_ID, "XML");
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

    public ExportGuiWizardPage typeFileName(String fileName) {
        setValueOnTextField(TEXT_FIELD_FILE_NAME_ID, Data.createSingleData(fileName));
        return this;
    }

    @Step("Close the wizard")
    public LanguageServicePage closeTheWizard() {
        DelayUtils.sleep(300);
        clickOnAccept();
        return new LanguageServicePage(driver);
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

    @Step("Uncheck the Export to File with Headers checkbox")
    public ExportGuiWizardPage uncheckTheExportToFileWithHeaders() {
        uncheckTheCheckbox(CHECKBOX_EXPORT_WITH_HEADERS_ID);
        return this;
    }

    @Step("Change Quote Character on Combobox")
    public ExportGuiWizardPage changeQuoteCharacter(String value) {
        setValueContainsOnCombobox(COMBOBOX_QUOTE_CHARACTER_ID, value);
        return this;
    }

    @Step("Change CSV Delimiter on Combobox")
    public ExportGuiWizardPage changeCSVDelimiter(String value) {
        setValueOnCombobox(COMBOBOX_CSV_DELIMITER_ID, value);
        return this;
    }

    @Step("Change Date Mask on Combobox")
    public ExportGuiWizardPage changeDateMask(String value) {
        setValueContainsOnCombobox(COMBOBOX_DATE_MASK_ID, value);
        return this;
    }


    void setValueOnCombobox(String comboboxId, String value) {
        getWizard().getComponent(comboboxId, ComponentType.COMBOBOX)
                .setSingleStringValue(value);
    }

    private void setValueContainsOnCombobox(String comboboxId, String value) {
        getWizard().getComponent(comboboxId, ComponentType.COMBOBOX).clear();
        getWizard().getComponent(comboboxId, ComponentType.COMBOBOX)
                .setSingleStringValueContains(value);
    }

    void setValueOnTextField(String textFieldId, Data value) {
        TextField textField = (TextField) getComponent(textFieldId, Input.ComponentType.TEXT_FIELD);
        textField.setValue(value);
    }

    void checkTheCheckbox(String checkboxId) {
        Input checkBox = getWizard().getComponent(checkboxId, ComponentType.CHECKBOX);
        checkBox.setSingleStringValue("true");
    }

    private void uncheckTheCheckbox(String checkboxId) {
        Input checkBox = getWizard().getComponent(checkboxId, ComponentType.CHECKBOX);
        checkBox.setSingleStringValue("false");
    }

    private Input getComponent(String componentId, Input.ComponentType componentType) {
        return getWizard().getComponent(componentId, componentType);
    }

    private void clickOnAccept() {
        getWizard().clickAccept();
       DelayUtils.waitForPageToLoad(driver,wait);
    }

    private void clickOnNext() {
        getWizard().clickNext();
    }
}

