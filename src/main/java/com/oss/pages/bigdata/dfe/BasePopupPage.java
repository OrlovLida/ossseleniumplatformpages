package com.oss.pages.bigdata.dfe;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.TextField;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public abstract class BasePopupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BasePopupPage.class);

    private static final String NAME_INPUT_ID = "name";
    private static final String DESCRIPTION_INPUT_ID = "description";
    private static final String WIZARD_ID = "Popup";

    public BasePopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void fillName(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(name, NAME_INPUT_ID);
        log.debug("Setting name with: {}", name);
    }

    public void fillDescription(String description) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(description, DESCRIPTION_INPUT_ID);
        log.debug("Setting description with: {}", description);
    }

    @Step("I click Save")
    public void clickSave() {
        Wizard.createByComponentId(driver, wait, WIZARD_ID).clickSave();
        log.info("Finishing by clicking 'Save'");
    }

    protected void fillTextField(String value, String componentId) {
        TextField input = (TextField) Wizard.createByComponentId(driver, wait, WIZARD_ID).getComponent(componentId, Input.ComponentType.TEXT_FIELD);
        input.setValue(Data.createSingleData(value));
    }
}
