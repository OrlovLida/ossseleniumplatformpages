package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class CreateCableWizardPage extends BasePage {

    public CreateCableWizardPage(WebDriver driver) { super(driver); }

    @Step("Choose Cable model from the search box")
    public void setCableModel(String cableModel) {
        getCableCreationWizard().setComponentValue("model", cableModel, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Insert name of a Cable")
    public void setCableName(String cableName) {
        getCableCreationWizard().setComponentValue("name", cableName, Input.ComponentType.TEXT_FIELD);
    }

    public void clickAccept() { getCableCreationWizard().clickAccept(); }

    private Wizard getCableCreationWizard() {
        return Wizard.createByComponentId(driver, wait, "Popup");
    }
}