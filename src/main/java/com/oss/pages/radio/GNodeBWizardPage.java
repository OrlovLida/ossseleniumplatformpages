package com.oss.pages.radio;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
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
    private static final String G_NODE_B_LOCATION_DATA_ATTRIBUTE_NAME = "location";
    private static final String G_NODE_B_DESCRIPTION_DATA_ATTRIBUTE_NAME = "description";
    private static final String G_NODE_B_HOST_OPERATOR_DATA_ATTRIBUTE_NAME = "hostOperator";
    private static final String G_NODE_B_USER_LABEL_DATA_ATTRIBUTE_NAME = "userLabel";
    private static final String G_NODE_B_MNS_DATA_ATTRIBUTE_NAME = "NMS";

    public GNodeBWizardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Accept button")
    public void accept() {
        getGNodeBWizard().clickAccept();
    }

    @Step("Create gNodeB with mandatory fields (Name, gNodeB ID, gNodeB Model, MCC-MNC Primary) filled in")
    public void createGNodeB(String gNodeBName, String gNodeBId, String gNodeBModel, String mccMncPrimary) {
        setName(gNodeBName);
        setId(gNodeBId);
        setModel(gNodeBModel);
        setMccMncPrimary(mccMncPrimary);
        DelayUtils.waitForPageToLoad(driver, wait);
        accept();
    }

    @Step("Set name")
    public GNodeBWizardPage setName(String gNodeBName) {
        getGNodeBWizard().setComponentValue(G_NODE_B_NAME_DATA_ATTRIBUTE_NAME, gNodeBName);
        return this;
    }

    @Step("Set gNodeB Id")
    public void setId(String gNodeBId) {
        getGNodeBWizard().setComponentValue(G_NODE_B_ID_DATA_ATTRIBUTE_NAME, gNodeBId);
    }

    @Step("Set gNodeB Model")
    public void setModel(String gNodeBModel) {
        getGNodeBWizard().setComponentValue(G_NODE_B_MODEL_DATA_ATTRIBUTE_NAME, gNodeBModel);
    }

    @Step("Set MCC-MNC Primary")
    public void setMccMncPrimary(String mccMncPrimary) {
        getGNodeBWizard().setComponentValue(G_NODE_B_MCC_MNC_DATA_ATTRIBUTE_NAME, mccMncPrimary);
    }

    @Step("Set description")
    public GNodeBWizardPage setDescription(String description) {
        getGNodeBWizard().setComponentValue(G_NODE_B_DESCRIPTION_DATA_ATTRIBUTE_NAME, description);
        return this;
    }

    private Wizard getGNodeBWizard() {
        return Wizard.createByComponentId(driver, wait, G_NODE_B_WIZARD_DATA_ATTRIBUTE_NAME);
    }

}