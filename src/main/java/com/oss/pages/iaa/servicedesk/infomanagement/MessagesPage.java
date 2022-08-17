package com.oss.pages.iaa.servicedesk.infomanagement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;
import com.oss.pages.iaa.servicedesk.BaseSearchPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

public class MessagesPage extends BaseSearchPage {

    private static final String INFO_MANAGEMENT_MESSAGES_URL_PATTERN = "%s/#/views/info-management/notifications";
    private static final String TABLE_ID = "table";
    private static final String CREATE_NEW_MESSAGE_ID = "notification-new-message-action-NONE";
    private static final String CREATE_MESSAGE_WIZARD_ID = "notification-wizard_prompt-card";
    private static final String COLUMN_TYPE_ID = "messageType";
    private static final String COLUMN_TO_ID = "to";
    private static final String COLUMN_FROM_ID = "from";
    private static final String COLUMN_PREVIEW_ID = "preview";
    private static final String COLUMN_CONTEXT_ID = "context";
    private static final String COLUMN_DATE_ID = "receivedDate";
    private static final String COLUMN_STATUS_ID = "notificationStatus";
    private static final String EXPORT_AS_HTML_BUTTON_ID = "notification-export-action";
    private static final String MARK_AS_READ_BUTTON_ID = "notification-mark-read-action";
    private static final String MARK_AS_UNREAD_BUTTON_ID = "notification-mark-unread-action";
    private static final String ASSIGN_TO_OBJECT_BUTTON_ID = "notification-relink-action";
    private static final String ASSIGN_TO_OBJECT_WIZARD_ID = "card-content_notification-relink-wizard-view_prompt-card";

    public MessagesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Click Create New Message")
    public SDWizardPage clickCreateMessage() {
        getIssueTable().callAction(CREATE_NEW_MESSAGE_ID);
        log.info("Clicking Create New Message");
        return new SDWizardPage(driver, wait, CREATE_MESSAGE_WIZARD_ID);
    }

    @Step("Check if messages table is empty")
    public boolean isMessagesTableEmpty() {
        return getIssueTable().hasNoData();
    }

    @Step("Search fullText: {fullText}")
    public void searchFullText(String fullText) {
        getIssueTable().fullTextSearch(fullText);
        log.info("Searching text: {}", fullText);
    }

    @Step("Get Message type from first row")
    public String getMessageType() {
        return getIssueTable().getCellValue(0, COLUMN_TYPE_ID);
    }

    @Step("Get Message preview from first row")
    public String getMessagePreview() {
        return getIssueTable().getCellValue(0, COLUMN_PREVIEW_ID);
    }

    @Step("Get Message from - from first fow")
    public String getMessageFrom() {
        return getIssueTable().getCellValue(0, COLUMN_FROM_ID);
    }

    @Step("Get Message to from first fow")
    public String getMessageTo() {
        return getIssueTable().getCellValue(0, COLUMN_TO_ID);
    }

    @Step("Get Message Context from first row")
    public String getMessageContext() {
        return getIssueTable().getCellValue(0, COLUMN_CONTEXT_ID);
    }

    @Step("Get Message Date from first row")
    public String getMessageDate() {
        return getIssueTable().getCellValue(0, COLUMN_DATE_ID);
    }

    @Step("Get Message Status from first row")
    public String getMessageStatus() {
        return getIssueTable().getCellValue(0, COLUMN_STATUS_ID);
    }

    @Step("Click Export")
    public void clickExport(String fileName) {
        exportFromTable(getTableId(), fileName);
    }

    @Step("Select first Message on the list")
    public void selectFirstMessage() {
        getIssueTable().selectRow(0);
        log.info("Selecting first Message on the list");
    }

    @Step("Click Export as HTML")
    public void clickExportAsHTML() {
        clickTableButton(EXPORT_AS_HTML_BUTTON_ID);
        log.info("Clicking Export as HTML Button");
    }

    @Step("Click Mark as read button")
    public void clickMarkAsRead() {
        clickTableButton(MARK_AS_READ_BUTTON_ID);
        log.info("Clicking Mark as read button");
    }

    @Step("Click Mark as unread")
    public void clickMarkAsUnread() {
        clickTableButton(MARK_AS_UNREAD_BUTTON_ID);
        log.info("Clicking Mark as unread button");
    }

    @Step("Click Assign to Object button")
    public SDWizardPage clickAssignToObject() {
        clickTableButton(ASSIGN_TO_OBJECT_BUTTON_ID);
        log.info("Clicking Assign to Object button");
        return new SDWizardPage(driver, wait, ASSIGN_TO_OBJECT_WIZARD_ID);
    }

    public NotificationPreviewPage getNotificationPreview() {
        return new NotificationPreviewPage(driver, wait);
    }

    @Step("Check if message is unread - bold font")
    public boolean isFontBold() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getIssueTable().isCellValueBold(0, COLUMN_PREVIEW_ID);
    }

    private void clickTableButton(String buttonId) {
        getIssueTable().callAction(buttonId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Override
    @Step("Open Messages Page")
    public MessagesPage openView(WebDriver driver, String basicURL) {
        openPage(driver, String.format(INFO_MANAGEMENT_MESSAGES_URL_PATTERN, basicURL));
        return this;
    }

    @Override
    public String getSearchPageUrl() {
        return INFO_MANAGEMENT_MESSAGES_URL_PATTERN;
    }

    @Override
    public String getIssueType() {
        return null;
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }
}
