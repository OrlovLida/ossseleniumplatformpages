package com.oss.pages.acd.scenarioSummaryView;

import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.serviceDeskAdvancedSearch.ServiceDeskAdvancedSearch;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.acd.BaseACDPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.MULTI_SEARCH_FIELD;

public class ApdScenarioSummaryViewPage extends BaseACDPage {

    private static final Logger log = LoggerFactory.getLogger(ApdScenarioSummaryViewPage.class);

    private final String DETECTED_ISSUES_WINDOW_ID = "DetectedIssuesWindowId";

    private final OldTable table;
    private final ServiceDeskAdvancedSearch advancedSearch;

    public ApdScenarioSummaryViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

        table = OldTable.createByComponentDataAttributeName(driver, wait, DETECTED_ISSUES_WINDOW_ID);
        advancedSearch = ServiceDeskAdvancedSearch.create(driver, wait, DETECTED_ISSUES_WINDOW_ID);
    }

    @Step("I Open APD Scenario Summary View")
    public static ApdScenarioSummaryViewPage goToPage(WebDriver driver, String suffixURL, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 150);

        String pageUrl = String.format(suffixURL, basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new ApdScenarioSummaryViewPage(driver, wait);
    }

    @Step("Check if there is data in issues table")
    public Boolean isDataInIssuesTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Checking if there is data in issues table");
        return !table.hasNoData();
    }

    @Step("Set value of Issue Id multiSearch")
    public void setValueOfIssueIdSearch() {
        if (!isDataInIssuesTable()) {
            log.info("Table doesn't have data for chosen filters. Issue ID cannot be set");
        } else {
            String firstIdInTable = table.getCellValue(0, "Issue Id");
            log.info("Setting value of Issue Id");
            ComponentFactory.create("id", MULTI_SEARCH_FIELD, driver, wait)
                    .setSingleStringValue(firstIdInTable);
            DelayUtils.sleep();
        }
    }

    @Step("Clear multiComboBox")
    public void clearMultiComboBox(String multiComboBoxId) {
        Input issueTypeComboBox = advancedSearch.getComponent(multiComboBoxId, Input.ComponentType.MULTI_COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
        issueTypeComboBox.clear();
        log.info("Clearing multiComboBox");
    }

    @Step("Clear multiSearch")
    public void clearMultiSearch(String multiSearchId) {
        Input issueIdSearch = advancedSearch.getComponent(multiSearchId, Input.ComponentType.MULTI_SEARCH_FIELD);
        issueIdSearch.clear();
        log.info("Clearing multiSearch");
    }

}
