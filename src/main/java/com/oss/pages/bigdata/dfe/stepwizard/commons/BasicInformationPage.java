package com.oss.pages.bigdata.dfe.stepwizard.commons;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.*;

public class BasicInformationPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(BasicInformationPage.class);

    private static final String CATEGORY_INPUT_ID = "category";
    private final String NAME_INPUT_ID = "name";
    private final String PROCESS_ID = "factId";
    private final Wizard basicInfoWizard;

    public BasicInformationPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        basicInfoWizard = Wizard.createWizard(driver, wait);
    }

    public void fillCategory(String category) {
        basicInfoWizard.setComponentValue(CATEGORY_INPUT_ID, category, MULTI_SEARCH_FIELD);
        log.debug("Setting category with: {}", category);
    }

    public void fillName(String name) {
        basicInfoWizard.setComponentValue(NAME_INPUT_ID, name, TEXT_FIELD);
        log.debug("Setting name with: {}", name);
    }

    public void fillProcess(String process) {
        basicInfoWizard.setComponentValue(PROCESS_ID, process, COMBOBOX);
        log.debug("Setting process with: {}", process);
    }

    @Step("I click Next Step")
    public void clickNextStep() {
        DelayUtils.waitForPageToLoad(driver, wait);
        basicInfoWizard.clickNextStep();
        log.info("I click Next Step");
    }
}
