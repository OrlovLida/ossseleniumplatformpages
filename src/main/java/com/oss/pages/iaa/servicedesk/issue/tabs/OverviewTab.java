package com.oss.pages.iaa.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.MoreDetailsPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.COMMON_WIZARD_ID;

public class OverviewTab extends IssueDetailsPage {

    private static final Logger log = LoggerFactory.getLogger(OverviewTab.class);

    private static final String EDIT_DETAILS_LABEL = "Edit details";
    private static final String ISSUE_ASSIGNEE_ID = "assignee";
    private static final String STATUS_INPUT = "change-ticket-status-combobox";
    private static final String MORE_DETAILS_LABEL = "More details";

    public OverviewTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Open edit issue wizard")
    public SDWizardPage openEditIssueWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getDetailsViewOldActionsContainer().callActionByLabel(EDIT_DETAILS_LABEL);
        log.info("Edit Issue Wizard is opened");
        return new SDWizardPage(driver, wait, COMMON_WIZARD_ID);
    }

    @Step("Change Issue assignee")
    public void changeIssueAssignee(String assignee) {
        ComponentFactory.create(ISSUE_ASSIGNEE_ID, driver, wait)
                .setSingleStringValue(assignee);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Check Issue Assignee")
    public String checkAssignee() {
        return ComponentFactory.create(ISSUE_ASSIGNEE_ID, driver, wait).getStringValue();
    }

    @Step("Change issue status")
    public void changeIssueStatus(String status) {
        ComponentFactory.create(STATUS_INPUT, driver, wait)
                .setSingleStringValue(status);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Check status in issue status combobox")
    public String checkIssueStatus() {
        return ComponentFactory.create(STATUS_INPUT, driver, wait).getStringValue();
    }

    @Step("Click More Details")
    public MoreDetailsPage clickMoreDetails() {
        getDetailsViewOldActionsContainer().callActionByLabel(MORE_DETAILS_LABEL);
        log.info("Clicking More Details");
        return new MoreDetailsPage(driver, wait);
    }
}
