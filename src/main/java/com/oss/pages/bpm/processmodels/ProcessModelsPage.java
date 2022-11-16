package com.oss.pages.bpm.processmodels;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.bpm.milestones.Milestone;
import com.oss.pages.bpm.milestones.MilestoneWizardPage;
import com.oss.pages.platform.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    private static final String BPM_ADMINISTRATION = "BPM Administration";
    private static final String PROCESS_MODELS = "Process Models";
    private static final String KEYWORDS_LABEL = "Keywords";

    public ProcessModelsPage(WebDriver driver) {
        super(driver);
    }

    public static ProcessModelsPage goToProcessModelsPage(WebDriver driver, WebDriverWait webDriverWait) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        HomePage homePage = new HomePage(driver);
        homePage.chooseFromLeftSideMenu(PROCESS_MODELS, BPM_AND_PLANNING, BPM_ADMINISTRATION);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return new ProcessModelsPage(driver);
    }

    public static ProcessModelsPage goToProcessModelsPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/bpm/models" +
                "?perspective=LIVE", basicURL));
        return new ProcessModelsPage(driver);
    }

    public ProcessModelsPage chooseDomain(String domain) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Input domainInput = ComponentFactory.create(DOMAIN_CHOOSER_COMBOBOX_ID, driver, wait);
        domainInput.clear();
        DelayUtils.waitForPageToLoad(driver, wait);
        domainInput.setSingleStringValue(domain);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    public ProcessModelsPage selectModelByName(String name) {
        getModelsTable().searchByAttributeWithLabel(NAME_LABEL, Input.ComponentType.TEXT_FIELD, name);
        getModelsTable().selectRow(0);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    public ProcessModelsPage selectModel(String modelName, String modelKeyword) {
        getModelsTable().searchByAttributeWithLabel(NAME_LABEL, Input.ComponentType.TEXT_FIELD, modelName);
        getModelsTable().searchByAttributeWithLabel(KEYWORDS_LABEL, Input.ComponentType.TEXT_FIELD, modelKeyword);
        getModelsTable().selectRow(0);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
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

    public boolean isModelExists(String modelName, String modelKeyword) {
        try {
            selectModel(modelName, modelKeyword);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public void deleteSelectedModel() {
        Wizard deleteWizard = openWizardForSelectedModel(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID,
                DELETE_ACTION_BUTTON_ID, DELETE_MODEL_POPUP_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        deleteWizard.clickButtonById(DELETE_MODEL_CONFIRMATION_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void cloneSelectedModel(String clonedModelName, String clonedIdentifier, String clonedDescription) {
        Wizard cloneWizard = openWizardForSelectedModel(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID,
                CLONE_ACTION_BUTTON_ID, CLONE_MODEL_POPUP_ID);
        setWizardAttributeValue(cloneWizard, NAME_FIELD_ID, clonedModelName);
        setWizardAttributeValue(cloneWizard, IDENTIFIER_FIELD_ID, clonedIdentifier);
        setWizardAttributeValue(cloneWizard, DESCRIPTION_FIELD_ID, clonedDescription);

        cloneWizard.clickButtonById(CLONE_MODEL_ACCEPT_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void setWizardAttributeValue(Wizard wizard, String attributeId, String value) {
        Input attribute = wizard.getComponent(attributeId);
        attribute.clear();
        attribute.setSingleStringValue(value);
    }

    public void setKeywordForSelectedModel(String keyword) {
        Wizard editKeywordsWizard = openWizardForSelectedModel(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID,
                EDIT_KEYWORDS_ACTION_BUTTON_ID, EDIT_KEYWORDS_POPUP_ID);
        Input attribute = editKeywordsWizard.getComponent(KEYWORDS_INPUT_ID);
        attribute.setSingleStringValueContains(keyword);
        editKeywordsWizard.clickButtonById(EDIT_KEYWORDS_ACCEPT_BUTTON_ID);
    }

    public Wizard openWizardForSelectedModel(String groupingActionId, String actionId, String wizardPopUpId) {
        TableInterface modelsList = getModelsTable();
        modelsList.callAction(groupingActionId, actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
        return Wizard.createByComponentId(driver, wait, wizardPopUpId);
    }

    public void exportSelectedModelAsBAR() {
        callAction(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, EXPORT_AS_BAR_ACTION_BUTTON_ID);
    }

    public void exportSelectedModelAsXML() {
        callAction(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, EXPORT_AS_XML_ACTION_BUTTON_ID);
    }

    private TableInterface getModelsTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createById(driver, wait, MODEL_LIST_TABLE_ID);
    }

    public ProcessModelsPage openInitialParametersTab() {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsWidget milestoneTab = TabsWidget.createById(driver, wait, MODELS_TABS_ID);
        milestoneTab.selectTabById(INITIAL_PARAMETERS_TAB_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    public ProcessModelsPage openMilestoneTab() {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsWidget milestoneTab = TabsWidget.createById(driver, wait, MODELS_TABS_ID);
        milestoneTab.selectTabById(MILESTONE_TAB_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        return this;
    }

    public String getMilestoneValue(String milestoneName, String attributeName) {
        CommonList milestoneList = CommonList.create(driver, wait, MILESTONE_LIST_ID);
        return milestoneList.getRow(NAME_LABEL, milestoneName).getFullContent(attributeName);
    }

    public boolean isMilestonesTabEmpty() {
        CommonList milestoneList = CommonList.create(driver, wait, MILESTONE_LIST_ID);
        return milestoneList.getRows().isEmpty();
    }

    public List<Milestone> addMilestonesForSelectedModel(List<Milestone> milestones) {
        DelayUtils.waitForPageToLoad(driver, wait);
        callAction(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, EDIT_MILESTONE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard editMilestonesWizard = Wizard.createByComponentId(driver, wait, EDIT_MILESTONES_WIZARD_ID);
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, EDIT_MILESTONES_LIST_ID);
        List<Milestone> addedMilestones = milestones.stream().map(milestoneWizardPage::addMilestoneRow)
                .collect(Collectors.toList());
        DelayUtils.waitForPageToLoad(driver, wait);
        editMilestonesWizard.clickAccept();
        return addedMilestones;
    }

    public List<Milestone> editMilestonesForSelectedModel(List<Milestone> milestones) {
        DelayUtils.waitForPageToLoad(driver, wait);
        callAction(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, EDIT_MILESTONE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard editMilestonesWizard = Wizard.createByComponentId(driver, wait, EDIT_MILESTONES_WIZARD_ID);
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, EDIT_MILESTONES_LIST_ID);

        AtomicInteger row = new AtomicInteger(1);
        List<Milestone> editedMilestones = milestones.stream().map(milestone -> milestoneWizardPage.
                editMilestoneRow(milestone, row.getAndIncrement())).collect(Collectors.toList());

        DelayUtils.waitForPageToLoad(driver, wait);
        editMilestonesWizard.clickAccept();
        return editedMilestones;
    }

    public void removeMilestonesForSelectedModel(int deleteMilestonesNumber) {
        DelayUtils.waitForPageToLoad(driver, wait);
        callAction(MODEL_OPERATIONS_GROUPING_ACTION_BUTTON_ID, EDIT_MILESTONE_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard editMilestonesWizard = Wizard.createByComponentId(driver, wait, EDIT_MILESTONES_WIZARD_ID);
        MilestoneWizardPage milestoneWizardPage = new MilestoneWizardPage(driver, EDIT_MILESTONES_LIST_ID);
        for (int i = 0; i < deleteMilestonesNumber; i++) {
            milestoneWizardPage.removeMilestoneRow(1);
            DelayUtils.waitForPageToLoad(driver, wait);
        }
        editMilestonesWizard.clickAccept();
    }

}


