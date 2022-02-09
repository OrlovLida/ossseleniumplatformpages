package com.oss.pages.transport;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.navigation.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.framework.widgets.dockedpanel.DockedPanel;
import com.oss.framework.widgets.dockedpanel.DockedPanelInterface;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.tabs.TabsWidget;
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

public class NetworkViewPage extends BasePage {

    public static final String ATTRIBUTES_AND_TERMINATIONS_ACTION = "EDIT_Attributes and terminations-null";
    public static final String CREATE_DEVICE_ACTION = "CREATE_Create Device-null";
    public static final String DELETE_TRAIL_ACTION = "Delete Trail";
    public static final String DELETE_ELEMENT_ACTION = "Delete Element-null";
    public static final String DELETE_DEVICE_ACTION = "EDIT_Delete Device-null";
    public static final String ADD_TO_VIEW_ACTION = "add_to_view_group";
    public static final String CREATE_MEDIATION_CONFIGURATION_ID = "CREATE_Mediation Configuration-null";
    public static final String DELETE_CONNECTION_ID = "EDIT_Delete-null";
    public static final String CREATE_CONNECTION_ID = "CREATE_Create Connection-null";
    public static final String HIERARCHY_VIEW_ACTION = "NAVIGATION_Hierarchy View-null";
    public static final String DEVICE_ACTION = "add_to_view_group_Device-null";
    public static final String CONNECTION_ACTION = "add_to_view_group_Connection-null";
    private static final String CREATE_CONNECTION_ACTION = "Create Connection";
    private static final String DELETE_TERMINATION_ACTION = "Delete termination";
    private static final String START_EDITING_CONNECTION_ACTION = "EDIT_Start editing Connection-null";
    private static final String STOP_EDITING_CONNECTION_ACTION = "EDIT_Stop editing Connection-null";
    private static final String TERMINATION_ACTION = "add_to_group_Termination-null";
    private static final String ROUTING = "add_to_group_Routing-null";
    private static final String ACCEPT_BUTTON = "Accept";
    private static final String DELETE_BUTTON = "Delete";
    private static final String ADD_TO_GROUP_ACTION = "add_to_group";
    private static final String TRAIL_TYPE_COMBOBOX_ID = "trailTypeCombobox";
    private static final String LEFT_PANEL_TAB_ID = "LeftPanelWidget";
    private static final String VALIDATION_RESULT_ID = "Validation Results";
    private static final String ROUTING_TABLE_APP_ID = "routing-table-app";
    private static final String TRAIL_TYPES_POPUP_ID = "trailTypesPopup";
    private static final String SUPPRESSION_WIZARD_ID = "plaSuppressionWizard";
    private static final String SUPPRESSION_WIZARD_CONTEXT_ACTION_ID = "frameworkCustomMore_Suppression wizard";
    private static final String PROPERTY_PANEL_ID = "NetworkViewPropertyPanelWidget";
    private static final String BOTTOM_TABS_ID = "bottomTabs";
    private static final String ACTION_CONTAINER_ID = "undefined-windowToolbar";

    private Wizard wizard = Wizard.createWizard(driver, wait);

    public NetworkViewPage(WebDriver driver) {
        super(driver);
    }

    @Deprecated //action performed not in network view
    @Step("Open Network View")
    public void openNetworkView() {
        waitForPageToLoad();
        SideMenu sideMenu = SideMenu.create(driver, wait);
        sideMenu.callActionByLabel("Network View", "Views", "Transport");
        waitForPageToLoad();
    }

    @Step("Use context action {action}")
    public void useContextAction(String action) {
        getMainActionContainer().callActionById(action);
        waitForPageToLoad();
    }

    @Step("Use context action {action} from group {group}")
    public void useContextAction(String group, String action) {
        getMainActionContainer().callActionById(group, action);
        waitForPageToLoad();
    }

    @Step("Use context action {action} from group {group} and confirm action by clicking {confirmation}")
    public void useContextActionAndClickConfirmation(String group, String action, String confirmation) {
        useContextAction(group, action);
        clickConfirmationBoxButtonByLabel(confirmation);
    }

    @Step("Open new Trail create wizard")
    public void openWizardPage(String trailType) {
        useContextAction(ActionsContainer.CREATE_GROUP_ID, CREATE_CONNECTION_ACTION);
        selectTrailType(trailType);
        clickConfirmationBoxButtonByLabel(ACCEPT_BUTTON);
    }

    @Step("Add selected objects to Routing")
    public RoutingWizardPage addSelectedObjectsToRouting() {
        useContextAction(ADD_TO_GROUP_ACTION, ROUTING);
        return new RoutingWizardPage(driver);
    }

    @Step("Add selected objects to Termination V2")
    public TerminationStepPage addSelectedObjectsToTerminationV2() {
        useContextAction(ADD_TO_GROUP_ACTION, TERMINATION_ACTION);
        return new TerminationStepPage(driver);
    }

    @Step("Add selected objects to Termination")
    public TerminationWizardPage addSelectedObjectsToTermination() {
        useContextAction(ADD_TO_GROUP_ACTION, TERMINATION_ACTION);
        return new TerminationWizardPage(driver);
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

    @Step("Open Trail update wizard")
    public <T extends TrailWizardPage> T openUpdateTrailWizard(Class<T> trailWizardPageClass) {
        useContextAction(ActionsContainer.EDIT_GROUP_ID, ATTRIBUTES_AND_TERMINATIONS_ACTION);
        try {
            return getWizardPage(trailWizardPageClass);
        } catch (Exception ignored) {
            throw new IllegalStateException("Cannot get Trail wizard page: " + trailWizardPageClass.getSimpleName());
        }
    }

    @Step("Click Start editing trail button")
    public void startEditingSelectedTrail() {
        useContextAction("EDIT",START_EDITING_CONNECTION_ACTION);
    }

    @Step("Click Stop editing trail button")
    public void stopEditingTrail() {
        useContextAction("EDIT",STOP_EDITING_CONNECTION_ACTION);
    }

    @Step("Click confirmation box button")
    public void clickConfirmationBoxButtonByLabel(String label) {
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel(label);
        waitForPageToLoad();
    }

    @Step("Select trail type")
    public void selectTrailType(String trailType) {
        Wizard popup = Wizard.createByComponentId(driver, wait, TRAIL_TYPES_POPUP_ID);
        popup.setComponentValue(TRAIL_TYPE_COMBOBOX_ID, trailType, COMBOBOX);
    }

    @Step("Create Device by context action")
    public DeviceWizardPage openCreateDeviceWizard() {
        useContextAction(ActionsContainer.CREATE_GROUP_ID, CREATE_DEVICE_ACTION);
        waitForPageToLoad();
        return new DeviceWizardPage(driver);
    }

    @Step("Accept trail type")
    public void acceptTrailType() {
        wizard.clickButtonById("wizard-submit-button-trailTypeWizardWidget");
    }

    @Step("Open modify termination wizard")
    public void modifyTermination() {
        TabsWidget tabsWidget = TabsWidget.createById(driver, wait, BOTTOM_TABS_ID);
        tabsWidget.callActionById("Modify Termination");
    }

    @Step("Set trail termination port")
    public void setTrailPort(String port) {
        wizard.setComponentValue("portId", port, SEARCH_FIELD);
    }

    @Step("Expand docked panel")
    public void expandDockedPanel(String position) {
        waitForPageToLoad();
        DockedPanelInterface dockedPanel = DockedPanel.createDockedPanelByPosition(driver, wait, position);
        dockedPanel.expandDockedPanel(position);
    }

    @Step("Hide docked panel")
    public void hideDockedPanel(String position) {
        waitForPageToLoad();
        DockedPanelInterface dockedPanel = DockedPanel.createDockedPanelByPosition(driver, wait, position);
        dockedPanel.hideDockedPanel(position);
    }

    @Step("Select object in view content")
    public void selectObjectInViewContent(String name, String value) {
        waitForPageToLoad();
        TableInterface table = OldTable.createById(driver, wait, LEFT_PANEL_TAB_ID);
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

    @Step("Click on object in View content by partial name: {partialName} and index {index}")
    public void clickOnObject(String partialName, int index) {
        expandViewContentPanel();
        OldTable oldTable = OldTable.createById(driver, wait, LEFT_PANEL_TAB_ID);
        oldTable.selectRowByPartialNameAndIndex(partialName, index);
        waitForPageToLoad();
    }

    @Deprecated //xpath allowed only in Framework
    @Step("Check object presence in View content")
    public boolean isObjectInViewContent(String partialName) {
        String xpath = getXPathForObjectInViewContentByPartialName(partialName);
        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }

    @Step("Select object in details tab")
    public void selectObjectInDetailsTab(String name, String value) {
        waitForPageToLoad();
        TableInterface table = OldTable.createById(driver, wait, BOTTOM_TABS_ID);
        table.selectRowByAttributeValueWithLabel(name, value);
    }

    @Step("Select Termination in details panel")
    public void selectTermination(String column, String value) {
        openTerminationsTab();
        selectObjectInBottomPanel("termination-table", column, value);
        waitForPageToLoad();
    }

    @Deprecated //xpath allowed only in Framework
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

    @Step("Suppress incomplete routing")
    public void supressValidationResult(String reason) {
        openValidationResultsTab();
        TabsWidget tabsWidget = TabsWidget.createById(driver, wait, BOTTOM_TABS_ID);
        TableInterface table = OldTable.createById(driver, wait, "validation-results-table");
        waitForPageToLoad();
        table.selectRow(0);
        waitForPageToLoad();
        tabsWidget.callActionById(SUPPRESSION_WIZARD_CONTEXT_ACTION_ID);
        waitForPageToLoad();
        suppressionWizard().setComponentValue("reasonField", reason, TEXT_AREA);
        waitForPageToLoad();
        suppressionWizard().clickProceed();
        waitForPageToLoad();
    }

    @Step("Delete Routing Element")
    public void deleteSelectedElementsFromRouting() {
        removeSelectedTrailsFromRouting();
        refreshRoutingElements();
        waitForPageToLoad();
    }

    @Step("Remove from routing")
    public void removeSelectedTrailsFromRouting() {
        Button button = Button.createById(driver, "Remove from routing");
        button.click();
        waitForPageToLoad();
        clickConfirmationBoxButtonByLabel(DELETE_BUTTON);
        waitForPageToLoad();
    }

    @Step("Remove terminations")
    public void removeSelectedTerminations() {
        useContextAction(DELETE_TERMINATION_ACTION);
        waitForPageToLoad();
        clickConfirmationBoxButtonByLabel(DELETE_BUTTON);
        waitForPageToLoad();
    }

    @Step("Get value from Attributes panel")
    public String getAttributeValue(String attributeName) {
        expandAttributesPanel();
        OldPropertyPanel attributesPanel = OldPropertyPanel.createById(driver, wait , PROPERTY_PANEL_ID);
        return attributesPanel.getPropertyValue(attributeName);
    }

    @Step("Add element quered in advanced search")
    public void queryElementAndAddItToView(String componentId, Input.ComponentType componentType, String value) {
        AdvancedSearchWidget advancedSearchWidget = AdvancedSearchWidget.createById(driver, wait, "advancedSearch");
        advancedSearchWidget.getComponent(componentId, componentType).clearByAction();
        advancedSearchWidget.getComponent(componentId, componentType).setSingleStringValue(value);
        waitForPageToLoad();
        advancedSearchWidget.getTableComponent("win1").selectRow(0);
        DelayUtils.sleep(500);
        waitForPageToLoad();
        advancedSearchWidget.clickAdd();
        waitForPageToLoad();
    }

    private OldActionsContainer getMainActionContainer() {
        waitForPageToLoad();
        return OldActionsContainer.createById(driver, wait, ACTION_CONTAINER_ID);
    }

    private <T extends TrailWizardPage> T getWizardPage(Class<T> trailWizardPage) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Constructor<T> constructor = trailWizardPage.getConstructor(WebDriver.class);
        return constructor.newInstance(driver);
    }

    @Deprecated //xpath allowed only in Framework
    private void clickOnObjectInViewContentByPartialName(String partialName) {
        String xpath = getXPathForObjectInViewContentByPartialName(partialName);
        driver.findElement(By.xpath(xpath)).click();
        waitForPageToLoad();
    }

    private void expandViewContentPanel() {
        expandDockedPanel("left");
        waitForPageToLoad();
    }

    @Deprecated //xpath allowed only in Framework
    private String getXPathForObjectInViewContentByPartialName(String partialName) {
        return String.format("//div[contains(@class, 'Col_ColumnId_Name')]//div[contains(text(), '%s')]", partialName);
    }

    private void selectObjectInBottomPanel(String tableId, String column, String value) {
        TableInterface table = OldTable.createById(driver, wait, tableId);
        table.selectRowByAttributeValueWithLabel(column, value);
    }

    @Deprecated //xpath allowed only in Framework
    private boolean isObjectInBottomTab(String partialValue) {
        String xpath =
                String.format("//div[@data-attributename = 'bottomTabs']//div[contains(@class, 'Cell')]//a[contains(text(), '%s')]",
                        partialValue);
        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }

    private Wizard suppressionWizard() {
        return Wizard.createByComponentId(driver, wait, SUPPRESSION_WIZARD_ID);
    }

    private void openValidationResultsTab() {
        expandDetailsPanel();
        selectTabFromBottomPanel(VALIDATION_RESULT_ID);
        waitForPageToLoad();
    }

    private void refreshRoutingElements() {
        Button button = Button.createById(driver, "Refresh");
        button.click();
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

    private void selectTabFromBottomPanel(String tabName) {
        expandDetailsPanel();
        TabsWidget tabsWidget = TabsWidget.createById(driver, wait, BOTTOM_TABS_ID);
        tabsWidget.selectTabByLabel(tabName);
    }

    @Deprecated //xpath allowed only in Framework
    private void openSubTabFromBottomPanel(String subTabName) {
        expandDetailsPanel();
        String xpath = String.format("//div[@data-attributename = 'bottomTabs']//span[text() = '%s']/..", subTabName);
        driver.findElement(By.xpath(xpath)).click();
    }

    private void expandAttributesPanel() {
        expandDockedPanel("right");
        waitForPageToLoad();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
