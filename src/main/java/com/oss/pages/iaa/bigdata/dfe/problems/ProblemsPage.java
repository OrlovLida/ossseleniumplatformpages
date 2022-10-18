package com.oss.pages.iaa.bigdata.dfe.problems;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.iaa.bigdata.dfe.BaseDfePage;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class ProblemsPage extends BaseDfePage {

    private static final String TABLE_ID = "problemsAppId";
    private static final String ADD_NEW_PROBLEM_LABEL = "Add New Problems Configuration";
    private static final String EDIT_PROBLEM_LABEL = "Edit Problems Configuration";
    private static final String DELETE_PROBLEM_LABEL = "Delete Problems Configuration";
    private static final String SEARCH_INPUT_ID = "input_problemsSearchAppId";
    private static final String NAME_COLUMN_LABEL = "Name";
    private static final String DELETE_LABEL = "Delete";
    private static final String THRESHOLDS_TABLE_ID = "notificationsTableId";
    private static final String PROBLEM_IDENTIFIER_COLUMN = "Problem Identifier";
    private static final String PROBLEM_IDENTIFIER_SEARCH_ID = "problem_identifier";

    public ProblemsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open Problems View")
    public static ProblemsPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "problems");
        return new ProblemsPage(driver, wait);
    }

    @Step("I click add new Problem")
    public void clickAddNewProblem() {
        clickContextActionAdd();
    }

    @Step("I click edit Problem")
    public void clickEditProblem() {
        clickContextActionEdit();
    }

    @Step("I click delete Problem")
    public void clickDeleteProblem() {
        clickContextActionDelete();
    }

    @Step("I check if Problem: {problemName} exists into the table")
    public Boolean problemExistsIntoTable(String problemName) {
        waitForPageToLoad(driver, wait);
        return feedExistIntoTable(problemName, NAME_COLUMN_LABEL);
    }

    @Step("Clear Search field")
    public void clearSearchField() {
        ComponentFactory.create(SEARCH_INPUT_ID, driver, wait).clear();
        log.info("Clearing search field");
        waitForPageToLoad(driver, wait);
    }

    @Step("I select found Problem")
    public void selectFoundProblem() {
        waitForPageToLoad(driver, wait);
        getTable().selectRow(0);
    }

    @Step("Get Problem Identifier from first row")
    public String getProblemIdentifier() {
        log.info("Getting problem Id");
        return getTable().getCellValue(0, PROBLEM_IDENTIFIER_COLUMN);
    }

    @Step("Search for Problem id: {problemId}")
    public void searchForProblem(String problemId) {
        ComponentFactory.create(PROBLEM_IDENTIFIER_SEARCH_ID, driver, wait).setSingleStringValue(problemId);
        log.info("Searching for Problem ID: {}", problemId);
    }

    @Step("Check if Threshold Table is Empty")
    public boolean isThresholdsTableEmpty() {
        waitForPageToLoad(driver, wait);
        return getThresholdsTable().hasNoData();
    }

    private OldTable getThresholdsTable() {
        return OldTable.createById(driver, wait, THRESHOLDS_TABLE_ID);
    }

    @Step("I confirm the removal")
    public void confirmDelete() {
        confirmDelete(DELETE_LABEL);
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_PROBLEM_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_PROBLEM_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_PROBLEM_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}
