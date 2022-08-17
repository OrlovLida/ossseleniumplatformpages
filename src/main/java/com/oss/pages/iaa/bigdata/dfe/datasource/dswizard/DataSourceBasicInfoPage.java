package com.oss.pages.iaa.bigdata.dfe.datasource.dswizard;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class DataSourceBasicInfoPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceBasicInfoPage.class);
    private static final String NAME_INPUT_ID = "dataSourcesNameId";
    private static final String WIZARD_ID = "dataSourcesWizardId";

    private final Wizard basicInfoWizard;

    public DataSourceBasicInfoPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        basicInfoWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    public void fillName(String name) {
        basicInfoWizard.setComponentValue(NAME_INPUT_ID, name);
        log.debug("Setting name with: {}", name);
    }

    @Step("I fill Data Source Name: {name}")
    public void fillBasicInformationStep(String name) {
        fillName(name);
    }
}
