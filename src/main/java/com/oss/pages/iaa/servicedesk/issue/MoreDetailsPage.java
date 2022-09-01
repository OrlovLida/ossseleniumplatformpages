package com.oss.pages.iaa.servicedesk.issue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.pages.iaa.servicedesk.BaseSDPage;

import io.qameta.allure.Step;

public class MoreDetailsPage extends BaseSDPage {
    private static final Logger log = LoggerFactory.getLogger(MoreDetailsPage.class);

    private static final String LOGS_TABLE_ID = "_logsApp_details";
    private static final String MESSAGE_COLUMN_NAME = "Message";
    private static final String PROPERTY_PANEL_ID = "_detailsPropertyPanel";

    public MoreDetailsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Check if logs table has entry with note")
    public boolean isNoteInLogsTable(String note) {
        log.info("Check if logs table has entry with note");
        return CommonList.create(driver, wait, LOGS_TABLE_ID)
                .getRowContains(MESSAGE_COLUMN_NAME, note)
                .getValue(MESSAGE_COLUMN_NAME)
                .contains(note);
    }

    @Step("Check {attributeName} value in details tab")
    public String checkValueOfAttribute(String attributeName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        String propertyValue = OldPropertyPanel.createById(driver, wait, PROPERTY_PANEL_ID).getPropertyValue(attributeName);
        log.info("Value of: {} is: {}", attributeName, propertyValue);
        return propertyValue;
    }
}
