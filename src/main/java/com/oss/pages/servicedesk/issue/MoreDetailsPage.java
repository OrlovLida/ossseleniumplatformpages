package com.oss.pages.servicedesk.issue;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.widgets.list.CommonList;
import com.oss.pages.servicedesk.BaseSDPage;

import io.qameta.allure.Step;

public class MoreDetailsPage extends BaseSDPage {
    private static final Logger log = LoggerFactory.getLogger(MoreDetailsPage.class);

    private static final String LOGS_TABLE_ID = "_logsApp";
    private static final String MESSAGE_COLUMN_NAME = "Message";

    public MoreDetailsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Check if logs table has entry with Remainder note")
    public boolean isReminderNoteInLogsTable(String reminderNote) {
        log.info("Check if logs table has entry with Remainder note");
        return CommonList.create(driver, wait, LOGS_TABLE_ID)
                .getRowContains(MESSAGE_COLUMN_NAME, reminderNote)
                .getValue(MESSAGE_COLUMN_NAME)
                .contains(reminderNote);
    }
}
