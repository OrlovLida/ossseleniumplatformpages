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

    public void accept() {
        Wizard eNodeBWizard = Wizard.createByComponentId(driver, wait, "e-node-b-wizard");
        eNodeBWizard.clickAccept();
    }

    @Step("Create eNodeB with mandatory fields (Name, eNodeB ID, eNodeB Model, MCC-MNC Primary) filled in")
    public void createENodeB(String eNodeBName, String eNodeBId, String eNodeBModel, String MCCMNCPrimary) {
        Wizard eNodeBWizard = Wizard.createByComponentId(driver, wait, "e-node-b-wizard");
        Input nameField = eNodeBWizard.getComponent("name", Input.ComponentType.TEXT_FIELD);
        nameField.setSingleStringValue(eNodeBName);
        Input eNodeBIdField = eNodeBWizard.getComponent("eNodeBId", Input.ComponentType.TEXT_FIELD);
        eNodeBIdField.setSingleStringValue(eNodeBId);
        Input eNodeBModelField = eNodeBWizard.getComponent("eNodeBModel", Input.ComponentType.COMBOBOX);
        eNodeBModelField.setSingleStringValue(eNodeBModel);
        Input MCCMNCPrimaryField = eNodeBWizard.getComponent("primaryMccMnc", Input.ComponentType.COMBOBOX);
        MCCMNCPrimaryField.setSingleStringValue(MCCMNCPrimary);
    }

    @Step("Type description")
    public ENodeBWizardPage typeDescription(String description) {
        Wizard eNodeBWizard = Wizard.createByComponentId(driver, wait, "e-node-b-wizard");
        Input descriptionField = eNodeBWizard.getComponent("description", Input.ComponentType.TEXT_FIELD);
        descriptionField.setSingleStringValue(description);
        return this;
    }

}
