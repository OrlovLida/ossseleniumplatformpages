package com.oss.pages.bigdata.dfe.externalresource;

import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.BaseDfePage;
import com.oss.pages.bigdata.dfe.dictionary.DictionaryPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalResourcesPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(ExternalResourcesPage.class);
    private static final String TABLE_ID = "external-resources-listAppId";

    private final String ADD_NEW_EXTERNAL_RESOURCE_LABEL = "Add New External Resource";
    private final String EDIT_EXTERNAL_RESOURCE_LABEL = "Edit External Resource";
    private final String DELETE_EXTERNAL_RESOURCE_LABEL = "Delete External Resource";
    private final String SEARCH_INPUT_ID = "external-resources-listSearchAppId";

    private final String NAME_COLUMN_LABEL = "Name";
    private final String DELETE_LABEL = "Delete";

    private final ExternalResourcesPopupPage externalResourcePopup;

    public ExternalResourcesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        externalResourcePopup = new ExternalResourcesPopupPage(driver, wait);
    }

    public ExternalResourcesPopupPage getExternalResourcePopup() {
        return externalResourcePopup;
    }

    @Step("I Open External Resources View")
    public static ExternalResourcesPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "external-resources");
        return new ExternalResourcesPage(driver, wait);
    }

    @Step("I click add new External Resource")
    public void clickAddNewExternalResource() {
        clickContextActionAdd();
    }

    @Step("I click edit External Resource")
    public void clickEditExternalResource() {
        clickContextActionEdit();
    }

    @Step("I click delete External Resource")
    public void clickDeleteExternalResource() {
        clickContextActionDelete();
    }

    @Step("I check if External Resource: {externalResourceName} exists into the table")
    public Boolean externalResourceExistsIntoTable(String externalResourceName) {
        searchFeed(externalResourceName);
        DelayUtils.waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(NAME_COLUMN_LABEL);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, externalResourceName);
        return numberOfRowsInTable == 1;
    }

    @Step("I select found External Resource")
    public void selectFoundExternalResource() {
        getTable(driver, wait).selectRow(0);
    }

    @Step("I confirm the removal of External Resource")
    public void confirmDelete() {
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(DELETE_LABEL);
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_EXTERNAL_RESOURCE_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_EXTERNAL_RESOURCE_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_EXTERNAL_RESOURCE_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}