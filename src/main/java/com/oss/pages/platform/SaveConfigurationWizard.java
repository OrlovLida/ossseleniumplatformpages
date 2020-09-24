package com.oss.pages.platform;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.utils.LocatingUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;

public class SaveConfigurationWizard extends BasePage {

    public SaveConfigurationWizard(WebDriver driver){
        super(driver);
    }

    private String DEFAULTT_VIEW_COMBOBOX_ID = "default_view_combo";
    private String NAME_TEXTFIELD_ID = "name";
    private String WIZARD_ID = "configuration_popup";
    private String SAVE_AS_NEW = "configuration_popup_button_save_as_new";

    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);


    public SaveConfigurationWizard typeName(String name){
        wizard.getComponent(NAME_TEXTFIELD_ID, Input.ComponentType.TEXT_FIELD).setSingleStringValue(name);
        return this;
    }

    public SaveConfigurationWizard setAsDefaultFor(String value){
        wizard.getComponent(DEFAULTT_VIEW_COMBOBOX_ID, Input.ComponentType.COMBOBOX).setSingleStringValue(value);
        return this;
    }

    public NewInventoryViewPage save(){
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickActionById(SAVE_AS_NEW);
        return new NewInventoryViewPage(driver);
    }
}
