package com.oss.pages.acd.scenarioSummaryView;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.serviceDeskAdvancedSearch.ServiceDeskAdvancedSearch;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.pages.acd.BaseACDPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;

public class AsdScenarioSummaryViewPage extends BaseACDPage {

    private static final Logger log = LoggerFactory.getLogger(AsdScenarioSummaryViewPage.class);

    private final String DETECTED_ISSUES_WINDOW_ID = "DetectedIssuesWindowId";
    private final String VISUALIZATION_TYPE_ID = "widgetType-input";
    private final String ATTRIBUTE_ID = "attribute1Id-input";

    private final OldTable table;
    private final ServiceDeskAdvancedSearch advancedSearch;
    private final Wizard attributeWizard;
    private final Wizard visualizationTypeWizard;

    public AsdScenarioSummaryViewPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

        table = OldTable.createByComponentDataAttributeName(driver, wait, DETECTED_ISSUES_WINDOW_ID);
        advancedSearch = ServiceDeskAdvancedSearch.create(driver, wait, DETECTED_ISSUES_WINDOW_ID);
        visualizationTypeWizard = Wizard.createWizard(driver, wait);
        attributeWizard = Wizard.createWizard(driver, wait);
    }

    @Step("I Open ASD Scenario Summary View")
    public static AsdScenarioSummaryViewPage goToPage(WebDriver driver, String suffixURL, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 150);

        String pageUrl = String.format(suffixURL, basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new AsdScenarioSummaryViewPage(driver, wait);
    }

    @Step("Choose Predefined Filter type")
    public void chooseVisualizationType(String visualizationType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        visualizationTypeWizard.setComponentValue(VISUALIZATION_TYPE_ID, visualizationType, COMBOBOX);
        log.debug("Setting visualization type: {}", visualizationType);
    }

    @Step("Choose attribute for predefined filter")
    public void chooseAttribute(String attributeName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        attributeWizard.setComponentValue(ATTRIBUTE_ID, attributeName, COMBOBOX);
        log.debug("Setting attribute for predefined filter: {}", attributeName);
    }

    @Step("I click Accept button")
    public void savePredefinedFilter() {
        attributeWizard.clickAcceptOldWizard();
        log.info("I save predefined filter by clicking 'Accept'");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Set value of Issue Id multiSearch")
    public void setValueOfIssueIdSearch() {
        Input issueIdSearch = advancedSearch.getComponent("id", Input.ComponentType.MULTI_SEARCH_FIELD);
        String firstIdInTable = table.getCellValue(0, "Issue Id");
        log.info("Setting value of Issue Id");
        issueIdSearch.setValue(Data.createSingleData(firstIdInTable));
        DelayUtils.sleep();
    }

    @Step("Set value in multiComboBox")
    public void setValueInMultiComboBox(String attributeName, String inputValue) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Input multiComboBox = advancedSearch.getComponent(attributeName, Input.ComponentType.MULTI_COMBOBOX);
        multiComboBox.setValue(Data.createSingleData(inputValue));
        log.info("Setting value of {} attribute", inputValue, " as {}", attributeName);
    }

    @Step("Clear multiComboBox")
    public void clearMultiComboBox(String multiComboBoxId) {
        Input issueTypeComboBox = advancedSearch.getComponent(multiComboBoxId, Input.ComponentType.MULTI_COMBOBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
        issueTypeComboBox.clear();
        log.info("Clearing multicombobox");
    }

    @Step("Clear multiSearch")
    public void clearMultiSearch(String multiSearchId) {
        Input issueIdSearch = advancedSearch.getComponent(multiSearchId, Input.ComponentType.MULTI_SEARCH_FIELD);
        issueIdSearch.clear();
        log.info("Clearing multisearch");
    }

    @Step("Check if data in issues table is empty")
    public Boolean checkDataInIssuesTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Checking if issues table is empty");
        return table.hasNoData();
    }

}