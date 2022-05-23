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
import com.oss.framework.wizard.Wizard;
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
    private static final String DELETE_VSI_COMPONENT_ID = "deleteAppId_prompt-card";
    private static final String DELETE_ROUTE_TARGET_COMPONENT_ID = "remove_id_prompt-card";
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
        RouteTargetWizardPage routeTargetWizard = goToRouteTargetWizardPage();
        fillRouteTargetWizardToCreate(routeTargetWizard);
        createdRouteTargetNewInventoryViewPageURL = driver.getCurrentUrl();
    }

    @Test(priority = 2)
    @Step("Create VSI")
    public void createVSI() {
        VSIAttributes vsiAttributes = getVSIAttributesToCreate();
        VSIWizardPage wizardPage = goToVSIWizardPage();

        goThroughWizardAtCreation(wizardPage, vsiAttributes);
        wizardPage.clickAccept();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        softAssert.assertEquals((systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType()), SystemMessageContainer.MessageType.SUCCESS);
        systemMessage.clickMessageLink();

        newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        assertVsiAttributes(newInventoryViewPage, vsiAttributes);

        newInventoryViewPage.selectTabByLabel(INTERFACES_TAB_LABEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        assertInterfacesAfterCreate();
    }

    @Test(priority = 3)
    @Step("Update VSI attributes")
    public void updateVSI() {
        VSIAttributes vsiAttributes = getVSIAttributesToUpdate();
        VSIWizardPage vsiWizard = clickEdit();

        goThroughWizardAtUpdate(vsiWizard, vsiAttributes);
        vsiWizard.clickAccept();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        softAssert.assertEquals((systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType()), SystemMessageContainer.MessageType.SUCCESS);

        newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectTabByLabel(PROPERTIES_TAB_LABEL);
        waitForPageToLoad();
        assertVsiAttributes(newInventoryViewPage, vsiAttributes);

        newInventoryViewPage.selectTabByLabel(INTERFACES_TAB_LABEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        assertInterfacesAfterUpdate();
    }

    @Test(priority = 4)
    @Step("Assign created earlier Route Target")
    public void assignCreatedRouteTarget() {
        newInventoryViewPage.selectFirstRow();
        AssignRouteTargetWizardPage assignRouteTargetWizard = clickAssignRouteTarget();
        goThroughAssignRouteTargetWizard(assignRouteTargetWizard);
        assignRouteTargetWizard.clickAccept();

        newInventoryViewPage.selectTabByLabel(ROUTE_TARGETS_TAB_LABEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        assertRouteTargetsAfterAssign();
    }

    @Test(priority = 5)
    @Step("Remove VSI")
    public void removeVsi() {
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        deleteVSI();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        softAssert.assertEquals((systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType()), SystemMessageContainer.MessageType.SUCCESS);
        assertVsiRemoval();
    }

    @Test(priority = 6)
    @Step("Remove created Route Target")
    public void removeCreatedRouteTarget() {
        driver.navigate().to(createdRouteTargetNewInventoryViewPageURL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        deleteRouteTarget();
        assertRouteTargetRemoval();
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

    private RouteTargetWizardPage goToRouteTargetWizardPage() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        driver.get(String.format("%s/#/view/transport/tpt/vpn/routetarget?perspective=LIVE", BASIC_URL));
        waitForPageToLoad();
        return new RouteTargetWizardPage(driver);
    }

    private VSIWizardPage goToVSIWizardPage() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        driver.get(String.format("%s/#/view/transport/ip/mpls/vsi?perspective=LIVE", BASIC_URL));

        return new VSIWizardPage(driver);
    }

    private void fillRouteTargetWizardToCreate(RouteTargetWizardPage routeTargetWizard) {
        routeTargetWizard.setRouteTarget(ROUTE_TARGET_TO_CREATE);
        routeTargetWizard.clickAccept();
        waitForPageToLoad();
    }

    private void goThroughWizardAtCreation(VSIWizardPage wizardPage, VSIAttributes vsiAttributes) {
        wizardPage.setDevice(vsiAttributes.device);
        fillVsiAttributes(wizardPage, vsiAttributes);
        fulfillSecondStepAtCreate(wizardPage, vsiAttributes);
    }

    private void goThroughWizardAtUpdate(VSIWizardPage wizardPage, VSIAttributes vsiAttributes) {
        fillVsiAttributes(wizardPage, vsiAttributes);
        fulfillSecondStepAtUpdate(wizardPage, vsiAttributes);
    }

    private void goThroughAssignRouteTargetWizard(AssignRouteTargetWizardPage wizardPage) {
        fillRouteTargetAttributes(wizardPage);
    }

    private void fulfillSecondStepAtCreate(VSIWizardPage wizardPage, VSIAttributes vsiAttributes) {
        wizardPage.navigateThroughSecondPhase(vsiAttributes.device, ETHERNET_INTERFACE_LABEL, INTERFACE_NAME_AT_CREATION);
    }

    private void fulfillSecondStepAtUpdate(VSIWizardPage wizardPage, VSIAttributes vsiAttributes) {
        wizardPage.navigateThroughSecondPhase(vsiAttributes.device, ETHERNET_INTERFACE_LABEL, INTERFACE_NAME_AT_CREATION, INTERFACE_NAME_AT_UPDATE);
    }

    private void fillVsiAttributes(VSIWizardPage wizardPage, VSIAttributes vsiAttributes) {
        wizardPage.setVpnId(vsiAttributes.vpnId);
        wizardPage.setName(vsiAttributes.name);
        wizardPage.setVeId(vsiAttributes.veID);
        wizardPage.setRouteDistinguisher(vsiAttributes.routeDistinguisher);
        wizardPage.setDescription(vsiAttributes.description);
        wizardPage.clickNextStep();
    }

    private void fillRouteTargetAttributes(AssignRouteTargetWizardPage wizardPage) {
        wizardPage.setRouteTarget(ROUTE_TARGET_TO_ASSIGN);
        wizardPage.setImport();
        wizardPage.setExport();
    }

    @Step("Click edit button")
    public VSIWizardPage clickEdit() {
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, EDIT_VSI_ACTION_ID);
        return new VSIWizardPage(driver);
    }

    @Step("Click delete button")
    public void clickDelete() {
        Wizard.createByComponentId(driver, webDriverWait, DELETE_VSI_COMPONENT_ID).clickButtonByLabel("OK");
    }

    @Step("Click Ok button")
    public void clickOk() {
        Wizard.createByComponentId(driver, webDriverWait, DELETE_ROUTE_TARGET_COMPONENT_ID).clickButtonByLabel("Ok");
    }

    @Step("Delete VSI")
    public void deleteVSI() {
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_VSI_ACTION_ID);
        waitForPageToLoad();
        clickDelete();
    }

    @Step("Delete Route Target")
    public void deleteRouteTarget() {
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_ROUTE_TARGET_ACTION_ID);
        waitForPageToLoad();
        clickOk();
    }

    @Step("Click assign Route Target button")
    public AssignRouteTargetWizardPage clickAssignRouteTarget() {
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

    private void assertVsiRemoval() {
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.checkIfTableIsEmpty();
    }

    private void assertRouteTargetRemoval() {
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.checkIfTableIsEmpty();
    }

    public void assertInterfacesAfterCreate() {
        refreshInterfacesTab();
        String firstEthernetInterface = selectObjectInInterfacesTab(0, "name");

        Assert.assertEquals(firstEthernetInterface, INTERFACE_NAME_AT_CREATION);
    }

    public void assertInterfacesAfterUpdate() {
        refreshInterfacesTab();
        String aggregatedEthernetInterface = selectObjectInInterfacesTab(0, "name");

        Assert.assertEquals(aggregatedEthernetInterface, INTERFACE_NAME_AT_UPDATE);
    }

    public void assertRouteTargetsAfterAssign() {
        refreshRouteTargetsTab();
        String routeTarget = selectObjectInRouteTargetsTab(0, "routeTarget.value");

        Assert.assertEquals(routeTarget, ROUTE_TARGET_TO_ASSIGN);
    }

    public String selectObjectInInterfacesTab(Integer index, String column) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TableComponent tableComponent = TableComponent.create(driver, webDriverWait, INTERFACES_TABLE_COMPONENT_ID);
        tableComponent.selectRow(index);
        return tableComponent.getCellValue(index, column);
    }

    public String selectObjectInRouteTargetsTab(Integer index, String column) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        TableComponent tableComponent = TableComponent.create(driver, webDriverWait, ROUTE_TARGETS_TABLE_COMPONENT_ID);
        tableComponent.selectRow(index);
        return tableComponent.getCellValue(index, column);
    }

    public TableWidget getInterfacesTable() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return TableWidget.createById(driver, INTERFACES_TABLE_COMPONENT_ID, webDriverWait);
    }

    public TableWidget getRouteTargetsTable() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        return TableWidget.createById(driver, ROUTE_TARGETS_TABLE_COMPONENT_ID, webDriverWait);
    }

    public void refreshInterfacesTab() {
        callActionInInterfacesTab(ActionsContainer.KEBAB_GROUP_ID, TableWidget.REFRESH_ACTION_ID);
    }

    public void refreshRouteTargetsTab() {
        callActionInRouteTargetsTab(ActionsContainer.KEBAB_GROUP_ID, TableWidget.REFRESH_ACTION_ID);
    }

    @Step("Call {actionId} action from {groupId} group")
    public NewInventoryViewPage callActionInInterfacesTab(String groupId, String actionId) {
        getInterfacesTable().callAction(groupId, actionId);
        return new NewInventoryViewPage(driver, webDriverWait);
    }

    @Step("Call {actionId} action from {groupId} group")
    public NewInventoryViewPage callActionInRouteTargetsTab(String groupId, String actionId) {
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

