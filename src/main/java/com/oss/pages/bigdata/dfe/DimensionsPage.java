package com.oss.pages.bigdata.dfe;

import java.time.LocalDateTime;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Step;

public class DimensionsPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(DimensionsPage.class);

    private static final String TABLE_ID = "dimension-listAppId";

    private static final String ADD_NEW_DIMENSION_LABEL = "Add New Dimension";
    private static final String EDIT_DIMENSION_LABEL = "Edit Dimension";
    private static final String DELETE_DIMENSION_LABEL = "Delete Dimension";
    private static final String SEARCH_INPUT_ID = "dimension-listSearchAppId";
    private static final String LOGS_TAB = "Logs";
    private static final String REFRESH_LABEL = "Refresh";
    private static final String LOGS_TABLE_TAB_ID = "logsId";
    private static final String COLUMN_TIME_LABEL = "Time";
    private static final String COLUMN_SEVERITY_LABEL = "Severity";
    private static final String TABS_WIDGET_ID = "card-content_TabsId";

    private static final String NAME_COLUMN_LABEL = "Name";
    private static final String DELETE_LABEL = "Delete";

    private DimensionsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open Dimensions View")
    public static DimensionsPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "dimension");
        return new DimensionsPage(driver, wait);
    }

    @Step("I click add new Dimension")
    public void clickAddNewDimension() {
        clickContextActionAdd();
    }

    @Step("I click edit Dimension")
    public void clickEditDimension() {
        clickContextActionEdit();
    }

    @Step("I click delete Dimension")
    public void clickDeleteDimension() {
        clickContextActionDelete();
    }

    @Step("I check if Dimension: {dimensionName} exists into the table")
    public Boolean dimensionExistsIntoTable(String dimensionName) {
        return feedExistIntoTable(dimensionName, NAME_COLUMN_LABEL);
    }

    @Step("I select found Dimension")
    public void selectFoundDimension() {
        getTable(driver, wait).selectRow(0);
    }

    @Step("I confirm the removal")
    public void confirmDelete() {
        confirmDelete(DELETE_LABEL);
    }

    @Step("I click Logs Tab")
    public void selectLogsTab() {
        selectTab(TABS_WIDGET_ID, LOGS_TAB);
    }

    @Step("I click refresh Tab Table")
    public void refreshLogsTable() {
        clickTabsContextAction(TABS_WIDGET_ID, REFRESH_LABEL);
    }

    @Step("I check last log time from Logs Tab")
    public LocalDateTime lastLogTimeInTimeColumn() {
        return lastLogTime(LOGS_TABLE_TAB_ID, COLUMN_TIME_LABEL);
    }

    @Step("I check if Last Log Time is fresh - up to 60 min old")
    public boolean isLastLogTimeFromTimeColumnFresh() {
        return isLastLogTimeFresh(lastLogTimeInTimeColumn());
    }


    @Step("I check Severity of Dimension Logs from Logs Tab")
    public String checkSeverity() {
        String severityOfDimension = checkLogStatus(LOGS_TABLE_TAB_ID, COLUMN_SEVERITY_LABEL);
        log.info("Severity of last dimension log is {}", severityOfDimension);

        return severityOfDimension;
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_DIMENSION_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_DIMENSION_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_DIMENSION_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}
