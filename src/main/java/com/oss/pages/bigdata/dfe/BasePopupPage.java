package com.oss.pages.bigdata.dfe;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.TextField;
import com.oss.framework.data.Data;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import com.oss.pages.bigdata.dfe.dictionary.DictionaryPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BasePopupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(DictionaryPage.class);

    public BasePopupPage(WebDriver driver, WebDriverWait wait){
        super(driver,wait);

    }

    protected void fillTextField(String value, String componentId) {
        TextField input =  (TextField) Wizard.createPopupWizard(driver, wait).getComponent(componentId, Input.ComponentType.TEXT_FIELD);
        input.setValue(Data.createSingleData(value));
    }

    @Step("I click Save")
    public void clickSave(){
        Wizard.createPopupWizard(driver, wait).clickSave();
        log.info("Finishing by clicking 'Save'");
    }
}
