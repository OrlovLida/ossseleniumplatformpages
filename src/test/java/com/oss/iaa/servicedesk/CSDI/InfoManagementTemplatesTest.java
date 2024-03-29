package com.oss.iaa.servicedesk.CSDI;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.servicedesk.infomanagement.TemplatesPage;
import com.oss.pages.iaa.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class InfoManagementTemplatesTest extends BaseTestCase {

    private static final String WIZARD_NAME_FIELD_ID = "template-wizard-name";
    private static final String HEADER_NAME = "Selenium Test Header Name";
    private static final String EDITED_HEADER_NAME = "Selenium Test Header Name Edited";
    private static final String FOOTER_NAME = "Selenium Test Footer Name";
    private static final String EDITED_FOOTER_NAME = "Selenium Test Footer Name Edited";
    private static final String SYSTEM_COMBOBOX_ID = "template-wizard-system";
    private static final String SYSTEM = "Service Desk";
    private static final String OBJECT_TYPE_COMBOBOX_ID = "template-wizard-context-type";
    private static final String OBJECT_TYPE_TT = "TroubleTicket";
    private static final String TEMPLATE_TEXT = "Test Selenium Header";
    private static final String TEMPLATE_HTML_EDITOR_ID = "template-wizard-content";
    private static final String FOOTER_TYPE = "FOOTER";
    private static final String HEADER_TYPE = "HEADER";
    private static final String TEMPLATE_TYPE = "TEMPLATE";
    private static final String TEMPLATE_NAME = "Selenium Test Template";
    private static final String TEMPLATE_TO_SHARE_NAME = "Selenium Test Template to Share";
    private static final String TEMPLATE_WITH_HEADER_AND_FOOTER_NAME = "Selenium Template with Header and Footer";
    private static final String EDITED_TEMPLATE_NAME = "Selenium Test Template Edited";
    private static final String CHANNEL_COMBOBOX_ID = "template-wizard-channel";
    private static final String CHANNEL_INTERNAL = "Internal";
    private static final String CHANNEL_EMAIL = "E-mail";
    private static final String TYPE_SUCCESS = "Success";
    private static final String TYPE_COMBOBOX_ID = "template-wizard-internal-type";
    private static final String WIZARD_HEADER_COMBOBOX_ID = "template-wizard-header";
    private static final String WIZARD_FOOTER_COMBOBOX_ID = "template-wizard-footer";
    private static final String CHANNEL_EMAIL_IN_SEARCH_PANEL = "EMAIL";
    private static final String NAME_LABEL = "Name";
    private static final String TYPE_LABEL = "Type";
    private static final String TEMPLATES_EXPRESSION_EDITOR_ID = "template-wizard-content";
    private static final String CANCEL_BUTTON_IN_TEMPLATE_WIZARD_ID = "wizard-cancel-button-template-wizard-wizard";
    private TemplatesPage templatesPage;
    private SDWizardPage sdWizardPage;

    @BeforeMethod
    public void goToTemplatesPage() {
        templatesPage = new TemplatesPage(driver, webDriverWait).goToTemplatesPage(BASIC_URL);
    }

    @Test(priority = 1, testName = "Create Header", description = "Create Header")
    @Description("Create Header")
    public void createHeader() {
        sdWizardPage = templatesPage.clickCreateHeader();
        sdWizardPage.insertValueToComponent(SYSTEM, SYSTEM_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(HEADER_NAME, WIZARD_NAME_FIELD_ID);
        sdWizardPage.insertValueToComponent(OBJECT_TYPE_TT, OBJECT_TYPE_COMBOBOX_ID);
        sdWizardPage.setValueInHtmlEditor(TEMPLATE_TEXT, TEMPLATE_HTML_EDITOR_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(templatesPage.isTemplatesTableEmpty(), "Template list is empty");
        Assert.assertTrue(templatesPage.isObjectInTable(HEADER_NAME));
        Assert.assertEquals(templatesPage.getTypeOfObject(HEADER_NAME), HEADER_TYPE);
    }

    @Test(priority = 2, testName = "Edit header", description = "Edit header")
    @Description("Edit header")
    public void editHeader() {
        Assert.assertFalse(templatesPage.isTemplatesTableEmpty());
        if (templatesPage.isObjectInTable(HEADER_NAME)) {
            sdWizardPage = templatesPage.clickEditOnObjectWithName(HEADER_NAME);
            sdWizardPage.insertValueToComponent(EDITED_HEADER_NAME, WIZARD_NAME_FIELD_ID);
            sdWizardPage.clickAcceptButtonInWizard();
        } else {
            Assert.fail("Header with name: " + HEADER_NAME + " is not in the Table");
        }

        Assert.assertFalse(templatesPage.isTemplatesTableEmpty(), "Template list is empty");
        Assert.assertTrue(templatesPage.isObjectInTable(EDITED_HEADER_NAME));
        Assert.assertEquals(templatesPage.getTypeOfObject(EDITED_HEADER_NAME), HEADER_TYPE);
    }

    @Test(priority = 3, testName = "Create Footer", description = "Create Footer")
    @Description("Create Footer")
    public void createFooter() {
        sdWizardPage = templatesPage.clickCreateFooter();
        sdWizardPage.insertValueToComponent(SYSTEM, SYSTEM_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(FOOTER_NAME, WIZARD_NAME_FIELD_ID);
        sdWizardPage.insertValueToComponent(OBJECT_TYPE_TT, OBJECT_TYPE_COMBOBOX_ID);
        sdWizardPage.setValueInHtmlEditor(TEMPLATE_TEXT, TEMPLATE_HTML_EDITOR_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(templatesPage.isTemplatesTableEmpty(), "Template list is empty");
        Assert.assertTrue(templatesPage.isObjectInTable(FOOTER_NAME));
        Assert.assertEquals(templatesPage.getTypeOfObject(FOOTER_NAME), FOOTER_TYPE);
    }

    @Test(priority = 4, testName = "Edit Footer", description = "Edit Footer")
    @Description("Edit Footer")
    public void EditFooter() {
        sdWizardPage = templatesPage.clickEditOnObjectWithName(FOOTER_NAME);
        sdWizardPage.insertValueToComponent(EDITED_FOOTER_NAME, WIZARD_NAME_FIELD_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(templatesPage.isTemplatesTableEmpty(), "Template list is empty");
        Assert.assertTrue(templatesPage.isObjectInTable(EDITED_FOOTER_NAME));
        Assert.assertEquals(templatesPage.getTypeOfObject(EDITED_FOOTER_NAME), FOOTER_TYPE);
    }

    @Test(priority = 5, testName = "Create Template with Header and Footer", description = "Create Template with Header and Footer")
    @Description("Create Template with Header and Footer")
    public void createTemplateWithHeaderAndFooter() {
        sdWizardPage = templatesPage.clickCreateTemplate();
        sdWizardPage.insertValueToComponent(TEMPLATE_WITH_HEADER_AND_FOOTER_NAME, WIZARD_NAME_FIELD_ID);
        sdWizardPage.insertValueToComponent(CHANNEL_EMAIL, CHANNEL_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(SYSTEM, SYSTEM_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(OBJECT_TYPE_TT, OBJECT_TYPE_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(EDITED_HEADER_NAME, WIZARD_HEADER_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(EDITED_FOOTER_NAME, WIZARD_FOOTER_COMBOBOX_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(templatesPage.isTemplatesTableEmpty(), "Template list is empty");
        Assert.assertTrue(templatesPage.isObjectInTable(TEMPLATE_WITH_HEADER_AND_FOOTER_NAME));
        Assert.assertEquals(templatesPage.getTypeOfObject(TEMPLATE_WITH_HEADER_AND_FOOTER_NAME), TEMPLATE_TYPE);
    }

    @Test(priority = 6, testName = "Delete Template with Header and Footer", description = "Delete Template with Header and Footer")
    @Description("Delete Template with Header and Footer")
    public void deleteTemplateWithHeaderAndFooter() {
        Assert.assertFalse(templatesPage.isTemplatesTableEmpty());
        if (templatesPage.isObjectInTable(TEMPLATE_WITH_HEADER_AND_FOOTER_NAME)) {
            templatesPage.clickDeleteOnObjectWithName(TEMPLATE_WITH_HEADER_AND_FOOTER_NAME);
            templatesPage.clickConfirmDelete();
        } else {
            Assert.fail("Header with name: " + TEMPLATE_WITH_HEADER_AND_FOOTER_NAME + " is not in the Table");
        }

        Assert.assertFalse(templatesPage.isObjectInTable(TEMPLATE_WITH_HEADER_AND_FOOTER_NAME));
    }

    @Test(priority = 7, testName = "Create Template", description = "Create Template")
    @Description("Create Template")
    public void createTemplate() {
        sdWizardPage = templatesPage.clickCreateTemplate();
        sdWizardPage.insertValueToComponent(TEMPLATE_NAME, WIZARD_NAME_FIELD_ID);
        sdWizardPage.insertValueToComponent(CHANNEL_INTERNAL, CHANNEL_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(SYSTEM, SYSTEM_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(OBJECT_TYPE_TT, OBJECT_TYPE_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(TYPE_SUCCESS, TYPE_COMBOBOX_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(templatesPage.isTemplatesTableEmpty(), "Template list is empty");
        Assert.assertTrue(templatesPage.isObjectInTable(TEMPLATE_NAME));
        Assert.assertEquals(templatesPage.getTypeOfObject(TEMPLATE_NAME), TEMPLATE_TYPE);
    }

    @Test(priority = 8, testName = "Edit Template", description = "Edit Template")
    @Description("Edit Template")
    public void editTemplate() {
        sdWizardPage = templatesPage.clickEditOnObjectWithName(TEMPLATE_NAME);
        sdWizardPage.insertValueToComponent(EDITED_TEMPLATE_NAME, WIZARD_NAME_FIELD_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(templatesPage.isTemplatesTableEmpty(), "Template list is empty");
        Assert.assertTrue(templatesPage.isObjectInTable(EDITED_TEMPLATE_NAME));
        Assert.assertEquals(templatesPage.getTypeOfObject(EDITED_TEMPLATE_NAME), TEMPLATE_TYPE);
    }

    @Test(priority = 9, testName = "Sort Template List", description = "Sort Template List by Created date and Name")
    @Description("Sort Template List by Created date and Name")
    public void sortTemplateList() {
        templatesPage.clickSortByDate();
        Assert.assertTrue(templatesPage.areDatesSorted(templatesPage.getCreationDateForRow(0), templatesPage.getCreationDateForRow(1)));

        templatesPage.clickSortByName();
        Assert.assertEquals(templatesPage.getNameOfObjectInRow(0), templatesPage.sortListByLabelAndGetFirstElement(NAME_LABEL));
    }

    @Test(priority = 10, testName = "Filter Templates", description = "Filter Templates in advanced Search and sort")
    @Description("Filter Templates in advanced Search and sort")
    public void filterTemplates() {
        templatesPage.filterByChannel(CHANNEL_EMAIL_IN_SEARCH_PANEL);
        Assert.assertEquals(templatesPage.getChannelForFirstRow(), CHANNEL_EMAIL_IN_SEARCH_PANEL);

        templatesPage.clickSortByType();
        Assert.assertEquals(templatesPage.getTypeForFirstRow(), templatesPage.sortListByLabelAndGetFirstElement(TYPE_LABEL));
    }

    @Test(priority = 11, testName = "Search edited footer name", description = "Search edited footer name in search box")
    @Description("Search edited footer name in search box")
    public void searchFullText() {
        templatesPage.searchForText(EDITED_FOOTER_NAME);

        Assert.assertEquals(templatesPage.getNameOfObjectInRow(0), EDITED_FOOTER_NAME);
    }

    @Test(priority = 12, testName = "Delete Template", description = "Delete Template")
    @Description("Delete Template")
    public void deleteTemplate() {
        Assert.assertFalse(templatesPage.isTemplatesTableEmpty());
        if (templatesPage.isObjectInTable(EDITED_TEMPLATE_NAME)) {
            templatesPage.clickDeleteOnObjectWithName(EDITED_TEMPLATE_NAME);
            templatesPage.clickConfirmDelete();
        } else {
            Assert.fail("Header with name: " + EDITED_TEMPLATE_NAME + " is not in the Table");
        }

        Assert.assertFalse(templatesPage.isObjectInTable(EDITED_TEMPLATE_NAME));
    }

    @Test(priority = 13, testName = "Delete Header", description = "Delete Header")
    @Description("Delete Header")
    public void deleteHeader() {
        Assert.assertFalse(templatesPage.isTemplatesTableEmpty());
        if (templatesPage.isObjectInTable(EDITED_HEADER_NAME)) {
            templatesPage.clickDeleteOnObjectWithName(EDITED_HEADER_NAME);
            templatesPage.clickConfirmDelete();
        } else {
            Assert.fail("Header with name: " + EDITED_HEADER_NAME + " is not in the Table");
        }

        Assert.assertFalse(templatesPage.isObjectInTable(EDITED_HEADER_NAME));
    }

    @Test(priority = 14, testName = "Delete Footer", description = "Delete Footer")
    @Description("Delete Footer")
    public void deleteFooter() {
        Assert.assertFalse(templatesPage.isTemplatesTableEmpty());
        if (templatesPage.isObjectInTable(EDITED_FOOTER_NAME)) {
            templatesPage.clickDeleteOnObjectWithName(EDITED_FOOTER_NAME);
            templatesPage.clickConfirmDelete();
        } else {
            Assert.fail("Header with name: " + EDITED_FOOTER_NAME + " is not in the Table");
        }
        Assert.assertFalse(templatesPage.isObjectInTable(EDITED_FOOTER_NAME));
    }

    @Parameters({"shareToUser", "password"})
    @Test(priority = 15, testName = "Share Template", description = "Share template to other user")
    @Description("Share template to other user")
    public void shareTemplate(
            @Optional("sd_seleniumtest2") String shareToUser,
            @Optional("oss") String password
    ) {
        sdWizardPage = templatesPage.clickCreateTemplate();
        sdWizardPage.insertValueToComponent(TEMPLATE_TO_SHARE_NAME, WIZARD_NAME_FIELD_ID);
        sdWizardPage.insertValueToComponent(CHANNEL_INTERNAL, CHANNEL_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(SYSTEM, SYSTEM_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(OBJECT_TYPE_TT, OBJECT_TYPE_COMBOBOX_ID);
        sdWizardPage.insertValueToComponent(TYPE_SUCCESS, TYPE_COMBOBOX_ID);
        sdWizardPage.clickAcceptButtonInWizard();
        Assert.assertTrue(templatesPage.isObjectInTable(TEMPLATE_TO_SHARE_NAME));

        templatesPage.shareObjectWithName(TEMPLATE_TO_SHARE_NAME);
        templatesPage.searchForUser(shareToUser);
        templatesPage.shareToUser(shareToUser);
        templatesPage.closeSharePanel();
        templatesPage.openLoginPanel().changeUser(shareToUser, password);
        Assert.assertTrue(templatesPage.isObjectInTable(TEMPLATE_TO_SHARE_NAME));

        sdWizardPage = templatesPage.clickEditOnObjectWithName(TEMPLATE_TO_SHARE_NAME);
        sdWizardPage.setValueInExpressionEditor(TEMPLATES_EXPRESSION_EDITOR_ID, TEMPLATE_NAME);
        sdWizardPage.clickAcceptButtonInWizard();
        Assert.assertEquals(templatesPage.getMessageFromPrompt(), "This template is shared with you in read only mode. You cannot change it.");

        sdWizardPage.clickButton(CANCEL_BUTTON_IN_TEMPLATE_WIZARD_ID);
        templatesPage.openLoginPanel().changeUser(CONFIGURATION.getLogin(), CONFIGURATION.getPassword());
        templatesPage.clickDeleteOnObjectWithName(TEMPLATE_TO_SHARE_NAME);
        templatesPage.clickConfirmDelete();
        Assert.assertFalse(templatesPage.isObjectInTable(TEMPLATE_TO_SHARE_NAME));
    }
}
