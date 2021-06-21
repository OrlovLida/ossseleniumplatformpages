package com.oss.pages.bigdata.dfe.DataSource.DSWizard;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.ScriptComponent;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.bigdata.dfe.stepwizard.commons.BaseStepPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SCRIPT_COMPONENT;

public class DataSourceSourceInformationPage extends BaseStepPage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceSourceInformationPage.class);
    private final String DATABASE_INPUT_ID = "dataSourceDatabaseId-input";
    private final String QUERY_INPUT_ID = "script-component";

    private final Wizard sourceInformationWizard;

    public DataSourceSourceInformationPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
        sourceInformationWizard = Wizard.createWizard(driver, wait);
    }

    @Step("I fill Database with: {database}")
    public void fillDatabase(String database) {
        DelayUtils.waitForPageToLoad(driver, wait);
        sourceInformationWizard.setComponentValue(DATABASE_INPUT_ID, database, COMBOBOX);
        log.debug("Setting database with: {}", database);
    }

    @Step("I fill Data Source query")
    public void fillQuery(String query) {
        DelayUtils.waitForPageToLoad(driver, wait);
        sourceInformationWizard.setComponentValue(QUERY_INPUT_ID, query, SCRIPT_COMPONENT);
//        ScriptComponent queryInput = (ScriptComponent) getWizard(driver,wait).getComponent(QUERY_INPUT_ID, Input.ComponentType.SCRIPT_COMPONENT);
//        queryInput.setValue(Data.createSingleData(query));
        log.debug("Setting query field with {}", query);
    }
}
