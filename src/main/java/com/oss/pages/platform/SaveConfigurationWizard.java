package com.oss.pages.platform;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class SaveConfigurationWizard extends BasePage {

    public SaveConfigurationWizard(WebDriver driver){
        super(driver);
    }

    private String DEFAULTT_VIEW_COMBOBOX_ID = "default_view_combo";
    private String NAME_TEXTFIELD_ID = "name";
    private String TYPE_COMBOBOX_ID = "type";
    private String WIZARD_ID = "configuration_popup";
    private String SAVE_AS_NEW_ID = "configuration_popup_button_save_as_new";
    private String SAVE_ID = "configuration_popup_button_save";
    private String CANCEL_BUTTON_ID = "configuration_popup_button_cancel";
    private String GROUPS_ID = "groups-input";
    private String GROUPS_DROPDOWN_ID = "groups-dropdown-search";

    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);


    private Wizard getWizard(String WIZARD_ID){
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public SaveConfigurationWizard typeName(String name){
        wizard.getComponent(NAME_TEXTFIELD_ID, Input.ComponentType.TEXT_FIELD).setSingleStringValue(name);
        return this;
    }

    public SaveConfigurationWizard setAsDefaultForMe(){
        wizard.getComponent(DEFAULTT_VIEW_COMBOBOX_ID, Input.ComponentType.COMBOBOX).setSingleStringValue("Me");
        return this;
    }

    public SaveConfigurationWizard setAsDefaultForGroup(String groupName){
        wizard.getComponent(DEFAULTT_VIEW_COMBOBOX_ID, Input.ComponentType.COMBOBOX).setSingleStringValue("Groups");
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.getComponent(GROUPS_ID, Input.ComponentType.COMBOBOX).click();
        wizard.getComponent(GROUPS_DROPDOWN_ID, Input.ComponentType.COMBOBOX).setSingleStringValue(groupName);
        wizard.getComponent(NAME_TEXTFIELD_ID, Input.ComponentType.COMBOBOX).click();
        return this;
    }

    public SaveConfigurationWizard setAsDefaultForAll(){
        wizard.getComponent(DEFAULTT_VIEW_COMBOBOX_ID, Input.ComponentType.COMBOBOX).setSingleStringValue("All");
        return this;
    }

    public SaveConfigurationWizard setForType(String type){
        wizard.getComponent(TYPE_COMBOBOX_ID, Input.ComponentType.COMBOBOX).setSingleStringValue(type);
        return this;
    }

    @Step("Save as new cnfiguration")
    public void saveAsNew(){
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickActionById(SAVE_AS_NEW_ID);
    }

    @Step("Save Configuration")
    public void save(){
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickActionById(SAVE_ID);
    }

    @Step("Cancel Configuration saving")
    public void cancel(){
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickActionById(CANCEL_BUTTON_ID);
    }

}
