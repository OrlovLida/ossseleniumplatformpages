package com.oss.pages.bigdata.dfe;

import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EtlDataCollectionsPage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(EtlDataCollectionsPage.class);

    private static final String TABLE_ID = "data-collection-listAppId";

    private static final String ADD_ETL_PROCESS_LABEL = "Add New ETL Process";
    private static final String EDIT_ETL_PROCESS_LABEL = "Edit ETL Process";
    private static final String DELETE_ETL_PROCESS_LABEL = "Delete ETL Process";

    private final String SEARCH_INPUT_ID = "data-collection-listSearchAppId";

    private final String NAME_COLUMN_LABEL = "Name";
    private final String DELETE_LABEL = "Delete";

    private EtlDataCollectionsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open ETL Data Collections View")
    public static EtlDataCollectionsPage goToPage(WebDriver driver, String basicURL){
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait,"etl-data-collection");
        return new EtlDataCollectionsPage(driver, wait);
    }

    @Step("I click add new ETL Process")
    public void clickAddNewEtlProcess() {
        clickContextActionAdd();
    }

    @Step("I click edit ETL Process")
    public void clickEditEtlProcess(){
        clickContextActionEdit();
    }

    @Step("I click delete ETL Process")
    public void clickDeleteEtlProcess(){
        clickContextActionDelete();
    }

    @Step("I check if ETL Process: {feedName} exists into the table")
    public Boolean etlProcessExistsIntoTable(String feedName){
        searchFeed(feedName);
        DelayUtils.waitForPageToLoad(driver, wait);

        int numberOfRowsInTable = getNumberOfRowsInTable(NAME_COLUMN_LABEL);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, feedName);

        return numberOfRowsInTable == 1;
    }

    @Step("I select found ETL Process")
    public void selectFoundEtlProcess(){
        getTable(driver, wait).selectRow(0);
    }

    @Step("I confirm the removal of ETL Process")
    public void confirmDelete(){
        confirmDelete(DELETE_LABEL);
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_ETL_PROCESS_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_ETL_PROCESS_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_ETL_PROCESS_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}

