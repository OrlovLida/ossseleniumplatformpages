package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.treewidget.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class ConnectionWizardPage extends BasePage {

    public ConnectionWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard wizard = Wizard.createPopupWizard(driver, wait);

    @Step("Set name")
    public void setName(String name) {
        Input input = wizard.getComponent("trailNameComponent", TEXT_FIELD);
        input.setSingleStringValue(name);
    }

    @Step("Click Next button")
    public void clickNext() {
        wizard.clickNext();
    }

    @Step("Click Accept button")
    public void clickAccept() {
        wizard.clickAccept();
    }

    @Step("")
    public void selectConnectionTermination(int position) {
        TreeWidget tree = TreeWidget.createByClass(driver, "tree-component", wait);
        tree.selectNodeByPosition(position);
    }

    @Step("Terminate Card/Component")
    public void terminateCardComponent(String name) {
        Input input = wizard.getComponent("terminationFormCardComponent", SEARCH_FIELD);
        input.setSingleStringValue(name);
    }

    @Step("Terminate Port")
    public void terminatePort(String name) {
        Input input = wizard.getComponent("terminationFormPortComponent", SEARCH_FIELD);
        input.setSingleStringValue(name);
    }

    @Step("Terminate Termination Port")
    public void terminateTerminationPort(String name) {
        Input input = wizard.getComponent("terminationFormPointComponent", SEARCH_FIELD);
        input.setSingleStringValue(name);
    }
}