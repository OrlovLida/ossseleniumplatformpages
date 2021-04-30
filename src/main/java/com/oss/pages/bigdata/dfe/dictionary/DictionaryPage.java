package com.oss.pages.bigdata.dfe.dictionary;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.listwidget.EditableList;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.bigdata.dfe.BaseDfePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictionaryPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(DictionaryPage.class);
    private OldActionsContainer actionsContainer;

    private static final String TABLE_ID = "dictionariesAppId";
    private static final String KEY_CELL_ID = "1_keyId";

    private final String ADD_NEW_DICTIONARY_LABEL = "Add New Dictionary";
    private final String EDIT_DICTIONARY_LABEL = "Edit Dictionary";
    private final String DELETE_DICTIONARY_LABEL = "Delete Dictionary";
    private final String SEARCH_INPUT_ID = "dictionariesSearchAppId";
    private final String ADD_NEW_ENTRY_LABEL = "Add New Entry";

    private final String NAME_COLUMN_LABEL = "Name";
    private final String DELETE_LABEL = "Delete";
    private final String DELETE_ENTRIES_LABEL = "DELETE";

    private final DictionaryPopupPage dictionaryPopup;
    private final EntryPopupPage entryPopup;

    public DictionaryPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        dictionaryPopup = new DictionaryPopupPage(driver, wait);
        entryPopup = new EntryPopupPage(driver, wait);
    }

    public DictionaryPopupPage getDictionaryPopup() {
        return dictionaryPopup;
    }

    public EntryPopupPage getEntryPopup() {
        return entryPopup;
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
        searchFeed(dictionaryName);
        DelayUtils.waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(NAME_COLUMN_LABEL);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, dictionaryName);
        return numberOfRowsInTable == 1;
    }

    @Step("I select found Dictionary")
    public void selectFoundDictionary() {
        getTable(driver, wait).selectRow(0);
    }

    @Step("I confirm the removal of Dictionary")
    public void confirmDelete() {
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(DELETE_LABEL);
    }

    @Step("I click {label} Tab")
    public void selectTab(String label) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tab = TabWindowWidget.create(driver, wait);
        tab.selectTabByLabel(label);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I check if Entry: {entryName} exists into the table")
    public Boolean entryExistsIntoTable(String entryName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        String textInTable = getEditableList().selectRow(0).selectCell(KEY_CELL_ID).getText();
        return textInTable.equals(entryName);
    }

    @Step("I check if Entry is deleted from the table")
    public Boolean entryDeletedFromTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getEditableList().isNoData();
    }

    private TabsInterface getActionsInterface() {
        return TabWindowWidget.create(driver, wait);
    }

    private EditableList getEditableList() {
        return EditableList.create(driver, wait);
    }

    protected void clickContextActionNew(String actionLabel) {
        getActionsInterface().callActionByLabel(actionLabel);
        log.debug("Clicking context action: {}", actionLabel);
    }

    protected void clickEditableListAction(String editableListActionLabel) {
        getEditableList().selectRow(0).callActionIcon(editableListActionLabel);
        log.debug("Clicking context action: {}", editableListActionLabel);
    }

    @Step("I click add new Entry")
    public void clickAddNewEntry() {
        clickContextActionNew(ADD_NEW_ENTRY_LABEL);
    }

    @Step("I click delete Entry")
    public void clickDeleteEntry() {
        DelayUtils.waitForPageToLoad(driver, wait);
        clickEditableListAction(DELETE_ENTRIES_LABEL);
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
