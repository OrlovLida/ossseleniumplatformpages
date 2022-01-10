package com.oss.pages.mediation;

import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ViewConnectionConfigurationPage extends BasePage {

    public static final String DELETE_BUTTON_ID = "Delete Connection Configuration";
    public static final String CONFIRMATION_PROMT = "confirmation-prompt";
    private static final String DELETE_BUTTON = "ConfirmationBox_confirmation-box_action_button";
    private static final String TABLE_ID = "object-table-id";

    public ViewConnectionConfigurationPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open view for Connection Configuration")
    public static void goToViewConnectionConfigurationPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/mediation-repository-view/object-viewer?" + "perspective=LIVE", basicURL));
        new ViewConnectionConfigurationPage(driver);
    }

    @Step("Use context action {action}")
    public void useContextAction(String group, String action) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, TABLE_ID);
        table.callAction(group, action);
    }

    @Step("Select mediation with name {name} and value {value}")
    public void selectRow(String name, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, TABLE_ID);
        table.selectRowByAttributeValueWithLabel(name, value);
    }

    @Step("Click Delete button")
    public void clickDelete() {
        getWizard().clickButtonById(DELETE_BUTTON);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, CONFIRMATION_PROMT);
    }
}
