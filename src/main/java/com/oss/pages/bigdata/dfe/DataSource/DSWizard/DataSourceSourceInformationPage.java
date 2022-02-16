package com.oss.pages.bigdata.dfe.DataSource.DSWizard;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.SCRIPT_COMPONENT;
import static com.oss.framework.utils.DelayUtils.waitForPageToLoad;

public class DataSourceSourceInformationPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceSourceInformationPage.class);
    private final String DATABASE_INPUT_ID = "dataSourceDatabaseId-input";
    private final String QUERY_INPUT_ID = "script-component";
    private final String WIZARD_ID = "dataSourcesWizardId";
    private final String UPLOAD_WIZARD = "dataSourceUploadDataFormatId";
    private final String DS_TYPE_INPUT_ID = "dataSourceDataSourceTypeId-input";

    private final Wizard sourceInformationWizard;

    public DataSourceSourceInformationPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        sourceInformationWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    @Step("I fill Database with: {database}")
    public void fillDatabase(String database) {
        waitForPageToLoad(driver, wait);
        sourceInformationWizard.setComponentValue(DATABASE_INPUT_ID, database, COMBOBOX);
        log.debug("Setting database with: {}", database);
    }

    @Step("I fill Data Source query")
    public void fillQuery(String query) {
        waitForPageToLoad(driver, wait);
        sourceInformationWizard.setComponentValue(QUERY_INPUT_ID, query, SCRIPT_COMPONENT);
        log.debug("Setting query field with {}", query);
    }

    @Step("I upload a Data Source file")
    public void uploadCSVFile(String path) {
        try {
            URL res = getClass().getClassLoader().getResource(path);
            File file = Paths.get(res.toURI()).toFile();
            String absolutePath = file.getAbsolutePath();
            Wizard wizard = Wizard.createByComponentId(driver, wait, UPLOAD_WIZARD);
            Input input = wizard.getComponent(UPLOAD_WIZARD, Input.ComponentType.FILE_CHOOSER);
            input.setSingleStringValue(absolutePath);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot load file", e);
        }
    }

    @Step("I fill Data Source Type with {dsType}")
    public void selectDSType(String dsType) {
        waitForPageToLoad(driver, wait);
        sourceInformationWizard.setComponentValue(DS_TYPE_INPUT_ID, dsType, COMBOBOX);
        log.debug("Setting Data Source Type with: {}", dsType);
    }
}
