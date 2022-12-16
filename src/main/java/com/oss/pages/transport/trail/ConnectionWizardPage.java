package com.oss.pages.transport.trail;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.tree.TreeComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.CHECKBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class ConnectionWizardPage extends BasePage {

    public static final String WIZARD_ID = "trailWizardId_prompt-card";
    private static final String NAME_ID = "trailNameComponent";
    private static final String DESCRIPTION_ID = "trailDescriptionComponent";
    private static final String TERMINATE_NETWORK_ELEMENT = "terminationFormDeviceComponent";
    private static final String TERMINATE_CARD_ID = "terminationFormCardComponent";
    private static final String TERMINATE_PORT_ID = "terminationFormPortComponent";
    private static final String TERMINATE_TP_ID = "terminationFormPointComponent";
    private static final String CAPACITY_UNIT_ID = "trailCapacityUnitComponent";
    private static final String CAPACITY_VALUE_ID = "trailCapacityValueComponent";
    private static final String ETHERNET_LINK_SPEED_ID = "oss.transport.trail.type.Ethernet Link.Speed";
    private static final String ADDRESS_TO_OPPOSITE_INTERFACE_ID = "oss.transport.trail.type.IP Link.AssignIPHostAddressComboboxComponent";
    private static final String NONEXISTENT_CARD_VALUE = "No Card/Component";

    public ConnectionWizardPage(WebDriver driver) {
        super(driver);
    }

    public Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("Set name = {name}")
    public void setName(String name) {
        getWizard().setComponentValue(NAME_ID, name);
    }

    @Step("Set description = {description}")
    public void setDescription(String description) {
        getWizard().setComponentValue(DESCRIPTION_ID, description);
    }

    @Step("Set capacity unit = {value}")
    public void setCapacityUnit(String value) {
        getWizard().setComponentValue(CAPACITY_UNIT_ID, value, COMBOBOX);
    }

    @Step("Set capacity value = {value}")
    public void setCapacityValue(String value) {
        getWizard().setComponentValue(CAPACITY_VALUE_ID, value, TEXT_FIELD);
    }

    @Step("Set ethernet link speed = {value}")
    public void setEthernetLinkSpeed(String value) {
        getWizard().setComponentValue(ETHERNET_LINK_SPEED_ID, value, COMBOBOX);
    }

    @Step("Click Next button")
    public void clickNext() {
        getWizard().clickNext();
    }

    @Step("Click Accept button")
    public void clickAccept() {
        getWizard().clickAccept();
    }

    @Step("Select connection termination by dataPath = {dataPath}")
    public void selectConnectionTermination(String dataPath) {
        TreeComponent treeComponent = getWizard().getTreeComponent();
        treeComponent.toggleNodeByPath(dataPath);
        getWizard().waitForWizardToLoad();
    }

    @Step("Terminate Card/Component")
    public void terminateCardComponent(String value) {
        getWizard().setComponentValue(TERMINATE_CARD_ID, value, SEARCH_FIELD);
    }

    @Step("Set nonexistent card")
    public void setNonexistentCard() {
        Input searchField = getWizard().getComponent(TERMINATE_CARD_ID);
        searchField.setSingleStringValueContains(NONEXISTENT_CARD_VALUE);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Terminate Port")
    public void terminatePort(String value) {
        getWizard().setComponentValue(TERMINATE_PORT_ID, value, SEARCH_FIELD);
    }

    @Step("Get Network Element Termination")
    public String getNetworkElementTermination() {
        return getWizard().getComponent(TERMINATE_NETWORK_ELEMENT).getStringValue();
    }

    @Step("Terminate Termination Port")
    public void terminateTerminationPort(String value) {
        getWizard().setComponentValue(TERMINATE_TP_ID, value, SEARCH_FIELD);
    }

    @Step("Assign IP Host Address to the opposite interface = {value}")
    public void assignAddressToOpositeInteface(boolean value) {
        getWizard().setComponentValue(ADDRESS_TO_OPPOSITE_INTERFACE_ID, String.valueOf(value), CHECKBOX);
    }

    public void setCheckbox(String componentId, Boolean value) {
        getWizard().setComponentValue(componentId, value.toString());
    }

    @Step("Choose termination in tree - {terminationType}")
    public void chooseTerminationType(ConnectionWizardPage.TerminationType terminationType) {
        TreeComponent treeComponent = getWizard().getTreeComponent();
        treeComponent.toggleNodeByPath(terminationType.getLabel());
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public enum TerminationType {
        START("1_1"),
        END("1_2");

        private final String label;

        private TerminationType(String value) {
            label = value;
        }

        private String getLabel() {
            return label;
        }
    }
}