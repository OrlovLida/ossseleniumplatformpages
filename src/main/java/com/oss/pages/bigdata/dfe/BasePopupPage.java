package com.oss.pages.bigdata.dfe;

import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.TextField;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BasePopupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BasePopupPage.class);

    private final String NAME_INPUT_ID = "name";
    private final String DESCRIPTION_INPUT_ID = "description";

    public BasePopupPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    protected void fillTextField(String value, String componentId) {
        TextField input = (TextField) Wizard.createPopupWizard(driver, wait).getComponent(componentId, Input.ComponentType.TEXT_FIELD);
        input.setValue(Data.createSingleData(value));
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
        Wizard.createPopupWizard(driver, wait).clickSave();
        log.info("Finishing by clicking 'Save'");
    }
}
