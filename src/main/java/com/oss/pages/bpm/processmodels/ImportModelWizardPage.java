package com.oss.pages.bpm.processmodels;

import com.oss.framework.components.inputs.FileChooser;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class ImportModelWizardPage extends BasePage {
    private static final String FILE_COMPONENT_ID = "bpm_models_view_import-model-popup_model-import-form";
    private static final String WIZARD_ID = "bpm_models_view_import-model-popup_prompt-card";
    private static final String IMPORT_BUTTON_ID = "bpm_models_view_import-model-popup_model-import-button-1";
    private final Wizard importModelWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public ImportModelWizardPage(WebDriver driver) {
        super(driver);
    }

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
        importModelWizard.clickButtonById(IMPORT_BUTTON_ID);
    }

    public String getImportStatus() {
        FileChooser input = (FileChooser) importModelWizard.getComponent(FILE_COMPONENT_ID, Input.ComponentType.FILE_CHOOSER);
        return input.getStatus();
    }

    public Boolean isImportWizardVisible() {
        return Wizard.isWizardVisible(driver, WIZARD_ID);
    }

}
