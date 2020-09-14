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
    private Wizard wizard;

    @Step("Open VLAN Range Wizard")
    public static VLANRangeWizardPage goToVLANRangeWizardPage(WebDriver driver, String basicURL){
        driver.get(String.format(VLAN_RANGE_WIZARD_URL, basicURL));
        return new VLANRangeWizardPage(driver);
    }

    @Step("Set Name")
    public VLANRangeWizardPage SetName(){
        DelayUtils.waitForPageToLoad(driver, wait);
        setValueOnTextType("VLAN_RANGE_NAME_ATTRIBUTE_ID", Input.ComponentType.TEXT_FIELD, "CreateVLANRangeSeleniumTest1");
        return this;
    }

    @Step("Set Range")
    public VLANRangeWizardPage SetRange(){
        setValueOnTextType("VLAN_RANGE_VLAN_ID_RANGE_ATTRIBUTE_ID", Input.ComponentType.TEXT_FIELD, "1, 3, 5-10");
        return this;
    }

    @Step("Set Description")
    public VLANRangeWizardPage SetDescription(){
        setValueOnTextType("VLAN_RANGE_DESCRIPTION_ATTRIBUTE_ID", Input.ComponentType.TEXT_AREA, "Description1");

        return this;
    }

    @Step("Save")
    public VLANRangeWizardPage Save(){
        wizard.clickSave();
        return this;
    }

    public Wizard getWizard() {
        if (wizard == null) {
            wizard = Wizard.createWizard(driver, wait);
        }
        return wizard;
    }

    private void setValueOnTextType(String componentId, Input.ComponentType componentType, String value) {
        DelayUtils.sleep();
        getWizard().getComponent(componentId, componentType).clearByAction();
        getWizard().getComponent(componentId, componentType).setSingleStringValue(value);
    }


    public Input getComponent(String componentId, Input.ComponentType componentType) {
        Input input = ComponentFactory.create(componentId, componentType, this.driver, this.wait);
        return input;
    }

}
