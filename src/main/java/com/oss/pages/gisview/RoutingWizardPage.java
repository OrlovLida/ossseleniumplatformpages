package com.oss.pages.gisview;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.widgets.list.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

public class RoutingWizardPage extends BasePage {
    private static final String START_LOCATION_INPUT_ID = "StartLocation_uid";
    private static final String OK_BUTTON_ID = "RoutingButtonsId-0";
    private static final String ROUTING_TABLE_ID = "DuctRoutingListId";
    private static final String INSERT_PHYSICAL_LOCATION_ID = "Insert Physical Location";
    private static final String INSERT_PHYSICAL_LOCATION_WIZARD_ID = "prompt_id";

    public RoutingWizardPage(WebDriver driver) {
        super(driver);
    }

    public void insertPhysicalLocation(String location) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Button button = Button.createById(driver, INSERT_PHYSICAL_LOCATION_ID);
        button.click();
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard wizard = Wizard.createByComponentId(driver, wait, INSERT_PHYSICAL_LOCATION_WIZARD_ID);
        wizard.setComponentValue(START_LOCATION_INPUT_ID, location, Input.ComponentType.SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickOK();
    }

    public void selectDuctForSegment(int row) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonList commonList = CommonList.create(driver, wait, ROUTING_TABLE_ID);
        commonList.expandAllCategories();
        DelayUtils.waitForPageToLoad(driver, wait);
        commonList.selectRow(row);
    }

    public void clickOk() {
        Button.createBySelectorAndId(driver, "a", OK_BUTTON_ID).click();
    }
}
