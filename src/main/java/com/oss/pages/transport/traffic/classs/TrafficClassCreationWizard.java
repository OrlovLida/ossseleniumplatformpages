package com.oss.pages.transport.traffic.classs;

import org.openqa.selenium.WebDriver;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.platform.NewInventoryViewPage;

import io.qameta.allure.Step;

public class TrafficClassCreationWizard extends TrafficClassWizardPage {

    private static final String COMPONENT_ID = "trafficClassWizard";
    private final Wizard wizard;

    public TrafficClassCreationWizard(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, COMPONENT_ID);
    }

    public Wizard getWizard() {
        return wizard;
    }

    @Step("Click accept button")
    public NewInventoryViewPage clickAccept() {
        getWizard().clickAccept();
        return new NewInventoryViewPage(driver, wait);
    }
}
