package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.framework.widgets.tree.TreeWidget;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.CHECKBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class ConnectionWizardPage extends BasePage {

    private static final String WIZARD_ID = "trailWizardId";
    private static final String NAME_ID = "trailNameComponent";
    private static final String TERMINATE_CARD_ID = "terminationFormCardComponent";
    private static final String TERMINATE_PORT_ID = "terminationFormPortComponent";
    private static final String TERMINATE_TP_ID = "terminationFormPointComponent";
    private static final String CAPACITY_UNIT_ID = "trailCapacityUnitComponent";
    private static final String CAPACITY_VALUE_ID = "trailCapacityValueComponent";
    private static final String ETHERNET_LINK_SPEED_ID = "oss.transport.trail.type.Ethernet Link.Speed";
    private static final String ADDRESS_TO_OPPOSITE_INTERFACE_ID = "oss.transport.trail.type.IP Link.AssignIPHostAddressComboboxComponent";

    public ConnectionWizardPage(WebDriver driver) {
        super(driver);
    }

    private Wizard wizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);

    @Step("Set name = {name}")
    public void setName(String name) {
        wizard.setComponentValue(NAME_ID, name, TEXT_FIELD);
    }

    @Step("Set capacity unit = {value}")
    public void setCapacityUnit(String value) {
        wizard.setComponentValue(CAPACITY_UNIT_ID, value, COMBOBOX);
    }

    @Step("Set capacity value = {value}")
    public void setCapacityValue(String value) {
        wizard.setComponentValue(CAPACITY_VALUE_ID, value, TEXT_FIELD);
    }

    @Step("Set ethernet link speed = {value}")
    public void setEthernetLinkSpeed(String value) {
        wizard.setComponentValue(ETHERNET_LINK_SPEED_ID, value, COMBOBOX);
    }

    @Step("Click Next button")
    public void clickNext() {
        wizard.clickNext();
    }

    @Step("Click Accept button")
    public void clickAccept() {
        wizard.clickAccept();
    }

    @Step("Select connection termination by position = {position}")
    public void selectConnectionTermination(int position) {
        TreeWidget tree = TreeWidget.createByClass(driver, "tree-component", wait);
        tree.selectNodeByPosition(position);
    }

    @Step("Terminate Card/Component")
    public void terminateCardComponent(String value) {
        wizard.setComponentValue(TERMINATE_CARD_ID, value, SEARCH_FIELD);
    }

    @Step("Terminate Port")
    public void terminatePort(String value) {
        wizard.setComponentValue(TERMINATE_PORT_ID, value, SEARCH_FIELD);
    }

    @Step("Terminate Termination Port")
    public void terminateTerminationPort(String value) {
        wizard.setComponentValue(TERMINATE_TP_ID, value, SEARCH_FIELD);
    }

    @Step("Assign IP Host Address to the opposite interface = {value}")
    public void assignAddressToOpositeInteface(boolean value) {
        wizard.setComponentValue(ADDRESS_TO_OPPOSITE_INTERFACE_ID, String.valueOf(value), CHECKBOX);
    }
}