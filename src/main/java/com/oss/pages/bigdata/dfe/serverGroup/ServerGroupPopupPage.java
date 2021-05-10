package com.oss.pages.bigdata.dfe.serverGroup;

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

public class ServerGroupPopupPage extends BasePopupPage {

    private static final Logger log = LoggerFactory.getLogger(ServerGroupPopupPage.class);


    private final String SERVER_GROUP_NAME_INPUT_ID = "name";
    private final String PROTOCOL_INPUT_ID = "protocol-input";

    public ServerGroupPopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void fillName(String name) {
        DelayUtils.waitForPageToLoad(driver,wait);
        fillTextField(name, SERVER_GROUP_NAME_INPUT_ID);
        log.debug("Setting name with: {}", name);
    }

    public void fillProtocol(String protocol){
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox protocolInput = (Combobox) Wizard.createPopupWizard(driver, wait).getComponent(PROTOCOL_INPUT_ID, Input.ComponentType.COMBOBOX);
        protocolInput.setValue(Data.createSingleData(protocol));
        log.debug("Setting protocol type with: {}", protocol);

    }

    @Step("Fill Add New Server Group popup fields with name: {name} and protocol {protocol}")
    public void fillServerGroupPopup(String name, String protocol) {
        fillName(name);
        fillProtocol(protocol);
        log.info("Filled Server Group Popup fields");
    }



}

