package com.oss.pages.bigdata.dfe.DataSource;

import com.oss.framework.components.common.TimePeriodChooser;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input.ComponentType;
import com.oss.framework.components.portals.DropdownList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.bigdata.dfe.BaseDfePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class DataSourcePage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(DataSourcePage.class);

    private static final String TABLE_ID = "datasource/datasource-listAppId";
    private final String SEARCH_INPUT_ID = "datasource/datasource-listSearchAppId";
    private final String ADD_NEW_DS_LABEL = "ADD";
    private final String CREATE_DS_QUERY = "OPEN_MODAL_QUERY";
    private final String CREATE_DS_CSV = "OPEN_MODAL_CSV";
    private final String CREATE_DS_KAFKA = "OPEN_MODAL_KAFKA";
    private final String DATA_SOURCE_NAME_COLUMN = "Name";
    private final String EDIT_DS_LABEL = "Edit";
    private final String DELETE_DS_LABEL = "Delete";
    private final String CONFIRM_DELETE_LABEL = "Delete";
    private final String LOGS_TAB = "Logs";
    private final String REFRESH_LABEL = "Refresh";
    private final String TIME_PERIOD_ID = "timePeriod";
    private final String SEVERITY_COMBOBOX_ID = "severityLogs-input";
    private final String LOG_TAB_TABLE_ID = "LogsId";
    private final String COLUMN_TIME_LABEL = "Time";
    private final String COLUMN_SEVERITY_LABEL = "Severity";

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
        DropdownList.create(driver, wait).selectOptionWithId(CREATE_DS_QUERY);
    }

    @Step("I select Create Data Source - CSV File")
    public void selectDSFromCSV() {
        DropdownList.create(driver, wait).selectOptionWithId(CREATE_DS_CSV);
    }

    @Step("I select Create Data Source - Kafka")
    public void selectDSFromKafka() {
        DropdownList.create(driver, wait).selectOptionWithId(CREATE_DS_KAFKA);
    }

    @Step("I check if data source: {name} exists into table")
    public Boolean dataSourceExistIntoTable(String name) {
        return feedExistIntoTable(name, DATA_SOURCE_NAME_COLUMN);
    }

    @Step("I select found Data Source")
    public void selectFoundDataSource() {
        getTable(driver, wait).selectRow(0);
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
        selectTab(LOGS_TAB);
    }

    @Step("I click refresh Tab Table")
    public void refreshLogsTable() {
        clickTabsContextAction(REFRESH_LABEL);
    }

    @Step("I set value in time period chooser")
    public void setValueInTimePeriodChooser(int days, int hours, int minutes) {
        log.info("Setting value for last option in time period chooser: {} days, {} hours, {} minutes", days, hours, minutes);
        DelayUtils.waitForPageToLoad(driver, wait);
        TimePeriodChooser timePeriodChooser = TimePeriodChooser.create(driver, wait, TIME_PERIOD_ID);
        timePeriodChooser.clickClearValue();
        timePeriodChooser.chooseOption(TimePeriodChooser.TimePeriodChooserOption.LAST);
        timePeriodChooser.setLastPeriod(days, hours, minutes);
    }

    @Step("I choose option from Severity combobox")
    public void setSeverityInCombobox(String severity) {
        ComponentFactory.create(SEVERITY_COMBOBOX_ID, ComponentType.COMBOBOX, driver, wait)
                .setSingleStringValue(severity);
    }

    @Step("I check if logs table is empty")
    public boolean isLogsTableEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return OldTable.createByComponentId(driver, wait, LOG_TAB_TABLE_ID).hasNoData();
    }

    @Step("I check Last Request Generation Time")
    public LocalDateTime lastIfRunTime() {
        return lastLogTime(LOG_TAB_TABLE_ID, COLUMN_TIME_LABEL);
    }

    @Step("I check if Last Request Generation Time is fresh - up to 60 min old")
    public boolean IsIfRunsFresh() {
        return isLastLogTimeFresh(lastIfRunTime());
    }

    @Step("I check Severity of Data Source from Logs tab")
    public String checkStatus() {
        String severityOfDataSource = checkLogStatus(LOG_TAB_TABLE_ID, COLUMN_SEVERITY_LABEL);
        log.info("Severity of last Data Source log in Logs Tab is {}", severityOfDataSource);

        return severityOfDataSource;
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
