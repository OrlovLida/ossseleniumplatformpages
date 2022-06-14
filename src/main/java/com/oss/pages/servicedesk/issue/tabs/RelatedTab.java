package com.oss.pages.servicedesk.issue.tabs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

public abstract class RelatedTab extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(RelatedTab.class);

    private static final String SHOW_ARCHIVED_SWITCHER_ID = "with_archived";
    private static final String TABS_CONTAINER_ID = "_tablesWindow";
    private static final String CONFIRM_UNLINK_BUTTON_LABEL = "Unlink";
    private static final String LINK_ISSUE_BUTTON_ID = "_buttonsApp-1";

    protected RelatedTab(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Open link issue prompt")
    public SDWizardPage openLinkIssueWizard() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(getLinkActionId());
        log.info("Link Issues prompt is opened");
        return new SDWizardPage(driver, wait, getLinkIssuePromptId());
    }

    @Step("Check related issues ID")
    public String checkRelatedIssueId(int ticketIndex) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check related issues ID");
        return getRelatedIssuesTable().getCellValue(ticketIndex, getIdAttribute());
    }

    @Step("Select issue with index: {index}")
    public void selectIssue(int index) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Selecting issue in row: {}", index);
        getRelatedIssuesTable().selectRow(index);
    }

    @Step("Check Related Issues table")
    public boolean isRelatedIssueTableEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Check if Related Issues Table is empty");
        return getRelatedIssuesTable().hasNoData();
    }

    public boolean isLinkIssueButtonActive() {
        return isActionPresent(getLinkActionId());
    }

    @Step("Click unlink Issue from Related Issues Tab")
    public void unlinkIssue() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickContextActionFromButtonContainer(getUnlinkActionId());
        log.info("Unlink issue popup is opened");
    }

    @Step("Confirm unlinking issue")
    public void confirmUnlinking() {
        DelayUtils.waitForPageToLoad(driver, wait);
        ConfirmationBox.create(driver, wait).clickButtonByLabel(CONFIRM_UNLINK_BUTTON_LABEL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Confirm unlinking issue");
    }

    @Step("Turn On Show archived switcher")
    public void turnOnShowArchived() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Turn On Show archived switcher");
        ComponentFactory.create(SHOW_ARCHIVED_SWITCHER_ID, driver, wait)
                .setSingleStringValue(Boolean.TRUE.toString());
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public RelatedTab linkIssue(String issueId, String componentId) {
        openLinkIssueWizard()
                .insertValueContainsToComponent(issueId, componentId)
                .clickButton(LINK_ISSUE_BUTTON_ID);
        return this;
    }

    public RelatedTab exportFromTabTable() {
        openNotificationPanel()
                .clearNotifications();
        clickExport();
        openNotificationPanel()
                .waitForExportFinish()
                .clickDownload();
        openNotificationPanel().waitAndGetFinishedNotificationText();
        openNotificationPanel().clearNotifications();
        attachRelatedIssuesFile();
        return this;
    }

    public RelatedTab unlinkFirstIssue() {
        selectIssue(0);
        unlinkIssue();
        confirmUnlinking();
        return this;
    }

    @Step("Click Export")
    public void clickExport() {
        clickExportFromTab(TABS_CONTAINER_ID);
    }

    @Step("Attach file with Related Issues to report")
    public void attachRelatedIssuesFile() {
        attachFileToReport(getRelatedIssueExportFile());
    }

    @Step("Check if exported file with Related Issues is not empty")
    public boolean isRelatedIssuesFileNotEmpty() {
        return checkIfFileIsNotEmpty(getRelatedIssueExportFile());
    }

    protected OldTable getRelatedIssuesTable() {
        return OldTable.createById(driver, wait, getTableId());
    }

    public abstract String getTableId();

    public abstract String getLinkActionId();

    public abstract String getUnlinkActionId();

    public abstract String getRelatedIssueExportFile();

    public abstract String getLinkIssuePromptId();

    public abstract String getIdAttribute();
}
