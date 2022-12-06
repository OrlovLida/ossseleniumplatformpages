package com.oss.pages.iaa.faultmanagement;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.search.AdvancedSearch;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.iaa.widgets.components.InfoComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.framework.widgets.tabs.TabsWidget;
import com.oss.pages.BasePage;
import com.oss.pages.exportguiwizard.ExportGuiWizardPage;
import com.oss.untils.FileDownload;

import io.qameta.allure.Step;

public class KEDBLibraryPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(KEDBLibraryPage.class);
    public static final String KEDB_LIBRARY_URL = "%s/#/views/fault-management/kedb/library?perspective=LIVE";
    public static final String TABLE_ID = "kedb-library-table";
    public static final String NAME_FILTER_ID = "name";
    public static final String NAME_COLUMN_DATA_COL_ID = "name";
    public static final String TABS_CONTAINER_ID = "AREA3";
    public static final String JAVA_CONTENT_TAB_ID = "card-area3-java-content";
    public static final String HISTORY_TAB_ID = "card-area3-history";
    public static final String COMPILATION_STATUS_TAB_ID = "card-area3-compilation-status";
    public static final String HISTORY_TABLE_ID = "table-area3-history";
    public static final String REFRESH_BUTTON_ID = "refreshButton";
    public static final String EXPORT_BUTTON_ID = "exportButton";
    private static final String EXPORT_WIZARD_ID = "exportgui-mainview_prompt-card";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    public KEDBLibraryPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static KEDBLibraryPage goToKEDBLibraryPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(90));
        String url = String.format(KEDB_LIBRARY_URL, basicURL);
        driver.get(url);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", url);
        return new KEDBLibraryPage(driver, wait);
    }

    @Step("I enable column {columnLabel} into table")
    public void enableColumnInTable(String columnLabel) {
        getKEDBTable().enableColumnByLabel(columnLabel);
        log.info("Enabling column with id: {}", columnLabel);
    }

    @Step("I disable column {columnLabel} into table")
    public void disableColumnInTable(String columnLabel) {
        getKEDBTable().disableColumnByLabel(columnLabel);
        log.info("Disabling column with id: {}", columnLabel);
    }

    @Step("I check if column with Header {columnHeader} is present in the Table")
    public boolean isColumnInTable(String columnHeader) {
        return getKEDBTable().getActiveColumnHeaders().contains(columnHeader);
    }

    @Step("Search for lib: {libName} by setting filter in advanced search")
    public void setFilterToLibName(String libName) {
        getAdvancedSearch().setFilter(NAME_FILTER_ID, libName);
        getAdvancedSearch().clickApply();
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Search for lib: {} by setting filter in advanced search", libName);
    }

    @Step("Get first Lib name from the table")
    public String getFirstLibName() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getKEDBTable().getCellValue(0, NAME_COLUMN_DATA_COL_ID);
    }

    @Step("Select first row in the table")
    public void selectFirstRow() {
        getKEDBTable().selectRow(0);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Select Java Content Tab")
    public void selectJavaContentTab() {
        selectTab(JAVA_CONTENT_TAB_ID);
        log.info("Selecting Java Content Tab");
    }

    @Step("Select History tab")
    public void selectHistoryTab() {
        selectTab(HISTORY_TAB_ID);
        log.info("Selecting History Tab");
    }

    @Step("Select Compilation Status tab")
    public void selectCompilationStatusTab() {
        selectTab(COMPILATION_STATUS_TAB_ID);
        log.info("Selecting Compilation Status Tab");
    }

    @Step("Get text from Compilation Status tab")
    public String getCompilationStatus() {
        log.info("Getting compilation status text");
        return getInfoComponent().getInfoComponentMessage();
    }

    @Step("Check presence of Success Icon")
    public boolean isIconSuccessPresent() {
        log.info("Checking if Success Icon is present");
        return getInfoComponent().isIconSuccessPresent();
    }

    @Step("Check if there are any errors")
    public boolean isAnyError() {
        return !SystemMessageContainer.create(driver, wait).getErrors().isEmpty();
    }

    @Step("Get number of headers in History Table")
    public int getNumOfHistoryTableHeaders() {
        return TableComponent.createById(driver, wait, HISTORY_TABLE_ID).getColumnHeaders().size();
    }

    @Step("Get number of KEDB libraries in Table")
    public int getNumOfLibraries() {
        return getKEDBTable().getRowsNumber();
    }

    @Step("Click Refresh button")
    public void clickRefresh() {
        getKEDBTable().callAction(ActionsContainer.KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click Export button")
    public ExportGuiWizardPage clickExport() {
        getKEDBTable().callAction(ActionsContainer.KEBAB_GROUP_ID, EXPORT_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        return new ExportGuiWizardPage(driver, wait, EXPORT_WIZARD_ID);
    }

    @Step("Attach downloaded file to report")
    public void attachFileToReport(String fileName) {
        FileDownload.attachDownloadedFileToReport(fileName);
        log.info("Attaching downloaded file to report");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public boolean checkIfFileIsNotEmpty(String fileName) {
        log.info("Checking if file is not empty");
        return FileDownload.checkIfFileIsNotEmpty(fileName);
    }

    public static String createFileName(String baseFileName) {
        return baseFileName + LocalDateTime.now().format(DATE_FORMATTER);
    }

    private InfoComponent getInfoComponent() {
        return InfoComponent.create(driver, wait);
    }

    private void selectTab(String tabId) {
        TabsWidget.createById(driver, wait, TABS_CONTAINER_ID).selectTabById(tabId);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private AdvancedSearch getAdvancedSearch() {
        return getKEDBTable().getAdvancedSearch();
    }

    private TableWidget getKEDBTable() {
        return TableWidget.createById(driver, TABLE_ID, wait);
    }
}
