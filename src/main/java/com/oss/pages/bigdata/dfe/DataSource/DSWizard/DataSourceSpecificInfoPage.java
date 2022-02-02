package com.oss.pages.bigdata.dfe.DataSource.DSWizard;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SEARCH_FIELD;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class DataSourceSpecificInfoPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceSpecificInfoPage.class);
    private static final String OFFSET_INPUT_ID = "dataSourceOffsetId-input";
    private static final String INTERVAL_UNIT_INPUT_ID = "dataSourceIntervalUnitId-input";
    private static final String INTERVAL_AMOUNT_INPUT_ID = "dataSourceIntervalAmountId";
    private static final String WIZARD_ID = "dataSourcesWizardId";
    private static final String SERVER_GROUP_INPUT_ID = "dataSourceServerGroupNameId";
    private static final String BASE_INTERVAL_ID = "dataSourceBaseIntervalId";
    private static final String TOPIC_ID = "dataSourceTopicId";
    private static final String EVENT_TYPE_ID = "dataSourceEventTypeId";

    private final Wizard specificInfoWizard;

    public DataSourceSpecificInfoPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        specificInfoWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    private void fillOffset(String offset) {
        specificInfoWizard.setComponentValue(OFFSET_INPUT_ID, offset, COMBOBOX);
        log.debug("Setting offset with: {}", offset);
    }

    private void fillIntervalUnit(String unit) {
        specificInfoWizard.setComponentValue(INTERVAL_UNIT_INPUT_ID, unit, COMBOBOX);
        log.debug("Setting interval unit with: {}", unit);
    }

    private void fillIntervalAmount(String intervalAmount) {
        specificInfoWizard.setComponentValue(INTERVAL_AMOUNT_INPUT_ID, intervalAmount, TEXT_FIELD);
        log.debug("Setting interval amount with: {}", intervalAmount);
    }

    @Step("I fill Specific Information with offset: {offset}, Interval Unit: {unit}, Interval Amount: {intervalAmount}")
    public void fillSpecificInfo(String offset, String unit, String intervalAmount) {
        fillOffset(offset);
        fillIntervalUnit(unit);
        fillIntervalAmount(intervalAmount);
        log.info("Filled Specific Information Wizard Page");
    }

    @Step("I fill Specific Information for CSV Data Source with Server Group name: {serverGroupName}, Base Interval: {interval}")
    public void fillSpecificInfoForCSV(String serverGroupName, String interval) {
        fillServerGroup(serverGroupName);
        fillBaseInterval(interval);
        log.info("Filled Specific Information Wizard Page");
    }

    @Step("I fill Specific Information for Kafka Data Source with Topic: {topic}, Event Type: {eventType}")
    public void fillSpecificInfoForKafka(String topic, String eventType) {
        fillTopic(topic);
        fillEventType(eventType);
        log.info("Filled Specific Information Wizard Page");
    }

    private void fillServerGroup(String serverGroupName) {
        specificInfoWizard.setComponentValue(SERVER_GROUP_INPUT_ID, serverGroupName, SEARCH_FIELD);
        log.debug("Setting Server Group name with: {}", serverGroupName);
    }

    private void fillBaseInterval(String interval) {
        specificInfoWizard.setComponentValue(BASE_INTERVAL_ID, interval, TEXT_FIELD);
        log.debug("Setting Base Interval with: {}", interval);
    }

    private void fillTopic(String topic) {
        specificInfoWizard.setComponentValue(TOPIC_ID, topic, TEXT_FIELD);
        log.debug("Setting Topic with: {}", topic);
    }

    private void fillEventType(String eventType) {
        specificInfoWizard.setComponentValue(EVENT_TYPE_ID, eventType, COMBOBOX);
        log.debug("Setting Event Type with: {}", eventType);
    }
}
