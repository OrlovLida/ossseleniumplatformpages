package com.oss.pages.radio;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class Cell4GWizardPage extends BasePage {

    public Cell4GWizardPage(WebDriver driver) {
        super(driver);
    }

    public void accept() {
        Wizard eNodeBWizard = Wizard.createByComponentId(driver, wait, "cell-4g-wizard");
        eNodeBWizard.clickAccept();
    }

    @Step("Create Cell4G with mandatory fields (Name, eNodeB, Cell Id, Carrier) filled in")
    public void createCell4G(String cell4GName, String eNodeBName, String cellId, String carrier4G) {
        Wizard cell4GWizard = Wizard.createByComponentId(driver, wait, "cell-4g-wizard");
        Input nameField = cell4GWizard.getComponent("name", Input.ComponentType.TEXT_FIELD);
        nameField.setSingleStringValue(cell4GName);
        Input eNodeBField = cell4GWizard.getComponent("eNodeBId", Input.ComponentType.SEARCH_FIELD);
        eNodeBField.setSingleStringValue(eNodeBName);
        Input cellIdField = cell4GWizard.getComponent("cellId", Input.ComponentType.TEXT_FIELD);
        cellIdField.setSingleStringValue(cellId);
        Input carrierField = cell4GWizard.getComponent("carrier", Input.ComponentType.COMBOBOX);
        carrierField.setSingleStringValueContains(carrier4G);
    }

    @Step("Type description")
    public Cell4GWizardPage typeDescription(String description) {
        Wizard cell4GWizard = Wizard.createByComponentId(driver, wait, "cell-4g-wizard");
        Input descriptionField = cell4GWizard.getComponent("description", Input.ComponentType.TEXT_FIELD);
        descriptionField.setSingleStringValue(description);
        return this;
    }
}
