package com.oss.pages.transport.aggregatedEthernetInterface;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.CommonHierarchyApp;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

/**
 * @author Kamil Jacko
 */
public class AEIWizardPage extends BasePage {

    private final Wizard wizard;

    private static final String NUMBER_FIELD_ID = "uid-number";
    private static final String NAME_FIELD_ID = "uid-name";
    private static final String DESCRIPTION_FIELD_ID = "uid-description";
    private static final String AGGREGATION_PROTOCOL_FIELD_ID = "uid-aggregationProtocol";
    private static final String LACP_MODE_FIELD_ID = "uid-lacpMode";
    private static final String LACP_SHORT_PERIOD_PATH = "uid-lacpShortPeriod";
    private static final String MINIMUM_ACTIVE_LINKS_FIELD_ID = "uid-minimumActiveLinks";
    private static final String MINIMUM_BANDWIDTH_FIELD_ID = "uid-minimumBandwidthMbps";
    private static final String MTU_FIELD_ID = "uid-mtu";
    private static final String MAC_ADDRESS_FIELD_ID = "uid-macAddress";
    private static final String ADMINISTRATIVE_STATE_FIELD_ID = "uid-administrativeState";
    private static final String ENCAPSULATION_FIELD_ID = "uid-encapsulation";

    private static final String COMMON_HIERARCHY_APP_CLASS = "md-input";

    private static final String LOCATION = "Warszawa-BU139";
    private static final String DEVICE = "SeleniumASR9001KSZ";
    private static final String DEVICE_TYPE = "Router-39";

    private static final String INTERFACE_SELECT_PATTERN = "//span[text()='%s-%s\\%s']";
    private static final String SELECT_BUTTON_PATTERN = "%s//following::button[1]";

    public AEIWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    public void setDeviceNumber(String number) {
        Input numberComponent = wizard.getComponent(NUMBER_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        numberComponent.clear();
        numberComponent.setSingleStringValue(number);
    }

    public void setName(String name) {
        Input nameComponent = wizard.getComponent(NAME_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        nameComponent.clear();
        nameComponent.setSingleStringValue(name);
    }

    public void setDescription(String description) {
        Input descriptionComponent = wizard.getComponent(DESCRIPTION_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        descriptionComponent.clear();
        descriptionComponent.setSingleStringValue(description);
    }

    public void setAggregationProtocol(String aggregationProtocol) {
        Input aggregateProtocolComponent = wizard.getComponent(AGGREGATION_PROTOCOL_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        aggregateProtocolComponent.clear();
        aggregateProtocolComponent.setSingleStringValue(aggregationProtocol);
    }

    public void setLACPMode(String lacpMode) {
        Input LACPModeComponent = wizard.getComponent(LACP_MODE_FIELD_ID, Input.ComponentType.COMBOBOX);
        LACPModeComponent.clear();
        LACPModeComponent.setSingleStringValue(lacpMode);
    }

    public void setLACPShortPeriod(String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Input checkbox = wizard.getComponent(LACP_SHORT_PERIOD_PATH, Input.ComponentType.CHECKBOX);
        checkbox.setSingleStringValue(value);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void setMinimumActiveLinks(String minimumActiveLinks) {
        Input MinimumActiveLinksComponent = wizard.getComponent(MINIMUM_ACTIVE_LINKS_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        MinimumActiveLinksComponent.clear();
        MinimumActiveLinksComponent.setSingleStringValue(minimumActiveLinks);
    }

    public void setMinimumBandwidth(String minimumBandwidth) {
        Input minimumBandwidthComponent = wizard.getComponent(MINIMUM_BANDWIDTH_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        minimumBandwidthComponent.clear();
        minimumBandwidthComponent.setSingleStringValue(minimumBandwidth);
    }

    public void setMTU(String mtu) {
        Input MTUComponent = wizard.getComponent(MTU_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        MTUComponent.clear();
        MTUComponent.setSingleStringValue(mtu);
    }

    public void setMACAddress(String macAddress) {
        Input MACAddressComponent = wizard.getComponent(MAC_ADDRESS_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        MACAddressComponent.clear();
        MACAddressComponent.setSingleStringValue(macAddress);
    }

    public void setAdministrativeState(String administrativeState) {
        Input administrativeStateComponent = wizard.getComponent(ADMINISTRATIVE_STATE_FIELD_ID, Input.ComponentType.COMBOBOX);
        administrativeStateComponent.clear();
        DelayUtils.waitForPageToLoad(driver, wait);
        administrativeStateComponent.setSingleStringValue(administrativeState);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void setEncapsulation(String encapsulation) {
        Input encapsulationComponent = wizard.getComponent(ENCAPSULATION_FIELD_ID, Input.ComponentType.TEXT_FIELD);
        encapsulationComponent.clear();
        encapsulationComponent.setSingleStringValue(encapsulation);
    }

    public void clickNextStep() {
        wizard.clickNextStep();
    }

    public void searchLocationAndDevice() {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonHierarchyApp hierarchyApp = CommonHierarchyApp.createByClass(driver, COMMON_HIERARCHY_APP_CLASS, wait);
        DelayUtils.waitForPageToLoad(driver, wait);
        hierarchyApp.setFirstObjectInHierarchy(LOCATION);
        DelayUtils.waitForPageToLoad(driver, wait);
        hierarchyApp.setNextObjectInHierarchy(DEVICE);
    }

    public void selectInterfacesInResourceTree(List<String> interfaceNames) {
        DelayUtils.waitForPageToLoad(driver, wait);
        for (String interfaceName : interfaceNames) {
            searchInterface(interfaceName);
        }
    }

    private void searchInterface(String interfaceName) {
        Actions action = new Actions(driver);
        String resourcePath = String.format(INTERFACE_SELECT_PATTERN, LOCATION, DEVICE_TYPE, interfaceName);
        WebElement selectedElement = driver.findElement(By.xpath(resourcePath));
        DelayUtils.waitByXPath(wait, resourcePath);
        action.moveToElement(selectedElement).click().perform();
        selectInterface(resourcePath);
    }

    private void selectInterface(String resourcePath) {
        Actions action = new Actions(driver);
        String selectButtonPath = String.format(SELECT_BUTTON_PATTERN, resourcePath);
        WebElement selectButton = getSelectInterfaceButton(selectButtonPath);
        DelayUtils.waitByXPath(wait, selectButtonPath);
        action.moveToElement(selectButton).click().perform();
    }

    private WebElement getSelectInterfaceButton(String selectedButtonPath) {
        return driver.findElement(By.xpath(selectedButtonPath));
    }

    public AEIInventoryViewPage clickAccept() {
        wizard.clickAcceptOldWizard();
        return new AEIInventoryViewPage(driver);
    }
}
