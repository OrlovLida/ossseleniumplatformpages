package com.oss.pages.mediation;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class ViewConnectionConfigurationPage extends BasePage {

    private final String DELETE_BUTTON = "ConfirmationBox_confirmation-box_action_button";

    private final Wizard wizard;

    public ViewConnectionConfigurationPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Open view for Connection Configuration")
    public static ViewConnectionConfigurationPage goToViewConnectionConfigurationPage(WebDriver driver, String basicURL) {
        driver.get(String.format("%s/#/view/mediation-repository-view/object-viewer?" + "perspective=LIVE", basicURL));
        return new ViewConnectionConfigurationPage(driver);
    }

    @Step("Use context action")
    public void useContextAction(String action) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, "object-table-id");
        table.callActionByLabel(action);
    }

    @Step("Select mediation")
    public void selectRow(String name, String value) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TableInterface table = OldTable.createByComponentDataAttributeName(driver, wait, "object-table-id");
        table.selectRowByAttributeValueWithLabel(name, value);
    }

    @Step("Click Delete button")
    public void clickDelete() {
        wizard.clickActionById(DELETE_BUTTON);
    }
}
