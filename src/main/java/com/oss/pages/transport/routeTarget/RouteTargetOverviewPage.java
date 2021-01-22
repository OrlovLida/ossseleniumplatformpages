package com.oss.pages.transport.routeTarget;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.OldPropertyPanel;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

/**
 * @author Kamil Sikora
 */
public class RouteTargetOverviewPage extends BasePage {

    private static final String EDIT_BUTTON_DATA_ATTRIBUTENAME = "buttonPanelId-0";
    private static final String REMOVE_BUTTON_DATA_ATTRIBUTENAME = "buttonPanelId-1";
    private static final String CONFIRM_REMOVAL_BUTTON_DATA_ATTRIBUTENAME = "ConfirmationBox_remove_id_action_button";

    public RouteTargetOverviewPage(WebDriver driver){
        super(driver);
    }


    @Step("Click edit button")
    public RouteTargetWizardPage clickEdit() {
        Button editButton = Button.createBySelectorAndId(driver, "a", EDIT_BUTTON_DATA_ATTRIBUTENAME);
        editButton.click();
        return new RouteTargetWizardPage(driver);
    }

    @Step("Click remove button")
    public void clickRemove() {
        Button removeButton = Button.createBySelectorAndId(driver, "a", REMOVE_BUTTON_DATA_ATTRIBUTENAME);
        removeButton.click();
    }

    @Step("Click confirm removal button")
    public void confirmRemoval() {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button confirmRemovalButton = Button.createById(driver, CONFIRM_REMOVAL_BUTTON_DATA_ATTRIBUTENAME);
        confirmRemovalButton.click();
    }

}
