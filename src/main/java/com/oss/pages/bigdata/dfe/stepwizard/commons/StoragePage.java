package com.oss.pages.bigdata.dfe.stepwizard.commons;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoragePage extends BaseStepPage {

    private static final Logger log = LoggerFactory.getLogger(StoragePage.class);
    final private String TABLE_NAME_INPUT_ID = "tableName";

    public StoragePage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    private void fillTableName(String tableName){
        fillTextField(tableName, TABLE_NAME_INPUT_ID);
        log.debug("Setting table name with {}", tableName);
    }

    @Step("I fill Storage Step with table name: {tableName}")
    public void fillStorageStep(String tableName){
        fillTableName(tableName);
        log.info("Filled Storage Step");
    }
}
