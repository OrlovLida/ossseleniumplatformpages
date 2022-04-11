package com.oss.pages.servicedesk.ticket.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.ticket.wizard.SDWizardPage;

import io.qameta.allure.Step;

public class RelatedProblemsTab extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(RelatedProblemsTab.class);

    private static final String CREATE_PROBLEM_ID = "_createProblem";
    private static final String RELATED_PROBLEMS_TABLE_ID = "_relatedProblemsApp";
    private static final String RELATED_PROBLEMS_TABLE_NAME_ATTRIBUTE_ID = "Name";
    private static final String RELATED_PROBLEMS_TABLE_ASSIGNEE_ATTRIBUTE_ID = "Assignee";
    private static final String RELATED_PROBLEMS_TABLE_LABEL_ATTRIBUTE_ID = "Label";
    private static final String LINK_PROBLEM_TO_PROBLEM_BUTTON_ID = "_linkProblem";
    private static final String COLUMN_PROBLEM_ID_LABEL = "Problem ID";
    private static final String UNLINK_PROBLEM_BUTTON_ID = "_unlinkProblem";

    public RelatedProblemsTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open Create Problem wizard")
    public SDWizardPage openCreateProblemWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(CREATE_PROBLEM_ID);
        log.info("Create Problem Wizard is opened");
        return new SDWizardPage(driver, wait);
    }

    @Step("I check related problem name")
    public String checkRelatedProblemName(int problemIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check related problem name");
        return getRelatedProblemsTable().getCellValue(problemIndex, RELATED_PROBLEMS_TABLE_NAME_ATTRIBUTE_ID);
    }

    @Step("I check related problem assignee")
    public String checkRelatedProblemAssignee(int problemIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check related problem assignee");
        return getRelatedProblemsTable().getCellValue(problemIndex, RELATED_PROBLEMS_TABLE_ASSIGNEE_ATTRIBUTE_ID);
    }

    @Step("I check related problem label")
    public String checkRelatedProblemLabel(int problemIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check related problem label");
        return getRelatedProblemsTable().getCellValue(problemIndex, RELATED_PROBLEMS_TABLE_LABEL_ATTRIBUTE_ID);
    }

    @Step("Link Related Problem to Problem")
    public SDWizardPage clickLinkProblem() {
        clickContextActionFromButtonContainer(LINK_PROBLEM_TO_PROBLEM_BUTTON_ID);

        return new SDWizardPage(driver, wait);
    }

    @Step("Select Problem in Related Problems Table")
    public void selectProblem(int row) {
        getRelatedProblemsTable().selectRow(row);
        log.info("Selecting problem in row: {}", row);
    }

    @Step("Unlink Related Problem from problem")
    public SDWizardPage clickUnlinkProblem() {
        clickContextActionFromButtonContainer(UNLINK_PROBLEM_BUTTON_ID);

        return new SDWizardPage(driver, wait);
    }

    @Step("Check related problem ID")
    public String checkRelatedProblemId(int ticketIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check related tickets ID");
        return getRelatedProblemsTable().getCellValue(ticketIndex, COLUMN_PROBLEM_ID_LABEL);
    }

    @Step("Check if Related Problems Table is Empty")
    public boolean isRelatedProblemsTableEmpty() {
        return getRelatedProblemsTable().hasNoData();
    }

    private OldTable getRelatedProblemsTable() {
        return OldTable.createById(driver, wait, RELATED_PROBLEMS_TABLE_ID);
    }
}

