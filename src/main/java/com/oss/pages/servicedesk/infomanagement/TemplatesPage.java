package com.oss.pages.servicedesk.infomanagement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.servicedesk.BaseSDPage;
import com.oss.pages.servicedesk.issue.wizard.SDWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.ServiceDeskConstants.INFO_MANAGEMENT_URL_PATTERN;

public class TemplatesPage extends BaseSDPage {

    private static final String TEMPLATES_URL = "template-list";
    private static final String TEMPLATES_COMMON_LIST_ID = "template-list-app";
    private static final String CREATE_HEADER_ID = "create-header-id";
    private static final String CREATE_FOOTER_ID = "create-footer-id";
    private static final String CREATE_TEMPLATE_ID = "create-template-id";
    private static final String WIZARD_ID = "template-wizard-wizard";
    private static final String NAME_LABEL = "Name";
    private static final String TYPE_LABEL = "Type";
    private static final String CHANNEL_LABEL = "Channel";
    private static final String CREATOR_LABEL = "Creator";
    private static final String EDIT_BUTTON_ICON_LABEL = "EDIT";
    private static final String DELETE_BUTTON_ICON_LABEL = "DELETE";
    private static final String DELETE_BUTTON_LABEL = "Delete";
    private static final String SORT_BY_BUTTON_ID = "sort-by-field-id";
    private static final String SORT_BY_CREATED_DESC_ID = "CREATE_DATE_DESC";
    private static final String SORT_BY_NAME_ASC_ID = "NAME_ASC";
    private static final String SORT_BY_TYPE_ASC_ID = "TYPE_ASC";
    private static final String CREATION_DATE_LABEL = "Creation Date";
    private static final String CHANNEL_ID = "channel";

    public TemplatesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public TemplatesPage goToTemplatesPage(String basicUrl) {
        String url = String.format(INFO_MANAGEMENT_URL_PATTERN, basicUrl, TEMPLATES_URL);
        openPage(driver, url);
        return new TemplatesPage(driver, wait);
    }

    @Step("Click Create Header")
    public SDWizardPage clickCreateHeader() {
        callListActionById(CREATE_HEADER_ID);
        log.info("Clicking Create Header");
        return new SDWizardPage(driver, wait, WIZARD_ID);
    }

    @Step("Click Create Footer")
    public SDWizardPage clickCreateFooter() {
        callListActionById(CREATE_FOOTER_ID);
        log.info("Clicking Create Footer button");
        return new SDWizardPage(driver, wait, WIZARD_ID);
    }

    @Step("Click Create Template")
    public SDWizardPage clickCreateTemplate() {
        callListActionById(CREATE_TEMPLATE_ID);
        log.info("Clicking Create Template button");
        return new SDWizardPage(driver, wait, WIZARD_ID);
    }

    @Step("Click Sort By Created Date Descending")
    public void clickSortByDate() {
        sortListByActionId(SORT_BY_CREATED_DESC_ID);
        log.info("Clicking Sort by Created Date Descending");
    }

    @Step("Click Sort By Name")
    public void clickSortByName() {
        sortListByActionId(SORT_BY_NAME_ASC_ID);
        log.info("Clicking Sort by Name");
    }

    @Step("Click Sort By Type Descending")
    public void clickSortByType() {
        sortListByActionId(SORT_BY_TYPE_ASC_ID);
        log.info("Clicking Sort By Type Descending");
    }

    private void callListActionById(String actionId) {
        getTemplatesList().callAction(actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private void sortListByActionId(String actionId) {
        getTemplatesList().callAction(SORT_BY_BUTTON_ID, actionId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Check if Templates table is empty")
    public boolean isTemplatesTableEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getTemplatesList().hasNoData();
    }

    @Step("Click Edit Object with name: {objectName}")
    public SDWizardPage clickEditOnObjectWithName(String objectName) {
        getTemplatesList().getRow(NAME_LABEL, objectName).callActionIcon(EDIT_BUTTON_ICON_LABEL);
        return new SDWizardPage(driver, wait, WIZARD_ID);
    }

    @Step("Click Delete object with name {objectName}")
    public void clickDeleteOnObjectWithName(String objectName) {
        getTemplatesList().getRow(NAME_LABEL, objectName).callActionIcon(DELETE_BUTTON_ICON_LABEL);
        log.info("Clicking Delete object with name {}", objectName);
    }

    @Step("Click confirm Delete")
    public void clickConfirmDelete() {
        ConfirmationBox.create(driver, wait).clickButtonByLabel(DELETE_BUTTON_LABEL);
        log.info("Clicking confirm Delete");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Get Type of object with name {objectName}")
    public String getTypeOfObject(String objectName) {
        return getRowOfObjectWithName(objectName).getValue(TYPE_LABEL);
    }

    @Step("Get Creation Date for row: {row}")
    public String getCreationDateForRow(int row) {
        return getValueOfRowWithLabel(row, CREATION_DATE_LABEL);
    }

    public String getChannelForFirstRow() {
        return getValueOfRowWithLabel(0, CHANNEL_LABEL);
    }

    public String getTypeForFirstRow() {
        return getValueOfRowWithLabel(0, TYPE_LABEL);
    }

    @Step("Check if dates are sorted")
    public boolean areDatesSorted(String date1, String date2) {
        return LocalDateTime.parse(date1, DATE_TIME_FORMATTER)
                .isAfter(LocalDateTime.parse(date2, DATE_TIME_FORMATTER));
    }

    @Step("Get Name of object in row: {row}")
    public String getNameOfObjectInRow(int row) {
        return getValueOfRowWithLabel(row, NAME_LABEL);
    }

    @Step("Sort List alphabetically and Check first element")
    public String sortListByLabelAndGetFirstElement(String label) {
        ArrayList<String> sorted = getAllObjectsLabels(label);
        Collections.sort(sorted);
        log.info("Sorting and getting first element in alphabetical order: {}", sorted.get(0));
        return sorted.get(0);
    }

    @Step("Get Names of all objects in the List")
    private ArrayList<String> getAllObjectsLabels(String label) {
        int rows = getTemplatesList().countRows();
        ArrayList<String> allObjectsNames = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            allObjectsNames.add(getTemplatesList().getRows().get(i).getValue(label));
        }
        log.info("Creating list of all object in template list");
        return allObjectsNames;
    }

    @Step("Get Creator user name of object with name {objectName}")
    public String getCreatorOfObject(String objectName) {
        return getRowOfObjectWithName(objectName).getValue(CREATOR_LABEL);
    }

    @Step("Check if object with name {objectName} is in the Table")
    public boolean isObjectInTable(String objectName) {
        return getTemplatesList().isRowDisplayed(NAME_LABEL, objectName);
    }

    @Step("Filter template list by channel: {channel}")
    public void filterByChannel(String channel) {
        getTemplatesList().searchByAttribute(CHANNEL_ID, Input.ComponentType.MULTI_COMBOBOX, channel);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Filtering by channel: {}", channel);
    }

    @Step("Search fullText: {text}")
    public void searchForText(String text) {
        getTemplatesList().fullTextSearch(text);
        log.info("Searching for text: {}", text);
    }

    private CommonList.Row getRowOfObjectWithName(String name) {
        return getTemplatesList().getRowContains(NAME_LABEL, name);
    }

    private String getValueOfRowWithLabel(int row, String columnLabel) {
        return getTemplatesList().getRows().get(row).getValue(columnLabel);
    }

    private CommonList getTemplatesList() {
        return CommonList.create(driver, wait, TEMPLATES_COMMON_LIST_ID);
    }
}
