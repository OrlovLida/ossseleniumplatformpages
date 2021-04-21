package com.oss.pages.gisView;

import com.oss.framework.components.inputs.Button;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RoutingWizardPage extends BasePage {
    private static final String START_LOCATION_INPUT_DATA_ATTRIBUTE_NAME = "StartLocation_uid";
    private static final String OK_BUTTON_DATA_ATTRIBUTE_NAME = "RoutingButtonsId-0";
    private static final String ROUTING_TABLE_DATA_ATTRIBUTE_NAME = "DuctRoutingListId";

    public RoutingWizardPage(WebDriver driver) {
        super(driver);
    }

    public void insertPhysicalLocation(String location) {
        DelayUtils.waitForPageToLoad(driver, wait);
        //TODO Change on Button after OSSWEB-9926
        driver.findElement(By.xpath("//a/button[@class='squareButton btn btn-sm btn-default']")).click();
        DelayUtils.waitForPageToLoad(driver, wait);
        Wizard wizard = Wizard.createPopupWizard(driver, wait);
        wizard.setComponentValue(START_LOCATION_INPUT_DATA_ATTRIBUTE_NAME, location, Input.ComponentType.SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickOK();
    }

    public void selectDuctForSegment(int row) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonList commonList = CommonList.create(driver, wait, ROUTING_TABLE_DATA_ATTRIBUTE_NAME);
        commonList.expandAllCategories();
        DelayUtils.waitForPageToLoad(driver, wait);
        commonList.selectRow(row);
    }

    public void clickOk() {
        Button.createBySelectorAndId(driver, "a", OK_BUTTON_DATA_ATTRIBUTE_NAME).click();
    }
}
