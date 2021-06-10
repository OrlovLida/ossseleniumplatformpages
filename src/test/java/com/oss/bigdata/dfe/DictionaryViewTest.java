package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.dictionary.DictionaryPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners({TestListener.class})
public class DictionaryViewTest extends BaseTestCase {

    private final static String DICTIONARY_DESCRIPTION = "Dictionary Selenium Test";
    private final static String ENTRIES_TAB = "Entries";
    private final static String ENTRIES_KEY = "Test Key";
    private final static String ENTRIES_VALUE = "Test Value";
    private final static Logger log = LoggerFactory.getLogger(DictionaryViewTest.class);

    private DictionaryPage dictionaryPage;
    private String dictionaryName;
    private String updatedDictionaryName;

    @BeforeClass
    public void goToDictionaryView() {
        dictionaryPage = DictionaryPage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        dictionaryName = "Selenium_" + date + "_DictTest";
        updatedDictionaryName = dictionaryName + "_updated";
    }

    @Test(priority = 1, testName = "Add new Dictionary", description = "Add new Dictionary")
    @Description("Add new Dictionary")
    public void addDictionary() {
        dictionaryPage.clickAddNewDictionary();
        dictionaryPage.getDictionaryPopup().fillDictionaryPopup(dictionaryName, DICTIONARY_DESCRIPTION);
        dictionaryPage.getDictionaryPopup().clickSave();
        Boolean dictionaryIsCreated = dictionaryPage.dictionaryExistsIntoTable(dictionaryName);

        Assert.assertTrue(dictionaryIsCreated);
    }

    @Test(priority = 2, testName = "Create Entries", description = "Create Entries")
    @Description("Create Entries")
    public void createEntries() {
        Boolean dictionaryExists = dictionaryPage.dictionaryExistsIntoTable(dictionaryName);
        if (dictionaryExists) {
            dictionaryPage.selectFoundDictionary();
            dictionaryPage.selectTab(ENTRIES_TAB);
            dictionaryPage.clickAddNewEntry();
            dictionaryPage.getEntryPopup().fillEntryPopup(ENTRIES_KEY, ENTRIES_VALUE);
            dictionaryPage.getEntryPopup().clickSave();
            Boolean entryIsCreated = dictionaryPage.entryExistsIntoTable(ENTRIES_KEY);

            Assert.assertTrue(entryIsCreated);
        } else {
            log.error("Entry with key: {} doesn't exist", ENTRIES_KEY);
            Assert.fail();
        }
    }

    @Test(priority = 3, testName = "Delete Entries", description = "Delete Entries")
    @Description("Delete Entries")
    public void deleteEntries() {
        Boolean entryExists = dictionaryPage.entryExistsIntoTable(ENTRIES_KEY);
        if (entryExists) {
            dictionaryPage.clickDeleteEntry();
            dictionaryPage.confirmDelete();
            Boolean entryDeleted = dictionaryPage.entryDeletedFromTable();

            Assert.assertTrue(entryDeleted);

            dictionaryPage.selectFoundDictionary();
        } else {
            log.error("Entry with key: {} was not deleted", ENTRIES_KEY);
            Assert.fail();
        }
    }

    @Test(priority = 4, testName = "Edit Dictionary", description = "Edit Dictionary", enabled = false)
    @Description("Edit Dictionary")
    public void editDictionary() {
        Boolean dictionaryExists = dictionaryPage.dictionaryExistsIntoTable(dictionaryName);
        if (dictionaryExists) {
            dictionaryPage.selectFoundDictionary();
            dictionaryPage.clickEditDictionary();
            dictionaryPage.getDictionaryPopup().fillName(updatedDictionaryName);
            dictionaryPage.getDictionaryPopup().clickSave();
            Boolean dictionaryIsCreated = dictionaryPage.dictionaryExistsIntoTable(updatedDictionaryName);

            Assert.assertTrue(dictionaryIsCreated);

        } else {
            log.error("Dictionary with name: {} doesn't exist", updatedDictionaryName);
            Assert.fail();
        }
    }

    @Test(priority = 5, testName = "Delete Dictionary", description = "Delete Dictionary")
    @Description("Delete Dictionary")
    public void deleteDictionary() {
        Boolean dictionaryExists = dictionaryPage.dictionaryExistsIntoTable(dictionaryName);
        if (dictionaryExists) {
            dictionaryPage.selectFoundDictionary();
            dictionaryPage.clickDeleteDictionary();
            dictionaryPage.confirmDelete();
            Boolean dictionaryDeleted = !dictionaryPage.dictionaryExistsIntoTable(dictionaryName);

            Assert.assertTrue(dictionaryDeleted);
        } else {
            log.error("Dictionary with name: {} was not deleted", dictionaryName);
            Assert.fail();
        }
    }
}
