package com.oss.pages.mediation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.prompts.Popup;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.table.OldTable;
import com.oss.framework.widgets.table.TableInterface;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ViewConnectionConfigurationPage extends BasePage {

    public static final String DELETE_BUTTON_ID = "Delete Connection Configuration";
    private static final String DELETE_BUTTON = "ConfirmationBox_confirmation-box_action_button";
    private static final String TABLE_ID = "object-table-id";

    public ViewConnectionConfigurationPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open view for Connection Configuration")
    public static ViewConnectionConfigurationPage goToViewConnectionConfigurationPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/mediation-repository-view/object-viewer?" + "perspective=LIVE", basicURL));
        return new ViewConnectionConfigurationPage(driver);
    }

    @Step("Use context action {action}")
    public void useContextAction(String group, String action) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createById(driver, wait, TABLE_ID);
        table.callAction(group, action);
    }

    @Step("Select mediation with name {name} and value {value}")
    public void selectRow(String name, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createById(driver, wait, TABLE_ID);
        table.selectRowByAttributeValueWithLabel(name, value);
    }

    public void fullTextSearch(String text) {
        DelayUtils.waitForPageToLoad(driver, wait);
        OldTable table = OldTable.createById(driver, wait, TABLE_ID);
        table.fullTextSearch(text);
    }

    @Step("Click Delete button")
    public void clickDelete() {
        getPopup().clickButtonById(DELETE_BUTTON);
    }

    private Popup getPopup() {
        return Popup.create(driver, wait);
    }
}
