package com.oss.web;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.TableWidget;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.NewInventoryViewPage;

public class PhysicalDeviceInventoryViewTest extends BaseTestCase {

    private static String PHYSICAL_DEVICE_TYPE = "PhysicalDevice";

    private static String PHYSICAL_DEVICE_NAME = "PhysicalDevice_IV_TEST";

    private static String PHYSICAL_DEVICE_CREATE_ACTION_ID = "CreateDeviceWithoutSelectionWizardAction";
    private static String PHYSICAL_DEVICE_DELETE_ACTION_ID = "DeleteDeviceWizardAction";

    private NewInventoryViewPage inventoryViewPage;

    @BeforeClass
    public void goToInventoryView() {
        inventoryViewPage = NewInventoryViewPage.goToInventoryViewPage(driver, BASIC_URL, PHYSICAL_DEVICE_TYPE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test
    public void createPhysicalDevice () {
        TableWidget physicalDeviceTable = inventoryViewPage.getMainTable();
        physicalDeviceTable.callAction(ActionsContainer.CREATE_GROUP_ID, PHYSICAL_DEVICE_CREATE_ACTION_ID);

        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.createDeviceOnlyByName(PHYSICAL_DEVICE_NAME);

        //Wait till indexed
        DelayUtils.sleep(3000);

        physicalDeviceTable.typeIntoSearch(PHYSICAL_DEVICE_NAME);
        DelayUtils.sleep(3000);

        int rows = physicalDeviceTable.getRowsNumber();
        String nameValue = physicalDeviceTable.getValueFromNthRow("Name", 1);
        System.out.println("");

    }

    @Test
    public void fullTextSearchTest() {
        TableWidget tableWidget = inventoryViewPage.getMainTable();
        String secondID = tableWidget.getValueFromNthRow("XId", 2);
        tableWidget.typeIntoSearch(secondID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        String newFirstID = tableWidget.getValueFromNthRow("XId", 1);
        Assert.assertEquals(secondID, newFirstID);
        Assert.assertEquals(tableWidget.getRowsNumber(), 1);
    }

    public void searchByName() {

    }

    public void searchByLocationAndName() {

    }

    public void searchByEquipmentTypeAndName() {

    }

    //TODO
    public void searchByCreatedDateAndName() {

    }

    public void compareMandatoryAttributesWithPropertyPanel() {

    }

    public void enableHiddenAttributeOnMainTable() {

    }

    public void changeAttributesOrderOnMainTable() {

    }

    public void enableHiddenAttributeOnPropertyPanel() {

    }

    public void changeAttributesOrderOnPropertyPanel () {

    }

    public void enableHiddenTab() {

    }

    public void changeOrderOfTabs() {

    }

    @Test
    public void deletePhysicalDevice () {
        TableWidget physicalDeviceTable = inventoryViewPage.getMainTable();
        physicalDeviceTable.typeIntoSearch(PHYSICAL_DEVICE_NAME);
        DelayUtils.sleep(3000);
        physicalDeviceTable.selectFirstRow();
        physicalDeviceTable.callAction(ActionsContainer.EDIT_GROUP_ID, PHYSICAL_DEVICE_DELETE_ACTION_ID);

        ConfirmationBoxInterface confirmationBox = ConfirmationBox.create(driver, webDriverWait);
        confirmationBox.clickButtonByLabel("Yes");


    }

}