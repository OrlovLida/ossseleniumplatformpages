package com.oss.pages.templatecm;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.tabs.TabWindowWidget;
import com.oss.framework.widgets.tabs.TabsInterface;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ChangeConfigurationPage extends BasePage {

    private static final String SET_PARAMETERS_BUTTON = "Set parameters";
    private static final String INVENTORY_OBJECTS_TAB_ID = "deviceSelectionWindowTabId";
    private static final String EXECUTE_ID = "dropdownButtonAction";
    private static final String IMMEDIATELY_ID = "executeImmediateButtonAction";
    private static final String OBJECT_TYPE_INPUT_ID = "objectTypeValue";
    private static final String DEVICE_SELECT_TABLE = "DeviceSelectionApp";
    private static final String TEMPLATE_SELECT_TABLE = "TemplatesTableApp";
    private static final String NAME = "Name";

    private Wizard wizard = Wizard.createWizard(driver, wait);

    public ChangeConfigurationPage(WebDriver driver) {
        super(driver);
    }

    @Step("Select object inventory tab")
    public void selectObjectInventoryTab() {
        TabsInterface tabs = TabWindowWidget.create(driver, wait);
        tabs.selectTabById(INVENTORY_OBJECTS_TAB_ID);
    }

    @Step("Select object type")
    public void selectObjectType(String objectType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        selectObjectInventoryTab();
        wizard.getComponent(OBJECT_TYPE_INPUT_ID, Input.ComponentType.COMBOBOX).setSingleStringValue(objectType);
    }

    @Step("Query object for change configuration")
    public void selectObject(String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, DEVICE_SELECT_TABLE);
        table.searchByAttributeWithLabel(NAME, Input.ComponentType.TEXT_FIELD, value);
        table.selectRowByAttributeValueWithLabel(NAME, value);
    }

    @Step("Query template with configuration")
    public void selectTemplate(String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, TEMPLATE_SELECT_TABLE);
        table.searchByAttributeWithLabel(NAME, Input.ComponentType.TEXT_FIELD, value);
        table.selectRowByAttributeValueWithLabel(NAME, value);
    }

    @Step("Click set parameters button")
    public void clickSetParameters() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button setParametersButton = Button.create(driver, SET_PARAMETERS_BUTTON, "a");
        setParametersButton.click();
    }

    @Step("Deploy template immediately")
    public void deployImmediately() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.createBySelectorAndId(driver, "button", EXECUTE_ID).click();
        Button.createBySelectorAndId(driver, "a", IMMEDIATELY_ID).click();
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
