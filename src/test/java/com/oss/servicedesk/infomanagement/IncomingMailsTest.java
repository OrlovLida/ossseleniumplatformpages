package com.oss.servicedesk.infomanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.infomanagement.MessagesPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

public class IncomingMailsTest extends BaseTestCase {
    private MessagesPage messagesPage;
    private SDWizardPage sdWizardPage;

    private static final String DATE_ID_IN_FILTER_PANEL = "receivedDate";
    private static final String MESSAGE_STATUS_COMBOBOX_ID = "notificationStatus";
    private static final String MESSAGE_STATUS = "Manual Processing";

    @BeforeMethod
    @Parameters({"MessageSubject"})
    public void goToMessagesPage(
            @Optional("SeleniumTest") String messageSubject
    ) {
        messagesPage = new MessagesPage(driver, webDriverWait).openView(driver, BASIC_URL);
        messagesPage.searchFullText(messageSubject);
        messagesPage.filterBy(MESSAGE_STATUS_COMBOBOX_ID, MESSAGE_STATUS);

        if (messagesPage.isMessagesTableEmpty()) {
            Assert.fail("No emails with Manual Processing Status");
        }
    }

    @Parameters({"TicketID", "MessageSubject"})
    @Test(priority = 1, testName = "Assign to object", description = "Assign to object")
    @Description("Assign to object")
    public void assignToObject(
            @Optional("30") String ticketID,
            @Optional("SeleniumTest") String messageSubject
    ) {
        messagesPage.selectFirstMessage();
        String date = messagesPage.getMessageDate();
        sdWizardPage = messagesPage.clickAssignToObject();
        sdWizardPage.insertValueToComponent("TroubleTicket", "context-object-wizard-components-context-type");
        sdWizardPage.insertValueToComponent(ticketID, "context-object-wizard-components-context-object");
        sdWizardPage.clickAcceptButtonInWizard();
        messagesPage.clearAllFilters();

        messagesPage.filterByDate(DATE_ID_IN_FILTER_PANEL, date + " - " + date);
        messagesPage.searchFullText(messageSubject);

        Assert.assertFalse(messagesPage.isMessagesTableEmpty());

        Assert.assertEquals(messagesPage.getMessageStatus(), "Received");
        Assert.assertEquals(messagesPage.getMessageContext(), "TroubleTicket #" + ticketID);
    }
}
