package com.oss.pages.bigdata.dfe.externalresource;

import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class ExternalResourcesPopupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ExternalResourcesPopupPage.class);

    private final String NAME_INPUT_ID = "name";
    private final String TYPE_INPUT_ID = "type";
    private final String CONNECTION_INPUT_ID = "connection";

    private final Wizard externalResourcesWizard;

    public ExternalResourcesPopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        externalResourcesWizard = Wizard.createWizard(driver, wait);
    }

    public void fillName(String name) {
        externalResourcesWizard.setComponentValue(NAME_INPUT_ID, name, TEXT_FIELD);
        log.debug("Setting name with: {}", name);
    }

    public void fillType(String type) {
        externalResourcesWizard.setComponentValue(TYPE_INPUT_ID, type, COMBOBOX);
        log.debug("Setting External Resource Type with: {}", type);
    }

    public void fillConnectionUrl(String connection) {
        externalResourcesWizard.setComponentValue(CONNECTION_INPUT_ID, connection, TEXT_FIELD);
        log.debug("Setting Connection Url with: {}", connection);
    }

    @Step("I fill External Resources Popup fields with name: {name} and type: {type}")
    public void fillExternalResourcesPopup(String name, String type, String connection) {
        fillName(name);
        fillType(type);
        fillConnectionUrl(connection);
        log.info("Filled External Resources Popup fields");
    }

    @Step("I click Save")
    public void clickSave() {
        externalResourcesWizard.clickSave();
    }
}
