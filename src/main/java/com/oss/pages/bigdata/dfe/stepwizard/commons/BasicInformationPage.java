package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Switcher;
import com.oss.framework.components.inputs.TextArea;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasicInformationPage extends BaseStepPage {

    final private String NAME_INPUT_ID = "name";
    final private String DESCRIPTION_INPUT_ID = "description";
    final private String IS_ACTIVE_INPUT_ID = "active";

    public BasicInformationPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    public void fillName(String name){
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(name, NAME_INPUT_ID);
    }

    public void fillDescription(String description){
        DelayUtils.waitForPageToLoad(driver, wait);
        TextArea descriptionInput = (TextArea) getWizard(driver, wait).getComponent(DESCRIPTION_INPUT_ID, Input.ComponentType.TEXT_AREA);
        descriptionInput.setValue(Data.createSingleData(description));
    }

    public void fillIsActive(Boolean isActive){
        DelayUtils.waitForPageToLoad(driver, wait);
        Switcher isActiveInput = (Switcher) getWizard(driver, wait).getComponent(IS_ACTIVE_INPUT_ID, Input.ComponentType.SWITCHER);
        isActiveInput.setValue(Data.createSingleData(isActive.toString()));
    }

    @Step("I fill Basic Information Step with name: {name}")
    public void fillBasicInformationStep(String name){
        fillName(name);
    }

}
