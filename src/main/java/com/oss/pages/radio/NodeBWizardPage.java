package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class NodeBWizardPage extends BasePage {

    private static final String NODE_B_WIZARD_DATA_ATTRIBUTE_NAME = "node-b-wizard";
    private static final String NODE_B_NAME_DATA_ATTRIBUTE_NAME = "name";
    private static final String NODE_B_ID_DATA_ATTRIBUTE_NAME = "nodeBId";
    private static final String NODE_B_MODEL_DATA_ATTRIBUTE_NAME = "nodeBModel";
    private static final String NODE_B_RNC_DATA_ATTRIBUTE_NAME = "controller";
    private static final String NODE_B_LOCATION_DATA_ATTRIBUTE_NAME = "location";
    private static final String NODE_B_DESCRIPTION_DATA_ATTRIBUTE_NAME = "description";
    private static final String NODE_B_USER_LABEL_DATA_ATTRIBUTE_NAME = "userLabel";
    private static final String NODE_B_NMS_DATA_ATTRIBUTE_NAME = "NMS";

    public NodeBWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Create NodeB with mandatory fields (Name, NodeB ID, NodeB Model, RNC) filled in")
    public void createNodeB(String nodeBName, String nodeBId, String nodeBModel, String rnc) {
        setName(nodeBName);
        setNodeBId(nodeBId);
        setNodeBModel(nodeBModel);
        setRNC(rnc);
        accept();
    }

    @Step("Click Accept button")
    public void accept() {
        getNodeBWizard().clickAccept();
    }

    @Step("Set name")
    public NodeBWizardPage setName(String eNodeBName) {
        getNodeBWizard().setComponentValue(NODE_B_NAME_DATA_ATTRIBUTE_NAME, eNodeBName);
        return this;
    }

    @Step("Set NodeB Id")
    public void setNodeBId(String nodeBId) {
        getNodeBWizard().setComponentValue(NODE_B_ID_DATA_ATTRIBUTE_NAME, nodeBId);
    }

    @Step("Set NodeB Model")
    public void setNodeBModel(String nodeBModel) {
        getNodeBWizard().setComponentValue(NODE_B_MODEL_DATA_ATTRIBUTE_NAME, nodeBModel);
    }

    @Step("Set RNC")
    public void setRNC(String rnc) {
        getNodeBWizard().setComponentValue(NODE_B_RNC_DATA_ATTRIBUTE_NAME, rnc);
    }

    @Step("Set description")
    public NodeBWizardPage setDescription(String description) {
        getNodeBWizard().setComponentValue(NODE_B_DESCRIPTION_DATA_ATTRIBUTE_NAME, description);
        return this;
    }

    private Wizard getNodeBWizard() {
        return Wizard.createByComponentId(driver, wait, NODE_B_WIZARD_DATA_ATTRIBUTE_NAME);
    }
}
