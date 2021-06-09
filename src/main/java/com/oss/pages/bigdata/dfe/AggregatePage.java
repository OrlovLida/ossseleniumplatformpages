package com.oss.pages.bigdata.dfe;

import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AggregatePage extends BaseDfePage {

    private static final Logger log = LoggerFactory.getLogger(AggregatePage.class);

    private static final String TABLE_ID = "aggregates-tableAppId";

    private final String ADD_NEW_AGGREGATE_LABEL = "Add New Aggregate";
    private final String EDIT_AGGREGATE_LABEL = "Edit Aggregate";
    private final String DELETE_AGGREGATE_LABEL = "Delete Aggregate";
    private final String SEARCH_INPUT_ID = "aggregates-tableSearchAppId";

    private final String NAME_COLUMN_LABEL = "Name";
    private final String DELETE_LABEL = "Delete";

    private AggregatePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I Open Aggregates View")
    public static AggregatePage goToPage(WebDriver driver, String basicURL){
        WebDriverWait wait = new WebDriverWait(driver, 45);

        BaseDfePage.openDfePage(driver, basicURL, wait, "aggregates");
        return new AggregatePage(driver, wait);
    }

    @Step("I click add new Aggregate")
    public void clickAddNewAggregate(){
        clickContextActionAdd();
    }

    @Step("I click edit Aggregate")
    public void clickEditAggregate(){
        clickContextActionEdit();
    }

    @Step("I click delete Aggregate")
    public void clickDeleteAggregate(){
        clickContextActionDelete();
    }

    @Step("I check if Aggregate: {aggregateName} exists into the table")
    public Boolean aggregateExistsIntoTable(String aggregateName){
        DelayUtils.sleep(2000);
        searchFeed(aggregateName);
        DelayUtils.waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(NAME_COLUMN_LABEL);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, aggregateName);
        return numberOfRowsInTable == 1;
    }

    @Step("I select found Aggegate")
    public void selectFoundAggregate(){
        getTable(driver, wait).selectRow(0);
    }

    @Step("I confirm the removal of Aggregate")
    public void confirmDelete(){
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(DELETE_LABEL);
    }

    @Override
    public String getTableId() {
        return TABLE_ID;
    }

    @Override
    public String getContextActionAddLabel() {
        return ADD_NEW_AGGREGATE_LABEL;
    }

    @Override
    public String getContextActionEditLabel() {
        return EDIT_AGGREGATE_LABEL;
    }

    @Override
    public String getContextActionDeleteLabel() {
        return DELETE_AGGREGATE_LABEL;
    }

    @Override
    public String getSearchId() {
        return SEARCH_INPUT_ID;
    }
}
