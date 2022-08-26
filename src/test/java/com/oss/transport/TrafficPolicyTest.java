package com.oss.transport;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.EditableList;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.transport.TrafficPolicyWizardPage;

import io.qameta.allure.Step;

import static com.oss.framework.components.contextactions.ActionsContainer.CREATE_GROUP_ID;
import static com.oss.framework.components.contextactions.ActionsContainer.EDIT_GROUP_ID;

public class TrafficPolicyTest extends BaseTestCase {

    private static final String PRE_CREATED_DEVICE = "SeleniumTestDeviceTC";
    private static final String CREATE_TRAFFIC_POLICY_ID = "CreateTrafficPolicyContextAction";
    private static final String EDIT_TRAFFIC_POLICY_ID = "TrafficPolicyEditContextAction";
    private static final String ADD_ENTRY_ID = "TrafficPolicyAddEntryContextAction";
    private static final String TRAFFIC_POLICY_NAME = "TrafficPolicySeleniumTest" + (int) (Math.random() * 101);
    private static final String TRAFFIC_POLICY_NAME_UPDATE = "TrafficPolicySeleniumTestUpdate" + (int) (Math.random() * 101);
    private static final String TRAFFIC_POLICY_DESCRIPTION = "Description after update";
    private static final String ENTRIES_TAB_ID = "Traffic Policy Entries";
    private static final String ASSIGNMENT_TAB_ID = "Traffic Policy Assignments";
    private static final String ASSIGNMENT_TABLE_ID = "AssignmentsWidget";
    private static final String EDIT_ENTRY_ID = "TrafficPolicyModifyEntryContextAction";
    private static final String TABLE_ENTRY_ID = "EntriesWidget";
    private static final String ASSIGN_INTERFACE_ID = "TrafficPolicyModifyAssignmentContextAction";
    private static final String REMOVE_ENTRY_ID = "TrafficPolicyDeleteEntryContextAction";
    private static final String TRAFFIC_POLICY_REMOVE_BUTTON_ID = "TrafficPolicyDeleteContextAction";
    private static final String EDITABLE_LIST_ID = "card-content_TrafficPolicyAssignInterfaceView_prompt-card";
    private static final String ASSIGN_INTERFACE_DIRECTION = "Bidirectional";
    private static final String DIRECTION_COLUMN_ID = "direction-column-id";
    private static final String SAVE_BUTTON_ID = "buttons-app-id-1";
    private static final String KEBAB_GROUP_ID = "frameworkCustomButtonsGroup";
    private static final String REFRESH_BUTTON_ID = "refreshButton";

    @Test(priority = 1)
    @Step("Create Traffic Policy")
    public void createTrafficPolicy() {
        TrafficPolicyWizardPage trafficPolicyWizard = goToWizardAtCreate();
        waitForPageToLoad();
        trafficPolicyWizard.createTrafficPolicy(TRAFFIC_POLICY_NAME);
        SystemMessageContainer.create(driver, webDriverWait).clickMessageLink();
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.searchObject(TRAFFIC_POLICY_NAME).selectFirstRow();
        waitForPageToLoad();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 2)
    @Step("Edit Traffic Policy")
    public void editTrafficPolicy() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.callAction(EDIT_GROUP_ID, EDIT_TRAFFIC_POLICY_ID);
        TrafficPolicyWizardPage trafficPolicyWizardPage = new TrafficPolicyWizardPage(driver);
        trafficPolicyWizardPage.editTrafficPolicy(TRAFFIC_POLICY_NAME_UPDATE, TRAFFIC_POLICY_DESCRIPTION);
        newInventoryViewPage.searchObject(TRAFFIC_POLICY_NAME_UPDATE).selectFirstRow();
        waitForPageToLoad();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 3)
    @Step("Add Entry to Traffic Policy")
    public void addEntryToTrafficPolicy() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.callAction(EDIT_GROUP_ID, ADD_ENTRY_ID);
        TrafficPolicyWizardPage trafficPolicyWizardPage = new TrafficPolicyWizardPage(driver);
        TrafficPolicyEntryAttributes trafficPolicyEntryAttributes = getEntryAttributesForCreate();
        fillEntryToTrafficPolicy(trafficPolicyEntryAttributes, trafficPolicyWizardPage);
        trafficPolicyWizardPage.clickSave();
    }

    @Test(priority = 4)
    @Step("Edit Traffic Policy Entry")
    public void editTrafficPolicyEntry() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.selectTabByLabel(ENTRIES_TAB_ID);
        TableWidget entriesTable = TableWidget.createById(driver, TABLE_ENTRY_ID, webDriverWait);
        entriesTable.selectFirstRow();
        entriesTable.callAction(EDIT_GROUP_ID, EDIT_ENTRY_ID);
        TrafficPolicyEntryAttributes trafficPolicyEntryAttributes = getEntryAttributesForUpdate();
        TrafficPolicyWizardPage trafficPolicyWizardPage = new TrafficPolicyWizardPage(driver);
        fillEntryToTrafficPolicyUpdate(trafficPolicyEntryAttributes, trafficPolicyWizardPage);
        trafficPolicyWizardPage.clickSave();
    }

    @Test(priority = 5)
    @Step("Assign Interface")
    public void assignInterfaceToTrafficPolicy() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.callAction(EDIT_GROUP_ID, ASSIGN_INTERFACE_ID);
        EditableList editableList = EditableList.createById(driver, webDriverWait, EDITABLE_LIST_ID);
        editableList.setValue(1, ASSIGN_INTERFACE_DIRECTION, DIRECTION_COLUMN_ID, DIRECTION_COLUMN_ID);
        Button saveButton = Button.createById(driver, SAVE_BUTTON_ID);
        saveButton.click();
        newInventoryViewPage.selectTabByLabel(ASSIGNMENT_TAB_ID);
        TableWidget assignmentTable = TableWidget.createById(driver, ASSIGNMENT_TABLE_ID, webDriverWait);
        waitForPageToLoad();
        Assert.assertFalse(assignmentTable.hasNoData());
    }

    @Test(priority = 6)
    @Step("Remove added Entry and assigned Interface")
    public void removeEntryAndInterface() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.selectTabByLabel(ENTRIES_TAB_ID);
        TableWidget entriesTable = TableWidget.createById(driver, TABLE_ENTRY_ID, webDriverWait);
        entriesTable.selectFirstRow();
        entriesTable.callAction(EDIT_GROUP_ID, REMOVE_ENTRY_ID);
        newInventoryViewPage.clickConfirmationRemovalButton();

        newInventoryViewPage.callAction(EDIT_GROUP_ID, ASSIGN_INTERFACE_ID);
        EditableList editableList = EditableList.createById(driver, webDriverWait, EDITABLE_LIST_ID);
        editableList.setValue(1, "-", DIRECTION_COLUMN_ID, DIRECTION_COLUMN_ID);
        Button saveButton = Button.createById(driver, SAVE_BUTTON_ID);
        saveButton.click();

        entriesTable.callAction(KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
        waitForPageToLoad();
        Assert.assertTrue(entriesTable.hasNoData());
        newInventoryViewPage.selectTabByLabel(ASSIGNMENT_TAB_ID);
        TableWidget assignmentTable = TableWidget.createById(driver, ASSIGNMENT_TABLE_ID, webDriverWait);
        assignmentTable.callAction(KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
        waitForPageToLoad();
        Assert.assertTrue(assignmentTable.hasNoData());
    }

    @Test(priority = 7)
    @Step("Remove Traffic Policy")
    public void removeTrafficPolicy() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.searchObject(TRAFFIC_POLICY_NAME_UPDATE).selectFirstRow();
        newInventoryViewPage.callAction(EDIT_GROUP_ID, TRAFFIC_POLICY_REMOVE_BUTTON_ID).clickConfirmationRemovalButton();
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
    }

    private TrafficPolicyWizardPage goToWizardAtCreate() {
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory");
        SearchObjectTypePage searchObjectType = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectType.searchType("Physical Device");
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(PRE_CREATED_DEVICE).selectFirstRow();
        newInventoryViewPage.callAction(CREATE_GROUP_ID, CREATE_TRAFFIC_POLICY_ID);
        return new TrafficPolicyWizardPage(driver);
    }

    private void fillEntryToTrafficPolicy(TrafficPolicyEntryAttributes trafficPolicyEntryAttributes, TrafficPolicyWizardPage trafficPolicyWizardPage) {
        trafficPolicyWizardPage.setTrafficObject(trafficPolicyEntryAttributes.trafficObject);
        trafficPolicyWizardPage.setMatch(trafficPolicyEntryAttributes.match);
        trafficPolicyWizardPage.setBandwidth(trafficPolicyEntryAttributes.bandwidth);
        trafficPolicyWizardPage.setQueueLimit(trafficPolicyEntryAttributes.queueLimit);
        trafficPolicyWizardPage.setFairQueue(trafficPolicyEntryAttributes.fairQueue);
        trafficPolicyWizardPage.setPriority(trafficPolicyEntryAttributes.priority);
        trafficPolicyWizardPage.setRandomDetect(trafficPolicyEntryAttributes.randomDetect);
        trafficPolicyWizardPage.setIPPrecedence(trafficPolicyEntryAttributes.ipPrecedence);
        trafficPolicyWizardPage.setMPLSExperimental(trafficPolicyEntryAttributes.mplsExperimental);
        trafficPolicyWizardPage.setShape(trafficPolicyEntryAttributes.shape);
        trafficPolicyWizardPage.setShapeRate(trafficPolicyEntryAttributes.shapeRate);
        trafficPolicyWizardPage.setIPDSCP(trafficPolicyEntryAttributes.ipDSCP);
        trafficPolicyWizardPage.setCIRIngress(trafficPolicyEntryAttributes.cirIngress);
        trafficPolicyWizardPage.setCIREgress(trafficPolicyEntryAttributes.cirEgress);
        trafficPolicyWizardPage.setPIRIngress(trafficPolicyEntryAttributes.pirIngress);
        trafficPolicyWizardPage.setPIREgress(trafficPolicyEntryAttributes.pirEgress);
    }

    private void fillEntryToTrafficPolicyUpdate(TrafficPolicyEntryAttributes trafficPolicyEntryAttributes, TrafficPolicyWizardPage trafficPolicyWizardPage) {
        trafficPolicyWizardPage.setMatch(trafficPolicyEntryAttributes.match);
        trafficPolicyWizardPage.setBandwidth(trafficPolicyEntryAttributes.bandwidth);
        trafficPolicyWizardPage.setQueueLimit(trafficPolicyEntryAttributes.queueLimit);
        trafficPolicyWizardPage.setFairQueue(trafficPolicyEntryAttributes.fairQueue);
        trafficPolicyWizardPage.setPriority(trafficPolicyEntryAttributes.priority);
        trafficPolicyWizardPage.setRandomDetect(trafficPolicyEntryAttributes.randomDetect);
        trafficPolicyWizardPage.setIPPrecedence(trafficPolicyEntryAttributes.ipPrecedence);
        trafficPolicyWizardPage.setMPLSExperimental(trafficPolicyEntryAttributes.mplsExperimental);
        trafficPolicyWizardPage.setShape(trafficPolicyEntryAttributes.shape);
        trafficPolicyWizardPage.setShapeRate(trafficPolicyEntryAttributes.shapeRate);
        trafficPolicyWizardPage.setIPDSCP(trafficPolicyEntryAttributes.ipDSCP);
        trafficPolicyWizardPage.setCIRIngress(trafficPolicyEntryAttributes.cirIngress);
        trafficPolicyWizardPage.setCIREgress(trafficPolicyEntryAttributes.cirEgress);
        trafficPolicyWizardPage.setPIRIngress(trafficPolicyEntryAttributes.pirIngress);
        trafficPolicyWizardPage.setPIREgress(trafficPolicyEntryAttributes.pirEgress);
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

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
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
}
