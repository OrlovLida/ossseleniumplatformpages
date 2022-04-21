package com.oss.pages.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.oss.framework.components.contextactions.ButtonContainer;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.ComponentFactory;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;

public class DashboardPage extends BasePage {

    private static final String WIDGET_TYPE_ID = "registeredId-input";
    private static final String WIDGET_TITLE_ID = "title";
    private static final String WIDGET_SAVE_LABEL = "Save";
    private static final String ADD_WIDGET = "Add Widget";

    public DashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void clickEditCustomDashboardButton() {
        ButtonContainer.create(driver, wait).callActionById("encourageButton-0");
    }

    public void chooseWidgetType(String chosenWidgetType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ComponentFactory.create(WIDGET_TYPE_ID, Input.ComponentType.COMBOBOX, driver, wait)
                .setSingleStringValue(chosenWidgetType);
    }

    public void fillWidgetTitle(String widgetTitle) {
        DelayUtils.waitForPageToLoad(driver, wait);
        ComponentFactory.create(WIDGET_TITLE_ID, Input.ComponentType.TEXT_FIELD, driver, wait)
                .setSingleStringValue(widgetTitle);
    }

    public void clickSaveButton() {
        ButtonContainer.create(driver, wait).callActionByLabel(WIDGET_SAVE_LABEL);
    }

    public void clickAddWidgetButton() {
        Button.createByLabel(driver, ADD_WIDGET).click();
    }
}
