package com.oss.pages.bigdata.dfe.DataSource.DSWizard;


import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class DataSourceBasicInfoPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceBasicInfoPage.class);
    private final String NAME_INPUT_ID = "dataSourcesNameId";

    private final Wizard basicInfoWizard;

    public DataSourceBasicInfoPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        basicInfoWizard = Wizard.createWizard(driver, wait);
    }

    public void fillName(String name) {
        basicInfoWizard.setComponentValue(NAME_INPUT_ID, name, TEXT_FIELD);
        log.debug("Setting name with: {}", name);
    }

    @Step("I fill Data Source Name: {name}")
    public void fillBasicInformationStep(String name) {
        fillName(name);
    }
}
