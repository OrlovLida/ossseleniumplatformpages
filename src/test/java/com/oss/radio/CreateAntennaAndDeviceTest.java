package com.oss.radio;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.radio.AntennaArrayWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.RanAntennaWizardPage;

import io.qameta.allure.Step;

import static java.lang.String.format;

public class CreateAntennaAndDeviceTest extends BaseTestCase {
    private CellSiteConfigurationPage cellSiteConfigurationPage;
    private AntennaArrayWizardPage antennaArrayWizardPage;
    private String LOCATION_NAME = "SeleniumLocationTest";
    private String BASE_BAND_UNIT_MODEL = "HUAWEI Technology Co.,Ltd BBU5900";
    private String RADIO_UNIT_MODEL = "HUAWEI Technology Co.,Ltd RRU5301";
    private String RAN_ANTENNA_MODEL = "HUAWEI Technology Co.,Ltd APE4518R14V06";
    private String BBU_NAME = "SeleniumTestBBU";
    private String RADIO_UNIT_NAME = "SeleniumTestRadioUnit";
    private String ANTENNA_NAME = "SeleniumTestAntenna";
    private String BBU_EQUIPMENT_TYPE = "Base Band Unit";
    private String RADIO_UNIT_EQUIPMENT_TYPE = "Remote Radio Head/Unit";

    public static void goToCellSiteConfiguration(WebDriver driver, String basicURL) {
        driver.get(format("%s#/view/radio/cellsite/xid?ids=75521556&perspective=LIVE", basicURL));
    }

    @BeforeClass
    public void goToCellSiteConfiguration() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        goToCellSiteConfiguration(driver, BASIC_URL);
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
    }

    @Step("Create Base Band Unit")
    @Test(priority = 1)
    public void createBaseBandUnit() {
        cellSiteConfigurationPage.expandTreeToLocation("Site", LOCATION_NAME);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Devices");

        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create Device");
        DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        deviceWizardPage.setEquipmentType(BBU_EQUIPMENT_TYPE);
        deviceWizardPage.setModel(BASE_BAND_UNIT_MODEL);
        DelayUtils.sleep(1000);
        deviceWizardPage.setName(BBU_NAME);
        deviceWizardPage.create();
        checkPopup();
    }

    @Step("Create three Radio Units")
    @Test(priority = 2)
    public void createRadioUnit() {
        for (int i = 0; i < 3; i++) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create Device");
            DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deviceWizardPage.setEquipmentType(RADIO_UNIT_EQUIPMENT_TYPE);
            deviceWizardPage.setModel(RADIO_UNIT_MODEL);
            DelayUtils.sleep(1000);
            deviceWizardPage.setName(RADIO_UNIT_NAME);
            deviceWizardPage.create();
            checkPopup();
        }
    }

    @Step("Create three RAN Antennas")
    @Test(priority = 3)
    public void createRanAntennaAndArray() {
        for (int i = 0; i < 3; i++) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create RAN Antenna");
            RanAntennaWizardPage ranAntennaWizardPage = new RanAntennaWizardPage(driver);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            ranAntennaWizardPage.setName(ANTENNA_NAME);
            ranAntennaWizardPage.setModel(RAN_ANTENNA_MODEL);
            DelayUtils.sleep(1000);
            ranAntennaWizardPage.setPreciseLocation(LOCATION_NAME);
            ranAntennaWizardPage.clickAccept();

            antennaArrayWizardPage = new AntennaArrayWizardPage(driver);
            antennaArrayWizardPage.clickAccept();
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            checkPopup();
        }
    }

    @Step("Delete created devices")
    @Test(priority = 4)
    public void deleteDevices() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.filterObject("Name", BBU_NAME);
        cellSiteConfigurationPage.removeObject();
        checkPopup();

        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.filterObject("Name", RADIO_UNIT_NAME);
            cellSiteConfigurationPage.removeObject();
            checkPopup();

            cellSiteConfigurationPage.filterObject("Name", ANTENNA_NAME);
            cellSiteConfigurationPage.removeObject();
            checkPopup();
        }
    }

    private void checkPopup() {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assertions.assertThat(messages).hasSize(1);
        Assertions.assertThat(systemMessage.getFirstMessage().orElseThrow(() -> new RuntimeException("The list is empty")).getMessageType())
                .isEqualTo(SystemMessageContainer.MessageType.SUCCESS);
    }
}
