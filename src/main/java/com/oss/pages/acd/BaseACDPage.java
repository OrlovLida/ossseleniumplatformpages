package com.oss.pages.acd;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.prompts.ConfirmationBox;
import com.oss.framework.iaa.widgets.timeperiodchooser.TimePeriodChooser;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.components.layout.Card;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class BaseACDPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BaseACDPage.class);

    private static final String ADD_PREDEFINED_FILTER_BUTTON = "contextButton-0";
    private static final String SWITCHER_ID = "switcherId";
    private static final String VISUALIZATION_TYPE_ID = "widgetType-input";
    private static final String ATTRIBUTE_ID = "attribute1Id-input";
    private static final String ATTRIBUTE_VALUES_ID = "attribute1ValuesId";
    private static final String CONFIRM_DELETE_BUTTON_ID = "ConfirmationBox_prompt_action_button";

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
    public boolean checkCardMaximize(String windowId){
        DelayUtils.sleep(1000);
        Card card = Card.createCard(driver, wait, windowId);
        log.info("Checking if card {} is maximized", windowId);
        return card.isCardMaximized();
    }

    @Step("Add predefined filter")
    public void clickAddPredefinedFilter() {
        Button button = Button.createById(driver, ADD_PREDEFINED_FILTER_BUTTON);
        button.click();
        log.info("Clicking Add predefined filter button");
    }

    @Step("Choose Predefined Filter type")
    public void chooseVisualizationType(String visualizationType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard.createWizard(driver, wait).setComponentValue(VISUALIZATION_TYPE_ID, visualizationType, Input.ComponentType.COMBOBOX);
        log.info("Setting visualization type: {}", visualizationType);
    }

    @Step("Choose attribute for predefined filter")
    public void chooseAttribute(String attributeName) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard.createWizard(driver, wait).setComponentValue(ATTRIBUTE_ID, attributeName, Input.ComponentType.COMBOBOX);
        log.debug("Setting attribute for predefined filter: {}", attributeName);
    }

    @Step("I insert {value} to multiComboBox component with id {componentId}")
    public void insertAttributeValueToMultiComboBoxComponent(String text) {
        insertValueToComponent(ATTRIBUTE_VALUES_ID, text, Input.ComponentType.MULTI_COMBOBOX);
    }

    @Step("I save Predefined Filter")
    public void savePredefinedFilter() {
        Wizard.createWizard(driver, wait).clickAccept();
        log.info("I save predefined filter by clicking 'Accept' button");
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("I delete Predefined Filter")
    public void deletePredefinedFilter() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep();

        ButtonContainer deleteButton = ButtonContainer.create(driver, wait);
        deleteButton.callActionByLabel("DELETE");

        DelayUtils.waitForPageToLoad(driver, wait);

        ConfirmationBox.create(driver,wait).clickButtonById(CONFIRM_DELETE_BUTTON_ID);

        log.info("I deleted predefined filer");
    }

    @Step("Set value in time period chooser")
    public void setValueInTimePeriodChooser(String widgetId, int days, int hours, int minutes) {
        TimePeriodChooser timePeriod = TimePeriodChooser.create(driver, wait, widgetId);
        timePeriod.chooseOption(TimePeriodChooser.TimePeriodChooserOption.LAST);
        log.info("Setting value in the time period chooser");
        timePeriod.setLastPeriod(days, hours, minutes);
        DelayUtils.sleep();
    }

    @Step("Set value in multiComboBox")
    public void setValueInMultiComboBox(String attributeName, String inputValue) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ComponentFactory.create(attributeName, Input.ComponentType.MULTI_COMBOBOX, driver, wait)
                .setSingleStringValue(inputValue);
        log.info("Setting value of {} attribute", inputValue, " as {}", attributeName);
    }

    @Step("Turn On Include Issues without Roots switcher")
    public void turnOnSwitcher() {
        ComponentFactory.create(SWITCHER_ID, Input.ComponentType.SWITCHER, driver, wait).click();
        log.info("Turning on Include Issues without Roots");
    }

    @Step("Refresh issues table")
    public void refreshIssuesTable(String issuesTableRefreshButtonId) {
        Button button = Button.createById(driver, issuesTableRefreshButtonId);
        button.click();
        log.info("Clicking refresh issues table button");
    }

    @Step("Clear time period chooser")
    public void clearTimePeriod(String widgetId) {
        TimePeriodChooser timePeriod = TimePeriodChooser.create(driver, wait, widgetId);
        timePeriod.clickClearValue();
        log.info("Clearing time period chooser");
    }

    private void insertValueToComponent(String componentId, String text, Input.ComponentType componentType) {
        getWizard().setComponentValue(componentId, text, componentType);
    }

    private Wizard getWizard() {
        return Wizard.createWizard(driver, wait);
    }
}
