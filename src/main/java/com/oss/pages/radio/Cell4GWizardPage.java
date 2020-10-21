package com.oss.pages.radio;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class Cell4GWizardPage extends BasePage {

    public Cell4GWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard cell4GWizard = Wizard.createByComponentId(driver, wait, "cell-4g-wizard");

    public void accept() {
        cell4GWizard.clickAccept();
    }

    public void setComponentValue(String componentId, String value, Input.ComponentType componentType) {
        cell4GWizard.setComponentValue(componentId, value, componentType);
    }

    public String getComponentValue(String componentId, Input.ComponentType componentType) {
        Input value = cell4GWizard.getComponent(componentId, componentType);
        return value.getStringValue();
    }

    public Input getComponent(String componentId, Input.ComponentType componentType) {
        return cell4GWizard.getComponent(componentId, componentType);
    }

    @Step("Create Cell4G with mandatory fields (Name, eNodeB, Cell Id, Carrier) filled in")
    public void createCell4G(String cell4GName, String eNodeBName, String cellId, String carrier4G) {
        setComponentValue("name", cell4GName, Input.ComponentType.TEXT_FIELD);
        setComponentValue("eNodeBId", eNodeBName, Input.ComponentType.SEARCH_FIELD);
        setComponentValue("cellId", cellId, Input.ComponentType.TEXT_FIELD);
        getComponent("carrier", Input.ComponentType.COMBOBOX).setSingleStringValueContains(carrier4G);
    }

    @Step("Type description")
    public Cell4GWizardPage typeDescription(String description) {
        setComponentValue("description", description, Input.ComponentType.TEXT_FIELD);
        return this;
    }
}
