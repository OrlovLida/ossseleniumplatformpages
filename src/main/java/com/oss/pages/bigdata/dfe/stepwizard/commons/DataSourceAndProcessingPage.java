package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceAndProcessingPage extends BaseStepPage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceAndProcessingPage.class);
    final private String FEED_INPUT_ID = "dataSource-input";

    public DataSourceAndProcessingPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    @Step("I fill Data Source and Processing Step with feed name: {feedName}")
    public void fillFeed(String feedName){
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox feedInput = (Combobox) getWizard(driver, wait).getComponent(FEED_INPUT_ID, Input.ComponentType.COMBOBOX);
        feedInput.setValue(Data.createSingleData(feedName));
        log.debug("Setting feed name with: {}", feedName);
        log.info("Filled Data Source and Processing Step");
    }
}
