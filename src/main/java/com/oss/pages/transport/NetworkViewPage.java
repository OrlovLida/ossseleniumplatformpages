package com.oss.pages.transport;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;

import com.oss.framework.alerts.SystemMessageContainer;
import com.oss.framework.alerts.SystemMessageContainer.Message;
import com.oss.framework.alerts.SystemMessageContainer.MessageType;
import com.oss.framework.alerts.SystemMessageInterface;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.dockedPanel.DockedPanel;
import com.oss.framework.widgets.dockedPanel.DockedPanelInterface;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class NetworkViewPage extends BasePage {

    public NetworkViewPage(WebDriver driver) {
        super(driver);
    }

    public static NetworkViewPage goToNetworkViewPageLive(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/transport/trail/network?" + "perspective=LIVE", basicURL));
        return new NetworkViewPage(driver);
    }

    @Step("Expand docked panel")
    public void expandDockedPanel(String position) {
        DelayUtils.waitForPageToLoad(driver, wait);
        DockedPanelInterface dockedPanel = DockedPanel.createDockedPanelByPosition(driver, wait, position);
        dockedPanel.expandDockedPanel(position);
    }

    @Step("Select object in view content")
    public void selectObjectInViewContent(String name, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, "leftPanelTab");
        table.selectRowByAttributeValueWithLabel(name, value);
    }

    @Step("Select object in details tab")
    public void selectObjectInDetailsTab(String name, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, "bottomTabs");
        table.selectRowByAttributeValueWithLabel(name, value);
    }

    @Step("Use context action")
    public void useContextAction(String group, String action) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, "dockedPanel-left");
        table.callAction(group, action);
    }

    @Step("Add element quered in advanced search")
    public void queryElementAndAddItToView(String componentId, Input.ComponentType componentType, String value) {
        AdvancedSearch advancedSearch = AdvancedSearch.createById(driver, wait, "advancedSearch");
        advancedSearch.getComponent(componentId, componentType).clearByAction();
        advancedSearch.getComponent(componentId, componentType).setSingleStringValue(value);
        DelayUtils.waitForPageToLoad(driver, wait);
        advancedSearch.getTableWidget().selectFirstRow();
        DelayUtils.sleep(500);
        DelayUtils.waitForPageToLoad(driver, wait);
        advancedSearch.clickAdd();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click confirmation box button")
    public void clickConfirmationBoxButtonByLabel(String label) {
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel(label);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private Wizard physicalDeviceWizard = Wizard.createWizard(driver, wait);

    @Step("Set model")
    public void setModel(String model) {
        Input input = physicalDeviceWizard.getComponent("search_model", SEARCH_FIELD);
        input.setSingleStringValue(model);
    }

    @Step("Set name")
    public void setName(String name) {
        Input input = physicalDeviceWizard.getComponent("text_name", TEXT_FIELD);
        input.setSingleStringValue(name);
    }

    @Step("Set hostname")
    public void setHostname(String hostname) {
        Input input = physicalDeviceWizard.getComponent("text_hostname", TEXT_FIELD);
        input.setSingleStringValue(hostname);
    }

    @Step("Create device")
    public void create() {
        physicalDeviceWizard.clickActionById("physical_device_common_buttons_app-1");
    }

    @Step("Select trail type")
    public void selectTrailType(String trailType) {
        Input input = physicalDeviceWizard.getComponent("trailTypeCombobox", COMBOBOX);
        input.setSingleStringValue(trailType);
    }

    @Step("Accept trail type")
    public void acceptTrailType() {
        physicalDeviceWizard.clickActionById("wizard-submit-button-trailTypeWizardWigdet");
    }

    @Step("Set trail name")
    public void setTrailName(String name) {
        Input input = physicalDeviceWizard.getComponent("name-uid", TEXT_FIELD);
        input.setSingleStringValue(name);
    }

    @Step("Create trail")
    public void proceedTrailCreation() {
        physicalDeviceWizard.clickActionById("IP_LINK_BUTTON_APP_ID-1");
    }

    @Step("Open modify termination wizard")
    public void modifyTermination() {
        ButtonContainer button = ButtonContainer.create(driver, wait);
        button.callActionById("Modify termination");
    }

    @Step("Set trail termination port")
    public void setTrailPort(String port) {
        Input input = physicalDeviceWizard.getComponent("portId", SEARCH_FIELD);
        input.setSingleStringValue(port);
    }

    @Step("Click Proceed")
    public void clickProceed() {
        Wizard.createWizard(driver, wait).proceed();
    }

}
