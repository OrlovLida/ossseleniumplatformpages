package com.oss.pages.templateCM;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import static java.lang.String.format;


public class ChangeConfigurationPage extends BasePage {

    private String SET_PARAMETERS_BUTTON = "Set parameters";
    private String INVENTORY_OBJECTS_TAB_ID = "deviceSelectionWindowTabId";
    private String OBJECT_TYPE_INPUT_ID = "objectTypeValue";
    private String DEVICE_SELECT_TABLE = "DeviceSelectionApp";
    private String TEMPLATE_SELECT_TABLE = "TemplatesTableApp";
    private String EXECUTE_BUTTON = "SingleImmediateExecutionPropertiesSubmitButton-0";
    private String RETRY_COMBOBOX = "retryComboBox-input";
    private Wizard wizard = Wizard.createWizard(driver, wait);


    public static ChangeConfigurationPage goToChangeConfigurationPage(WebDriver driver, String basicURL) {
        driver.get(format("%s/#/view/cm/template-filler-view/view-change?perspective=LIVE", basicURL));
        return new ChangeConfigurationPage(driver);
    }

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
        table.searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD, value);
        table.selectRowByAttributeValueWithLabel("Name", value);
    }

    @Step("Query template with configuration")
    public void selectTemplate(String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, TEMPLATE_SELECT_TABLE);
        table.searchByAttributeWithLabel("Name", Input.ComponentType.TEXT_FIELD, value);
        table.selectRowByAttributeValueWithLabel("Name", value);
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
        //TODO change when data-attributename will be added
        OldActionsContainer executeButton = OldActionsContainer.createFromXPath(driver, wait, "//ul[@class = 'nav groupList']");
        executeButton.callAction("dropdownButtonAction", "Immediately");
    }
}
