package com.oss.iaa.bigdata.dfe.regressiontests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.iaa.bigdata.dfe.dictionary.DictionaryPage;

import io.qameta.allure.Description;

public class DictionaryRegressionTests extends BaseTestCase {

    private static final String DICTIONARY_VIEW_TITLE = "Dictionaries";
    private static final String NAME = "Name";
    private DictionaryPage dictionaryPage;

    @BeforeMethod
    public void goToDictionaryView() {
        dictionaryPage = DictionaryPage.goToPage(driver, BASIC_URL);
    }

    @Test(priority = 1, testName = "Opening Dictionaries View", description = "Opening Dictionaries View")
    @Description("Opening Dictionaries View")
    public void openDictionaryViewTest() {
        Assert.assertEquals(dictionaryPage.getViewTitle(), DICTIONARY_VIEW_TITLE);
        Assert.assertFalse(dictionaryPage.isTableEmpty());
    }

    @Test(priority = 2, testName = "Check details tab", description = "Check details tab")
    @Description("Check details tab")
    public void checkDetailsTab() {
        dictionaryPage.selectFirstDictionaryInTable();
        String dictionaryName = dictionaryPage.getValueFromColumn(NAME);
        Assert.assertEquals(dictionaryPage.getValueFromDictionaryPropertyPanel(NAME), dictionaryName);
    }

    @Test(priority = 3, testName = "Check Entries Tab", description = "Check Entries Tab")
    @Description("Check Entries Tab")
    public void checkEntries() {
        dictionaryPage.selectFirstDictionaryInTable();
        dictionaryPage.selectEntriesTab();
        int numOfDictionaries = dictionaryPage.countDictionaries();
        int row = 1;
        while (dictionaryPage.isNoDataInEntriesTable() && row < numOfDictionaries) {
            dictionaryPage.selectRowInDictionaryTable(row);
            dictionaryPage.selectEntriesTab();
            row += 1;
        }
        Assert.assertFalse(dictionaryPage.isNoDataInEntriesTable());
        Assert.assertFalse(dictionaryPage.getTextFromEntry().isEmpty());
    }
}
