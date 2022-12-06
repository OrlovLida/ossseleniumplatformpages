package com.oss.pages.iaa.acd.settingsview;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.iaa.acd.BaseACDPage;

import io.qameta.allure.Step;

import java.time.Duration;

public class AssignmentManagementPage extends BaseACDPage {

    private static final Logger log = LoggerFactory.getLogger(AssignmentManagementPage.class);

    private static final String ASSIGNMENT_MANAGEMENT_TABLE_ID = "assignmentManagementTableId";
    private static final String CREATE_NEW_RULE_BUTTON = "assignmentManagementButtonId-4";
    private static final String DELETE_LABEL = "Delete";
    private String ruleID;

    private final OldTable table;

    public AssignmentManagementPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        table = OldTable.createById(driver, wait, ASSIGNMENT_MANAGEMENT_TABLE_ID);
    }

    @Step("I open Settings View")
    public static AssignmentManagementPage goToPage(WebDriver driver, String suffixURL, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(150));

        String pageUrl = String.format(suffixURL, basicURL);
        driver.get(pageUrl);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", pageUrl);

        return new AssignmentManagementPage(driver, wait);
    }

    @Step("I click Create new rule")
    public void clickCreateNewRule() {
        Button.createById(driver, CREATE_NEW_RULE_BUTTON).click();
        log.info("Clicking Create new rule button");
    }

    @Step("I search for Assignment Management rule")
    public Boolean searchingThroughTable(String comboBoxId, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);

        if (!isDataInAssignmentManagementTable()) {
            log.info("Assignment Management table has no data");
            return false;
        }
        ComponentFactory.create(comboBoxId, driver, wait).setSingleStringValue(value);
        log.info("I am searching for created rule");
        return true;
    }

    @Step("I check if there is data in Assignment Management table")
    public boolean isDataInAssignmentManagementTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I am checking if there is data in Action Templates table");
        return !table.hasNoData();
    }

    @Step("I select rule")
    public void selectFirstRuleFromTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I am selecting Action Template");
        table.selectRow(0);
    }

    @Step("I click tabsContainer button")
    public void clickTabsContainerButton(String buttonId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I am clicking tabsContainer button: {}", buttonId);
        Button.createById(driver, buttonId).click();
    }

    @Step("I get new rule id")
    public String getNewRuleId() {

        if (!isDataInAssignmentManagementTable()) {
            log.info("Table doesn't have data for chosen filters. ID cannot be found.");
        } else {
            ruleID = table.getCellValue(0, "Automatic Distribution Id");
        }
        return ruleID;
    }

    @Step("I get current name of the rule")
    public String getRuleName() {
        return table.getCellValue(0, "Automatic Distribution Name");
    }

    @Step("I check if status value is changed")
    public boolean isStatusValueInactive() {
        String firstRowInTable = table.getCellValue(0, "Status");
        log.info("Value of first row for creation type is: {}", firstRowInTable);

        if (!firstRowInTable.equals("Inactive")) {
            log.info("Selected rule has different status than Inactive");
            return false;
        }
        return true;
    }

    @Step("I click delete rule")
    public void deleteRule(String buttonId) {
        clickTabsContainerButton(buttonId);
        log.info("I am confirming removal of the rule");
        ConfirmationBox.create(driver, wait).clickButtonByLabel(DELETE_LABEL);
    }
}
