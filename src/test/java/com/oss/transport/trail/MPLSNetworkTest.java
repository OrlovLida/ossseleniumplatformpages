/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.transport.trail;

import com.oss.BaseTestCase;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.transport.NetworkViewPage;
import com.oss.pages.transport.trail.IPLinkWizardPage;
import com.oss.pages.transport.trail.MPLSNetworkWizardPage;
import com.oss.pages.transport.trail.RoutingWizardPage;
import com.oss.pages.transport.trail.TerminationWizardPage;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Robert Nawrat
 */
public class MPLSNetworkTest extends BaseTestCase {

    private static final String MPLS_NETWORK = "MPLS Network";
    private static final String IP_LINK = "IP Link";
    private static final String MPLS_NETWORK_1 = "MPLSNetwork1";
    private static final String MPLS_NETWORK_2 = "MPLSNetwork2";
    private static final String LOCATION = "SeleniumTest";
    private static final String LOCATION_TYPE = "Site";
    private static final String DEVICE = "Device";
    private static final String DEVICE_TYPE = "IP Device";
    private static final String DEVICE_MODEL = "Cisco Systems Inc. ASR9001";
    private static final String TYPE_COLUMN = "Type";
    private static final String START = "Start";
    private static final String END = "End";

    private NetworkViewPage networkView;

    @BeforeClass
    public void init() {
        networkView = new NetworkViewPage(driver);
        networkView.openNetworkView();
    }

    @Test(priority = 1)
    public void createMPLSNetworkWithSetAllAttributes() {
        MPLSNetworkWizardPage mplsNetworkWizard = networkView.openCreateTrailWizard(MPLSNetworkWizardPage.class);
        fillAllAttributes(mplsNetworkWizard, getMPLSNetworkAttributesToCreate());
        mplsNetworkWizard.proceed();
        assertMPLSNetworkAttributes(getMPLSNetworkAttributesToCreate());
    }

    @Test(priority = 2)
    public void createMPLSNetworkWithEmptyAllOptionalAttributes() {
        networkView.selectObject(MPLS_NETWORK_1);
        MPLSNetworkWizardPage mplsNetworkWizard = networkView.openCreateTrailWizard(MPLSNetworkWizardPage.class);
        mplsNetworkWizard.proceed();
        assertMPLSNetworkAttributes(getMPLSNetworkAttributesDefaultValues());
    }

    @Test(priority = 3)
    public void updateMPLSNetworkAllAttributesToOtherValues() {
        MPLSNetworkWizardPage mplsNetworkWizard = networkView.openUpdateTrailWizard(MPLSNetworkWizardPage.class);
        fillAllAttributes(mplsNetworkWizard, getMPLSNetworkAttributesToUpdate());
        mplsNetworkWizard.proceed();
        assertMPLSNetworkAttributes(getMPLSNetworkAttributesToUpdate());
    }

    @Test(priority = 4)
    public void clearMPLSNetworkAllOptionalAttributes() {
        networkView.selectObject(MPLS_NETWORK_2);
        networkView.unselectObject(MPLS_NETWORK_1);
        MPLSNetworkWizardPage mplsNetworkWizard = networkView.openUpdateTrailWizard(MPLSNetworkWizardPage.class);
        clearAllOptionalAttributes(mplsNetworkWizard);
        mplsNetworkWizard.proceed();
        assertMPLSNetworkAttributes(getMPLSNetworkAttributesDefaultValues());
    }

    @Test(priority = 5)
    public void createIPLink() {
        networkView.unselectObject(MPLS_NETWORK);
        IPLinkWizardPage ipLinkWizard = networkView.openCreateTrailWizard(IPLinkWizardPage.class);
        ipLinkWizard.proceed();
        Assertions.assertThat(networkView.isObjectInViewContent(IP_LINK)).isTrue();
    }

    @Test(priority = 6)
    public void addIPLinkToRouting() {
        networkView.unselectObject(IP_LINK);
        networkView.selectObject(MPLS_NETWORK);
        networkView.startEditingSelectedTrail();
        networkView.unselectObject(MPLS_NETWORK);
        networkView.selectObject(IP_LINK);
        RoutingWizardPage routingWizard = networkView.addSelectedObjectsToRouting();
        routingWizard.proceed();
        networkView.unselectObject(IP_LINK);
        networkView.selectObject(MPLS_NETWORK);
        Assertions.assertThat(networkView.isObjectInRouting1stLevel(IP_LINK)).isTrue();
    }

    @Test(priority = 7)
    public void createDevice() {
        networkView.unselectObject(MPLS_NETWORK);
        DeviceWizardPage deviceWizard = networkView.openCreateDeviceWizard();
        deviceWizard.setModel(DEVICE_MODEL);
        deviceWizard.setName(DEVICE);
        deviceWizard.setLocation(LOCATION);
        deviceWizard.create();
        Assertions.assertThat(networkView.isObjectInViewContent(DEVICE)).isTrue();
    }

    @Test(priority = 8)
    public void addDeviceToTerminations() {
        TerminationWizardPage terminationWizard = networkView.addSelectedObjectsToTermination();
        terminationWizard.setTerminationType(TerminationWizardPage.TerminationType.Start);
        terminationWizard.proceed();
        networkView.unselectObject(DEVICE);
        networkView.selectObject(MPLS_NETWORK);
        Assertions.assertThat(networkView.isObjectInTerminations(DEVICE)).isTrue();
    }

    @Test(priority = 9)
    public void addLocationToTerminations() {
        networkView.unselectObject(MPLS_NETWORK);
        networkView.selectObject(LOCATION);
        TerminationWizardPage terminationWizard = networkView.addSelectedObjectsToTermination();
        terminationWizard.setTerminationType(TerminationWizardPage.TerminationType.End);
        terminationWizard.clearNetworkElement();
        terminationWizard.proceed();
        networkView.unselectObject(LOCATION);
        networkView.selectObject(MPLS_NETWORK);
        Assertions.assertThat(networkView.isObjectInTerminations(LOCATION)).isTrue();
    }

    @Test(priority = 10)
    public void addDeviceAndLocationToElementRoutingSegments() {
        networkView.unselectObject(MPLS_NETWORK);
        networkView.selectObject(LOCATION);
        networkView.selectObject(DEVICE);
        RoutingWizardPage routingWizard = networkView.addSelectedObjectsToRouting();
        routingWizard.accept();
        networkView.unselectObject(DEVICE);
        networkView.unselectObject(LOCATION);
        networkView.selectObject(MPLS_NETWORK);
        Assertions.assertThat(networkView.isObjectInRoutingElements(DEVICE)).isTrue();
        Assertions.assertThat(networkView.isObjectInRoutingElements(LOCATION)).isTrue();
    }

    @Test(priority = 11)
    public void removeTerminations() {
        networkView.selectTermination(TYPE_COLUMN, START);
        networkView.removeSelectedTerminations();
        networkView.selectTermination(TYPE_COLUMN, END);
        networkView.removeSelectedTerminations();
        Assertions.assertThat(networkView.isObjectInTerminations(DEVICE)).isFalse();
        Assertions.assertThat(networkView.isObjectInTerminations(LOCATION)).isFalse();
    }

    @Test(priority = 12)
    public void removeElementRoutingSegments() {
        networkView.selectRoutingElement(TYPE_COLUMN, DEVICE_TYPE);
        networkView.deleteSelectedElementsFromRouting();
        networkView.selectRoutingElement(TYPE_COLUMN, LOCATION_TYPE);
        networkView.deleteSelectedElementsFromRouting();
        Assertions.assertThat(networkView.isObjectInRoutingElements(DEVICE)).isFalse();
        Assertions.assertThat(networkView.isObjectInRoutingElements(LOCATION)).isFalse();
    }

    @Test(priority = 13)
    public void removeTrailFromRouting() {
        networkView.selectTrailInRouting(IP_LINK);
        networkView.removeSelectedTrailsFromRouting();
        Assertions.assertThat(networkView.isObjectInRouting1stLevel(IP_LINK)).isFalse();
    }

    @Test(priority = 14)
    public void deleteTrails() {
        networkView.selectObject(MPLS_NETWORK_2);
        networkView.selectObject(IP_LINK);
        networkView.useContextActionAndClickConfirmation(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.DELETE_TRAIL_ACTION, ConfirmationBox.PROCEED);
        Assertions.assertThat(networkView.isObjectInViewContent(MPLS_NETWORK)).isFalse();
        Assertions.assertThat(networkView.isObjectInViewContent(MPLS_NETWORK_2)).isFalse();
        Assertions.assertThat(networkView.isObjectInViewContent(IP_LINK)).isFalse();
    }

    @Test(priority = 15)
    public void deleteDevice() {
        networkView.selectObject(DEVICE);
        networkView.useContextActionAndClickConfirmation(ActionsContainer.EDIT_GROUP_ID, NetworkViewPage.DELETE_ELEMENT_ACTION, ConfirmationBox.YES);
        Assertions.assertThat(networkView.isObjectInViewContent(DEVICE)).isFalse();
    }

    private MPLSNetworkAttributes getMPLSNetworkAttributesToCreate() {
        MPLSNetworkAttributes attributes = new MPLSNetworkAttributes();
        attributes.name = MPLS_NETWORK_1;
        attributes.description = "Original description";
        attributes.capacityUnit = MPLSNetworkWizardPage.CapacityUnit.kbps;
        attributes.capacityValue = "1.25";
        attributes.autonomousSystem = "22.33";
        return attributes;
    }

    private MPLSNetworkAttributes getMPLSNetworkAttributesToUpdate() {
        MPLSNetworkAttributes attributes = new MPLSNetworkAttributes();
        attributes.name = MPLS_NETWORK_2;
        attributes.description = "Updated description";
        attributes.capacityUnit = MPLSNetworkWizardPage.CapacityUnit.Mbps;
        attributes.capacityValue = "2.5";
        attributes.autonomousSystem = "44.55";
        return attributes;
    }

    private MPLSNetworkAttributes getMPLSNetworkAttributesDefaultValues() {
        MPLSNetworkAttributes attributes = new MPLSNetworkAttributes();
        attributes.name = "";
        attributes.description = "";
        attributes.capacityUnit = MPLSNetworkWizardPage.CapacityUnit.bps;
        attributes.capacityValue = "0";
        attributes.autonomousSystem = "";
        return attributes;
    }

    private void fillAllAttributes(MPLSNetworkWizardPage mplsNetworkWizard, MPLSNetworkAttributes attributes) {
        mplsNetworkWizard.setName(attributes.name);
        mplsNetworkWizard.setDescription(attributes.description);
        mplsNetworkWizard.setCapacityUnit(attributes.capacityUnit);
        mplsNetworkWizard.setCapacityValue(attributes.capacityValue);
        mplsNetworkWizard.setAutonomousSystem(attributes.autonomousSystem);
    }

    private void clearAllOptionalAttributes(MPLSNetworkWizardPage mplsNetworkWizard) {
        mplsNetworkWizard.clearName();
        mplsNetworkWizard.clearDescription();
        mplsNetworkWizard.setDefaultCapacityUnit();
        mplsNetworkWizard.setDefaultCapacityValue();
        mplsNetworkWizard.clearAutonomousSystem();
    }

    private void assertMPLSNetworkAttributes(MPLSNetworkAttributes expected) {
        String actualName = networkView.getAttributeValue("Name");
        String actualDescription = networkView.getAttributeValue("Description");
        String actualCapacity = networkView.getAttributeValue("Capacity");
        String expectedCapacity = getCapacityWithUnit(expected.capacityValue, expected.capacityUnit);
        String actualAutonomousSystem = networkView.getAttributeValue("Autonomous System");
        Assertions.assertThat(actualName).isEqualTo(expected.name);
        Assertions.assertThat(actualDescription).isEqualTo(expected.description);
        Assertions.assertThat(actualCapacity).isEqualTo(expectedCapacity);
        Assertions.assertThat(actualAutonomousSystem).isEqualTo(expected.autonomousSystem);
    }

    private String getCapacityWithUnit(String capacityValue, MPLSNetworkWizardPage.CapacityUnit capacityUnit) {
        return String.join(" ", capacityValue, capacityUnit.toString());
    }

    private static class MPLSNetworkAttributes {
        private String name;
        private String description;
        private MPLSNetworkWizardPage.CapacityUnit capacityUnit;
        private String capacityValue;
        private String autonomousSystem;
    }

}
