package com.oss.pages.transport;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class TrafficPolicyWizardPage extends BasePage {

    public TrafficPolicyWizardPage(WebDriver driver) {
        super(driver);
    }

    private static final String WIZARD_ID = "trafficPolicyWizardApp";
    private static final String EDIT_WIZARD_ID = "trafficPolicyModificationWizard_prompt-card";
    private static final String ADD_ENTRY_WIZARD_ID = "traffic_policy_entry_wizard_form_id";
    private static final String TRAFFIC_POLICY_NAME_ID = "uid-name";
    private static final String TRAFFIC_POLICY_DESCRIPTION_ID = "uid-description";
    private static final String TRAFFIC_OBJECT_FIELD_ID = "trafficObject-form";
    private static final String MATCH_FIELD_ID = "match-form";
    private static final String BANDWIDTH_FIELD_ID = "bandwidth-form";
    private static final String QUEUE_LIMIT_FIELD_ID = "queueLimit-form";
    private static final String FAIR_QUEUE_FIELD_ID = "fairQueue-form";
    private static final String PRIORITY_FIELD_ID = "priority-form";
    private static final String RANDOM_DETECT_CHECKBOX_ID = "randomDetect-form";
    private static final String IP_PRECEDENCE_FIELD_ID = "IpPrecedence-form";
    private static final String MPLS_EXPERIMENTAL_FIELD_ID = "mplsExperimental-form";
    private static final String SHAPE_FIELD_ID = "shape-form";
    private static final String SHAPE_RATE_FIELD_ID = "shapeRate-form";
    private static final String IP_DSCP_FIELD_ID = "dscp-form";
    private static final String CIR_INGRESS_FIELD_ID = "cir-ingress-form";
    private static final String CIR_EGRESS_FIELD_ID = "cir-egress-form";
    private static final String PIR_INGRESS_FIELD_ID = "pir-ingress-form";
    private static final String PIR_EGRESS_FIELD_ID = "pir-egress-form";
    private static final String SAVE_BUTTON_ID = "buttons_app_id-1";

    @Step("Create Traffic Policy with Name {name}")
    public TrafficPolicyWizardPage createTrafficPolicy(String name) {
        waitForPageToLoad();
        getWizard().setComponentValue(TRAFFIC_POLICY_NAME_ID, name);
        waitForPageToLoad();
        getWizard().clickAccept();
        return this;
    }

    @Step("Edit Traffic Policy and set name {newName} and description {description}")
    public TrafficPolicyWizardPage editTrafficPolicy(String newName, String description) {
        waitForPageToLoad();
        getEditWizard().setComponentValue(TRAFFIC_POLICY_NAME_ID, newName);
        waitForPageToLoad();
        getEditWizard().setComponentValue(TRAFFIC_POLICY_DESCRIPTION_ID, description);
        waitForPageToLoad();
        getEditWizard().clickSave();
        return this;
    }

    @Step("Add Entry to Traffic Policy")
    public TrafficPolicyWizardPage addEntryToTrafficPolicy() {

        return this;
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    private Wizard getEditWizard() {
        return Wizard.createByComponentId(driver, wait, EDIT_WIZARD_ID);
    }

    private Wizard getEntryWizard() {
        return Wizard.createByComponentId(driver, wait, ADD_ENTRY_WIZARD_ID);
    }

    public void setTrafficObject(String trafficObject) {
        getEntryWizard().setComponentValue(TRAFFIC_OBJECT_FIELD_ID, trafficObject);
    }

    public void setMatch(String match) {
        getEntryWizard().setComponentValue(MATCH_FIELD_ID, match);
    }

    public void setBandwidth(String bandwidth) {
        getEntryWizard().setComponentValue(BANDWIDTH_FIELD_ID, bandwidth);
    }

    public void setQueueLimit(String queueLimit) {
        getEntryWizard().setComponentValue(QUEUE_LIMIT_FIELD_ID, queueLimit);
    }

    public void setFairQueue(String fairQueue) {
        getEntryWizard().setComponentValue(FAIR_QUEUE_FIELD_ID, fairQueue);
    }

    public void setPriority(String priority) {
        getEntryWizard().setComponentValue(PRIORITY_FIELD_ID, priority);
    }

    public void setRandomDetect(String randomDetect) {
        getEntryWizard().setComponentValue(RANDOM_DETECT_CHECKBOX_ID, randomDetect);
    }

    public void setIPPrecedence(String ipPrecedence) {
        getEntryWizard().setComponentValue(IP_PRECEDENCE_FIELD_ID, ipPrecedence);
    }

    public void setMPLSExperimental(String mplsExperimental) {
        getEntryWizard().setComponentValue(MPLS_EXPERIMENTAL_FIELD_ID, mplsExperimental);
    }

    public void setShape(String shape) {
        getEntryWizard().setComponentValue(SHAPE_FIELD_ID, shape);
    }

    public void setShapeRate(String shapeRate) {
        getEntryWizard().setComponentValue(SHAPE_RATE_FIELD_ID, shapeRate);
    }

    public void setIPDSCP(String ipdscp) {
        getEntryWizard().setComponentValue(IP_DSCP_FIELD_ID, ipdscp);
    }

    public void setCIRIngress(String cirIngress) {
        getEntryWizard().setComponentValue(CIR_INGRESS_FIELD_ID, cirIngress);
    }

    public void setCIREgress(String cirEgress) {
        getEntryWizard().setComponentValue(CIR_EGRESS_FIELD_ID, cirEgress);
    }

    public void setPIRIngress(String pirIngress) {
        getEntryWizard().setComponentValue(PIR_INGRESS_FIELD_ID, pirIngress);
    }

    public void setPIREgress(String pirEgress) {
        getEntryWizard().setComponentValue(PIR_EGRESS_FIELD_ID, pirEgress);
    }

    @Step("Click Save button")
    public TrafficPolicyWizardPage clickSave() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getEntryWizard().clickButtonById(SAVE_BUTTON_ID);
        return this;
    }
}
