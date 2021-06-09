package com.oss.transport;


import com.oss.BaseTestCase;
import com.oss.configuration.Configuration;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.transport.VSI.VSIRouteTargetAssignmentPage;
import com.oss.pages.transport.VSI.VSIOverviewPage;
import com.oss.pages.transport.VSI.VSIWizardPage;
import com.oss.pages.transport.routeTarget.RouteTargetOverviewPage;
import com.oss.pages.transport.routeTarget.RouteTargetWizardPage;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kamil Sikora
 */
public class VSITest extends BaseTestCase {

    private static final String PRE_CREATED_DEVICE_NAME = "SeleniumTestDeviceVSI";
    private static final String PRE_CREATED_AEI_INTERFACE_NAME = "LAG-213";

    private static final String ROUTE_TARGET_TO_CREATE = "1233:123";

    private static final String INTERFACE_NAME_AT_CREATION_1 = "SFP+ 1";
    private static final String INTERFACE_NAME_AT_CREATION_2 = "CLUSTER 1";
    private static final String INTERFACE_NAME_AT_UPDATE_1 = "SYNC 1";
    private static final String INTERFACE_NAME_AT_UPDATE_2 = "MGT LAN 0";

    private static final String ETHERNET_INTERFACE_LABEL = "Ethernet Interface";
    private static final String AEI_LABEL = "AEI";
    private static final String ENVIRONMENT_INDEPENDENT_URL_PART_AFTER_REMOVAL_REDIRECT = "/#/dashboard/predefined/id/transport-dashboard?perspective=LIVE";
    private static final String WIZARDS = "Wizards";
    private static final String TRANSPORT = "Transport";
    private static final String VSI = "VSI";

    private String createdRouteTargetOverviewPageURL;

    @BeforeClass
    @Step("Create Route Target")
    public void createRouteTarget(){
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel("Route Target", WIZARDS, TRANSPORT);

        RouteTargetWizardPage routeTargetWizard = new RouteTargetWizardPage(driver);
        routeTargetWizard.setRouteTarget(ROUTE_TARGET_TO_CREATE);
        RouteTargetOverviewPage routeTargetOverview = routeTargetWizard.clickAccept();
        createdRouteTargetOverviewPageURL = driver.getCurrentUrl();
    }

    @AfterClass
    @Step("Remove created Route Target")
    public void removeCreatedRouteTarget(){
        driver.navigate().to(createdRouteTargetOverviewPageURL);
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        RouteTargetOverviewPage routeTargetOverviewPage = new RouteTargetOverviewPage(driver);
        routeTargetOverviewPage.clickRemove();
        routeTargetOverviewPage.confirmRemoval();
    }

    @Test(priority = 1)
    @Step("Create VSI")
    public void createVSI(){
        VSIAttributes vsiAttributes = getVSIAttributesToCreate();

        VSIWizardPage wizardPage = goToVSIWizardPage();
        goThroughWizardAtCreation(wizardPage, vsiAttributes);
        VSIOverviewPage vsiOverview = wizardPage.clickAccept();

        assertVsiAttributes(vsiOverview, vsiAttributes);
        assertVsiInterfaces(vsiOverview, INTERFACE_NAME_AT_CREATION_1, INTERFACE_NAME_AT_CREATION_2, PRE_CREATED_AEI_INTERFACE_NAME);
        assertRouteTargets(vsiOverview);
    }

    @Test(priority = 2)
    @Step("Update VSI attributes")
    public void updateVSI(){
        VSIAttributes vsiAttributes = getVSIAttributesToUpdate();

        VSIOverviewPage vsiOverview = new VSIOverviewPage(driver);
        VSIWizardPage vsiWizard = vsiOverview.clickEdit();
        goThroughWizardAtUpdate(vsiWizard, vsiAttributes);
        VSIOverviewPage vsiOverviewAfterUpdate = vsiWizard.clickAccept();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        assertVsiAttributes(vsiOverviewAfterUpdate, vsiAttributes);
        assertVsiInterfaces(vsiOverviewAfterUpdate, INTERFACE_NAME_AT_CREATION_1, INTERFACE_NAME_AT_CREATION_2, INTERFACE_NAME_AT_UPDATE_1, INTERFACE_NAME_AT_UPDATE_2);
        assertRouteTargets(vsiOverviewAfterUpdate);
    }

    @Test(priority = 3)
    @Step("Assign created earlier Route Target")
    public void assignCreatedRouteTarget(){
        VSIOverviewPage vsiOverview = new VSIOverviewPage(driver);
        vsiOverview.openBottomRouteTargetsTab();
        VSIRouteTargetAssignmentPage routeTargetWizard = vsiOverview.addRouteTarget();
        routeTargetWizard.setRouteTarget(ROUTE_TARGET_TO_CREATE);
        routeTargetWizard.setImportRole();
        VSIOverviewPage vsiOverviewAfterRouteTargetAssignment = routeTargetWizard.clickAccept();

        assertRouteTargets(vsiOverviewAfterRouteTargetAssignment, ROUTE_TARGET_TO_CREATE);
    }

    @Test(priority = 4)
    @Step("Detach assigned Route Target")
    public void detachRouteTarget(){
        VSIOverviewPage vsiOverview = new VSIOverviewPage(driver);
        vsiOverview.openBottomRouteTargetsTab();
        vsiOverview.removeFirstRouteTarget();

        assertRouteTargets(vsiOverview);
    }

    @Test(priority = 5)
    @Step("Detach assigned interfaces")
    public void detachInterfaces(){
        VSIOverviewPage vsiOverview = new VSIOverviewPage(driver);

        VSIWizardPage vsiWizard = vsiOverview.clickEdit();
        vsiWizard.clickNextStep();
        vsiWizard.navigateThroughSecondPhase(PRE_CREATED_DEVICE_NAME, ETHERNET_INTERFACE_LABEL, INTERFACE_NAME_AT_CREATION_1, INTERFACE_NAME_AT_CREATION_2, INTERFACE_NAME_AT_UPDATE_1, INTERFACE_NAME_AT_UPDATE_2);
        VSIOverviewPage vsiOverviewAfterDetachment = vsiWizard.clickAccept();

        assertVsiInterfaces(vsiOverviewAfterDetachment);
    }

    @Test(priority = 6)
    @Step("Remove VSI")
    public void removeVsi(){
        VSIOverviewPage vsiOverview = new VSIOverviewPage(driver);
        vsiOverview.removeVsi();
        DelayUtils.waitForPageToLoad(driver, webDriverWait);

        assertVsiRemoval();
    }

    private VSIAttributes getVSIAttributesToCreate(){
        VSIAttributes attributes = new VSIAttributes();
        attributes.vpnId = "1234";
        attributes.device = PRE_CREATED_DEVICE_NAME;
        attributes.name = "testVsi 1";
        attributes.veID = "331";
        attributes.routeDistinguisher = "123:321";
        attributes.description = "test description";
        return attributes;
    }

    private VSIAttributes getVSIAttributesToUpdate(){
        VSIAttributes attributes = new VSIAttributes();
        attributes.vpnId = "4321";
        attributes.device = PRE_CREATED_DEVICE_NAME;
        attributes.name = "testVsi 2";
        attributes.veID = "133";
        attributes.routeDistinguisher = "321:123";
        attributes.description = "test description 2";
        return attributes;
    }

    private VSIWizardPage goToVSIWizardPage(){
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        SideMenu sideMenu = SideMenu.create(driver, webDriverWait);
        sideMenu.callActionByLabel(VSI, WIZARDS, TRANSPORT);
        return new VSIWizardPage(driver);
    }

    private void goThroughWizardAtCreation(VSIWizardPage wizardPage, VSIAttributes vsiAttributes) {
        wizardPage.setDevice(vsiAttributes.device);
        fillVsiAttributes(wizardPage, vsiAttributes);
        fulfillSecondStepAtCreate(wizardPage, vsiAttributes);
    }

    private void goThroughWizardAtUpdate(VSIWizardPage wizardPage, VSIAttributes vsiAttributes){
        fillVsiAttributes(wizardPage, vsiAttributes);
        fulfillSecondStepAtUpdate(wizardPage, vsiAttributes);
    }

    private void fulfillSecondStepAtCreate(VSIWizardPage wizardPage, VSIAttributes vsiAttributes) {
        wizardPage.navigateThroughSecondPhase(vsiAttributes.device, ETHERNET_INTERFACE_LABEL, INTERFACE_NAME_AT_CREATION_1, INTERFACE_NAME_AT_CREATION_2);
        wizardPage.navigateThroughSecondPhase(vsiAttributes.device, AEI_LABEL, PRE_CREATED_AEI_INTERFACE_NAME);
    }

    private void fulfillSecondStepAtUpdate(VSIWizardPage wizardPage, VSIAttributes vsiAttributes){
        wizardPage.navigateThroughSecondPhase(vsiAttributes.device, ETHERNET_INTERFACE_LABEL, INTERFACE_NAME_AT_UPDATE_1, INTERFACE_NAME_AT_UPDATE_2);
        wizardPage.navigateThroughSecondPhase(vsiAttributes.device, AEI_LABEL, PRE_CREATED_AEI_INTERFACE_NAME);
    }

    private void fillVsiAttributes(VSIWizardPage wizardPage, VSIAttributes vsiAttributes){
        wizardPage.setVpnId(vsiAttributes.vpnId);
        wizardPage.setName(vsiAttributes.name);
        wizardPage.setVeId(vsiAttributes.veID);
        wizardPage.setRouteDistinguisher(vsiAttributes.routeDistinguisher);
        wizardPage.setDescription(vsiAttributes.description);
        wizardPage.clickNextStep();
    }

    private void assertVsiAttributes(VSIOverviewPage vsiOverview, VSIAttributes vsiAttributes) {
        String vsiName = vsiOverview.getNameValue();
        String vpnId = vsiOverview.getVpnIdValue();
        String veId = vsiOverview.getVeIdValue();
        String device = vsiOverview.getDeviceValue();
        String description = vsiOverview.getDescriptionValue();
        String routeDistinguisher = vsiOverview.getRouteDistinguisherValue();

        Assert.assertEquals(vsiName, vsiAttributes.name);
        Assert.assertEquals(vpnId, vsiAttributes.vpnId);
        Assert.assertEquals(veId, vsiAttributes.veID);
        Assert.assertEquals(device, vsiAttributes.device);
        Assert.assertEquals(description, vsiAttributes.description);
        Assert.assertEquals(routeDistinguisher, vsiAttributes.routeDistinguisher);
    }

    private void assertVsiInterfaces(VSIOverviewPage vsiOverview, String... expectedInterfaces){
        vsiOverview.openBottomVSIInterfacesTab();

        List<String> assignedInterfaces = vsiOverview.getAssignedInterfaces();
        boolean doesAllInterfacesMatch = assignedInterfaces.containsAll(Arrays.asList(expectedInterfaces));
        boolean doesNumberOfElementsMatch = assignedInterfaces.size() == expectedInterfaces.length;

        Assert.assertTrue(doesAllInterfacesMatch);
        Assert.assertTrue(doesNumberOfElementsMatch);
    }

    private void assertRouteTargets(VSIOverviewPage vsiOverview, String... expectedRouteTargets){
        vsiOverview.openBottomRouteTargetsTab();

        List<String> assignedRouteTargets = vsiOverview.getAssignedRouteTargets();
        boolean doesAllRouteTargetsMatch = assignedRouteTargets.containsAll(Arrays.asList(expectedRouteTargets));

        Assert.assertTrue(doesAllRouteTargetsMatch);
    }

    private void assertVsiRemoval() {
        String properUrlAfterRemoval = Configuration.CONFIGURATION.getUrl() + ENVIRONMENT_INDEPENDENT_URL_PART_AFTER_REMOVAL_REDIRECT;
        DelayUtils.waitForPageToLoad(driver, webDriverWait);
        Assert.assertEquals(driver.getCurrentUrl(), properUrlAfterRemoval);
    }

    private static class VSIAttributes{
        private String vpnId;
        private String device;
        private String name;
        private String veID;
        private String routeDistinguisher;
        private String description;
    }

}

