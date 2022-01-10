package com.oss.pages.physical;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class CableRoutingViewPage extends BasePage {

    private static final String ROUTING_TABLE_DATA_ATTRIBUTE_NAME = "routing-list";
    private static final String SAVE_ID = "cable-routing-cancel-proceed-buttons-1";
    private static final String ROUTING_TABLE_WIZARD_ID = "cable-routing-table-app";
    private static final String INSERT_LOCATION_ID = "Insert Location";
    private static final String REMOVE_LOCATION_PATTERN = "Remove Location %s";
    private static final String WIZARD_ID = "Popup";

    public CableRoutingViewPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Insert Location button")
    public void clickInsertLocation() {
        getCableCommonList().callAction(INSERT_LOCATION_ID);
    }

    @Step("Click Remove Location {locationName} ")
    public void clickRemoveLocation(String locationName) {
        getCableCommonList().callAction(String.format(REMOVE_LOCATION_PATTERN, locationName));
    }

    @Step("Insert Location to routing")
    public void insertLocationToRouting(String locationName) {
        Wizard.createByComponentId(driver, wait, WIZARD_ID)
                .setComponentValue("physicalconnectivity_cableRoutingFormLocation", locationName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Click Add location to routing")
    public void clickAddLocationToRouting() {
        Wizard.createByComponentId(driver, wait, WIZARD_ID)
                .clickButtonByLabel("Add location to routing");
    }

    @Step("Choose segment")
    public void selectSegment(int row) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonList commonList = getCableCommonList();
        commonList.expandAllCategories();
        DelayUtils.waitForPageToLoad(driver, wait);
        commonList.selectRow(row);
    }

    @Step("Click Save")
    public void clickSave() {
        getRoutingTableWizard().clickButtonById(SAVE_ID);
    }

    @Step("Click close")
    public void clickClose() {
        Wizard.createByComponentId(driver, wait, WIZARD_ID).clickButtonByLabel("Close");
    }

    private CommonList getCableCommonList() {
        return CommonList.create(driver, wait, ROUTING_TABLE_DATA_ATTRIBUTE_NAME);
    }

    private Wizard getRoutingTableWizard() {
        return Wizard.createByComponentId(driver, wait, ROUTING_TABLE_WIZARD_ID);
    }
}
