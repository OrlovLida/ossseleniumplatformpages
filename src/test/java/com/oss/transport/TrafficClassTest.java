package com.oss.transport;

import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.transport.traffic.classs.TrafficClassCreationWizard;
import com.oss.pages.transport.traffic.classs.TrafficClassModificationWizardPage;
import com.oss.pages.transport.traffic.classs.TrafficClassWizardPage;

import io.qameta.allure.Step;

import static com.oss.framework.components.contextactions.ActionsContainer.CREATE_GROUP_ID;

/**
 * @author Kamil Sikora
 */
public class TrafficClassTest extends BaseTestCase {

    private static final String PRE_CREATED_LOCATION = "SELENIUM_TRANSPORT_LOCATION";
    private static final String PRE_CREATED_DEVICE = "SeleniumTestDeviceTC";
    private static final String INTERFACE_CREATE = "MGT LAN 0\\1";
    private static final String INTERFACE_UPDATE = "SFP+ 0\\1";
    private static final String EDIT_BUTTON_INSIDE_EDIT_GROUP_TEST_ID = "TrafficClassEditContextAction";
    private static final String REMOVE_BUTTON_INSIDE_EDIT_GROUP_TEST_ID = "TrafficClassDeleteContextAction";
    private static final String CREATE_TRAFFIC_CLASS = "CreateTrafficClassContextAction";
    private static final String CONFIRMATION_REMOVE_BUTTON = "ConfirmationBox_deleteAppId_action_button";

    Random rand = new Random();

    @Test(priority = 1)
    @Step("Create Traffic Class")
    public void createTrafficClass() {
        TrafficClassAttributes trafficClassAttributes = getTrafficClassAttributesToCreate();

        TrafficClassCreationWizard trafficClassWizard = goToWizardAtCreate();
        fillWizardAtCreate(trafficClassWizard, trafficClassAttributes);
        NewInventoryViewPage inventoryViewPage = trafficClassWizard.clickAccept();
        SystemMessageContainer.create(driver, webDriverWait).clickMessageLink();
        inventoryViewPage.refreshMainTable();
        inventoryViewPage.searchObject(trafficClassAttributes.trafficClassName).selectFirstRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        Assert.assertFalse(inventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 2)
    @Step("Update Traffic Class attributes")
    public void updateTrafficClass() {
        TrafficClassAttributes trafficClassAttributes = getTrafficClassAttributesToUpdate();

        NewInventoryViewPage inventoryViewBeforeUpdate = new NewInventoryViewPage(driver, webDriverWait);
        TrafficClassModificationWizardPage trafficClassModificationWizard = goToWizardAtUpdate(inventoryViewBeforeUpdate);
        fillWizardAtUpdate(trafficClassModificationWizard, trafficClassAttributes);
        NewInventoryViewPage inventoryViewAfterUpdate = trafficClassModificationWizard.clickSaveChanges();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        inventoryViewAfterUpdate.searchObject(trafficClassAttributes.trafficClassName);

        Assert.assertFalse(inventoryViewAfterUpdate.checkIfTableIsEmpty());
    }

    @Test(priority = 3)
    @Step("Remove created Traffic Class")
    public void removeTrafficClass() {
        NewInventoryViewPage inventoryView = new NewInventoryViewPage(driver, webDriverWait);

        deleteTrafficClass(inventoryView);
        boolean hasBeenDeleted = hasBeenDeleted();

        Assert.assertTrue(hasBeenDeleted);
        inventoryView.refreshMainTable();
        Assert.assertTrue(inventoryView.checkIfTableIsEmpty());
    }

    private TrafficClassAttributes getTrafficClassAttributesToCreate() {
        TrafficClassAttributes trafficClassAttributes = new TrafficClassAttributes();
        trafficClassAttributes.trafficClassName = "TrafficClassTest" + rand.nextInt(1000);
        trafficClassAttributes.description = "traffic class test description";
        trafficClassAttributes.matchType = "All";
        trafficClassAttributes.ipPrecedence = "5";
        trafficClassAttributes.mplsExperimental = "2";
        trafficClassAttributes.mplsExperimentalTop = "5";
        trafficClassAttributes.ipDscp = "BE";
        trafficClassAttributes.accessList = "test access list";
        trafficClassAttributes.inputInterface = PRE_CREATED_LOCATION + "-Router-6" + "\\" + INTERFACE_CREATE;
        trafficClassAttributes.protocol = "test protocol";
        trafficClassAttributes.cirIngress = "1";
        trafficClassAttributes.cirEgress = "2";
        trafficClassAttributes.pirIngress = "3";
        trafficClassAttributes.pirEgress = "4";
        return trafficClassAttributes;
    }

    private TrafficClassCreationWizard goToWizardAtCreate() {

        SideMenu sidemenu = SideMenu.create(driver, webDriverWait);
        sidemenu.callActionByLabel("Inventory View", "Resource Inventory");
        SearchObjectTypePage searchObjectTypePage = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectTypePage.searchType("Physical Device");

        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(PRE_CREATED_DEVICE).selectFirstRow();
        newInventoryViewPage.callAction(CREATE_GROUP_ID, CREATE_TRAFFIC_CLASS);

        return new TrafficClassCreationWizard(driver);
    }

    private void fillWizardAtCreate(TrafficClassWizardPage trafficClassWizard, TrafficClassAttributes trafficClassAttributes) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        setAttributesToCreate(trafficClassWizard, trafficClassAttributes);
    }

    private TrafficClassAttributes getTrafficClassAttributesToUpdate() {
        TrafficClassAttributes trafficClassAttributes = new TrafficClassAttributes();
        trafficClassAttributes.trafficClassName = "updated TrafficClassTest" + rand.nextInt(1000);
        trafficClassAttributes.description = "updated traffic class test description";
        trafficClassAttributes.matchType = "Any";
        trafficClassAttributes.ipPrecedence = "2";
        trafficClassAttributes.mplsExperimental = "3";
        trafficClassAttributes.mplsExperimentalTop = "4";
        trafficClassAttributes.ipDscp = "EF";
        trafficClassAttributes.accessList = "updated test access list";
        trafficClassAttributes.inputInterface = PRE_CREATED_LOCATION + "-Router-6" + "\\" + INTERFACE_UPDATE;
        trafficClassAttributes.protocol = "updated test protocol";
        trafficClassAttributes.cirIngress = "6";
        trafficClassAttributes.cirEgress = "7";
        trafficClassAttributes.pirIngress = "8";
        trafficClassAttributes.pirEgress = "9";
        return trafficClassAttributes;
    }

    private TrafficClassModificationWizardPage goToWizardAtUpdate(NewInventoryViewPage inventoryViewBeforeUpdate) {
        inventoryViewBeforeUpdate.selectFirstRow();
        inventoryViewBeforeUpdate.callAction(ActionsContainer.EDIT_GROUP_ID, EDIT_BUTTON_INSIDE_EDIT_GROUP_TEST_ID);
        return new TrafficClassModificationWizardPage(driver);
    }

    private void fillWizardAtUpdate(TrafficClassModificationWizardPage trafficClassModificationWizard, TrafficClassAttributes trafficClassAttributes) {
        setAttributesToUpdate(trafficClassModificationWizard, trafficClassAttributes);
    }

    private void setAttributesToCreate(TrafficClassWizardPage trafficClassWizard, TrafficClassAttributes trafficClassAttributes) {
        trafficClassWizard.setName(trafficClassAttributes.trafficClassName);
        trafficClassWizard.setDescription(trafficClassAttributes.description);
        trafficClassWizard.setMatchType(trafficClassAttributes.matchType);
        trafficClassWizard.setIpPrecedence(trafficClassAttributes.ipPrecedence);
        trafficClassWizard.setMpls(trafficClassAttributes.mplsExperimental);
        trafficClassWizard.setMplsTop(trafficClassAttributes.mplsExperimentalTop);
        trafficClassWizard.setIpDscp(trafficClassAttributes.ipDscp);
        trafficClassWizard.setAccessList(trafficClassAttributes.accessList);
        trafficClassWizard.setInputInterface(trafficClassAttributes.inputInterface);
        trafficClassWizard.setProtocol(trafficClassAttributes.protocol);
        trafficClassWizard.setCirIngress(trafficClassAttributes.cirIngress);
        trafficClassWizard.setCirEgress(trafficClassAttributes.cirEgress);
        trafficClassWizard.setPirIngress(trafficClassAttributes.pirIngress);
        trafficClassWizard.setPirEgress(trafficClassAttributes.pirEgress);
    }

    private void setAttributesToUpdate(TrafficClassModificationWizardPage trafficClassModificationWizard, TrafficClassAttributes trafficClassAttributes) {
        trafficClassModificationWizard.setName(trafficClassAttributes.trafficClassName);
        trafficClassModificationWizard.setDescription(trafficClassAttributes.description);
        trafficClassModificationWizard.setMatchType(trafficClassAttributes.matchType);
        trafficClassModificationWizard.setIpPrecedence(trafficClassAttributes.ipPrecedence);
        trafficClassModificationWizard.setMpls(trafficClassAttributes.mplsExperimental);
        trafficClassModificationWizard.setMplsTop(trafficClassAttributes.mplsExperimentalTop);
        trafficClassModificationWizard.setIpDscp(trafficClassAttributes.ipDscp);
        trafficClassModificationWizard.setAccessList(trafficClassAttributes.accessList);
        trafficClassModificationWizard.setInputInterface(trafficClassAttributes.inputInterface);
        trafficClassModificationWizard.setProtocol(trafficClassAttributes.protocol);
        trafficClassModificationWizard.setCirIngress(trafficClassAttributes.cirIngress);
        trafficClassModificationWizard.setCirEgress(trafficClassAttributes.cirEgress);
        trafficClassModificationWizard.setPirIngress(trafficClassAttributes.pirIngress);
        trafficClassModificationWizard.setPirEgress(trafficClassAttributes.pirEgress);
    }

    private void deleteTrafficClass(NewInventoryViewPage inventoryView) {
        inventoryView.selectFirstRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        inventoryView.callAction(ActionsContainer.EDIT_GROUP_ID, REMOVE_BUTTON_INSIDE_EDIT_GROUP_TEST_ID);
        inventoryView.clickConfirmationBox(CONFIRMATION_REMOVE_BUTTON);
    }

    private boolean hasBeenDeleted() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> popupMessages = systemMessage.getMessages();
        return popupMessages.stream().anyMatch(this::isMessageValidForDeletion);
    }

    private boolean isMessageValidForDeletion(SystemMessageContainer.Message popupMessage) {
        return popupMessage.getMessageType() == SystemMessageContainer.MessageType.SUCCESS;
    }

    private static class TrafficClassAttributes {
        private String trafficClassName;
        private String description;
        private String matchType;
        private String ipPrecedence;
        private String mplsExperimental;
        private String mplsExperimentalTop;
        private String ipDscp;
        private String accessList;
        private String inputInterface;
        private String protocol;
        private String cirIngress;
        private String cirEgress;
        private String pirIngress;
        private String pirEgress;
    }

}