package com.oss.pages.bigdata.dfe.stepwizard.commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class StoragePage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(StoragePage.class);
    private static final String TABLE_NAME_INPUT_ID = "tableName";
    private final Wizard storageWizard;

    public StoragePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        storageWizard = Wizard.createWizard(driver, wait);
    }

    @Step("I fill Storage Step with table name: {tableName}")
    public void fillStorageStep(String tableName) {
        fillTableName(tableName);
        log.info("Filled Storage Step");
    }

    @Step("I click Accept")
    public void clickAccept() {
        DelayUtils.waitForPageToLoad(driver, wait);
        storageWizard.clickAccept();
        log.info("Finishing Step Wizard by clicking 'Accept'");
    }

    private void fillTableName(String tableName) {
        storageWizard.setComponentValue(TABLE_NAME_INPUT_ID, tableName, TEXT_FIELD);
        log.debug("Setting table name with {}", tableName);
    }
}
