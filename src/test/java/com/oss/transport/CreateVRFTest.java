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
import com.oss.pages.transport.VRF.VRFImpExpRouteTargetWizardPage;
import com.oss.pages.transport.VRF.VRFOverviewPage;
import com.oss.pages.transport.VRF.VRFWizardPage;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.oss.configuration.Configuration.CONFIGURATION;

/**
 * @author Kamil Szota
 */
public class CreateVRFTest extends BaseTestCase {

    private static final String WIZARDS = "Wizards";
    private static final String TRANSPORT = "Transport";
    private static final String VRF = "VRF";

    private static final String VRF_NAME = "vrfNameTest2";
    private static final String ROUTE_DISTINGUISHER = "999:999";
    private static final String DESCRIPTION = "Description1";
    private static final String DEVICE_NAME = "ipdevice13";
    private static final String INTERFACE1_NAME = "CLUSTER 0";
    private static final String INTERFACE2_NAME = "MGT LAN 0";

    private static final String ROUTE_DISTINGUISHER2 = "888:888";
    private static final String DESCRIPTION2 = "Description9";
    private static final String INTERFACE3_NAME = "CLUSTER 1";
    private static final String INTERFACE4_NAME = "MGT LAN 1";

    private static final String ROUTE_TARGET = "3453:3453";
    private static final String ADDRESS_FAMILY = "IPv4";

    private static final String ENVIRONMENT_INDEPENDENT_URL_REDIRECT_PART = "/#/dashboard/predefined/id/transport-dashboard";

    @Test(priority = 1)
    @Step("Create VRF")
    public void createVRF() {
        VRFAttributes vrfAttributes = getVrfAttributesToCreate();

        VRFWizardPage vrfWizard = goToVRFWizard();
        fillVRFWizardToCreate(vrfAttributes, vrfWizard);
        DelayUtils.sleep(5000);
        VRFOverviewPage vrfOverview = vrfWizard.clickAccept();

        assertVRFAttributes(vrfAttributes, vrfOverview);
        assertAssignedInterfaces(vrfOverview, Arrays.asList(INTERFACE1_NAME, INTERFACE2_NAME));
    }

    @Test(priority = 2)
    @Step("Update VRF attributes")
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
    @Step("Assign Route Target")
    public void addRouteTarget() {
        ImpExpAttributes impExpAttributes = getImpExpAttributesToAssign();

        VRFOverviewPage vrfOverviewBeforeAssign = new VRFOverviewPage(driver);
        vrfOverviewBeforeAssign.openRouteTargetTab();
        VRFImpExpRouteTargetWizardPage impExpWizard = vrfOverviewBeforeAssign.clickAddRouteTargetButton();
        assignRouteTarget(impExpWizard, impExpAttributes);
        VRFOverviewPage vrfOverviewAfterAssign = impExpWizard.clickAccept();

        assertAssignedRouteTargets(vrfOverviewAfterAssign, impExpAttributes);
    }

    @Test(priority = 4)
    @Step("Remove VRF")
    public void removeVRF() {
        VRFOverviewPage vrfOverview = new VRFOverviewPage(driver);
        vrfOverview.clickRemove();
        vrfOverview.confirmRemoval();
        String url = driver.getCurrentUrl();

        assertVRFRemove(url);
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
        vrfWizard.clickNextStep();
        fulfillSecondStep(vrfWizard, vrfAttributes.deviceName, Arrays.asList(INTERFACE3_NAME, INTERFACE4_NAME));
    }

    private VRFWizardPage goToVRFWizard() {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sidemenu = SideMenu.create(driver, webDriverWait);
        sidemenu.callActionByLabel(VRF, WIZARDS, TRANSPORT);
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
        String descriptionValue = vrfOverview.getDescriptionValue();
        String ipv4Value = vrfOverview.getAddressFamilyIPv4Value();
        String ipv6Value = vrfOverview.getAddressFamilyIPv6Value();
        String routeDistinguisherValue = vrfOverview.getRouteDistinguisherValue();
        String deviceValue = vrfOverview.getDeviceNameValue();

        Assert.assertEquals(descriptionValue, vrfAttributes.description);
        Assert.assertEquals(ipv4Value, vrfAttributes.ipv4);
        Assert.assertEquals(ipv6Value, vrfAttributes.ipv6);
        Assert.assertEquals(routeDistinguisherValue, vrfAttributes.routeDistinguisher);
        Assert.assertEquals(nameValue, vrfAttributes.vrfName);
        Assert.assertEquals(deviceValue, vrfAttributes.deviceName);
    }

    private void assertAssignedInterfaces(VRFOverviewPage vrfOverview, Collection<String> assignedInterfacesNames) {
        vrfOverview.openInterfaceAssignmentTab();
        List<String> assignedInterfaces = vrfOverview.getAssignedInterfaces();
        boolean doesAllInterfacesMatch = assignedInterfaces.containsAll(assignedInterfacesNames);
        Assert.assertTrue(doesAllInterfacesMatch);
    }

    private void changeAttributesOnFirstStep(VRFWizardPage vrfWizard, VRFAttributes vrfAttributes) {
        vrfWizard.setRouteDistinguisher(vrfAttributes.routeDistinguisher);
        vrfWizard.setDescription(vrfAttributes.description);
    }

    private void assignRouteTarget(VRFImpExpRouteTargetWizardPage impExpWizard, ImpExpAttributes impExpAttributes) {
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        impExpWizard.selectRouteTarget(impExpAttributes.routeTarget);
        impExpWizard.setAddressFamily(impExpAttributes.addressFamily);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
    }

    private void assertAssignedRouteTargets(VRFOverviewPage vrfOverview, ImpExpAttributes impExpAttributes) {
        List<String> assignedRouteTargets = vrfOverview.getAssignedRouteTargets();
        List<String> assignedAddressFamilies = vrfOverview.getAssignedAddressFamilies();

        Assert.assertEquals(assignedRouteTargets.get(0), impExpAttributes.routeTarget);
        Assert.assertEquals(assignedAddressFamilies.get(0).toUpperCase(), impExpAttributes.addressFamily.toUpperCase());
    }

    private void assertVRFRemove(String url){
        Assertions.assertThat(url).isEqualTo(CONFIGURATION.getUrl() + ENVIRONMENT_INDEPENDENT_URL_REDIRECT_PART);
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
