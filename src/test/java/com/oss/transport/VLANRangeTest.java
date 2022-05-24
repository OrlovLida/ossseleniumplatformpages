package com.oss.transport;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.SearchObjectTypePage;
import com.oss.pages.transport.VLANRangeWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class VLANRangeTest extends BaseTestCase {
    private NewInventoryViewPage newInventoryViewPage;
    private static final String VLAN_NAME_1 = "VLANRangeSeleniumTest" + (int) (Math.random() * 1000);
    private static final String VLAN_NAME_2 = "VLANRangeSeleniumTestModified" + (int) (Math.random() * 1000);
    private static final String VLAN_RANGE_1 = "1, 3, 5-10";
    private static final String VLAN_RANGE_2 = "1, 3, 5-9";
    private static final String VLAN_DESCRIPTION_1 = "DescriptionBefore";
    private static final String VLAN_DESCRIPTION_2 = "DescriptionAfter";
    private static final String DELETE_VLAN_RANGE_ACTION_ID = "DeleteVLANRangeContextAction";
    private static final String CREATE_VLAN_RANGE_ACTION_ID = "CreateVLANRangeContextAction";

    @BeforeClass
    public void openWebConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Set fields and create VLAN Range")
    public void createVLANRange() {
        homePage.chooseFromLeftSideMenu("Inventory View", "Resource Inventory ");
        waitForPageToLoad();
        SearchObjectTypePage searchObjectType = new SearchObjectTypePage(driver, webDriverWait);
        searchObjectType.searchType("VLAN Range");
        waitForPageToLoad();
        newInventoryViewPage.callAction(ActionsContainer.CREATE_GROUP_ID, CREATE_VLAN_RANGE_ACTION_ID);
        VLANRangeWizardPage vLANRangeWizardPage = new VLANRangeWizardPage(driver);
        vLANRangeWizardPage.setName(VLAN_NAME_1)
                .setRange(VLAN_RANGE_1)
                .setDescription(VLAN_DESCRIPTION_1)
                .save();
        waitForPageToLoad();
        SystemMessageContainer.create(driver, webDriverWait).clickMessageLink();
        newInventoryViewPage.refreshMainTable();
    }

    @Test(priority = 2)
    @Description("Check if VLAN Range is created")
    public void checkIfVLANRangeIsCreated() {
        newInventoryViewPage.searchObject(VLAN_NAME_1);
        waitForPageToLoad();
        newInventoryViewPage.doRefreshWhileNoData();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 3)
    @Description("Edit VLAN Range")
    public void editVLANRange() {
        newInventoryViewPage.selectFirstRow();
        waitForPageToLoad();
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, "EditVLANRangeContextAction");
        waitForPageToLoad();
        VLANRangeWizardPage vLANRangeWizardPage = new VLANRangeWizardPage(driver);
        vLANRangeWizardPage.setName(VLAN_NAME_2)
                .setRange(VLAN_RANGE_2)
                .setDescription(VLAN_DESCRIPTION_2)
                .save();
        waitForPageToLoad();
        SystemMessageContainer.create(driver, webDriverWait).clickMessageLink();
        newInventoryViewPage.refreshMainTable();
    }

    @Test(priority = 4)
    @Description("Check if VLAN Range is edited correctly")
    public void checkIfVLANRangeIsEditedCorrectly() {
        newInventoryViewPage.searchObject(VLAN_NAME_2);
        newInventoryViewPage.doRefreshWhileNoData();
        waitForPageToLoad();
        Assert.assertEquals(newInventoryViewPage.getMainTable().getCellValue(0, "name"), VLAN_NAME_2);
        Assert.assertEquals(newInventoryViewPage.getMainTable().getCellValue(0, "description"), VLAN_DESCRIPTION_2);
    }

    @Test(priority = 5)
    @Description("Delete VLAN Range")
    public void deleteVLANRange() {
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_VLAN_RANGE_ACTION_ID);
        newInventoryViewPage.clickConfirmationRemovalButton();
        waitForPageToLoad();
        checkPopup();
        waitForPageToLoad();
        newInventoryViewPage.refreshMainTable();
        newInventoryViewPage.searchObject(VLAN_NAME_1);
        newInventoryViewPage.searchObject(VLAN_NAME_2);
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
    }

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.SUCCESS);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
