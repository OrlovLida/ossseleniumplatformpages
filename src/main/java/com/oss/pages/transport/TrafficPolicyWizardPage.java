package com.oss.pages.transport;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
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
    private static final String SAVE_BUTTON_ID_ASSIGNMENT = "buttons-app-id-1";
    private static final String EDITABLE_LIST_ID = "card-content_TrafficPolicyAssignInterfaceView_prompt-card";
    private static final String DIRECTION_COLUMN_ID = "direction-column-id";

    @Step("Create Traffic Policy with Name {name}")
    public void createTrafficPolicy(String name) {
        waitForPageToLoad();
        getWizard().setComponentValue(TRAFFIC_POLICY_NAME_ID, name);
        waitForPageToLoad();
        getWizard().clickAccept();
    }

    @Step("Edit Traffic Policy and set name {newName} and description {description}")
    public void editTrafficPolicy(String newName, String description) {
        waitForPageToLoad();
        getEditWizard().setComponentValue(TRAFFIC_POLICY_NAME_ID, newName);
        waitForPageToLoad();
        getEditWizard().setComponentValue(TRAFFIC_POLICY_DESCRIPTION_ID, description);
        waitForPageToLoad();
        getEditWizard().clickSave();
    }

    @Step("Add Entry to Traffic Policy")
    public void addEntryToTrafficPolicy() {
        TrafficPolicyEntryAttributes trafficPolicyEntryAttributes = getEntryAttributesForCreate();
        fillEntryToTrafficPolicy(trafficPolicyEntryAttributes);
        clickSave();
    }

    @Step("Edit Traffic Policy Entry")
    public void editTrafficPolicyEntry() {
        TrafficPolicyEntryAttributes trafficPolicyEntryAttributes = getEntryAttributesForUpdate();
        fillEntryToTrafficPolicyUpdate(trafficPolicyEntryAttributes);
        clickSave();
    }

    @Step("Set Assigned Interface Direction")
    public void setAssignInterfaceDirection(String directionValue) {
        EditableList editableList = EditableList.createById(driver, wait, EDITABLE_LIST_ID);
        editableList.setValue(1, directionValue, DIRECTION_COLUMN_ID, DIRECTION_COLUMN_ID);
        waitForPageToLoad();
        Button saveButton = Button.createById(driver, SAVE_BUTTON_ID_ASSIGNMENT);
        saveButton.click();
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
        waitForPageToLoad();
        getEntryWizard().clickButtonById(SAVE_BUTTON_ID);
        return this;
    }

    private static class TrafficPolicyEntryAttributes {
        private String trafficObject;
        private String match;
        private String bandwidth;
        private String queueLimit;
        private String fairQueue;
        private String priority;
        private String randomDetect;
        private String ipPrecedence;
        private String mplsExperimental;
        private String shape;
        private String shapeRate;
        private String ipDSCP;
        private String cirIngress;
        private String cirEgress;
        private String pirIngress;
        private String pirEgress;
    }

    private TrafficPolicyEntryAttributes getEntryAttributesForCreate() {
        TrafficPolicyEntryAttributes trafficPolicyEntryAttributes = new TrafficPolicyEntryAttributes();
        trafficPolicyEntryAttributes.trafficObject = "Selenium Test Traffic Class";
        trafficPolicyEntryAttributes.match = "FIRST";
        trafficPolicyEntryAttributes.bandwidth = "123";
        trafficPolicyEntryAttributes.queueLimit = "70";
        trafficPolicyEntryAttributes.fairQueue = "10";
        trafficPolicyEntryAttributes.priority = "1";
        trafficPolicyEntryAttributes.randomDetect = "True";
        trafficPolicyEntryAttributes.ipPrecedence = "5";
        trafficPolicyEntryAttributes.mplsExperimental = "5";
        trafficPolicyEntryAttributes.shape = "AVERAGE";
        trafficPolicyEntryAttributes.shapeRate = "20";
        trafficPolicyEntryAttributes.ipDSCP = "BE";
        trafficPolicyEntryAttributes.cirIngress = "10";
        trafficPolicyEntryAttributes.cirEgress = "20";
        trafficPolicyEntryAttributes.pirIngress = "30";
        trafficPolicyEntryAttributes.pirEgress = "50";
        return trafficPolicyEntryAttributes;
    }

    private TrafficPolicyEntryAttributes getEntryAttributesForUpdate() {
        TrafficPolicyEntryAttributes trafficPolicyEntryAttributes = new TrafficPolicyEntryAttributes();
        trafficPolicyEntryAttributes.match = "ANY";
        trafficPolicyEntryAttributes.bandwidth = "456";
        trafficPolicyEntryAttributes.queueLimit = "15";
        trafficPolicyEntryAttributes.fairQueue = "3";
        trafficPolicyEntryAttributes.priority = "2";
        trafficPolicyEntryAttributes.randomDetect = "False";
        trafficPolicyEntryAttributes.ipPrecedence = "3";
        trafficPolicyEntryAttributes.mplsExperimental = "3";
        trafficPolicyEntryAttributes.shape = "PEAK";
        trafficPolicyEntryAttributes.shapeRate = "12";
        trafficPolicyEntryAttributes.ipDSCP = "CS2";
        trafficPolicyEntryAttributes.cirIngress = "21";
        trafficPolicyEntryAttributes.cirEgress = "31";
        trafficPolicyEntryAttributes.pirIngress = "41";
        trafficPolicyEntryAttributes.pirEgress = "101";
        return trafficPolicyEntryAttributes;
    }

    private void fillEntryToTrafficPolicy(TrafficPolicyEntryAttributes trafficPolicyEntryAttributes) {
        setTrafficObject(trafficPolicyEntryAttributes.trafficObject);
        setMatch(trafficPolicyEntryAttributes.match);
        setBandwidth(trafficPolicyEntryAttributes.bandwidth);
        setQueueLimit(trafficPolicyEntryAttributes.queueLimit);
        setFairQueue(trafficPolicyEntryAttributes.fairQueue);
        setPriority(trafficPolicyEntryAttributes.priority);
        setRandomDetect(trafficPolicyEntryAttributes.randomDetect);
        setIPPrecedence(trafficPolicyEntryAttributes.ipPrecedence);
        setMPLSExperimental(trafficPolicyEntryAttributes.mplsExperimental);
        setShape(trafficPolicyEntryAttributes.shape);
        setShapeRate(trafficPolicyEntryAttributes.shapeRate);
        setIPDSCP(trafficPolicyEntryAttributes.ipDSCP);
        setCIRIngress(trafficPolicyEntryAttributes.cirIngress);
        setCIREgress(trafficPolicyEntryAttributes.cirEgress);
        setPIRIngress(trafficPolicyEntryAttributes.pirIngress);
        setPIREgress(trafficPolicyEntryAttributes.pirEgress);
    }

    private void fillEntryToTrafficPolicyUpdate(TrafficPolicyEntryAttributes trafficPolicyEntryAttributes) {
        setMatch(trafficPolicyEntryAttributes.match);
        setBandwidth(trafficPolicyEntryAttributes.bandwidth);
        setQueueLimit(trafficPolicyEntryAttributes.queueLimit);
        setFairQueue(trafficPolicyEntryAttributes.fairQueue);
        setPriority(trafficPolicyEntryAttributes.priority);
        setRandomDetect(trafficPolicyEntryAttributes.randomDetect);
        setIPPrecedence(trafficPolicyEntryAttributes.ipPrecedence);
        setMPLSExperimental(trafficPolicyEntryAttributes.mplsExperimental);
        setShape(trafficPolicyEntryAttributes.shape);
        setShapeRate(trafficPolicyEntryAttributes.shapeRate);
        setIPDSCP(trafficPolicyEntryAttributes.ipDSCP);
        setCIRIngress(trafficPolicyEntryAttributes.cirIngress);
        setCIREgress(trafficPolicyEntryAttributes.cirEgress);
        setPIRIngress(trafficPolicyEntryAttributes.pirIngress);
        setPIREgress(trafficPolicyEntryAttributes.pirEgress);
    }
}
