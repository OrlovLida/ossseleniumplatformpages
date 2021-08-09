package com.oss.transport;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.mainheader.PerspectiveChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.transport.VLANRangeWizardPage;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({ TestListener.class })
public class VLANRangeTest extends BaseTestCase {

    private static final String VLAN_NAME_1 = "VLANRangeSeleniumTest";
    private static final String VLAN_NAME_2 = "VLANRangeSeleniumTestModified";
    private static final String VLAN_RANGE_1 = "1, 3, 5-10";
    private static final String VLAN_RANGE_2 = "1, 3, 5-9";
    private static final String VLAN_DESCRIPTION_1 = "DescriptionBefore";
    private static final String VLAN_DESCRIPTION_2 = "DescriptionAfter";

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertNotNull(messages);
        Assert.assertEquals(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType(),
                SystemMessageContainer.MessageType.SUCCESS);
    }

    @BeforeClass
    public void openWebConsole() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 1)
    @Description("Set fields and create VLAN Range")
    public void createVLANRange() {
        driver.get("https://10.132.118.207:25081/#/view/transport/ip/ethernet/vlan-range/create?perspective=LIVE");
        //homePage.chooseFromLeftSideMenu("Create VLAN Range", "Network domains", "Transport & IP");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        VLANRangeWizardPage vLANRangeWizardPage = new VLANRangeWizardPage(driver);
        vLANRangeWizardPage.setName(VLAN_NAME_1)
                .setRange(VLAN_RANGE_1)
                .setDescription(VLAN_DESCRIPTION_1)
                .save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 2)
    @Description("Check if VLAN Range is created")
    public void checkIfVLANRangeIsCreated() {
        HomePage homePage = new HomePage(driver)
                .goToHomePage(driver, BASIC_URL);
        homePage.chooseFromLeftSideMenu("Legacy Inventory Dashboard", "Resource Inventory");
        homePage.setNewObjectType("VLAN Range");
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(VLAN_NAME_1);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.doRefreshWhileNoData();
        Assert.assertFalse(newInventoryViewPage.checkIfTableIsEmpty());
    }

    @Test(priority = 3)
    @Description("Edit VLAN Range")
    public void editVLANRange() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, "EditVLANRangeContextAction");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        VLANRangeWizardPage vLANRangeWizardPage = new VLANRangeWizardPage(driver);
        vLANRangeWizardPage.setName(VLAN_NAME_2)
                .setRange(VLAN_RANGE_2)
                .setDescription(VLAN_DESCRIPTION_2)
                .save();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    @Description("Check if VLAN Range is edited correctly")
    public void checkIfVLANRangeIsEditedCorrectly() {
        HomePage homePage = new HomePage(driver)
                .goToHomePage(driver, BASIC_URL);
        homePage.chooseFromLeftSideMenu("Legacy Inventory Dashboard", "Resource Inventory");
        homePage.setNewObjectType("VLAN Range");
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.searchObject(VLAN_NAME_2);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(newInventoryViewPage.getMainTable().getCellValue(0, "Name"), VLAN_NAME_2);
        Assert.assertEquals(newInventoryViewPage.getMainTable().getCellValue(0, "Description"), VLAN_DESCRIPTION_2);
    }

    @Test(priority = 5)
    @Description("Delete VLAN Range")
    public void deleteVLANRange() {
        NewInventoryViewPage newInventoryViewPage = new NewInventoryViewPage(driver, webDriverWait);
        newInventoryViewPage.selectFirstRow();
        newInventoryViewPage.callActionById("DeleteVLANRangeContextAction");
        newInventoryViewPage.getWizard().clickButtonByLabel("OK");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        checkPopup();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        newInventoryViewPage.getMainTable()
                .callAction(ActionsContainer.KEBAB_GROUP_ID, TableWidget.REFRESH_ACTION_ID);
        Assert.assertTrue(newInventoryViewPage.checkIfTableIsEmpty());
    }
}