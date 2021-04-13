package com.oss.pages.bigdata.dfe.stepwizard.aggregate;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.stepwizard.commons.BasicInformationPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AggregatesBasicInformationPage extends BasicInformationPage {

    private static final Logger log = LoggerFactory.getLogger(AggregatesBasicInformationPage.class);

    final private String ETL_PROCESS_INPUT_ID = "factId-input";

    public AggregatesBasicInformationPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    public void fillETLProcess(String etlProcess){
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox etlProcessInput = (Combobox) getWizard(driver, wait).getComponent(ETL_PROCESS_INPUT_ID, Input.ComponentType.COMBOBOX);
        etlProcessInput.setValue(Data.createSingleData(etlProcess));
        log.debug("Setting ETL Process: {}", etlProcess);
    }

    @Step("I fill Basic Information Step with name: {name} and feed: {etlProcess}")
    public void fillBasicInformationStep(String name, String etlProcess){
        fillName(name);
        fillETLProcess(etlProcess);
        log.info("Filled Basic Information Step");
    }
}
