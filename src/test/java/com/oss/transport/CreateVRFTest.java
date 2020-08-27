/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.transport;

import com.oss.BaseTestCase;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.transport.VRFImpExpRouteTargetWizardPage;
import com.oss.pages.transport.VRFOverviewPage;
import com.oss.pages.transport.VRFWizardPage;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.oss.configuration.Configuration.CONFIGURATION;
import static com.oss.framework.utils.DelayUtils.HUMAN_REACTION_MS;

/**
 * @author Kamil Szota
 */
public class CreateVRFTest extends BaseTestCase {

    private static final String OK_BUTTON_PATH = "//button[text()='Ok']";

    private static final String WIZARDS = "Wizards";
    private static final String TRANSPORT = "Transport";
    private static final String VRF = "VRF";
    private static final String VRF_NAME = "vrfNameTest1";
    private static final String ROUTE_DISTINGUISHER = "999:999";
    private static final String DESCRIPTION = "Description1";
    private static final String DEVICE_NAME = "SeleniumASR9001KSZ876";
    private static final String INTERFACE1_NAME = "CLUSTER 0";
    private static final String INTERFACE2_NAME = "MGT LAN 0";

    private static final String ROUTE_DISTINGUISHER2 = "888:888";
    private static final String DESCRIPTION2 = "Description9";
    private static final String INTERFACE3_NAME = "CLUSTER 1";
    private static final String INTERFACE4_NAME = "MGT LAN 1";

    private static final String ROUTE_TARGET = "3453:3453";
    private static final String ADDRESS_FAMILY = "IPV4";

    @Test(priority = 1)
    public void createVRF() {
        VRFAttributes vrfAttributes = getVrfAttributesToCreate();

        VRFWizardPage vrfWizard = goToVRFWizard();
        fillVRFWizardToCreate(vrfAttributes, vrfWizard);
        VRFOverviewPage vrfOverview = vrfWizard.clickAccept();

        assertVRFAttributes(vrfAttributes, vrfOverview);
        assertAssignedInterfaces(vrfOverview, Arrays.asList(INTERFACE1_NAME, INTERFACE2_NAME));
    }

    @Test(priority = 2)
    public void updateVRF() {
        VRFAttributes vrfAttributes = getVRFAttributesToUpdate();

        VRFOverviewPage vrfOverviewBeforeUpdate = new VRFOverviewPage(driver);
        VRFWizardPage vrfWizard = vrfOverviewBeforeUpdate.clickEdit();
        fillVRFWizardToUpdate(vrfAttributes, vrfWizard);
        VRFOverviewPage vrfOverviewAfterUpdate = vrfWizard.clickAccept();

        assertVRFAttributes(vrfAttributes, vrfOverviewAfterUpdate);
        assertAssignedInterfaces(vrfOverviewAfterUpdate, Arrays.asList(INTERFACE1_NAME, INTERFACE2_NAME, INTERFACE3_NAME, INTERFACE4_NAME));
    }

    @Test(priority = 3)
    public void addRouteTarget() {
        ImpExpAttributes impExpAttributes = getImpExpAttributesToAssign();

        VRFOverviewPage vrfOverviewBeforeAssign = new VRFOverviewPage(driver);
        vrfOverviewBeforeAssign.openRouteTargetTab();
        VRFImpExpRouteTargetWizardPage impExpWizard = vrfOverviewBeforeAssign.clickAddRouteTargetButton();
        assignRouteTarget(impExpWizard, impExpAttributes);
        DelayUtils.sleep(HUMAN_REACTION_MS); // TODO: KS Zmienic na inny delay (do ustalenia z webem)
        VRFOverviewPage vrfOverviewAfterAssign = impExpWizard.clickAccept();

        assertAssignedRouteTargets(vrfOverviewAfterAssign, impExpAttributes);
    }

    @Test(priority = 4)
    public void removeVRF() {
        VRFOverviewPage vrfOverview = new VRFOverviewPage(driver);
        vrfOverview.clickRemove();
        DelayUtils.waitByXPath(webDriverWait, OK_BUTTON_PATH);
        vrfOverview.clickOk();
        String url = driver.getCurrentUrl();
        assertVRFRemove(url);
        //TODO: KS Zapytać o asercje z przejsciem na glowna strone po usunieciu.

    }

    private VRFAttributes getVrfAttributesToCreate() {
        VRFAttributes vrfAttributes = new VRFAttributes();
        vrfAttributes.vrfName = VRF_NAME;
        vrfAttributes.routeDistinguisher = ROUTE_DISTINGUISHER;
        vrfAttributes.description = DESCRIPTION;
        vrfAttributes.deviceName = DEVICE_NAME;
        vrfAttributes.ipv4 = "true";
        vrfAttributes.ipv6 = "false";
        return vrfAttributes;
    }

    private VRFAttributes getVRFAttributesToUpdate() {
        VRFAttributes vrfAttributes = new VRFAttributes();
        vrfAttributes.vrfName = VRF_NAME;
        vrfAttributes.routeDistinguisher = ROUTE_DISTINGUISHER2;
        vrfAttributes.description = DESCRIPTION2;
        vrfAttributes.deviceName = DEVICE_NAME;
        vrfAttributes.ipv4 = "true";
        vrfAttributes.ipv6 = "false";
        return vrfAttributes;
    }

    private ImpExpAttributes getImpExpAttributesToAssign() {
        ImpExpAttributes impExpAttributes = new ImpExpAttributes();
        impExpAttributes.addressFamily = ADDRESS_FAMILY;
        impExpAttributes.routeTarget = ROUTE_TARGET;
        return impExpAttributes;
    }

    private void fillVRFWizardToCreate(VRFAttributes vrfAttributes, VRFWizardPage vrfWizard) {
        fulfillFirstStep(vrfWizard, vrfAttributes);
        vrfWizard.clickNextStep();
        fulfillSecondStep(vrfWizard, vrfAttributes.deviceName, Arrays.asList(INTERFACE1_NAME, INTERFACE2_NAME));
    }

    private void fillVRFWizardToUpdate(VRFAttributes vrfAttributes, VRFWizardPage vrfWizard) {
        changeAttributesOnFirstStep(vrfWizard, vrfAttributes);
        //TODO: KS Zapytać o to
        DelayUtils.sleep(500);
        vrfWizard.clickNextStep();
        fulfillSecondStep(vrfWizard, vrfAttributes.deviceName, Arrays.asList(INTERFACE3_NAME, INTERFACE4_NAME));
    }

    private VRFWizardPage goToVRFWizard() {
        SideMenu sidemenu = new SideMenu(driver, webDriverWait);
        sidemenu.goToTechnologyByLeftSideMenu(WIZARDS, TRANSPORT, VRF);
        return new VRFWizardPage(driver);
    }

    private void fulfillFirstStep(VRFWizardPage vrfWizard, VRFAttributes vrfAttributes) {
        vrfWizard.selectDevice(vrfAttributes.deviceName);
        vrfWizard.setName(vrfAttributes.vrfName);
        vrfWizard.setRouteDistinguisher(vrfAttributes.routeDistinguisher);
        vrfWizard.setDescription(vrfAttributes.description);
    }

    private void fulfillSecondStep(VRFWizardPage vrfWizard, String deviceName, List<String> interfacesNamesToSelect) {
        vrfWizard.selectInterfacesInResourceTree(deviceName, interfacesNamesToSelect);
    }

    private void assertVRFAttributes(VRFAttributes vrfAttributes, VRFOverviewPage vrfOverview) {
        String nameValue = vrfOverview.getNameValue();
        Assertions.assertThat(nameValue).isEqualTo(vrfAttributes.vrfName);
        String descriptionValue = vrfOverview.getDescriptionValue();
        Assertions.assertThat(descriptionValue).isEqualTo(vrfAttributes.description);
        String ipv4Value = vrfOverview.getAddressFamilyIPv4Value();
        Assertions.assertThat(ipv4Value).isEqualTo(vrfAttributes.ipv4);
        String ipv6Value = vrfOverview.getAddressFamilyIPv6Value();
        Assertions.assertThat(ipv6Value).isEqualTo(vrfAttributes.ipv6);
        String routeDistinguisherValue = vrfOverview.getRouteDistinguisherValue();
        Assertions.assertThat(routeDistinguisherValue).isEqualTo(vrfAttributes.routeDistinguisher);
        String deviceValue = vrfOverview.getDeviceNameValue();
        Assertions.assertThat(deviceValue).isEqualTo(vrfAttributes.deviceName);
    }

    private void assertAssignedInterfaces(VRFOverviewPage vrfOverview, Collection<String> assignedInterfacesNames) {
        vrfOverview.openInterfaceAssignmentTab();
        for (String assignedInterfaceName : assignedInterfacesNames) {
            String interface1Name = vrfOverview.getInterface(assignedInterfaceName);
            Assertions.assertThat(interface1Name).isEqualTo(assignedInterfaceName);
        }
    }

    private void changeAttributesOnFirstStep(VRFWizardPage vrfWizard, VRFAttributes vrfAttributes) {
        vrfWizard.setRouteDistinguisher(vrfAttributes.routeDistinguisher);
        vrfWizard.setDescription(vrfAttributes.description);
    }

    private void assignRouteTarget(VRFImpExpRouteTargetWizardPage impExpWizard, ImpExpAttributes impExpAttributes) {
        impExpWizard.selectRouteTarget(impExpAttributes.routeTarget);
        impExpWizard.setAddressFamily(impExpAttributes.addressFamily);
    }

    private void assertAssignedRouteTargets(VRFOverviewPage vrfOverview, ImpExpAttributes impExpAttributes) {
        String routeTargetValue = vrfOverview.getRouteTargetAssignment(impExpAttributes.routeTarget);
        Assertions.assertThat(routeTargetValue).isEqualTo(impExpAttributes.routeTarget);
        String addressFamilyValue = vrfOverview.getAddressFamilyValue(impExpAttributes.addressFamily);
        Assertions.assertThat(addressFamilyValue).isEqualTo(impExpAttributes.addressFamily);
    }

    private void assertVRFRemove(String url){
        Assertions.assertThat(url).isEqualTo(CONFIGURATION.getUrl() + "/#/dashboard/predefined/id/transport-dashboard");
    }

    private static class VRFAttributes {
        private String vrfName;
        private String routeDistinguisher;
        private String description;
        private String deviceName;
        private String ipv4;
        private String ipv6;
    }

    private static class ImpExpAttributes {
        private String routeTarget;
        private String addressFamily;
    }
}