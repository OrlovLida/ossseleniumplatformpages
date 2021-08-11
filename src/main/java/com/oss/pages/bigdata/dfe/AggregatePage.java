package com.oss.pages.bigdata.dfe;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class AggregatePage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(AggregatePage.class);

    private static final String TABLE_ID = "aggregates-tableAppId";

    private final String ADD_NEW_AGGREGATE_LABEL = "Add New Aggregate";
    private final String EDIT_AGGREGATE_LABEL = "Edit Aggregate";
    private final String DELETE_AGGREGATE_LABEL = "Delete Aggregate";
    private final String SEARCH_INPUT_ID = "aggregates-tableSearchAppId";

    private final String NAME_COLUMN_LABEL = "Name";
    private final String DELETE_LABEL = "Delete";
    private final String EXECUTION_HISTORY_TAB = "Execution History";
    private final String TABLE_TAB_ID = "executionHistoryId";
    private final String COLUMN_REQUEST_GENERATION_TIME_LABEL = "Request Generation Time";
    private final String COLUMN_STATUS_LABEL = "Status";
    private final String REFRESH_LABEL = "Refresh";

    private AggregatePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open Aggregates View")
    public static AggregatePage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "aggregates");
        return new AggregatePage(driver, wait);
    }

    @Step("I click add new Aggregate")
    public void clickAddNewAggregate(){
        clickContextActionAdd();
    }

    @Step("I click edit Aggregate")
    public void clickEditAggregate(){
        clickContextActionEdit();
    }

    @Step("I click delete Aggregate")
    public void clickDeleteAggregate(){
        clickContextActionDelete();
    }

    @Step("I check if Aggregate: {aggregateName} exists into the table")
    public Boolean aggregateExistsIntoTable(String aggregateName) {
        DelayUtils.sleep(2000);
        searchFeed(aggregateName);
        DelayUtils.waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(NAME_COLUMN_LABEL);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, aggregateName);
        return numberOfRowsInTable == 1;
    }

    @Step("I select found Aggregate")
    public void selectFoundAggregate() {
        getTable(driver, wait).selectRow(0);
    }

    @Step("I confirm the removal of Aggregate")
    public void confirmDelete() {
        confirmDelete(DELETE_LABEL);
    }

    @Step("I click Execution History Tab")
    public void selectExecutionHistoryTab() {
        selectTab(EXECUTION_HISTORY_TAB);
    }

    @Step("I click Refresh Table Tab")
    public void clickRefreshInTabTable() {
        clickRefreshTabTable(REFRESH_LABEL);
    }

    @Step("I check if IfRuns are not empty")
    public Boolean ifRunsNotEmpty() {
        boolean ifRunsExists = OldTable
                .createByComponentId(driver, wait, TABLE_TAB_ID)
                .getNumberOfRowsInTable(COLUMN_REQUEST_GENERATION_TIME_LABEL) >= 1;
        log.info("In if Runs exist at least one row: {}", ifRunsExists);

        return ifRunsExists;
    }

    @Step("I check Last Request Generation Time")
    public LocalDateTime lastIfRunTime() {
        return lastLogTime(TABLE_TAB_ID, COLUMN_REQUEST_GENERATION_TIME_LABEL);
    }

    @Step("I check if Last Request Generation Time is fresh - up to 60 min old")
    public boolean IsIfRunsFresh() {
        return isLastLogTimeFresh(lastIfRunTime());
    }

    @Step("I check Status of Aggregate from Execution History tab")
    public String checkStatus() {
        return checkLogStatus(TABLE_TAB_ID, COLUMN_STATUS_LABEL);
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
}
