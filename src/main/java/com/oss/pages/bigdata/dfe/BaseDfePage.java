package com.oss.pages.bigdata.dfe;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tabswidget.TabWindowWidget;
import com.oss.framework.widgets.tabswidget.TabsInterface;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class BaseDfePage extends BasePage implements BaseDfePageInterface {

    private static final Logger log = LoggerFactory.getLogger(BaseDfePage.class);

    public BaseDfePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public OldTable getTable(WebDriver driver, WebDriverWait wait) {
        return OldTable.createByComponentDataAttributeName(driver, wait, getTableId());
    }

    public static void openDfePage(WebDriver driver, String basicURL, WebDriverWait wait, String viewName) {
        String pageUrl = String.format("%s/#/view/dfe/%s", basicURL, viewName);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opening page: {}", pageUrl);
    }

    public void searchFeed(String searchText) {
        SearchField search = (SearchField) ComponentFactory.create(getSearchId(), Input.ComponentType.SEARCH_FIELD, driver, wait);
        search.clear();
        search.typeValue(searchText);
        log.debug("Searching feed {}", searchText);
    }

    public int getNumberOfRowsInTable(String columnLabel) {
        return getTable(driver, wait).getNumberOfRowsInTable(columnLabel);
    }

    protected void clickContextActionAdd() {
        clickContextAction(getContextActionAddLabel());
    }

    protected void clickContextActionEdit() {
        clickContextAction(getContextActionEditLabel());
    }

    protected void clickContextActionDelete() {
        clickContextAction(getContextActionDeleteLabel());
    }

    protected void clickContextAction(String actionLabel) {
        getTable(driver, wait).callActionByLabel(actionLabel);
        log.debug("Clicking context action: {}", actionLabel);
    }

    protected void clickTabsContextAction(String actionLabel) {
        TabWindowWidget.create(driver, wait).callActionByLabel(actionLabel);
        log.debug("Clicking context action: {}", actionLabel);
    }

    protected Boolean feedExistIntoTable(String name, String columnLabel) {
        searchFeed(name);
        DelayUtils.waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(columnLabel);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, name);
        return numberOfRowsInTable == 1;
    }

    protected void selectTab(String label) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsInterface tab = TabWindowWidget.create(driver, wait);
        tab.selectTabByLabel(label);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I confirm the removal")
    public void confirmDelete(String deleteLabel) {
        ConfirmationBox confirmationBox = ConfirmationBox.create(driver, wait);
        confirmationBox.clickButtonByLabel(deleteLabel);
    }
}