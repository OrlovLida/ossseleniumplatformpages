package com.oss.transport;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageContainer.Message;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.transport.VLANPoolWizardPage;

import io.qameta.allure.Description;

public class VLANPoolTest extends BaseTestCase {
    private NewInventoryViewPage newInventoryViewPage;
    private static final String LOCATION_NAME = "SELENIUM_TRANSPORT_LOCATION";
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
    private static final String CONFIRM_REMOVAL_BUTTON_ID = "ConfirmationBox_deleteBoxAppId_action_button";

    @BeforeClass
    public void openWebConsole() {
        waitForPageToLoad();
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        waitForPageToLoad();
    }

    @Test(priority = 1)
    @Description("Set fields and create VLAN Pool")
    public void createVLANPool() {
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory");
        waitForPageToLoad();
        SearchObjectTypePage searchObjectType = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectType.searchType("VLAN Pool");
        waitForPageToLoad();
        newInventoryViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_VLAN_POOL_ACTION_ID);
        waitForPageToLoad();
        VLANPoolWizardPage wizardPage = new VLANPoolWizardPage(driver);
        wizardPage.setName(VLAN_POOL_NAME_BEFORE);
        waitForPageToLoad();
        wizardPage.setDescription(VLAN_DESCRIPTION_BEFORE);
        waitForPageToLoad();
        wizardPage.setVlanRange(VLAN_RANGE_BEFORE);
        waitForPageToLoad();
        wizardPage.clickNextStep();
        waitForPageToLoad();
        wizardPage.selectLocationAndDevice(LOCATION_NAME, IPDEVICE_NAME);
        waitForPageToLoad();
        wizardPage.clickAccept();
        waitForPageToLoad();
        SystemMessageContainer.create(driver, webDriverWait).clickMessageLink();
    }

    @Test(priority = 2)
    @Description("Check if VLAN Pool is created")
    public void checkIfVLANPoolIsCreated() {
        newInventoryViewPage.searchObject(VLAN_POOL_NAME_AFTER);
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        newInventoryViewPage.searchObject(VLAN_POOL_NAME_BEFORE);
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 3)
    @Description("Edit VLAN Pool")
    public void editVLANPool() {
        newInventoryViewPage.selectFirstRow().callAction(ActionsContainer.EDIT_GROUP_ID, EDIT_VLAN_POOL_ACTION_ID);
        waitForPageToLoad();
        VLANPoolWizardPage wizardPage = new VLANPoolWizardPage(driver);
        wizardPage.setName(VLAN_POOL_NAME_AFTER);
        waitForPageToLoad();
        wizardPage.setDescription(VLAN_DESCRIPTION_AFTER);
        waitForPageToLoad();
        wizardPage.setVlanRange(VLAN_RANGE_AFTER);
        waitForPageToLoad();
        wizardPage.clickNextStep();
        waitForPageToLoad();
        wizardPage.clickAccept();
        waitForPageToLoad();
        SystemMessageContainer.create(driver, webDriverWait).clickMessageLink();
    }

    @Test(priority = 4)
    @Description("Check if VLAN Pool is edited")
    public void checkIfVLANPoolIsEdited() {
        newInventoryViewPage.unselectObjectByRowId(0);
        waitForPageToLoad();
        newInventoryViewPage.searchObject(VLAN_POOL_NAME_BEFORE);
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
        waitForPageToLoad();
        newInventoryViewPage.searchObject(VLAN_POOL_NAME_AFTER);
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 5)
    @Description("Delete VLAN Pool")
    public void deleteVLANPool() {
        newInventoryViewPage.selectFirstRow().callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_VLAN_POOL_ACTION_ID).clickConfirmationBox(CONFIRM_REMOVAL_BUTTON_ID);
        checkPopup();
        waitForPageToLoad();
        newInventoryViewPage.unselectObjectByRowId(0);
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        newInventoryViewPage.searchObject(VLAN_POOL_NAME_BEFORE);
        waitForPageToLoad();
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
        waitForPageToLoad();
        newInventoryViewPage.searchObject(VLAN_POOL_NAME_AFTER);
        newInventoryViewPage.refreshMainTable();
        waitForPageToLoad();
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
    }

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.SUCCESS);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
