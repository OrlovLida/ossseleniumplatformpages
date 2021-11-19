package com.oss.pages.acd;

import com.oss.framework.components.common.TimePeriodChooser;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.view.Card;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.serviceDeskAdvancedSearch.ServiceDeskAdvancedSearch;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;

public class BaseACDPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BaseACDPage.class);

    private final String ADD_PREDEFINED_FILTER_BUTTON = "contextButton-0";  //"contextButton-0";
    private final String VISUALIZATION_TYPE_ID = "widgetType-input";
    private final String ATTRIBUTE_ID = "attribute1Id-input";
    private final String SWITCHER_ID = "switcherId";
    private final String DETECTED_ISSUES_TABLE_WINDOW_ID = "IssueTableWindowId";
    private final String DETECTED_ISSUES_TABLE_ID = "IssueTableWindowId";

    private final Wizard visualizationTypeWizard;
    private final Wizard attributeWizard;
    private final OldTable table;
    private final ServiceDeskAdvancedSearch advancedSearch;

    public BaseACDPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        visualizationTypeWizard = Wizard.createWizard(driver, wait);
        attributeWizard = Wizard.createWizard(driver, wait);
        table = OldTable.createByComponentDataAttributeName(driver, wait, DETECTED_ISSUES_TABLE_ID);
        advancedSearch = ServiceDeskAdvancedSearch.create(driver, wait, DETECTED_ISSUES_TABLE_WINDOW_ID);
    }

    @Step("Maximize window")
    public void maximizeWindow(String windowId) {
        Card.createCard(driver, wait, windowId).maximizeCard();
        log.info("Maximizing window");
        log.info("Waiting for Predefined Filters presence");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Minimize window")
    public void minimizeWindow(String windowId) {
        Card.createCard(driver, wait, windowId).minimizeCard();
        log.info("Minimizing window");
    }

    @Step("Add predefined filter")
    public void clickAddPredefinedFilter() {
        Button button = Button.createByXpath(ADD_PREDEFINED_FILTER_BUTTON, "li", CSSUtils.DATA_WIDGET_ID, driver);
        button.click();
        log.info("Clicking Add predefined filter button");
    }

    @Step("Choose Predefined Filter type")
    public void chooseVisualizationType(String visualizationType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        visualizationTypeWizard.setComponentValue(VISUALIZATION_TYPE_ID, visualizationType, COMBOBOX);
        log.debug("Setting visualization type: {}", visualizationType);
    }

    @Step("Choose attribute for predefined filter")
    public void chooseAttribute(String attributeName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        attributeWizard.setComponentValue(ATTRIBUTE_ID, attributeName, COMBOBOX);
        log.debug("Setting attribute for predefined filter: {}", attributeName);
    }

    @Step("I insert {value} to multi combo box component with id {componentId}")
    public void insertAttributeValueToMultiComboBoxComponent(String text, String componentId) {
        insertValueToComponent(componentId, text, Input.ComponentType.MULTI_COMBOBOX);
    }

    @Step("I click Accept button")
    public void savePredefinedFilter() {
        attributeWizard.clickAcceptOldWizard();
        log.info("I save predefined filter by clicking 'Accept'");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void insertValueToComponent(String componentId, String text, Input.ComponentType componentType) {
        getWizard().setComponentValue(componentId, text, componentType);
    }

    private Wizard getWizard() {
        return Wizard.createWizard(driver, wait);
    }

    @Step("I delete Predefined Filter")
    public void deletePredefinedFilter() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep();

        ButtonContainer deleteButton = ButtonContainer.create(driver, wait);
        deleteButton.callActionByLabel("DELETE");

        DelayUtils.waitForPageToLoad(driver, wait);

        Button deleteButton2 = Button.create(driver, "Delete");
        deleteButton2.click();

        log.info("I deleted predefined filer");
    }

    @Step("Set value of Issue Id multiSearch")
    public void setValueOfIssueIdSearch() {
        Input issueIdSearch = advancedSearch.getComponent("id", Input.ComponentType.MULTI_SEARCH_FIELD);
        String firstIdInTable = table.getCellValue(0, "Issue Id");
        log.info("Setting value of Issue Id");
        issueIdSearch.setValue(Data.createSingleData(firstIdInTable));
        DelayUtils.sleep();
    }

    @Step("Set value in multiComboBox")
    public void setValueInMultiComboBox(String attributeName, String inputValue) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Input multiComboBox = advancedSearch.getComponent(attributeName, Input.ComponentType.MULTI_COMBOBOX);
        multiComboBox.setValue(Data.createSingleData(inputValue));
        log.info("Setting value of {} attribute", inputValue, " as {}", attributeName);
    }

    @Step("Set value in time period chooser")
    public void setValueInTimePeriodChooser(String widgetId, int days, int hours, int minutes) {
        TimePeriodChooser timePeriod = TimePeriodChooser.create(driver, wait, widgetId);
        timePeriod.chooseOption(TimePeriodChooser.TimePeriodChooserOption.LAST);
        log.info("Setting value in the time period chooser");
        timePeriod.setLastPeriod(days, hours, minutes);
        DelayUtils.sleep();
    }

    @Step("Check if data in issues table is empty")
    public Boolean checkDataInIssuesTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Checking if issues table is empty");
        return table.hasNoData();
    }

    @Step("Turn On Include Issues without Roots switcher")
    public void turnOnSwitcher() {
        ComponentFactory.create(SWITCHER_ID, Input.ComponentType.SWITCHER, driver, wait).click();
        log.info("Turning on Include Issues without Roots");
    }

    @Step("Refresh issues table")
    public void refreshIssuesTable(String issuesTableRefreshButtonId) {
        Button button = Button.createByXpath(issuesTableRefreshButtonId, "li", CSSUtils.DATA_WIDGET_ID, driver);
        button.click();
        log.info("Clicking refresh issues table button");
    }

    @Step("Clear multiComboBox")
    public void clearMultiComboBox(String multiComboBoxId) {
        Input issueTypeComboBox = advancedSearch.getComponent(multiComboBoxId, Input.ComponentType.MULTI_COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
        issueTypeComboBox.clear();
        log.info("Clearing multicombobox");
    }

    @Step("Clear multiSearch")
    public void clearMultiSearch(String multiSearchId) {
        Input issueIdSearch = advancedSearch.getComponent(multiSearchId, Input.ComponentType.MULTI_SEARCH_FIELD);
        issueIdSearch.clear();
        log.info("Clearing multisearch");
    }

    @Step("Clear time period chooser")
    public void clearTimePeriod(String widgetId) {
        TimePeriodChooser timePeriod = TimePeriodChooser.create(driver, wait, widgetId);
        timePeriod.clickClearValue();
        log.info("Clearing time period chooser");
    }

}
