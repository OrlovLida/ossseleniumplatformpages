package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DataSourceAndProcessingPage extends BaseStepPage {

    final private String FEED_INPUT_ID = "dataSource-input";

    public DataSourceAndProcessingPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    public void fillFeed(String feedName){
        DelayUtils.waitForPageToLoadWithoutAppPreloader(driver, wait);
        Combobox feedInput = (Combobox) getWizard(driver, wait).getComponent(FEED_INPUT_ID, Input.ComponentType.COMBOBOX);
        feedInput.setValue(Data.createSingleData(feedName));
    }
}
