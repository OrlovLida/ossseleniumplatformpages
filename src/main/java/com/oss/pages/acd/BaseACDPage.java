package com.oss.pages.acd;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.layout.Card;
import com.oss.framework.iaa.widgets.timeperiodchooser.TimePeriodChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class BaseACDPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BaseACDPage.class);

    public BaseACDPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Step("Maximize window")
    public void maximizeWindow(String windowId) {
        Card.createCard(driver, wait, windowId).maximizeCard();
        log.info("Maximizing window");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Minimize window")
    public void minimizeWindow(String windowId) {
        Card.createCard(driver, wait, windowId).minimizeCard();
        log.info("Minimizing window");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I check if card is maximized")
    public boolean checkCardMaximize(String windowId) {
        DelayUtils.sleep(1000);
        Card card = Card.createCard(driver, wait, windowId);
        log.info("Checking if card {} is maximized", windowId);
        return card.isCardMaximized();
    }

    @Step("Click context button")
    public void clickContextButton(String contextButtonId) {
        Button.createById(driver, contextButtonId).click();
        log.info("Clicking context button");
    }

    @Step("Clear multiSearchField")
    public void clearMultiSearch(String multiSearchFieldId) {
        Input multiSearchField = ComponentFactory.create(multiSearchFieldId, Input.ComponentType.MULTI_SEARCH_FIELD, driver, wait);
        multiSearchField.clear();
        log.info("Clearing multiSearch");
    }

    @Step("Set value in ComboBox")
    public void setValueInComboBox(String componentId, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard.createByComponentId(driver, wait, componentId).setComponentValue(componentId, value, Input.ComponentType.COMBOBOX);
        log.info("Setting value of {componentId} attribute as {value}");
    }

    @Step("I Set value in multiComboBox")
    public void setValueInMultiComboBox(String attributeName, String inputValue) {

        if (Boolean.TRUE.equals(isMultiComboBoxFilled(attributeName))) {
            clearMultiComboBox(attributeName);
        }

        DelayUtils.waitForPageToLoad(driver, wait);
        ComponentFactory.create(attributeName, Input.ComponentType.MULTI_COMBOBOX, driver, wait)
                .setSingleStringValue(inputValue);
        log.info("Setting value in MultiComboBox");
    }

    @Step("I Clear multiComboBox")
    public void clearMultiComboBox(String multiComboBoxId) {
        Input multiComboBox = ComponentFactory.create(multiComboBoxId, Input.ComponentType.MULTI_COMBOBOX, driver, wait);
        multiComboBox.clear();
        log.info("MultiComboBox is cleared");
    }

    @Step("Check if multiComboBox is filled")
    public Boolean isMultiComboBoxFilled(String multiComboBoxId) {
        log.info("Checking if MultiComboBox is empty");
        return !ComponentFactory.create(multiComboBoxId, Input.ComponentType.MULTI_COMBOBOX, driver, wait)
                .getStringValues()
                .isEmpty();
    }

    @Step("I set value in time period chooser")
    public void setValueInTimePeriodChooser(String widgetId, int days, int hours, int minutes) {

        if (Boolean.FALSE.equals(isTimePeriodChooserFilled(widgetId))) {
            clearTimePeriod(widgetId);
        }

        TimePeriodChooser timePeriod = TimePeriodChooser.create(driver, wait, widgetId);
        timePeriod.chooseOption(TimePeriodChooser.TimePeriodChooserOption.LAST);
        log.info("Setting value in the time period chooser");
        timePeriod.setLastPeriod(days, hours, minutes);
        DelayUtils.sleep();
    }

    @Step("I clear time period chooser")
    public void clearTimePeriod(String widgetId) {
        TimePeriodChooser.create(driver, wait, widgetId).clickClearValue();
        log.info("Clearing time period chooser");
    }

    @Step("I check if timePeriodChooser is filled")
    public Boolean isTimePeriodChooserFilled(String widgetId) {

        return TimePeriodChooser.create(driver, wait, widgetId).toString() != null;
    }

    @Step("I set value in InputField")
    public void setValueInInputField(String fieldNameId, String value) {
        ComponentFactory.create(fieldNameId, Input.ComponentType.TEXT_FIELD, driver, wait).setSingleStringValue(value);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Turn On Include Issues without Roots switcher")
    public void turnOnSwitcher(String switcherID) {
        ComponentFactory.create(switcherID, Input.ComponentType.SWITCHER, driver, wait).click();
        log.info("Turning on Include Issues without Roots switcher");
    }

    @Step("Refresh issues table")
    public void refreshIssuesTable(String issuesTableRefreshButtonId) {
        Button.createById(driver, issuesTableRefreshButtonId).click();
        log.info("Clicking refresh issues table button");
    }
}
