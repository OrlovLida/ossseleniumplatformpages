package com.oss.physical.devices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.HierarchyViewPage;

import io.qameta.allure.Step;

public class CreateDeviceAndCheckItsAttributesTest extends BaseTestCase {

    private static final String MODEL_VALUE = "";
    private static final String EQUIPMENT_TYPE_ATTRIBUTE = "Equipment Type";
    private static final String EQUIPMENT_TYPE_VALUE = "DWDM Device";
    private static final String NAME_ATTRIBUTE = "Name";
    private static final String NAME_VALUE = "Selenium Test Device";
    private static final String SERIAL_NUMBER_ATTRIBUTE = "Serial Number";
    private static final String SERIAL_NUMBER_VALUE = "serial number";
    private static final String HOSTNAME_ATTRIBUTE = "Hostname";
    private static final String HOSTNAME_VALUE = "hostname";
    private static final String FIRMWARE_VERSION_ATTRIBUTE = "Firmware Licence";
    private static final String FIRMWARE_VERSION_VALUE = "firmware version";
    private static final String HARDWARE_VERSION_ATTRIBUTE = "Hardware Licence";
    private static final String HARDWARE_VERSION_VALUE = "hardware version";
    private static final String DESCRIPTION_ATTRIBUTE = "Description";
    private static final String DESCRIPTION_VALUE = "description";
    private static final String REMARKS_ATTRIBUTE = "Remarks";
    private static final String REMARKS_VALUE = "remarks";
    private static final String LOCATION_VALUE = "";

    private static final String DELETE_DEVICE_WIZARD_ACTION_ID = "DeleteDeviceWizardAction";
    private static final String PROPERTY_PANEL_WIDGET_ID = "PropertyPanelWidget";

    private DeviceWizardPage deviceWizard;
    private SoftAssert softAssert;
    private HierarchyViewPage hierarchyViewPage;
    private HashMap<String, String> attributesLabelToValueMap;

    @BeforeClass
    private void init() {
        deviceWizard = new DeviceWizardPage(driver);
        softAssert = new SoftAssert();
        hierarchyViewPage = new HierarchyViewPage(driver);
        attributesLabelToValueMap = new HashMap<>();
    }

    @Test(description = "Create device with all basic attributes filled in", priority = 1)
    public void createDevice() {
        openCreateDeviceWizardFromLeftSideMenu();

        fulfillFirstStep();
        deviceWizard.next();
        fulfillSecondStep();
        deviceWizard.accept();

        checkSystemMessage("Device has been created successfully");
        clickSystemMessageLink();
        closeSystemMessage();
    }

    @Test(description = "Check device attributes on property panel", priority = 2)
    public void checkDeviceAttributes() {
        hierarchyViewPage.selectFirstObject();
        loadPropertyPanelAttributesAndValues();
        checkDeviceAttributesAndValues();
        softAssert.assertAll();
    }

    @AfterClass
    private void deleteCreatedDevice() {
        if (!hierarchyViewPage.getMainTree().getNode(0).isToggled()) {
            hierarchyViewPage.selectFirstObject();
        }
        deleteDevice();
        checkSystemMessage("Element deleted successfully");
    }

    @Step("Open Create Device wizard from left side menu")
    private void openCreateDeviceWizardFromLeftSideMenu() {
        homePage.chooseFromLeftSideMenu("Create Device", "Infrastructure management", "Create Infrastructure");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Step("Fulfill device attributes in first step")
    private void fulfillFirstStep() {
        deviceWizard.setEquipmentType(EQUIPMENT_TYPE_VALUE);
        deviceWizard.setModel(MODEL_VALUE);
        deviceWizard.setName(NAME_VALUE + "_" + new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(Calendar.getInstance().getTime()));
        deviceWizard.setSerialNumber(SERIAL_NUMBER_VALUE);
        deviceWizard.setHostname(HOSTNAME_VALUE);
        deviceWizard.setFirmwareVersion(FIRMWARE_VERSION_VALUE);
        deviceWizard.setHardwareVersion(HARDWARE_VERSION_VALUE);
        deviceWizard.setDescription(DESCRIPTION_VALUE);
        deviceWizard.setRemarks(REMARKS_VALUE);
    }

    @Step("Fulfill location attributes in second step")
    private void fulfillSecondStep() {
        deviceWizard.setLocation(LOCATION_VALUE);
        deviceWizard.setPhysicalLocation(LOCATION_VALUE);
        deviceWizard.setPreciseLocation(LOCATION_VALUE);
    }

    @Step("Check system message")
    private void checkSystemMessage(String message) {
        Assert.assertTrue(getMessageWithText(message).isPresent());
    }

    @Step("Click system message link")
    private void clickSystemMessageLink() {
        SystemMessageContainer.create(driver, webDriverWait).clickMessageLink();
    }

    @Step("Close system message")
    private void closeSystemMessage() {
        SystemMessageContainer.create(driver, webDriverWait).close();
    }

    @Step("Load device attributes and values")
    private void loadPropertyPanelAttributesAndValues() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        PropertyPanel propertyPanel = PropertyPanel.createById(driver, webDriverWait, PROPERTY_PANEL_WIDGET_ID);
        List<String> propertyPanelAttributesList = propertyPanel.getVisibleAttributes();

        for (int i = 0; i < propertyPanel.getVisibleAttributes().size(); i++) {
            attributesLabelToValueMap.put(propertyPanel.getPropertyLabels().get(i), propertyPanel.getPropertyValue(propertyPanelAttributesList.get(i)));
        }
    }

    @Step("Check correctness of saved attributes for created device")
    private void checkDeviceAttributesAndValues() {
        softAssert.assertNotNull(attributesLabelToValueMap.get(NAME_ATTRIBUTE), "Name attribute doesn't exist on Property Panel");
        if (attributesLabelToValueMap.get(NAME_ATTRIBUTE) != null) {
            softAssert.assertEquals(attributesLabelToValueMap.get(NAME_ATTRIBUTE).split("_")[0], NAME_VALUE);
        }

        softAssert.assertNotNull(attributesLabelToValueMap.get(EQUIPMENT_TYPE_ATTRIBUTE), "Equipment Type attribute doesn't exist on Property Panel");
        if (attributesLabelToValueMap.get(EQUIPMENT_TYPE_ATTRIBUTE) != null) {
            softAssert.assertEquals(attributesLabelToValueMap.get(EQUIPMENT_TYPE_ATTRIBUTE), EQUIPMENT_TYPE_VALUE.split(" ")[0]);
        }

        softAssert.assertNotNull(attributesLabelToValueMap.get(SERIAL_NUMBER_ATTRIBUTE), "Serial Number attribute doesn't exist on Property Panel");
        if (attributesLabelToValueMap.get(SERIAL_NUMBER_ATTRIBUTE) != null) {
            softAssert.assertEquals(attributesLabelToValueMap.get(SERIAL_NUMBER_ATTRIBUTE), SERIAL_NUMBER_VALUE);
        }

        softAssert.assertNotNull(attributesLabelToValueMap.get(HOSTNAME_ATTRIBUTE), "Hostname attribute doesn't exist on Property Panel");
        if (attributesLabelToValueMap.get(HOSTNAME_ATTRIBUTE) != null) {
            softAssert.assertEquals(attributesLabelToValueMap.get(HOSTNAME_ATTRIBUTE), HOSTNAME_VALUE);
        }

        softAssert.assertNotNull(attributesLabelToValueMap.get(FIRMWARE_VERSION_ATTRIBUTE), "Firmware Licence attribute doesn't exist on Property Panel");
        if (attributesLabelToValueMap.get(FIRMWARE_VERSION_ATTRIBUTE) != null) {
            softAssert.assertEquals(attributesLabelToValueMap.get(FIRMWARE_VERSION_ATTRIBUTE), FIRMWARE_VERSION_VALUE);
        }

        softAssert.assertNotNull(attributesLabelToValueMap.get(HARDWARE_VERSION_ATTRIBUTE), "Hardware Licence attribute doesn't exist on Property Panel");
        if (attributesLabelToValueMap.get(HARDWARE_VERSION_ATTRIBUTE) != null) {
            softAssert.assertEquals(attributesLabelToValueMap.get(HARDWARE_VERSION_ATTRIBUTE), HARDWARE_VERSION_VALUE);
        }

        softAssert.assertNotNull(attributesLabelToValueMap.get(DESCRIPTION_ATTRIBUTE), "Description attribute doesn't exist on Property Panel");
        if (attributesLabelToValueMap.get(DESCRIPTION_ATTRIBUTE) != null) {
            softAssert.assertEquals(attributesLabelToValueMap.get(DESCRIPTION_ATTRIBUTE), DESCRIPTION_VALUE);
        }

        softAssert.assertNotNull(attributesLabelToValueMap.get(REMARKS_ATTRIBUTE), "Remarks attribute doesn't exist on Property Panel");
        if (attributesLabelToValueMap.get(REMARKS_ATTRIBUTE) != null) {
            softAssert.assertEquals(attributesLabelToValueMap.get(REMARKS_ATTRIBUTE), REMARKS_VALUE);
        }
    }

    @Step("Delete created device")
    private void deleteDevice() {
        hierarchyViewPage.useTreeContextAction("EDIT", DELETE_DEVICE_WIZARD_ACTION_ID);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hierarchyViewPage.clickButtonInConfirmationBox("Yes");
    }

    private Optional<SystemMessageContainer.Message> getMessageWithText(String message) {
        return SystemMessageContainer.create(driver, webDriverWait)
                .getMessages()
                .stream()
                .filter(m -> m.getText().contains(message))
                .findFirst();
    }
}