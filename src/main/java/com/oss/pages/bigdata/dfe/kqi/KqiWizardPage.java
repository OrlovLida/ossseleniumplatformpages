package com.oss.pages.bigdata.dfe.kqi;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.wizard.Wizard;
import com.oss.pages.BasePage;

import io.qameta.allure.Step;

public class KqiWizardPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(KqiWizardPage.class);

    private static final String KPI_NAME_INPUT_ID = "kqiNameId";
    private static final String VALUE_TYPE_ID = "kqiValueType";
    private static final String UNIT_TYPE_ID = "kqiUnitId";
    private static final String FORMULA_ID = "kqiFormulaId";
    private static final String KQI_WIZARD_ID = "kqiWizardWidgetId";
    private final Wizard kqiWizard;

    public KqiWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        kqiWizard = Wizard.createByComponentId(driver, wait, KQI_WIZARD_ID);
    }

    public void fillName(String name) {
        kqiWizard.setComponentValue(KPI_NAME_INPUT_ID, name);
        log.debug("Setting name with: {}", name);
    }

    public void fillValueType(String valueType) {
        kqiWizard.setComponentValue(VALUE_TYPE_ID, valueType);
        log.debug("Setting value type to: {}", valueType);
    }

    public void fillUnitType(String unitType) {
        kqiWizard.setComponentValue(UNIT_TYPE_ID, unitType);
        log.debug("Setting unit type to: {}", unitType);
    }

    public void fillFormula(String formula) {
        kqiWizard.setComponentValue(FORMULA_ID, formula);
        log.debug("Setting formula with: {}", formula);
    }

    @Step("I fill KQI name with {name}," +
            "value type with {valueType}," +
            "unit type with: {unitType}," +
            "formula with: {formula}")
    public void fillKQIWizard(String name, String valueType, String unitType, String formula) {
        fillName(name);
        fillValueType(valueType);
        fillUnitType(unitType);
        fillFormula(formula);
        log.info("Filled Basic Information Step");
    }

    @Step("I click Accept")
    public void clickAccept() {
        kqiWizard.clickAccept();
        log.info("Finishing Step Wizard by clicking 'Accept'");
        DelayUtils.waitForPageToLoad(driver, wait);
    }
}