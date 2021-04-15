package com.oss.pages.transport;

import java.lang.reflect.Constructor;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.framework.widgets.dockedPanel.DockedPanel;
import com.oss.framework.widgets.dockedPanel.DockedPanelInterface;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.framework.widgets.tabswidget.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.transport.trail.RoutingWizardPage;
import com.oss.pages.transport.trail.TerminationWizardPage;
import com.oss.pages.transport.trail.TrailWizardPage;
import com.oss.pages.transport.trail.v2.TerminationStepPage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_AREA;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class NetworkViewPage extends BasePage {

    private static final String ATTRIBUTES_AND_TERMINATIONS_ACTION = "Attributes and terminations";
    private static final String CREATE_CONNECTION_ACTION = "Create Connection";
    private static final String CREATE_DEVICE_ACTION = "Create Device";
    private static final String DELETE_TRAIL_ACTION = "Delete Trail";
    private static final String DELETE_ELEMENT_ACTION = "Delete Element";
    private static final String DELETE_CONNECTION_ACTION = "Delete Connection";
    private static final String DELETE_TERMINATION_ACTION = "Delete termination";
    private static final String START_EDITING_CONNECTION_ACTION = "Start editing connection";
    private static final String STOP_EDITING_CONNECTION_ACTION = "Stop editing connection";
    private static final String TERMINATION_ACTION = "Termination";
    private static final String ROUTING = "Routing";
    private static final String ACCEPT_BUTTON = "Accept";
    private static final String DELETE_BUTTON = "Delete";
    private static final String ADD_TO_GROUP_ACTION = "add_to_group";
    private static final String TRAIL_TYPE_COMBOBOX_ID = "trailTypeCombobox";
    private static final String DOCKED_PANEL_LEFT_ID = "dockedPanel-left";
    private static final String LEFT_PANEL_TAB_ID = "leftPanelTab";
    private static final String VALIDATION_RESULT_ID = "Validation Results";
    private static final String ROUTING_TABLE_APP_ID = "routing-table-app";

    private Wizard physicalDeviceWizard = Wizard.createWizard(driver, wait);

    public NetworkViewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open Network View")
    public void openNetworkView() {
        waitForPageToLoad();
        SideMenu sideMenu = SideMenu.create(driver, wait);
        sideMenu.callActionByLabel("Network View", "Views", "Transport");
        waitForPageToLoad();
    }

    @Step("Open new Trail create wizard")
    public void openCreateTrailWizardV2(String trailType) {
        openWizardPage(trailType);
    }

    @Step("Open new Trail update wizard")
    public void openUpdateTrailWizardV2() {
        useContextAction(ActionsContainer.EDIT_GROUP_ID, ATTRIBUTES_AND_TERMINATIONS_ACTION);
        waitForPageToLoad();
    }

    @Step("Open Trail create wizard")
    public <T extends TrailWizardPage> T openCreateTrailWizard(Class<T> trailWizardPageClass) {
        T trailWizardPage;
        try {
            trailWizardPage = getWizardPage(trailWizardPageClass);
        } catch (Exception ignored) {
            throw new IllegalStateException("Cannot get Trail wizard page: " + trailWizardPageClass.getSimpleName());
        }
        openWizardPage(trailWizardPage.getTrailType());
        return trailWizardPage;
    }

    private void openWizardPage(String trailType) {
        useContextAction(ActionsContainer.CREATE_GROUP_ID, CREATE_CONNECTION_ACTION);
        selectTrailType(trailType);
        clickButton(ACCEPT_BUTTON);
        waitForPageToLoad();
    }

    @Step("Select trail type")
    public void selectTrailType(String trailType) {
        Wizard popup = Wizard.createPopupWizard(driver, wait);
        popup.setComponentValue(TRAIL_TYPE_COMBOBOX_ID, trailType, COMBOBOX);
    }

    @Step("Open Trail update wizard")
    public <T extends TrailWizardPage> T openUpdateTrailWizard(Class<T> trailWizardPageClass) {
        useContextAction(ActionsContainer.EDIT_GROUP_ID, ATTRIBUTES_AND_TERMINATIONS_ACTION);
        waitForPageToLoad();
        try {
            return getWizardPage(trailWizardPageClass);
        } catch (Exception ignored) {
            throw new IllegalStateException("Cannot get Trail wizard page: " + trailWizardPageClass.getSimpleName());
        }
    }

    private <T extends TrailWizardPage> T getWizardPage(Class<T> trailWizardPage) throws Exception {
        Constructor<T> constructor = trailWizardPage.getConstructor(WebDriver.class);
        return constructor.newInstance(driver);
    }

    @Step("Create Device by context action")
    public DeviceWizardPage openCreateDeviceWizard() {
        useContextAction(ActionsContainer.CREATE_GROUP_ID, CREATE_DEVICE_ACTION);
        waitForPageToLoad();
        return new DeviceWizardPage(driver);
    }

    @Step("Delete selected trails by context action")
    public void deleteSelectedTrails() {
        useContextAction(ActionsContainer.EDIT_GROUP_ID, DELETE_TRAIL_ACTION);
        waitForPageToLoad();
        clickButton("Proceed");
        waitForPageToLoad();
    }

    @Step
    public void deleteSelectedConnections() {
        useContextAction(ActionsContainer.EDIT_GROUP_ID, DELETE_CONNECTION_ACTION);
        waitForPageToLoad();
        clickButton(DELETE_BUTTON);
    }

    @Step("Delete selected elements by context action")
    public void deleteSelectedElement() {
        useContextAction(ActionsContainer.EDIT_GROUP_ID, DELETE_ELEMENT_ACTION);
        waitForPageToLoad();
        clickButton("Yes");
        waitForPageToLoad();
    }

    @Step("Click Start editing trail button")
    public void startEditingSelectedTrail() {
        Button button = Button.create(driver, START_EDITING_CONNECTION_ACTION);
        button.click();
    }

    @Step("Click Stop editing trail button")
    public void stopEditingTrail() {
        Button button = Button.create(driver, STOP_EDITING_CONNECTION_ACTION);
        button.click();
    }

    @Step("Add selected objects to Routing")
    public RoutingWizardPage addSelectedObjectsToRouting() {
        useContextAction(ADD_TO_GROUP_ACTION, ROUTING);
        waitForPageToLoad();
        return new RoutingWizardPage(driver);
    }

    @Step("Add selected objects to Termination V2")
    public TerminationStepPage addSelectedObjectsToTerminationV2() {
        useContextAction(ADD_TO_GROUP_ACTION, TERMINATION_ACTION);
        waitForPageToLoad();
        return new TerminationStepPage(driver);
    }

    @Step("Add selected objects to Termination")
    public TerminationWizardPage addSelectedObjectsToTermination() {
        useContextAction(ADD_TO_GROUP_ACTION, TERMINATION_ACTION);
        waitForPageToLoad();
        return new TerminationWizardPage(driver);
    }

    @Step("Use context action")
    public void useContextAction(String group, String action) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, DOCKED_PANEL_LEFT_ID);
        table.callAction(group, action);
    }

    @Step("Select object in view content")
    public void selectObjectInViewContent(String name, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, LEFT_PANEL_TAB_ID);
        table.selectRowByAttributeValueWithLabel(name, value);
    }

    @Step("Select object in details tab")
    public void selectObjectInDetailsTab(String name, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, "bottomTabs");
        table.selectRowByAttributeValueWithLabel(name, value);
    }

    @Step("Select object in View content")
    public void selectObject(String partialName) {
        // TODO: OSSTPT-30835 - select object only when it's not selected
        expandViewContentPanel();
        clickOnObjectInViewContentByPartialName(partialName);
    }

    @Step("Unselect object in View content")
    public void unselectObject(String partialName) {
        // TODO: OSSTPT-30835 - unselect object only when it's selected
        expandViewContentPanel();
        clickOnObjectInViewContentByPartialName(partialName);
    }

    private void clickOnObjectInViewContentByPartialName(String partialName) {
        String xpath = getXPathForObjectInViewContentByPartialName(partialName);
        driver.findElement(By.xpath(xpath)).click();
        waitForPageToLoad();
    }

    @Step("Click on object in View content by partial name: {partialName} and index {index}")
    public void clickOnObject(String partialName, int index) {
        expandViewContentPanel();
        OldTable oldTable = OldTable.createByComponentDataAttributeName(driver, wait, LEFT_PANEL_TAB_ID);
        oldTable.selectRowByPartialNameAndIndex(partialName, index);
        waitForPageToLoad();
    }

    @Step("Check object presence in View content")
    public boolean isObjectInViewContent(String partialName) {
        String xpath = getXPathForObjectInViewContentByPartialName(partialName);
        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }

    private void expandViewContentPanel() {
        expandDockedPanel("left");
        waitForPageToLoad();
    }

    private String getXPathForObjectInViewContentByPartialName(String partialName) {
        return String.format("//div[contains(@class, 'Col_ColumnId_Name')]//div[contains(text(), '%s')]", partialName);
    }

    @Step("Get value from Attributes panel")
    public String getAttributeValue(String attributeName) {
        expandAttributesPanel();
        OldPropertyPanel attributesPanel = OldPropertyPanel.create(driver, wait);
        return attributesPanel.getPropertyValue(attributeName);
    }

    private void expandAttributesPanel() {
        expandDockedPanel("right");
        waitForPageToLoad();
    }

    @Step("Select Termination in details panel")
    public void selectTermination(String column, String value) {
        openTerminationsTab();
        selectObjectInBottomPanel("termination-table", column, value);
        waitForPageToLoad();
    }

    @Step("Select trail in Routing tab in details panel")
    public void selectTrailInRouting(String partialValue) {
        openRouting1stLevelTab();
        String xpath =
                String.format("//div[@data-attributename = 'bottomTabs']//div[contains(@class, 'Cell')]//a[contains(text(), '%s')]/..",
                        partialValue);
        driver.findElement(By.xpath(xpath)).click();
        waitForPageToLoad();
    }

    @Step("Select Routing element in details panel")
    public void selectRoutingElement(String column, String value) {
        openRoutingElementsTab();
        selectObjectInBottomPanel(ROUTING_TABLE_APP_ID, column, value);
        waitForPageToLoad();
    }

    private void selectObjectInBottomPanel(String tableId, String column, String value) {
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, tableId);
        table.selectRowByAttributeValueWithLabel(column, value);
    }

    @Step("Check object presence in Routing 1st level")
    public boolean isObjectInRouting1stLevel(String partialValue) {
        openRouting1stLevelTab();
        return isObjectInBottomTab(partialValue);
    }

    @Step("Check object presence in Routing Elements")
    public boolean isObjectInRoutingElements(String partialValue) {
        openRoutingElementsTab();
        return isObjectInBottomTab(partialValue);
    }

    @Step("Check object presence in Terminations")
    public boolean isObjectInTerminations(String partialValue) {
        expandDetailsPanel();
        openTerminationsTab();
        return isObjectInBottomTab(partialValue);
    }

    private boolean isObjectInBottomTab(String partialValue) {
        String xpath =
                String.format("//div[@data-attributename = 'bottomTabs']//div[contains(@class, 'Cell')]//a[contains(text(), '%s')]",
                        partialValue);
        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }

    private void openRouting1stLevelTab() {
        openRoutingTab();
        select1stLevelTab();
    }

    private void openRoutingElementsTab() {
        openRoutingTab();
        selectElementsTab();
    }

    private void openRoutingTab() {
        expandDetailsPanel();
        selectTabFromBottomPanel(ROUTING);
        waitForPageToLoad();
    }

    private void openTerminationsTab() {
        expandDetailsPanel();
        selectTabFromBottomPanel("Terminations");
        waitForPageToLoad();
    }

    private void select1stLevelTab() {
        openSubTabFromBottomPanel("1st level");
        waitForPageToLoad();
    }

    private void selectElementsTab() {
        openSubTabFromBottomPanel("Elements");
        waitForPageToLoad();
    }

    private void expandDetailsPanel() {
        expandDockedPanel("bottom");
        waitForPageToLoad();
    }

    @Step("Expand docked panel")
    public void expandDockedPanel(String position) {
        DelayUtils.waitForPageToLoad(driver, wait);
        DockedPanelInterface dockedPanel = DockedPanel.createDockedPanelByPosition(driver, wait, position);
        dockedPanel.expandDockedPanel(position);
    }

    @Step("Hide docked panel")
    public void hideDockedPanel(String position) {
        DelayUtils.waitForPageToLoad(driver, wait);
        DockedPanelInterface dockedPanel = DockedPanel.createDockedPanelByPosition(driver, wait, position);
        dockedPanel.hideDockedPanel(position);
    }

    private void selectTabFromBottomPanel(String tabName) {
        moveToTheBottomPanel();
        TabsWidget tabsWidget = TabsWidget.create(driver, wait);
        tabsWidget.selectTabByLabel(tabName);
    }

    private void moveToTheBottomPanel() {
        String xpath = "//div[@data-attributename = 'dockedPanel-bottom']";
        WebElement element = driver.findElement(By.xpath(xpath));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    private void openSubTabFromBottomPanel(String subTabName) {
        moveToTheBottomPanel();
        String xpath = String.format("//div[@data-attributename = 'bottomTabs']//span[text() = '%s']/..", subTabName);
        driver.findElement(By.xpath(xpath)).click();
    }

    @Step("Remove terminations")
    public void removeSelectedTerminations() {
        useContextAction(DELETE_TERMINATION_ACTION);
        waitForPageToLoad();
        clickButton(DELETE_BUTTON);
        waitForPageToLoad();
    }

    public void useContextAction(String action) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, DOCKED_PANEL_LEFT_ID);
        table.callAction(action);
    }

    @Step("Delete Routing Element")
    public void deleteSelectedElementsFromRouting() {
        removeSelectedTrailsFromRouting();
        refreshRoutingElements();
        waitForPageToLoad();
    }

    @Step("Remove from routing")
    public void removeSelectedTrailsFromRouting() {
        Button button = Button.createBySelectorAndId(driver, "a", "Remove from routing");
        button.click();
        waitForPageToLoad();
        clickButton(DELETE_BUTTON);
        waitForPageToLoad();
    }

    private void refreshRoutingElements() {
        Button button = Button.createBySelectorAndId(driver, "a", "Refresh");
        button.click();
    }

    @Step("Add element quered in advanced search")
    public void queryElementAndAddItToView(String componentId, Input.ComponentType componentType, String value) {
        AdvancedSearchWidget advancedSearchWidget = AdvancedSearchWidget.createById(driver, wait, "advancedSearch");
        advancedSearchWidget.getComponent(componentId, componentType).clearByAction();
        advancedSearchWidget.getComponent(componentId, componentType).setSingleStringValue(value);
        DelayUtils.waitForPageToLoad(driver, wait);
        advancedSearchWidget.getTableWidget().selectFirstRow();
        DelayUtils.sleep(500);
        DelayUtils.waitForPageToLoad(driver, wait);
        advancedSearchWidget.clickAdd();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Set model")
    public void setModel(String model) {
        physicalDeviceWizard.setComponentValue("search_model", model, SEARCH_FIELD);
    }

    @Step("Set name")
    public void setName(String name) {
        physicalDeviceWizard.setComponentValue("text_name", name, TEXT_FIELD);
    }

    @Step("Set hostname")
    public void setHostname(String hostname) {
        physicalDeviceWizard.setComponentValue("text_hostname", hostname, TEXT_FIELD);
    }

    @Step("Set serial number")
    public void setSerialNumber(String serialNumber) {
        physicalDeviceWizard.setComponentValue("text_serial_number", serialNumber, TEXT_FIELD);
    }

    @Deprecated
    @Step("Create device")
    public void create() {
        physicalDeviceWizard.clickActionById("physical_device_common_buttons_app-1");
    }

    @Step("Accept trail type")
    public void acceptTrailType() {
        physicalDeviceWizard.clickActionById("wizard-submit-button-trailTypeWizardWigdet");
    }

    @Step("Open modify termination wizard")
    public void modifyTermination() {
        ButtonContainer button = ButtonContainer.create(driver, wait);
        button.callActionById("Modify Termination");
    }

    @Step("Set trail termination port")
    public void setTrailPort(String port) {
        physicalDeviceWizard.setComponentValue("portId", port, SEARCH_FIELD);
    }

    @Step("Click Proceed")
    public void clickProceed() {
        Wizard.createWizard(driver, wait).proceed();
    }

    private void clickButton(String label) {
        clickConfirmationBoxButtonByLabel(label);
        waitForPageToLoad();
    }

    @Step("Click confirmation box button")
    public void clickConfirmationBoxButtonByLabel(String label) {
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel(label);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Delate trail")
    public void delateTrailWizard() {
        clickConfirmationBoxButtonByLabel(DELETE_BUTTON);
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Suppress incomplete routing")
    public void supressValidationResult(String validationResultType, String reason) {
        openValidationResultsTab();
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, "bottomTabs");
        table.selectRowByAttributeValueWithLabel("Type", validationResultType);
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tabsWidget = TabsWidget.createById(driver, wait, "bottomTabs");
        tabsWidget.callAction("__more-group", "Suppression wizard");
        DelayUtils.waitForPageToLoad(driver, wait);
        physicalDeviceWizard.setComponentValue("reasonField", reason, TEXT_AREA);
        clickProceed();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void openValidationResultsTab() {
        expandDetailsPanel();
        selectTabFromBottomPanel(VALIDATION_RESULT_ID);
        waitForPageToLoad();
    }

}
