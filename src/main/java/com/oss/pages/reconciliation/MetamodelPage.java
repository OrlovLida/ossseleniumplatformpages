package com.oss.pages.reconciliation;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class MetamodelPage extends BasePage {

    private static final String INTERFACE_ID = "input_narComponent_metamodelEditorViewIdsearchCmInterfaceId";
    private static final String GENERATE_METAMODEL_ID = "narComponent_MetamodelTypeActionGenerateMetamodelId";
    private static final String IMPORT_METAMODEL_ID = "narComponent_MetamodelTypeActionImportId";
    private static final String IMPORT_METAMODEL_WIZARD_ID = "narComponent_metamodelEditorViewIdimportMetamodelPopupId_prompt-card";
    private static final String IMPORT_BUTTON_ID = "importMetamodelPopupButtons-1";
    private static final String TREE_WIDGET_ID = "narComponent_metamodelEditorViewIdobjectTypesTreeWindowId";
    private static final String PROPERTY_PANEL_ID = "card-content_narComponent_metamodelEditorViewIdobjectTypePropertiesWindowId";
    private static final String VALIDATION_RULES_DEFINITIONS_TABS_ID = "narComponent_metamodelEditorViewIdvalidationRulesWindowId";
    private static final String CREATE_DEFINITION_ACTION_ID = "networkParametersAudit_ValidationRulesDefinitionActionCreateId";

    public MetamodelPage(WebDriver driver) {
        super(driver);
    }

    public static MetamodelPage goToMetamodelPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/reco/network-repository-view/metamodel-editor" +
                "?perspective=NETWORK", basicURL));
        return new MetamodelPage(driver);
    }

    @Step("Search for Interface by name = {interfaceName}")
    public void searchInterfaceByName(String interfaceName) {
        getComponent().setSingleStringValue(interfaceName);
    }

    @Step("Generate new Metamodel for Interface")
    public void generateMetamodel() {
        getTreeWidget().callActionById(ActionsContainer.OTHER_GROUP_ID, GENERATE_METAMODEL_ID);
    }

    @Step("Search for object by name = {objectName}")
    public void searchForObject(String objectName) {
        getTreeWidget().search(objectName);
    }

    @Step("Check if object exists by name = {objectName}")
    public boolean isObjectDisplayed(String objectName) {
        return getTreeWidget().isRowPresent(objectName);
    }

    @Step("Check if Object name is correct")
    public boolean isObjectNameCorrect(String objectName) {
        waitForPageToLoad();
        PropertyPanel propertyPanel = getPropertyPanel();
        String value = propertyPanel.getPropertyValue("Class Name");
        return value.equals(objectName);
    }

    @Step("Select Object with name = {objectName}")
    public void selectObjectType(String objectName) {
        getTreeWidget().selectTreeRow(objectName);
    }

    public void importMetamodel(String path) throws URISyntaxException {
        waitForPageToLoad();
        getTreeWidget().callActionById(ActionsContainer.OTHER_GROUP_ID, IMPORT_METAMODEL_ID);
        URL res = getClass().getClassLoader().getResource(path);
        try {
            assert res != null;
            File file = Paths.get(res.toURI()).toFile();
            String absolutePath = file.getAbsolutePath();
            Wizard wizard = Wizard.createByComponentId(driver, wait, IMPORT_METAMODEL_WIZARD_ID);
            waitForPageToLoad();
            ;
            Input input = wizard.getComponent(IMPORT_METAMODEL_WIZARD_ID, Input.ComponentType.FILE_CHOOSER);
            input.setSingleStringValue(absolutePath);
            wizard.clickButtonById(IMPORT_BUTTON_ID);
        } catch (URISyntaxException e) {
            throw new URISyntaxException("Cant load file", e.getReason());
        }
    }

    public void openRuleDefinitionWizard() {
        getValidationRulesDefinitionsTabs().callActionById(CREATE_DEFINITION_ACTION_ID);
    }

    public void selectSimpleValueCheckTab() {
        getValidationRulesDefinitionsTabs().selectTabById("0");
    }

    public void selectComplexValueCheckTab() {
        getValidationRulesDefinitionsTabs().selectTabById("1");
    }

    public void selectObjectCardinalityCheckTab() {
        getValidationRulesDefinitionsTabs().selectTabById("2");
    }

    public void selectCardinalityAndValuesCheckTab() {
        getValidationRulesDefinitionsTabs().selectTabById("3");
    }

    private PropertyPanel getPropertyPanel() {
        return PropertyPanel.createById(driver, wait, PROPERTY_PANEL_ID);
    }

    private TreeWidget getTreeWidget() {
        return TreeWidget.createById(driver, wait, TREE_WIDGET_ID);
    }

    private Input getComponent() {
        return ComponentFactory.create(MetamodelPage.INTERFACE_ID, driver, wait);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private TabsInterface getValidationRulesDefinitionsTabs() {
        return TabsWidget.createById(driver, wait, VALIDATION_RULES_DEFINITIONS_TABS_ID);
    }
}
