package com.oss.pages.bigdata.dfe.DataSource.DSWizard;

import com.oss.framework.components.inputs.Combobox;
import com.oss.framework.components.inputs.Input;
import com.oss.framework.data.Data;
import com.oss.framework.utils.DelayUtils;
import com.oss.pages.bigdata.dfe.stepwizard.commons.BaseStepPage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceSpecificInfoPage extends BaseStepPage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceSpecificInfoPage.class);
    private final String OFSET_INPUT_ID = "dataSourceOffsetId-input";
    private final String INTERVAL_UNIT_INPUT_ID = "dataSourceIntervalUnitId-input";
    private final String INTERVAL_AMOUNT_INPUT_ID = "dataSourceIntervalAmountId";

    public DataSourceSpecificInfoPage(WebDriver driver, WebDriverWait wait, String wizardId) {
        super(driver, wait, wizardId);
    }

    public void fillOffset(String offset) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox offsetInput = (Combobox) getWizard(driver, wait).getComponent(OFSET_INPUT_ID, Input.ComponentType.COMBOBOX);
        offsetInput.setValue(Data.createSingleData(offset));
        log.debug("Setting offset with: {}", offset);
    }

    public void fillIntervalUnit(String unit) {
        DelayUtils.waitForPageToLoad(driver, wait);
        Combobox intervalUnitInput = (Combobox) getWizard(driver, wait).getComponent(INTERVAL_UNIT_INPUT_ID, Input.ComponentType.COMBOBOX);
        intervalUnitInput.setValue(Data.createSingleData(unit));
        log.debug("Setting interval unit with: {}", unit);
    }

    public void fillIntervalAmount(String intervalAmount) {
        DelayUtils.waitForPageToLoad(driver, wait);
        fillTextField(intervalAmount, INTERVAL_AMOUNT_INPUT_ID);
        log.debug("Setting interval amount with: {}", intervalAmount);
    }

    @Step("I fill Specific Information with offset: {offset}, Interval Unit: {unit}, Interval Amount: {intervalAmount}")
    public void fillSpecificInfo(String offset, String unit, String intervalAmount) {
        fillOffset(offset);
        fillIntervalUnit(unit);
        fillIntervalAmount(intervalAmount);
        log.info("Filled Specific Information Wizard Page");
    }

}
