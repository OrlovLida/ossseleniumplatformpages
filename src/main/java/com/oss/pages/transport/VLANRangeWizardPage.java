package com.oss.pages.transport;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;

import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class VLANRangeWizardPage extends BasePage {

    public VLANRangeWizardPage(WebDriver driver) {
        super(driver);
    }
    private static final String VLAN_RANGE_WIZARD_URL = "%s/#/view/transport/ip/ethernet/vlan-range/create?";

    @Step("Set Name")
    public VLANRangeWizardPage setName(String name){
        getWizard().setComponentValue("VLAN_RANGE_NAME_ATTRIBUTE_ID", name, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set Range")
    public VLANRangeWizardPage setRange(String range){
        getWizard().setComponentValue("VLAN_RANGE_VLAN_ID_RANGE_ATTRIBUTE_ID", range, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set Description")
    public VLANRangeWizardPage setDescription(String description){
        getWizard().setComponentValue("VLAN_RANGE_DESCRIPTION_ATTRIBUTE_ID", description, Input.ComponentType.TEXT_AREA);
        return this;
    }

    @Step("Save")
    public VLANRangeWizardPage save(){
        getWizard().clickSave();
        return this;
    }

    public Wizard getWizard() { return Wizard.createByComponentId(driver, wait, "Popup"); }

}
