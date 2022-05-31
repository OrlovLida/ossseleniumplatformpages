package com.oss.servicedesk.infomanagement;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.servicedesk.infomanagement.TemplatesPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Description;

import static com.oss.pages.servicedesk.ServiceDeskConstants.USER_NAME;

public class TemplatesTest extends BaseTestCase {

    private TemplatesPage templatesPage;
    private SDWizardPage sdWizardPage;

    private static final String WIZARD_NAME_FIELD_ID = "template-wizard-name";
    private static final String HEADER_NAME = "Selenium Test Header Name";
    private static final String EDITED_HEADER_NAME = "Selenium Test Header Name Edited";
    private static final String FOOTER_NAME = "Selenium Test Footer Name";
    private static final String EDITED_FOOTER_NAME = "Selenium Test Footer Name Edited";
    private static final String SYSTEM_COMBOBOX_ID = "template-wizard-system";
    private static final String SYSTEM = "Service Desk";
    private static final String OBJECT_TYPE_COMBOBOX_ID = "template-wizard-context-type";
    private static final String OBJECT_TYPE_HEADER = "TroubleTicket";
    private static final String OBJECT_TYPE_FOOTER = "Problem";
    private static final String TEMPLATE_TEXT = "Test Selenium Header";
    private static final String TEMPLATE_HTML_EDITOR_ID = "template-wizard-content";
    private static final String FOOTER_TYPE = "FOOTER";
    private static final String HEADER_TYPE = "HEADER";
    private static final String TEMPLATE_TYPE = "TEMPLATE";
    private static final String TEMPLATE_NAME = "Selenium Test Template";
    private static final String EDITED_TEMPLATE_NAME = "Selenium Test Template Edited";
    private static final String CHANNEL_COMBOBOX_ID = "template-wizard-channel";
    private static final String CHANNEL_INTERNAL = "Internal";
    private static final String TYPE_SUCCESS = "Success";
    private static final String TYPE_COMBOBOX_ID = "template-wizard-internal-type";

    @BeforeMethod
    public void goToTemplatesPage() {
        templatesPage = new TemplatesPage(driver, webDriverWait).goToTemplatesPage(BASIC_URL);
    }

    @Test(priority = 1, testName = "Create Header", description = "Create Header")
    @Description("Create Header")
    public void createHeader() {
        sdWizardPage = templatesPage.clickCreateHeader();
        sdWizardPage.insertValueToMultiCombobox(SYSTEM, SYSTEM_COMBOBOX_ID);
        sdWizardPage.insertValueToTextComponent(HEADER_NAME, WIZARD_NAME_FIELD_ID);
        sdWizardPage.insertValueToMultiCombobox(OBJECT_TYPE_HEADER, OBJECT_TYPE_COMBOBOX_ID);
        sdWizardPage.setValueInHtmlEditor(TEMPLATE_TEXT, TEMPLATE_HTML_EDITOR_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(templatesPage.isTemplatesTableEmpty(), "Template list is empty");
        Assert.assertTrue(templatesPage.isObjectInTable(HEADER_NAME));
        Assert.assertEquals(templatesPage.getTypeOfObject(HEADER_NAME), HEADER_TYPE);
        Assert.assertEquals(templatesPage.getCreatorOfObject(HEADER_NAME), USER_NAME);
    }

    @Test(priority = 2, testName = "Edit header", description = "Edit header")
    @Description("Edit header")
    public void editHeader() {
        Assert.assertFalse(templatesPage.isTemplatesTableEmpty());
        if (templatesPage.isObjectInTable(HEADER_NAME)) {
            sdWizardPage = templatesPage.clickEditOnObjectWithName(HEADER_NAME);
            sdWizardPage.insertValueToTextComponent(EDITED_HEADER_NAME, WIZARD_NAME_FIELD_ID);
            sdWizardPage.clickAcceptButtonInWizard();
        } else {
            Assert.fail("Header with name: " + HEADER_NAME + " is not in the Table");
        }

        Assert.assertFalse(templatesPage.isTemplatesTableEmpty(), "Template list is empty");
        Assert.assertTrue(templatesPage.isObjectInTable(EDITED_HEADER_NAME));
        Assert.assertEquals(templatesPage.getTypeOfObject(EDITED_HEADER_NAME), HEADER_TYPE);
    }

    @Test(priority = 3, testName = "Delete Header", description = "Delete Header")
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

    @Test(priority = 4, testName = "Create Footer", description = "Create Footer")
    @Description("Create Footer")
    public void createFooter() {
        sdWizardPage = templatesPage.clickCreateFooter();
        sdWizardPage.insertValueToMultiCombobox(SYSTEM, SYSTEM_COMBOBOX_ID);
        sdWizardPage.insertValueToTextComponent(FOOTER_NAME, WIZARD_NAME_FIELD_ID);
        sdWizardPage.insertValueToMultiCombobox(OBJECT_TYPE_FOOTER, OBJECT_TYPE_COMBOBOX_ID);
        sdWizardPage.setValueInHtmlEditor(TEMPLATE_TEXT, TEMPLATE_HTML_EDITOR_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(templatesPage.isTemplatesTableEmpty(), "Template list is empty");
        Assert.assertTrue(templatesPage.isObjectInTable(FOOTER_NAME));
        Assert.assertEquals(templatesPage.getTypeOfObject(FOOTER_NAME), FOOTER_TYPE);
        Assert.assertEquals(templatesPage.getCreatorOfObject(FOOTER_NAME), USER_NAME);
    }

    @Test(priority = 5, testName = "Edit Footer", description = "Edit Footer")
    @Description("Edit Footer")
    public void EditFooter() {
        sdWizardPage = templatesPage.clickEditOnObjectWithName(FOOTER_NAME);
        sdWizardPage.insertValueToTextComponent(EDITED_FOOTER_NAME, WIZARD_NAME_FIELD_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(templatesPage.isTemplatesTableEmpty(), "Template list is empty");
        Assert.assertTrue(templatesPage.isObjectInTable(EDITED_FOOTER_NAME));
        Assert.assertEquals(templatesPage.getTypeOfObject(EDITED_FOOTER_NAME), FOOTER_TYPE);
    }

    @Test(priority = 6, testName = "Delete Footer", description = "Delete Footer")
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

    @Test(priority = 7, testName = "Create Template", description = "Create Template")
    @Description("Create Template")
    public void createTemplate() {
        sdWizardPage = templatesPage.clickCreateTemplate();
        sdWizardPage.insertValueToTextComponent(TEMPLATE_NAME, WIZARD_NAME_FIELD_ID);
        sdWizardPage.insertValueToComboBoxComponent(CHANNEL_INTERNAL, CHANNEL_COMBOBOX_ID);
        sdWizardPage.insertValueToComboBoxComponent(SYSTEM, SYSTEM_COMBOBOX_ID);
        sdWizardPage.insertValueToComboBoxComponent(OBJECT_TYPE_HEADER, OBJECT_TYPE_COMBOBOX_ID);
        sdWizardPage.insertValueToComboBoxComponent(TYPE_SUCCESS, TYPE_COMBOBOX_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(templatesPage.isTemplatesTableEmpty(), "Template list is empty");
        Assert.assertTrue(templatesPage.isObjectInTable(TEMPLATE_NAME));
        Assert.assertEquals(templatesPage.getTypeOfObject(TEMPLATE_NAME), TEMPLATE_TYPE);
    }

    @Test(priority = 8, testName = "Edit Template", description = "Edit Template")
    @Description("Edit Template")
    public void editTemplate() {
        sdWizardPage = templatesPage.clickEditOnObjectWithName(TEMPLATE_NAME);
        sdWizardPage.insertValueToTextComponent(EDITED_TEMPLATE_NAME, WIZARD_NAME_FIELD_ID);
        sdWizardPage.clickAcceptButtonInWizard();

        Assert.assertFalse(templatesPage.isTemplatesTableEmpty(), "Template list is empty");
        Assert.assertTrue(templatesPage.isObjectInTable(EDITED_TEMPLATE_NAME));
        Assert.assertEquals(templatesPage.getTypeOfObject(EDITED_TEMPLATE_NAME), TEMPLATE_TYPE);
    }

    @Test(priority = 9)
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
}
