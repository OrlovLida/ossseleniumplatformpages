package com.oss.pages.bigdata.dfe.DataSource.DSWizard;


import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.stepwizard.commons.BaseStepPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceBasicInfoPage extends BaseStepPage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceBasicInfoPage.class);
    private final String NAME_INPUT_ID = "dataSourcesNameId";

    public DataSourceBasicInfoPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    public void fillName(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(name, NAME_INPUT_ID);
        log.debug("Setting name with: {}", name);
    }

    @Step("I fill Data Source Name: {name}")
    public void fillBasicInformationStep(String name) {
        fillName(name);
    }
}
