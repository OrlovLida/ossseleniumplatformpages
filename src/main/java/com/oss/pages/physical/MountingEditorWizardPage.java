package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class MountingEditorWizardPage extends BasePage {

    private static final String NAME = "name-id";
    private static final String PRECISE_LOCATION = "precise-location-id";
    private static final String MODEL = "model-id";
    private static final String WIZARD_ID = "mounting-editor-widget-id";

    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    public MountingEditorWizardPage(WebDriver driver) {
        super(driver);
    }

    public TableComponent getTable() {
        return TableComponent.create(driver, wait, WIZARD_ID);
    }

    @Step("Set Name")
    public void setName(String name) {
        wizard.setComponentValue(NAME, name, ComponentType.TEXT_FIELD);
    }

    @Step("Set Precise Location")
    public void setPreciseLocation(String preciseLocation) {
        wizard.getComponent(PRECISE_LOCATION).setSingleStringValueContains(preciseLocation);
    }

    @Step("Set Name")
    public void setModel(String model) {
        wizard.setComponentValue(MODEL, model, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Select second row in devices to mount table")
    public void clickCheckbox() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTable().selectAll();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click Next button")
    public void clickNext() {
        wizard.clickNext();
    }

    @Step("Click Accept button")
    public void clickAccept() {
        wizard.clickAccept();
    }
}
