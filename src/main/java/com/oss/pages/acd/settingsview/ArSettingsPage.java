package com.oss.pages.acd.settingsview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.acd.BaseACDPage;

import io.qameta.allure.Step;

public class ArSettingsPage extends BaseACDPage {

    private static final Logger log = LoggerFactory.getLogger(ArSettingsPage.class);

    private static final String ADD_ACTION_TEMPLATE_BUTTON = "actionTemplateButtons-1";
    private static final String REMOVE_ACTION_TEMPLATE_BUTTON = "actionTemplateButtons-0";
    private static final String ACTION_TEMPLATE_TABLE_ID = "actionTemplateTable";
    private static final String DELETE_LABEL = "Delete";
    private final OldTable table;

    public ArSettingsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        table = OldTable.createById(driver, wait, ACTION_TEMPLATE_TABLE_ID);
    }

    @Step("I Open AR Settings View")
    public static ArSettingsPage goToPage(WebDriver driver, String suffixURL, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 150);

        String pageUrl = String.format(suffixURL, basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new ArSettingsPage(driver, wait);
    }

    @Step("I click add new Action Template")
    public void clickAddActionTemplate() {
        Button button = Button.createById(driver, ADD_ACTION_TEMPLATE_BUTTON);
        button.click();
        log.info("Clicking Add new Action Template button");
    }

    @Step("I set value in ComboBox")
    public void chooseOptionInComboBox(String comboBoxId, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard.createByComponentId(driver, wait, comboBoxId).setComponentValue(comboBoxId, value, Input.ComponentType.COMBOBOX);
        log.info("Setting value in ComboBox: {}", value);
    }

    @Step("I click Save button")
    public void clickSaveButton() {
        Button.createByLabel(driver, "Save").click();
    }

    @Step("I search for Action Template")
    public Boolean searchingThroughActionTemplates(String multiSearchFIeldId, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);

        if (!isDataInActionTemplatesTable()) {
            log.info("Action Template table has no data");
            return false;
        }
        ComponentFactory.create(multiSearchFIeldId, Input.ComponentType.MULTI_SEARCH_FIELD, driver, wait).setSingleStringValue(value);
        log.info("I am searching for created Action Template");
        return true;
    }

    @Step("Check if created Action Template exists")
    public Boolean isThereActionTemplateCreated() {
        log.info("I am in isThereActionTemplateCreated method");

        DelayUtils.waitForPageToLoad(driver, wait);
        String firstActionIdInTable = table.getCellValue(0, "Action Id");

        if (!isDataInActionTemplatesTable()) {
            log.info("Action Template table has no data");
            return false;
        }
        log.info("Action Id of newly created object is = {} ", firstActionIdInTable);
        return true;
    }

    @Step("I Clear multiSearchBox")
    public void clearMultiSearchBox(String multiSearchFieldId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clearing multiSearchBox");
        ComponentFactory.create(multiSearchFieldId, Input.ComponentType.MULTI_SEARCH_FIELD, driver, wait)
                .clear();
        log.info("MultiSearchBox is cleared");
    }

    @Step("I check if there is data in Action Template table")
    public boolean isDataInActionTemplatesTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I am checking if there is data in Action Templates table");
        return !table.hasNoData();
    }

    @Step("I select Action Template")
    public void selectFirstActionTemplateFromTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I am selecting Action Template");
        OldTable.createById(driver, wait, ACTION_TEMPLATE_TABLE_ID).selectRow(0);
    }

    @Step("I click delete Action Template")
    public Boolean deleteActionTemplate() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I am clicking Remove Action Template button");
        Button deleteButton = Button.createById(driver, REMOVE_ACTION_TEMPLATE_BUTTON);
        deleteButton.click();
        log.info("I am confirming removal of Action Template");
        ConfirmationBox.create(driver, wait).clickButtonByLabel(DELETE_LABEL);

        log.info("I am checking if Action Template has been removed");
        if (isDataInActionTemplatesTable()) {
            log.info("Action Template table is not empty - table still contains data.");
            return false;
        }
        log.info("Table is empty. Action Template has been removed successfully");
        return true;
    }
}
