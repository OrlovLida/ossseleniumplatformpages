package com.oss.pages.templatecm.changeconfig;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.contextactions.OldActionsContainer.createById;

public class ChangeConfigurationPage extends BasePage {

    private static final String SET_PARAMETERS_BUTTON = "SetParametersActionName";
    private static final String INVENTORY_OBJECTS_TAB_ID = "deviceSelectionWindowTabId";
    private static final String EXECUTE_ID = "dropdownButtonAction";
    private static final String IMMEDIATELY_ID = "executeImmediateButtonAction";
    private static final String OBJECT_TYPE_INPUT_ID = "objectTypeValue";
    private static final String DEVICE_SELECT_TABLE = "DeviceSelectionApp";
    private static final String TEMPLATE_SELECT_TABLE = "TemplatesTableApp";
    private static final String NAME = "Name";
    private static final String SCRIPT_EXECUTION_WINDOW = "ScriptExecutionWindow";

    private static final String TAB_ID = "DataSourceTabsWindow";

    public ChangeConfigurationPage(WebDriver driver) {
        super(driver);
    }

    @Step("Select object inventory tab")
    public void selectObjectInventoryTab() {
        TabsInterface tabs = TabsWidget.createById(driver, wait, TAB_ID);
        tabs.selectTabById(INVENTORY_OBJECTS_TAB_ID);
    }

    @Step("Select object type")
    public void selectObjectType(String objectType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        selectObjectInventoryTab();
        ComponentFactory.create(OBJECT_TYPE_INPUT_ID, Input.ComponentType.COMBOBOX, driver, wait).setSingleStringValue(objectType);
    }

    @Step("Query object for change configuration")
    public void selectObject(String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable table = OldTable.createById(driver, wait, DEVICE_SELECT_TABLE);
        table.searchByColumn(NAME, value);
        table.selectRowByAttributeValueWithLabel(NAME, value);
    }

    @Step("Query template with configuration")
    public void selectTemplate(String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable table = OldTable.createById(driver, wait, TEMPLATE_SELECT_TABLE);
        table.searchByColumn(NAME, value);
        table.selectRowByAttributeValueWithLabel(NAME, value);
    }

    @Step("Click set parameters button")
    public void clickSetParameters() {
        DelayUtils.waitForPageToLoad(driver, wait);
        createById(driver, wait, SCRIPT_EXECUTION_WINDOW).callActionById(SET_PARAMETERS_BUTTON);
    }

    @Step("Deploy template immediately")
    public void deployImmediately() {
        DelayUtils.waitForPageToLoad(driver, wait);
        createById(driver, wait, SCRIPT_EXECUTION_WINDOW).callActionById(EXECUTE_ID, IMMEDIATELY_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
