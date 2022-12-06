package com.oss.pages.iaa.bigdata.dfe;

import java.time.Duration;
import java.time.LocalDateTime;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class AggregatePage extends BaseDfePage {

    private static final String TABLE_ID = "aggregates-tableAppId";
    private static final String ADD_NEW_AGGREGATE_LABEL = "Add New Aggregate";
    private static final String EDIT_AGGREGATE_LABEL = "Edit Aggregate";
    private static final String DELETE_AGGREGATE_LABEL = "Delete Aggregate";
    private static final String SEARCH_INPUT_ID = "input_aggregates-tableSearchAppId";
    private static final String NAME_COLUMN_LABEL = "Name";
    private static final String DELETE_LABEL = "Delete";
    private static final String TABS_WIDGET_ID = "card-content_tabsId";
    private static final String EXECUTION_HISTORY_TAB = "Execution History";
    private static final String TABLE_TAB_ID = "executionHistoryId";
    private static final String COLUMN_REQUEST_GENERATION_TIME_LABEL = "Request Generation Time";
    private static final String COLUMN_STATUS_LABEL = "Status";
    private static final String REFRESH_LABEL = "Refresh";
    private static final String TAB_WIDGET_ID = "card-content_tabsId";
    private static final String DETAILS_TAB = "Details";
    private static final String CONFIGURATIONS_TAB = "Configurations";
    private static final String PROPERTY_PANEL_ID = "detailsId";
    private static final String NAME_PROPERTY = "Name";
    private static final String CONFIGURATIONS_TABLE_TAB = "aggregateConfigurationsId";
    private static final String MEASURES_TAB = "Measures";
    private static final String MEASURES_TABLE_ID = "aggregateMeasuresId";
    private static final String USED_IN_TAB = "Used In";
    private static final String USED_IN_TABLE_TAB = "usedInId";
    private static final String COLUMN_REBUILD_STATUS_LABEL = "Rebuild Status";
    private static final String CONFIGURATION_TABLE_TAB_ID = "aggregateConfigurationsId";

    private AggregatePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open Aggregates View")
    public static AggregatePage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));

        openDfePage(driver, basicURL, wait, "aggregates");
        return new AggregatePage(driver, wait);
    }

    @Step("I click add new Aggregate")
    public void clickAddNewAggregate() {
        clickContextActionAdd();
    }

    @Step("I click edit Aggregate")
    public void clickEditAggregate() {
        clickContextActionEdit();
    }

    @Step("I click delete Aggregate")
    public void clickDeleteAggregate() {
        clickContextActionDelete();
    }

    @Step("I check if Aggregate: {aggregateName} exists into the table")
    public Boolean aggregateExistsIntoTable(String aggregateName) {
        DelayUtils.sleep(2000);
        searchFeed(aggregateName);
        DelayUtils.waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(NAME_COLUMN_LABEL);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, aggregateName);
        return numberOfRowsInTable >= 1;
    }

    @Step("I select found Aggregate")
    public void selectFirstAggregateInTable() {
        getTable().selectRow(0);
    }

    @Step("I confirm the removal of Aggregate")
    public void confirmDelete() {
        confirmDelete(DELETE_LABEL);
    }

    @Step("I click Execution History Tab")
    public void selectExecutionHistoryTab() {
        selectTab(TABS_WIDGET_ID, EXECUTION_HISTORY_TAB);
    }

    @Step("I click Refresh Table Tab")
    public void clickRefreshInTabTable() {
        clickRefreshTabTable(TABS_WIDGET_ID, REFRESH_LABEL);
    }

    @Step("I check if IfRuns are not empty")
    public boolean ifRunsNotEmpty() {
        boolean ifRunsExists = OldTable
                .createById(driver, wait, TABLE_TAB_ID)
                .countRows(COLUMN_REQUEST_GENERATION_TIME_LABEL) >= 1;
        log.info("In if Runs exist at least one row: {}", ifRunsExists);

        return ifRunsExists;
    }

    @Step("I check Last Request Generation Time")
    public LocalDateTime lastIfRunTime() {
        return lastLogTime(TABLE_TAB_ID, COLUMN_REQUEST_GENERATION_TIME_LABEL);
    }

    @Step("I check if Last Request Generation Time is fresh - up to 60 min old")
    public boolean isIfRunsFresh() {
        return isLastLogTimeFresh(lastIfRunTime());
    }

    @Step("I check Status of Aggregate from Execution History tab")
    public String checkStatus() {
        String statusOfAggregate = checkLogStatus(TABLE_TAB_ID, COLUMN_STATUS_LABEL);
        log.info("Status of last aggregate log in Execution History is {}", statusOfAggregate);

        return statusOfAggregate;
    }

    @Step("I check if Rebuild Status is Valid in each rows")
    public boolean isRebuildStatusValid() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I check if Rebuild Status is Valid");
        for (int row = 0; row < getNumberOfRowInConfigurationsTabTable(); row++) {
            if (!checkRebuildStatus(row).equals("Valid")) {
                return false;
            }
        }
        return true;
    }

    @Step("Select Details tab")
    public void selectDetailsTab() {
        selectTab(TAB_WIDGET_ID, DETAILS_TAB);
    }

    @Step("Select Configurations tab")
    public void selectConfigurationsTab() {
        selectTab(TAB_WIDGET_ID, CONFIGURATIONS_TAB);
    }

    @Step("Select Measures tab")
    public void selectMeasuresTab() {
        selectTab(TAB_WIDGET_ID, MEASURES_TAB);
    }

    @Step("Select Used In tab")
    public void selectUsedTab() {
        selectTab(TAB_WIDGET_ID, USED_IN_TAB);
    }

    @Step("Check name in details tab")
    public String getNameFromPropertyPanel() {
        waitForPageToLoad(driver, wait);
        return getValueFromPropertyPanel(PROPERTY_PANEL_ID, NAME_PROPERTY);
    }

    @Step("Check if Configurations Tab Table is empty")
    public boolean isConfigurationsTabTableEmpty() {
        return isTabTableEmpty(CONFIGURATIONS_TABLE_TAB);
    }

    @Step("Check if Measures Tab Table is empty")
    public boolean isMeasuresTabTableEmpty() {
        return isTabTableEmpty(MEASURES_TABLE_ID);
    }

    @Step("Check if Used In Table is empty")
    public boolean isUsedInTabTableEmpty() {
        return isTabTableEmpty(USED_IN_TABLE_TAB);
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_AGGREGATE_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_AGGREGATE_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_AGGREGATE_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }

    @Step("Check number of rows in Configurations Tab Table")
    private int getNumberOfRowInConfigurationsTabTable() {
        return getConfigurationsTabTable().countRows(COLUMN_REBUILD_STATUS_LABEL);
    }

    private OldTable getConfigurationsTabTable() {
        return OldTable.createById(driver, wait, CONFIGURATION_TABLE_TAB_ID);
    }

    @Step("Check Rebuild Status in selected row")
    private String checkRebuildStatus(int row) {
        return getConfigurationsTabTable().getCellValue(row, COLUMN_REBUILD_STATUS_LABEL);
    }
}
