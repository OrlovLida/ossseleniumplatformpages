package com.oss.pages.reconciliation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class MetamodelPage extends BasePage {

    private static final String INTERFACE_ID = "narComponent_metamodelEditorViewIdsearchCmInterfaceId";
    private static final String GENERATE_METAMODEL_ID = "narComponent_MetamodelTypeActionGenerateMetamodelId";
    private static final String TREE_WIDGET_ID = "narComponent_metamodelEditorViewIdobjectTypesTreeWindowId";
    private static final String PROPERTY_PANEL_ID = "card-content_narComponent_metamodelEditorViewIdobjectTypePropertiesWindowId";

    protected MetamodelPage(WebDriver driver) {
        super(driver);
    }

    public static MetamodelPage goToMetamodelPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/reco/network-repository-view/metamodel-editor" +
                "?perspective=NETWORK", basicURL));
        return new MetamodelPage(driver);
    }

    @Step("Search for Interface by name = {interfaceName}")
    public void searchInterfaceByName(String interfaceName) {
        getComponent(INTERFACE_ID).setSingleStringValue(interfaceName);
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
        DelayUtils.waitForPageToLoad(driver, wait);
        PropertyPanel propertyPanel = getPropertyPanel();
        String value = propertyPanel.getPropertyValue("Class Name");
        return value.equals(objectName);
    }

    private PropertyPanel getPropertyPanel() {
        return PropertyPanel.createById(driver, wait, PROPERTY_PANEL_ID);
    }

    private TreeWidget getTreeWidget() {
        return TreeWidget.createById(driver, wait, TREE_WIDGET_ID);
    }

    private Input getComponent(String componentId) {
        return ComponentFactory.create(componentId, driver, wait);
    }

    private TabsInterface getTabsInterface() {
        return TabsWidget.createById(driver, wait, TREE_WIDGET_ID);
    }
}
