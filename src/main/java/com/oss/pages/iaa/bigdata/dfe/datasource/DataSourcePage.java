package com.oss.pages.iaa.bigdata.dfe.datasource;

import java.time.LocalDateTime;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.iaa.widgets.timeperiodchooser.TimePeriodChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.iaa.bigdata.dfe.BaseDfePage;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class DataSourcePage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(DataSourcePage.class);

    private static final String TABLE_ID = "datasource/datasource-listAppId";
    private static final String SEARCH_INPUT_ID = "input_datasource/datasource-listSearchAppId";
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
    private static final String SEVERITY_COMBOBOX_ID = "severityLogs";
    private static final String LOG_TAB_TABLE_ID = "LogsId";
    private static final String COLUMN_TIME_LABEL = "Time";
    private static final String COLUMN_SEVERITY_LABEL = "Severity";
    private static final String PROCESSED_FILES_TABLE_ID = "stats-tableAppId";
    private static final String DOWNLOAD_FILE_LABEL = "Download file";
    private static final String SHOW_FILE_TABLE_ID = "TableId";
    private static final String TAB_WIDGET_ID = "card-content_TabsId";
    private static final String DETAILS_TAB = "Details";
    private static final String PROPERTY_PANEL_ID = "DetailsId";
    private static final String IS_ACTIVE_PROPERTY = "Is Active";
    private static final String DATABASE_PROPERTY = "Database";
    private static final String CATEGORY_SEARCH_ID = "category";
    private static final String STATUS_SEARCH_ID = "status";
    private static final String DATA_FORMAT_SEARCH_ID = "format";
    private static final String REFRESH_BUTTON_ID = "REFRESH_DATA_SOURCE_MODAL";
    private static final String FORMAT_TAB_ID = "Format";
    private static final String FORMAT_TABLE_ID = "FormatTableAppId";

    public DataSourcePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Open Data Source View")
    public static DataSourcePage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "datasource");
        return new DataSourcePage(driver, wait);
    }

    @Step("Click add new")
    public void clickAddNewDS() {
        clickContextActionAdd();
    }

    @Step("Select Create Data Source - Query Result")
    public void selectDSFromQuery() {
        DropdownList.create(driver, wait).selectOptionById(CREATE_DS_QUERY);
    }

    @Step("Select Create Data Source - CSV File")
    public void selectDSFromCSV() {
        DropdownList.create(driver, wait).selectOptionById(CREATE_DS_CSV);
    }

    @Step("Select Create Data Source - Kafka")
    public void selectDSFromKafka() {
        DropdownList.create(driver, wait).selectOptionById(CREATE_DS_KAFKA);
    }

    @Step("Check if data source: {name} exists into table")
    public Boolean dataSourceExistIntoTable(String name) {
        return feedExistIntoTable(name, DATA_SOURCE_NAME_COLUMN);
    }

    @Step("Select first Data Source in table")
    public void selectFirstDataSourceInTable() {
        getTable().selectRow(0);
        log.info("I selected found Data Source");
    }

    @Step("Get Value of: {columnName} from first Data Source from Table")
    public String getValueFromFirstRow(String columnName) {
        return getTable().getCellValue(0, columnName);
    }

    @Step("Check if Data Source table is empty")
    public boolean isDataSourceTableEmpty() {
        return getTable().hasNoData();
    }

    @Step("Click Edit Data Source")
    public void clickEditDS() {
        clickContextActionEdit();
    }

    @Step("Click Delete Data Source")
    public void clickDeleteDS() {
        clickContextActionDelete();
    }

    @Step("Confirm the removal")
    public void clickConfirmDelete() {
        confirmDelete(CONFIRM_DELETE_LABEL);
    }

    @Step("Click Refresh Data Source")
    public void clickRefresh() {
        getTable().callAction(REFRESH_BUTTON_ID);
        waitForPageToLoad(driver, wait);
    }

    @Step("Select logs tab")
    public void selectLogsTab() {
        selectTab(TAB_WIDGET_ID, LOGS_TAB);
        log.info("Selecting Logs tab");
    }

    @Step("Select Details Tab")
    public void selectDetailsTab() {
        selectTab(TAB_WIDGET_ID, DETAILS_TAB);
        log.info("Selecting Details tab");
    }

    @Step("Select Processed Files tab")
    public void selectProcessedFilesTab() {
        selectTab(TAB_WIDGET_ID, PROCESSED_FILES_TAB);
        log.info("Selecting Processed Files Tab");
    }

    @Step("Select Format Tab")
    public void selectFormatTab() {
        selectTab(TAB_WIDGET_ID, FORMAT_TAB_ID);
        log.info("Selecting Format Tab");
    }

    @Step("Check name in details tab")
    public String getIsActiveFromPropertyPanel() {
        waitForPageToLoad(driver, wait);
        return getValueFromPropertyPanel(PROPERTY_PANEL_ID, IS_ACTIVE_PROPERTY);
    }

    @Step("Check database in details tab")
    public String getDatabaseFromPropertyPanel() {
        waitForPageToLoad(driver, wait);
        return getValueFromPropertyPanel(PROPERTY_PANEL_ID, DATABASE_PROPERTY);
    }

    @Step("Click refresh Tab Table")
    public void refreshLogsTable() {
        clickTabsContextAction(TAB_WIDGET_ID, REFRESH_LABEL);
    }

    @Step("Click show file in Tab Table")
    public void showFile() {
        clickTabsContextAction(TAB_WIDGET_ID, SHOW_FILE_LABEL);
        log.info("waiting for show file table to load");
        waitForPageToLoad(driver, wait);
    }

    @Step("Set value in time period chooser")
    public void setValueInTimePeriodChooser(int days, int hours, int minutes) {
        log.info("Setting value for last option in time period chooser: {} days, {} hours, {} minutes", days, hours, minutes);
        waitForPageToLoad(driver, wait);
        TimePeriodChooser timePeriodChooser = TimePeriodChooser.create(driver, wait, TIME_PERIOD_ID);
        timePeriodChooser.clickClearValue();
        waitForPageToLoad(driver, wait);
        timePeriodChooser.chooseOption(TimePeriodChooser.TimePeriodChooserOption.LAST);
        timePeriodChooser.setLastPeriod(days, hours, minutes);
    }

    @Step("Set Category search")
    public void setCategory(String categoryName) {
        ComponentFactory.create(CATEGORY_SEARCH_ID, driver, wait).setSingleStringValue(categoryName);
        waitForPageToLoad(driver, wait);
        log.info("Category search is set to: {}", categoryName);
    }

    @Step("Set Status search")
    public void setStatus(String statusName) {
        ComponentFactory.create(STATUS_SEARCH_ID, driver, wait).setSingleStringValue(statusName);
        waitForPageToLoad(driver, wait);
        log.info("Status search is set to: {}", statusName);
    }

    @Step("Set Data Format search")
    public void setDataFormat(String dataFormat) {
        ComponentFactory.create(DATA_FORMAT_SEARCH_ID, driver, wait).setSingleStringValue(dataFormat);
        waitForPageToLoad(driver, wait);
        log.info("Data Format search is set to: {}", dataFormat);
    }

    @Step("Choose option from Severity combobox")
    public void setSeverity(String severity) {
        ComponentFactory.create(SEVERITY_COMBOBOX_ID, driver, wait).setSingleStringValue(severity);
        log.info("setting severity: {}", severity);
    }

    @Step("Check if logs table is empty")
    public boolean isLogsTableEmpty() {
        return OldTable.createById(driver, wait, LOG_TAB_TABLE_ID).hasNoData();
    }

    @Step("Chek if Format table is empty")
    public boolean isFormatTableEmpty() {
        return OldTable.createById(driver, wait, FORMAT_TABLE_ID).hasNoData();
    }

    @Step("Check Last Request Generation Time")
    public LocalDateTime lastIfRunTime() {
        return lastLogTime(LOG_TAB_TABLE_ID, COLUMN_TIME_LABEL);
    }

    @Step("Check if Last Request Generation Time is fresh - up to 60 min old")
    public boolean isIfRunsFresh() {
        return isLastLogTimeFresh(lastIfRunTime());
    }

    @Step("Check Severity of Data Source from Logs tab")
    public String checkStatus() {
        String severityOfDataSource = checkLogStatus(LOG_TAB_TABLE_ID, COLUMN_SEVERITY_LABEL);
        log.info("Severity of last Data Source log in Logs Tab is {}", severityOfDataSource);

        return severityOfDataSource;
    }

    @Step("Select first file from Processed Files table")
    public void selectFirstFileInTheTable() {
        OldTable.createById(driver, wait, PROCESSED_FILES_TABLE_ID).selectRow(0);
        log.info("Selecting first file in the table");
    }

    @Step("Check if Show File table is empty")
    public boolean isShowFileTableEmpty() {
        return OldTable.createById(driver, wait, SHOW_FILE_TABLE_ID).hasNoData();
    }

    @Step("Check first file name")
    public String getNameOfFirstFileInTheTable() {
        return OldTable.createById(driver, wait, PROCESSED_FILES_TABLE_ID).getCellValue(0, "Filename");
    }

    @Step("Click Download File button in Processed Files Tab")
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
