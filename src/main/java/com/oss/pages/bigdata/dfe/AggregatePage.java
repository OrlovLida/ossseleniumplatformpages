package com.oss.pages.bigdata.dfe;

import com.oss.framework.components.contextactions.OldActionsContainer;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AggregatePage extends BasePage {

    private final String ADD_NEW_AGGREGATE_LABEL = "Add New Aggregate";
    private final String SEARCH_INPUT_ID = "aggregates-tableSearchAppId";
    private final String TABLE_ID = "aggregates-tableAppId";
    private final String NAME_COLUMN_LABEL = "Name";
    private final OldTable aggregatesTable;


    public AggregatePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        this.aggregatesTable = OldTable.createByComponentDataAttributeName(driver, wait, TABLE_ID);
    }

    public void searchAggregate(String searchText){
        SearchField search = (SearchField) ComponentFactory.create(SEARCH_INPUT_ID, Input.ComponentType.SEARCH_FIELD, driver, wait);
        search.typeValue(searchText);
    }

    public int getNumberOfRowsInTable(){
        return aggregatesTable.getNumberOfRowsInTable(NAME_COLUMN_LABEL);
    }

    @Step("I Open Aggregates View")
    public static AggregatePage goToAggregatePage(WebDriver driver, String basicURL){
        driver.get(String.format("%s/#/view/sqm/aggregates", basicURL));
        WebDriverWait wait = new WebDriverWait(driver, 45);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new AggregatePage(driver, wait);
    }

    @Step("I click add new Aggregate")
    public void clickAddNewAggregate(){
        OldActionsContainer contextActions = OldActionsContainer.createFromParent(driver, wait, driver.findElement(By.className("OssWindow")));
        contextActions.callActionByLabel(ADD_NEW_AGGREGATE_LABEL);
    }

    @Step("I check if Aggregate: {aggregateName} exists into the table")
    public Boolean aggregateExistsIntoTable(String aggregateName){
        searchAggregate(aggregateName);
        DelayUtils.waitForPageToLoad(driver, wait);
        return getNumberOfRowsInTable() == 1;
    }

}
