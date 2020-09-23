/* @(#) $$Id$$
 *
 * Copyright (c) 2000-2020 Comarch SA All Rights Reserved. Any usage,
 * duplication or redistribution of this software is allowed only according to
 * separate agreement prepared in written between Comarch and authorized party.
 */
package com.oss.pages.transport;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author Kamil Szota
 */
public class VRFOverviewPage extends BasePage {

    private static final String NAME = "VRF Name";
    private static final String DESCRIPTION = "Description";
    private static final String ADDRESS_FAMILY_IPV4 = "Address Family IPv4";
    private static final String ADDRESS_FAMILY_IPV6 = "Address Family IPv6";
    private static final String ROUTE_DISTINGUISHER = "Route Distinguisher";
    private static final String DEVICE_NAME = "Device";
    private static final String INTERFACE_ASSIGNMENT_TAB_PATH = "//div[text()='Interface Assignment']";
    private static final String INTERFACE_NAME_PATH_PATTERN = ".//div[text()='%s']";
    private static final String EDIT_BUTTON_PATH = "//a[text()='Edit']";
    private static final String REMOVE_BUTTON_PATH = "//a[text()='Remove']";
    private static final String OK_BUTTON_PATH = "//button[text()='Ok']";
    private static final String ROUTE_TARGET_ASSIGNMENT_TAB_PATH = "//div[text()='Route Target']";
    private static final String ADD_ROUTE_TARGET_BUTTON_PATH = "//i[@aria-label='Add Route Target']";
    private static final String ROUTE_TARGET_ASSIGNMENT_PATH_PATTERN = ".//div[text()='%s']";

    private final OldPropertyPanel propertyPanel;

    public VRFOverviewPage(WebDriver driver) {
        super(driver);
        propertyPanel = OldPropertyPanel.create(driver, wait);
    }

    public VRFWizardPage clickEdit() {
        WebElement editButton = driver.findElement(By.xpath(EDIT_BUTTON_PATH));
        DelayUtils.waitForVisibility(wait, editButton);
        editButton.click();
        return new VRFWizardPage(driver);
    }

    public void clickRemove() {
        WebElement removeButton = driver.findElement(By.xpath(REMOVE_BUTTON_PATH));
        DelayUtils.waitForVisibility(wait, removeButton);
        removeButton.click();
    }

    public void clickOk() {
        WebElement okButton = driver.findElement(By.xpath(OK_BUTTON_PATH));
        DelayUtils.waitForVisibility(wait, okButton);
        okButton.click();
    }

    public String getNameValue() {
        return getAttributeValue(NAME);
    }

    public String getDescriptionValue() {
        return getAttributeValue(DESCRIPTION);
    }

    public String getAddressFamilyIPv4Value() {
        return getAttributeValue(ADDRESS_FAMILY_IPV4);
    }

    public String getAddressFamilyIPv6Value() {
        return getAttributeValue(ADDRESS_FAMILY_IPV6);
    }

    public String getRouteDistinguisherValue() {
        return getAttributeValue(ROUTE_DISTINGUISHER);
    }

    public String getDeviceNameValue() {
        return getAttributeValue(DEVICE_NAME);
    }

    public void openInterfaceAssignmentTab() {
        DelayUtils.waitByXPath(wait, INTERFACE_ASSIGNMENT_TAB_PATH);
        WebElement interfaceAssignmentTab = driver.findElement(By.xpath(INTERFACE_ASSIGNMENT_TAB_PATH));
        interfaceAssignmentTab.click();
    }

    public String getInterface(String interfaceName) { //TODO: OSSTPT-29878 KS Poprawic ta metode i asercje dla podpietych RT i Interface'ow

        String interfacePath = String.format(INTERFACE_NAME_PATH_PATTERN, interfaceName);
        DelayUtils.waitByXPath(wait, interfacePath);
        WebElement interfaceElement = driver.findElement(By.xpath(interfacePath));
        return interfaceElement.getText();
    }

    public void openRouteTargetTab() {
        DelayUtils.waitByXPath(wait, ROUTE_TARGET_ASSIGNMENT_TAB_PATH);
        WebElement routeTargetTab = driver.findElement(By.xpath(ROUTE_TARGET_ASSIGNMENT_TAB_PATH));
        routeTargetTab.click();
    }

    public VRFImpExpRouteTargetWizardPage clickAddRouteTargetButton() {
        WebElement addButton = driver.findElement(By.xpath(ADD_ROUTE_TARGET_BUTTON_PATH));
        DelayUtils.waitForClickability(wait, addButton);
        addButton.click();
        return new VRFImpExpRouteTargetWizardPage(driver);
    }

    public String getRouteTargetAssignment(String routeTargetValue) { //TODO: OSSTPT-29878 KS Dorobić wczytywanie obiektu i jego atrybutów
        String routeTargetPath = String.format(ROUTE_TARGET_ASSIGNMENT_PATH_PATTERN, routeTargetValue);
        DelayUtils.waitByXPath(wait, routeTargetPath);
        WebElement routeTargetElement = driver.findElement(By.xpath(routeTargetPath));
        return routeTargetElement.getText();
    }

    public String getAddressFamilyValue(String addressFamilyValue) { //TODO: KS Przenieść do metody getRouteTargetAssignment
        String xPath = ".//div[text()='%s']";
        String addressFamilyXpath = String.format(xPath, addressFamilyValue);
        DelayUtils.waitByXPath(wait, addressFamilyXpath);
        WebElement addressFamilyElement = driver.findElement(By.xpath(addressFamilyXpath));
        return addressFamilyElement.getText();
    }

    private String getAttributeValue(String attributeName) {
        return propertyPanel.getPropertyValue(attributeName);
    }
}
