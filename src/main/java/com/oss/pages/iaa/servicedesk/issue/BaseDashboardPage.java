package com.oss.pages.iaa.servicedesk.issue;

import java.io.IOException;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.pages.iaa.servicedesk.BaseSDPage;

import io.qameta.allure.Step;

import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.ID_UPPERCASE_ATTRIBUTE;
import static com.oss.pages.iaa.servicedesk.ServiceDeskConstants.PREDEFINED_DASHBOARD_URL_PATTERN;

public abstract class BaseDashboardPage extends BaseSDPage {

    private static final Logger log = LoggerFactory.getLogger(BaseDashboardPage.class);

    private static final String ASSIGNEE_ATTRIBUTE = "Assignee";
    private static final String STATE_ATTRIBUTE = "State";

    protected BaseDashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public String getDashboardURL(String basicURL, String dashboardName) {
        return String.format(PREDEFINED_DASHBOARD_URL_PATTERN, basicURL, dashboardName);
    }

    @Step("Check if current url leads to dashboard {dashboardName}")
    public boolean isDashboardOpen(String basicURL, String dashboardName) {
        return driver.getCurrentUrl().contains(getDashboardURL(basicURL, dashboardName));
    }

    public String getAttributeFromTable(int index, String attributeName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        String attributeValue = getTable().getCellValue(index, attributeName);
        log.info("Got value for {} attribute: {} from Table", attributeName, attributeValue);
        return attributeValue;
    }

    @Step("Get row number for issue with {id}")
    public int getRowForIssueWithID(String id) {
        return getTable().getRowNumber(id, ID_UPPERCASE_ATTRIBUTE);
    }

    @Step("Creating Dashboard Table")
    protected OldTable getTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Create Dashboard Table");
        return OldTable.createById(driver, wait, getTableID());
    }

    @Step("Check if Table exists")
    public boolean checkIfTableExists() {
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("I check if table exists");
        if (!getTable().hasNoData()) {
            log.info("Table exists and has some data");
            return true;
        } else {
            log.error("Table doesn't exist");
            return false;
        }
    }

    @Step("Check if Reminder icon is present in Table")
    public boolean isReminderPresent(int cellIndex, String reminderText) {
        String iconTitles = getTable().getCellValue(cellIndex, STATE_ATTRIBUTE);
        log.info("Check if Reminder icon is present in Table");
        return iconTitles.contains(reminderText);
    }

    public int getRowWithIssueAssignee(String assignee) {
        return getTable().getRowNumber(assignee, ASSIGNEE_ATTRIBUTE);
    }

    public String getIssueIdWithAssignee(String assignee) {
        return getTable().getCellValue(getRowWithIssueAssignee(assignee), ID_UPPERCASE_ATTRIBUTE);
    }

    public BaseDashboardPage exportFromDashboard(String fileName) {
        try {
            log.info("{}", Paths.get(".").toRealPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        exportFromTable(getTableID(), fileName);
        return this;
    }

    protected abstract String getTableID();
}