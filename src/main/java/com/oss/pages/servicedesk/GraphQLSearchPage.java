package com.oss.pages.servicedesk;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.contextactions.ActionsContainer;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.TableWidget;
import com.oss.pages.platform.NotificationWrapperPage;
import com.oss.pages.servicedesk.issue.wizard.ExportWizardPage;

import io.qameta.allure.Step;

import static com.oss.pages.servicedesk.URLConstants.VIEWS_URL_PATTERN;

public abstract class GraphQLSearchPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(GraphQLSearchPage.class);

    public static final String ID_ATTRIBUTE = "id";
    public static final String CREATION_TIME_ATTRIBUTE = "createDate";
    public static final String DESCRIPTION_ATTRIBUTE = "incidentDescription";

    private static final String EXPORT_BUTTON_ID = "exportButton";
    private static final String REFRESH_BUTTON_ID = "refreshButton";
    private static final String EXPORT_WIZARD_ID = "exportgui-wizard-widget";
    private static final String EXPORT_FILE_NAME = "Selenium test " + BaseSDPage.getDateFormat();
    private static final String DATE_MASK = "ISO Local Date";
    private static final String DOWNLOAD_FILE = "Selenium test*.csv";
    private static final int MAX_SEARCH_TIME_6_HOURS = 360;

    protected GraphQLSearchPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public abstract TableWidget getIssueTable();

    public void goToPage(WebDriver driver, String basicURL, String pageURL) {
        openPage(driver, String.format(VIEWS_URL_PATTERN, basicURL, pageURL));
    }

    @Step("I filter issues by text attribute {attributeName} set to {attributeValue}")
    public void filterByTextField(String attributeName, String attributeValue) {
        log.info("Filtering issues by text attribute {} set to {}", attributeName, attributeValue);
        filterBy(attributeName, attributeValue, Input.ComponentType.TEXT_FIELD);
    }

    @Step("I filter issues by combo-box attribute {attributeName} set to {attributeValue}")
    public void filterByComboBox(String attributeName, String attributeValue) {
        log.info("Filtering issues by combo-box attribute {} set to {}", attributeName, attributeValue);
        filterBy(attributeName, attributeValue, Input.ComponentType.MULTI_COMBOBOX);
    }

    public String getIdForNthTicketInTable(int n) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getAttributeFromTable(n, ID_ATTRIBUTE);
    }

    private String getAttributeFromTable(int index, String attributeName) {
        String attributeValue = getIssueTable().getCellValue(index, attributeName);
        log.info("Got value for {} attribute: {}", attributeName, attributeValue);
        return attributeValue;
    }

    public TableWidget getTable(WebDriver driver, WebDriverWait wait, String tableWidgetId) {
        return TableWidget.createById(driver, tableWidgetId, wait);
    }

    public void filterBy(String attributeName, String attributeValue, Input.ComponentType componentType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        getIssueTable().searchByAttribute(attributeName, componentType, attributeValue);
    }

    public boolean isIssueTableEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return getIssueTable().hasNoData();
    }

    @Step("Click Export button in GraphQL Page")
    public ExportWizardPage clickExportInTicketSearch() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getIssueTable().callAction(ActionsContainer.KEBAB_GROUP_ID, EXPORT_BUTTON_ID);
        log.info("Clicking Export Button");
        return new ExportWizardPage(driver, wait, EXPORT_WIZARD_ID);
    }

    @Step("Count number of visible issues")
    public int countIssuesInTable() {
        return getIssueTable().getRowsNumber();
    }

    @Step("Click Refresh button")
    public void clickRefresh() {
        getIssueTable().callAction(ActionsContainer.KEBAB_GROUP_ID, REFRESH_BUTTON_ID);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Clicking Refresh Button");
    }

    public void exportFromSearchViewTable(String exportWizardId) {
        NotificationWrapperPage notificationWrapperPage = new NotificationWrapperPage(driver);
        notificationWrapperPage.openNotificationPanel();
        notificationWrapperPage.clearNotifications();
        if (!isIssueTableEmpty()) {
            int minutes = 60;
            filterByTextField(GraphQLSearchPage.CREATION_TIME_ATTRIBUTE, getTimePeriodForLastNMinutes(minutes));
            while (isIssueTableEmpty()) {
                minutes += 30;
                if (minutes > MAX_SEARCH_TIME_6_HOURS) {
                    throw new RuntimeException("No tickets to export created within last 6 hours");
                }
                filterByTextField(GraphQLSearchPage.CREATION_TIME_ATTRIBUTE, getTimePeriodForLastNMinutes(minutes));
            }
            clickExportInTicketSearch();
            ExportWizardPage exportWizardPage = new ExportWizardPage(driver, wait, exportWizardId);
            exportWizardPage.fillFileName(EXPORT_FILE_NAME);
            exportWizardPage.fillDateMask(DATE_MASK);
            exportWizardPage.clickAccept();
            notificationWrapperPage.openNotificationPanel();
            notificationWrapperPage.waitForExportFinish();
            notificationWrapperPage.clickDownload();
            notificationWrapperPage.clearNotifications();
            attachFileToReport(DOWNLOAD_FILE);
        }
    }

    @Step("Check if current url leads to search page page")
    public boolean isSearchPageOpened(String basicURL, String pageURL) {
        log.info("Current URL is: {}", driver.getCurrentUrl());
        return driver.getCurrentUrl().equals(String.format(VIEWS_URL_PATTERN, basicURL, pageURL));
    }

    public String getIssueID(int index) {
        return getIssueTable().getCellValue(index, ID_ATTRIBUTE);
    }
}
