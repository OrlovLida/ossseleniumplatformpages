package com.oss.pages.mediation;

import com.oss.framework.components.inputs.Button;
import org.openqa.selenium.WebDriver;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class ViewConnectionConfigurationPage extends BasePage {

    private static final String DELETE_BUTTON = "ConfirmationBox_confirmation-box_action_button";
    private static final String TABLE_ID = "object-table-id";
    private static final String WIZARD_ID = "Popup";

    public ViewConnectionConfigurationPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open view for Connection Configuration")
    public static ViewConnectionConfigurationPage goToViewConnectionConfigurationPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/mediation-repository-view/object-viewer?" + "perspective=LIVE", basicURL));
        return new ViewConnectionConfigurationPage(driver);
    }

    @Step("Use context action {action}")
    public void useContextAction(String action) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button.create(driver, action, "a").click();
        //TODO use this version when OSSWEB-10596 will be ready
       /* TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, TABLE_ID);
        table.callActionByLabel(action);*/
    }

    @Step("Select mediation with name {name} and value {value}")
    public void selectRow(String name, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, TABLE_ID);
        table.selectRowByAttributeValueWithLabel(name, value);
    }

    @Step("Click Delete button")
    public void clickDelete() {
        getWizard().clickActionById(DELETE_BUTTON);
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }
}
