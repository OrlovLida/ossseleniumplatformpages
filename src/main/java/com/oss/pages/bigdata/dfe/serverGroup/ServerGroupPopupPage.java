package com.oss.pages.bigdata.dfe.serverGroup;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.bigdata.dfe.BasePopupPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class ServerGroupPopupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ServerGroupPopupPage.class);
    private final String SERVER_GROUP_NAME_INPUT_ID = "name";
    private final String PROTOCOL_INPUT_ID = "protocol-input";

    private final Wizard serverGroupPopupWizard;

    public ServerGroupPopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        serverGroupPopupWizard = Wizard.createPopupWizard(driver, wait);
    }

    public void fillName(String name) {
        serverGroupPopupWizard.setComponentValue(SERVER_GROUP_NAME_INPUT_ID, name, TEXT_FIELD);
        log.debug("Setting name with: {}", name);
    }

    public void fillProtocol(String protocol) {
        serverGroupPopupWizard.setComponentValue(PROTOCOL_INPUT_ID,protocol, COMBOBOX);
        log.debug("Setting protocol type with: {}", protocol);
    }

    @Step("Fill Add New Server Group popup fields with name: {name} and protocol {protocol}")
    public void fillServerGroupPopup(String name, String protocol) {
        fillName(name);
        fillProtocol(protocol);
        log.info("Filled Server Group Popup fields");
    }

    @Step("I click Save")
    public void clickSave() {
        DelayUtils.waitForPageToLoad(driver, wait);
        serverGroupPopupWizard.clickSave();
        log.info("Finishing by clicking 'Save'");
    }
}

