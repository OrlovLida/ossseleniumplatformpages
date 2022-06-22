package com.oss.pages.bpm.processmodels;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.milestones.MilestoneWizardPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.pages.BasePage;

import static com.oss.configuration.Configuration.CONFIGURATION;

/**
 * @author Paweł Rother
 */
public class ProcessModelsPage extends BasePage {
    private static final String KEYWORDS_INPUT_ID = "keywordsComponentId";
    private static final String IDENTIFIER_FIELD_ID = "identifier-field";
    private static final String DESCRIPTION_FIELD_ID = "description-field";
    private static final String NAME_FIELD_ID = "name-field";
    private static final String NAME_LABEL = "Name";
    private static final String DOMAIN_CHOOSER_COMBOBOX_ID = "domain";
    private static final String MODEL_LIST_TABLE_ID = "bpm_models_view_app-model-list";
    private static final String MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID = "grouping-action-model-operations";
    private static final String DELETE_ACTION_BUTTON_ID = "delete";
    private static final String CLONE_ACTION_BUTTON_ID = "clone";
    private static final String EDIT_KEYWORDS_ACTION_BUTTON_ID = "edit-model-keywords";
    private static final String EXPORT_AS_BAR_ACTION_BUTTON_ID = "export-bar";
    private static final String EXPORT_AS_XML_ACTION_BUTTON_ID = "export-with-configuration-files";
    private static final String DELETE_MODEL_POPUP_ID = "bpm_models_view_delete-model-popup_prompt-card";
    private static final String DELETE_MODEL_CONFIRMATION_BUTTON_ID = "ConfirmationBox_bpm_models_view_delete-model-popup_DELETE_MODEL_CONFIRMATION_BOX_VIEW_ID_action_button";
    private static final String CLONE_MODEL_POPUP_ID = "bpm_models_view_clone-model-popup_prompt-card";
    private static final String CLONE_MODEL_ACCEPT_BUTTON_ID = "bpm_models_view_clone-model-popup_clone-model-buttons-1";
    private static final String EDIT_KEYWORDS_POPUP_ID = "bpm_models_view_edit-keywords-popup_prompt-card";
    private static final String EDIT_KEYWORDS_ACCEPT_BUTTON_ID = "wizard-submit-button-bpm_models_view_edit-keywords-popup_prompt-form-id";
    private static final String MODELS_TABS_ID = "bpm_models_view_tabs-container";
    private static final String MILESTONE_TAB_ID = "bpm_models_view_milestones-tab";
    private static final String MILESTONE_LIST_ID = "milestones-data";
    private static final String INITIAL_PARAMETERS_TAB_ID = "tab_bpm_models_view_initial-parameters-tab";
    private static final String EDIT_MILESTONE_ACTION_ID = "edit-milestones";
    private static final String EDIT_MILESTONES_WIZARD_ID = "bpm_models_view_milestones-popup_wizard-app-id";
    private static final String EDIT_MILESTONES_LIST_ID = "bpm_models_view_milestones-popup_wizard-editable-list-id";
    private static final String BPM_AND_PLANNING = "BPM and Planning";
    private static final String PROCESS_MODELS = "Process Models";
    private static final String VIEWS = "Views";
    private static final String BPM = "Business Process Management";

    public ProcessModelsPage(WebDriver driver) {
        super(driver);
    }

    public static ProcessModelsPage goToProcessModelsPage(WebDriver driver, WebDriverWait webDriverWait) {
        SideMenu sidemenu = SideMenu.create(driver, webDriverWait);
        if (driver.getPageSource().contains(BPM_AND_PLANNING))
            sidemenu.callActionByLabel(PROCESS_MODELS, BPM_AND_PLANNING, BPM);
        else
            sidemenu.callActionByLabel(PROCESS_MODELS, VIEWS, BPM);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return new ProcessModelsPage(driver);
    }

    public static ProcessModelsPage goToProcessModelsPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/bpm/models" +
                "?perspective=LIVE", basicURL));
        return new ProcessModelsPage(driver);
    }

    public void chooseDomain(String domain) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Input domainInput = ComponentFactory.create(DOMAIN_CHOOSER_COMBOBOX_ID, driver, wait);
        domainInput.clear();
        DelayUtils.waitForPageToLoad(driver, wait);
        domainInput.setSingleStringValue(domain);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void selectModelByName(String name) {
        getModelsTable().searchByAttributeWithLabel(NAME_LABEL, Input.ComponentType.TEXT_FIELD, name);
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

    public boolean isFileDownloaded(String fileName) throws IOException {
        String downloadPath = CONFIGURATION.getDownloadDir();
        File dir = new File(downloadPath);
        File[] dirContents = dir.listFiles();
        if (dirContents == null) {
            return false;
        }

        for (File dirContent : dirContents) {
            if (dirContent.getName().contains(fileName)) {
                boolean isFileNotEmpty = dirContent.length() > 0;
                // File has been found, it can now be deleted:
                Files.delete(Paths.get(dirContent.getAbsolutePath()));
                return isFileNotEmpty;
            }
        }
        return false;
    }

    public void deleteModel(String modelName) {
        Wizard deleteWizard = openWizardForSelectedModel(modelName, MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, DELETE_ACTION_BUTTON_ID, DELETE_MODEL_POPUP_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        deleteWizard.clickButtonById(DELETE_MODEL_CONFIRMATION_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void cloneModel(String baseModelName, String clonedModelName, String clonedIdentifier, String clonedDescription) {
        Wizard cloneWizard = openWizardForSelectedModel(baseModelName, MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, CLONE_ACTION_BUTTON_ID, CLONE_MODEL_POPUP_ID);
        setWizardAttributeValue(cloneWizard, NAME_FIELD_ID, Input.ComponentType.TEXT_FIELD, clonedModelName);
        setWizardAttributeValue(cloneWizard, IDENTIFIER_FIELD_ID, Input.ComponentType.TEXT_FIELD, clonedIdentifier);
        setWizardAttributeValue(cloneWizard, DESCRIPTION_FIELD_ID, Input.ComponentType.TEXT_FIELD, clonedDescription);

        cloneWizard.clickButtonById(CLONE_MODEL_ACCEPT_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void setWizardAttributeValue(Wizard wizard, String attributeId, Input.ComponentType inputType, String value) {
        Input attribute = wizard.getComponent(attributeId, inputType);
        attribute.clear();
        attribute.setSingleStringValue(value);
    }

    public void setKeyword(String modelName, String keyword) {
        Wizard editKeywordsWizard = openWizardForSelectedModel(modelName, MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, EDIT_KEYWORDS_ACTION_BUTTON_ID, EDIT_KEYWORDS_POPUP_ID);
        setWizardAttributeValue(editKeywordsWizard, KEYWORDS_INPUT_ID, Input.ComponentType.MULTI_SEARCH_FIELD, keyword);
        editKeywordsWizard.clickButtonById(EDIT_KEYWORDS_ACCEPT_BUTTON_ID);
    }

    public Wizard openWizardForSelectedModel(String modelName, String actionId, String wizardPopUpId) {
        TableInterface modelsList = getModelsTable();
        selectModelByName(modelName);
        modelsList.callAction(actionId);
        return Wizard.createByComponentId(driver, wait, wizardPopUpId);
    }

    public Wizard openWizardForSelectedModel(String modelName, String groupingActionId, String actionId, String wizardPopUpId) {
        TableInterface modelsList = getModelsTable();
        selectModelByName(modelName);
        modelsList.callAction(groupingActionId, actionId);
        return Wizard.createByComponentId(driver, wait, wizardPopUpId);
    }

    public void exportModelAsBAR(String modelName) {
        selectModelByName(modelName);
        callAction(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, EXPORT_AS_BAR_ACTION_BUTTON_ID);
    }

    public void exportModelAsXML(String modelName) {
        selectModelByName(modelName);
        callAction(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, EXPORT_AS_XML_ACTION_BUTTON_ID);
    }

    private TableInterface getModelsTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createById(driver, wait, MODEL_LIST_TABLE_ID);
    }

    public void selectInitialParametersTab(String processModelName) {
        selectModelByName(processModelName);
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsWidget milestoneTab = TabsWidget.createById(driver, wait, MODELS_TABS_ID);
        milestoneTab.selectTabById(INITIAL_PARAMETERS_TAB_ID);
    }

    public void selectMilestoneTab(String processModelName) {
        selectModelByName(processModelName);
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsWidget milestoneTab = TabsWidget.createById(driver, wait, MODELS_TABS_ID);
        milestoneTab.selectTabById(MILESTONE_TAB_ID);
    }

    public String getMilestoneValue(String milestoneName, String attributeName) {
        CommonList milestoneList = CommonList.create(driver, wait, MILESTONE_LIST_ID);
        return milestoneList.getRow(NAME_LABEL, milestoneName).getFullContent(attributeName);
    }

    public boolean isMilestonesTabEmpty() {
        CommonList milestoneList = CommonList.create(driver, wait, MILESTONE_LIST_ID);
        return milestoneList.getRows().isEmpty();
    }

    public List<Milestone> addMilestonesForProcessModel(String processModelName, List<Milestone> milestones) {
        selectModelByName(processModelName);
        DelayUtils.waitForPageToLoad(driver, wait);
        callAction(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, EDIT_MILESTONE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard editMilestonesWizard = Wizard.createByComponentId(driver, wait, EDIT_MILESTONES_WIZARD_ID);
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver);
        List<Milestone> addedMilestones = milestones.stream().map(milestone -> milestoneWizardPage.addMilestoneRow(milestone,
                EDIT_MILESTONES_LIST_ID)).collect(Collectors.toList());
        DelayUtils.waitForPageToLoad(driver, wait);
        editMilestonesWizard.clickAccept();
        return addedMilestones;
    }

    public List<Milestone> editMilestonesForProcessModel(String processModelName, List<Milestone> milestones) {
        selectModelByName(processModelName);
        DelayUtils.waitForPageToLoad(driver, wait);
        callAction(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, EDIT_MILESTONE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard editMilestonesWizard = Wizard.createByComponentId(driver, wait, EDIT_MILESTONES_WIZARD_ID);
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver);

        AtomicInteger row = new AtomicInteger(1);
        List<Milestone> editedMilestones = milestones.stream().map(milestone -> milestoneWizardPage.
                editMilestoneRow(milestone, row.getAndIncrement(), EDIT_MILESTONES_LIST_ID)).collect(Collectors.toList());

        DelayUtils.waitForPageToLoad(driver, wait);
        editMilestonesWizard.clickAccept();
        return editedMilestones;
    }

    public void removeMilestonesForProcessModel(String processModelName, int deleteMilestonesNumber) {
        selectModelByName(processModelName);
        DelayUtils.waitForPageToLoad(driver, wait);
        callAction(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, EDIT_MILESTONE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard editMilestonesWizard = Wizard.createByComponentId(driver, wait, EDIT_MILESTONES_WIZARD_ID);
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver);
        for (int i = 0; i < deleteMilestonesNumber; i++) {
            milestoneWizardPage.removeMilestoneRow(1, EDIT_MILESTONES_LIST_ID);
            DelayUtils.waitForPageToLoad(driver, wait);
        }
        editMilestonesWizard.clickAccept();
    }

}


