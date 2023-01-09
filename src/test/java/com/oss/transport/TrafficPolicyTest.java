package com.oss.transport;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.comarch.oss.web.pages.SearchObjectTypePage;
import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.oss.pages.transport.TrafficPolicyWizardPage;

import io.qameta.allure.Step;

public class TrafficPolicyTest extends BaseTestCase {

    private static final String PRE_CREATED_DEVICE = "SeleniumTestDeviceTC";
    private static final String CREATE_TRAFFIC_POLICY_ID = "CreateTrafficPolicyContextAction";
    private static final String EDIT_TRAFFIC_POLICY_ID = "TrafficPolicyEditContextAction";
    private static final String TRAFFIC_POLICY_NAME = "TrafficPolicySeleniumTest" + (int) (Math.random() * 101);
    private static final String TRAFFIC_POLICY_NAME_UPDATE = "TrafficPolicySeleniumTestUpdate" + (int) (Math.random() * 101);
    private static final String TRAFFIC_POLICY_DESCRIPTION = "Description after update";
    private static final String ENTRIES_TAB_ID = "Traffic Policy Entries";
    private static final String ASSIGNMENT_TAB_ID = "Traffic Policy Assignments";
    private static final String ASSIGNMENT_TABLE_ID = "AssignmentsWidget";
    private static final String TABLE_ENTRY_ID = "EntriesWidget";
    private static final String ASSIGN_INTERFACE_ID = "TrafficPolicyModifyAssignmentContextAction";
    private static final String REMOVE_ENTRY_ID = "TrafficPolicyDeleteEntryContextAction";
    private static final String TRAFFIC_POLICY_REMOVE_BUTTON_ID = "TrafficPolicyDeleteContextAction";
    private static final String ASSIGN_INTERFACE_DIRECTION = "Bidirectional";
    private static final String CLEAR_ASSIGNED_INTERFACE_DIRECTION = "-";
    private static final String EDIT_ENTRY_ID = "TrafficPolicyModifyEntryContextAction";
    private static final String ADD_ENTRY_ID = "TrafficPolicyAddEntryContextAction";

    @Test(priority = 1)
    @Step("Create Traffic Policy")
    public void createTrafficPolicy() {
        goToWizardAtCreate();
        waitForPageToLoad();
        TrafficPolicyWizardPage trafficPolicyWizard = new TrafficPolicyWizardPage(driver);
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
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, EDIT_TRAFFIC_POLICY_ID);
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
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, ADD_ENTRY_ID);
        TrafficPolicyWizardPage trafficPolicyWizardPage = new TrafficPolicyWizardPage(driver);
        trafficPolicyWizardPage.addEntryToTrafficPolicy();
    }

    @Test(priority = 4)
    @Step("Edit Traffic Policy Entry")
    public void editTrafficPolicyEntry() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.selectTabByLabel(ENTRIES_TAB_ID);
        TableWidget entriesTable = TableWidget.createById(driver, TABLE_ENTRY_ID, webDriverWait);
        entriesTable.selectFirstRow();
        entriesTable.callAction(ActionsContainer.EDIT_GROUP_ID, EDIT_ENTRY_ID);
        TrafficPolicyWizardPage trafficPolicyWizardPage = new TrafficPolicyWizardPage(driver);
        trafficPolicyWizardPage.editTrafficPolicyEntry();
    }

    @Test(priority = 5)
    @Step("Assign Interface")
    public void assignInterfaceToTrafficPolicy() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, ASSIGN_INTERFACE_ID);
        TrafficPolicyWizardPage trafficPolicyWizardPage = new TrafficPolicyWizardPage(driver);
        trafficPolicyWizardPage.setAssignInterfaceDirection(ASSIGN_INTERFACE_DIRECTION);
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
        entriesTable.callAction(ActionsContainer.EDIT_GROUP_ID, REMOVE_ENTRY_ID);
        newInventoryViewPage.clickConfirmationRemovalButton();
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, ASSIGN_INTERFACE_ID);

        TrafficPolicyWizardPage trafficPolicyWizardPage = new TrafficPolicyWizardPage(driver);
        trafficPolicyWizardPage.setAssignInterfaceDirection(CLEAR_ASSIGNED_INTERFACE_DIRECTION);

        entriesTable.callAction(ActionsContainer.KEBAB_GROUP_ID, TableWidget.REFRESH_ACTION_ID);
        waitForPageToLoad();
        Assert.assertTrue(entriesTable.hasNoData());
        newInventoryViewPage.selectTabByLabel(ASSIGNMENT_TAB_ID);
        TableWidget assignmentTable = TableWidget.createById(driver, ASSIGNMENT_TABLE_ID, webDriverWait);
        assignmentTable.callAction(ActionsContainer.KEBAB_GROUP_ID, TableWidget.REFRESH_ACTION_ID);
        waitForPageToLoad();
        Assert.assertTrue(assignmentTable.hasNoData());
    }

    @Test(priority = 7)
    @Step("Remove Traffic Policy")
    public void removeTrafficPolicy() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.searchObject(TRAFFIC_POLICY_NAME_UPDATE).selectFirstRow();
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, TRAFFIC_POLICY_REMOVE_BUTTON_ID).clickConfirmationRemovalButton();
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Step("Open wizard to create Traffic Policy")
    private void goToWizardAtCreate() {
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory");
        SearchObjectTypePage searchObjectType = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectType.searchType("Physical Device");
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(PRE_CREATED_DEVICE).selectFirstRow();
        newInventoryViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_TRAFFIC_POLICY_ID);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

}
