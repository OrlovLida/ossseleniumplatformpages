package com.oss.pages.transport.VSI;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

/**
 * @author Kamil Sikora
 */
public class VSIRouteTargetAssignmentPage extends BasePage {

    private static final String ROUTE_TARGET_DATA_ATTRIBUTENAME = "Add_Imp/Exp_Route_Target_id";
    private static final String EXPORT_CHECKBOX_DATA_ATTRIBUTENAME = "check_box_export_uid";
    private static final String IMPORT_CHECKBOX_DATA_ATTRIBUTENAME = "check_box_import_uid";

    private final Wizard wizard;

    public VSIRouteTargetAssignmentPage(WebDriver driver){
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Set Route Target value to {routeTarget}")
    public void setRouteTarget(String routeTarget){
        wizard.setComponentValue(ROUTE_TARGET_DATA_ATTRIBUTENAME, routeTarget, Input.ComponentType.SEARCH_FIELD);
        DelayUtils.waitForPageToLoad(driver, wait);
    }

    @Step("Set import checkbox value to true")
    public void setImportRole(){
        wizard.setComponentValue(IMPORT_CHECKBOX_DATA_ATTRIBUTENAME, "true", Input.ComponentType.CHECKBOX);
    }

    @Step("Set export checkbox value to true")
    public void setExportRole(){
        wizard.setComponentValue(EXPORT_CHECKBOX_DATA_ATTRIBUTENAME, "true", Input.ComponentType.CHECKBOX);
    }

    @Step("Click accept button")
    public VSIOverviewPage clickAccept(){
        DelayUtils.waitForPageToLoad(driver, wait);
        wizard.clickAcceptOldWizard();
        return new VSIOverviewPage(driver);
    }

}
