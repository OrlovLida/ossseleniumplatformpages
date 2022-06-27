package com.oss.pages.reconciliation;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.mainheader.Notifications;
import com.oss.framework.components.mainheader.NotificationsInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class MetamodelPage extends BasePage {

    private static final String CM_INTERFACE_ID = "narComponent_metamodelEditorViewIdsearchCmInterfaceId";
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

    @Step("Search for CMInterface by its name")
    public void searchInterfaceByName(String cmInterfaceName) {
        Input searchField = getComponent(CM_INTERFACE_ID, Input.ComponentType.SEARCH_BOX);
        searchField.setSingleStringValue(cmInterfaceName);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Generate new Metamodel for CMInterface")
    public void generateMetamodel() {
        DelayUtils.waitForPageToLoad(driver, wait);
        TreeWidget objectsTree = getTreeWidget();
        objectsTree.callActionById(ActionsContainer.OTHER_GROUP_ID, GENERATE_METAMODEL_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Search for object by name")
    public void searchForObject(String objectName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TreeWidget objectsTree = getTreeWidget();
        objectsTree.search(objectName);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Check if object exists by name")
    public boolean checkIfObjectExists(String objectName) {
        TreeWidget treeWidget = getTreeWidget();
        return treeWidget.isElementPresent(objectName);
    }

    @Step("Check if Object name is correct")
    public boolean isObjectNameCorrect(String objectName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        PropertyPanel propertyPanel = getPropertyPanel();
        String value = propertyPanel.getPropertyValue("Class Name");
        if (value.equals(objectName)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Step("Check Notificaiton")
    public String checkNotificationMessage() {
        return Notifications.create(driver, wait).getNotificationMessage();
    }

    @Step("Clear old notification")
    public void clearNotification() {
        NotificationsInterface notifications = Notifications.create(driver, wait);
        notifications.clearAllNotification();
    }

    private PropertyPanel getPropertyPanel() {
        return PropertyPanel.createById(driver, wait, PROPERTY_PANEL_ID);
    }

    private TreeWidget getTreeWidget() {
        return TreeWidget.createById(driver, wait, TREE_WIDGET_ID);
    }

    private Input getComponent(String componentId, Input.ComponentType componentType) {
        return ComponentFactory.create(componentId, componentType, driver, wait);
    }

    private TabsInterface getTabsInterface(){
        return TabsWidget.createById(driver, wait, TREE_WIDGET_ID);
    }
}
