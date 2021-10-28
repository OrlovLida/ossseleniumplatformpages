package com.oss.pages.bpm.processmodels;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ImportModelWizardPage extends BasePage {
    public ImportModelWizardPage(WebDriver driver) {
        super(driver);
    }

    private static final String FILE_COMPONENT_ID = "bpm_models_view_import-model-popup_model-import-form";
    private static final String WIZARD_ID = "bpm_models_view_import-model-popup";
    private static final String IMPORT_BUTTON_ID = "bpm_models_view_import-model-popup_model-import-button-1";


    private Wizard importModelWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public void attachFile(String filePath) {
        Input component = importModelWizard.getComponent(FILE_COMPONENT_ID, Input.ComponentType.FILE_CHOOSER);
        component.setSingleStringValue(filePath);
    }

    public void deleteFiles() {
        Input input = importModelWizard.getComponent(FILE_COMPONENT_ID, Input.ComponentType.FILE_CHOOSER);
        input.clear();
    }

    public List<String> getAttachmentName() {
        Input input = importModelWizard.getComponent(FILE_COMPONENT_ID, Input.ComponentType.FILE_CHOOSER);
        return input.getStringValues();
    }

    public void importButton() {
        importModelWizard.clickActionById(IMPORT_BUTTON_ID);
    }

    public String getImportStatus (){
        WebElement importStatus = driver.findElement(By.xpath(".//span[@class='uploadStatus']"));
        return importStatus.getText();
    }

}
