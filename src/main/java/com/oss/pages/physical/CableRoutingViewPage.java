package com.oss.pages.physical;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.listwidget.CommonList;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.framework.widgets.tablewidget.OldTable;
import com.oss.framework.widgets.tablewidget.TableInterface;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public class CableRoutingViewPage extends BasePage {
    private static final String ROUTING_TABLE_DATA_ATTRIBUTE_NAME = "routing-list";

    public CableRoutingViewPage(WebDriver driver) { super(driver); }

    @Step("Click Insert Location button")
    public void clickInsertLocation() {
        getCableRoutingView().callButtonByLabel("Insert Location");
    }


    @Step("Click Remove Location {locationName} ")
    public void clickRemoveLocation(String locationName) {
        getCableRoutingView().callButtonByLabel("Remove Location " + locationName);
    }

    @Step("Insert Location to routing")
    public void insertLocationToRouting(String locationName) {
        Wizard.createByComponentId(driver, wait, "Popup")
                .setComponentValue("physicalconnectivity_cableRoutingFormLocation", locationName, Input.ComponentType.SEARCH_FIELD);
    }

    @Step("Click Add location to routing")
    public void clickAddLocationToRouting() {
        Wizard.createByComponentId(driver, wait, "Popup")
                .clickButtonByLabel("Add location to routing");
    }

    @Step("Choose segment")
    public void selectSegment(int row) {
        DelayUtils.waitForPageToLoad(driver, wait);
        CommonList commonList = CommonList.create(driver, wait, ROUTING_TABLE_DATA_ATTRIBUTE_NAME);
        commonList.expandAllCategories();
        DelayUtils.waitForPageToLoad(driver, wait);
        commonList.selectRow(row);
    }

    @Step("Click Save")
    public void clickSave() {
        getCableRoutingView().clickButtonByLabel("Save");
    }

    @Step("Click close")
    public void clickClose() {
        Wizard.createByComponentId(driver, wait, "Popup").clickButtonByLabel("Close");
    }

    public Wizard getCableRoutingView() {
        return Wizard.createWizardByHeaderText(driver, wait, "Cable Routing View");
    }
}
