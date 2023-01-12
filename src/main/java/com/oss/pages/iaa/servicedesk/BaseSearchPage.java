package com.oss.pages.iaa.servicedesk;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comarch.oss.web.pages.NotificationWrapperPage;
import com.comarch.oss.web.pages.exportguiwizard.ExportGuiWizardPage;
import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DATE_MASK;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.DOWNLOAD_FILE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.ID_ATTRIBUTE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.VIEWS_URL_PATTERN;
import static com.oss.pages.iaa.servicedesk.issue.IssueDetailsPage.DETAILS_PAGE_URL_PATTERN;

public abstract class BaseSearchPage extends BaseSDPage {

    protected static final Logger log = LoggerFactory.getLogger(BaseSearchPage.class);

    public static final String CREATION_TIME_ATTRIBUTE = "createDate";
    public static final String DESCRIPTION_ATTRIBUTE = "incidentDescription";
    private static final String EXPORT_BUTTON_ID = "exportButton";
    private static final String REFRESH_BUTTON_ID = "refreshButton";
    private static final String EXPORT_WIZARD_ID = "exportgui-wizard-widget";
    private static final String TABLE_ATTRIBUTE_TYPE = "ticketOut.issueOut.type";
    private static final int MAX_SEARCH_TIME_6_HOURS = 360;
    public static final String ID_ATTRIBUTE_SEVERITY = "ticketOut.issueOut.severity";

    protected BaseSearchPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public TableWidget getIssueTable() {
        return TableWidget.createById(driver, getTableId(), wait);
    }

    public abstract BaseSearchPage openView(WebDriver driver, String basicURL);

    public abstract String getSearchPageUrl();

    public abstract String getIssueType();

    public abstract String getTableId();

    public void goToPage(WebDriver driver, String basicURL, String pageURL) {
        openPage(driver, String.format(VIEWS_URL_PATTERN, basicURL, pageURL));
    }

    @Step("Check if current url leads to {1} page")
    public boolean isPageOpened(String basicURL) {
        log.info("Current URL is: {}", driver.getCurrentUrl());
        return driver.getCurrentUrl().contains(String.format(VIEWS_URL_PATTERN, basicURL, getSearchPageUrl()));
    }

    public String getIdForNthTicketInTable(int n) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getAttributeFromTable(n, ID_ATTRIBUTE);
    }

    public String getSeverityForNthTicketInTable(int n){
        DelayUtils.waitForPageToLoad(driver, wait);
        return getAttributeFromTable(n, ID_ATTRIBUTE_SEVERITY);
    }

    private String getAttributeFromTable(int index, String attributeName) {
        String attributeValue = getIssueTable().getCellValue(index, attributeName);
        log.info("Got value for {} attribute: {}", attributeName, attributeValue);
        return attributeValue;
    }

    @Step("Filter by: set in component with id {attributeName} value: {attributeValue}")
    public void filterBy(String attributeName, String attributeValue) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getIssueTable().searchByAttribute(attributeName, attributeValue);
        log.info("Filtering issues by component with id {} set to {}", attributeName, attributeValue);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void filterByDate(String attributeName, String attributeValue) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getIssueTable().searchByAttribute(attributeName, Input.ComponentType.TEXT_FIELD, attributeValue);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public void clearAllFilters() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getIssueTable().clearAllFilters();
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    public boolean isIssueTableEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getIssueTable().hasNoData();
    }

    @Step("Click Export button in GraphQL Page")
    public ExportGuiWizardPage clickExportInSearchTable() {
        clickExportFromTable(getTableId());
        DelayUtils.waitForPageToLoad(driver, wait);
        return new ExportGuiWizardPage(driver, wait, EXPORT_WIZARD_ID);
    }

    @Override
    public void clickExportFromTable(String tableId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableWidget.createById(driver, getTableId(), wait).callAction(ActionsContainer.KEBAB_GROUP_ID, EXPORT_BUTTON_ID);
        log.info("Clicking Export Button");
    }

    @Step("Count number of visible issues")
    public int countIssuesInTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getIssueTable().getRowsNumber();
    }

    @Step("Click Refresh button")
    public void clickRefresh() {
        getIssueTable().callAction(ActionsContainer.KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Refresh Button");
    }

    @Step("Check if Tickets Table is visible")
    public boolean checkTicketsTableVisibility() {
        if (isIssueTableEmpty()) {
            log.info("Tickets Table exists and is empty");
            return true;
        } else if (!isIssueTableEmpty()) {
            log.info("Tickets Table exists and has some data");
            return true;
        } else {
            log.info("Ticket table is not visible");
            return false;
        }
    }

    @Step("I get Id for first Ticket excluding {excludedTicketType} type")
    public String getIdOfFirstFilteredTicket(String excludedTicketType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        int rowsNumber = getIssueTable().countRows();
        for (int row = 0; row < rowsNumber; row++) {
            if (!getAttributeFromTable(row, TABLE_ATTRIBUTE_TYPE).equals(excludedTicketType)) {
                return getIdForNthTicketInTable(row);
            }
        }
        log.info("Only {} tickets are available on the list", excludedTicketType);
        return getIdForNthTicketInTable(0);
    }

    public void exportFromSearchViewTable(String exportWizardId) {
        NotificationWrapperPage notificationWrapperPage = new NotificationWrapperPage(driver);
        notificationWrapperPage.openNotificationPanel();
        notificationWrapperPage.clearNotifications();
        if (!isIssueTableEmpty()) {
            int minutes = 60;
            filterByDate(BaseSearchPage.CREATION_TIME_ATTRIBUTE, getTimePeriodForLastNMinutes(minutes));
            while (isIssueTableEmpty()) {
                minutes += 30;
                if (minutes > MAX_SEARCH_TIME_6_HOURS) {
                    throw new RuntimeException("No tickets to export created within last 6 hours");
                }
                filterByDate(BaseSearchPage.CREATION_TIME_ATTRIBUTE, getTimePeriodForLastNMinutes(minutes));
            }
            clickExportInSearchTable();
            ExportGuiWizardPage exportWizardPage = new ExportGuiWizardPage(driver, wait, exportWizardId);
            String exportFileName = "Selenium test " + BaseSDPage.getDateFormat();
            exportWizardPage.setFileName(exportFileName);
            exportWizardPage.changeDateMaskContains(DATE_MASK);
            exportWizardPage.closeTheWizard();
            notificationWrapperPage.openNotificationPanel();
            notificationWrapperPage.waitForExportFinish();
            notificationWrapperPage.clickDownload();
            notificationWrapperPage.clearNotifications();
            attachFileToReport(DOWNLOAD_FILE);
        }
    }

    public String getIssueID(int index) {
        return getIssueTable().getCellValue(index, ID_ATTRIBUTE);
    }

    @Step("I open details view for {rowIndex} issue in Issue table")
    public IssueDetailsPage openIssueDetailsViewFromSearchPage(String rowIndex, String basicURL) {
        String issueId = getIssueTable().getCellValue(Integer.parseInt(rowIndex), ID_ATTRIBUTE);
        openPage(driver, String.format(DETAILS_PAGE_URL_PATTERN, basicURL, getIssueType(), issueId));
        return new IssueDetailsPage(driver, wait);
    }
}