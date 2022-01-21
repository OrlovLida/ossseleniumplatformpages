package com.oss.pages.transport.routeTarget;

import org.openqa.selenium.WebDriver;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

/**
 * @author Kamil Sikora
 */
public class RouteTargetWizardPage extends BasePage {

    private static final String ROUTE_TARGET_VALUE_DATA_ATTRIBUTENAME = "uid.field.value";
    private static final String DESCRIPTION_DATA_ATTRIBUTENAME = "uid.field.description";

    private final Wizard wizard;

    public RouteTargetWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createWizard(driver, wait);
    }

    @Step("Set Route Target value to {routeTarget}")
    public void setRouteTarget(String routeTarget) {
        wizard.setComponentValue(ROUTE_TARGET_VALUE_DATA_ATTRIBUTENAME, routeTarget, Input.ComponentType.TEXT_FIELD);
    }

    @Step("Set description to {description}")
    public void setDescription(String description) {
        wizard.setComponentValue(DESCRIPTION_DATA_ATTRIBUTENAME, description, Input.ComponentType.TEXT_AREA);
    }

    @Step("Click accept button")
    public RouteTargetOverviewPage clickAccept() {
        wizard.clickAccept();
        return new RouteTargetOverviewPage(driver);
    }
}
