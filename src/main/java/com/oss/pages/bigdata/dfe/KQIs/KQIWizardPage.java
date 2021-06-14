package com.oss.pages.bigdata.dfe.KQIs;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.components.inputs.TextArea;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.stepwizard.commons.BaseStepPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KQIWizardPage extends BaseStepPage {

    private static final Logger log = LoggerFactory.getLogger(KQIWizardPage.class);

    private final String KPI_NAME_INPUT_ID = "kqiNameId";
    private final String VALUE_TYPE_ID = "kqiValueType-input";
    private final String UNIT_TYPE_ID = "kqiUnitId-input";
    private final String FORMULA_ID = "kqiFormulaId";

    public KQIWizardPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    public void fillName(String name) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(name, KPI_NAME_INPUT_ID);
        log.debug("Setting name with: {}", name);
    }

    public void fillValueType(String valueType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox kqiValueTypeInput = (Combobox) getWizard(driver, wait).getComponent(VALUE_TYPE_ID, Input.ComponentType.COMBOBOX);
        kqiValueTypeInput.setValue(Data.createSingleData(valueType));
        log.debug("Setting value type to: {}", valueType);
    }

    public void fillUnitType(String unitType) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox kqiUnitTypeInput = (Combobox) getWizard(driver, wait).getComponent(UNIT_TYPE_ID, Input.ComponentType.COMBOBOX);
        kqiUnitTypeInput.setValue(Data.createSingleData(unitType));
        log.debug("Setting unit type to: {}", unitType);
    }

    public void fillFormula(String formula) {
        DelayUtils.waitForPageToLoad(driver, wait);
        TextArea input = (TextArea) getWizard(driver, wait).getComponent(FORMULA_ID, Input.ComponentType.TEXT_AREA);
        input.setValue(Data.createSingleData(formula));
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
        getWizard(driver, wait).clickAccept();
        log.info("Finishing Step Wizard by clicking 'Accept'");
    }
}