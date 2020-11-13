package com.oss.pages.transport;

import java.lang.reflect.Constructor;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.prompts.ConfirmationBoxInterface;
import com.oss.framework.sidemenu.SideMenu;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.dockedPanel.DockedPanel;
import com.oss.framework.widgets.dockedPanel.DockedPanelInterface;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.framework.widgets.tabswidget.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.pages.physical.DeviceWizardPage;
import com.oss.pages.transport.trail.RoutingWizardPage;
import com.oss.pages.transport.trail.TerminationWizardPage;
import com.oss.pages.transport.trail.TrailWizardPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class NetworkViewPage extends BasePage {

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

    public static NetworkViewPage goToNetworkViewPageLive(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/transport/trail/network?" + "perspective=LIVE", basicURL));
        return new NetworkViewPage(driver);
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
        useContextAction("CREATE", "Create Trail");
        selectTrailType(trailType);
        clickButton("Accept");
        waitForPageToLoad();
    }

    @Step("Select trail type")
    public void selectTrailType(String trailType) {
        Input input = physicalDeviceWizard.getComponent("trailTypeCombobox", COMBOBOX);
        input.setSingleStringValue(trailType);
    }

    @Step("Open Trail update wizard")
    public <T extends TrailWizardPage> T openUpdateTrailWizard(Class<T> trailWizardPageClass) {
        useContextAction("EDIT", "Attributes and terminations");
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
        useContextAction("CREATE", "Create Device");
        waitForPageToLoad();
        return new DeviceWizardPage(driver);
    }

    @Step("Delete selected trails by context action")
    public void deleteSelectedTrails() {
        useContextAction("EDIT", "Delete Trail");
        waitForPageToLoad();
        clickButton("Proceed");
        waitForPageToLoad();
    }

    @Step("Delete selected elements by context action")
    public void deleteSelectedElement() {
        useContextAction("EDIT", "Delete Element");
        waitForPageToLoad();
        clickButton("Yes");
        waitForPageToLoad();
    }

    @Step("Click Start editing trail button")
    public void startEditingSelectedTrail() {
        Button button = Button.create(driver, "Start editing trail");
        button.click();
    }

    @Step("Add selected objects to Routing")
    public RoutingWizardPage addSelectedObjectsToRouting() {
        useContextAction("add_to_group", "Routing");
        waitForPageToLoad();
        return new RoutingWizardPage(driver);
    }

    @Step("Add selected objects to Termination")
    public TerminationWizardPage addSelectedObjectsToTermination() {
        useContextAction("add_to_group", "Termination");
        waitForPageToLoad();
        return new TerminationWizardPage(driver);
    }

    @Step("Use context action")
    public void useContextAction(String group, String action) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, "dockedPanel-left");
        table.callAction(group, action);
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

    @Step("Select object in View content")
    public void selectObject(String partialName) {
        //TODO: OSSTPT-30835 - select object only when it's not selected
        expandViewContentPanel();
        clickOnObjectInViewContentByPartialName(partialName);
    }

    @Step("Unselect object in View content")
    public void unselectObject(String partialName) {
        //TODO: OSSTPT-30835 - unselect object only when it's selected
        expandViewContentPanel();
        clickOnObjectInViewContentByPartialName(partialName);
    }

    private void clickOnObjectInViewContentByPartialName(String partialName) {
        String xpath = getXPathForObjectInViewContentByPartialName(partialName);
        driver.findElement(By.xpath(xpath)).click();
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
        selectObjectInBottomPanel("routing-table-app", column, value);
        waitForPageToLoad();
    }

    private void selectObjectInBottomPanel(String attribute, String column, String value) {
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, attribute);
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
        selectTabFromBottomPanel("Routing");
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
        Button button = Button.createBySelectorAndId(driver, "a", "Delete termination");
        button.click();
        waitForPageToLoad();
        clickButton("Delete");
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
        Button button = Button.createBySelectorAndId(driver, "a", "Remove from routing");
        button.click();
        waitForPageToLoad();
        clickButton("Delete");
        waitForPageToLoad();
    }

    private void refreshRoutingElements() {
        Button button = Button.createBySelectorAndId(driver, "a", "Refresh");
        button.click();
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

    private void waitForPageToLoad() {
        DelayUtils.waitForPageToLoad(driver, wait);
    }

}
