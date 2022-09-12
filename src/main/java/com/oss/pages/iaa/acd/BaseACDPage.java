package com.oss.pages.iaa.acd;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.layout.Card;
import com.oss.framework.iaa.widgets.timeperiodchooser.TimePeriodChooser;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.tabs.TabsWidget;
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

    @Step("I click Label button")
    public void clickButtonByLabel(String label) {
        Button.createByLabel(driver, label).click();
        log.info("Clicking {} button", label);
    }

    @Step("I search by attribute")
    public void setAttributeValue(String attributeId, String inputValue) {

        if (isAttributeFilled(attributeId)) {
            log.info("Input is not empty");
            clearAttributeValue(attributeId);
            log.info("Input has been cleared");
        }

        DelayUtils.waitForPageToLoad(driver, wait);
        ComponentFactory.create(attributeId, driver, wait).setSingleStringValue(inputValue);
        log.info("Setting value of {} attribute as {}", attributeId, inputValue);
    }

    @Step("I Clear Attribute value")
    public void clearAttributeValue(String attributeId) {
        ComponentFactory.create(attributeId, driver, wait).clear();
        log.info("Attribute value is cleared");
    }

    @Step("Check if multiComboBox is filled")
    public Boolean isAttributeFilled(String attributeId) {
        log.info("Checking if MultiComboBox is empty");

        return !ComponentFactory.create(attributeId, driver, wait).getStringValues().isEmpty();
    }

    @Step("I set value in time period chooser")
    public void setValueInTimePeriodChooser(String widgetId, int days, int hours, int minutes) {

        if (isTimePeriodChooserFilled(widgetId)) {
            clearTimePeriod(widgetId);
        }
        getTimePeriodChooser(widgetId).chooseOption(TimePeriodChooser.TimePeriodChooserOption.LAST);
        log.info("Setting value in the time period chooser");
        getTimePeriodChooser(widgetId).setLastPeriod(days, hours, minutes);
        DelayUtils.sleep();
    }

    @Step("I clear time period chooser")
    public void clearTimePeriod(String widgetId) {
        getTimePeriodChooser(widgetId).clickClearValue();
        log.info("Clearing time period chooser");
    }

    @Step("I check if timePeriodChooser is filled")
    public Boolean isTimePeriodChooserFilled(String widgetId) {
        return getTimePeriodChooser(widgetId).isCloseIconPresent();
    }

    @Step("I set value in Text field")
    public void setValueInTextField(String fieldNameId, String value) {
        ComponentFactory.create(fieldNameId, driver, wait).setSingleStringValue(value);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Turn On Include Issues without Roots switcher")
    public void turnOnSwitcher(String switcherID) {
        ComponentFactory.create(switcherID, driver, wait).click();
        log.info("Turning on Include Issues without Roots switcher");
    }

    @Step("Refresh issues table")
    public void refreshIssuesTable(String issuesTableRefreshButtonId) {
        Button.createById(driver, issuesTableRefreshButtonId).click();
        log.info("Clicking refresh issues table button");
    }

    @Step("I go to {tabLabel} tab")
    public void goToTab(String tabId, String tabLabel) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TabsWidget.createById(driver, wait, tabId).selectTabByLabel(tabLabel);
        log.info("I opened {} tab", tabLabel);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    private TimePeriodChooser getTimePeriodChooser(String widgetId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        return TimePeriodChooser.create(driver, wait, widgetId);
    }
}