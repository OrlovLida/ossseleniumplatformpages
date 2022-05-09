package com.oss.pages.servicedesk.issue.problem;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.pages.servicedesk.issue.BaseDashboardPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.COMMON_WIZARD_ID;
import static com.oss.pages.servicedesk.ServiceDeskConstants.PROBLEMS_DASHBOARD;

public class ProblemDashboardPage extends BaseDashboardPage {

    private static final String NAME_ATTRIBUTE = "Name";
    private static final String ASSIGNEE_ATTRIBUTE = "Assignee";
    private static final String PROBLEM_ID_ATTRIBUTE = "Problem ID";
    private static final String STATUS_ATTRIBUTE = "Status";
    private static final String CREATE_PROBLEM_BUTTON_ID = "PM_WIZARD_CREATE_TITLE";
    private static final String PROBLEM_BUTTON_ID = "Problem";
    private static final String PROBLEMS_TABLE_ID = "_tableProblems";

    public ProblemDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open problem dashboard View")
    public ProblemDashboardPage goToPage(WebDriver driver, String basicURL) {
        openPage(driver, getDashboardURL(basicURL, PROBLEMS_DASHBOARD));

        return new ProblemDashboardPage(driver, wait);
    }

    @Step("I open create problem wizard")
    public SDWizardPage clickCreateProblem() {
        return openCreateWizard(PROBLEM_BUTTON_ID, CREATE_PROBLEM_BUTTON_ID, COMMON_WIZARD_ID);
    }

    @Step("Check if problem with {problemName} is in the table")
    public boolean isProblemCreated(String problemName) {
        return getTable().getRowNumber(problemName, NAME_ATTRIBUTE) >= 0;
    }

    @Step("Get problem Id with Problem Name: {problemName}")
    public String getProblemIdWithProblemName(String problemName) {
        return getAttributeFromTable(getRowWithProblemName(problemName), PROBLEM_ID_ATTRIBUTE);
    }

    @Step("Get problem assignee")
    public String getProblemAssignee(String problemName) {
        return getAttributeFromTable(getRowWithProblemName(problemName), ASSIGNEE_ATTRIBUTE);
    }

    @Step("Get problem status")
    public String getProblemStatus(String problemName) {
        return getAttributeFromTable(getRowWithProblemName(problemName), STATUS_ATTRIBUTE);
    }

    private int getRowWithProblemName(String problemName) {
        return getTable().getRowNumber(problemName, NAME_ATTRIBUTE);
    }

    @Override
    protected String getTableID() {
        return PROBLEMS_TABLE_ID;
    }
}
