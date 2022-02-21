package com.oss.pages.bigdata.dfe;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.tabs.TabWindowWidget;
import com.oss.pages.BasePage;
import com.oss.untils.FileDownload;

import io.qameta.allure.Step;

import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public abstract class BaseDfePage extends BasePage implements BaseDfePageInterface {

    private static final Logger log = LoggerFactory.getLogger(BaseDfePage.class);

    protected BaseDfePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public OldTable getTable(WebDriver driver, WebDriverWait wait) {
        return OldTable.createById(driver, wait, getTableId());
    }

    public static void openDfePage(WebDriver driver, String basicURL, WebDriverWait wait, String viewName) {
        String pageUrl = String.format("%s/#/view/dfe/%s", basicURL, viewName);
        driver.get(pageUrl);
        waitForPageToLoad(driver, wait);
        log.info("Opening page: {}", pageUrl);
    }

    protected void searchFeed(String searchText) {
        SearchField search = (SearchField) ComponentFactory.create(getSearchId(), Input.ComponentType.SEARCH_FIELD, driver, wait);
        search.clear();
        search.typeValue(searchText);
        log.debug("Searching feed {}", searchText);
    }

    protected int getNumberOfRowsInTable(String columnLabel) {
        return getTable(driver, wait).countRows(columnLabel);
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
        waitForPageToLoad(driver, wait);
        int numberOfRowsInTable = getNumberOfRowsInTable(columnLabel);
        log.trace("Found rows count: {}. Filtered by {}", numberOfRowsInTable, name);
        return numberOfRowsInTable >= 1;
    }

    protected void selectTab(String label) {
        TabWindowWidget.create(driver, wait).selectTabByLabel(label);
    }

    protected void confirmDelete(String deleteLabel) {
        ConfirmationBox.create(driver, wait).clickButtonByLabel(deleteLabel);
    }

    protected void clickRefreshTabTable(String refreshLabel) {
        TabWindowWidget.create(driver, wait).callActionByLabel(refreshLabel);
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
}