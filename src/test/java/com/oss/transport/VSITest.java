package com.oss.transport;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.VSI.AssignRouteTargetWizardPage;
import com.oss.pages.transport.VSI.VSIWizardPage;
import com.oss.pages.transport.routeTarget.RouteTargetWizardPage;

import io.qameta.allure.Step;

/**
 * @author Kamil Sikora
 */
public class VSITest extends BaseTestCase {

    private NewInventoryViewPage newInventoryViewPage;
    private static Random rand = new Random();

    private static final String PRE_CREATED_DEVICE_NAME = "VSISeleniumTestDevice";

    private static final String ROUTE_TARGET_TO_CREATE = rand.nextInt(9 + 1) * 100 + ":" + rand.nextInt(9 + 1) * 100;
    private static final String ROUTE_TARGET_TO_ASSIGN = "12345:67890";

    private static final String INTERFACE_NAME_AT_CREATION = "SFP+ 1";
    private static final String INTERFACE_NAME_AT_UPDATE = "SYNC 1";

    private static final String ETHERNET_INTERFACE_LABEL = "Ethernet Interface";
    private static final String EDIT_VSI_ACTION_ID = "EditVSIContextAction";
    private static final String DELETE_VSI_ACTION_ID = "DeleteVSIContextAction";
    private static final String DELETE_ROUTE_TARGET_ACTION_ID = "DeleteRouteTargetContextAction";
    private static final String INTERFACES_TABLE_COMPONENT_ID = "ServerTerminationPointsWidget";
    private static final String ROUTE_TARGETS_TABLE_COMPONENT_ID = "VsiImpExpWidget";

    private static final String ROUTE_TARGETS_TAB_LABEL = "VSI Imp/Exp Route Targets";
    private static final String INTERFACES_TAB_LABEL = "Interfaces in VSI";
    private static final String PROPERTIES_TAB_LABEL = "Properties";

    private String createdRouteTargetNewInventoryViewPageURL;
    private SoftAssert softAssert;

    @BeforeClass
    public void openConsole() {
        softAssert = new SoftAssert();
        waitForPageToLoad();
    }

    @Test(priority = 1)
    @Step("Create Route Target")
    public void createRouteTarget() {
        RouteTargetWizardPage routeTargetWizard = new RouteTargetWizardPage(driver);
        routeTargetWizard.goToRouteTargetWizardPage();
        fillRouteTargetWizardToCreate(routeTargetWizard);
        createdRouteTargetNewInventoryViewPageURL = driver.getCurrentUrl();
    }

    @Test(priority = 2)
    @Step("Create VSI")
    public void createVSI() {
        VSIAttributes vsiAttributes = getVSIAttributesToCreate();
        VSIWizardPage vsiWizardPage = new VSIWizardPage(driver);
        vsiWizardPage.goToVSIWizardPage();

        goThroughWizardAtCreation(vsiWizardPage, vsiAttributes);
        vsiWizardPage.clickAccept();
        checkSystemMessage();
        SystemMessageContainer.create(driver, webDriverWait).clickMessageLink();

        newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        assertVsiAttributes(newInventoryViewPage, vsiAttributes);

        openInterfacesTab();
        assertInterfacesAfterCreate();
    }

    @Test(priority = 3)
    @Step("Update VSI attributes")
    public void updateVSI() {
        VSIAttributes vsiAttributes = getVSIAttributesToUpdate();
        VSIWizardPage vsiWizard = clickEdit();

        goThroughWizardAtUpdate(vsiWizard, vsiAttributes);
        vsiWizard.clickAccept();
        checkSystemMessage();

        openPropertiesTab();
        waitForPageToLoad();
        assertVsiAttributes(newInventoryViewPage, vsiAttributes);

        openInterfacesTab();
        assertInterfacesAfterUpdate();
    }

    @Test(priority = 4)
    @Step("Assign created earlier Route Target")
    public void assignCreatedRouteTarget() {
        newInventoryViewPage.selectFirstRow();
        AssignRouteTargetWizardPage assignRouteTargetWizard = clickAssignRouteTarget();
        fillRouteTargetAttributes(assignRouteTargetWizard);
        assignRouteTargetWizard.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        openRouteTargetsTab();
        assertRouteTargetsAfterAssign();
    }

    @Test(priority = 5)
    @Step("Remove VSI")
    public void removeVsi() {
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        deleteVSI();
        checkSystemMessage();
        checkVsiRemoval();
    }

    @Test(priority = 6)
    @Step("Remove created Route Target")
    public void removeCreatedRouteTarget() {
        driver.navigate().to(createdRouteTargetNewInventoryViewPageURL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        deleteRouteTarget();
        checkRouteTargetRemoval();
    }

    private VSIAttributes getVSIAttributesToCreate() {
        VSIAttributes attributes = new VSIAttributes();
        attributes.vpnId = "1" + rand.nextInt(9 + 1) * 100;
        attributes.device = PRE_CREATED_DEVICE_NAME;
        attributes.name = "testVsi 1" + rand.nextInt(9 + 1) * 100;
        attributes.veID = "331";
        attributes.routeDistinguisher = "123:321";
        attributes.description = "test description";
        return attributes;
    }

    private VSIAttributes getVSIAttributesToUpdate() {
        VSIAttributes attributes = new VSIAttributes();
        attributes.vpnId = "1" + rand.nextInt(9 + 1) * 100;
        attributes.device = PRE_CREATED_DEVICE_NAME;
        attributes.name = "testVsi 2" + rand.nextInt(9 + 1) * 100;
        attributes.veID = "133";
        attributes.routeDistinguisher = "321:123";
        attributes.description = "test description 2";
        return attributes;
    }

    private void fillRouteTargetWizardToCreate(RouteTargetWizardPage routeTargetWizard) {
        routeTargetWizard.setRouteTarget(ROUTE_TARGET_TO_CREATE);
        routeTargetWizard.clickAccept();
        waitForPageToLoad();
    }

    private void goThroughWizardAtCreation(VSIWizardPage vsiWizardPage, VSIAttributes vsiAttributes) {
        vsiWizardPage.setDevice(vsiAttributes.device);
        fillVsiAttributes(vsiWizardPage, vsiAttributes);
        fulfillSecondStepAtCreate(vsiWizardPage, vsiAttributes);
    }

    private void goThroughWizardAtUpdate(VSIWizardPage vsiWizardPage, VSIAttributes vsiAttributes) {
        fillVsiAttributes(vsiWizardPage, vsiAttributes);
        fulfillSecondStepAtUpdate(vsiWizardPage, vsiAttributes);
    }

    private void fulfillSecondStepAtCreate(VSIWizardPage vsiWizardPage, VSIAttributes vsiAttributes) {
        vsiWizardPage.navigateThroughSecondPhase(vsiAttributes.device, ETHERNET_INTERFACE_LABEL, INTERFACE_NAME_AT_CREATION);
    }

    private void fulfillSecondStepAtUpdate(VSIWizardPage vsiWizardPage, VSIAttributes vsiAttributes) {
        vsiWizardPage.navigateThroughSecondPhase(vsiAttributes.device, ETHERNET_INTERFACE_LABEL, INTERFACE_NAME_AT_CREATION, INTERFACE_NAME_AT_UPDATE);
    }

    private void fillVsiAttributes(VSIWizardPage vsiWizardPage, VSIAttributes vsiAttributes) {
        vsiWizardPage.setVpnId(vsiAttributes.vpnId);
        vsiWizardPage.setName(vsiAttributes.name);
        vsiWizardPage.setVeId(vsiAttributes.veID);
        vsiWizardPage.setRouteDistinguisher(vsiAttributes.routeDistinguisher);
        vsiWizardPage.setDescription(vsiAttributes.description);
        vsiWizardPage.clickNextStep();
    }

    private void fillRouteTargetAttributes(AssignRouteTargetWizardPage wizardPage) {
        wizardPage.setRouteTarget(ROUTE_TARGET_TO_ASSIGN);
        wizardPage.setImport("true");
        wizardPage.setExport("false");
    }

    @Step("Click edit button")
    private VSIWizardPage clickEdit() {
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, EDIT_VSI_ACTION_ID);
        return new VSIWizardPage(driver);
    }

    @Step("Delete VSI")
    private void deleteVSI() {
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_VSI_ACTION_ID);
        waitForPageToLoad();
        newInventoryViewPage.clickConfirmationRemovalButton();
    }

    @Step("Delete Route Target")
    private void deleteRouteTarget() {
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_ROUTE_TARGET_ACTION_ID);
        waitForPageToLoad();
        newInventoryViewPage.clickConfirmationRemovalButton();
    }

    @Step("Click assign Route Target button")
    private AssignRouteTargetWizardPage clickAssignRouteTarget() {
        newInventoryViewPage.callAction(ActionsContainer.ASSIGN_GROUP_ID, "AssignRouteTargetContextAction");
        return new AssignRouteTargetWizardPage(driver);
    }

    private void assertVsiAttributes(NewInventoryViewPage newInventoryViewPage, VSIAttributes vsiAttributes) {
        String vsiName = newInventoryViewPage.getPropertyPanelValue("name");
        String device = newInventoryViewPage.getPropertyPanelValue("physicalDevice.name");
        String routeDistinguisher = newInventoryViewPage.getPropertyPanelValue("routeDistinguisher");
        String veId = newInventoryViewPage.getPropertyPanelValue("veId");
        String vpnId = newInventoryViewPage.getPropertyPanelValue("vpnId");

        Assert.assertEquals(vsiName, vsiAttributes.name);
        Assert.assertEquals(vpnId, vsiAttributes.vpnId);
        Assert.assertEquals(veId, vsiAttributes.veID);
        Assert.assertEquals(device, vsiAttributes.device);
        Assert.assertEquals(routeDistinguisher, vsiAttributes.routeDistinguisher);
    }

    private void checkVsiRemoval() {
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.checkIfTableIsEmpty();
    }

    private void checkRouteTargetRemoval() {
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.checkIfTableIsEmpty();
    }

    private void assertInterfacesAfterCreate() {
        refreshInterfacesTab();
        String firstEthernetInterface = selectObjectInInterfacesTab(0, "name");

        Assert.assertEquals(firstEthernetInterface, INTERFACE_NAME_AT_CREATION);
    }

    private void assertInterfacesAfterUpdate() {
        refreshInterfacesTab();
        String aggregatedEthernetInterface = selectObjectInInterfacesTab(0, "name");

        Assert.assertEquals(aggregatedEthernetInterface, INTERFACE_NAME_AT_UPDATE);
    }

    private void assertRouteTargetsAfterAssign() {
        refreshRouteTargetsTab();
        String routeTarget = selectObjectInRouteTargetsTab(0, "routeTarget.value");

        Assert.assertEquals(routeTarget, ROUTE_TARGET_TO_ASSIGN);
    }

    private void checkSystemMessage() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        softAssert.assertEquals((systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType()), SystemMessageContainer.MessageType.SUCCESS);
    }

    private void openInterfacesTab() {
        newInventoryViewPage.selectTabByLabel(INTERFACES_TAB_LABEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void openRouteTargetsTab() {
        newInventoryViewPage.selectTabByLabel(ROUTE_TARGETS_TAB_LABEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void openPropertiesTab() {
        newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectTabByLabel(PROPERTIES_TAB_LABEL);
    }

    private String selectObjectInInterfacesTab(Integer index, String column) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TableComponent tableComponent = TableComponent.create(driver, webDriverWait, INTERFACES_TABLE_COMPONENT_ID);
        tableComponent.selectRow(index);
        return tableComponent.getCellValue(index, column);
    }

    private String selectObjectInRouteTargetsTab(Integer index, String column) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TableComponent tableComponent = TableComponent.create(driver, webDriverWait, ROUTE_TARGETS_TABLE_COMPONENT_ID);
        tableComponent.selectRow(index);
        return tableComponent.getCellValue(index, column);
    }

    private TableWidget getInterfacesTable() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return TableWidget.createById(driver, INTERFACES_TABLE_COMPONENT_ID, webDriverWait);
    }

    private TableWidget getRouteTargetsTable() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return TableWidget.createById(driver, ROUTE_TARGETS_TABLE_COMPONENT_ID, webDriverWait);
    }

    private void refreshInterfacesTab() {
        callActionInInterfacesTab(ActionsContainer.KEBAB_GROUP_ID, TableWidget.REFRESH_ACTION_ID);
    }

    private void refreshRouteTargetsTab() {
        callActionInRouteTargetsTab(ActionsContainer.KEBAB_GROUP_ID, TableWidget.REFRESH_ACTION_ID);
    }

    @Step("Call {actionId} action from {groupId} group")
    private NewInventoryViewPage callActionInInterfacesTab(String groupId, String actionId) {
        getInterfacesTable().callAction(groupId, actionId);
        return new NewInventoryViewPage(driver, webDriverWait);
    }

    @Step("Call {actionId} action from {groupId} group")
    private NewInventoryViewPage callActionInRouteTargetsTab(String groupId, String actionId) {
        getRouteTargetsTable().callAction(groupId, actionId);
        return new NewInventoryViewPage(driver, webDriverWait);
    }

    private static class VSIAttributes {
        private String vpnId;
        private String device;
        private String name;
        private String veID;
        private String routeDistinguisher;
        private String description;
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}

