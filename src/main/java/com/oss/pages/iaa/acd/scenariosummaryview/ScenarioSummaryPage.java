package com.oss.pages.iaa.acd.scenariosummaryview;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.iaa.widgets.components.Table2DComponent;
import com.oss.framework.iaa.widgets.table.Table2DWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.iaa.acd.BaseACDPage;

import io.qameta.allure.Step;

public class ScenarioSummaryPage extends BaseACDPage {

    private static final Logger log = LoggerFactory.getLogger(ScenarioSummaryPage.class);

    private static final String CONFIRM_DELETE_BUTTON_ID = "ConfirmationBox_prompt_action_button";
    private static final String DETECTED_ISSUES_WINDOW_ID = "DetectedIssuesWindowId";
    private static final String CREATION_TYPE_ID = "creation_type";
    private static final String CREATE_TIME_ID = "create_time";
    private static final String ATTRIBUTE_ID = "id";
    private static final String SWITCHER_ID = "switcherValue";

    public ScenarioSummaryPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open {suffixURL} Scenario Summary View")
    public static ScenarioSummaryPage goToPage(WebDriver driver, String basicURL, String chosenView) {
        WebDriverWait wait = new WebDriverWait(driver,  Duration.ofSeconds(150));
        String pageUrl = String.format("%s/#/view/acd/%s", basicURL, chosenView);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);
        return new ScenarioSummaryPage(driver, wait);
    }

    @Step("Check if there is data in issues table")
    public Boolean isDataInIssuesTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Checking if there is data in issues table");
        return !getIssuesTable().hasNoData();
    }

    @Step("Set value of Issue Id multiSearch")
    public void setValueOfIssueIdSearch() {

        if (isDataInIssuesTable()) {
            String firstIdInTable = getIssuesTable().getCellValue(0, "Issue Id");
            log.info("Setting value of Issue Id");
            ComponentFactory.create("id", driver, wait).setSingleStringValue(firstIdInTable);
            DelayUtils.sleep();
        } else {
            log.info("Table doesn't have data for chosen filters. Issue ID cannot be set");
        }
    }

    public OldTable getIssuesTable() {
        return OldTable.createById(driver, wait, DETECTED_ISSUES_WINDOW_ID);
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

    public void clearIssueTableFilters() {
        clearAttributeValue(CREATION_TYPE_ID);
        clearAttributeValue(ATTRIBUTE_ID);
        clearTimePeriod(CREATE_TIME_ID);
    }

    public void setAdvancedSearchAttributes(String creationTypeValue, int days, int hours, int minutes) {
        setAttributeValue(CREATION_TYPE_ID, creationTypeValue);
        setValueInTimePeriodChooser(CREATE_TIME_ID, days, hours, minutes);
        setValueOfIssueIdSearch();
    }

    public void checkIssuesTableWithFilter(String creationTypeValue, int days, int hours, int minutes) {

        if (isDataInIssuesTable()) {
            log.info("Table contains data for issues with roots");
        } else {
            log.info("Table doesn't have data for issues with roots");
            turnOnSwitcher(SWITCHER_ID);
            if (!isDataInIssuesTable()) {
                throw new NoSuchElementException("Table doesn't have data for issues without roots");
            } else {
                log.info("Table contains data for issues without roots");
            }
        }
        setAdvancedSearchAttributes(creationTypeValue, days, hours, minutes);
        if (!isDataInIssuesTable()) {
            throw new NoSuchElementException("Table doesn't have data for provided filters");
        }
        DelayUtils.sleep();
        clearIssueTableFilters();
    }
}