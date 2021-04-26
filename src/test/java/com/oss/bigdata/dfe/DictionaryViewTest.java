package com.oss.bigdata.dfe;

import com.oss.BaseTestCase;
import com.oss.pages.bigdata.dfe.AggregatePage;
import com.oss.pages.bigdata.dfe.DictionaryPage;
import com.oss.pages.bigdata.dfe.stepwizard.dictionary.DictionaryStepWizardPage;
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


//    private final static String ETL_PROCESS_NAME = "t:CRUD#ETLforAggr";
//    private final static String AGGREGATE_CONFIGURATION_DIMENSION_NAME = "t:SMOKE#D_HOST (HOST_NM)";
//    private final static String AGGREGATE_CONFIGURATION_NAME = "Selenium_Aggregate_Test";
//    private final static String AGGREGATE_CONFIGURATION_TABLE_PREFIX = "Selenium_Aggregate_Test";

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
        WebDriverWait wait = new WebDriverWait(driver, 45);
        DictionaryStepWizardPage dictionaryStepWizard = new DictionaryStepWizardPage(driver, wait);
        dictionaryStepWizard.getAddNewDictionaryStep().fillAddNewDictionary(dictionaryName, DICTIONARY_DESCRIPTION);
        dictionaryStepWizard.clickSave();
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


            WebDriverWait wait = new WebDriverWait(driver, 45);
            DictionaryStepWizardPage dictionaryStepWizard = new DictionaryStepWizardPage(driver, wait);
            dictionaryStepWizard.getAddNewEntryStep().fillAddNewEntry(ENTRIES_TAB, ENTRIES_TAB);
          dictionaryStepWizard.clickSave();
//            Boolean dictionaryIsCreated = dictionaryPage.dictionaryExistsIntoTable(updatedDictionaryName);
//
//            Assert.assertTrue(dictionaryIsCreated);

        } else {
            Assert.fail();
        }
    }
//    @Test(priority = 2)
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

    @Test(priority = 2)
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
