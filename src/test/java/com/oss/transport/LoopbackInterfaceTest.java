package com.oss.transport;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.NewInventoryViewPage;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import com.oss.pages.transport.loopbackInterface.LoopbackInterfaceWizardPage;

import io.qameta.allure.Step;

/**
 * @author Kamil Jacko
 */
public class LoopbackInterfaceTest extends BaseTestCase {

    private static final String PRE_CREATED_LOCATION_NAME = "SELENIUM_TRANSPORT_LOCATION";
    private static final String PRE_CREATED_DEVICE_NAME = "SeleniumTestDeviceLI";

    private static final String NUMBER = "1" + (int) (Math.random() * 100);
    private static final String NUMBER2 = "2" + (int) (Math.random() * 100);
    private static final String DESCRIPTION = "Opis1";
    private static final String DESCRIPTION2 = "Opis2";

    private static final String DESCRIPTION_ATTRIBUTE_NAME = "Description";
    private static final String BOTTOM_PROPERTIES_TABLE_TEST_ID = "properties(LoopbackInterface)";
    private static final String EDIT_LOOPBACK_INTERFACE_CONTEXT_ACTION_ID = "EditLoopbackInterfaceContextAction";
    private static final String DELETE_LOOPBACK_INTERFACE_CONTEXT_ACTION_ID = "DeleteLoopbackInterfaceContextAction";
    private static final String EMPTY_NUMBER_ATTRIBUTE_VALUE = "";
    private static final String EMPTY_DESCRIPTION_ATTRIBUTE_VALUE = "";

    private Map<String, String> propertyNamesToValues;

    @Test(priority = 1)
    @Step("Create Loopback Interface")
    public void create() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LoopbackInterfaceAttributes loopbackAttributes = getLoopbackAttributesToCreate();

        LoopbackInterfaceWizardPage loopbackWizard = goToLoopbackWizard();
        fillLoopbackWizardToCreate(loopbackAttributes, loopbackWizard);
        NewInventoryViewPage inventoryView = loopbackWizard.clickAccept();
        inventoryView.searchObject("Loopback" + loopbackAttributes.number);

        Assert.assertFalse(inventoryView.checkIfTableIsEmpty());

        /* assertLoopbackDescription(loopbackAttributes, inventoryView);*/
    }

    @Test(priority = 2)
    @Step("Update Loopback Interface")
    public void update() {
        LoopbackInterfaceAttributes loopbackAttributes = getLoopbackAttributesToUpdate();

        NewInventoryViewPage inventoryViewBeforeUpdate = new NewInventoryViewPage(driver, webDriverWait);
        inventoryViewBeforeUpdate.selectFirstRow();
        LoopbackInterfaceWizardPage loopbackWizard = goToEditWizard(inventoryViewBeforeUpdate);
        fillLoopbackWizardToUpdate(loopbackAttributes, loopbackWizard);
        NewInventoryViewPage inventoryView = loopbackWizard.clickAccept();
        inventoryView.searchObject("Loopback" + loopbackAttributes.number);

        Assert.assertFalse(inventoryView.checkIfTableIsEmpty());

        /*assertLoopbackDescription(loopbackAttributes, inventoryView);*/
    }

    @Test(priority = 3)
    @Step("Clear Loopback Interface attributes")
    public void clearAttributes() {
        LoopbackInterfaceAttributes loopbackAttributes = getLoopbackAttributesEmpty();

        NewInventoryViewPage inventoryViewAfterUpdate = new NewInventoryViewPage(driver, webDriverWait);
        inventoryViewAfterUpdate.selectFirstRow();
        LoopbackInterfaceWizardPage loopbackWizard = goToEditWizard(inventoryViewAfterUpdate);
        fillLoopbackWizardToClear(loopbackWizard);
        NewInventoryViewPage inventoryView = loopbackWizard.clickAccept();
        inventoryView.selectFirstRow();
        Assert.assertFalse(inventoryView.checkIfTableIsEmpty());
        /*assertLoopbackDescription(loopbackAttributes, inventoryView);*/
    }

    @Test(priority = 4)
    @Step("Remove Loopback Interface")
    public void remove() {
        NewInventoryViewPage inventoryViewBeforeDelete = new NewInventoryViewPage(driver, webDriverWait);

        inventoryViewBeforeDelete.selectFirstRow();
        inventoryViewBeforeDelete.callAction(ActionsContainer.EDIT_GROUP_ID, DELETE_LOOPBACK_INTERFACE_CONTEXT_ACTION_ID);
        inventoryViewBeforeDelete.clickConfirmationRemovalButton();

        Assert.assertTrue(isRemoveMessageCorrect());
        inventoryViewBeforeDelete.refreshMainTable();
        Assert.assertTrue(inventoryViewBeforeDelete.checkIfTableIsEmpty());
    }

    public boolean isRemoveMessageCorrect() {
        SystemMessageInterface message = SystemMessageContainer.create(driver, webDriverWait);
        return containsRemoveMessage(message.getMessages());
    }

    private LoopbackInterfaceAttributes getLoopbackAttributesToCreate() {
        LoopbackInterfaceAttributes loopbackAttributes = new LoopbackInterfaceAttributes();
        loopbackAttributes.number = NUMBER;
        loopbackAttributes.description = DESCRIPTION;
        return loopbackAttributes;
    }

    private LoopbackInterfaceAttributes getLoopbackAttributesToUpdate() {
        LoopbackInterfaceAttributes loopbackAttributes = new LoopbackInterfaceAttributes();
        loopbackAttributes.number = NUMBER2;
        loopbackAttributes.description = DESCRIPTION2;
        return loopbackAttributes;
    }

    private LoopbackInterfaceAttributes getLoopbackAttributesEmpty() {
        LoopbackInterfaceAttributes loopbackAttributes = new LoopbackInterfaceAttributes();
        loopbackAttributes.number = EMPTY_NUMBER_ATTRIBUTE_VALUE;
        loopbackAttributes.description = EMPTY_DESCRIPTION_ATTRIBUTE_VALUE;
        return loopbackAttributes;
    }

    private LoopbackInterfaceWizardPage goToLoopbackWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        driver.get(String.format("%s/#/view/transport/ip/ethernet/loopback-interface?perspective=LIVE", BASIC_URL));

        return new LoopbackInterfaceWizardPage(driver);
    }

    private LoopbackInterfaceWizardPage goToEditWizard(NewInventoryViewPage inventoryViewPage) {
        inventoryViewPage.callAction(ActionsContainer.EDIT_GROUP_ID, EDIT_LOOPBACK_INTERFACE_CONTEXT_ACTION_ID);
        return new LoopbackInterfaceWizardPage(driver);
    }

    private void fillLoopbackWizardToCreate(LoopbackInterfaceAttributes loopbackAttributes, LoopbackInterfaceWizardPage loopbackWizard) {
        fulfillFirstStep(loopbackAttributes, loopbackWizard);
        loopbackWizard.clickNextStep();
        loopbackWizard.searchLocationAndDevice(PRE_CREATED_LOCATION_NAME, PRE_CREATED_DEVICE_NAME);
    }

    private void fillLoopbackWizardToUpdate(LoopbackInterfaceAttributes loopbackAttributes, LoopbackInterfaceWizardPage loopbackWizard) {
        fulfillFirstStep(loopbackAttributes, loopbackWizard);
    }

    private void fillLoopbackWizardToClear(LoopbackInterfaceWizardPage loopbackWizard) {
        loopbackWizard.clearNumber();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        loopbackWizard.clearDescription();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void fulfillFirstStep(LoopbackInterfaceAttributes loopbackAttributes, LoopbackInterfaceWizardPage loopbackWizard) {
        loopbackWizard.setNumber(loopbackAttributes.number);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        loopbackWizard.setDescription(loopbackAttributes.description);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void assertLoopbackDescription(LoopbackInterfaceAttributes loopbackAttributes, OldInventoryViewPage inventoryViewPage) {
        propertyNamesToValues = inventoryViewPage.getProperties(BOTTOM_PROPERTIES_TABLE_TEST_ID);

        boolean isDescriptionSetCorrectly = isDescriptionAttributeCorrect(loopbackAttributes.description);

        Assert.assertTrue(isDescriptionSetCorrectly);
    }

    private boolean isDescriptionAttributeCorrect(String expectedValue) {
        if (expectedValue.equals(EMPTY_DESCRIPTION_ATTRIBUTE_VALUE)) {
            return !propertyNamesToValues.containsKey(DESCRIPTION_ATTRIBUTE_NAME);
        }
        return propertyNamesToValues.get(DESCRIPTION_ATTRIBUTE_NAME).equals(expectedValue);
    }

    private boolean containsRemoveMessage(List<SystemMessageContainer.Message> messages) {
        return messages.stream().anyMatch(this::isRemovalMessageValid);
    }

    private boolean isRemovalMessageValid(SystemMessageContainer.Message popupMessage) {
        return popupMessage.getMessageType() == SystemMessageContainer.MessageType.SUCCESS;
    }

    private static class LoopbackInterfaceAttributes {
        private String number;
        private String description;
    }
}
