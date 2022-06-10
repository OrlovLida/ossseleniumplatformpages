package com.oss.servicedesk.infomanagement;

import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.infomanagement.MessagesPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.BaseSDPage.DATE_TIME_FORMATTER;

public class MessagesTest extends BaseTestCase {

    private MessagesPage messagesPage;
    private SDWizardPage sdWizardPage;

    private static final String E_MAIL_MESSAGE = "Selenium test Message in Messages page";
    private static final String DATE_ID_IN_FILTER_PANEL = "receivedDate";
    private static final String MESSAGE_SUBJECT = "Email notification test";
    private static final String MESSAGE_TYPE_COMBOBOX_ID = "messageType";
    private static final String MESSAGE_TYPE = "EMAIL";
    private static final String FROM_ID = "from";
    private static final String TO_ID = "to";
    private static final String EXPORT_FILE_NAME = "*.csv";
    private static final String EXPORT_HTML_ZIP = "*.zip";

    @BeforeMethod
    public void goToMessagesPage() {
        messagesPage = new MessagesPage(driver, webDriverWait).openView(driver, BASIC_URL);
    }

    @Parameters({"emailTo", "emailFrom"})
    @Test(priority = 1, testName = "Create Message", description = "Create New Message")
    @Description("Create New Message")
    public void createMessage(
            @Optional("kornelia.odrobinska@comarch.com") String emailTo,
            @Optional("switch.ticket@comarch.com") String emailFrom
    ) {
        sdWizardPage = messagesPage.clickCreateMessage();
        sdWizardPage.createEmailNotification(emailTo, emailFrom, E_MAIL_MESSAGE);
        String startDate = LocalDateTime.now().minusMinutes(5).format(DATE_TIME_FORMATTER);
        String endDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String date = startDate + " - " + endDate;
        messagesPage.filterByTextField(DATE_ID_IN_FILTER_PANEL, date);

        Assert.assertFalse(messagesPage.isMessagesTableEmpty());

        messagesPage.searchFullText(MESSAGE_SUBJECT);
        messagesPage.selectFirstMessage();
        Assert.assertEquals(messagesPage.getNotificationPreview().getMessageText(), E_MAIL_MESSAGE);
        Assert.assertEquals(messagesPage.getNotificationPreview().getMessageChannel(), "E-mail");
    }

    @Parameters({"emailTo", "emailFrom"})
    @Test(priority = 2, testName = "Search and Filter", description = "Search and Filter")
    @Description("Search and Filter")
    public void SearchAndFilter(
            @Optional("kornelia.odrobinska@comarch.com") String emailTo,
            @Optional("switch.ticket@comarch.com") String emailFrom
    ) {
        messagesPage.searchFullText(MESSAGE_SUBJECT);
        messagesPage.filterByComboBox(MESSAGE_TYPE_COMBOBOX_ID, MESSAGE_TYPE);
        messagesPage.filterByTextField(FROM_ID, emailFrom);
        messagesPage.filterByTextField(TO_ID, emailTo);

        Assert.assertEquals(messagesPage.getMessageType(), MESSAGE_TYPE);
        Assert.assertEquals(messagesPage.getMessageFrom(), emailFrom);
        Assert.assertEquals(messagesPage.getMessageTo(), emailTo);
        Assert.assertEquals(messagesPage.getMessagePreview(), MESSAGE_SUBJECT);

        messagesPage.selectFirstMessage();
        Assert.assertEquals(messagesPage.getNotificationPreview().getStatusLabel(), "SUCCESS");
    }

    @Test(priority = 3, testName = "Check read/unread functionality", description = "Check read/unread functionality")
    @Description("Check read/unread functionality")
    public void checkReadUnread() {
        messagesPage.searchFullText(MESSAGE_SUBJECT);
        messagesPage.selectFirstMessage();
        if (!messagesPage.isFontBold() && !messagesPage.getNotificationPreview().isNewBadgeVisible()) {
            messagesPage.clickMarkAsUnread();
            Assert.assertTrue(messagesPage.isFontBold());
            Assert.assertTrue(messagesPage.getNotificationPreview().isNewBadgeVisible());

            messagesPage.clickMarkAsRead();
            Assert.assertFalse(messagesPage.isFontBold());
            Assert.assertFalse(messagesPage.getNotificationPreview().isNewBadgeVisible());
        } else {
            Assert.fail("First message was not read");
        }
    }

    @Test(priority = 4, testName = "Export", description = "Export from Message Search")
    @Description("Export from Message Search")
    public void export() {
        messagesPage.clickExport(EXPORT_FILE_NAME);

        Assert.assertEquals(messagesPage.openNotificationPanel().amountOfNotifications(), 0);
        Assert.assertTrue(messagesPage.checkIfFileIsNotEmpty(EXPORT_FILE_NAME));
    }

    @Test(priority = 5, testName = "Export as HTML", description = "Export as HTML")
    @Description("Export as HTML")
    public void exportAsHTML() {
        messagesPage.selectFirstMessage();
        messagesPage.openNotificationPanel().clearNotifications().close();
        messagesPage.clickExportAsHTML();
        messagesPage.openNotificationPanel().waitForExportFinish().clickDownload();
        messagesPage.openNotificationPanel().clearNotifications();
        messagesPage.attachFileToReport(EXPORT_HTML_ZIP);

        Assert.assertEquals(messagesPage.openNotificationPanel().amountOfNotifications(), 0);
        Assert.assertTrue(messagesPage.checkIfFileIsNotEmpty(EXPORT_HTML_ZIP));
    }
}
