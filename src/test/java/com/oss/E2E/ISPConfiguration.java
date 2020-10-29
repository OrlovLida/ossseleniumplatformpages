package com.oss.E2E;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.*;
import io.qameta.allure.Description;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class ISPConfiguration extends BaseTestCase {

    public static final String LOCATION_NAME = "ISPConfiguration_Building";
    public static final String SUBLOCATION_NAME = "ISPConfiguration_Room";
    public static final String GEOGRAPHICAL_ADDRESS = "Test";
    String LOCATION_OVERVIEW_URL = "";
    public static final String PHYSICAL_DEVICE_MODEL = "Alcatel 7360 ISAM FX-8";
    public static final String PHYSICAL_DEVICE_NAME = "ISPPhysicalDevice";
    public static final String MODEL_UPDATE = "Alcatel 7302";
    public static final String MOUNTING_EDITOR_MODEL = "Generic 19\" 42U 600x800 (Bottom-Up)";
    public static final String MOUNTING_EDITOR_NAME = "ISPMountingEditor";

    @BeforeClass
    @Description("Open Create Location Wizard")
    public void openCreateLocationWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("Create Location", "Wizards", "Physical Inventory");
        //CardCreateWizardPage cardCreateWizardPage = new CardCreateWizardPage(driver);
        //CardCreateWizardPage.goToCardCreateWizardPageLive(driver, BASIC_URL);
    }

    @Test(priority = 1)
    @Description("Create Building")
    public void createBuilding() {
        LocationWizardPage locationWizardPage = new LocationWizardPage(driver);
        locationWizardPage.setLocationType("Building");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setLocationName(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.setGeographicalAddress(GEOGRAPHICAL_ADDRESS);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        locationWizardPage.accept();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 2)
    @Description("Show building in Location Overview")
    public void showLocationOverviewFromPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        LOCATION_OVERVIEW_URL = driver.getCurrentUrl();
    }

    @Test(priority = 3)
    @Description("Open Create Sublocation Wizard")
    public void openSublocationWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.useContextAction("Create Sublocation");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 4)
    @Description("Create Room")
    public void createRoom() {
        SublocationWizardPage sublocationWizardPage = new SublocationWizardPage(driver);
        sublocationWizardPage.setSublocationType("Room");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.setSublocationName(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        sublocationWizardPage.Accept();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 5)
    @Description("Open Create Device Wizard")
    public void openDeviceWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.useContextAction("Create Device");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 6)
    @Description("Create Physical Device")
    public void createPhysicalDevice() {
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        deviceWizardPage.setModel(PHYSICAL_DEVICE_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setName(PHYSICAL_DEVICE_NAME);
        deviceWizardPage.setPreciseLocation(LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.create();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 7)
    @Description("Show device in Device Overview")
    public void showDeviceOverviewFromPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        systemMessage.clickMessageLink();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 8)
    @Description("Open Change Model Wizard")
    public void openChangeModelWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(PHYSICAL_DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Change Model");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 9)
    @Description("Change device model")
    public void changeDeviceModel() {
        ChangeModelWizardPage changeModelWizardPage = new ChangeModelWizardPage(driver);
        changeModelWizardPage.setModel(MODEL_UPDATE);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeModelWizardPage.clickUpdate();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 10)
    @Description("Open Create Card Wizard")
    public void openCreateCardWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(PHYSICAL_DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Create Card");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 11)
    @Description("Set Card Model")
    public void setCardModel() {
        CardCreateWizardPage cardCreateWizardPage = new CardCreateWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setModel("Alcatel NELT-B");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setSlots("LT3");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.setSlots("LT4");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cardCreateWizardPage.clickAccept();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 12)
    @Description("Open Change Card Model Wizard")
    public void openChangeCardWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow("NELT-B");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Change model");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 13)
    @Description("Change Card Model")
    public void changeCardModel() {
        ChangeCardModelWizard changeCardModelWizard = new ChangeCardModelWizard(driver);
        changeCardModelWizard.setModelCard("Alcatel NGLT-A");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        changeCardModelWizard.clickUpdate();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }

    @Test(priority = 14)
    @Description("Open Mounting Editor Wizard")
    public void openMountingEditorWizard() {
        DeviceOverviewPage deviceOverviewPage = new DeviceOverviewPage(driver);
        deviceOverviewPage.selectTreeRow(PHYSICAL_DEVICE_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceOverviewPage.useContextAction("Mounting Editor");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    @Test(priority = 15)
    @Description("Set Mounting Editor")
    public void setMountingEditor() {
        MountingEditorWizardPage mountingEditorWizardPage = new MountingEditorWizardPage(driver);
        mountingEditorWizardPage.setName(MOUNTING_EDITOR_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mountingEditorWizardPage.setPreciseLocation(SUBLOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mountingEditorWizardPage.setModel(MOUNTING_EDITOR_MODEL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mountingEditorWizardPage.clickNext();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mountingEditorWizardPage.clickCheckbox();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        mountingEditorWizardPage.clickAccept();
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }
}