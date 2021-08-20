package com.oss.pages.bigdata.dfe;

import com.oss.framework.utils.DelayUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class ThresholdPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(ThresholdPage.class);

    private static final String TABLE_ID = "thresholdsAppId";

    private final String ADD_NEW_THRESHOLD_LABEL = "Add New Threshold";
    private final String EDIT_THRESHOLD_LABEL = "Edit Threshold";
    private final String DELETE_THRESHOLD_LABEL = "Delete Threshold";
    private final String SEARCH_INPUT_ID = "thresholdsSearchAppId";
    private final String EXECUTION_HISTORY_TAB = "Execution History";
    private final String TABLE_TAB_ID = "logsId";
    private final String COLUMN_REQUEST_GENERATION_TIME_LABEL = "Request Generation Time";
    private final String COLUMN_STATUS_LABEL = "Status";
    private final String REFRESH_LABEL = "Refresh";

    private final String NAME_COLUMN_LABEL = "Name";
    private final String DELETE_LABEL = "Delete";

    private ThresholdPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open Thresholds View")
    public static ThresholdPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "thresholds");
        return new ThresholdPage(driver, wait);
    }

    @Step("I click add new Threshold")
    public void clickAddNewThreshold() {
        clickContextActionAdd();
    }

    @Step("I click edit Threshold")
    public void clickEditThreshold() {
        clickContextActionEdit();
    }

    @Step("I click delete Threshold")
    public void clickDeleteThreshold() {
        clickContextActionDelete();
    }

    @Step("I check if Threshold: {thresholdName} exists into the table")
    public Boolean thresholdExistsIntoTable(String thresholdName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return feedExistIntoTable(thresholdName, NAME_COLUMN_LABEL);
    }

    @Step("I select found Threshold")
    public void selectFoundThreshold() {
        getTable(driver, wait).selectRow(0);
    }

    @Step("I confirm the removal of Threshold")
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
        String statusOfThreshold = checkLogStatus(TABLE_TAB_ID, COLUMN_STATUS_LABEL);
        log.info("Status of last threshold log in Execution History is {}", statusOfThreshold);

        return statusOfThreshold;
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_THRESHOLD_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_THRESHOLD_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_THRESHOLD_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}
