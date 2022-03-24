package com.oss.pages.acd;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.layout.Card;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.iaa.widgets.components.Table2DComponent;
import com.oss.framework.iaa.widgets.table.Table2DWidget;
import com.oss.framework.iaa.widgets.timeperiodchooser.TimePeriodChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class BaseACDPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BaseACDPage.class);

    private static final String ADD_PREDEFINED_FILTER_BUTTON = "contextButton-0";
    private static final String SWITCHER_ID = "switcherId";
    private static final String CONFIRM_DELETE_BUTTON_ID = "ConfirmationBox_prompt_action_button";

    public BaseACDPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Maximize window")
    public void maximizeWindow(String windowId) {
        Card.createCard(driver, wait, windowId).maximizeCard();
        log.info("Maximizing window");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Minimize window")
    public void minimizeWindow(String windowId) {
        Card.createCard(driver, wait, windowId).minimizeCard();
        log.info("Minimizing window");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I check if card is maximized")
    public boolean checkCardMaximize(String windowId) {
        DelayUtils.sleep(1000);
        Card card = Card.createCard(driver, wait, windowId);
        log.info("Checking if card {} is maximized", windowId);
        return card.isCardMaximized();
    }

    @Step("Add predefined filter")
    public void clickAddPredefinedFilter() {
        Button button = Button.createById(driver, ADD_PREDEFINED_FILTER_BUTTON);
        button.click();
        log.info("Clicking Add predefined filter button");
    }

    @Step("Set value in ComboBox")
    public void setValueInComboBox(String componentId, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard.createByComponentId(driver, wait, componentId).setComponentValue(componentId, value, Input.ComponentType.COMBOBOX);
        log.info("Setting value of {} attribute", componentId, " as {}", value);
    }

    @Step("I insert {value} to multiComboBox component with id {componentId}")
    public void chooseValueInMultiComboBox(String componentId, String value) {
        Wizard.createByComponentId(driver, wait, componentId).setComponentValue(componentId, value, Input.ComponentType.MULTI_COMBOBOX);
    }

    @Step("I save Predefined Filter")
    public void savePredefinedFilter(String componentId) {
        Wizard.createByComponentId(driver, wait, componentId).clickAccept();
        log.info("I save predefined filter by clicking 'Accept' button");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I delete Predefined Filter")
    public void deletePredefinedFilter() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep();

        ButtonContainer deleteButton = ButtonContainer.create(driver, wait);
        deleteButton.callActionByLabel("DELETE");

        DelayUtils.waitForPageToLoad(driver, wait);

        ConfirmationBox.create(driver, wait).clickButtonById(CONFIRM_DELETE_BUTTON_ID);

        log.info("I deleted predefined filer");
    }

    @Step("Set value in time period chooser")
    public void setValueInTimePeriodChooser(String widgetId, int days, int hours, int minutes) {
        TimePeriodChooser timePeriod = TimePeriodChooser.create(driver, wait, widgetId);
        timePeriod.chooseOption(TimePeriodChooser.TimePeriodChooserOption.LAST);
        log.info("Setting value in the time period chooser");
        timePeriod.setLastPeriod(days, hours, minutes);
        DelayUtils.sleep();
    }

    @Step("Set value in multiComboBox")
    public void setValueInMultiComboBox(String attributeName, String inputValue) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ComponentFactory.create(attributeName, Input.ComponentType.MULTI_COMBOBOX, driver, wait)
                .setSingleStringValue(inputValue);
        log.info("Setting value of {} attribute as {}", inputValue, attributeName);
    }

    @Step("Turn On Include Issues without Roots switcher")
    public void turnOnSwitcher() {
        ComponentFactory.create(SWITCHER_ID, Input.ComponentType.SWITCHER, driver, wait).click();
        log.info("Turning on Include Issues without Roots switcher");
    }

    @Step("Refresh issues table")
    public void refreshIssuesTable(String issuesTableRefreshButtonId) {
        Button button = Button.createById(driver, issuesTableRefreshButtonId);
        button.click();
        log.info("Clicking refresh issues table button");
    }

    @Step("Clear time period chooser")
    public void clearTimePeriod(String widgetId) {
        TimePeriodChooser timePeriod = TimePeriodChooser.create(driver, wait, widgetId);
        timePeriod.clickClearValue();
        log.info("Clearing time period chooser");
    }

    @Step("Select chosen cell in {tableName} Table")
    public void selectCellInTable(String tableName, String leftHeader, String columnName) {
        Table2DWidget table2DWidget = Table2DWidget.create(driver, wait, tableName);
        table2DWidget.selectCell(leftHeader, columnName);
        log.info("Cell from row '{}' and column '{}' in '{}' Table is selected", leftHeader, columnName, tableName);
    }

    @Step("Check if counted rows in Issue Table match number in selected cell from {tableName} Table")
    public boolean checkIfFilteringBySelectingCellWorks(String tableName, String leftHeader, String columnName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        String rowsNumber = String.valueOf(OldTable.createById(driver, wait, "DetectedIssuesTableId").getTotalCount());
        String cellValue = Table2DWidget.create(driver, wait, tableName).getCellValue(leftHeader, columnName);
        log.info("Counted rows in Issue Table: {} -  Displayed value in '{}' Table: {}", rowsNumber, tableName, cellValue);
        return (cellValue.equals(rowsNumber));
    }

    @Step("Check if counted rows in Issue Table match number in selected row from {tableName} Table")
    public boolean checkIfFilteringBySelectingRowOrColumnWorks(String tableName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        int rowsNumber = OldTable.createById(driver, wait, "DetectedIssuesTableId").getTotalCount();
        int valuesOfSelectedCells = getValuesOfSelectedCells(tableName);
        log.info("Counted rows in Issue Table: {} -  Displayed value in selected cells from '{}' Table: {}", rowsNumber, tableName, valuesOfSelectedCells);
        return (valuesOfSelectedCells == rowsNumber);
    }

    @Step("Clear filters in {tableName} Table")
    public void clearFiltersInTable(String tableName) {
        Table2DWidget table2DWidget = Table2DWidget.create(driver, wait, tableName);
        table2DWidget.clearFilter();
        log.info("Filters in '{}' Table are cleared", tableName);
    }

    @Step("Select chosen column in {tableName} Table")
    public void selectColumnInTable(String tableName, String columnName) {
        Table2DWidget table2DWidget = Table2DWidget.create(driver, wait, tableName);
        table2DWidget.selectColumn(columnName);
        log.info("Column '{}' in '{}' Table is selected", columnName, tableName);
    }

    @Step("Select chosen row in {tableName} Table")
    public void selectRowInTable(String tableName, String rowName) {
        Table2DWidget table2DWidget = Table2DWidget.create(driver, wait, tableName);
        table2DWidget.selectRow(rowName);
        log.info("Row '{}' in '{}' Table is selected", rowName, tableName);
    }

    @Step("Sum values of selected cells")
    public int sumValuesOfSelectedCells(String tableName) {
        int selectedCells = getValuesOfSelectedCells(tableName);
        log.info("Sum of selected cells values: {}", selectedCells);
        return selectedCells;
    }

    public int getValuesOfSelectedCells(String tableName) {
        Table2DWidget table2DWidget = Table2DWidget.create(driver, wait, tableName);
        List<Table2DComponent.Cell2D> selectedCells = table2DWidget.getSelectedCells();
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Summing values of selected cells");
        return selectedCells.stream().map(c -> Integer.parseInt(c.getValue())).collect(Collectors.summingInt(Integer::intValue));
    }
}
