package com.oss.radio;

import com.oss.BaseTestCase;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.platform.HomePage;
import com.oss.pages.platform.OldInventoryViewPage;
import com.oss.pages.radio.CellSiteConfigurationPage;
import com.oss.repositories.*;
import com.oss.services.PhysicalInventoryClient;
import com.oss.services.ResourceCatalogClient;
import com.oss.services.TPServiceClient;
import com.oss.untils.Constants;
import com.oss.untils.Environment;
import com.oss.utils.RandomGenerator;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
public class LossCalculationTests extends BaseTestCase {

    private Environment env = Environment.getInstance();

    private static String locationId;
    private static final String locationName = "LocationSeleniumLossCalculation" + (int) (Math.random() * 10000);
    private static Long addressId;
    private static final String countryName = "CountrySeleniumTests";
    private static final String postalCodeName = "PostalCodeSeleniumTests";
    private static final String regionName = "RegionSeleniumTests";
    private static final String districtName = "DistrictSeleniumTests";
    private static final String cityName = "CitySeleniumTests";
    private static Long eNodeBId;
    private static final String eNodeBName = "eNodeBLossCalculation" + (int) (Math.random() * 10000);
    private static Long cellIdA51;
    private static final String cell4GId1 = RandomGenerator.generateRandomCell4GId();
    private static final String cell4GA51 = "Cell4GLossCalculation" + (int) (Math.random() * 10000) + "A51";
    private static Long cellIdA61;
    private static final String cell4GId2 = RandomGenerator.generateRandomCell4GId();
    private static final String cell4GA61 = "Cell4GLossCalculation" + (int) (Math.random() * 10000) + "A61";
    private static Long cellIdA71;
    private static final String cell4GId3 = RandomGenerator.generateRandomCell4GId();
    private static final String cell4GA71 = "Cell4GLossCalculation" + (int) (Math.random() * 10000) + "A71";
    private static Long cellIdA81;
    private static final String cell4GId4 = RandomGenerator.generateRandomCell4GId();
    private static final String cell4GA81 = "Cell4GLossCalculation" + (int) (Math.random() * 10000) + "A81";
    private static Long deviceModelId;
    private static Long rru60Id;
    private static final String rru60DeviceName = "BTS5900," + locationName + "/0/MRRU,60";
    private static Long rru70Id;
    private static final String rru70DeviceName = "BTS5900," + locationName + "/0/MRRU,70";
    private static Long rru80Id;
    private static final String rru80DeviceName = "BTS5900," + locationName + "/0/MRRU,80";
    private static Long antennaId;
    private static final String antennaName = "RANAntennaLossCalculation" + (int) (Math.random() * 10000);
    private static Long mhaId;
    private static final String mhaName = "MHALossCalculation" + (int) (Math.random() * 10000);
    private static Long combinerId;
    private static final String combinerName = "CombinerLossCalculation" + (int) (Math.random() * 10000);
    private static String connector1Id;
    private static String connector2Id;
    private static final String locationTypeSite = "Site";
    private static final String carrier4G = "L800-B20-5";
    private static final String MCC = "234";
    private static final String MNC = "20";
    private static String arrayId;

    @BeforeClass
    public void createTestData() {
        getOrCreateAddress();
        createPhysicalLocation();
        createENodeB(eNodeBName);
        createCell4GA51(cell4GA51, cell4GId1);
        createCell4GA61(cell4GA61, cell4GId2);
        createCell4GA71(cell4GA71, cell4GId3);
        createCell4GA81(cell4GA81, cell4GId4);
        createRRU60Device(Constants.RRU5301_MODEL, rru60DeviceName, Constants.DEVICE_MODEL_TYPE);
        createRRU70Device(Constants.RRU5301_MODEL, rru70DeviceName, Constants.DEVICE_MODEL_TYPE);
        createRRU80Device(Constants.RRU5501_MODEL, rru80DeviceName, Constants.DEVICE_MODEL_TYPE);
        createAntennaDevice(Constants.AQU4518R22v07ANTENNA_MODEL, antennaName, Constants.ANTENNA_MODEL_TYPE);
        createMHADevice(Constants.ATADU2022v07MHA_MODEL, mhaName, Constants.DEVICE_MODEL_TYPE);
        createCombinerDevice(Constants.ACOMT2H10v06COMBINER_MODEL, combinerName, Constants.DEVICE_MODEL_TYPE);
        createHRToDevice(rru60Id, cellIdA51);
        createHRToDevice(rru80Id, cellIdA61);
        createHRToDevice(rru80Id, cellIdA71);
        createHRToDevice(rru70Id, cellIdA81);
        DelayUtils.sleep(5000);
        createHRToArray(Constants.AQU4518R22v07ANTENNA_MODEL + "_r1", antennaId, cellIdA51);
        createHRToArray(Constants.AQU4518R22v07ANTENNA_MODEL + "_Cy2", antennaId, cellIdA61);
        createHRToArray(Constants.AQU4518R22v07ANTENNA_MODEL + "_Cy2", antennaId, cellIdA71);
        createHRToArray(Constants.AQU4518R22v07ANTENNA_MODEL + "_Ry3", antennaId, cellIdA81);
        createCable(antennaId, mhaId, "Cy2", "ANT1_H", "-", "Rx/Tx",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2);
        createCable(antennaId, mhaId, "Cy2", "ANT0_H", "+", "Rx/Tx",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2);
        createCable(antennaId, mhaId, "r1", "ANT1_L", "-", "Rx/Tx",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2);
        createCable(antennaId, mhaId, "r1", "ANT0_L", "+", "Rx/Tx",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2);
        createCable(antennaId, mhaId, "Ry3", "ANT1_M", "-", "Rx/Tx",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2);
        createCable(antennaId, mhaId, "Ry3", "ANT0_M", "+", "Rx/Tx",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2);
        createCable(combinerId, rru60Id, "Port1", "C T/R", "-", "Rx/Tx",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2);
        createCable(combinerId, rru60Id, "Port1", "A T/R", "+", "Rx/Tx",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2);
        createCable(combinerId, rru70Id, "Port2", "C T/R", "-", "Rx/Tx",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2);
        createCable(combinerId, rru70Id, "Port2", "A T/R", "+", "Rx/Tx",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2);
        createCable(combinerId, rru80Id, "Port3", "C T01/R01", "-", "Rx/Tx",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2);
        createCable(combinerId, rru80Id, "Port3", "A T01/R01", "+", "Rx/Tx",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2);
        createMultipleSegmentCable(mhaId, combinerId, "BTS0", "Port4", "Rx/Tx", "+",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2, Constants.LCF1250J12_CABLE, 30, Constants.LCF1250J_3m_CABLE, 3);
        createMultipleSegmentCable(mhaId, combinerId, "BTS1", "Port4", "Rx/Tx", "-",
                Constants.LCF1250J_2m_CABLE, Constants.RFS_MANUFACTURER, 2, Constants.LCF1250J12_CABLE, 30, Constants.LCF1250J_3m_CABLE, 3);
    }

    @BeforeMethod
    public void goToHomePage() {
        HomePage homePage = new HomePage(driver);
        homePage.goToHomePage(driver, BASIC_URL);
    }

    @Test
    @Description("The user goes to Cell Site Configuration, clicks Calculate Losses button in Rigging Losses tab and checks loss value and status for Route 1 CellA51")
    public void tSRAN74CalculateLosses() {

        homePage.setOldObjectType(locationTypeSite);
        new OldInventoryViewPage(driver)
                .filterObject("Name", locationName)
                .expandShowOnAndChooseView("Cell Site Configuration");
        new CellSiteConfigurationPage(driver)
                .expandTreeToBaseStation(locationTypeSite, locationName, eNodeBName)
                .selectTab("Rigging Losses")
                .selectRowByAttributeValueWithLabel("Type", "Cell4G") //change to name OSSWEB-12088
                .useTableContextActionByLabel("Calculate Losses");
        DelayUtils.sleep(3000);
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Status", 1).contains("Incomplete")); //to change after adding model MHA to RF
        Assert.assertTrue(new CellSiteConfigurationPage(driver).getValueByRowNumber("Loss", 1).contains("103.049")); //to change after adding model MHA to RF
    }


    private void getOrCreateAddress() {
        AddressRepository addressRepository = new AddressRepository(env);
        addressId = addressRepository.updateOrCreateAddress(countryName, postalCodeName, regionName, cityName, districtName);
    }

    private void createPhysicalLocation() {
        LocationInventoryRepository locationInventoryRepository = new LocationInventoryRepository(env);
        locationId = locationInventoryRepository.createLocation(locationName, locationTypeSite, addressId);
    }

    private void createENodeB(String eNodeBNameForCreate) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        eNodeBId = radio4gRepository.createENodeB(eNodeBNameForCreate, Long.valueOf(locationId), MCC, MNC, Constants.HUAWEI_ENODEB_MODEL);
    }

    private void createCell4GA51(String cell4GNameForCreate, String cell4GIdForCreate) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        cellIdA51 = radio4gRepository.createCell4g(cell4GNameForCreate, Integer.valueOf(cell4GIdForCreate), eNodeBId, MCC, MNC, carrier4G);
    }

    private void createCell4GA61(String cell4GNameForCreate, String cell4GIdForCreate) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        cellIdA61 = radio4gRepository.createCell4g(cell4GNameForCreate, Integer.valueOf(cell4GIdForCreate), eNodeBId, MCC, MNC, carrier4G);
    }

    private void createCell4GA71(String cell4GNameForCreate, String cell4GIdForCreate) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        cellIdA71 = radio4gRepository.createCell4g(cell4GNameForCreate, Integer.valueOf(cell4GIdForCreate), eNodeBId, MCC, MNC, carrier4G);
    }

    private void createCell4GA81(String cell4GNameForCreate, String cell4GIdForCreate) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        cellIdA81 = radio4gRepository.createCell4g(cell4GNameForCreate, Integer.valueOf(cell4GIdForCreate), eNodeBId, MCC, MNC, carrier4G);
    }

    private void createRRU60Device(String deviceModel, String deviceName, String deviceModelType) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        deviceModelId = resourceCatalogClient.getModelIds(deviceModel);
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        rru60Id = physicalInventoryRepository.createDevice(locationTypeSite, Long.valueOf(locationId), deviceModelId, deviceName, deviceModelType);
    }

    private void createRRU70Device(String deviceModel, String deviceName, String deviceModelType) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        deviceModelId = resourceCatalogClient.getModelIds(deviceModel);
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        rru70Id = physicalInventoryRepository.createDevice(locationTypeSite, Long.valueOf(locationId), deviceModelId, deviceName, deviceModelType);
    }

    private void createRRU80Device(String deviceModel, String deviceName, String deviceModelType) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        deviceModelId = resourceCatalogClient.getModelIds(deviceModel);
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        rru80Id = physicalInventoryRepository.createDevice(locationTypeSite, Long.valueOf(locationId), deviceModelId, deviceName, deviceModelType);
    }

    private void createAntennaDevice(String deviceModel, String deviceName, String deviceModelType) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        deviceModelId = resourceCatalogClient.getModelIds(deviceModel);
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        antennaId = physicalInventoryRepository.createDevice(locationTypeSite, Long.valueOf(locationId), deviceModelId, deviceName, deviceModelType);
    }

    private void createMHADevice(String deviceModel, String deviceName, String deviceModelType) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        deviceModelId = resourceCatalogClient.getModelIds(deviceModel);
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        mhaId = physicalInventoryRepository.createDevice(locationTypeSite, Long.valueOf(locationId), deviceModelId, deviceName, deviceModelType);
    }

    private void createCombinerDevice(String deviceModel, String deviceName, String deviceModelType) {
        ResourceCatalogClient resourceCatalogClient = new ResourceCatalogClient(env);
        deviceModelId = resourceCatalogClient.getModelIds(deviceModel);
        PhysicalInventoryRepository physicalInventoryRepository = new PhysicalInventoryRepository(env);
        combinerId = physicalInventoryRepository.createDevice(locationTypeSite, Long.valueOf(locationId), deviceModelId, deviceName, deviceModelType);
    }

    private void createHRToDevice(Long deviceId, Long cellId) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        // radio4gRepository.createHRENodeBDevice(Long.valueOf(deviceId), eNodeBId);
        radio4gRepository.createHRCellDevice(Long.valueOf(deviceId), eNodeBId, cellId);
    }

    private void createHRToArray(String arrayName, Long deviceId, Long cellId) {
        Radio4gRepository radio4gRepository = new Radio4gRepository(env);
        PhysicalInventoryClient physicalInventoryClient = new PhysicalInventoryClient(env);
        arrayId = physicalInventoryClient.getAntennaArrayId(deviceId, arrayName);
        radio4gRepository.createHRCellDevice(Long.valueOf(arrayId), eNodeBId, cellId);
    }

    private void createCable(Long device1Id, Long device2Id, String portName1, String portName2, String connectorName1,
                             String connectorName2, String modelName, String manufacturerName, int length) {
        TPServiceClient tpServiceClient = new TPServiceClient(env);
        connector1Id = tpServiceClient.getConnectorId(device1Id, getPortId(device1Id, portName1), connectorName1);
        connector2Id = tpServiceClient.getConnectorId(device2Id, getPortId(device2Id, portName2), connectorName2);
        PhysicalConnectivityRepository physicalConnectivityRepository = new PhysicalConnectivityRepository(env);
        physicalConnectivityRepository.createCable(device1Id, device2Id, Long.valueOf(connector1Id), Long.valueOf(connector2Id), modelName, manufacturerName, length);
    }

    private String getPortId(Long deviceId, String portName) {
        PhysicalInventoryClient physicalInventoryClient = new PhysicalInventoryClient(env);
        return physicalInventoryClient.getDevicePortId(deviceId, portName);
    }

    private void createMultipleSegmentCable(Long device1Id, Long device2Id, String portName1, String portName2, String connectorName1,
                                            String connectorName2, String modelName1, String manufacturerName, int length1, String modelName2, int length2, String modelName3, int length3) {
        TPServiceClient tpServiceClient = new TPServiceClient(env);
        connector1Id = tpServiceClient.getConnectorId(device1Id, getPortId(device1Id, portName1), connectorName1);
        connector2Id = tpServiceClient.getConnectorId(device2Id, getPortId(device2Id, portName2), connectorName2);
        PhysicalConnectivityRepository physicalConnectivityRepository = new PhysicalConnectivityRepository(env);
        physicalConnectivityRepository.createMultipleSegmentCable(Long.valueOf(connector1Id), Long.valueOf(connector2Id),
                modelName1, manufacturerName, length1, modelName2, length2, modelName3, length3);
    }

}
