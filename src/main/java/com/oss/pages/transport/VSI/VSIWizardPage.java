package com.oss.pages.transport.VSI;

import java.util.Arrays;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.commonhierarchy.CommonHierarchyApp;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * @author Kamil Sikora
 */
public class VSIWizardPage extends BasePage {

    private static final String VPN_ID_FIELD_ID = "uidFieldVpnId";
    private static final String DEVICE_FIELD_ID = "uidFieldDevice";
    private static final String NAME_FIELD_ID = "uidFieldName";
    private static final String VE_ID_FIELD_ID = "uidFieldVeId";
    private static final String ROUTE_DISTINGUISHER_FIELD_ID = "uidFieldRouteDistinguisher";
    private static final String DESCRIPTION_FIELD_ID = "uidFieldDescription";

    private final Wizard wizard;

    public VSIWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Set VPN id to {vpnId}")
    public void setVpnId(String vpnId) {
        setTextFieldComponentValue(VPN_ID_FIELD_ID, vpnId);
    }

    @Step("Set name to {name}")
    public void setName(String name) {
        setTextFieldComponentValue(NAME_FIELD_ID, name);
    }

    @Step("Set Ve id to {veId}")
    public void setVeId(String veId) {
        setTextFieldComponentValue(VE_ID_FIELD_ID, veId);
    }

    @Step("Set Route Distinguisher to {routeDistinguisher}")
    public void setRouteDistinguisher(String routeDistinguisher) {
        setTextFieldComponentValue(ROUTE_DISTINGUISHER_FIELD_ID, routeDistinguisher);
    }

    @Step("Set device to {device}")
    public void setDevice(String device) {
        wizard.setComponentValue(DEVICE_FIELD_ID, device, Input.ComponentType.SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Set description to {description}")
    public void setDescription(String description) {
        wizard.setComponentValue(DESCRIPTION_FIELD_ID, description, Input.ComponentType.TEXT_AREA);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click next step button")
    public void clickNextStep() {
        wizard.clickNextStep();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Navigate through common hierarchy app widget selecting {deviceName} -> {interfaceType} and interface values")
    public void navigateThroughSecondPhase(String deviceName, String interfaceType, String... interfaceValues) {
        CommonHierarchyApp commonHierarchyApp = CommonHierarchyApp.createByClass(driver, wait);
        commonHierarchyApp.callAvailableAction(Arrays.asList(interfaceValues), deviceName, interfaceType);
    }

    @Step("Click accept button")
    public VSIOverviewPage clickAccept() {
        wizard.clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
        return new VSIOverviewPage(driver);
    }

    private void setTextFieldComponentValue(String componentId, String value) {
        wizard.setComponentValue(componentId, value, Input.ComponentType.TEXT_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void setComponentValue(Input component, String value) {
        component.setSingleStringValue(value);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}

