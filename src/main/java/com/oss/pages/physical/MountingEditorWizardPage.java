package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Widget;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MountingEditorWizardPage extends BasePage {

    private static final String NAME = "name-id";
    private static final String PRECISE_LOCATION = "precise-location-id";
    private static final String MODEL = "model-id";

    private Wizard wizard = Wizard.createByComponentId(driver, wait, "mounting-editor-modal-id");
    private TableWidget mainTable;

    public MountingEditorWizardPage(WebDriver driver) {
        super(driver);
    }

    public TableWidget getTable() {
        if (mainTable == null) {
            Widget.waitForWidget(wait, "TableWidget");
            mainTable = TableWidget.create(driver, "TableWidget", wait);
            DelayUtils.waitForPageToLoad(driver, wait);
        }
        return mainTable;
    }

    @Step("Set Name")
    public void setName(String name) {
        Input ipAddressComponent = wizard.getComponent(NAME, Input.ComponentType.TEXT_FIELD);
        ipAddressComponent.clear();
        ipAddressComponent.setSingleStringValue(name);
    }

    @Step("Set Precise Location")
    public void setPreciseLocation(String preciseLocation) {
        Input ipAddressComponent = wizard.getComponent(PRECISE_LOCATION, Input.ComponentType.SEARCH_FIELD);
        ipAddressComponent.clear();
        ipAddressComponent.setSingleStringValue(preciseLocation);
    }

    @Step("Set Name")
    public void setModel(String model) {
        Input ipAddressComponent = wizard.getComponent(MODEL, Input.ComponentType.SEARCH_FIELD);
        ipAddressComponent.clear();
        ipAddressComponent.setSingleStringValue(model);
    }

    @Step("Select second row in devices to mount table")
    public void clickCheckbox() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getTable().selectRow(1);
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
