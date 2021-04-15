package com.oss.utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class AttachmentsManager {

    private static final Logger log = LoggerFactory.getLogger(AttachmentsManager.class);

    @Attachment(value = "Page screenshot", type = "image/png")
    public static byte[] saveScreenshotPNG(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    //Text attachments for Allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    //HTML attachments for Allure
    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    @Attachment(value="Console Logs")
    public static String attachConsoleLogs(WebDriver webDriver) {
        if(CONFIGURATION.getDriver().equals("gecko")){
            log.debug("Gecko driver doesn't support capturing browser logs");
            return "";
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
            log.debug(builder.toString());
            return builder.toString();
        }
    }

}
