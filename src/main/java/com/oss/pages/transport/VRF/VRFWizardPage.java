/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.transport.VRF;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.commonhierarchy.CommonHierarchyApp;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * @author Kamil Szota
 */
public class VRFWizardPage extends BasePage {

    private static final String NAME_FIELD_ID = "uidFieldName";
    private static final String ROUTE_DISTINGUISHER_FIELD_ID = "uidFieldRouteDistinguisher";
    private static final String DESCRIPTION_FIELD_ID = "uidFieldDescription";
    private static final String DEVICE_FIELD_ID = "uidFieldDevice";
    private static final String WIDGET_ID = "CommonHierarchyApp-vrfAppSelect";
    private static final String ID = "NEEDS_TO_UPDATE_ID";

    private final Wizard wizard;

    public VRFWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, ID);
    }

    @Step("Set name to {name}")
    public void setName(String name) {
        Input nameComponent = getEmptyTextFieldComponent(NAME_FIELD_ID);
        nameComponent.setSingleStringValue(name);
    }

    @Step("Set Route distinguisher to {routeDistinguisher}")
    public void setRouteDistinguisher(String routeDistinguisher) {
        Input routeDistinguisherComponent = getEmptyTextFieldComponent(ROUTE_DISTINGUISHER_FIELD_ID);
        routeDistinguisherComponent.setSingleStringValue(routeDistinguisher);
    }

    @Step("Set description to {description}")
    public void setDescription(String description) {
        Input descriptionComponent = getEmptyTextFieldComponent(DESCRIPTION_FIELD_ID);
        descriptionComponent.setSingleStringValue(description);
    }

    @Step("Set device to {deviceName}")
    public void selectDevice(String deviceName) {
        Input deviceComponent = wizard.getComponent(DEVICE_FIELD_ID, Input.ComponentType.SEARCH_FIELD);
        deviceComponent.clear();
        deviceComponent.setSingleStringValue(deviceName);
    }

    @Step("Click next step button")
    public void clickNextStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickNextStep();
    }

    @Step("Click accept button")
    public VRFOverviewPage clickAccept() {
        wizard.clickAccept();
        return new VRFOverviewPage(driver);
    }

    @Step("Navigate through Common Hierarchy App widget selecting {resourceName} and names of interfaces")
    public void selectInterfacesInResourceTree(String resourceName, List<String> interfaceNames) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonHierarchyApp commonHierarchyApp = CommonHierarchyApp.create(driver, wait, WIDGET_ID);
        commonHierarchyApp.callAvailableAction(interfaceNames, resourceName);
    }

    private Input getEmptyTextFieldComponent(String componentId) {
        Input component = wizard.getComponent(componentId, Input.ComponentType.TEXT_FIELD);
        component.clear();
        return component;
    }

}
