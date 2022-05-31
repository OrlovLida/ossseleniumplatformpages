package com.oss.pages.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.COMMON_WIZARD_ID;

public class RelatedProblemsTab extends RelatedTab {

    private static final Logger log = LoggerFactory.getLogger(RelatedProblemsTab.class);

    private static final String CREATE_PROBLEM_ID = "PM_WIZARD_CREATE_TITLE";
    private static final String PROBLEM_BUTTON_ID = "Problem";
    private static final String RELATED_PROBLEMS_TABLE_ID = "_relatedProblemsApp";
    private static final String RELATED_PROBLEMS_TABLE_NAME_ATTRIBUTE_ID = "Name";
    private static final String RELATED_PROBLEMS_TABLE_ASSIGNEE_ATTRIBUTE_ID = "Assignee";
    private static final String LINK_PROBLEM_TO_PROBLEM_BUTTON_ID = "_linkProblem";
    private static final String LINK_PROMPT_ID = "_linkProblemModal_prompt-card";
    private static final String COLUMN_PROBLEM_ID_LABEL = "Problem ID";
    private static final String UNLINK_PROBLEM_BUTTON_ID = "_unlinkProblem";
    private static final String RELATED_PROBLEMS_EXPORT_FILE = "Problem*.xlsx";
    private static final String OLD_ACTIONS_CONTAINER_ID = "_tablesWindow-windowToolbar";

    public RelatedProblemsTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open Create Problem wizard")
    public SDWizardPage openCreateProblemWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldActionsContainer.createById(driver, wait, OLD_ACTIONS_CONTAINER_ID).callActionById(CREATE_PROBLEM_ID, PROBLEM_BUTTON_ID);
        log.info("Create Problem Wizard is opened");
        return new SDWizardPage(driver, wait, COMMON_WIZARD_ID);
    }

    public boolean isCreateProblemButtonActive() {
        return isActionPresent(CREATE_PROBLEM_ID);
    }

    @Step("I check related problem name")
    public String checkRelatedProblemName(int problemIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check related problem name");
        return getRelatedIssuesTable().getCellValue(problemIndex, RELATED_PROBLEMS_TABLE_NAME_ATTRIBUTE_ID);
    }

    @Step("I check related problem assignee")
    public String checkRelatedProblemAssignee(int problemIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check related problem assignee");
        return getRelatedIssuesTable().getCellValue(problemIndex, RELATED_PROBLEMS_TABLE_ASSIGNEE_ATTRIBUTE_ID);
    }

    @Override
    public String getTableId() {
        return RELATED_PROBLEMS_TABLE_ID;
    }

    @Override
    public String getLinkActionId() {
        return LINK_PROBLEM_TO_PROBLEM_BUTTON_ID;
    }

    @Override
    public String getUnlinkActionId() {
        return UNLINK_PROBLEM_BUTTON_ID;
    }

    @Override
    public String getRelatedIssueExportFile() {
        return RELATED_PROBLEMS_EXPORT_FILE;
    }

    @Override
    public String getLinkIssuePromptId() {
        return LINK_PROMPT_ID;
    }

    @Override
    public String getIdAttribute() {
        return COLUMN_PROBLEM_ID_LABEL;
    }
}

