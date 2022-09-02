package com.oss.pages.administration.administrationpanel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.utils.DelayUtils;

import io.qameta.allure.Step;

import static com.oss.transport.infrastructure.ServicesClient.BASIC_URL;

public class KafkaPage extends BaseAdminPanelPage {

    private static final String KAFKA_PAGE_URL = String.format("%s/#/view/admin/kafka", BASIC_URL);
    private static final String KAFKA_TABLE_ID = "ADMINISTRATION_KAFKA_TABLE_APP_ID";
    private static final String HELP_BUTTON_ID = "TOPIC_HELP_ACTION_ID";
    private static final String HTML_EDITOR_ID = "HELP_HTML_EDITOR_READ_ONLY_COMPONENT_ID";
    private static final String HELP_HEADER_TEXT = "Administration - Kafka Help";
    private static final String KAFKA_CONSUMER_TABLE_ID = "ADMINISTRATION_KAFKA_CONSUMER_TABLE_APP_ID";
    private static final String KAFKA_TOPIC_TABLE_ID = "ADMINISTRATION_KAFKA_TOPIC_CONSUMER_TABLE_APP_ID";
    private static final String KAFKA_PARTITION_TABLE_ID = "ADMINISTRATION_KAFKA_PARTITION_TABLE_APP_ID";


    public KafkaPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public static KafkaPage goToKafkaPage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        goToPage(driver, wait, KAFKA_PAGE_URL);
        return new KafkaPage(driver, wait);
    }

    public void clickHelp() {
        callActionInTable(KAFKA_TABLE_ID, HELP_BUTTON_ID);
        log.info("Clicking Help button in Kafka table");
    }

    public boolean isTextInHelp() {
        return isTextInHelp(HTML_EDITOR_ID, HELP_HEADER_TEXT);
    }

    @Step("Click Refresh in Kafka table")
    public void clickRefreshInKafkaTable() {
        clickRefreshInTable(KAFKA_TABLE_ID);
        log.info("Clicking Refresh button in Kafka table");
    }

    @Step("Click Refresh in Kafka Consumer table")
    public void clickRefreshInKafkaConsumerTable() {
        clickRefreshInTable(KAFKA_CONSUMER_TABLE_ID);
        log.info("Clicking Refresh button in Kafka table");
    }

    @Step("Check if Kafka table is empty")
    public boolean isKafkaTableEmpty() {
        return isTableEmpty(KAFKA_TABLE_ID);
    }

    @Step("Check if Kafka Consumer table is empty")
    public boolean isKafkaConsumerTableEmpty() {
        return isTableEmpty(KAFKA_CONSUMER_TABLE_ID);
    }

    @Step("Check if Kafka Topic table is empty")
    public boolean isKafkaTopicTableEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return isTableEmpty(KAFKA_TOPIC_TABLE_ID);
    }

    @Step("Check if Kafka Partition table is empty")
    public boolean isKafkaPartitionTableEmpty() {
        DelayUtils.waitForPageToLoad(driver, wait);
        return isTableEmpty(KAFKA_PARTITION_TABLE_ID);
    }

    @Step("Change first column in Kafka table")
    public void changeFirstColumnInKafkaTable(String columnLabel) {
        changeFirstColumn(KAFKA_TABLE_ID, columnLabel);
        log.info("Change column {} to be first in Kafka table", columnLabel);
    }

    @Step("Change first column in Kafka Consumer table")
    public void changeFirstColumnInKafkaConsumerTable(String columnLabel) {
        changeFirstColumn(KAFKA_CONSUMER_TABLE_ID, columnLabel);
        log.info("Change column {} to be first in Kafka Consumer table", columnLabel);
    }

    @Step("Select first row in Kafka Table")
    public void selectFirstRowInKafkaTable() {
        selectFirstRowInTable(KAFKA_TABLE_ID);
    }

    @Step("Select first row in Kafka Consumer Table")
    public void selectFirstRowInKafkaConsumerTable() {
        selectFirstRowInTable(KAFKA_CONSUMER_TABLE_ID);
    }
}