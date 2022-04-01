package com.oss.pages.bigdata.dfe.externalresource;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class ExternalResourcesPopupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ExternalResourcesPopupPage.class);

    private static final String NAME_INPUT_ID = "name";
    private static final String TYPE_INPUT_ID = "type";
    private static final String CONNECTION_INPUT_ID = "connection";

    private final Wizard externalResourcesWizard;

    public ExternalResourcesPopupPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait);
        externalResourcesWizard = Wizard.createByComponentId(driver, wait, wizardId);
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
        log.info("Clicking Save button");
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}
