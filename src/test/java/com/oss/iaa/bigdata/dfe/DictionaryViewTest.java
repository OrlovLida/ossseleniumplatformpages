package com.oss.iaa.bigdata.dfe;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.dictionary.DictionaryPage;
import com.oss.pages.iaa.bigdata.dfe.dictionary.DictionaryPopupPage;
import com.oss.pages.iaa.bigdata.dfe.dictionary.EntryPopupPage;
import com.oss.pages.iaa.bigdata.utils.ConstantsDfe;

import io.qameta.allure.Description;

public class DictionaryViewTest extends BaseTestCase {

    private static final String DICTIONARY_DESCRIPTION = "Dictionary Selenium Test";
    private static final String ENTRIES_KEY = "Test Key";
    private static final String ENTRIES_VALUE = "Test Value";
    private static final String ADD_WIZARD_TEST_ID = "add-prompt-id_prompt-card";
    private static final String EDIT_WIZARD_TEST_ID = "edit-prompt-id_prompt-card";
    private static final String DICTIONARY_CATEGORY = "Selenium Tests";
    private static final String CATEGORY_COLUMN = "Category";

    private DictionaryPage dictionaryPage;
    private String dictionaryName;
    private String updatedDictionaryName;

    @BeforeClass
    public void setNames() {
        dictionaryName = ConstantsDfe.createName() + "_DictTest";
        updatedDictionaryName = dictionaryName + "_updated";
    }

    @BeforeMethod
    public void goToDictionaryView() {
        dictionaryPage = DictionaryPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Add new Dictionary", description = "Add new Dictionary")
    @Description("Add new Dictionary")
    public void addDictionary() {
        dictionaryPage.clickAddNewDictionary();
        DictionaryPopupPage dictionaryWizard = new DictionaryPopupPage(driver, webDriverWait, ADD_WIZARD_TEST_ID);
        dictionaryWizard.fillDictionaryPopup(dictionaryName, DICTIONARY_DESCRIPTION, DICTIONARY_CATEGORY);
        dictionaryWizard.clickSave();
        boolean dictionaryIsCreated = dictionaryPage.dictionaryExistsIntoTable(dictionaryName);

        Assert.assertTrue(dictionaryIsCreated);
    }

    @Test(priority = 2, testName = "Category Search", description = "Category Search")
    @Description("Category Search")
    public void categorySearchTest() {
        dictionaryPage.setCategorySearch(DICTIONARY_CATEGORY);
        dictionaryPage.selectFirstDictionaryInTable();
        Assert.assertEquals(dictionaryPage.getValueFromColumn(CATEGORY_COLUMN), DICTIONARY_CATEGORY);
    }

    @Test(priority = 3, testName = "Create Entries", description = "Create Entries")
    @Description("Create Entries")
    public void createEntries() {
        boolean dictionaryExists = dictionaryPage.dictionaryExistsIntoTable(dictionaryName);
        if (dictionaryExists) {
            dictionaryPage.selectFoundDictionary();
            dictionaryPage.selectEntriesTab();
            dictionaryPage.clickAddNewEntry();
            EntryPopupPage entryWizard = new EntryPopupPage(driver, webDriverWait, ADD_WIZARD_TEST_ID);
            entryWizard.fillEntryPopup(ENTRIES_KEY, ENTRIES_VALUE);
            entryWizard.clickSave();
            boolean entryIsCreated = dictionaryPage.entryExistsIntoTable(ENTRIES_KEY);

            Assert.assertTrue(entryIsCreated, "Entry with key: " + ENTRIES_KEY + " doesn't exist");
        } else {
            Assert.fail(failMessage(dictionaryName));
        }
    }

    @Test(priority = 4, testName = "Delete Entries", description = "Delete Entries")
    @Description("Delete Entries")
    public void deleteEntries() {
        boolean dictionaryExists = dictionaryPage.dictionaryExistsIntoTable(dictionaryName);
        if (dictionaryExists) {
            dictionaryPage.selectFoundDictionary();
            dictionaryPage.selectEntriesTab();
            boolean entryExists = dictionaryPage.entryExistsIntoTable(ENTRIES_KEY);
            if (entryExists) {
                dictionaryPage.clickDeleteEntry();
                dictionaryPage.confirmDelete();
                boolean entryDeleted = dictionaryPage.entryDeletedFromTable();
                Assert.assertTrue(entryDeleted);

                dictionaryPage.selectFoundDictionary();
            } else {
                Assert.fail("Entry with key: " + ENTRIES_KEY + " was not deleted");
            }
        } else {
            Assert.fail(failMessage(dictionaryName));
        }
    }

    @Test(priority = 5, testName = "Edit Dictionary", description = "Edit Dictionary")
    @Description("Edit Dictionary")
    public void editDictionary() {
        boolean dictionaryExists = dictionaryPage.dictionaryExistsIntoTable(dictionaryName);
        if (dictionaryExists) {
            dictionaryPage.selectFoundDictionary();
            dictionaryPage.clickEditDictionary();
            DictionaryPopupPage dictionaryWizard = new DictionaryPopupPage(driver, webDriverWait, EDIT_WIZARD_TEST_ID);
            dictionaryWizard.fillName(updatedDictionaryName);
            dictionaryWizard.clickSave();
            boolean dictionaryIsCreated = dictionaryPage.dictionaryExistsIntoTable(updatedDictionaryName);

            Assert.assertTrue(dictionaryIsCreated);
        } else {
            Assert.fail(failMessage(updatedDictionaryName));
        }
    }

    @Test(priority = 6, testName = "Delete Dictionary", description = "Delete Dictionary")
    @Description("Delete Dictionary")
    public void deleteDictionary() {
        boolean dictionaryExists = dictionaryPage.dictionaryExistsIntoTable(updatedDictionaryName);
        if (dictionaryExists) {
            dictionaryPage.selectFoundDictionary();
            dictionaryPage.clickDeleteDictionary();
            dictionaryPage.confirmDelete();
            boolean dictionaryDeleted = !dictionaryPage.dictionaryExistsIntoTable(updatedDictionaryName);

            Assert.assertTrue(dictionaryDeleted);
        } else {
            Assert.fail("Dictionary with name: " + updatedDictionaryName + " was not deleted");
        }
    }

    private String failMessage(String dictionaryName) {
        return String.format("Dictionary with name: %s doesn't exist", dictionaryName);
    }
}
