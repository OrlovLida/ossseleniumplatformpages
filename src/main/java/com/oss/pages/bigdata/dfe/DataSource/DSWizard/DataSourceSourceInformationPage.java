package com.oss.pages.bigdata.dfe.DataSource.DSWizard;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.stepwizard.commons.BaseStepPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceSourceInformationPage extends BaseStepPage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceSourceInformationPage.class);
    private final String DATABASE_INPUT_ID = "dataSourceDatabaseId-input";

    public DataSourceSourceInformationPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    @Step("I fill Database with: {database}")
    public void fillDatabase(String database) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox databaseInput = (Combobox) getWizard(driver, wait).getComponent(DATABASE_INPUT_ID, Input.ComponentType.COMBOBOX);
        databaseInput.setValue(Data.createSingleData(database));
        log.debug("Setting database with: {}", database);
    }

    // TODO podmienić fragment kodu jak framework dorzuci data-attributename, żeby nie używać xpath w page'ach
    @Step("I fill Data Source query")
    public void fillQuery(String query) {
        DelayUtils.waitForPageToLoad(driver, wait);
        WebElement queryInput = driver.findElement(By.className("CodeMirror"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].CodeMirror.setValue(\"" + query + "\");", queryInput);
        log.debug("Setting query field with {}", query);
    }
}
