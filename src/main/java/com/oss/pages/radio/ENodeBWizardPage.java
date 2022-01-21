package com.oss.pages.radio;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class ENodeBWizardPage extends BasePage {

    private static final String E_NODE_B_WIZARD_DATA_ATTRIBUTE_NAME = "e-node-b-wizard";
    private static final String E_NODE_B_NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String E_NODE_B_ID_DATA_ATTRIBUTE_NAME = "eNodeBId";
    private static final String E_NODE_B_MODEL_DATA_ATTRIBUTE_NAME = "eNodeBModel";
    private static final String E_NODE_B_MCC_MNC_PRIMARY_DATA_ATTRIBUTE_NAME = "primaryMccMnc";
    private static final String E_NODE_B_MCC_MNC_ADDITIONAL_DATA_ATTRIBUTE_NAME = "additionalMccMnc";
    private static final String E_NODE_B_ADMINISTRATIVE_STATE_DATA_ATTRIBUTE_NAME = "administrativeState";
    private static final String E_NODE_B_LOCATION_DATA_ATTRIBUTE_NAME = "location_OSF";
    private static final String E_NODE_B_DESCRIPTION_DATA_ATTRIBUTE_NAME = "description";
    private static final String E_NODE_B_HOST_OPERATOR_DATA_ATTRIBUTE_NAME = "hostOperator";
    private static final String E_NODE_B_USER_LABEL_DATA_ATTRIBUTE_NAME = "userLabel";
    private static final String E_NODE_B_MNS_DATA_ATTRIBUTE_NAME = "NMS_OSF";

    public ENodeBWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Accept button")
    public void accept() {
        getENodeBWizard().clickAccept();
    }

    @Step("Create eNodeB with mandatory fields (Name, eNodeB ID, eNodeB Model, MCC-MNC Primary) filled in")
    public void createENodeB(String eNodeBName, String eNodeBId, String eNodeBModel, String MCCMNCPrimary) {
        setName(eNodeBName);
        setENodeBId(eNodeBId);
        setENodeBModel(eNodeBModel);
        setMccMncPrimary(MCCMNCPrimary);
        accept();
    }

    @Step("Set name")
    public ENodeBWizardPage setName(String eNodeBName) {
        getENodeBWizard().setComponentValue(E_NODE_B_NAME_DATA_ATTRIBUTE_NAME, eNodeBName, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set eNodeB Id")
    public ENodeBWizardPage setENodeBId(String eNodeBId) {
        getENodeBWizard().setComponentValue(E_NODE_B_ID_DATA_ATTRIBUTE_NAME, eNodeBId, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    @Step("Set eNodeB Model")
    public ENodeBWizardPage setENodeBModel(String eNodeBModel) {
        getENodeBWizard().setComponentValue(E_NODE_B_MODEL_DATA_ATTRIBUTE_NAME, eNodeBModel, Input.ComponentType.COMBOBOX);
        return this;
    }

    @Step("Set MCC-MNC Primary")
    public ENodeBWizardPage setMccMncPrimary(String MCCMNCPrimary) {
        getENodeBWizard().setComponentValue(E_NODE_B_MCC_MNC_PRIMARY_DATA_ATTRIBUTE_NAME, MCCMNCPrimary, Input.ComponentType.COMBOBOX);
        return this;
    }

    @Step("Set description")
    public ENodeBWizardPage setDescription(String description) {
        getENodeBWizard().setComponentValue(E_NODE_B_DESCRIPTION_DATA_ATTRIBUTE_NAME, description, Input.ComponentType.TEXT_FIELD);
        return this;
    }

    private Wizard getENodeBWizard() {
        return Wizard.createByComponentId(driver, wait, E_NODE_B_WIZARD_DATA_ATTRIBUTE_NAME);
    }

}