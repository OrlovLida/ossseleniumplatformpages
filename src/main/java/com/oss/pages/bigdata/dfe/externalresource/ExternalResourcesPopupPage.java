package com.oss.pages.bigdata.dfe.externalresource;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.bigdata.dfe.BasePopupPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalResourcesPopupPage extends BasePopupPage {

    private static final Logger log = LoggerFactory.getLogger(ExternalResourcesPopupPage.class);

    private final String NAME_INPUT_ID = "name";
    private final String TYPE_INPUT_ID = "type";
    private final String CONNECTION_INPUT_ID = "connection";

    public ExternalResourcesPopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void fillName(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(name, NAME_INPUT_ID);
        log.debug("Setting name with: {}", name);
    }

    public void fillType(String type) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox typeInput = (Combobox) Wizard.createPopupWizard(driver, wait).getComponent(TYPE_INPUT_ID, Input.ComponentType.COMBOBOX);
        typeInput.setValue(Data.createSingleData(type));
        log.debug("Setting External Resource Type with: {}", type);
    }

    public void fillConnectionUrl(String connection) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(connection, CONNECTION_INPUT_ID);
        log.debug("Setting Connection Url with: {}", connection);
    }

    @Step("I fill External Resources Popup fields with name: {name} and type: {type}")
    public void fillExternalResourcesPopup(String name, String type, String connection) {
        fillName(name);
        fillType(type);
        fillConnectionUrl(connection);
        log.info("Filled External Resources Popup fields");
    }

}
