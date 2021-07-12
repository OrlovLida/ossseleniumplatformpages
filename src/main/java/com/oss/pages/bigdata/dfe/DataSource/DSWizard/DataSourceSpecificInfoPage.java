package com.oss.pages.bigdata.dfe.DataSource.DSWizard;

import com.oss.framework.widgets.Wizard;
import com.oss.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oss.framework.components.inputs.Input.ComponentType.COMBOBOX;
import static com.oss.framework.components.inputs.Input.ComponentType.TEXT_FIELD;

public class DataSourceSpecificInfoPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(DataSourceSpecificInfoPage.class);
    private final String OFFSET_INPUT_ID = "dataSourceOffsetId-input";
    private final String INTERVAL_UNIT_INPUT_ID = "dataSourceIntervalUnitId-input";
    private final String INTERVAL_AMOUNT_INPUT_ID = "dataSourceIntervalAmountId";
    private final String WIZARD_ID = "dataSourcesWizardId";

    private final Wizard specificInfoWizard;

    public DataSourceSpecificInfoPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        specificInfoWizard = Wizard.createByComponentId(driver, wait, WIZARD_ID);
    }

    private void fillOffset(String offset) {
        specificInfoWizard.setComponentValue(OFFSET_INPUT_ID, offset, COMBOBOX);
        log.debug("Setting offset with: {}", offset);
    }

    private void fillIntervalUnit(String unit) {
        specificInfoWizard.setComponentValue(INTERVAL_UNIT_INPUT_ID, unit, COMBOBOX);
        log.debug("Setting interval unit with: {}", unit);
    }

    private void fillIntervalAmount(String intervalAmount) {
        specificInfoWizard.setComponentValue(INTERVAL_AMOUNT_INPUT_ID, intervalAmount, TEXT_FIELD);
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
