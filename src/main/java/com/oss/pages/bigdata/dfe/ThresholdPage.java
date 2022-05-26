package com.oss.pages.bigdata.dfe;

import java.time.LocalDateTime;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.data.Data;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.MULTI_COMBOBOX;
import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class ThresholdPage extends BaseDfePage {

    private static final String DETAILS_TAB = "Details";
    private static final Logger log = LoggerFactory.getLogger(ThresholdPage.class);
    private static final String TABLE_ID = "thresholdsAppId";
    private static final String ADD_NEW_THRESHOLD_LABEL = "Add New Threshold";
    private static final String EDIT_THRESHOLD_LABEL = "Edit Threshold";
    private static final String DELETE_THRESHOLD_LABEL = "Delete Threshold";
    private static final String SEARCH_INPUT_ID = "thresholdsSearchAppId";
    private static final String TABS_WIDGET_ID = "card-content_tabsId";
    private static final String EXECUTION_HISTORY_TAB = "Execution History";
    private static final String TABLE_TAB_ID = "logsId";
    private static final String COLUMN_REQUEST_GENERATION_TIME_LABEL = "Request Generation Time";
    private static final String COLUMN_STATUS_LABEL = "Status";
    private static final String REFRESH_LABEL = "Refresh";
    private static final String SEARCH_CATEGORIES_ID = "category";
    private static final String NAME_COLUMN_LABEL = "Name";
    private static final String CATEGORY_COLUMN_LABEL = "Category";
    private static final String DELETE_LABEL = "Delete";
    private static final String TAB_WIDGET_ID = "card-content_tabsId";
    private static final String PROPERTY_PANEL_ID = "detailsId";
    private static final String NAME_PROPERTY = "Name";
    private static final String CONDITIONS_TAB = "Conditions";
    private static final String PARAMETERS_TABLE_ID = "conditionsTableId";
    private static final String SEARCH_STATUS_ID = "status";
    private static final String STATUS_COLUMN_LABEL = "Status";
    private static final String SEARCH_IS_ACTIVE_ID = "is_active";
    private static final String IS_ACTIVE_COLUMN_LABEL = "Is Active";
    private static final String SEARCH_PROBLEM_ID = "problem_id";
    private static final String PROBLEM_ID_COLUMN_LABEL = "Problem ID";
    private static final String ACTIVATE_BATCH_LABEL = "Activate Batch of Thresholds Configuration";
    private static final String DEACTIVATE_BATCH_LABEL = "Deactivate Batch of Thresholds Configuration";
    private static final String WIZARD_ID = "deactivateItemsId_prompt-card";
    private static final String SAVE_LABEL = "Save";

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

    @Step("I click Activate Batch")
    public void clickActivateBatch() {
        clickContextAction(ACTIVATE_BATCH_LABEL);
    }

    @Step("I click Deactivate Batch")
    public void clickDeactivateBatch() {
        clickContextAction(DEACTIVATE_BATCH_LABEL);
    }

    @Step("I click Save")
    public void clickSave() {
        Wizard.createByComponentId(driver, wait, WIZARD_ID).clickSave();
        log.info("Confirmation by clicking 'Save'");
    }

    @Step("I check if Threshold: {thresholdName} exists into the table")
    public Boolean thresholdExistsIntoTable(String thresholdName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return feedExistIntoTable(thresholdName, NAME_COLUMN_LABEL);
    }

    @Step("Check if Threshold is Active")
    public Boolean thresholdISActive() {
        String isActive = getTable(driver, wait).getCellValue(0, IS_ACTIVE_COLUMN_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        return isActive.contains("Yes");
    }

    @Step("I select found Threshold")
    public void selectFoundThreshold() {
        getTable(driver, wait).selectRow(0);
    }

    @Step("I confirm the removal of Threshold")
    public void confirmDelete() {
        confirmDelete(DELETE_LABEL);
    }

    @Step("I confirm deactivation or activation of Threshold")
    public void confirmDeactivationOrActivation() {
        confirmDeactivation();
    }

    @Step("I click Execution History Tab")
    public void selectExecutionHistoryTab() {
        selectTab(TABS_WIDGET_ID, EXECUTION_HISTORY_TAB);
    }

    @Step("I click Conditions Tab")
    public void selectConditionsTab() {
        selectTab(TAB_WIDGET_ID, CONDITIONS_TAB);
    }

    @Step("I click Refresh Table Tab")
    public void clickRefreshInTabTable() {
        clickRefreshTabTable(TABS_WIDGET_ID, REFRESH_LABEL);
    }

    @Step("I check Last Request Generation Time")
    public LocalDateTime lastIfRunTime() {
        return lastLogTime(TABLE_TAB_ID, COLUMN_REQUEST_GENERATION_TIME_LABEL);
    }

    @Step("I check if Last Request Generation Time is fresh - up to 60 min old")
    public boolean isIfRunsFresh() {
        return isLastLogTimeFresh(lastIfRunTime());
    }

    @Step("I check Status of Threshold from Execution History tab")
    public String checkStatus() {
        String statusOfThreshold = checkLogStatus(TABLE_TAB_ID, COLUMN_STATUS_LABEL);
        log.info("Status of last threshold log in Execution History is {}", statusOfThreshold);

        return statusOfThreshold;
    }

    @Step("I look for Threshold with set categories")
    public void searchCategories(String categories) {
        waitForPageToLoad(driver, wait);
        Input searchCategoriesComponent = ComponentFactory.create(SEARCH_CATEGORIES_ID, MULTI_COMBOBOX, driver, wait);
        searchCategoriesComponent.setValue(Data.createSingleData(categories));
        log.debug("Filled categories with: {}", categories);
    }

    @Step("I look for Threshold with set Problem ID")
    public void searchProblemId(String problemId) {
        waitForPageToLoad(driver, wait);
        Input searchIdComponent = ComponentFactory.create(SEARCH_PROBLEM_ID, MULTI_COMBOBOX, driver, wait);
        searchIdComponent.setValue(Data.createSingleData(problemId));
        log.debug("Filled Problem ID with: {}", problemId);
    }

    @Step("Choose Status")
    public void chooseStatus(String status) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Input searchStatusComponent = ComponentFactory.create(SEARCH_STATUS_ID, Input.ComponentType.MULTI_COMBOBOX, driver, wait);
        searchStatusComponent.setSingleStringValue(status);
        log.info("Choose status: {}", status);
    }

    @Step("Choose 'Is Active' Status")
    public void chooseIsActive(String activity) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Input searchActiveComponent = ComponentFactory.create(SEARCH_IS_ACTIVE_ID, Input.ComponentType.MULTI_COMBOBOX, driver, wait);
        searchActiveComponent.setSingleStringValue(activity);
        log.info("Is Active: {}", activity);
    }

    @Step("I look for category name")
    public String getCategoryName(int index) {
        return getTable(driver, wait).getCellValue(index, CATEGORY_COLUMN_LABEL);
    }

    @Step("I look for problem ID")
    public String getProblemId(int index) {
        return getTable(driver, wait).getCellValue(index, PROBLEM_ID_COLUMN_LABEL);
    }

    @Step("Check if Threshold is active")
    public String getIsActive(int index) {
        return getTable(driver, wait).getCellValue(index, IS_ACTIVE_COLUMN_LABEL);
    }

    @Step("I look for status name")
    public String getStatus(int index) {
        return getTable(driver, wait).getCellValue(index, STATUS_COLUMN_LABEL);
    }

    @Step("Click details tab")
    public void selectDetailsTab() {
        selectTab(TAB_WIDGET_ID, DETAILS_TAB);
    }

    @Step("Check name value in details tab")
    public String checkNameInPropertyPanel() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return checkValueInPropertyPanel(PROPERTY_PANEL_ID,
                NAME_PROPERTY);
    }

    @Step("I check if Conditions Table contains simple and else condition")
    public Boolean isSimpleAndElseConditionInTable() {
        log.info("Check if there are at least 2 conditions in Conditions Table");
        return OldTable
                .createById(driver, wait, PARAMETERS_TABLE_ID)
                .countRows("Name") > 1;
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
