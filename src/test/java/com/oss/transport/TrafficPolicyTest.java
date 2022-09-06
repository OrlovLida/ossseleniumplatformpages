package com.oss.transport;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.TrafficPolicyWizardPage;

import io.qameta.allure.Step;

public class TrafficPolicyTest extends BaseTestCase {

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

    @Test(priority = 1)
    @Step("Create Traffic Policy")
    public void createTrafficPolicy() {
        TrafficPolicyWizardPage trafficPolicyWizard = new TrafficPolicyWizardPage(driver);
        trafficPolicyWizard.goToWizardAtCreate();
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
        TrafficPolicyWizardPage trafficPolicyWizardPage = new TrafficPolicyWizardPage(driver);
        trafficPolicyWizardPage.addEntryToTrafficPolicy();
    }

    @Test(priority = 4)
    @Step("Edit Traffic Policy Entry")
    public void editTrafficPolicyEntry() {
        TrafficPolicyWizardPage trafficPolicyWizardPage = new TrafficPolicyWizardPage(driver);
        trafficPolicyWizardPage.editTrafficPolicyEntry();
    }

    @Test(priority = 5)
    @Step("Assign Interface")
    public void assignInterfaceToTrafficPolicy() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, ASSIGN_INTERFACE_ID);
        TrafficPolicyWizardPage trafficPolicyWizardPage = new TrafficPolicyWizardPage(driver);
        trafficPolicyWizardPage.assignInterfaceToTrafficPolicy();
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
        trafficPolicyWizardPage.removeAssignedInterface();

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

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

}
