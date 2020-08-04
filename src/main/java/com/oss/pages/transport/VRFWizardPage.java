/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.transport;

import com.google.common.collect.Iterables;
import com.oss.framework.components.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Collection;
import java.util.List;


/**
 * @author Kamil Szota
 */
public class VRFWizardPage extends BasePage {

    private static final String NAME_FIELD_ID = "uidFieldName";
    private static final String ROUTE_DISTINGUISHER_FIELD_ID = "uidFieldRouteDistinguisher";
    private static final String DESCRIPTION_FIELD_ID = "uidFieldDescription";
    private static final String DEVICE_FIELD_ID = "uidFieldDevice";
    private static final String NEXT_STEP_BUTTON_PATH = "//button[text()='Next Step']";
    private static final String RESOURCE_NAME_PATH_PATTERN = "//span[text()='%s']";
    private static final String INTERFACE_SEARCH_FIELD_PATH = "//input[contains(@class,'form-control SearchText')]";
    private static final String SELECT_BUTTON_PATH = ".//span[text()='Select']";

    private final Wizard wizard;

    public VRFWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    public void setName(String name) {
        Input nameComponent = wizard.getComponent(NAME_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        nameComponent.clear();
        nameComponent.setSingleStringValue(name);
    }

    public void setRouteDistinguisher(String routeDistinguisher) {
        Input routeDistinguisherComponent = wizard.getComponent(ROUTE_DISTINGUISHER_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        routeDistinguisherComponent.clear();
        routeDistinguisherComponent.setSingleStringValue(routeDistinguisher);
    }

    public void setDescription(String description) {
        Input descriptionComponent = wizard.getComponent(DESCRIPTION_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        descriptionComponent.clear();
        descriptionComponent.setSingleStringValue(description);
    }

    public void selectDevice(String deviceName) {
        Input deviceComponent = wizard.getComponent(DEVICE_FIELD_ID, Input.ComponentType.SEARCH_FIELD);
        deviceComponent.clear();
        deviceComponent.setSingleStringValue(deviceName);
    }

    public void clickNextStep() {
        WebElement nextStepButton = driver.findElement(By.xpath(NEXT_STEP_BUTTON_PATH));
        DelayUtils.waitForClickability(wait, nextStepButton);
        nextStepButton.click();
    }

    public VRFOverviewPage clickAccept() {
        wizard.clickAcceptOldWizard();
        return new VRFOverviewPage(driver);
    }

    public void selectInterfacesInResourceTree(String resourceName, List<String> interfaceNames) {
        String resourcePath = String.format(RESOURCE_NAME_PATH_PATTERN, resourceName);
        DelayUtils.waitByXPath(wait, resourcePath);
        WebElement resourceElement = driver.findElement(By.xpath(resourcePath));
        DelayUtils.waitForClickability(wait, resourceElement);
        resourceElement.click();

        for (String interfaceName : interfaceNames) {
            selectInterface(interfaceName);
        }
    }

    private void selectInterface(String interfaceName) {
        searchInterface(interfaceName);
        selectInterface();
    }

    private void searchInterface(String interfaceName) {
        DelayUtils.waitByXPath(wait, INTERFACE_SEARCH_FIELD_PATH);
        WebElement searchField = driver.findElement(By.xpath(INTERFACE_SEARCH_FIELD_PATH));
        DelayUtils.waitByXPath(wait, INTERFACE_SEARCH_FIELD_PATH);
        searchField.clear();
        searchField.sendKeys(interfaceName);
        searchField.sendKeys(Keys.RETURN);
    }

    private void selectInterface() {
        Actions action = new Actions(driver);
        DelayUtils.waitByXPath(wait, SELECT_BUTTON_PATH);
        WebElement selectButton = getSelectInterfaceButton();

        action.moveToElement(selectButton).click().perform();
    }

    private WebElement getSelectInterfaceButton() {
        Collection<WebElement> selectButtons = driver.findElements(By.xpath(SELECT_BUTTON_PATH));

        for (int i = 0; i < 10 && selectButtons.size() > 1; i++) {
            DelayUtils.sleep(100);
            selectButtons = driver.findElements(By.xpath(SELECT_BUTTON_PATH));
        }
        return Iterables.getLast(selectButtons);
    }
}
