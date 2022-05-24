package com.oss.pages.bigdata.dfe.datasource;

import java.time.LocalDateTime;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.iaa.widgets.timeperiodchooser.TimePeriodChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.bigdata.dfe.BaseDfePage;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class DataSourcePage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(DataSourcePage.class);

    private static final String TABLE_ID = "datasource/datasource-listAppId";
    private static final String SEARCH_INPUT_ID = "datasource/datasource-listSearchAppId";
    private static final String ADD_NEW_DS_LABEL = "ADD";
    private static final String CREATE_DS_QUERY = "OPEN_MODAL_QUERY";
    private static final String CREATE_DS_CSV = "OPEN_MODAL_CSV";
    private static final String CREATE_DS_KAFKA = "OPEN_MODAL_KAFKA";
    private static final String DATA_SOURCE_NAME_COLUMN = "Name";
    private static final String EDIT_DS_LABEL = "Edit";
    private static final String DELETE_DS_LABEL = "Delete";
    private static final String CONFIRM_DELETE_LABEL = "Delete";
    private static final String LOGS_TAB = "Logs";
    private static final String PROCESSED_FILES_TAB = "Processed Files";
    private static final String REFRESH_LABEL = "Refresh";
    private static final String SHOW_FILE_LABEL = "Show file";
    private static final String TIME_PERIOD_ID = "input_timePeriod";
    private static final String SEVERITY_COMBOBOX_ID = "severityLogs-input";
    private static final String LOG_TAB_TABLE_ID = "LogsId";
    private static final String COLUMN_TIME_LABEL = "Time";
    private static final String COLUMN_SEVERITY_LABEL = "Severity";
    private static final String PROCESSED_FILES_TABLE_ID = "stats-tableAppId";
    private static final String DOWNLOAD_FILE_LABEL = "Download file";
    private static final String SHOW_FILE_TABLE_ID = "TableId";
    private static final String TAB_WIDGET_ID = "card-content_TabsId";

    public DataSourcePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open Data Source View")
    public static DataSourcePage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "datasource");
        return new DataSourcePage(driver, wait);
    }

    @Step("I click add new")
    public void clickAddNewDS() {
        clickContextActionAdd();
    }

    @Step("I select Create Data Source - Query Result")
    public void selectDSFromQuery() {
        DropdownList.create(driver, wait).selectOptionById(CREATE_DS_QUERY);
    }

    @Step("I select Create Data Source - CSV File")
    public void selectDSFromCSV() {
        DropdownList.create(driver, wait).selectOptionById(CREATE_DS_CSV);
    }

    @Step("I select Create Data Source - Kafka")
    public void selectDSFromKafka() {
        DropdownList.create(driver, wait).selectOptionById(CREATE_DS_KAFKA);
    }

    @Step("I check if data source: {name} exists into table")
    public Boolean dataSourceExistIntoTable(String name) {
        return feedExistIntoTable(name, DATA_SOURCE_NAME_COLUMN);
    }

    @Step("I select found Data Source")
    public void selectFoundDataSource() {
        getTable(driver, wait).selectRow(0);
        log.info("I selected found Data Source");
    }

    @Step("I click Edit Data Source")
    public void clickEditDS() {
        clickContextActionEdit();
    }

    @Step("I click Delete Data Source")
    public void clickDeleteDS() {
        clickContextActionDelete();
    }

    @Step("I confirm the removal")
    public void clickConfirmDelete() {
        confirmDelete(CONFIRM_DELETE_LABEL);
    }

    @Step("I select logs tab")
    public void selectLogsTab() {
        selectTab(TAB_WIDGET_ID, LOGS_TAB);
        log.info("Selecting logs tab");
    }

    @Step("I select Processed Files tab")
    public void selectProcessedFilesTab() {
        selectTab(TAB_WIDGET_ID, PROCESSED_FILES_TAB);
        log.info("Selecting Processed Files Tab");
    }

    @Step("I click refresh Tab Table")
    public void refreshLogsTable() {
        clickTabsContextAction(TAB_WIDGET_ID, REFRESH_LABEL);
    }

    @Step("I click show file in Tab Table")
    public void showFile() {
        clickTabsContextAction(TAB_WIDGET_ID, SHOW_FILE_LABEL);
        log.info("waiting for show file table to load");
        waitForPageToLoad(driver, wait);
    }

    @Step("I set value in time period chooser")
    public void setValueInTimePeriodChooser(int days, int hours, int minutes) {
        log.info("Setting value for last option in time period chooser: {} days, {} hours, {} minutes", days, hours, minutes);
        waitForPageToLoad(driver, wait);
        TimePeriodChooser timePeriodChooser = TimePeriodChooser.create(driver, wait, TIME_PERIOD_ID);
        timePeriodChooser.clickClearValue();
        waitForPageToLoad(driver, wait);
        timePeriodChooser.chooseOption(TimePeriodChooser.TimePeriodChooserOption.LAST);
        timePeriodChooser.setLastPeriod(days, hours, minutes);
    }

    @Step("I choose option from Severity combobox")
    public void setSeverityInCombobox(String severity) {
        ComponentFactory.create(SEVERITY_COMBOBOX_ID, ComponentType.COMBOBOX, driver, wait)
                .setSingleStringValue(severity);
        log.info("setting severity: {}", severity);
    }

    @Step("I check if logs table is empty")
    public boolean isLogsTableEmpty() {
        waitForPageToLoad(driver, wait);
        return OldTable.createById(driver, wait, LOG_TAB_TABLE_ID).hasNoData();
    }

    @Step("I check Last Request Generation Time")
    public LocalDateTime lastIfRunTime() {
        return lastLogTime(LOG_TAB_TABLE_ID, COLUMN_TIME_LABEL);
    }

    @Step("I check if Last Request Generation Time is fresh - up to 60 min old")
    public boolean isIfRunsFresh() {
        return isLastLogTimeFresh(lastIfRunTime());
    }

    @Step("I check Severity of Data Source from Logs tab")
    public String checkStatus() {
        String severityOfDataSource = checkLogStatus(LOG_TAB_TABLE_ID, COLUMN_SEVERITY_LABEL);
        log.info("Severity of last Data Source log in Logs Tab is {}", severityOfDataSource);

        return severityOfDataSource;
    }

    @Step("I select first file from Processed Files table")
    public void selectFirstFileInTheTable() {
        OldTable.createById(driver, wait, PROCESSED_FILES_TABLE_ID).selectRow(0);
        log.info("Selecting first file in the table");
    }

    @Step("I check if Show File table is not empty")
    public boolean checkIfShowFileTableIsNotEmpty() {
        return !OldTable.createById(driver, wait, SHOW_FILE_TABLE_ID).hasNoData();
    }

    @Step("I check first file name")
    public String getNameOfFirstFileInTheTable() {
        return OldTable.createById(driver, wait, PROCESSED_FILES_TABLE_ID).getCellValue(0, "Filename");
    }

    @Step("I click Download File button in Processed Files Tab")
    public void clickDownloadFile() {
        clickTabsContextAction(TAB_WIDGET_ID, DOWNLOAD_FILE_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking on download file button");
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_DS_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_DS_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_DS_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}
