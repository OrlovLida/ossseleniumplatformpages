package com.oss.pages.servicedesk;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.HtmlEditor;
import com.oss.framework.components.layout.Card;
import com.oss.framework.components.mainheader.ToolbarWidget;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.pages.servicedesk.issue.IssueDetailsPage;
import com.oss.untils.FileDownload;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.issue.IssueDetailsPage.DETAILS_PAGE_URL_PATTERN;

public abstract class BaseSDPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BaseSDPage.class);

    private static final String CREATE_DATE_FILTER_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyMMddHHmmSS";
    public static final DateTimeFormatter CREATE_DATE_FILTER_DATE_FORMATTER = DateTimeFormatter.ofPattern(CREATE_DATE_FILTER_DATE_PATTERN);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final String GROUP_ID_OLD_ACTIONS_CONTAINER = "frameworkCustomEllipsis";
    private static final String EXPORT_BUTTON_ID = "EXPORT";

    protected BaseSDPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void openPage(WebDriver driver, String url) {
        WebDriverWait wait = new WebDriverWait(driver, 150);
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
        String startDate = LocalDateTime.now().minusMinutes(minutes).format(CREATE_DATE_FILTER_DATE_FORMATTER);
        String endDate = LocalDateTime.now().format(CREATE_DATE_FILTER_DATE_FORMATTER);
        return startDate + " - " + endDate;
    }

    public static String getDateFormat() {
        return LocalDateTime.now().format(DATE_FORMATTER);
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

    public void clickExportFromTable(String tableId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable.createById(driver, wait, tableId).callAction(GROUP_ID_OLD_ACTIONS_CONTAINER, EXPORT_BUTTON_ID);

        log.info("Exporting file");
    }

    public void clickExportFromTab(String tabContainerId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsWidget.createById(driver, wait, tabContainerId).callActionById(GROUP_ID_OLD_ACTIONS_CONTAINER, EXPORT_BUTTON_ID);

        log.info("Exporting file");
    }

    public String getViewTitle() {
        return ToolbarWidget.create(driver, wait).getViewTitle();
    }

    public boolean isIssueTypeTicket() {
        return getViewTitle().contains("Ticket");
    }
}