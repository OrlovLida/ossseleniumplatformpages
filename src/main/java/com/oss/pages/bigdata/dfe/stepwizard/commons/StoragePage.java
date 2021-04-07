package com.oss.pages.bigdata.dfe.stepwizard.commons;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class StoragePage extends BaseStepPage {

    final private String TABLE_NAME_INPUT_ID = "tableName";

    public StoragePage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    private void fillTableName(String tableName){
        fillTextField(tableName, TABLE_NAME_INPUT_ID);
    }

    @Step("I fill Storage Step with table name: {tableName}")
    public void fillStorageStep(String tableName){
        fillTableName(tableName);
    }
}
