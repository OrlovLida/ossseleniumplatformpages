package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.SearchField;
import com.oss.framework.components.inputs.TextField;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BaseStepPage extends BasePage implements StepWizardInterface {

    private final String wizardId;

    public BaseStepPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait);

        this.wizardId = wizardId;
    }

    @Override
    public String getWizardId() {
        return wizardId;
    }

    protected void fillTextField(String value, String componentId) {
        TextField input =  (TextField) getWizard(driver, wait).getComponent(componentId, Input.ComponentType.TEXT_FIELD);
        input.setValue(Data.createSingleData(value));
    }

    public void fillCombobox(String input, String comboboxId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox comboboxInput = (Combobox) getWizard(driver, wait).getComponent(comboboxId, Input.ComponentType.COMBOBOX);
        comboboxInput.setValue(Data.createSingleData(input));
    }

    public void fillSearchField(String input, String searchFieldId) {
        DelayUtils.waitForPageToLoad(driver, wait);
        SearchField searchFieldInput = (SearchField) getWizard(driver, wait).getComponent(searchFieldId, Input.ComponentType.SEARCH_FIELD);
        searchFieldInput.setValue(Data.createSingleData(input));
    }
}
