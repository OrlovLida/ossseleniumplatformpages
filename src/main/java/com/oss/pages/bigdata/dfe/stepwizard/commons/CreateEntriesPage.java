package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.TextField;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateEntriesPage extends BaseStepPage{

    private static final Logger log = LoggerFactory.getLogger(CreateEntriesPage.class);

    final private String KEY_INPUT_ID = "Key";
    final private String VALUE_INPUT_ID = "Value";
    
    public CreateEntriesPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }


    protected void fillTextField(String value, String componentId) {
        TextField input =  (TextField) getWizard(driver, wait).getComponent(componentId, Input.ComponentType.TEXT_FIELD);
        input.setValue(Data.createSingleData(value));
    }
    public void fillKey(String key){
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(key, KEY_INPUT_ID);
        log.debug("Setting name with: {}", key);
    }

    public void fillValue(String value){
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(value,VALUE_INPUT_ID);
        log.debug("Setting description with: {}", value);
    }

    @Step("I fill Add New Entry field with key: {key} and value with: {value}")
    public void fillAddNewEntry(String key, String value){
       fillKey(key);
       fillValue(value);
        log.info("Filled Add New Entry");
    }
}
