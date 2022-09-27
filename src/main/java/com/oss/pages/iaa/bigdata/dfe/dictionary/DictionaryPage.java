package com.oss.pages.iaa.bigdata.dfe.dictionary;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.widgets.list.EditableList;
import com.oss.pages.iaa.bigdata.dfe.BaseDfePage;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class DictionaryPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(DictionaryPage.class);

    private static final String LIST_ID = "ExtendedList-entriesId";
    private static final String TABLE_ID = "dictionariesAppId";
    private static final String KEY_CELL_ID = "keyId";
    private static final String TAB_WIDGET_ID = "card-content_dictionaryTabsId";
    private static final String ENTRIES_TAB = "Entries";
    private static final String ADD_NEW_DICTIONARY_LABEL = "Add New Dictionary";
    private static final String EDIT_DICTIONARY_LABEL = "Edit Dictionary";
    private static final String DELETE_DICTIONARY_LABEL = "Delete Dictionary";
    private static final String SEARCH_INPUT_ID = "input_dictionariesSearchAppId";
    private static final String ADD_NEW_ENTRY_LABEL = "Add New Entry";
    private static final String NAME_COLUMN_LABEL = "Name";
    private static final String DELETE_LABEL = "Delete";
    private static final String DELETE_ENTRIES_LABEL = "DELETE";
    private static final String CATEGORY_SEARCH_ID = "category";
    private static final String DICTIONARY_PROPERTY_PANEL_ID = "dictionaries/tabs/detailsAppId";

    public DictionaryPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open Dictionaries View")
    public static DictionaryPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "dictionaries");
        return new DictionaryPage(driver, wait);
    }

    @Step("I click add new Dictionary")
    public void clickAddNewDictionary() {
        clickContextActionAdd();
    }

    @Step("I click edit Dictionary")
    public void clickEditDictionary() {
        clickContextActionEdit();
    }

    @Step("I click delete Dictionary")
    public void clickDeleteDictionary() {
        clickContextActionDelete();
    }

    @Step("I check if Dictionary: {dictionaryName} exists into the table")
    public Boolean dictionaryExistsIntoTable(String dictionaryName) {
        waitForPageToLoad(driver, wait);
        searchFeed(dictionaryName);
        waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(NAME_COLUMN_LABEL);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, dictionaryName);
        return numberOfRowsInTable == 1;
    }

    @Step("I select found Dictionary")
    public void selectFoundDictionary() {
        getTable().selectRow(0);
    }

    @Step("I confirm the removal")
    public void confirmDelete() {
        confirmDelete(DELETE_LABEL);
    }

    @Step("I click Entries Tab")
    public void selectEntriesTab() {
        selectTab(TAB_WIDGET_ID, ENTRIES_TAB);
    }

    @Step("I check if Entry: {entryName} exists into the table")
    public Boolean entryExistsIntoTable(String entryName) {
        waitForPageToLoad(driver, wait);
        return getTextFromEntry().equals(entryName);
    }

    @Step("Get text from entry")
    public String getTextFromEntry() {
        return getEditableList().getRow(0).getCell(KEY_CELL_ID).getText();
    }

    @Step("I check if Entry is deleted from the table")
    public Boolean entryDeletedFromTable() {
        waitForPageToLoad(driver, wait);
        return getEditableList().hasNoData();
    }

    private EditableList getEditableList() {
        return EditableList.createById(driver, wait, LIST_ID);
    }

    public boolean isNoDataInEntriesTable() {
        return getEditableList().hasNoData();
    }

    public int countDictionaries() {
        return getTable().countRows(NAME_COLUMN_LABEL);
    }

    protected void clickEditableListAction(String editableListActionLabel) {
        getEditableList().getRow(0).callActionIcon(editableListActionLabel);
        log.debug("Clicking context action: {}", editableListActionLabel);
    }

    @Step("I click add new Entry")
    public void clickAddNewEntry() {
        clickTabsContextAction(TAB_WIDGET_ID, ADD_NEW_ENTRY_LABEL);
    }

    @Step("I click delete Entry")
    public void clickDeleteEntry() {
        waitForPageToLoad(driver, wait);
        clickEditableListAction(DELETE_ENTRIES_LABEL);
    }

    @Step("Set category search")
    public void setCategorySearch(String categoryName) {
        ComponentFactory.create(CATEGORY_SEARCH_ID, driver, wait).setSingleStringValue(categoryName);
        log.info("Setting category search to: {}", categoryName);
        waitForPageToLoad(driver, wait);
    }

    @Step("Select first Dictionary in table")
    public void selectFirstDictionaryInTable() {
        selectFirstRowInTable();
    }

    @Step("Select row: {row} in dictionary table")
    public void selectRowInDictionaryTable(int row) {
        getTable().selectRow(row);
    }

    @Step("Get value: {propertyName} form Dictionary Property Panel")
    public String getValueFromDictionaryPropertyPanel(String propertyName) {
        return getValueFromPropertyPanel(DICTIONARY_PROPERTY_PANEL_ID, propertyName);
    }

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
