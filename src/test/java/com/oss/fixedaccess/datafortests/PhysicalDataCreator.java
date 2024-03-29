package com.oss.fixedaccess.datafortests;

import java.util.ArrayList;
import java.util.List;

import com.oss.framework.utils.DelayUtils;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.PhysicalConnectivityRepository;
import com.oss.repositories.PhysicalInventoryRepository;
import com.oss.repositories.TrailCoreRepository;
import com.oss.repositories.fixedaccessrepository.DistributionAreaRepository;
import com.oss.services.PhysicalInventoryClient;
import com.oss.services.ResourceCatalogClient;
import com.oss.services.TPServiceClient;
import com.oss.untils.Environment;

public class PhysicalDataCreator {

    private static final String INSTALLATION_LOCATION_NAME = "CPBuildingSeleniumSQView";
    private static final String CENTRAL_OFFICE_LOCATION_NAME = "COBuildingSeleniumSQView";
    private static final String COUNTRY_NAME = "CountrySeleniumTests";
    private static final String POSTAL_CODE_NAME = "PostalCodeSeleniumTests";
    private static final String REGION_NAME = "RegionSeleniumTests";
    private static final String DISTRICT_NAME = "DistrictSeleniumTests";
    private static final String CITY_NAME = "CitySeleniumTests";
    private static final String LOCATION_TYPE_SITE = "Building";
    private static final String OPTICAL_OUTLET_NAME = "OutletCPSeleniumSQView";
    private static final String SPLITTER_NAME = "SplitterCPSeleniumSQView";
    private static final String AN_NAME = "ANCPSeleniumSQView";
    private static final String CP_ODF_NAME = "CPODFSeleniumSQView";
    private static final String CO_ODF_NAME = "COODFSeleniumSQView";
    private static final String TECHNICAL_STANDARD = "FTTH";
    private static final boolean COVERAGE_FLAG = true;
    private static final String MSAN_BLACKBOX_MODEL = "7302 ISAM (blackbox)";
    private static final String SPLITTER_MODEL_NAME = "SPL1x16/1316";
    private static final String OUTLET_MODEL_NAME = "Optical outlet";
    private static final String ODF_MODEL_NAME = "PSU-300/432";
    private static final String ODF_CARD_MODEL_NAME = "MPK-12";
    private static final String SC_PC_FRONT_BACK_PLUGGABLE_MODULE = "SC/PC Front/Back";
    private static final String PHYSICAL_DEVICE_MODEL_TYPE_NAME = "DeviceModel";
    private static final String PORT_MODEL_TYPE_NAME = "EquipmentInterfaceType";
    private static final String CARD_MODEL_TYPE_NAME = "CardModel";
    private static final String PLUGGABLE_MODULE_MODEL_TYPE_NAME = "PluggableModuleModel";
    private static final String SPLITTER_MODEL_TYPE_NAME = "SplitterModel";
    private static final String DOWNLINK_ADSL_PORT_MODEL = "Generic ADSL Downlink Port";
    private static final String DOWNLINK_VDSL_PORT_MODEL = "Generic VDSL Downlink Port";
    private static final String DOWNLINK_GPON_PORT_MODEL = "Generic GPON Downlink Port";
    private static final String DOWNLINK_EPON_PORT_MODEL = "Generic EPON Downlink Port";
    private static final String DOWNLINK_DOCSIS_PORT_MODEL = "DOCSIS Port Downstream";
    private static final String CABLE_MODEL_NAME = "2 x Fiber SM";
    private static final String CABLE_MANUFACTURER_NAME = "Generic";
    private static final String MEDIUM_TYPE = "Fiber";
    private final List<Long> installationMediumsList;
    private final List<Long> installationDevicesList;
    private final List<Long> accessInterfacesList;
    private final PhysicalInventoryClient physicalInventoryClient;
    private final AddressRepository addressRepository;
    private final LocationInventoryRepository locationInventoryRepository;
    private final PhysicalInventoryRepository physicalInventoryRepository;
    private final PhysicalConnectivityRepository physicalConnectivityRepository;
    private final DistributionAreaRepository distributionAreaRepository;
    private final ResourceCatalogClient resourceCatalogClient;
    private final TPServiceClient tpServiceClient;
    private final TrailCoreRepository trailCoreRepository;
    private Long installationAddressId;
    private Long centralOfficeAddressId;
    private Long installationBuildingId;
    private Long centralOfficeBuildingId;
    private Long opticalOutletId;
    private Long outletConnectorFrontId;
    private Long installationODFId;
    private Long installationODFConnectorFrontId;
    private Long installationODFConnectorBackId;
    private Long splitterId;
    private Long splitterConnectorInPortOneId;
    private Long splitterConnectorInPortInId;
    private Long centralOfficeODFId;
    private Long centralOfficeODFConnectorFrontId;
    private Long centralOfficeODFConnectorBackId;
    private Long accessNodeId;
    private Long gponConnectorId;
    private Long gponAI;
    private Long eponAI;
    private Long adslAI;
    private Long vdslAI;
    private Long docsisAI;
    private Long distributionAreaId;
    private String streetNumber;
    private Long cableOutletCPODF;
    private Long cableCPODFSplitter;
    private Long cableSplitterCOODF;
    private Long cableCOODFAN;
    private Long mediumFiberOutletCPODF;
    private Long mediumFiberCPODFSplitter;
    private Long mediumFiberSplitterCOODF;
    private Long mediumFiberCOODFAN;

    public PhysicalDataCreator(Environment env) {
        this.installationMediumsList = new ArrayList<>();
        this.installationDevicesList = new ArrayList<>();
        this.accessInterfacesList = new ArrayList<>();
        this.addressRepository = new AddressRepository(env);
        this.locationInventoryRepository = new LocationInventoryRepository(env);
        this.physicalInventoryRepository = new PhysicalInventoryRepository(env);
        this.physicalInventoryClient = new PhysicalInventoryClient(env);
        this.physicalConnectivityRepository = new PhysicalConnectivityRepository(env);
        this.distributionAreaRepository = new DistributionAreaRepository(env);
        this.resourceCatalogClient = new ResourceCatalogClient(env);
        this.tpServiceClient = new TPServiceClient(env);
        this.trailCoreRepository = new TrailCoreRepository(env);
    }

    public Long getInstallationAddressId() {
        return installationAddressId;
    }

    public Long getDistributionAreaId() {
        return distributionAreaId;
    }

    public String getAddressName(Long addressId) {
        return addressRepository.getGeographicalAddressName(addressId);
    }

    public String getDistributionAreaName(Long distributionAreaId) {
        return distributionAreaRepository.getDistributionAreaName(distributionAreaId);
    }

    public Long getCentralOfficeAddressId() {
        return centralOfficeAddressId;
    }

    public Long getInstallationBuildingId() {
        return installationBuildingId;
    }

    public Long getCentralOfficeBuildingId() {
        return centralOfficeBuildingId;
    }

    public Long getOpticalOutletId() {
        return opticalOutletId;
    }

    public Long getInstallationODFId() {
        return installationODFId;
    }

    public Long getSplitterId() {
        return splitterId;
    }

    public Long getCentralOfficeODFId() {
        return centralOfficeODFId;
    }

    public Long getAccessNodeId() {
        return accessNodeId;
    }

    public Long getCableOutletCPODF() {
        return cableOutletCPODF;
    }

    public Long getCableCPODFSplitter() {
        return cableCPODFSplitter;
    }

    public Long getCableSplitterCOODF() {
        return cableSplitterCOODF;
    }

    public Long getCableCOODFAN() {
        return cableCOODFAN;
    }

    public void createSimplePhysicalDataForSQTests() {
        createInstallationAndCentralOfficeLocation();
        createOutlet();
        createInstallationODF();
        createSplitter();
        createCentralOfficeODF();
        createBlackBoxAccessNodeWithPorts();
        createConnectionsForSqStructure();
        gatherDevicesAndMediaForSqStructureInLists();
        distributionAreaId = createDistributionArea(COVERAGE_FLAG, TECHNICAL_STANDARD, installationAddressId, splitterId,
                installationDevicesList, installationMediumsList, accessNodeId, accessInterfacesList);
        DelayUtils.sleep(10000);
    }

    public void deleteAllDataForSimplePhysicalDataForSQTests(Long distributionAreaId, Long cableOutletCPODF,
                                                             Long cableCPODFSplitter, Long cableSplitterCOODF, Long cableCOODFAN, Long accessNodeId, Long centralOfficeODFId,
                                                             Long splitterId, Long installationODFId, Long opticalOutletId, Long installationBuildingId,
                                                             Long centralOfficeBuildingId, Long centralOfficeAddressId, Long installationAddressId) {
        distributionAreaRepository.removeDistributionArea(distributionAreaId);
        physicalConnectivityRepository.removeCable(cableOutletCPODF);
        physicalConnectivityRepository.removeCable(cableCPODFSplitter);
        physicalConnectivityRepository.removeCable(cableSplitterCOODF);
        physicalConnectivityRepository.removeCable(cableCOODFAN);
        physicalInventoryRepository.deleteDevice(accessNodeId);
        physicalInventoryRepository.deleteDevice(centralOfficeODFId);
        physicalInventoryRepository.deleteDevice(splitterId);
        physicalInventoryRepository.deleteDevice(installationODFId);
        physicalInventoryRepository.deleteDevice(opticalOutletId);
        locationInventoryRepository.deleteLocation(installationBuildingId, "Building");
        locationInventoryRepository.deleteLocation(centralOfficeBuildingId, "Building");
        addressRepository.deleteGeographicalAddress(installationAddressId);
        addressRepository.deleteGeographicalAddress(centralOfficeAddressId);
    }

    private void createInstallationAndCentralOfficeLocation() {
        installationAddressId = createAddress(COUNTRY_NAME, POSTAL_CODE_NAME, REGION_NAME, CITY_NAME,
                DISTRICT_NAME);
        centralOfficeAddressId = createAddress(COUNTRY_NAME, POSTAL_CODE_NAME, REGION_NAME, CITY_NAME,
                DISTRICT_NAME);
        installationBuildingId = Long.parseLong(createBuilding(INSTALLATION_LOCATION_NAME,
                installationAddressId));
        centralOfficeBuildingId = Long.parseLong(createBuilding(CENTRAL_OFFICE_LOCATION_NAME,
                centralOfficeAddressId));
    }

    private void createOutlet() {
        opticalOutletId = createDevice(installationBuildingId, getModelId(OUTLET_MODEL_NAME),
                OPTICAL_OUTLET_NAME, PHYSICAL_DEVICE_MODEL_TYPE_NAME);
        String installationOutletPortId = getPortIdInTextFormat(opticalOutletId, "Port");
        outletConnectorFrontId = getConnectorId(opticalOutletId, installationOutletPortId, "Front");
    }

    private void createConnectionsForSqStructure() {
        int cableLength = 0;
        Long mediumNumber = 1L;
        Long bundleNumber = 0L;
        cableOutletCPODF = createCable(opticalOutletId, installationODFId, outletConnectorFrontId, installationODFConnectorBackId,
                CABLE_MODEL_NAME, CABLE_MANUFACTURER_NAME, cableLength, mediumNumber, bundleNumber);
        cableCPODFSplitter = createCable(installationODFId, splitterId, installationODFConnectorFrontId, splitterConnectorInPortOneId,
                CABLE_MODEL_NAME, CABLE_MANUFACTURER_NAME, cableLength, mediumNumber, bundleNumber);
        cableSplitterCOODF = createCable(splitterId, centralOfficeODFId, splitterConnectorInPortInId, centralOfficeODFConnectorBackId,
                CABLE_MODEL_NAME, CABLE_MANUFACTURER_NAME, cableLength, mediumNumber, bundleNumber);
        cableCOODFAN = createCable(centralOfficeODFId, accessNodeId, centralOfficeODFConnectorFrontId, gponConnectorId,
                CABLE_MODEL_NAME, CABLE_MANUFACTURER_NAME, cableLength, mediumNumber, bundleNumber);
        mediumFiberOutletCPODF = trailCoreRepository.getFirstMediumIdConnectedOnTerminationPoint(outletConnectorFrontId, MEDIUM_TYPE);
        mediumFiberCPODFSplitter = trailCoreRepository.getFirstMediumIdConnectedOnTerminationPoint(splitterConnectorInPortOneId, MEDIUM_TYPE);
        mediumFiberSplitterCOODF = trailCoreRepository.getFirstMediumIdConnectedOnTerminationPoint(splitterConnectorInPortInId, MEDIUM_TYPE);
        mediumFiberCOODFAN = trailCoreRepository.getFirstMediumIdConnectedOnTerminationPoint(gponConnectorId, MEDIUM_TYPE);
    }

    private void gatherDevicesAndMediaForSqStructureInLists() {
        installationDevicesList.add(installationODFId);
        installationDevicesList.add(centralOfficeODFId);
        installationMediumsList.add(mediumFiberOutletCPODF);
        installationMediumsList.add(mediumFiberCPODFSplitter);
        installationMediumsList.add(mediumFiberSplitterCOODF);
        installationMediumsList.add(mediumFiberCOODFAN);
        accessInterfacesList.add(gponAI);
        accessInterfacesList.add(eponAI);
        accessInterfacesList.add(adslAI);
        accessInterfacesList.add(vdslAI);
        accessInterfacesList.add(docsisAI);
    }

    private void createSplitter() {
        splitterId = createDevice(installationBuildingId, getModelId(SPLITTER_MODEL_NAME),
                SPLITTER_NAME, SPLITTER_MODEL_TYPE_NAME);
        String installationSplitterPortOneId = getPortIdInTextFormat(splitterId, "1");
        String installationSplitterPortInId = getPortIdInTextFormat(splitterId, "IN");
        splitterConnectorInPortOneId = getConnectorId(splitterId, installationSplitterPortOneId, "In/Out");
        splitterConnectorInPortInId = getConnectorId(splitterId, installationSplitterPortInId, "IN");
    }

    private void createInstallationODF() {
        installationODFId = createDeviceWithCard(installationBuildingId, getModelId(ODF_MODEL_NAME),
                CP_ODF_NAME, PHYSICAL_DEVICE_MODEL_TYPE_NAME, "1", getModelId(ODF_CARD_MODEL_NAME), CARD_MODEL_TYPE_NAME);
        Long installationODFPortId = getPortIdUnderChassisAndCard(installationODFId, "1",
                "Chassis", "MPK-12");
        addPluggableModuleToPort(installationODFPortId, getModelId(SC_PC_FRONT_BACK_PLUGGABLE_MODULE),
                PLUGGABLE_MODULE_MODEL_TYPE_NAME);
        installationODFConnectorFrontId = getConnectorId(installationODFId, String.valueOf(installationODFPortId),
                "Front");
        installationODFConnectorBackId = getConnectorId(installationODFId, String.valueOf(installationODFPortId),
                "Back");
    }

    private void createCentralOfficeODF() {
        centralOfficeODFId = createDeviceWithCard(centralOfficeBuildingId, getModelId(ODF_MODEL_NAME),
                CO_ODF_NAME, PHYSICAL_DEVICE_MODEL_TYPE_NAME, "1", getModelId(ODF_CARD_MODEL_NAME), CARD_MODEL_TYPE_NAME);
        Long centralOfficeODFPortId = getPortIdUnderChassisAndCard(centralOfficeODFId, "1", "Chassis",
                "MPK-12");
        addPluggableModuleToPort(centralOfficeODFPortId, getModelId(SC_PC_FRONT_BACK_PLUGGABLE_MODULE),
                PLUGGABLE_MODULE_MODEL_TYPE_NAME);
        centralOfficeODFConnectorFrontId = getConnectorId(centralOfficeODFId, String.valueOf(centralOfficeODFPortId),
                "Front");
        centralOfficeODFConnectorBackId = getConnectorId(centralOfficeODFId, String.valueOf(centralOfficeODFPortId),
                "Back");
    }

    private void createBlackBoxAccessNodeWithPorts() {
        accessNodeId = createDevice(centralOfficeBuildingId, getModelId(MSAN_BLACKBOX_MODEL),
                AN_NAME, PHYSICAL_DEVICE_MODEL_TYPE_NAME);
        addPortToDevice(accessNodeId, getModelId(DOWNLINK_GPON_PORT_MODEL), PORT_MODEL_TYPE_NAME, "GPON");
        String accessNodeGPONPortId = getPortIdInTextFormat(accessNodeId, "GPON");
        gponConnectorId = getConnectorId(accessNodeId, accessNodeGPONPortId, "Rx/Tx");
        addPortToDevice(accessNodeId, getModelId(DOWNLINK_ADSL_PORT_MODEL), PORT_MODEL_TYPE_NAME, "ADSL");
        addPortToDevice(accessNodeId, getModelId(DOWNLINK_VDSL_PORT_MODEL), PORT_MODEL_TYPE_NAME, "VDSL");
        addPortToDevice(accessNodeId, getModelId(DOWNLINK_EPON_PORT_MODEL), PORT_MODEL_TYPE_NAME, "EPON");
        addPortToDevice(accessNodeId, getModelId(DOWNLINK_DOCSIS_PORT_MODEL), PORT_MODEL_TYPE_NAME, "DOCSIS");
        gponAI = getAccessInterfaceId(accessNodeId, getPortIdInTextFormat(accessNodeId, "GPON"), "GPON");
        eponAI = getAccessInterfaceId(accessNodeId, getPortIdInTextFormat(accessNodeId, "EPON"), "10G-EPON");
        adslAI = getAccessInterfaceId(accessNodeId, getPortIdInTextFormat(accessNodeId, "ADSL"), "ADSL2+");
        vdslAI = getAccessInterfaceId(accessNodeId, getPortIdInTextFormat(accessNodeId, "VDSL"), "VDSL2");
        docsisAI = getAccessInterfaceId(accessNodeId, getPortIdInTextFormat(accessNodeId, "DOCSIS"), "DOCSIS 3.1");
    }

    private Long createAddress(String countryName, String postalCode, String region, String city,
                               String district) {
        streetNumber = String.valueOf(generateRandomNumber());
        return addressRepository.createOrUpdateGeographicalAddressWithStreetNumber(countryName, postalCode, region,
                city, district, streetNumber);
    }

    private String createBuilding(String locationName, Long addressId) {
        locationName = locationName + generateRandomNumber();
        return locationInventoryRepository.createLocation(locationName, LOCATION_TYPE_SITE, addressId);
    }

    private Long createDevice(Long addressId, Long modelId, String deviceName, String deviceModelType) {
        return physicalInventoryRepository.createDevice(LOCATION_TYPE_SITE, addressId, modelId, deviceName, deviceModelType);
    }

    private Long createDeviceWithCard(Long addressId, Long modelId, String deviceName,
                                      String deviceModelType, String slotName, Long cardModel, String cardModelType) {
        return physicalInventoryRepository.createDeviceWithCard(LOCATION_TYPE_SITE, addressId, modelId, deviceName,
                deviceModelType, slotName, cardModel, cardModelType);
    }

    private void addPortToDevice(Long deviceId, Long portModelId, String portModelType, String portName) {
        physicalInventoryRepository.addPortToDevice(deviceId, portModelId, portModelType, portName);
    }

    private void addPluggableModuleToPort(Long portId, Long pluggableModuleModelId, String pluggableModlueModelType) {
        physicalInventoryRepository.addPluggableModuleToDevice(portId, pluggableModuleModelId, pluggableModlueModelType);
    }

    private Long createCable(Long firstTerminationDeviceId, Long secondTerminationDeviceId, Long firstTermination, Long secondTermination,
                             String cableModelName, String manufacturerName, int length, Long mediumNumber, Long bundleNumber) {
        return physicalConnectivityRepository.createCableWithSpecificMediumAndBundleNumber(firstTerminationDeviceId, secondTerminationDeviceId,
                firstTermination, secondTermination, cableModelName, manufacturerName, length, mediumNumber, bundleNumber);
    }

    private Long createDistributionArea(boolean coverageFlag, String technicalStandard, Long addressId, Long accessPointId, List<Long> installationDevicesIds,
                                        List<Long> installationMediumsIds, Long accessNodeId, List<Long> accessInterfacesIds) {
        return distributionAreaRepository.createDistributionAreaWithAddressAccessPointAccessNodeInstallationMediumsInstallationDevices(coverageFlag,
                technicalStandard, addressId, accessPointId, installationDevicesIds, installationMediumsIds, accessNodeId, accessInterfacesIds);
    }

    private Long getModelId(String modelName) {
        return resourceCatalogClient.getModelIds(modelName);
    }

    private Long getAccessInterfaceId(Long deviceId, String portId, String layerName) {
        return tpServiceClient.getAccessInterfaceId(deviceId, portId, layerName);
    }

    private Long getConnectorId(Long deviceId, String portId, String connectorName) {
        return Long.valueOf(tpServiceClient.getConnectorId(deviceId, portId, connectorName));
    }

    private Long getPortIdUnderChassisAndCard(Long deviceId, String portName, String chassisName, String cardName) {
        Long chassisId = physicalInventoryClient.getDeviceChassisId(deviceId, chassisName);
        Long cardId = physicalInventoryClient.getDeviceCardUnderChassisId(chassisId, cardName);
        Long portId = physicalInventoryClient.getDevicePortUnderCardId(cardId, portName);
        return portId;
    }

    private String getPortIdInTextFormat(Long deviceId, String portName) {
        return physicalInventoryClient.getDevicePortId(deviceId, portName);
    }

    private int generateRandomNumber() {
        return (int) (Math.random() * 1000000000) + 1;
    }

}
