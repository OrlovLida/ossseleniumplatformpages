package com.oss.pages.radio;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.physical.LocationWizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class ENodeBWizardPage extends BasePage {

    public ENodeBWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard eNodeBWizard = Wizard.createByComponentId(driver, wait, "e-node-b-wizard");

    public void accept() {
        eNodeBWizard.clickAccept();
    }

    public void setComponentValue(String componentId, String value, Input.ComponentType componentType) {
        eNodeBWizard.setComponentValue(componentId, value, componentType);
    }

    public String getComponentValue(String componentId, Input.ComponentType componentType) {
        Input value = eNodeBWizard.getComponent(componentId, componentType);
        return value.getStringValue();
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return eNodeBWizard.getComponent(componentId, componentType);
    }

    @Step("Create eNodeB with mandatory fields (Name, eNodeB ID, eNodeB Model, MCC-MNC Primary) filled in")
    public void createENodeB(String eNodeBName, String eNodeBId, String eNodeBModel, String MCCMNCPrimary) {
        setComponentValue("name", eNodeBName, Input.ComponentType.TEXT_FIELD);
        setComponentValue("eNodeBId", eNodeBId, Input.ComponentType.TEXT_FIELD);
        setComponentValue("eNodeBModel", eNodeBModel, Input.ComponentType.COMBOBOX);
        setComponentValue("primaryMccMnc", MCCMNCPrimary, Input.ComponentType.COMBOBOX);
    }

    @Step("Type description")
    public ENodeBWizardPage typeDescription(String description) {
        setComponentValue("description", description, Input.ComponentType.TEXT_FIELD);
        return this;
    }

}
