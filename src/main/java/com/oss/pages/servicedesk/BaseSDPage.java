package com.oss.pages.servicedesk;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.HtmlEditor;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import com.oss.untils.FileDownload;

import io.qameta.allure.Step;

public abstract class BaseSDPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BaseSDPage.class);

    private static final String CREATE_DATE_FILTER_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyMMddHHmm";
    public static final DateTimeFormatter CREATE_DATE_FILTER_DATE_FORMATTER = DateTimeFormatter.ofPattern(CREATE_DATE_FILTER_DATE_PATTERN);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    protected BaseSDPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void openPage(WebDriver driver, String url) {
        driver.get(url);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opening page: {}", url);
    }

    public void setValueInHtmlEditor(String value, String componentId) {
        HtmlEditor htmlEditor = HtmlEditor.create(driver, wait, componentId);
        htmlEditor.clear();
        htmlEditor.setSingleStringValue(value);
    }

    public String getTimePeriodForLastNMinutes(int minutes) {
        String startDate = LocalDateTime.now().minusMinutes(minutes).format(CREATE_DATE_FILTER_DATE_FORMATTER);
        String endDate = LocalDateTime.now().format(CREATE_DATE_FILTER_DATE_FORMATTER);
        return startDate + " - " + endDate;
    }

    public static String getDateFormat() {
        return LocalDateTime.now().format(DATE_FORMATTER);
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