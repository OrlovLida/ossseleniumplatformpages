package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Switcher;
import com.oss.framework.components.inputs.TextArea;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicInformationPage extends BaseStepPage {

    private static final Logger log = LoggerFactory.getLogger(BasicInformationPage.class);

    final private String NAME_INPUT_ID = "name";
    final private String DESCRIPTION_INPUT_ID = "description";
    final private String IS_ACTIVE_INPUT_ID = "active";

    public BasicInformationPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    public void fillName(String name){
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(name, NAME_INPUT_ID);
        log.debug("Setting name with: {}", name);
    }

    public void fillDescription(String description){
        DelayUtils.waitForPageToLoad(driver, wait);
        TextArea descriptionInput = (TextArea) getWizard(driver, wait).getComponent(DESCRIPTION_INPUT_ID, Input.ComponentType.TEXT_AREA);
        descriptionInput.setValue(Data.createSingleData(description));
        log.debug("Setting description with: {}", description);
    }

    public void fillIsActive(Boolean isActive){
        DelayUtils.waitForPageToLoad(driver, wait);
        Switcher isActiveInput = (Switcher) getWizard(driver, wait).getComponent(IS_ACTIVE_INPUT_ID, Input.ComponentType.SWITCHER);
        isActiveInput.setValue(Data.createSingleData(isActive.toString()));
        log.debug("Setting isActive: {}", isActive);
    }

    @Step("I fill Basic Information Step with name: {name}")
    public void fillBasicInformationStep(String name){
        fillName(name);
        log.info("Filled Basic Information Step");
    }

}
