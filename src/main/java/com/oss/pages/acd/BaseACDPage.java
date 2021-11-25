package com.oss.pages.acd;

import com.oss.framework.components.common.TimePeriodChooser;
import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.CSSUtils;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.view.Card;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseACDPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BaseACDPage.class);

    private final String ADD_PREDEFINED_FILTER_BUTTON = "contextButton-0";
    private final String SWITCHER_ID = "switcherId";

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

    @Step("Add predefined filter")
    public void clickAddPredefinedFilter() {
        Button button = Button.createByXpath(ADD_PREDEFINED_FILTER_BUTTON, "li", CSSUtils.DATA_WIDGET_ID, driver);
        button.click();
        log.info("Clicking Add predefined filter button");
    }

    @Step("I insert {value} to multi combo box component with id {componentId}")
    public void insertAttributeValueToMultiComboBoxComponent(String text, String componentId) {
        insertValueToComponent(componentId, text, Input.ComponentType.MULTI_COMBOBOX);
    }

    private void insertValueToComponent(String componentId, String text, Input.ComponentType componentType) {
        getWizard().setComponentValue(componentId, text, componentType);
    }

    private Wizard getWizard() {
        return Wizard.createWizard(driver, wait);
    }

    @Step("I delete Predefined Filter")
    public void deletePredefinedFilter() {
        DelayUtils.waitForPageToLoad(driver, wait);
        DelayUtils.sleep();

        ButtonContainer deleteButton = ButtonContainer.create(driver, wait);
        deleteButton.callActionByLabel("DELETE");

        DelayUtils.waitForPageToLoad(driver, wait);

        Button deleteButton2 = Button.create(driver, "Delete");
        deleteButton2.click();

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

    @Step("Turn On Include Issues without Roots switcher")
    public void turnOnSwitcher() {
        ComponentFactory.create(SWITCHER_ID, Input.ComponentType.SWITCHER, driver, wait).click();
        log.info("Turning on Include Issues without Roots");
    }

    @Step("Refresh issues table")
    public void refreshIssuesTable(String issuesTableRefreshButtonId) {
        Button button = Button.createByXpath(issuesTableRefreshButtonId, "li", CSSUtils.DATA_WIDGET_ID, driver);
        button.click();
        log.info("Clicking refresh issues table button");
    }

    @Step("Clear time period chooser")
    public void clearTimePeriod(String widgetId) {
        TimePeriodChooser timePeriod = TimePeriodChooser.create(driver, wait, widgetId);
        timePeriod.clickClearValue();
        log.info("Clearing time period chooser");
    }
}
