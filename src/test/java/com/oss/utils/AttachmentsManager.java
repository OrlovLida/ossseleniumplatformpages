package com.oss.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Allure;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class AttachmentsManager {

    private static final Logger log = LoggerFactory.getLogger(AttachmentsManager.class);

    public static void saveScreenshotPNG(WebDriver driver) {
        File screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            Allure.addAttachment("Page screenshot", "image/png", FileUtils.openInputStream(screenshotAs), ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveLink(WebDriver driver) {
        Allure.addAttachment("Console link", driver.getCurrentUrl());
     }

    //Text attachments for Allure
    public static void saveTextLog(String message) {
        Allure.addAttachment("{0}", "text/plain", message, ".txt");
    }

    //HTML attachments for Allure
    public static void attachHtml(String html) {
        Allure.addAttachment("{0}", "text/html", html, ".html");
    }

    public static void attachConsoleLogs(WebDriver webDriver) {
        if (CONFIGURATION.getDriver().equals("gecko")) {
            log.debug("Gecko driver doesn't support capturing browser logs");
        } else {
            LogEntries logEntries = webDriver.manage().logs().get(LogType.BROWSER);
            StringBuilder builder = new StringBuilder();
            builder.append("JS console logs:\n");
            for (LogEntry entry : logEntries) {
                Timestamp ts = new Timestamp(entry.getTimestamp());
                builder.append(ts.toLocalDateTime());
                builder.append(" ");
                builder.append(entry.getLevel());
                builder.append(" ");
                builder.append(entry.getMessage());
                builder.append("\n");
            }
            builder.append("\n");
            Allure.addAttachment("Console logs", builder.toString());
        }
    }
}