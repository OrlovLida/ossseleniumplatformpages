package com.oss.pages.exportguiwizard;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.inputs.TextField;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.languageservice.LanguageServicePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ExportGuiWizardPage extends BasePage {

    public ExportGuiWizardPage(WebDriver driver) {super(driver); getWizard(); }

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
    private static Wizard wizard;

    public Wizard getWizard() {
        if (wizard == null) {
            wizard = Wizard.createWizard(driver, wait);
        }
        return wizard;
    }

    @Step("Choose CSV File Type")
    public ExportGuiWizardPage chooseCSV(){
        setValueOnCombobox(COMBOBOX_FILE_TYPE_ID, "CSV");
        return this;
    }

    @Step("Choose XLSX File Type")
    public ExportGuiWizardPage chooseXLSX(){
        setValueOnCombobox(COMBOBOX_FILE_TYPE_ID, "XLSX");
        return this;
    }

    @Step("Choose XLS File Type")
    public ExportGuiWizardPage chooseXLS(){
        setValueOnCombobox(COMBOBOX_FILE_TYPE_ID, "XLS");
        return this;
    }

    @Step("Choose XML File Type")
    public ExportGuiWizardPage chooseXML(){
        setValueOnCombobox(COMBOBOX_FILE_TYPE_ID, "XML");
        return this;
    }

    @Step("Choose Export to PDF checkbox")
    public ExportGuiWizardPage chooseExportToPDF(){
        checkTheCheckbox(CHECKBOX_GENERATE_PDF_ID);
        return this;
    }

    @Step("Choose compressed file checkbox")
    public ExportGuiWizardPage chooseCompressedFile(){
        checkTheCheckbox(CHECKBOX_COMPRESS_FILE_ID);
        return this;
    }

    @Step("Choose export to file with headers checkbox")
    public ExportGuiWizardPage chooseExportToFileWithHeaders(){
        checkTheCheckbox(CHECKBOX_EXPORT_WITH_HEADERS_ID);
        return this;
    }

    @Step("Choose send by email checkbox")
    public ExportGuiWizardPage chooseSendByEmail(){
        checkTheCheckbox(CHECKBOX_SEND_BY_EMAIL_ID);
        return this;
    }

    @Step("Choose schedule export checkbox")
    public ExportGuiWizardPage chooseScheduleExport(){
        checkTheCheckbox(CHECKBOX_SCHEDULE_EXPORT_TASK_ID);
        return this;
    }

    @Step("Choose remote upload checkbox")
    public ExportGuiWizardPage chooseRemoteUpload(){
        checkTheCheckbox(CHECKBOX_REMOTE_UPLOAD_ID);
        return this;
    }

    public ExportGuiWizardPage typeFileName(String FILE_NAME){
        setValueOnTextField(TEXT_FIELD_FILE_NAME_ID, Data.createSingleData(FILE_NAME));
        return this;
    }

    @Step("Close the wizard")
    public LanguageServicePage closeTheWizard(){
        DelayUtils.sleep(300);
        clickOnAccept();
        getWizard().waitToClose();
        return new LanguageServicePage(driver);
    }

    @Step("Go to next step of wizard - Fill Server Data")
    public FillServerDataPage goToTheFillServerData(){
        DelayUtils.waitForPageToLoad(driver, wait);
        clickOnNext();
        return new FillServerDataPage(driver);
    }

    @Step("Go to next step of wizard - Schedule Task")
    public ScheduleTaskPage goToTheScheduleTask(){
        DelayUtils.waitForPageToLoad(driver, wait);
        clickOnNext();
        return new ScheduleTaskPage(driver);
    }

    @Step("Go to next step of wizard - Send File By Email")
    public SendFileByEmailPage goToSendFileByEmailPage(){
        DelayUtils.waitForPageToLoad(driver,wait);
        clickOnNext();
        return new SendFileByEmailPage(driver);
    }

    @Step("Uncheck the Export to File with Headers checkbox")
    public ExportGuiWizardPage uncheckTheExportToFileWithHeaders(){
        uncheckTheCheckbox(CHECKBOX_EXPORT_WITH_HEADERS_ID);
        return this;
    }

    @Step("Change Quote Character on Combobox")
    public ExportGuiWizardPage changeQuoteCharacter(String value){
        setValueOnCombobox(COMBOBOX_QUOTE_CHARACTER_ID, value);
        return this;
    }

    @Step("Change CSV Delimiter on Combobox")
    public ExportGuiWizardPage changeCSVDelimiter(String value){
        setValueOnCombobox(COMBOBOX_CSV_DELIMITER_ID, value);
        return this;
    }

    @Step("Change Date Mask on Combobox")
    public ExportGuiWizardPage changeDateMask(String value){
        setValueContainsOnCombobox(COMBOBOX_DATE_MASK_ID, value);
        return this;
    }


    protected void setValueOnCombobox (String COMBOBOX_ID, String value){
        getWizard().getComponent(COMBOBOX_ID, ComponentType.COMBOBOX)
                .setSingleStringValue(value);
    }

    protected void setValueContainsOnCombobox (String COMBOBOX_ID, String value){
        getWizard().getComponent(COMBOBOX_ID, ComponentType.COMBOBOX)
                .setSingleStringValueContains(value);
    }

    protected void setValueOnTextField (String TEXT_FIELD_ID, Data value){
        TextField textField = (TextField) getComponent(TEXT_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        textField.setValue(value);
    }

    protected void checkTheCheckbox(String CHECKBOX_ID){
        Input checkBox = getWizard().getComponent(CHECKBOX_ID, ComponentType.CHECKBOX);
        checkBox.setSingleStringValue("true");
    }

    private void uncheckTheCheckbox(String CHECKBOX_ID){
        Input checkBox = getWizard().getComponent(CHECKBOX_ID, ComponentType.CHECKBOX);
        checkBox.setSingleStringValue("false");
    }

    private Input getComponent(String componentId, Input.ComponentType componentType) {
        return getWizard().getComponent(componentId, componentType);
    }

    private void clickOnAccept() {getWizard().clickAccept();}

    private void clickOnNext() {getWizard().clickNext();}
}

