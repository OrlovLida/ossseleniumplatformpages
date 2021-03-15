package com.oss.pages.bigdata.dfe.stepwizard.aggregate;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.stepwizard.commons.BasicInformationPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AggregatesBasicInformationPage extends BasicInformationPage {

    final private String ETL_PROCESS_INPUT_ID = "factId-input";

    public AggregatesBasicInformationPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    public void fillETLProcess(String etlProcess){
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox etlProcessInput = (Combobox) getWizard(driver, wait).getComponent(ETL_PROCESS_INPUT_ID, Input.ComponentType.COMBOBOX);
        etlProcessInput.setValue(Data.createSingleData(etlProcess));
    }

    @Step("I fill Basic Information Step with name: {name} and feed: {etlProcess}")
    public void fillBasicInformationStep(String name, String etlProcess){
        fillName(name);
        fillETLProcess(etlProcess);
    }
}
