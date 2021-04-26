package com.oss.pages.bigdata.dfe.stepwizard.dictionary;


import com.oss.pages.bigdata.dfe.stepwizard.commons.AddNewDictionaryPage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.CreateEntriesPage;
import com.oss.pages.bigdata.dfe.stepwizard.commons.StepWizardPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DictionaryStepWizardPage extends StepWizardPage {



   final private String DICTIONARY_POPUP = "Popup";
   final private AddNewDictionaryPage addNewDictionaryStep;
   final private CreateEntriesPage  addNewEntryStep;

    public DictionaryStepWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        addNewDictionaryStep = new AddNewDictionaryPage(driver,wait,getWizardId());
        addNewEntryStep = new CreateEntriesPage(driver,wait,"commonForm");
    }

    public AddNewDictionaryPage getAddNewDictionaryStep() {return addNewDictionaryStep;}

    public CreateEntriesPage getAddNewEntryStep() {return addNewEntryStep;}

    @Override
    public String getWizardId() {
        return DICTIONARY_POPUP;
    }
}
