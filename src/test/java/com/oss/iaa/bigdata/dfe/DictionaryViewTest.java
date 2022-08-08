package com.oss.iaa.bigdata.dfe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.dictionary.DictionaryPage;
import com.oss.pages.iaa.bigdata.dfe.dictionary.DictionaryPopupPage;
import com.oss.pages.iaa.bigdata.dfe.dictionary.EntryPopupPage;
import com.oss.pages.iaa.bigdata.utils.ConstantsDfe;
import com.oss.utils.TestListener;

import io.qameta.allure.Description;

@Listeners({TestListener.class})
public class DictionaryViewTest extends BaseTestCase {

    private static final String DICTIONARY_DESCRIPTION = "Dictionary Selenium Test";
    private static final String ENTRIES_KEY = "Test Key";
    private static final String ENTRIES_VALUE = "Test Value";
    private static final String ADD_WIZARD_TEST_ID = "add-prompt-id_prompt-card";
    private static final String EDIT_WIZARD_TEST_ID = "edit-prompt-id_prompt-card";

    private static final Logger log = LoggerFactory.getLogger(DictionaryViewTest.class);

    private DictionaryPage dictionaryPage;
    private String dictionaryName;
    private String updatedDictionaryName;

    @BeforeClass
    public void goToDictionaryView() {
        dictionaryPage = DictionaryPage.goToPage(driver, BASIC_URL);

        dictionaryName = ConstantsDfe.createName() + "_DictTest";
        updatedDictionaryName = dictionaryName + "_updated";
    }

    @Test(priority = 1, testName = "Add new Dictionary", description = "Add new Dictionary")
    @Description("Add new Dictionary")
    public void addDictionary() {
        dictionaryPage.clickAddNewDictionary();
        DictionaryPopupPage dictionaryWizard = new DictionaryPopupPage(driver, webDriverWait, ADD_WIZARD_TEST_ID);
        dictionaryWizard.fillDictionaryPopup(dictionaryName, DICTIONARY_DESCRIPTION);
        dictionaryWizard.clickSave();
        boolean dictionaryIsCreated = dictionaryPage.dictionaryExistsIntoTable(dictionaryName);

        Assert.assertTrue(dictionaryIsCreated);
    }

    @Test(priority = 2, testName = "Create Entries", description = "Create Entries")
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

            Assert.assertTrue(entryIsCreated);
        } else {
            log.error("Entry with key: {} doesn't exist", ENTRIES_KEY);
            Assert.fail();
        }
    }

    @Test(priority = 3, testName = "Delete Entries", description = "Delete Entries")
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
                log.error("Entry with key: {} was not deleted", ENTRIES_KEY);
                Assert.fail();
            }
        } else {
            log.error("Dictionary with name: {} doesn't exist", dictionaryName);
            Assert.fail();
        }
    }

    @Test(priority = 4, testName = "Edit Dictionary", description = "Edit Dictionary")
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
            log.error("Dictionary with name: {} doesn't exist", updatedDictionaryName);
            Assert.fail();
        }
    }

    @Test(priority = 5, testName = "Delete Dictionary", description = "Delete Dictionary")
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
            log.error("Dictionary with name: {} was not deleted", updatedDictionaryName);
            Assert.fail();
        }
    }
}
