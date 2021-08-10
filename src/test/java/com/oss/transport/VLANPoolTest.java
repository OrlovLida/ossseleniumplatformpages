package com.oss.transport;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import com.oss.pages.transport.VLANPoolWizardPage;

import io.qameta.allure.Description;

public class VLANPoolTest extends BaseTestCase {

    private static final String LOCATION_NAME = "VLANPoolSeleniumTest";
    private static final String IPDEVICE_NAME = "VLANPoolSeleniumTest";
    private static final String VLAN_POOL_NAME_BEFORE = "VLANPoolSeleniumTestBefore";
    private static final String VLAN_POOL_NAME_AFTER = "VLANPoolSeleniumTestAfter";
    private static final String VLAN_RANGE_BEFORE = "VLANPool1Test";
    private static final String VLAN_RANGE_AFTER = "VLANPool2Test";
    private static final String VLAN_DESCRIPTION_BEFORE = "Description before modification";
    private static final String VLAN_DESCRIPTION_AFTER = "Description after modification";
    private static final String CREATE_VLAN_POOL_ACTION_ID = "CreateVLANPoolContextAction";
    private static final String EDIT_VLAN_POOL_ACTION_ID = "EditVLANPoolContextAction";
    private static final String DELETE_VLAN_POOL_ACTION_ID = "DeleteVLANPoolContextAction";
    private static final String CONFIRM_DELETE_ID = "ConfirmationBox_deleteBoxAppId_action_button";

    @BeforeClass
    public void openWebConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Set fields and create VLAN Pool")
    public void createVLANPool() {
        homePage.chooseFromLeftSideMenu("Legacy Inventory Dashboard", "Resource Inventory ");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.setOldObjectType("VLAN Pool");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.useContextAction(ActionsContainer.CREATE_GROUP_ID, CREATE_VLAN_POOL_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        VLANPoolWizardPage wizardPage = new VLANPoolWizardPage(driver);
        wizardPage.setName(VLAN_POOL_NAME_BEFORE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizardPage.setDescription(VLAN_DESCRIPTION_BEFORE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizardPage.setVlanRange(VLAN_RANGE_BEFORE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizardPage.clickNextStep();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizardPage.selectLocationAndDevice(LOCATION_NAME, IPDEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizardPage.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    @Description("Check if VLAN Pool is created")
    public void checkIfVLANPoolIsCreated() {
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.getTableWidget().searchByAttributeWithLabel("Name", ComponentType.TEXT_FIELD, VLAN_POOL_NAME_AFTER);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(oldInventoryViewPage.getTableWidget().hasNoData());
        oldInventoryViewPage.filterObject("Name", VLAN_POOL_NAME_BEFORE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertFalse(oldInventoryViewPage.getTableWidget().hasNoData());
    }

    @Test(priority = 3)
    @Description("Edit VLAN Pool")
    public void editVLANPool() {
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, EDIT_VLAN_POOL_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        VLANPoolWizardPage wizardPage = new VLANPoolWizardPage(driver);
        wizardPage.setName(VLAN_POOL_NAME_AFTER);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizardPage.setDescription(VLAN_DESCRIPTION_AFTER);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizardPage.setVlanRange(VLAN_RANGE_AFTER);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizardPage.clickNextStep();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        wizardPage.clickAccept();
    }

    @Test(priority = 4)
    @Description("Check if VLAN Pool is edited")
    public void checkIfVLANPoolIsEdited() {
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.getTableWidget().searchByAttributeWithLabel("Name", ComponentType.TEXT_FIELD, VLAN_POOL_NAME_BEFORE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(oldInventoryViewPage.getTableWidget().hasNoData());
        oldInventoryViewPage.filterObject("Name", VLAN_POOL_NAME_AFTER);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertFalse(oldInventoryViewPage.getTableWidget().hasNoData());
    }

    @Test(priority = 5)
    @Description("Delete VLAN Range")
    public void deleteVLANRange() {
        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.useContextAction(ActionsContainer.EDIT_GROUP_ID, DELETE_VLAN_POOL_ACTION_ID);
        oldInventoryViewPage.getWizard().callButtonById(CONFIRM_DELETE_ID);
        checkPopup();
        oldInventoryViewPage.getTableWidget().searchByAttributeWithLabel("Name", ComponentType.TEXT_FIELD, VLAN_POOL_NAME_BEFORE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(oldInventoryViewPage.getTableWidget().hasNoData());
        oldInventoryViewPage.getTableWidget().searchByAttributeWithLabel("Name", ComponentType.TEXT_FIELD, VLAN_POOL_NAME_AFTER);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertTrue(oldInventoryViewPage.getTableWidget().hasNoData());
    }

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.SUCCESS);
    }
}
