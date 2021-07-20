package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;

public class DataSourceAndProcessingPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceAndProcessingPage.class);
    final private String FEED_INPUT_ID = "dataSource-input";
    final private Wizard dsAndProcessingWizard;

    public DataSourceAndProcessingPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        dsAndProcessingWizard = Wizard.createWizard(driver, wait);
    }

    @Step("I fill Data Source and Processing Step with feed name: {feedName}")
    public void fillFeed(String feedName) {
        dsAndProcessingWizard.setComponentValue(FEED_INPUT_ID, feedName, COMBOBOX);
        log.debug("Setting feed name with: {}", feedName);
        log.info("Filled Data Source and Processing Step");
    }

    @Step("I click Next Step")
    public void clickNextStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        dsAndProcessingWizard.clickNextStep();
        log.info("I click Next Step");
    }
}
