package com.oss.pages.iaa.servicedesk;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.HtmlEditor;
import com.oss.framework.components.layout.Card;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage;
import com.oss.untils.FileDownload;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DATE_PATTERN;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DATE_PATTERN_WITHOUT_SPACE;
import static com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage.DETAILS_PAGE_URL_PATTERN;

public abstract class BaseSDPage extends BasePage {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN_WITHOUT_SPACE);
    protected static final Logger log = LoggerFactory.getLogger(BaseSDPage.class);
    private static final String GROUP_ID_OLD_ACTIONS_CONTAINER = "frameworkCustomEllipsis";
    private static final String EXPORT_BUTTON_ID = "EXPORT";

    protected BaseSDPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static String getDateFormat() {
        return LocalDateTime.now().format(DATE_FORMATTER);
    }

    public void openPage(WebDriver driver, String url) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(150));
        driver.get(url);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opening page: {}", url);
    }

    @Step("Set value in HTML Editor")
    public void setValueInHtmlEditor(String value, String componentId) {
        HtmlEditor htmlEditor = HtmlEditor.create(driver, wait, componentId);
        htmlEditor.clear();
        htmlEditor.setSingleStringValue(value);
    }

    public String getTimePeriodForLastNMinutes(int minutes) {
        String startDate = LocalDateTime.now().minusMinutes(minutes).format(DATE_TIME_FORMATTER);
        String endDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        return startDate + " - " + endDate;
    }

    @Step("Attach downloaded file to report")
    public void attachFileToReport(String fileName) {
        FileDownload.attachDownloadedFileToReport(fileName);
        log.info("Attaching downloaded file to report");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public boolean checkIfFileIsNotEmpty(String fileName) {
        log.info("Checking if file is not empty");
        return FileDownload.checkIfFileIsNotEmpty(fileName);
    }

    @Step("Open Details View")
    public IssueDetailsPage openIssueDetailsView(String issueId, String basicURL, String issueType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opening issue details with id: {}", issueId);
        driver.get(String.format(DETAILS_PAGE_URL_PATTERN, basicURL, issueType, issueId));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new IssueDetailsPage(driver, wait);
    }

    @Step("Maximize window")
    public void maximizeWindow(String windowId) {
        Card.createCard(driver, wait, windowId).maximizeCard();
        log.info("Maximizing window");
    }

    @Step("Minimize window")
    public void minimizeWindow(String windowId) {
        Card.createCard(driver, wait, windowId).minimizeCard();
        log.info("Minimizing window");
    }

    @Step("Click Context action from button with label {contextActionLabel}")
    public void clickContextActionFromButtonContainer(String contextActionLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ButtonContainer.create(driver, wait).callActionById(contextActionLabel);
        log.info("Clicking Context action {}", contextActionLabel);
    }

    @Step("Check if Context action {contextActionID} is present")
    public boolean isActionPresent(String contextActionID) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Checking if Context action {} is present", contextActionID);
        return ButtonContainer.create(driver, wait).isElementPresent(contextActionID);
    }

    public void clickExportFromTable(String tableId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable.createById(driver, wait, tableId).callAction(GROUP_ID_OLD_ACTIONS_CONTAINER, EXPORT_BUTTON_ID);

        log.info("Exporting file");
    }

    public void exportFromTable(String tableId, String fileName) {
        openNotificationPanel()
                .clearNotifications()
                .close();
        clickExportFromTable(tableId);
        openNotificationPanel()
                .waitForExportFinish()
                .clickDownload();
        openNotificationPanel()
                .clearNotifications();
        attachFileToReport(fileName);
    }

    public void clickExportFromTab(String tabContainerId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsWidget.createById(driver, wait, tabContainerId).callActionById(GROUP_ID_OLD_ACTIONS_CONTAINER, EXPORT_BUTTON_ID);

        log.info("Exporting file");
    }

    public String getMessageFromPrompt() {
        return SystemMessageContainer.create(driver, wait)
                .getFirstMessage()
                .map(SystemMessageContainer.Message::getText)
                .orElse(null);
    }

    public String getIdFromMessage() {
        if (!getMessageFromPrompt().isEmpty()) {
            String[] splitMessage = getMessageFromPrompt().split(" ");
            log.info("id is: {}", splitMessage[1]);
            return splitMessage[1];
        }
        return "No message is shown";
    }

    public void closeMessagePrompt() {
        SystemMessageContainer.create(driver, wait).close();
    }
}