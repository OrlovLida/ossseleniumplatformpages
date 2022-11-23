package com.oss.iaa.servicedesk.CSDI;

import java.time.LocalDateTime;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.infomanagement.MessagesPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.iaa.servicedesk.BaseSDPage.DATE_TIME_FORMATTER;

public class InfoManagementMessagesTest extends BaseTestCase {

    private static final String E_MAIL_MESSAGE = "Selenium test Message in Messages page";
    private static final String E_MAIL_MESSAGE_REPLY = "Selenium test Reply Message in Messages page";
    private static final String DATE_ID_IN_FILTER_PANEL = "receivedDate";
    private static final String MESSAGE_SUBJECT = "Email notification test";
    private static final String MESSAGE_TYPE_COMBOBOX_ID = "messageType";
    private static final String MESSAGE_TYPE = "EMAIL";
    private static final String FROM_ID = "from";
    private static final String TO_ID = "to";
    private static final String EXPORT_FILE_NAME = "*.csv";
    private static final String E_MAIL_STATUS = "Sent";
    private MessagesPage messagesPage;
    private SDWizardPage sdWizardPage;

    @BeforeMethod
    public void goToMessagesPage() {
        messagesPage = new MessagesPage(driver, webDriverWait).openView(driver, BASIC_URL);
    }

    @Parameters({"emailTo", "emailFrom"})
    @Test(priority = 1, testName = "Create Message", description = "Create New Message")
    @Description("Create New Message")
    public void createMessage(
            @Optional("kinga.balcar-mazur@comarch.com") String emailTo,
            @Optional("csdi-demo@comarch.com") String emailFrom
    ) {
        sdWizardPage = messagesPage.clickCreateMessage();
        sdWizardPage.createEmailNotification(emailTo, emailFrom, E_MAIL_MESSAGE);
        String startDate = LocalDateTime.now().minusMinutes(5).format(DATE_TIME_FORMATTER);
        String endDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String date = startDate + " - " + endDate;
        messagesPage.filterByDate(DATE_ID_IN_FILTER_PANEL, date);

        Assert.assertFalse(messagesPage.isMessagesTableEmpty());

        messagesPage.searchFullText(MESSAGE_SUBJECT);
        messagesPage.selectFirstMessage();
        Assert.assertEquals(messagesPage.getNotificationPreview().getMessageText(), E_MAIL_MESSAGE);
        Assert.assertEquals(messagesPage.getNotificationPreview().getMessageChannel(), "E-mail");
    }

    @Test(priority = 2, testName = "Check Message Status", description = "Check if Message was send")
    @Description("Check if Message was send")
    public void checkMessageStatus() {
        messagesPage.searchFullText(MESSAGE_SUBJECT);
        Assert.assertEquals(messagesPage.getMessageStatus(), E_MAIL_STATUS);
    }

    @Test(priority = 3, testName = "Check Search", description = "Check Search ")
    @Description("Check Search")
    public void checkSearch(
    ) {
        messagesPage.searchFullText(MESSAGE_SUBJECT);
        Assert.assertEquals(messagesPage.getMessagePreview(), MESSAGE_SUBJECT);
    }

    @Parameters({"emailTo", "emailFrom"})
    @Test(priority = 4, testName = "Check Filter", description = "Check Filter")
    @Description("Check Filter")
    public void checkFilter(
            @Optional("kinga.balcar-mazur@comarch.com") String emailTo,
            @Optional("csdi-demo@comarch.com") String emailFrom
    ) {
        messagesPage.filterBy(MESSAGE_TYPE_COMBOBOX_ID, MESSAGE_TYPE);
        messagesPage.filterBy(FROM_ID, emailFrom);
        messagesPage.filterBy(TO_ID, emailTo);

        Assert.assertEquals(messagesPage.getMessageType(), MESSAGE_TYPE);
        Assert.assertEquals(messagesPage.getMessageFrom(), emailFrom);
        Assert.assertEquals(messagesPage.getMessageTo(), emailTo);
    }

    @Test(priority = 5, testName = "Export", description = "Export from Message Search")
    @Description("Export from Message Search")
    public void export() {
        messagesPage.searchFullText(MESSAGE_SUBJECT);
        messagesPage.clickExport(EXPORT_FILE_NAME);

        Assert.assertEquals(messagesPage.openNotificationPanel().amountOfNotifications(), 0);
        Assert.assertTrue(messagesPage.checkIfFileIsNotEmpty(EXPORT_FILE_NAME));
    }

    @Test(priority = 6, testName = "Check read/unread functionality", description = "Check read/unread functionality")
    @Description("Check read/unread functionality")
    public void checkReadUnread() {
        messagesPage.searchFullText(MESSAGE_SUBJECT);
        messagesPage.selectFirstMessage();
        if (messagesPage.isFontBold() && messagesPage.getNotificationPreview().isNewBadgeVisible()) {
            messagesPage.clickMarkAsRead();

            Assert.assertFalse(messagesPage.isFontBold());
            Assert.assertFalse(messagesPage.getNotificationPreview().isNewBadgeVisible());

            messagesPage.clickMarkAsUnread();
            Assert.assertTrue(messagesPage.isFontBold());
            Assert.assertTrue(messagesPage.getNotificationPreview().isNewBadgeVisible());
        } else {
            Assert.fail("First message was read");
        }
    }

    @Parameters({"emailTo", "emailFrom"})
    @Test(priority = 7, testName = "Reply Message", description = "Reply to the Received Message")
    @Description("Reply to the Received Message")
    public void replyMessage(
            @Optional("magdalena.kaczmarska@comarch.com") String emailTo,
            @Optional("csdi-demo@comarch.com") String emailFrom
    ) {
        messagesPage.searchFullText(MESSAGE_SUBJECT);
        messagesPage.selectFirstMessage();
        sdWizardPage = messagesPage.getNotificationPreview().clickReply();
        sdWizardPage.createEmailNotification(emailTo, emailFrom, E_MAIL_MESSAGE_REPLY);

        String startDate = LocalDateTime.now().minusMinutes(5).format(DATE_TIME_FORMATTER);
        String endDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String date = startDate + " - " + endDate;

        messagesPage.filterByDate(DATE_ID_IN_FILTER_PANEL, date);
        messagesPage.filterBy(TO_ID, emailTo);

        Assert.assertFalse(messagesPage.isMessagesTableEmpty());

        messagesPage.searchFullText(MESSAGE_SUBJECT);
        messagesPage.selectFirstMessage();

        Assert.assertEquals(messagesPage.getMessageTo(), emailTo);
        Assert.assertEquals(messagesPage.getMessageStatus(), E_MAIL_STATUS);
    }

}
