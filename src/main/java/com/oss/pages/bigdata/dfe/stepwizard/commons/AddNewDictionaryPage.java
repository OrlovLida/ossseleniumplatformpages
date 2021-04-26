package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.utils.DelayUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddNewDictionaryPage extends BaseStepPage {

    private static final Logger log = LoggerFactory.getLogger(AddNewDictionaryPage.class);

    final private String NAME_INPUT_ID = "name";
    final private String DESCRIPTION_INPUT_ID = "description";


    public AddNewDictionaryPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    public void fillName(String name){
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(name, NAME_INPUT_ID);
        log.debug("Setting name with: {}", name);
    }

    public void fillDescription(String description){
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(description,DESCRIPTION_INPUT_ID);
        log.debug("Setting description with: {}", description);
    }

    @Step("I fill Add New Dictionary field with name: {name} and description with: {description}")
    public void fillAddNewDictionary(String name, String description){
        fillName(name);
        fillDescription(description);
        log.info("Filled Add New Dictionary ");
    }

}
