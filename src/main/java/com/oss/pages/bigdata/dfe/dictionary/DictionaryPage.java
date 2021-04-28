package com.oss.pages.bigdata.dfe.dictionary;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.bigdata.dfe.BaseDfePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictionaryPage extends BaseDfePage {
    
    

    private static final Logger log = LoggerFactory.getLogger(DictionaryPage.class);
    private OldActionsContainer actionsContainer;
    private CommonList commonList;
    private EditableList editableList;

    private static final String TABLE_ID = "dictionariesAppId";
    private static final String ENTRIES_TAB_ID = "tab_tabsEntriesId";


    private final String ADD_NEW_DICTIONARY_LABEL = "Add New Dictionary";
    private final String EDIT_DICTIONARY_LABEL = "Edit Dictionary";
    private final String DELETE_DICTIONARY_LABEL = "Delete Dictionary";
    private final String SEARCH_INPUT_ID = "dictionariesSearchAppId";
    private final String ADD_NEW_ENTRY_LABEL = "Add New Entry";

    private final String NAME_COLUMN_LABEL = "Name"; // do spr
    private final String DELETE_LABEL = "Delete";  // do spr
    private final String DELETE_ENTRIES_LABEL = "DELETE";  // do spr


    final private String DICTIONARY_POPUP = "Popup";
    final private DictionaryPopupPage addNewDictionaryStep;
    final private EntryPopupPage addNewEntryStep;

    public DictionaryPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        addNewDictionaryStep = new DictionaryPopupPage(driver,wait);
        addNewEntryStep = new EntryPopupPage(driver,wait);
    }

    public DictionaryPopupPage getAddNewDictionaryStep() {return addNewDictionaryStep;}

    public EntryPopupPage getAddNewEntryStep() {return addNewEntryStep;}

    
    public String getWizardId() {
        return DICTIONARY_POPUP;
    }

    @Step("I Open Dictionaries View")
    public static DictionaryPage goToPage(WebDriver driver, String basicURL){
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "dictionaries");
        return new DictionaryPage(driver, wait);
    }
    @Step("I click add new Dictionary")
    public void clickAddNewDictionary(){
        clickContextActionAdd();
    }

    @Step("I click edit Dictionary")
    public void clickEditDictionary(){
        clickContextActionEdit();
    }

    @Step("I click delete Dictionary")
    public void clickDeleteDictionary(){clickContextActionDelete(); }

    @Step("I check if Dictionary: {dictionaryName} exists into the table")
    public Boolean dictionaryExistsIntoTable(String dictionaryName){
        searchFeed(dictionaryName);
        DelayUtils.waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(NAME_COLUMN_LABEL);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, dictionaryName);
        return numberOfRowsInTable == 1;
    }

    @Step("I select found Dictionary")
    public void selectFoundDictionary(){
        getTable(driver, wait).selectRow(0);
    }

    @Step("I confirm the removal of Dictionary")
    public void confirmDelete(){
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(DELETE_LABEL);
    }

   @Step("I click Entry Tab")
    public void selectTab(String label) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tab = TabWindowWidget.create(driver, wait);
        tab.selectTabByLabel(label);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I check if Entry: {entryName} exists into the table")
    public Boolean entryExistsIntoTable(String entryName){
        DelayUtils.waitForPageToLoad(driver, wait);
        String textInTable = getEditableList().selectRow(0).selectCell("1_keyId").getText();
        if (textInTable.equals(entryName)) {return true;}
        else {return false;}


    }

//AP  klikanie po dolnych tabach - akcje kontekstowe
    public OldTable getTableNew(WebDriver driver, WebDriverWait wait) {
        return OldTable.createByComponentDataAttributeName(driver, wait, "dictionariesTabsId");
    }

    private OldActionsContainer getActionsInterface() {
//        if (actionsContainer == null) {
            DelayUtils.waitForVisibility(wait, driver.findElement(By.xpath("//div[@class='OssWindow tabWindow']")));
            actionsContainer = OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.xpath("//div[@class='OssWindow tabWindow']")));
//        }
        return actionsContainer;
    }

//    private CommonList getCommonList(){
//        DelayUtils.waitForVisibility(wait, driver.findElement(By.xpath("//div[@class='flexRow last']")));
//        commonList = CommonList.create(driver, wait, "dictionaryTabsId");
//
//        return commonList;
//    }

    private EditableList getEditableList(){
        DelayUtils.waitForVisibility(wait, driver.findElement(By.xpath("//div[@class='flexRow last']")));
        editableList = EditableList.create(driver, wait);

        return editableList;
    }

    protected void clickContextActionNew(String actionLabel) {
        getActionsInterface().callActionByLabel(actionLabel);
        log.debug("Clicking context action: {}", actionLabel);
    }

    protected void clickEditableListAction(String editableListActionLabel){
        getEditableList().selectRow(0).callActionIcon(editableListActionLabel);
        log.debug("Clicking context action: {}", editableListActionLabel);
    }

    public String getEntryLabel() {
        return ADD_NEW_ENTRY_LABEL;
    }

    public String getDeleteEntryLabel() { return DELETE_ENTRIES_LABEL; }

//    @Step("I click add new Entry")
//    public void clickAddNewDictionaryTest(){ clickContextAction(getContextActionAddLabel()); }

//    Wizard getWizard(WebDriver driver, WebDriverWait wait) {
//        return Wizard.createByComponentId(driver, wait, getWizardId());
//    }

//    Wizard getWizard(WebDriver driver, WebDriverWait wait) {
//        return Wizard.createPopupWizard(driver, wait);
//    }

//    @Step("I click Save")
//    public void clickSave(){
//        getWizard(driver, wait).clickSave();
//        log.info("Finishing by clicking 'Save'");
//    }

    @Step("I click add new Entry")
    public void clickAddNewEntry(){ clickContextActionNew(getEntryLabel()); }

    @Step("I click delete Entry")
    public void clickDeleteEntry(){
        DelayUtils.waitForPageToLoad(driver, wait);
        clickEditableListAction(getDeleteEntryLabel()); }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_DICTIONARY_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_DICTIONARY_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_DICTIONARY_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }



}
