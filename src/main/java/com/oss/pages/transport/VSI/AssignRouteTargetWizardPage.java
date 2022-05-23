package com.oss.pages.transport.VSI;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.platform.NewInventoryViewPage;

import io.qameta.allure.Step;

public class AssignRouteTargetWizardPage extends BasePage {

    private static final String ROUTE_TARGET_FIELD_ID = "Add_Imp/Exp_Route_Target_id";
    private static final String EXPORT_FIELD_ID = "check_box_export_uid";
    private static final String IMPORT_FIELD_ID = "check_box_import_uid";
    private static final String COMPONENT_ID = "vsi.impExpRoutetarget.app";

    private final Wizard wizard;

    public AssignRouteTargetWizardPage(WebDriver driver){
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, COMPONENT_ID);
    }

    @Step("Set Route Target {routeTarget}")
    public void setRouteTarget(String routeTarget){
        wizard.setComponentValue(ROUTE_TARGET_FIELD_ID, routeTarget, Input.ComponentType.SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Set import to {imp}")
    public void setImport(){
        wizard.setComponentValue(IMPORT_FIELD_ID, "true", Input.ComponentType.CHECKBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Set export to {exp}")
    public void setExport(){
        wizard.setComponentValue(EXPORT_FIELD_ID, "false", Input.ComponentType.CHECKBOX);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Click accept button")
    public NewInventoryViewPage clickAccept() {
        wizard.clickAccept();
        DelayUtils.waitForPageToLoad(driver, wait);
        return new NewInventoryViewPage(driver, wait);
    }
}
