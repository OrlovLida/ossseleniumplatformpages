package com.oss.pages.transport;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.components.prompts.ConfirmationBoxInterface;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.advancedsearch.AdvancedSearchWidget;
import com.oss.framework.widgets.dockedpanel.DockedPanel;
import com.oss.framework.widgets.dockedpanel.DockedPanelInterface;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.platform.configuration.ChooseConfigurationWizard;
import com.oss.pages.transport.trail.RoutingWizardPage;
import com.oss.pages.transport.trail.TerminationWizardPage;
import com.oss.pages.transport.trail.TrailWizardPage;
import com.oss.pages.transport.trail.v2.TerminationStepPage;

import io.qameta.allure.Step;

import static com.oss.framework.components.contextactions.ActionsContainer.EDIT_GROUP_ID;
import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_AREA;

public class NetworkViewPage extends BasePage {

    public static final String ATTRIBUTES_AND_TERMINATIONS_ACTION = "EDIT_Attributes and terminations-null";
    public static final String CREATE_DEVICE_ACTION = "CREATE_Create Physical Device-null";
    public static final String DELETE_ELEMENT_ACTION = "Delete Element-null";
    public static final String DELETE_DEVICE_ACTION = "EDIT_Delete Physical Device-null";
    public static final String ADD_TO_VIEW_ACTION = "add_to_view_group";
    public static final String HIERARCHY_VIEW_ACTION = "NAVIGATION_Hierarchy View-null";
    public static final String DEVICE_ACTION = "add_to_view_group_Device-null";
    public static final String CONNECTION_ACTION = "add_to_view_group_Connection-null";
    public static final String CREATE_MEDIATION_CONFIGURATION_ID = "CREATE_Mediation Configuration-null";
    public static final String DELETE_CONNECTION_ID = "EDIT_Delete-null";
    public static final String CREATE_CONNECTION_ID = "CREATE_Create Connection-null";
    public static final String MODIFY_TERMINATION_ACTION_ID = "ModifyTerminationActionId";
    public static final String TRAIL_TYPE_ACCEPT_BUTTON_ID = "wizard-submit-button-trailTypeWizardWidget";

    private static final String START_EDITING_CONNECTION_ACTION = "EDIT_Start editing Connection-null";
    private static final String STOP_EDITING_CONNECTION_ACTION = "EDIT_Stop editing Connection-null";
    private static final String TERMINATION_ACTION = "EDIT_Add to Termination-null";
    private static final String ADD_TO_GROUP_ACTION = "add_to_group";
    private static final String SUPPRESSION_WIZARD_CONTEXT_ACTION_ID = "vrSuppress";
    private static final String ACTION_CONTAINER_ID = "logicalview-windowToolbar";
    private static final String REMOVE_FROM_ROUTING_ACTION_ID = "DeleteRoutingActionId";
    private static final String DELETE_TERMINATION_ACTION_ID = "DeleteTerminationActionId";
    private static final String ADVANCED_SEARCH_WIDGET_ID = "advancedSearch";
    private static final String TRAIL_TYPE_COMBOBOX_ID = "trailTypeCombobox";
    private static final String LEFT_PANEL_TAB_ID = "LeftPanelWidget";
    private static final String VALIDATION_RESULT_ID = "Validation Results";
    private static final String ELEMENT_ROUTING_TABLE_APP_ID = "routing-elements-table-app";
    private static final String TERMINATION_TABLE_APP_ID = "TerminationIndexedWidget";
    private static final String FIRST_LEVEL_ROUTING_TABLE_APP_ID = "RoutingSegmentIndexedWidget";
    private static final String SUPPRESSION_WIZARD_ID = "plaSuppressionWizard_prompt-card";
    private static final String DELETE_FROM_ROUTING_WIZARD_ID = "deleteRoutingId_prompt-card";
    private static final String DELETE_TERMINATION_WIZARD_ID = "deleteTerminationId_prompt-card";
    private static final String BOTTOM_TABS_ID = "logicalview";
    private static final String TRAIL_TYPE_WIZARD_ID = "trailTypesPopup_prompt-card";
    private static final String GEAR_OBJECT_GROUP_ID = "frameworkCustomButtonsSecondaryGroup";
    private static final String CHOOSE_CONFIGURATION_ID = "chooseConfiguration";
    private static final String ATTRIBUTES_PANEL_ID = "NetworkViewPropertyPanelWidget";
    private static final String REFRESH_BUTTON_ID = "refreshButton";
    private static final String REFRESH_BUTTON_IN_ELEMENT_ROUTING_TAB_ID = "Reload element routing";
    private static final String REMOVE_FROM_ELEMENT_ROUTING_BUTTON_ID = "Remove from routing";
    private static final String TERMINATIONS_PANEL_ID = "table-TerminationIndexedWidget";
    private static final String VALIDATION_RESULTS_PANEL_ID = "plaValidationResultsV3ForProcessApp";
    private static final String REASON_FIELD_ID = "reasonField";
    private static final String ROUTING_ACTION_ID = "add_to_group_Routing-null";

    private static final String FIRST_LEVEL_ROUTING_TAB_LABEL = "Routing - 1st level";
    private static final String ELEMENT_ROUTING_TAB_LABEL = "Element Routing";
    private static final String TERMINATIONS_TAB_LABEL = "Terminations";
    private static final String DELETE_BUTTON_LABEL = "Delete";
    private static final String DOCKED_PANEL_LEFT_POSITION = "left";
    private static final String DOCKED_PANEL_RIGHT_POSITION = "right";
    private static final String DOCKED_PANEL_BOTTOM_POSITION = "bottom";
    private static final String TRAIL_WIZARD_ERROR_MESSAGE = "Cannot get Trail wizard page: ";

    public NetworkViewPage(WebDriver driver) {
        super(driver);
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
        useContextAction(ActionsContainer.CREATE_GROUP_ID, CREATE_CONNECTION_ID);
        selectTrailType(trailType);
        acceptTrailType();
    }

    @Step("Add selected objects to Routing")
    public RoutingWizardPage addSelectedObjectsToRouting() {
        useContextAction(ADD_TO_GROUP_ACTION, ROUTING_ACTION_ID);
        return new RoutingWizardPage(driver);
    }

    @Step("Add selected objects to Termination V2")
    public TerminationStepPage addSelectedObjectsToTerminationV2() {
        useContextAction(ADD_TO_GROUP_ACTION, TERMINATION_ACTION);
        return new TerminationStepPage(driver);
    }

    @Step("Add selected objects to Termination")
    public TerminationWizardPage addSelectedObjectsToTermination() {
        useContextAction(EDIT_GROUP_ID, TERMINATION_ACTION);
        waitForPageToLoad();
        return new TerminationWizardPage(driver);
    }

    @Step("Create Device by context action")
    public DeviceWizardPage openCreateDeviceWizard() {
        useContextAction(ActionsContainer.CREATE_GROUP_ID, CREATE_DEVICE_ACTION);
        waitForPageToLoad();
        return new DeviceWizardPage(driver);
    }

    @Step("Open Trail create wizard")
    public <T extends TrailWizardPage> T openCreateTrailWizard(Class<T> trailWizardPageClass) {
        T trailWizardPage;
        try {
            trailWizardPage = getWizardPage(trailWizardPageClass);
        } catch (Exception ignored) {
            throw new IllegalStateException(TRAIL_WIZARD_ERROR_MESSAGE + trailWizardPageClass.getSimpleName());
        }
        openWizardPage(trailWizardPage.getTrailType());
        return trailWizardPage;
    }

    @Step("Open Trail update wizard")
    public <T extends TrailWizardPage> T openUpdateTrailWizard(Class<T> trailWizardPageClass) {
        useContextAction(EDIT_GROUP_ID, ATTRIBUTES_AND_TERMINATIONS_ACTION);
        try {
            return getWizardPage(trailWizardPageClass);
        } catch (Exception ignored) {
            throw new IllegalStateException(TRAIL_WIZARD_ERROR_MESSAGE + trailWizardPageClass.getSimpleName());
        }
    }

    @Step("Click Start editing trail button")
    public void startEditingSelectedTrail() {
        useContextAction(EDIT_GROUP_ID, START_EDITING_CONNECTION_ACTION);
    }

    @Step("Click Stop editing trail button")
    public void stopEditingTrail() {
        useContextAction(EDIT_GROUP_ID, STOP_EDITING_CONNECTION_ACTION);
    }

    @Step("Click confirmation box button")
    private void clickConfirmationBoxButtonByLabel(String label) {
        ConfirmationBoxInterface prompt = ConfirmationBox.create(driver, wait);
        prompt.clickButtonByLabel(label);
        waitForPageToLoad();
    }

    @Step("Select trail type")
    public void selectTrailType(String trailType) {
        Wizard popup = Wizard.createByComponentId(driver, wait, TRAIL_TYPE_WIZARD_ID);
        popup.setComponentValue(TRAIL_TYPE_COMBOBOX_ID, trailType, COMBOBOX);
    }

    @Step("Accept trail type")
    public void acceptTrailType() {
        Wizard wizard = Wizard.createByComponentId(driver, wait, TRAIL_TYPE_WIZARD_ID);
        wizard.clickButtonById(TRAIL_TYPE_ACCEPT_BUTTON_ID);
    }

    @Step("Open modify termination wizard")
    public void modifyTermination() {
        TableWidget tabsWidget = TableWidget.createById(driver, TERMINATION_TABLE_APP_ID, wait);
        tabsWidget.callAction(EDIT_GROUP_ID, MODIFY_TERMINATION_ACTION_ID);
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

    public void expandViewContentPanel() {
        expandDockedPanel(DOCKED_PANEL_LEFT_POSITION);
        waitForPageToLoad();
    }

    private void expandDetailsPanel() {
        expandDockedPanel(DOCKED_PANEL_BOTTOM_POSITION);
        waitForPageToLoad();
    }

    public void expandAttributesPanel() {
        expandDockedPanel(DOCKED_PANEL_RIGHT_POSITION);
        waitForPageToLoad();
    }

    @Step("Select object in view content")
    public void selectObjectInViewContent(String name, String value) {
        waitForPageToLoad();
        TableInterface table = OldTable.createById(driver, wait, LEFT_PANEL_TAB_ID);
        table.selectRowByAttributeValueWithLabel(name, value);
    }

    @Step("Unselect object in view content")
    public void unselectObjectInViewContent(String name, String value) {
        waitForPageToLoad();
        OldTable table = OldTable.createById(driver, wait, LEFT_PANEL_TAB_ID);
        table.unselectRow(name, value);
    }

    // TODO: OSSTPT-30835 - select object only when it's not selected
    @Step("Select object in View content")
    public void selectObject(String partialName) {
        expandViewContentPanel();
        clickOnObjectInViewContentByPartialName(partialName);
    }

    // TODO: OSSTPT-30835 - unselect object only when it's selected
    @Step("Unselect object in View content")
    public void unselectObject(String partialName) {
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

    @Step("Select Termination in details panel")
    public void selectTermination(String attributeId, String value) {
        selectObjectInTableWidgetTab(TERMINATION_TABLE_APP_ID, attributeId, value);
        waitForPageToLoad();
    }

    @Step("Select trail in Routing tab in details panel")
    public void selectConnectionInRouting(String attributeId, String value) {
        selectObjectInTableWidgetTab(FIRST_LEVEL_ROUTING_TABLE_APP_ID, attributeId, value);
        waitForPageToLoad();
    }

    @Step("Select Routing element in details panel")
    public void selectRoutingElement(String column, String value) {
        selectObjectInOldTableTab(ELEMENT_ROUTING_TABLE_APP_ID, column, value);
        waitForPageToLoad();
    }

    @Step("Check object presence in View content")
    public boolean isObjectInViewContent(String partialName) {
        DockedPanelInterface dockedPanel = DockedPanel.createDockedPanelByPosition(driver, wait, DOCKED_PANEL_LEFT_POSITION);
        return dockedPanel.isElementPresentByPartialName(partialName);
    }

    @Step("Check object presence in Routing 1st level")
    public boolean isObjectInRouting1stLevel(String partialValue) {
        return isObjectInTableWidgetTab(FIRST_LEVEL_ROUTING_TABLE_APP_ID, partialValue);
    }

    @Step("Check object presence in Routing Elements")
    public boolean isObjectInRoutingElements(String partialValue) {
        return isObjectInOldTableTab(ELEMENT_ROUTING_TABLE_APP_ID, partialValue);
    }

    @Step("Check object presence in Terminations")
    public boolean isObjectInTerminations(String partialValue) {
        return isObjectInTableWidgetTab(TERMINATIONS_PANEL_ID, partialValue);
    }

    @Step("Suppress incomplete routing")
    public void suppressValidationResult(String columnId, String value, String reason) {
        openValidationResultsTab();
        selectObjectInTableWidgetTab(VALIDATION_RESULTS_PANEL_ID, columnId, value);
        waitForPageToLoad();
        getTableWidget(VALIDATION_RESULTS_PANEL_ID).callAction(SUPPRESSION_WIZARD_CONTEXT_ACTION_ID);
        waitForPageToLoad();
        getSuppressionWizard().setComponentValue(REASON_FIELD_ID, reason, TEXT_AREA);
        waitForPageToLoad();
        getSuppressionWizard().clickProceed();
        waitForPageToLoad();
    }

    @Step("Delete Routing Element")
    public void deleteSelectedElementsFromRouting() {
        getTabsWidget(BOTTOM_TABS_ID).callActionById(REMOVE_FROM_ELEMENT_ROUTING_BUTTON_ID);
        waitForPageToLoad();
        clickConfirmationBoxButtonByLabel(DELETE_BUTTON_LABEL);
        refreshElementRoutingTab();
    }

    @Step("Delete from routing")
    public void deleteSelectedConnectionsFromRouting() {
        getTableWidget(FIRST_LEVEL_ROUTING_TABLE_APP_ID).callAction(EDIT_GROUP_ID, REMOVE_FROM_ROUTING_ACTION_ID);
        waitForPageToLoad();
        getWizard(DELETE_FROM_ROUTING_WIZARD_ID).clickButtonByLabel(DELETE_BUTTON_LABEL);
        waitForPageToLoad();
    }

    @Step("Remove terminations")
    public void removeSelectedTerminations() {
        getTableWidget(TERMINATIONS_PANEL_ID).callAction(EDIT_GROUP_ID, DELETE_TERMINATION_ACTION_ID);
        waitForPageToLoad();
        getWizard(DELETE_TERMINATION_WIZARD_ID).clickButtonByLabel(DELETE_BUTTON_LABEL);
        waitForPageToLoad();
    }

    @Step("Add element queried in advanced search")
    public void queryElementAndAddItToView(String componentId, Input.ComponentType componentType, String value) {
        getAdvancedSearchWidget(ADVANCED_SEARCH_WIDGET_ID).getComponent(componentId, componentType).clearByAction();
        getAdvancedSearchWidget(ADVANCED_SEARCH_WIDGET_ID).getComponent(componentId, componentType).setSingleStringValue(value);
        waitForPageToLoad();
        getAdvancedSearchWidget(ADVANCED_SEARCH_WIDGET_ID).getTableComponent().selectRow(0);
        DelayUtils.sleep(500);
        waitForPageToLoad();
        getAdvancedSearchWidget(ADVANCED_SEARCH_WIDGET_ID).clickAdd();
        waitForPageToLoad();
    }

    @Step("Get value from Attributes panel")
    public String getAttributeValue(String attributeName) {
        return getPropertyPanel(ATTRIBUTES_PANEL_ID).getPropertyValue(attributeName);
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

    private void clickOnObjectInViewContentByPartialName(String partialName) {
        getOldTable(LEFT_PANEL_TAB_ID).selectRowByPartialName(partialName);
        waitForPageToLoad();
    }

    private void selectObjectInOldTableTab(String tableId, String column, String value) {
        TableInterface table = OldTable.createById(driver, wait, tableId);
        table.selectRowByAttributeValueWithLabel(column, value);
    }

    private void selectObjectInTableWidgetTab(String tableId, String attributeId, String value) {
        getTableWidget(tableId).selectRowByAttributeValue(attributeId, value);
    }

    private void selectTabFromBottomPanel(String tabName) {
        getTabsWidget(BOTTOM_TABS_ID).selectTabByLabel(tabName);
    }

    private boolean isObjectInOldTableTab(String tableId, String partialName) {
        return getOldTable(tableId).isElementPresentByPartialName(partialName);
    }

    private boolean isObjectInTableWidgetTab(String tableId, String partialName) {
        return getTableWidget(tableId).isElementPresentByPartialName(partialName);
    }

    public String getObjectValueFromTab(Integer index, String column, String componentId) {
        return getTableWidget(componentId).getCellValue(index, column);
    }

    private Wizard getSuppressionWizard() {
        return getWizard(SUPPRESSION_WIZARD_ID);
    }

    private Wizard getWizard(String wizardId){
        return Wizard.createByComponentId(driver, wait, wizardId);
    }

    private OldTable getOldTable(String oldTableId){
        return OldTable.createById(driver, wait, oldTableId);
    }

    private TableWidget getTableWidget(String tableId){
        return TableWidget.createById(driver, tableId, wait);
    }

    private TabsWidget getTabsWidget(String tabId){
        return TabsWidget.createById(driver, wait, tabId);
    }

    private PropertyPanel getPropertyPanel(String propertyPanelId){
        return PropertyPanel.createById(driver, wait, propertyPanelId);
    }

    private AdvancedSearchWidget getAdvancedSearchWidget(String advancedSearchWidgetId) {
        return AdvancedSearchWidget.createById(driver, wait, advancedSearchWidgetId);
    }

    private ChooseConfigurationWizard getChooseConfigurationWizard() {
        return ChooseConfigurationWizard.create(driver, wait);
    }

    public void refreshFirstLevelRoutingTab() {
        getTableWidget(FIRST_LEVEL_ROUTING_TABLE_APP_ID).callAction(ActionsContainer.KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
        waitForPageToLoad();
    }

    public void refreshTerminationsTab() {
        getTableWidget(TERMINATION_TABLE_APP_ID).callAction(ActionsContainer.KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
        waitForPageToLoad();
    }

    public void refreshElementRoutingTab() {
        getTabsWidget(BOTTOM_TABS_ID).callActionById(ActionsContainer.KEBAB_GROUP_ID, REFRESH_BUTTON_IN_ELEMENT_ROUTING_TAB_ID);
        waitForPageToLoad();
    }

    public void openRouting1stLevelTab() {
        expandDetailsPanel();
        selectTabFromBottomPanel(FIRST_LEVEL_ROUTING_TAB_LABEL);
        waitForPageToLoad();
    }

    public void openValidationResultsTab() {
        expandDetailsPanel();
        selectTabFromBottomPanel(VALIDATION_RESULT_ID);
        waitForPageToLoad();
    }

    public void openRoutingElementsTab() {
        expandDetailsPanel();
        selectTabFromBottomPanel(ELEMENT_ROUTING_TAB_LABEL);
        waitForPageToLoad();
    }

    public void openTerminationsTab() {
        expandDetailsPanel();
        selectTabFromBottomPanel(TERMINATIONS_TAB_LABEL);
        waitForPageToLoad();
    }

    @Step("Set Configuration = {configurationName}")
    public void applyConfigurationForAttributesPanel(String configurationName) {
        waitForPageToLoad();
        getPropertyPanel(ATTRIBUTES_PANEL_ID).callAction(GEAR_OBJECT_GROUP_ID, CHOOSE_CONFIGURATION_ID);
        getChooseConfigurationWizard().chooseConfiguration(configurationName).apply();
    }

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
