package com.oss.pages.bpm.processmodels;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

/**
 * @author Paweł Rother
 */
public class ProcessModelsPage extends BasePage {
    private static final String DOMAIN_CHOOSER_COMBOBOX_ID = "domain-chooser--domain-combobox";

    private static final String MODEL_LIST_TABLE_ID = "bpm_models_view_app-model-list";

    private static final String MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID = "grouping-action-model-operations";
    private static final String DELETE_ACTION_BUTTON_ID = "delete";
    private static final String CLONE_ACTION_BUTTON_ID = "clone";
    private static final String EDIT_KEYWORDS_ACTION_BUTTON_ID = "edit-model-keywords";
    private static final String EXPORT_AS_BAR_ACTION_BUTTON_ID = "export-bar";
    private static final String EXPORT_AS_XML_ACTION_BUTTON_ID = "export-with-configuration-files";



    private static final String DELETE_MODEL_POPUP_ID = "bpm_models_view_delete-model-popup";
    private static final String DELETE_MODEL_CONFIRMATION_BUTTON_ID = "ConfirmationBox_bpm_models_view_delete-model-popup_DELETE_MODEL_CONFIRMATION_BOX_VIEW_ID_action_button";

    private static final String CLONE_MODEL_POPUP_ID = "bpm_models_view_clone-model-popup";
    private static final String CLONE_MODEL_ACCEPT_BUTTON_ID = "bpm_models_view_clone-model-popup_clone-model-buttons-1";

    private static final String EDIT_KEYWORDS_POPUP_ID = "bpm_models_view_edit-keywords-popup";
    private static final String EDIT_KEYWORDS_ACCEPT_BUTTON_ID = "wizard-submit-button-bpm_models_view_edit-keywords-popup_prompt-form-id";

    public ProcessModelsPage(WebDriver driver) {
        super(driver);
    }

    public static ProcessModelsPage goToProcessModelsPage(WebDriver driver, WebDriverWait webDriverWait) {
        SideMenu sidemenu = SideMenu.create(driver, webDriverWait);
        if (driver.getPageSource().contains("BPM and Planning"))
            sidemenu.callActionByLabel("Process Models", "BPM and Planning", "Business Process Management");
        else
            sidemenu.callActionByLabel("Process Models", "Views", "Business Process Management");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return new ProcessModelsPage(driver);
    }

    public static ProcessModelsPage goToProcessModelsPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/bpm/models" +
                "?perspective=LIVE", basicURL));

        return new ProcessModelsPage(driver);
    }

    public void chooseDomain(String domain) {
        Input domain_input = ComponentFactory.create(DOMAIN_CHOOSER_COMBOBOX_ID, Input.ComponentType.COMBOBOX, driver, wait);
        domain_input.setSingleStringValue(domain);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private TableInterface getModelsTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentDataAttributeName(driver, wait, MODEL_LIST_TABLE_ID);
    }

    public void selectModelByName(String name) {
        getModelsTable().searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD, name);
        getModelsTable().selectRow(0);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void callAction(String actionId) {
        getModelsTable().callAction(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void callAction(String groupId, String actionId) {
        getModelsTable().callAction(groupId, actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public boolean isFileDownloaded(String downloadPath, String fileName) {
        File dir = new File(downloadPath);
        File[] dirContents = dir.listFiles();
        if (dirContents == null) {
            return false;
        }

        for (int i = 0; i < dirContents.length; i++) {
            if (dirContents[i].getName().contains(fileName)) {
                // File has been found, it can now be deleted:
                dirContents[i].delete();
                return true;
            }
        }
        return false;
    }


    public void deleteModel(String modelName) {
        Wizard deleteWizard = openWizardForSelectedModel(modelName,MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID,DELETE_ACTION_BUTTON_ID, DELETE_MODEL_POPUP_ID);

        deleteWizard.clickActionById(DELETE_MODEL_CONFIRMATION_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void cloneModel(String baseModelName, String clonedModelName, String clonedIdentifier, String clonedDescription) {
        Wizard cloneWizard = openWizardForSelectedModel(baseModelName,MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID,CLONE_ACTION_BUTTON_ID, CLONE_MODEL_POPUP_ID);
        setWizardAttributeValue(cloneWizard,"name-field", Input.ComponentType.TEXT_FIELD,clonedModelName);
        setWizardAttributeValue(cloneWizard,"identifier-field",Input.ComponentType.TEXT_FIELD,clonedIdentifier);
        setWizardAttributeValue(cloneWizard,"description-field",Input.ComponentType.TEXT_FIELD,clonedDescription);

        cloneWizard.clickActionById(CLONE_MODEL_ACCEPT_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void setWizardAttributeValue(Wizard wizard, String attributeId, Input.ComponentType inputType, String value){
        Input attribute = wizard.getComponent(attributeId,inputType);
        attribute.clear();
        attribute.setSingleStringValue(value);
    }

    public void setKeyword(String modelName, String keyword) {
        Wizard editKeywordsWizard = openWizardForSelectedModel(modelName,MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, EDIT_KEYWORDS_ACTION_BUTTON_ID, EDIT_KEYWORDS_POPUP_ID);
        setWizardAttributeValue(editKeywordsWizard,"keywordsComponentId", Input.ComponentType.MULTI_SEARCH_FIELD,keyword);
        editKeywordsWizard.clickActionById(EDIT_KEYWORDS_ACCEPT_BUTTON_ID);
    }
    public Wizard openWizardForSelectedModel(String modelName, String actionId, String wizardPopUpId){
        TableInterface modelsList = getModelsTable();
        selectModelByName(modelName);
        modelsList.callAction(actionId);
        return Wizard.createByComponentId(driver, wait, wizardPopUpId);
    }

    public Wizard openWizardForSelectedModel(String modelName,String groupingActionId, String actionId, String wizardPopUpId){
        TableInterface modelsList = getModelsTable();
        selectModelByName(modelName);
        modelsList.callAction(groupingActionId, actionId);
        return Wizard.createByComponentId(driver, wait, wizardPopUpId);
    }

    public void exportModelAsBAR(String modelName){
        selectModelByName(modelName);
        callAction(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID,EXPORT_AS_BAR_ACTION_BUTTON_ID);
    }

    public void exportModelAsXML(String modelName){
        selectModelByName(modelName);
        callAction(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID,EXPORT_AS_XML_ACTION_BUTTON_ID);
    }

}
