package com.oss.pages.bigdata.dfe.stepwizard.aggregate;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AggregateProcessSettingsPage extends BasePage {

    final private String ON_FAILURE_INPUT_ID = "failure-input";
    final private String STORAGE_POLICY_INPUT_ID = "storagePolicy-input";
    final private Wizard wizard;

    public AggregateProcessSettingsPage(WebDriver driver, WebDriverWait wait, Wizard wizard) {
        super(driver, wait);

        this.wizard = wizard;
    }

    private void fillOnFailure(String onFailure){
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox onFailureInput = (Combobox) wizard.getComponent(ON_FAILURE_INPUT_ID, Input.ComponentType.COMBOBOX);
        onFailureInput.setValue(Data.createSingleData(onFailure));
    }

    private void fillStoragePolicy(String storagePolicy){
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox storagePolicyInput = (Combobox) wizard.getComponent(STORAGE_POLICY_INPUT_ID, Input.ComponentType.COMBOBOX);
        storagePolicyInput.setValue(Data.createSingleData(storagePolicy));
    }

    @Step("I fill Process Settings Step with onFailure: {onFailure} and storage policy: {storagePolicy}")
    public void fillProcessSettingsStep(String onFailure, String storagePolicy){
        fillOnFailure(onFailure);
        fillStoragePolicy(storagePolicy);
    }

}
