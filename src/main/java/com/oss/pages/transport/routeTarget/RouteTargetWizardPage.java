package com.oss.pages.transport.routeTarget;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.configuration.Configuration.CONFIGURATION;

/**
 * @author Kamil Sikora
 */
public class RouteTargetWizardPage extends BasePage {

    private static final String ROUTE_TARGET_VALUE_DATA_ATTRIBUTENAME = "uid.field.value";
    private static final String DESCRIPTION_DATA_ATTRIBUTENAME = "uid.field.description";
    private static final String COMPONENT_ID = "vpn.routetarget.app";

    public RouteTargetWizardPage(WebDriver driver) {
        super(driver);
    }

    public RouteTargetWizardPage goToRouteTargetWizardPage() {
        DelayUtils.waitForPageToLoad(driver, wait);
        driver.get(String.format("%s/#/view/transport/tpt/vpn/routetarget?perspective=LIVE", CONFIGURATION.getUrl()));
        DelayUtils.waitForPageToLoad(driver, wait);
        return new RouteTargetWizardPage(driver);
    }

    @Step("Set Route Target value to {routeTarget}")
    public void setRouteTarget(String routeTarget) {
        getWizard().setComponentValue(ROUTE_TARGET_VALUE_DATA_ATTRIBUTENAME, routeTarget, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set description to {description}")
    public void setDescription(String description) {
        getWizard().setComponentValue(DESCRIPTION_DATA_ATTRIBUTENAME, description, Input.ComponentType.TEXT_AREA);
    }

    @Step("Click accept button")
    public void clickAccept() {
        getWizard().clickAccept();
    }

    private Wizard getWizard() {
        return Wizard.createByComponentId(driver, wait, COMPONENT_ID);
    }
}
