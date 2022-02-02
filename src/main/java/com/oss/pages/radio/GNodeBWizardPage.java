package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class GNodeBWizardPage extends BasePage {

    private static final String G_NODE_B_WIZARD_DATA_ATTRIBUTE_NAME = "gnodeb-wizard-id";
    private static final String G_NODE_B_NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String G_NODE_B_ID_DATA_ATTRIBUTE_NAME = "gNodeBId";
    private static final String G_NODE_B_MODEL_DATA_ATTRIBUTE_NAME = "gNodeBModel";
    private static final String G_NODE_B_MCC_MNC_DATA_ATTRIBUTE_NAME = "mccMnc";
    private static final String G_NODE_B_SEARCH_MCC_MNC_DATA_ATTRIBUTE_NAME = "mccMnc-dropdown-search";
    private static final String G_NODE_B_ADMINISTRATIVE_STATE_DATA_ATTRIBUTE_NAME = "administrativeState";
    private static final String G_NODE_B_LOCATION_DATA_ATTRIBUTE_NAME = "location_OSF";
    private static final String G_NODE_B_DESCRIPTION_DATA_ATTRIBUTE_NAME = "description";
    private static final String G_NODE_B_HOST_OPERATOR_DATA_ATTRIBUTE_NAME = "hostOperator";
    private static final String G_NODE_B_USER_LABEL_DATA_ATTRIBUTE_NAME = "userLabel";
    private static final String G_NODE_B_MNS_DATA_ATTRIBUTE_NAME = "NMS_OSF";

    public GNodeBWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Accept button")
    public void accept() {
        getGNodeBWizard().clickAccept();
    }

    @Step("Create gNodeB with mandatory fields (Name, gNodeB ID, gNodeB Model, MCC-MNC Primary) filled in")
    public void createGNodeB(String gNodeBName, String gNodeBId, String gNodeBModel, String MCCMNCPrimary) {
        setName(gNodeBName);
        setId(gNodeBId);
        setModel(gNodeBModel);
        setMccMncPrimary(MCCMNCPrimary);
        accept();
    }

    @Step("Set name")
    public GNodeBWizardPage setName(String gNodeBName) {
        getGNodeBWizard().setComponentValue(G_NODE_B_NAME_DATA_ATTRIBUTE_NAME, gNodeBName, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set gNodeB Id")
    public void setId(String gNodeBId) {
        getGNodeBWizard().setComponentValue(G_NODE_B_ID_DATA_ATTRIBUTE_NAME, gNodeBId, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set gNodeB Model")
    public void setModel(String gNodeBModel) {
        getGNodeBWizard().setComponentValue(G_NODE_B_MODEL_DATA_ATTRIBUTE_NAME, gNodeBModel, Input.ComponentType.COMBOBOX);
    }

    @Step("Set MCC-MNC Primary")
    public void setMccMncPrimary(String MCCMNCPrimary) {
        getGNodeBWizard().getComponent(G_NODE_B_MCC_MNC_DATA_ATTRIBUTE_NAME, Input.ComponentType.COMBOBOX).click();
        getGNodeBWizard().setComponentValue(G_NODE_B_SEARCH_MCC_MNC_DATA_ATTRIBUTE_NAME, MCCMNCPrimary, Input.ComponentType.COMBOBOX);
    }

    @Step("Set description")
    public GNodeBWizardPage setDescription(String description) {
        getGNodeBWizard().setComponentValue(G_NODE_B_DESCRIPTION_DATA_ATTRIBUTE_NAME, description, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    private Wizard getGNodeBWizard() {
        return Wizard.createByComponentId(driver, wait, G_NODE_B_WIZARD_DATA_ATTRIBUTE_NAME);
    }

}