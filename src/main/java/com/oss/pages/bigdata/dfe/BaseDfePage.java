package com.oss.pages.bigdata.dfe;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

abstract public class BaseDfePage extends BasePage implements BaseDfePageInterface {

    public BaseDfePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public OldTable getTable(WebDriver driver, WebDriverWait wait) {
        return OldTable.createByComponentDataAttributeName(driver, wait, getTableId());
    }

    public static void openDfePage(WebDriver driver, String basicURL, WebDriverWait wait, String viewName){
        driver.get(String.format("%s/#/view/dfe/%s", basicURL, viewName));
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
    }

    public void searchFeed(String searchText){
        SearchField search = (SearchField) ComponentFactory.create(getSearchId(), Input.ComponentType.SEARCH_FIELD, driver, wait);
        search.clear();
        search.typeValue(searchText);
    }

    public int getNumberOfRowsInTable(String columnLabel){
        return getTable(driver, wait).getNumberOfRowsInTable(columnLabel);
    }

    protected void clickContextActionAdd(){
        clickContextAction(getContextActionAddLabel());
    }

    protected void clickContextActionEdit(){
        clickContextAction(getContextActionEditLabel());
    }

    protected void clickContextActionDelete(){
        clickContextAction(getContextActionDeleteLabel());
    }

    protected void clickContextAction(String actionLabel) {
        getTable(driver, wait).callActionByLabel(actionLabel);
    }



}
