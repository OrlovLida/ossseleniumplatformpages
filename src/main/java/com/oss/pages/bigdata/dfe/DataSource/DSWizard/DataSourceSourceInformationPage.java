package com.oss.pages.bigdata.dfe.DataSource.DSWizard;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SCRIPT_COMPONENT;

public class DataSourceSourceInformationPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceSourceInformationPage.class);
    private final String DATABASE_INPUT_ID = "dataSourceDatabaseId-input";
    private final String QUERY_INPUT_ID = "script-component";
    private final String WIZARD_ID = "dataSourcesWizardId";

    private final Wizard sourceInformationWizard;

    public DataSourceSourceInformationPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        sourceInformationWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
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
        log.debug("Setting query field with {}", query);
    }
}
