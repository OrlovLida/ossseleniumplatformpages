package com.oss.E2E;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;
import com.oss.pages.radio.Cell4GBulkWizardPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.pages.radio.HostingWizardPage;

import io.qameta.allure.Description;
import io.qameta.allure.Step;

public class TP_OSS_RM_RAN_004_Steps2_5 extends BaseTestCase {

    private static final String locationName = "XYZ_SeleniumTests";
    private static final String eNodeBName = "TP_OSS_RM_RAN_004_eNodeB";
    private static final int amountOfCells = 3;
    private static final String carrierL1800 = "L1800 (1392)";
    private static final String carrierL2100 = "L2100 (10562)";
    private static final int crp = 2;
    private static final String[] cellNamesL1800 = { "TP_OSS_RM_RAN_004_L1800_cell_1", "TP_OSS_RM_RAN_004_L1800_cell_2", "TP_OSS_RM_RAN_004_L1800_cell_3" };
    private static final String[] cellNamesL2100 = { "TP_OSS_RM_RAN_004_L2100_cell_1", "TP_OSS_RM_RAN_004_L2100_cell_2", "TP_OSS_RM_RAN_004_L2100_cell_3" };

    private static final String radioUnitEquipmentType = "Remote Radio Head/Unit";
    private static final String radioUnitModel = "HUAWEI Technology Co.,Ltd RRU5301";
    private static final String radioUnitNames[] = { "TP_OSS_RM_RAN_004_RRU_1",
            "TP_OSS_RM_RAN_004_RRU_2",
            "TP_OSS_RM_RAN_004_RRU_3" };

    private static final String[] antennaNames = { "TP_OSS_RM_RAN_004_Antenna_1", "TP_OSS_RM_RAN_004_Antenna_2", "TP_OSS_RM_RAN_004_Antenna_3" };
    private static final int[] localCellsId1800 = { 1, 2, 3 };
    private static final int[] localCellsId2100 = { 4, 5, 6 };

    private CellSiteConfigurationPage cellSiteConfigurationPage;

    @Test(priority = 1)
    @Description("Find location and open cell site configuration view")
    public void findLocation() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        homePage.setOldObjectType("Site");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        OldInventoryViewPage oldInventoryViewPage = new OldInventoryViewPage(driver);
        oldInventoryViewPage.filterObject("Name", locationName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        oldInventoryViewPage.expandShowOnAndChooseView("Cell Site Configuration");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage = new CellSiteConfigurationPage(driver);
    }

    @Test(priority = 2)
    @Description("Create Cell4G with Bulk Wizard")
    public void createCell4GL1800Bulk() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.expandTreeToBaseStation("Site", locationName, eNodeBName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Cells 4G");

        createCellBulk(amountOfCells, carrierL1800, cellNamesL1800, localCellsId1800, crp);
    }

    @Test(priority = 3)
    @Description("Create Cell4G with Bulk Wizard")
    public void createCell4GL2100Bulk() {
        createCellBulk(amountOfCells, carrierL2100, cellNamesL2100, localCellsId2100, crp);
    }

    @Step("Create three Radio Units")
    @Test(priority = 4)
    public void createRadioUnit() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(locationName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTab("Devices");
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Create Device");
            DeviceWizardPage deviceWizardPage = new DeviceWizardPage(driver);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deviceWizardPage.setEquipmentType(radioUnitEquipmentType);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deviceWizardPage.setModel(radioUnitModel);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deviceWizardPage.setName(radioUnitNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            deviceWizardPage.create();
            checkPopup("Device has been created successfully");
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    @Test(priority = 5)
    @Description("Create hosting relation between Cell 4G and RRU")
    public void hostCell4GL1800OnRRU() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(eNodeBName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        hostCell4GOnRRU(cellNamesL1800, radioUnitNames);
    }

    @Test(priority = 6)
    @Description("Create hosting relation between Cell 4G and RAN Antenna Array")
    public void hostCell4GL1800OnRANAntennaArray() {
        hostCell4GOnRANAntennaArray(cellNamesL1800, antennaNames);
    }

    @Test(priority = 7)
    @Description("Create hosting relation between Cell 4G and RRU")
    public void hostCell4GL2100OnRRU() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.selectTreeRow(eNodeBName);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        hostCell4GOnRRU(cellNamesL1800, radioUnitNames);
    }

    @Test(priority = 8)
    @Description("Create hosting relation between Cell 4G and RAN Antenna Array")
    public void hostCell4GL2100OnRANAntennaArray() {
        hostCell4GOnRANAntennaArray(cellNamesL1800, antennaNames);
    }

    private void checkPopup(String text) {
        SystemMessageInterface systemMessage = SystemMessageContainer.create(driver, webDriverWait);
        List<SystemMessageContainer.Message> messages = systemMessage.getMessages();
        Assert.assertEquals(messages.size(), 1);
        Assert.assertEquals(messages.get(0).getMessageType(), SystemMessageContainer.MessageType.SUCCESS);
        Assert.assertTrue((messages.get(0).getText()).contains(text));
    }

    private void hostCell4GOnRRU(String[] cellNames, String[] radioUnitNames) {
        for (int i = 0; i < 3; i++) {
            cellSiteConfigurationPage.selectTreeRow(cellNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.selectTab("Hosting");
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Host on Device");
            HostingWizardPage wizard = new HostingWizardPage(driver);
            wizard.setDevice(radioUnitNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            wizard.clickAccept();
            checkPopup("Hosting Create Success");
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
        }
    }

    private void hostCell4GOnRANAntennaArray(String[] cellNames, String[] antennaNames) {
        for (int i = 0; i < 3; i++) {
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.selectTreeRow(cellNames[i]);
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            cellSiteConfigurationPage.clickPlusIconAndSelectOption("Host on Antenna Array");
            HostingWizardPage wizard = new HostingWizardPage(driver);
            wizard.setHostingContains(antennaNames[i] + "/APE4518R14V06_Ly1");
            DelayUtils.waitForPageToLoad(driver, webDriverWait);
            wizard.clickAccept();
            checkPopup("Hosting Create Success");
        }
    }

    private void createCellBulk(int amountOfCells, String carrier, String[] cellNames, int[] localCellsId, int crp) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cellSiteConfigurationPage.clickPlusIconAndSelectOption("Cell 4G Bulk Wizard");
        Cell4GBulkWizardPage cell4GBulkWizardPage = new Cell4GBulkWizardPage(driver);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        cell4GBulkWizardPage.createCell4GBulkWizardWithDefaultValues(amountOfCells, carrier, cellNames, localCellsId, crp);
        checkPopup("Cells 4G created success");
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }
}
