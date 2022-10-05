/* @(#) $Id$
 *
 * Copyright (c) 2000-2021 ComArch S.A. All Rights Reserved. Any usage, duplication or redistribution of this
 * software is allowed only according to separate agreement prepared in written between ComArch and authorized party.
 */
package com.oss.transport.trail;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.oss.BaseTestCase;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.mainheader.PerspectiveChooser;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.trail.RoutingWizardPage;
import com.oss.pages.transport.trail.v2.IPLinkTerminationStepPage;
import com.oss.pages.transport.trail.v2.IPLinkWizardAttributesStepPage;
import com.oss.pages.transport.trail.v2.TerminationStepPage;
import com.oss.pages.transport.trail.v2.TerminationStepPage.TerminationType;
import com.oss.repositories.AddressRepository;
import com.oss.repositories.LocationInventoryRepository;
import com.oss.repositories.ModelsRepository;
import com.oss.untils.Environment;
import io.qameta.allure.Description;

public class IPLinkTest extends BaseTestCase {

    private static final String IP_LINK_NAME = "ipLinkSeleniumTest";
    private static final String IP_LINK_DESCRIPTION = "ipLinkSeleniumTestDescription";
    private static final IPLinkWizardAttributesStepPage.CapacityUnit IP_LINK_CAPACITY_UNIT =
            IPLinkWizardAttributesStepPage.CapacityUnit.kbps;
    private static final String IP_LINK_CAPACITY_VALUE = "2.0";
    private static final String IP_LINK_EFFECTIVE_BANDWIDTH = "23";
    private static final IPLinkWizardAttributesStepPage.IPAddressVersion IP_LINK_IP_ADDRESS_VERSION =
            IPLinkWizardAttributesStepPage.IPAddressVersion.IPv4;
    private static final String IP_LINK_START_IP_ADDRESS = "101.132.111.130";
    private static final String IP_LINK_START_IP_ADDRESS_MASK = "32";
    private static final String IP_LINK_END_IP_ADDRESS = "101.132.111.135";
    private static final String IP_LINK_END_IP_ADDRESS_MASK = "32";
    private static final boolean IP_LINK_MPLS_ACTIVE = true;
    private static final boolean IP_LINK_MPLS_TE_ACTIVE = true;
    private static final boolean IP_LINK_MPLS_TP_ACTIVE = true;

    private static final String UPDATED_IP_LINK_NAME = "UpdatedIpLinkSeleniumTest";
    private static final String UPDATED_IP_LINK_DESCRIPTION = "UpdatedipLinkSeleniumTestDescription";
    private static final IPLinkWizardAttributesStepPage.CapacityUnit UPDATED_IP_LINK_CAPACITY_UNIT =
            IPLinkWizardAttributesStepPage.CapacityUnit.Mbps;
    private static final String UPDATED_IP_LINK_CAPACITY_VALUE = "14.0";
    private static final String UPDATED_IP_LINK_EFFECTIVE_BANDWIDTH = "12";
    private static final IPLinkWizardAttributesStepPage.IPAddressVersion UPDATED_IP_LINK_IP_ADDRESS_VERSION =
            IPLinkWizardAttributesStepPage.IPAddressVersion.IPv4;
    private static final String UPDATED_IP_LINK_START_IP_ADDRESS = "101.138.111.17";
    private static final String UPDATED_IP_LINK_START_IP_ADDRESS_MASK = "16";
    private static final String UPDATED_IP_LINK_END_IP_ADDRESS = "101.138.112.172";
    private static final String UPDATED_IP_LINK_END_IP_ADDRESS_MASK = "16";
    private static final boolean UPDATED_IP_LINK_MPLS_ACTIVE = true;
    private static final boolean UPDATED_IP_LINK_MPLS_TE_ACTIVE = true;
    private static final boolean UPDATED_IP_LINK_MPLS_TP_ACTIVE = true;

    private static final String IP_LINK_EMPTY_ATTRIBUTES_CAPACITY_VALUE = "0";
    private static final IPLinkWizardAttributesStepPage.CapacityUnit IP_LINK_EMPTY_ATTRIBUTES_CAPACITY_UNIT =
            IPLinkWizardAttributesStepPage.CapacityUnit.bps;

    private static final String IP_ADDRESS_PARTS_SEPARATOR = "\\.";
    private static final String EMPTY_STRING = "";

    private static final String DEVICE_MODEL = "SeleniumTransportIPDeviceModel-ASR9001";
    private static final String DEVICE_TYPE = "IP Device";
    private static final String LOCATION_NAME = "SeleniumTestTransportLocation";
    private static final String LOCATION_TYPE = "Site";
    private static final String COUNTRY_NAME = "SeleniumTestTransportCountry";
    private static final String REGION_NAME = "SeleniumTestTransportRegion";
    private static final String DISTRICT_NAME = "SeleniumTestTransportDistrict";
    private static final String CITY_NAME = "SeleniumTestTransportCity";
    private static final String POSTAL_CODE_NAME = "SeleniumTestTransportPostalCode";
    private static final String DEVICE_1_NAME = "device1";
    private static final String DEVICE_2_NAME = "device2";
    private static final String PORT_NAME = "CLUSTER 0";
    private static final String ETHERNET_INTERFACE_NAME = "ETH";

    private static final String IP_LINK_TYPE = "IP Link";

    private static final String ETHERNET_LINK_EXPECTED_SPEED = "Unknown";

    private static final String TYPE_COLUMN = "Type";
    private static final String START_TYPE = "Start";
    private static final String END_TYPE = "End";

    private NetworkViewPage networkView;

    @BeforeClass
    @Description("Open network view in live perspective")
    public void init() {
        networkView = new NetworkViewPage(driver);

        //Use SideMenu class to open Network View
        //networkView.openNetworkView();
        PerspectiveChooser.create(driver, webDriverWait).setLivePerspective();
    }

    @Test(priority = 1)
    @Description("Create IP Device model if does not exist")
    public void createIpDeviceModel() {
        ModelsRepository modelsRepository = new ModelsRepository(Environment.getInstance());
        modelsRepository.getOrCreateDeviceModel(DEVICE_MODEL, "IPDeviceModel");
    }

    @Test(priority = 2)
    @Description("Create Location if does not exist")
    public void createLocation() {
        Long addressId = prepareAddress();
        LocationInventoryRepository locationRepository = new LocationInventoryRepository(Environment.getInstance());
        locationRepository.getOrCreateLocation(LOCATION_NAME, LOCATION_TYPE, addressId);
    }

    private Long prepareAddress() {
        AddressRepository addressRepository = new AddressRepository(Environment.getInstance());
        return addressRepository.updateOrCreateAddress(COUNTRY_NAME, POSTAL_CODE_NAME, REGION_NAME, CITY_NAME, DISTRICT_NAME);
    }

    @Test(priority = 3)
    @Description("Create IP Devices by wizard")
    public void createIpDevices() {
        createIpDevice(DEVICE_1_NAME);
        createIpDevice(DEVICE_2_NAME);
        networkView.isObjectInViewContent(DEVICE_1_NAME);
        networkView.isObjectInViewContent(DEVICE_2_NAME);
    }

    private void createIpDevice(String deviceName) {
        DeviceWizardPage deviceWizard = networkView.openCreateDeviceWizard();
        deviceWizard.createDevice(DEVICE_MODEL, deviceName, LOCATION_NAME);
        networkView.unselectObject(deviceName);
    }

    @Test(priority = 4)
    @Description("Create IP Link with associated ethernet link")
    public void createIPLinkWithEthernetLink() {
        IPLinkAttributes attributesToCreate = prepareIPLinkAttributesToCreate();
        createIPLinkWithAttributesAndTermination(attributesToCreate);
        networkView.clickOnObject(IP_LINK_NAME, 1);
        networkView.clickOnObject(IP_LINK_NAME, 2);
        assertElementWithSameName(1, attributesToCreate);
        assertElementWithSameName(2, attributesToCreate);
    }

    private void createIPLinkWithAttributesAndTermination(IPLinkAttributes attributes) {
        selectDevices();
        IPLinkWizardAttributesStepPage ipLinkWizard = openCreateIPLinkWizard();
        fillAttributes(ipLinkWizard, attributes);

        IPLinkTerminationStepPage terminationStep = ipLinkWizard.next();
        fillTermination(terminationStep, TerminationStepPage.TerminationType.START);
        fillTermination(terminationStep, TerminationStepPage.TerminationType.END);

        terminationStep.setCreateAssociatedEthernetLink(true);

        terminationStep.accept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private IPLinkWizardAttributesStepPage openCreateIPLinkWizard() {
        networkView.openWizardPage(IPLinkWizardAttributesStepPage.TRAIL_TYPE);
        return IPLinkWizardAttributesStepPage.getAttributesStepPage(driver);
    }

    private IPLinkAttributes prepareIPLinkAttributesToCreate() {
        IPLinkAttributes attributes = new IPLinkAttributes();
        attributes.name = IP_LINK_NAME;
        attributes.description = IP_LINK_DESCRIPTION;
        attributes.capacityUnit = IP_LINK_CAPACITY_UNIT;
        attributes.capacityValue = IP_LINK_CAPACITY_VALUE;
        attributes.effectiveBandwidth = IP_LINK_EFFECTIVE_BANDWIDTH;
        attributes.ipAddressVersion = IP_LINK_IP_ADDRESS_VERSION;
        attributes.startIpAddress = IP_LINK_START_IP_ADDRESS;
        attributes.startIpAddressMask = IP_LINK_START_IP_ADDRESS_MASK;
        attributes.endIpAddress = IP_LINK_END_IP_ADDRESS;
        attributes.endIpAddressMask = IP_LINK_END_IP_ADDRESS_MASK;
        attributes.mplsActive = IP_LINK_MPLS_ACTIVE;
        attributes.mplsTeActive = IP_LINK_MPLS_TE_ACTIVE;
        attributes.mplsTpActive = IP_LINK_MPLS_TP_ACTIVE;
        return attributes;
    }

    private void selectDevices() {
        networkView.selectObject(DEVICE_1_NAME);
        networkView.selectObject(DEVICE_2_NAME);
    }

    private void fillTermination(TerminationStepPage terminationStep, TerminationStepPage.TerminationType terminationType) {
        terminationStep.chooseTerminationType(terminationType);
        terminationStep.setNonexistentCard();
        terminationStep.setPort(PORT_NAME);
        terminationStep.setTerminationPoint(ETHERNET_INTERFACE_NAME);
    }

    private void assertElementWithSameName(int elementIndex, IPLinkAttributes attributesToCreate) {
        networkView.clickOnObject(IP_LINK_NAME, elementIndex);
        String type = networkView.getAttributeValue("Type");
        assertElementWithGivenType(attributesToCreate, type);
        networkView.clickOnObject(IP_LINK_NAME, elementIndex);
    }

    private void assertElementWithGivenType(IPLinkAttributes attributesToCreate, String type) {
        if (type.equals(IP_LINK_TYPE)) {
            assertCreatedIPLink(attributesToCreate);
        } else {
            assertEthernetLinkAttributes(attributesToCreate);
        }
    }

    private void assertCreatedIPLink(IPLinkAttributes attributesToCreate) {
        assertIPLinkAttributes(attributesToCreate);
        networkView.isObjectInRouting1stLevel(IP_LINK_NAME);
    }

    private void assertEthernetLinkAttributes(IPLinkAttributes expected) {
        String actualName = networkView.getAttributeValue("Name");
        String actualDescription = networkView.getAttributeValue("Description");
        String actualSpeed = networkView.getAttributeValue("Speed");

        Assert.assertEquals(actualName, expected.name);
        Assert.assertEquals(actualDescription, expected.description);
        Assert.assertEquals(actualSpeed, ETHERNET_LINK_EXPECTED_SPEED);
    }

    @Test(priority = 5)
    @Description("Create IP Link with empty optional attributes")
    public void createIPLinkWithEmptyOptionalAttributes() {
        createEmptyIPLink();
        IPLinkAttributes expectedAttributes = prepareEmptyIPLinkExpectedAttributes();
        assertIPLinkAttributes(expectedAttributes);
        networkView.unselectObject(IP_LINK_TYPE);
    }

    private void createEmptyIPLink() {
        IPLinkWizardAttributesStepPage ipLinkWizard = openCreateIPLinkWizard();
        ipLinkWizard.accept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private IPLinkAttributes prepareEmptyIPLinkExpectedAttributes() {
        IPLinkAttributes attributes = new IPLinkAttributes();
        attributes.name = EMPTY_STRING;
        attributes.description = EMPTY_STRING;
        attributes.capacityUnit = IP_LINK_EMPTY_ATTRIBUTES_CAPACITY_UNIT;
        attributes.capacityValue = IP_LINK_EMPTY_ATTRIBUTES_CAPACITY_VALUE;
        attributes.effectiveBandwidth = EMPTY_STRING;
        attributes.startIpAddress = EMPTY_STRING;
        attributes.startIpAddressMask = EMPTY_STRING;
        attributes.endIpAddress = EMPTY_STRING;
        attributes.endIpAddressMask = EMPTY_STRING;
        attributes.mplsActive = false;
        attributes.mplsTeActive = false;
        attributes.mplsTpActive = false;
        return attributes;
    }

    @Test(priority = 6)
    @Description("Update empty IP Link with new attributes")
    public void updateIPLinkWithNewAttributes() {
        IPLinkAttributes attributesToUpdate = prepareIPLinkAttributesToUpdate();
        updateIPLinkWithAttributes(attributesToUpdate);
        assertIPLinkAttributes(attributesToUpdate);
        networkView.unselectObject(UPDATED_IP_LINK_NAME);
    }

    private IPLinkAttributes prepareIPLinkAttributesToUpdate() {
        IPLinkAttributes attributes = new IPLinkAttributes();
        attributes.name = UPDATED_IP_LINK_NAME;
        attributes.description = UPDATED_IP_LINK_DESCRIPTION;
        attributes.capacityUnit = UPDATED_IP_LINK_CAPACITY_UNIT;
        attributes.capacityValue = UPDATED_IP_LINK_CAPACITY_VALUE;
        attributes.effectiveBandwidth = UPDATED_IP_LINK_EFFECTIVE_BANDWIDTH;
        attributes.ipAddressVersion = UPDATED_IP_LINK_IP_ADDRESS_VERSION;
        attributes.startIpAddress = UPDATED_IP_LINK_START_IP_ADDRESS;
        attributes.startIpAddressMask = UPDATED_IP_LINK_START_IP_ADDRESS_MASK;
        attributes.endIpAddress = UPDATED_IP_LINK_END_IP_ADDRESS;
        attributes.endIpAddressMask = UPDATED_IP_LINK_END_IP_ADDRESS_MASK;
        attributes.mplsActive = UPDATED_IP_LINK_MPLS_ACTIVE;
        attributes.mplsTeActive = UPDATED_IP_LINK_MPLS_TE_ACTIVE;
        attributes.mplsTpActive = UPDATED_IP_LINK_MPLS_TP_ACTIVE;
        return attributes;
    }

    private void updateIPLinkWithAttributes(IPLinkAttributes attributesToUpdate) {
        networkView.selectObject(IP_LINK_TYPE);
        networkView.useContextAction(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.ATTRIBUTES_AND_TERMINATIONS_ACTION);
        IPLinkWizardAttributesStepPage ipLinkWizard = IPLinkWizardAttributesStepPage.getAttributesStepPage(driver);
        fillAttributes(ipLinkWizard, attributesToUpdate);
        ipLinkWizard.accept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void fillAttributes(IPLinkWizardAttributesStepPage ipLinkWizard, IPLinkAttributes attributes) {
        ipLinkWizard.setName(attributes.name);
        ipLinkWizard.setDescription(attributes.description);
        ipLinkWizard.setCapacityUnit(attributes.capacityUnit);
        ipLinkWizard.setCapacityValue(attributes.capacityValue);
        ipLinkWizard.setEffectiveBandwidth(attributes.effectiveBandwidth);
        ipLinkWizard.setIPAddressVersion(attributes.ipAddressVersion);
        ipLinkWizard.setStartIPAddress(attributes.startIpAddress.replaceAll(IP_ADDRESS_PARTS_SEPARATOR, EMPTY_STRING));
        ipLinkWizard.setStartIPAddressMask(attributes.startIpAddressMask);
        ipLinkWizard.setEndIPAddress(attributes.endIpAddress.replaceAll(IP_ADDRESS_PARTS_SEPARATOR, EMPTY_STRING));
        ipLinkWizard.setEndIPAddressMask(attributes.endIpAddressMask);
        ipLinkWizard.setMplsActive(attributes.mplsActive);
        ipLinkWizard.setMplsTeActive(attributes.mplsTeActive);
        ipLinkWizard.setMplsTpActive(attributes.mplsTpActive);
    }

    private void assertIPLinkAttributes(IPLinkAttributes expected) {
        String actualName = networkView.getAttributeValue("Name");
        String actualDescription = networkView.getAttributeValue("Description");
        String actualCapacity = networkView.getAttributeValue("Capacity");
        String expectedCapacity = prepareCapacityWithUnit(expected.capacityValue, expected.capacityUnit);
        String actualEffectiveBandwidth = networkView.getAttributeValue("Effective bandwidth [Mbps]");
        String actualStartIPAddress = networkView.getAttributeValue("Start IP Address");
        String actualStartIPAddressMask = networkView.getAttributeValue("Start IP Mask");
        String actualEndIPAddress = networkView.getAttributeValue("End IP Address");
        String actualEndIPAddressMask = networkView.getAttributeValue("End IP Mask");
        String actualMplsActive = networkView.getAttributeValue("MPLS active");
        String actualMplsTeActive = networkView.getAttributeValue("MPLS-TE active");
        String actualMplsTpActive = networkView.getAttributeValue("MPLS-TP active");

        Assert.assertEquals(actualName, expected.name);
        Assert.assertEquals(actualDescription, expected.description);
        Assert.assertEquals(actualCapacity, expectedCapacity);
        Assert.assertEquals(actualEffectiveBandwidth, expected.effectiveBandwidth);
        Assert.assertEquals(actualStartIPAddress, expected.startIpAddress);
        Assert.assertEquals(actualStartIPAddressMask, expected.startIpAddressMask);
        Assert.assertEquals(actualEndIPAddress, expected.endIpAddress);
        Assert.assertEquals(actualEndIPAddressMask, expected.endIpAddressMask);
        Assert.assertEquals(actualMplsActive, expected.mplsActive);
        Assert.assertEquals(actualMplsTeActive, expected.mplsTeActive);
        Assert.assertEquals(actualMplsTpActive, expected.mplsTpActive);
    }

    private String prepareCapacityWithUnit(String capacityValue, IPLinkWizardAttributesStepPage.CapacityUnit capacityUnit) {
        return String.join(" ", capacityValue, capacityUnit.toString());
    }

    @Test(priority = 7)
    @Description("Add IP Device and Location to IP Link routing")
    public void addIpDeviceAndLocationToRouting() {
        networkView.selectObject(UPDATED_IP_LINK_NAME);
        networkView.startEditingSelectedTrail();
        networkView.unselectObject(UPDATED_IP_LINK_NAME);
        networkView.selectObject(DEVICE_1_NAME);
        networkView.selectObject(LOCATION_NAME);
        RoutingWizardPage routingWizard = networkView.addSelectedObjectsToRouting();
        routingWizard.accept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkView.unselectObject(DEVICE_1_NAME);
        networkView.unselectObject(LOCATION_NAME);
        networkView.selectObject(UPDATED_IP_LINK_NAME);
        Assert.assertTrue(networkView.isObjectInRoutingElements(DEVICE_1_NAME));
        Assert.assertTrue(networkView.isObjectInRoutingElements(LOCATION_NAME));
        networkView.unselectObject(UPDATED_IP_LINK_NAME);
    }

    @Test(priority = 8)
    @Description("Add Location to IP Link terminations")
    public void addLocationToTerminations() {
        networkView.selectObject(LOCATION_NAME);
        TerminationStepPage terminationStep = networkView.addSelectedObjectsToTerminationV2();
        terminationStep.chooseTerminationType(TerminationType.START);
        terminationStep.accept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkView.unselectObject(LOCATION_NAME);
        networkView.selectObject(UPDATED_IP_LINK_NAME);
        Assert.assertTrue(networkView.isObjectInTerminations(LOCATION_NAME));
        networkView.unselectObject(UPDATED_IP_LINK_NAME);
    }

    @Test(priority = 9)
    @Description("Add IP Device to IP Link terminations")
    public void addIpDeviceToTerminations() {
        networkView.selectObject(DEVICE_2_NAME);
        TerminationStepPage terminationStep = networkView.addSelectedObjectsToTerminationV2();
        terminationStep.chooseTerminationType(TerminationType.END);
        terminationStep.accept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        networkView.unselectObject(DEVICE_2_NAME);
        networkView.selectObject(UPDATED_IP_LINK_NAME);
        Assert.assertTrue(networkView.isObjectInTerminations(DEVICE_2_NAME));
        networkView.unselectObject(DEVICE_2_NAME);
    }

    @Test(priority = 10)
    @Description("Remove IP Link terminations")
    public void removeTerminations() {
        networkView.selectTermination(TYPE_COLUMN, START_TYPE);
        networkView.removeSelectedTerminations();
        networkView.selectTermination(TYPE_COLUMN, END_TYPE);
        networkView.removeSelectedTerminations();
        Assert.assertFalse(networkView.isObjectInTerminations(DEVICE_2_NAME));
        Assert.assertFalse(networkView.isObjectInTerminations(LOCATION_NAME));
    }

    @Test(priority = 11)
    @Description("Remove IP Link element routing segments")
    public void removeElementRoutingSegments() {
        networkView.selectRoutingElement(TYPE_COLUMN, DEVICE_TYPE);
        networkView.deleteSelectedElementsFromRouting();
        networkView.selectRoutingElement(TYPE_COLUMN, LOCATION_TYPE);
        networkView.deleteSelectedElementsFromRouting();
        Assert.assertFalse(networkView.isObjectInRoutingElements(DEVICE_1_NAME));
        Assert.assertFalse(networkView.isObjectInRoutingElements(LOCATION_NAME));
    }

    @Test(priority = 12)
    @Description("Remove created IP Links and Ethernet Link")
    public void deleteCreatedTrails() {
        networkView.clickOnObject(IP_LINK_NAME, 1);
        networkView.clickOnObject(IP_LINK_NAME, 2);
        networkView.selectObject(UPDATED_IP_LINK_NAME);
        networkView.useContextActionAndClickConfirmation(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.DELETE_CONNECTION_ID, ConfirmationBox.DELETE);
        Assert.assertFalse(networkView.isObjectInViewContent(IP_LINK_NAME));
        Assert.assertFalse(networkView.isObjectInViewContent(UPDATED_IP_LINK_NAME));
    }

    @Test(priority = 13)
    @Description("Remove created devices")
    public void deleteDevices() {
        deleteDevice(DEVICE_1_NAME);
        deleteDevice(DEVICE_2_NAME);
    }

    private void deleteDevice(String deviceName) {
        networkView.selectObject(deviceName);
        networkView.useContextActionAndClickConfirmation(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.DELETE_ELEMENT_ACTION, ConfirmationBox.YES);
        Assert.assertFalse(networkView.isObjectInViewContent(deviceName));
    }

    private static class IPLinkAttributes {

        private String name;
        private String description;
        private IPLinkWizardAttributesStepPage.CapacityUnit capacityUnit;
        private String capacityValue;
        private String effectiveBandwidth;
        private IPLinkWizardAttributesStepPage.IPAddressVersion ipAddressVersion;
        private String startIpAddress;
        private String startIpAddressMask;
        private String endIpAddress;
        private String endIpAddressMask;
        private boolean mplsActive = false;
        private boolean mplsTeActive = false;
        private boolean mplsTpActive = false;

    }
}
