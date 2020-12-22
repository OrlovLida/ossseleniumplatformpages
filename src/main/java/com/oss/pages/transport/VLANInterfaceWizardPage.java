package com.oss.pages.transport;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class VLANInterfaceWizardPage extends BasePage {

    public VLANInterfaceWizardPage(WebDriver driver) {
        super(driver);
    }

    private static final String TYPE_ID = "typeSelectionApp";
    private static final String SUBINTERFACE_ID = "subinterface-id-uid";
    private static final String WIZARD_ID = "vlanInterfaceWizard";
    private static final String ACCEPT_ID = "vlanInterfaceWizardApp-finish";
    private static final String NEXT_ID = "vlanInterfaceWizardApp-next";

    @Step("Set VLAN Interface type to {type}")
    public void setType(String type) {
        getWizard().setComponentValue(TYPE_ID, type, ComponentType.COMBOBOX);
    }

    @Step("Set Subinterface ID to {subinterfaceId}")
    public void setSubinterfaceId(String subinterfaceId) {
        getWizard().setComponentValue(SUBINTERFACE_ID, subinterfaceId, ComponentType.NUMBER_FIELD);
    }

    //TODO correct
    @Step("Set Location to {location}")
    public void setLocation(String location) {

    }

    @Step("Next")
    public void clickNext() {
        getWizard().clickActionById(NEXT_ID);
    }

    @Step("Accept")
    public void clickAccept() {
        getWizard().clickActionById(ACCEPT_ID);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}
