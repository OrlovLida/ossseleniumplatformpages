package com.oss.iaa.administrationpanel;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.oss.BaseTestCase;
import com.oss.pages.administration.administrationpanel.KafkaPage;

import io.qameta.allure.Description;

public class KafkaViewTest extends BaseTestCase {

    private KafkaPage kafkaPage;

    @BeforeMethod
    public void goToKafkaPage() { kafkaPage = KafkaPage.goToKafkaPage(driver); }

    @Test(priority = 1, testName = "Check Help button in Kafka Table", description = "Check Help button in Kafka Table")
    @Description("Check Help button in Kafka Table")
    public void checkHelpButton() {
        kafkaPage.clickHelp();
        Assert.assertTrue(kafkaPage.isTextInHelp());
        kafkaPage.clickAccept();
        Assert.assertFalse(kafkaPage.isKafkaTableEmpty());
    }

    @Test(priority = 2, testName = "Check Refresh button in Kafka Consumer table", description = "Check Refresh button in Kafka Consumer table")
    @Description("Check Refresh button in Kafka Consumer table")
    public void checkRefreshKafkaConsumerTable() {
        Assert.assertFalse(kafkaPage.isKafkaConsumerTableEmpty());
        kafkaPage.clickRefreshInKafkaConsumerTable();
        Assert.assertFalse(kafkaPage.isKafkaConsumerTableEmpty());
    }

    @Test(priority = 3, testName = "Check Refresh button in Kafka table", description = "Check Refresh button in Kafka table")
    @Description("Check Refresh button in Kafka table")
    public void checkRefreshKafkaTable() {
        Assert.assertFalse(kafkaPage.isKafkaTableEmpty());
        kafkaPage.clickRefreshInKafkaTable();
        Assert.assertFalse(kafkaPage.isKafkaTableEmpty());
    }

    @Test(priority = 4, testName = "Check Kafka Partition Table", description = "Check Kafka Partition Table")
    @Description("Check Kafka Partition Table")
    public void checkKafkaPartitionTable() {
        Assert.assertTrue(kafkaPage.isKafkaPartitionTableEmpty());
        kafkaPage.selectFirstRowInKafkaTable();
        Assert.assertFalse(kafkaPage.isKafkaPartitionTableEmpty());
    }

    @Test(priority = 5, testName = "Check Topic Table", description = "Check Kafka Topic Table")
    @Description("Check Topic Table")
    public void checkKafkaTopicTable() {
        Assert.assertTrue(kafkaPage.isKafkaTopicTableEmpty());
        kafkaPage.selectFirstRowInKafkaConsumerTable();
        Assert.assertFalse(kafkaPage.isKafkaTopicTableEmpty());
    }
}
