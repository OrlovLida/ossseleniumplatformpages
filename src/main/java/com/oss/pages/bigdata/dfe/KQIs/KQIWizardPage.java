package com.oss.pages.bigdata.dfe.KQIs;

import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.*;

public class KQIWizardPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(KQIWizardPage.class);

    private final String KPI_NAME_INPUT_ID = "kqiNameId";
    private final String VALUE_TYPE_ID = "kqiValueType-input";
    private final String UNIT_TYPE_ID = "kqiUnitId-input";
    private final String FORMULA_ID = "kqiFormulaId";
    private final Wizard kqiWizard;

    public KQIWizardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        kqiWizard = Wizard.createWizard(driver, wait);
    }

    public void fillName(String name) {
        kqiWizard.setComponentValue(KPI_NAME_INPUT_ID, name, TEXT_FIELD);
        log.debug("Setting name with: {}", name);
    }

    public void fillValueType(String valueType) {
        kqiWizard.setComponentValue(VALUE_TYPE_ID, valueType, COMBOBOX);
        log.debug("Setting value type to: {}", valueType);
    }

    public void fillUnitType(String unitType) {
        kqiWizard.setComponentValue(UNIT_TYPE_ID, unitType, COMBOBOX);
        log.debug("Setting unit type to: {}", unitType);
    }

    public void fillFormula(String formula) {
        kqiWizard.setComponentValue(FORMULA_ID, formula, TEXT_AREA);
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
    }
}