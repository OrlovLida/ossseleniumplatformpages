package com.oss.pages.transport.traffic.classs;

import org.openqa.selenium.WebDriver;

import com.comarch.oss.web.pages.NewInventoryViewPage;
import com.oss.framework.components.inputs.Button;
import com.oss.framework.wizard.Wizard;

import io.qameta.allure.Step;

/**
 * @author Kamil Szota
 */
public class TrafficClassModificationWizardPage extends TrafficClassWizardPage {

    private static final String COMPONENT_ID = "card-content_trafficClassModificationWizard_prompt-card";
    private static final String SAVE_CHANGES_BUTTON_TEST_ID = "buttonAppId-0";

    private final Wizard wizard;

    public TrafficClassModificationWizardPage(WebDriver driver) {
        super(driver);
        wizard = Wizard.createByComponentId(driver, wait, COMPONENT_ID);
    }

    public Wizard getWizard() {
        return wizard;
    }

    @Step("Click save changes button")
    public NewInventoryViewPage clickSaveChanges() {
        Button saveButton = Button.createById(driver, SAVE_CHANGES_BUTTON_TEST_ID);
        saveButton.click();
        return new NewInventoryViewPage(driver, wait);
    }
}
