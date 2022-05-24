package com.oss.pages.transport.VSI;

import java.util.Arrays;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.commonhierarchy.CommonHierarchyApp;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.configuration.Configuration.CONFIGURATION;

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
    private static final String COMPONENT_ID = "vsiApp";

    public VSIWizardPage(WebDriver driver) {
        super(driver);
    }

    public VSIWizardPage goToVSIWizardPage() {
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.get(String.format("%s/#/view/transport/ip/mpls/vsi?perspective=LIVE", CONFIGURATION.getUrl()));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new VSIWizardPage(driver);
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
        getWizard().setComponentValue(DEVICE_FIELD_ID, device, Input.ComponentType.SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Set description to {description}")
    public void setDescription(String description) {
        getWizard().setComponentValue(DESCRIPTION_FIELD_ID, description, Input.ComponentType.TEXT_AREA);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click next step button")
    public void clickNextStep() {
        getWizard().clickNextStep();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Navigate through common hierarchy app widget selecting {deviceName} -> {interfaceType} and interface values")
    public void navigateThroughSecondPhase(String deviceName, String interfaceType, String... interfaceValues) {
        CommonHierarchyApp commonHierarchyApp = CommonHierarchyApp.create(driver, wait, COMPONENT_ID);
        commonHierarchyApp.callAvailableAction(Arrays.asList(interfaceValues), deviceName, interfaceType);
    }

    @Step("Click accept button")
    public void clickAccept() {
        getWizard().clickAccept();
    }

    private void setTextFieldComponentValue(String componentId, String value) {
        getWizard().setComponentValue(componentId, value, Input.ComponentType.TEXT_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, COMPONENT_ID);
    }
}

