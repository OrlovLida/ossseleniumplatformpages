package com.oss.iaa.servicedesk.infomanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.infomanagement.ConfigurationPanelPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

public class ConfigurationPanelTest extends BaseTestCase {

    private ConfigurationPanelPage configurationPanelPage;
    private SDWizardPage sdWizardPage;

    private static final String WIZARD_NAME_FIELD_ID = "mailbox-wizard-name";
    private static final String MAILBOX_NAME = "Test Mailbox";
    private static final String EDITED_MAILBOX_EMAIL = "editedemail@test.pl";
    private static final String WIZARD_EMAIL_FIELD_ID = "mailbox-wizard-email";
    private static final String EMAIL = "seleniumtest@test.pl";
    private static final String WIZARD_INCOMING_PREFIX = "mailbox-wizardin-";
    private static final String WIZARD_OUTGOING_PREFIX = "mailbox-wizardout-";
    private static final String PROTOCOL_COMBOBOX_ID = "protocol";
    private static final String PROTOCOL_IN = "IMAP";
    private static final String PROTOCOL_OUT = "SMTP";
    private static final String WIZARD_HOST_FIELD_ID = "host";
    private static final String HOST = "test";
    private static final String WIZARD_PORT_FIELD_ID = "port";
    private static final String PORT = "404";
    private static final String WIZARD_USERNAME_FIELD_ID = "user";
    private static final String USERNAME = "test";
    private static final String WIZARD_PASSWORD_FIELD_ID = "pass";
    private static final String PASSWORD = "test";
    private static final String WIZARD_INBOX_FOLDER_FIELD_ID = "mailbox-wizardin-folder";
    private static final String INBOX_FOLDER = "INBOX";

    @BeforeMethod
    public void goToConfigurationPanelPage() {
        configurationPanelPage = new ConfigurationPanelPage(driver, webDriverWait).goToConfigurationPanelPage(BASIC_URL);
    }

    @Test(priority = 1, testName = "Create New Configuration", description = "Create New Configuration")
    @Description("Create New Configuration")
    public void createNewConfiguration() {
        sdWizardPage = configurationPanelPage.clickCreateNewConfiguration();
        sdWizardPage.insertValueToComponent(MAILBOX_NAME, WIZARD_NAME_FIELD_ID);
        sdWizardPage.insertValueToComponent(EMAIL, WIZARD_EMAIL_FIELD_ID);
        sdWizardPage.turnOnSwitcher("mailbox-wizard-active");
        sdWizardPage.insertValueToComponent(PROTOCOL_IN, WIZARD_INCOMING_PREFIX + PROTOCOL_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(HOST, WIZARD_INCOMING_PREFIX + WIZARD_HOST_FIELD_ID);
        sdWizardPage.insertValueToComponent(PORT, WIZARD_INCOMING_PREFIX + WIZARD_PORT_FIELD_ID);
        sdWizardPage.insertValueToComponent(USERNAME, WIZARD_INCOMING_PREFIX + WIZARD_USERNAME_FIELD_ID);
        sdWizardPage.insertValueToComponent(PASSWORD, WIZARD_INCOMING_PREFIX + WIZARD_PASSWORD_FIELD_ID);
        sdWizardPage.insertValueToComponent(INBOX_FOLDER, WIZARD_INBOX_FOLDER_FIELD_ID);

        sdWizardPage.insertValueToComponent(PROTOCOL_OUT, WIZARD_OUTGOING_PREFIX + PROTOCOL_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(HOST, WIZARD_OUTGOING_PREFIX + WIZARD_HOST_FIELD_ID);
        sdWizardPage.insertValueToComponent(PORT, WIZARD_OUTGOING_PREFIX + WIZARD_PORT_FIELD_ID);
        sdWizardPage.insertValueToComponent(USERNAME, WIZARD_OUTGOING_PREFIX + WIZARD_USERNAME_FIELD_ID);
        sdWizardPage.insertValueToComponent(PASSWORD, WIZARD_OUTGOING_PREFIX + WIZARD_PASSWORD_FIELD_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(configurationPanelPage.isMailboxesTableEmpty(), "Mailboxes list is empty");
        Assert.assertTrue(configurationPanelPage.isObjectInTable(MAILBOX_NAME));
    }

    @Test(priority = 2, testName = "Edit Mailbox", description = "Edit Mailbox")
    @Description("Edit Mailbox")
    public void editMailbox() {
        Assert.assertFalse(configurationPanelPage.isMailboxesTableEmpty());
        if (configurationPanelPage.isObjectInTable(MAILBOX_NAME)) {
            sdWizardPage = configurationPanelPage.clickEditOnObjectWithName(MAILBOX_NAME);
            sdWizardPage.insertValueToComponent(EDITED_MAILBOX_EMAIL, WIZARD_EMAIL_FIELD_ID);
            sdWizardPage.insertValueToComponent(PASSWORD, WIZARD_INCOMING_PREFIX + WIZARD_PASSWORD_FIELD_ID);
            sdWizardPage.insertValueToComponent(PASSWORD, WIZARD_OUTGOING_PREFIX + WIZARD_PASSWORD_FIELD_ID);
            sdWizardPage.clickAcceptButtonInWizard();
        } else {
            Assert.fail("Mailbox with name: " + MAILBOX_NAME + " is not in the Table");
        }

        Assert.assertFalse(configurationPanelPage.isMailboxesTableEmpty(), "Mailboxes list is empty");
        Assert.assertEquals(configurationPanelPage.getEmailOfObject(MAILBOX_NAME), EDITED_MAILBOX_EMAIL);
    }

    @Test(priority = 3, testName = "Disable Mailbox", description = "Disable Mailbox")
    @Description("Disable Mailbox")
    public void disableMailbox() {
        Assert.assertFalse(configurationPanelPage.isMailboxesTableEmpty());
        if (configurationPanelPage.isObjectInTable(MAILBOX_NAME)) {
            configurationPanelPage.clickDisableOnObjectWithName(MAILBOX_NAME);
            configurationPanelPage.clickConfirmDisable();
        } else {
            Assert.fail("Mailbox with name: " + MAILBOX_NAME + " is not in the Table");
        }

        Assert.assertFalse(configurationPanelPage.isMailboxesTableEmpty(), "Mailboxes list is empty");
        Assert.assertTrue(configurationPanelPage.isObjectInTable(MAILBOX_NAME));
        Assert.assertEquals(configurationPanelPage.getStatusOfObject(MAILBOX_NAME), "Disabled");
    }

    @Test(priority = 4, testName = "Delete Mailbox", description = "Delete Mailbox")
    @Description("Delete Mailbox")
    public void deleteMailbox() {
        Assert.assertFalse(configurationPanelPage.isMailboxesTableEmpty());
        if (configurationPanelPage.isObjectInTable(MAILBOX_NAME)) {
            configurationPanelPage.clickDeleteOnObjectWithName(MAILBOX_NAME);
            configurationPanelPage.clickConfirmDelete();
        } else {
            Assert.fail("Mailbox with name: " + MAILBOX_NAME + " is not in the Table");
        }

        Assert.assertFalse(configurationPanelPage.isObjectInTable(MAILBOX_NAME));
    }
}
