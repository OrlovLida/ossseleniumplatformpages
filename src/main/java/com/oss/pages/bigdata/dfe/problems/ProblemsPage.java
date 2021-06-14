package com.oss.pages.bigdata.dfe.problems;

import com.oss.pages.bigdata.dfe.BaseDfePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProblemsPage extends BaseDfePage {

    private static final String TABLE_ID = "problemsAppId";
    private final String ADD_NEW_PROBLEM_LABEL = "Add New Problems Configuration";
    private final String EDIT_PROBLEM_LABEL = "Edit Problems Configuration";
    private final String DELETE_PROBLEM_LABEL = "Delete Problems Configuration";
    private final String SEARCH_INPUT_ID = "problemsSearchAppId";

    private final String NAME_COLUMN_LABEL = "Name";
    private final String DELETE_LABEL = "Delete";

    private final ProblemsPopupPage problemsPopupPage;

    public ProblemsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        problemsPopupPage = new ProblemsPopupPage(driver, wait);
    }

    public ProblemsPopupPage getProblemsPopup() {
        return problemsPopupPage;
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
        return feedExistIntoTable(problemName, NAME_COLUMN_LABEL);
    }

    @Step("I select found Problem")
    public void selectFoundProblem() {
        getTable(driver, wait).selectRow(0);
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
