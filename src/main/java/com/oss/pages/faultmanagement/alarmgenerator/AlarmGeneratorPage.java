package com.oss.pages.faultmanagement.alarmgenerator;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.alerts.SystemMessageContainer;
import com.oss.framework.components.layout.Card;
import com.oss.framework.components.table.TableComponent;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class AlarmGeneratorPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(AlarmGeneratorPage.class);
    private static final String HTTP_URL_TO_ALARM_GENERATOR = "%s/#/views/fault-management/alarm-generator";

    private static final String ALARMS_GENERATOR_CARD_ID = "area2-alarm-generator-table";
    private static final String ADD_BUTTON_ID = "AddNewRowAction";
    private static final String GENERATE_BUTTON_ID = "GENERATE";
    private static final String CREATE_ALARM_BUTTON_ID = "GenerateCreateEditAlarmsAction";
    private static final String TERMINATE_ALARM_BUTTON_ID = "GenerateRemoveAlarmsAction";
    private static final String ALARMS_GENERATOR_TABLE_ID = "table-alarm-generator-table";

    public AlarmGeneratorPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("I open Alarm Generator Page")
    public static AlarmGeneratorPage goToPage(WebDriver driver, String basicURL) {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        String webURL = String.format(HTTP_URL_TO_ALARM_GENERATOR, basicURL);
        driver.navigate().to(webURL);
        DelayUtils.waitForPageToLoad(driver, wait);
        log.info("Opened page: {}", webURL);
        return new AlarmGeneratorPage(driver, wait);
    }

    public void clickAddButton() {
        getAlarmsGeneratorCard().callActionById(ADD_BUTTON_ID);
        log.info("Clicking button 'Add'");
    }

    public void clickGenerateAlarmButton() {
        getAlarmsGeneratorCard().callActionById(GENERATE_BUTTON_ID, CREATE_ALARM_BUTTON_ID);
        log.info("Clicking button 'Generate Create/Update Operation'");
    }

    public void clickTerminateAlarmButton() {
        getAlarmsGeneratorCard().callActionById(GENERATE_BUTTON_ID, TERMINATE_ALARM_BUTTON_ID);
        log.info("Clicking button 'Generate Terminate Operation'");
    }

    public void selectFirstRowInTable() {
        DelayUtils.waitForPageToLoad(driver, wait);
        getAlarmsGeneratorTable().selectRow(0);
        log.info("Selecting first row in Alarm Generator Table");
    }

    public String getMessageFromPrompt() {
        return SystemMessageContainer.create(driver, wait)
                .getFirstMessage()
                .map(SystemMessageContainer.Message::getText)
                .orElse(null);
    }

    public String getMessageTypeFromPrompt() {
        return SystemMessageContainer.create(driver, wait)
                .getMessages().get(0).getMessageType().toString();
    }

    private Card getAlarmsGeneratorCard() {
        return Card.createCard(driver, wait, ALARMS_GENERATOR_CARD_ID);
    }

    private TableComponent getAlarmsGeneratorTable() {
        return TableComponent.createById(driver, wait, ALARMS_GENERATOR_TABLE_ID);
    }
}
