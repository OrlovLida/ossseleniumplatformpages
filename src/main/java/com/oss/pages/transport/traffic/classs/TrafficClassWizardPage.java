package com.oss.pages.transport.traffic.classs;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.CommonHierarchyApp;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.platform.OldInventoryView.OldInventoryViewPage;

import io.qameta.allure.Step;

/**
 * @author Kamil Sikora
 */
public class TrafficClassWizardPage extends BasePage {

    private static final String NAME_FIELD_ID = "uid-name";
    private static final String DESCRIPTION_FIELD_ID = "uid-description";
    private static final String MATCH_FIELD_ID = "uid-match-input";
    private static final String IP_PRECEDENCE_FIELD_ID = "uid-ipPrecedence";
    private static final String MPLS_FIELD_ID = "uid-mplsExperimental";
    private static final String MPLS_TOP_FIELD_ID = "uid-mplsExperimentalTopmost";
    private static final String IP_DSCP_FIELD_ID = "uid-ipDSCP-input";
    private static final String ACCESS_LIST_FIELD_ID = "uid-accessList";
    private static final String INPUT_INTERFACE_FIELD_ID = "uid-inputInterface-input";
    private static final String PROTOCOL_FIELD_ID = "uid-protocol";
    private static final String SAVE_CHANGES_BUTTON_TEST_ID = "buttonAppId-0";

    private final Wizard wizard;

    public TrafficClassWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Navigate through Common Hierarchy app widget selecting {location} -> {deviceName}")
    public void selectLocationAndDevice(String location, String deviceName) {
        CommonHierarchyApp commonHierarchyApp = CommonHierarchyApp.createByClass(driver, wait);
        commonHierarchyApp.navigateToPath(location, deviceName);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click next step button")
    public void clickNextStep() {
        wizard.clickNextStep();
    }

    @Step("Set name to {name}")
    public void setName(String name) {
        Input nameComponent = getEmptyTextFieldComponent(NAME_FIELD_ID);
        nameComponent.setSingleStringValue(name);
    }

    @Step("Set description to {description}")
    public void setDescription(String description) {
        Input descriptionComponent = getEmptyTextFieldComponent(DESCRIPTION_FIELD_ID);
        descriptionComponent.setSingleStringValue(description);
    }

    @Step("Set Ip precedence to {ipPrecedence}")
    public void setIpPrecedence(String ipPrecedence) {
        Input ipComponent = getEmptyTextFieldComponent(IP_PRECEDENCE_FIELD_ID);
        ipComponent.setSingleStringValue(ipPrecedence);
    }

    @Step("Set Mpls to {mpls}")
    public void setMpls(String mpls) {
        Input mplsComponent = getEmptyTextFieldComponent(MPLS_FIELD_ID);
        mplsComponent.setSingleStringValue(mpls);
    }

    @Step("Set Mpsl Top to {mplsTop}")
    public void setMplsTop(String mplsTop) {
        Input mplsTopComponent = getEmptyTextFieldComponent(MPLS_TOP_FIELD_ID);
        mplsTopComponent.setSingleStringValue(mplsTop);
    }

    @Step("Set Access list to {accessList}")
    public void setAccessList(String accessList) {
        Input accessListComponent = getEmptyTextFieldComponent(ACCESS_LIST_FIELD_ID);
        accessListComponent.setSingleStringValue(accessList);
    }

    @Step("Set Protocol to {protocol}")
    public void setProtocol(String protocol) {
        Input protocolComponent = getEmptyTextFieldComponent(PROTOCOL_FIELD_ID);
        protocolComponent.setSingleStringValue(protocol);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Set Match type to {type}")
    public void setMatchType(String type) {
        Input typeComponent = getEmptyComboBoxComponent(MATCH_FIELD_ID);
        typeComponent.setSingleStringValue(type);
    }

    @Step("Set Ip dscp to {ipDscp}")
    public void setIpDscp(String ipDscp) {
        Input ipDscpComponent = getEmptyComboBoxComponent(IP_DSCP_FIELD_ID);
        ipDscpComponent.setSingleStringValue(ipDscp);
    }

    @Step("Set Input interface to {inputInterface}")
    public void setInputInterface(String inputInterface) {
        Input inputInterfaceComponent = getEmptyComboBoxComponent(INPUT_INTERFACE_FIELD_ID);
        inputInterfaceComponent.setSingleStringValue(inputInterface);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click accept button")
    public OldInventoryViewPage clickAccept() {
        wizard.clickAccept();
        return new OldInventoryViewPage(driver);
    }

    @Step("Click save chenges button")
    public OldInventoryViewPage clickSaveChanges() {
        Button saveButton = Button.createBySelectorAndId(driver, "a", SAVE_CHANGES_BUTTON_TEST_ID);
        saveButton.click();
        return new OldInventoryViewPage(driver);
    }

    private Input getEmptyTextFieldComponent(String componentId) {
        Input component = wizard.getComponent(componentId, Input.ComponentType.TEXT_FIELD);
        component.clear();
        return component;
    }

    private Input getEmptyComboBoxComponent(String componentId) {
        Input component = wizard.getComponent(componentId, Input.ComponentType.COMBOBOX);
        component.clear();
        return component;
    }

}
