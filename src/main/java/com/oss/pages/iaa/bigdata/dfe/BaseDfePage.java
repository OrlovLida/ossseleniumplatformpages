package com.oss.pages.iaa.bigdata.dfe;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.untils.FileDownload;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public abstract class BaseDfePage extends BasePage implements BaseDfePageInterface {

    private static final Logger log = LoggerFactory.getLogger(BaseDfePage.class);
    private static final String SAVE_LABEL = "Save";
    private static final String CATEGORY_COLUMN_LABEL = "Category";
    private static final String CATEGORY_ID = "category";

    protected BaseDfePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static void openDfePage(WebDriver driver, String basicURL, WebDriverWait wait, String viewName) {
        String pageUrl = String.format("%s/#/view/dfe/%s", basicURL, viewName);
        driver.get(pageUrl);
        waitForPageToLoad(driver, wait);
        log.info("Opening page: {}", pageUrl);
    }

    public OldTable getTable() {
        return OldTable.createById(driver, wait, getTableId());
    }

    @Step("Attach downloaded file to report")
    public void attachFileToReport(String fileName) {
        FileDownload.attachDownloadedFileToReport(fileName);
        log.info("Attaching downloaded file to report");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Check if file is not empty")
    public boolean checkIfFileIsNotEmpty(String fileName) {
        log.info("Checking if file is not empty");
        return FileDownload.checkIfFileIsNotEmpty(fileName);
    }

    @Step("Get category name")
    public String getCategoryName(int index) {
        return getTable().getCellValue(index, CATEGORY_COLUMN_LABEL);
    }

    @Step("Search category")
    public void searchCategories(String category) {
        waitForPageToLoad(driver, wait);
        ComponentFactory.create(CATEGORY_ID, driver, wait).setSingleStringValue(category);
        log.debug("Filled category with: {}", category);
    }

    @Step("Check if Table is empty")
    public Boolean isTabTableEmpty(String tableId) {
        log.info("Check if table with id: {} is empty", tableId);
        waitForPageToLoad(driver, wait);
        return OldTable
                .createById(driver, wait, tableId)
                .hasNoData();
    }

    protected void searchFeed(String searchText) {
        ComponentFactory.create(getSearchId(), driver, wait).setSingleStringValue(searchText);
        log.debug("Searching feed {}", searchText);
    }

    protected int getNumberOfRowsInTable(String columnLabel) {
        return getTable().countRows(columnLabel);
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
        getTable().callActionByLabel(actionLabel);
        log.debug("Clicking context action: {}", actionLabel);
    }

    protected void clickTabsContextAction(String widgetId, String actionLabel) {
        TabsWidget.createById(driver, wait, widgetId).callActionByLabel(actionLabel);
        log.debug("Clicking context action: {}", actionLabel);
    }

    protected Boolean feedExistIntoTable(String name, String columnLabel) {
        waitForPageToLoad(driver, wait);
        searchFeed(name);
        waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(columnLabel);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, name);
        return numberOfRowsInTable >= 1;
    }

    protected void selectTab(String widgetId, String label) {
        TabsWidget.createById(driver, wait, widgetId).selectTabByLabel(label);
        waitForPageToLoad(driver, wait);
    }

    protected void confirmDelete(String deleteLabel) {
        ConfirmationBox.create(driver, wait).clickButtonByLabel(deleteLabel);
    }

    protected void confirmDeactivation() {
        ConfirmationBox.create(driver, wait).clickButtonByLabel(SAVE_LABEL);
    }

    protected void clickRefreshTabTable(String widgetId, String refreshLabel) {
        TabsWidget.createById(driver, wait, widgetId).callActionByLabel(refreshLabel);
        log.debug("Click context action: {}", refreshLabel);
    }

    protected LocalDateTime lastLogTime(String tabId, String columnLabel) {
        String lastLog = OldTable
                .createById(driver, wait, tabId)
                .getCellValue(0, columnLabel);

        LocalDateTime lastLogTime = LocalDateTime.parse(lastLog, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("Last Log Time is: {}", lastLogTime);

        return lastLogTime;
    }

    protected boolean isLastLogTimeFresh(LocalDateTime lastLogTime) {
        return ChronoUnit.MINUTES.between(lastLogTime, LocalDateTime.now()) < 60;
    }

    protected String checkLogStatus(String logsTableTabId, String columnLabel) {
        waitForPageToLoad(driver, wait);
        return OldTable
                .createById(driver, wait, logsTableTabId)
                .getCellValue(0, columnLabel);
    }

    protected String getValueFromPropertyPanel(String propertyPanelId, String propertyName) {
        String propertyValue = OldPropertyPanel.createById(driver, wait, propertyPanelId).getPropertyValue(propertyName);
        log.info("Value of: {} is: {}", propertyName, propertyValue);
        return propertyValue;
    }
}