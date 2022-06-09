package com.oss.pages.bigdata.dfe;

import java.time.LocalDateTime;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Step;

public class EtlDataCollectionsPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(EtlDataCollectionsPage.class);

    private static final String TABLE_ID = "data-collection-listAppId";
    private static final String ADD_ETL_PROCESS_LABEL = "Add New ETL Process";
    private static final String EDIT_ETL_PROCESS_LABEL = "Edit ETL Process";
    private static final String DELETE_ETL_PROCESS_LABEL = "Delete ETL Process";
    private static final String SEARCH_INPUT_ID = "data-collection-listSearchAppId";
    private static final String NAME_COLUMN_LABEL = "Name";
    private static final String DELETE_LABEL = "Delete";
    private static final String TABS_WIDGET_ID = "card-content_tabsId";
    private static final String EXECUTION_HISTORY_TAB_LABEL = "Execution History";
    private static final String TABLE_TAB_ID = "logsId";
    private static final String COLUMN_REQUEST_GENERATION_TIME_LABEL = "Request Generation Time";
    private static final String REFRESH_LABEL = "Refresh";
    private static final String COLUMN_STATUS_LABEL = "Status";
    private static final String TAB_WIDGET_ID = "card-content_tabsId";
    private static final String FORMAT_TAB = "Format";
    private static final String DETAILS_TAB = "Details";
    private static final String PROPERTY_PANEL_ID = "detailsId";
    private static final String NAME_PROPERTY = "Name";
    private static final String MEASURES_TAB = "Measures";
    private static final String FORMAT_TABLE_ID = "formatTableId";
    private static final String MEASURES_TABLE_ID = "measuresTable";

    private EtlDataCollectionsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open ETL Data Collections View")
    public static EtlDataCollectionsPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "etl-data-collection");
        return new EtlDataCollectionsPage(driver, wait);
    }

    @Step("I click add new ETL Process")
    public void clickAddNewEtlProcess() {
        clickContextActionAdd();
    }

    @Step("I click edit ETL Process")
    public void clickEditEtlProcess() {
        clickContextActionEdit();
    }

    @Step("I click delete ETL Process")
    public void clickDeleteEtlProcess() {
        clickContextActionDelete();
    }

    @Step("I check if ETL Process: {feedName} exists into the table")
    public Boolean etlProcessExistsIntoTable(String feedName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        searchFeed(feedName);
        DelayUtils.waitForPageToLoad(driver, wait);

        int numberOfRowsInTable = getNumberOfRowsInTable(NAME_COLUMN_LABEL);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, feedName);

        return numberOfRowsInTable == 1;
    }

    @Step("I select found ETL Process")
    public void selectFoundEtlProcess() {
        getTable(driver, wait).selectRow(0);
    }

    @Step("I confirm the removal of ETL Process")
    public void confirmDelete() {
        confirmDelete(DELETE_LABEL);
    }

    @Step("I select Execution History Tab")
    public void selectExecutionHistoryTab() {
        selectTab(TABS_WIDGET_ID, EXECUTION_HISTORY_TAB_LABEL);
    }

    @Step("I check Last Request Generation Time")
    public LocalDateTime lastIfRunTime() {
        return lastLogTime(TABLE_TAB_ID, COLUMN_REQUEST_GENERATION_TIME_LABEL);
    }

    @Step("I click Refresh Table Tab")
    public void clickRefreshInTabTable() {
        clickRefreshTabTable(TABS_WIDGET_ID, REFRESH_LABEL);
    }

    @Step("I check if Last Request Generation Time is fresh - up to 60 min old")
    public boolean isIfRunsFresh() {
        return isLastLogTimeFresh(lastIfRunTime());
    }

    @Step("I check Status of ETL from Execution History tab")
    public String checkStatus() {
        String statusOfEtl = checkLogStatus(TABLE_TAB_ID, COLUMN_STATUS_LABEL);
        log.info("Status of last ETL log in Execution History is {}", statusOfEtl);

        return statusOfEtl;
    }

    @Step("Select Format Tab")
    public void selectFormatTab() {
        selectTab(TAB_WIDGET_ID, FORMAT_TAB);
    }

    @Step("Select Measures Tab")
    public void selectMeasuresTab() {
        selectTab(TAB_WIDGET_ID, MEASURES_TAB);
    }

    @Step("Select Details Tab")
    public void selectDetailsTab() {
        selectTab(TAB_WIDGET_ID, DETAILS_TAB);
    }

    @Step("Check name value in details tab")
    public String checkNameInPropertyPanel() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return checkValueInPropertyPanel(PROPERTY_PANEL_ID,
                NAME_PROPERTY);
    }

    @Step("Check if Format Tab Table is empty")
    public boolean isFormatTabTableEmpty()  { return isTabTableEmpty(FORMAT_TABLE_ID); }

    @Step("Check if Measures Tab Table is empty")
    public boolean isMeasuresTabTableEmpty()  { return isTabTableEmpty(MEASURES_TABLE_ID); }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_ETL_PROCESS_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_ETL_PROCESS_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_ETL_PROCESS_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}

