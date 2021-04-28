package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.dictionary.DictionaryPage;
import com.oss.utils.TestListener;
import io.qameta.allure.Description;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners({TestListener.class})
public class DictionaryViewTest extends BaseTestCase {
    private DictionaryPage dictionaryPage;
    private String dictionaryName;
    private String updatedDictionaryName;

    private final static String DICTIONARY_DESCRIPTION = "Dictionary Selenium Test";
    private final static String ENTRIES_TAB = "Entries";
    private final static String ENTRIES_KEY = "Test Key";
    private final static String ENTRIES_VALUE = "Test Value";



    @BeforeClass
    public void goToDictionaryView(){
        dictionaryPage = DictionaryPage.goToPage(driver, BASIC_URL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        dictionaryName = "Selenium_" + date + "_DictTest";
        updatedDictionaryName = dictionaryName + "_updated";
    }

    @Test(priority = 1)
    @Description("Add new Dictionary")
    public void addDictionary(){

        dictionaryPage.clickAddNewDictionary();
        dictionaryPage.getAddNewDictionaryStep().fillAddNewDictionary(dictionaryName, DICTIONARY_DESCRIPTION);
        dictionaryPage.getAddNewEntryStep().clickSave();
        Boolean dictionaryIsCreated = dictionaryPage.dictionaryExistsIntoTable(dictionaryName);

        Assert.assertTrue(dictionaryIsCreated);

    }

    @Test(priority = 2)
    @Description("Create Entries")
    public void createEntries(){
        Boolean dictionaryExists = dictionaryPage.dictionaryExistsIntoTable(dictionaryName);
        if(dictionaryExists){
            dictionaryPage.selectFoundDictionary();
            dictionaryPage.selectTab(ENTRIES_TAB);
            dictionaryPage.clickAddNewEntry();
            dictionaryPage.getAddNewEntryStep().fillAddNewEntry(ENTRIES_KEY, ENTRIES_VALUE);
            dictionaryPage.getAddNewEntryStep().clickSave();
            Boolean entryIsCreated = dictionaryPage.entryExistsIntoTable(ENTRIES_KEY);
            Assert.assertTrue(entryIsCreated);
            dictionaryPage.clickDeleteEntry();
            dictionaryPage.confirmDelete();

            dictionaryPage.selectFoundDictionary();
        } else {
            Assert.fail();
        }
    }
//    @Test(priority = 2) // nie działa - bug
//    @Description("Edit Dictionary")
//    public void editDictionary(){
//        Boolean dictionaryExists = dictionaryPage.dictionaryExistsIntoTable(dictionaryName);
//        if(dictionaryExists){
//            dictionaryPage.selectFoundDictionary();
//            dictionaryPage.clickEditDictionary();
//
//            WebDriverWait wait = new WebDriverWait(driver, 45);
//            DictionaryStepWizardPage dictionaryStepWizard = new DictionaryStepWizardPage(driver, wait);
//            dictionaryStepWizard.getAddNewDictionaryStep().fillName(updatedDictionaryName);
//            dictionaryStepWizard.clickSave();
//            Boolean dictionaryIsCreated = dictionaryPage.dictionaryExistsIntoTable(updatedDictionaryName);
//
//            Assert.assertTrue(dictionaryIsCreated);
//
//        } else {
//            Assert.fail();
//        }
//    }

    @Test(priority = 3)
    @Description("Delete Dictionary")
    public void deleteDictionary(){
        Boolean dictionaryExists = dictionaryPage.dictionaryExistsIntoTable(dictionaryName);
        if(dictionaryExists){
            dictionaryPage.selectFoundDictionary();
            dictionaryPage.clickDeleteDictionary();
            dictionaryPage.confirmDelete();
            Boolean dictionaryDeleted = !dictionaryPage.dictionaryExistsIntoTable(dictionaryName);

            Assert.assertTrue(dictionaryDeleted);
        } else {
            Assert.fail();
        }
    }
}
