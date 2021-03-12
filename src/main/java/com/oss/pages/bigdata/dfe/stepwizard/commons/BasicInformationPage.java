package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.Switcher;
import com.oss.framework.components.inputs.TextArea;
import com.oss.framework.components.inputs.TextField;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasicInformationPage extends BasePage {

    final private String NAME_INPUT_ID = "name";
    final private String DESCRIPTION_INPUT_ID = "description";
    final private String ETL_PROCESS_INPUT_ID = "factId-input";
    final private String IS_ACTIVE_INPUT_ID = "active";
    final private Wizard wizard;


    public BasicInformationPage(WebDriver driver, WebDriverWait wait, Wizard wizard) {
        super(driver, wait);
        this.wizard = wizard;
    }

    public void fillName(String name){
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(name, NAME_INPUT_ID);
    }

    public void fillDescription(String description){
        DelayUtils.waitForPageToLoad(driver, wait);
        TextArea descriptionInput = (TextArea) wizard.getComponent(DESCRIPTION_INPUT_ID, Input.ComponentType.TEXT_AREA);
        descriptionInput.setValue(Data.createSingleData(description));
    }

    public void fillETLProcess(String etlProcess){
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox etlProcessInput = (Combobox) wizard.getComponent(ETL_PROCESS_INPUT_ID, Input.ComponentType.COMBOBOX);
        etlProcessInput.setValue(Data.createSingleData(etlProcess));
    }

    public void fillIsActive(Boolean isActive){
        DelayUtils.waitForPageToLoad(driver, wait);
        Switcher isActiveInput = (Switcher) wizard.getComponent(IS_ACTIVE_INPUT_ID, Input.ComponentType.SWITCHER);
        isActiveInput.setValue(Data.createSingleData(isActive.toString()));
    }

    private void fillTextField(String name, String componentId) {
        TextField nameInput =  (TextField) wizard.getComponent(componentId, Input.ComponentType.TEXT_FIELD);
        nameInput.setValue(Data.createSingleData(name));
    }

    @Step("I fill Basic Information Step with name: {name} and feed: {etlProcess}")
    public void fillBasicInformationStep(String name, String etlProcess){
        fillName(name);
        fillETLProcess(etlProcess);
    }



}
