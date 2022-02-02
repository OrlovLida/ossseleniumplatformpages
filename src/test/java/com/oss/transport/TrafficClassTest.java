package com.oss.transport;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import com.oss.pages.transport.traffic.classs.TrafficClassCreationWizard;
import com.oss.pages.transport.traffic.classs.TrafficClassModificationWizardPage;
import com.oss.pages.transport.traffic.classs.TrafficClassWizardPage;

import io.qameta.allure.Step;

/**
 * @author Kamil Sikora
 */
public class TrafficClassTest extends BaseTestCase {

    private static final String PRE_CREATED_LOCATION = "SELENIUM_TRANSPORT_LOCATION";
    private static final String PRE_CREATED_DEVICE = "SeleniumTestDeviceTC";
    private static final String INTERFACE_CREATE = "MGT LAN 0\\1";
    private static final String INTERFACE_UPDATE = "SFP+ 0\\1";

    private static final String PROPERTIES_BOTTOM_TABLE_TEST_ID = "properties(TrafficClass)";
    private static final String EDIT_BUTTON_INSIDE_EDIT_GROUP_TEST_ID = "TrafficClassEditContextAction";
    private static final String REMOVE_BUTTON_INSIDE_EDIT_GROUP_TEST_ID = "TrafficClassDeleteContextAction";

    Random rand = new Random();
    private Map<String, String> propertyNameToPropertyValue;

    @Test(priority = 1)
    @Step("Create Traffic Class")
    public void createTrafficClass() {
        TrafficClassAttributes trafficClassAttributes = getTrafficClassAttributesToCreate();

        TrafficClassCreationWizard trafficClassWizard = goToWizardAtCreate();
        fillWizardAtCreate(trafficClassWizard, trafficClassAttributes);
        OldInventoryViewPage inventoryViewPage = trafficClassWizard.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        propertyNameToPropertyValue = inventoryViewPage.getProperties(PROPERTIES_BOTTOM_TABLE_TEST_ID);

        assertTrafficClassAttributes(trafficClassAttributes);
    }

    @Test(priority = 2)
    @Step("Update Traffic Class attributes")
    public void updateTrafficClass() {
        TrafficClassAttributes trafficClassAttributes = getTrafficClassAttributesToUpdate();

        OldInventoryViewPage inventoryViewBeforeUpdate = new OldInventoryViewPage(driver);
        TrafficClassModificationWizardPage trafficClassModificationWizard = goToWizardAtUpdate(inventoryViewBeforeUpdate);
        fillWizardAtUpdate(trafficClassModificationWizard, trafficClassAttributes);
        OldInventoryViewPage inventoryViewAfterUpdate = trafficClassModificationWizard.clickSaveChanges();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        propertyNameToPropertyValue = inventoryViewAfterUpdate.getProperties(PROPERTIES_BOTTOM_TABLE_TEST_ID);

        assertTrafficClassAttributes(trafficClassAttributes);
    }

    @Test(priority = 3)
    @Step("Remove created Traffic Class")
    public void removeTrafficClass() {
        OldInventoryViewPage inventoryView = new OldInventoryViewPage(driver);

        deleteTrafficClass(inventoryView);
        boolean hasBeenDeleted = hasBeenDeleted();

        Assert.assertTrue(hasBeenDeleted);
    }

    private TrafficClassAttributes getTrafficClassAttributesToCreate() {
        TrafficClassAttributes trafficClassAttributes = new TrafficClassAttributes();
        trafficClassAttributes.location = PRE_CREATED_LOCATION;
        trafficClassAttributes.device = PRE_CREATED_DEVICE;
        trafficClassAttributes.trafficClassName = "TrafficClassTest" + rand.nextInt(1000);
        trafficClassAttributes.description = "traffic class test description";
        trafficClassAttributes.matchType = "All";
        trafficClassAttributes.ipPrecedence = "5";
        trafficClassAttributes.mplsExperimental = "2";
        trafficClassAttributes.mplsExperimentalTop = "5";
        trafficClassAttributes.ipDscp = "BE";
        trafficClassAttributes.accessList = "test access list";
        trafficClassAttributes.inputInterface = PRE_CREATED_LOCATION + "-Router-1" + "\\" + INTERFACE_CREATE;
        trafficClassAttributes.protocol = "test protocol";
        return trafficClassAttributes;
    }

    private TrafficClassCreationWizard goToWizardAtCreate() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        driver.get(String.format("%s/#/view/transport/ip/ethernet/traffic-class?perspective=LIVE", BASIC_URL));
        return new TrafficClassCreationWizard(driver);
    }

    private void fillWizardAtCreate(TrafficClassWizardPage trafficClassWizard, TrafficClassAttributes trafficClassAttributes) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        trafficClassWizard.selectLocationAndDevice(trafficClassAttributes.location, trafficClassAttributes.device);
        trafficClassWizard.clickNextStep();
        setAttributesToCreate(trafficClassWizard, trafficClassAttributes);
    }

    private TrafficClassAttributes getTrafficClassAttributesToUpdate() {
        TrafficClassAttributes trafficClassAttributes = new TrafficClassAttributes();
        trafficClassAttributes.location = PRE_CREATED_LOCATION;
        trafficClassAttributes.device = PRE_CREATED_DEVICE;
        trafficClassAttributes.trafficClassName = "updated TrafficClassTest";
        trafficClassAttributes.description = "updated traffic class test description";
        trafficClassAttributes.matchType = "Any";
        trafficClassAttributes.ipPrecedence = "2";
        trafficClassAttributes.mplsExperimental = "3";
        trafficClassAttributes.mplsExperimentalTop = "4";
        trafficClassAttributes.ipDscp = "EF";
        trafficClassAttributes.accessList = "updated test access list";
        trafficClassAttributes.inputInterface = PRE_CREATED_LOCATION + "-Router-1" + "\\" + INTERFACE_UPDATE;
        trafficClassAttributes.protocol = "updated test protocol";
        return trafficClassAttributes;
    }

    private TrafficClassModificationWizardPage goToWizardAtUpdate(OldInventoryViewPage inventoryViewBeforeUpdate) {
        inventoryViewBeforeUpdate.selectRowInTableAtIndex(0);
        inventoryViewBeforeUpdate.expandEditAndChooseAction(EDIT_BUTTON_INSIDE_EDIT_GROUP_TEST_ID);
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
    }

    private void assertTrafficClassAttributes(TrafficClassAttributes trafficClassAttributes) {
        String trafficClassName = propertyNameToPropertyValue.get("Name");
        String description = propertyNameToPropertyValue.get("Description");
        String accessList = propertyNameToPropertyValue.get("Access List");
        String ipDscp = propertyNameToPropertyValue.get("IP DSCP");
        String ipPrecedence = propertyNameToPropertyValue.get("IP Precedence");
        String matchType = propertyNameToPropertyValue.get("Match");
        String mpsl = propertyNameToPropertyValue.get("MPLS Experimental");
        String mpslTop = propertyNameToPropertyValue.get("MPLS Experimental Topmost");
        String protocol = propertyNameToPropertyValue.get("Protocol");

        Assert.assertEquals(trafficClassName, trafficClassAttributes.trafficClassName);
        Assert.assertEquals(description, trafficClassAttributes.description);
        Assert.assertEquals(accessList, trafficClassAttributes.accessList);
        Assert.assertEquals(ipDscp, trafficClassAttributes.ipDscp);
        Assert.assertEquals(ipPrecedence, trafficClassAttributes.ipPrecedence);
        Assert.assertEquals(matchType, trafficClassAttributes.matchType);
        Assert.assertEquals(mpsl, trafficClassAttributes.mplsExperimental);
        Assert.assertEquals(mpslTop, trafficClassAttributes.mplsExperimentalTop);
        Assert.assertEquals(protocol, trafficClassAttributes.protocol);
    }

    private void deleteTrafficClass(OldInventoryViewPage inventoryView) {
        inventoryView.selectRowInTableAtIndex(0);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        inventoryView.expandEditAndChooseAction(REMOVE_BUTTON_INSIDE_EDIT_GROUP_TEST_ID);
        inventoryView.clickConfirmRemovalButton();
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
        private String location;
        private String device;
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
    }

}